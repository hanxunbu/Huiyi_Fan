package com.huiyi.huiyifan.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.huiyi.huiyifan.R;
import com.huiyi.huiyifan.base.TopBarBaseActivity;
import com.huiyi.huiyifan.db.AboutDao;
import com.huiyi.huiyifan.db.CustomerDao;
import com.huiyi.huiyifan.db.ProducerDao;
import com.huiyi.huiyifan.db.ProductDao;
import com.huiyi.huiyifan.db.SorderDao;
import com.huiyi.huiyifan.db.XorderDao;
import com.huiyi.huiyifan.util.AppManager;
import com.huiyi.huiyifan.util.DBHelper;
import com.huiyi.huiyifan.util.HttpConnSoap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LW on 2017/7/31.
 * 更新页面
 */

public class UpdateActivity extends TopBarBaseActivity implements View.OnClickListener{

    private Button update_bt;//更新按钮
    private TextView account_tv;//账号信息
    private TextView product_tv;//产品信息
    private TextView customer_tv;//客户信息
    private TextView production_tv;//生产订单
    private TextView sell_tv;//销售订单
    private String producer_id;
    private HttpConnSoap Soap = new HttpConnSoap();//连接WebService;
    private String ServerUrl;//URL
    private DBHelper dbhelper;
    private SQLiteDatabase db;
    private String data_1,data_2,data_3,data_4,data_5;


    //UI布局
    @Override
    protected int getContentView() {
        AppManager.getAppManager().addActivity(this);
        Intent intent = getIntent();
        producer_id = intent.getStringExtra("producer_id");

        return R.layout.activity_update;
    }

