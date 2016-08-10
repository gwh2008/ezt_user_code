package com.eztcn.user.hall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.customView.PullToRefreshListView;
import com.eztcn.user.hall.common.Constant;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.Request;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.model.ResultResponse.CityListDataResponse;
import com.eztcn.user.hall.model.ResultResponse.HospitalListResponse;
import com.eztcn.user.hall.utils.LogUtils;
import com.eztcn.user.hall.utils.ToastUtils;
import com.eztcn.user.hall.utils.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 预约挂号首页面
 *
 * @author 蒙
 */
public class AppointmentMainActivity extends BaseActivity implements View.OnClickListener,
        ResultSubscriber.OnResultListener, PullToRefreshListView.OnRefreshListener, PullToRefreshListView.OnLoadMoreListener {
    private PullToRefreshListView listView;
    ArrayList<HospitalListResponse> datas = new ArrayList<>();//搜索到的数据源
    private ImageView back;//返回按钮
    private ImageView search_pic;//搜索按钮
    private TextView location;//城市按钮
    private TextView select;//筛选按钮
    private Adapter adapter = new Adapter();

    private CityListDataResponse cityData;//当前城市数据

    private String numType = "";//号源类型
    private String level = "";//医院级别
    private String areaId = "";//地区id
    private String cityId = "";//城市id

    private ImageView search_hospital_failure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int preView() {
        return R.layout.new_activity_appointment_main;
    }

    public void initView() {
        listView = (PullToRefreshListView) findViewById(R.id.new_activity_appointment_main_listView);
        back = (ImageView) findViewById(R.id.new_activity_appointment_main_back);
        back.setOnClickListener(this);
        search_pic = (ImageView) findViewById(R.id.new_activity_appointment_main_search_pic);
        location = (TextView) findViewById(R.id.new_activity_appointment_main_location);
        select = (TextView) findViewById(R.id.new_activity_appointment_main_select);
        location.setOnClickListener(this);
        select.setOnClickListener(this);
        search_pic.setOnClickListener(this);
        search_hospital_failure = (ImageView) findViewById(R.id.search_hospital_failure);
    }

    @Override
    protected void initData() {

        cityData = (CityListDataResponse) getIntent().getSerializableExtra("cityData");
        location.setText(cityData.getName());
        listView.setAdapter(adapter);
        listView.setCanLoadMore(true);
        listView.setCanRefresh(true);
        listView.setAutoLoadMore(true);
        listView.setMoveToFirstItemAfterRefresh(true);
        listView.setDoRefreshOnUIChanged(false);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("1".equals(datas.get(position - 1).getEhIsneedPatientCard())) {//对天津市肿瘤医院的标示进行赋值
                    Constant.IS_SPECIAL_HOSPITAL = true;
                } else {
                    Constant.IS_SPECIAL_HOSPITAL = false;
                }
                startActivity(new Intent(mContext, SelectDepartmentActivity.class)
                        .putExtra("hospital", datas.get(position - 1)));
            }
        });
        cityId = cityData.getId();
        getHospitalData(cityId, "", "", "");

        listView.setEmptyView(search_hospital_failure);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.new_activity_appointment_main_back://返回上个页面
                finish();
                break;
            case R.id.new_activity_appointment_main_search_pic://跳到搜索页面
                startActivity(new Intent(mContext, SearchActivity.class));
                break;
            case R.id.new_activity_appointment_main_location://跳到选择城市页面
                intent = new Intent(mContext, SelectCityActivity.class);
                intent.putExtra("cityData", cityData);
                startActivityForResult(intent, 222);
                break;
            case R.id.new_activity_appointment_main_select://跳到筛选页面
                intent = new Intent(mContext, SelectHospitalRuleActivity.class);
                startActivityForResult(intent, 111);
                break;
        }
    }

    public class Adapter extends BaseAdapter {


        public int getCount() {
            return datas.size();
        }

        public Object getItem(int position) {
            return datas.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.new_activity_appointment_main_list_item, null);
                viewHolder.name = (TextView) convertView.findViewById(R.id.new_activity_appointment_main_list_item_name);
                viewHolder.remain = (TextView) convertView.findViewById(R.id.new_activity_appointment_main_list_item_remain);
                viewHolder.all_count = (TextView) convertView.findViewById(R.id.new_activity_appointment_main_list_item_all_count);
                viewHolder.level = (ImageView) convertView.findViewById(R.id.new_activity_appointment_main_list_item_sanjia);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.name.setText(datas.get(position).getEhName());
            viewHolder.remain.setText(datas.get(position).getRpMainNum());
            viewHolder.all_count.setText(datas.get(position).getRpNum());
            if (datas.get(position).getEd_value_showCn().contains("三")) {
                viewHolder.level.setVisibility(View.VISIBLE);
            } else {
                viewHolder.level.setVisibility(View.GONE);
            }

            return convertView;
        }

        class ViewHolder {
            TextView name;//医院的名字
            TextView remain;//剩余放号量
            TextView all_count;//全部放号量
            ImageView level;//医院的等级

        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount();
        }
    }

    /**
     * 获取医院列表数据
     *
     * @param cityid   城市id
     * @param countyid 区域id
     * @param level    医院级别
     * @param numType  号源类别
     */
    public void getHospitalData(String cityid, String countyid, String level, String numType) {
        page_index = 1;
        showProgressDialog("正在加载医院...");
        Request request = new Request();
        Map<String, String> params = new HashMap<>();
        params.put("cityid", cityid);//城市id
        params.put("countyid", countyid);//区域id
        params.put("level", level);//医院级别
        params.put("numType", numType);//号源类别
        params.put("hospitalName", "");//医院名称

        params.put("pageNum", page_index + "");//页数
        params.put("pageSize", "10");//每页个数
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC).getHospitalSearchData(request.getFormMap(params), 111, this);

    }

    /**
     * 获取医院列表数据,下拉刷新时调用,没有等待弹出框
     */
    public void getHospitalData() {
        Request request = new Request();
        Map<String, String> params = new HashMap<>();
        params.put("cityid", cityId);//城市id
        params.put("countyid", areaId);//区域id
        params.put("level", level);//医院级别
        params.put("numType", numType);//号源类别
        params.put("hospitalName", "");//医院名称

        params.put("pageNum", page_index + "");//页数
        params.put("pageSize", "10");//每页个数
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC).getHospitalSearchData(request.getFormMap(params), 111, this);

    }

    /**
     * 下拉刷新。
     */
    @Override
    public void onRefresh() {
        page_index = 1;
        getHospitalData();
    }

    private int page_index = 1;

    /**
     * 上拉加载更多
     */
    @Override
    public void onLoadMore() {
        page_index++;
        getHospitalData();
    }

    @Override
    public void onNext(IModel t, int requestType) {
        dismissProgressDialog();
        Response response = (Response) t;
        if (response.isFlag()) {
            if (requestType == 111) {
                ArrayList<HospitalListResponse> listResponses = (ArrayList<HospitalListResponse>) response.getData();
                if (listResponses == null) {
                    listResponses = new ArrayList<>();
                }

                if (listResponses.size() < 10) {
                    listView.setAutoLoadMore(false);
                } else {
                    listView.setAutoLoadMore(true);
                }
                if (page_index == 1) {
                    datas.clear();
                    datas.addAll(listResponses);
                } else {
                    datas.addAll(listResponses);
                }

                /**
                 *南院区:中医一附属国医堂现场号                              310
                 *北院区：天津市中医药大学第一附属医院国医堂现场号              314
                 *中心妇产科医院现场预约                                      6
                 *天津市口腔医院儿科普号                                      116
                 *天津医科大学代谢病医院（病床）                              291
                 *天津市泰达国际心血管病医院病床                              292
                 */
                for (int i = datas.size() - 1; i >= 0; i--) {//屏蔽指定id的医院不显示
                    String id = datas.get(i).getId();
                    if ("310".equals(id) || "314".equals(id) || "6".equals(id) || "116".equals(id)
                            || "291".equals(id) || "292".equals(id)) {

                        datas.remove(i);
                    }
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
        if (page_index == 1) {
            listView.onRefreshComplete();
        } else {
            listView.onLoadMoreComplete();
        }

    }

    @Override
    public void onError(int requestType) {
        dismissProgressDialog();
        if (page_index == 1) {
            listView.onRefreshComplete();
        } else {
            listView.onLoadMoreComplete();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == 111) {//筛选页面返回
            numType = data.getStringExtra("numType");
            level = data.getStringExtra("level");
            getHospitalData(cityId, areaId, level, numType);
        } else if (resultCode == 222) {//选择城市页面返回
            areaId = data.getStringExtra("areaId");
            cityId = data.getStringExtra("cityId");
            location.setText(data.getStringExtra("cityName"));
            getHospitalData(cityId, areaId, level, numType);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}
