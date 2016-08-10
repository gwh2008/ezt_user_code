package com.eztcn.user.eztcn.utils.http;

import java.util.HashMap;

import android.os.AsyncTask;

/**
 * 异步加载数据 基类
 * 
 * @date 2014-3-11
 * @author ichina
 */
public class EztHttp<Input> extends AsyncTask<Input, Exception, String> {
//
//	protected OnAsyncCallBack onAsyncCallBack;
//
//	public EztHttp() {
//		super();
//	}
//
//	/**
//	 * 设置回调方法
//	 * 
//	 * @param onAsyncCallBack
//	 */
//	public void setOnAsyncCallBack(OnAsyncCallBack onAsyncCallBack) {
//		this.onAsyncCallBack = onAsyncCallBack;
//	}
//
//	public interface OnAsyncCallBack {
//		/**
//		 * 操作成功
//		 * 
//		 * @param result
//		 */
//		public void onSuccess(Object result);
//
//		/**
//		 * 操作失败
//		 * 
//		 * @param error
//		 */
//		public void onError(Object error);
//	}
//
//	// 1 执行后台任务前对UI做一些标记
//	@Override
//	protected void onPreExecute() {
//		super.onPreExecute();
//
//	}
//
//	// 2 执行较为费时的操作
	@Override
	protected String doInBackground(Input... params1) {
//		HashMap<String, Object> params = (HashMap<String, Object>) params1[1];
//		String url = params1[0].toString();
//		String result = HttpConnUtil.doPost(url, params);
//		return result;
		return null;
	}
//
//	// 3 当后台操作结束时，此方法将会被调用
//	protected void onPostExecute(String result) {
//		if (result != null) {
//			onAsyncCallBack.onSuccess(result);
//		} else {
//			onAsyncCallBack.onError(result);
//		}
//
//	};
//
//	@Override
//	protected void onProgressUpdate(Exception... values) {
//
//		super.onProgressUpdate(values);
//	}
//
//	@Override
//	protected void onCancelled() {
//		super.onCancelled();
//	}
//
}
