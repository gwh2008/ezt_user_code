package com.eztcn.user.hall.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.hall.common.Constant;
import com.eztcn.user.hall.utils.DensityUtils;
import com.eztcn.user.hall.utils.ToastUtils;
import com.eztcn.user.hall.views.RecycleViewDivider;

/**
 * Created by lx on 2016/6/3.
 * 我的预约列表界面。
 */
public class MyRegistrationListActivity extends BaseActivity {

    private RecyclerView my_registration_recycleView;
    private RecyclerAdapter recyclerAdapter=new RecyclerAdapter();
    private TextView top_right;
    @Override
    protected int preView() {
        return R.layout.new_activity_my_registration_list;
    }

    @Override
    protected void initView() {
        top_right=loadTitleBar(true,"我的预约","添加预约");
        top_right.setTextColor(getResources().getColor(R.color.text_color_deep_gray));
        top_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,AppointmentMainActivity.class)
                        .putExtra("cityData", Constant.cityListDataResponse));
            }
        });
        my_registration_recycleView= (RecyclerView) this.findViewById(R.id.my_registration_recycleView);
        my_registration_recycleView.setLayoutManager(new LinearLayoutManager(this));
        my_registration_recycleView.setAdapter(recyclerAdapter);
        my_registration_recycleView.addItemDecoration(
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
                    R.layout.new_item_my_registration, parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext,MyRegistrationDetailsActivity.class));
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
