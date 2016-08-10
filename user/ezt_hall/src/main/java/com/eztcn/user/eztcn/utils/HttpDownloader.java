/**
 * 
 */
package com.eztcn.user.eztcn.utils;

/**
 * @author Liu Gang
 * 
 * 2015年9月28日
 * 下午5:38:36
 * http://zhoujingxian.iteye.com/blog/859597
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class HttpDownloader {

	private URL url = null;

	public void readFile(final Context context, final Handler handler,final String fileName) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				StringBuffer sb = new StringBuffer();
				String line = null;
				BufferedReader buffer = null;
				InputStream stream = null;
				try {
					stream = context.getAssets().open(fileName);
					buffer = new BufferedReader(new InputStreamReader(stream));
					while ((line = buffer.readLine()) != null) {
						sb.append(line);
					}

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						buffer.close();
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				Message msg = handler.obtainMessage();
				msg.obj = sb.toString();
				msg.sendToTarget();

			}
		}).start();

	}

	/**
	 * 根据URL下载文件,前提是这个文件当中的内容是文本,函数的返回值就是文本当中的内容 1.创建一个URL对象
	 * 2.通过URL对象,创建一个HttpURLConnection对象 3.得到InputStream 4.从InputStream当中读取数据
	 * 
	 * @param urlStr
	 * @return
	 */
	public void readJsonFile(final String urlStr, final Handler handler) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO 自动生成的方法存根

				StringBuffer sb = new StringBuffer();
				String line = null;
				BufferedReader buffer = null;
				try {
					url = new URL(urlStr);
					HttpURLConnection urlConn = (HttpURLConnection) url
							.openConnection();
					buffer = new BufferedReader(new InputStreamReader(urlConn
							.getInputStream()));
					while ((line = buffer.readLine()) != null) {
						sb.append(line);
					}
					Message msg = handler.obtainMessage();
					msg.obj = sb.toString();
					msg.sendToTarget();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						buffer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

}
