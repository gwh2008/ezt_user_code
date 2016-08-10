package com.eztcn.user.eztcn.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.discover.SubscriptionActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.InformationColumn;
import com.eztcn.user.eztcn.customView.PagerSlidingTobTab;
import com.eztcn.user.eztcn.fragment.FinalFragment;
import com.eztcn.user.eztcn.fragment.InfomationChildFragment;
import com.eztcn.user.eztcn.impl.NewsImpl;
import com.eztcn.user.eztcn.utils.CacheUtils;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.Logger;
import com.eztcn.user.eztcn.utils.ResourceUtils;

import java.util.ArrayList;
import java.util.Map;

import xutils.http.RequestParams;
import xutils.view.annotation.event.OnClick;

/**
 * @author ezt
 * @title 微资讯 ----》2016-1-4点击进入【健康头条】（原微资讯，现更名为健康头条）
 * @describe
 * @created 2014年12月16日
 */
public class InformationFragment extends FinalFragment implements IHttpResult {

    private ViewPager mPager;

    private PagerSlidingTobTab myPagerTab;

    public LinearLayout columnLayout;

    private ListView lvSubscription;// 已订阅的资讯列表

    private TextView tvInformation;

    private TextView tvSubscription;

    private ImageView imgRight;

    private TextView tvBack;

    private MyPagerAdapter adapter;
    private ArrayList<InformationColumn> columns;
    private final String INFO_COLUMN_DATA = "infoColumnData";// 缓存key-栏目
    private CacheUtils mCache;

    private int flag = 0;// 0发现正常进入，1为成功页面跳入
    private Activity mActivity;
    private View view;

    public static InformationFragment newInstance() {
        InformationFragment fragment = new InformationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        flag = mActivity.getIntent().getIntExtra("flag", 0);
        mCache = CacheUtils.get(mActivity);


//		if (BaseApplication.getInstance().isNetConnected) {
//			((FinalActivity) mActivity).showProgressToast();
//			getColumnData();
//		} else {
//			ArrayList<InformationColumn> cacheColumnData = (ArrayList<InformationColumn>) mCache
//					.getAsObject(INFO_COLUMN_DATA);
//			if (cacheColumnData != null && cacheColumnData.size() != 0) {
//				columns = cacheColumnData;
//				columnLayout.setVisibility(View.VISIBLE);
//				initialTitleTab();
//			} else {
//				Toast.makeText(mActivity, getString(R.string.network_hint),
//						Toast.LENGTH_SHORT).show();
//			}
//
//		}


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // 避免UI重新加载
        if (view == null) {
            view = inflater.inflate(R.layout.activity_information_new, null);
            initial();
        }

        if (BaseApplication.getInstance().isNetConnected) {
            ((FinalActivity) mActivity).showProgressToast();
            getColumnData();
        } else {
            ArrayList<InformationColumn> cacheColumnData = (ArrayList<InformationColumn>) mCache
                    .getAsObject(INFO_COLUMN_DATA);
            if (cacheColumnData != null && cacheColumnData.size() != 0) {
                columns = cacheColumnData;
                columnLayout.setVisibility(View.VISIBLE);
                if (null == adapter)
                    initialTitleTab();
            } else {
                Toast.makeText(mActivity, getString(R.string.network_hint),
                        Toast.LENGTH_SHORT).show();
            }

        }

        // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        return view;

    }

    private void initial() {
        mPager = (ViewPager) view.findViewById(R.id.information_vpager);
        myPagerTab = (PagerSlidingTobTab) view.findViewById(R.id.tabs);
        columnLayout = (LinearLayout) view
                .findViewById(R.id.title_column_layout);
        lvSubscription = (ListView) view.findViewById(R.id.subscription_lt);// 已订阅的资讯列表
        tvInformation = (TextView) view.findViewById(R.id.information_tv);// 已订阅的资讯列表
        tvSubscription = (TextView) view.findViewById(R.id.subscription_tv);
        imgRight = (ImageView) view.findViewById(R.id.information_right_btn);
        tvBack = (TextView) view.findViewById(R.id.left_btn);
        tvBack.setVisibility(View.GONE);

    }

