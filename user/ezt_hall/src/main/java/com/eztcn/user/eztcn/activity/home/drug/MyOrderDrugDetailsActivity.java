package com.eztcn.user.eztcn.activity.home.drug;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.db.sqlite.WhereBuilder;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.MessageAll;
import com.eztcn.user.eztcn.bean.Record_Info;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.BackValidateDialog;
import com.eztcn.user.eztcn.customView.BackValidateDialog.CodeSure;
import com.eztcn.user.eztcn.customView.wheel.WheelDialog;
import com.eztcn.user.eztcn.customView.wheel.WheelDialog.CancleBtnListener;
import com.eztcn.user.eztcn.customView.wheel.WheelDialog.SureBtnListener;
import com.eztcn.user.eztcn.customView.wheel.WheelDialog.TextAdapter;
import com.eztcn.user.eztcn.customView.wheel.view.OnWheelChangedListener;
import com.eztcn.user.eztcn.customView.wheel.view.OnWheelClickedListener;
import com.eztcn.user.eztcn.customView.wheel.view.OnWheelScrollListener;
import com.eztcn.user.eztcn.customView.wheel.view.WheelView;
import com.eztcn.user.eztcn.customView.CustomDialog;
import com.eztcn.user.eztcn.db.EztDb;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.impl.RegistratioImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.ScreenUtils;
/**
 * 我的预约详情药品。
 * @author LX
 *@date2016-3-29 @time下午2:47:49
 */
public class MyOrderDrugDetailsActivity extends FinalActivity implements CodeSure,IHttpResult,SureBtnListener,
CancleBtnListener, OnWheelClickedListener, OnWheelScrollListener,
OnWheelChangedListener{
	
	@ViewInject(R.id.drug_details_image)
	private ImageView drug_details_image;
	@ViewInject(R.id.drug_name)
	private TextView drug_name;
	@ViewInject(R.id.drug_type)
	private TextView drug_type;
	@ViewInject(R.id.drug_hospital)
	private TextView drug_hospital;
	@ViewInject(R.id.order_name_tx)
	private TextView order_name_tx;
	@ViewInject(R.id.telphone)
	private TextView telphone;
	@ViewInject(R.id.order_time)
	private TextView order_time;
	@ViewInject(R.id.charge_way_tx)
	private TextView charge_way_tx;
	@ViewInject(R.id.health_type)
	private TextView health_type;
	@ViewInject(R.id.cancel_order_button)
	private Button cancel_order_button;
	
	private Intent intent_drug;
	private Record_Info record_Info;
	private Context context=MyOrderDrugDetailsActivity.this;
	View content_view;
	WheelView cancel_cause;
	private BackValidateDialog validateDialog;
	private String temp_regId, temp_pfId;
	private WheelDialog wheelDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_order_drug_details);
		content_view=this.findViewById(R.id.drug_layout);
		ViewUtils.inject(MyOrderDrugDetailsActivity.this);
		loadTitleBar(true, "预约详情", null);
		intent_drug=getIntent();
		JudgeData();
		
	}
	private void setWheelView() {

		wheelDialog=new WheelDialog(context);
		Window win = wheelDialog.getWindow();
		LayoutParams params = wheelDialog.getWindow()
				.getAttributes();
		win.setGravity(Gravity.BOTTOM);
		params.width = ScreenUtils.gainDM(mContext).widthPixels;
		params.height = ScreenUtils.gainDM(mContext).heightPixels / 3;
		List<Integer> currIndexs = new ArrayList<Integer>();
		currIndexs.add(1);
		List<List<String>> wheelData = new ArrayList<List<String>>();
		List<String> Data = new ArrayList<String>();
		
		Data.add("暂时不想预约药品了");
		Data.add("信息填写错误，重新填写药品");
		Data.add("其他原因");
		wheelData.add(Data);
		List<Integer> wheelIds = new ArrayList<Integer>();
		wheelIds.add(0);
		wheelDialog.config(this, this, wheelData, currIndexs, wheelIds, this,
				this, this, "请选择原因");
		wheelDialog.show();
	}
	private void JudgeData() {
		if(intent_drug!=null){
			record_Info=(Record_Info) intent_drug.getSerializableExtra("record");
			if(record_Info!=null){
				setData(record_Info);
			}
		}
	}
	private void setData(Record_Info record_Info) {
		
		switch (record_Info.getDoctorId()) {
		case 6364:
			drug_hospital.setText("天津市第一中心医院");
			drug_details_image.setImageResource(R.drawable.tianjin_first_hospital);
			break;
		case 8345:
			drug_hospital.setText("天津市工人医院");
			drug_details_image.setImageResource(R.drawable.tianjin_worker_hospital);
			break;
		}
		order_name_tx.setText(record_Info.getPatientName());
		
		drug_name.setText("舒筋节利药品");
		telphone.setText(record_Info.getPhone());
		drug_type.setText(record_Info.getDept());//科室的名字。

		
		String date=record_Info.getDate();
		String begin=record_Info.getBeginTime();
		String end=record_Info.getEndTime();
		String date_str=date.substring(0, date.indexOf(" "));
		
		String begin_str=begin.substring(begin.indexOf(" ") + 1,
				begin.lastIndexOf(":"));
		String end_str=end.substring(end.indexOf(" ") + 1, end.lastIndexOf(":"));
		order_time.setText(date_str+ " "+begin_str+"-"+end_str);
		charge_way_tx.setText("到院缴费");
		
		switch (record_Info.getPayType()) {
		case 0:
			health_type.setText("自费");
			break;
		case 1:
			health_type.setText("医保");
			break;
		}
	}
	
	//取消订单。
	@OnClick(R.id.cancel_order_button)
	private void cancelOrderClick(View v){
		setWheelView();
		
	}
	  /**
	   * 显示popupWindow
	   */
