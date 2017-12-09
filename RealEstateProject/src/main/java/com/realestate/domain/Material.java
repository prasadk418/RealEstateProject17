package com.realestate.domain;


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
@Table(name="material")
public class Material implements Serializable {

	private static final long serialVersionUID = 123456L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name")
	private String name;

	@OneToMany(cascade=CascadeType.ALL , fetch=FetchType.EAGER)
	@JoinColumn(name="material_id", referencedColumnName="id")
	private List<UOM> uom;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<UOM> getUom() {
		return uom;
	}

	public void setUom(List<UOM> uom) {
		this.uom = uom;
	}

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
		
		if(!(obj instanceof Material))
			return false;
		
		final Material material = (Material) obj;		
		return material.name.equals(name) && material.id==id;
	}
	
	
	
}
