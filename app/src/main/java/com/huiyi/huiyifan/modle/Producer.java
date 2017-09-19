package com.huiyi.huiyifan.modle;


/**
 * producer 生产商账号信息表
 * */
public class Producer {
	
	private Integer ID;
	private String name;
	private String password;
	
	public Producer(String name,String password) {
		super();
		this.name = name;
		this.password = password;
	}
	
	public Producer(Integer iD, String name, String password) {
		super();
		ID = iD;
		this.name = name;
		this.password = password;
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
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	

}
