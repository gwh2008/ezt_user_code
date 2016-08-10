package com.eztcn.user.eztcn.activity.home.orderbed;
import xutils.BitmapUtils;
import xutils.ViewUtils;
import xutils.bitmap.BitmapCommonUtils;
import xutils.view.annotation.ViewInject;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.bean.OrderBed;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.RoundImageView;
/**
 * 预约病床详情界面。
 * @author LX
 *@date2016-3-31 @time下午3:54:39
 */
public class OrderBedDetailsActivity extends FinalActivity{
	
	@ViewInject(R.id.order_details_succeed_layout)
	private LinearLayout order_details_succeed_layout;
	@ViewInject(R.id.user_head)
	private RoundImageView user_head;
	@ViewInject(R.id.secNameTv)
	private TextView secNameTv;
	@ViewInject(R.id.secMobileTv)
	private TextView secMobileTv;
	@ViewInject(R.id.order_hintTv)
	private TextView order_hintTv;
	@ViewInject(R.id.order_bed_item_image)
	private ImageView order_bed_item_image;
	@ViewInject(R.id.hospital_name)
	private TextView hospital_name;
	@ViewInject(R.id.hospital_address_hint)
	private TextView hospital_address_hint;
	@ViewInject(R.id.order_status_tx)
	private TextView order_status_tx;
	
	private BitmapUtils bitmapUtils;
	private Bitmap defaultBit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_bed_details);
		ViewUtils.inject(OrderBedDetailsActivity.this);
		loadTitleBar(true, "订单详情", null);
		setBitConfig();
		getIntentData();
	}

	private void setBitConfig() {
		
		defaultBit = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.order_bed_light_hospital_icon);
		bitmapUtils = new BitmapUtils(mContext);
		bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils
				.getScreenSize(mContext).scaleDown(3));
		bitmapUtils.configDefaultLoadingImage(defaultBit);
		bitmapUtils.configDefaultLoadFailedImage(defaultBit);		
	}

	private void getIntentData() {
		
		Intent intent_data=getIntent();
		if(intent_data!=null){
			
			OrderBed bed_data=(OrderBed) intent_data.getSerializableExtra("bed");
			setData(bed_data);
			
		}
	}
	private void setData(OrderBed bed) {
		
		if(bed!=null){
			
			if((bed.getSysDoctorMobile()!=null&&!bed.getSysDoctorName().equals(""))&&(bed.getSysDoctorName()!=null&&!bed.getSysDoctorMobile().equals(""))){
				order_details_succeed_layout.setVisibility(View.VISIBLE);
				order_hintTv.setVisibility(View.GONE);
				secNameTv.setText(bed.getSysDoctorName());
				secMobileTv.setText(bed.getSysDoctorMobile());
			}else{
				order_details_succeed_layout.setVisibility(View.GONE);
				order_hintTv.setVisibility(View.VISIBLE);
			}
			bitmapUtils.display(order_bed_item_image, EZTConfig.HOS_PHOTO+"hosView"+bed.getHospitalId()+".jpg");
			hospital_name.setText(bed.getHospitalName());
			hospital_address_hint.setText(bed.getHospitalAddress());
			setOrderSatstus(order_status_tx,bed.getAuditingStatus());
		}
	}

	//设置订单的状态。
	@SuppressWarnings("deprecation")
	private void setOrderSatstus(TextView status_tx ,int status) {
		

		Resources resources = mContext.getResources();
		Drawable drawable1 = resources
				.getDrawable(R.drawable.order_bed_status_bg_one);// red
		Drawable drawable2 = resources
				.getDrawable(R.drawable.order_bed_status_bg_two); // orange
		Drawable drawable3 = resources
				.getDrawable(R.drawable.order_bed_status_bg_three); // green

		if (status_tx != null) {
			switch (status) {
			case EZTConfig.BED_STATUS_UNPAY:
				status_tx.setText("待支付");
				status_tx.setBackgroundDrawable(drawable1);
			case EZTConfig.BED_STATUS_UNAUDIT:
				status_tx.setText("待审核");
				status_tx.setBackgroundDrawable(drawable1);
				break;
			case EZTConfig.BED_STATUS_AUDITSUCCESS:
				status_tx.setText("正在咨询");
				status_tx.setBackgroundDrawable(drawable3);
				break;
			case EZTConfig.BED_STATUS_ORDERSUCCESS:
				status_tx.setText("预约成功");
				status_tx.setBackgroundDrawable(drawable3);

				break;
			case EZTConfig.BED_STATUS_REFUSEAUDIT:
				status_tx.setText("拒绝订单");
				status_tx.setBackgroundDrawable(drawable1);

				break;
			case EZTConfig.BED_STATUS_PAYOUTTIME:
				status_tx.setText("支付超时");
				status_tx.setBackgroundDrawable(drawable1);
				break;
			case EZTConfig.BED_STATUS_DEPOSIT:
				status_tx.setText("收取押金");
				status_tx.setBackgroundDrawable(drawable2);

				break;
			case EZTConfig.BED_STATUS_SUCCESS:
				status_tx.setText("支付成功");
				status_tx.setBackgroundDrawable(drawable3);
				break;
			case EZTConfig.BED_STATUS_NOTBED:
				status_tx.setText("暂无病床");
				status_tx.setBackgroundDrawable(drawable1);
				break;
			}
		}
		
	}
}
