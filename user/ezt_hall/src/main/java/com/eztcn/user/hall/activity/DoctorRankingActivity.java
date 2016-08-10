package com.eztcn.user.hall.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.fragment.DoctorRankingFragment;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.PatientBean;
import com.eztcn.user.hall.model.Request;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.model.ResultResponse.AttentionDoctorResponse;
import com.eztcn.user.hall.model.ResultResponse.DoctorDatasOneLevelResponse;
import com.eztcn.user.hall.model.ResultResponse.RankDataResponse;
import com.eztcn.user.hall.utils.FileUtils;
import com.eztcn.user.hall.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * 预约量排行榜的页面
 *
 * @author 蒙
 */
public class DoctorRankingActivity extends BaseActivity implements OnClickListener
        , ResultSubscriber.OnResultListener {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private DoctorRankingFragment weekFragment;
    private DoctorRankingFragment monthFragment;
    private DoctorRankingFragment yearFragment;
    private TextView weekTextView;
    private TextView yearTextView;
    private TextView monthTextView;
    private TextView weekBottomTextView;
    private TextView yearBottomTextView;
    private TextView monthBottomTextView;
    private String deptId;//科室id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTitleBar(true, "预约量排行榜", null);

    }

    @Override
    protected int preView() {
        return R.layout.new_activity_doctorranking;
    }

    @Override
    public void initView() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        weekFragment = DoctorRankingFragment.newInstance(1);
        monthFragment = DoctorRankingFragment.newInstance(2);
        yearFragment = DoctorRankingFragment.newInstance(3);

        fragmentTransaction.add(R.id.fragment, yearFragment, "yearFragment");
        fragmentTransaction.add(R.id.fragment, monthFragment, "monthFragment");
        fragmentTransaction.add(R.id.fragment, weekFragment, "weekFragment");
        fragmentTransaction.commit();
        weekTextView = (TextView) findViewById(R.id.new_activity_doctor_rank_week);
        monthTextView = (TextView) findViewById(R.id.new_activity_doctor_rand_month);
        yearTextView = (TextView) findViewById(R.id.new_activity_doctor_rank_year);
        weekTextView.setOnClickListener(this);
        monthTextView.setOnClickListener(this);
        yearTextView.setOnClickListener(this);
        weekBottomTextView = (TextView) findViewById(R.id.new_activity_doctor_rank_week_bottom);
        monthBottomTextView = (TextView) findViewById(R.id.new_activity_doctor_rank_month_bottom);
        yearBottomTextView = (TextView) findViewById(R.id.new_activity_doctor_rank_year_bottom);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(yearFragment);
        fragmentTransaction.hide(monthFragment);
        fragmentTransaction.show(weekFragment);
        fragmentTransaction.commit();


    }

    @Override
    protected void initData() {
        deptId = getIntent().getStringExtra("deptId");
        getRankDatas();
    }

    /**
     * 获取排行榜据
     */
    public void getRankDatas() {
        showProgressDialog("数据加载中...");
        Request request = new Request();
        Map<String, String> params = new HashMap<>();
        params.put("deptId", deptId);//科室id
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.PASS).getRankDatas(request.getFormMap(params), 111, this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.new_activity_doctor_rank_week:
                if (weekFragment != null && !weekFragment.isHidden()) {
                    break;
                }

                changeType(weekFragment);
                break;

            case R.id.new_activity_doctor_rand_month:
                if (monthFragment != null && !monthFragment.isHidden()) {
                    break;
                }
                changeType(monthFragment);
                break;
            case R.id.new_activity_doctor_rank_year:
                if (yearFragment != null && !yearFragment.isHidden()) {
                    break;
                }
                changeType(yearFragment);
                break;
        }
    }

    /**
     * 进行按钮状态的切换
     *
     * @param fragment 点击到的fragment
     */
    private void changeType(DoctorRankingFragment fragment) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(yearFragment);
        fragmentTransaction.hide(monthFragment);
        fragmentTransaction.hide(weekFragment);
        weekTextView.setTextColor(getResources().getColor(R.color.text_color_deep_gray));
        monthTextView.setTextColor(getResources().getColor(R.color.text_color_deep_gray));
        yearTextView.setTextColor(getResources().getColor(R.color.text_color_deep_gray));
        weekBottomTextView.setVisibility(View.INVISIBLE);
        monthBottomTextView.setVisibility(View.INVISIBLE);
        yearBottomTextView.setVisibility(View.INVISIBLE);
        if (fragment == weekFragment) {
            fragmentTransaction.show(weekFragment);
            weekTextView.setTextColor(getResources().getColor(R.color.border_line));
            weekBottomTextView.setVisibility(View.VISIBLE);
        } else if (fragment == monthFragment) {
            fragmentTransaction.show(monthFragment);
            monthTextView.setTextColor(getResources().getColor(R.color.border_line));
            monthBottomTextView.setVisibility(View.VISIBLE);
        } else if (fragment == yearFragment) {
            fragmentTransaction.show(yearFragment);
            yearTextView.setTextColor(getResources().getColor(R.color.border_line));
            yearBottomTextView.setVisibility(View.VISIBLE);
        }
        fragmentTransaction.commit();

    }

    @Override
    public void onNext(IModel t, int requestType) {
        dismissProgressDialog();
        Response response = (Response) t;
        if (response.isFlag()) {
            if (requestType == 111) {//关注医生成功
                RankDataResponse rankDataResponse = (RankDataResponse) response.getData();
                weekFragment.setDatas(rankDataResponse.getWeek());
                monthFragment.setDatas(rankDataResponse.getMonth());
                yearFragment.setDatas(rankDataResponse.getYear());
            }
        } else {
            ToastUtils.shortToast(mContext, response.getMessage());
        }
    }

    @Override
    public void onStart(int requestType) {

    }

    @Override
    public void onCompleted(int requestType) {

    }

    @Override
    public void onError(int requestType) {
        dismissProgressDialog();

    }

}
