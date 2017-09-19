package com.huiyi.huiyifan.ui;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.huiyi.huiyifan.R;
import com.huiyi.huiyifan.base.TopBarBaseActivity;
import com.huiyi.huiyifan.db.AboutDao;
import com.huiyi.huiyifan.db.ShipmentDao;
import com.huiyi.huiyifan.modle.Dict_Product;
import com.huiyi.huiyifan.util.AppManager;
import com.huiyi.huiyifan.util.DBHelper;
import com.huiyi.huiyifan.util.DateTimePickDialogUtil;
import com.huiyi.huiyifan.util.HttpConnSoap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by LW on 2017/7/29.
 * 出货管理
 */

public class ShipmentActivity extends TopBarBaseActivity implements View.OnClickListener{

    private Spinner delivery_order_sp;//交货单号
    private Spinner product_name_sp;//产品名称
    private EditText specifications_et;//产品规格
    private EditText customer_name_et;//客户名称
    private EditText delivered_number_et;//交货数量
    private EditText shipping_time_et;//出货时间
    private RadioGroup radioGroup;
    private RadioButton offline_rb;//离线出货
    private RadioButton deletion_rb;//本地删除
    private EditText qr_et;//二维码
    private TextView qr_number_tv;//扫描数量
    private String ServiceUrl;//url
    private DBHelper dbhelper;
    private SQLiteDatabase db;
    private String record;//记录
    private ArrayList<String> arrayList = new ArrayList<String>();
    private ArrayList<String> brrayList = new ArrayList<String>();
    private ArrayList<String> crrayList = new ArrayList<String>();
    private HttpConnSoap Soap = new HttpConnSoap();
    protected static final String DateTime = null;
    private String producer_id,shuliang,xorderID,xcode,datetime,quantity,producerID,xs_number,pcode="",kk="1";//表元素
    private MediaPlayer mediaPlayer;


