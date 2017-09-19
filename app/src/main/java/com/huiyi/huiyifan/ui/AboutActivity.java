package com.huiyi.huiyifan.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.huiyi.huiyifan.R;
import com.huiyi.huiyifan.base.TopBarBaseActivity;
import com.huiyi.huiyifan.db.AboutDao;
import com.huiyi.huiyifan.util.AppManager;
import com.huiyi.huiyifan.util.DBHelper;


/**
 * Created by LW on 2017/7/26.
 * 关于页面
 */
public class AboutActivity extends TopBarBaseActivity implements View.OnClickListener{

	private EditText address_et;//主页
	private Button  save_bt;
	private DBHelper dbHelper;
	private SQLiteDatabase db;
	private int record;
	private String url;
	private String producer_id;


	//UI布局
	@Override
	protected int getContentView() {
		AppManager.getAppManager().addActivity(this);

		return R.layout.activity_about;

	}
	//自定义Toolbar
	@Override
	protected void init(Bundle savedInstanceState) {
		initView();
		setTitle("设置");
		setLeftButton(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick() {
				Intent intent = new Intent(AboutActivity.this,LoginActivity.class);
				intent.putExtra("producer_id",producer_id);
				startActivity(intent);
			}
		});
	}

	private void initView() {
		address_et = (EditText) findViewById(R.id.about_address_et);
		save_bt = (Button) findViewById(R.id.about_save_bt);
		save_bt.setOnClickListener(this);

		dbHelper = new DBHelper(getBaseContext());
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) as jilu  from t_about", null);
		if (cursor != null) {
			while (cursor.moveToNext()) {//移动光标
				record = cursor.getInt(cursor.getColumnIndex("jilu"));
			}
		}
		cursor.close();


		if (record > 0) {
			Cursor cursor_data = db.rawQuery("select url from t_about", null);
			if (cursor_data != null) {
				while (cursor_data.moveToNext()) {
					String url = cursor_data.getString(cursor_data.getColumnIndex("url"));
					address_et.setText(url);
				}
			}
			cursor_data.close();

		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.about_save_bt:
				dbHelper= new DBHelper(getBaseContext());
				db = dbHelper.getReadableDatabase();
				url = address_et.getText().toString();
				if (address_et.getText().toString().trim().equals("") || address_et.getText().toString().trim() == null) {
					Toast.makeText(this, "参数不能为空！", Toast.LENGTH_SHORT).show();
				}else{
					int aa = record;
					if(aa > 0) {
						AboutDao aboutDao = new AboutDao(getBaseContext());
						aboutDao.ModifyAbout(url);
						Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
					}else {
						AboutDao aboutDao = new AboutDao(getBaseContext());
						aboutDao.AddAbout(url);
						Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
					}
				}

				break;
		}
	}

	//返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK){
			Intent myIntent = new Intent(AboutActivity.this,LoginActivity.class);
			startActivity(myIntent);
			AppManager.getAppManager().finishActivity();
		}
		return super.onKeyDown(keyCode, event);
	}
}
