package com.eztcn.user.hall.utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.hall.interfaces.CommonBottomDialogListener;

import java.util.List;

/**
 * 通用的底部弹出框
 *
 * @author Administrator
 */
public class CommonBottomDialog {

    /**
     * 底部弹出框
     * @param context 用Activity的this当做参数，不然会出现弹框加载窗体失败的情况
     * @param datas 数据源，最大为 5
     * @param listener
     */
    public static void showCommonBottomDialog(Activity context, List<String> datas, final CommonBottomDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog alertDialog = builder.create();
        View view = View.inflate(context, R.layout.new_common_bottom_dialog, null);

        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        alertDialog.show();
        window.setContentView(view);

        TextView canceltextView = (TextView) view.findViewById(R.id.new_common_bottom_dialog_textViewcancel);
        canceltextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }

            }
        });

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.new_common_bottom_dialog_linearLayout);
        for (int i = 0; i < datas.size(); i++) {
            final int index = i;
            TextView textView = (TextView) layout.getChildAt(index);
            textView.setVisibility(View.VISIBLE);
            textView.setText(datas.get(i));
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }
                    listener.itemClick(index);

                }
            });
        }
    }

    /**
     * 肿瘤医院特殊弹出框,无法复用
     */
    public static void specialShowCommonBottomDialog(Activity context, final CommonBottomDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog alertDialog = builder.create();
        View view = View.inflate(context, R.layout.new_special_bottom_dialog, null);

        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        alertDialog.show();
        window.setContentView(view);

        TextView canceltextView = (TextView) view.findViewById(R.id.new_common_bottom_dialog_textViewcancel);
        canceltextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }

            }
        });

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.new_common_bottom_dialog_linearLayout);
        layout.getChildAt(0).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }
                    listener.itemClick(0);

                }
            });
        layout.getChildAt(1).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }
                    listener.itemClick(0);

                }
            });
        layout.getChildAt(2).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }
                    listener.itemClick(1);

                }
            });
    }

}
