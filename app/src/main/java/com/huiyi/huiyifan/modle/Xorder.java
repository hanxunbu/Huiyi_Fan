package com.huiyi.huiyifan.modle;


/**
 * xorder 销售订单表
 */

public class Xorder {
	
	private Integer ID;
	private String number;
	private Integer sorderID;
	private Integer customerID;
	private String xquantity;
	private String yquantity;
	private String datetime;
	private Integer producerID;
	
	public Xorder(String number,Integer sorderID,Integer customerID,String xquantity,String yquantity,String datetime,Integer producerID) {
		super();
		this.number = number;
		this.sorderID = sorderID;
		this.customerID = customerID;
		this.xquantity = xquantity;
		this.yquantity = yquantity;
		this.datetime = datetime;
		this.producerID = producerID;
	}
	
	public Xorder(Integer iD, String number,Integer sorderID,Integer customerID,String xquantity,String yquantity,String datetime,Integer producerID) {
		super();
		ID = iD;
		this.number = number;
		this.sorderID = sorderID;
		this.customerID = customerID;
		this.xquantity = xquantity;
		this.yquantity = yquantity;
		this.datetime = datetime;
		this.producerID = producerID;
	}

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	public Integer getSorderID() {
		return sorderID;
	}

	public void setSorderID(Integer sorderID) {
		this.sorderID = sorderID;
	}
	
	public Integer getCustomerID() {
		return customerID;
	}

	public void setCustomerID(Integer customerID) {
		this.customerID = customerID;
	}
	
	public String getXquantity() {
		return xquantity;
	}

	public void setXquantity(String xquantity) {
		this.xquantity = xquantity;
	}
	
	public String getYquantity() {
		return yquantity;
	}

	public void setYquantity(String yquantity) {
		this.yquantity = yquantity;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	
	public Integer getProducerID() {
		return producerID;
	}

	public void setProducerID(Integer producerID) {
		this.producerID = producerID;
	}
	

}
