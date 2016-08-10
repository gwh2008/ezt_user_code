/**
 *
 */
package com.eztcn.user.eztcn.activity.home;

import java.util.Calendar;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;

/**
 * @author Liu Gang
 *         <p/>
 *         2015年11月30日 下午2:04:36
 */
public class TumorCaseActivity extends FinalActivity {//implements OnClickListener
    /**
     * 预约日期
     */
    private String regDateStr;
    @ViewInject(R.id.tv_reg_date)
    private TextView tv_reg_date;
    @ViewInject(R.id.cb_readcase)
    private CheckBox cb_readcase;
    @ViewInject(R.id.btnSure)//, click = "onClick"
    private Button btnSure;
    @ViewInject(R.id.btnCancle)//, click = "onClick"
    private Button btnCancle;

    @ViewInject(R.id.et_outdate)
    private EditText et_outdate;
    private Calendar c;
    private Intent intent;
    private boolean canReturn = false;
    //	private String wrongDate = "离出院日期不足25天";
    private String wrongDate = "复印病例预约日期需在出院日期25天后。";
    private DatePickerDialog datePickerDialog;

    protected void onCreate(android.os.Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tumorcase);
        ViewUtils.inject(TumorCaseActivity.this);
        intent = getIntent();
        regDateStr = intent.getStringExtra("reg_date");
        String dateStr = "";
        if (null != regDateStr)
            dateStr = regDateStr.replace("-", "");
        if (!"".equals(dateStr))
            tv_reg_date.setText(dateStr);
        loadTitleBar(true, "复印病历-声明", null);
        et_outdate.setOnFocusChangeListener(fosCL);

    }

    ;
    OnFocusChangeListener fosCL = new OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                // 选择日期
                int year = 0, monthOfYear = 0, dayOfMonth = 0;
                if (et_outdate.getText().toString().equals("点击选择") || et_outdate.getText().toString().equals("")) {
                    c = Calendar.getInstance();
                    year = c.get(Calendar.YEAR);
                    monthOfYear = c.get(Calendar.MONTH);
                    dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
                } else {
                    // 出院日期 给 datepickerdialog赋值
                    String outDateStr = et_outdate.getText().toString();
                    year = Integer.parseInt(String.valueOf(outDateStr.subSequence(
                            0, 4)));
                    monthOfYear = Integer.parseInt(String.valueOf(outDateStr
                            .subSequence(4, 6))) - 1;
                    dayOfMonth = Integer.parseInt(String.valueOf(outDateStr
                            .subSequence(6, 8)));
                }
                datePickerDialog = new DatePickerDialog(mContext, dateSetListener,
                        year, monthOfYear, dayOfMonth);
                datePickerDialog.show();
                et_outdate.clearFocus();
            }
        }
    };

    @OnClick(R.id.btnSure)
    public void btnSureClick(View v) {

        if (!cb_readcase.isChecked()) {
            Toast.makeText(mContext, "请阅读并接受《医指通声明》", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if (!canReturn) {
            Toast.makeText(mContext, wrongDate, Toast.LENGTH_SHORT).show();
            return;
        }
        intent.putExtra("canReg", true);
        setResult(1, intent);
        finish();
    }


    @OnClick(R.id.btnCancle)
    public void btnCancleClick(View v) {

        intent.putExtra("canReg", false);
        setResult(1, intent);
        finish();
    }

//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.btnSure: {
//
//			if (!cb_readcase.isChecked()) {
//				Toast.makeText(mContext, "请阅读并接受《医指通声明》", Toast.LENGTH_SHORT)
//						.show();
//				return;
//			}
//
//			if (!canReturn) {
//				Toast.makeText(mContext, wrongDate, Toast.LENGTH_SHORT).show();
//				return;
//			}
//			intent.putExtra("canReg", true);
//			setResult(1, intent);
//			finish();
//		}
//			break;
//		case R.id.btnCancle: {
//			intent.putExtra("canReg", false);
//			setResult(1, intent);
//			finish();
//		}
//			break;
//		}
//	}

    private OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // 25天长度
            long twentyFive = 26 * 24 * 60 * 60 * 1000L;
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, monthOfYear);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            Calendar regCalendar = Calendar.getInstance();
            String[] regDate = regDateStr.split("-");
            String reg_year = regDate[0];
            String reg_month = regDate[1];
            String reg_day = regDate[2];

            regCalendar.set(Calendar.YEAR, Integer.parseInt(reg_year));
            regCalendar.set(Calendar.MONTH, Integer.parseInt(reg_month) - 1);
            regCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(reg_day));
            //
            long diff = regCalendar.getTimeInMillis() - c.getTimeInMillis();
            if (diff < twentyFive) {
                // 不够25天给予否决

                if ("".equals(et_outdate.getText().toString())) {// 2016-02-17
                    // 若选择日期为空时候该标志位为否定，不为空时候顺延原来标志位
                    canReturn = false;
                }
                Toast.makeText(mContext, wrongDate, Toast.LENGTH_SHORT).show();
            } else {

                String yearStr = "" + year;
                String monthStr = (monthOfYear + 1) >= 10 ? ""
                        + (monthOfYear + 1) : "0" + (monthOfYear + 1);
                String dayStr = (dayOfMonth >= 10) ? "" + dayOfMonth : "0"
                        + dayOfMonth;
                et_outdate.setText(yearStr + monthStr + dayStr);

                canReturn = true;
            }
            datePickerDialog.cancel();
        }
    };

}
