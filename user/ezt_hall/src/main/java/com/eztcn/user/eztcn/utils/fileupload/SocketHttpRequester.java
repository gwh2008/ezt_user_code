package com.eztcn.user.eztcn.utils.fileupload;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.eztcn.user.eztcn.utils.Logger;

public class SocketHttpRequester {
	/**
	 * 直接通过HTTP协议提交数据到服务器,实现如下面表单提交功能: <FORM METHOD=POST ACTION=
	 * "http://api.eztcn.com/paas/api/v2/register/health/addMedicalRecordsPic.do"
	 * enctype="multipart/form-data"> <INPUT TYPE="text" NAME="name"> <INPUT
	 * TYPE="text" NAME="id"> <input type="file" name="imagefile"/> <input
	 * type="file" name="zip"/> </FORM>
	 * 
	 * @param path
	 *            上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://
	 *            www.iteye.cn或http://192.168.1.101:8083这样的路径测试)
	 * @param params
	 *            请求参数 key为参数名,value为参数值
	 * @param file
	 *            上传文件
	 */
	public static String post(String actionUrl, Map<String, String> params,
			List<File> files) {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		// String CHARSET = "UTF-8";
		try {
			URL url = new URL(actionUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/* 允许Input、Output，不使用Cache */
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			/* 设置传送的method=POST */
			con.setRequestMethod("POST");
			/* setRequestProperty */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			// 首先组拼文本类型的参数
			StringBuilder sb = new StringBuilder();
			if (params != null) {
				for (Map.Entry<String, String> entry : params.entrySet()) {

					sb.append(twoHyphens);
					sb.append(boundary);
					sb.append(end);
					sb.append("Content-Disposition: form-data; name=\""
							+ entry.getKey() + "\"" + end);
					sb.append(end);
					sb.append(entry.getValue());
					sb.append(end);
				}
			}

			Logger.i("xxx----", sb.toString());

			OutputStream out = con.getOutputStream();

			DataOutputStream ds = new DataOutputStream(out);
			ds.write(sb.toString().getBytes());
			/* 设置DataOutputStream */

			// 发送文件数据
			if (files != null) {
				int i = 0;
				for (File file : files) {
					StringBuilder sb2 = new StringBuilder();
					sb2.append(twoHyphens);
					sb2.append(boundary);
					sb2.append(end);
					sb2.append("Content-Disposition: form-data; " + "name=\""
							+ "myfile" + "\";filename=\"" + file.getName()
							+ "\"" + end);
					sb2.append("Content-Type: application/octet-stream" + end);
					/*
					 * sb2
					 * .append("Content-Type: application/octet-stream; charset="
					 * + CHARSET + end);
					 */
					sb2.append(end);

					ds.write(sb2.toString().getBytes());

					/* 取得文件的FileInputStream */
					FileInputStream fStream = new FileInputStream(file);
					// long total = fStream.available();
					/* 设置每次写入1024bytes */
					int bufferSize = 1024;
					byte[] buffer = new byte[bufferSize];

					int count = 0;
					// int length = 0;
					/* 从文件读取数据至缓冲区 */
					while ((count = fStream.read(buffer)) != -1) {
						/* 将资料写入DataOutputStream中 */
						ds.write(buffer, 0, count);
						// length += count;
						// if (handler != null) {
						// android.os.Message msg = new android.os.Message();
						// msg.what = WHAT_UPLOAD_FILE;
						// msg.arg1 = (int) ((length / (float) totalLength) *
						// 100);// 进度
						// msg.arg2 = i + 1;
						// handler.sendMessage(msg);
						// }
					}

					/* close streams */
					fStream.close();
					ds.write(end.getBytes());
					i = i + 1;
				}

			}

			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			ds.flush();

			/* 取得Response内容 */
			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			Logger.i("", "response :" + b.toString().trim());
			/* 将Response显示于Dialog */
			// showDialog(b.toString().trim());

			/* 关闭DataOutputStream */
			ds.close();
			con.disconnect();
			return b.toString().trim();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * 提交数据到服务器
	 * 
	 * @param path
	 *            上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://
	 *            www.itcast.cn或http://192.168.1.10:8080这样的路径测试)
	 * @param params
	 *            请求参数 key为参数名,value为参数值
	 * @param file
	 *            上传文件
	 */
	public static String post(String path, Map<String, String> params, File file)
			throws Exception {
		List<File> fileList = new ArrayList<File>();
		fileList.add(file);
		return post(path, params, fileList);
	}
}
