package com.huiyi.huiyifan.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.huiyi.huiyifan.util.DBHelper;


public class RefundDao {

	private DBHelper dbhelper;
	private SQLiteDatabase db;

	public RefundDao(Context context) {
		this.dbhelper = new DBHelper(context);
	}

	//获取数据库对象
	private void init(){
		db = dbhelper.getWritableDatabase();//获取一个可读可写的数据库
	}

	/**
	 * 离线添加
	 * @param t_xscode
	 * @param t_datetime
	 * @param t_remarks
	 */
	public void AddRefund(String t_xscode,String t_datetime,String t_remarks) {
		try {
			//取得数据库操作实例
			init();
			String sql = "insert into t_refund(t_xscode,t_datetime,t_remarks) values('"+t_xscode+"','"+t_datetime+"','"+t_remarks+"')";
			db.execSQL(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据条码删除(本地删除)
	 * @param ma
	 */
	public void DeleteRefund_ma(String ma){
		init();
		String sql = " delete from t_refund where t_xscode = '"+ma+"' ";
		db.execSQL(sql);

	}

	/**
	 * 删除(上传后删除记录)
	 */
	public void DeleteRefund(){
		init();
		String sql = " delete from t_refund ";
		db.execSQL(sql);

	}

	/**
	 * 添加（本地添加）
	 */
	public void UpdateShipment(String code,String xorderID,String sorderID,String customerID,String quantity,String datetime,String status,String producerID)
	{
		try {
			//取得数据库操作实例
			init();
			String sql = " update t_storage set xorderID ='"+ xorderID +"',sorderID ='"+sorderID +"',customerID ='"+customerID +"',quantity='"+ quantity +"',datetime ='"+ datetime +"',status='"+ status +"',producerID ='"+ producerID +"' where code ='"+ code +"'  ";
			db.execSQL(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 删除(导出后删除记录)一个订单多个产品
	 */
	public void DeleteShipment3(String nuber){
		init();
		String sql = " delete from t_shipment where number = '"+ nuber +"' ";
		db.execSQL(sql);

	}



}
