package com.eztcn.user.hall.utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.hall.interfaces.CommonDialogOkListener;
import com.eztcn.user.hall.interfaces.SelectorListener;
import com.eztcn.user.hall.views.CustomNumberPicker;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 通用的底部弹出框
 *
 * @author Administrator
 */
public class CommonDialog {

    /**
     * 通用弹出框，一个按钮，一个标题
     *
     * @param context  用Activity的this当做参数，不然会出现弹框加载窗体失败的情况
     * @param listener
     */
    public static void showCommonDialog(Activity context, String title, String button
            , final CommonDialogOkListener listener, boolean isCanCancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog alertDialog = builder.create();
        View view = View.inflate(context, R.layout.new_dialog_one_button_one_title, null);

        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        alertDialog.setCancelable(isCanCancel);
        alertDialog.show();
        window.setContentView(view);

        TextView titleView = (TextView) view.findViewById(R.id.title);
        titleView.setText(title);
        TextView okView = (TextView) view.findViewById(R.id.button);
        okView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
                listener.onClick();
            }
        });

    }

    private static AlertDialog mAlertDialog;
    private static int index = 0;

    /**
     * @param activity         当前上下文
     * @param title            中间的标题文本
     * @param datas            列表中的数据源
     * @param selectorListener 确认按钮监听
     */
    public static void showTimeDialog(Activity activity, String title, final String[] datas, final SelectorListener selectorListener) {
        index = 0;
        final View view = View.inflate(activity, R.layout.new_selectdate_dialog, null);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.linearLayout);
        view.findViewById(R.id.dialog_cancel).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mAlertDialog.cancel();
            }
        });
        view.findViewById(R.id.dialog_ok).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                selectorListener.confirm(index);
                mAlertDialog.cancel();
            }
        });
        final CustomNumberPicker mPicker = new CustomNumberPicker(activity);

        mPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);//关掉编辑模式，不然Picker上会有光标和软键盘弹出来

        mPicker.setDisplayedValues(datas);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mPicker.setLayoutParams(layoutParams);
        setNumberPickerDividerColor(activity, mPicker);
        mPicker.setMinValue(0);
        mPicker.setMaxValue(datas.length - 1);
        mPicker.setValue(0);
        mPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                index = newVal;
            }
        });
        layout.addView(mPicker);
        mAlertDialog = new AlertDialog.Builder(activity).setView(view).create();
        mAlertDialog.setCanceledOnTouchOutside(true);
        Window w = mAlertDialog.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        w.setAttributes(lp);

        if (!mAlertDialog.isShowing()) {
            mAlertDialog.show();
        }

    }

    /**
     * 设置NumberPicker的分界线颜色
     *
     * @param activity
     * @param numberPicker
     */
    private static void setNumberPickerDividerColor(Activity activity, NumberPicker numberPicker) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值
                    pf.set(picker, new ColorDrawable(activity.getResources().getColor(R.color.line_color_gray)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }


}
