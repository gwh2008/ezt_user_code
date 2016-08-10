package com.eztcn.user.eztcn.activity.home;

import java.util.Map;

import xutils.BitmapUtils;
import xutils.ViewUtils;
import xutils.bitmap.BitmapCommonUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.mine.AutonymAuthActivity;
import com.eztcn.user.eztcn.activity.mine.ModifyPhoneActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Doctor;
import com.eztcn.user.eztcn.bean.TelDocState;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.RoundImageView;
import com.eztcn.user.eztcn.db.EztDictionaryDB;
import com.eztcn.user.eztcn.impl.AttentionImpl;
import com.eztcn.user.eztcn.impl.HospitalImpl;
import com.eztcn.user.eztcn.impl.RegistratioImpl;
import com.eztcn.user.eztcn.impl.TelDocImpl;
import com.eztcn.user.eztcn.impl.UserImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.Logger;
import com.eztcn.user.eztcn.utils.MapUtil;
import com.eztcn.user.eztcn.utils.ResourceUtils;
import com.eztcn.user.eztcn.utils.StringUtil;
import com.eztcn.user.hall.utils.Constant;

/**
 * @title 个人医生站
 * @describe
 * @author ezt
 * @created 2014年12月18日
 */
public class DoctorIndexActivity extends FinalActivity implements IHttpResult,
		OnClickListener {

	@ViewInject(R.id.order)
	// , click = "onClick"
	private TextView order;// 预约挂号
	@ViewInject(R.id.doctorPhone)
	// , click = "onClick"
	private TextView doctorPhone;// 电话医生
	@ViewInject(R.id.seeIllRecord)
	// , click = "onClick"
	private TextView seeIllRecord;// 专家看病历

	@ViewInject(R.id.pic_text_inquiry_tv)
	// , click = "onClick"
	private TextView tvPicText;// 图文问诊

	@ViewInject(R.id.totalEvaLayout)
	// , click = "onClick"
	private RelativeLayout totalEvaLayout;// 综合评价
	@ViewInject(R.id.evaluateLayout)
	// , click = "onClick"
	private LinearLayout evaluateLayout;// 未评价
	@ViewInject(R.id.thankLetter)
	// , click = "onClick"
	private TextView thankLetter;// 感谢信
	@ViewInject(R.id.doctorEssay)
	// , click = "onClick"
	private TextView doctorEssay;

	@ViewInject(R.id.attention)
	// , click = "onClick"
	private TextView tvAttent;// 关注

	@ViewInject(R.id.doctorPhoto)
	private RoundImageView imgHead;// 医生头像
	

	@ViewInject(R.id.doctorName)
	private TextView tvName;// 医生名称

	@ViewInject(R.id.jobTitle)
	private TextView tvPosition;// 医生职位

	@ViewInject(R.id.hospital)
	private TextView tvHos;// 所属医院

	@ViewInject(R.id.dept)
	private TextView tvDept;// 所属科室

	@ViewInject(R.id.fansCount)
	private TextView tvFans;// 粉丝数

	@ViewInject(R.id.orderRate)
	private TextView tvRate;// 预约率

	@ViewInject(R.id.putNums)
	private TextView tvPutNums;// 放号量

	@ViewInject(R.id.goodAt)
	private TextView tvGoodAt;// 擅长

	@ViewInject(R.id.unEvaluateCount)
	private TextView tvUnECount;// 未评价数

	@ViewInject(R.id.totalEvaluate)
	private RatingBar totalEvaluate;
	@ViewInject(R.id.evaluate_star1)
	private RatingBar evaluate_star1;
	@ViewInject(R.id.evaluate_star2)
	private RatingBar evaluate_star2;

	private TextView tvBack;// 返回

	private TextView tvRequest;// 请愿
	private Intent intent;

	private String docId = "";// 医生id
	private String hosId = "";// 医院id
	private String deptId = "";// 科室id
	private String deptDocId = "";// 医生科室id
	private Doctor doc = null;
	private String attentId = "";// 关注主键ID
	private String sourcePfId = "355";// 平台来源id
	private int fensCount;// 粉丝数量
	private TelDocState state;// 电话医生状态
	// 2015-12-18 医院对接中
	private final int NORMAL = 1;
	private final int WARNING = 0;
	private int ehDockingStatus = NORMAL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctorindex);
		ViewUtils.inject(DoctorIndexActivity.this);
		tvRequest = loadTitleBar(true, "医生详情", "请愿");
		tvBack = (TextView) findViewById(R.id.left_btn);
		tvBack.setOnClickListener(this);

		tvRequest.setVisibility(View.GONE);
		tvRequest.setEnabled(false);
		tvAttent.setEnabled(false);
		tvRequest.setVisibility(View.INVISIBLE);
		tvRequest.setBackgroundResource(R.drawable.selector_main_btn_bg);
		tvRequest.setOnClickListener(this);
		docId = getIntent().getStringExtra("docId");
		deptId = getIntent().getStringExtra("deptId");
		deptDocId = getIntent().getStringExtra("deptDocId");
		// 2015-12-18 医院对接
		if (getIntent().hasExtra("ehDockingStatus")) {
			ehDockingStatus = getIntent()
					.getIntExtra("ehDockingStatus", NORMAL);
		}
		if (BaseApplication.getInstance().isNetConnected) {
			initData();
		} else {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 获取数据
	 */
	private void initData() {
		showProgressToast();
		// 医生详情
		// HashMap<String, Object> params = new HashMap<String, Object>();
		// params.put("deptid", deptId);
		// params.put("doctorid", docId);
		// params.put("deptdocid", deptDocId);
		// HospitalImpl impl = new HospitalImpl();

		RequestParams params = new RequestParams();
		params.addBodyParameter("deptid", deptId);
		params.addBodyParameter("doctorid", docId);
		params.addBodyParameter("deptdocid", deptDocId);
		HospitalImpl impl = new HospitalImpl();
		impl.getDocInfo(params, this);
		checkYnRemain(docId, deptId);
		checkTelDocState(docId);
		getUnEvaluateCount();
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkAttentState();
	}

	/**
	 * 初始化医生头像
	 */
	public void initDocPhoto(String photo) {
		// FinalBitmap fb = FinalBitmap.create(this);
		Bitmap defaultBit = BitmapFactory.decodeResource(getResources(),
				R.drawable.default_doc_img);
		BitmapUtils bitmapUtils = new BitmapUtils(DoctorIndexActivity.this);
		bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(
				mContext).scaleDown(3));
		bitmapUtils.configDefaultLoadingImage(defaultBit);
		bitmapUtils.configDefaultLoadFailedImage(defaultBit);
		bitmapUtils.display(imgHead, EZTConfig.DOC_PHOTO + photo);
		
//		bitmapUtils.display(imgHead,"http://images.eztcn.com.cn/images/doctor/ezt2.0/%E9%99%B6%E5%AE%B6%E9%A9%B9.jpg");
		
		// fb.display(imgHead, EZTConfig.DOC_PHOTO + photo, defaultBit,
		// defaultBit);
	}

	/**
	 * 请愿请求
	 */
	private void petitionDoc() {
		// HashMap<String, Object> params = new HashMap<String, Object>();
		// UserImpl impl = new UserImpl();
		// params.put("doctorId", docId);
		// params.put("serviceIds", 1);// 暂时写死，后期需改动

		RequestParams params = new RequestParams();
		UserImpl impl = new UserImpl();
		params.addBodyParameter("doctorId", docId);
		params.addBodyParameter("serviceIds", "" + 1);// 暂时写死，后期需改动
		impl.addPetition(params, this);
		showProgressToast();
	}

	/**
	 * 检测医生是否有号
	 */
	private void checkYnRemain(String docId, String deptId) {
		// HashMap<String, Object> params2 = new HashMap<String, Object>();
		// params2.put("deptId", deptId);
		// params2.put("doctorId", docId);
		// params2.put("pfId", sourcePfId);

		RequestParams params2 = new RequestParams();
		params2.addBodyParameter("deptId", deptId);
		params2.addBodyParameter("doctorId", docId);
		params2.addBodyParameter("pfId", sourcePfId);
		new HospitalImpl().ynRemain(params2, this);
	}
	/**
	 * 检测电话医生服务状态
	 * 
	 * @param docId
	 */
	private void checkTelDocState(String docId) {
		RequestParams param3 = new RequestParams();
		param3.addBodyParameter("doctorId", docId);
		new TelDocImpl().checkTelDocState(param3, this);
	}

	/**
	 * 检查关注状态
	 */
	private void checkAttentState() {
		if (BaseApplication.patient != null) {
			RequestParams params1 = new RequestParams();
			params1.addBodyParameter("userId",
					BaseApplication.patient.getUserId() + "");
			params1.addBodyParameter("deptDocId", deptDocId);
			new AttentionImpl().getAttentDocState(params1, this);
		}
	}

	/**
	 * 获取未评价数
	 */
	private void getUnEvaluateCount() {
		if (BaseApplication.patient != null) {
			RequestParams params = new RequestParams();
			params.addBodyParameter("userId",
					BaseApplication.patient.getUserId() + "");
			params.addBodyParameter("doctorId", docId);
			new RegistratioImpl().getUnEvaluateCount(params, this);
		}
	}

	@OnClick(R.id.attention)
	public void attentionClick(View v) {
		// 关注
		if (BaseApplication.getInstance().isNetConnected) {
			if ("+关注".equals(tvAttent.getText().toString().trim())) {
				if (BaseApplication.patient == null) {
					HintToLogin(Constant.LOGIN_COMPLETE);
					return;
				} else {
					RequestParams params = new RequestParams();
					params.addBodyParameter("contentId", docId);
					params.addBodyParameter("userId",
							BaseApplication.patient.getUserId() + "");

					new AttentionImpl().attentDoc(params, this);
				}
			} else {// 取消关注
				RequestParams params = new RequestParams();
				params.addBodyParameter("id", attentId);
				new AttentionImpl().cancelAttentDoc(params, this);

			}
			tvAttent.setEnabled(false);
			showProgressToast();
		} else {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
		}
	}

	@OnClick(R.id.goodAt)
	public void goodAtClick(View v) {
		intent = new Intent();
		// 擅长
		intent.setClass(this, DoctorInfoActivity.class);
		if (doc != null) {
			intent.putExtra("docGoodAt", doc.getDocGoodAt());
			intent.putExtra("docIntro", doc.getDocIntro());
			// intent.putExtra("docEducBg", doc.getDocEducBg());
			intent.putExtra("docSuc", doc.getDocAcademicSuc());
		}
		startActivity(intent);
	}

	@OnClick(R.id.order)
	public void orderClick(View v) {
		intent = new Intent();
		// 预约挂号
		if (!BaseApplication.getInstance().isNetConnected) {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (BaseApplication.patient != null) {
			if (TextUtils.isEmpty(BaseApplication.patient.getEpMobile())) {
				hintPerfectInfo("请完善个人手机号！", 2);

			} else {
				double distance = MapUtil.calculatePath(doc.getHosLat(),
						doc.getHosLon());
				if (hosId.equals("83") && distance > 30 * 1000) {// 判断是否是深圳儿童医院，并且距离是否>30公里
					intent = null;
					hintLargeDistance();
					return;
				} else {
					intent.setClass(this, OrderRegistrationActivity.class);
					intent.putExtra("pos", 0);
					intent.putExtra("deptid", deptId);
					intent.putExtra("doctorid", docId);
					intent.putExtra("hosId", hosId);
					intent.putExtra("hosName", tvHos.getText().toString());
					intent.putExtra("docName", tvName.getText().toString());
				}
			}
		} else {
			HintToLogin(Constant.LOGIN_COMPLETE);
			return;
		}

		if (intent != null) {
			startActivity(intent);
		}
	}

	@OnClick(R.id.doctorPhone)
	public void doctorPhoneClick(View v) {
		// 电话医生
		if (BaseApplication.patient == null) {
			HintToLogin(Constant.LOGIN_COMPLETE);
			return;
		}
		if (doc != null) {
			startActivity(new Intent(mContext, PhoneDoctorActivity.class)
					.putExtra("doc", doc));
		}
	}

	@OnClick(R.id.seeIllRecord)
	public void seeIllRecordClick(View v) {
		intent = new Intent();

		// 专家看病历

		if (BaseApplication.patient == null) {
			HintToLogin(Constant.LOGIN_COMPLETE);
			return;
		}
		intent.setClass(this, SeeIllRecordActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.pic_text_inquiry_tv)
	public void inquiryClick(View v) {
		// 图文问诊
		Toast.makeText(mContext, getString(R.string.function_hint),
				Toast.LENGTH_SHORT).show();
	}

	@OnClick(R.id.totalEvaLayout)
	public void totalEvaLayoutClick(View v) {
		intent = new Intent();

		// 综合评价
		intent.setClass(this, UserEvaluateListActivity.class);
		intent.putExtra("doctorId", docId);
		if (doc != null) {
			intent.putExtra("doctorName", doc.getDocName() + "");
		}

		startActivity(intent);
	}

	@OnClick(R.id.evaluateLayout)
	public void evaluateLayoutClick(View v) {
	}

	@OnClick(R.id.thankLetter)
	public void thankLetterClick(View v) {
		intent = new Intent();
		// 感谢信
		intent.setClass(this, ThanksLetterListActivity.class);
		intent.putExtra("doctorId", docId);
		startActivity(intent);
	}

	@OnClick(R.id.doctorEssay)
	public void doctorEssayClick(View v) {
		// 医生博文
		// intent.setClass(this, DoctorEssayActivity.class);
		Toast.makeText(mContext, getString(R.string.function_hint),
				Toast.LENGTH_SHORT).show();
		intent = null;
	}

	public void onClick(View v) {
		intent = new Intent();
		switch (v.getId()) {
		case R.id.left_btn:// 返回按钮
			onBackPressed();
			intent = null;
			break;
		}
		if (v == tvRequest) {
			// 请愿
			if (!BaseApplication.getInstance().isNetConnected) {
				Toast.makeText(mContext, getString(R.string.network_hint),
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (BaseApplication.patient == null) {
				HintToLogin(Constant.LOGIN_COMPLETE);
				return;
			}
			hintDialog();
			intent = null;
		}
	}
	@Override
	public void onBackPressed() {
		setResult(11);
		finish();
		super.onBackPressed();
	}

	/**
	 * 请愿弹出框
	 */
	private void hintDialog() {
		String docName = tvName.getText().toString();
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setIcon(android.R.drawable.ic_dialog_info).setTitle("提示")
				.setMessage("确定请求" + docName + "医生开通电话医生服务？")
				.setCancelable(false)
				.setNegativeButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						petitionDoc();
					}
				}).setPositiveButton("取消", null);

		AlertDialog dialog = builder.create();
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
		dialog.show();
	}

	/**
	 * 温馨提示（针对儿童医院，距离>30公里）
	 */
	public void hintLargeDistance() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setIcon(android.R.drawable.ic_dialog_info).setTitle("提示")
				.setMessage("您的行程已超出30公里外，将错过就诊时间，请预约其他时间段")
				.setCancelable(false)
				.setNegativeButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});

		AlertDialog dialog = builder.create();
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
		dialog.show();
	}

	/**
	 * 提醒完善信息
	 */
	private void hintPerfectInfo(String hint, final int type) {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setIcon(android.R.drawable.ic_dialog_info).setTitle("提示")
				.setMessage(hint).setCancelable(false)
				.setNegativeButton("完善", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						if (type == 1) {// 完善个人信息
							mContext.startActivity(new Intent(mContext,
									AutonymAuthActivity.class));
						} else {// 完善个人手机号
							mContext.startActivity(new Intent(mContext,
									ModifyPhoneActivity.class));
						}

					}
				}).setPositiveButton("取消", null);

		AlertDialog dialog = builder.create();
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
		dialog.show();

	}

	/**
	 * 判断用户信息完善度
	 */
	public boolean judgeUserInfo() {
		if (TextUtils.isEmpty(BaseApplication.patient.getEpHiid())) {
			return false;
		}
		return true;
	}

	// 标记访问接口是否回调(成功+1)
	private int backCoumn = 0;

	// 未评价数
	private String count = "0";

	// 医生是否有号
	private int isHaveNum = -1;

	// 获取关注状态
	private boolean isAttent = false;

	@Override
	public void result(Object... object) {
		if (object == null) {
			hideProgressToast();
			return;
		}
		int type = (Integer) object[0];
		boolean isSucc = (Boolean) object[1];
		switch (type) {
		case HttpParams.ADD_PETITION:// 请愿

			if (isSucc) {
				Map<String, Object> petitionMap = (Map<String, Object>) object[2];
				if (petitionMap != null) {
					boolean flag = (Boolean) petitionMap.get("flag");
					if (flag) {
						Toast.makeText(mContext, "请愿成功！", Toast.LENGTH_SHORT)
								.show();
						tvRequest.setText("已请愿");
						tvRequest.setEnabled(false);
						tvRequest.setOnClickListener(null);
					} else {
						Toast.makeText(mContext, "请愿失败，请稍后重试！",
								Toast.LENGTH_SHORT).show();
						Logger.i("请愿失败--", petitionMap.get("msg"));
					}
				} else {
					Toast.makeText(mContext, getString(R.string.service_error),
							Toast.LENGTH_SHORT).show();
				}

			} else {
				Toast.makeText(mContext, object[3].toString(),
						Toast.LENGTH_SHORT).show();
			}
			hideProgressToast();
			break;

		case HttpParams.GET_DOC_INFO:// 获取医生信息
			backCoumn++;
			if (isSucc) {// 成功
				doc = (Doctor) object[2];
			} else {
				Logger.i("获取医生信息", object[3]);
			}

			break;

		case HttpParams.ATTENT_DOC:// 关注医生
			hideProgressToast();
			tvAttent.setEnabled(true);
			if (isSucc) {
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map != null) {
					boolean flag = (Boolean) map.get("flag");
					if (flag) {// 成功
						attentId = (String) map.get("id");
						tvAttent.setTextColor(getResources().getColor(
								R.color.dark_gray));

						tvAttent.setBackgroundResource(R.drawable.shape_border_small);
						tvAttent.setTextSize(ResourceUtils.getXmlDef(mContext,
								R.dimen.small_size));
						tvAttent.setText("取消关注");
						tvFans.setText(++fensCount + "");
					} else {
						Toast.makeText(mContext, map.get("msg").toString(),
								Toast.LENGTH_SHORT).show();
					}
				}

			} else {
				Toast.makeText(mContext, object[3].toString(),
						Toast.LENGTH_SHORT).show();
			}

			return;

		case HttpParams.CANCEL_ATTENT_DOC:// 取消关注
			hideProgressToast();
			tvAttent.setEnabled(true);
			if (isSucc) {
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map != null && map.size() != 0) {
					boolean flag = (Boolean) map.get("flag");
					if (flag) {// 成功
						tvAttent.setTextColor(getResources().getColor(
								R.color.light_main_color));
						tvAttent.setBackgroundResource(R.drawable.shape_main_color_broder_bg);
						tvAttent.setTextSize(ResourceUtils.getXmlDef(mContext,
								R.dimen.medium_size));
						tvAttent.setText("+关注");
						if (fensCount > 0) {
							tvFans.setText(--fensCount + "");
						}
					} else {
						Toast.makeText(mContext, map.get("msg").toString(),
								Toast.LENGTH_SHORT).show();
					}
				}

			} else {
				Toast.makeText(mContext, object[3].toString(),
						Toast.LENGTH_SHORT).show();
			}
			return;

		case HttpParams.GET_ATTENT_DOC_STATE:// 获取关注状态
			backCoumn++;
			if (isSucc) {
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map != null) {
					isAttent = (Boolean) map.get("flag");
					if (isAttent)
						attentId = (String) map.get("id");
				}

			} else {
				Logger.i("获取关注状态", object[3]);
			}
			break;

		case HttpParams.YN_REMAIN:// 医生是否有号
			backCoumn++;
			if (isSucc) {
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map != null) {
					boolean flag = (Boolean) map.get("flag");
					if (flag) {// 成功
						isHaveNum = Integer.parseInt(map.get("isHaveNum")
								.toString());

					} else {
						Logger.i("医生是否有号", map.get("msg"));
					}
				}

			} else {
				Logger.i("医生是否有号", object[3]);
			}
			break;

		case HttpParams.CHECK_TEL_DOC_STATE:// 获取电话医生状态
			backCoumn++;
			if (isSucc) {
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map != null) {
					boolean flag = (Boolean) map.get("flag");
					if (flag) {// 成功
						state = (TelDocState) map.get("data");
					} else {
						Logger.i("获取电话医生状态", map.get("msg"));
					}
				}

			} else {
				Logger.i("获取电话医生状态", object[3]);
			}
			break;
		case HttpParams.UNEVALUATE_COUNT:// 未评价数
			backCoumn++;
			Map<String, Object> map = null;
			if (isSucc) {
				map = (Map<String, Object>) object[2];
			}
			if (map == null || map.size() == 0) {
				return;
			}
			boolean flag = (Boolean) map.get("flag");
			if (!flag) {
				return;
			}
			count = map.get("data").toString();

			break;
		}

		if (BaseApplication.patient != null) {
			if (backCoumn == 5) {
				tvAttent.setEnabled(true);
				// 2015-12-18 医院对接
				if (ehDockingStatus == WARNING) {// 标注灰色 且不可点击
					order.setText("预约挂号");
					order.setTextColor(getResources().getColor(
							R.color.dark_gray2));

					Drawable draw = getResources().getDrawable(
							R.drawable.ic_visit_form_gray);
					draw.setBounds(0, 0, draw.getMinimumWidth(),
							draw.getMinimumHeight());
					order.setCompoundDrawables(null, draw, null, null);
					order.setCompoundDrawablePadding(5);
					order.setEnabled(false);

					doctorPhone.setText("电话医生");
					doctorPhone.setTextColor(getResources().getColor(
							R.color.dark_gray2));

					doctorPhone.setCompoundDrawables(null, draw, null, null);

					doctorPhone.setEnabled(false);

				} else {

					/**
					 * 电话医生状态
					 */
					if (state != null) {
						switch (state.getYnOnline()) {
						case -1:// 未开通
						case 0:// 没在线
							doctorPhone.setText("电话医生");
							doctorPhone.setTextColor(getResources().getColor(
									R.color.dark_gray2));

							Drawable draw = getResources().getDrawable(
									R.drawable.ic_tel2_gray);
							draw.setBounds(0, 0, draw.getMinimumWidth(),
									draw.getMinimumHeight());
							doctorPhone.setCompoundDrawables(null, draw, null,
									null);

							doctorPhone.setEnabled(false);
							break;

						case 1:// 在线
							doctorPhone.setText("电话医生");
							doctorPhone.setTextColor(getResources().getColor(
									R.color.light_main_color));

							Drawable draw1 = getResources().getDrawable(
									R.drawable.ic_tel2);
							draw1.setBounds(0, 0, draw1.getMinimumWidth(),
									draw1.getMinimumHeight());
							doctorPhone.setCompoundDrawables(null, draw1, null,
									null);

							doctorPhone.setEnabled(true);
							break;
						}
						if (state.getYnOnline() == 1) {// 判断请愿状态
							tvRequest.setVisibility(View.GONE);
						} else {
							tvRequest.setVisibility(View.GONE);// 暂时隐藏
							tvRequest.setEnabled(true);
						}
					} else {
						tvRequest.setVisibility(View.GONE);// 暂时隐藏
						tvRequest.setEnabled(true);
					}

					/**
					 * 未评价数
					 */
					tvUnECount.setText("(" + count + ")");

					/**
					 * 医生是否有号
					 */
					switch (isHaveNum) {// -1 没放号，0为预约已满，1有号
					case -1:
						order.setText("预约挂号");
						order.setTextColor(getResources().getColor(
								R.color.dark_gray2));

						Drawable draw = getResources().getDrawable(
								R.drawable.ic_visit_form_gray);
						draw.setBounds(0, 0, draw.getMinimumWidth(),
								draw.getMinimumHeight());
						order.setCompoundDrawables(null, draw, null, null);
						order.setCompoundDrawablePadding(5);
						order.setEnabled(false);
						break;

					case 0:
						order.setText("预约已满");
						order.setTextColor(Color.RED);

						Drawable draw1 = getResources().getDrawable(
								R.drawable.ic_visit_form_red);
						draw1.setBounds(0, 0, draw1.getMinimumWidth(),
								draw1.getMinimumHeight());
						order.setCompoundDrawables(null, draw1, null, null);

						order.setEnabled(false);
						break;
					case 1:
						order.setText("预约挂号");
						order.setTextColor(getResources().getColor(
								R.color.light_main_color));
						Drawable draw2 = getResources().getDrawable(
								R.drawable.ic_visit_form);
						draw2.setBounds(0, 0, draw2.getMinimumWidth(),
								draw2.getMinimumHeight());
						order.setCompoundDrawables(null, draw2, null, null);

						order.setEnabled(true);
						break;
					}
				}
				/**
				 * 获取关注状态
				 */

				if (isAttent) {// 成功
					tvAttent.setBackgroundResource(R.drawable.shape_border_small);
					tvAttent.setTextSize(ResourceUtils.getXmlDef(mContext,
							R.dimen.small_size));
					tvAttent.setTextColor(getResources().getColor(
							R.color.dark_gray));
					tvAttent.setText("取消关注");

				} else {
					tvAttent.setBackgroundResource(R.drawable.shape_main_color_broder_bg);
					tvAttent.setTextSize(ResourceUtils.getXmlDef(mContext,
							R.dimen.medium_size));
					tvAttent.setTextColor(getResources().getColor(
							R.color.light_main_color));
					tvAttent.setText("+关注");
				}

				/**
				 * 医生信息
				 */
				if (doc != null) {

					tvName.setText(doc.getDocName());
					hosId = doc.getDocHosId();
					initDocPhoto(doc.getDocHeadUrl());
					if (doc.getDocPosition() != null) {
						String strLevel = EztDictionaryDB.getInstance(mContext)
								.getLabelByTag("doctorLevel",
										doc.getDocPosition());

						tvPosition.setText(strLevel);
					}
					tvHos.setText(doc.getDocHos());
					tvDept.setText(doc.getDocDept());
					try {
						fensCount = Integer.parseInt(doc.getDocFans());
					} catch (Exception e) {

					}
					if (fensCount < 0) {
						fensCount = 0;
					}
					tvFans.setText(fensCount + "");
					tvRate.setText(doc.getDocRate() == null ? "0" : StringUtil
							.getTwoRadixPoint(Double.parseDouble(doc
									.getDocRate()) * 100)
							+ "%");
					tvPutNums.setText(doc.getDocAllocaeNum() + "/周");

					String str = "擅长：" + doc.getDocGoodAt();
					SpannableStringBuilder style = new SpannableStringBuilder(
							str);
					style.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 2,
							Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					style.setSpan(
							new AbsoluteSizeSpan(ResourceUtils.getXmlDef(
									mContext, R.dimen.small_size), true), 0, 2,
							Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

					tvGoodAt.setText(TextUtils.isEmpty(doc.getDocGoodAt()) ? "暂无擅长"
							: style);
					// tvUnECount.setText(doc.getDocUnEvaluateNum() + "");
					totalEvaluate.setRating(doc.getDocOverallMerit());
					evaluate_star1.setRating(doc.getDocResult());
					evaluate_star2.setRating(doc.getDocServiceAttitude());
				} else {
				}
				hideProgressToast();
			}

		} else {

			if (backCoumn == 3) {
				tvAttent.setEnabled(true);

				// 2015-12-18 医院对接
				if (ehDockingStatus == WARNING) {// 标注灰色 且不可点击
					order.setText("预约挂号");
					order.setTextColor(getResources().getColor(
							R.color.dark_gray2));

					Drawable draw = getResources().getDrawable(
							R.drawable.ic_visit_form_gray);
					draw.setBounds(0, 0, draw.getMinimumWidth(),
							draw.getMinimumHeight());
					order.setCompoundDrawables(null, draw, null, null);
					order.setCompoundDrawablePadding(5);
					order.setEnabled(false);

					doctorPhone.setText("电话医生");
					doctorPhone.setTextColor(getResources().getColor(
							R.color.dark_gray2));

					doctorPhone.setCompoundDrawables(null, draw, null, null);

					doctorPhone.setEnabled(false);

				} else {
					/**
					 * 电话医生状态
					 */
					if (state != null) {

						switch (state.getIsOpenService()) {
						case -1:// 未开通
							doctorPhone.setText("电话医生");
							doctorPhone.setTextColor(getResources().getColor(
									R.color.dark_gray2));

							Drawable draw = getResources().getDrawable(
									R.drawable.ic_tel2_gray);
							draw.setBounds(0, 0, draw.getMinimumWidth(),
									draw.getMinimumHeight());
							doctorPhone.setCompoundDrawables(null, draw, null,
									null);

							doctorPhone.setEnabled(false);
							break;

						case 0:// 没在线
						case 1:// 在线
							tvRequest.setVisibility(View.GONE);
							doctorPhone.setText("电话医生");
							doctorPhone.setTextColor(getResources().getColor(
									R.color.light_main_color));

							Drawable draw1 = getResources().getDrawable(
									R.drawable.ic_tel2);
							draw1.setBounds(0, 0, draw1.getMinimumWidth(),
									draw1.getMinimumHeight());
							doctorPhone.setCompoundDrawables(null, draw1, null,
									null);

							doctorPhone.setEnabled(true);
							break;
						}
					}

					/**
					 * 医生是否有号
					 */
					switch (isHaveNum) {// -1 没放号，0为预约已满，1有号
					case -1:
						order.setText("预约挂号");
						order.setTextColor(getResources().getColor(
								R.color.dark_gray2));

						Drawable draw = getResources().getDrawable(
								R.drawable.ic_visit_form_gray);
						draw.setBounds(0, 0, draw.getMinimumWidth(),
								draw.getMinimumHeight());
						order.setCompoundDrawables(null, draw, null, null);
						order.setCompoundDrawablePadding(5);
						// order.setEnabled(false);
						break;

					case 0:
						order.setText("预约已满");
						order.setTextColor(Color.RED);

						Drawable draw1 = getResources().getDrawable(
								R.drawable.ic_visit_form_red);
						draw1.setBounds(0, 0, draw1.getMinimumWidth(),
								draw1.getMinimumHeight());
						order.setCompoundDrawables(null, draw1, null, null);

						// order.setEnabled(false);
						break;
					case 1:
						order.setText("预约挂号");
						order.setTextColor(getResources().getColor(
								R.color.light_main_color));
						Drawable draw2 = getResources().getDrawable(
								R.drawable.ic_visit_form);
						draw2.setBounds(0, 0, draw2.getMinimumWidth(),
								draw2.getMinimumHeight());
						order.setCompoundDrawables(null, draw2, null, null);

						order.setEnabled(true);
						break;
					}
				}

				/**
				 * 医生信息
				 */
				if (doc != null) {
					tvName.setText(doc.getDocName());
					hosId = doc.getDocHosId();
					initDocPhoto(doc.getDocHeadUrl());
					if (doc.getDocPosition() != null) {
						String strLevel = EztDictionaryDB.getInstance(mContext)
								.getLabelByTag("doctorLevel",
										doc.getDocPosition());

						tvPosition.setText(strLevel);
					}
					tvHos.setText(doc.getDocHos());
					tvDept.setText(doc.getDocDept());
					try {
						fensCount = Integer.parseInt(doc.getDocFans());
					} catch (Exception e) {

					}
					if (fensCount < 0) {
						fensCount = 0;
					}
					tvFans.setText(fensCount + "");
					tvRate.setText(doc.getDocRate() == null ? "0" : StringUtil
							.getTwoRadixPoint(Double.parseDouble(doc
									.getDocRate()) * 100)
							+ "%");
					tvPutNums.setText(doc.getDocAllocaeNum() + "/周");

					String str = "擅长：" + doc.getDocGoodAt();
					SpannableStringBuilder style = new SpannableStringBuilder(
							str);
					style.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 2,
							Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					style.setSpan(
							new AbsoluteSizeSpan(ResourceUtils.getXmlDef(
									mContext, R.dimen.small_size), true), 0, 2,
							Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

					tvGoodAt.setText(TextUtils.isEmpty(doc.getDocGoodAt()) ? "暂无擅长"
							: style);
					// tvUnECount.setText(doc.getDocUnEvaluateNum() + "");
					totalEvaluate.setRating(doc.getDocOverallMerit());
					evaluate_star1.setRating(doc.getDocResult());
					evaluate_star2.setRating(doc.getDocServiceAttitude());
				} else {
				}
				hideProgressToast();
			}
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == 222) {
			checkAttentState();
		}
	}
}
