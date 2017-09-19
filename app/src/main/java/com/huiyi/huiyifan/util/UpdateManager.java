package com.huiyi.huiyifan.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.huiyi.huiyifan.R;

/**
 *@author coolszy
 *@blog http://blog.92coding.com
 */

public class UpdateManager
{
	/* 下载中 */
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	/* 保存解析的XML信息 */
	HashMap<String, String> mHashMap;
	/* 下载保存路径 */
	private String mSavePath;
	/* 记录进度条数量 */
	private int progress;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;

	private Context mContext;
	/* 更新进度条 */
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;

	//服务器版本号
	public int serviceCode;
	//更新标题与内容
	public String title,contentA,contentB,contentC,contentD;

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				// 正在下载
				case DOWNLOAD:
					// 设置进度条位置
					mProgress.setProgress(progress);
					break;
				case DOWNLOAD_FINISH:
					// 安装文件
					installApk();
					break;
				default:
					break;
			}
		};
	};

	public UpdateManager(Context context)
	{
		this.mContext = context;
	}

	/**
	 * 检测软件更新
	 */
	public void checkUpdate(String ServerUrl)
	{
		if (isUpdate(ServerUrl))
		{
			// 显示提示对话框
			showNoticeDialog();
		} else
		{
			//Toast.makeText(mContext, "已经是最新版本", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 检查软件是否有更新版本
	 *
	 * @return
	 */
	private boolean isUpdate(String ServerUrl)
	{
		// 获取当前软件版本
		int versionCode = getVersionCode(mContext);
		// 把version.xml放到网络上，然后获取文件信息
		//InputStream inStream = ParseXmlService.class.getClassLoader().getResourceAsStream("version.xml");
		// 解析XML文件。 由于XML文件比较小，因此使用DOM方式进行解析
		ParseXmlService service = new ParseXmlService();
		try
		{
			String path = ""+ServerUrl+"/version.xml";
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setReadTimeout(5*1000);
			conn.setRequestMethod("GET");
			InputStream inStream = conn.getInputStream();
			mHashMap = service.parseXml(inStream);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		if (null != mHashMap)
		{

			serviceCode = Integer.valueOf(mHashMap.get("version"));

			if (serviceCode > versionCode)
			{
				/**
				 * 要先检查入库与出货记录是否还有，还有的话就不能更新
				 */


				title = mHashMap.get("title");

				contentA = mHashMap.get("contentA");

				contentB = mHashMap.get("contentB");


				return true;
			}
		}
		return false;
	}

	/**
	 * 获取软件版本号
	 *
	 * @param context
	 * @return
	 */
	private int getVersionCode(Context context)
	{
		int versionCode = 0;
		try
		{
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionCode = context.getPackageManager().getPackageInfo("com.dongyu.huiyi", 0).versionCode;
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return versionCode;
	}



	/**
	 * 显示软件更新对话框
	 */
	private void showNoticeDialog()
	{
		// 构造对话框
		AlertDialog.Builder builder = new Builder(mContext);
		/*builder.setTitle(R.string.soft_update_title);*/
		builder.setTitle(title);//更新提示的标题
		builder.setMessage("更新内容：\r\n"+contentA+"\r\n"+contentB+"\r\n");//更新提示的内容
		//builder.setMessage("更新内容：\r\n1、优化了更新功能\r\n2、增加了导出入库出货的离线文件功能\r\n");//更新提示的内容

		// 更新
		builder.setPositiveButton(R.string.soft_update_updatebtn, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();

				// 显示下载对话框
				showDownloadDialog();

			}
		});
		// 稍后更新
		builder.setNegativeButton(R.string.soft_update_later, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}

	protected Context getBaseContext() {

		return null;
	}

	/**
	 * 显示软件下载对话框
	 */
	private void showDownloadDialog()
	{
		// 构造软件下载对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.soft_updating);
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// 取消更新
		builder.setNegativeButton(R.string.soft_update_cancel, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				// 设置取消状态
				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// 现在文件
		downloadApk();
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk()
	{
		// 启动新线程下载软件
		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 *
	 * @author coolszy
	 *@date 2017-8-11
	 *@blog http://blog.92coding.com
	 */
	private class downloadApkThread extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory() + "/";
					mSavePath = sdpath + "download";
					URL url = new URL(mHashMap.get("url"));

					/*String a = String.valueOf(url);

					openFile(downLoadFile(a));*/
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists())
					{
						file.mkdir();
					}
					File apkFile = new File(mSavePath, mHashMap.get("name"));
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do
					{
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0)
						{
							// 下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载.
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			// 取消下载对话框显示
			mDownloadDialog.dismiss();
		}
	};


	/**
	 * 安装APK文件
	 */
	private void installApk()
	{
		File apkfile = new File(mSavePath, mHashMap.get("name"));
		if (!apkfile.exists())
		{
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//没有这句的话安装完成没有提示完成，打开这两个按钮
		mContext.startActivity(i);
	}

}
