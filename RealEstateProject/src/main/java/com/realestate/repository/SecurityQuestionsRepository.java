package com.realestate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.realestate.domain.SecurityQuestions;

@Repository
public interface SecurityQuestionsRepository extends JpaRepository<SecurityQuestions, Integer>{

	@Query("select sq.question from SecurityQuestions sq where sq.question=:ques")
	public String findByQuestion(@Param("ques")String ques);
}
