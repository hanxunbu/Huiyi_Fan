package com.huiyi.huiyifan.modle;


/**
 * refund 退货信息表
 * */

public class Refund {
	
	private Integer t_id;
	private String t_xscode;
	private String t_datetime;
	private String t_remarks;
	
	public Refund() {
		super();
	}

	public Refund(String t_xscode, String t_datetime, String t_remarks) {
		super();
		this.t_xscode = t_xscode;
		this.t_datetime = t_datetime;
		this.t_remarks = t_remarks;
	}

	public Integer getT_id() {
		return t_id;
	}

	public void setT_id(Integer t_id) {
		this.t_id = t_id;
	}

	public String getT_xscode() {
		return t_xscode;
	}

	public void setT_xscode(String t_xscode) {
		this.t_xscode = t_xscode;
	}

	public String getT_datetime() {
		return t_datetime;
	}

	public void setT_datetime(String t_datetime) {
		this.t_datetime = t_datetime;
	}

	public String getT_remarks() {
		return t_remarks;
	}

	public void setT_remarks(String t_remarks) {
		this.t_remarks = t_remarks;
	}

	
	
	
	

}
