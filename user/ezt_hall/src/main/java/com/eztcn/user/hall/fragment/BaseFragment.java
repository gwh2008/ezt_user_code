package com.eztcn.user.hall.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;

import com.eztcn.user.eztcn.customView.CustomProgressToast;

/**
 * @Author: lizhipeng
 * @Data: 16/6/4 下午3:13
 * @Description: 基类Fragment
 */
public abstract class BaseFragment extends Fragment {
    private ProgressDialog mProgressDialog;
    private boolean mIsShowDialog = false;
    protected boolean mFragmentIsVisible = false;//当前Fragment是否可见
    protected boolean mInitOver = false;//标记视图是否加载结束
    protected Context mContext;

    private CustomProgressToast progressToast = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {//可见并且第一次进入
            mFragmentIsVisible = true;
            onFragmentFirstResume();
        } else {
            mFragmentIsVisible = false;
        }
    }

    /**
     * Fragment 第一次显示的时候调用,可以在这里加载数据
     */
    protected abstract void onFragmentFirstResume();

    /**
     * 显示加载动画
     *
     * @param message
     */
    protected void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(true);
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
        mIsShowDialog = true;
    }

    /**
     * 取消加载动画
     */
    protected void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            if (mIsShowDialog){
                mProgressDialog.dismiss();
                mIsShowDialog = false;
            }
        }
    }

    /**
     * 开始加载进度条
     */
    public void showProgressToast() {
        if (progressToast == null) {
            progressToast = CustomProgressToast.makeText(
                    getContext(), Integer.MAX_VALUE);
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
}
