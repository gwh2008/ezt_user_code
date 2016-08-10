package com.eztcn.user.eztcn.utils.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.xmlpull.v1.XmlPullParserException;

import android.widget.Toast;

import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.utils.Logger;

/**
 * @title 联网工具类
 * @describe
 * @author ezt
 * @created 2015年3月5日
 */
public class HttpConnUtil {

	/**
	 * Cache for most recent request
	 */
	private static RequestCache requestCache = null;
	static long time = 0;

	private static final String TAG = "HTTP";
	private static String sessionId;

	/**
	 * 打印POST请求地址
	 * 
	 * @param url
	 * @param params
	 */
	private static void printPosRequestUrl(String url,
			HashMap<String, Object> params) {
		for (Iterator<String> iterator = params.keySet().iterator(); iterator
				.hasNext();) {
			String key = (String) iterator.next();
			if (key != null)
				url += key
						+ "="
						+ (params.get(key) == null ? "" : params.get(key))
								.toString() + "&";
		}
		Logger.i(TAG + "-post", url.substring(0, url.length()));
	}

	/**
	 * 执行GET方法 (获取网络数据)
	 * 
	 * @param url
	 *            请求URL
	 * @return 返回结果字符串格式 @
	 */
	public static String doGet(String url) {
		Logger.i(TAG + "-get", url);
		String data = null;
		if (requestCache != null) {
			data = (String) requestCache.get(url);
			if (data != null) {
				return data;
			}
		}

		// initialize HTTP GET request objects
		HttpParams httpParameters = new BasicHttpParams();// Set the timeout in
															// milliseconds
															// until a
															// connection is
															// established.
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				EZTConfig.HTTP_TIMEOUT_CONNECTION);// Set the default socket
													// timeout (SO_TIMEOUT) //
													// in milliseconds which is
													// the timeout for waiting
													// for data.
		HttpConnectionParams.setSoTimeout(httpParameters,
				EZTConfig.HTTP_SOTIMOUT);
		HttpClient httpClient = new DefaultHttpClient(httpParameters);
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse = null;
		if (sessionId != null) {
			httpGet.setHeader("Cookie", "JSESSIONID=" + sessionId);
		}

