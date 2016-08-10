package com.eztcn.user.hall.activity.loginSetting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.GuidanceActivity;
import com.eztcn.user.eztcn.activity.home.MyDialog;
import com.eztcn.user.hall.activity.BaseActivity;
import com.eztcn.user.hall.utils.Constant;

/**
 * Created by lx on 2016/6/4.
 * 设置界面。
 */
public class HelpActivity extends BaseActivity implements View.OnClickListener, MyDialog.DialogCancle,MyDialog.DialogSure {

    private Context context=HelpActivity.this;
    private RelativeLayout hot_line_relayout;
    private RelativeLayout suggest_feedback_relayout;
    private RelativeLayout function_introduce_relayout;
    private MyDialog dialog;
    @Override
    protected int preView() {
        return R.layout.new_activity_help;
    }

    @Override
    protected void initView() {

        loadTitleBar(true,"帮助","");
        hot_line_relayout= (RelativeLayout) this.findViewById(R.id.hot_line_relayout);
        suggest_feedback_relayout= (RelativeLayout) this.findViewById(R.id.suggest_feedback_relayout);
        function_introduce_relayout= (RelativeLayout) this.findViewById(R.id.function_introduce_relayout);
        setOnClickListener();
    }

    /**
     * 设置点击事件。
     */
    private void setOnClickListener() {

        hot_line_relayout.setOnClickListener(this);
        suggest_feedback_relayout.setOnClickListener(this);
        function_introduce_relayout.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }
    /**
     * 对话框确定
     */
    @Override
    public void dialogSure() {
        if(null!=dialog){
            Intent intent=new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + getString(R.string.hotline_telphone)));
            startActivity(intent);
            dialog.dissMiss();
        }
    }

    /**
     * 对话框取消。
     */
    @Override
    public void dialogCancle() {
        if(null!=dialog){
            dialog.dissMiss();
        }
    }

    /**
     * 点击事件。
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.hot_line_relayout:
                View viewContent = View.inflate(mContext, R.layout.ordercheck_dialog,
                        null);
                TextView costTv = (TextView) viewContent.findViewById(R.id.orderHintTv);
                costTv.setText("即将为您拨打"+getString(R.string.hotline_telphone)+"请确定");
                dialog= new MyDialog(context,"确定", "取消","拨打电话", viewContent);
                dialog.setDialogSure(this);
                dialog.setDialogCancle(this);
                dialog.show();
                break;
            case R.id.suggest_feedback_relayout:

                if(BaseApplication.patient==null){
                    hintToLogin(Constant.LOGIN_COMPLETE);
                    return;
                }
                startActivity(new Intent(context,FeedbackActivity.class));
                break;
            case R.id.function_introduce_relayout:

            startActivity(new Intent(context,GuidanceActivity.class));
                break;

        }
    }
}
