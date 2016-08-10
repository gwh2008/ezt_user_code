package com.eztcn.user.eztcn.activity;

import java.util.List;

import xutils.ViewUtils;
import xutils.db.sqlite.WhereBuilder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.mine.AutonymAuthActivity;
import com.eztcn.user.eztcn.activity.mine.ChoiceCityActivity;
import com.eztcn.user.eztcn.activity.mine.ModifyPhoneActivity;
import com.eztcn.user.eztcn.activity.mine.UserLoginActivity;
import com.eztcn.user.eztcn.bean.City;
import com.eztcn.user.eztcn.broadcast.NetworkBroadcast;
import com.eztcn.user.eztcn.broadcast.PhoneBootReceiver;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.CustomProgressToast;
import com.eztcn.user.eztcn.db.EztDb;
import com.eztcn.user.eztcn.fragment.HomeFragment30;
import com.eztcn.user.eztcn.utils.AppManager;
import com.eztcn.user.eztcn.utils.MsgUtil;
import com.eztcn.user.eztcn.utils.SDCardUtil;
import com.eztcn.user.eztcn.utils.js.WVJBWebViewClient;
import com.eztcn.user.hall.activity.loginSetting.CompletePersonalInfoActivity;
import com.eztcn.user.hall.activity.loginSetting.LoginActivity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * @author ezt
 * @title Activity 基类
 * @describe
 * @created 2014年11月10日
 */

public class FinalActivity extends FragmentActivity {

    public static NetworkBroadcast netReceiver = null;
    public PhoneBootReceiver phoneReceiver = null;

    private CustomProgressToast progressToast = null;
    private static FinalActivity instance = null;
    private AppManager appManager;
    public static Activity mContext;//初始化应该放到oncreate方法里面，如果放到onresume里面的话会出现赋值不及时的情况发生

    protected boolean isShowNotification = true;// 是否启用后台常驻机制

    private final UMSocialService mController = UMServiceFactory
            .getUMSocialService(EZTConfig.DESCRIPTOR);// 友盟分享

    /*****
     * 百度地图定位
     **********/
    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    private WebView webView;

    protected void configWebView(WebView webView,
                                 WVJBWebViewClient webViewClient, String htmUrl) {
//		http://www.cnblogs.com/zgz345/p/3768174.html
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(htmUrl);
        webViewClient.enableLogging();
        webView.setWebViewClient(webViewClient);
        this.webView = webView;
    }

    protected void toast(String msg, int time) {
        Toast.makeText(mContext, msg, time).show();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mContext.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//        ViewUtils.inject(this);
        instance = this;

        // 把当前activity加入栈中
        appManager = AppManager.getAppManager();
        appManager.addActivity(this);

        // 2016-1-25 百度用户偏好设置 start
        StatService.setSessionTimeOut(30);
        /*
		 * 设置启动时日志发送延时的秒数<br/> 单位为秒，大小为0s到30s之间<br/>
		 * 注：请在StatService.setSendLogStrategy之前调用，否则设置不起作用
		 * 
		 * 如果设置的是发送策略是启动时发送，那么这个参数就会在发送前检查您设置的这个参数，表示延迟多少S发送。<br/>
		 * 这个参数的设置暂时只支持代码加入， 在您的首个启动的Activity中的onCreate函数中使用就可以。<br/>
		 */
        StatService.setLogSenderDelayed(0);
		/*
		 * 用于设置日志发送策略<br /> 嵌入位置：Activity的onCreate()函数中 <br />
		 * 
		 * 调用方式：StatService.setSendLogStrategy(this,SendStrategyEnum.
		 * SET_TIME_INTERVAL, 1, false); 第二个参数可选： SendStrategyEnum.APP_START
		 * SendStrategyEnum.ONCE_A_DAY SendStrategyEnum.SET_TIME_INTERVAL 第三个参数：
		 * 这个参数在第二个参数选择SendStrategyEnum.SET_TIME_INTERVAL时生效、
		 * 取值。为1-24之间的整数,即1<=rtime_interval<=24，以小时为单位 第四个参数：
		 * 表示是否仅支持wifi下日志发送，若为true，表示仅在wifi环境下发送日志；若为false，表示可以在任何联网环境下发送日志
		 */
        StatService.setSendLogStrategy(this,
                SendStrategyEnum.SET_TIME_INTERVAL, 1, false);
        // 调试百度统计SDK的Log开关，可以在Eclipse中看到sdk打印的日志，发布时去除调用，或者设置为false
        StatService.setDebugOn(true);
        // 设置app发布渠道
        StatService.setAppChannel(this, "Baidu Market", true);
        // 2016-1-25 百度用户偏好设置 end

        // 定位-获取当前经纬度
        mLocationClient = new LocationClient(getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setLocationMode(LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(mMyLocationListener);
        mLocationClient.requestLocation();
        mLocationClient.start();

        // 初始化友盟分享
        configPlatforms();

    }

    public static FinalActivity getInstance() {
        return instance;
    }

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
    }

    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 2016-1-25 百度用户偏好统计 应当是当前 Activity引用，并非全局Application context
		StatService.onResume(mContext);
        // 监听网络广播
        if (netReceiver == null)
            netReceiver = new NetworkBroadcast();
        registerReceiver(netReceiver, new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION));
        // 监听电话广播
        // if (phoneReceiver == null)
        // phoneReceiver = new PhoneBootReceiver();
        // registerReceiver(phoneReceiver, new IntentFilter(
        // ConnectivityManager.CONNECTIVITY_ACTION));

