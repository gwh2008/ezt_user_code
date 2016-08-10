package com.eztcn.user.eztcn.activity.home;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.mine.AutonymAuthActivity;
import com.eztcn.user.eztcn.activity.mine.ModifyPhoneActivity;
import com.eztcn.user.eztcn.adapter.PoolAdapter;
import com.eztcn.user.eztcn.adapter.PoolAdapter.RegClickListener;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Doctor;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.bean.Pool;
import com.eztcn.user.eztcn.bean.PoolTimes;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.RoundImageView;
import com.eztcn.user.eztcn.customView.wheel.WheelDialog;
import com.eztcn.user.eztcn.customView.wheel.WheelDialog.CancleBtnListener;
import com.eztcn.user.eztcn.customView.wheel.WheelDialog.SureBtnListener;
import com.eztcn.user.eztcn.customView.wheel.WheelDialog.TextAdapter;
import com.eztcn.user.eztcn.customView.wheel.view.OnWheelChangedListener;
import com.eztcn.user.eztcn.customView.wheel.view.OnWheelClickedListener;
import com.eztcn.user.eztcn.customView.wheel.view.OnWheelScrollListener;
import com.eztcn.user.eztcn.customView.wheel.view.WheelView;
import com.eztcn.user.eztcn.db.EztDictionaryDB;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.impl.AttentionImpl;
import com.eztcn.user.eztcn.impl.HospitalImpl;
import com.eztcn.user.eztcn.impl.UserImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.Logger;
import com.eztcn.user.eztcn.utils.MapUtil;
import com.eztcn.user.eztcn.utils.ResourceUtils;
import com.eztcn.user.eztcn.utils.ScreenUtils;
import com.eztcn.user.hall.utils.Constant;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import xutils.BitmapUtils;
import xutils.ViewUtils;
import xutils.bitmap.BitmapCommonUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import xutils.view.annotation.event.OnItemClick;
/**
 * @author ezt
 * @title 个人医生站
 * @describe
 * @created 2014年12月18日
 */
public class DoctorIndex30Activity extends FinalActivity implements
        IHttpResult, OnClickListener, RegClickListener, SureBtnListener,
        CancleBtnListener, OnWheelClickedListener, OnWheelScrollListener,
        OnWheelChangedListener {

    @ViewInject(R.id.attention)
    private TextView tvAttent;// 关注

    @ViewInject(R.id.doctorPhoto)
    private RoundImageView imgHead;// 医生头像
    @ViewInject(R.id.doctorName)
    private TextView tvName;// 医生名称
    @ViewInject(R.id.jobTitle)
    private TextView tvPosition;// 医生职位
    @ViewInject(R.id.dept)
    private TextView tvDept;// 所属科室
    @ViewInject(R.id.hospital)
    private TextView tvHos;// 所属医院

    @ViewInject(R.id.goodAt)
    private TextView tvGoodAt;// 擅长

    private TextView tvBack;// 返回

    private TextView tvRequest;// 请愿

    private String docId = "";// 医生id
    private String hosId = "";// 医院id
    private String deptId = "";// 科室id
    private String deptDocId = "";// 医生科室id
    private Doctor doc = null;
    private String attentId = "";// 关注主键ID
    private String sourcePfId = "355";// 平台来源id
    // 2015-12-18 医院对接中
    private final int NORMAL = 1;
    private final int WARNING = 0;
    private int ehDockingStatus = NORMAL;
    @ViewInject(R.id.orderTimeLv)
    private ListView orderTimeLv;
    @ViewInject(R.id.orderTimeLayout)
    private View orderTimeLayout;
    @ViewInject(R.id.addrValueTv)
    private TextView addrValueTv;
    private WheelDialog wheelDialog;
    private int currentPoolClickPosition;
    private Hospital hospital;
    @ViewInject(R.id.hosLayout)
    private LinearLayout hosLayout;
    @ViewInject(R.id.regBtn)
    private View regBtn;
    private boolean isDayReg = false;// 是否当日挂号
    private ArrayList<Pool> savePoolList;
    @ViewInject(R.id.showNotTv)
    private TextView showNotTv;
//    private AlertDialog hintPerfectInfodialog = null;//提示信息对话框。

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctorindex30);
        savedInstanceState = getIntent().getExtras();
        if (savedInstanceState != null) {
            hospital = (Hospital) savedInstanceState
                    .getSerializable("hospital");
        }

        ViewUtils.inject(DoctorIndex30Activity.this);
        tvRequest = loadTitleBar(true, "医生详情", "请愿");
        tvBack = (TextView) findViewById(R.id.left_btn);
        tvBack.setOnClickListener(this);

        tvRequest.setVisibility(View.GONE);
        tvRequest.setEnabled(false);
        tvAttent.setEnabled(false);
        tvRequest.setVisibility(View.INVISIBLE);
        tvRequest.setBackgroundResource(R.drawable.selector_main_btn_bg);
        tvRequest.setOnClickListener(this);
        docId = getIntent().getStringExtra("docId");
        deptId = getIntent().getStringExtra("deptId");
        deptDocId = getIntent().getStringExtra("deptDocId");

