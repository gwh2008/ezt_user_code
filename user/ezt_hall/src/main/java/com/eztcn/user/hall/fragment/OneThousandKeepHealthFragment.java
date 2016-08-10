package com.eztcn.user.hall.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eztcn.user.R;
import com.eztcn.user.hall.adapter.OneThousandDoctorRVAdapter;
import com.eztcn.user.hall.views.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lizhipeng
 * @Data: 16/5/4 下午2:22
 * @Description: 养生 界面
 */
public class OneThousandKeepHealthFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private OneThousandDoctorRVAdapter mAdapter;
    private List<String> mDatas;
    public static OneThousandKeepHealthFragment newInstance() {
        OneThousandKeepHealthFragment fragment = new OneThousandKeepHealthFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("fragment","养生ooncreateview");
        mDatas = new ArrayList<>();
        View view = inflater.inflate(R.layout.new_fragment_one_thousand_all, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.new_one_thousand_all_fragment_list_view);
        mAdapter = new OneThousandDoctorRVAdapter(mRecyclerView.getContext(),R.layout.new_item_one_thousand_doctor_rv,mDatas);
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
        Log.i("fragment","养生onActivityCreated");
    }

    @Override
    protected void onFragmentFirstResume() {
        showProgressDialog("");
        Log.i("fragment","养生");
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (mInitOver) {
                    for (int i = 0; i < 100; i++) {
                        mDatas.add("这是第====" + i + "====条");
                    }
                    mAdapter.notifyDataSetChanged(mDatas);
                    dismissProgressDialog();
                    //item 点击监听
                    mAdapter.setOnItemClickLitener(new OneThousandDoctorRVAdapter.OnItemClickLitener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            //TODO 点击事件
                        }
                    });
                }
            }
        }, 3000); //延迟1秒跳转
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("fragment","养生onresume");
    }
}
