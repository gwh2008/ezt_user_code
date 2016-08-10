package com.eztcn.user.hall.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.home.dragoncard.ActivityDragonCard;
import com.eztcn.user.hall.activity.dragonCard.MyDragonCardActivity;
import com.eztcn.user.hall.activity.loginSetting.LoginActivity;
import com.eztcn.user.hall.common.DragonStatusSingle;

/**
 * Created by zll on 2016/5/29.
 * 发现的fragment
 */
public class FoundFragment extends FinalFragment implements View.OnClickListener{

    private Activity activity;
    private View rootView;

    public static FoundFragment getInstance() {
        FoundFragment fragment = new FoundFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.activity = this.getActivity();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 避免UI重新加载
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.new_fragment_found, null);// 缓存Fragment

            initView();

        }
        // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
//		loadNewsMsg();

        return rootView;
    }

    /**
     * 初始化View控件
     */
    private void initView() {
        rootView.findViewById(R.id.img_dragon).setOnClickListener(this);//进入龙卡详情页面
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.img_dragon://跳转到以前的龙卡详情页面

                if (BaseApplication.patient != null) {
                    if (DragonStatusSingle.getInstance().isOpenDragon()) {
                        startActivity(new Intent(mContext, ActivityDragonCard.class));
                    } else {
                        startActivity(new Intent(mContext, MyDragonCardActivity.class));
                    }
                } else {
                    hintToLogin(com.eztcn.user.hall.utils.Constant.LOGIN_COMPLETE);
                }
//                startActivity(new Intent(mContext, ActivityDragonCard.class));
                break;
        }
    }
    /**
     * 登录提示。
     */
    public void hintToLogin(final int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
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
                startActivityForResult(new Intent(getActivity(), LoginActivity.class), requestCode);
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


}
