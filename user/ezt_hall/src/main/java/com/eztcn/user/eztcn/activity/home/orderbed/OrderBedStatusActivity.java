package com.eztcn.user.eztcn.activity.home.orderbed;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xutils.BitmapUtils;
import xutils.ViewUtils;
import xutils.bitmap.BitmapCommonUtils;
import xutils.http.RequestParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.OrderBed;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.impl.OrderBedImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.JsonUtil;
/**
 * 预约病床的状态界面。
 * @author LX
 *@date2016-3-31 @time下午3:49:44
 */
public class OrderBedStatusActivity extends FinalActivity implements IHttpResult{
	private ImageView request_order, order_pay, order_check, order_succeed;
	private TextView request_order_tx, order_pay_tx, order_check_tx,
			order_succeed_tx;
	private ImageView check_status_image;
	private TextView check_status_tx;
	private TextView patient_name_tx, patient_contact_telphone_tx;
	private TextView symptom_describe_tx,special_requirements_tx;
	private Button see_details_bt;
	private Button back_home_bt;
	private String order_bed_id="";
	private Context context=OrderBedStatusActivity.this;
//	private TextView rigtht_tx=null;
	private GridView grid;
	private GridAddapter grid_adapter;
	private OrderBed bed_result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_bed_status);
		ViewUtils.inject(OrderBedStatusActivity.this);
		loadTitleBar(true, "订单详情", null);