    //UI布局
    @Override
    protected int getContentView() {
        AppManager.getAppManager().addActivity(this);
        Intent intent = getIntent();
        producer_id = intent.getStringExtra("producer_id");

        return R.layout.activity_shipment;
    }
    //自定义Toolbar
    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        setTitle("出货管理");
        setLeftButton(R.drawable.back, new OnClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(ShipmentActivity.this,MainActivity.class);
                intent.putExtra("producer_id",producer_id);
                startActivity(intent);

            }
        });
    }

    private void initView() {
        delivery_order_sp = (Spinner) findViewById(R.id.shipment_deliver_order_sp);
        product_name_sp = (Spinner) findViewById(R.id.shipment_product_name_sp);
        specifications_et = (EditText) findViewById(R.id.shipment_specifications_et);
        customer_name_et = (EditText) findViewById(R.id.shipment_customer_name_et);
        delivered_number_et = (EditText) findViewById(R.id.shipment_delivered_number_et);
        shipping_time_et = (EditText) findViewById(R.id.shipment_storagetime_et);
        radioGroup = (RadioGroup) findViewById(R.id.shipment_rg);
        offline_rb = (RadioButton) findViewById(R.id.shipment_offline_rb);
        deletion_rb = (RadioButton) findViewById(R.id.shipment_deletion_rb);
        qr_et = (EditText) findViewById(R.id.shipment_qr_et);
        qr_number_tv = (TextView) findViewById(R.id.shipment_qr_number_tv);

        // 时间文本框触发开始
        shipping_time_et.setOnClickListener(this);

        //获取接口
        AboutDao aboutDao = new AboutDao(getBaseContext());
        ServiceUrl = aboutDao.AboutCha();
        //获取数据库
        dbhelper = new DBHelper(getBaseContext());
        db = dbhelper.getReadableDatabase();

        //交货单号
        Cursor cursor_delivery = db.rawQuery("select distinct number from t_xorder", null);
        List<Dict_Product> dicts1 = new ArrayList<Dict_Product>();
        if (cursor_delivery != null) {
            while (cursor_delivery.moveToNext()) {
                String number = cursor_delivery.getString(cursor_delivery.getColumnIndex("number"));
                dicts1.add(new Dict_Product(0, number));
            }
        }
        cursor_delivery.close();
        //Adapter适配器
        ArrayAdapter<Dict_Product> adapter_d = new ArrayAdapter<Dict_Product>(this,android.R.layout.simple_spinner_item,dicts1);
        //设置样式
        adapter_d.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        //加载适配器
        delivery_order_sp.setAdapter(adapter_d);
        //注册OnItemSelected事件
        delivery_order_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cpphah();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                String sInfo = "什么也没有选!";
                Toast.makeText(getApplicationContext(),sInfo,Toast.LENGTH_SHORT).show();
            }
        });

        // 当前日期
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        shipping_time_et.setText(str);
        // 离线出库的记录开始
        Cursor cz_ru = db.rawQuery(" select count(*) as jilu  from  t_shipment ", null);
        if (cz_ru != null) {
            while (cz_ru.moveToNext()) {
                shuliang = cz_ru.getString(cz_ru.getColumnIndex("jilu"));
            }
        }
        cz_ru.close();
        // 离线出库的记录结束
        qr_number_tv.setText(shuliang);// 本地添加数量

        qr_et.requestFocus();//获取光标
        qr_et.setOnKeyListener(onKey);
    }

    View.OnKeyListener onKey = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            //是否是回车键  event.getAction() == KeyEvent.ACTION_DOWN是处理让他只执行一次
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                if (imm.isActive()) {

                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    dbhelper = new DBHelper(getBaseContext());
                    db = dbhelper.getReadableDatabase();

                    try {
                        xorderID = ((Dict_Product) product_name_sp.getSelectedItem()).getId().toString();//产品名称
                        xs_number = ((Dict_Product) delivery_order_sp.getSelectedItem()).getText().toString();//生产订单id
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    xcode = qr_et.getText().toString();// 条码
                    datetime = shipping_time_et.getText().toString();// 出货时间
                    quantity ="1"; //出货数量
                    producerID = producer_id;//生产商id
                    if (xcode.trim().equals("") || xcode.trim() == null) {
                        Toast.makeText(getBaseContext(),"二维码不能为空！",Toast.LENGTH_SHORT).show();
                        //播放错误信息
                        mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                        mediaPlayer.start();
                    }
                    else if (xorderID.trim().equals("0") || xorderID.trim() == null) {
                        Toast.makeText(getBaseContext(),"交货单号不能为空！",Toast.LENGTH_SHORT).show();
                    } else{
                        //选择框
                        // 选中离线添加
                        if (offline_rb.isChecked()){
                            //======判断二维码数字设定的产品与选中的入库产品是否一致===========================================
                            String uu = xorderID;
                            // 根据订单id查询产品码开始
                            Cursor cz_ru = db.rawQuery(" select t_product.pcode from t_xorder inner join t_product on t_xorder.sorderID = t_product.ID where t_xorder.ID="+ xorderID +" ", null);
                            if (cz_ru != null) {
                                while (cz_ru.moveToNext()) {
                                    pcode = cz_ru.getString(cz_ru.getColumnIndex("pcode"));
                                }
                            }
                            cz_ru.close();
                            // 根据订单id查询产品码开始
                            String canshuzhi_pd = getValueByName(xcode,"c");

                            if(canshuzhi_pd.length() == 20) {
                                kk = canshuzhi_pd.substring(8, 11);

                            }if(canshuzhi_pd.length() == 19){
                                    kk = canshuzhi_pd.substring(7, 10);

                            }
                            if(canshuzhi_pd == null || canshuzhi_pd.equals("")){
                                    Toast.makeText(getBaseContext(),"二维码格式不正确！",Toast.LENGTH_SHORT).show();
                                    qr_et.setText("");
                                    Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
                                    vibrator.vibrate(400);
                                    //播放错误信息
                                    mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                                    mediaPlayer.start();
                            }else if(isNumeric(canshuzhi_pd) == false){
                                    Toast.makeText(getBaseContext(),"二维码格式不正确！",Toast.LENGTH_SHORT).show();
                                    qr_et.setText("");
                                    Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
                                    vibrator.vibrate(400);
                                    //播放错误信息
                                    mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                                    mediaPlayer.start();
                            }else if(kk.equals(pcode) == false){
                                    Toast.makeText(getBaseContext(),"扫描失败，出货的产品与交货订单的产品不一致！",Toast.LENGTH_SHORT).show();
                                    qr_et.setText("");
                                    Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
                                    vibrator.vibrate(400);
                                    //播放错误信息
                                    mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                                    mediaPlayer.start();
                            } else{
                                    //===是否一致
                                    // 条码是否已存在开始
                                    Cursor chu_z = db.rawQuery(" select count(*) as jilu from  t_shipment where code='"+ xcode + "'  ", null);
                                    if (chu_z != null) {
                                        while (chu_z.moveToNext()) {
                                            record = chu_z.getString(chu_z.getColumnIndex("jilu"));
                                        }
                                    }
                                    chu_z.close();
                                    String test1_chu = record;

                                    if (Integer.parseInt(test1_chu) == 0) {
                                        ShipmentDao shipmentDao = new ShipmentDao(getBaseContext());
                                        shipmentDao.AddShipment(xcode, xorderID, "0", "0", quantity, datetime, "1", producerID);
                                        Toast.makeText(getBaseContext(),"离线添加出货数据成功！",Toast.LENGTH_SHORT).show();
                                        qr_et.setText("");

                                        Cursor ssc = db.rawQuery(" select count(*) as jilu  from  t_shipment  ", null);
                                        if (ssc != null) {
                                            while (ssc.moveToNext()) {
                                                String zongshu = ssc.getString(ssc.getColumnIndex("jilu"));
                                                qr_number_tv.setText(zongshu);
                                            }
                                        }
                                        ssc.close();

                                    }else{
                                        Toast.makeText(getBaseContext(),"二维码出货记录已存在!",Toast.LENGTH_SHORT).show();
                                        qr_et.setText("");
                                        Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
                                        vibrator.vibrate(400);
                                        //播放错误信息
                                        mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                                        mediaPlayer.start();
                                    }
                                }
                                //是否一致
                            }
                            //本地删除
                            if(deletion_rb.isChecked()) {    // 条码是否已存在开始
                                // 条码是否已存在开始
                                Cursor chu_z = db.rawQuery(" select COUNT(*) as jilu from  t_shipment where code='"+ xcode + "'  ", null);
                                if (chu_z != null) {
                                    while (chu_z.moveToNext()) {
                                        record = chu_z.getString(chu_z.getColumnIndex("jilu"));
                                    }
                                }
                                chu_z.close();
                                String test1_chu = record;
                                if (Integer.parseInt(test1_chu) == 0) {
                                    Toast.makeText(getBaseContext(),"二维码不存在！",Toast.LENGTH_SHORT).show();
                                    qr_et.setText("");
                                    //播放错误信息
                                    mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                                    mediaPlayer.start();
                                }else{
                                    ShipmentDao shipmentDao = new ShipmentDao(getBaseContext());
                                    shipmentDao.DeleteShipment_ma(xcode);
                                    Toast.makeText(getBaseContext(),"本地删除成功！",Toast.LENGTH_SHORT).show();
                                    qr_et.setText("");

                                    // 本地入库的记录
                                    Cursor cz_r = db.rawQuery(" select count(*) as jilu  from  t_shipment ",null);
                                    if (cz_r != null) {
                                        while (cz_r.moveToNext()) {
                                            shuliang = cz_r.getString(cz_r.getColumnIndex("jilu"));
                                        }
                                    }
                                    cz_r.close();
                                    // 本地入库的记录结束
                                    qr_number_tv.setText(shuliang);// 离线扫描无就清空有的话不清
                                }
                            }

                        }
                    }

                return true;
            } else {
                return false;
            }
        }
    };

    public void  cpphah(){
        dbhelper = new DBHelper(getBaseContext());
        db = dbhelper.getReadableDatabase();

        // 下拉产品开始
        xs_number = ((Dict_Product) delivery_order_sp.getSelectedItem()).getText().toString();//销售订单id
        Cursor cp1 = db.rawQuery(" select t_product.sName,t_xorder.ID from t_xorder inner join t_product on t_xorder.sorderID = t_product.ID  where t_xorder.number='" + xs_number + "' ", null);
        List<Dict_Product> dicts1_cp = new ArrayList<Dict_Product>();
        if (cp1 != null) {
            while (cp1.moveToNext()) {
                int ID = Integer.parseInt(cp1.getString(cp1.getColumnIndex("ID")));
                String sName = cp1.getString(cp1.getColumnIndex("sName"));
                dicts1_cp.add(new Dict_Product(ID, sName));
            }
        }
        cp1.close();

        //初始化未弹出前
        ArrayAdapter<Dict_Product> adapter1_cp = new ArrayAdapter<Dict_Product>(
                this, android.R.layout.simple_gallery_item, dicts1_cp);

        //这个是弹出的字体
        adapter1_cp.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        //加载适配器
        product_name_sp.setAdapter(adapter1_cp);

        product_name_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String	xsID = ((Dict_Product) product_name_sp.getSelectedItem()).getId().toString();//销售订单id
                dbhelper = new DBHelper(getBaseContext());
                db = dbhelper.getReadableDatabase();

                // 产品名称,规格,交货数量
                Cursor  d = db.rawQuery(" select t_xorder.ID" + "," +
                        "t_xorder.xquantity," +
                        "t_product.ID as cpid," +
                        "t_product.sName," +
                        "t_product.standard," +
                        "t_customer.sName as kehu from t_xorder inner join t_product on t_xorder.sorderID = t_product.ID inner join t_customer on t_xorder.customerID = t_customer.ID where t_xorder.ID = " + xsID + " ", null);
                if (d != null) {
                    while (d.moveToNext()) {
                        String standard = d.getString(d.getColumnIndex("standard"));
                        String client = d.getString(d.getColumnIndex("kehu"));
                        String xdshul = d.getString(d.getColumnIndex("xquantity"));
                        specifications_et.setText(standard);
                        customer_name_et.setText(client);
                        delivered_number_et.setText(xdshul);
                    }
                }
                d.close();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                String sInfo = "什么也没有选!";
                Toast.makeText(getApplicationContext(),sInfo,Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.shipment_storagetime_et:
                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(ShipmentActivity.this, DateTime);
                dateTimePicKDialog.dateTimePicKDialog(shipping_time_et);
                break;

        }
    }

    /***
     * 获取url 指定name的value;
     * @param url 网址
     * @param name 参数名
     * @return
     */
    public static String getValueByName(String url, String name) {
        String result = "";
        int index = url.indexOf("?");
        String temp = url.substring(index + 1);
        String[] keyValue = temp.split("&");
        for (String str : keyValue) {
            if (str.contains(name)) {
                result = str.replace(name + "=", "");
                break;
            }
        }
        return result;
    }

    // 判断字符串是否为数字
    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

}
