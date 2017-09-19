package com.huiyi.huiyifan.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.huiyi.huiyifan.util.DBHelper;


public class ProducerDao {

	private DBHelper dbhelper;
	private SQLiteDatabase db;

	public ProducerDao(Context context) {
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
	 * @param password
	 */
	public void AddProducer(String ID, String name, String password)
	{
		//取得数据库操作实例
		init();
		String sql = "insert into t_producer(ID,name,password) values("+ID+",'"+name+"','"+password+"')";
		db.execSQL(sql);

	}

	/**
	 * 删除
	 */
	public void DeleteProducer(){
		init();
		String sql = " delete from t_producer ";
		db.execSQL(sql);

	}

}
