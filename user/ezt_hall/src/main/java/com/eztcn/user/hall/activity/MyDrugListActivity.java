package com.eztcn.user.hall.activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import com.eztcn.user.R;
/**
 * Created by lx on 2016/6/3.
 * 我的药品清单界面
 */
public class MyDrugListActivity extends  BaseActivity {

    private TextView top_right;
    @Override
    protected int preView() {
        return R.layout.new_activity_my_drug_list;
    }

    @Override
    protected void initView() {
        top_right=loadTitleBar(true,"药品清单","添加药品");
        top_right.setTextColor(getResources().getColor(R.color.border_line));
        top_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,OrderDrugActivity.class));
            }
        });
    }

    @Override
    protected void initData() {

    }
}
