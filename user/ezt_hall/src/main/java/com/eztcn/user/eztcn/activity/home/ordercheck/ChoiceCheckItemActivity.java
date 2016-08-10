/**
 *
 */
package com.eztcn.user.eztcn.activity.home.ordercheck;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import xutils.view.annotation.event.OnItemClick;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.MyDialog;
import com.eztcn.user.eztcn.activity.home.MyDialog.DialogCancle;
import com.eztcn.user.eztcn.activity.home.MyDialog.DialogSure;
import com.eztcn.user.eztcn.adapter.BaseArrayListAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.bean.ordercheck.CheckOrderItem;
import com.eztcn.user.eztcn.impl.OrderCheckImpl;
import com.eztcn.user.eztcn.utils.HttpParams;

/**
 * @author Liu Gang 预约检查 2016年3月14日 上午10:18:03
 */
public class ChoiceCheckItemActivity extends FinalActivity implements
        OnClickListener, IHttpResult, DialogSure, DialogCancle {
    @ViewInject(R.id.cost_info)
    private TextView cost_info;
    @ViewInject(R.id.checkItemLv)
    private ListView checkItemLv;
    @ViewInject(R.id.submitOrder)
    private Button submitOrder;
    private ItemAadpter adapter;
    private View rightView;
    private Hospital hospital;
    private MyDialog dialog;
    private List<CheckOrderItem> transList;
    private String baseCost;
    @ViewInject(R.id.choose_check_rl)
    private RelativeLayout choose_check_rl;
    @ViewInject(R.id.submitOrderLayout)
    private RelativeLayout submitOrderLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ccheckitem);
        ViewUtils.inject(ChoiceCheckItemActivity.this);
        adapter = new ItemAadpter(mContext);
        checkItemLv.setAdapter(adapter);
        rightView = loadTitleBar(true, "选择检查项", null);
        rightView.setOnClickListener(this);
        gainCheckData();

    }

    @OnItemClick(R.id.checkItemLv)
    public void checkLvItemClick(AdapterView<?> parent, View view,
                                 int position, long id) {
        boolean answer = adapter.switchCheck(position);
        if (answer) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取预约检查数据
     */
    private void gainCheckData() {
        hospital = (Hospital) getIntent().getExtras().get("hospital");

        if (null != mContext && !BaseApplication.getInstance().isNetConnected) {
            toast(getString(R.string.network_hint), Toast.LENGTH_SHORT);
            return;
        }
        OrderCheckImpl orderCheckImpl = new OrderCheckImpl();
        RequestParams params = new RequestParams();
        params.addBodyParameter("hosId", String.valueOf(hospital.getId()));
        orderCheckImpl.getCheckList(params, this);
    }

    private class ItemAadpter extends BaseArrayListAdapter<CheckOrderItem> {

        private Context context;
        private final int MAXCOUNT = 5;
        class Holder {
            ImageView imgView;
            TextView nameTv;
            TextView addrTv;
            TextView costTv;
            TextView costTypeTv;
            ImageView checkBox;
        }

        Holder holder;

        public ItemAadpter(Activity context) {
            super(context);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                holder = new Holder();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.item_checkitem, null);
                holder.imgView = (ImageView) convertView
                        .findViewById(R.id.checkitemIv);
                holder.nameTv = (TextView) convertView
                        .findViewById(R.id.itemTitleTv);
                holder.addrTv = (TextView) convertView
                        .findViewById(R.id.itemAddressTv);
                holder.costTypeTv = (TextView) convertView
                        .findViewById(R.id.itemcostTypeTv);
                holder.costTv = (TextView) convertView
                        .findViewById(R.id.itemCostValTv);
                holder.checkBox = (ImageView) convertView
                        .findViewById(R.id.itemcheck);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.checkBox.setTag(position);
            CheckOrderItem checkOrderItem = mList.get(position);
            if (checkOrderItem.isChecked()) {
                // holder.checkBox.setChecked(checkMap.get(position));
                // holder.checkBox.setChecked(true);
                holder.checkBox.setImageResource(R.drawable.cichecked);
            } else {
                holder.checkBox.setImageResource(R.drawable.cinocheck);
                // holder.checkBox.setChecked(false);
            }

            String typeStr = checkOrderItem.getOrderTypeStr();
            if (typeStr.contains("分泌物")) {
                holder.imgView.setImageResource(R.drawable.cifmw);
            } else if (typeStr.contains("影像")) {
                holder.imgView.setImageResource(R.drawable.cimovie);
            } else {
                holder.imgView.setImageResource(R.drawable.cimr);
            }
            holder.addrTv.setText(checkOrderItem.getAddr());
            holder.nameTv.setText(checkOrderItem.getNameStr());

            holder.costTypeTv.setText("检查费");
            holder.costTv.setText("¥" + checkOrderItem.getCostStr());
            return convertView;
        }

        /**
         * 调用去更改checkBox状态
         *
         * @param position
         * @return
         */
        public boolean switchCheck(int position) {
            Boolean flag = true;// 是否可以继续点

            CheckOrderItem checkOrderItem = mList.get(position);
            boolean currentCheck = checkOrderItem.isChecked();// 获取当前的checkbox选中状态
            // 统计当前已有数组的各个checkBox选中状态
            int currentCheckNum = 0;
            boolean includeCurrent = false;
            for (int i = 0; i < mList.size(); i++) {
                CheckOrderItem tempCheckItem = mList.get(i);
                if (tempCheckItem.isChecked()) {
                    currentCheckNum++;
                    if (i == position) {
                        includeCurrent = true;
                    }
                }
            }

            // 选中5个不包含 且 不包含的为未选中状态 不可选
            if (currentCheckNum == MAXCOUNT) {
                if (includeCurrent == false) {
                    if (!currentCheck) {
                        toast("最多一次预约" + MAXCOUNT + "个", Toast.LENGTH_SHORT);
                        flag = false;
                    }
                } else { // 选中5个包含
                    // 放置相反
                    mList.get(position).setChecked(!currentCheck);
                    flag = true;
                }
            } else if (currentCheckNum < MAXCOUNT) {// 选中少于5个
                // 放置相反
                mList.get(position).setChecked(!currentCheck);
                flag = true;
            }

            // if (flag)
            // notifyDataSetChanged();
            return flag;
        }
        /**
         * 调用去更改checkBox状态
         *
         * @param position
         * @return
         */
    }

    @Override
    public void onClick(View v) {
        if (v == rightView) {

        }
    }

    @OnClick(R.id.submitOrder)
    public void subClickListener(View v) {

        transList = new ArrayList<CheckOrderItem>();
        double cost = 0.0;
        if (adapter.getList() != null && adapter.getList().get(0) != null) {
            cost = Double.parseDouble(adapter.getList().get(0)
                    .getBaseCostStr());
        }
        if (adapter != null && null != adapter.getList()) {
            for (CheckOrderItem checkOrderItem : adapter.getList()) {
                if (checkOrderItem.isChecked()) {
                    transList.add(checkOrderItem);
                    cost += Double.parseDouble(checkOrderItem.getCostStr());
                }
            }
        }
        if (transList.size() == 0) {
            toast("请至少选择一项检查项目", Toast.LENGTH_SHORT);
            return;
        }

        View viewContent = View.inflate(mContext, R.layout.ordercheck_dialog,
                null);

        TextView costTv = (TextView) viewContent.findViewById(R.id.orderHintTv);
        costTv.setText(String.format(
                getResources().getString(R.string.order_hintstr),
                new DecimalFormat("###,###,##0.00").format(cost)));

        if(null==dialog){
            dialog = new MyDialog(mContext, "确定", "取消", "订单确认", viewContent);
            dialog.setDialogSure(this);
            dialog.setDialogCancle(this);
        }else{
            dialog.resetContentView(viewContent,mContext,"订单确认");
        }
            dialog.show();

    }

    @Override
    public void result(Object... object) {
        int type = (Integer) object[0];
        boolean isSucc =false;
                if(null!=object[1]){
                    isSucc=(Boolean) object[1];
                }
        switch (type) {
            case HttpParams.GAIN_ORDER_CHECK_LIST:// 获取区域列表
                if (isSucc) {
                    Map<String, Object> map = (Map<String, Object>) object[2];
                    // 基本费用
                    String baseCostStr = String.valueOf(map.get("baseCost"));
                    // 列表
                    List<CheckOrderItem> checkOrderList = (List<CheckOrderItem>) map
                            .get("orderCheckList");
                    if (null != checkOrderList && checkOrderList.size() > 0) {
                        adapter.setList(checkOrderList);

                        cost_info.setText("办理预约检查需要收取" + map.get("baseCost")
                                + "元挂号费，\n该费由医院收取");
                    } else {
                        setViewInVisible();
                        toast("暂无数据", Toast.LENGTH_SHORT);
                        return;
                    }

                    baseCost = map.get("baseCost") + "";
                } else {
                    Toast.makeText(mContext, getString(R.string.service_error),
                            Toast.LENGTH_SHORT).show();

                }
                hideProgressToast();
                break;
        }
    }
    /**
     * 当没有数据的时候，隐藏部分控件。
     */
    private void setViewInVisible() {

         choose_check_rl.setVisibility(View.GONE);
         submitOrderLayout.setVisibility(View.GONE);
    }
    @Override
    public void dialogCancle() {
        // TODO 自动生成的方法存根
        dialog.dissMiss();
    }

    @Override
    public void dialogSure() {
        Intent intent = new Intent(ChoiceCheckItemActivity.this,
                WrittingOrderActivity.class);
        intent.putExtra("checkOrderItemList", (Serializable) transList);
        intent.putExtra("hospital", hospital);
        intent.putExtra("baseCost", baseCost);
        startActivity(intent);
        dialog.dissMiss();
    }
}
