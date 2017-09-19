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
import com.huiyi.huiyifan.db.StorageDao;
import com.huiyi.huiyifan.modle.Dict_Product;
import com.huiyi.huiyifan.util.AppManager;
import com.huiyi.huiyifan.util.DBHelper;
import com.huiyi.huiyifan.util.DateTimePickDialogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by LW on 2017/7/27.
 * 入库管理
 */

public class StorageActivity extends TopBarBaseActivity implements View.OnClickListener{

    protected static final String DateTime = null;
    private Spinner product_order_sp;//生产订单号
    private EditText dentification_mark_et;//产品批号
    private EditText product_name_et;//产品名称
    private EditText product_specifications_et;//产品规格
    private EditText package_size_first_et,package_size_second_et,package_size_three_et,package_size_four_et,package_size_five_et;//包装比例
    private EditText storage_time_et;//入库时间
    private RadioGroup radioGroup;//复选框
    private RadioButton offline_add_rb;//离线添加
    private RadioButton deletion_rb;//本地删除
    private EditText qr_et;//二维码
    private TextView total_tv,total_second_tv;//包装总数
    private TextView storage_number_tv;//入库数量
    private String ServiceUrl;//url
    private DBHelper dbhelper;
    private SQLiteDatabase db;
    private String count;
    private String producer_id, sorderID, xcode, dateTime, quantity, producerID;//表元素
    private String record,pcode="",kk="1";
    public int xma_record = 0, zjl = 0, djl = 0, zz = 0,zhi = 0;
    private MediaPlayer mediaPlayer;

