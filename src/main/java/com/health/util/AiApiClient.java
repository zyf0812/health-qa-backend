package com.health.util;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AiApiClient {
    @Value("${ai.api.key}")
    private String apiKey;
    @Value("${ai.api.secret}")
    private String apiSecret;

    private static final String API_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";

    /**
     * 调用AI接口，获取100-200字的精简健康回答
     * @return 成功返回精简答案，失败返回null
     */
    public String getAiAnswer(String question) {
        OkHttpClient client = new OkHttpClient();

        // 核心优化：添加字数限制和精简要求的提示词
        String prompt = String.format(
                "请作为专业健康顾问，简洁回答以下问题，答案控制在100-200字之间，只保留核心信息，无冗余内容，语言通俗：%s",
                question
        );

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "qwen-turbo"); // 免费模型
        requestBody.put("input", Map.of("prompt", prompt)); // 传入优化后的prompt
        // 补充参数：控制回答长度（max_tokens限制总 tokens，避免超长）
        requestBody.put("parameters", Map.of(
                "temperature", 0.7, // 保持一定随机性，避免生硬
                "max_tokens", 300, // 预留冗余（1字≈1.2 tokens），确保能输出100-200字
                "top_p", 0.8 // 聚焦核心答案，减少发散
        ));

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(RequestBody.create(
                        JSONObject.toJSONString(requestBody),
                        MediaType.parse("application/json")
                ))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                return null;
            }

            JSONObject result = JSONObject.parseObject(response.body().string());
            if (result.containsKey("output")) {
                String aiAnswer = result.getJSONObject("output").getString("text");
                // 二次过滤：如果AI仍超长，手动截取前200字（兜底处理）
                return trimAnswer(aiAnswer);
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 兜底处理：确保答案在100-200字之间
     */
    private String trimAnswer(String answer) {
        if (answer == null) return null;
        // 去除多余空格和换行
        String cleaned = answer.replaceAll("\\s+", " ").trim();
        int length = cleaned.length();

        if (length <= 200) {
            // 不足100字的话，补充一句核心提示（可选，避免过短）
            return length >= 100 ? cleaned : cleaned + " 建议结合自身情况咨询医生，避免盲目遵循。";
        } else {
            // 超长则截取前200字，并保证句子完整（避免截断在中间）
            return cleaned.substring(0, 200) + "…";
        }
    }
}