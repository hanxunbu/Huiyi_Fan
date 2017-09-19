package com.huiyi.huiyifan.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.huiyi.huiyifan.util.DBHelper;


public class AboutDao {
	
	private DBHelper dbhelper;
	public String url;
	private SQLiteDatabase db;

	public AboutDao(Context context)
	{
		this.dbhelper = new DBHelper(context);
	}

	//获取数据库对象
	private void init(){
		db = dbhelper.getWritableDatabase();//获取一个可读可写的数据库
	}

	/**
	 * 添加
	 * @param a
	 */
	public void AddAbout(String a) {
		//取得数据库操作实例
		init();
    	String sql = "insert into t_about(url) values('"+a+"')";
        db.execSQL(sql);

    }  
	
	/**
	 *修改
	 * @param a
	 */
	public void ModifyAbout(String a) {
		//取得数据库操作实例
		init();
    	String sql = "UPDATE t_about SET url='"+a+"'";
        db.execSQL(sql);

    } 

	/**
	 * 查询
	 * @return
	 */
	public String AboutCha() {
		init();
		Cursor cz_About = db.rawQuery("select url  from t_about",null);
		if (cz_About != null) {
			while (cz_About.moveToNext()) {
				url = cz_About.getString(cz_About.getColumnIndex("url"));
				}
			}
		cz_About.close();

		return url;
	}

}
