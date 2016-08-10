package com.eztcn.user.hall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lizhipeng
 * @Data: 16/4/29 下午1:52
 * @Description: 一千零医夜 是配器
 */
public class OneThousandDoctorRVAdapter extends RecyclerView.Adapter<OneThousandDoctorRVAdapter.ViewHolder> {

    private int mLayoutId;

    /**
     * ItemClick的回调接口
     *
     * @author
     */
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private LayoutInflater mInflater;
    private List<String> mDatas;
    private Context mContext;

    public OneThousandDoctorRVAdapter(Context context, int layoutId, List<String> datats) {
        mInflater = LayoutInflater.from(context);
        mDatas = new ArrayList<>();
        if (datats != null && datats.size() > 0) {
            mDatas.addAll(datats);
        }
        mContext = context;
        mLayoutId = layoutId;
    }

    public void notifyDataSetChanged(List<String> datats) {
        mDatas.clear();
        mDatas.addAll(datats);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        ImageView mPayIv;
        ImageView mSexIv;
        TextView mTxt;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(mLayoutId,viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
//        viewHolder.mTxt = (TextView) view.findViewById(R.id.item_text);
//        viewHolder.mPayIv = (ImageView) view.findViewById(R.id.item_patient_fragment_recycler_view_iv_pay);
//        viewHolder.mSexIv = (ImageView) view.findViewById(R.id.item_patient_fragment_recycler_view_iv_sex);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
//        Glide.with(mContext).load(mDatas.get(i)).into(viewHolder.mImg);
//        viewHolder.mTxt.setText(mDatas.get(i));
        switch (i % 2) {
            case 0:
                break;
            case 1:
                break;
            default:
                break;
        }
        //如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, i);
                }
            });

        }

    }

}
