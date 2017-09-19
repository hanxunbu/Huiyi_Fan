package com.huiyi.huiyifan.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.huiyi.huiyifan.R;
import com.huiyi.huiyifan.base.TopBarBaseActivity;
import com.huiyi.huiyifan.db.RefundDao;
import com.huiyi.huiyifan.db.ShipmentDao;
import com.huiyi.huiyifan.db.StorageDao;
import com.huiyi.huiyifan.modle.Dict_Product;
import com.huiyi.huiyifan.util.AppManager;
import com.huiyi.huiyifan.util.DBHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LW on 2017/7/31.
 * 导出数据
 */
public class ExportActivity extends TopBarBaseActivity implements View.OnClickListener{

    private Spinner production_sp;//生产单号
    private Spinner delivery_sp;//交货单号
    private Button production_bt;
    private Button delivery_bt;
    private Button refund_bt;
    private DBHelper dbhelper;
    private SQLiteDatabase db;
    private String sorderID;
    private String producer_id,d_number;
    private int aru = 0 ,bchu = 0;
    private MediaPlayer mediaPlayer;

    //UI布局
    @Override
    protected int getContentView() {
        AppManager.getAppManager().addActivity(this);
        Intent intent = getIntent();
        producer_id = intent.getStringExtra("producer_id");
        return R.layout.activity_export;
    }
    //自定义Toolbar
    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        setTitle("导出数据");
        setLeftButton(R.drawable.back, new OnClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(ExportActivity.this,MainActivity.class);
                intent.putExtra("producer_id",producer_id);
                startActivity(intent);

            }
        });

    }
    //初始化
    private void initView() {
        production_sp = (Spinner) findViewById(R.id.export_production_order_sp);
        delivery_sp = (Spinner) findViewById(R.id.export_delivery_order_sp);
        production_bt = (Button) findViewById(R.id.export_production_order_bt);
        delivery_bt = (Button) findViewById(R.id.export_delivery_order_bt);
        refund_bt = (Button) findViewById(R.id.export_refund_order_bt);

        production_bt.setOnClickListener(this);
        delivery_bt.setOnClickListener(this);
        refund_bt.setOnClickListener(this);


        //Spinner建立数据源(生产订单)
        dbhelper = new DBHelper(getBaseContext());
        db = dbhelper.getReadableDatabase();
        Cursor cursor_p = db.rawQuery("select * from t_sorder",null);
        List<Dict_Product> arraylist_p = new ArrayList<Dict_Product>();
        if (cursor_p != null) {
            while (cursor_p.moveToNext()) {
                int ID = Integer.parseInt(cursor_p.getString(cursor_p.getColumnIndex("ID")));
                String number = cursor_p.getString(cursor_p.getColumnIndex("number"));
                arraylist_p.add(new Dict_Product(ID, number));
            }
        }
        cursor_p.close();
        //Adapter适配器
        ArrayAdapter<Dict_Product> adapter_p = new ArrayAdapter<Dict_Product>(this,android.R.layout.simple_gallery_item,arraylist_p);
        //设置样式
        adapter_p.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        //加载适配器
        production_sp.setAdapter(adapter_p);


        //Spinner建立数据源(交货订单)
        Cursor cursor_d = db.rawQuery("select DISTINCT number from t_xorder",null);
        List<Dict_Product> arraylist_d = new ArrayList<Dict_Product>();
        if (cursor_d != null) {
            while (cursor_d.moveToNext()) {
//                int ID = Integer.parseInt(cursor_d.getString(cursor_d.getColumnIndex("ID")));
                String number = cursor_d.getString(cursor_d.getColumnIndex("number"));
                arraylist_d.add(new Dict_Product(0, number));
            }
        }
        cursor_d.close();
        //Adapter适配器
        ArrayAdapter<Dict_Product> adapter_d = new ArrayAdapter<Dict_Product>(this,android.R.layout.simple_spinner_item,arraylist_d);
        //设置样式
        adapter_d.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        //加载适配器
        delivery_sp.setAdapter(adapter_d);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.export_production_order_bt://导出入库数据
                try {

                    dbhelper = new DBHelper(getBaseContext());
                    db = dbhelper.getReadableDatabase();

                    sorderID = ((Dict_Product) production_sp.getSelectedItem()).getId().toString();// 生产订单id
                    String	number = ((Dict_Product) production_sp.getSelectedItem()).getText().toString();//生产单号
                    String a = sorderID;
                    aru=0;
                    if (sorderID.trim().equals("0") || sorderID.trim() == null) {
                        Toast.makeText(this,"生产单号不能为空！",Toast.LENGTH_SHORT).show();
                    } else {
                        StringBuffer buffer = new StringBuffer();
                        buffer.append("一级码,二级码,三级码,四级码,五级码,生产订单号\r\n");
                        Cursor c = db.rawQuery(
                                " select t_storage.*,t_sorder.number,t_producer.sName from t_storage inner join t_sorder on t_storage.sorderID = t_sorder.ID inner join t_producer on t_sorder.producerID = t_producer.ID where t_sorder.ID="
                                        + sorderID + " ", null);
                        if (c != null) {
                            while (c.moveToNext()) {
                                String a1 = c.getString(c.getColumnIndex("xcode"));
                                String a2 = c.getString(c.getColumnIndex("zcode"));
                                String a3 = c.getString(c.getColumnIndex("dcode"));
                                String a4 = c.getString(c.getColumnIndex("sicode"));
                                String a5 = c.getString(c.getColumnIndex("wucode"));
                                String a6 = c.getString(c.getColumnIndex("number"));
                                buffer.append(a1+","+a2+","+a3+","+a4+","+a5+","+a6+"\r\n");
                                aru++;
                            }
                        }
                        c.close();

                        String data = buffer.toString();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                        String str = formatter.format(curDate);
                        String filename = "入库信息_共导出" + aru + "条记录_生产单号"+number+"_时间"+str+".txt";

                        String path = Environment.getExternalStorageDirectory() + "/Huiyi/storage/";
                        if (!new File(path).exists()) {
                            new File(path).mkdirs();
                        }
                        File file = new File(path, filename);
                        OutputStream out = new FileOutputStream(file);
                        // excel需要BOM签名才能解析utf-8格式的编码
                        byte b[] = { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };
                        out.write(b);
                        out.write(data.getBytes());
                        out.close();
//                        Toast.makeText(this,"入库信息文件导出成功共" + aru + "条记录！请到文件管理中Huiyi/1ruku/文件夹查看",Toast.LENGTH_SHORT).show();
                        // 删除信息
                        StorageDao storageDao = new StorageDao(getBaseContext());
                        storageDao.DeleteStorage2(sorderID);
                        Toast.makeText(getApplicationContext(),
                                "入库信息文件导出成功共" + aru + "条记录！请到SD卡中Huiyi/storage/文件夹查看",Toast.LENGTH_SHORT)
                                .show();
                    }
                } catch (Exception e) {

                    Toast.makeText(this,"导出失败，发生异常！",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;

            case R.id.export_delivery_order_bt://导出出货数据
                try {

                    d_number = ((Dict_Product) delivery_sp.getSelectedItem()).getText().toString();// 交货订单编号
				    bchu=0;
                    dbhelper = new DBHelper(getBaseContext());
                    db = dbhelper.getReadableDatabase();

                    StringBuffer buffer = new StringBuffer();
                    buffer.append("二维码,交货单号,交货单号id,产品id\r\n");
                    Cursor c = db.rawQuery(
                                    " select t_xorder.ID as x_id,t_xorder.number,t_xorder.sorderID as chanpinID,t_shipment.* from t_shipment inner join t_xorder on t_shipment.xorderID = t_xorder.ID where t_xorder.number='"
                                            + d_number + "' ", null);
                    if (c != null) {
                        while (c.moveToNext()) {
                            String a1 = c.getString(c.getColumnIndex("code"));
                            String a2 = c.getString(c.getColumnIndex("number"));
                            String a3 = c.getString(c.getColumnIndex("x_id"));
                            String a4 = c.getString(c.getColumnIndex("chanpinID"));
                            buffer.append(a1+","+a2+","+a3+","+a4+"\r\n");
                            bchu++;
                        }

                    }
                    c.close();
                    try {

                        String data = buffer.toString();


                        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                        String str = formatter.format(curDate);

                        String filename = "出货信息_共导出" + bchu + "条记录_交货单号" + d_number + "_时间"+str+".txt";

                        String path = Environment.getExternalStorageDirectory() + "/Huiyi/shipment/";
                        if (!new File(path).exists()) {
                            new File(path).mkdirs();
                        }
                        File file = new File(path, filename);
                        OutputStream out = new FileOutputStream(file);
                        // excel需要BOM签名才能解析utf-8格式的编码
                        byte b[] = { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };
                        out.write(b);
                        out.write(data.getBytes());
                        out.close();
                    } catch (FileNotFoundException e) {
                        Toast.makeText(this,"导出失败，发生异常！",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                    // 删除信息
                    Cursor sc_c = db
                            .rawQuery(
                                    " select ID from t_xorder where number ='"+d_number+"' ", null);
                    if (sc_c != null) {
                        while (sc_c.moveToNext()) {
                            String ID = sc_c.getString(sc_c.getColumnIndex("ID"));
                            // 删除信息
                            ShipmentDao shipmentDao = new ShipmentDao(getBaseContext());
                            shipmentDao.DeleteShipment2(ID);
                        }
                    }
                    sc_c.close();

                    Toast.makeText(getApplicationContext(),
                            "出货信息文件导出成功_共" + bchu + "条记录！请到文件管理中文件夹Huiyi/shipment/中查看", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this,"导出失败，发生异常！",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;

            case R.id.export_refund_order_bt://导出退货数据
                try {
                    bchu=0;
                    dbhelper = new DBHelper(getBaseContext());
                    db = dbhelper.getReadableDatabase();

                    StringBuffer buffer = new StringBuffer();
                    buffer.append("二维码,退货原因,退货时间\r\n");
                    Cursor c = db.rawQuery(" select t_xscode,t_remarks,t_datetime from t_refund ", null);
                    if (c != null) {
                        while (c.moveToNext()) {
                            String a1 = c.getString(c.getColumnIndex("t_xscode"));
                            String a2 = c.getString(c.getColumnIndex("t_remarks"));
                            String a3 = c.getString(c.getColumnIndex("t_datetime"));
                            buffer.append(a1+","+a2+","+a3+"\r\n");
                            bchu++;
                        }

                    }
                    c.close();

                    try {

                        String data = buffer.toString();

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                        String str = formatter.format(curDate);
                        String filename = "退货信息_共导出" + bchu + "条记录_时间"+str+".txt";

                        String path = Environment.getExternalStorageDirectory() + "/Huiyi/refund/";
                        if (!new File(path).exists()) {
                            new File(path).mkdirs();
                        }
                        File file = new File(path, filename);
                        OutputStream out = new FileOutputStream(file);
                        // excel需要BOM签名才能解析utf-8格式的编码
                        byte b[] = { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };
                        out.write(b);
                        out.write(data.getBytes());
                        out.close();
                    } catch (FileNotFoundException e) {
                        Toast.makeText(this,"导出失败，发生异常！",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 删除信息
                    RefundDao refundDao = new RefundDao(getBaseContext());
                    refundDao.DeleteRefund();
                    Toast.makeText(getApplicationContext(),
                            "退货信息文件导出成功_共" + bchu + "条记录！请到文件管理中文件夹Huiyi/refund/中查看", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this,"导出失败，发生异常！",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
        }
    }

}
