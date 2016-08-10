package com.eztcn.user.hall.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.interfaces.IFormListener;
import com.eztcn.user.hall.interfaces.SelectorListener;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.PatientBean;
import com.eztcn.user.hall.model.Request;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.model.ResultResponse.AttentionDoctorResponse;
import com.eztcn.user.hall.model.ResultResponse.DoctorDatasOneLevelResponse;
import com.eztcn.user.hall.model.ResultResponse.DoctorListResponse;
import com.eztcn.user.hall.model.ResultResponse.WeekDatasResponse;
import com.eztcn.user.hall.model.ResultResponse.WeekTimesCountDataResponse;
import com.eztcn.user.hall.utils.CommonDialog;
import com.eztcn.user.hall.utils.Constant;
import com.eztcn.user.hall.utils.FileUtils;
import com.eztcn.user.hall.utils.GlideUtils;
import com.eztcn.user.hall.utils.ToastUtils;
import com.eztcn.user.hall.views.FormView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 选择预约时间页面
 *
 * @author 蒙
 */
public class SelectAppointmentTimeActivity extends BaseActivity implements View.OnClickListener,
        ResultSubscriber.OnResultListener {
    private TextView good_at_content;
    private boolean isOpen = false;//擅长文字是否打开全展示
    private ImageView right_top;
    private ViewPager viewPager;
    private Button commit;

    private ImageView headpic;
    private TextView name;
    private ImageView doctorLevel;
    private TextView hospitalName;
    private TextView departmentName;
    private Adapter adapter = new Adapter();


    private DoctorListResponse doctorData;//上个页面传过来的医生的一些信息

    private boolean isAttention;//是否关注了该医生

    private DoctorDatasOneLevelResponse datasOneLevelResponse;//网络加载的医生数据
    private String cancelAttentionId;//取消关注需要用到的id

    private int attention_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int preView() {
        return R.layout.new_activity_select_appointment_time;
    }
    TextView count_attention;
    public void initView() {
        good_at_content = (TextView) findViewById(R.id.new_activity_select_appointment_time_good_at_content);
        findViewById(R.id.new_activity_select_appointment_time_open_text).setOnClickListener(this);
        findViewById(R.id.new_activity_select_appointment_time_rank).setOnClickListener(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(0);
        commit = (Button) findViewById(R.id.new_activity_select_appointment_time_commit);
        commit.setEnabled(false);
        commit.setOnClickListener(this);
         count_attention = (TextView) findViewById(R.id.new_activity_select_appointment_time_count_attention);
    }
    private int index_pager=0;
    @Override
    protected void initData() {
        doctorData = (DoctorListResponse) getIntent().getSerializableExtra("hospital");
        headpic = (ImageView) findViewById(R.id.new_activity_select_appointment_time_headPic);
        if ("0".equals(doctorData.getDocSex())) {//性别为男
            GlideUtils.intoRound(mContext, doctorData.getDocPic(), 0, headpic
                    , R.drawable.new_default_doctor_head_man);
        } else {//性别为女
            GlideUtils.intoRound(mContext, doctorData.getDocPic(), 0, headpic
                    , R.drawable.new_default_doctor_head_women);
        }
        doctorLevel = (ImageView) findViewById(R.id.new_activity_select_appointment_time_doctor_type);
        //医生级别的加载
        if (doctorData.getLevelName().contains("主治")){
            doctorLevel.setImageResource(R.drawable.new_attending_doctor_orange);
        }else if (doctorData.getLevelName().contains("副")){
            doctorLevel.setImageResource(R.drawable.new_chief_doctor_second_orange);
        }else if (doctorData.getLevelName().contains("主任")){
            doctorLevel.setImageResource(R.drawable.new_chief_doctor_orange);
        }

        //医生一些基本信息的加载
        name = (TextView) findViewById(R.id.new_activity_select_appointment_time_name);
        name.setText(doctorData.getDocName());
        hospitalName = (TextView) findViewById(R.id.new_activity_select_appointment_time_hospital_name);

        //导入字体库
//        Typeface typeFace =Typeface.createFromAsset(getAssets(), "fonts/robotolight.ttf");
//        hospitalName.setTypeface(typeFace);

        hospitalName.setText(doctorData.getHospitalName());
        departmentName = (TextView) findViewById(R.id.new_activity_select_appointment_time_department_name);
        departmentName.setText(doctorData.getDptName());
        good_at_content.setText(doctorData.getGoodAt());

        //默认不显示右上角的关注按钮
        right_top = loadTitleBar(true, "选择预约时间", 0);
        right_top.setOnClickListener(this);

        getDoctorDatas();
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                index_pager=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        getWeeksDatas();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_activity_select_appointment_time_open_text:
                if (isOpen) {
                    //将擅长信息隐藏，展示局部
                    good_at_content.setMaxLines(2);
                    isOpen = false;
                } else {
                    //将擅长信息展开，全部显示
                    good_at_content.setMaxLines(5000);
                    isOpen = true;
                }
                break;
            case R.id.new_activity_select_appointment_time_rank://排行榜页面
//                startActivity(new Intent(mContext, DoctorRankingActivity.class).putExtra("deptId", doctorData.getDeptId()));
                break;
            case R.id.new_activity_select_appointment_time_commit:
                Intent intent = new Intent(mContext, ConfirmAppointmentActivity.class);
                intent.putExtra("doctorData", doctorData);//医生信息
                intent.putExtra("timeData", weekTimesCountDataResponse);//时间段数据
                startActivity(intent);
                break;
            case R.id.right_btn1://关注或者取消关注医生
                if (isAttention) {
                    cancelAttentionDoctor();
                } else {
                    attentionDoctor();
                }
                break;
        }
    }

    /**
     * 关注医生访问接口
     */
    public void attentionDoctor() {
        showProgressDialog("正在关注该医生...");
        Request request = new Request();
        Map<String, String> params = new HashMap<>();
        params.put("userId", ((PatientBean) FileUtils.getObject(mContext, "patient")).getUserId() + "");//用户id
        params.put("contentId", doctorData.getDocId());//医生id
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC).attentionDoctor(request.getFormMap(params), 111, this);
    }

    /**
     * 取消关注医生访问接口
     */
    public void cancelAttentionDoctor() {
        showProgressDialog("正在取消关注...");
        Request request = new Request();
        Map<String, String> params = new HashMap<>();
        params.put("id", cancelAttentionId);
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC).cancelAttentionDoctor(request.getFormMap(params), 222, this);
    }

    /**
     * 获取医生数据
     */
    public void getDoctorDatas() {
        showProgressDialog("加载数据中...");
        Request request = new Request();
        Map<String, String> params = new HashMap<>();
        params.put("deptId", doctorData.getDeptId());//科室id
        params.put("docId", doctorData.getDocId());//医生id
        params.put("userId", ((PatientBean) FileUtils.getObject(mContext, "patient")).getUserId() + "");//用户id
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.PASS).getDoctorDatas(request.getFormMap(params), 333, this);

    }

    /**
     * 获取星期表数据
     */
    public void getWeeksDatas() {
        showProgressDialog("加载数据中...");
        Request request = new Request();
        Map<String, String> params = new HashMap<>();
        params.put("doctorid", doctorData.getDocId());//医生id
        params.put("deptid", doctorData.getDeptId());//科室id
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.PASS).getWeeksDatas(request.getFormMap(params), 444, this);
    }


    @Override
    public void onNext(IModel t, int requestType) {
        dismissProgressDialog();
        Response response = (Response) t;
        if (response.isFlag()) {
            if (requestType == 111) {//关注医生成功
                cancelAttentionId = ((AttentionDoctorResponse) response.getData()).getId();
                isAttention = true;
                right_top.setImageResource(R.drawable.new_has_attention);

                attention_count++;
                count_attention.setText(attention_count+"");

                ToastUtils.shortToast(mContext, "关注成功");
            } else if (requestType == 222) {//取消关注医生成功
                isAttention = false;
                right_top.setImageResource(R.drawable.new_not_attention);

                attention_count--;
                count_attention.setText(attention_count+"");

                ToastUtils.shortToast(mContext, "关注已经取消");
            } else if (requestType == 333) {//获取医生数据
                datasOneLevelResponse = (DoctorDatasOneLevelResponse) response.getData();
                if (datasOneLevelResponse == null) {
                    datasOneLevelResponse = new DoctorDatasOneLevelResponse();
                }
                if ("-1".equals(datasOneLevelResponse.getIsCollect())) {
                    isAttention = false;
                } else {
                    isAttention = true;
                }
                cancelAttentionId = datasOneLevelResponse.getIsCollect();
                if (isAttention) {//根据关注状态进行赋值
                    right_top.setImageResource(R.drawable.new_has_attention);
                } else {
                    right_top.setImageResource(R.drawable.new_not_attention);
                }
                right_top.setVisibility(View.VISIBLE);
                TextView count_appoint = (TextView) findViewById(R.id.new_activity_select_appointment_time_count_appoint);

                TextView week = (TextView) findViewById(R.id.new_activity_select_appointment_time_week);
                TextView month = (TextView) findViewById(R.id.new_activity_select_appointment_time_month);
                TextView year = (TextView) findViewById(R.id.new_activity_select_appointment_time_year);
                count_appoint.setText(datasOneLevelResponse.getRegCount());
                count_attention.setText(datasOneLevelResponse.getCollectCount());
                attention_count=Integer.parseInt(datasOneLevelResponse.getCollectCount());
                week.setText(datasOneLevelResponse.getRankBean().getWeekRank());
                if ("1".equals(datasOneLevelResponse.getRankBean().getWeekFlag())) {//上升
                    week.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.new_rank_add, 0);
                } else {//下降
                    week.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.new_rank_reduce, 0);
                }
                month.setText(datasOneLevelResponse.getRankBean().getMonthRank());
                if ("1".equals(datasOneLevelResponse.getRankBean().getMonthFlag())) {//上升
                    month.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.new_rank_add, 0);
                } else {//下降
                    month.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.new_rank_reduce, 0);
                }
                year.setText(datasOneLevelResponse.getRankBean().getYearRank());
                if ("1".equals(datasOneLevelResponse.getRankBean().getYearFlag())) {//上升
                    year.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.new_rank_add, 0);
                } else {//下降
                    year.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.new_rank_reduce, 0);
                }
            } else if (requestType == 444) {//获取星期表数据
                weekDatas = (ArrayList<WeekDatasResponse>) response.getData();
                if (weekDatas == null) {
                    weekDatas = new ArrayList<>();
                }
                if (weekDatas.size()!=0){
                    findViewById(R.id.new_week_table_no_data).setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }
        } else {
            ToastUtils.shortToast(mContext, response.getDetailMsg());
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

    private ArrayList<WeekDatasResponse> weekDatas = new ArrayList<>();
    private HashMap<Integer,FormView> formViewHashMap=new HashMap<>();


    private String[] colText = {"排班", "上午", "下午"};//星期表格第一列的数据文本

    class Adapter extends PagerAdapter {
        @Override
        public int getCount() {
            return weekDatas.size() / 7;//将数据源 分为7的倍数
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //构建每一个viewpager的数据源，也就是每一个FormView上午数据源
            final String[] rowDates = new String[8];
            final String[] rowText = new String[8];
            HashMap<Integer, ArrayList<WeekTimesCountDataResponse>> timesData = new HashMap<>();
            positionToData(position, rowDates, rowText, timesData);

            final FormView myFormView = new FormView(mContext);
            myFormView.setTimesAllData(timesData);//所有时间段的数据源
            myFormView.setPosition(position);//当前在viewpager中的位置，是第几个
            //设置点击监听
            myFormView.setFormListener(new IFormListener() {
                @Override
                public void showNum(int index, ArrayList<WeekTimesCountDataResponse> datas) {
                    showInfo(index, datas);
                }
            });
            ViewTreeObserver vto = myFormView.getViewTreeObserver();

            //观察加载完之后这是数据，进行绘制
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    myFormView.getViewTreeObserver().removeOnPreDrawListener(this);
//                    int height = myFormView.getMeasuredHeight();
//                    int width = myFormView.getMeasuredWidth();
//                    myFormView.init(width, height);
                    myFormView.setRowAndColText(rowText, colText, rowDates);
                    return true;
                }
            });

            //设置点击事件监听，
            myFormView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {//在up事件的时候进行事件的监听
                        float touchX = event.getX();
                        float touchY = event.getY();
                        myFormView.testTouchColorPanel(touchX, touchY);
                    }
                    return true;
                }
            });
            container.addView(myFormView);
            formViewHashMap.put(position,myFormView);
            return myFormView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    /**
     * 由viewpager的位置所以构建该条目需要的数据源
     *
     * @param position  viewpager的位置所以
     * @param rowDates  行小子文本数据
     * @param rowText   行文本数据
     * @param timesData 该条目需要的整体数据
     */
    public void positionToData(int position, String[] rowDates, String[] rowText
            , HashMap<Integer, ArrayList<WeekTimesCountDataResponse>> timesData) {
        rowText[0] = "";
        rowDates[0] = "";
        for (int i = 1; i < 8; i++) {
            rowText[i] = numberToWeek(weekDatas.get(position * 7 + i - 1).getDoctorPools().get(0).getRegDateWeek());
            rowDates[i] = weekDatas.get(position * 7 + i - 1).getDoctorPoolDate().substring(5);
        }

        for (int i = 0; i < 7; i++) {
            int num = Integer.parseInt(weekDatas.get(position * 7 + i).getDoctorPoolsNums());
            if (num != 0) {
                ArrayList<WeekTimesCountDataResponse> list_am = new ArrayList<>();
                ArrayList<WeekTimesCountDataResponse> list_pm = new ArrayList<>();

                for (int i1 = 0; i1 < num; i1++) {
                    WeekTimesCountDataResponse times = weekDatas.get(position * 7 + i).getDoctorPools().get(i1);
                    if ("0".equals(times.getIsAmPm())) {//上午
                        list_am.add(times);
                    } else {//下午
                        list_pm.add(times);
                    }
                }
                if (list_am.size() > 0) {
                    timesData.put(9 + i, list_am);
                }
                if (list_pm.size() > 0) {
                    timesData.put(17 + i, list_pm);
                }


            }
        }


    }

    /**
     * 根据数据获取对应的星期数
     *
     * @param number
     * @return
     */
    public static String numberToWeek(String number) {
        String weekString = "";
        if ("1".equals(number)) {
            weekString = "周一";
        } else if ("2".equals(number)) {
            weekString = "周二";
        } else if ("3".equals(number)) {
            weekString = "周三";
        } else if ("4".equals(number)) {
            weekString = "周四";
        } else if ("5".equals(number)) {
            weekString = "周五";
        } else if ("6".equals(number)) {
            weekString = "周六";
        } else if ("7".equals(number)) {
            weekString = "周日";
        }
        return weekString;
    }

    private String[] times; //弹出框里面的时间数据源
    private WeekTimesCountDataResponse weekTimesCountDataResponse;//选中的时间段实体类

    private void showInfo(final int index, final ArrayList<WeekTimesCountDataResponse> datas) {
        times = new String[datas.size()];
        for (int i = 0; i < datas.size(); i++) {//构建弹出框时间数据源
            times[i] = datas.get(i).getStartDate().substring(11, 16) + "--" + datas.get(i).getEndDate().substring(11, 16);
        }
        CommonDialog.showTimeDialog(this, "可预约时间", times, new SelectorListener() {
            @Override
            public void confirm(int position) {
                weekTimesCountDataResponse = datas.get(position);
                if (weekTimesCountDataResponse != null) {//选择时间段以后将提交按钮可用，并记录选中的数据
                    commit.setBackgroundResource(R.drawable.new_oval_button_commit_orange);
                    commit.setEnabled(true);
                    commit.setTextColor(getResources().getColor(R.color.white));
                }

                //将对应的FormView进行从新绘制，将选择的时间段显示出来
                FormView formview = formViewHashMap.get(viewPager.getCurrentItem());

                formview.setIndexSelected(index);
                formview.setTimesCountDataResponse(datas.get(position));
            }
        });
    }
    @Override
    protected void onBack() {
        super.onBack();
        setResult(Constant.DOCTOR_MAIN_PAGE);
    }
}
