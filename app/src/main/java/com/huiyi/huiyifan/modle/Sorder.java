package com.huiyi.huiyifan.modle;


/***
 *sorder 生产订单表
 */

public class Sorder {
	
	private Integer ID;
	private String number;
	private String pihao;
	private Integer productID;
	private String startTime;
	private String endTime;
	private String quantity;
	private Integer status;
	private Integer packing;
	private String da;
	private String zhong;
	private String xiao;
	private String sUrl;
	private Integer producerID;
	
	public Sorder(String number,String pihao,Integer productID,String startTime,String endTime,String quantity,Integer status,Integer packing,String da,String zhong,String xiao,String sUrl,Integer producerID) {
		super();
		this.number = number;
		this.pihao = pihao;
		this.productID = productID;
		this.startTime = startTime;
		this.endTime = endTime;
		this.quantity = quantity;
		this.status = status;
		this.packing = packing;
		this.da = da;
		this.zhong = zhong;
		this.xiao = xiao;
		this.sUrl = sUrl;
		this.producerID = producerID;
	}
	
	public Sorder(Integer iD, String number,String pihao,Integer productID,String startTime,String endTime,String quantity,Integer status,Integer packing,String da,String zhong,String xiao,String sUrl,Integer producerID) {
		super();
		ID = iD;
		this.number = number;
		this.pihao = pihao;
		this.productID = productID;
		this.startTime = startTime;
		this.endTime = endTime;
		this.quantity = quantity;
		this.status = status;
		this.packing = packing;
		this.da = da;
		this.zhong = zhong;
		this.xiao = xiao;
		this.sUrl = sUrl;
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
	
	public String getPihao() {
		return pihao;
	}

	public void setPihao(String pihao) {
		this.pihao = pihao;
	}
	
	public Integer getProductID() {
		return productID;
	}

	public void setProductID(Integer productID) {
		this.productID = productID;
	}
	
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
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

	public void setStatus(Integer number) {
		this.status = status;
	}
	
	public Integer getPacking() {
		return packing;
	}

	public void setPacking(Integer number) {
		this.packing = packing;
	}
	
	public String getDa() {
		return da;
	}

	public void setDa(String da) {
		this.da = da;
	}
	
	public String getZhong() {
		return zhong;
	}

	public void setZhong(String zhong) {
		this.zhong = zhong;
	}
	
	public String getXiao() {
		return xiao;
	}

	public void setXiao(String xiao) {
		this.xiao = xiao;
	}
	
	public String getSUrl() {
		return sUrl;
	}

	public void setSUrl(String sUrl) {
		this.sUrl = sUrl;
	}
	
	public Integer getproducerID() {
		return producerID;
	}

	public void setproducerID(Integer producerID) {
		this.producerID = producerID;
	}
	

	

}