//		tvGoodAt.setClickable(false);
        // 2015-12-18 医院对接
        if (getIntent().hasExtra("ehDockingStatus")) {
            ehDockingStatus = getIntent()
                    .getIntExtra("ehDockingStatus", NORMAL);
        }
        if (null != hospital)
            addrValueTv.setText(hospital.gethAddress());
        else {
            hosLayout.setVisibility(View.GONE);
        }

        if (BaseApplication.getInstance().isNetConnected) {
            initData();

            poolAdapter = new PoolAdapter(mContext, this);
            orderTimeLv.setAdapter(poolAdapter);

        } else {
            Toast.makeText(mContext, getString(R.string.network_hint),
                    Toast.LENGTH_SHORT).show();
        }
        wheelDialog = new WheelDialog(DoctorIndex30Activity.this);
    }

    /**
     * 获取数据
     */
    private void initData() {
        Log.i("initData","initData");
        showProgressToast();
        // 医生详情
        RequestParams params = new RequestParams();
        params.addBodyParameter("deptid", deptId);
        params.addBodyParameter("doctorid", docId);
        params.addBodyParameter("deptdocid", deptDocId);
        HospitalImpl impl = new HospitalImpl();
        impl.getDocInfo(params, this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAttentState();
        boolean canReg = getIntent().getBooleanExtra("canReg", false);
        if (canReg) {
            getPools();
        } else {
            orderTimeLv.setVisibility(View.GONE);
            showNotTv.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化医生头像
     */
    public void initDocPhoto(String photo) {
        Bitmap defaultBit = BitmapFactory.decodeResource(getResources(),
                R.drawable.default_doc_img);
        BitmapUtils bitmapUtils = new BitmapUtils(DoctorIndex30Activity.this);
        bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(
                mContext).scaleDown(3));
        bitmapUtils.configDefaultLoadingImage(defaultBit);
        bitmapUtils.configDefaultLoadFailedImage(defaultBit);
        bitmapUtils.display(imgHead, EZTConfig.DOC_PHOTO + photo);
    }

    /**
     * 请愿请求
     */
    private void petitionDoc() {

        RequestParams params = new RequestParams();
        UserImpl impl = new UserImpl();
        params.addBodyParameter("doctorId", docId);
        params.addBodyParameter("serviceIds", "" + 1);// 暂时写死，后期需改动
        impl.addPetition(params, this);
        showProgressToast();
    }

    /**
     * 检查关注状态
     */
    private void checkAttentState() {
        if (BaseApplication.patient != null) {

            RequestParams params1 = new RequestParams();
            params1.addBodyParameter("userId",
                    BaseApplication.patient.getUserId() + "");
            params1.addBodyParameter("deptDocId", deptDocId);
            new AttentionImpl().getAttentDocState(params1, this);
        }
    }

    @OnClick(R.id.attention)
    public void attentionClick(View v) {
        if (null == BaseApplication.patient) {
            HintToLogin(Constant.LOGIN_COMPLETE);
            return;
        }
        // 关注
        if (BaseApplication.getInstance().isNetConnected) {
            if ("+关注".equals(tvAttent.getText().toString().trim())) {
                if (BaseApplication.patient == null) {
                    HintToLogin(Constant.LOGIN_COMPLETE);
                    return;
                } else {
                    RequestParams params = new RequestParams();
                    params.addBodyParameter("contentId", docId);
                    params.addBodyParameter("userId",
                            BaseApplication.patient.getUserId() + "");

                    new AttentionImpl().attentDoc(params, this);
                }
            } else {// 取消关注
                RequestParams params = new RequestParams();
                params.addBodyParameter("id", attentId);
                new AttentionImpl().cancelAttentDoc(params, this);

            }
            tvAttent.setEnabled(false);
            showProgressToast();
        } else {
            Toast.makeText(mContext, getString(R.string.network_hint),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.goodAt)
    public void goodAtClick(View v) {
        Intent intent = new Intent();
        // 擅长
        intent.setClass(this, DoctorInfoActivity.class);
        if (doc != null) {
            intent.putExtra("docGoodAt", doc.getDocGoodAt());
            intent.putExtra("docIntro", doc.getDocIntro());
            // intent.putExtra("docEducBg", doc.getDocEducBg());
            intent.putExtra("docSuc", doc.getDocAcademicSuc());
        }
        startActivity(intent);
    }
    @OnClick(R.id.doctorPhone)
    public void doctorPhoneClick(View v) {
        // intent = new Intent();
        // 电话医生
        if (BaseApplication.patient == null) {
            HintToLogin(Constant.LOGIN_COMPLETE);
            return;
        }
        if (doc != null) {
            startActivity(new Intent(mContext, PhoneDoctorActivity.class)
                    .putExtra("doc", doc));
        }
        // if (intent != null) {
        // startActivity(intent);
        // }
    }

    @OnClick(R.id.seeIllRecord)
    public void seeIllRecordClick(View v) {
        Intent intent = new Intent();

        // 专家看病历

        if (BaseApplication.patient == null) {
            HintToLogin(Constant.LOGIN_COMPLETE);
            return;
        }
        intent.setClass(this, SeeIllRecordActivity.class);
        startActivity(intent);
    }

    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.left_btn:// 返回按钮
                onBackPressed();
                intent = null;
                break;
        }
        if (v == tvRequest) {
            // 请愿
            if (!BaseApplication.getInstance().isNetConnected) {
                Toast.makeText(mContext, getString(R.string.network_hint),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (BaseApplication.patient == null) {
                HintToLogin(Constant.LOGIN_COMPLETE);
                return;
            }
            hintDialog();
            intent = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if (hintPerfectInfodialog != null && hintPerfectInfodialog.isShowing()) {
//            hintPerfectInfodialog.dismiss();
//        }
        setResult(11);
        finish();

    }

    /**
     * 请愿弹出框
     */
    private void hintDialog() {
        String docName = tvName.getText().toString();
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setIcon(android.R.drawable.ic_dialog_info).setTitle("提示")
                .setMessage("确定请求" + docName + "医生开通电话医生服务？")
                .setCancelable(false)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        petitionDoc();
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
     * 温馨提示（针对儿童医院，距离>30公里）
     */
    public void hintLargeDistance() {
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setIcon(android.R.drawable.ic_dialog_info).setTitle("提示")
                .setMessage("您的行程已超出30公里外，将错过就诊时间，请预约其他时间段")
                .setCancelable(false)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

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

        AlertDialog hintPerfectInfodialog = builder.create();
        hintPerfectInfodialog.setOnKeyListener(new OnKeyListener() {

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
        if (!DoctorIndex30Activity.this.isFinishing())
            hintPerfectInfodialog.show();

    }

    /**
     * 判断用户信息完善度
     */
    public boolean judgeUserInfo() {

        if (TextUtils.isEmpty(BaseApplication.patient.getEpHiid())) {
            return false;
        }

        return true;
    }

    // 标记访问接口是否回调(成功+1)
    private int backCoumn = 0;

    // 获取关注状态
    private boolean isAttent = false;

    @Override
    public void result(Object... object) {
        if (object == null) {
            hideProgressToast();
            return;
        }
        int type = (Integer) object[0];
        boolean isSucc = (Boolean) object[1];
        switch (type) {
            case HttpParams.ADD_PETITION:// 请愿

                if (isSucc) {
                    Map<String, Object> petitionMap = (Map<String, Object>) object[2];
                    if (petitionMap != null) {
                        boolean flag = (Boolean) petitionMap.get("flag");
                        if (flag) {
                            Toast.makeText(mContext, "请愿成功！", Toast.LENGTH_SHORT)
                                    .show();
                            tvRequest.setText("已请愿");
                            tvRequest.setEnabled(false);
                            tvRequest.setOnClickListener(null);
                        } else {
                            Toast.makeText(mContext, "请愿失败，请稍后重试！",
                                    Toast.LENGTH_SHORT).show();
                            Logger.i("请愿失败--", petitionMap.get("msg"));
                        }
                    } else {
                        Toast.makeText(mContext, getString(R.string.service_error),
                                Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(mContext, object[3].toString(),
                            Toast.LENGTH_SHORT).show();
                }
                hideProgressToast();
                break;
            case HttpParams.GET_DOC_INFO:// 获取医生信息
                backCoumn++;
                if (isSucc) {// 成功
                    doc = (Doctor) object[2];
                } else {
                    Logger.i("获取医生信息", object[3]);
                }
                break;
            case HttpParams.ATTENT_DOC:// 关注医生
                hideProgressToast();
                tvAttent.setEnabled(true);
                if (isSucc) {
                    Map<String, Object> map = (Map<String, Object>) object[2];
                    if (null != map) {
                        boolean flag = (Boolean) map.get("flag");
                        if (flag) {// 成功
                            attentId = (String) map.get("id");

                            tvAttent.setTextColor(getResources().getColor(
                                    R.color.dark_gray));

                            tvAttent.setBackgroundResource(R.drawable.shape_border_small);
                            tvAttent.setTextSize(ResourceUtils.getXmlDef(mContext,
                                    R.dimen.doc_index_cancelattent));

                            tvAttent.setText("取消关注");
                        } else {
                            if (map.containsKey("msg") && null != map.get("msg"))
                                Toast.makeText(mContext, map.get("msg").toString(),
                                        Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(mContext, "服务器异常请您稍后再试",
                                        Toast.LENGTH_SHORT).show();

                        }
                    }
                } else {
                    Toast.makeText(mContext, object[3].toString(),
                            Toast.LENGTH_SHORT).show();
                }
                return;
            case HttpParams.CANCEL_ATTENT_DOC:// 取消关注
                hideProgressToast();
                tvAttent.setEnabled(true);
                if (isSucc) {
                    Map<String, Object> map = (Map<String, Object>) object[2];
                    if (map != null && map.size() != 0) {
                        boolean flag = (Boolean) map.get("flag");
                        if (flag) {// 成功
                            tvAttent.setTextColor(getResources().getColor(
                                    R.color.light_main_color));
                            tvAttent.setBackgroundResource(R.drawable.shape_main_color_broder_bg);
                            tvAttent.setTextSize(ResourceUtils.getXmlDef(mContext,
                                    R.dimen.doc_index_attent));
                            tvAttent.setText("+关注");
                        } else {
                            Toast.makeText(mContext, map.get("msg").toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(mContext, object[3].toString(),
                            Toast.LENGTH_SHORT).show();
                }
                return;

            case HttpParams.GET_ATTENT_DOC_STATE:// 获取关注状态
                backCoumn++;
                if (isSucc) {
                    Map<String, Object> map = (Map<String, Object>) object[2];
                    if (map != null) {
                        if (map.get("flag") != null) {
                            isAttent = (Boolean) map.get("flag");
                        }

                        if (isAttent)
                            attentId = (String) map.get("id");
                        if (null != map.get("msg")) {
                            if (map.get("msg").toString().equals("未关注")) {
                                tvAttent.setText("+关注");
                            } else {
                                tvAttent.setBackgroundResource(R.drawable.shape_border_small);
                                tvAttent.setTextSize(ResourceUtils.getXmlDef(mContext,
                                        R.dimen.doc_index_cancelattent));
                                tvAttent.setTextColor(getResources().getColor(
                                        R.color.dark_gray));
                                tvAttent.setText("取消关注");
                            }
                        }
                    } else {
                        Logger.i("获取关注状态", object[3]);
                    }
                }
                break;

            case HttpParams.GET_DOC_POOL:// 获取号池
                if (isSucc) {// 成功
                    poolist = (ArrayList<Pool>) object[2];
                    dealWithPoolData();

                }
                break;

        }

        tvAttent.setEnabled(true);

        /**
         * 医生信息
         */
        if (doc != null) {
            tvName.setText(doc.getDocName());
            hosId = doc.getDocHosId();
            initDocPhoto(doc.getDocHeadUrl());
            if (doc.getDocPosition() != null) {
                String strLevel = EztDictionaryDB.getInstance(mContext)
                        .getLabelByTag("doctorLevel", doc.getDocPosition());

                tvPosition.setText(strLevel);
            }
            tvHos.setText(doc.getDocHos());
            tvDept.setText(doc.getDocDept());

            String str = doc.getDocGoodAt();
            tvGoodAt.setText(TextUtils.isEmpty(doc.getDocGoodAt()) ? "暂无擅长"
                    : str);
        }

        hideProgressToast();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 222) {
            checkAttentState();
        }
    }

    // 停诊bug 解决所需
    private ArrayList<Pool> poolist;//
    // 存储最终的号池
    // private ArrayList<Pool> savePoolList;
    private PoolAdapter poolAdapter;

    /**
     * 获取号池资料
     */
    private void getPools() {

        RequestParams params = new RequestParams();
        params.addBodyParameter("deptid", deptId);
        params.addBodyParameter("doctorid", docId);
        params.addBodyParameter("isExist", 1 + "");// 默认查询预约未满的号池信息
        HospitalImpl impl = new HospitalImpl();
        impl.getDocPool(params, this);
    }

    /**
     * 处理号池信息
     */
    private void dealWithPoolData() {
        List<PoolTimes> ptList;
        savePoolList = new ArrayList<Pool>();
        ArrayList<Pool> dayPoolList = new ArrayList<Pool>();
        if (null != poolist && poolist.size() > 0) {
            orderTimeLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < poolist.size(); i++) {
                // 从号池里获取时间列表
                ptList = poolist.get(i).getTimeList();
                if (ptList == null || ptList.size() == 0) {// 时间表为null
                    continue;
                }
                for (int j = 0; j < ptList.size(); j++) {
                    if (!ptList.get(j).isRemains()) {
                        continue;
                    } else {
                        Pool tempPool = poolist.get(i);
                        tempPool.setRemain(1);
                        savePoolList.add(tempPool);
                        // 当日挂号处理
                        SimpleDateFormat sdf = new SimpleDateFormat(
                                "yyyy-MM-dd");
                        String todayStr = sdf.format(new Date());
                        if (todayStr.equals(tempPool.getDate())) {
                            dayPoolList.add(tempPool);
                        }

                        break;
                    }
                }
            }
            if (savePoolList.size() != 0) {
                isDayReg = getIntent().getBooleanExtra(
                        EZTConfig.KEY_DF_IS_DAY_REG, false);
                if (isDayReg) {// 若是当日挂号则显示预约挂号按钮，点击显示全部号

                    if (dayPoolList.size() == 0) {
                        orderTimeLv.setVisibility(View.GONE);
                        showNotTv.setVisibility(View.VISIBLE);
                    }
                    regBtn.setVisibility(View.VISIBLE);
                    poolAdapter.setList(dayPoolList);
                } else {// 若不是则只显示当天号
                    regBtn.setVisibility(View.GONE);
                    poolAdapter.setList(savePoolList);
                }
            } else {
                // 此处代码无用
                regBtn.setVisibility(View.GONE);
            }

        } else {
            // Toast.makeText(mContext, getString(R.string.sorry_pool_wrong),
            // Toast.LENGTH_SHORT).show();
            regBtn.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.regBtn)
    public void regBtnClick(View view) {
        view.setVisibility(View.GONE);
        isDayReg = false;
        SystemPreferences.save(EZTConfig.KEY_DF_IS_DAY_REG, false);

        if (savePoolList.size() > 0) {
            orderTimeLv.setVisibility(View.VISIBLE);
            showNotTv.setVisibility(View.GONE);
        }
        poolAdapter.setList(savePoolList);
    }

    @Override
    public void onRegClick(int position) {
        onItemClick(position);
    }

    @OnItemClick(R.id.orderTimeLv)
    public void onPoolItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
        onItemClick(position);
    }

    private void onItemClick(int position) {
        Intent intent = new Intent();
        // 预约挂号
        if (!BaseApplication.getInstance().isNetConnected) {
            Toast.makeText(mContext, getString(R.string.network_hint),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (BaseApplication.patient != null) {
            if (BaseApplication.patient != null && (TextUtils.isEmpty(BaseApplication.patient.getEpMobile()))) {
                hintPerfectInfo("请完善个人手机号！", 2);

            } else {
                double distance = 0.0;
                if (doc != null) {
                    distance = MapUtil.calculatePath(doc.getHosLat(),
                            doc.getHosLon());
                }
                if (hosId.equals("83") && distance > 30 * 1000) {// 判断是否是深圳儿童医院，并且距离是否>30公里
                    intent = null;
                    hintLargeDistance();
                    return;
                } else {
                    if (isDayReg) {
                        if (!TextUtils
                                .isEmpty(BaseApplication.getInstance().city)) {
                            if (null != hospital) {
                                if (!hospital.gethName().contains(
                                        BaseApplication.getInstance().city)) {
                                    // 判断非本地医院
                                    toast("亲，暂不支持预约非本地的当天号", Toast.LENGTH_SHORT);
                                    return;
                                }
                            }
                        }

                    }

                    // 记录当前位置 哪一天
                    currentPoolClickPosition = position;
                    Pool pool = (Pool) poolAdapter.getItem(position);
                    setWheelData(pool);
                    Window win = wheelDialog.getWindow();
                    LayoutParams params = wheelDialog.getWindow()
                            .getAttributes();
                    win.setGravity(Gravity.BOTTOM);
                    params.width = ScreenUtils.gainDM(mContext).widthPixels;
                    params.height = ScreenUtils.gainDM(mContext).heightPixels / 3;
                    wheelDialog.show();
                }
            }
        } else {
            HintToLogin(11);
            return;
        }

        // if (intent != null) {
        // startActivity(intent);
        // }
    }

    /**
     * 设置滚轮号池数据
     *
     * @param pool
     */
    private void setWheelData(Pool pool) {
        List<PoolTimes> poolTimeList = pool.getTimeList();
        List<String> wheelPoolDatas = new ArrayList<String>();

        String titleStr = getString(R.string.title_DocIndex_Wheel_day);
        for (PoolTimes poolTimes : poolTimeList) {

            StringBuffer itemStr = new StringBuffer();
            if (!isDayReg) {// 不是当日挂号
                itemStr.append(pool.getDate());
                titleStr = getString(R.string.title_DocIndex_Wheel);
            }
            itemStr.append(" " + poolTimes.getStartDates().trim() + "-"
                    + poolTimes.getEndDates().trim());
            wheelPoolDatas.add(itemStr.toString());
        }
        List<Integer> currIndexs = new ArrayList<Integer>();
        currIndexs.add(0);
        List<List<String>> wheelData = new ArrayList<List<String>>();
        wheelData.add(wheelPoolDatas);
        List<Integer> wheelIds = new ArrayList<Integer>();
        wheelIds.add(0);
        wheelDialog.config(this, this, wheelData, currIndexs, wheelIds, this,
                this, this, titleStr);
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO 自动生成的方法存根
        wheelDialog.setTextviewSize(wheel.getCurrentItem(),
                (TextAdapter) wheel.getViewAdapter());
    }

    @Override
    public void onScrollingStarted(WheelView wheel) {
        // TODO 自动生成的方法存根

    }

    @Override
    public void onScrollingFinished(WheelView wheel) {
        // TODO 自动生成的方法存根
        wheelDialog.setTextviewSize(wheel.getCurrentItem(),
                (TextAdapter) wheel.getViewAdapter());
    }

    @Override
    public void onItemClicked(WheelView wheel, int itemIndex) {
        // TODO 自动生成的方法存根

    }

    @Override
    public void cancleClick() {
        // TODO 自动生成的方法存根

    }

    @Override
    public void sureClick(LinearLayout wheelLayout) {
        wheelDialog.dismiss();
        Intent intent = new Intent();
        intent.setClass(this, OrderRegistrationActivity.class);
        intent.putExtra("pos", currentPoolClickPosition);
        Pool pool = (Pool) poolAdapter.getItem(currentPoolClickPosition);

        intent.putExtra("poolDate", pool.getDate());
        intent.putExtra("poolList", (Serializable) poolAdapter.mList);
        intent.putExtra("deptid", deptId);
        intent.putExtra("doctorid", docId);
        intent.putExtra("hosId", hosId);
        intent.putExtra("hosName", tvHos.getText().toString());
        intent.putExtra("docName", tvName.getText().toString());
        if (doc != null) {
            intent.putExtra("docDept", doc.getDocDept());
        } else {
            intent.putExtra("docDept", "");
        }
        WheelView wheelView = (WheelView) wheelLayout.getChildAt(0);
        intent.putExtra("timePos", wheelView.getCurrentItem());
        startActivity(intent);
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (hintPerfectInfodialog != null && hintPerfectInfodialog.isShowing()) {
//            hintPerfectInfodialog.dismiss();
//        }
//    }
}
