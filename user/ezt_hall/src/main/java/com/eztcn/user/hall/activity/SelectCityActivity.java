package com.eztcn.user.hall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.Request;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.model.ResultResponse.AreaListDataResponse;
import com.eztcn.user.hall.model.ResultResponse.CityListDataResponse;
import com.eztcn.user.hall.utils.LogUtils;
import com.eztcn.user.hall.utils.SaveGetDataUtil;
import com.eztcn.user.hall.utils.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;


/**
 * 预约挂号里面的选择城市页面
 *
 * @author 蒙
 */
public class SelectCityActivity extends BaseActivity implements ResultSubscriber.OnResultListener {
    private List<AreaListDataResponse> areaListDatas = new ArrayList<AreaListDataResponse>();//数据源
    private ListView leftListView;//左边的listview
    private ListView rightListView;//右边的listview
    private LeftAdapter leftAdapter = new LeftAdapter();//左边listview的适配器
    private RightAdapter rightAdapter = new RightAdapter();//右边listview的适配器
    private int indexListView = 0;//左边ListView的选中的下标，默认选中第一项

    private ArrayList<CityListDataResponse> cityListMap;
    private CityListDataResponse cityData;

    private TextView current_city_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTitleBar(true, "选择城市", null);
    }

    @Override
    protected int preView() {
        return R.layout.new_activity_selectcity;
    }

    /**
     * 初始化页面的控件
     */
    public void initView() {
        leftListView = (ListView) findViewById(R.id.new_activity_selectCity_left_listView);

        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                indexListView = position;//更新选中的listview的下标
                leftAdapter.notifyDataSetChanged();//让适配器更新，是为了让选中的条目的背景置为灰色
                if (areaListDatas == null) {
                    areaListDatas = new ArrayList<AreaListDataResponse>();//数据源
                }
                areaListDatas.clear();
                rightAdapter.notifyDataSetChanged();//让适配器进行刷新，因为做点不同条目的选中导致了数据源的变化
                getAreaListData(indexListView);
            }
        });
        rightListView = (ListView) findViewById(R.id.new_activity_selectCity_right_listView);

        rightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //强选择的城市id和地区id返回
                Intent intent = new Intent();

                if (position==0){//全部地区
                    intent.putExtra("areaId","");
                    intent.putExtra("areaName",areaListDatas.get(position).getCountyname() );
                }else{//具体的某个地区
                    intent.putExtra("areaId",areaListDatas.get(position).getId() );
                    intent.putExtra("areaName",areaListDatas.get(position).getCountyname() );
                }

                intent.putExtra("cityId",cityListMap.get(indexListView).getId() );
                intent.putExtra("cityName",cityListMap.get(indexListView).getName() );
                setResult(222, intent);
                finish();
            }
        });

        current_city_name = (TextView) findViewById(R.id.new_activity_selectCity_currentCity);
    }

    @Override
    protected void initData() {
        cityData = (CityListDataResponse) getIntent().getSerializableExtra("cityData");
        current_city_name.setText(cityData.getName());

        cityListMap = SaveGetDataUtil.getCityListData();
        leftListView.setAdapter(leftAdapter);
        rightListView.setAdapter(rightAdapter);


        getAreaListData(0);//获取默认的第一个城市的区域列表
    }

    /**
     * 获取有合作的城市列表
     */
    private void getAreaListData(int index) {
        showProgressDialog("加载中。。。");
        Request request = new Request();
        HashMap<String, String> parmas = new HashMap<String, String>();
        parmas.put("cityid", cityListMap.get(index).getId());
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC).getAreaListData(request.getFormMap(parmas), 111, this);
    }

    public class LeftAdapter extends BaseAdapter {


        public int getCount() {
            if(cityListMap!=null){
                return cityListMap.size();
            }else {
                return 0;
            }
        }

        public Object getItem(int position) {
            return cityListMap.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            final String mContent = cityListMap.get(position).getName();
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.new_select_city_list_item, null);
                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (indexListView == position) {//将选中的条目置为不同的颜色
                convertView.setBackgroundResource(R.color.background);
            } else {
                convertView.setBackgroundResource(R.color.white);
            }
            viewHolder.name.setText(mContent);

            return convertView;
        }

        class ViewHolder {
            TextView name;
        }


    }

    public class RightAdapter extends BaseAdapter {


        public int getCount() {
            return areaListDatas.size();
        }

        public Object getItem(int position) {
            return areaListDatas.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            final String mContent = areaListDatas.get(position).getCountyname();
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.new_select_city_list_item, null);
                convertView.setBackgroundResource(R.color.background);
                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                viewHolder.name.setTextColor(mContext.getResources().getColor(R.color.text_color_gray));
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.name.setText(mContent);

            return convertView;
        }

        class ViewHolder {
            TextView name;
        }
    }

    @Override
    public void onNext(IModel t, int requestType) {
        dismissProgressDialog();
        Response response = (Response) t;
        if (response.isFlag()) {
            if (requestType == 111) {
                areaListDatas = (List<AreaListDataResponse>) response.getData();
                AreaListDataResponse dataResponse=new AreaListDataResponse();
                dataResponse.setCountyname("全部");
                areaListDatas.add(0,dataResponse);
                rightAdapter.notifyDataSetChanged();
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