    //UI布局
    @Override
    protected int getContentView() {
        AppManager.getAppManager().addActivity(this);
        Intent intent = getIntent();
        producer_id = intent.getStringExtra("producer_id");
        return R.layout.activity_storage;
    }
    //自定义Toolbar
    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        setTitle("入库管理");
        setLeftButton(R.drawable.back, new OnClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(StorageActivity.this,MainActivity.class);
                intent.putExtra("producer_id",producer_id);
                startActivity(intent);

            }
        });
    }

    //
    private void initView() {
        product_order_sp = (Spinner) findViewById(R.id.storage_product_order_sp);
        dentification_mark_et = (EditText) findViewById(R.id.storage_identification_et);
        product_name_et = (EditText) findViewById(R.id.storage_productname_et);
        product_specifications_et = (EditText) findViewById(R.id.storage_specifications_et);
        package_size_first_et = (EditText) findViewById(R.id.package_size_first_et);
        package_size_second_et = (EditText) findViewById(R.id.package_size_second_et);
        package_size_three_et = (EditText) findViewById(R.id.package_size_three_et);
        package_size_four_et = (EditText) findViewById(R.id.package_size_four_et);
        package_size_five_et = (EditText) findViewById(R.id.package_size_five_et);
        storage_time_et = (EditText) findViewById(R.id.storage_storagetime_et);
        radioGroup = (RadioGroup) findViewById(R.id.storage_rg);
        offline_add_rb = (RadioButton) findViewById(R.id.storage_add_rb);
        deletion_rb = (RadioButton) findViewById(R.id.storage_deletion_rb);
        qr_et = (EditText) findViewById(R.id.storage_qr_et);
        total_tv = (TextView) findViewById(R.id.storage_package_total_tv);
        total_second_tv = (TextView) findViewById(R.id.storage_package_total_second_tv);
        storage_number_tv = (TextView) findViewById(R.id.storage_number_tv);


        //获取接口
        AboutDao aboutDao = new AboutDao(getBaseContext());
        ServiceUrl = aboutDao.AboutCha();
        //获取数据库
        dbhelper = new DBHelper(getBaseContext());
        db = dbhelper.getReadableDatabase();


        // Spinner生产订单号
        Cursor cursor_p = db.rawQuery("select * from t_sorder ", null);
        List<Dict_Product> dicts1 = new ArrayList<>();
        if (cursor_p != null) {
            while (cursor_p.moveToNext()) {
                int ID = Integer.parseInt(cursor_p.getString(cursor_p.getColumnIndex("ID")));
                String number = cursor_p.getString(cursor_p.getColumnIndex("number"));
                dicts1.add(new Dict_Product(ID, number));
            }
        }
        cursor_p.close();

        //Adapter适配器
        ArrayAdapter<Dict_Product> adapter_d = new ArrayAdapter<Dict_Product>(this,android.R.layout.simple_spinner_item,dicts1);
        //设置样式
        adapter_d.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        //加载适配器
        product_order_sp.setAdapter(adapter_d);
        //每一项
        product_order_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,View view, int position, long id) {
                //给每一项赋值
                String ID = ((Dict_Product) product_order_sp.getSelectedItem()).getId().toString();// 生产订单id
                dbhelper = new DBHelper(getBaseContext());
                db = dbhelper.getReadableDatabase();
                // 产品批号
                Cursor cursor_mark = db.rawQuery(" select pihao,wu,si,da,zhong,xiao from  t_sorder where ID=" + ID + " ", null);
                if (cursor_mark != null) {
                    while (cursor_mark.moveToNext()) {
                        String number = cursor_mark.getString(cursor_mark.getColumnIndex("pihao"));
                        String five_qr = cursor_mark.getString(cursor_mark.getColumnIndex("wu"));
                        String four_qr = cursor_mark.getString(cursor_mark.getColumnIndex("si"));
                        String large_qr = cursor_mark.getString(cursor_mark.getColumnIndex("da"));
                        String middle_qr = cursor_mark.getString(cursor_mark.getColumnIndex("zhong"));
                        String small_qr = cursor_mark.getString(cursor_mark.getColumnIndex("xiao"));

                        dentification_mark_et.setText(number);
                        package_size_five_et.setText(five_qr);
                        package_size_four_et.setText(four_qr);
                        package_size_three_et.setText(large_qr);
                        package_size_second_et.setText(middle_qr);
                        package_size_first_et.setText(small_qr);
                    }
                }
                cursor_mark.close();

                // 产品名称、规格
                Cursor cursor_d = db.rawQuery(" select * from t_product where ID = (select productID from t_sorder where ID =" + ID + ") ", null);
                if (cursor_d != null) {
                    while (cursor_d.moveToNext()) {
                        String sName = cursor_d.getString(cursor_d.getColumnIndex("sName"));
                        String standard = cursor_d.getString(cursor_d.getColumnIndex("standard"));
                        product_name_et.setText(sName);//产品名称
                        product_specifications_et.setText(standard);//
                    }
                }
                cursor_d.close();

                // 包装比例
                String five_bl = package_size_five_et.getText().toString().trim();// 五级码
                String four_bl = package_size_four_et.getText().toString().trim();// 四级码
                String large_bl = package_size_three_et.getText().toString().trim();// 大码
                String middle_bl = package_size_second_et.getText().toString().trim();// 中码
                String small_bl = package_size_first_et.getText().toString().trim();// 小码
                String qr_bl = qr_et.getText().toString().trim();// 二维码

                if (Integer.parseInt(small_bl) == 1) {
                    total_tv.setText("0");
                    total_second_tv.setText("0");
                    //大小标记
                } else if (Integer.parseInt(small_bl) != 1
                        && Integer.parseInt(middle_bl) != 0
                        && Integer.parseInt(large_bl) == 0
                        && Integer.parseInt(four_bl) == 0
                        && Integer.parseInt(five_bl) == 0) {
                    //大中小标记
                } else if (Integer.parseInt(small_bl) != 1
                        && Integer.parseInt(middle_bl) != 0
                        && Integer.parseInt(large_bl) != 0
                        && Integer.parseInt(four_bl) == 0
                        && Integer.parseInt(five_bl) == 0){
                }

                    // 箱总数
                Cursor cursor_cases = db.rawQuery(" select count(distinct dcode) as jilu  from  t_storage where sorderID =" + ID + " ", null);
                if (cursor_cases != null) {
                    while (cursor_cases.moveToNext()) {
                        String total_cases = cursor_cases.getString(cursor_cases.getColumnIndex("jilu"));
                        total_tv.setText(total_cases);
                    }
                }
                cursor_cases.close();
                // 箱总数

                // 包装总数
                Cursor cursor_packing = db.rawQuery(" select count(*) as jilu  from  t_storage where sorderID=" + ID + " ", null);
                if (cursor_packing != null) {
                    while (cursor_packing.moveToNext()) {
                        String zongshu = cursor_packing.getString(cursor_packing.getColumnIndex("jilu"));
                        total_second_tv.setText(zongshu);
                    }
                }
                cursor_packing.close();
                // 包装总数
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                String sInfo = "什么也没有选!";
                Toast.makeText(getApplicationContext(),sInfo,Toast.LENGTH_SHORT).show();
            }
        });

        //获取入库时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        storage_time_et.setText(str);
        // 时间文本框触发开始
        storage_time_et.setOnClickListener(this);
        // 离线入库的记录
        Cursor cz_ru = db.rawQuery(" select count(*) as jilu  from  t_storage where status = 1 ", null);
        if (cz_ru != null) {
            while (cz_ru.moveToNext()) {
                count = cz_ru.getString(cz_ru.getColumnIndex("jilu"));
            }
        }
        cz_ru.close();

        storage_number_tv.setText(count);// 本地添加数量
        qr_et.requestFocus();// 获取光标
        qr_et.setOnKeyListener(onKey);

    }

    View.OnKeyListener onKey = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            // 是否是回车键 event.getAction() == KeyEvent.ACTION_DOWN是处理让他只执行一次
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

                    dbhelper = new DBHelper(getBaseContext());
                    db = dbhelper.getReadableDatabase();

                    try {
                        sorderID = ((Dict_Product) product_order_sp.getSelectedItem()).getId().toString();// 生产订单id
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    xcode = qr_et.getText().toString();// 二维码
                    dateTime = storage_time_et.getText().toString();// 入库时间
                    quantity = "1"; // 入库数量
                    producerID = producer_id;// 生产商id
                    if (xcode.trim().equals("") || xcode.trim() == null) {
                        Toast.makeText(getBaseContext(),"二维码不能为空！",Toast.LENGTH_SHORT).show();
                    }
                    else if (sorderID.trim().equals("0") || sorderID.trim() == null) {
                        Toast.makeText(getBaseContext(),"生产订单号不能为空！",Toast.LENGTH_SHORT).show();
                    } else {

                        final String wuma_bl = package_size_five_et.getText().toString().trim();// 五级码
                        final String sima_bl = package_size_four_et.getText().toString().trim();// 四级码
                        final String dma_bl = package_size_three_et.getText().toString().trim();// 大码
                        final String zma_bl = package_size_second_et.getText().toString().trim();// 中码
                        final String xma_bl = package_size_first_et.getText().toString().trim();// 小码
                        final String xma = qr_et.getText().toString().trim();// 二维码

                        if (wuma_bl.trim().equals("") || wuma_bl.trim() == null) {
                            Toast.makeText(getBaseContext(),"五级码比例不能为空！",Toast.LENGTH_SHORT).show();
                        } else if (sima_bl.trim().equals("") || sima_bl.trim() == null) {
                            Toast.makeText(getBaseContext(),"四级码比例不能为空！",Toast.LENGTH_SHORT).show();
                        } else if (dma_bl.trim().equals("") || dma_bl.trim() == null) {
                            Toast.makeText(getBaseContext(),"三级码比例不能为空！",Toast.LENGTH_SHORT).show();
                        } else if (zma_bl.trim().equals("") || zma_bl.trim() == null) {
                            Toast.makeText(getBaseContext(),"二级码比例不能为空！",Toast.LENGTH_SHORT).show();
                        } else if (xma_bl.trim().equals("") || xma_bl.trim() == null) {
                            Toast.makeText(getBaseContext(),"一级码比例不能为空！",Toast.LENGTH_SHORT).show();
                        }else {

                            //选择框
                            //离线添加
                            if (offline_add_rb.isChecked()){
                                //======判断二维码数字设定的产品与选中的入库产品是否一致===========================================
                                String canshuzhi_pd = getValueByName(xcode,
                                        "c");
                                if(canshuzhi_pd.length()==20){
                                    kk =canshuzhi_pd.substring(8, 11);
                                    String jjh= kk;
                                    Cursor cshif = db.rawQuery("select t_product.pcode from t_sorder inner join t_product on t_sorder.productID=t_product.ID where t_sorder.ID="+sorderID+"", null);
                                    if (cshif != null) {
                                        while (cshif.moveToNext()) {
                                            pcode = cshif.getString(cshif.getColumnIndex("pcode"));
                                        }
                                    }
                                    cshif.close();

                                }
                                if(canshuzhi_pd.length()==19){
                                    kk =canshuzhi_pd.substring(7, 10);
                                    Cursor cshif = db.rawQuery("select t_product.pcode from t_sorder inner join t_product on t_sorder.productID=t_product.ID where t_sorder.ID="+sorderID+"", null);
                                    if (cshif != null) {
                                        while (cshif.moveToNext()) {
                                            pcode = cshif.getString(cshif
                                                    .getColumnIndex("pcode"));
                                        }
                                    }
                                    cshif.close();

                                }
                                if(canshuzhi_pd == null || canshuzhi_pd.equals("")){
                                    Toast.makeText(getBaseContext(),"二维码格式不正确！",Toast.LENGTH_SHORT).show();
                                    Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
                                    vibrator.vibrate(400);
                                    qr_et.setText("");
                                }else if(isNumeric(canshuzhi_pd)==false){
                                    Toast.makeText(getBaseContext(),"二维码格式不正确！",Toast.LENGTH_SHORT).show();
                                    Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
                                    vibrator.vibrate(400);
                                    qr_et.setText("");
                                }else if (kk.equals(pcode)==false){
                                    Toast.makeText(getBaseContext(),"扫描失败，入库的产品与生产订单的产品不一致！",Toast.LENGTH_SHORT).show();
                                    qr_et.setText("");
                                    Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
                                    vibrator.vibrate(400);
                                    mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                                    mediaPlayer.start();
                                }
                                else{

                                    //单个包装
                                    if (Integer.parseInt(xma_bl) == 1) {
                                        try {
                                            // 二维码是否已存在开始
                                            Cursor cz_ru = db.rawQuery(
                                                    " select count(*) as jilu  from  t_storage where  xcode='"
                                                            + xcode + "'  or zcode='"
                                                            + xcode + "'  or dcode='"
                                                            + xcode + "'  or sicode='"
                                                            + xcode + "'  or wucode='"
                                                            + xcode + "'  ", null);
                                            if (cz_ru != null) {
                                                while (cz_ru.moveToNext()) {
                                                    record = cz_ru.getString(cz_ru
                                                            .getColumnIndex("jilu"));
                                                }
                                            }
                                            cz_ru.close();
                                            // 二维码是否已存在结束
                                            String test1 = record;
                                            if (Integer.parseInt(test1) == 0) {
                                                String canshuzhi = getValueByName(xcode, "c");
                                                if(canshuzhi==""){
                                                    Toast.makeText(getBaseContext(),"二维码格式不正确！",Toast.LENGTH_SHORT).show();
                                                    qr_et.setText("");
                                                    mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                                                    mediaPlayer.start();
                                                }else{
                                                    zhi = Integer.parseInt(canshuzhi
                                                            .substring(0, 1));// 截取s中从begin开始至end结束时的字符串，并将其赋值给s;
                                                    if (zhi == 1) {
                                                        StorageDao storageDao = new StorageDao(
                                                                getBaseContext());
                                                        storageDao.AddStorage(xcode, "0", "0", "0", "0", sorderID, quantity, "1", dateTime, producerID);
                                                        Toast.makeText(getBaseContext(),"离线添加成功！",Toast.LENGTH_SHORT).show();
                                                        qr_et.setText("");
                                                        // 离线添加的所有记录开始
                                                        Cursor lu = db.rawQuery(" select count(*) as jilu  from  t_storage where status=1 ", null);
                                                        if (lu != null) {
                                                            while (lu.moveToNext()) {
                                                                count = lu.getString(lu.getColumnIndex("jilu"));
                                                            }
                                                        }
                                                        lu.close();
                                                        String test2 = count;
                                                        // 离线添加的所有记录结束
                                                        storage_number_tv.setText(test2);// 离线添加数量
                                                    } else {

                                                        Toast.makeText(getBaseContext(),"请先做好关联！！",Toast.LENGTH_SHORT).show();
                                                        qr_et.setText("");
                                                        mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                                                        mediaPlayer.start();
                                                    }
                                                }
                                            } else {
                                                Toast.makeText(getBaseContext(),"二维码已存在！",Toast.LENGTH_SHORT).show();
                                                qr_et.setText("");
                                                Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
                                                vibrator.vibrate(400);
                                                mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                                                mediaPlayer.start();
                                            }
                                        } catch (NumberFormatException e) {
                                            Toast.makeText(getBaseContext(),"添加失败，发生异常！",Toast.LENGTH_SHORT).show();
                                            e.printStackTrace();
                                            mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                                            mediaPlayer.start();
                                        }

                                    } else {
                                        //大小包装
                                        if (Integer.parseInt(xma_bl) != 1
                                                && Integer.parseInt(zma_bl) != 0
                                                && Integer.parseInt(dma_bl) == 0
                                                && Integer.parseInt(sima_bl) == 0
                                                && Integer.parseInt(wuma_bl) == 0) {
                                            try {
                                                Toast.makeText(getBaseContext(),"你设置的是大小标比例！",Toast.LENGTH_SHORT).show();
                                                // 先判断数据库是否有小标，有的话是多少
                                                // 判断二维码是否已存在
                                                Cursor cz = db.rawQuery(
                                                        " select COUNT(*) as jilu from t_storage where  xcode='"+ xma + "'  or zcode='"+ xma + "'  or dcode='"+ xma + "'  or sicode='"+ xma + "'  or wucode='"+ xma + "'  ", null);
                                                if (cz != null) {
                                                    while (cz.moveToNext()) {
                                                        xma_record = cz.getInt(cz.getColumnIndex("jilu"));
                                                    }
                                                }
                                                cz.close();

                                                if (xma_record > 0) {
                                                    Toast.makeText(getBaseContext(),"二维码已存在！",Toast.LENGTH_SHORT).show();
                                                    qr_et.setText("");
                                                    Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
                                                    vibrator.vibrate(400);
                                                    mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                                                    mediaPlayer.start();
                                                } else {
                                                    String canshuzhi = getValueByName(xma,
                                                            "c");
                                                    if(canshuzhi==""){
                                                        Toast.makeText(getBaseContext(),"二维码格式不正确！",Toast.LENGTH_SHORT).show();
                                                        qr_et.setText("");
                                                        mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                                                        mediaPlayer.start();
                                                    }else{
                                                        int zhi = Integer.parseInt(canshuzhi
                                                                .substring(0, 1));// 截取s中从begin开始至end结束时的字符串，并将其赋值给s;
                                                        if (zhi == 1) {
                                                            // 判断是否有记录有就显示出来
                                                            try {
                                                                Cursor c = db.rawQuery(" select COUNT(*) as jilu from t_storage where zcode='0' and sorderID=" + sorderID + " ", null);
                                                                if (c != null) {
                                                                    while (c.moveToNext()) {
                                                                        xma_record = c.getInt(c.getColumnIndex("jilu"));
                                                                    }
                                                                }
                                                                c.close();
                                                                int ajilu = xma_record;
                                                                // 与比例相等则提示扫二级码
                                                                if (Integer.parseInt(xma_bl) == ajilu) {
                                                                    Toast.makeText(getBaseContext(),"小码已扫完成，请扫二级码！",Toast.LENGTH_SHORT).show();
                                                                    qr_et.setText("");
                                                                    Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
                                                                    vibrator.vibrate(new long[]{0,1000}, -1);

                                                                } else {
                                                                    StorageDao storageDao = new StorageDao(getBaseContext());
                                                                    storageDao.AddStorage(xcode, "0", "0", "0", "0", sorderID, quantity, "1", dateTime, producerID);
                                                                    Toast.makeText(getBaseContext(),"添加小码成功！",Toast.LENGTH_SHORT).show();
                                                                    qr_et.setText("");
                                                                    //再检查数据库是否存在与比例相等的小码
                                                                    Cursor c1 = db.rawQuery(
                                                                            " select COUNT(*) as jilu from t_storage where zcode='0' and sorderID=" + sorderID + " ", null);
                                                                    if (c1 != null) {
                                                                        while (c1.moveToNext()) {
                                                                            xma_record = c1.getInt(c1.getColumnIndex("jilu"));
                                                                        }
                                                                    }
                                                                    c1.close();
                                                                    int ajilu_c1 = xma_record;
                                                                    // 与比例相等则提示扫二级码
                                                                    if (Integer.parseInt(xma_bl) == ajilu_c1) {
                                                                        Toast.makeText(getBaseContext(),"小码已扫完成，请扫二级码！",Toast.LENGTH_SHORT).show();
                                                                        qr_et.setText("");
                                                                        Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
                                                                        vibrator.vibrate(new long[]{0,1000}, -1);
                                                                    }
                                                                }
                                                            } catch (Exception e1) {
                                                                e1.printStackTrace();
                                                            }

                                                        } else if (zhi == 2) {
                                                            // 先判断小标是否扫完后添加二级码
                                                            try {
                                                                Cursor c = db.rawQuery(
                                                                        " select COUNT(*) as jilu from t_storage where zcode='0' and sorderID=" + sorderID + " ", null);
                                                                if (c != null) {
                                                                    while (c.moveToNext()) {
                                                                        xma_record = c.getInt(c.getColumnIndex("jilu"));
                                                                    }
                                                                }
                                                                c.close();
                                                                int ajilu = xma_record;
                                                                // 与比例相等则扫二级码
                                                                if (Integer.parseInt(xma_bl) == ajilu) {
                                                                    // 添加二级码
                                                                    StorageDao storageDao = new StorageDao(
                                                                            getBaseContext());
                                                                    storageDao.UpdateStorage_er(xma, sorderID);
                                                                    Toast.makeText(getBaseContext(),"添加二级码成功！",Toast.LENGTH_SHORT).show();
                                                                    qr_et.setText("");
                                                                    //判断是否为0，那就是扫完二级码了
                                                                    Cursor d = db.rawQuery(" select COUNT(*) as jilu from t_storage where zcode='0' and sorderID=" + sorderID + " ", null);
                                                                    if (d != null) {
                                                                        while (d.moveToNext()) {
                                                                            xma_record = d.getInt(d.getColumnIndex("jilu"));
                                                                        }
                                                                    }
                                                                    d.close();
                                                                    int ajilu_d = xma_record;
                                                                    if (ajilu_d==0) {
                                                                        Toast.makeText(getBaseContext(),"二级码扫完，完成一箱！",Toast.LENGTH_SHORT).show();
                                                                        qr_et.setText("");
                                                                    }
                                                                } else {
                                                                    Toast.makeText(getBaseContext(),"请先扫完成小码！",Toast.LENGTH_SHORT).show();
                                                                    qr_et.setText("");
                                                                }

                                                            } catch (Exception e1) {
                                                                e1.printStackTrace();
                                                            }

                                                        } else {
                                                            Toast.makeText(getBaseContext(),"二维码格式不正确！",Toast.LENGTH_SHORT).show();
                                                            qr_et.setText("");
                                                            mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                                                            mediaPlayer.start();
                                                        }

                                                    }
                                                }

                                            } catch (NumberFormatException e) {
                                                Toast.makeText(getBaseContext(),"添加失败，发生异常！",Toast.LENGTH_SHORT).show();
                                                e.printStackTrace();
                                                mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                                                mediaPlayer.start();
                                            }
                                        }

                                        // 大中小包装
                                        else if (Integer.parseInt(xma_bl) != 1
                                                && Integer.parseInt(zma_bl) != 0
                                                && Integer.parseInt(dma_bl) != 0
                                                && Integer.parseInt(sima_bl) == 0
                                                && Integer.parseInt(wuma_bl) == 0) {
                                            try {
                                                Toast.makeText(getBaseContext(),"你设置的是大中小标比例！",Toast.LENGTH_SHORT).show();
                                                // 先判断数据库是否有小标，有的话是多少

                                                // 判断二维码是否已存在
                                                Cursor cz = db.rawQuery(
                                                        " select COUNT(*) as jilu from t_storage where  xcode='"+ xma + "'  or zcode='"+ xma + "'  or dcode='"+ xma + "'  or sicode='"+ xma + "'  or wucode='"+ xma + "'  ", null);
                                                if (cz != null) {
                                                    while (cz.moveToNext()) {
                                                        xma_record = cz.getInt(cz
                                                                .getColumnIndex("jilu"));
                                                    }
                                                }
                                                cz.close();
                                                if (xma_record > 0) {

                                                    Toast.makeText(getBaseContext(),"二维码已存在！",Toast.LENGTH_SHORT).show();
                                                    qr_et.setText("");
                                                    Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
                                                    vibrator.vibrate(400);
                                                    mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                                                    mediaPlayer.start();
                                                } else {
                                                    String canshuzhi = getValueByName(xma, "c");
                                                    if(canshuzhi==""){
                                                        Toast.makeText(getBaseContext(),"二维码格式不正确！",Toast.LENGTH_SHORT).show();
                                                        qr_et.setText("");
                                                        mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                                                        mediaPlayer.start();
                                                        return false;
                                                    }else{
                                                        int zhi = Integer.parseInt(canshuzhi.substring(0, 1));// 截取s中从begin开始至end结束时的字符串，并将其赋值给s;
                                                        if (zhi == 1) {
                                                            // 二级是否完成然后提示添加三级
                                                            try {
                                                                Cursor pd3ji = db.rawQuery(" select COUNT(*) as jilu from t_storage where  dcode='0' and zcode!='0' and sorderID=" + sorderID + " ", null);
                                                                if (pd3ji != null) {
                                                                    while (pd3ji.moveToNext()) {
                                                                        xma_record = pd3ji.getInt(pd3ji.getColumnIndex("jilu"));
                                                                    }
                                                                }
                                                                pd3ji.close();
                                                                int ajilu2 = xma_record;
                                                                // 与比例相等则扫三级码
                                                                int c3 = Integer.parseInt(zma_bl) * 2;
                                                                if (c3 == ajilu2) {
                                                                    Toast.makeText(getBaseContext(),"二级码已扫描完成，请添加三级码！",Toast.LENGTH_SHORT).show();
                                                                    qr_et.setText("");
                                                                    Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
                                                                    vibrator.vibrate(new long[]{0,1000}, -1);
                                                                } else {
                                                                    // 判断是否有记录有就显示出来
                                                                    try {
                                                                        Cursor pd2ji = db
                                                                                .rawQuery(" select COUNT(*) as jilu from t_storage where zcode='0' and sorderID=" + sorderID + " ", null);
                                                                        if (pd2ji != null) {
                                                                            while (pd2ji.moveToNext()) {
                                                                                xma_record = pd2ji.getInt(pd2ji.getColumnIndex("jilu"));
                                                                            }
                                                                        }
                                                                        pd2ji.close();
                                                                        int ajilu1 = xma_record;
                                                                        // 与比例相等则提示扫二级码
                                                                        if (Integer.parseInt(xma_bl) == ajilu1) {
                                                                            Toast.makeText(getBaseContext(),"小码已扫完成，请扫二级码！",Toast.LENGTH_SHORT).show();
                                                                            qr_et.setText("");
                                                                            Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
                                                                            vibrator.vibrate(new long[]{0,1000}, -1);
                                                                        } else {
                                                                            StorageDao storageDao = new StorageDao(getBaseContext());
                                                                            storageDao.AddStorage(xcode, "0", "0", "0", "0", sorderID, quantity, "1", dateTime, producerID);

                                                                            Toast.makeText(getBaseContext(),"添加小码成功！",Toast.LENGTH_SHORT).show();
                                                                            qr_et.setText("");
                                                                            // 后直接提示
                                                                            Cursor pd2jih = db
                                                                                    .rawQuery(" select COUNT(*) as jilu from t_storage where zcode='0' and sorderID=" + sorderID + " ", null);
                                                                            if (pd2jih != null) {
                                                                                while (pd2jih.moveToNext()) {
                                                                                    xma_record = pd2jih.getInt(pd2jih.getColumnIndex("jilu"));
                                                                                }
                                                                            }
                                                                            pd2ji.close();
                                                                            int ajilu1h = xma_record;
                                                                            if (Integer
                                                                                    .parseInt(xma_bl) == ajilu1h) {
                                                                                Toast.makeText(getBaseContext(),"小码已扫完成，请扫二级码！",Toast.LENGTH_SHORT).show();
                                                                                qr_et.setText("");
                                                                                Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
                                                                                vibrator.vibrate(new long[]{0,1000}, -1);
                                                                            }
                                                                        }

                                                                    } catch (Exception e1) {
                                                                        e1.printStackTrace();
                                                                    }
                                                                }

                                                            } catch (Exception e1) {
                                                                e1.printStackTrace();
                                                            }
                                                        } else if (zhi == 2) {
                                                            // 二级是否完成然后提示添加三级
                                                            try {
                                                                Cursor pd3ji = db
                                                                        .rawQuery(" select COUNT(*) as jilu from t_storage where dcode='0' and zcode!='0' and sorderID=" + sorderID + " ", null);
                                                                if (pd3ji != null) {
                                                                    while (pd3ji.moveToNext()) {
                                                                        xma_record = pd3ji.getInt(pd3ji.getColumnIndex("jilu"));
                                                                    }
                                                                }
                                                                pd3ji.close();
                                                                int ajilu2 = xma_record;
                                                                // 与比例相等则扫三级码
                                                                if (Integer.parseInt(zma_bl) * 2 == ajilu2) {
                                                                    Toast.makeText(getBaseContext(),"二级码已扫描完成，请添加三级码！",Toast.LENGTH_SHORT).show();
                                                                    qr_et.setText("");
                                                                    Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
                                                                    vibrator.vibrate(new long[]{0,1000}, -1);
                                                                } else {

                                                                    // 先判断小标是否扫完后添加二级码开始====================================================
                                                                    try {
                                                                        Cursor pd2ji = db
                                                                                .rawQuery(" select COUNT(*) as jilu from t_storage where zcode='0' and sorderID=" + sorderID + " ", null);
                                                                        if (pd2ji != null) {
                                                                            while (pd2ji.moveToNext()) {
                                                                                xma_record = pd2ji.getInt(pd2ji.getColumnIndex("jilu"));
                                                                            }
                                                                        }
                                                                        pd2ji.close();
                                                                        int ajilu1 = xma_record;
                                                                        // 与比例相等则扫二级码
                                                                        if (Integer.parseInt(xma_bl) == ajilu1) {
                                                                            // 添加二级码
                                                                            StorageDao storageDao = new StorageDao(
                                                                                    getBaseContext());
                                                                            storageDao.UpdateStorage_er(xma, sorderID);
                                                                            Toast.makeText(getBaseContext(),"添加二级码成功！",Toast.LENGTH_SHORT).show();
                                                                            qr_et.setText("");
                                                                            // =======================
                                                                            Cursor pd3jih = db
                                                                                    .rawQuery(" select COUNT(*) as jilu from t_storage where dcode='0' and zcode!='0' and sorderID=" + sorderID + " ", null);
                                                                            if (pd3jih != null) {
                                                                                while (pd3jih.moveToNext()) {
                                                                                    xma_record = pd3jih.getInt(pd3jih.getColumnIndex("jilu"));
                                                                                }
                                                                            }
                                                                            pd3jih.close();
                                                                            int ajilu2h = xma_record;
                                                                            // 与比例相等则扫三级码
                                                                            if (Integer.parseInt(zma_bl) * 2 == ajilu2h) {
                                                                                Toast.makeText(getBaseContext(),"二级码已扫描完成，请添加三级码！",Toast.LENGTH_SHORT).show();
                                                                                qr_et.setText("");
                                                                                Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
                                                                                vibrator.vibrate(new long[]{0,1000}, -1);
                                                                            } else {
                                                                                Toast.makeText(getBaseContext(),"二级码已扫描完成，请继续添加小码！！",Toast.LENGTH_SHORT).show();
                                                                                qr_et.setText("");
                                                                                Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
                                                                                vibrator.vibrate(new long[]{0,1000}, -1);
                                                                            }
                                                                            // 与比例相等则扫三级码
                                                                        } else {

                                                                            Toast.makeText(getBaseContext(),"请先扫完成小码！",Toast.LENGTH_SHORT).show();
                                                                            qr_et.setText("");
                                                                        }

                                                                    } catch (Exception e1) {
                                                                        e1.printStackTrace();
                                                                    }

                                                                }

                                                            } catch (Exception e1) {

                                                                e1.printStackTrace();
                                                            }
                                                        } else if (zhi == 3) {

                                                            // 先判断二级码是否扫完后添加三级码
                                                            try {
                                                                Cursor c = db
                                                                        .rawQuery(" select COUNT(*) as jilu from t_storage where dcode='0' and zcode!='0' and sorderID=" + sorderID + " ", null);
                                                                if (c != null) {
                                                                    while (c.moveToNext()) {
                                                                        xma_record = c.getInt(c.getColumnIndex("jilu"));
                                                                    }
                                                                }
                                                                c.close();
                                                                int ajilu = xma_record;
                                                                // 与比例相等则扫三级码
                                                                if (Integer.parseInt(zma_bl) * 2 == ajilu) {
                                                                    // 添加三级码
                                                                    StorageDao storageDao = new StorageDao(getBaseContext());
                                                                    storageDao.UpdateStorage_san(xma, sorderID);

                                                                    Toast.makeText(getBaseContext(),"添加三级码成功,完成一箱！",Toast.LENGTH_SHORT).show();
                                                                    qr_et.setText("");
                                                                } else {

                                                                    Toast.makeText(getBaseContext(),"请先扫完成二级码！",Toast.LENGTH_SHORT).show();
                                                                    qr_et.setText("");
                                                                }

                                                            } catch (Exception e1) {
                                                                e1.printStackTrace();
                                                            }

                                                        } else {
                                                            Toast.makeText(getBaseContext(),"二维码格式不正确！",Toast.LENGTH_SHORT).show();
                                                            qr_et.setText("");
                                                            mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                                                            mediaPlayer.start();
                                                        }

                                                    }
                                                }

                                            } catch (NumberFormatException e) {

                                                Toast.makeText(getBaseContext(),"添加失败，发生异常！",Toast.LENGTH_SHORT).show();
                                                e.printStackTrace();
                                                mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                                                mediaPlayer.start();
                                            }

                                        }

                                        // 四大中小标
                                        else if (Integer.parseInt(xma_bl) != 1
                                                && Integer.parseInt(zma_bl) != 0
                                                && Integer.parseInt(dma_bl) != 0
                                                && Integer.parseInt(sima_bl) != 0
                                                && Integer.parseInt(wuma_bl) == 0) {
                                            Toast.makeText(getBaseContext(),"你设置的是四大中小标比例！",Toast.LENGTH_SHORT).show();
                                        }
                                        // 五四大中小标
                                        else if (Integer.parseInt(xma_bl) != 1
                                                && Integer.parseInt(zma_bl) != 0
                                                && Integer.parseInt(dma_bl) != 0
                                                && Integer.parseInt(sima_bl) != 0
                                                && Integer.parseInt(wuma_bl) != 0) {
                                            Toast.makeText(getBaseContext(),"你设置的是五四大中小标比例！",Toast.LENGTH_SHORT).show();
                                        } else {
                                            qr_et.setText("比例没设置好！");
                                        }
                                    }

                                    if (Integer.parseInt(xma_bl) == 1) {
                                        total_tv.setText("0");
                                        total_second_tv.setText("0");
                                    }
                                    // 大小标
                                    else if (Integer.parseInt(xma_bl) != 1
                                            && Integer.parseInt(zma_bl) != 0
                                            && Integer.parseInt(dma_bl) == 0
                                            && Integer.parseInt(sima_bl) == 0
                                            && Integer.parseInt(wuma_bl) == 0) {
                                        // 箱总数
                                        Cursor ssc = db.rawQuery(" select count(DISTINCT zcode) as jilu  from  t_storage where  sorderID=" + sorderID + " ", null);
                                        if (ssc != null) {
                                            while (ssc.moveToNext()) {
                                                String xiangshu = ssc.getString(ssc.getColumnIndex("jilu"));
                                                total_tv.setText(xiangshu);
                                            }
                                        }
                                        ssc.close();
                                        // 箱总数
                                        // 离线入库的记录开始
                                        Cursor cz_ru = db.rawQuery(" select count(*) as jilu  from  t_storage where status=1 ", null);
                                        if (cz_ru != null) {
                                            while (cz_ru.moveToNext()) {
                                                count = cz_ru.getString(cz_ru.getColumnIndex("jilu"));
                                            }
                                        }
                                        cz_ru.close();
                                        // 离线入库的记录结束
                                        storage_number_tv.setText(count);// 本地添加数量
                                    }

                                    // 大中小标
                                    else if (Integer.parseInt(xma_bl) != 1
                                            && Integer.parseInt(zma_bl) != 0
                                            && Integer.parseInt(dma_bl) != 0
                                            && Integer.parseInt(sima_bl) == 0
                                            && Integer.parseInt(wuma_bl) == 0) {
                                        // 箱总数
                                        Cursor ssc = db.rawQuery(" select count(DISTINCT dcode) as jilu  from  t_storage where sorderID=" + sorderID + " ", null);
                                        if (ssc != null) {
                                            while (ssc.moveToNext()) {
                                                String xiangshu = ssc.getString(ssc.getColumnIndex("jilu"));
                                                total_tv.setText(xiangshu);
                                            }
                                        }
                                        ssc.close();
                                        // 箱总数
                                    }

                                    // 包装总数
                                    Cursor ssc = db.rawQuery(
                                            " select count(*) as jilu  from  t_storage where sorderID="
                                                    + sorderID + " ", null);
                                    if (ssc != null) {
                                        while (ssc.moveToNext()) {
                                            String zongshu = ssc.getString(ssc
                                                    .getColumnIndex("jilu"));
                                            total_second_tv.setText(zongshu);
                                        }
                                    }
                                    ssc.close();
                                    // 包装总数
                                    // 离线入库的记录开始
                                    Cursor cz_ru = db.rawQuery(" select count(*) as jilu  from  t_storage where status=1 ", null);
                                    if (cz_ru != null) {
                                        while (cz_ru.moveToNext()) {
                                            count = cz_ru.getString(cz_ru.getColumnIndex("jilu"));
                                        }
                                    }
                                    cz_ru.close();
                                    // 离线入库的记录结束

                                    storage_number_tv.setText(count);// 本地添加数量
                                }
                            }
                                    //本地删除
                                    if(deletion_rb.isChecked()) {
                                        // 大小包装
                                        if (Integer.parseInt(xma_bl) != 1
                                                && Integer.parseInt(zma_bl) != 0
                                                && Integer.parseInt(dma_bl) == 0
                                                && Integer.parseInt(sima_bl) == 0
                                                && Integer.parseInt(wuma_bl) == 0) {
                                            Cursor cz = db.rawQuery(" select count(*) as jilu from t_storage where  zcode != '0' ", null);
                                            if (cz != null) {
                                                while (cz.moveToNext()) {
                                                    zjl = cz.getInt(cz.getColumnIndex("jilu"));
                                                }
                                            }
                                            cz.close();
                                        }
                                        // 大中小包装
                                        if (Integer.parseInt(xma_bl) != 1
                                                && Integer.parseInt(zma_bl) != 0
                                                && Integer.parseInt(dma_bl) != 0
                                                && Integer.parseInt(sima_bl) == 0
                                                && Integer.parseInt(wuma_bl) == 0) {
                                            Cursor cz = db.rawQuery(" select count(*) as jilu from t_storage where dcode != '0' ", null);
                                            if (cz != null) {
                                                while (cz.moveToNext()) {
                                                    djl = cz.getInt(cz.getColumnIndex("jilu"));
                                                }
                                            }
                                            cz.close();
                                        }

                                        String canshuzhi = getValueByName(xma, "c");
                                        if(canshuzhi==""){
                                            Toast.makeText(getBaseContext(),"二维码格式不正确！",Toast.LENGTH_SHORT).show();
                                            qr_et.setText("");

                                        }else{
                                            zz= Integer.parseInt(canshuzhi.substring(0, 1));// 截取s中从begin开始至end结束时的字符串，并将其赋值给s;
                                        }

                                        if (zjl > 0 && zz != 2) {
                                            Toast.makeText(getBaseContext(),"已关联好的一整箱不能删除小码，只能删除整箱的大码！",Toast.LENGTH_SHORT).show();
                                            qr_et.setText("");
                                        } else if (djl > 0 && zz != 3) {
                                            Toast.makeText(getBaseContext(),"已关联好的一整箱不能删除小码，只能删除整箱的大码！",Toast.LENGTH_SHORT).show();
                                            qr_et.setText("");
                                        } else {
                                            // 判断二维码是否已存在
                                            Cursor cz = db.rawQuery(
                                                    " select count(*) as jilu from t_storage where  " +
                                                            "xcode='" + xma + "'  or zcode='" + xma + "' " +
                                                            " or dcode='" + xma + "'  or sicode='" + xma + "' " +
                                                            " or wucode='" + xma + "'  ", null);
                                            if (cz != null) {
                                                while (cz.moveToNext()) {
                                                    xma_record = cz.getInt(cz.getColumnIndex("jilu"));
                                                }
                                            }
                                            cz.close();
                                            if (xma_record == 0) {
                                                Toast.makeText(getBaseContext(),"二维码不存在！",Toast.LENGTH_SHORT).show();
                                                qr_et.setText("");
                                                //播放错误信息
                                                mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                                                mediaPlayer.start();
                                            } else {
                                                StorageDao storageDao = new StorageDao(getBaseContext());
                                                storageDao.DeleteStorage_ma(xcode);
                                                // 箱总数
                                                Cursor ssc = db.rawQuery(" select count(distinct dcode) as jilu from t_storage where sorderID=" + sorderID + " ", null);
                                                if (ssc != null) {
                                                    while (ssc.moveToNext()) {
                                                        String xiangshu = ssc.getString(ssc.getColumnIndex("jilu"));
                                                        total_tv.setText(xiangshu);
                                                    }
                                                }
                                                ssc.close();
                                                // 箱总数
                                                // 包装总数
                                                Cursor ssc2 = db.rawQuery(" select count(*) as jilu from t_storage where sorderID =" + sorderID + " ", null);
                                                if (ssc2 != null) {
                                                    while (ssc2.moveToNext()) {
                                                        String zongshu = ssc2.getString(ssc.getColumnIndex("jilu"));
                                                        total_second_tv.setText(zongshu);
                                                    }
                                                }
                                                ssc2.close();
                                                // 包装总数
                                                // 离线入库的记录开始
                                                Cursor cz_ru = db.rawQuery(" select count(*) as jilu  from  t_storage where status = 1 ", null);
                                                if (cz_ru != null) {
                                                    while (cz_ru.moveToNext()) {
                                                        count = cz_ru.getString(cz_ru.getColumnIndex("jilu"));
                                                    }
                                                }
                                                cz_ru.close();
                                                // 离线入库的记录结束
                                                total_tv.setText(count);// 本地添加数量
                                                Toast.makeText(getBaseContext(),"本地删除成功！",Toast.LENGTH_SHORT).show();
                                                qr_et.setText("");
                                            }
                                        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.storage_storagetime_et:
                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(StorageActivity.this, DateTime);
                dateTimePicKDialog.dateTimePicKDialog(storage_time_et);
                break;
        }
    }

    /***
     * 获取url 指定name的value;
     *
     * @param url
     *            网址
     * @param name
     *            参数名
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
