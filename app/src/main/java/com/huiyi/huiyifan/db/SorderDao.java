package com.huiyi.huiyifan.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.huiyi.huiyifan.util.DBHelper;


public class SorderDao {

	private DBHelper dbhelper;
	private SQLiteDatabase db;

	public SorderDao(Context context) {
		this.dbhelper = new DBHelper(context);
	}

	//获取数据库对象
	private void init(){
		db = dbhelper.getWritableDatabase();//获取一个可读可写的数据库
	}

	/**
	 * 删除
	 */
	public void DeleteSorder(){
		init();
		String sql = " delete from t_sorder ";
		db.execSQL(sql);

	}


}
