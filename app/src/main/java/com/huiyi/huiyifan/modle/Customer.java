package com.huiyi.huiyifan.modle;

/***
 *customer 客户信息表
 */
public class Customer {
	
	private Integer ID;
	private String name;
	
	public Customer(String name) {
		super();
		this.name = name;
	}
	
	public Customer(Integer iD, String name) {
		super();
		ID = iD;
		this.name = name;
	}

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	

}
