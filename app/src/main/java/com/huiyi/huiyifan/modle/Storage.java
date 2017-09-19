package com.huiyi.huiyifan.modle;

public class Storage {
	
	private Integer ID;
	private String xcode;
	private String zcode;
	private String dcode;
	private String sicode;
	private String wucode;
	private Integer sorderID;
	private String quantity;
	private Integer status;
	private String dateTime;
	private Integer producerID;
	
	public Storage(String xcode,String zcode,String dcode,String sicode,String wucode,Integer sorderID,String quantity,Integer status,String dateTime,Integer producerID) {
		super();
		this.xcode = xcode;
		this.zcode = zcode;
		this.dcode = dcode;
		this.sicode = sicode;
		this.wucode = wucode;
		this.sorderID = sorderID;
		this.quantity = quantity;
		this.status = status;
		this.dateTime = dateTime;
		this.producerID = producerID;
	}
	
	public Storage(Integer iD,String xcode,String zcode,String dcode,String sicode,String wucode,Integer sorderID,String quantity,Integer status,String dateTime,Integer producerID) {
		super();
		ID = iD;
		this.xcode = xcode;
		this.zcode = zcode;
		this.dcode = dcode;
		this.sicode = sicode;
		this.wucode = wucode;
		this.sorderID = sorderID;
		this.quantity = quantity;
		this.status = status;
		this.dateTime = dateTime;
		this.producerID = producerID;
	}

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public String getXcode() {
		return xcode;
	}

	public void setXcode(String xcode) {
		this.xcode = xcode;
	}
	
	public String getZcode() {
		return zcode;
	}

	public void setZcode(String zcode) {
		this.zcode = zcode;
	}
	
	public String getDcode() {
		return dcode;
	}

	public void setDcode(String dcode) {
		this.dcode = dcode;
	}
	
	public String getSicode() {
		return sicode;
	}

	public void setSicode(String sicode) {
		this.sicode = sicode;
	}
	
	public String getWucode() {
		return wucode;
	}

	public void setWucode(String wucode) {
		this.wucode = wucode;
	}
	
	public Integer getSorderID() {
		return sorderID;
	}

	public void setSorderID(Integer sorderID) {
		this.sorderID = sorderID;
	}
	
	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	public Integer getProducerID() {
		return producerID;
	}

	public void setProducerID(Integer producerID) {
		this.producerID = producerID;
	}
	

}
