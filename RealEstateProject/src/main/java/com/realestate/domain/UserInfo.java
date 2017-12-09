package com.realestate.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.sun.scenario.effect.Effect.AccelType;

@Entity
@Table(name="userinfo")
public class UserInfo implements Serializable{

	private static final long serialVersionUID = 123456L;
	
	
	@Id	
	@Column(name="business_email")
	@JsonProperty
	private String business_email;
	
	@Column(name="firm_name" , unique=true)
	private String firm_name;
	
	@OneToOne(cascade=CascadeType.ALL , fetch=FetchType.EAGER)
	@JoinColumn(name="user_id", referencedColumnName="user_id" , unique=true)
	@JsonProperty
	private User user_id;
	
	@Column(name="proprietor_name")
	@JsonProperty
	private String proprietor_name;
	
	@Column(name="comp_reg_no")
	@JsonProperty
	private String comp_reg_no;
	
	@Column(name="comp_pan_no")
	@JsonProperty
	private String comp_pan_no;
	
	@Column(name="gst_no")
	@JsonProperty
	private String gst_no;
	
	@Column(name="address")
	@JsonProperty
	private String address;
	
	@Column(name="pincode")
	@JsonProperty
	private String pincode;
	
	@Column(name="landline_no")
	@JsonProperty
	private String landline_no;
	
	@Column(name="mobile")
	private String mobile;
	
	@Column(name="fax")
	@JsonProperty
	private String fax;
	
	
	
	@Column(name="secondary_email")
	@JsonProperty
	private String secondary_email;
	
	public String getFirm_name() {
		return firm_name;
	}
	public void setFirm_name(String firm_name) {
		this.firm_name = firm_name;
	}
	
	
	public String getProprietor_name() {
		return proprietor_name;
	}
	public void setProprietor_name(String proprietor_name) {
		this.proprietor_name = proprietor_name;
	}
	
	public String getComp_reg_no() {
		return comp_reg_no;
	}
	public void setComp_reg_no(String comp_reg_no) {
		this.comp_reg_no = comp_reg_no;
	}
	
	public String getComp_pan_no() {
		return comp_pan_no;
	}
	public void setComp_pan_no(String comp_pan_no) {
		this.comp_pan_no = comp_pan_no;
	}
	
	public String getGst_no() {
		return gst_no;
	}
	public void setGst_no(String gst_no) {
		this.gst_no = gst_no;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	
	public String getLandline_no() {
		return landline_no;
	}
	public void setLandline_no(String landline_no) {
		this.landline_no = landline_no;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	
	public String getBusiness_email() {
		return business_email;
	}
	public void setBusiness_email(String business_email) {
		this.business_email = business_email;
	}
	
	public String getSecondary_email() {
		return secondary_email;
	}
	public void setSecondary_email(String secondary_email) {
		this.secondary_email = secondary_email;
	}
	
	public User getUser_id() {
		return user_id;
	}
	public void setUser_id(User user_id) {
		this.user_id = user_id;
	}
	
	
		
}
