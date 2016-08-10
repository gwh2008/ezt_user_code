package com.eztcn.user.eztcn.fragment;

import java.util.ArrayList;
import java.util.Map;

import xutils.http.RequestParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.fdoc.ForeignPatient_TPDetailsActivity;
import com.eztcn.user.eztcn.activity.fdoc.TumourServicePackageActivity;
import com.eztcn.user.eztcn.adapter.F_TumourPackageAdapter;
import com.eztcn.user.eztcn.adapter.F_TumourPackageAdapter.IClick;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.ForeignPatient_Service;
import com.eztcn.user.eztcn.impl.ForeignPatientImpl;

/**
 * @title 外患服务套餐
 * @describe
 * @author ezt
 * @created 2015年3月12日
 */
public class TumourPackageFragment extends FinalFragment implements IClick,
		OnItemClickListener, IHttpResult {

	private Activity mActivity;
	private String id;// id
	private String deptId;
	private GridView gt;
	private F_TumourPackageAdapter adapter;

	public static TumourPackageFragment newInstance(String id,String deptId) {
		TumourPackageFragment fragment = new TumourPackageFragment();
		Bundle b = new Bundle();
		b.putString("id", id);
		b.putString("deptId", deptId);
		fragment.setArguments(b);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
		id = getArguments().getString("id");
		deptId=getArguments().getString("deptId");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// 避免UI重新加载
		if (gt == null) {
			gt = new GridView(mActivity);
			gt.setOnItemClickListener(this);
			gt.setSelector(android.R.color.transparent);
			gt.setNumColumns(GridView.AUTO_FIT);
			// gt.setVerticalSpacing(ResourceUtils.getXmlDef(mActivity,
			// R.dimen.large_margin));
			adapter = new F_TumourPackageAdapter(mActivity);
			adapter.adapterClick(this);
			gt.setAdapter(adapter);
		}
		if (adapter.getList() == null) {
			initialData();
		}

		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) gt.getParent();
		if (parent != null) {
			parent.removeView(gt);
		}
		return gt;
	}

	/**
	 * 获取数据
	 */
	private void initialData() {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("type", id);
//		IForeignPatient api = new ForeignPatientImpl();
		RequestParams params=new RequestParams();
		params.addBodyParameter("type", id);
		ForeignPatientImpl api = new ForeignPatientImpl();
		api.getTrapackage_list(params, this);
		((TumourServicePackageActivity) mActivity).showProgressToast();
	}

	@Override
	public void Click(int pos) {// 添加购物车

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		String strName = adapter.getList().get(position).getTitle();
		String strId = adapter.getList().get(position).getId();
		startActivity(new Intent(mActivity,
				ForeignPatient_TPDetailsActivity.class).putExtra("title",
				strName).putExtra("id", strId).putExtra("deptId", deptId));

	}

	@Override
	public void result(Object... object) {

		boolean isSuc = (Boolean) object[1];

		if (isSuc) {
			Map<String, Object> map = (Map<String, Object>) object[2];
			if (map != null) {
				boolean flag = (Boolean) map.get("flag");
				if (flag) {
					ArrayList<ForeignPatient_Service> list = (ArrayList<ForeignPatient_Service>) map
							.get("data");
					adapter.setList(list);

				} else {
					Toast.makeText(mActivity, map.get("msg").toString(),
							Toast.LENGTH_SHORT).show();
				}

			} else {
				Toast.makeText(mActivity, getString(R.string.request_fail),
						Toast.LENGTH_SHORT).show();
			}

		} else {
			Toast.makeText(mActivity, getString(R.string.service_error),
					Toast.LENGTH_SHORT).show();
		}

		((TumourServicePackageActivity) mActivity).hideProgressToast();

	}

}