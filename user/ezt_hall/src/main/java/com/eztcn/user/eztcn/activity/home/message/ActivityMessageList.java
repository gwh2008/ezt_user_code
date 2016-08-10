package com.eztcn.user.eztcn.activity.home.message;
import java.util.ArrayList;
import java.util.Map;
import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnItemClick;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.BaseArrayListAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.message.Message;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.PullToRefreshListView;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnLoadMoreListener;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnRefreshListener;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.impl.MessageImpl;
import com.eztcn.user.eztcn.utils.CacheUtils;

public class ActivityMessageList extends FinalActivity implements IHttpResult,
        OnLoadMoreListener, OnRefreshListener {
    @ViewInject(R.id.message_lv)
    private PullToRefreshListView lv;// 医生列表
    private MessageAdapter adapter;
    /**
     * 消息列表
     */
    private ArrayList<Message> messageList;
    private int currentPage = 1;// 当前页数
    private final int pageSize = 15;
    private CacheUtils mCache;
    private final String MESS_AGELIST_DATA = "MessageListData";// 缓存key-消息列表
    private int result_Code = 0;// 标记是否为回调返回调用接口（非0为回调接口）

    @Override
    public void result(Object... object) {
        int type = (Integer) object[0];
        boolean isSucc = false;
        if (object[1] != null) {
            isSucc = (Boolean) object[1];
        }
        if (isSucc) {
            Map<String, Object> map;
            ArrayList<Message> data = null;
            // docList = (ArrayList<Doctor>) object[2]; // 2015-12-26 接口對接
            map = (Map<String, Object>) object[2];
            if (map.containsKey("total")) {
                SystemPreferences.save("total", map.get("total"));
            }
            if (map.containsKey("msgList")) {
                messageList = (ArrayList<Message>) map.get("msgList");
            }
            hideProgressToast();
            if (messageList != null && messageList.size() > 0) {
                if (currentPage == 1) {// 第一次加载或刷新
                    data = (ArrayList<Message>) messageList;
                    if (messageList.size() < pageSize) {
                        lv.setAutoLoadMore(false);
                        lv.onLoadMoreComplete();
                    }
                    lv.onRefreshComplete();
                    mCache.put(MESS_AGELIST_DATA, messageList);

                } else {// 加载更多
                    data = (ArrayList<Message>) adapter.getList();
                    if (data == null || data.size() <= 0)
                        data = (ArrayList<Message>) messageList;
                    else
                        data.addAll(messageList);
                    lv.onLoadMoreComplete();

                }
                adapter.setList(data);
                lv.setVisibility(View.VISIBLE);

            } else { // 没有从服务器获取到数据
                if (adapter.getList() != null && adapter.getList().size() != 0) {// 加载
                    if (currentPage > 1) { // >1只有加载更多
                        currentPage--;// 把先前加的捡回去
                    }
                    if (messageList != null) {
                        if (messageList.size() == 0) {
                            lv.setAutoLoadMore(false);
                        }
                    } else {
                        Toast.makeText(mContext,
                                getString(R.string.request_fail),
                                Toast.LENGTH_SHORT).show();
                    }
                    lv.onLoadMoreComplete();
                    data = (ArrayList<Message>) adapter.getList();
                    if (result_Code != 0) {
                        adapter.mList.clear();
                        adapter.notifyDataSetChanged();
                        lv.setVisibility(View.GONE);
                    }

                } else {// 刷新
                    lv.onRefreshComplete();
                    Toast.makeText(mContext, "抱歉，暂无消息", Toast.LENGTH_SHORT)
                            .show();
                    hideProgressToast();
                }

            }

            if (data != null) {
                messageList = data;
            }

        } else {
            hideProgressToast();
            if (currentPage > 1) {
                currentPage--;
            }
            lv.onLoadMoreComplete();
            Toast.makeText(mContext, getString(R.string.service_error),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ViewUtils.inject(ActivityMessageList.this);
        loadTitleBar(true, "消息", null);
        adapter = new MessageAdapter(this);
        lv.setAdapter(adapter);
        lv.setCanLoadMore(true);
        lv.setCanRefresh(true);
        lv.setAutoLoadMore(true);
        lv.setMoveToFirstItemAfterRefresh(false);
        lv.setDoRefreshOnUIChanged(false);
        lv.setOnLoadListener(this);
        lv.setOnRefreshListener(this);
        mCache = CacheUtils.get(this);
        initialData();
    }
    /**
     * 筛选后获取医生列表
     */
    private void getData() {
        showProgressToast();
        lv.setSelection(0);
        if (adapter.mList != null) {// 返回刷新，如果之前adapter，则清除
            adapter.mList.clear();
            lv.setAdapter(adapter);
            lv.setVisibility(View.GONE);
        }
        currentPage = 1;// 回调返回初始化页码
        initialData();
    }
    /**
     * 数据
     */
    private void initialData() {
        RequestParams params = new RequestParams();
        MessageImpl impl = new MessageImpl();
        if (BaseApplication.patient != null) {
            params.addBodyParameter("userId",
                    String.valueOf(BaseApplication.patient.getUserId()));
        }
        params.addBodyParameter("page", String.valueOf(currentPage));
        impl.findOrderInfo(params, this);
        showProgressToast();
    }
    private class MessageAdapter extends BaseArrayListAdapter<Message> {
        private LayoutInflater inflater;

        public MessageAdapter(Activity context) {
            super(context);
            inflater = LayoutInflater.from(context);
        }
        private Holder holder;
        class Holder {
            ImageView icon;
            TextView titleTv;
            TextView contentTv;
            TextView dateTv;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                holder = new Holder();
                convertView = inflater.inflate(R.layout.item_message, null);
                holder.icon = (ImageView) convertView
                        .findViewById(R.id.msgIcon);
                holder.titleTv = (TextView) convertView
                        .findViewById(R.id.titleTv);
                holder.contentTv = (TextView) convertView
                        .findViewById(R.id.contentTv);
                holder.dateTv = (TextView) convertView
                        .findViewById(R.id.dateTv);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            Message message = mList.get(position);
            switch (message.getIconType()) {
                case EZTConfig.MSG_YYJC: {
                    holder.icon.setImageResource(R.drawable.msg_hytx);
                }
                break;
                case EZTConfig.MSG_YYBC: {
                    holder.icon.setImageResource(R.drawable.msg_hytx);
                }
                break;
                case EZTConfig.MSG_YYYP: {
                    holder.icon.setImageResource(R.drawable.msg_hytx);
                }
                break;
                case EZTConfig.MSG_GHMY: {
                    holder.icon.setImageResource(R.drawable.msg_hytx);
                }
                break;
                case EZTConfig.MSG_LKFW: {
                    holder.icon.setImageResource(R.drawable.msg_hytx);
                }
                break;
            }
            holder.titleTv.setText(message.getTitleStr());
            holder.contentTv.setText(message.getContentStr());
            holder.dateTv.setText(message.getDateStr());
            return convertView;
        }

    }

    @Override
    public void onRefresh() {
        lv.setAutoLoadMore(true);
        currentPage = 1;
        initialData();
    }

    @Override
    public void onLoadMore() {
        if (messageList != null) {
            if (messageList.size() < pageSize
                    || (messageList.size() > pageSize && messageList.size()
                    % pageSize != 0)) {
                lv.setAutoLoadMore(false);
                lv.onLoadMoreComplete();
            } else {
                currentPage++;
                initialData();
            }
        }
    }

    @Override
    protected void onDestroy() {
        hideProgressToast();
        super.onDestroy();
    }

    @OnItemClick(R.id.message_lv)
    public void msgItemClick(AdapterView<?> parent, View view, int position,
                             long id) {
        if (position - 1 > 0 && (position - 1) < messageList.size())
            startActivity(new Intent(ActivityMessageList.this,
                    ActivityMessageDetail.class).putExtra("msgDetail", messageList
                    .get(position - 1).getContentStr()));
    }
}
