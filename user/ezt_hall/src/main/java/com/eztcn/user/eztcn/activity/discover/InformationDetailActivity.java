package com.eztcn.user.eztcn.activity.discover;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import xutils.view.annotation.event.OnItemClick;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.DoctorIndex30Activity;
import com.eztcn.user.eztcn.adapter.QuickAppointListAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Doctor;
import com.eztcn.user.eztcn.bean.Information;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.MyListView;
import com.eztcn.user.eztcn.customView.UmengCustomShareBoard;
import com.eztcn.user.eztcn.impl.NewsImpl;
import com.eztcn.user.eztcn.utils.CacheUtils;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.Logger;
import com.eztcn.user.hall.utils.Constant;

/**
 * @title 资讯详情
 * @describe
 * @author ezt
 * @created 2014年12月31日
 */
@SuppressLint("JavascriptInterface")
public class InformationDetailActivity extends FinalActivity implements
		TextWatcher, IHttpResult {//, OnClickListener

	@ViewInject(R.id.informate_info_web)
	private WebView webDetail;// 内容显示

	// @ViewInject(R.id.create_type_tv)
	// private TextView tvType;// 文章类型
	//
	// @ViewInject(R.id.info_title_tv)
	// private TextView tvTitle;// 标题
	//
	// @ViewInject(R.id.create_time_tv)
	// private TextView tvTime;// 创建时间
	//
	// @ViewInject(R.id.from_tv)
	// private TextView tvFrom;// 来源
	//
	// @ViewInject(R.id.evaluate_num_tv)
	// private TextView tvEvaluateNum;// 评价数

	@ViewInject(R.id.informate_num_tv)
	private TextView tvNum;// 底部评价数

	@ViewInject(R.id.informate_info_et)
	private EditText etInfo;// 编辑框

	@ViewInject(R.id.layout1)
	private RelativeLayout layout1;

	@ViewInject(R.id.textView2)
	private TextView tv;

	@ViewInject(R.id.informate_doc_lv)
	private MyListView lvDoc;

	@ViewInject(R.id.bottom_line)
	private View line;

	@ViewInject(R.id.bottom_line2)
	private View line2;

	private String id;// 文章id
	private String strNum;// 评论数
	private Information info = null;
	private String infoUrl;// 图片地址
	private String strTitle;

	private final String INFO_DETAIL_DATA = "infoDetailData";// 缓存key
	private CacheUtils mCache;
	private String infoId;
	// private int type;// 进入方式1、首页轮播图进入

	private QuickAppointListAdapter docAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_information_detail);
		ViewUtils.inject(InformationDetailActivity.this);
		mCache = CacheUtils.get(this);
		infoId = getIntent().getStringExtra("infoId");// 资讯id
		infoUrl = getIntent().getStringExtra("infoUrl");// 资讯图片地址
		// ImageView imgShare = loadTitleBar(true, "资讯详情",
		// R.drawable.selector_share_bg);
		// imgShare.setOnClickListener(this);

		loadTitleBar(true, "", null);//去除分享  资讯详情

