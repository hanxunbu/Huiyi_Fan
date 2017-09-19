package com.huiyi.huiyifan.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.huiyi.huiyifan.util.DBHelper;

public class XorderDao {

	private DBHelper dbhelper;
	private SQLiteDatabase db;

	public XorderDao(Context context)
	{
		this.dbhelper = new DBHelper(context);
	}

	//获取数据库对象
	private void init(){
		db = dbhelper.getWritableDatabase();//获取一个可读可写的数据库
	}

	/**
	 * 删除
	 */
	public void DeleteXorder(){
		init();
		String sql = " delete from t_xorder ";
		db.execSQL(sql);

	}


}
