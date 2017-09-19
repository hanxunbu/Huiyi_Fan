package com.huiyi.huiyifan.modle;

/**
 * about 参数配置表
 * */

public class About {
	
	private Integer ID;
	private String url;

	public About(String url) {
		super();
		this.url = url;
	}
	
	public About(Integer iD, String url) {
		super();
		ID = iD;
		this.url = url;
	}
	
	
	public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}


	
	
	

}
