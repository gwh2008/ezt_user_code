package com.eztcn.user.hall.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.hall.activity.WebViewActivity;
import com.eztcn.user.hall.adapter.BaseCommonRvAdapter;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.OneThousandRequest;
import com.eztcn.user.hall.model.OneThousandResponse;
import com.eztcn.user.hall.utils.GlideUtils;
import com.eztcn.user.hall.utils.ToastUtils;
import com.eztcn.user.hall.views.EztBannerView;
import com.eztcn.user.hall.views.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lizhipeng
 * @Data: 16/5/4 下午2:22
 * @Description: 一千零医夜 界面
 */
public class OneThousandAusleseFragment extends BaseFragment implements ResultSubscriber.OnResultListener {

    private static final int REQUEST_DATA = 10001;//请求数据列表，后台默认每页10条


    private int mPageNumber = 1;
    private RecyclerView mRecyclerView;
    private BaseCommonRvAdapter mAdapter;
    private EztBannerView mEztBannerView;
    private List<OneThousandResponse.DataBean> mDatas = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isMore = true;

    public static OneThousandAusleseFragment newInstance() {
        OneThousandAusleseFragment fragment = new OneThousandAusleseFragment();

        return fragment;
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_fragment_one_thousand_all, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.new_one_thousand_all_fragment_srl);
        final NestedScrollView nestedScrollView = (NestedScrollView) view.findViewById(R.id.new_one_thousand_all_fragment_nsv);
        mEztBannerView = (EztBannerView) view.findViewById(R.id.new_one_thousand_all_fragment_ebv);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.new_one_thousand_all_fragment_list_view);
//        mAdapter = new OneThousandDoctorRVAdapter(mRecyclerView.getContext(),R.layout.new_item_one_thousand_doctor_rv, mDatas);
        initAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mRecyclerView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL, 20));
        mRecyclerView.setAdapter(mAdapter);
        //禁用上层ScrollView的滑动事件，解决嵌套冲突
        mRecyclerView.setNestedScrollingEnabled(false);
        nestedScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Log.i("onTouch", "" + nestedScrollView.canScrollVertically(1));//是否能向下滚动，false为滚动到底部
//                Log.i("onTouch", "" + nestedScrollView.canScrollVertically(-1));
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!nestedScrollView.canScrollVertically(1)) {
                            if (isMore) {
                                mPageNumber++;
                                loadData(true);
                            }else {
                                ToastUtils.shortToast(getContext(),"已加载全部数据");
                            }
                        }
                        break;
                }
                return false;
            }
        });
        mInitOver = true;
        return view;
    }

    private void initAdapter() {
        mAdapter = new BaseCommonRvAdapter<OneThousandResponse.DataBean>(getContext(), R.layout.new_item_one_thousand_doctor_rv, mDatas) {
            @Override
            public void convert(BaseCommonRvAdapter.ViewHolder holder, OneThousandResponse.DataBean dataBean) {
                TextView title = holder.getView(R.id.new_item_one_thousand_doctor_rv_tv_title);
                TextView sub_title = holder.getView(R.id.new_item_one_thousand_doctor_rv_tv_sub_title);
                ImageView img = holder.getView(R.id.new_item_one_thousand_doctor_rv_iv_img);

                title.setText(dataBean.getTitle());
                sub_title.setText(dataBean.getSubTitle());
                GlideUtils.into(getContext(), HTTPHelper.BASE_PATH_IMG + dataBean.getImageUrl(), img);
            }
        };
        mAdapter.setOnItemClickListener(new BaseCommonRvAdapter.OnItemClickListener<OneThousandResponse.DataBean>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, OneThousandResponse.DataBean dataBean, int position) {
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("infoUrl", HTTPHelper.BASE_PATH_ARTICLE + dataBean.getArticleUrl());
                intent.putExtra("title", "一千零医夜");
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, OneThousandResponse.DataBean dataBean, int position) {
                return false;
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //设置布局文件
        mEztBannerView.setItemView(R.layout.new_item_one_thousand_banner, 4);
        final List<String> titles = new ArrayList<>();
        titles.add("从“亚健康”到“健康”只需6件小事");
        titles.add("腹泻高发季 宝宝拉肚子千万别犯这些错");
        titles.add("烈日炎炎时刻警惕防晒八大误区");
        titles.add("想要“瓷肌肤”远离这些坏习惯");
        final List<String> htmlUrls = new ArrayList<>();
        htmlUrls.add("file:///android_asset/one_thousand_banner/banner_1/one_thousand_1.htm");
        htmlUrls.add("file:///android_asset/one_thousand_banner/banner_2/one_thousand_2.htm");
        htmlUrls.add("file:///android_asset/one_thousand_banner/banner_3/one_thousand_3.htm");
        htmlUrls.add("file:///android_asset/one_thousand_banner/banner_4/one_thousand_4.htm");
        mEztBannerView.setOnBannerClickListener(new EztBannerView.OnBannerClickListener() {
            @Override
            public void onBannerClick(View v, int position) {
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("infoUrl", htmlUrls.get(position % htmlUrls.size()));
                intent.putExtra("title", "一千零医夜");
                startActivity(intent);
            }
        });
        final List<Integer> imageIds = new ArrayList<>();
        imageIds.add(R.drawable.new_one_thousand_1);
        imageIds.add(R.drawable.new_one_thousand_2);
        imageIds.add(R.drawable.new_one_thousand_3);
        imageIds.add(R.drawable.new_one_thousand_4);
        mEztBannerView.setInitItemViewListener(new EztBannerView.InitItemViewListener() {
            @Override
            public void initItemView(View v, int position) {
                ImageView imageView = (ImageView) v.findViewById(R.id.new_item_one_thousand_banner_iv);
                TextView textView = (TextView) v.findViewById(R.id.new_item_one_thousand_banner_tv);
//                textView.setVisibility(View.GONE);
                GlideUtils.into(getContext(), imageIds.get(position % imageIds.size()), imageView);
//                textView.setBackgroundResource(R.drawable.new_bg_60);
                textView.setText(titles.get(position % titles.size()));

            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNumber = 1;
                loadData(false);
            }
        });

    }

    @Override
    protected void onFragmentFirstResume() {
        loadData(true);
    }

    private void loadData(boolean isShow) {
        if (isShow){
            showProgressToast();
        }
        OneThousandRequest request = new OneThousandRequest();
        OneThousandRequest.DataBean bean = new OneThousandRequest.DataBean();
        bean.setPageNumber("" + mPageNumber);
        bean.setUserID("0");
        request.setData(bean);
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.DATA_REPOSITORY).postOneThousandAll(request, REQUEST_DATA, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mEztBannerView.stopScroll();
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mEztBannerView.startScroll();
    }

    @Override
    public void onStart(int requestType) {
//        showProgressDialog("正在获取数据...");

    }

    @Override
    public void onCompleted(int requestType) {
        dismissProgressDialog();
        hideProgressToast();
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onError(int requestType) {
        dismissProgressDialog();
        hideProgressToast();
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onNext(IModel t, int requestType) {
        switch (requestType) {
            case REQUEST_DATA:
                OneThousandResponse response = (OneThousandResponse) t;
                if (mDatas.size() > 0 && mPageNumber != 1) {
                    mDatas.addAll(response.getData());
                } else {
                    mDatas = response.getData();
                }
                if (response.getData().size() == 0) {
                    isMore = false;
                }
                mAdapter.notifyDataSetChanged(mDatas);
                break;
            default:
                break;
        }
    }
}