		/*
		 * mAMapLocManager.setGpsEnable(false);//
		 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
		 * API定位采用GPS和网络混合定位方式
		 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
		 */
        if (mLocationClient != null
                && TextUtils.isEmpty(BaseApplication.getInstance().city)) {
            startLocation();
        }

        // // 删除安装包
        // File file = new File(UpManager.saveFileName);
        // if (file.exists()) {//存储文件存在
        // PackageManager pkgManager = BaseApplication.getInstance()
        // .getPackageManager();
        // PackageInfo info =
        // pkgManager.getPackageArchiveInfo(UpManager.saveFileName,
        // PackageManager.GET_ACTIVITIES);
        // if (null != info) {
        // try {
        // int versionCode = VersionManager.getVersionCode(this);
        // //当前版本>=存储文件版本
        // if (versionCode >= info.versionCode) {
        // file.delete();//将存储文件版本删除
        // }
        // } catch (Exception e) {
        //
        // }
        //
        // }
        //
        // }

    }

    private boolean jump = false;

    /**
     * 实现实位回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location != null) {
                if (!TextUtils.isEmpty(location.getCity())) {
                    String city = location.getCity().replace("市", "");
                    if (!TextUtils.isEmpty(city)) {
                        BaseApplication.getInstance().city = city;
                    }
                    BaseApplication.getInstance().lon = location.getLongitude();
                    BaseApplication.getInstance().lat = location.getLatitude();


                    if (TextUtils.isEmpty(BaseApplication.selectCity)) {// 当选择城市为空时
                        BaseApplication.selectCity = city;
                        WhereBuilder selector = WhereBuilder.b("cityName", "like", "%" + city + "%");
                        List<City> tempCitys = EztDb.getInstance(instance).queryAll(new City(), selector, null);
                        if (null != tempCitys && tempCitys.size() > 0) {
                            BaseApplication.cCity = tempCitys.get(0);
                        }

                    }
                    if (FinalActivity.this instanceof ChoiceCityActivity) {
                        if (ChoiceCityActivity.tvLtCity != null) {
                            ChoiceCityActivity.tvLtCity.setText(city);
                        }
                    }

                    if (!jump) {
                        // appManager.currentActivity() instanceof MainActivity
                        if (FinalActivity.this instanceof MainActivity) {
                            HomeFragment30 homFragment = (HomeFragment30) ((MainActivity) FinalActivity.this)
                                    .getInstance().getFragments().get(0);
                            if (null != homFragment && homFragment.isVisible()
                                    && !homFragment.isHidden()) {
                                if (!BaseApplication.selectCity
                                        .equals(homFragment.getLocationTV()
                                                .getText().toString().trim()))
                                    homFragment.getLocationTV().setText(
                                            BaseApplication.selectCity);

                            }
                        }
                        jump = true;
                    }

                    stopLocation();// 销毁掉定位
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        hideProgressToast();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // 2016-1-25 百度用户偏好统计
		StatService.onPause(mContext);
        if (netReceiver != null) {
            unregisterReceiver(netReceiver);
        }
        hideProgressToast();
        // if (phoneReceiver != null) {
        // unregisterReceiver(phoneReceiver);
        // }
        // stopLocation();// 停止定位
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        BaseApplication.getInstance().isBackground = MsgUtil
                .isAppOnBackground();
        isShowNotification = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideProgressToast();
        if (isShowNotification) {// 根据设置来决定是否显示通知
            BaseApplication.getInstance().isBackground = MsgUtil
                    .isAppOnBackground();
        } else {
            BaseApplication.getInstance().isBackground = false;// 调用图库等看做不为后台运行
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        appManager.finishActivity(this);// activity关闭，从栈中移除
        hideProgressToast();
    }

    /**
     * 初始化标题栏1(返回Button)
     *
     * @param isShowBackBtn
     * @param titleText
     * @param rightBtText   按钮上的文字
     * @return 右上角按钮，便于添加事件
     */
    public TextView loadTitleBar(boolean isShowBackBtn, String titleText,
                                 String rightBtText) {
        // 左上角返回按钮
        TextView leftBtn = (TextView) findViewById(R.id.left_btn);
        leftBtn.setVisibility(isShowBackBtn ? View.VISIBLE : View.GONE);
        leftBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                hideProgressToast();
            }
        });

        // 标题文字
        TextView titleTv = (TextView) findViewById(R.id.title_tv);
        if (titleTv != null) {
            if (titleText == null)
                titleTv.setVisibility(View.INVISIBLE);
            else if (!titleText.equals("")) {
                titleTv.setVisibility(View.VISIBLE);
                titleTv.setText(titleText);
            }
        }

        // 右上角按钮
        TextView rightBtn = (TextView) findViewById(R.id.right_btn);
        if (rightBtn != null) {
            if (rightBtText == null)
                rightBtn.setVisibility(View.GONE);
            else
                rightBtn.setVisibility(View.VISIBLE);
            rightBtn.setText(rightBtText);
        }

        return rightBtn;
    }

    /**
     * 初始化标题栏2(返回ImgView)
     *
     * @param isShowBackBtn
     * @param titleText
     * @param imgId         图片对应的id(参数为0的时候为隐藏)
     * @return 右上角按钮，便于添加事件
     */
    public ImageView loadTitleBar(boolean isShowBackBtn, String titleText,
                                  int imgId) {
        // 左上角返回按钮
        TextView leftBtn = (TextView) findViewById(R.id.left_btn);
        leftBtn.setVisibility(isShowBackBtn ? View.VISIBLE : View.GONE);
        leftBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                hideProgressToast();
            }
        });

        // 标题文字
        TextView titleTv = (TextView) findViewById(R.id.title_tv);
        if (titleTv != null) {
            if (titleText == null)
                titleTv.setVisibility(View.INVISIBLE);
            else if (!titleText.equals("")) {
                titleTv.setVisibility(View.VISIBLE);
                titleTv.setText(titleText);
            }
        }

        // 右上角按钮
        ImageView rightImgBt = (ImageView) findViewById(R.id.right_btn1);
        if (rightImgBt != null) {
            if (imgId == 0)
                rightImgBt.setVisibility(View.GONE);
            else
                rightImgBt.setVisibility(View.VISIBLE);
            rightImgBt.setImageResource(imgId);
        }

        return rightImgBt;
    }

    /**
     * 调用系统图库
     */
    public void openImgLibrary(int requestCode) {
        isShowNotification = false;
        if (SDCardUtil.isSDCardEnable()) {
            try {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*"); // 设置文件类型
                startActivityForResult(intent, requestCode);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "打开相册失败", Toast.LENGTH_SHORT).show();
            }

        } else
            Toast.makeText(this, "没有sd卡", Toast.LENGTH_LONG).show();
    }

    /**
     * 调用相机
     *
     * @param imgPath
     */
    public void openCamera(int requestCode) {
        isShowNotification = false;
        if (SDCardUtil.isSDCardEnable()) {
            try {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, requestCode);
            } catch (Exception e) {
                Toast.makeText(this, "打开相机失败", Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(this, "没有sd卡", Toast.LENGTH_LONG).show();

    }

    /**
     * 从图库或拍照获取bitmap
     *
     * @param data
     * @return
     */
    protected Bitmap getBitmapFromImage(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            // 取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
            Uri mImageCaptureUri = data.getData();
            // 返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
            if (mImageCaptureUri != null) {
                try {
                    // 这个方法是根据Uri获取Bitmap图片的静态方法
                    bm = Media
                            .getBitmap(getContentResolver(), mImageCaptureUri);
                } catch (Exception e) {
                    bm = null;
                    e.printStackTrace();
                }
            } else {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    // 这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
                    bm = extras.getParcelable("data");
                }
            }
        }
        return bm;
    }

    /**
     * 登录提醒
     */
    public void HintToLogin(final int requestCode) {
        AlertDialog.Builder builder = new Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("提示")
                .setMessage("你还未登录！")
                .setCancelable(false)
                .setNegativeButton("去登录",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                startActivityForResult(new Intent(mContext,LoginActivity.class), requestCode);

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
     * 返回主页
     *
     * @param activity
     * @param selectIndex 切换底部选项卡下标
     */
    public void BackHome(final Activity activity, int selectIndex) {
        Intent intent = new Intent(activity, com.eztcn.user.hall.activity.MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("selectIndex", selectIndex);
        startActivity(intent);
    }

    /**
     * 开始加载进度条
     */
    public void showProgressToast() {
        if (progressToast == null) {
            progressToast = CustomProgressToast.makeText(
                    getApplicationContext(), Integer.MAX_VALUE);
            progressToast.setGravity(Gravity.CENTER, 0, 0);
        }
        try {
            progressToast.show();

        } catch (Exception e) {

        }
    }

    /**
     * 结束加载进度条
     */
    public void hideProgressToast() {
        if (progressToast != null) {
            progressToast.hide();
            progressToast = null;
        }
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public int getWindowWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return (int) (dm.widthPixels);
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public int getWindowHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return (int) (dm.heightPixels);
    }

    /**
     * 显示软键盘
     */

    public void showSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        // InputMethodManager imm = (InputMethodManager)
        // getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

    }

    /**
     * 隐藏软键盘
     *
     * @param view
     */
    public void hideSoftInput(View view) {

        // if (getWindow().getAttributes().softInputMode ==
        // WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
        // 隐藏软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        // }

    }

    /**
     * 弹窗电话提示
     *
     * @param telNum  电话号码
     * @param strHint 提示语
     */
    public void hintTelDialog(final String telNum, String strHint) {
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setIcon(android.R.drawable.ic_dialog_info).setTitle("提示")
                .setMessage(strHint).setCancelable(false)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 用intent启动拨打电话
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri
                                .parse("tel:" + telNum));
                        startActivity(intent);
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
     * 开始定位
     */
    public void startLocation() {

        if (mLocationClient != null) {
            mLocationClient.registerLocationListener(mMyLocationListener);
            mLocationClient.requestLocation();
            mLocationClient.start();
        }
    }

    /**
     * 销毁定位
     */
    private void stopLocation() {
        if (mLocationClient != null) {
            mLocationClient.stop();
            mLocationClient.unRegisterLocationListener(mMyLocationListener);
        }
    }

    /**
     *
     * 监听手机back 返回键
     *
     */
    /**
     * -------------------友盟分享--------------------/**
     */
    // 配置分享平台参数
    private void configPlatforms() {

        // 添加新浪SSO授权
        mController.getConfig().setSsoHandler(new SinaSsoHandler());

        // 添加QQ、QZone平台
        // String appQQId = "100424468";
        // String appKey = "c7394704798a158208a74ab60104f0ba";
        String appQQId = "1104451178";
        String appKey = "NPjWOCi0yaZVwvnN";
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(instance, appQQId,
                appKey);
        qqSsoHandler.setTargetUrl("http://www.eztcn.com");
        // qqSsoHandler.setTargetUrl("http://www.eztcn.com/download/member.html");
        qqSsoHandler.addToSocialSDK();

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(instance,
                appQQId, appKey);
        qZoneSsoHandler.addToSocialSDK();
        // qZoneSsoHandler.setTargetUrl("http://www.eztcn.com/download/member.html");//2015-12-21

        // 添加微信、微信朋友圈平台
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        // String appId = "wx9703d4810c5dfe31";
        // String appSecret = "cecbc414241997f13517e9dc9ceba874";
        String appId = "wx4227d0e43292b36b";
        String appSecret = "aa1a72f7771a845227ef0c3b46e2a8e3";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(instance, appId, appSecret);
        wxHandler.addToSocialSDK();
        // wxHandler.setTargetUrl("http://www.eztcn.com/download/member.html");//2015-12-21

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(instance, appId,
                appSecret);
        // wxCircleHandler.setTargetUrl("http://www.eztcn.com/download/member.html");//2015-12-21

        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

    }

    /**
     * 设置分享内容
     *
     * @param content   内容
     * @param title     标题
     * @param targetUrl 链接地址
     * @param urlLogo   显示图标,网络地址
     * @param urlBitmap 本地图片 （网络地址跟本地图片可选一）
     */
    public void setShareContent(String content, String title, String targetUrl,
                                String urlLogo, Bitmap bitmapLogo) {
        // 图片分享
        UMImage urlImage;
        if (!TextUtils.isEmpty(urlLogo)) {
            urlImage = new UMImage(instance, urlLogo);
        } else {
            urlImage = new UMImage(instance, bitmapLogo);
        }

        // UMImage urlImage = new UMImage(mContext,
        // "http://www.umeng.com/images/pic/social/integrated_3.png");
        // UMEmoji emoji = new UMEmoji(mContext,
        // "http://www.pc6.com/uploadimages/2010214917283624.gif");
        // UMEmoji emoji = new UMEmoji(mContext,
        // "/storage/sdcard0/emoji.gif");

        // 视频分享
        // UMVideo video = new UMVideo(
        // "http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
        // vedio.setThumb("http://www.umeng.com/images/pic/home/social/img-1.png");
        // video.setTitle("友盟社会化组件视频");
        // video.setThumb(urlImage);

        // 音频分享
        // UMusic uMusic = new UMusic(
        // "http://music.huoxing.com/upload/20130330/1364651263157_1085.mp3");
        // uMusic.setAuthor("umeng");
        // uMusic.setTitle("天籁之音");
        // uMusic.setThumb(urlImage);
        // uMusic.setThumb("http://www.umeng.com/images/pic/social/chart_1.png");

        // 设置微信分享的内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(content);
        weixinContent.setTitle(title);
        weixinContent.setTargetUrl(targetUrl);
        weixinContent.setShareMedia(urlImage);
        mController.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(content);
        circleMedia.setTitle(title);
        circleMedia.setShareMedia(urlImage);
        // circleMedia.setShareMedia(uMusic);
        // circleMedia.setShareMedia(video);
        circleMedia.setTargetUrl(targetUrl);
        mController.setShareMedia(circleMedia);

        // 设置QQ空间分享内容
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(content);
        qzone.setTargetUrl(targetUrl);
        qzone.setTitle(title);
        qzone.setShareMedia(urlImage);
        // qzone.setShareMedia(uMusic);
        mController.setShareMedia(qzone);

        // 设置新浪微博分享内容
        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent.setTitle(title);
        // if(content.length()>140){//2015-12-24 新浪微博文字长度限制
        // content=content.substring(0, 140);
        // }
        sinaContent.setShareContent(content);
        sinaContent.setShareImage(urlImage);
        sinaContent.setTargetUrl(targetUrl);
        mController.setShareMedia(sinaContent);

    }

    /**
     * -------------------友盟分享结束--------------------/**
     *
     */

    /**
     * 跳转到权限管理
     */
    public void toPowerManager() {
        PackageManager pm = getPackageManager();
        PackageInfo info = null;
        try {
            info = pm.getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        Intent i = new Intent("miui.intent.action.APP_PERM_EDITOR");
        i.setClassName("com.android.settings",
                "com.miui.securitycenter.permission.AppPermissionsEditor");
        i.putExtra("extra_package_uid", info.applicationInfo.uid);
        try {
            startActivity(i);
        } catch (Exception e) {
            Toast.makeText(this, "只有MIUI才可以设置哦", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    // 2015-12-24 添加新浪授权回调
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** 使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
                requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    /**
     * 2015 11 27 销毁所有的Activity；
     */
    protected void finishAll() {
        appManager.finishAllActivity();
    }

    /**
     * 结束制定类名的Activity 2015 11 27
     *
     * @param cls
     */
    protected void finisActivity(Class cls) {
        appManager.finishActivity(cls);
    }

    /**
     * 提醒完善信息
     */
    protected void hintPerfectInfo(String hint, final int type, final Activity activity) {
        AlertDialog.Builder builder = new Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_info).setTitle("提示")
                .setMessage(hint).setCancelable(false)
                .setNegativeButton("完善", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (type == 1) {// 完善个人信息
                          /*  mContext.startActivity(new Intent(mContext,
                                    AutonymAuthActivity.class)); */

                            mContext.startActivity(new Intent(mContext,
                                    CompletePersonalInfoActivity.class));
                        } else {// 完善个人手机号
                            mContext.startActivity(new Intent(mContext,
                                    ModifyPhoneActivity.class));
                        }
                        activity.finish();
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
     * 退出监听
     */
    // @Override
    // public boolean dispatchKeyEvent(KeyEvent event) { 此处必须注释掉 否则子类无法处理 返回时候
    // onbackHome
    // if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
    // && event.getAction() == KeyEvent.ACTION_DOWN
    // && event.getRepeatCount() == 0) {
    // if (null != webView) {
    // if (webView.canGoBack()) {
    // webView.goBack();
    // return false;
    // } else {
    // instance.finish();
    // return super.dispatchKeyEvent(event);
    // }
    // } else {
    // instance.finish();
    // return super.dispatchKeyEvent(event);
    // }
    // }
    // return super.dispatchKeyEvent(event);
    // }
}
