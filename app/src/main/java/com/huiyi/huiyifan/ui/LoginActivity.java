package com.huiyi.huiyifan.ui;

import java.util.ArrayList;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyi.huiyifan.R;
import com.huiyi.huiyifan.base.TopBarBaseActivity;
import com.huiyi.huiyifan.db.AboutDao;
import com.huiyi.huiyifan.db.ExportDao;
import com.huiyi.huiyifan.util.AppManager;
import com.huiyi.huiyifan.util.DBHelper;
import com.huiyi.huiyifan.util.HttpConnSoap;
import com.huiyi.huiyifan.util.UpdateManager;

/**
 * Created by LW on 2017/7/26.
 * 登录界面
 */

public class LoginActivity extends TopBarBaseActivity implements  OnClickListener{

	private EditText name_et;//账号
	private EditText password_et;//密码
	private CheckBox remember_cb;//记住密码
	private RadioGroup radioGroup;//复选框
	private RadioButton offline_rb;//离线登录
	private RadioButton online_rb;//在线登录
	private Button login_bt;//登录
	private TextView about_t;//关于
	private SharedPreferences sp ;//储存
	private DBHelper dbhelper;
	private SQLiteDatabase db;
	private String name;//用户名
	private String pwd;//密码
	private ArrayList<String> arrayList = new ArrayList<String>();
	private ArrayList<String> brrayList = new ArrayList<String>();
	private ArrayList<String> crrayList = new ArrayList<String>();
	private HttpConnSoap Soap = new HttpConnSoap();
	private String ServerUrl;
	private String producer_id="0",producer_idB = "0";
	private int record,record_export;


	@Override
	protected int getContentView() {
		sp = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		AppManager.getAppManager().addActivity(this);
		return R.layout.activity_login;
	}

	@Override
	protected void init(Bundle savedInstanceState) {
		initView();
		setTitle("生产商登录");

	}

