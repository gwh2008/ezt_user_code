package com.eztcn.user.eztcn.activity.home;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnChildClick;
import xutils.view.annotation.event.OnClick;

import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.mine.AutonymAuthActivity;
import com.eztcn.user.eztcn.activity.mine.ModifyPhoneActivity;
import com.eztcn.user.eztcn.activity.mine.MyMedicalRecordListActivity;
import com.eztcn.user.eztcn.adapter.DeptDataAdapter;
import com.eztcn.user.eztcn.adapter.PopupWindowAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Dept;
import com.eztcn.user.eztcn.bean.FamilyMember;
import com.eztcn.user.eztcn.bean.MedicalRecord;
import com.eztcn.user.eztcn.customView.ScrollerNumberPicker;
import com.eztcn.user.eztcn.customView.ScrollerNumberPicker.OnSelectListener;
import com.eztcn.user.eztcn.customView.SwitchButton;
import com.eztcn.user.eztcn.db.EztDictionaryDB;
import com.eztcn.user.eztcn.impl.HospitalImpl;
import com.eztcn.user.eztcn.impl.RegistratioImpl;
import com.eztcn.user.eztcn.impl.UserImpl;
import com.eztcn.user.eztcn.utils.CharacterParser;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.Logger;
import com.eztcn.user.eztcn.utils.PinyinComparatorDept;
import com.eztcn.user.eztcn.utils.ResourceUtils;
import com.eztcn.user.hall.utils.Constant;

/**
 * @author ezt
 * @title 预约登记
 * @describe
 * @created 2014年12月15日
 */
