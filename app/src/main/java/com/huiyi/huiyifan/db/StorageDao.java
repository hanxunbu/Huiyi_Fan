package com.huiyi.huiyifan.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.huiyi.huiyifan.util.DBHelper;


public class StorageDao {

	private DBHelper dbhelper;
	private SQLiteDatabase db;


	public StorageDao(Context context)
	{
		this.dbhelper = new DBHelper(context);
	}

	//获取数据库对象
	private void init(){
		db = dbhelper.getWritableDatabase();//获取一个可读可写的数据库
	}

	/**
	 * 添加（本地添加）
	 */
	public void AddStorage(String xcode,String zcode,String dcode,String sicode,String wucode,String sorderID,String quantity,String status,String dateTime,String producerID)
	{
		try {
			//取得数据库操作实例
			init();
			String sql = "insert into t_storage(xcode,zcode,dcode,sicode,wucode,sorderID,quantity,status,dateTime,producerID) values(" +
					"'"+ xcode +"','"+ zcode +"','"+ dcode +"','"+ sicode +"','"+ wucode +"','"+ sorderID +"','"+ quantity +"','"+ status +"','"+ dateTime +"','"+ producerID +"')";
			db.execSQL(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 修改（本地修改）
	 */
	public void UpdateStorage(String xcode,String sorderID,String quantity,String status,String dateTime,String producerID)
	{
		try {
			//取得数据库操作实例
			init();
			String sql = " update t_storage set sorderID='"+ sorderID +"',quantity='"+ quantity +"',status='"+ status +"',dateTime ='"+ dateTime +"',producerID ='"+ producerID +"' where xcode ='"+ xcode +"' or zcode ='"+ xcode +"'  or dcode ='"+ xcode +"' or  sicode ='"+ xcode +"'  or wucode ='"+ xcode +"' ";
			db.execSQL(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 添加二级码，修改小标记录
	 */
	public void UpdateStorage_er(String code,String sorderID)
	{
		try {
			//取得数据库操作实例
			init();
			String sql = "update t_storage set zcode='"+code+"' where zcode='0' and sorderID="+sorderID+" ";
			db.execSQL(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加三级码，修改小标记录
	 */
	public void UpdateStorage_san(String code,String sorderID)
	{
		try {
			//取得数据库操作实例
			init();
			String sql = "update t_storage set dcode='"+code+"' where dcode='0' and sorderID ="+ sorderID +" ";
			db.execSQL(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 状态(上传后修改状态记录)
	 */
	public void UStorage(){
		init();
		String sql = "update t_storage set status='3'  ";
		db.execSQL(sql);

	}

	/**
	 * 删除(上传后删除记录)
	 */
	public void DeleteStorage(){
		init();
		String sql = " delete from t_storage ";
		db.execSQL(sql);

	}

	/**
	 * 删除(上传后删除记录)
	 */
	public void DeleteStorage2(String sorderID){
		init();
		String sql = " delete from t_storage where sorderID ="+ sorderID +"";
		db.execSQL(sql);

	}

	/**
	 * 本地删除(根据二维码删除)
	 */
	public void DeleteStorage_ma(String ma){
		init();
		String sql = " delete from t_storage where xcode ='"+ ma +"' or zcode ='"+ ma +"' or dcode ='"+ ma +"' ";
		db.execSQL(sql);

	}

}
