package com.eztcn.user.eztcn.activity.mine;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import xutils.view.annotation.event.OnItemLongClick;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.FamilyMemberAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.FamilyMember;
import com.eztcn.user.eztcn.impl.UserImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
/**
 * @author ezt
 * @title 家庭成员
 * @describe
 * @created 2014年12月25日
 */
public class FamilyMemberActivity extends FinalActivity implements
        IHttpResult {

    @ViewInject(R.id.memberList)
    private ListView memberList;

    @ViewInject(R.id.memberCount)
    private TextView memberCount;

    @ViewInject(R.id.all_layout)
    private RelativeLayout allLayout;

    private FamilyMemberAdapter adapter;

    @ViewInject(R.id.addFamilyBtn)
    private Button addFamilyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.familymember);
        ViewUtils.inject(FamilyMemberActivity.this);
        loadTitleBar(true, "就诊人管理", null);
        if (BaseApplication.getInstance().isNetConnected) {
            getFamilyMember();
        } else {
            addFamilyBtn.setVisibility(View.GONE);
            Toast.makeText(mContext, getString(R.string.network_hint),
                    Toast.LENGTH_SHORT).show();
        }

    }
    @OnClick(R.id.addFamilyBtn)
    public void addMemberClick(View v) {
        Intent intent = new Intent(this, AddMemberActivity.class);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);

        if (arg0 == 2 && arg1 == 2) {
            getFamilyMember();
        }
    }
    /**
     * 初始化成员列表
     */
    public void showMemberList(List<FamilyMember> list) {
        int count = list.size();
        adapter = new FamilyMemberAdapter(mContext);
        memberList.setAdapter(adapter);
        adapter.setList(list);
        if (count == 4) {
            addFamilyBtn.setVisibility(View.GONE);
            memberCount.setVisibility(View.GONE);
        } else {
            addFamilyBtn.setVisibility(View.VISIBLE);
            memberCount.setVisibility(View.VISIBLE);
            memberCount.setText("温馨提示：您还能添加" + (4 - count) + "个用户");
        }
    }
    /**
     * 获取家庭成员列表
     */
    public void getFamilyMember() {
        showProgressToast();
        UserImpl impl = new UserImpl();
        RequestParams params = new RequestParams();
        if (BaseApplication.patient != null) {
            params.addBodyParameter("userId", BaseApplication.patient.getUserId() + "");
        }
        impl.getMemberCenter(params, this);
    }
    @Override
    public void result(Object... object) {
        hideProgressToast();
        if (object == null) {
            return;
        }
        ArrayList<FamilyMember> list = new ArrayList<FamilyMember>();
        Object[] obj = object;
        Integer taskID = (Integer) obj[0];

        switch (taskID) {
            case HttpParams.MEMBER_CENTER:
                boolean flag = (Boolean) obj[1];
                if (flag) {
                    list = (ArrayList<FamilyMember>) object[2];
                }
                if (list != null) {
                    allLayout.setVisibility(View.VISIBLE);
                    showMemberList(list);
                }
                break;
            case HttpParams.MODIFY_FAMILY:
                String msg;
                Map<String, Object> modifyMap = (Map<String, Object>) obj[2];
                if (modifyMap == null) {
                    return;
                }
                boolean modifyFlag = (Boolean) modifyMap.get("flag");
                if (modifyFlag) {
                    msg = "修改成功";
                } else {
                    msg = (String) modifyMap.get("msg");
                }
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
                        .show();
                break;

            case HttpParams.DEL_MEMBER:// 删除家庭成员
                Map<String, Object> delMap = (Map<String, Object>) obj[2];
                if (delMap == null || delMap.size() == 0) {
                    return;
                }
                boolean delFlag = (Boolean) delMap.get("flag");
                if (delFlag) {// 成功
                    getFamilyMember();
                } else {
                    String delMsg = (String) delMap.get("msg");
                    Toast.makeText(getApplicationContext(), delMsg,
                            Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
    /**
     * 删除成员
     *
     * @param id
     */
    private void isDelMember(final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                FamilyMemberActivity.this);
        builder.setTitle("提示")
                .setIcon(android.R.drawable.ic_delete)
                .setMessage("确定删除家庭成员")
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestParams params = new RequestParams();
                        params.addBodyParameter("userId",
                                BaseApplication.patient.getUserId() + "");
                        params.addBodyParameter("id", id + "");
                        new UserImpl().delMemberCenter(params,
                                FamilyMemberActivity.this);

                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create().show();
    }
    @OnItemLongClick(R.id.memberList)
    private boolean reportListItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
        if (position != 0) {
            isDelMember(adapter.getList().get(position).getFamilyId());
        }
        return true;
    }
}
