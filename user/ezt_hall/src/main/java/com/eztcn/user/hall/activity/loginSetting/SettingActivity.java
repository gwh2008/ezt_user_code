package com.eztcn.user.hall.activity.loginSetting;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.SoftVersion;
import com.eztcn.user.eztcn.controller.UpManager;
import com.eztcn.user.eztcn.controller.VersionManager;
import com.eztcn.user.eztcn.impl.SoftUpdateImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.hall.activity.BaseActivity;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.Request;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.model.bean.UpdateBean;
import com.eztcn.user.hall.utils.AppManager;
import com.eztcn.user.hall.utils.DataCleanManager;
import com.eztcn.user.hall.utils.ToastUtils;
import com.eztcn.user.hall.utils.Tools;
import java.util.HashMap;
import java.util.Map;
import xutils.http.RequestParams;
/**
 * Created by lx on 2016/6/4.
 * 设置界面。
 */
public class SettingActivity extends BaseActivity implements ResultSubscriber.OnResultListener,IHttpResult{

    private Context context=SettingActivity.this;
    private RelativeLayout account_manager_rl;
    private RelativeLayout clear_cache_rl;
    private TextView cache_num_tx;
    private RelativeLayout about_rl;
    private RelativeLayout help_rl;
    private RelativeLayout statement_agreement_rl;
    private RelativeLayout check_update_rl;
    private Button exit_login_bt;
    private TextView version;
    private  String TAG="SettingActivity";
    private  final  int CHECK_UPDATE=3;
    private AlertDialog.Builder builder;
    private UpManager up;

    @Override
    protected int preView() {
        return R.layout.new_activity_setting;
    }

    @Override
    protected void initView() {
        loadTitleBar(true,"设置","");
        account_manager_rl= (RelativeLayout) this.findViewById(R.id.account_manager_rl);
        clear_cache_rl= (RelativeLayout) this.findViewById(R.id.clear_cache_rl);
        cache_num_tx= (TextView) this.findViewById(R.id.cache_num_tx);
        about_rl= (RelativeLayout) this.findViewById(R.id.about_rl);
        help_rl= (RelativeLayout) this.findViewById(R.id.help_rl);
        statement_agreement_rl= (RelativeLayout) this.findViewById(R.id.statement_agreement_rl);
        check_update_rl= (RelativeLayout) this.findViewById(R.id.check_update_rl);
        exit_login_bt= (Button) this.findViewById(R.id.exit_login_bt);
        version= (TextView) this.findViewById(R.id.version);
        setOnClickListener();
        setInvisible();
        getCurrentVersion();
        initialCacheFileSide();
        builder = new AlertDialog.Builder(this);
    }
    /**
     * 设置部分控件不可见。
     */
    private void setInvisible() {

        if(BaseApplication.patient==null){
            account_manager_rl.setVisibility(View.GONE);
            exit_login_bt.setVisibility(View.GONE);
        }else{
            account_manager_rl.setVisibility(View.VISIBLE);
            exit_login_bt.setVisibility(View.VISIBLE);
        }
    }
    /**
     * 获取当前版本。
     */
    private void getCurrentVersion() {

        if(!"".equals(AppManager.getVersion(context))){
            String  version_string=AppManager.getVersion(context);
            version.setText(version_string);
        }
    }
    /**
     * 设置点击事件。
     */
    private void setOnClickListener() {
        account_manager_rl.setOnClickListener(account_manager_rlOnClickListener);
        clear_cache_rl.setOnClickListener(clear_cache_rlOnClickListener);
        about_rl.setOnClickListener(about_rlOnClickListener);
        help_rl.setOnClickListener(help_rlOnClickListener);
        statement_agreement_rl.setOnClickListener(statement_agreement_rlOnClickListener);
        exit_login_bt.setOnClickListener(exit_login_btOnClickListener);
        check_update_rl.setOnClickListener(check_update_rlOnClickListener);
    }

