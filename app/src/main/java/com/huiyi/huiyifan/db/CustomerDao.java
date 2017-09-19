package com.huiyi.huiyifan.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.huiyi.huiyifan.util.DBHelper;

public class CustomerDao {

	private DBHelper dbhelper;
	private SQLiteDatabase db;

	public CustomerDao(Context context)
	{
		this.dbhelper = new DBHelper(context);
	}

	//获取数据库对象
	private void init(){
		db = dbhelper.getWritableDatabase();//获取一个可读可写的数据库
	}

	/**
	 * 添加
	 * @param ID
	 * @param name
	 */
	public void AddCustomer(String ID, String name)
	{
		//取得数据库操作实例
		init();
		String sql = "insert into t_customer(ID,name) values("+ID+",'"+name+"')";
		db.execSQL(sql);

	}

	/**
	 * 删除
	 */
	public void DeleteCustomer(){
		init();
		String sql = " delete from t_customer ";
		db.execSQL(sql);

	}

}
