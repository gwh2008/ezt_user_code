package com.eztcn.user.hall.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.hall.views.RecycleViewDivider;

/**
 * Created by lx on 2016/6/1.
 * 我的复印病历界面。
 */
public class MyCaseCopyListActivity extends BaseActivity {

    private RecyclerView recycleView;
    private RecyclerAdapter recyclerAdapter=new RecyclerAdapter();
    private TextView top_right;
    @Override
    protected int preView() {
        return R.layout.new_activity_my_case_copylist;
    }

    @Override
    protected void initView() {
        top_right=loadTitleBar(true,"复印清单","添加病历");
        top_right.setTextColor(getResources().getColor(R.color.text_color_gray));
        top_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,OrderCopyCaseActivity.class));
            }
        });
        recycleView= (RecyclerView) this.findViewById(R.id.my_case_copy_list);
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
                    R.layout.new_item_mycase_copy, parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext,MyCaseCopyDetailsActivity.class));
                }
            });
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