public class AppointCheckInActivity extends FinalActivity implements
        OnCheckedChangeListener, OnItemClickListener,
        OnClickListener, IHttpResult {

    @ViewInject(R.id.appoint_checkin_name_layout)//, click = "onClick"
    private RelativeLayout layoutName;//

    @ViewInject(R.id.appoint_checkin_time_layout)//, click = "onClick"
    private RelativeLayout layoutTime;//

    @ViewInject(R.id.appoint_checkin_area_layout)//, click = "onClick"
    private RelativeLayout layoutArea;//

    @ViewInject(R.id.appoint_checkin_dept_layout)//, click = "onClick"
    private RelativeLayout layoutDept;//

    @ViewInject(R.id.appoint_checkin_case_history_layout)//, click = "onClick"
    private RelativeLayout layoutCase;//

    @ViewInject(R.id.appoint_checkin_yes_bt)//, click = "onClick"
    private Button btYes;//

    @ViewInject(R.id.appoint_checkin_name)
    private TextView tvName;// 名称

    @ViewInject(R.id.appoint_checkin_time)
    private TextView tvTime;// 时间

    @ViewInject(R.id.appoint_checkin_area)
    private TextView tvArea;// 地区

    @ViewInject(R.id.appoint_checkin_dept)
    private TextView tvDept;// 科室

    @ViewInject(R.id.illNums)
    private EditText illNums;// 病案号

    @ViewInject(R.id.appoint_checkin_case_history)
    private TextView tvCase;// 病历

    @ViewInject(R.id.appoint_checkin_feetype)
    private TextView tvFeeType;// 付费方式

    @ViewInject(R.id.medicalLayout)
    private LinearLayout medicalLayout;// 医保
    @ViewInject(R.id.medicalNo)
    private EditText medicalNo;

    @ViewInject(R.id.appoint_sdtype_sb)
    private SwitchButton appoint_sdtype_sb;// 初诊/复诊
    @ViewInject(R.id.sd_text)
    private TextView sd_text;

    @ViewInject(R.id.appoint_checkin_et)
    private EditText et_appoint_checkin;

    @ViewInject(R.id.appoint_checkin_feetype_sb)
    private SwitchButton sbFeeType;//

    /**
     * 选择时间，就诊人
     */
    private LinearLayout layout1, layout2;

    private View view, view1;

    private TextView affirmDate, affirmPerson;

    private ScrollerNumberPicker oDate, personView;

    private PopupWindow pWindow;

    private int type;

    private ArrayList<String> dateList, personList;

    private String currentDate, strPerson, strDept;// 选中日期、就诊人、就诊科室

    /**
     * 选择科室
     */
    private Dialog deptChoice;

    private PopupWindowAdapter deptAdapter;

    private ListView ltDept;

    private TextView tvDeptTitle;

    private TextView tvCancel;

    private int dialogWidth;// 弹出框宽

    private String operateUserId = "";

    private String patientId = "";
    private ArrayList<FamilyMember> familyList;
    private String person;// 所选就诊人

    private final String LEVEL = "1";// 科室分类级别（1大分类，2小分类）
    private CharacterParser characterParser;
    private DeptDataAdapter adapterType;// 科室类型adapter
    private List<Dept> deptList;
    private String cityId;
    private String areaId;
    private String pay_style;// 缴费方式
    private String sd_style;// 初诊/复诊
    private String deptTypeId;// 大科室id
    private String mId;// 病历id
    private String cardId;
    /**
     * 医保卡号
     */
    private String strMedicalNo;
    /**
     * 弹窗选择就诊人时候 所选择的第几个位置
     */
    private int currentSelectPos = 0;
    private int tempSelectPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint_checkin);
        ViewUtils.inject(AppointCheckInActivity.this);
        if (null != BaseApplication.patient)
            patientId = BaseApplication.patient.getId() + "";// 操作用户id
        else{
            HintToLogin(Constant.LOGIN_COMPLETE);
            return;
        }
        loadTitleBar(true, "预约登记", null);
        dialogWidth = (int) (getWindowWidth() * 0.8);
        initialDialogView();
        initOrderDate();
        getMembers();
        sbFeeType.setOnCheckedChangeListener(this);
        sbFeeType.setChecked(true);
        appoint_sdtype_sb.setOnCheckedChangeListener(this);
        appoint_sdtype_sb.setChecked(true);
        characterParser = CharacterParser.getInstance();
        adapterType = new DeptDataAdapter(this, false);
        ltDept.setAdapter(adapterType);
        getDeptTypes();
        if (BaseApplication.patient != null) {
            operateUserId = BaseApplication.patient.getUserId() + "";// 操作用户id
        }
    }

    /**
     * 登记
     */
    private void orderRegister() {
        if (TextUtils.isEmpty(cardId)) {
            hintPerfectInfo("请完善个人信息再进行预约登记!", 1);
            return;
        }
        if (TextUtils.isEmpty(tvArea.getText().toString())) {
            Toast.makeText(getApplicationContext(), "请选择就诊地区",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (!pay_style.equals("0")) {
            if (strMedicalNo.equals("")) {
                // 如果没有医保卡号则填完整
                strMedicalNo = BaseApplication.patient.getEpHiid();
                medicalNo.setText(strMedicalNo);
            }
            String mNo = medicalNo.getText().toString();
            if (TextUtils.isEmpty(mNo)) {
                Toast.makeText(getApplicationContext(), "请输入医保号",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (mNo.length() < 15) {
                Toast.makeText(getApplicationContext(), "请输入15~20位的医保号",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }
        RequestParams params = new RequestParams();
        RegistratioImpl impl = new RegistratioImpl();
        params.addBodyParameter("patientId", patientId);
        params.addBodyParameter("clinicalTime", currentDate);
        params.addBodyParameter("cityId", cityId);
        params.addBodyParameter("countyId", areaId);
        params.addBodyParameter("deptCateId", deptTypeId);
        params.addBodyParameter("payWay", pay_style);
        params.addBodyParameter("isFirst", sd_style);
        params.addBodyParameter("epHiid", medicalNo.getText().toString());
        params.addBodyParameter("mId", mId);
        params.addBodyParameter("cardNum", illNums.getText().toString());
        params.addBodyParameter("demand", et_appoint_checkin.getText().toString());
        impl.orderRegister(params, this);
        showProgressToast();
    }

    /**
     * 提醒完善信息
     */
    private void hintPerfectInfo(String hint, final int type) {
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setIcon(android.R.drawable.ic_dialog_info).setTitle("提示")
                .setMessage(hint).setCancelable(false)
                .setNegativeButton("完善", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (type == 1) {// 完善个人信息
                            mContext.startActivity(new Intent(mContext,
                                    AutonymAuthActivity.class));
                        } else {// 完善个人手机号
                            mContext.startActivity(new Intent(mContext,
                                    ModifyPhoneActivity.class));
                        }

                    }
                }).setPositiveButton("取消", null);

        AlertDialog dialog = builder.create();
        dialog.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
                return false;
            }
        });
        dialog.show();
    }
    /**
     * 初始化选择框view
     */
    private void initialDialogView() {

        // 选择时间
        view = LayoutInflater.from(this).inflate(R.layout.selecttime_checkin,
                null);
        layout1 = (LinearLayout) view.findViewById(R.id.all_layout);
        layout1.getLayoutParams().width = dialogWidth;
        oDate = (ScrollerNumberPicker) view.findViewById(R.id.date);
        affirmDate = (TextView) view.findViewById(R.id.affirm);

        // 选择就诊人
        view1 = LayoutInflater.from(this).inflate(R.layout.selectperson, null);
        layout2 = (LinearLayout) view1.findViewById(R.id.all_layout);
        layout2.getLayoutParams().width = dialogWidth;
        affirmPerson = (TextView) view1.findViewById(R.id.add);
        personView = (ScrollerNumberPicker) view1
                .findViewById(R.id.person_wheelview);

        // 选择就诊科室
        deptChoice = new Dialog(this, R.style.ChoiceDialog);
        View dialogView = LinearLayout.inflate(this, R.layout.dialog_choice,
                null);
        ltDept = (ListView) dialogView.findViewById(R.id.dialog_lt);
        tvDeptTitle = (TextView) dialogView.findViewById(R.id.title);
        tvCancel = (TextView) dialogView.findViewById(R.id.cancel_tv);// 取消按钮

        tvCancel.setOnClickListener(this);
        deptAdapter = new PopupWindowAdapter(mContext);
        ltDept.setAdapter(deptAdapter);
        ltDept.setOnItemClickListener(this);
        deptChoice.setCanceledOnTouchOutside(true);
        deptChoice.setContentView(dialogView);
    }
    /**
     * 初始化WheelView数据
     *
     * @param view
     */
    private void initialTimeData(ScrollerNumberPicker view, List<String> data,
                                 int pos) {
        view.setData(data);
        view.setDefault(pos);
    }
    /**
     * 初始化可预约日期
     */
    public void initOrderDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String temp;
        dateList = new ArrayList<String>();
        Date date = new Date();
        int nowDay = date.getDate();
        temp = sdf.format(date);
        dateList.add(temp);
        for (int i = 0; i < 14; i++) {
            date.setDate(nowDay + 1);
            temp = sdf.format(date);
            dateList.add(temp);
            nowDay = date.getDate();
        }
        currentDate = dateList.get(0);
        tvTime.setText(currentDate);
        initialTimeData(oDate, dateList, 0);
    }

    /**
     * 预约时间选择器
     */
    public void selectOrderTime() {
        configPopupWindow(view);
        affirmDate.setOnClickListener(this);
        oDate.setOnSelectListener(new OnSelectListener() {

            @Override
            public void selecting(int id, String text) {

            }

            @Override
            public void endSelect(int id, String text) {
                currentDate = dateList.get(id);
            }
        });
    }

    /**
     * 获取就诊人
     */
    private void getMembers() {
        RequestParams params1 = new RequestParams();
        params1.addBodyParameter("userId", operateUserId);

        new UserImpl().getMemberCenter(params1, this);
        showProgressToast();
    }

    /**
     * 获取科室分类列表
     */
    private void getDeptTypes() {
        RequestParams params = new RequestParams();
        HospitalImpl impl = new HospitalImpl();
        params.addBodyParameter("level", LEVEL);// 分类级别
        impl.getBigDeptList(params, this);
    }

    /**
     * 就诊人选择
     */
    private void selectPerson() {

        initialTimeData(personView, personList, currentSelectPos);

        configPopupWindow(view1);
        affirmPerson.setOnClickListener(this);
        if (personList != null && personList.size() > 0) {// 默认第一个被选中
            strPerson = personList.get(currentSelectPos);
        }
        personView.setOnSelectListener(new OnSelectListener() {
            @Override
            public void selecting(int id, String text) {

            }

            @Override
            public void endSelect(int id, String text) {
                tempSelectPos = id;
                // 只有确定的时候才能确定下来就诊人的信息
            }
        });

    }

    /**
     * 窗口设置
     */
    private void configPopupWindow(final View view) {
        pWindow = new PopupWindow();
        pWindow.setContentView(view);
        pWindow.setWidth(LayoutParams.MATCH_PARENT);
        pWindow.setHeight(LayoutParams.MATCH_PARENT);
        pWindow.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        pWindow.setBackgroundDrawable(dw);
        pWindow.setFocusable(true);
        view.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float left = v.findViewById(R.id.titleLayout).getLeft();
                float top = v.findViewById(R.id.titleLayout).getTop();
                float right;
                float bottom;
                if (type == 1) {
                    right = v.findViewById(R.id.affirm).getRight();
                    bottom = v.findViewById(R.id.affirm).getBottom();
                } else {
                    right = v.findViewById(R.id.add).getRight();
                    bottom = v.findViewById(R.id.add).getBottom();
                }
                float x = event.getX();
                float y = event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    if (x < left && x > right) {
                        if (y < top || y > bottom) {
                            pWindow.dismiss();
                        }
                    } else {
                        if (y < top || y > bottom) {
                            pWindow.dismiss();
                        }
                    }
                }
                return true;
            }
        });
    }

    /**
     * 弹出框选择区域
     */
    private void choiceDept(List<Dept> data, String title) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < data.size(); i++) {
            list.add(data.get(i).getdName());
        }
        tvDeptTitle.setText(title);
        deptAdapter.setList(list);
        // 获取listview的高度
        int totalHeight = 0;
        for (int i = 0, len = deptAdapter.getCount(); i < len; i++) {
            View listItem = deptAdapter.getView(i, null, ltDept);
            listItem.measure(0, 0); // 计算子项View 的宽高
            int item_height = listItem.getMeasuredHeight()
                    + ltDept.getDividerHeight();
            totalHeight += item_height; // 统计所有子项的总高度
        }
        Window dialogWindow = deptChoice.getWindow();

        totalHeight = totalHeight
                + ResourceUtils.dip2px(mContext, ResourceUtils.getXmlDef(
                mContext, R.dimen.dialog_title_bar_size));
        dialogWindow.setLayout(dialogWidth,
                totalHeight > getWindowHeight() / 2 ? getWindowHeight() / 2
                        : totalHeight);
        if (!deptChoice.isShowing()) {
            deptChoice.show();
        } else {
            deptChoice.dismiss();
        }
    }

    @OnClick(R.id.appoint_checkin_name_layout)
    private void checkinClick(View v) {
        // 就诊人
        type = 2;
        selectPerson();
        pWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }
    @OnClick(R.id.appoint_checkin_time_layout)
    private void checkin_timeClick(View v) {
        // 期望就诊时间
        type = 1;
        selectOrderTime();
        pWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }
    @OnClick(R.id.appoint_checkin_area_layout)
    private void checkin_areaClick(View v) {
        // 期望就诊地区
        startActivityForResult(new Intent(mContext,
                AppointCheckIn_ChoiceAreaActivity.class), 2);
    }

    @OnClick(R.id.appoint_checkin_dept_layout)
    private void checkin_deptClick(View v) {
        // 就诊科室
        choiceDept(deptList, "选择科室");
    }

    @OnClick(R.id.appoint_checkin_case_history_layout)
    private void uploadClick(View v) {
        // 上传病例
        // Toast.makeText(AppointCheckInActivity.this, " 上传病例",
        // Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MyMedicalRecordListActivity.class);
        intent.putExtra("enterType", 22);
        intent.putExtra("patientId", patientId);
        startActivityForResult(intent, 22);
    }

    @OnClick(R.id.appoint_checkin_yes_bt)
    private void sureClick(View v) {
        // 确定登记
        orderRegister();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.add:// 确定就诊人
                currentSelectPos = tempSelectPos;
                strPerson = personList.get(currentSelectPos);
                patientId = familyList.get(currentSelectPos).getPatientId();
                cardId = familyList.get(currentSelectPos).getIdCard();
                strMedicalNo = familyList.get(currentSelectPos).getMedicalNo();

                tvName.setText(strPerson);
                // 添加医保信息
                medicalNo.setText(strMedicalNo);
                if (pWindow != null) {
                    pWindow.dismiss();
                }
                break;
            case R.id.affirm:// 确定预约时间
                tvTime.setText(currentDate);
                if (pWindow != null) {
                    pWindow.dismiss();
                }
                break;
            case R.id.cancel_tv:// 取消按钮
                if (deptChoice != null) {
                    deptChoice.dismiss();
                }
                break;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == sbFeeType) {
            if (isChecked) {
                tvFeeType.setText("自费");
                pay_style = "0";
                medicalLayout.setVisibility(View.GONE);
            } else {
                tvFeeType.setText("医保");
                pay_style = "1";
                medicalLayout.setVisibility(View.VISIBLE);
                if (BaseApplication.patient != null) {
                    medicalNo.setText(strMedicalNo);
                }
            }
        } else {
            if (isChecked) {
                sd_text.setText("初诊");
                sd_style = "0";
            } else {
                sd_text.setText("复诊");
                sd_style = "1";
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == 2) {// 选择区域返回
            if (data != null) {
                String strArea = data.getStringExtra("strArea");
                areaId = data.getStringExtra("areaId");
                String strCity = data.getStringExtra("strCity");
                cityId = data.getStringExtra("cityId");
                tvArea.setText(strCity + " " + strArea);
            }

        }
        if (resultCode == 22) {
            if (data != null) {
                MedicalRecord record = (MedicalRecord) data.getExtras()
                        .getSerializable("record");
                mId = record.getId();
                tvCase.setText(record.getRecordNum());
            }
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        strDept = deptAdapter.getList().get(position);
        tvDept.setText(strDept);
        deptTypeId = deptList.get(position).getId() + "";
        deptChoice.dismiss();
    }

    @Override
    public void result(Object... object) {
        hideProgressToast();
        if (object == null) {
            return;
        }
        Integer taskID = (Integer) object[0];
        boolean isSuc = (Boolean) object[1];

        switch (taskID) {
            case HttpParams.MEMBER_CENTER:// 获取家庭成员
                if (isSuc) {
                    familyList = (ArrayList<FamilyMember>) object[2];
                    if (familyList != null) {
                        personList = new ArrayList<String>();
                        for (int i = 0; i < familyList.size(); i++) {
                            String strName = familyList.get(i).getMemberName();
                            String strCardNo = familyList.get(i).getIdCard();
                            int isMain = familyList.get(i).getMainUser();
                            String strRelation;
                            if (isMain == 1) {
                                strRelation = "自己";
                            } else {
                                strRelation = EztDictionaryDB.getInstance(
                                        getApplicationContext()).getLabelByTag(
                                        "kinship",
                                        familyList.get(i).getRelation() + "");
                            }
                            String strSex = familyList.get(i).getSex() == 0 ? "男"
                                    : "女";
                            if (TextUtils.isEmpty(strName)) {
                                strName = "";
                            }
                            String strNew = strName + "(" + strRelation + ")";
                            personList.add(strNew);

                        }

                        if (familyList.size() > 0) {
                            String strName = familyList.get(0).getMemberName();
                            int isMain = familyList.get(0).getMainUser();
                            cardId = familyList.get(0).getIdCard();
                            strMedicalNo = familyList.get(0).getMedicalNo();
                            String strRelation;
                            if (isMain == 1) {
                                strRelation = "自己";
                                strMedicalNo = BaseApplication.patient
                                        .getEpHiid();
                                medicalNo.setText(strMedicalNo);
                            } else {
                                strRelation = EztDictionaryDB.getInstance(
                                        getApplicationContext()).getLabelByTag(
                                        "kinship",
                                        familyList.get(0).getRelation() + "");

                            }
                            if (TextUtils.isEmpty(strName)) {
                                strName = "";
                            }
                            person = strName + "(" + strRelation + ")";
                            tvName.setText(person);

                        }
                    } else {
                        strMedicalNo = BaseApplication.patient.getEpHiid();
                        medicalNo.setText(strMedicalNo);
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.request_fail),
                                Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), object[3].toString(),
                            Toast.LENGTH_SHORT).show();
                }

                break;

            case HttpParams.GET_BIG_DEPT:// 科室分类
                if (isSuc) {
                    deptList = (ArrayList<Dept>) object[2];
                    for (int i = 0; i < deptList.size(); i++) {
                        Dept sortModel = new Dept();
                        String dName = deptList.get(i).getdName();
                        sortModel.setdName(dName);
                        sortModel.setId(deptList.get(i).getId());
                        // 汉字转换成拼音
                        String pinyin = characterParser.getSelling(dName);
                        String sortString = pinyin.substring(0, 1).toUpperCase();

                        // 正则表达式，判断首字母是否是英文字母
                        if (sortString.matches("[A-Z]")) {
                            sortModel.setSortLetters(sortString.toUpperCase());
                        } else {
                            sortModel.setSortLetters("#");
                        }
                        deptList.set(i, sortModel);
                        // mSortList.add(sortModel);
                    }

                    // 根据a-z进行排序源数据
                    Collections.sort(deptList, new PinyinComparatorDept());
                    adapterType.setList(deptList);
                    if (deptList != null && deptList.size() > 0) {
                        tvDept.setText(deptList.get(0).getdName());
                        deptTypeId = deptList.get(0).getId() + "";
                    } else {

                        Logger.i("科室分类", "数据为null");
                    }

                } else {
                    Logger.i("科室分类", object[3]);
                }
                break;
            case HttpParams.ORDERREGISTER:
                if (isSuc) {
                    Map<String, Object> map = (Map<String, Object>) object[2];
                    boolean bool = (Boolean) map.get("flag");
                    if (bool) {
                        startActivity(new Intent(AppointCheckInActivity.this,
                                SucHintActivity.class).putExtra("type", 3));
                    } else {
                        String msg = map.get("msg").toString();
                        Toast.makeText(getApplicationContext(), msg + "",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "服务器繁忙，请稍后重试",
                            Toast.LENGTH_SHORT).show();
                }
                break;

        }

    }
}
