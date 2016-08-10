package com.eztcn.user.hall.views;

import android.content.Context;
import android.os.Environment;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.eztcn.user.R;

/**
 * @Author: lizhipeng
 * @Data: 16/6/13 下午2:41
 * @Description: 通用的webView
 */
public class EztWebView extends WebView {

    private ProgressBar mProgressBar;
    /**
     * 网页缓存目录
     */
    private static final String cacheDirPath = Environment
            .getExternalStorageDirectory() + "/LoadingWebViewDome/webCache/";

    public EztWebView(Context context) {
        super(context, null);
    }

    public EztWebView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public EztWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initContext(context);
    }

    private void initContext(Context context) {
        requestFocus();
        setInitialScale(39);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//支持通过Javascript打开新窗口
        getSettings().setJavaScriptEnabled(true);//设置WebView属性，能够执行Javascript脚本
        getSettings().setUseWideViewPort(true);//将图片调整到适合webview的大小
        getSettings().setLoadWithOverviewMode(true);// 缩放至屏幕的大小
        getSettings().setDomStorageEnabled(true);//设置是否启用了DOM Storage API
        getSettings().setDatabaseEnabled(true);//开启database storage API功能
        getSettings().setDatabasePath(cacheDirPath); //设置数据库缓存路径
        getSettings().setAppCachePath(cacheDirPath);//设置Application Caches缓存目录
        getSettings().setAppCacheEnabled(true);//开启Application Caches功能
    }

    /**
     * 加载网页url
     *
     * @param url
     */
    public void loadMessageUrl(String url) {
        super.loadUrl(url);
        setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { // 重写此方法表明点击网页里面的链接不调用系统浏览器，而是在本WebView中显示
                loadUrl(url);//加载需要显示的网页
                return true;
            }
        });
    }

    /**
     * 添加进度条
     */
    public void addProgressBar() {
        mProgressBar = new ProgressBar(getContext(), null,
                android.R.attr.progressBarStyleHorizontal);
        mProgressBar.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, 5, 0, 0));
        mProgressBar.setProgressDrawable(getContext().getResources()
                .getDrawable(R.drawable.new_bg_web_loading));
        addView(mProgressBar);//添加进度条至LoadingWebView中

        setWebChromeClient(new WebChromeClient());//设置setWebChromeClient对象
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mProgressBar.setVisibility(GONE);
            } else {
                if (mProgressBar.getVisibility() == GONE)
                    mProgressBar.setVisibility(VISIBLE);
                mProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    /**
     * 回收webview
     */
    public void destroyWebView() {
        clearCache(true);
        clearHistory();
    }

}
