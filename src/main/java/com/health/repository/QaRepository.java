package com.health.repository;
import com.health.entity.QaPair;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

// 必须是接口 + 继承 CrudRepository + 正确的 SQL
public interface QaRepository extends CrudRepository<QaPair, Integer> {
    @Query("SELECT answer FROM qa_pair WHERE question LIKE CONCAT('%', :question, '%')")
    String findAnswerByQuestion(@Param("question") String question);
}