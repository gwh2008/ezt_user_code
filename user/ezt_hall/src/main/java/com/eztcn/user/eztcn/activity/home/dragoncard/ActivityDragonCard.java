/**
 *
 */
package com.eztcn.user.eztcn.activity.home.dragoncard;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.ChoiceDeptByHosActivity;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.utils.MD5;
import com.eztcn.user.eztcn.utils.js.WVJBWebViewClient;
import com.eztcn.user.hall.common.DragonStatusSingle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;

/**
 * @author Liu Gang
 *         <p/>
 *         2016年3月30日 下午6:02:45
 */
public class ActivityDragonCard extends FinalActivity {
    @ViewInject(R.id.webView)
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ViewUtils.inject(ActivityDragonCard.this);
        // http://192.168.0.105/weixin/Dragoncard/Index/index/userId/%@.html
        String md5tel = "";
        String url = "http://www.eztcn.com/Dragoncard/Maintenance/index.html";
        if (DragonStatusSingle.getInstance().isOpenDragon()) {//开通龙卡
            if (BaseApplication.patient != null) {
                md5tel = (MD5.getMD5ofStr("ezt" + BaseApplication.patient.getEpMobile())).toLowerCase();
                 url = EZTConfig.SERVER_Dragon + "Dragoncard/Index/index/token/"
                        + md5tel
                        + "/userId/" + BaseApplication.patient.getUserId() + ".html";
            }
        }
//		if(Build.VERSION.SDK_INT >= 19) {
//	        webView.getSettings().setLoadsImagesAutomatically(true);
//	    } else {
//	        webView.getSettings().setLoadsImagesAutomatically(false);
//	    }
//		String url = "http://192.168.0.105/weixin/Dragoncard/Maintenance/index.html";
            webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
            showProgressToast();
            configWebView(webView, new MyWebViewClient(webView), url);
    }

//	 @Override  
//     protected void onPause(){  
//         super.onPause();  
//
//         webView.pauseTimers();  
//         if(isFinishing()){  
//        	 webView.loadUrl("about:blank");  
//             setContentView(new FrameLayout(this));  
//         }  
//     }  
//
//     @Override  
//     protected void onResume(){  
//         super.onResume();  
//         webView.resumeTimers();  
//     }  

    class MyWebViewClient extends WVJBWebViewClient {
        public MyWebViewClient(WebView webView) {

            // support js send
            super(webView, new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                }

            });
            registerHandler("goBack", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {
                    String jsonStr = String.valueOf(data);
                    if (null != jsonStr) {
                        try {
                            JSONObject json = new JSONObject(jsonStr);
                            // String hosId = json.getString("hosId");
                            // String hosName = json.getString("hosName");
                            // hosName = java.net.URLDecoder.decode(
                            // (String) hosName, "UTF-8");
                            // SystemPreferences.save(EZTConfig.KEY_HOS_ID,
                            // hosId);
                            // SystemPreferences.save(EZTConfig.KEY_HOS_NAME,
                            // hosName);
                            //
                            // Intent intent_ = new Intent(
                            // ActivityDragonCard.this,
                            // ChoiceDeptByHosActivity.class);
                            //
                            // startActivity(intent_.putExtra("hosId", hosId)
                            // .putExtra("hosName", hosName)
                            // .putExtra("isNearHos", false));
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });

            /**
             * 日常挂号
             */
            registerHandler("DayRegister", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {
                    String jsonStr = String.valueOf(data);
                    if (null != jsonStr) {
                        try {
                            JSONObject json = new JSONObject(jsonStr);
                            String hosId = json.getString("hosId");
                            String hosName = json.getString("hosName");
                            hosName = java.net.URLDecoder.decode(
                                    (String) hosName, "UTF-8");
                            SystemPreferences.save(EZTConfig.KEY_HOS_ID, hosId);
                            SystemPreferences.save(EZTConfig.KEY_HOS_NAME,
                                    hosName);

                            Intent intent_ = new Intent(
                                    ActivityDragonCard.this,
                                    ChoiceDeptByHosActivity.class);

                            startActivity(intent_.putExtra("hosId", hosId)
                                    .putExtra("hosName", hosName)
                                    .putExtra("isNearHos", false));

                        } catch (JSONException e) {
                            // TODO 自动生成的 catch 块
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            // TODO 自动生成的 catch 块
                            e.printStackTrace();
                        }
                    }

                }
            });

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            hideProgressToast();
            webView.setOverScrollMode(WebView.OVER_SCROLL_IF_CONTENT_SCROLLS);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return super.shouldOverrideUrlLoading(view, url);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        hideProgressToast();
        return super.onKeyDown(keyCode, event);
    }
}