//		etInfo.addTextChangedListener(this);
//		tvNum.setTextColor(getResources().getColor(R.color.dark_gray));
//		tvNum.setTextSize(16);
		/**
		 * webview初始化
		 */
		webDetail.getSettings().setJavaScriptEnabled(true);
		webDetail.addJavascriptInterface(new JavascriptInterface(this),
				"imagelistener");

		webDetail.getSettings().setRenderPriority(RenderPriority.HIGH);
		webDetail.getSettings().setLayoutAlgorithm(
				LayoutAlgorithm.SINGLE_COLUMN);// 居中
		// webDetail.getSettings().setDefaultFontSize(18);// 设置显示的字体大小
		webDetail.getSettings().setSupportZoom(true);
		webDetail.setBackgroundColor(Color.parseColor("#ffffff"));// 设置背景
		webDetail.getSettings().setBuiltInZoomControls(false);// 设置放大缩小的控件显示
		// infoWeb.setScrollbarFadingEnabled(true);
		webDetail.getSettings().setDomStorageEnabled(true);
		webDetail.setWebViewClient(new MyWebViewClient());
		webDetail.clearMatches();
		docAdapter = new QuickAppointListAdapter(this, 1);
		lvDoc.setAdapter(docAdapter);
        webDetail.loadUrl(infoUrl);//这个页面现在改为只加载一个webview页面
	}

	class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// 监听器加载这是为了防止动态加载图片时新加载的图片无法预览
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onLoadResource(WebView view, String url) {
			super.onLoadResource(view, url);
			// 监听器加载这是为了防止动态加载图片时新加载的图片无法预览
			addImageClickListener();
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			addImageClickListener();
		}
	}

	// js通信接口
	public class JavascriptInterface {

		private Context context;

		public JavascriptInterface(Context context) {
			this.context = context;
		}

		public void openImage(String object, int position) {
			Intent intent = new Intent();
			intent.putExtra(ShowWebImageActivity.IMAGE_URLS, object);
			intent.putExtra(ShowWebImageActivity.POSITION, position);
			intent.putExtra(ShowWebImageActivity.TITLE, strTitle);
			intent.putExtra(ShowWebImageActivity.NUM, tvNum.getText()
					.toString());
			intent.putExtra(ShowWebImageActivity.ID, id);

			intent.setClass(context, ShowWebImageActivity.class);
			context.startActivity(intent);
		}
	}

	// 注入js函数监听
	private void addImageClickListener() {
		// 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
		String imageloadJS = getFromAssets("imageload.js");
		if (!TextUtils.isEmpty(imageloadJS)) {
			webDetail.loadUrl(imageloadJS);

			if (docAdapter.getList() != null
					&& docAdapter.getList().size() != 0) {
				handler.sendEmptyMessageDelayed(1, 1000);
			}

		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			lvDoc.setVisibility(View.VISIBLE);
			tv.setVisibility(View.VISIBLE);
			line.setVisibility(View.VISIBLE);
			line2.setVisibility(View.VISIBLE);
		}

	};

	// 读取assets中的文件
	private String getFromAssets(String fileName) {
		try {
			InputStreamReader inputReader = new InputStreamReader(
					getResources().getAssets().open(fileName));
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";
			String Result = "";
			while ((line = bufReader.readLine()) != null)
				Result += line;
			return Result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (webDetail != null) {
			// webDetail.clearCache(true);
			// webDetail.clearHistory();
			webDetail.destroy();
		}
	}

	/**
	 * 初始化数据
	 */
	private void initialData(String infoId) {
		showProgressToast();
		// HashMap<String, Object> params = new HashMap<String, Object>();
		// params.put("infoid", infoId);
		RequestParams params = new RequestParams();

		params.addBodyParameter("infoid", infoId);

		// //params.put("infoid", 678);
		new NewsImpl().getNewsDetail(params, this);
	}

	@OnClick(R.id.informate_num_tv)
	private void tvNumClick(View v) {
		if (BaseApplication.getInstance().isNetConnected) {
			if ("发送".equals(tvNum.getText().toString().trim())) {
				if (BaseApplication.patient != null) {
					String strInfo = etInfo.getText().toString();
					strInfo = strInfo.replaceAll(" ", "");
					RequestParams params = new RequestParams();

					params.addBodyParameter("aid", id);
					params.addBodyParameter("userid",
							BaseApplication.patient.getUserId() + "");
					params.addBodyParameter("username",
							TextUtils.isEmpty(BaseApplication.patient
									.getEpName()) ? "匿名"
									: BaseApplication.patient.getEpName());
					params.addBodyParameter("content", strInfo);
					params.addBodyParameter("sourcetype", "android");

					new NewsImpl().addNewsComment(params, this);
					showProgressToast();
				} else {
					HintToLogin(Constant.LOGIN_COMPLETE);
				}
			} else {// 跳到评价列表
				if (Integer.parseInt(tvNum.getText().toString()) != 0) {
					startActivity(new Intent(mContext,
							InformationCommentListActivity.class).putExtra(
							"id", id).putExtra("commentNum", strNum));
				}
			}
		} else {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
		}
	}
	// 输入框监听
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		if (!TextUtils.isEmpty(s.toString().replaceAll(" ", ""))) {// 发送
			tvNum.setText("发送");
			tvNum.setTextColor(getResources().getColor(R.color.main_color));
			tvNum.setTextSize(18);
			// Drawable leftDrawable = getResources().getDrawable(
			// R.drawable.ic_launcher);
			// leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(),
			// leftDrawable.getMinimumHeight());
			tvNum.setCompoundDrawables(null, null, null, null);
		} else {
			tvNum.setText(strNum);
			tvNum.setTextColor(getResources().getColor(R.color.dark_gray));
			tvNum.setTextSize(16);

			Drawable leftDrawable = getResources().getDrawable(
					R.drawable.ic_comment);
			leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(),
					leftDrawable.getMinimumHeight());
			tvNum.setCompoundDrawables(leftDrawable, null, null, null);
		}
	}

	@Override
	public void result(Object... object) {
		hideProgressToast();
		boolean isSuc = (Boolean) object[1];
		int type = (Integer) object[0];
		switch (type) {
		case HttpParams.ADD_NEWS_COMMENT:// 增加评论
			if (isSuc) {// 成功
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map != null) {
					String msg = (String) map.get("msg");
					if ((Boolean) map.get("flag")) {
						etInfo.setText("");
						etInfo.clearFocus();
						hideSoftInput(etInfo);
						// strNum++;
						// tvEvaluateNum.setText(strNum);
						// tvNum.setText(strNum);

					}
					Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

				} else {
					Toast.makeText(mContext, getString(R.string.service_error),
							Toast.LENGTH_SHORT).show();
				}

			} else {
				Toast.makeText(mContext, getString(R.string.service_error),
						Toast.LENGTH_SHORT).show();
			}

			break;

		default:// 获取资讯详情
			if (isSuc) {
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map != null) {
					if ((Boolean) map.get("flag")) {
						info = (Information) map.get("info");
						if (info != null) {
							dealWithInfo(info);
							mCache.put(INFO_DETAIL_DATA + infoId, info);
						} else {
							Logger.i("资讯详情", map.get("msg"));
						}

					} else {
						Logger.i("资讯详情", map.get("msg"));
					}
				}

			} else {
				Logger.i("资讯详情", object[3]);
			}
			break;
		}

	}

	/**
	 * 处理内容
	 * 
	 * @param infos
	 */
	private void dealWithInfo(Information infos) {

		id = infos.getId();
		// tvTitle.setText(info.getInfoTitle());
		// tvTime.setText(info.getCreateTime());
		// tvType.setText(info.getType());
		// tvFrom.setText("来源：" + info.getSource());
		// tvEvaluateNum
		// .setText("评价：" + info.getEvaluateNum());

		String newTitle = "<div style="
				+ "\"width:100%\""
				+ ">"
				+ "<div style="
				+ "\"width:100%;text-align:center;margin:50px 0 0px 0;line-height:150%;font-size:20px;\""
				+ ">"
				+ infos.getInfoTitle()
				+ "<div/>"
				+ "<div style="
				+ "\"width:100%;text-align:center;border-bottom:1px #E0E0E0 solid;height:38px;line-height:26px;font-size:14px;margin-top:34px;\""
				+ ">" + "时间"+ "<span style="//infos.getType() +
				+ "\"color:#E0E0E0;margin:0 10px 0 10px;\"" + ">|</span>"
				+ infos.getCreateTime() + "<span style="
				+ "\"color:#E0E0E0;margin:0 10px 0 10px;\"" + ">|</span>"
				+ "来源：" + infos.getSource() + "</div></div>";
		//"<span style="
//		+ "\"color:#E0E0E0;margin:0 10px 0 10px;\"" + ">|</span>"
//		+ "评价：" + infos.getEvaluateNum() 

		String strInfo;
		if (infos.getBody() == null) {
			strInfo = "加载内容出现问题！";
		} else {
			strInfo = "<div style="
					+ "\"line-height:150%;font-size:18px;text-align:left;\""
					+ ">" + infos.getBody() + "<div/>";
		}
		String strNewInfo = newTitle + strInfo;
		if (strNewInfo != null) {
			try {
				webDetail.loadDataWithBaseURL(EZTConfig.OFFICIAL_WEBSITE,
						strNewInfo, "text/html", "UTF-8", null);
			} catch (Exception e) {
			}

		}

		strTitle = infos.getInfoTitle();
		strNum = infos.getEvaluateNum();
		tvNum.setText(strNum);
		strInfo = Html.fromHtml(strInfo).toString();
		// 设置分享的内容
		// setShareContent(strInfo, infos.getInfoTitle(),
		// EZTConfig.OFFICIAL_WEBSITE + "/ezt-cms" + infos.getUrl(),
		// infoUrl, null);
		// 2015-12-21 更新资讯分享内容
		setShareContent(strInfo, infos.getInfoTitle(),
				EZTConfig.OFFICIAL_WEBSITE + "/eztcms" + infos.getUrl(),
				infoUrl, null);
		// 初始化推荐医生
		ArrayList<Doctor> docList = infos.getDoclist();
		docAdapter.setList(docList);
	}

	@OnItemClick(R.id.informate_doc_lv)
	private void lvDocItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		if (BaseApplication.getInstance().isNetConnected) {
			String docId = docAdapter.getList().get(position).getId();
			String deptId = docAdapter.getList().get(position).getDocDeptId();
			String deptDocId = docAdapter.getList().get(position)
					.getDeptDocId();
			Intent intent = new Intent(mContext, DoctorIndex30Activity.class)
					.putExtra("deptId", deptId).putExtra("docId", docId)
					.putExtra("deptDocId", deptDocId);
			startActivity(intent);
		} else {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
		}
	}

}