    //自定义Toolbar
    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        setTitle("更新数据");
        setLeftButton(R.drawable.back, new OnClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(UpdateActivity.this,MainActivity.class);
                intent.putExtra("producer_id",producer_id);
                startActivity(intent);

            }
        });
    }

    //初始化
    private void initView() {
        update_bt = (Button) findViewById(R.id.update_bt);
        account_tv = (TextView) findViewById(R.id.update_account_tv);
        product_tv = (TextView) findViewById(R.id.update_product_tv);
        customer_tv = (TextView) findViewById(R.id.update_clientele_tv);
        production_tv = (TextView) findViewById(R.id.update_production_tv);
        sell_tv = (TextView) findViewById(R.id.update_sell_tv);

        update_bt.setOnClickListener(this);

        //获取接口地址
        AboutDao aboutDao = new AboutDao(getBaseContext());
        ServerUrl = aboutDao.AboutCha();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update_bt :
                //删除数据库表
                try {
                    dbhelper = new DBHelper(getBaseContext());
                    dbhelper.getReadableDatabase();

                    //删除生产商账号表数据
                    ProducerDao producerDao = new ProducerDao(getBaseContext());
                    producerDao.DeleteProducer();

                    //删除产品表数据
                    ProductDao productDao = new ProductDao(getBaseContext());
                    productDao.DeleteProduct();

                    //删除客户表数据
                    CustomerDao customerDao = new CustomerDao(getBaseContext());
                    customerDao.DeleteCustomer();

                    //删除生产订单表数据
                    SorderDao sorderDao = new SorderDao(getBaseContext());
                    sorderDao.DeleteSorder();

                    //删除销售订单表数据
                    XorderDao xorderDao = new XorderDao(getBaseContext());
                    xorderDao.DeleteXorder();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                getRemoteInfo();
                break;
        }
    }

    //获取json数据
    public void getRemoteInfo() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("更新数据");
        progressDialog.setMessage("正在更新中...");
        progressDialog.onStart();
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    producer(producer_id,"json_producerAll",ServerUrl);//生产商账号信息
                    product(producer_id,"json_productAll",ServerUrl);//产品信息
                    customer(producer_id,"json_customerAll",ServerUrl);//客户信息
                    sorder(producer_id,"json_sorderAll",ServerUrl);//生产订单信息
                    xorder(producer_id,"json_xorderAll",ServerUrl);//销售订单信息
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    progressDialog.dismiss();
                }
            }
        }).start();

    }
    //生产商账号信息
    public void producer(String sproducer_id,String fmethodName,String fuwuqi_url){

        Soap = new HttpConnSoap();
        String result = Soap.Getjson2(sproducer_id,fmethodName, fuwuqi_url);
        Message message = handler_producer.obtainMessage();
        message.obj = result;
        handler_producer.sendMessage(message);
    }
    //产品信息
    public void product(String sproducer_id,String fmethodName,String fuwuqi_url){

        Soap = new HttpConnSoap();
        String result = Soap.Getjson2(sproducer_id,fmethodName, fuwuqi_url);
        Message message = handler_product.obtainMessage();
        message.obj = result;
        handler_product.sendMessage(message);
    }
    //客户信息
    public void customer(String sproducer_id,String fmethodName,String fuwuqi_url){

        Soap = new HttpConnSoap();
        String result = Soap.Getjson2(sproducer_id,fmethodName, fuwuqi_url);
        Message message = handler_customer.obtainMessage();
        message.obj = result;
        handler_customer.sendMessage(message);
    }
    //生产订单信息
    public void sorder(String sproducer_id,String fmethodName,String fuwuqi_url){

        Soap = new HttpConnSoap();
        String result = Soap.Getjson2(sproducer_id,fmethodName, fuwuqi_url);
        Message message = handler_sorder.obtainMessage();
        message.obj = result;
        handler_sorder.sendMessage(message);
    }
    //销售订单信息
    public void xorder(String sproducer_id,String fmethodName,String fuwuqi_url){

        Soap = new HttpConnSoap();
        String result = Soap.Getjson2(sproducer_id,fmethodName, fuwuqi_url);
        Message message = handler_xorder.obtainMessage();
        message.obj = result;
        handler_xorder.sendMessage(message);
    }

    //生产商账号信息
    private Handler handler_producer = new Handler() {
        public void handleMessage(Message msg) {
            // 将WebService得到的结果返回给TextView
            String jsonshuju = msg.obj.toString();
            try {
                ArrayList<String> list = new ArrayList<String>();
                JSONArray jsonArray = new JSONObject(jsonshuju).getJSONArray("Table");// 获得json字符串里名字为ds的数组，如果ds不是最外层，请通过ds的父对象来取
                int count = jsonArray.length();
                for (int index = 0; index < count; index++) {
                    String mId = jsonArray.optJSONObject(index).getString("ID").toString();
                    String mName = jsonArray.optJSONObject(index).getString("name").toString();
                    String mPassword = jsonArray.optJSONObject(index).getString("password").toString();
                    String sName = jsonArray.optJSONObject(index).getString("sName").toString();
                    String sql = "insert into t_producer(ID,name,password,sName) values(" + mId + ",'" + mName + "','" + mPassword + "','" + sName + "')";
                    list.add(sql);
                }
                inertOrUpdateDateBatch(list);
                // 本地记录开始
                dbhelper = new DBHelper(getBaseContext());
                db = dbhelper.getReadableDatabase();
                Cursor cz_ru = db.rawQuery(" select count(*) as jilu from t_producer ", null);
                if (cz_ru != null) {
                    while (cz_ru.moveToNext()) {
                        data_1 = cz_ru.getString(cz_ru.getColumnIndex("jilu"));
                    }
                }
                cz_ru.close();
                // 本地记录结束
                account_tv.setText("1.账号资料更新成功..." + data_1 + "条");
            } catch (JSONException e) {
                account_tv.setText("1.账号资料更新成功...0条");
                e.printStackTrace();
            }
        }
    };

    //产品信息
    private Handler handler_product = new Handler() {
        public void handleMessage(Message msg) {
            // 将WebService得到的结果返回给TextView
            String jsonshuju = msg.obj.toString();
            try {
                ArrayList<String> list = new ArrayList<String>();
                JSONArray jsonArray = new JSONObject(jsonshuju).getJSONArray("Table");// 获得json字符串里名字为ds的数组，如果ds不是最外层，请通过ds的父对象来取
                int count = jsonArray.length();
                for (int index = 0; index < count; index++) {
                    String mId = jsonArray.optJSONObject(index).getString("ID").toString();
                    String sName = jsonArray.optJSONObject(index).getString("sName").toString();
                    String standard = jsonArray.optJSONObject(index).getString("standard").toString();
                    String pcode = jsonArray.optJSONObject(index).getString("pcode").toString();
                    String sql = "insert into t_product(ID,sName,standard,pcode) values(" + mId + ",'" + sName + "','" + standard + "','" + pcode + "')";
                    list.add(sql);

                }
                inertOrUpdateDateBatch(list);
                // 本地入库的记录开始
                dbhelper = new DBHelper(getBaseContext());
                db = dbhelper.getReadableDatabase();
                Cursor cz_ru = db.rawQuery(" select count(*) as jilu  from t_product ", null);
                if (cz_ru != null) {
                    while (cz_ru.moveToNext()) {
                        data_2 = cz_ru.getString(cz_ru.getColumnIndex("jilu"));
                    }
                }
                cz_ru.close();
                // 本地入库的记录结束
                product_tv.setText("2.产品资料更新成功..." + data_2 + "条");
            } catch (JSONException e) {
                product_tv.setText("2.产品资料更新成功...0条");
                e.printStackTrace();
            }
        }
    };

    //客户信息
    private Handler handler_customer = new Handler() {
        public void handleMessage(Message msg) {
            // 将WebService得到的结果返回给TextView
            String jsonshuju = msg.obj.toString();
            try {
                ArrayList<String> list = new ArrayList<String>();
                JSONArray jsonArray = new JSONObject(jsonshuju).getJSONArray("Table");// 获得json字符串里名字为ds的数组，如果ds不是最外层，请通过ds的父对象来取
                int count = jsonArray.length();
                for (int index = 0; index < count; index++) {
                    String mId = jsonArray.optJSONObject(index).getString("ID").toString();
                    String sName = jsonArray.optJSONObject(index).getString("sName").toString();
                    String sql = "insert into t_customer(ID,sName) values(" + mId + ",'" + sName + "')";
                    list.add(sql);

                }
                inertOrUpdateDateBatch(list);
                // 本地入库的记录开始
                dbhelper = new DBHelper(getBaseContext());
                db = dbhelper.getReadableDatabase();
                Cursor cz_ru = db.rawQuery(" select count(*) as jilu  from t_customer ", null);
                if (cz_ru != null) {
                    while (cz_ru.moveToNext()) {
                        data_3 = cz_ru.getString(cz_ru.getColumnIndex("jilu"));
                    }
                }
                cz_ru.close();
                // 本地入库的记录结束
                customer_tv.setText("3.客户资料更新成功..." + data_3 + "条");
            } catch (JSONException e) {
                customer_tv.setText("3.客户资料更新成功...0条");
                e.printStackTrace();
            }
        };
    };

    //生产订单信息
    private Handler handler_sorder = new Handler() {
        public void handleMessage(Message msg) {
            // 将WebService得到的结果返回给TextView
            String jsonshuju = msg.obj.toString();
            try {
                ArrayList<String> list = new ArrayList<String>();
                JSONArray jsonArray = new JSONObject(jsonshuju).getJSONArray("Table");// 获得json字符串里名字为ds的数组，如果ds不是最外层，请通过ds的父对象来取
                int count = jsonArray.length();
                for (int index = 0; index < count; index++) {
                    String mId = jsonArray.optJSONObject(index).getString("ID").toString();
                    String number = jsonArray.optJSONObject(index).getString("number").toString();
                    String batch_number = jsonArray.optJSONObject(index).getString("pihao").toString();
                    String productID = jsonArray.optJSONObject(index).getString("productID").toString();
                    String startTime = jsonArray.optJSONObject(index).getString("startTime").toString();
                    String endTime = jsonArray.optJSONObject(index).getString("endTime").toString();
                    String quantity = jsonArray.optJSONObject(index).getString("quantity").toString();
                    String status = jsonArray.optJSONObject(index).getString("status").toString();
                    String packing = jsonArray.optJSONObject(index).getString("packing").toString();
                    String five_qr = jsonArray.optJSONObject(index).getString("wu").toString();
                    String four_qr = jsonArray.optJSONObject(index).getString("si").toString();
                    String large_qr = jsonArray.optJSONObject(index).getString("da").toString();
                    String middle_qr = jsonArray.optJSONObject(index).getString("zhong").toString();
                    String small_qr = jsonArray.optJSONObject(index).getString("xiao").toString();
                    String sUrl = jsonArray.optJSONObject(index).getString("sUrl").toString();
                    String producerID = jsonArray.optJSONObject(index).getString("producerID").toString();
                    String sql = "insert into t_sorder(ID,number,pihao,productID,startTime,endTime,quantity,status,packing,wu,si,da,zhong,xiao,sUrl,producerID) values("
                            + mId + ",'" + number + "','" + batch_number + "','" + productID + "','" + startTime + "','" + endTime + "','" + quantity + "','" + status + "','" + packing + "','"
                            + five_qr + "','" + four_qr + "','" + large_qr + "','" + middle_qr + "','" + small_qr + "','" + sUrl + "','" + producerID + "')";
                    list.add(sql);

                }
                inertOrUpdateDateBatch(list);
                // 本地入库的记录开始
                dbhelper = new DBHelper(getBaseContext());
                db = dbhelper.getReadableDatabase();
                Cursor cz_ru = db.rawQuery(" select count(*) as jilu  from t_sorder ", null);
                if (cz_ru != null) {
                    while (cz_ru.moveToNext()) {
                        data_4 = cz_ru.getString(cz_ru.getColumnIndex("jilu"));
                    }
                }
                cz_ru.close();
                // 本地入库的记录结束
                production_tv.setText("4.生产订单资料更新成功..." + data_4 + "条");
            } catch (JSONException e) {
                production_tv.setText("4.生产订单资料更新成功...0条");
                e.printStackTrace();
            }
        };
    };


    //销售订单信息
    private Handler handler_xorder = new Handler() {
        public void handleMessage(Message msg) {
            // 将WebService得到的结果返回给TextView
            String jsonshuju = msg.obj.toString();
            try {
                ArrayList<String> list = new ArrayList<String>();
                JSONArray jsonArray = new JSONObject(jsonshuju).getJSONArray("ds");// 获得json字符串里名字为ds的数组，如果ds不是最外层，请通过ds的父对象来取
                int count = jsonArray.length();
                for (int index = 0; index < count; index++) {
                    String mId = jsonArray.optJSONObject(index).getString("ID").toString();
                    String number = jsonArray.optJSONObject(index).getString("number").toString();
                    String sorderID = jsonArray.optJSONObject(index).getString("sorderID").toString();
                    String customerID = jsonArray.optJSONObject(index).getString("customerID").toString();
                    String xquantity = jsonArray.optJSONObject(index).getString("xquantity").toString();
                    String yquantity = jsonArray.optJSONObject(index).getString("yquantity").toString();
                    String datetime = jsonArray.optJSONObject(index).getString("datetime").toString();
                    String producerID = jsonArray.optJSONObject(index).getString("producerID").toString();
                    String sql = "insert into t_xorder(ID,number,sorderID,customerID,xquantity,yquantity,datetime,producerID) values("
                            + mId + ",'" + number + "','" + sorderID + "','" + customerID + "','" + xquantity + "','" + yquantity + "','" + datetime + "','" + producerID + "')";
                    list.add(sql);
                }
                inertOrUpdateDateBatch(list);
                // 本地入库的记录开始
                dbhelper = new DBHelper(getBaseContext());
                db = dbhelper.getReadableDatabase();
                Cursor cz_ru = db.rawQuery(" select count(*) as jilu  from t_xorder ", null);
                if (cz_ru != null) {
                    while (cz_ru.moveToNext()) {
                        data_5 = cz_ru.getString(cz_ru.getColumnIndex("jilu"));
                    }
                }
                cz_ru.close();
                // 本地入库的记录结束
                sell_tv.setText("5.销售订单资料更新成功..." + data_5 + "条");
            } catch (JSONException e) {
                sell_tv.setText("5.销售订单资料更新成功...0条");
                e.printStackTrace();
            }
        };
    };

    //批量添加公用方法
    public void inertOrUpdateDateBatch(List<String> sqls) {
        dbhelper = new DBHelper(getBaseContext());
        db = dbhelper.getReadableDatabase();
        db.beginTransaction();
        try {
            for (String sql : sqls) {
                db.execSQL(sql);
            }
            // 设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 结束事务
            db.endTransaction();
            db.close();
        }
    }


}
