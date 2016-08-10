package com.eztcn.user.hall.activity.dragonCard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.home.dragoncard.DragonWriteActivity;
import com.eztcn.user.eztcn.activity.mine.ModifyPhoneActivity;
import com.eztcn.user.hall.activity.BaseActivity;
import com.eztcn.user.hall.activity.loginSetting.CompletePersonalInfoActivity;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.model.IModel;

/**
 * Created by lx on 2016/6/3.
 * 未开通我的龙卡界面
 */
public class MyDragonCardActivity extends BaseActivity implements ResultSubscriber.OnResultListener,View.OnClickListener{

    private Button mBindDragonBtn;

    @Override
    protected int preView() {
//        return R.layout.new_activity_my_dragon_card;
        return R.layout.activity_dragontoactivite30;
    }

    @Override
    protected void initView() {
        loadTitleBar(true, "我的龙卡", "");
//        findViewById(R.id.relativeLayout_my_privilege).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                startActivity(new Intent(mContext, MyPrivilegeActivity.class));
//            }
//        });
//        CheckSignUtils jni = new CheckSignUtils();
//        String token = jni.MD5("ezt"+ BaseApplication.eztUser.getMobile());
//        String path = "userId/"+ BaseApplication.eztUser.getUserId()+"/token/"+token;
//        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.DRAGON).getDragon(path,1001,this);

        mBindDragonBtn = (Button) findViewById(R.id.bindCardBtn);
//        mBindDragonBtn = (Button) findViewById(R.id.my_dragon_card_activity_bind_dragon_btn);
    }

    @Override
    protected void initData() {
        mBindDragonBtn.setOnClickListener(this);
    }

    @Override
    public void onStart(int requestType) {

    }

    @Override
    public void onCompleted(int requestType) {

    }

    @Override
    public void onError(int requestType) {

    }

    @Override
    public void onNext(IModel t, int requestType) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bindCardBtn:
                String idCard = BaseApplication.patient.getEpPid();
                String mobileStr=BaseApplication.patient.getEpMobile();
                if (TextUtils.isEmpty(idCard)) {
                    hintPerfectInfo("请完善个人信息再进行龙卡绑定!", 1, MyDragonCardActivity.this);
                    return;
                }
                startActivity(new Intent(this, DragonWriteActivity.class));
                break;
            case R.id.right_btn:

                break;
            default:
                break;
        }
    }

    /**
     * 提醒完善信息
     */
    protected void hintPerfectInfo(String hint, final int type, final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        dialog.show();

    }
}
