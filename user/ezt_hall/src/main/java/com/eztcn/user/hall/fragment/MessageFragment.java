package com.eztcn.user.hall.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.home.message.ActivityMessageDetail;
import com.eztcn.user.eztcn.bean.message.Message;
import com.eztcn.user.eztcn.customView.PullToRefreshListView;
import com.eztcn.user.eztcn.utils.CacheUtils;
import com.eztcn.user.hall.adapter.BaseCommonAdapter;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.MessageResponse;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.utils.AlertDialogUtils;
import com.eztcn.user.hall.utils.GlideUtils;
import com.eztcn.user.hall.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xutils.view.annotation.event.OnItemClick;

/**
 * Created by lx on 2016/5/29.
 * 消息的fragment
 */
public class MessageFragment extends BaseFragment implements
        PullToRefreshListView.OnLoadMoreListener, PullToRefreshListView.OnRefreshListener, ResultSubscriber.OnResultListener {

    public static final int GET_MESSAGE = 1001;
    public static final int DEL_MESSAGE = 1002;

    private PullToRefreshListView lv;// 医生列表
    private BaseCommonAdapter<MessageResponse.RowsBean> mAdapter;
    private List<MessageResponse.RowsBean> mData = new ArrayList<>();
    /**
     * 消息列表
     */
    private ArrayList<Message> messageList;
    private int mCurrentPage = 1;// 当前页数
    private final int pageSize = 15;
    private CacheUtils mCache;
    private final String MESS_AGELIST_DATA = "MessageListData";// 缓存key-消息列表
    private int result_Code = 0;// 标记是否为回调返回调用接口（非0为回调接口）
    private View rootView;
    private FrameLayout mNoMessageFl;
    private boolean isAllData = false;
    private int mDelPosition;
    public static MessageFragment getInstance() {
        MessageFragment fragment = new MessageFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 避免UI重新加载
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.activity_message, null);// 缓存Fragment
            initView();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    /**
     * 初始化控件。
     */
    private void initView() {
        mNoMessageFl = (FrameLayout) rootView.findViewById(R.id.message_fragment_no_message_fl);
        if (mData.size() <= 0) {
            mNoMessageFl.setVisibility(View.VISIBLE);
        } else {
            mNoMessageFl.setVisibility(View.GONE);
        }
        mAdapter = new BaseCommonAdapter<MessageResponse.RowsBean>(getContext(), mData) {
            @Override
            public void convert(ViewHolder holder, MessageResponse.RowsBean item, int position, View convertView, ViewGroup parent) {
                ImageView imgView = holder.getView(R.id.msgIcon);
                TextView titleView = holder.getView(R.id.titleTv);
                TextView decView = holder.getView(R.id.contentTv);
                TextView timeView = holder.getView(R.id.dateTv);

                if ("预约挂号".equals(item.getTitle())) {
                    GlideUtils.intoRound(getContext(), R.drawable.new_icon_message_yu_yue_gua_hao, 0, imgView);
                } else if ("复印病历".equals(item.getTitle())) {
                    GlideUtils.intoRound(getContext(), R.drawable.new_icon_message_fu_yin_bing_li, 0, imgView);
                } else if ("指导挂号提交成功".equals(item.getTitle())) {
                    GlideUtils.intoRound(getContext(), R.drawable.new_icon_message_zhi_dao_gua_hao, 0, imgView);
                } else if ("导诊小医".equals(item.getTitle())) {
                    GlideUtils.intoRound(getContext(), R.drawable.new_icon_message_dao_zhen_xiao_yi, 0, imgView);
                } else if ("号源提醒".equals(item.getTitle())) {
                    GlideUtils.intoRound(getContext(), R.drawable.new_icon_message_hao_yuan_ti_xing, 0, imgView);
                } else {
                }
                titleView.setText(item.getTitle());
                decView.setText(item.getContent());
                timeView.setText(item.getCreatetime());
            }

            @Override
            public ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
                return ViewHolder.get(getContext(), convertView, parent, R.layout.item_message, position);
            }
        };
        lv = (PullToRefreshListView) rootView.findViewById(R.id.message_lv);
        lv.setAdapter(mAdapter);
        lv.setCanLoadMore(true);
        lv.setCanRefresh(true);
//        lv.setAutoLoadMore(true);
        lv.setMoveToFirstItemAfterRefresh(false);
        lv.setDoRefreshOnUIChanged(false);
        lv.setOnLoadListener(this);
        lv.setOnRefreshListener(this);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialogUtils.showDialog(getContext(), "提示", "确认删除此条信息", new AlertDialogUtils.DialogClickListener() {
                    @Override
                    public void onConfirmClick(AlertDialog alertDialog) {
                        Log.i("onConfirmClick","position=="+position);
                        Map<String,String> map = new HashMap<String, String>();
                        if (BaseApplication.patient != null) {
                            map.put("userId",""+BaseApplication.patient.getUserId());
                            map.put("traId",""+mData.get(position-1).getId());
                            mDelPosition = position -1;
                            HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC).postDeleteMessage(map,DEL_MESSAGE,MessageFragment.this);
                        }
                        alertDialog.dismiss();

                    }

                    @Override
                    public void onCancelClick(AlertDialog alertDialog) {
                        alertDialog.dismiss();
                    }
                },"取消","确认");
                return false;
            }
        });
        mCache = CacheUtils.get(getActivity());
        if (mData.size() > 0){
            mData.clear();
        }
        initialData();
    }

    /**
     * 筛选后获取医生列表
     */