		try {
			// execute request
			try {
				httpResponse = httpClient.execute(httpGet);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (SocketException e) {
				e.printStackTrace();
			}

			// request data返回数据
			HttpEntity httpEntity = httpResponse.getEntity();

			CookieStore mCookieStore = ((AbstractHttpClient) httpClient)
					.getCookieStore();
			List<Cookie> cookies = mCookieStore.getCookies();
			for (int i = 0; i < cookies.size(); i++) {
				// 这里是读取Cookie['PHPSESSID']的值存在静态变量中，保证每次都是同一个值
				if ("JSESSIONID".equals(cookies.get(i).getName())) {
					sessionId = cookies.get(i).getValue();
					break;
				}
			}

			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				data = convertStreamToString(inputStream);
				// cache the result
				if (requestCache != null) {
					requestCache.put(url, data);
				}
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}

	/**
	 * 执行GET方法获取数据，返回数据流
	 * 
	 * @param url
	 * @return 返回 InputStream @
	 */
	public static InputStream doGetStream(String url) {
		InputStream data = null;
		// initialize HTTP GET request objects
		HttpParams httpParameters = new BasicHttpParams();// Set the timeout in
															// milliseconds
															// until a
															// connection is
															// established.
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				EZTConfig.HTTP_TIMEOUT_CONNECTION);// Set the default socket
													// timeout (SO_TIMEOUT) //
													// in milliseconds which is
													// the timeout for waiting
													// for data.
		HttpConnectionParams.setSoTimeout(httpParameters,
				EZTConfig.HTTP_SOTIMOUT);
		HttpClient httpClient = new DefaultHttpClient(httpParameters);

		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse = null;

		try {
			// execute request
			try {
				httpResponse = httpClient.execute(httpGet);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (SocketException e) {
				e.printStackTrace();
			}

			// request data
			HttpEntity httpEntity = httpResponse.getEntity();

			if (httpEntity != null) {
				data = httpEntity.getContent();
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}

	/**
	 * 将网络返回的数据流转换为字符串
	 * 
	 * @param is
	 * @return
	 */
	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}

	public static void setRequestCache(RequestCache requestCache) {
		HttpConnUtil.requestCache = requestCache;
	}

	/**
	 * 执行POST方法 提交数据给服务器
	 * 
	 * @param url
	 * @param params
	 *            传递参数
	 * @return @
	 */
	public static String doPost(String url, HashMap<String, Object> params) {
		printPosRequestUrl(url, params);
		String data = null;
		/**
		 * Post 运行传递参数必须用 NameValuePair[] 数组存储
		 */
		List<NameValuePair> _params = new ArrayList<NameValuePair>();

		Iterator<?> iterator = params.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			_params.add(new BasicNameValuePair("" + key, "" + value));
		}

		HttpParams httpParameters = new BasicHttpParams();// Set the timeout in
															// milliseconds
															// until a
															// connection is
															// established.
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				EZTConfig.HTTP_TIMEOUT_CONNECTION);// Set the default socket
													// timeout (SO_TIMEOUT) //
													// in milliseconds which is
													// the timeout for waiting
													// for data.
		HttpConnectionParams.setSoTimeout(httpParameters,
				EZTConfig.HTTP_SOTIMOUT);
		HttpClient httpClient = new DefaultHttpClient(httpParameters);

		HttpPost httppost = new HttpPost(url);
		if (sessionId != null) {
			httppost.setHeader("Cookie", "JSESSIONID=" + sessionId);
		}
		try {
			// 添加参数
			httppost.setEntity(new UrlEncodedFormEntity(_params, HTTP.UTF_8));
			// 执行
			HttpResponse httpResponse = httpClient.execute(httppost);
			// 返回数据
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 取出答应字符串
				data = EntityUtils.toString(httpResponse.getEntity(),
						HTTP.UTF_8);
				CookieStore mCookieStore = ((AbstractHttpClient) httpClient)
						.getCookieStore();
				List<Cookie> cookies = mCookieStore.getCookies();
				for (int i = 0; i < cookies.size(); i++) {
					// 这里是读取Cookie['PHPSESSID']的值存在静态变量中，保证每次都是同一个值
					if ("JSESSIONID".equals(cookies.get(i).getName())) {
						sessionId = cookies.get(i).getValue();
						break;
					}
				}
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * .net webService
	 * 
	 * @param url
	 *            .net url
	 * @param methodName
	 *            .net方法名
	 * @param params
	 *            .net参数
	 * @return
	 */
	public static String doPostNet(String url, String methodName,
			HashMap<String, Object> params) {
		printPosRequestUrl(url, params);
		// 命名空间
		final String NAMESPACE = "http://tempuri.org/";
		// 调用的方法名
		final String METHOD_NAME = methodName;
		// 此处是命名空间+方法名
		final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
		SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
		Iterator<?> iterator = params.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			PropertyInfo pi = new PropertyInfo();
			pi.setName(String.valueOf(key));
			pi.setValue(value);
			rpc.addProperty(pi);
		}
		Element[] header = new Element[1];
		header[0] = new Element().createElement(NAMESPACE, "MySoapHeader");
		Element username = new Element().createElement(NAMESPACE, "UserName");
		username.addChild(Node.TEXT, "ezt");
		header[0].addChild(Node.ELEMENT, username);
		Element pass = new Element().createElement(NAMESPACE, "PassWord");
		pass.addChild(Node.TEXT, "ezt123456");
		header[0].addChild(Node.ELEMENT, pass);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.headerOut = header;
		envelope.bodyOut = rpc;
		envelope.dotNet = true;

		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(url);
		try {
			ht.call(SOAP_ACTION, envelope);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (XmlPullParserException e1) {
			e1.printStackTrace();
		}
		// 此处如果用soapobject会报错
		SoapPrimitive detail = null;
		try {
			detail = (SoapPrimitive) envelope.getResponse();
		} catch (SoapFault e) {
			e.printStackTrace();
		}
		return null == detail ? null : String.valueOf(detail);
	}

	// public static final int WHAT_UPLOAD_FILE = 5;

	/**
	 * 单个文件上传
	 * 
	 * @param actionUrl
	 * @param name
	 * @param files
	 * @param params
	 * @param handler
	 * @return
	 */
	public static String uploadFile(String actionUrl, String name,
			HashMap<String, File> files, HashMap<String, String> params) {
		return uploadFile(actionUrl, new String[] { name }, files, params);
	}

	/**
	 * 多个文件上传
	 * 
	 * @param actionUrl
	 * @param names
	 * @param files
	 * @param params
	 * @param handler
	 * @return
	 */
	public static String uploadFile(String actionUrl, String[] names,
			HashMap<String, File> files, HashMap<String, String> params) {
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
				for (Map.Entry<String, File> file : files.entrySet()) {
					StringBuilder sb2 = new StringBuilder();
					sb2.append(twoHyphens);
					sb2.append(boundary);
					sb2.append(end);
					sb2.append("Content-Disposition: form-data; " + "name=\""
							+ names[i] + "\";filename=\"" + file.getKey()
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
					FileInputStream fStream = new FileInputStream(
							file.getValue());
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
}
