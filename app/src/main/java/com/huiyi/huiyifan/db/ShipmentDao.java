package com.huiyi.huiyifan.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.huiyi.huiyifan.util.DBHelper;


public class ShipmentDao {

	private DBHelper dbhelper;
	private SQLiteDatabase db;

	public ShipmentDao(Context context) {
		this.dbhelper = new DBHelper(context);
	}

	//获取数据库对象
	private void init(){
		db = dbhelper.getWritableDatabase();//获取一个可读可写的数据库
	}

	/**
	 * 添加（本地添加）
	 */
	public void AddShipment(String code,String xorderID,String sorderID,String customerID,String quantity,String datetime,String status,String producerID) {
		try {
			//取得数据库操作实例
			init();
			String sql = "insert into t_shipment(code,xorderID,sorderID,customerID,quantity,datetime,status,producerID) values('"+code+"','"+xorderID+"','"+sorderID+"','"+customerID+"','"+quantity+"','"+datetime+"','"+status+"','"+producerID+"')";
			db.execSQL(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加（本地添加）
	 */
	public void UpdateShipment(String code,String xorderID,String sorderID,String customerID,String quantity,String datetime,String status,String producerID) {
		try {
			//取得数据库操作实例
			init();
			String sql = " update t_storage set xorderID='"+xorderID+"',sorderID='"+sorderID+"',customerID='"+customerID+"',quantity='"+quantity+"',datetime='"+datetime+"',status='"+status+"',producerID='"+producerID+"' where code='"+code+"'  ";
			db.execSQL(sql);

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	/**
	 * 删除(上传后删除记录)
	 */
	public void DeleteShipment(){
		init();
		String sql = " delete from t_shipment ";
		db.execSQL(sql);

	}

	/**
	 * 删除(导出后删除记录)一个订单一个产品
	 */
	public void DeleteShipment2(String xorderID){
		init();
		String sql = " delete from t_shipment where xorderID = "+xorderID+" ";
		db.execSQL(sql);

	}

	/**
	 * 删除(导出后删除记录)一个订单多个产品
	 */
	public void DeleteShipment3(String nuber){
		init();
		String sql = " delete from t_shipment where number = '"+nuber+"' ";
		db.execSQL(sql);
		db.close();
	}

	/**
	 * 根据码删除
	 */
	public void DeleteShipment_ma(String ma){
		init();
		String sql = " delete from t_shipment where code = '"+ma+"' ";
		db.execSQL(sql);

	}

}
