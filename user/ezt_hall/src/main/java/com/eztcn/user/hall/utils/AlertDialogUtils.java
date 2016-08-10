package com.eztcn.user.hall.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.eztcn.user.R;


/**
 * Created by xfz_zhipeng on 15/9/21.
 * 弹出框工具类
 */
public class AlertDialogUtils {

    /**
     * 显示提示信息alert
     * @param context
     * @param title
     * @param message
     * @param clickListener
     * @param confirm
     * @param cancel
     */
    public static void showDialog(Context context, String title, String message, final DialogClickListener clickListener, String cancel, String confirm) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
        alertDialog.show();//必须在设置layout之前调用
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.alert_dialog_tooltip);
        TextView titleTv = (TextView) window.findViewById(R.id.alert_dialog_tooltip_title);
        TextView messageTv = (TextView) window.findViewById(R.id.alert_dialog_tooltip_message);
        TextView confirmTv = (TextView) window.findViewById(R.id.alert_dialog_tooltip_confirm);
        TextView cancelTv = (TextView) window.findViewById(R.id.alert_dialog_tooltip_cancel);
        View lineV = (View) window.findViewById(R.id.alert_dialog_tooltip_line);
        titleTv.setText(title);
        messageTv.setText(message);
        confirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener == null) {
                    alertDialog.dismiss();
                } else {
                    clickListener.onCancelClick(alertDialog);
                }
            }
        });
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener == null) {
                    alertDialog.dismiss();
                } else {
                    clickListener.onConfirmClick(alertDialog);
                }
            }
        });
        if (TextUtils.isEmpty(confirm) && TextUtils.isEmpty(cancel)) {
            confirmTv.setVisibility(View.GONE);
            cancelTv.setVisibility(View.GONE);
            lineV.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(cancel)) {
            cancelTv.setBackgroundResource(R.drawable.bg_alert_dialog_button);
            confirmTv.setVisibility(View.GONE);
            lineV.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(confirm)) {
            confirmTv.setBackgroundResource(R.drawable.bg_alert_dialog_button);
            cancelTv.setVisibility(View.GONE);
            lineV.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(title)){
            titleTv.setVisibility(View.GONE);
        }

        confirmTv.setText(cancel);
        cancelTv.setText(confirm);


    }

    public static abstract class DialogClickListener {
        public abstract void onConfirmClick(AlertDialog alertDialog);

        public abstract void onCancelClick(AlertDialog alertDialog);
    }

    /**
     * 隐藏软键盘
     */
    public static void hideKeyboard(Context context) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (((Activity)context).getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (((Activity)context).getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(((Activity)context).getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
