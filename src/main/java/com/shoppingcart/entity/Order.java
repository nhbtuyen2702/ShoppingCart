package com.shoppingcart.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Orders")
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", length = 50)
	private String id;

	@Column(name = "Order_Date", nullable = false)
	@Temporal(TemporalType.DATE)//ngày tháng năm
	private Date orderDate;
	
	@Column(name = "Order_Date1")
	@Temporal(TemporalType.TIMESTAMP)//ngày tháng năm giờ phút giây
	private Date orderDate1;

	@Column(name = "Order_Num", nullable = false)
	private int orderNum;

	@Column(name = "Amount", nullable = false)
	private double amount;

	@Column(name = "Customer_Name", length = 255, nullable = false)
	private String customerName;

	@Column(name = "Customer_Address", length = 255, nullable = false)
	private String customerAddress;

	@Column(name = "Customer_Email", length = 128, nullable = false)
	private String customerEmail;

	@Column(name = "Customer_Phone", length = 128, nullable = false)
	private String customerPhone;
	
	//@OneToMany(fetch = FetchType.EAGER, mappedBy = "order", cascade={CascadeType.PERSIST})
	//private Set<OrderDetail> orderDetails;
	
	//public Set<OrderDetail> getOrderDetails() {
		//return orderDetails;
	//}

	//public void setOrderDetails(Set<OrderDetail> orderDetails) {
		//this.orderDetails = orderDetails;
	//}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	
	public Date getOrderDate1() {
		return orderDate1;
	}

	public void setOrderDate1(Date orderDate1) {
		this.orderDate1 = orderDate1;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

}
