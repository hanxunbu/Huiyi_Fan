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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.huiyi.huiyifan.R;
import com.huiyi.huiyifan.base.TopBarBaseActivity;
import com.huiyi.huiyifan.db.AboutDao;
import com.huiyi.huiyifan.db.RefundDao;
import com.huiyi.huiyifan.util.AppManager;
import com.huiyi.huiyifan.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by LW on 2017/7/29.
 * 退货管理
 */
public class RefundActivity extends TopBarBaseActivity{

    private EditText specifications;//退货原因
    private EditText return_time;//退货时间
    private RadioGroup radioGroup;
    private RadioButton offline_rb;//离线出货
    private RadioButton deletion_rb;//本地删除
    private EditText bar_code;//条码
    private TextView qr_number;//离线扫描数量
    private String ServiceUrl;//url
    private DBHelper dbhelper;
    private SQLiteDatabase db;
    private String count = "0";//扫描总数量
    private String record = "0";//原因数量
    private String producer_id;
    private MediaPlayer mediaPlayer;

    //UI布局
    @Override
    protected int getContentView() {
        AppManager.getAppManager().addActivity(this);
        Intent intent = getIntent();
        producer_id = intent.getStringExtra("producer_id");

        return R.layout.activity_refund;
    }

    //自定义Toolbar
    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        setTitle("退货管理");
        setLeftButton(R.drawable.back, new OnClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(RefundActivity.this,MainActivity.class);
                intent.putExtra("producer_id",producer_id);
                startActivity(intent);
            }
        });
    }

    //初始化
    private void initView() {
        specifications = (EditText) findViewById(R.id.refund_specifications_et);
        return_time = (EditText) findViewById(R.id.refund_return_time_et);
        radioGroup = (RadioGroup) findViewById(R.id.refund_rg);
        offline_rb = (RadioButton) findViewById(R.id.refund_offline_rb);
        deletion_rb = (RadioButton) findViewById(R.id.refund_deletion_rb);
        bar_code = (EditText) findViewById(R.id.refund_bar_code_et);
        qr_number = (TextView) findViewById(R.id.refund_qr_number_tv);

        //获取接口
        AboutDao aboutDao = new AboutDao(getBaseContext());
        ServiceUrl = aboutDao.AboutCha();
        //获取数据库
        dbhelper = new DBHelper(getBaseContext());
        db = dbhelper.getReadableDatabase();

        // 离线入库的记录结束
        qr_number.setText(count);// 本地添加数量
        bar_code.requestFocus();// 获取光标
        // 触发回车键
        bar_code.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // 是否是回车键 event.getAction() == KeyEvent.ACTION_DOWN是处理让他只执行一次
                if (keyCode == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

                        dbhelper = new DBHelper(getBaseContext());
                        db = dbhelper.getReadableDatabase();

                        final String reason = specifications.getText().toString();// 退货原因

                        // 退货时间
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                        Date xs_shijian1 = addOneSecond(curDate);//当前时间加一秒
                        String str = sdf.format(xs_shijian1);  //日期转字符串
                        final String dateTime = str;
                        return_time.setText(dateTime);

                        // 条码
                        String ma = bar_code.getText().toString();

                        if (reason.trim().equals("") || reason.trim() == null) {
                            Toast.makeText(getBaseContext(), "退货原因不能为空！", Toast.LENGTH_SHORT).show();
                            specifications.setText("");
                        } else if (dateTime.trim().equals("") || dateTime.trim() == null) {
                            Toast.makeText(getBaseContext(), "退货日期不能为空！", Toast.LENGTH_SHORT).show();
                            return_time.setText("");
                        } else if (ma.trim().equals("") || ma.trim() == null) {
                            Toast.makeText(getBaseContext(), "条码不能为空！", Toast.LENGTH_SHORT).show();
                            bar_code.setText("");
                        } else {
                            String canshuzhi_pd = getValueByName(ma, "c");
                            if (canshuzhi_pd == null || canshuzhi_pd.equals("")) {
                                Toast.makeText(getBaseContext(), "二维码格式不正确！", Toast.LENGTH_SHORT).show();
                                bar_code.setText("");
                                //播放错误信息
                                mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                                mediaPlayer.start();
                            } else {
                                // 条码是否已存在开始
                                Cursor ru_z = db.rawQuery(" select count(*) as jilu from t_refund where t_xscode='"
                                                + ma + "'   ", null);
                                if (ru_z != null) {
                                    while (ru_z.moveToNext()) {
                                        record = ru_z.getString(ru_z.getColumnIndex("jilu"));
                                        String test1 = record;
                                    }
                                }
                                ru_z.close();

                                //离线出货
                                if (offline_rb.isChecked()) {
                                    if (Integer.parseInt(record) == 0) {
                                        RefundDao refundDao = new RefundDao(getBaseContext());
                                        refundDao.AddRefund(ma, dateTime, reason);
                                        Toast.makeText(getBaseContext(), "离线出货成功！", Toast.LENGTH_SHORT).show();
                                        bar_code.setText("");
                                    } else {
                                        Toast.makeText(getBaseContext(), "条码已存在！", Toast.LENGTH_SHORT).show();
                                        bar_code.setText("");
                                        Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                                        vibrator.vibrate(new long[]{0, 1000}, -1);
                                        //播放错误信息
                                        mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                                        mediaPlayer.start();
                                    }
                                }
                                //本地删除
                                if (deletion_rb.isChecked()) {
                                    if (Integer.parseInt(record) != 0) {
                                        RefundDao refundDao = new RefundDao(getBaseContext());
                                        refundDao.DeleteRefund_ma(ma);
                                        Toast.makeText(getBaseContext(), "本地删除成功！", Toast.LENGTH_SHORT).show();
                                        bar_code.setText("");

                                    } else {
                                        Toast.makeText(getBaseContext(), "条码不存在！", Toast.LENGTH_SHORT).show();
                                        bar_code.setText("");
                                        Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                                        vibrator.vibrate(new long[]{0, 1000}, -1);
                                        //播放错误信息
                                        mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                                        mediaPlayer.start();
                                    }
                                }
                                // 离线入库的记录开始
                                Cursor cz_ru = db.rawQuery(" select count(*) as jilu  from  t_refund ", null);
                                if (cz_ru != null) {
                                    while (cz_ru.moveToNext()) {
                                        count = cz_ru.getString(cz_ru.getColumnIndex("jilu"));
                                    }
                                }
                                cz_ru.close();
                                // 离线入库的记录结束
                                qr_number.setText(count);// 本地添加数量
                            }
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }

        });

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

    // 时间类型加一秒
    public Date addOneSecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, 1);
        return calendar.getTime();
    }
}
