package com.eztcn.user.hall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.hall.adapter.CitySortAdapter;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.model.City;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.Request;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.model.ResultResponse.AllCityListDataResponse;
import com.eztcn.user.hall.utils.CharacterParser;
import com.eztcn.user.hall.utils.PinyinComparatorCity;
import com.eztcn.user.hall.utils.SaveGetDataUtil;
import com.eztcn.user.hall.utils.ToastUtils;
import com.eztcn.user.hall.views.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


/**
 * 预约挂号里面的选择城市页面
 *
 * @author 蒙
 */
public class SelectAllCityActivity extends BaseActivity implements ResultSubscriber.OnResultListener {

    private ListView sortListView;// 分类城市列表
    private SideBar sideBar;// 右边选择栏
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private ArrayList<City> cityList;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparatorCity pinyinComparatorCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTitleBar(true, "选择城市", null);
    }

    @Override
    protected int preView() {
        return R.layout.new_activity_select_all_city;
    }

    private String[] b = {"热", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};

    /**
     * 初始化页面的控件
     */
    public void initView() {
        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        sideBar.setIndexData(b);
        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                if (s.equals("热")) {//如果点击是热，就位置显示到0
                    sortListView.setSelection(0);
                } else {//其他的字母索引点击
                    // 该字母首次出现的位置
                    for (int i = 0; i < alldata.size(); i++) {
                        if (s.equals(alldata.get(i).getSortLetters())) {
                            sortListView.setSelection(i);
                            break;
                        }
                    }
                }
            }
        });
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cityname = ((City) adapter.getItem(position)).getCityname();
                Intent intent = new Intent();
                intent.putExtra("name", cityname);
                setResult(222, intent);
                finish();
            }
        });
    }

    private CitySortAdapter adapter;
    ArrayList<City> alldata = new ArrayList<>();


    @Override
    protected void initData() {
        characterParser = CharacterParser.getInstance();
        pinyinComparatorCity = new PinyinComparatorCity();
        adapter = new CitySortAdapter(this);//全部城市列表需要的适配器
        sortListView.setAdapter(adapter);
        AllCityListDataResponse dataResponse = SaveGetDataUtil.getAllCityListData();
        if (dataResponse == null) {//本地缓存是否有
            getAllCityListData();
        } else {
            ArrayList<City> all=newCityListsAZ(dataResponse.getAllCity());
            ArrayList<City> hot=newCityListsAddLetter(dataResponse.getHotCity());
            all.addAll(0, hot);
            adapter.updateListView(all);
            alldata.addAll(all);
        }
    }


    /**
     * 获取有合作的城市列表
     */
    private void getAllCityListData() {
        showProgressDialog("城市加载中。。。");
        Request request = new Request();
        HashMap<String, String> parmas = new HashMap<String, String>();
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC).getAllCityListData(request.getFormMap(parmas), 111, this);
    }

    @Override
    public void onNext(IModel t, int requestType) {
        Response response = (Response) t;
        if (response.isFlag()) {
            if (requestType == 111) {
                AllCityListDataResponse dataResponse = (AllCityListDataResponse) response.getData();

                SaveGetDataUtil.setAllCityListData(dataResponse);//将列表数据保存到本地

                if (dataResponse == null) {
                    dataResponse = new AllCityListDataResponse();
                }
                ArrayList<City> all = newCityListsAZ(dataResponse.getAllCity());
                ArrayList<City> hot = newCityListsAddLetter(dataResponse.getHotCity());
                all.addAll(0, hot);//将热门城市加载到全部城市的上面
                adapter.updateListView(all);//更新适配器
                alldata.addAll(all);

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
        dismissProgressDialog();
    }

    @Override
    public void onError(int requestType) {
        dismissProgressDialog();
    }

    /**
     * 排序处理得到的城市列表
     *
     * @param cityList
     */
    private ArrayList<City> newCityListsAZ(ArrayList<City> cityList) {
        for (int i = 0; i < cityList.size(); i++) {
            City sortModel = new City();
            String cityName = cityList.get(i).getCityname();
            sortModel.setCityname(cityName);
            String cityId = cityList.get(i).getId();
            sortModel.setId(cityId);

            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(cityName);
            if (pinyin.length() > 0) {
                String sortString = pinyin.substring(0, 1).toUpperCase();
                // 正则表达式，判断首字母是否是英文字母
                if (sortString.matches("[A-Z]")) {
                    sortModel.setSortLetters(sortString.toUpperCase());
                } else {
                    sortModel.setSortLetters("#");
                }
            } else {
                sortModel.setSortLetters("#");
            }
            cityList.set(i, sortModel);
            // mSortList.add(sortModel);
        }
        // 根据a-z进行排序源数据
        Collections.sort(cityList, pinyinComparatorCity);

        return cityList;

    }

    /**
     * 排序处理得到的城市列表
     *
     * @param cityList
     */
    private ArrayList<City> newCityListsAddLetter(ArrayList<City> cityList) {

        for (int i = 0; i < cityList.size(); i++) {
            City sortModel = new City();
            String cityName = cityList.get(i).getCityname();
            sortModel.setCityname(cityName);
            String cityId = cityList.get(i).getId();
            sortModel.setId(cityId);
            sortModel.setSortLetters("@");
            cityList.set(i, sortModel);
        }
        return cityList;

    }
}