//		rigtht_tx.setVisibility(View.GONE);
		initView();
		getorderId();
		getData();
	}
	
	private void initView() {

			request_order = (ImageView) this.findViewById(R.id.request_order);
			order_pay = (ImageView) this.findViewById(R.id.order_pay);
			order_check = (ImageView) this.findViewById(R.id.order_check);
			order_succeed = (ImageView) this.findViewById(R.id.order_succeed);

			request_order_tx = (TextView) this.findViewById(R.id.request_order_tx);
			order_pay_tx = (TextView) this.findViewById(R.id.order_pay_tx);
			order_check_tx = (TextView) this.findViewById(R.id.order_check_tx);
			order_succeed_tx = (TextView) this.findViewById(R.id.order_succeed_tx);
			check_status_image=(ImageView) this.findViewById(R.id.check_status_image);
			check_status_tx=(TextView) this.findViewById(R.id.check_status_tx);
			patient_name_tx = (TextView) this.findViewById(R.id.patient_name_tx);
			patient_contact_telphone_tx = (TextView) this
					.findViewById(R.id.patient_contact_telphone_tx);
			symptom_describe_tx=(TextView) this.findViewById(R.id.symptom_describe_tx);
			special_requirements_tx=(TextView) this.findViewById(R.id.special_requirements_tx);
			grid=(GridView) this.findViewById(R.id.grid);
			see_details_bt = (Button) this.findViewById(R.id.see_details_bt);
			see_details_bt.setOnClickListener(see_details_btClickListener);
			back_home_bt = (Button) this.findViewById(R.id.back_home_bt);
			back_home_bt.setOnClickListener(back_home_btClickListener);
//			rigtht_tx.setOnClickListener(rigtht_txOnClickListener);
			
		}
	   // see details
		OnClickListener see_details_btClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				if(bed_result.getAuditingStatus()==1||bed_result.getAuditingStatus()==8){
					
					
					if(bed_result.getAuditingStatus()==EZTConfig.BED_STATUS_UNPAY){
						
						intent.putExtra("cost", bed_result.getBedPrice()+"");
						intent.putExtra("sumCost", bed_result.getBedPrice()+"");
						intent.putExtra("isFirstPay", true);
						intent.putExtra("orderNoStr",bed_result.getOrderNumber()+"");
						intent.putExtra("orderId",bed_result.getId()+"" );
						
					}else if(bed_result.getAuditingStatus()==EZTConfig.BED_STATUS_DEPOSIT){
						
						intent.putExtra("cost", bed_result.getDeposit()+"");
						intent.putExtra("sumCost", bed_result.getDeposit()+"");
						intent.putExtra("isFirstPay", false);
						intent.putExtra("orderNoStr",bed_result.getOrderNumber()+"");
						intent.putExtra("orderId",bed_result.getId()+"" );
						
					}
					//跳转到支付界面。
					intent.setClass(OrderBedStatusActivity.this, OrderBedPayActivity.class);
				}else{
					intent.putExtra("bed", bed_result);
					// 跳转到详情界面。
					intent.setClass(OrderBedStatusActivity.this, OrderBedDetailsActivity.class);
				}
				startActivity(intent);
				
			}
		};
		OnClickListener back_home_btClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				BackHome(OrderBedStatusActivity.this,0);
			}
		};
		
		/*//编辑订单
		OnClickListener rigtht_txOnClickListener=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//
				Intent intent=new Intent();
				intent.putExtra("bed", bed_result);
			    intent.setClass(OrderBedStatusActivity.this, ActivityOrderBedWrite.class);
				startActivity(intent);
			}
		};*/
 
	private void getorderId() {
		if (getIntent() != null) {
			order_bed_id = getIntent().getStringExtra("order_id");
		}
	}
		//获取数据。
	private void getData() {
		RequestParams params = new RequestParams();
		OrderBedImpl  impl=new OrderBedImpl();
		if (order_bed_id != null && order_bed_id.length() != 0) {
			params.addBodyParameter("orderBedId", order_bed_id);
			impl.gainOrderBedStatus(params, this);
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void result(Object... object) {

		int type = (Integer) object[0];
		boolean isSucc = (Boolean) object[1];
		Map<String, Object> map = (Map<String, Object>) object[2];
		hideProgressToast();
		switch (type) {
		case HttpParams.GET_ORDER_BED_STATUS:
			if (isSucc) {
				if ((Boolean) map.get("flag")) {
					if(map.containsKey("data")){
						Object obj = map.get("data");
						OrderBed  bed;
						
						
//						bed = JsonUtil.getBean(obj.toString(),
//								OrderBed.class);
						
//						JSONObject json;
//						try {
//							json = new JSONObject(obj.toString());
//							JSONArray picArray=json.getJSONArray("picUrls");
//							String picUrils[]=new String[picArray.length()];
//							for (int i = 0; i < picArray.length(); i++) {
//								picUrils[i]=picArray.getString(i);
//							}
//							bed.setPicUrls(picUrils);
//						} catch (JSONException e) {
//							// TODO 自动生成的 catch 块
//							e.printStackTrace();
//						}
						bed=(OrderBed) obj;
						
						bed_result=bed;
						setData(bed);
					}
				} else {
					String msgStr = "";
					if (map.containsKey("msg")) {
						msgStr = String.valueOf(map.get("msg"));
					}
					Toast.makeText(this, msgStr, Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(mContext, "服务器繁忙",
						Toast.LENGTH_SHORT).show();
			}
			break;
		
		default:
			Toast.makeText(context, "请求失败", Toast.LENGTH_SHORT).show();
			break;
		}
	}
	//设置数据。
	private void setData(OrderBed bed) {
		
		if(bed!=null){
			
			setBedStatus(bed.getAuditingStatus());
			List<String> list=new ArrayList<String>();
			if(null!=bed.getPicUrls())
			for (int i = 0; i < bed.getPicUrls().length; i++) {
				
				list.add(bed.getPicUrls()[i]);
			}
			setImage(list);
			setInfo(bed);
		}
	}
	//设置图片显示图片。
	private void setImage(List<String> list) {
		 setGridView(list);
	}
	private void setGridView(List<String> list) {
		int size = list.size();
        int length = 80;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 5) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        grid.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        grid.setColumnWidth(itemWidth); // 设置列表项宽
        grid.setHorizontalSpacing(3); // 设置列表项水平间距
        grid.setStretchMode(GridView.NO_STRETCH);
        grid.setNumColumns(size); // 设置列数量=列表集合数
        grid_adapter=new GridAddapter(context);
		grid_adapter.setData(list);
		grid.setAdapter(grid_adapter);
	}

	//设置预约病床的信息。
	private void setInfo(OrderBed bed) {
		if(bed!=null){
			patient_name_tx.setText(bed.getPatientName());
			patient_contact_telphone_tx.setText(bed.getPatientPhone());
			symptom_describe_tx.setText(bed.getPatientStatus());
			special_requirements_tx.setText(bed.getPatientSpecialNeed());
		}
	}

	//设置病床的状态。
	private void setBedStatus(int status) {
		
		/*request_order_notpass_icon
		request_order_passed_icon*/
		
		/*order_check_notpass_icon
		order_check_passed_icon*/
		
		/*order_bed_not_passed_icon
		order_bed_passed_icon*/
		
		/*order_not_succeed_icon
		order_succeed_icon*/
		switch (status) {
		
		case EZTConfig.BED_STATUS_UNPAY:
			request_order.setImageResource(R.drawable.request_order_notpass_icon);
			order_pay.setImageResource(R.drawable.order_check_notpass_icon);
			order_check.setImageResource(R.drawable.order_bed_not_passed_icon);
			order_succeed.setImageResource(R.drawable.order_not_succeed_icon);
			request_order_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
			order_pay_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
			order_check_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
		    order_succeed_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
		    check_status_image.setImageResource(R.drawable.order_money);
			check_status_tx.setText("待支付...");
			check_status_tx.setTextColor(getResources().getColor(R.color.wait_pay));
			see_details_bt.setText("立即支付");
			break;
		case EZTConfig.BED_STATUS_UNAUDIT:
			
			request_order.setImageResource(R.drawable.request_order_passed_icon);
			order_pay.setImageResource(R.drawable.order_check_notpass_icon);
			order_check.setImageResource(R.drawable.order_bed_not_passed_icon);
			order_succeed.setImageResource(R.drawable.order_not_succeed_icon);
			request_order_tx.setTextColor(getResources().getColor(R.color.order_normal));
			order_pay_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
			order_check_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
		    order_succeed_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
		    check_status_image.setImageResource(R.drawable.order_examine);
			check_status_tx.setText("待审核...");
			check_status_tx.setTextColor(getResources().getColor(R.color.wait_pay));
//			rigtht_tx.setVisibility(View.VISIBLE);
			
			break;
		case EZTConfig.BED_STATUS_AUDITSUCCESS:
			request_order.setImageResource(R.drawable.request_order_passed_icon);
			order_pay.setImageResource(R.drawable.order_check_passed_icon);
			order_check.setImageResource(R.drawable.order_bed_not_passed_icon);
			order_succeed.setImageResource(R.drawable.order_not_succeed_icon);
			request_order_tx.setTextColor(getResources().getColor(R.color.order_normal));
			order_pay_tx.setTextColor(getResources().getColor(R.color.order_normal));
			order_check_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
		    order_succeed_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
		    check_status_image.setImageResource(R.drawable.order_consult);
			check_status_tx.setText("正在咨询...");
			check_status_tx.setTextColor(getResources().getColor(R.color.wait_pay));
			
			break;
		case EZTConfig.BED_STATUS_ORDERSUCCESS:
			request_order.setImageResource(R.drawable.request_order_passed_icon);
			order_pay.setImageResource(R.drawable.order_check_passed_icon);
			order_check.setImageResource(R.drawable.order_bed_passed_icon);
			order_succeed.setImageResource(R.drawable.order_succeed_icon);
			request_order_tx.setTextColor(getResources().getColor(R.color.order_normal));
			order_pay_tx.setTextColor(getResources().getColor(R.color.order_normal));
			order_check_tx.setTextColor(getResources().getColor(R.color.order_normal));
		    order_succeed_tx.setTextColor(getResources().getColor(R.color.order_normal));
		    check_status_image.setImageResource(R.drawable.order_succeed_icon);
			check_status_tx.setText("预约成功");
			check_status_tx.setTextColor(getResources().getColor(R.color.order_normal));
			
			break;
		case EZTConfig.BED_STATUS_REFUSEAUDIT:
			request_order.setImageResource(R.drawable.request_order_passed_icon);
			order_pay.setImageResource(R.drawable.order_check_notpass_icon);
			order_check.setImageResource(R.drawable.order_bed_not_passed_icon);
			order_succeed.setImageResource(R.drawable.order_not_succeed_icon);
			request_order_tx.setTextColor(getResources().getColor(R.color.order_normal));
			order_pay_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
			order_check_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
		    order_succeed_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
		    check_status_image.setImageResource(R.drawable.order_reject);
			check_status_tx.setText("拒绝订单");
			check_status_tx.setTextColor(getResources().getColor(R.color.wait_pay));
			
			break;
		case EZTConfig.BED_STATUS_PAYBACK:
			request_order.setImageResource(R.drawable.request_order_passed_icon);
			order_pay.setImageResource(R.drawable.order_check_passed_icon);
			order_check.setImageResource(R.drawable.order_bed_passed_icon);
			order_succeed.setImageResource(R.drawable.order_succeed_icon);
			request_order_tx.setTextColor(getResources().getColor(R.color.order_normal));
			order_pay_tx.setTextColor(getResources().getColor(R.color.order_normal));
			order_check_tx.setTextColor(getResources().getColor(R.color.order_normal));
		    order_succeed_tx.setTextColor(getResources().getColor(R.color.order_normal));
		    check_status_image.setImageResource(R.drawable.order_succeed_icon);
			check_status_tx.setText("办理退款");
			check_status_tx.setTextColor(getResources().getColor(R.color.order_normal));
			
			break;
		case EZTConfig.BED_STATUS_PAYOUTTIME:
			request_order.setImageResource(R.drawable.request_order_notpass_icon);
			order_pay.setImageResource(R.drawable.order_check_notpass_icon);
			order_check.setImageResource(R.drawable.order_bed_not_passed_icon);
			order_succeed.setImageResource(R.drawable.order_not_succeed_icon);
			request_order_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
			order_pay_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
			order_check_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
		    order_succeed_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
		    check_status_image.setImageResource(R.drawable.order_time_out);
			check_status_tx.setText("支付超时");
			check_status_tx.setTextColor(getResources().getColor(R.color.wait_pay));
			see_details_bt.setVisibility(View.GONE);
			
			break;
		case EZTConfig.BED_STATUS_DEPOSIT:
			
			//收取押金
			request_order.setImageResource(R.drawable.request_order_passed_icon);
			order_pay.setImageResource(R.drawable.order_check_passed_icon);
			order_check.setImageResource(R.drawable.order_bed_passed_icon);
			order_succeed.setImageResource(R.drawable.order_succeed_icon);
			request_order_tx.setTextColor(getResources().getColor(R.color.order_normal));
			order_pay_tx.setTextColor(getResources().getColor(R.color.order_normal));
			order_check_tx.setTextColor(getResources().getColor(R.color.order_normal));
		    order_succeed_tx.setTextColor(getResources().getColor(R.color.order_normal));
		    check_status_image.setImageResource(R.drawable.order_money);
			check_status_tx.setText("待支付...");
			check_status_tx.setTextColor(getResources().getColor(R.color.wait_pay));
			see_details_bt.setText("立即支付");
			break;
		case EZTConfig.BED_STATUS_SUCCESS:
			request_order.setImageResource(R.drawable.request_order_passed_icon);
			order_pay.setImageResource(R.drawable.order_check_passed_icon);
			order_check.setImageResource(R.drawable.order_bed_passed_icon);
			order_succeed.setImageResource(R.drawable.order_succeed_icon);
			request_order_tx.setTextColor(getResources().getColor(R.color.order_normal));
			order_pay_tx.setTextColor(getResources().getColor(R.color.order_normal));
			order_check_tx.setTextColor(getResources().getColor(R.color.order_normal));
		    order_succeed_tx.setTextColor(getResources().getColor(R.color.order_normal));
		    check_status_image.setImageResource(R.drawable.order_succeed_icon);
			check_status_tx.setText("支付成功");
			check_status_tx.setTextColor(getResources().getColor(R.color.order_normal));
			
			break;
		case EZTConfig.BED_STATUS_NOTBED:
			//暂无病床
			request_order.setImageResource(R.drawable.request_order_passed_icon);
			order_pay.setImageResource(R.drawable.order_check_passed_icon);
			order_check.setImageResource(R.drawable.order_bed_not_passed_icon);
			order_succeed.setImageResource(R.drawable.order_not_succeed_icon);
			request_order_tx.setTextColor(getResources().getColor(R.color.order_normal));
			order_pay_tx.setTextColor(getResources().getColor(R.color.order_normal));
			order_check_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
		    order_succeed_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
		    check_status_image.setImageResource(R.drawable.order_reject);
			check_status_tx.setText("暂无病床");
			check_status_tx.setTextColor(getResources().getColor(R.color.wait_pay));
			break;
		}
	}
	class GridAddapter extends BaseAdapter{

		private List<String> list_image=new ArrayList<String>();
		private Context context;
		private BitmapUtils bitmapUtils;
		private Bitmap defaultBit;
		@Override
		public int getCount() {
			return list_image.size();
		}

		public GridAddapter(Context context) {
			super();
			this.context=context;
			defaultBit = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.order_bed_light_hospital_icon);

			bitmapUtils = new BitmapUtils(context);
			bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils
					.getScreenSize(mContext).scaleDown(3));
			bitmapUtils.configDefaultLoadingImage(defaultBit);
			bitmapUtils.configDefaultLoadFailedImage(defaultBit);
		}
		
		public void setData(List<String> list_image){
			if(list_image!=null){
				this.list_image=list_image;
			}
		}
		@Override
		public Object getItem(int arg0) {
			return list_image.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			    LayoutInflater layoutInflater = LayoutInflater.from(context);
	            convertView = layoutInflater.inflate(R.layout.item_grid_image, null);
	            ImageView grid_image=(ImageView) convertView.findViewById(R.id.grid_image);
	            String address = list_image.get(position);
	            bitmapUtils.display(grid_image, EZTConfig.ORDERBED_IMG+address);
	            return convertView;
		}
	}
		
}
