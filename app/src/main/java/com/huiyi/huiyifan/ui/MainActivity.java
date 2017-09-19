package com.huiyi.huiyifan.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.huiyi.huiyifan.R;
import com.huiyi.huiyifan.util.AppManager;


/**
 * 主页
 * */

public class MainActivity extends AppCompatActivity implements OnClickListener{
	private LinearLayout storage_ll;//入库
	private LinearLayout shipment_ll;//出货
	private LinearLayout refund_ll;// 退货
	private LinearLayout export_ll;//导出数据
	private LinearLayout update_ll;//更新数据
	private LinearLayout exit_ll;//退出
	private String producer_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		AppManager.getAppManager().addActivity(this);

		Intent intent = getIntent();
		producer_id = intent.getStringExtra("producer_id");
		initView();
	}

	private void initView() {
		storage_ll = (LinearLayout) findViewById(R.id.main_storage_ll);
		shipment_ll = (LinearLayout) findViewById(R.id.main_shipment_ll);
		refund_ll = (LinearLayout) findViewById(R.id.main_refund_ll);
		export_ll = (LinearLayout) findViewById(R.id.main_export_ll);
		update_ll = (LinearLayout) findViewById(R.id.main_update_ll);
		exit_ll = (LinearLayout) findViewById(R.id.main_exit_ll);

		storage_ll.setOnClickListener(this);
		shipment_ll.setOnClickListener(this);
		refund_ll.setOnClickListener(this);
		export_ll.setOnClickListener(this);
		update_ll.setOnClickListener(this);
		exit_ll.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.main_storage_ll:
				Intent intent_storage = new Intent(this,StorageActivity.class);
				intent_storage.putExtra("producer_id",producer_id);
				startActivity(intent_storage);
				break;
			case R.id.main_shipment_ll:
				Intent intent_shipment = new Intent(this,ShipmentActivity.class);
				intent_shipment.putExtra("producer_id",producer_id);
				startActivity(intent_shipment);
				break;
			case R.id.main_refund_ll:
				Intent intent_refund = new Intent(this,RefundActivity.class);
				intent_refund.putExtra("producer_id",producer_id);
				startActivity(intent_refund);
				break;
			case R.id.main_export_ll:
				Intent intent_export = new Intent(this,ExportActivity.class);
				intent_export.putExtra("producer_id",producer_id);
				startActivity(intent_export);
				break;
			case R.id.main_update_ll:
				Intent intent_update = new Intent(this,UpdateActivity.class);
				intent_update.putExtra("producer_id",producer_id);
				startActivity(intent_update);
				break;
			case R.id.main_exit_ll:
				onKeyDown(KeyEvent.KEYCODE_BACK,null);
				break;
		}
	}

	// 监听手机的返回键，按到返回键时执行这个方法，提示是否退出。
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 弹出确定退出对话框
			new AlertDialog.Builder(this)
					.setTitle("退出")
					.setMessage("确定退出吗？")
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
													int which) {
									dialog.cancel();
								}
							})
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									AppManager.getAppManager().finishActivity();
									AppManager.getAppManager().AppExit(this);
								}
							}).show();
			// 这里不需要执行父类的点击事件，所以直接return
			return true;
		}
		// 继续执行父类的其他点击事件
		return super.onKeyDown(keyCode, event);
	}
}
