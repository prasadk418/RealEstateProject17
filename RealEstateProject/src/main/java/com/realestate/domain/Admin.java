package com.realestate.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/*
public class Admin implements Serializable{

	
	private static final long serialVersionUID = 123456L;

	@Id
	@Column(name="name")
	private String name;
	@Column(name="pwd")
	private String password;
	
	@Column(name="ans")
	private String answer;
	@Column(name="changepwd")
	private Boolean changepwd;
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="sqid" , referencedColumnName="sqid")
	private SecurityQuestions sqid;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
		
	public SecurityQuestions getSqid() {
		return sqid;
	}
	public void setSqid(SecurityQuestions sqid) {
		this.sqid = sqid;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public Boolean getChangepwd() {
		return changepwd;
	}
	public void setChangepwd(Boolean changepwd) {
		this.changepwd = changepwd;
	}

	
}*/