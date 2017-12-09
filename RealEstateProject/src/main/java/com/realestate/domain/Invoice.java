package com.realestate.domain;

import java.io.Serializable;
import java.util.Date;

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

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="invoice")
public class Invoice implements Serializable {
	
	private static final long serialVersionUID = 123456L;

	@Id
	@Column(name="invoice_id")
	private String invoiceId;
	
	@Column(name="invoice_date")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd" , timezone = "IST")
	@Temporal(TemporalType.DATE)
	private Date invoiceDate;
	
	@OneToOne(cascade=CascadeType.ALL , fetch=FetchType.EAGER)
	@JoinColumn(name="order_id" , referencedColumnName="order_id" ,unique=true)
	private Order orderId;
	
	
	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Order getOrderId() {
		return orderId;
	}

	public void setOrderId(Order orderId) {
		this.orderId = orderId;
	}

	
}
