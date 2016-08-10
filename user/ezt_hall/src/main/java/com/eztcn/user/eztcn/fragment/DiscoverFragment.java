package com.eztcn.user.eztcn.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.discover.BigDataReadActivity;
import com.eztcn.user.eztcn.activity.discover.InformationActivity;
import com.eztcn.user.eztcn.activity.discover.RimHospitalActivity;
import com.eztcn.user.eztcn.utils.CommonUtil;
import com.zxing.activity.QrcodeActivity;
/**
 * @title 发现
 * @describe
 * @author ezt
 * @created 2014年12月16日
 */
public class DiscoverFragment extends FinalFragment implements OnClickListener {
	private View rootView;// 缓存Fragment view
	private RelativeLayout layoutInformation, layoutRimHos, layoutScan,
			layoutShopping;
	private TextView tvTitle;//
	private ImageView imgRead;// 大数据阅读

	public static DiscoverFragment newInstance() {
		DiscoverFragment fragment = new DiscoverFragment();
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 避免UI重新加载
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_discover, null);
			initialView();

		}
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	/**
	 * 初始化界面
	 */
	private void initialView() {
		layoutShopping = (RelativeLayout) rootView
				.findViewById(R.id.discover_shopping_layout);// 微商城

		layoutInformation = (RelativeLayout) rootView
				.findViewById(R.id.discover_information_layout);// 微资讯
		layoutRimHos = (RelativeLayout) rootView
				.findViewById(R.id.discover_rim_hos_layout);// 周边医院
		layoutScan = (RelativeLayout) rootView
				.findViewById(R.id.discover_scan_layout);// 扫一扫
		tvTitle = (TextView) rootView.findViewById(R.id.title_tv);// 标题

		imgRead = (ImageView) rootView.findViewById(R.id.read_img);// 大数据
		tvTitle.setText("发现");
		layoutInformation.setOnClickListener(this);
		layoutRimHos.setOnClickListener(this);
		layoutScan.setOnClickListener(this);
		layoutShopping.setOnClickListener(this);
		imgRead.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.discover_information_layout:// 微资讯
			startActivity(new Intent(getActivity(), InformationActivity.class));
			break;

		case R.id.discover_rim_hos_layout:// 周边医院
			startActivity(new Intent(getActivity(), RimHospitalActivity.class));
			break;

		case R.id.discover_scan_layout:// 扫一扫

			if (CommonUtil.isCameraCanUse()) {// 摄像头可使用
				Intent intent = new Intent(getActivity(), QrcodeActivity.class);
				getActivity().startActivityForResult(intent, 0);
			} else {
				String msg = getResources().getString(R.string.scanner_hint);
				hintLargeDistance(msg);
			}

			break;

		case R.id.discover_shopping_layout:// 微商城
			Toast.makeText(getActivity(), getString(R.string.function_hint),
					Toast.LENGTH_SHORT).show();
			// startActivity(new Intent(getActivity(),
			// MicroStoreActivity.class));
			break;

		case R.id.read_img:// 阅读
			getActivity().startActivity(
					new Intent(getActivity(), BigDataReadActivity.class));
			break;
		}

	}

	/**
	 * 权限管理跳转温馨提示
	 */
	public void hintLargeDistance(String msg) {
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setIcon(android.R.drawable.ic_dialog_info).setTitle("温馨提示")
				.setMessage(msg).setCancelable(false)
				.setNegativeButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						((FinalActivity) getActivity()).toPowerManager();
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
	}
}
