package com.huiyi.huiyifan.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.huiyi.huiyifan.R;
import com.huiyi.huiyifan.util.AppManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by LW on 2017/7/26.
 * 欢迎页面
 */
public class HelloActivity extends Activity{
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hello);
		AppManager.getAppManager().addActivity(this);

		final Intent myIntent = new Intent(this, LoginActivity.class);
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				startActivity(myIntent);
				AppManager.getAppManager().finishActivity();
			}
		};
		timer.schedule(task, 1000 * 2);



	}
}
