package com.eztcn.user.eztcn.activity.discover;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;

import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.EvaluateListAdapter;
import com.eztcn.user.eztcn.adapter.EvaluateListAdapter.ReplyCallBack;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.InformateComment;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.PullToRefreshListView;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnLoadMoreListener;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnRefreshListener;
import com.eztcn.user.eztcn.impl.NewsImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.Logger;
import com.eztcn.user.hall.utils.Constant;

/**
 * @title 资讯评价列表
 * @describe
 * @author ezt
 * @created 2014年12月31日
 */
public class InformationCommentListActivity extends FinalActivity implements
		 TextWatcher, IHttpResult, OnLoadMoreListener,
		OnRefreshListener {

	@ViewInject(R.id.evaluate_lv)
	private PullToRefreshListView lvEvaluate;//

	@ViewInject(R.id.evaluate_tv)
	private TextView tvEvaluate;

	@ViewInject(R.id.evaluate_et)
	private EditText etEvaluate;

	private EvaluateListAdapter adapter;

	private int pagesize = EZTConfig.PAGE_SIZE;
	private int page = 1;
	private String id;
	private String commentNum;// 评价条数
	private ArrayList<InformateComment> comments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluate_list);
		ViewUtils.inject(InformationCommentListActivity.this);
		loadTitleBar(true, "读者评论", null);
		etEvaluate.addTextChangedListener(this);
		tvEvaluate.setTextSize(16);
		tvEvaluate.setTextColor(getResources().getColor(R.color.dark_gray));
		adapter = new EvaluateListAdapter(this);
		lvEvaluate.setAdapter(adapter);

		lvEvaluate.setCanLoadMore(true);
		lvEvaluate.setCanRefresh(true);
		lvEvaluate.setAutoLoadMore(true);
		lvEvaluate.setMoveToFirstItemAfterRefresh(false);
		lvEvaluate.setDoRefreshOnUIChanged(false);
		lvEvaluate.setOnLoadListener(this);
		lvEvaluate.setOnRefreshListener(this);

		adapter.Reply(new ReplyCallBack() {
			@Override
			public void onclickReply(int pos) {
				etEvaluate.requestFocus();
				showSoftInput(etEvaluate);
				Toast.makeText(mContext, pos + "----", Toast.LENGTH_SHORT)
						.show();

			}
		});
		id = getIntent().getStringExtra("id");// 资讯id
		commentNum = getIntent().getStringExtra("commentNum");// 评论数
		tvEvaluate.setText(commentNum + " 条");
		initialData();
		showProgressToast();

	}

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
			tvEvaluate.setText("发送");
			tvEvaluate.setTextSize(18);
			tvEvaluate
					.setTextColor(getResources().getColor(R.color.main_color));

		} else {
			tvEvaluate.setText((TextUtils.isEmpty(commentNum) ? "0"
					: commentNum) + " 条");
			tvEvaluate.setTextSize(16);
			tvEvaluate.setTextColor(getResources().getColor(R.color.dark_gray));
		}

	}
	@OnClick(R.id.evaluate_tv)
	private void tvEvaluateClick(View v){
		if ("发送".equals(tvEvaluate.getText().toString().trim())) {
			// etEvaluate.setText("");

			if (BaseApplication.patient != null) {
				String strInfo = etEvaluate.getText().toString();
				strInfo = strInfo.replaceAll(" ", "");
				RequestParams params=new RequestParams();
				params.addBodyParameter("aid", id);
				params.addBodyParameter("userid", BaseApplication.patient.getUserId() + "");
				params.addBodyParameter("username", TextUtils
						.isEmpty(BaseApplication.patient.getEpName()) ? "匿名"
						: BaseApplication.patient.getEpName());
				params.addBodyParameter("content", strInfo);
				params.addBodyParameter("sourcetype", "android");
				
				new NewsImpl().addNewsComment(params, this);
				showProgressToast();
			} else {
				HintToLogin(Constant.LOGIN_COMPLETE);
			}

		}

	}

	/**
	 * 获取数据
	 */
	private void initialData() {
		if (id != null) {
//			HashMap<String, Object> params = new HashMap<String, Object>();
//			params.put("commentid", id);
//			params.put("start", page + "");
//			params.put("pagesize", pagesize + "");
			
			RequestParams params=new RequestParams();
			params.addBodyParameter("commentid", id);
			params.addBodyParameter("start", page + "");
			params.addBodyParameter("pagesize", pagesize + "");
			new NewsImpl().getNewsCommentList(params, this);
		}
	}

	@Override
	public void result(Object... object) {
		hideProgressToast();
		int type = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];
		switch (type) {
		case HttpParams.GET_NEWS_COMMENT_LIST:// 资讯评价列表
			if (isSuc) {
				Map<String, Object> map = (Map<String, Object>) object[2];
				ArrayList<InformateComment> data = null;
				boolean flag = (Boolean) map.get("flag");
				if (flag) {// 成功
					comments = (ArrayList<InformateComment>) map
							.get("comments");

					if (comments != null && comments.size() > 0) {
						if (page == 1) {// 第一次加载或刷新

							data = comments;
							if (comments.size() < pagesize) {
								lvEvaluate.setCanLoadMore(false);
								lvEvaluate.setAutoLoadMore(false);
								lvEvaluate.onLoadMoreComplete();
								lvEvaluate.mEndRootView.setVisibility(View.GONE);
							}
							lvEvaluate.onRefreshComplete();
							lvEvaluate.setVisibility(View.VISIBLE);

						} else {// 加载更多
							data = (ArrayList<InformateComment>) adapter
									.getList();
							if (data == null || data.size() <= 0)
								data = comments;
							else
								data.addAll(comments);

							if (comments.size() < EZTConfig.PAGE_SIZE) {
								lvEvaluate.setAutoLoadMore(false);
								lvEvaluate.setCanLoadMore(false);
								lvEvaluate.onLoadMoreComplete();
								lvEvaluate.mEndRootView.setVisibility(View.GONE);
							} else {
								lvEvaluate.onRefreshComplete();
							}

						}
						adapter.setList(data);

					} else {
						if (page != 1) {
							lvEvaluate.setAutoLoadMore(false);
							lvEvaluate.onLoadMoreComplete();
						}
					}
					if (data != null) {
						comments = data;
					}

				} else {
					Toast.makeText(mContext, getString(R.string.request_fail),
							Toast.LENGTH_SHORT).show();
				}

			} else {
				Logger.i("获取资讯评价列表", object[3].toString());
			}
			break;

		case HttpParams.ADD_NEWS_COMMENT:// 增加评论
			if (isSuc) {// 成功
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map != null) {
					String msg = (String) map.get("msg");
					if ((Boolean) map.get("flag")) {
						etEvaluate.setText("");
						etEvaluate.clearFocus();
						hideSoftInput(etEvaluate);

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

		default:
			break;
		}

	}

	@Override
	public void onRefresh() {
		page = 1;
		lvEvaluate.setAutoLoadMore(true);
		initialData();

	}

	@Override
	public void onLoadMore() {

		if (comments != null) {
			if (comments.size() < pagesize) {
				lvEvaluate.setAutoLoadMore(false);
				lvEvaluate.onLoadMoreComplete();

			} else {
				page++;
				initialData();
			}
		}

	}

}
