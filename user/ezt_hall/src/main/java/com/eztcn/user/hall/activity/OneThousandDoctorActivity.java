package com.eztcn.user.hall.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.hall.adapter.BaseViewPagerAdapter;
import com.eztcn.user.hall.fragment.OneThousandAusleseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lizhipeng
 * @Data: 16/6/3 下午5:34
 * @Description: 一千零医夜 界面
 */
public class OneThousandDoctorActivity extends BaseActivity implements View.OnClickListener {
    private TextView mBackTv;
    private ViewPager mViewPager;
//    private SlidingTabLayout mSlidingTabLayout;
    private List<String> mTitleList;

    @Override
    protected int preView() {
//        return R.layout.new_activity_one_thousand_doctor;
        return R.layout.new_activity_one_thousand_doctor2;
    }

    @Override
    protected void initView() {
        mBackTv = (TextView) findViewById(R.id.new_one_thousand_doctor_activity_left_btn);
        mViewPager = (ViewPager) findViewById(R.id.new_one_thousand_doctor_activity_view_pager);
        /**自定义部分属性*/
//        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.new_one_thousand_doctor_activity_tabs);
    }

    @Override
    protected void initData() {
        mBackTv.setOnClickListener(this);
//        mTitleList = new ArrayList<>();
//        mTitleList.add("精选");
//        mTitleList.add("医说");
//        mTitleList.add("养生");

        List<Fragment> list = new ArrayList<>();
        list.add(OneThousandAusleseFragment.newInstance());
//        list.add(OneThousandDoctorSayFragment.newInstance());
//        list.add(OneThousandKeepHealthFragment.newInstance());
        final BaseViewPagerAdapter adapter = new BaseViewPagerAdapter(getSupportFragmentManager(), list, mTitleList);
        mViewPager.setAdapter(adapter);
//        mSlidingTabLayout.setViewPager(mViewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_one_thousand_doctor_activity_left_btn:
                finish();
                break;
            default:
                break;
        }
    }
}