//    private void getData() {
//        lv.setSelection(0);
//        if (adapter.mList != null) {// 返回刷新，如果之前adapter，则清除
//            adapter.mList.clear();
//            lv.setAdapter(adapter);
//            lv.setVisibility(View.GONE);
//        }
//        currentPage = 1;// 回调返回初始化页码
//        initialData();
//    }

    /**
     * 数据
     */
    private void initialData() {
        Map<String, String> map = new HashMap<>();
        if (BaseApplication.patient != null) {
            map.put("userId", BaseApplication.patient.getUserId() + "");
            map.put("page", "" + mCurrentPage);
            map.put("rowsPerPage", "15");
            HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC).postMessage(map, GET_MESSAGE, this);
        }
    }

    @Override
    protected void onFragmentFirstResume() {
    }

    @Override
    public void onStart(int requestType) {
//        showProgressDialog("加载中...");
        showProgressToast();
    }

    @Override
    public void onCompleted(int requestType) {
//        dismissProgressDialog();
        hideProgressToast();
        lv.onRefreshComplete();
        lv.onLoadMoreComplete();
        Log.i("size==","size=="+mData.size());
    }

    @Override
    public void onError(int requestType) {
//        dismissProgressDialog();
        hideProgressToast();
        lv.onRefreshComplete();
        lv.onLoadMoreComplete();
    }

    @Override
    public void onNext(IModel t, int requestType) {
        switch (requestType) {
            case GET_MESSAGE:
                Response<MessageResponse> messageResponse = (Response<MessageResponse>) t;
                MessageResponse data = messageResponse.getData();
                if (data != null) {
                    List<MessageResponse.RowsBean> rowsBeen = data.getRows();
                    if (rowsBeen != null) {
                        if (rowsBeen.size() > 0) {
                            isAllData = false;
                            mData.addAll(rowsBeen);
                        } else {
                            isAllData = true;
                        }
                        if (mData.size() <= 0) {
                            mNoMessageFl.setVisibility(View.VISIBLE);
                            lv.setVisibility(View.GONE);
                        } else {
                            mNoMessageFl.setVisibility(View.GONE);
                            lv.setVisibility(View.VISIBLE);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }
                break;
            case DEL_MESSAGE:
                mData.remove(mDelPosition);
                mAdapter.notifyDataSetChanged();
                ToastUtils.shortToast(getContext(),"删除成功");
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
//        lv.setAutoLoadMore(true);
        mCurrentPage = 1;
        mData.clear();
        initialData();
    }

    @Override
    public void onLoadMore() {
//        if (messageList != null) {
//            if (messageList.size() < pageSize
//                    || (messageList.size() > pageSize && messageList.size()
//                    % pageSize != 0)) {
//                lv.setAutoLoadMore(false);
//                lv.onLoadMoreComplete();
//            } else {
//                mCurrentPage++;
//                initialData();
//            }
//        }
        if (isAllData){
            ToastUtils.shortToast(getContext(),"已加载全部数据");
        }else {
            mCurrentPage++;
            initialData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        dismissProgressDialog();
        hideProgressToast();
    }

    @OnItemClick(R.id.message_lv)
    public void msgItemClick(AdapterView<?> parent, View view, int position,
                             long id) {
        if (position - 1 > 0 && (position - 1) < messageList.size())
            startActivity(new Intent(getActivity(),
                    ActivityMessageDetail.class).putExtra("msgDetail", messageList
                    .get(position - 1).getContentStr()));
    }

    /**
     * 刷新数据。
     */

    public void RefreshData() {
        if (mCurrentPage == 1 && mData.size() > 0){
            mData.clear();
        }
        initialData();
    }

}
