package com.eztcn.user.hall.fragment.dragonCard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eztcn.user.R;
import com.eztcn.user.hall.activity.SelectAppointmentTimeActivity;
import com.eztcn.user.hall.adapter.BaseRvAdapter;
import com.eztcn.user.hall.fragment.BaseFragment;
import com.eztcn.user.hall.utils.ToastUtils;
import com.eztcn.user.hall.views.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lizhipeng
 * @Data: 16/6/7 上午10:16
 * @Description: 指导挂号
 */
public class MyPrivilegeGuideNumFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private BaseRvAdapter mAdapter;
    private List<String> mDatas;

    public static MyPrivilegeGuideNumFragment newInstance() {
        MyPrivilegeGuideNumFragment fragment = new MyPrivilegeGuideNumFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_fragment_my_privilege, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.new_my_privilege_fragment_list_view);
        mAdapter = new BaseRvAdapter(mRecyclerView.getContext(),R.layout.new_item_privilege_guide_num_rv, mDatas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mRecyclerView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL, 20));
        mRecyclerView.setAdapter(mAdapter);
        //禁用上层ScrollView的滑动事件，解决嵌套冲突
        mRecyclerView.setNestedScrollingEnabled(false);
        mInitOver = true;
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    protected void onFragmentFirstResume() {
        showProgressDialog("");
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (mInitOver) {
                    mDatas = new ArrayList<>();
                    for (int i = 0; i < 100; i++) {
                        mDatas.add("这是第====" + i + "====条");
                    }
                    mAdapter.notifyDataSetChanged(mDatas);
                    dismissProgressDialog();
                    //item 点击监听
                    mAdapter.setOnItemClickLitener(new BaseRvAdapter.OnItemClickLitener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            ToastUtils.shortToast(mContext,"咨询详情页面没写");
                        }
                    });
                }
            }
        }, 1000); //延迟1秒
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
