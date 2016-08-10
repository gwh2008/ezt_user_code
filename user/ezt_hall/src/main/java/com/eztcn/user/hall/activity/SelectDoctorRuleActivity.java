package com.eztcn.user.hall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.Request;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.model.ResultResponse.DoctorSelectRuleResponse;
import com.eztcn.user.hall.model.ResultResponse.SelectRulesResponse;
import com.eztcn.user.hall.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 预约挂号里面的筛选页面
 *
 * @author 蒙
 */
public class SelectDoctorRuleActivity extends BaseActivity implements View.OnClickListener, ResultSubscriber.OnResultListener {
    private TextView all;//全部按钮
    private TextView currentDay;//当日挂号
    private TextView pre;//预约挂号
    private TextView threeLevel;//等级三
    private TextView twoLevel;//等级二
    private TextView oneLevel;//等级一
    private TextView clear;//清空输入框
    private TextView ok;//确认按钮
    private boolean isAll = true;//是否是点击了全部
    private String data_from = "";//号源类型
    private List<String> level_hospital = new ArrayList<>();//医院等级复选

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTitleBar(true, "筛选", null);
    }

    @Override
    protected int preView() {
        return R.layout.new_activity_select;
    }

    public void initView() {
        all = (TextView) findViewById(R.id.new_activity_select_all);
        all.setOnClickListener(this);
        currentDay = (TextView) findViewById(R.id.new_activity_select_currentDay);
        currentDay.setOnClickListener(this);
        pre = (TextView) findViewById(R.id.new_activity_select_pre);
        pre.setOnClickListener(this);
        threeLevel = (TextView) findViewById(R.id.new_activity_select_threeLevel);
        threeLevel.setOnClickListener(this);
        twoLevel = (TextView) findViewById(R.id.new_activity_select_twoLevel);
        twoLevel.setOnClickListener(this);
        oneLevel = (TextView) findViewById(R.id.new_activity_select_oneLevel);
        oneLevel.setOnClickListener(this);
        clear = (TextView) findViewById(R.id.new_activity_select_clear);
        clear.setOnClickListener(this);
        ok = (TextView) findViewById(R.id.new_activity_select_ok);
        ((TextView) findViewById(R.id.doctor_level)).setText("医生等级(多选)");
        ok.setOnClickListener(this);


    }

    @Override
    protected void initData() {
        getDoctorRulesData();

    }

    /**
     * 获取筛选规则
     */
    public void getDoctorRulesData() {
        showProgressDialog("正在加载数据...");
        Request request = new Request();
        Map<String, String> params = new HashMap<>();
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC).getDoctorRulesData(request.getFormMap(params), 111, this);
    }
    private DoctorSelectRuleResponse selectRulesResponse;
    @Override
    public void onNext(IModel t, int requestType) {
        dismissProgressDialog();
        Response response = (Response) t;
        if (response.isFlag()) {
            if (requestType == 111) {
                 selectRulesResponse = (DoctorSelectRuleResponse) response.getData();
                if (selectRulesResponse==null){
                    selectRulesResponse=new DoctorSelectRuleResponse();
                }
                currentDay.setText(selectRulesResponse.getRegisterType().get(0).getName());
                pre.setText(selectRulesResponse.getRegisterType().get(1).getName());
                threeLevel.setText(selectRulesResponse.getDoctorLevel().get(0).getName());
                twoLevel.setText(selectRulesResponse.getDoctorLevel().get(1).getName());
                oneLevel.setText(selectRulesResponse.getDoctorLevel().get(2).getName());
            }
        } else {
            ToastUtils.shortToast(mContext, response.getMessage());
            finish();
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
        finish();
    }

    public void changeType(TextView textView) {
        all.setBackgroundResource(R.drawable.new_oval_select_border_gray);
        all.setTextColor(getResources().getColor(R.color.text_color_gray));
        if (textView == all || textView == clear) {//全部或者清空是一样的操作
            all.setBackgroundResource(R.drawable.new_oval_select_border_green);
            all.setTextColor(getResources().getColor(R.color.border_line));
            currentDay.setBackgroundResource(R.drawable.new_oval_select_border_gray);
            currentDay.setTextColor(getResources().getColor(R.color.text_color_gray));
            pre.setBackgroundResource(R.drawable.new_oval_select_border_gray);
            pre.setTextColor(getResources().getColor(R.color.text_color_gray));
            threeLevel.setBackgroundResource(R.drawable.new_oval_select_border_gray);
            threeLevel.setTextColor(getResources().getColor(R.color.text_color_gray));
            twoLevel.setBackgroundResource(R.drawable.new_oval_select_border_gray);
            twoLevel.setTextColor(getResources().getColor(R.color.text_color_gray));
            oneLevel.setBackgroundResource(R.drawable.new_oval_select_border_gray);
            oneLevel.setTextColor(getResources().getColor(R.color.text_color_gray));
        } else if (textView == currentDay) {
            currentDay.setBackgroundResource(R.drawable.new_oval_select_border_green);
            currentDay.setTextColor(getResources().getColor(R.color.border_line));
            pre.setBackgroundResource(R.drawable.new_oval_select_border_gray);
            pre.setTextColor(getResources().getColor(R.color.text_color_gray));
        } else if (textView == pre) {
            pre.setBackgroundResource(R.drawable.new_oval_select_border_green);
            pre.setTextColor(getResources().getColor(R.color.border_line));
            currentDay.setBackgroundResource(R.drawable.new_oval_select_border_gray);
            currentDay.setTextColor(getResources().getColor(R.color.text_color_gray));
        } else if (textView == threeLevel) {
            threeLevel.setBackgroundResource(R.drawable.new_oval_select_border_green);
            threeLevel.setTextColor(getResources().getColor(R.color.border_line));
        } else if (textView == twoLevel) {
            twoLevel.setBackgroundResource(R.drawable.new_oval_select_border_green);
            twoLevel.setTextColor(getResources().getColor(R.color.border_line));
        } else if (textView == oneLevel) {
            oneLevel.setBackgroundResource(R.drawable.new_oval_select_border_green);
            oneLevel.setTextColor(getResources().getColor(R.color.border_line));
        }
    }

    @Override
    public void onClick(View v) {
        if (v == ok) {
            Intent intent = new Intent();
            //将选择的条件返回给上个页面
            if (isAll) {
                intent.putExtra("registerType", "");//号源类别
                intent.putExtra("docLevel", "");//医生等级
            } else {
                intent.putExtra("registerType", data_from);
                if (level_hospital.size() == 0) {
                    intent.putExtra("docLevel", "");
                } else if (level_hospital.size() == 1) {
                    intent.putExtra("docLevel", level_hospital.get(0));
                } else if (level_hospital.size() == 2) {
                    intent.putExtra("docLevel", level_hospital.get(0) + "," + level_hospital.get(1));
                } else if (level_hospital.size() == 3) {
                    intent.putExtra("docLevel", level_hospital.get(0) + "," + level_hospital.get(1) + "," + level_hospital.get(2));
                }
            }
            setResult(111, intent);
            finish();
        }
        isAll = false;
        if (v == all) {
            isAll = true;
            level_hospital.clear();
            changeType(all);
        } else if (v == currentDay) {
            data_from = selectRulesResponse.getRegisterType().get(0).getId();
            changeType(currentDay);
        } else if (v == pre) {
            data_from = selectRulesResponse.getRegisterType().get(1).getId();
            changeType(pre);
        } else if (v == threeLevel) {
            if (threeLevel.getCurrentTextColor()==getResources().getColor(R.color.border_line)){
                return;
            }
            level_hospital.add(selectRulesResponse.getDoctorLevel().get(0).getId());
            changeType(threeLevel);
        } else if (v == twoLevel) {
            if (twoLevel.getCurrentTextColor()==getResources().getColor(R.color.border_line)){
                return;
            }
            level_hospital.add(selectRulesResponse.getDoctorLevel().get(1).getId());
            changeType(twoLevel);
        } else if (v == oneLevel) {
            if (oneLevel.getCurrentTextColor()==getResources().getColor(R.color.border_line)){
                return;
            }
            level_hospital.add(selectRulesResponse.getDoctorLevel().get(2).getId());
            changeType(oneLevel);
        } else if (v == clear) {
            level_hospital.clear();
            data_from="";
            isAll = true;
            changeType(clear);
        }
    }

}
