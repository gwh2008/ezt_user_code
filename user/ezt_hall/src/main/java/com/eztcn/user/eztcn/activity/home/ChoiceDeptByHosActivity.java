package com.eztcn.user.eztcn.activity.home;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnItemClick;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.MyDialog.DialogCancle;
import com.eztcn.user.eztcn.activity.home.MyDialog.DialogSure;
import com.eztcn.user.eztcn.activity.home.orderbed.ActivityOrderBedWrite;
import com.eztcn.user.eztcn.adapter.Dept2DataAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Dept;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.impl.HospitalImpl;
import com.eztcn.user.eztcn.utils.CharacterParser;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.PinyinComparatorDept;
import com.eztcn.user.hall.utils.Constant;

/**
 * @author ezt
 * @title 根据医院id选择科室
 * @describe
 * @created 2015年2月9日
 */
public class ChoiceDeptByHosActivity extends FinalActivity implements
        IHttpResult, DialogSure, DialogCancle {

    @ViewInject(R.id.deptList)
    // , itemClick = "onItemClick"
    private ListView deptList;

    private Dept2DataAdapter adapterDept;// 科室adapter
    private String hospitalId;
    private String hosName;
    private int deptPos;// 选中的医院科室下标
    private List<Dept> list;
    private CharacterParser characterParser;// 汉字转换成拼音的类
    private boolean isOrderBed = false;
    private boolean isNearHos = false;// 是否为附近医院进入
    private boolean isHosDetail = false;// 是否为医院详情科室医生进入
    /**
     * 是否妇女幼儿医院
     */
    private boolean isWoB = false;
    private Hospital hospital;
    @ViewInject(R.id.orderbed_hintLayout)
    private View orderbed_hintLayout;
    @ViewInject(R.id.bedHintTv)
    private TextView bedHintTv;
    private String costStr = "";
    private MyDialog dialog;
    /**
     * 预约病床的部门id
     */
    private int bedSelDetpId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstanceState = getIntent().getExtras();
        if (savedInstanceState != null) {
            hospital = (Hospital) savedInstanceState
                    .getSerializable("hospital");
        }
        gainIsSpecial();
        setContentView(R.layout.choicedeptbyhos);
        ViewUtils.inject(ChoiceDeptByHosActivity.this);
        // loadTitleBar(true, "选择科室", null);
        isOrderBed = getIntent().getBooleanExtra("isOrderBed", false);
        if (!isOrderBed) {
            orderbed_hintLayout.setVisibility(View.GONE);
        }

        characterParser = CharacterParser.getInstance();
        adapterDept = new Dept2DataAdapter(this, false);
        deptList.setAdapter(adapterDept);
        isNearHos = getIntent().getBooleanExtra("isNearHos", false);
        isHosDetail = getIntent().getBooleanExtra("isHosDetail", false);
        if (isNearHos || isHosDetail) {
            hospitalId = getIntent().getStringExtra("hosId");
            hosName = getIntent().getStringExtra("hosName");
        } else {

            if (!isWoB) {
                hospitalId = SystemPreferences.getString(EZTConfig.KEY_HOS_ID);
                hosName = SystemPreferences.getString(EZTConfig.KEY_HOS_NAME);
                deptPos = SystemPreferences.getInt(
                        EZTConfig.KEY_SELECT_DEPT_POS, -1);
            }

            // else {
            // hosName = SystemPreferences
            // .getString(EZTConfig.KEY_WB_HOS_NAME);
            // hospitalId = SystemPreferences
            // .getString(EZTConfig.KEY_WB_HOS_ID);
            // deptPos = SystemPreferences.getInt(
            // EZTConfig.KEY_SELECT_WB_DEPT_POS, -1);
            // }
            // deptList.setSelector(R.color.green);
        }
        loadTitleBar(true, hosName.equals("") ? "选择科室" : hosName, null);
        getDeptTypes();
    }

    /**
     * 获取医院科室列表
     */
    private void getDeptTypes() {// hospitalId=18&level=1
        // HashMap<String, Object> params = new HashMap<String, Object>();
        // HospitalImpl impl = new HospitalImpl();
        // params.put("hospitalId", hospitalId);

        RequestParams params = new RequestParams();
        HospitalImpl impl = new HospitalImpl();
        params.addBodyParameter("hospitalId", hospitalId);
        if (!isOrderBed)
            impl.getDeptList2(params, this);
        else
            impl.findResBedDept(params, this);
        showProgressToast();
    }

    /**
     * 获取妇幼
     */
    private void gainIsSpecial() {
        Intent intent = getIntent();
        if (null != intent && intent.getIntExtra("isWomenBaby", 0) == 1) {
            isWoB = true;
        } else {
            isWoB = false;
        }
    }

    /**
     * listview item
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    // public void onItemClick(AdapterView<?> parent, View view, int position,
    // long id) {
    @OnItemClick(R.id.deptList)
    public void itemClick(AdapterView<?> parent, View view, int position,
                          long id) {
        deptPos = position;
        adapterDept.notifyDataSetChanged();
        Dept dept = list.get(position);
        if (isOrderBed) {
            bedSelDetpId = dept.getId();
            if (null == BaseApplication.patient) {
                HintToLogin(Constant.LOGIN_COMPLETE);
                return;
            }
            if(null==BaseApplication.patient.getEpPid()){
                hintPerfectInfo("请完善个人信息",1,ChoiceDeptByHosActivity.this);
                return;
            }
            View viewContent = View.inflate(mContext,
                    R.layout.ordercheck_dialog, null);
            TextView costTv = (TextView) viewContent
                    .findViewById(R.id.orderHintTv);
            if (StringUtils.isNotBlank(costStr))
                costTv.setText(String.format(
                        getResources().getString(R.string.orderbedhint),
                        costStr));
            if (null != viewContent) {
                dialog = new MyDialog(this, "确定", "取消", "支付提醒", viewContent);
                dialog.setDialogSure(this);
                dialog.setDialogCancle(this);
                dialog.show();
            }

        } else {
            if (isNearHos) {
                Intent intent = new Intent();
                intent.putExtra("deptId", dept.getId() + "").putExtra(
                        "deptName", dept.getdName());
                setResult(22, intent);
                // finish();
            } else {

                if (!isHosDetail) {// 快速预约进入
                    if (!isWoB) {
                        SystemPreferences.save(EZTConfig.KEY_DEPT_ID,
                                dept.getId() + "");
                        SystemPreferences.save(EZTConfig.KEY_STR_DEPT,
                                dept.getdName());
                        SystemPreferences.save(EZTConfig.KEY_SELECT_DEPT_POS,
                                position);
                    }
                    if (TextUtils.isEmpty(hospitalId)) {
                        Toast.makeText(this, "请选择医院", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    } else if (dept.getId() == 0) {
                        Toast.makeText(this, "请选择科室", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    } else {
                        Intent intent = new Intent(getApplicationContext(),
                                DoctorList30Activity.class);
                        if (isWoB) {
                            intent.putExtra("isWomenBaby", 1);

                        }
                        intent.putExtra("hospital", hospital);
                        startActivity(intent.putExtra("hosName", hosName)
                                .putExtra("deptName", dept.getdName())
                                .putExtra("type", 1));
                    }
                } else {// 医生详情 点击科室医生进入
                    Intent intent = new Intent(mContext,
                            DoctorList30Activity.class);
                    if (isWoB) {
                        intent.putExtra("isWomenBaby", 1);
                    }
                    intent.putExtra("hospital", hospital);
                    startActivity(intent.putExtra("type", 1)
                            .putExtra("deptName", dept.getdName())

                            .putExtra("hosId", this.hospitalId)
                            .putExtra("deptId", dept.getId() + "")

                            .putExtra("isAllSearch", true)
                            .putExtra("hosName", hosName));
                }
            }
        }

    }

    @Override
    public void result(Object... object) {
        hideProgressToast();
        if (object == null) {
            return;
        }
        Object[] obj = object;
        Integer taskID = (Integer) obj[0];
        if (taskID == null) {
            return;
        }
        boolean status = (Boolean) obj[1];
        if (!status) {
            Toast.makeText(mContext, obj[3] + "", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean flag = (Boolean) object[1];
        switch (taskID) {
            case HttpParams.GET_DEPT_LIST2:
                // if (flag) {
                // list = (ArrayList<Dept>) object[2];
                // }
                // if (list == null) {
                // list = new ArrayList<Dept>();
                // }
                // if (list.size() == 0) {
                // Toast.makeText(this, "暂无可预约科室", Toast.LENGTH_SHORT).show();
                // }

                HashMap<String, Object> map = (HashMap<String, Object>) object[2];
                if (flag) {
                    if (map.containsKey("deptList")) {

                        list = (List<Dept>) map.get("deptList");
                    }
                    // list = (ArrayList<Dept>) object[2];2015-12-6 更新对接接口
                }
                // if (list == null) {2015-12-6 更新对接接口
                // list = new ArrayList<Dept>();
                // }
                // if (list.size() == 0) {2015-12-6 更新对接接口
                // Toast.makeText(this, "暂无可预约科室", Toast.LENGTH_SHORT).show();
                // }
                if (isOrderBed) {
                    if (map.containsKey("bedPrice")) {
                        orderbed_hintLayout.setVisibility(View.VISIBLE);
                        costStr = (String) map.get("bedPrice");

                        costStr = new DecimalFormat("###,###,##0.00").format(Double
                                .parseDouble(costStr));

                        bedHintTv.setText("办理预约病床需要收取" + costStr + "元挂号费");
                    }
                }
                if (null != list) {// 2015-12-6 更新对接接口
                    for (int i = 0; i < list.size(); i++) {
                        Dept sortModel = new Dept();
                        String dName = list.get(i).getdName();
                        sortModel.setdName(dName);
                        sortModel.setId(list.get(i).getId());
                        // 汉字转换成拼音
                        String pinyin = characterParser.getSelling(dName);
                        String sortString = pinyin.substring(0, 1).toUpperCase();

//					// 正则表达式，判断首字母是否是英文字母
//					if (sortString.matches("[A-Z]")) {
//						sortModel.setSortLetters(sortString.toUpperCase());
//					} else {
//						sortModel.setSortLetters("#");
//					}
//					list.set(i, sortModel);
                    }
                    // 根据a-z进行排序源数据
//				Collections.sort(list, new PinyinComparatorDept());
//				if (deptPos != -1 && !isNearHos && !isHosDetail) {
//					adapterDept.setSelectedPosition(deptPos);
//				}
                    adapterDept.setList(list);

                } else {
                    // 2015-12-6 更新对接接口
                    String errorMsg = "暂无可预约科室";
                    if (map.containsKey("msg")) {
                        errorMsg = String.valueOf(map.get("msg"));
                    }
                    Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void dialogCancle() {
        dialog.dissMiss();
    }

    @Override
    public void dialogSure() {
        dialog.dissMiss();
        // 预约病床跳转
        Intent intent = new Intent();
        intent.setClass(mContext, ActivityOrderBedWrite.class);
        intent.putExtra("cost", costStr);
        intent.putExtra("hospital", hospital);
        intent.putExtra("deptId", bedSelDetpId);
        startActivity(intent);
    }
}
