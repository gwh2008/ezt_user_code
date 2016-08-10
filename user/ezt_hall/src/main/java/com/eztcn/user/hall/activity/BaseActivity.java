package com.eztcn.user.hall.activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.customView.CustomProgressToast;
import com.eztcn.user.eztcn.utils.AppManager;
import com.eztcn.user.hall.activity.loginSetting.LoginActivity;

public abstract class BaseActivity extends FragmentActivity {

    private ProgressDialog mProgressDialog;
    private CustomProgressToast progressToast = null;
    public static Context mContext;
    AppManager appManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(preView());
        mContext = this;
        appManager=AppManager.getAppManager();
        // showTitleLeftIcon(this);
        initView();
        initData();
        appManager.addActivity(this);
    }

    protected abstract int preView();

    protected abstract void initView();

    protected abstract void initData();


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
        leftBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBack();
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
        leftBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBack();
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

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK&& event.getAction() == KeyEvent.ACTION_UP ) {
            onBack();
            return true;//系统不会自己销毁页面了
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 将返回事件统一处理，如果在返回事件时需要一些自定义的事件就重写这个方法
     *
     */
    protected void onBack() {

        finish();
    }

    /**
     * 显示加载动画
     *
     * @param message
     */
    protected void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(true);
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
        mProgressDialog.setOnDismissListener(mProgressDialogOnDissListener);
    }

    /**
     * 取消加载动画
     */
    protected void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            if (!isFinishing())
                mProgressDialog.dismiss();
        }
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

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        dismissProgressDialog();
        super.onPause();
    }

    protected void exit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("努力加载中，请稍后…")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        android.os.Process.killProcess(android.os.Process
                                .myPid());
                    }
                }).setCancelable(false).create().show();
    }

    protected void exit(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        android.os.Process.killProcess(android.os.Process
                                .myPid());
                    }
                }).setCancelable(false).create().show();
    }


//	protected void loadData(int dataType, OnLoadListener listener) {
//		AsyncLoader loader = new AsyncLoader(dataType);
//		loader.setOnLoadListener(listener);
//		loader.execute();
//	}
//
//	protected String getImeiMac() {
//		return ContentManager.getInstance().getImei()
//				+ ContentManager.getInstance().getMac();
//	}

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
     * 登录提示。
     */
    public void hintToLogin(final int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View dialogView = inflater.inflate(R.layout.item_login_dialog, null);
        TextView login = (TextView) dialogView.findViewById(R.id.dialog_login);
        TextView cancel = (TextView) dialogView
                .findViewById(R.id.dialog_cancel);
        dialog.show();
        dialog.setContentView(dialogView);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivityForResult(new Intent(mContext,LoginActivity.class), requestCode);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

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
    }
    DialogInterface.OnDismissListener   mProgressDialogOnDissListener=new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {

            disDialogCallBack();
        }
    };
    /**
     * 对话框监听回调方法。
     */
     protected   void disDialogCallBack() {


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        appManager.finishActivity(this);
    }
}
