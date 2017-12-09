package com.realestate.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAnySetter;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;

@Entity
@Table(name = "orders")
public class Order implements Serializable {

	
	private static final long serialVersionUID = 123456L;

	@Id
	@Column(name = "order_id")
	private String orderId;
	
	@Column(name = "order_date")
	//@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "IST")
	@Temporal(TemporalType.DATE)
	private Date orderDate;
	
	@OneToOne(cascade=CascadeType.ALL , fetch=FetchType.EAGER)
	@JoinColumn(name="material" , referencedColumnName="id")
	private Material material;
	
	@OneToOne(cascade=CascadeType.ALL , fetch=FetchType.EAGER)
	@JoinColumn(name="uom" , referencedColumnName="id")
	private UOM uom;
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@Column(name = "size")
	private String size;
	
	@OneToOne(cascade=CascadeType.ALL , fetch=FetchType.EAGER)
	@JoinColumn(name="business_email" , referencedColumnName="business_email")	
	private UserInfo firm_name;
	
	@OneToOne(cascade=CascadeType.MERGE, fetch=FetchType.EAGER)
	@JoinColumn(name="order_status" , referencedColumnName="name")
	private Status orderStatus;
	
	@Column(name = "amount")
	private Double amount;
	
	@Column(name = "delivery_date")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "IST")
	@Temporal(TemporalType.DATE)
	private Date deliveryDate;
	
	@Column(name = "delivery_quantity")
	private Integer deliveryQuantity;
	
	@Column(name = "order_confirmation_date")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "IST")
	@Temporal(TemporalType.DATE)
	private Date orderConfirmationDate;
	
	@Column(name = "unit_price")
	private Double unitPrice;
	
	@Column(name="comments")
	private String comments;
	
	@Column(name="vendor_firm")
	private String vendor_firm;
	
	@Column(name="vendor_mobile")
	private String vendor_mobile;
	
	
	public String getVendor_firm() {
		return vendor_firm;
	}

	public void setVendor_firm(String vendor_firm) {
		this.vendor_firm = vendor_firm;
	}

	public String getVendor_mobile() {
		return vendor_mobile;
	}

	public void setVendor_mobile(String vendor_mobile) {
		this.vendor_mobile = vendor_mobile;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Integer getDeliveryQuantity() {
		return deliveryQuantity;
	}

	public void setDeliveryQuantity(Integer deliveryQuantity) {
		this.deliveryQuantity = deliveryQuantity;
	}

	public Date getOrderConfirmationDate() {
		return orderConfirmationDate;
	}

	public void setOrderConfirmationDate(Date orderConfirmationDate) {
		this.orderConfirmationDate = orderConfirmationDate;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public UOM getUom() {
		return uom;
	}

	public void setUom(UOM uom) {
		this.uom = uom;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQualtity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public UserInfo getFirm_name() {
		return firm_name;
	}

	public void setFirm_name(UserInfo firm_name) {
		this.firm_name = firm_name;
	}

	public Status getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Status orderStatus) {
		this.orderStatus = orderStatus;
	}

	

		
}
