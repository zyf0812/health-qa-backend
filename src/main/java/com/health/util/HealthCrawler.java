package com.health.util;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class HealthCrawler {

    // 常量配置（集中管理，便于维护）
    private static final String BASE_JIB_URL = "https://jib.xywy.com/";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/141.0.0.0 Safari/537.36";
    private static final int TIMEOUT = 15000; // 超时时间延长到15秒，应对网络波动
    private static final String REFERER = "https://www.google.com/";
    private static final String TARGET_TAG_NAME = "简介"; // 仅该标签提取第一个P标签

    public List<Map<String, String>> crawlDxyColdData() {
        List<Map<String, String>> dataList = new ArrayList<>();
        log.info("开始爬取寻医问药网疾病数据...");

        try {
            // 1. 第一步：获取问题列表页的问题和链接
            List<Map<String, String>> questionUrlList = getQuestionUrlList("https://club.xywy.com/juhe");
            log.info("成功获取问题列表，共 {} 个问题", questionUrlList.size());

            if (questionUrlList.isEmpty()) {
                log.warn("未获取到任何问题链接，爬虫终止");
                return dataList;
            }

            // 2. 第二步：通过问题页获取疾病详情页链接（secondUrl）
            List<String> diseaseDetailUrlList = getDiseaseDetailUrlList(questionUrlList);
            log.info("成功获取疾病详情页链接，共 {} 个", diseaseDetailUrlList.size());

            if (diseaseDetailUrlList.isEmpty()) {
                log.warn("未获取到任何疾病详情页链接，爬虫终止");
                return dataList;
            }

            // 3. 第三步：获取疾病介绍页链接（thirdUrl）
            List<String> diseaseIntroUrlList = getDiseaseIntroUrlList(diseaseDetailUrlList);
            log.info("成功获取疾病介绍页链接，共 {} 个", diseaseIntroUrlList.size());

            if (diseaseIntroUrlList.isEmpty()) {
                log.warn("未获取到任何疾病介绍页链接，爬虫终止");
                return dataList;
            }

            // 4. 第四步：解析疾病介绍页，提取疾病名和各标签文本（按规则提取）
            dataList = parseDiseaseIntroPages(diseaseIntroUrlList);
            log.info("爬虫结束，共获取 {} 条疾病完整数据", dataList.size());

        } catch (Exception e) {
            log.error("爬虫整体执行异常", e);
        }

        return dataList;
    }

    private List<Map<String, String>> getQuestionUrlList(String questionListUrl) throws IOException {
        List<Map<String, String>> questionUrlList = new ArrayList<>();

        Document doc = createJsoupConnection(questionListUrl).get();
        Elements questionElements = doc.select("div.juhe_Pincon a"); // 简化选择器，直接定位a标签

        int questionCount = 0;
        for (Element element : questionElements) {
            String questionText = element.text().trim();
            String href = element.attr("href").trim();

            if (!questionText.isEmpty() && !href.isEmpty()) {
                Map<String, String> map = new HashMap<>();
                map.put("question", questionText);
                map.put("href", href);
                questionUrlList.add(map);
            }
            questionCount++;
            if (questionCount >= 10) {
                break;
            }
        }

        return questionUrlList;
    }

    private List<String> getDiseaseDetailUrlList(List<Map<String, String>> questionUrlList) {
        List<String> diseaseDetailUrlList = new ArrayList<>();

        for (Map<String, String> questionMap : questionUrlList) {
            String questionHref = questionMap.get("href");
            if (questionHref == null || questionHref.isEmpty()) {
                log.warn("问题链接为空，跳过");
                continue;
            }

            try {
                // 拼接完整URL（处理相对路径）
                String fullQuestionUrl = questionHref.startsWith("https:") ? questionHref : "https:" + questionHref;
                Document doc = createJsoupConnection(fullQuestionUrl).get();

                // 定位"查看详情>>"链接（优化选择器，更精准）
                Element detailLink = doc.selectFirst("a.pa:containsOwn(查看详情>>)");
                if (detailLink != null) {
                    String detailHref = detailLink.attr("href").trim();
                    if (!detailHref.isEmpty()) {
                        diseaseDetailUrlList.add(detailHref);
                    }
                } else {
                    log.debug("问题页 {} 未找到'查看详情>>'链接，跳过", fullQuestionUrl);
                }

            } catch (Exception e) {
                log.error("解析问题页 {} 异常", questionMap.get("href"), e);
                continue;
            }
        }

        return diseaseDetailUrlList;
    }

    private List<String> getDiseaseIntroUrlList(List<String> diseaseDetailUrlList) {
        List<String> diseaseIntroUrlList = new ArrayList<>();

        for (String detailUrl : diseaseDetailUrlList) {
            try {
                Document doc = createJsoupConnection(detailUrl).get();
                Element introLink = doc.selectFirst("a:containsOwn(疾病介绍)"); // 用containsOwn避免包含子元素文本

                if (introLink != null) {
                    String introHref = introLink.attr("href").trim();
                    if (!introHref.isEmpty()) {
                        diseaseIntroUrlList.add(introHref);
                    }
                } else {
                    log.debug("疾病详情页 {} 未找到'疾病介绍'链接，跳过", detailUrl);
                }

            } catch (Exception e) {
                log.error("解析疾病详情页 {} 异常", detailUrl, e);
                continue;
            }
        }

        return diseaseIntroUrlList;
    }

    private List<Map<String, String>> parseDiseaseIntroPages(List<String> diseaseIntroUrlList) {
        List<Map<String, String>> dataList = new ArrayList<>();

        for (String introUrl : diseaseIntroUrlList) {
            // 拼接完整的疾病介绍页URL（处理相对路径）
            String fullIntroUrl = introUrl.startsWith(BASE_JIB_URL) ? introUrl : BASE_JIB_URL + introUrl;
            Map<String, String> diseaseMap = new HashMap<>();

            try {
                // 加载疾病介绍页
                Document doc3 = createJsoupConnection(fullIntroUrl).get();

                // 1. 提取疾病名（优化选择器，增加容错）
                String diseaseName = "未知疾病";
                Element diseaseNameElem = doc3.selectFirst("div.jb-name.fYaHei.gre");
                if (diseaseNameElem != null) {
                    diseaseName = diseaseNameElem.text().trim();
                }
                diseaseMap.put("疾病名", diseaseName);
                log.debug("开始解析疾病：{}，URL：{}", diseaseName, fullIntroUrl);

                // 2. 定位导航容器，获取所有标签链接
                Element navContainer = doc3.selectFirst("div.jib-nav");
                if (navContainer == null) {
                    log.warn("疾病 {} 未找到导航容器，跳过", diseaseName);
                    dataList.add(diseaseMap);
                    continue;
                }

                Elements tagLinks = navContainer.select("ul.jib-nav-list li a");
                if (tagLinks.isEmpty()) {
                    log.warn("疾病 {} 未找到标签链接，跳过", diseaseName);
                    dataList.add(diseaseMap);
                    continue;
                }

                // 3. 循环解析每个标签页文本（核心规则：简介=第一个P，其他=所有P）
                for (Element tagLink : tagLinks) {
                    String tagName = tagLink.text().trim();
                    String tagPageUrl = tagLink.absUrl("href"); // 自动获取完整URL

                    // 过滤无效标签
                    if (tagName.isEmpty() || tagPageUrl.isEmpty() || !tagPageUrl.startsWith(BASE_JIB_URL)) {
                        log.debug("疾病 {} 无效标签，标签名：{}，URL：{}，跳过", diseaseName, tagName, tagPageUrl);
                        continue;
                    }

                    // 按标签名选择提取逻辑
                    String tagContent;
                    if (TARGET_TAG_NAME.equals(tagName)) {
                        // 仅"简介"标签：提取第一个P标签文本
                        tagContent = parseFirstPTagContent(tagPageUrl);
                    } else {
                        // 其他标签：提取所有P标签文本
                        tagContent = parseAllPTagContent(tagPageUrl);
                    }

                    diseaseMap.put(tagName, tagContent);
                }

                dataList.add(diseaseMap);
                log.debug("成功解析疾病：{}，共获取 {} 个标签内容", diseaseName, diseaseMap.size() - 1);

            } catch (Exception e) {
                log.error("解析疾病介绍页 {} 异常", fullIntroUrl, e);
                diseaseMap.put("疾病名", "未知疾病（页面加载失败）");
                dataList.add(diseaseMap);
            }
        }

        return dataList;
    }

    private String parseFirstPTagContent(String tagPageUrl) {
        try {
            Document tagDoc = createJsoupConnection(tagPageUrl)
                    .timeout(TIMEOUT)
                    .ignoreHttpErrors(true)
                    .get();

            // 优先获取疾病内容区域的第一个P标签（最相关的核心内容）
            Element firstP = tagDoc.selectFirst("div.jib-articl-con p");
            if (firstP == null) {
                // 兜底：如果内容区域没有P标签，取页面第一个P标签
                firstP = tagDoc.selectFirst("p");
            }

            if (firstP != null) {
                String pText = firstP.text().trim();
                // 过滤广告和空文本
                if (!pText.isEmpty() && !pText.contains("广告") && !pText.contains("推广")) {
                    return pText;
                }
            }

            // 无有效P标签时返回提示
            return "无相关文本内容";

        } catch (Exception e) {
            log.error("解析标签页 {} 的第一个P标签异常", tagPageUrl, e);
            return "标签内容提取失败";
        }
    }

    private String parseAllPTagContent(String tagPageUrl) {
        try {
            Document tagDoc = createJsoupConnection(tagPageUrl)
                    .timeout(TIMEOUT)
                    .ignoreHttpErrors(true)
                    .get();

            // 优化选择器：只提取疾病内容区域的<p>（避免广告等无关文本）
            Elements contentPTags = tagDoc.select("div.jib-articl-con p");
            if (contentPTags.isEmpty()) {
                contentPTags = tagDoc.select("p"); // 兜底：如果没找到内容区域，取所有<p>
            }

            StringBuilder tagContent = new StringBuilder();
            for (Element p : contentPTags) {
                String pText = p.text().trim();
                // 过滤掉广告、空文本等冗余内容
                if (!pText.isEmpty() && !pText.contains("广告") && !pText.contains("推广")) {
                    tagContent.append(pText).append("\n");
                }
            }

            String finalContent = tagContent.toString().trim();
            return finalContent.isEmpty() ? "无相关文本内容" : finalContent;

        } catch (Exception e) {
            log.error("解析标签页 {} 的所有P标签异常", tagPageUrl, e);
            return "标签内容提取失败";
        }
    }

    private org.jsoup.Connection createJsoupConnection(String url) {
        Objects.requireNonNull(url, "请求URL不能为空");
        return Jsoup.connect(url)
                .userAgent(USER_AGENT)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                .header("Connection", "keep-alive")
                .referrer(REFERER)
                .ignoreHttpErrors(true)
                .timeout(TIMEOUT);
    }
}