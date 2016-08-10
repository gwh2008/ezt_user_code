package com.eztcn.user.eztcn.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.controller.MedicalRecordManager;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.fileupload.SocketHttpRequester;
import com.eztcn.user.eztcn.utils.http.HttpConnUtil;

/**
 * @title 文件上传
 * @describe
 * @author ezt
 * @created 2015年3月23日
 */
public class FileUploadImpl {

	// public void upLoadFiles(AjaxParams params, final IHttpResult callBack) {
	// FinalHttp http = FinalHttp.getInstance();
	// http.post(EZTConfig.CREATE_EMR_UPLOAD, params,
	// new AjaxCallBack<Object>() {
	// @Override
	// public void onFailure(Throwable t, int errorNo,
	// String strMsg) {
	// super.onFailure(t, errorNo, strMsg);
	// callBack.result(HttpParams.CREATE_EMR_UPLOAD, false,
	// strMsg);
	// }
	//
	// @Override
	// public void onSuccess(Object t) {
	// super.onSuccess(t);
	// Map<String, Object> map = MedicalRecordManager
	// .parseMImage(t.toString());
	// callBack.result(HttpParams.CREATE_EMR_UPLOAD, true, map);
	// }
	//
	// @Override
	// public void onLoading(long count, long current) {
	// super.onLoading(count, current);
	// }
	// });
	// }

	public void upLoadFiles(String actionUrl, Map<String, String> params,
			List<File> files, final IHttpResult callBack) {
		// String result = HttpConnUtil
		// .uploadFile(actionUrl, names, files, params);
		String result = SocketHttpRequester.post(EZTConfig.CREATE_EMR_UPLOAD,
				params, files);
		Map<String, Object> map = MedicalRecordManager.parseMImage(result);
		callBack.result(HttpParams.CREATE_EMR_UPLOAD, true, map);
	}
}
