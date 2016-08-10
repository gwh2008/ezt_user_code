package com.eztcn.user.hall.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.exception.MapException;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.PatientBean;
import com.eztcn.user.hall.model.Request;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.model.UserBean;
import com.eztcn.user.hall.utils.Constant;
import com.eztcn.user.hall.utils.FileUtils;
import com.eztcn.user.hall.utils.GlideUtils;
import com.eztcn.user.hall.utils.IdVerification;
import com.eztcn.user.hall.utils.ToastUtils;
import com.yljt.cascadingmenu.CascadingMenuPopWindow;
import com.yljt.cascadingmenu.DBhelper;
import com.yljt.cascadingmenu.interfaces.CascadingMenuViewOnSelectListener;
import com.yljt.model.Area;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LX on 2016/6/2.
 * 个人信息界面。
 */
public class PersonInfoActivity extends BaseActivity implements ResultSubscriber.OnResultListener {

    private static final int UP_LOADE_AVATAR = 1001;//上传头像

    private Context context = PersonInfoActivity.this;
    private RelativeLayout sex_rl;
    private TextView sex_tx;
    private AlertDialog.Builder builder;
    private ImageView head_image;
    private AlertDialog.Builder headPhotoDialog;
    private final int PIC_FROM_CAMERA = 1;// 相机
    private final int PIC_FROM＿LOCAL_PHOTO = 0;//相册。
    private Uri photoUri;
    private TextView edit_tx;
    // private int flag=0;//当前的标记。
    private EditText info_name;
    private TextView info_address;
    private EditText info_id_number;
    private EditText info_email;
    private TextView info_telephone;
    private EditText address_tx;
    private RelativeLayout details_address_rl;
    private UserBean user = null;
    private PatientBean patient = null;
    private int sex_tag = 0;//性别标注。0,男1，女。
    private final int SAVE_USER_INFO = 0;
    private ArrayList<Area> provinceList;
    // 两级联动菜单数据
    private CascadingMenuPopWindow cascadingMenuPopWindow = null;
    private DBhelper dBhelper;
    //
    private String province_id = "";//省份的code
    private String city_id = "";//city 的code
    private String county_id = "";//区域的id

    @Override
    protected int preView() {
        return R.layout.new_activity_personal_info;
    }

    @Override
    protected void initView() {

        sex_rl = (RelativeLayout) this.findViewById(R.id.sex_rl);
        sex_tx = (TextView) this.findViewById(R.id.sex_tx);
        head_image = (ImageView) this.findViewById(R.id.head_image);
        info_name = (EditText) this.findViewById(R.id.info_name);
        info_address = (TextView) this.findViewById(R.id.info_address);
        info_id_number = (EditText) this.findViewById(R.id.info_id_number);
        info_email = (EditText) this.findViewById(R.id.info_email);
        info_telephone = (TextView) this.findViewById(R.id.info_telephone);
        details_address_rl = (RelativeLayout) this.findViewById(R.id.details_address_rl);
        address_tx= (EditText) this.findViewById(R.id.address_tx);
        edit_tx = loadTitleBar(true, "个人信息", "保存");
        setOnClickListener();
        builder = new AlertDialog.Builder(this);
        user = (UserBean) FileUtils.getObject(context, "user");
        patient = (PatientBean) FileUtils.getObject(context, "patient");
        setConfig();
        setData();
        showHead();
    }

    /**
     * 加载显示头像。
     */
    private void showHead() {
      /*  if (patient != null) {
            if (patient.getEpPic() != null) {
                GlideUtils.into(context,HTTPHelper.USER_PHOTO+patient.getEpPic(),head_image);
                GlideUtils.intoRound(context, HTTPHelper.USER_PHOTO + patient.getEpPic(), 0, head_image);
                Log.i("pic---address", HTTPHelper.USER_PHOTO + patient.getEpPic());
            }
        }*/
        if (BaseApplication.patient != null){
            if(BaseApplication.patient.getEpPic()!=null){
                GlideUtils.intoRound(context, HTTPHelper.BASE_PATH_IMG_AVATAR + BaseApplication.patient.getEpPic(),0,head_image);
            }
        }
    }
    /**
     * 初始化一些数据的设置。
     */
    private void setConfig() {
        //向三级menu添加地区数据
        dBhelper = new DBhelper(this);
        provinceList = dBhelper.getProvince();
        builder = new AlertDialog.Builder(this);
    }
    /**
     * 设置数据。
     */

