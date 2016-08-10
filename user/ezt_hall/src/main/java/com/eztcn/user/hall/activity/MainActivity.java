package com.eztcn.user.hall.activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.fragment.FoundFragment;
import com.eztcn.user.hall.fragment.HomeFragment;
import com.eztcn.user.hall.fragment.MessageFragment;
import com.eztcn.user.hall.fragment.PersonalFragment;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.Request;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.model.TabEntity;
import com.eztcn.user.hall.utils.Constant;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class MainActivity extends BaseActivity implements ResultSubscriber.OnResultListener ,OnTabSelectListener{
    private static final String TAG = "MainActivity";
    private static final int CODE = 1;

    private HomeFragment mHomeFragment;
    private MessageFragment mMessageFragment;
    private FoundFragment mFoundFragment;
    private PersonalFragment mPersonalFragment;

    private String[] mBottomTitles = {"门诊大厅", "消息", "发现", "我"};
    private int[] mIconUnselectIds = {
            R.drawable.new_icon_home_unselect, R.drawable.new_icon_message_unselect,
            R.drawable.new_icon_find_unselect, R.drawable.new_icon_mine_unselect};
    private int[] mIconSelectIds = {
            R.drawable.new_icon_home_select, R.drawable.new_icon_message_select,
            R.drawable.new_icon_find_select, R.drawable.new_icon_mine_select};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private CommonTabLayout mBottomBarCtl;
    private final int MY_ATTENTION_NUM=1;
    private final int LOGIN_COMPLETE=4;

    @Override
    protected int preView() {
        return R.layout.new_activity_main;
    }

    @Override
    protected void initView() {
        mBottomBarCtl = (CommonTabLayout) findViewById(R.id.new_main_activity_ctl);
        mBottomBarCtl.setOnTabSelectListener(this);
    }
    @Override
    protected void initData() {
        initFragment();
        for (int i = 0; i < mBottomTitles.length; i++) {
            mTabEntities.add(new TabEntity(mBottomTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mBottomBarCtl.setTabData(mTabEntities,this,R.id.new_main_activity_fl,mFragments);
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        mHomeFragment = HomeFragment.getInstance();
        mMessageFragment = MessageFragment.getInstance();
        mFoundFragment = FoundFragment.getInstance();
        mPersonalFragment = PersonalFragment.getInstance();
        mFragments.add(mHomeFragment);
        mFragments.add(mMessageFragment);
        mFragments.add(mFoundFragment);
        mFragments.add(mPersonalFragment);
    }

    /**
     * 网络请求前调用，通常显示Progressialog
     *
     * @param requestType
     */
    @Override
    public void onStart(int requestType) {
        Log.i(TAG, "onStart");
       // showProgressDialog("");
    }

    /**
     * 网络请求完成调用，通常销毁Progressialog
     *
     * @param requestType
     */
    @Override
    public void onCompleted(int requestType) {
        Log.i(TAG, "onCompleted");
        dismissProgressDialog();
    }

    /**
     * 网络请求错误后调用
     *
     * @param requestType
     */
    @Override
    public void onError(int requestType) {
        Log.i(TAG, "onError");

        dismissProgressDialog();
    }

    /**
     * onNext 方法中处理请求下来的数据
     *
     * @param iModel
     * @param requestType
     */
    @Override
    public void onNext(IModel iModel, int requestType) {
        Log.i(TAG, "onNext" + requestType);
        if (requestType == CODE) {
//            mTextView.setText(((WeatherResponse) iModel).getWeatherinfo().toString());
        }
       switch (requestType){
           //我的关注。
           case MY_ATTENTION_NUM:
               Response response=(Response) iModel;
               Double num= (Double) response.getData();
               int attention_num=num.intValue();
               mPersonalFragment.refreshMyAttentionNum(attention_num);
               break;
       }
    }
    @Override
    public void onTabSelect(int position) {

        if(position==3){
            refreshMyAttention();
            mPersonalFragment.refreshPersonData();
        }else  if(position==1){
            refreshMessage();
        }
    }
    /**
     * 刷新我的消息fragment
     */
    private void refreshMessage() {
        mMessageFragment.RefreshData();
    }

    /**
     * 刷新我的关注数量。
     */
    private void refreshMyAttention() {

        // 请求我的关注数量。
        getMyAttentionNum();
    }
    /**
     * 请求我的关注数量
     */
    private void getMyAttentionNum() {

        if(BaseApplication.patient!=null){
            Map<String,String> params=new HashMap<>();
            params.put("userId",BaseApplication.patient.getUserId()+"");
            Request request=new Request();
            Map<String,String> map=request.getFormMap(params);
            HTTPHelper helper= HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC);
            helper.postMyDoctorNum(map,MY_ATTENTION_NUM,this);
        }
    }
    @Override
    public void onTabReselect(int position) {

    }
    @Override
    protected void onBack() {
//        dismissProgressDialog();
//        super.onBack();
        //回到手机home页
        Intent MyIntent = new Intent(Intent.ACTION_MAIN);
        MyIntent.addCategory(Intent.CATEGORY_HOME);
        startActivity(MyIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if(requestCode== Constant.REFRESH_MY_ATTENTION_NUM){
           refreshMyAttention();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPersonalFragment.refreshPersonData();
    }
}
