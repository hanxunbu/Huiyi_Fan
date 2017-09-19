package com.huiyi.huiyifan.modle;


/**
 * product 产品信息表
 * */

public class Product {
	
	private Integer ID;
	private String sName;
	private String standard;
	private String pcode;
	
	public Product(String sName, String standard, String pcode) {
		super();
		this.sName = sName;
		this.standard = standard;
		this.pcode = pcode;
	}
	
	public Product(Integer iD, String sName, String standard, String pcode) {
		super();
		ID = iD;
		this.sName = sName;
		this.standard = standard;
		this.pcode = pcode;
	}

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public String getsName() {
		return sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getPcode() {
		return pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	

	
	
	



	

}