    private void setData() {
        if (BaseApplication.patient != null){
            if(!TextUtils.isEmpty(BaseApplication.patient.getEpPic())){
                GlideUtils.intoRound(this, HTTPHelper.BASE_PATH_IMG_AVATAR + BaseApplication.patient.getEpPic(),0,head_image);
            }
        }
        UserBean user = (UserBean) FileUtils.getObject(context, "user");

        PatientBean patient = (PatientBean) FileUtils.getObject(context, "patient");
        if (patient != null) {

            if (patient.getEpName() != null) {
                info_name.setText(patient.getEpName());
            }
            switch (patient.getEpSex()) {
                case 0:
                    sex_tx.setText("男");
                    break;
                case 1:
                    sex_tx.setText("女");
                    break;
            }
            if (patient.getEpPid() != null) {
                info_id_number.setText(patient.getEpPid() + "");
                info_id_number.setFocusable(false);
            }
            if (user.getEuEmail() != null) {
                info_email.setText(user.getEuEmail() + "");
            }
            if (patient.getEpMobile() != null) {
                info_telephone.setText(patient.getEpMobile());
            }
            if (user != null && user.getEuEmail() != null) {
                info_email.setText(user.getEuEmail());
            }
            province_id = patient.getEpProvince() + "";
            city_id = patient.getEpCity() + "";
            county_id = patient.getEpCounty() + "";
            if (province_id != null && !"".equals(province_id) && !"0".equals(province_id)) {
                String provinceName = dBhelper.getProvinceName(province_id).getName();
                String cityName = dBhelper.getCityName(city_id).getName();
                String countyName = dBhelper.getCountyName(county_id).getName();
                info_address.setText(provinceName + cityName + countyName);
            }
            if(BaseApplication.patient.getEpAddress()!=null){
                address_tx.setText(BaseApplication.patient.getEpAddress());
            }
        }
    }
    /**
     * 设置点击事件。
     */