    @Override
    protected void initData() {

    }
    /**
     * 账号管理
     */
   View.OnClickListener  account_manager_rlOnClickListener=new View.OnClickListener() {
       @Override
       public void onClick(View v) {

           startActivity(new Intent(context,AccountManageActivity.class));
       }
   };
    /**
     * 检查更新。
     */
    View.OnClickListener check_update_rlOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

//         checkUpdate();
            checkUpdate2();
        }
    };
    /**
     * 检查更新调取接口。
     */
    private void checkUpdate() {

        Map<String,String> params=new HashMap<>();
        params.put("type","1");
        Request request=new Request();
        Map<String,String> map=request.getFormMap(params);
        HTTPHelper helper= HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC);
        helper.checkUpdate(map,CHECK_UPDATE,this);
        check_update_rl.setEnabled(false);
        showProgressDialog("加载中...");
    }

    private void checkUpdate2() {

        RequestParams params = new RequestParams();
        params.addBodyParameter("type", "1");
        new SoftUpdateImpl().getSoftVersion(params, this);
       // showProgressDialog("检查中");
        check_update_rl.setEnabled(false);
        showProgressDialog("加载中...");
    }

    /**
     * 清除缓存。
     */
    View.OnClickListener clear_cache_rlOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          /*  SettingManager.clearCache(context, true);
            initialCacheFileSide();*/
            DataCleanManager.clearAllCache(context);
            cache_num_tx.setText("");
            ToastUtils.shortToast(context,"清除完成");
        }
    };
    /**
     * 关于
     */
    View.OnClickListener about_rlOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(context,AboutEZTActivity.class));
        }
    };
    /**
     * 帮助
     */
    View.OnClickListener help_rlOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(context,HelpActivity.class));
        }
    };
    /**
     * 负责协议和声明
     */
    View.OnClickListener statement_agreement_rlOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            startActivity(new Intent(context,StatementActivity.class));
        }
    };
    /**
     * 退出登录
     */
    View.OnClickListener exit_login_btOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Tools.exitApp(context);
            SettingActivity.this.finish();
            startActivity(new Intent(mContext, LoginActivity.class));
        }
    };
    /**
     * 初始化缓存大小
     */
    private void initialCacheFileSide() {
        try {
           String cache_string= DataCleanManager.getTotalCacheSize(context);

            if(cache_string!=null&&!TextUtils.isEmpty(cache_string)){
                cache_num_tx.setText(cache_string);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onStart(int requestType) {

        Log.i(TAG,"onStart");
    }

    @Override
    public void onCompleted(int requestType) {

        check_update_rl.setEnabled(true);
        dismissProgressDialog();
        Log.i(TAG,"onStart");
    }

    @Override
    public void onError(int requestType) {
        check_update_rl.setEnabled(true);
        dismissProgressDialog();
        Log.i(TAG,"onStart");
    }

    @Override
    public void onNext(IModel t, int requestType) {

        check_update_rl.setEnabled(true);
        dismissProgressDialog();

        switch (requestType){

            case  CHECK_UPDATE:
                Response response= (Response) t;
                UpdateBean updatebean= (UpdateBean) response.getData();
            break;

        }
    }


    @Override
    public void result(Object... object) {

        dismissProgressDialog();
        if (!check_update_rl.isEnabled()) {
            check_update_rl.setEnabled(true);
        }
        if (object == null || object.equals("")) {
            Toast.makeText(getApplicationContext(), "检查新版本失败",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        int type = (Integer) object[0];
        boolean bool = (Boolean) object[1];
        if (!bool) {
            Toast.makeText(getApplicationContext(), "检查新版本失败",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        switch (type) {
            case HttpParams.POST_ERROR:
                Toast.makeText(getApplicationContext(), "已是最新版本",
                        Toast.LENGTH_SHORT).show();

                if (!check_update_rl.isEnabled()) {
                    check_update_rl.setEnabled(true);
                }
                break;

            case HttpParams.SOFT_VERSION:// 获取服务器版本信息
                check_update_rl.setEnabled(true);

                if (object[2] == null || object[2] instanceof String) {
                    Toast.makeText(getApplicationContext(), "检查新版本失败",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                @SuppressWarnings("unchecked")
                Map<String, Object> versionMap = (Map<String, Object>) object[2];
                if (versionMap == null || versionMap.size() == 0) {

                    return;
                }
                SoftVersion version = (SoftVersion) versionMap.get("version");

                int vNum = VersionManager.getVersionCode(this);
                try {
                    if (vNum >= version.getVersionNum()) {
                        builder.setTitle("提示");
                        builder.setMessage("此版本为最新版本，无需更新");
                        builder.setPositiveButton("确定", null);
                        builder.create().show();
                    } else {
                        up = new UpManager(SettingActivity.this, version);
//					up.checkUpdataInfo(version.getForce());
                        up.checkUpdataInfo(0,context);
                    }
                } catch (Exception e) {
                    builder.setTitle("提示");
                    builder.setMessage("此版本为最新版本，无需更新");
                    builder.setPositiveButton("确定", null);
                    builder.create().show();
                }
                break;
        }

    }
}
