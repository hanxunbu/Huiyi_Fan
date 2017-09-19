package com.huiyi.huiyifan.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.huiyi.huiyifan.util.DBHelper;

public class ExportDao {

	private DBHelper dbhelper;
	private SQLiteDatabase db;
	public String url;


	public ExportDao(Context context)
	{
		this.dbhelper = new DBHelper(context);
	}
	//获取数据库对象
	private void init(){
		db = dbhelper.getWritableDatabase();//获取一个可读可写的数据库
	}
	/**
	 * 添加
	 * @param xma
	 * @param zma
	 * @param dma
	 * @param sima
	 * @param wuma
	 */
	public void AddExport(int xma,int zma,int dma,int sima,int wuma)
	{
		//取得数据库操作实例
		init();
		String sql = "insert into t_export(xma,zma,dma,sima,wuma) values('"+xma+"','"+zma+"','"+dma+"','"+sima+"','"+wuma+"')";
		db.execSQL(sql);

	}


	/**
	 * 修改
	 * @param xma
	 * @param zma
	 * @param dma
	 * @param sima
	 * @param wuma
	 */
	public void ModifyExport(int xma,int zma,int dma,int sima,int wuma)
	{
		//取得数据库操作实例
		init();
		String sql = "UPDATE t_export SET xma='"+xma+"',zma='"+zma+"',dma='"+dma+"',sima='"+sima+"',wuma='"+wuma+"' ";
		db.execSQL(sql);

	}

	/**
	 * 删除
	 */
	public void DeleteExport(){
		init();
		String sql = " delete from t_export ";
		db.execSQL(sql);

	}


}
