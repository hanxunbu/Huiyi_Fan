package com.huiyi.huiyifan.modle;



/**
 * shipment 出货信息表
 * */

public class Shipment {
	
	private Integer ID;
	private String code;
	private Integer xorderID;
	private Integer sorderID;
	private Integer customerID;
	private String quantity;
	private String datetime;
	private Integer status;
	private Integer producerID;
	
	public Shipment(String code,Integer xorderID,Integer sorderID,Integer customerID,String quantity,String datetime,Integer status,Integer producerID) {
		super();
		this.code = code;
		this.xorderID = xorderID;
		this.sorderID = sorderID;
		this.customerID = customerID;
		this.quantity = quantity;
		this.datetime = datetime;
		this.status = status;
		this.producerID = producerID;
	}
	
	public Shipment(Integer iD, String code,Integer xorderID,Integer sorderID,Integer customerID,String quantity,String datetime,Integer status,Integer producerID) {
		super();
		ID = iD;
		this.code = code;
		this.xorderID = xorderID;
		this.sorderID = sorderID;
		this.customerID = customerID;
		this.quantity = quantity;
		this.datetime = datetime;
		this.status = status;
		this.producerID = producerID;
	}

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public Integer getXorderID() {
		return xorderID;
	}

	public void setXorderID(Integer xorderID) {
		this.xorderID = xorderID;
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
	
	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Integer getProducerID() {
		return producerID;
	}

	public void setProducerID(Integer producerID) {
		this.producerID = producerID;
	}

	

}
