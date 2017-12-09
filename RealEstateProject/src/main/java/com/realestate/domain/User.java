package com.realestate.domain;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name="users")
public class User implements Serializable{

	private static final long serialVersionUID = 123456L;
	
	
	@Id	
	@Column(name="user_id")	
	private String user_id;
	
	@Column(name="user_name" , unique=true)
	private String user_name;
	
	@Column(name="password")
	@JsonProperty(access=Access.WRITE_ONLY)
	private String password;
		
	@Column(name="doj")	
	@JsonProperty(access=Access.WRITE_ONLY)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "IST")
	@Temporal(TemporalType.DATE)
	private Date doj;
	
	@Column(name="full_name")
	private String full_name;
	
	@OneToOne(cascade=CascadeType.MERGE, fetch=FetchType.EAGER)
	@JoinColumn(name="status" , referencedColumnName="name")
	private Status status;
	
	@Column(name="comments")
	@JsonProperty(access = Access.WRITE_ONLY)	
	private String comments;
	
	@Column(name="user_type")
	private String user_type;
	
	@OneToOne(cascade=CascadeType.MERGE, fetch=FetchType.EAGER)
	@JoinColumn(name="sqid" , referencedColumnName="sqid")
	private SecurityQuestions sqId;

	@Column(name="sqpwd")
	@JsonProperty(access=Access.WRITE_ONLY)
	private String sqPwd;
	
	/*@ManyToMany(cascade=CascadeType.ALL , fetch=FetchType.EAGER)
	@JoinTable(name="USER_MATERIAL" , joinColumns={	@JoinColumn(name="user_id" , referencedColumnName="user_id")},
	inverseJoinColumns={@JoinColumn(name="material_id", referencedColumnName="id")})
*/		
	/*	
	@OneToMany(cascade=CascadeType.ALL , fetch=FetchType.EAGER)
	@JoinColumn(name="user_id", referencedColumnName="user_id")
	private List<MaterialTemp> tempMaterial;
	*/
	@Transient
	private List<String> material;
	
	

	@Column(name="vendor_materials")
	private String vendor_materials;
	
	/*@Column(name="sqpwd")
	private Material material;*/


	
	
	public String getVendor_materials() {
		return vendor_materials;
	}

	public List<String> getMaterial() {
		return material;
	}

	public void setMaterial(List<String> material) {
		this.material = material;
	}

	public void setVendor_materials(String vendor_materials) {
		this.vendor_materials = vendor_materials;
	}
	
	
	@JsonIgnore
	public String getSqPwd() {
		return sqPwd;
	}

	public void setSqPwd(String sqPwd) {
		this.sqPwd = sqPwd;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	@JsonIgnore
	public Date getDoj() {
		return doj;
	}

	public void setDoj(Date doj) {
		this.doj = doj;
	}

	public String getFull_name() {
		return full_name;
	}

	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}

	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public SecurityQuestions getSqId() {
		return sqId;
	}

	public void setSqId(SecurityQuestions sqId) {
		this.sqId = sqId;
	}

	
	
	
	}
