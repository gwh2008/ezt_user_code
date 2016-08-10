package com.eztcn.user.eztcn.activity.mine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnItemClick;
import xutils.view.annotation.event.OnItemLongClick;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.MyMedicalRecordAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.MedicalRecord;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.PullToRefreshListView;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnLoadMoreListener;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnRefreshListener;
import com.eztcn.user.eztcn.impl.MedicalRecordImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.Logger;
/**
 * @title 我的病历
 * @describe
 * @author ezt
 * @created 2014年12月12日
 */
public class MyMedicalRecordListActivity extends FinalActivity implements
		OnClickListener, IHttpResult, 
		OnLoadMoreListener, OnRefreshListener {
	@ViewInject(R.id.medicalRecord)
	private PullToRefreshListView lv;
	private TextView textAdd;
	@ViewInject(R.id.none_layout)
	private LinearLayout noneLayout;
	@ViewInject(R.id.none_txt)
	private TextView noneTv;
	private MyMedicalRecordAdapter adapter;
	private ArrayList<MedicalRecord> rList;

	private int enterType;// （22、选择病历）
	private String patientId;// 患者id
	private int delIndex;// 选中删除病历下标
	private int currentPage = 1;// 当前页数
	private int pageSize = EZTConfig.PAGE_SIZE;// 每页条数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mymedicalrecord_list);
		ViewUtils.inject(MyMedicalRecordListActivity.this);
		noneTv.setText("暂无病历");
		textAdd = loadTitleBar(true, "我的病历", "新建");
		textAdd.setVisibility(View.GONE);
		textAdd.setOnClickListener(this);
		// isChoice = getIntent().getBooleanExtra("isChoice", false);
		adapter = new MyMedicalRecordAdapter(this);
		lv.setAdapter(adapter);
//		lv.setOnItemLongClickListener(this);
		lv.setCanLoadMore(true);
		lv.setCanRefresh(true);
		lv.setAutoLoadMore(true);
		lv.setMoveToFirstItemAfterRefresh(false);
		lv.setDoRefreshOnUIChanged(false);
		lv.setOnLoadListener(this);
		lv.setOnRefreshListener(this);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			enterType = bundle.getInt("enterType");
			patientId = bundle.getString("patientId");
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (BaseApplication.getInstance().isNetConnected) {
			getMedicalRecord();
			showProgressToast();
		} else {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
		}

	}

	public void onClick(View v) {// 添加病历
		
		Toast.makeText(MyMedicalRecordListActivity.this, "新建病历", Toast.LENGTH_SHORT).show();
		startActivityForResult(new Intent(mContext,
				MyMedicalRecordCreateOneActivity.class), 1);
	}

	/**
	 * 删除病历
	 * 
	 * @param id
	 */
	public void delRecord(String id) {
//		HashMap<String, Object> params = new HashMap<String, Object>();
		RequestParams params=new RequestParams();
		params.addBodyParameter("mrId", id);
		MedicalRecordImpl impl = new MedicalRecordImpl();
		impl.delMyIll(params, this);
	}

	/**
	 * 获取病历列表
	 */
	public void getMedicalRecord() {
		if (BaseApplication.getInstance().patient != null) {
			RequestParams params=new RequestParams();
			params.addBodyParameter("userId",
					BaseApplication.getInstance().patient.getUserId() + "");
			params.addBodyParameter("rowsPerPage", pageSize+ "");
			params.addBodyParameter("page", currentPage+ "");
			new MedicalRecordImpl().getMyIllRecords(params, this);
		}
	}

	
	@OnItemClick(R.id.medicalRecord)
	private void medicalRecordItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		position = position - 1;
		if (enterType == 22) {// 选择病历

			String patientid = adapter.getList().get(position).getPatientId();
			if (patientId.equals(patientid)) {
				Intent intent = new Intent();
				intent.putExtra("record", adapter.getList().get(position));
				setResult(22, intent);
				finish();
			} else {
				Toast.makeText(mContext, "请选择就诊人对应病历", Toast.LENGTH_SHORT).show();
			}

		} else {
			MedicalRecord record = adapter.getList().get(position);
			String recordId = record.getId();
			String recordNum = record.getRecordNum();
			startActivity(new Intent(MyMedicalRecordListActivity.this,
					MRDetailActivity.class).putExtra("recordId", recordId)
					.putExtra("recordNum", recordNum));
		}
	}
	/**
	 * listview item点击事件
	 * 
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 */
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//		position = position - 1;
//		if (enterType == 22) {// 选择病历
//
//			String patientid = adapter.getList().get(position).getPatientId();
//			if (patientId.equals(patientid)) {
//				Intent intent = new Intent();
//				intent.putExtra("record", adapter.getList().get(position));
//				setResult(22, intent);
//				finish();
//			} else {
//				Toast.makeText(mContext, "请选择就诊人对应病历", Toast.LENGTH_SHORT).show();
//			}
//
//		} else {
//			MedicalRecord record = adapter.getList().get(position);
//			String recordId = record.getId();
//			String recordNum = record.getRecordNum();
//			startActivity(new Intent(MyMedicalRecordListActivity.this,
//					MRDetailActivity.class).putExtra("recordId", recordId)
//					.putExtra("recordNum", recordNum));
//		}
//
//	}

	@Override
	protected void onActivityResult(int request, int result, Intent intent) {
		super.onActivityResult(request, result, intent);

		if (request == 1 && result == 11) {// 添加病历
			showProgressToast();
			onRefresh();
		} else if (request == 2 && result == 11) {// 编辑病历

		}

	}

	@Override
	public void result(Object... object) {
		int type = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];

		switch (type) {
		case HttpParams.GET_MY_ILL_RECORDS:// 获取我的病历列表
			ArrayList<MedicalRecord> data = null;
			if (isSuc) {
				rList = (ArrayList<MedicalRecord>) object[2];
				if (rList != null && rList.size() > 0) {
					lv.setVisibility(View.VISIBLE);
					if (currentPage == 1) {// 第一次加载或刷新
						data = rList;
						if (rList.size() < pageSize) {
							lv.setAutoLoadMore(false);
							lv.onLoadMoreComplete();
						}
						lv.onRefreshComplete();

					} else {// 加载更多
						data = (ArrayList<MedicalRecord>) adapter.getList();
						if (data == null || data.size() <= 0)
							data = rList;
						else
							data.addAll(rList);

						if (rList.size() < pageSize) {
							lv.setAutoLoadMore(false);
						}
						lv.onLoadMoreComplete();

					}
					adapter.setList(data);

				} else {
					if (adapter.getList() != null) {// 加载
						lv.setAutoLoadMore(false);
						lv.onLoadMoreComplete();
						data = (ArrayList<MedicalRecord>) adapter.getList();
					} else {// 刷新
						lv.onRefreshComplete();
						noneLayout.setVisibility(View.VISIBLE);
					}
				}
				if (data != null) {
					rList = data;
				}

			} else {
				Logger.i("获取我的病历列表", object[3]);
			}
			textAdd.setVisibility(View.VISIBLE);
			hideProgressToast();
			break;

		case HttpParams.DEL_MY_ILL:// 删除病历
			if (isSuc) {
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map == null) {
					Toast.makeText(mContext, getString(R.string.request_fail),
							Toast.LENGTH_SHORT).show();
					return;
				}
				boolean flag = (Boolean) map.get("flag");
				if (flag) {
					Toast.makeText(mContext, "删除病历成功！", Toast.LENGTH_SHORT);
					adapter.getList().remove(delIndex);
					adapter.notifyDataSetChanged();
					if (adapter.getList().size() == 0) {
						lv.setVisibility(View.GONE);
						noneLayout.setVisibility(View.VISIBLE);
					}
				} else {
					Toast.makeText(mContext, map.get("msg") == null ? "删除病历失败！"
							: map.get("msg").toString(), Toast.LENGTH_SHORT);
				}

			} else {
				Toast.makeText(mContext, getString(R.string.service_error),
						Toast.LENGTH_SHORT).show();
			}
			hideProgressToast();

			break;
		}
	}

//	@Override
//	public boolean onItemLongClick(AdapterView<?> parent, View view,
//			final int position, long id) {
		
		@OnItemLongClick(R.id.medicalRecord)
		public boolean itemLongClick(AdapterView<?> parent, View view,
				final int position, long id) {
		delIndex = position - 1;
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setIcon(android.R.drawable.ic_dialog_info).setTitle("提示")
				.setMessage("确定删除该病历？").setCancelable(false)
				.setNegativeButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						List<MedicalRecord> list = adapter.getList();
						String id = list.get(delIndex).getId();
						delRecord(id);
						showProgressToast();
					}
				}).setPositiveButton("取消", null);

		AlertDialog dialog = builder.create();
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (dialog != null) {
						dialog.dismiss();
					}
				}
				return false;
			}
		});
		dialog.show();

		return true;
	}

	@Override
	public void onRefresh() {
		currentPage = 1;// 还原页值
		lv.setAutoLoadMore(true);
		getMedicalRecord();
	}

	@Override
	public void onLoadMore() {
		if (rList != null) {
			if (rList.size() < pageSize) {
				lv.setAutoLoadMore(false);
				lv.onLoadMoreComplete();

			} else {
				currentPage++;
				getMedicalRecord();
			}
		}

	}
}
