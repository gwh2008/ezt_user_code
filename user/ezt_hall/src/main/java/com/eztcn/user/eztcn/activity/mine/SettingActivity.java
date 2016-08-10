package com.eztcn.user.eztcn.activity.mine;
import java.util.Map;
import xutils.BitmapUtils;
import xutils.ViewUtils;
import xutils.db.sqlite.WhereBuilder;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.MainActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.MessageAll;
import com.eztcn.user.eztcn.bean.MsgType;
import com.eztcn.user.eztcn.bean.SoftVersion;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.controller.SettingManager;
import com.eztcn.user.eztcn.controller.UpManager;
import com.eztcn.user.eztcn.controller.VersionManager;
import com.eztcn.user.eztcn.customView.SwitchButton;
import com.eztcn.user.eztcn.db.EztDb;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.impl.SoftUpdateImpl;
import com.eztcn.user.eztcn.utils.AppManager;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.MsgUtil;
import com.eztcn.user.eztcn.utils.ResourceUtils;
import com.eztcn.user.hall.activity.loginSetting.AccountManageActivity;
import com.eztcn.user.hall.utils.Constant;
import com.eztcn.user.hall.utils.FileUtils;
import com.eztcn.user.hall.utils.Tools;
import com.igexin.sdk.PushManager;
/**
 * @author ezt
 * @title 设置
 * @describe
 * @created 2014年12月15日
 */
public class SettingActivity extends FinalActivity implements OnClickListener, IHttpResult {

    @ViewInject(R.id.accountManager)
    // , click = "onClick"
    private TextView accountManager;
    @ViewInject(R.id.switchButton)
    private SwitchButton switchButton;
    @ViewInject(R.id.clear)
    // , click = "onClick"
//	private TextView clear;
    private View clear;
    @ViewInject(R.id.statement)
    // , click = "onClick"
    private TextView statement;
    @ViewInject(R.id.aboutEZT)
    private TextView aboutEZT;

    @ViewInject(R.id.cache_num_tv)
    private TextView tvCacheNum;// 缓存大小
    @ViewInject(R.id.versionUpdate)
    private TextView versionUpdate;
    // @ViewInject(R.id.aboutEZT_layout)//, click = "onClick"
    private RelativeLayout layoutAboutEZT;

