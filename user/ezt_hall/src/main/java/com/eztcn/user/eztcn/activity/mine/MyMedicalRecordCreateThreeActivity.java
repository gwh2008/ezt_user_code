package com.eztcn.user.eztcn.activity.mine;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.fireking.app.imagelib.entity.ImageBean;
import org.fireking.app.imagelib.widget.PicSelectActivity;
import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.CreateEMRAdapter;
import com.eztcn.user.eztcn.adapter.CreateEMRAdapter.onAddClickListener;
import com.eztcn.user.eztcn.adapter.PopupWindowAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.EztDictionary;
import com.eztcn.user.eztcn.bean.MedicalRecord;
import com.eztcn.user.eztcn.bean.MedicalRecord_ImgType;
import com.eztcn.user.eztcn.bean.Medical_img;
import com.eztcn.user.eztcn.customView.SelectPicPopupWindow;
import com.eztcn.user.eztcn.db.EztDictionaryDB;
import com.eztcn.user.eztcn.impl.FileUploadImpl;
import com.eztcn.user.eztcn.impl.MedicalRecordImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
/**
 * @title 创建病历（3步）
 * @describe
 * @author ezt
 * @created 2015年1月22日
 */
public class MyMedicalRecordCreateThreeActivity extends FinalActivity implements
		OnClickListener, onAddClickListener, IHttpResult,
		OnItemLongClickListener {

	@ViewInject(R.id.title_tv)
	private TextView title_tv;

	private TextView left_btn;// 返回按钮
	private TextView addType;// 添加类型
	@ViewInject(R.id.imgsList)
	// , itemClick = "onItemClick"
	private ListView imgsList;
	@ViewInject(R.id.complete)
	// , click = "onClick"
	private Button complete;// 上传

	private PopupWindow pWindow;
	private ListView itemList;
	private List<EztDictionary> dicList;// 字典表——病历类型
	private PopupWindowAdapter adapter;

	private String mId;// 病历id

	private MedicalRecord record;
	private List<Medical_img> list;
	private CreateEMRAdapter imgAdapter;

	// 自定义的底部弹出框类（用于图片选择）
	private SelectPicPopupWindow menuWindow;
	private int selectLine;// 选定的行数
	private int deletePosition;// 要操作的图片位置
	private int operate;// 0、添加 1、修改

	private int enterType = 0;// 判断是创建还是编辑

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createemr_uploadimg);
		ViewUtils.inject(MyMedicalRecordCreateThreeActivity.this);
		addType = loadTitleBar(true, "创建病历", "添加");
		addType.setOnClickListener(this);
		left_btn = (TextView) findViewById(R.id.left_btn);
		left_btn.setOnClickListener(this);
		list = new ArrayList<Medical_img>();
		configPopupWindow();
		initTypeList();
		init();
		if (enterType == 0) {
			title_tv.setText("创建病历");
		} else {
			title_tv.setText("编辑病历");
		}
		imgAdapter = new CreateEMRAdapter(mContext);
		imgsList.setAdapter(imgAdapter);
		// imgAdapter.setList(list);
		imgAdapter.setOnAddListener(this);
		imgsList.setOnItemLongClickListener(this);
	}

	public void init() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			record = (MedicalRecord) bundle.getSerializable("record");
			enterType = bundle.getInt("enterType");
			if (record == null) {
				mId = bundle.getString("mId");
			} else {
				mId = record.getId();
			}
		}
		if (record != null) {
			list = record.getImgList();
		} else {
			list = new ArrayList<Medical_img>();
		}

	}

	@OnClick(R.id.complete)
	private void completeClick(View v) {
		if (judgeHaveImg()) {
			v.setClickable(false);
			showProgressToast();
			upThread = new UploadThread();
			upThread.start();
		} else {
			Toast.makeText(getApplicationContext(), "无可上传文件",
					Toast.LENGTH_SHORT).show();
			return;
		}
	}

	@Override
	public void onClick(View v) {
		if (v == addType) {// 添加类型
			if (dicList == null || dicList.size() == 0) {
				Toast.makeText(getApplicationContext(), "类型添加已满",
						Toast.LENGTH_SHORT).show();
				return;
			}
			createImgList(dicList.get(0).getLabel(), dicList.get(0).getValue());
		} else if (v == complete) {// 上传
		// if (judgeHaveImg()) {
		// upThread = new UploadThread();
		// upThread.start();
		// } else {
		// Toast.makeText(getApplicationContext(), "无可上传文件",
		// Toast.LENGTH_SHORT).show();
		// return;
		// }

		} else if (v == left_btn) {
			Intent intent = new Intent(this, MyMedicalRecordListActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}

	/**
	 * 初始化上传类型列表
	 */
	public void initTypeList() {
		if (list == null) {
			return;
		}
		dicList = EztDictionaryDB.getInstance(getApplicationContext())
				.getDictionaryList("mrPicType");
		if (dicList == null) {
			return;
		}
		List<String> emrType = new ArrayList<String>();
		String type;
		String type2;
		for (int i = 0; i < list.size(); i++) {
			type = list.get(i).getRecordType();
			for (int j = 0; j < dicList.size(); j++) {
				type2 = dicList.get(j).getValue();
				if (type2.equals(type)) {
					dicList.remove(j);
				}
			}
		}
		for (int i = 0; i < dicList.size(); i++) {
			emrType.add(dicList.get(i).getLabel());
		}
		adapter.setList(emrType);
	}

	/**
	 * 选择图
	 */
	// private void dialogChoicePic(View v) {
	// menuWindow = new SelectPicPopupWindow(mContext, new ChoiceImgClick());
	// // 显示窗口
	// menuWindow.showAtLocation(v,
	// Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	//
	// }

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
		startActivityForResult(intent, 0x123);

	}

	// /**
	// * 选择图片
	// */
	// public class ChoiceImgClick implements OnClickListener {
	//
	// @Override
	// public void onClick(View v) {
	// switch (v.getId()) {
	// case R.id.btn_take_photo:// 拍照
	// openCamera(11);
	// break;
	// case R.id.btn_pick_photo:// 从相册选择
	// openImgLibrary(22);
	// break;
	// default:// 取消
	// break;
	// }
	// if (menuWindow != null) {
	// menuWindow.dismiss();
	// }
	// }
	//
	// }

	/**
	 * 添加/修改一张图片
	 * 
	 * @param url
	 */
	public void addImages(String url) {
		Medical_img mImg = list.get(selectLine);
		List<MedicalRecord_ImgType> il = mImg.getUrlList();
		MedicalRecord_ImgType imgType;
		imgType = new MedicalRecord_ImgType();
		if (operate == 0) {
			imgType.setImgUrl(url);
			il.add(imgType);
		} else {
			imgType = il.get(deletePosition);
			String temp = imgType.getImgUrl();
			if (temp.contains("http")) {
				deleteFormHttp(imgType.getId());
			}
			imgType.setImgUrl(url);
		}
		mImg.setUrlList(il);
		imgAdapter.notifyDataSetChanged();
	}

	/**
	 * 修改服务器图片
	 */
	// public void modifyFromHttp() {
	// Medical_img mImg = list.get(selectLine);
	// List<MedicalRecord_ImgType> il = mImg.getUrlList();
	// MedicalRecord_ImgType imgType;
	//
	// mImg.setUrlList(il);
	// imgAdapter.notifyDataSetChanged();
	// }

	/**
	 * 删除一张图片
	 * 
	 * @param position
	 */
	public void removeImages() {
		Medical_img mImg = list.get(selectLine);
		List<MedicalRecord_ImgType> il = mImg.getUrlList();
		il.remove(deletePosition);
		if (mImg.getUrlList().size() == 0) {
			list.remove(selectLine);
		}
		imgAdapter.notifyDataSetChanged();
	}

	/**
	 * 删除服务器图片
	 */
	public void deleteFormHttp(int picId) {
		// HashMap<String, Object> params = new HashMap<String, Object>();
		RequestParams params = new RequestParams();
		params.addBodyParameter("picId", picId + "");
		new MedicalRecordImpl().delIllRecordImg(params, this);
		showProgressToast();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// 拍照或图库返回结果
		if (data == null) {
			Toast.makeText(mContext, "获取图片失败", Toast.LENGTH_LONG).show();
			return;
		}
		if (requestCode == 0x123 && resultCode == RESULT_OK) {
			@SuppressWarnings("unchecked")
			List<ImageBean> images = (List<ImageBean>) data
					.getSerializableExtra("images");
			String strPath;
			for (int i = 0; i < images.size(); i++) {
				strPath = images.get(i).path;
				// Bitmap bip = BitmapFactory.decodeFile(strPath);
				addImages(strPath);
			}
		}

		// // 拍照或图库返回结果
		// if (requestCode == 11 || requestCode == 22) {// 身份证
		// Bitmap bitmap = getBitmapFromImage(data);
		// if (bitmap == null)
		// Toast.makeText(mContext, "获取图片失败", Toast.LENGTH_LONG).show();
		// else {
		// // photo.setImageBitmap(bitmap);
		// Bitmap newBitmap = PictureUtils.imageZoom(bitmap, 200);
		// File photoFile = CommonUtil.saveImgToSdcard(
		// newBitmap,
		// SDCardUtil.getDirectory(EZTConfig.RES_SAVE_DIR
		// + File.separator + EZTConfig.DIR_SD_CARD_IMG),
		// System.currentTimeMillis() + ".jpg");
		// addImages(photoFile.getAbsolutePath());
		// }
		// }

	}

	/**
	 * 添加类型创建图片列表
	 * 
	 * @param type
	 *            上传类型名称
	 * @param tId
	 *            上传类型id
	 */
	public void createImgList(String type, String tId) {
		Medical_img img = new Medical_img();
		img.setTypeName(type);
		img.setRecordType(tId);
//		if (list.size() <= 3) {
			list.add(img);
			imgAdapter.setList(list);
			initTypeList();
//		} else {
//			Toast.makeText(MyMedicalRecordCreateThreeActivity.this, "类型添加已满",
//					Toast.LENGTH_SHORT).show();
//		}
	}

	UploadThread upThread; 

	class UploadThread extends Thread {

		@Override
		public void run() {
			super.run();
			uploadFile();
		}
	}

	/**
	 * 上传图片到服务器
	 * 
	 * @param imageFile
	 *            包含路径
	 */
	public void uploadFile() {
		try {
			// 请求普通信息
			Map<String, String> params = new HashMap<String, String>();
			List<File> files = null;
			File formfile;
			List<MedicalRecord_ImgType> urlList;
			String fileName;
			int temp = 0;
			boolean canupload=true;
			for (int i = 0; i < list.size(); i++) {
				Medical_img medical = list.get(i);
				urlList = medical.getUrlList();
				int size = urlList.size();
				files = new ArrayList<File>();
				for (int j = 0; j < size; j++) {
					fileName = urlList.get(j).getImgUrl();
					if (fileName.contains("http")) {
						canupload=false;
						continue;
					}else{
						canupload=true;
					}
					formfile = new File(fileName);
					files.add(formfile);
				}
				if (files.size() == 0) {
					canupload=false;
					continue;
				}else{
					canupload=true;
				}
					params.put("type", medical.getRecordType() + "");
					params.put("mId", mId);
					new FileUploadImpl().upLoadFiles(null, params, files,
							MyMedicalRecordCreateThreeActivity.this);
//				handler.sendEmptyMessage(0);
			}
			if(!canupload){
				handler.sendEmptyMessage(1);//2016-1-4修复病历没有做修改后滚动条旋转bug
				findViewById(R.id.complete).setClickable(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	Handler handler = new Handler() {

		@SuppressLint("NewApi") @Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				if (!MyMedicalRecordCreateThreeActivity.this.isDestroyed()) {
					showProgressToast();
				}
			} else {
				hideProgressToast();
			}
		}
	};

	/**
	 * 判断是否有图片可上传
	 */
	public boolean judgeHaveImg() {
		if (list == null || list.size() == 0) {
			return false;
		}
		List<MedicalRecord_ImgType> urlList;
		for (int i = 0; i < list.size(); i++) {
			Medical_img medical = list.get(i);
			urlList = medical.getUrlList();
			if (urlList.size() > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 上传类型窗口
	 */
	public void configPopupWindow() {
		View view = LayoutInflater.from(this).inflate(
				R.layout.popwindow_choice, null);
		itemList = (ListView) view.findViewById(R.id.pop_list);
		adapter = new PopupWindowAdapter(this);
		itemList.setAdapter(adapter);
		pWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, false);
		pWindow.setOutsideTouchable(true);
		pWindow.setFocusable(true);
		pWindow.setBackgroundDrawable(new BitmapDrawable());
		itemList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// createImgList(dicList.get(position).getLabel(),
				// dicList.get(position).getValue());
				Medical_img mg = imgAdapter.getList().get(selectLine);
				mg.setTypeName(dicList.get(position).getLabel());
				mg.setRecordType(dicList.get(position).getValue());
				imgAdapter.notifyDataSetChanged();
				pWindow.dismiss();
				initTypeList();
			}
		});
	}

	/**
	 * 显示操作view(修改，删除，取消)
	 */
	public void showOperateView(final View v) {
		String[] arrays = { "修改", "删除", "取消" };
		AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
		ab.setItems(arrays, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					operate = 1;
					selectImg(false);
					break;
				case 1:
					Medical_img mImg = list.get(selectLine);
					List<MedicalRecord_ImgType> il = mImg.getUrlList();
					if (il.get(deletePosition).getImgUrl().contains("http")) {
						deleteFormHttp(il.get(deletePosition).getId());
					} else {
						removeImages();
					}
					break;
				default:

					break;
				}
			}
		});
		ab.create().show();
	}

	/**
	 * 删除类型(及该类型中所有图片)
	 */
	public void delTypeView(final int position) {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("删除操作");
		ab.setMessage("是否删除该类型所有图片？");
		ab.setIcon(android.R.drawable.ic_delete);
		ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Medical_img mg = list.get(position);
				if (mg.getUrlList().size() == 0) {
					list.remove(position);
					imgAdapter.notifyDataSetChanged();
					if (dicList != null) {
						initTypeList();
					}
				}
			}
		});
		ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		ab.create().show();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// delTypeView(position);
		return false;
	}

	/**
	 * 选择图片类型
	 */
	@Override
	public void selectType(View view, int position) {
		selectLine = position;
		pWindow.showAsDropDown(view, view.getWidth(), 0);
	}

	/**
	 * 添加图片
	 */
	@Override
	public void addClick(int position) {
		operate = 0;
		selectLine = position;
		selectImg(true);
	}

	/**
	 * 删除图片
	 */
	@Override
	public void deleteClick(View view, int line, int position) {
		selectLine = line;
		deletePosition = position;
		showOperateView(view);
	}

	/**
	 * 服务器返回信息
	 * 
	 * @param object
	 */
	@SuppressLint("NewApi") @Override
	public void result(Object... object) {
//		hideProgressToast();
		handler.sendEmptyMessage(1);
		if (object == null) {
			return;
		}
		Object[] obj = object;
		Integer taskID = (Integer) obj[0];
		if (taskID == null) {
			return;
		}
		boolean status = (Boolean) obj[1];
		if (!status) {
			Toast.makeText(mContext, getString(R.string.service_error),
					Toast.LENGTH_SHORT).show();
			return;
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) obj[2];
		if (map == null || map.size() == 0) {
			return;
		}
		boolean bool = false;
		if (map.get("flag") != null) {
			bool = (Boolean) map.get("flag");
			if (!bool) {
				Toast.makeText(mContext, getString(R.string.request_fail),
						Toast.LENGTH_SHORT).show();
				return;
			}
		}
		if (taskID == HttpParams.CREATE_EMR_UPLOAD) {
			handler.sendEmptyMessage(0);
			if (!bool) {
				return;
			}
			if (!this.isDestroyed()) {
				Intent intent = new Intent(this,
						MyMedicalRecordListActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			} else {
				hideProgressToast();
			}
		} else if (taskID == HttpParams.DEL_MY_ILL_IMG) {// 删除图片
			String msg;
			if (bool) {
				if (operate == 0) {
					removeImages();
				}
				msg = "删除成功";
			} else {
				msg = "服务器繁忙";
			}
			// Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		hideProgressToast();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(this, MyMedicalRecordListActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
}
