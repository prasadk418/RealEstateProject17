package com.realestate.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

/**
 * 
 */
@Entity
@Table(name="secret_questions")
public class SecurityQuestions implements Serializable {

	private static final long serialVersionUID = 123456L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sqid")
	private Integer sqId;

	@Column(name = "question")
	@JsonProperty(access=Access.READ_WRITE)
	private String question;

		
	public Integer getSqId() {
		return sqId;
	}

	public void setSqId(Integer sqId) {
		this.sqId = sqId;
	}
	@JsonIgnore
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	
}
