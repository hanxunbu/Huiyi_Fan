package com.huiyi.huiyifan.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public static final String BASE_NAME = "huiyi.db";
	private static final int VERSION = 1;//数据库版本
	private static final String TABLE_NAME = "test";

	/**
	 * 在SQLiteOpenHelper的子类中,必须有的构造函数
	 *  context 上下文
	 * BASE_NAME 数据库名称
	 * 游标工厂
	 * VERSION 当前数据库的版本
	 */
	public DBHelper(Context context){
		super(context,BASE_NAME,null,VERSION);
	}

	/**
	 * 删除数据库
	 * @param context
	 * @return
	 */
	public boolean deleteDatabase(Context context) {
		return context.deleteDatabase("huiyi.db");
	}

	//数据库被创建的时候调用该方法
	@Override
	public void onCreate(SQLiteDatabase db) {

		//t_sorder 生产订单表
		String t_sorder = "create table if not exists t_sorder(" +
				"ID integer,number varchar(500)," +
				"pihao varchar(500),productID integer," +
				"startTime varchar(500),endTime varchar(500)," +
				"quantity varchar(500)," +
				"status integer,packing integer," +
				"wu varchar(500)," +
				"si varchar(500)," +
				"da varchar(500)," +
				"zhong varchar(500)," +
				"xiao varchar(500),sUrl varchar(500)," +
				"producerID integer)";
		db.execSQL(t_sorder);

		//t_xorder 销售订单表
		String sql_xorder = "create table if not exists t_xorder(" +
				"ID integer,number varchar(500)," +
				"sorderID integer," +
				"customerID integer," +
				"xquantity varchar(500)," +
				"yquantity varchar(500)," +
				"datetime varchar(500)," +
				"producerID integer)";
		db.execSQL(sql_xorder);

		//t_customer 客户信息表
		String t_customer = "create table if not exists t_customer(" +
				"ID integer," +
				"sName varchar(500))";
		db.execSQL(t_customer);

		//t_storage 入库信息表
		String t_storage = "create table if not exists t_storage(" +
				"ID integer primary key autoincrement," +
				"xcode varchar(500)," +
				"zcode varchar(500)," +
				"dcode varchar(500)," +
				"sicode varchar(500)," +
				"wucode varchar(500)," +
				"sorderID integer," +
				"quantity varchar(500)," +
				"status integer," +
				"dateTime varchar(500)," +
				"producerID integer)";
		db.execSQL(t_storage);

		//t_shipment 出货信息表
		String t_shipment = "create table if not exists t_shipment(" +
				"ID integer primary key autoincrement," +
				"code varchar(500)," +
				"xorderID integer," +
				"sorderID integer," +
				"customerID integer," +
				"quantity varchar(500)," +
				"datetime varchar(500)," +
				"status integer," +
				"producerID integer)";
		db.execSQL(t_shipment);

		// t_refund 退货信息表
		String t_refund = "create table if not exists t_refund(" +
				"t_id integer primary key autoincrement," +
				"t_xscode varchar(500)," +
				"t_datetime varchar(500)," +
				"t_remarks varchar(500))";
		db.execSQL(t_refund);

		//t_producer 生产商账号信息表
		String t_producer = "create table if not exists t_producer(" +
				"ID integer," +
				"name varchar(500)," +
				"password varchar(500)," +
				"sName varchar(50))";
		db.execSQL(t_producer);

		//t_export 导出信息表
		String t_export = "create table if not exists t_export(" +
				"ID integer primary key autoincrement," +
				"xma integer," +
				"zma integer," +
				"dma integer," +
				"sima integer," +
				"wuma integer)";
		db.execSQL(t_export);

		//t_product 产品信息表
		String t_product = "create table if not exists t_product(" +
				"ID integer," +
				"sName varchar(500)," +
				"standard varchar(500)," +
				"pcode varchar(500))";
		db.execSQL(t_product);

		//sql_about 参数配置表
		String t_about = "create table if not exists t_about(" +
				"ID integer primary key autoincrement," +
				"url varchar(50))";
		db.execSQL(t_about);


	}

	//数据库版本号发生变更的时候被调用
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "DROP TABLE IF EXIST" + TABLE_NAME;
		db.execSQL(sql);
		this.onCreate(db);
		System.out.println("更新已完成");
	}

}
