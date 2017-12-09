package com.realestate.domain;

import java.io.Serializable;

public class VendorDTO implements Serializable{

	private static final long serialVersionUID = 123456L;
	
	private String firm_name;
	private String mobile;
	private String[] materials;
	
	public String getFirm_name() {
		return firm_name;
	}
	public void setFirm_name(String firm_name) {
		this.firm_name = firm_name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String[] getMaterials() {
		return materials;
	}
	public void setMaterials(String[] materials) {
		this.materials = materials;
	}
	
	
	
}
