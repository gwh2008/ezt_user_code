/**
 *
 */
package com.eztcn.user.eztcn.activity.home;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.drawable.BitmapDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.utils.ScreenUtils;

/**
 * @author Liu Gang
 *         <p/>
 *         2015年11月20日 下午4:54:42
 */
public class MyDialog {
    private int dialogWidth;
    public interface DialogCancle {
        public void dialogCancle();
    }
    public interface DialogSure {
        public void dialogSure();
    }

    private DialogCancle dialogCancle;
    private DialogSure dialogSure;
    private boolean showContent = false;
    private View view;
    /**
     * 给正文赋值
     * @param contentView
     * @param context
     * @param titleStr
     */
    public void resetContentView(View contentView,Context context,String titleStr){
        if(null==view){
            view= createDialog(context);
        }
        TextView viewTitle = (TextView) view.findViewById(R.id.dialogTitle);
        RelativeLayout contentLayout = (RelativeLayout) view
                .findViewById(R.id.dialogContent);
        View line1 = view.findViewById(R.id.line1);
        if (null != contentView) {// 居中显示正文
            contentLayout.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            if(contentLayout.getChildCount()>0){
                contentLayout.removeAllViews();
            }
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            contentLayout.addView(contentView, params);
            line1.setVisibility(View.VISIBLE);
        } else {
            contentLayout.setVisibility(View.GONE);
            line1.setVisibility(View.GONE);
        }
        line1.setVisibility(View.VISIBLE);

        // 赋值
        viewTitle.setText(titleStr);
    }
    public MyDialog(Context context, String sureStr, String cancleStr,
                    String title, View contentView) {
        if (null != contentView) {
            showContent = true;
        } else {
            showContent = false;
        }
        resetContentView(contentView,context,title);
        TextView btnY = (TextView) view.findViewById(R.id.btnY);
        TextView btnN = (TextView) view.findViewById(R.id.btnN);
        btnY.setText(sureStr);
        btnN.setText(cancleStr);
        btnN.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != dialogCancle)
                    dialogCancle.dialogCancle();
            }
        });
        btnY.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != dialogSure)
                    dialogSure.dialogSure();

            }
        });
    }
    public void setDialogCancle(DialogCancle dialogCancle) {
        this.dialogCancle = dialogCancle;
    }

    public void setDialogSure(DialogSure dialogSure) {
        this.dialogSure = dialogSure;
    }

    protected Dialog dialog;

    /**
     * 创造一个透明的对话框
     *
     * @param context
     * @return
     */
    private View createDialog(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog, null);
        dialog = new Dialog(context, R.style.ChoiceDialog);
        dialogWidth = (int) (ScreenUtils.gainDM(context).widthPixels * 0.85);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
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
        return view;
    }

    public void show() {
        if (null != dialog && !dialog.isShowing()) {
            WindowManager.LayoutParams params = dialog.getWindow()
                    .getAttributes();
            params.width = dialogWidth;
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setAttributes(params);
            dialog.show();
        }
    }

    public void dissMiss() {
        if (null != dialog && dialog.isShowing())
            dialog.dismiss();
    }
}
