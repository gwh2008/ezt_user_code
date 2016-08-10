package com.eztcn.user.hall.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.baidu.mobstat.StatService;
import com.eztcn.user.hall.activity.BaseActivity;

/**
 * Created by zll on 2016/5/27.
 *  fragment的基类
 */
public class FinalFragment extends Fragment {
    protected Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseActivity ss=(BaseActivity) getActivity();
       // ss.hideProgressToast();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BaseActivity ss= (BaseActivity) getActivity();
        //ss.hideProgressToast();
    }
}
