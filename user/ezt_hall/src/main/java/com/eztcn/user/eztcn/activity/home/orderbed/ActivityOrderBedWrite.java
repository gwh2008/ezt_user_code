package com.eztcn.user.eztcn.activity.home.orderbed;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.fireking.app.imagelib.entity.ImageBean;
import org.fireking.app.imagelib.widget.PicSelectActivity;
import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import xutils.view.annotation.event.OnItemClick;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.BaseArrayListAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.FamilyMember;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.bean.orderbed.OrderBedImg;
import com.eztcn.user.eztcn.customView.MyHorizontalListView;
import com.eztcn.user.eztcn.customView.ScrollerNumberPicker;
import com.eztcn.user.eztcn.customView.ScrollerNumberPicker.OnSelectListener;
import com.eztcn.user.eztcn.db.EztDictionaryDB;
import com.eztcn.user.eztcn.impl.OrderBedImpl;
import com.eztcn.user.eztcn.impl.UserImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.JudgeUtils;
/**
 * @author Liu Gang
 * 
 *         2016年3月31日 下午7:18:05
 * 
 */
public class ActivityOrderBedWrite extends FinalActivity implements
		IHttpResult, OnClickListener {
	private final long F_MAX_SIZE = 5 * 1024 * 1024;
	@ViewInject(R.id.costTv)
	private TextView costTv;
	@ViewInject(R.id.patientRLayout)
	private View patientRLayout;
	@ViewInject(R.id.patientTv)
	private TextView patientTv;
	@ViewInject(R.id.telEt)
	private EditText telEt;
	@ViewInject(R.id.zzmsEt)
	private EditText zzmsEt;
	@ViewInject(R.id.zzmsTv)
	private TextView zzmsTv;
	@ViewInject(R.id.tsyqTv)
	private TextView tsyqTv;
	@ViewInject(R.id.tsyqEt)
	private EditText tsyqEt;
	@ViewInject(R.id.tsyqRLayout)
	private View tsyqRLayout;
	@ViewInject(R.id.zzmsRLayout)
	private View zzmsRLayout;
	@ViewInject(R.id.commitBedOrderBtn)
	private Button commitBedOrderBtn;

	private String person;// 所选就诊人
	private ScrollerNumberPicker personView;
	private TextView add;
	private ArrayList<FamilyMember> familyList;
	private List<String> personList;
	private PopupWindow pWindow;
	private View viewPerson;

	@ViewInject(R.id.imgLayout)
	private MyHorizontalListView imgHLv;
	private List<OrderBedImg> list;
	private ImgAdapter adapter;

	private int deptId;
	private Hospital hospital;
	private String cost;
	private FamilyMember familyMember;

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_writebedorder);
		ViewUtils.inject(ActivityOrderBedWrite.this);
		loadTitleBar(true, "填写订单", null);
		
		deptId = getIntent().getIntExtra("deptId", -1);
		cost = getIntent().getStringExtra("cost");
		hospital = (Hospital) getIntent().getExtras().get("hospital");

		costTv.setText("挂号费用:¥" + cost);
		telEt.setEnabled(false);
		initialDialogView();
		initData();
		// 初始化+按钮

		tsyqRLayout.setOnFocusChangeListener(focusListener);
		zzmsRLayout.setOnFocusChangeListener(focusListener);

		zzmsEt.setFocusableInTouchMode(true);
		zzmsEt.addTextChangedListener(new Watcher(200, zzmsEt, zzmsTv, "症状描述"));
		tsyqEt.setFocusableInTouchMode(true);
		tsyqEt.addTextChangedListener(new Watcher(200, tsyqEt, tsyqTv, "特殊要求"));

		list = new ArrayList<OrderBedImg>();
		OrderBedImg orderBedImg = new OrderBedImg();
		list.add(orderBedImg);
		adapter = new ImgAdapter(mContext);
		imgHLv.setAdapter(adapter);
		adapter.setList(list);

	};

	@OnItemClick(R.id.imgLayout)
	private void imgHlvItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		if (null == list.get(position).getPath()) {
			// 添加相片
			selectImg(true);
		} else {

			list.remove(position);
			if (list.size() == 4 && null != list.get(list.size() - 1).getPath()) {
				list.add(new OrderBedImg());
			}
			adapter.notifyDataSetChanged();
		}
	}

	@OnClick(R.id.tsyqRLayout)
	public void tsyqLayoutClick(View v) {// 特殊要求
		tsyqTv.setVisibility(View.GONE);
		tsyqEt.setVisibility(View.VISIBLE);
		tsyqTv.requestFocus();
		showSoftInput(tsyqTv);
	}

	@OnClick(R.id.zzmsRLayout)
	public void zzmsLayoutClick(View v) {// 症状描述
		zzmsTv.setVisibility(View.GONE);
		zzmsEt.setVisibility(View.VISIBLE);
		zzmsEt.requestFocus();
		showSoftInput(zzmsEt);
	}

	private OnFocusChangeListener focusListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (v == zzmsRLayout && hasFocus == false) {
				hideSoftInput(zzmsEt);
			}

			if (v == tsyqRLayout && hasFocus == false) {
				hideSoftInput(tsyqEt);
			}

		}

	};

	@OnClick(R.id.patientRLayout)
	private void patientRLayoutListener(View v) {
		selectPerson();
		pWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
	}

	@OnClick(R.id.commitBedOrderBtn)
	private void submitBtnListener(View v) {
		// 提交按钮
		if (judgeParam()) {
			showProgressToast();
			OrderBedImpl createBedOrder = new OrderBedImpl();
			RequestParams params = new RequestParams();

			params.addBodyParameter("patientId", familyMember.getPatientId());
			params.addBodyParameter("userId",
					String.valueOf(BaseApplication.patient.getUserId()));
			params.addBodyParameter("patientName", familyMember.getMemberName());// BaseApplication.eztUser.getUserName()
			params.addBodyParameter("patientSex",
					familyMember.getSex() == 0 ? "男" : "女");// BaseApplication.eztUser.getSex()
															// == 0 ? "男" : "女"
			params.addBodyParameter("patientPhone", telEt.getText().toString());// BaseApplication.eztUser.getMobile()
			params.addBodyParameter("patientCardName", "身份证");
			params.addBodyParameter("patientCardNum", familyMember.getIdCard());
			params.addBodyParameter("patientOrderType", "预约病床");
			params.addBodyParameter("hospitalId",
					String.valueOf(hospital.getId()));
			params.addBodyParameter("hospitalName", hospital.gethName());
			params.addBodyParameter("deptId", String.valueOf(deptId));
			params.addBodyParameter("patientSpecialNeed", tsyqEt.getText()
					.toString());
			params.addBodyParameter("patientStatus", zzmsEt.getText()
					.toString());
			boolean isOk = true;
			ArrayList<String> fPathList = new ArrayList<String>();

			for (int i = 0; i < list.size(); i++) {
				OrderBedImg img = list.get(i);
				if (null != img.getPath()) {
					String fPath = img.getPath();
					File file = new File(fPath);
					if (file.length() > F_MAX_SIZE) {
						isOk = false;
						toast("单张图片容量最多不可超过5M", Toast.LENGTH_SHORT);
						hideProgressToast();
						break;
					}
					params.addBodyParameter("pic" + (i + 1),new File(fPath),"multipart/form-data");
					fPathList.add(fPath);
				}
			}
			try{
				createBedOrder.createBedOrder(params, ActivityOrderBedWrite.this);
			}catch (Exception e){
				Log.i("error","error-----");
			}
		}
	}

	private class MyHandler extends Handler {
		private RequestParams params;
		private int sum;
		private int current;
		private OrderBedImpl createBedOrder;

		MyHandler(RequestParams params, int sum, int current,
				OrderBedImpl createBedOrder) {
			this.params = params;
			this.sum = sum;
			this.current = current;
			this.createBedOrder = createBedOrder;
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String[] msgStr = (String[]) msg.obj;
			params.addBodyParameter(msgStr[0], msgStr[1]);
			if (current == (sum - 1)) {
				createBedOrder.createBedOrder(params,
						ActivityOrderBedWrite.this);
				showProgressToast();
			}
		}
	}

	private class EncodeRunnable implements Runnable {
		private RequestParams params;
		private MyHandler handler;
		private String preFix;
		private String fPath;
		private int i;
		private int sum;

		EncodeRunnable(RequestParams params, String fPath, int i, int sum,
				OrderBedImpl createBedOrder) {
			this.params = params;
			handler = new MyHandler(params, sum, i, createBedOrder);
			this.fPath = fPath;
			preFix = fPath.substring(fPath.lastIndexOf(".") + 1);
			this.i = i;
			this.sum = sum;
		}

		@Override
		public void run() {
			String imgStr = "";
			try {
				imgStr = encodeBase64File(fPath);// ImgUtils.
			} catch (Exception e) {
				e.printStackTrace();
			}
			Message msg = handler.obtainMessage();
			Log.i(fPath, imgStr);
			String[] strs = { "pic" + (i + 1), "jpeg" + "?eztpictype#" + imgStr };// 固定压缩成jpeg
			msg.obj = strs;
			handler.sendMessage(msg);
		}
	}

	// 计算图片的缩放值
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	// 根据路径获得图片并压缩，返回bitmap用于显示
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	// 把bitmap转换成String
	public String encodeBase64File(String filePath) throws Exception {
		// Bitmap
		// bm=BitmapFactory.decodeStream(getClass().getResourceAsStream(filePath));
		Bitmap bm =null;
		
		int degree=getBitmapDegree(filePath);
		if(degree==90){
			bm=toturn(getSmallBitmap(filePath));
		}else{
		 bm = getSmallBitmap(filePath);
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
		byte[] b = baos.toByteArray();
		baos.close();
		return Base64.encodeToString(b, Base64.DEFAULT);

	}

	private Boolean judgeParam() {
		if ("必选".equals(patientTv.getText().toString())) {
			toast("请选择就诊人", Toast.LENGTH_SHORT);
			return false;
		}
		if (TextUtils.isEmpty(telEt.getText().toString())) {
			toast("请输入联系电话", Toast.LENGTH_SHORT);
			return false;
		}
		if (!JudgeUtils.isMobile(telEt.getText().toString())) {
			toast("联系电话格式有误", Toast.LENGTH_SHORT);
			return false;
		}
		if (list.size() == 1 && null == list.get(0).getPath()) {
			toast("请上传病历信息", Toast.LENGTH_SHORT);
			return false;
		}
		return true;
	}

	private void initData() {
		getMembers();
		showProgressToast();
	}

	/**
	 * 初始化选择框view
	 */
	private void initialDialogView() {
		viewPerson = LayoutInflater.from(this).inflate(R.layout.selectperson,
				null);
		add = (TextView) viewPerson.findViewById(R.id.add);
		personView = (ScrollerNumberPicker) viewPerson
				.findViewById(R.id.person_wheelview);

	}

	/**
	 * 获取就诊人
	 */
	private void getMembers() {
		RequestParams params1 = new RequestParams();
		params1.addBodyParameter("userId",
				String.valueOf(BaseApplication.patient.getUserId()));
		new UserImpl().getMemberCenter(params1, this);
	}

	/**
	 * 初始化WheelView数据
	 * 
	 * @param view
	 */
	private void initialTimeData(ScrollerNumberPicker view, List<String> data,
			int pos) {
		view.setData(data);
		view.setDefault(pos);
	}

	/**
	 * 窗口设置
	 */
	public void configPopupWindow(final View view) {
		pWindow = new PopupWindow();
		pWindow.setContentView(view);
		pWindow.setWidth(LayoutParams.MATCH_PARENT);
		pWindow.setHeight(LayoutParams.MATCH_PARENT);
		pWindow.setAnimationStyle(R.style.PopupAnimation);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		pWindow.setBackgroundDrawable(dw);
		pWindow.setFocusable(true);
		view.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				float left = v.findViewById(R.id.titleLayout).getLeft();
				float top = v.findViewById(R.id.titleLayout).getTop();
				float right;
				float bottom;
				right = v.findViewById(R.id.add).getRight();
				bottom = v.findViewById(R.id.add).getBottom();
				float x = event.getX();
				float y = event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {

					if (x < left && x > right) {
						if (y < top || y > bottom) {
							pWindow.dismiss();
							pWindow = null;// 2015-12-25
						}
					} else {
						if (y < top || y > bottom) {
							pWindow.dismiss();
							pWindow = null;// 2015-12-25
						}
					}
				}
				return true;
			}
		});
	}

	/**
	 * 就诊人选择
	 */
	private void selectPerson() {
		initialTimeData(personView, personList, 0);
		configPopupWindow(viewPerson);
		add.setOnClickListener(this);
		if (personList != null && personList.size() > 0) {// 默认第一个被选中
			person = personList.get(0);
			familyMember = familyList.get(0);
		}
		personView.setOnSelectListener(new OnSelectListener() {
			@Override
			public void selecting(int id, String text) {

			}

			@Override
			public void endSelect(int id, String text) {
				person = personList.get(id);
				familyMember = familyList.get(id);
			}
		});

	}

	private class Watcher implements TextWatcher {
		int maxNum = 200;
		EditText et;
		TextView tv;

		CharSequence temp;
		int editStart;
		int editEnd;
		String hintStr;

		Watcher(int maxNum, EditText et, TextView tv, String hintStr) {
			this.maxNum = maxNum;
			this.et = et;
			this.tv = tv;
			this.hintStr = hintStr;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			if (s.length() == 0) {
				showSoftInput(et);
			}
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			temp = s.toString();
		}

		@Override
		public void afterTextChanged(Editable s) {
			editStart = et.getSelectionStart();
			editEnd = et.getSelectionEnd();
			if (s.length() == 0) {// 当字符串为0时候进行处理
				et.setVisibility(View.INVISIBLE);
				hideSoftInput(et);
				tv.setVisibility(View.VISIBLE);
			} else if (s.length() > maxNum) {
				toast(hintStr + "最多可以填写" + maxNum + "字", Toast.LENGTH_SHORT);
				// et.removeTextChangedListener(watcher);
				s.delete(editStart - 1, editEnd);
				int tempSelection = editStart;
				et.setText(s);
				et.setSelection(tempSelection);
			}

		}

	}

	@Override
	public void result(Object... object) {
		Integer type = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];
		hideProgressToast();
		switch (type) {
		case HttpParams.MEMBER_CENTER:// 获取家庭成员
			if (isSuc) {
				familyList = (ArrayList<FamilyMember>) object[2];
				if (familyList != null) {
					personList = new ArrayList<String>();
					for (int i = 0; i < familyList.size(); i++) {
						String strName = familyList.get(i).getMemberName();
						String strCardNo = familyList.get(i).getIdCard();
						String strRelation = EztDictionaryDB.getInstance(
								getApplicationContext())
								.getLabelByTag("kinship",
										familyList.get(i).getRelation() + "");
						String strSex = familyList.get(i).getSex() == 0 ? "男"
								: "女";
						if (familyList.size() == 1) {
							if (TextUtils.isEmpty(strRelation)) {
								strRelation = "自己";
							}

						}
						if (TextUtils.isEmpty(strName)) {
							strName = "";
						}
						String strNew = strName;
						personList.add(strNew);

					}
					if (familyList.size() > 0) {
						familyMember = familyList.get(0);
						String strName = familyList.get(0).getMemberName();
						String strRelation = EztDictionaryDB.getInstance(
								getApplicationContext())
								.getLabelByTag("kinship",
										familyList.get(0).getRelation() + "");
						if (strName == null || "".equals(strName)) {
							strName = "本人";
						}
						if (TextUtils.isEmpty(strRelation)) {
							strRelation = "自己";
						}
						// person = strName + "(" + strRelation + ")";
						person = strName;
						patientTv.setText(person);
						telEt.setText(null == familyMember.getPhone() ? ""
								: familyMember.getPhone().toString());
					}
				} else {
					Toast.makeText(getApplicationContext(),
							getString(R.string.request_fail),
							Toast.LENGTH_SHORT).show();
				}

			} else {
				patientTv.setVisibility(View.GONE);
				Toast.makeText(getApplicationContext(), object[3].toString(),
						Toast.LENGTH_SHORT).show();
			}

			break;
		case HttpParams.CREATE_BED_ORDER:// 创建订单
			Map<String, Object> map = (Map<String, Object>) object[2];
			if (isSuc) {
				String orderNumber = String.valueOf(map.get("orderNumber"));
				String orderId = String.valueOf(map.get("orderId"));
				String numberStr = String.valueOf(map.get("number"));// 状态码
				// if ("2001".equals(numberStr)) {
				Intent intent = new Intent(ActivityOrderBedWrite.this,
						OrderBedPayActivity.class);
				intent.putExtra("sumCost", cost);
				intent.putExtra("cost", cost);
				intent.putExtra("orderNoStr", orderNumber);
				intent.putExtra("isFirstPay", true);
				intent.putExtra("orderId", orderId);
				startActivity(intent);
				finish();
				// } // 2000为处理成功

			} else {
				String msgStr = "";
				if (null != map) {
					msgStr = String.valueOf(map.get("msg"));
				} else {
					msgStr = String.valueOf(object[3]);
				}
				toast(msgStr, Toast.LENGTH_SHORT);
				finish();
			}

			break;
		}
	}

	@Override
	public void onClick(View v) {
		if (v == add) {// 确定就诊人
			patientTv.setText(person);
			telEt.setText(null == familyMember.getPhone() ? "" : familyMember
					.getPhone().toString());
			if (pWindow != null) {
				pWindow.dismiss();
				pWindow = null;
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// 拍照或图库返回结果
		if (data == null) {
//			Toast.makeText(mContext, "获取图片失败", Toast.LENGTH_LONG).show();
			return;
		}
		if (requestCode == 0x123 && resultCode == RESULT_OK) {
			@SuppressWarnings("unchecked")
			List<ImageBean> images = (List<ImageBean>) data
					.getSerializableExtra("images");
			String strPath;
			if (6 > (list.size() + images.size())) {
				for (int i = 0; i < images.size(); i++) {
					strPath = images.get(i).path;
					// Bitmap bip = BitmapFactory.decodeFile(strPath);
					addImages(strPath, list.size() - 1);
				}
				adapter.notifyDataSetChanged();
			} else if (6 == (list.size() + images.size())) {
				list.remove(list.size() - 1);
				for (int i = 0; i < images.size(); i++) {
					strPath = images.get(i).path;
					// Bitmap bip = BitmapFactory.decodeFile(strPath);
					addImages(strPath, list.size());
				}
				adapter.notifyDataSetChanged();
			} else if (6 < (list.size() + images.size())) {
				list.remove(list.size() - 1);
				 int temp=5 - list.size();
				for (int i = 0; i <temp; i++) {
					strPath = images.get(i).path;
					addImages(strPath, list.size());
				}
				adapter.notifyDataSetChanged();
			}

		}

	}

	/**
	 * 添加/修改一张图片
	 * 
	 * @param url
	 */
	private void addImages(String url, int position) {
		OrderBedImg img = new OrderBedImg();
		img.setPath(url);
		list.add(position, img);

	}

	/**
	 * 选择图片
	 * 
	 * @param bool
	 *            是否支持选择多张
	 */
	private void selectImg(boolean bool) {
		Intent intent = new Intent(getApplicationContext(),
				PicSelectActivity.class);
		intent.putExtra("isMChoice", bool);
		intent.putExtra("selectNum", list.size()-1);
		startActivityForResult(intent, 0x123);
	}

	private class ImgAdapter extends BaseArrayListAdapter<OrderBedImg> {
		LayoutInflater flater;
		Bitmap bitmapAdd;

		private class Holder {
			ImageView delView;
			ImageView showView;
		}

		private Holder holder;

		public ImgAdapter(Activity context) {
			super(context);
			flater = LayoutInflater.from(context);
			bitmapAdd = BitmapFactory.decodeResource(getResources(),
					R.drawable.img_add);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (null == convertView) {
				holder = new Holder();
				convertView = flater.inflate(R.layout.item_bed_photo, null);
				holder.showView = (ImageView) convertView
						.findViewById(R.id.addView);
				holder.delView = (ImageView) convertView
						.findViewById(R.id.reddel);
				holder.showView.getLayoutParams().width = bitmapAdd.getWidth();
				holder.showView.getLayoutParams().height = bitmapAdd
						.getHeight();
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			OrderBedImg bedImg = mList.get(position);
			String path = bedImg.getPath();
			if (null != path) {
				holder.delView.setVisibility(View.VISIBLE);
				holder.showView.setTag(path);
				disPlay(holder.showView, path);
				holder.delView.setVisibility(View.VISIBLE);
			} else {
				holder.showView.setImageResource(R.drawable.img_add);
				holder.delView.setVisibility(View.INVISIBLE);
			}

			return convertView;
		}

		private void disPlay(ImageView img, String url) {
			if (img.getTag().equals(url)) {
				
				
				
				Bitmap bm =null;
				
				int degree=getBitmapDegree(url);
				if(degree==90){
					bm=toturn(getSmallBitmap(url));
				}else{
				 bm = getSmallBitmap(url);
				}
				img.setImageBitmap(bm);
			}
		}

	}
	
	/**
	 * 读取图片的旋转的角度
	 *
	 * @param path
	 *            图片绝对路径
	 * @return 图片的旋转角度
	 */
	private int getBitmapDegree(String path) {
	    int degree = 0;
	    try {
	        // 从指定路径下读取图片，并获取其EXIF信息
	        ExifInterface exifInterface = new ExifInterface(path);
	        // 获取图片的旋转信息
	        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
	                ExifInterface.ORIENTATION_NORMAL);
	        switch (orientation) {
	        case ExifInterface.ORIENTATION_ROTATE_90:
	            degree = 90;
	            break;
	        case ExifInterface.ORIENTATION_ROTATE_180:
	            degree = 180;
	            break;
	        case ExifInterface.ORIENTATION_ROTATE_270:
	            degree = 270;
	            break;
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return degree;
	}
	
	public static Bitmap toturn(Bitmap img){
        Matrix matrix = new Matrix();
        matrix.postRotate(+90); /*翻转90度*/
        int width = img.getWidth();
        int height =img.getHeight();
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
        return img;
    }

}
