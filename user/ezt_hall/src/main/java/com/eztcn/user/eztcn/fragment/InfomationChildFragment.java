package com.eztcn.user.eztcn.fragment;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;

import xutils.http.RequestParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.discover.InformationDetailActivity;
import com.eztcn.user.eztcn.adapter.InformationAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Information;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.MyImgScroll;
import com.eztcn.user.eztcn.customView.PullToRefreshListView;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnLoadMoreListener;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnRefreshListener;
import com.eztcn.user.eztcn.impl.NewsImpl;
import com.eztcn.user.eztcn.utils.CacheUtils;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.Logger;
import com.eztcn.user.eztcn.utils.ResourceUtils;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

/**
 * @title 自定义Fragment
 * @describe 微资讯子栏目数据显示
 * @author ezt
 * @created 2015年2月5日
 */
public class InfomationChildFragment extends FinalFragment implements
		OnItemClickListener, IHttpResult, OnLoadMoreListener,
		OnRefreshListener {//OnTouchListener, 

	private static final String ID = "id";
	private String infoColumnId;// 栏目id
	private int currentPage = 1;// 当前页数
	private int pageSize = EZTConfig.PAGE_SIZE;// 每页条数
	private int pos;// 栏目下标

	/**
	 * 广告图
	 */
	private ImageView adsDefault;// 图片未加载出来时显示的图片
	private MyImgScroll adsPager;// 图片容器
	private LinearLayout adsOvalLayout;// 圆点容器

	private InformationAdapter adapter;
	private PullToRefreshListView lt;

	private Activity mActivity;
	private int windowWidth;// 屏幕宽度

	private final String INFO_LIST_DATA = "infoListData";// 缓存key-内容
	private CacheUtils mCache;
	private  LinearLayout columnLayout;

	public static InfomationChildFragment newInstance(String id, int pos,LinearLayout columnLayout) {
		InfomationChildFragment fragment = new InfomationChildFragment();
		Bundle b = new Bundle();
		b.putString(ID, id);
		b.putInt("pos", pos);
		fragment.setArguments(b);
        fragment.columnLayout = columnLayout;
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
		mCache = CacheUtils.get(mActivity);
		infoColumnId = getArguments().getString(ID);
		this.pos = getArguments().getInt("pos");
		DisplayMetrics dm = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		windowWidth = dm.widthPixels;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// 避免UI重新加载
		if (lt == null) {
			lt = new PullToRefreshListView(mActivity);
			initialView();
		}

		if (adapter.getList() == null) {// 获取列表数据

			if (BaseApplication.getInstance().isNetConnected) {
				if (this.pos != 0 && this.pos != 1) {
					FinalActivity.getInstance().showProgressToast();
				}
				getData();// 获取资讯列表
			} else {// 无网络
				ArrayList<Information> infoListCache = (ArrayList<Information>) mCache
						.getAsObject(INFO_LIST_DATA + infoColumnId);
				adapter.setList(infoListCache);
				if (adapter.getList() != null && adapter.getList().size() != 0) {
					lt.setVisibility(View.VISIBLE);

				} else {
					Toast.makeText(mActivity, getString(R.string.network_hint),
							Toast.LENGTH_SHORT).show();
				}

			}
		}
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) lt.getParent();
		if (parent != null) {
			parent.removeView(lt);
		}
		return lt;
	}

	/**
	 * 初始化
	 */
	private void initialView() {
		adapter = new InformationAdapter(mActivity);
		/**
		 * 头部广告图
		 */
		View adsView = LinearLayout.inflate(mActivity,
				R.layout.item_ads_layout, null);
		adsView.setPadding(0, 0, 0, 0);
		adsDefault = (ImageView) adsView.findViewById(R.id.home_loading_img);
		adsPager = (MyImgScroll) adsView.findViewById(R.id.home_img_scroll);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, windowWidth / 8 * 4);
		adsDefault.setLayoutParams(params);
		adsPager.setLayoutParams(params);
		adsOvalLayout = (LinearLayout) adsView
				.findViewById(R.id.home_point_layout);
		// FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
		// LayoutParams.WRAP_CONTENT, ResourceUtils.dip2px(mActivity,
		// ResourceUtils.getXmlDef(mActivity,
		// R.dimen.ads_point_layout_height)),
		// Gravity.BOTTOM | Gravity.RIGHT);

		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, ResourceUtils.dip2px(mActivity,
						ResourceUtils.getXmlDef(mActivity,
								R.dimen.ads_point_layout_height)),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
		layoutParams.setMargins(0, 0, ResourceUtils.dip2px(mActivity,
				ResourceUtils.getXmlDef(mActivity, R.dimen.large_margin)),
				ResourceUtils.dip2px(mActivity, ResourceUtils.getXmlDef(
						mActivity, R.dimen.medium_margin)));
		adsOvalLayout.setLayoutParams(layoutParams);
		adsPager.setmScrollTime(EZTConfig.SCROLL_TIME);

		lt.addHeaderView(adsView);
		lt.setAdapter(adapter);
		lt.setCanLoadMore(true);
		lt.setCanRefresh(true);
		lt.setAutoLoadMore(true);
		lt.setMoveToFirstItemAfterRefresh(false);
		lt.setDoRefreshOnUIChanged(false);
		lt.setOnLoadListener(this);
		lt.setOnRefreshListener(this);
		lt.setOnItemClickListener(this);
		lt.setDivider(getResources().getDrawable(android.R.color.transparent));
		lt.setSelector(getResources().getDrawable(
				R.drawable.selector_listitem_bg));
		lt.setVisibility(View.GONE);
		lt.setAdapter(adapter);

		getAdsList();// 获取广告图数据
	}

	/**
	 * 获取资讯列表数据
	 */
	private void getData() {
		// HashMap<String, Object> params = new HashMap<String, Object>();
		// params.put("listid", infoColumnId);
		// params.put("start", currentPage + "");
		// params.put("pagesize", pageSize + "");

//		RequestParams params = new RequestParams();
//		params.addBodyParameter("listid", infoColumnId);
//		params.addBodyParameter("start", currentPage + "");
//		params.addBodyParameter("pagesize", pageSize + "");

		// INewsApi api = new NewsImpl();

        //这个地方是单独的需要访问二级json数据接口的地方
        RequestParams params = new RequestParams();
		JSONObject data=new JSONObject();
		JSONObject obj=new JSONObject();
        HttpEntity httpEntity=null;
		try {
            //构建二级json数据访问体
            if (BaseApplication.patient != null) {
                obj.put("userID", BaseApplication.patient.getUserId());
            }else{
                obj.put("userID", 0);
            }

			obj.put("pageNumber", currentPage);
			data.put("data", obj);
            httpEntity=new StringEntity(data.toString(),"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		params.setBodyEntity(httpEntity);
		params.setContentType("application/json");//设置网络访问的数据格式

		NewsImpl api = new NewsImpl();
		api.getNewsList(params, this);
	}

	/**
	 * 获取资讯广告图
	 */
	private void getAdsList() {
		ArrayList<String> imgs = new ArrayList<String>();
		String url = "";
		for (int i = 0; i < 3; i++) {
			switch (i) {
			case 0:
				url = R.drawable.info_ad_1 + "";
				break;

			case 1:
				url = R.drawable.info_ad_2 + "";
				break;

			case 2:
				url = R.drawable.info_ad_3 + "";
				break;
			}
			imgs.add(url);
		}
		adsDefault.setVisibility(View.GONE);
		ArrayList<View> listViews = InitAdsViewPager(imgs);
		adsPager.setmListViews(listViews);
		/* 重新加载初始化 */
		// myPager.stopTimer();
		// myPager.oldIndex = 0;
		// myPager.curIndex = 0;
		// ovalLayout.removeAllViews();
		/*------------*/
		adsPager.start(mActivity, adsOvalLayout);
	}

	/**
	 * 初始化图片
	 * 
	 * @throws FileNotFoundException
	 */
	private ArrayList<View> InitAdsViewPager(ArrayList<String> imgs) {
		ArrayList<View> listViews = new ArrayList<View>();
		for (int i = 0; i < imgs.size(); i++) {
			ImageView imageView = new ImageView(mActivity);
//			imageView.setOnTouchListener(this);
			imageView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {// 设置图片点击事件

					if (adsPager.getmListViews() != null) {
						// Toast.makeText(
						// HomeActivity.this,
						// "点击了:"
						// + advList.get(myPager.getCurIndex())
						// .getAdUrl(), Toast.LENGTH_SHORT)
						// .show();
					}
				}
			});
			if (imgs.size() == 0) {// 网络加载图片地址成功
				// finalBitMap.display(imageView, imgs.get(i), defaultBitmap,
				// failBitmap);
			} else {
				imageView.setImageResource(Integer.parseInt(imgs.get(i)));// 本地
			}
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, 300);
			imageView.setLayoutParams(params);
			imageView.setScaleType(ScaleType.FIT_XY);
			listViews.add(imageView);
		}
		return listViews;
	}

	@Override
	public void result(Object... object) {
		int type = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];
		switch (type) {
		case HttpParams.GET_NEWS_LIST:// 获取资讯列表
			// LinearLayout columnLayout = ((InformationActivity)
			// mActivity).columnLayout;
            if (columnLayout!=null&&columnLayout.getVisibility() == View.GONE) {
				columnLayout.setVisibility(View.VISIBLE);
			}
			FinalActivity.getInstance().hideProgressToast();
			if (isSuc) {
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map != null && map.size() != 0) {
					if ((Boolean) map.get("flag")) {
						if (map.containsKey("message")) {// 2015-12-22 提示信息
							String msgStr = map.get("message").toString();
							Toast.makeText(mActivity, msgStr,
									Toast.LENGTH_SHORT).show();
						}
						ArrayList<Information> infoList = (ArrayList<Information>) map
								.get("infoList");
						if (infoList != null && infoList.size() > 0) {

							ArrayList<Information> data = null;
							if (currentPage == 1) {// 第一次加载或刷新
								data = infoList;
								lt.setVisibility(View.VISIBLE);

								if (infoList.size() < 10) {//此处的每次加载的数量设置为10
									lt.setAutoLoadMore(false);
									lt.onLoadMoreComplete();
								}
								lt.onRefreshComplete();
								mCache.put(INFO_LIST_DATA + infoColumnId,
										infoList);

							} else {// 加载更多
								data = (ArrayList<Information>) adapter
										.getList();
								if (data == null || data.size() <= 0)
									data = infoList;
								else
									data.addAll(infoList);

								if (infoList.size() < 10) {//此处的每次加载的数量设置为10
									lt.setAutoLoadMore(false);
								}
								lt.onLoadMoreComplete();
							}
							adapter.setList(data);

						} else {
							if (currentPage != 1) {
								lt.setAutoLoadMore(false);
								lt.onLoadMoreComplete();
							}

						}

					} else {
						Logger.i("资讯列表", map.get("msg"));
						lt.setAutoLoadMore(false);
						lt.onLoadMoreComplete();
						lt.onRefreshComplete();
					}
				}

			} else {
				Logger.i("资讯列表", object[3]);
				lt.setAutoLoadMore(false);
				lt.onLoadMoreComplete();
				lt.onRefreshComplete();
			}

			break;

		default:
			break;
		}

	}

	@Override
	public void onRefresh() {
		lt.setAutoLoadMore(true);
		currentPage = 1;
		getData();
	}

	@Override
	public void onLoadMore() {
		currentPage++;
		getData();
	}

	/**
	 * 2016-4-22 重复starttimer bug修复
	 */
//	private boolean startTimer = true;

//	@Override
//	public boolean onTouch(View v, MotionEvent event) {
//		// || event.getAction() ==
//		// MotionEvent.ACTION_CANCEL||MotionEvent.ACTION_UP
//		if (event.getAction() != MotionEvent.ACTION_DOWN) {
//			if (!startTimer) {
//				adsPager.startTimer();
//				adsPager.requestDisallowInterceptTouchEvent(true);
//				startTimer = true;
//			}
//		} else {
//			if (startTimer) {
//				adsPager.stopTimer();
//				adsPager.requestDisallowInterceptTouchEvent(false);
//				startTimer = false;
//			}
//		}
//		return false;
//	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		position = position - 2;
		ArrayList<Information> lists = (ArrayList<Information>) adapter
				.getList();
		if (lists != null) {
			String infoId = lists.get(position).getId();
			String url = lists.get(position).getArticleUrl();

			startActivity(new Intent(mActivity.getApplicationContext(),
					InformationDetailActivity.class).putExtra("infoId", infoId)
					.putExtra("infoUrl", EZTConfig.WEB_VIEW_BASEULR + url));
		}

	}

}