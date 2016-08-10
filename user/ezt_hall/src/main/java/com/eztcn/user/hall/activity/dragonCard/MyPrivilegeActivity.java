package com.eztcn.user.hall.activity.dragonCard;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.hall.activity.BaseActivity;
import com.eztcn.user.hall.adapter.BaseViewPagerAdapter;
import com.eztcn.user.hall.fragment.dragonCard.MyPrivilegeGiveOutNumFragment;
import com.eztcn.user.hall.fragment.dragonCard.MyPrivilegeGuideNumFragment;
import com.eztcn.user.hall.fragment.dragonCard.MyPrivilegeProfessionalGuideFragment;
import com.eztcn.user.hall.utils.LogUtils;
import com.eztcn.user.hall.utils.ToastUtils;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lizhipeng
 * @Data: 16/6/6 下午7:53
 * @Description: 我的特权
 */
public class MyPrivilegeActivity extends BaseActivity implements View.OnClickListener{

    private TextView mBackTv;
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private List<String> mTitleList;
    private ImageView top_right;
    
    @Override
    protected int preView() {
        return R.layout.new_activity_my_privilege;
    }

    @Override
    protected void initView() {
        mBackTv = (TextView) findViewById(R.id.new_my_privilege_activity_left_btn);
        top_right=(ImageView) findViewById(R.id.right_btn1);
        mViewPager = (ViewPager) findViewById(R.id.new_my_privilege_activity_view_pager);
        /**自定义部分属性*/
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.new_my_privilege_activity_tabs);
    }

    @Override
    protected void initData() {
        mBackTv.setOnClickListener(this);
        top_right.setOnClickListener(this);
        mTitleList = new ArrayList<>();
        mTitleList.add("放号提醒");
        mTitleList.add("挂号指导");
        mTitleList.add("专业导诊");

        List<Fragment> list = new ArrayList<>();
        list.add(MyPrivilegeGiveOutNumFragment.newInstance());
        list.add(MyPrivilegeGuideNumFragment.newInstance());
        list.add(MyPrivilegeProfessionalGuideFragment.newInstance());
        final BaseViewPagerAdapter adapter = new BaseViewPagerAdapter(getSupportFragmentManager(), list, mTitleList);
        mViewPager.setAdapter(adapter);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_my_privilege_activity_left_btn:
                finish();
                break;
            case R.id.right_btn1:
                ToastUtils.shortToast(mContext,"进入挂号提醒流程");
                break;
        }
    }
}