    private void setOnClickListener() {

        sex_rl.setOnClickListener(sex_rlOnClickListener);
        head_image.setOnClickListener(head_imageOnClickListener);
        edit_tx.setOnClickListener(edit_txOnClickListener);
        details_address_rl.setOnClickListener(details_address_rlOnClickListener);
    }
    /**
     * 完成
     */
    View.OnClickListener edit_txOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (judgeParams()) {
                try {
                    saveUserInfo();
                } catch (MapException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * 调取接口保存数据
     *
     * @throws MapException
     */
    public void saveUserInfo() throws MapException {

        UserBean user = (UserBean) FileUtils.getObject(context, "user");
        PatientBean bean = (PatientBean) FileUtils.getObject(context, "patient");

        if (user != null && patient != null) {
            Map<String, String> params = new HashMap<>();
            params.put("patinentId", patient.getId() + "");
            params.put("userId", bean.getUserId() + "");
            params.put("epName", info_name.getText().toString().trim() + "");
            if (sex_tag == 0) {
                params.put("epSex", "0");
            } else if (sex_tag == 1) {
                params.put("epSex", "1");
            }
            if (info_address.getText().toString().trim().length() != 0) {
                params.put("epProvince", province_id);
                params.put("epCity", city_id);
                params.put("epCounty", county_id);
            }
            if(address_tx.getText().toString().trim().length()!=0){

                params.put("epAddress",address_tx.getText().toString().trim());
            }
            params.put("epPid", info_id_number.getText().toString().trim() + "");
            params.put("euEmail", info_email.getText().toString().trim());
            params.put("epIdnoType", "1");
            Request request = new Request();
            Map<String, String> map = request.getFormMap(params);
            if (map == null) {
                throw new MapException("map 为null");
            }
            HTTPHelper helper = HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC);
            helper.postSavePersonInfo(map, SAVE_USER_INFO, this);
            edit_tx.setEnabled(false);
            showProgressDialog("数据保存中");
        }
    }
    /**
     * 判断参数
     *
     * @return
     */

    private boolean judgeParams() {
        String n = info_name.getText().toString();
        if (TextUtils.isEmpty(n)) {
            Toast.makeText(mContext, "姓名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (n.length() < 2 || n.length() > 4) {
            Toast.makeText(mContext, "请输入正确的姓名", Toast.LENGTH_SHORT).show();
            return false;
        }
        String ic = info_id_number.getText().toString();
        if (TextUtils.isEmpty(ic)) {
            Toast.makeText(mContext, "身份证不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
          /*  if (!StringUtil.validateCard(ic)) {
                Toast.makeText(mContext, "身份证格式有误", Toast.LENGTH_SHORT).show();
                return false;
            }*/
        try {
            if (!"该身份证有效".equals(IdVerification.IDCardValidate(ic))) {
                Toast.makeText(mContext, "身份证格式有误", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 选择详细地址
     */
    View.OnClickListener details_address_rlOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showPopMenu();
        }
    };

    /**
     * 地区的展示。
     */
    private void showPopMenu() {
        if (cascadingMenuPopWindow == null) {
            cascadingMenuPopWindow = new CascadingMenuPopWindow(
                    getApplicationContext(), provinceList);
            cascadingMenuPopWindow
                    .setMenuViewOnSelectListener(new NMCascadingMenuViewOnSelectListener());
            cascadingMenuPopWindow.showAsDropDown(details_address_rl, 5, 5);
        } else if (cascadingMenuPopWindow != null
                && cascadingMenuPopWindow.isShowing()) {
            cascadingMenuPopWindow.dismiss();
        } else {
            cascadingMenuPopWindow.showAsDropDown(details_address_rl, 5, 5);
        }
    }

    // 级联菜单选择回调接口
    class NMCascadingMenuViewOnSelectListener implements
            CascadingMenuViewOnSelectListener {

        @Override
        public void getValue(Area area) {

            String code = area.getCode();
            String pcode = area.getPcode();
            Log.i("ppparea---------", pcode);//city的id
            Log.i("area---------", code);//区的id,
            //Todo 通过cityid反查询，province_id.
            Area city_bean = dBhelper.getCityName(pcode);

            Area province_bean = dBhelper.getProvinceName(city_bean.getPcode());

            //  Toast.makeText(context, "city_name-----"+city_bean.getName(), Toast.LENGTH_SHORT).show();

            //  Toast.makeText(context, "province_id-----"+city_bean.getPcode(), Toast.LENGTH_SHORT).show();

            //  Toast.makeText(context, "province_name-----"+province_bean.getName(), Toast.LENGTH_SHORT).show();

            //通过city的id查询市的名字，和省的名字以及省份的code。
            info_address.setText(province_bean.getName() + city_bean.getName() + area.getName());
            province_id = city_bean.getPcode();
            city_id = city_bean.getCode();
            county_id = area.getCode();

        }
    }

    /**
     * 选择头像。
     */

    View.OnClickListener head_imageOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            showSelectDialog();
        }
    };

    /**
     * 选择图片。
     */

    private void showSelectDialog() {
        headPhotoDialog = new AlertDialog.Builder(context, R.style.dialog);
        LayoutInflater Inflater = (LayoutInflater) context
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = Inflater.inflate(R.layout.item_photo_select, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        final AlertDialog dialog = headPhotoDialog.create();
        // 默认在中间，下面3行是从底部弹出的动画，
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        dialog.show();
        // 设置 对话框显示横向充满屏幕。。
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = dialog.getWindow()
                .getAttributes(); // 获取对话框当前的参数值
        //  p.height = (int) (d.getHeight() * 0.3); // 高度设置为屏幕的0.3
        p.width = (int) (d.getWidth() * 1); // 宽度设置为屏幕的0.5
        // p.alpha=0.0f;
        dialog.getWindow().setAttributes(p); // 设置生效
        dialog.setContentView(view);
        LinearLayout pop_layout = (LinearLayout) view.findViewById(R.id.pop_layout);
        TextView take_photo = (TextView) view.findViewById(R.id.take_photo);
        TextView choose_from_album = (TextView) view.findViewById(R.id.choose_from_album);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        pop_layout.getBackground().setAlpha(230);
        cancel.getBackground().setAlpha(230);

        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doHandlerPhoto(PIC_FROM_CAMERA);// 用户点击了从照相机获取
                dialog.dismiss();
            }
        });
        choose_from_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doHandlerPhoto(PIC_FROM＿LOCAL_PHOTO);// 从相册中去获取
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void initData() {

    }

    /**
     * 选择性别。
     */

    View.OnClickListener sex_rlOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String[] arrys = {"男", "女"};
            showSelector(arrys, 1);
        }
    };

    /**
     * 选择性别窗口
     */

    public void showSelector(String[] arrays, final int type) {
        builder.setItems(arrays, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (type == 1) {
                    if (which == 0) {
                        sex_tx.setText("男");
                        sex_tag = 0;
                    } else {
                        sex_tx.setText("女");
                        sex_tag = 1;
                    }
                }
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
/*
*
 * 根据不同方式选择图片设置ImageView
 * @param type 0-本地相册选择，非0为拍照*/

    private File picFile;
    private void doHandlerPhoto(int type) {
        try {
            //保存裁剪后的图片文件
            File pictureFileDir = new File(Environment.getExternalStorageDirectory(), "/patient_person_info");

            if (!pictureFileDir.exists()) {
                pictureFileDir.mkdirs();
            }
            picFile = new File(pictureFileDir, "patient_person_info_head.jpeg");
            if (!picFile.exists()) {
                picFile.createNewFile();
            }
            photoUri = Uri.fromFile(picFile);

            if (type == PIC_FROM＿LOCAL_PHOTO) {
                Intent intent = getCropImageIntent();
                startActivityForResult(intent, PIC_FROM＿LOCAL_PHOTO);
            } else {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(cameraIntent, PIC_FROM_CAMERA);
            }

        } catch (Exception e) {
            Log.i("HandlerPicError", "处理图片出现错误");
        }
    }

    /**
     * 调用图片剪辑程序
     */

    public Intent getCropImageIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        setIntentParams(intent);
        return intent;
    }

    /**
     * 启动裁剪
     */

    private void cropImageUriByTakePhoto() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");
        setIntentParams(intent);
        startActivityForResult(intent, PIC_FROM＿LOCAL_PHOTO);
    }

    /**
     * 设置公用参数
     */

    private void setIntentParams(Intent intent) {
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 600);
        intent.putExtra("noFaceDetection", true); // no face detection
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PIC_FROM_CAMERA: // 拍照
                try {
                    cropImageUriByTakePhoto();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case PIC_FROM＿LOCAL_PHOTO:
                try {
                    if (photoUri != null) {
                        Bitmap bitmap = decodeUriAsBitmap(photoUri);
                        GlideUtils.intoRound(context, FileUtils.getRealFilePath(context, photoUri), 0, head_image);
                        head_image.setImageBitmap(bitmap);
                    }
                    upLoadeAvatar(new File(FileUtils.getRealFilePath(context, photoUri)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 上传头像
     *
     * @param file
     */
    private void upLoadeAvatar(File file) {
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC).upLoadAvatar(file, UP_LOADE_AVATAR, this);
    }
    /**
     * 解析Bitmap。
     *
     * @param uri
     * @return
     */
    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    @Override
    public void onStart(int requestType) {

    }

    @Override
    public void onCompleted(int requestType) {

    }

    @Override
    public void onError(int requestType) {

        if (requestType == UP_LOADE_AVATAR){
            ToastUtils.shortToast(context, "头像上传失败");
        }
        ToastUtils.shortToast(context, "网络请求错误");
    }

    @Override
    public void onNext(IModel t, int requestType) {

        switch (requestType) {
            //保存信息成功
            case SAVE_USER_INFO:
                dismissProgressDialog();
                edit_tx.setEnabled(true);
                Response response = (Response) t;
                int number = Integer.valueOf(response.getNumber());
                if (number == 2002) {
                    ToastUtils.shortToast(context, "个人信息修改成功...");
                    //更新本地的数据。
                    PatientBean patient = (PatientBean) FileUtils.getObject(context, "patient");
                    UserBean user = (UserBean) FileUtils.getObject(context, "user");
                    patient.setEpProvince(Integer.valueOf(province_id));
                    patient.setEpCity(Integer.valueOf(city_id));
                    patient.setEpCounty(Integer.valueOf(county_id));
                    patient.setEpName(info_name.getText().toString().trim());
                    patient.setEpSex(sex_tag);
                    patient.setEpPid(info_id_number.getText().toString().trim());
                    patient.setEpAddress(address_tx.getText().toString().trim());
                    user.setEuEmail(info_email.getText().toString().trim());
                    //进行保存。
                    FileUtils.saveObject(context, "user", user);
                    FileUtils.saveObject(context, "patient", patient);
                    BaseApplication.patient=patient;
                    BaseApplication.user=user;
                    onBack();//返回。
                }else{
                    ToastUtils.shortToast(context,response.getDetailMsg());
                }
                break;

            case UP_LOADE_AVATAR :

                Response response2 = (Response) t;
                int number2 = Integer.valueOf(response2.getNumber());
                Log.i("UP_LOADE_AVATAR-----",number2+"");

            break;
        }
    }

    /**
     * 上传头像到服务器
     */
    public void uploadPhoto() {

      /*  if (photoPath == null || photoPath.equals("")) {
            return;
        }
        File photoFile = new File(photoPath);
        if (BaseApplication.eztUser != null && photoFile != null
                && photoFile.exists()) {

            UserImpl impl = new UserImpl();
            RequestParams params = new RequestParams();
            params.addBodyParameter("patientId", eztUser.getId());
            // try {
            params.addBodyParameter("myfile", photoFile);
            // } catch (FileNotFoundException e) {
            // e.printStackTrace();
            // }
            impl.userUploadPhoto(params, this);
            BaseApplication.eztUser.setPhoto(photoFile.getName());
        }*/
    }

    @Override
    protected void disDialogCallBack() {
        super.disDialogCallBack();
        edit_tx.setEnabled(true);
    }
    @Override
    protected void onBack() {
        super.onBack();
        setResult(Constant.REFRESH_PERSON_PAGE_INFO);
    }
}