    @ViewInject(R.id.exit)
    // , click = "onClick"
    private Button exit;
    @ViewInject(R.id.switchButton30)
    private ToggleButton switchButton30;
    private View dialogView;
    private TextView tvExitApp, tvExitUser;
    private AlertDialog dialog;
    private Builder builder;
    private UpManager up;
    private Context context=SettingActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ViewUtils.inject(SettingActivity.this);
        loadTitleBar(true, "设置", null);
        // 初始化弹出框
        dialogView = LinearLayout.inflate(this, R.layout.dialog_exit, null);
        tvExitApp = (TextView) dialogView.findViewById(R.id.dialog_exit2);// 注销用户
        tvExitUser = (TextView) dialogView.findViewById(R.id.dialog_exit1);// 退出应用
        tvExitApp.setOnClickListener(this);
        tvExitUser.setOnClickListener(this);
        dialog = new AlertDialog.Builder(this).create();
        dialog.setView(dialogView);
        // ------
        initSwitch();
        if (BaseApplication.patient != null) {
            accountManager.setVisibility(View.VISIBLE);
        }
        initialCacheFileSide();
        builder = new AlertDialog.Builder(this);
    }
    /**
     * 初始化缓存大小
     */
    private void initialCacheFileSide() {
        String outsideCacheFileSide = null;
        try {
            outsideCacheFileSide = SettingManager
                    .getCacheSize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvCacheNum.setText(outsideCacheFileSide);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            if (SystemPreferences.getBoolean(EZTConfig.KEY_IS_UPDATE)) {
                Drawable rightDrawable = getResources().getDrawable(
                        R.drawable.ic_red_point);
                rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
                        rightDrawable.getMinimumHeight());
                aboutEZT.setCompoundDrawables(null, null, rightDrawable, null);
                aboutEZT.setCompoundDrawablePadding(ResourceUtils.dip2px(
                        mContext, ResourceUtils.getXmlDef(mContext,
                                R.dimen.medium_margin)));
            } else {
                aboutEZT.setCompoundDrawables(null, null, null, null);
            }
        }
        super.onWindowFocusChanged(hasFocus);
    }

    @OnClick(R.id.accountManager)
    // 帐号管理
    private void accountManagerClick(View v) {
        Intent intent = new Intent();
        if (BaseApplication.patient == null) {
            HintToLogin(Constant.LOGIN_COMPLETE);
            return;
        }
        intent.setClass(this, AccountManageActivity.class);
        if (intent != null) {
            startActivity(intent);
        }
    }

    @OnClick(R.id.clear)
    // 清除缓存
    private void clearClick(View v) {

        SettingManager.clearCache(mContext, true);
        initialCacheFileSide();

    }

    @OnClick(R.id.statement)
    // 声明
    private void statementClick(View v) {
        Intent intent = new Intent();
        intent.setClass(this, StatementActivity.class);
        if (intent != null) {
            startActivity(intent);
        }

    }

    @OnClick(R.id.aboutEZT_layout)
    // 关于EZT
    private void aboutEZT_layoutClick(View v) {
        Intent intent = new Intent();
        intent.setClass(this, AboutEZTActivity.class);
        if (intent != null) {
            startActivity(intent);
        }
    }
    @OnClick(R.id.versionUpdate)
    private void versionUpdate_textClick(View v) {
        // TODO
        RequestParams params = new RequestParams();
        params.addBodyParameter("type", "1");
        new SoftUpdateImpl().getSoftVersion(params, this);
        showProgressToast();
        versionUpdate.setEnabled(false);

    }


    @OnClick(R.id.exit)
    // 关于EZT
    private void exitClick(View v) {
        exitSelect();
    }

    public void onClick(View v) {
        // Intent intent = new Intent();
        switch (v.getId()) {

            case R.id.dialog_exit2:// 退出应用
                // intent = null;
                PushManager.getInstance().stopService(getApplicationContext());// 停止个推服务
                BitmapUtils bitmapUtils = new BitmapUtils(SettingActivity.this);
                bitmapUtils.closeCache();

                AppManager.getAppManager().AppExit(mContext);
                dialog.dismiss();
                MsgUtil.clearNotificationMsg();
                break;
            case R.id.dialog_exit1:// 注销用户
                Tools.exitApp(context);
                finish();
                break;
        }
    }

    /**
     * 初始化新消息提醒按钮
     */
    public void initSwitch() {
        boolean bool = SystemPreferences.getBoolean(EZTConfig.KEY_SET_MSG_HINT,
                true);
        switchButton30.setChecked(bool);
        switchButton30.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                SystemPreferences.save(EZTConfig.KEY_SET_MSG_HINT, isChecked);
            }
        });
    }

    /**
     * 退出选项
     */
    public void exitSelect() {
        if (BaseApplication.patient == null) {
            PushManager.getInstance().stopService(getApplicationContext());// 停止个推服务
            new BitmapUtils(mContext).closeCache();

            AppManager.getAppManager().AppExit(mContext);
        } else {
//
            /**
             * 移除用户信息。
             */
            SystemPreferences.remove(EZTConfig.KEY_IS_AUTO_LOGIN);
            SystemPreferences.remove(EZTConfig.KEY_ACCOUNT);
            SystemPreferences.remove(EZTConfig.KEY_PW);
            /**
             * 移除选择医院、科室操作数据
             */
            SystemPreferences.remove(EZTConfig.KEY_DEPT_ID);
            SystemPreferences.remove(EZTConfig.KEY_STR_DEPT);
            SystemPreferences.remove(EZTConfig.KEY_SELECT_DEPT_POS);
            SystemPreferences.remove(EZTConfig.KEY_SELECT_AREA_POS);
            SystemPreferences.remove(EZTConfig.KEY_SELECT_HOS_POS);
            SystemPreferences.remove(EZTConfig.KEY_SELECT_N_POS);
            SystemPreferences.remove(EZTConfig.KEY_STR_CITY);
            SystemPreferences.remove(EZTConfig.KEY_CITY_ID);
            SystemPreferences.remove(EZTConfig.KEY_STR_N);
            SystemPreferences.remove(EZTConfig.KEY_HOS_ID);
            SystemPreferences.remove(EZTConfig.KEY_HOS_NAME);

            SystemPreferences.remove(EZTConfig.KEY_DF_DEPT_ID);
            SystemPreferences.remove(EZTConfig.KEY_DF_STR_DEPT);
            SystemPreferences.remove(EZTConfig.KEY_DF_SELECT_DEPT2_POS);
            SystemPreferences.remove(EZTConfig.KEY_DF_SELECT_DEPT_POS);
            //移除消息提醒
            SystemPreferences.remove(EZTConfig.KEY_HAVE_MSG);
            SystemPreferences.remove(EZTConfig.KEY_TOTAL);
            // // 删除用户有关的本地存储数据（消息箱用到）
            // EztDb.getInstance(mContext).delDataWhere(new MsgType(),
            // "typeId = 'register' or typeId = '4' or typeId = '5'");
            // EztDb.getInstance(mContext).delDataWhere(new MessageAll(),
            // "msgType = 'register' or msgType = '4' or msgType = '5'");

            WhereBuilder delTypeB = WhereBuilder.b("typeId", "=", "register")
                    .or("typeId", "=", "4").or("typeId", "=", "5");
            // 删除用户有关的本地存储数据（消息箱用到）
            EztDb.getInstance(mContext).delDataWhere(new MsgType(),
                    delTypeB);
            WhereBuilder delAllB = WhereBuilder.b("msgType", "=", "register")
                    .or("msgType", "=", "4").or("msgType", "=", "5");
            EztDb.getInstance(mContext).delDataWhere(new MessageAll(),
                    delAllB);

            finish();
//						MainActivity.getInstance().mUiTabBottom.tabIntent(0);
            MainActivity.getInstance().mUiTabBottom.tabIntent(4);
            dialog.dismiss();
            MsgUtil.clearNotificationMsg();


//			FinalBitmap.create(mContext).closeCache();
            new BitmapUtils(mContext).closeCache();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgressToast();
    }

    @Override
    public void result(Object... object) {
        hideProgressToast();
        if (!versionUpdate.isEnabled()) {
            versionUpdate.setEnabled(true);
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

                if (!versionUpdate.isEnabled()) {
                    versionUpdate.setEnabled(true);
                }
                break;

            case HttpParams.SOFT_VERSION:// 获取服务器版本信息
                versionUpdate.setEnabled(true);

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
