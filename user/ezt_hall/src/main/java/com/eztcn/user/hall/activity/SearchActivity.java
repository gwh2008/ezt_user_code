package com.eztcn.user.hall.activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
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
import com.eztcn.user.hall.model.ResultResponse.HospitalListResponse;
import com.eztcn.user.hall.model.ResultResponse.SelectRulesResponse;
import com.eztcn.user.hall.utils.LogUtils;
import com.eztcn.user.hall.utils.SaveGetDataUtil;
import com.eztcn.user.hall.utils.ToastUtils;
import com.eztcn.user.hall.utils.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * 搜索页面
 *
 * @author 蒙
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener, ResultSubscriber.OnResultListener
        , PullToRefreshListView.OnRefreshListener,PullToRefreshListView.OnLoadMoreListener{
    private PullToRefreshListView listView;
    ArrayList<HospitalListResponse> historyDatas = new ArrayList<>();//历史数据源
    ArrayList<HospitalListResponse> searchDatas = new ArrayList<>();//搜索到的数据源
    private String searchWords="";//搜索的关键字
    private EditText editText;
    private ImageView clear;//清空输入框按钮
    private TextView cancel ;//返回上个页面
    private MyAdapter adapter=new MyAdapter();
    private  boolean isFirstIn=true;
    private ImageView search_hospital_failure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int preView() {
        return R.layout.new_activity_search;
    }

    public void initView() {
        listView = (PullToRefreshListView) findViewById(R.id.new_activity_search_listView);


        editText = (EditText) findViewById(R.id.new_activity_search_edit);
        clear = (ImageView) findViewById(R.id.new_activity_search_clear);
        cancel=(TextView) findViewById(R.id.new_activity_search_cancel);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchWords=s.toString();//获取到搜索的关键字

                //根据是否有内容来让清空按钮是否显示或者消失
                if (TextUtils.isEmpty(searchWords)) {
                    cancel.setVisibility(View.INVISIBLE);
                } else {
                    cancel.setVisibility(View.VISIBLE);
                }
            }
        });

        //键盘上班的确认按钮，进行关键字的搜索
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    isFirstIn=false;//搜索过后将第一次进入页面的标志置为false

                    //搜索之后让刷新和加载更多可用
                    listView.setCanLoadMore(true);
                    listView.setCanRefresh(true);
                    listView.setEmptyView(search_hospital_failure);

                    Tools.forceHidenSoftKeyboad(mContext,SearchActivity.this);
                    getHospitalSearchData(searchWords);
                }
                return false;
            }
        });

        //清空输入框里面的内容
        clear.setOnClickListener(new View.OnClickListener() {//点击清空按钮使内容清空

            @Override
            public void onClick(View v) {
                editText.setText("");
                searchDatas.clear();
                adapter.notifyDataSetChanged();
            }
        });

        //返回到上一个页面
        cancel.setOnClickListener(new View.OnClickListener() {//点击清空按钮使内容清空

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        search_hospital_failure=(ImageView)findViewById(R.id.search_hospital_failure);
    }
    @Override
    protected void initData() {
//        historyDatas= SaveGetDataUtil.getHospitalHistoryListData();//获取历史搜索数据
//        if (historyDatas==null||historyDatas.size()==0){
//            historyDatas=new  ArrayList<>();
//            historyDatas.add(new HospitalListResponse());
//            SaveGetDataUtil.setHospitalHistoryListData(historyDatas);
//        }
//        listView.setAdapter(adapter);

        listView.setAutoLoadMore(true);
        listView.setMoveToFirstItemAfterRefresh(true);
        listView.setDoRefreshOnUIChanged(false);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (isFirstIn){
                    if ("1".equals(historyDatas.get(position-1).getEhIsneedPatientCard())){//对天津市肿瘤医院的标示进行赋值
                        Constant.IS_SPECIAL_HOSPITAL=true;
                    }else{
                        Constant.IS_SPECIAL_HOSPITAL=false;
                    }

                    startActivity(new Intent(mContext,SelectDepartmentActivity.class)
                            .putExtra("hospital",historyDatas.get(position-1)));
                }else{
                    if ("1".equals(searchDatas.get(position-1).getEhIsneedPatientCard())){//对天津市肿瘤医院的标示进行赋值
                        Constant.IS_SPECIAL_HOSPITAL=true;
                    }else{
                        Constant.IS_SPECIAL_HOSPITAL=false;
                    }
                    startActivity(new Intent(mContext,SelectDepartmentActivity.class)
                            .putExtra("hospital",searchDatas.get(position-1)));
                }

                saveHistoryData(position-1);

            }
        });

    }

    /**
     * 下拉刷新。
     */
    @Override
    public void onRefresh() {
        page_index=1;
        getHospitalSearchData();
    }

    private int page_index=1;
    /**
     * 上拉加载更多
     */
    @Override
    public void onLoadMore() {
        page_index++;
        getHospitalSearchData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirstIn){
            return;
        }
        historyDatas= SaveGetDataUtil.getHospitalHistoryListData();//获取历史搜索数据
        if (historyDatas==null||historyDatas.size()==0){
            historyDatas=new  ArrayList<>();
            historyDatas.add(new HospitalListResponse());
            SaveGetDataUtil.setHospitalHistoryListData(historyDatas);
        }
        listView.setAdapter(adapter);
    }

    /**
     * 将点击的数据保存到历史数据里面
     * @param position
     */
    public void saveHistoryData(int position){
        //将点击的数据保存到历史数据里面
        HospitalListResponse hospitalListResponse=(HospitalListResponse)adapter.getItem(position);
        historyDatas.add(0,hospitalListResponse);
        if (historyDatas.size()==7){//控制历史数据最多有5条，加上最后的删除本地记录那一条为6条
            historyDatas.remove(6);
        }
        SaveGetDataUtil.setHospitalHistoryListData(historyDatas);
    }

    /**
     * 获取搜索的数据
     */
    public void getHospitalSearchData(String searchWords){
        showProgressDialog("正在搜索...");
        Request request=new Request();
        Map<String, String> params = new HashMap<>();
        params.put("hospitalName",searchWords);//搜索的关键字
        params.put("pageNum",page_index+"");//页数
        params.put("pageSize","10");//每页个数
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC).getHospitalSearchData(request.getFormMap(params), 111, this);

    }
    /**
     * 刷新和加载
     */
    public void getHospitalSearchData(){
        Request request=new Request();
        Map<String, String> params = new HashMap<>();
        params.put("hospitalName",searchWords);//搜索的关键字
        params.put("pageNum",page_index+"");//页数
        params.put("pageSize","10");//每页个数
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC).getHospitalSearchData(request.getFormMap(params), 111, this);

    }

    @Override
    public void onClick(View v) {
    }
    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {//根据是否是第一次进入页面，来决定是否加载历史数据或者搜索到的数据

            //当历史数据只有一条时，其实是最后的删除本地记录那一条数据，所以返回0，是没有历史数据的
            return isFirstIn?(historyDatas.size()==1?0:historyDatas.size()):searchDatas.size();
        }

        @Override
        public Object getItem(int position) {//根据是否是第一次进入页面，来决定是否加载历史数据或者搜索到的数据
            return isFirstIn?historyDatas.get(position):searchDatas.get(position);
        }

        @Override
        public long getItemId(int position) {//根据是否是第一次进入页面，来决定是否加载历史数据或者搜索到的数据
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (isFirstIn&&(position==historyDatas.size()-1)){//加载历史数据时最后一条的显示

                convertView = getLayoutInflater().inflate(R.layout.new_delete_histroy, null);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        historyDatas.clear();
                        adapter.notifyDataSetChanged();
                        SaveGetDataUtil.setHospitalHistoryListData(historyDatas);
                        isFirstIn=false;
                    }
                });
                return convertView;
            }


            ViewHolder holder = null;
            if (convertView == null||convertView.getTag()==null) {
                convertView = getLayoutInflater().inflate(R.layout.new_search_list_item, null);
                holder = new ViewHolder();
                holder.name = (TextView) convertView .findViewById(R.id.name);
                holder.delete = (ImageView) convertView .findViewById(R.id.delete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.name.setText(isFirstIn?historyDatas.get(position).getEhName():searchDatas.get(position).getEhName());
            if (isFirstIn) {//如果是第一次进页面加载历史数据时就将删除按钮显示
                holder.delete.setVisibility(View.VISIBLE);
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //删除本条记录
                        historyDatas.remove(position);
                        adapter.notifyDataSetChanged();
                        SaveGetDataUtil.setHospitalHistoryListData(historyDatas);
                    }
                });
            }else{
                holder.delete.setVisibility(View.GONE);
            }


            return convertView;
        }

        class ViewHolder {
            private TextView name;
            private ImageView delete;
        }
    }

    @Override
    public void onNext(IModel t, int requestType) {
        dismissProgressDialog();
        Response response = (Response) t;
        if (response.isFlag()) {
            if (requestType == 111) {
                ArrayList<HospitalListResponse> listResponses = (ArrayList<HospitalListResponse>) response.getData();
                if (listResponses==null){
                    listResponses=new ArrayList<>();
                }


                /**
                 * 南院区:中医一附属国医堂现场号                              310
                 *北院区：天津市中医药大学第一附属医院国医堂现场号              314
                 *中心妇产科医院现场预约                                      6
                 *天津市口腔医院儿科普号                                      116
                 *天津医科大学代谢病医院（病床）                              291
                 *天津市泰达国际心血管病医院病床                              292
                 */
                for (int i = listResponses.size() - 1; i >= 0; i--) {//屏蔽指定id的医院不显示
                    String id=listResponses.get(i).getId();
                    if ("310".equals(id)||"314".equals(id)||"6".equals(id)||"116".equals(id)
                            ||"291".equals(id)||"292".equals(id)){

                        listResponses.remove(i);
                    }
                }

                if (listResponses.size()<10){
                    listView.setAutoLoadMore(false);
                }else{
                    listView.setAutoLoadMore(true);
                }
                if (page_index==1){
                    searchDatas.clear();
                    searchDatas.addAll(listResponses);
                }else{
                    searchDatas.addAll(listResponses);
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
        dismissProgressDialog();
        if (page_index==1){
            listView.onRefreshComplete();
        }else{
            listView.onLoadMoreComplete();
        }

    }

    @Override
    public void onError(int requestType) {
        dismissProgressDialog();
        if (page_index==1){
            listView.onRefreshComplete();
        }else{
            listView.onLoadMoreComplete();
        }
    }

}