    /**
     * 初始化导航栏数据
     */
    private void getColumnData() {
        // HashMap<String, Object> params = new HashMap<String, Object>();
        RequestParams params = new RequestParams();
//		JSONObject data=new JSONObject();
//		JSONObject obj=new JSONObject();
//
//		try {
//			obj.put("userID", 18);
//			obj.put("pageNumber", 1);
//			data.put("data", obj);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		params.addBodyParameter("data",data.toString());
//		params.setContentType("text/json");

        new NewsImpl().getNewsColumn(params, this);
    }

    /**
     * 实例化顶部导航栏
     */
    private void initialTitleTab() {
        adapter = new MyPagerAdapter(
                ((FinalActivity) mActivity).getSupportFragmentManager());
        mPager.setAdapter(adapter);
        // final int pageMargin = (int) TypedValue.applyDimension(
        // TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
        // .getDisplayMetrics());
        // mPager.setPageMargin(pageMargin);
        myPagerTab.setTextSize(ResourceUtils.dip2px(mActivity,
                ResourceUtils.getXmlDef(mActivity, R.dimen.medium_size)));
        myPagerTab.setIndicatorColorResource(R.color.main_color);
        myPagerTab.setTabTextSelectColor(getResources().getColor(
                R.color.main_color));
        myPagerTab.setIndicatorHeight(7);
        myPagerTab.setDividerColor(getResources().getColor(
                android.R.color.transparent));
        myPagerTab.setUnderlineHeight(1);
        myPagerTab.setUnderlineColorResource(R.color.dark_gray);
        myPagerTab.setTabPaddingLeftRight(ResourceUtils.dip2px(mActivity,
                ResourceUtils.getXmlDef(mActivity, R.dimen.large_margin)));
        myPagerTab.setIndicatorMargin(ResourceUtils.dip2px(mActivity,
                ResourceUtils.getXmlDef(mActivity, R.dimen.medium_margin)));

        // shouldExpand currentPositionOffset
        myPagerTab.setViewPager(mPager);

    }

    @OnClick(R.id.information_tv)
    private void inforClick(View v) {
        // 微资讯
        mPager.setVisibility(View.VISIBLE);
        lvSubscription.setVisibility(View.GONE);
    }

    @OnClick(R.id.subscription_tv)
    private void subscription(View v) {
        // 已订阅
        mPager.setVisibility(View.GONE);
        lvSubscription.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.information_right_btn)
    private void inforright(View v) {
        // 订阅
        startActivityForResult(
                new Intent(mActivity, SubscriptionActivity.class), 1);
    }

    @Override
    public void result(Object... object) {
        int type = (Integer) object[0];
        boolean isSuc = (Boolean) object[1];
        switch (type) {
            case HttpParams.GET_NEWS_COLUMN:// 获取资讯栏目
                if (isSuc) {
                    Map<String, Object> map = (Map<String, Object>) object[2];
                    if (map != null && map.size() != 0) {
                        if ((Boolean) map.get("flag")) {
                            columns = (ArrayList<InformationColumn>) map
                                    .get("columns");
                            if (columns != null && columns.size() != 0) {
                                // columnLayout.setVisibility(View.VISIBLE);
                                mCache.put(INFO_COLUMN_DATA, columns);
                                if (getActivity() != null) {
                                    initialTitleTab();
                                }
                            }

                        } else {

                            Logger.i("资讯栏目", map.get("msg"));
                        }
                    }

                } else {
                    ((FinalActivity) mActivity).hideProgressToast();
                    Logger.i("资讯栏目", object[3]);
                }
                break;

        }

    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // return columns.get(position).getInfoName();
            return columns.get(0).getInfoName();
        }

        @Override
        public int getCount() {
            // return columns.size();
            return 1;
        }

        @Override
        public Fragment getItem(int position) {
            // return InfomationChildFragment.newInstance(columns.get(position)
            // .getId(), position,columnLayout);
            return InfomationChildFragment.newInstance(columns.get(0).getId(),
                    position, columnLayout);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 11) {// 订阅确定返回
            String index = data.getStringExtra("info");
            Toast.makeText(mActivity, index, Toast.LENGTH_SHORT).show();
        }

    }

}