//	  private void backNumber2(final String pfId, final String regId) {
//
//			final CustomDialog backDialog = new CustomDialog(mContext);
//
//			backDialog.setOnPositiveListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					
//					backDialog.dismiss();
//				}
//			});
//			backDialog.setOnNegativeListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					temp_regId = "";
//					temp_pfId = "";
//					backDialog.dismiss();
//				}
//			});
//
//		}
	@Override
	public void onSureClick(String valideCode) {

		if (valideCode.isEmpty()) {
			Toast.makeText(MyOrderDrugDetailsActivity.this, "请输入验证码",
					Toast.LENGTH_SHORT).show();
		} else {
			validateDialog.dismiss();
			RegistratioImpl impl = new RegistratioImpl();
			RequestParams params = new RequestParams();
			params.addBodyParameter("regId", temp_regId);
			params.addBodyParameter("pfId", temp_pfId);
			params.addBodyParameter("mobile",
					BaseApplication.patient.getEpMobile());
			params.addBodyParameter("code", valideCode);
			params.addBodyParameter("userId",
					"" + BaseApplication.patient.getUserId());// 2015-12-30
			// 退号时候要求加
			// 用户id
			impl.backNumber(params, MyOrderDrugDetailsActivity.this);
			showProgressToast();
		}
	}
	@Override
	public void result(Object... object) {

		hideProgressToast();
		if (object == null) {
			return;
		}
		Integer taskID = (Integer) object[0];
		if (taskID == null) {
			return;
		}
		boolean flag = (Boolean) object[1];
		if (!flag) {
			if (taskID == HttpParams.BACKNUMBER) {
				// 退号后移除短信验证时间点信息2015-12-31
				SystemPreferences.remove(EZTConfig.KEY_BACKNUM_TIME + "_"
						+ temp_regId);
			}
			Toast.makeText(getApplicationContext(), object[2].toString(),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (taskID == HttpParams.BACKNUMBER) {
			String msg;
			Map<String, Object> map = (Map<String, Object>) object[2];
			boolean f = (Boolean) map.get("flag");
			if (f) {
				msg = "退号成功";
				// 退号后移除短信验证时间点信息2015-12-31
				SystemPreferences.remove(EZTConfig.KEY_BACKNUM_TIME + "_"
						+ temp_regId);
				// EztDb.getInstance(mContext).delDataWhere(new MessageAll(),
				// "msgId = " + info.getId()); // 退号删除相应的预约提醒消息
				WhereBuilder b = WhereBuilder.b("msgId", "=", record_Info.getId());
				EztDb.getInstance(mContext).delDataWhere(new MessageAll(), b); // 退号删除相应的预约提醒消息
				setResult(1);
				finish();
			} else {
				// 退号后移除短信验证时间点信息2015-12-31
				SystemPreferences.remove(EZTConfig.KEY_BACKNUM_TIME + "_"
						+ temp_regId);
				msg = map.get("msg").toString();
			}
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
					.show();
			return;
		}
	}
	@Override
	public void onChanged(
			com.eztcn.user.eztcn.customView.wheel.view.WheelView wheel,
			int oldValue, int newValue) {
		wheelDialog.setTextviewSize(wheel.getCurrentItem(),
				(TextAdapter) wheel.getViewAdapter());
		
	}
	@Override
	public void onScrollingStarted(
			com.eztcn.user.eztcn.customView.wheel.view.WheelView wheel) {
		
	}
	@Override
	public void onScrollingFinished(
			com.eztcn.user.eztcn.customView.wheel.view.WheelView wheel) {
		
		wheelDialog.setTextviewSize(wheel.getCurrentItem(),
				(TextAdapter) wheel.getViewAdapter());
	}
	@Override
	public void onItemClicked(
			com.eztcn.user.eztcn.customView.wheel.view.WheelView wheel,
			int itemIndex) {
		
	}
	@Override
	public void cancleClick() {
		temp_regId = "";
		temp_pfId="";
		
	}
	//完成点击
	@Override
	public void sureClick(LinearLayout wheelLayout) {
		
		wheelDialog.dismiss();
		WheelView wheelView = (WheelView) wheelLayout.getChildAt(0);
	    wheelView.getCurrentItem();
	    if(record_Info!=null){
			
	    	temp_pfId = record_Info.getPlatformId()+"";
	    	temp_regId = record_Info.getId() + "";
			validateDialog = new BackValidateDialog(
					MyOrderDrugDetailsActivity.this, record_Info.getId(),
					BaseApplication.patient.getUserId());
			validateDialog.setSure(MyOrderDrugDetailsActivity.this);
			validateDialog.show();
		}
	}
}
