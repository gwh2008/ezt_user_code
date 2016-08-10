package com.eztcn.user.eztcn.activity.fdoc;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eztcn.user.R;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;

import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.MyFragmentPagerAdapter;
import com.eztcn.user.eztcn.customView.NoScrollViewPager;
import com.eztcn.user.eztcn.fragment.Disease_FormFragment;
import com.eztcn.user.eztcn.fragment.Symptom_BodyFragment;
import com.eztcn.user.eztcn.fragment.Symptom_FormFragment;

/**
 * @author ezt
 * @title 症状自查
 * @describe
 * @created 2015年4月20日
 */
public class SymptomSelfActivity extends FinalActivity implements
        OnClickListener {

    @ViewInject(R.id.body_pic_tv)
    public TextView tvBodyView;// 人体图

    @ViewInject(R.id.symptom_form_tv)
    public TextView tvSymptomForm;// 症状表

    @ViewInject(R.id.disease_form_tv)
    public TextView tvDiseaseForm;// 疾病表

    @ViewInject(R.id.symptom_pager)
    public NoScrollViewPager pager; // 滑动页

    @ViewInject(R.id.v_buttom_line)
    private View viewLine;// 底部标记线

    private ArrayList<Fragment> fragmentList;

    private int currIndex;// 当前页卡编号

    private TextView tvBack;// 返回

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom);
        ViewUtils.inject(SymptomSelfActivity.this);
        loadTitleBar(true, "症状自查", null);
        tvBack = (TextView) findViewById(R.id.left_btn);
        tvBack.setOnClickListener(this);

        initialView();
    }

    /**
     * 初始化控件
     */
    private void initialView() {

        tvBodyView.setOnClickListener(new txListener(0));
        tvSymptomForm.setOnClickListener(new txListener(1));
        tvDiseaseForm.setOnClickListener(new txListener(2));

        tvBodyView.setTextColor(getResources().getColor(R.color.title_bar_bg));
        tvSymptomForm.setTextColor(getResources().getColor(R.color.dark_black));
        tvDiseaseForm.setTextColor(getResources().getColor(R.color.dark_black));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                getWindowWidth() / 3, 6);
        viewLine.setLayoutParams(params);
        // 初始化颜色
        viewLine.setBackgroundColor(getResources().getColor(
                R.color.title_bar_bg));
        initialPager();
    }

    /**
     * 初始化viewPager
     */
    private void initialPager() {
        fragmentList = new ArrayList<Fragment>();
        Fragment fragment1 = Symptom_BodyFragment.newInstance();
        Fragment fragment2 = Symptom_FormFragment.newInstance();
        Fragment fragment3 = Disease_FormFragment.newInstance();
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        fragmentList.add(fragment3);

        pager.setOffscreenPageLimit(3);// 设置显示页数
        // 给ViewPager设置适配器
        pager.setAdapter(new MyFragmentPagerAdapter(
                getSupportFragmentManager(), fragmentList));
        pager.setCurrentItem(0);// 设置当前显示标签页为第一页
        pager.setOnPageChangeListener(new OnPageChangeListener() {
            public void onPageSelected(int position) {
                Symptom_BodyFragment fragment = (Symptom_BodyFragment) fragmentList
                        .get(0);
                if (null != fragment && null != fragment.drawLayout)
                    if (fragment.drawLayout.isDrawerOpen(fragment.hideLayout)) {
                        fragment.drawLayout.closeDrawer(fragment.hideLayout);
                    }
                slideView(position);
                currIndex = position;
                if (position == 0) {//设置选项卡上三种文字颜色
                    tvBodyView.setTextColor(getResources().getColor(
                            R.color.title_bar_bg));
                    tvSymptomForm.setTextColor(getResources().getColor(
                            R.color.dark_black));
                    tvDiseaseForm.setTextColor(getResources().getColor(
                            R.color.dark_black));
                } else if (position == 1) {
                    tvBodyView.setTextColor(getResources().getColor(
                            R.color.dark_black));
                    tvSymptomForm.setTextColor(getResources().getColor(
                            R.color.title_bar_bg));
                    tvDiseaseForm.setTextColor(getResources().getColor(
                            R.color.dark_black));
                } else {
                    tvBodyView.setTextColor(getResources().getColor(
                            R.color.dark_black));
                    tvSymptomForm.setTextColor(getResources().getColor(
                            R.color.dark_black));
                    tvDiseaseForm.setTextColor(getResources().getColor(
                            R.color.title_bar_bg));

                }
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    public class txListener implements View.OnClickListener {
        private int index = 0;

        public txListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            pager.setCurrentItem(index);
        }
    }

    // 底部滑动， positionId， 你要滑动到的位置
    private void slideView(int positionIndex) {
        TranslateAnimation tran = null;
        tran = new TranslateAnimation(calcPosition(currIndex),
                calcPosition(positionIndex), 0, 0);
        tran.setDuration(100);
        tran.setFillAfter(true);
        viewLine.startAnimation(tran);
    }

    // 计算位置 position 需要移动到的位置
    private int calcPosition(int positionIndex) {
        int position = getWindowWidth() / 3;
        int currentPosition = 0;
        for (int i = 0; i < positionIndex; i++) {
            currentPosition += position;
        }
        return currentPosition;
    }

    @Override
    public void onClick(View v) {// 返回
        onBackPressed();

    }

    @Override
    public void onBackPressed() {
        Symptom_BodyFragment fragment = (Symptom_BodyFragment) fragmentList
                .get(0);
        if (fragment.drawLayout != null) {
            if (fragment.drawLayout.isDrawerOpen(fragment.hideLayout)) {
                fragment.drawLayout.closeDrawer(fragment.hideLayout);
            } else {
                super.onBackPressed();
            }
        }
    }

}
