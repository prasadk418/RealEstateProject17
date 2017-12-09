/*package com.realestate.domain;


import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="materialtemp")
public class MaterialTemp implements Serializable {

	private static final long serialVersionUID = 123456L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "material")
	private String material;
	
	@OneToMany(cascade=CascadeType.ALL , fetch=FetchType.EAGER)
	@JoinColumn(name="tempmaterial_id", referencedColumnName="id")
	private List<UOMTemp> uom;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	
	public List<UOMTemp> getUom() {
		return uom;
	}

	public void setUom(List<UOMTemp> uom) {
		this.uom = uom;
	}

	
	/*

	@Override
	public int hashCode() {
		int hash=((17*name.hashCode())+id);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		
		if(obj==this)
			return true;
		
		if(obj==null)
			return false;
		
		if(!(obj instanceof MaterialTemp))
			return false;
		
		final MaterialTemp material = (MaterialTemp) obj;		
		return material.name.equals(name) && material.id==id;
	}
	
	
	
}*/