	private void initView() {

		name_et = (EditText) findViewById(R.id.login_name_et);
		password_et = (EditText) findViewById(R.id.login_password_et);
		remember_cb = (CheckBox) findViewById(R.id.login_remember_cb);

		radioGroup = (RadioGroup) findViewById(R.id.login_rg);
		offline_rb = (RadioButton) findViewById(R.id.login_offline_rb);
		online_rb = (RadioButton) findViewById(R.id.login_online_rb);

		login_bt = (Button) findViewById(R.id.login_login_bt);
		about_t = (TextView) findViewById(R.id.login_about_tv);

		about_t.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
		about_t.setOnClickListener(this);
		login_bt.setOnClickListener(this);

		dbhelper = new DBHelper(getBaseContext());
		db = dbhelper.getReadableDatabase();

		//默认服务器地址t
		try {

			Cursor cza = db.rawQuery("select count(*) as jilu  from t_about",null);
			if (cza != null) {
				while (cza.moveToNext()) {
					record = cza.getInt(cza.getColumnIndex("jilu"));//是否已有记录
				}
			}
			cza.close();
			if(record>0){

			}else{
				AboutDao aboutDao = new AboutDao(getBaseContext());

				aboutDao.AddAbout("http://120.77.48.11:8035/web.asmx");
			}
		}catch (Exception e1) {

			e1.printStackTrace();
		}

		//默认给个关联值开始====================================================
		try {
			Cursor jID = db.rawQuery("select count(*) as jilu  from t_export",null);
			if (jID != null) {
				while (jID.moveToNext()) {
					record_export = jID.getInt(jID.getColumnIndex("jilu"));//是否已有记录
				}
			}
			jID.close();
			if(record_export > 0){

			}else{
				ExportDao dal_exportDao = new ExportDao(getBaseContext());
				dal_exportDao.AddExport(1, 0, 0, 0, 0);
			}
		}catch (Exception e1) {

			e1.printStackTrace();
		}
		//默认给个关联值结束====================================================

		// 获取接口地址
		AboutDao aboutDao = new AboutDao(getBaseContext());
		ServerUrl = aboutDao.AboutCha();

//		获取Activity 传过来的参数
//		取值：
		Intent intent=getIntent();
		String zhi=intent.getStringExtra("extra");
		if (zhi != null) {
			int a = Integer.valueOf(zhi).intValue();
			if (a == 1) {

				// 检查软件更新
				UpdateManager manager = new UpdateManager(LoginActivity.this);
				manager.checkUpdate("http://www.hy315.cc");
			}
		}

		//记住密码
		if (sp.getBoolean("checkboxBoolean",false)){
			name_et.setText(sp.getString("name",null));
			password_et.setText(sp.getString("password",null));
			remember_cb.setChecked(true);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.login_about_tv://跳转关于页面
				Intent intent_about = new Intent(this,AboutActivity.class);
				startActivity(intent_about);
				AppManager.getAppManager().finishActivity();
				break;
			case R.id.login_login_bt://跳转主界面
				//记住密码
				name = name_et.getText().toString().trim();
				pwd = password_et.getText().toString().trim();
				if(name.trim().equals("")){
					Toast.makeText(this,"请输入账号!",Toast.LENGTH_SHORT).show();
					return;
				}else if (pwd.trim().equals("")){
					Toast.makeText(this,"请输入密码!",Toast.LENGTH_SHORT).show();
					return;
				}else{
					//选择框
					//离线登录
					if (offline_rb.isChecked()) {
						try{
							dbhelper = new DBHelper(getBaseContext());
							db = dbhelper.getReadableDatabase();
							Cursor cursor = db.rawQuery("select ID from t_producer", null);
							if (cursor != null) {
								while (cursor.moveToNext()) {
									producer_id = cursor.getString(cursor.getColumnIndex("ID"));
								}
							}
							cursor.close();
							if (Integer.parseInt(producer_id) == 0) {
								Toast.makeText(getBaseContext(), "请先使用在线模式登录把账号资料更新下来，再用离线模式登录！", Toast.LENGTH_SHORT).show();
							} else {
								Cursor cursor1 = db.rawQuery(" select ID  from t_producer where name ='" + name + "' and password='" + pwd + "'   ", null);
								if (cursor1 != null) {
									while (cursor1.moveToNext()) {
										producer_idB = cursor1.getString(cursor1.getColumnIndex("ID"));
									}
								}
								cursor1.close();

								if(Integer.parseInt(producer_id)==Integer.parseInt(producer_idB)) {
									Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT)
											.show();
									Intent intent = new Intent();
									intent.putExtra("producer_id", producer_id);
									intent.setClass(LoginActivity.this, MainActivity.class);
									startActivity(intent);
								}else {
									Toast.makeText(LoginActivity.this,"账号或者密码错误",Toast.LENGTH_SHORT).show();
								}
							}

						}catch (Exception e){
							e.printStackTrace();
						}
						//在线登录
					}if (online_rb.isChecked()) {
						getLogin();
					}
				}

				boolean CheckBoxLogin = remember_cb.isChecked();
				if (CheckBoxLogin){
					SharedPreferences.Editor editor = sp.edit();
					editor.putString("name",name);
					editor.putString("password",pwd);
					editor.putBoolean("checkboxBoolean",true);
					editor.commit();
				}else {
					SharedPreferences.Editor editor = sp.edit();
					editor.putString("name",null);
					editor.putString("password",null);
					editor.putBoolean("checkboxBoolean",false);
					editor.commit();
				}
				break;
		}
	}

	public void getLogin() {
		final ProgressDialog progressDialog=new ProgressDialog(this);
		progressDialog.setTitle("登录");
		progressDialog.setMessage("正在登录中...");
		progressDialog.onStart();
		progressDialog.show();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Login(name,pwd);

				} catch (Exception e) {

					e.printStackTrace();
				}finally{
					progressDialog.dismiss();
				}

			}
		}).start();

	}

	public void Login(String name,String password) {
		ArrayList<String> List=new ArrayList<String>();
		arrayList.clear();
		brrayList.clear();

		arrayList.add("name");
		arrayList.add("password");
		brrayList.add(name);
		brrayList.add(password);

		crrayList = Soap.GetWebServre("xml_producer", arrayList,brrayList, ServerUrl);
		String str =String.valueOf(crrayList);
		String sj = (str.substring(str.indexOf("[") + 1, str.indexOf("]"))).trim();
		Message message = handler.obtainMessage();
		message.obj = sj;
		handler.sendMessage(message);
	}


	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 将WebService得到的结果返回给TextView
			String jsonshuju = msg.obj.toString();
			if (jsonshuju.trim().equals("") || jsonshuju.trim() == null) {
					/*tishi_shipment_textView1.setText("登录失败，请检测网络是否正常！");*/
				Toast.makeText(LoginActivity.this, "登录失败，请检测网络是否正常！", Toast.LENGTH_SHORT)
						.show();
			} else {
				try {
					String s = new String(jsonshuju);
					String a[] = s.split(",");
					String jilu = a[0].toString();
					producer_id = a[1].toString();
					if(Integer.parseInt(jilu) == 1){
						Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT)
								.show();
						Intent intent=new Intent();
						intent.putExtra("producer_id", producer_id);
						intent.setClass(LoginActivity.this, MainActivity.class);
						startActivity(intent);

					}else{
						Toast.makeText(getBaseContext(),"账号或密码错误！",Toast.LENGTH_SHORT).show();
					}

				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}
	};

}
