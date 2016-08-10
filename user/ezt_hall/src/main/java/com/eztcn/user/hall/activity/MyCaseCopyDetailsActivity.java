package com.eztcn.user.hall.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eztcn.user.R;
import com.eztcn.user.hall.views.RecycleViewDivider;

/**
 * Created by lx on 2016/6/2.
 * 我的复印详情界面。
 */
public class MyCaseCopyDetailsActivity extends BaseActivity {
    private RecyclerView recycleView;
    private RecyclerAdapter recyclerAdapter=new RecyclerAdapter();
    @Override
    protected int preView() {
        return R.layout.new_activity_my_case_copy_details;
    }

    @Override
    protected void initView() {
        loadTitleBar(true,"复印详情",null);
        recycleView= (RecyclerView) this.findViewById(R.id.copy_case_list);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setAdapter(recyclerAdapter);
        recycleView.addItemDecoration(
                new RecycleViewDivider(mContext,LinearLayoutManager.VERTICAL, 2));

    }

    @Override
    protected void initData() {

    }
    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>
    {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(
                    R.layout.new_item_mycase_copy, parent,false));//该item布局是错误的 ，是临时看效果的，需要修改
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position)
        {
        }

        @Override
        public int getItemCount()
        {
            return 20;
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {

            public MyViewHolder(View view)
            {
                super(view);
            }
        }
    }
}
