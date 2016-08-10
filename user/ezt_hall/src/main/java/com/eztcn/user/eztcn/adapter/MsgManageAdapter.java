package com.eztcn.user.eztcn.adapter;

import java.util.HashSet;
import java.util.Set;

import xutils.BitmapUtils;
import xutils.bitmap.BitmapCommonUtils;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.home.MsgManageActivity;
import com.eztcn.user.eztcn.bean.MsgType;
import com.eztcn.user.eztcn.fragment.MsgManageFragment;
import com.eztcn.user.eztcn.utils.StringUtil;

/**
 * @title 消息箱adapter
 * @describe
 * @author ezt
 * @created 2014年12月15日
 */
@SuppressLint("UseSparseArrays")
public class MsgManageAdapter extends BaseArrayListAdapter<MsgType> {

//	private FinalBitmap fb;
private BitmapUtils bitmapUtils;
	private Bitmap defaultBit;
	public MsgManageFragment fragment;
	public Set<Integer> checkedRecId;
	private MsgManageActivity activity;

	public MsgManageAdapter(MsgManageFragment fragment) {
		super(fragment.getActivity());
		if (null != fragment)
			this.fragment = fragment;
//		fb = FinalBitmap.create(fragment.getActivity());
		
		
		defaultBit = BitmapFactory.decodeResource(fragment.getActivity()
				.getResources(), R.drawable.logo_1);
		
		bitmapUtils=new BitmapUtils(fragment.getActivity());
		bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(mContext).scaleDown(3));
	        bitmapUtils.configDefaultLoadingImage(defaultBit);
		    bitmapUtils.configDefaultLoadFailedImage(defaultBit);
		
		checkedRecId = new HashSet<Integer>();// 保存所有选中的
	}

	public MsgManageAdapter(MsgManageActivity activity) {
		super(activity);
		this.activity = activity;
//		fb = FinalBitmap.create(activity);
		bitmapUtils=new BitmapUtils(activity);
		
		defaultBit = BitmapFactory.decodeResource(activity.getResources(),
				R.drawable.logo_1);
		
		bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(mContext).scaleDown(3));
        bitmapUtils.configDefaultLoadingImage(defaultBit);
	    bitmapUtils.configDefaultLoadFailedImage(defaultBit);
		
		checkedRecId = new HashSet<Integer>();// 保存所有选中的
	}

	public boolean isShow = false;// 标记是否显示多选框，默认false为不显示
	public boolean isSelectAll = false;// 是否全选

	class ViewHolder {
		TextView tvInfo;
		TextView tvTime;
		TextView tvTitle;
		TextView tvMsgCount;
		ImageView imgMsg;
		CheckBox cbChoice;
	}

	ViewHolder holder;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View
					.inflate(mContext, R.layout.item_msg_manage, null);
			holder.tvInfo = (TextView) convertView
					.findViewById(R.id.item_msg_info);// 消息内容
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.item_msg_time);// 消息时间
			holder.tvTitle = (TextView) convertView
					.findViewById(R.id.item_msg_title);// 消息标题
			holder.imgMsg = (ImageView) convertView
					.findViewById(R.id.item_msg_pic);// 消息头像
			holder.cbChoice = (CheckBox) convertView
					.findViewById(R.id.item_msg_cb);// 选择框
			holder.tvMsgCount = (TextView) convertView
					.findViewById(R.id.msg_item_msg);// 消息条数

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final MsgType type = mList.get(position);
		if (type != null) {
			holder.tvInfo.setText(type.getTypeContent());
			holder.tvTime
					.setText(StringUtil.judgeTime(type.getCreateTypeTime()));
			holder.tvTitle.setText(type.getTypeTitle());
			if (type.getClickState() == 0) {// 未点击
				holder.tvMsgCount.setVisibility(View.VISIBLE);
				holder.tvMsgCount.setText(type.getTypeCount() + "");
			} else {//
				holder.tvMsgCount.setVisibility(View.GONE);
			}

			final String imgurl = "";
//			fb.display(holder.imgMsg, imgurl, defaultBit, defaultBit);
			bitmapUtils.display(holder.imgMsg, imgurl);
		}

		if (isSelectAll) {// 全选
			holder.cbChoice.setChecked(true);
			checkedRecId.add(position);

		} else {// 取消全选
			holder.cbChoice.setChecked(false);
			if (checkedRecId.size() > 0)
				checkedRecId.remove(position);
		}

		if (isShow) {// 显示
			holder.cbChoice.setVisibility(View.VISIBLE);
			if(null!=activity){
			holder.cbChoice
					.startAnimation(((MsgManageActivity) mContext).showAnim);}
			else{
				holder.cbChoice
				.startAnimation(((MsgManageFragment)fragment).showAnim);	
			}
			// holder.arrow.setVisibility(View.GONE);
		} else {// 隐藏
			holder.cbChoice.setVisibility(View.GONE);
			holder.cbChoice.clearAnimation();
			// holder.arrow.setVisibility(View.VISIBLE);
		}
		holder.cbChoice
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// 如果是未选中的CheckBox,则添加动画
						// if (!mSelectMap.containsKey(position)
						// || !mSelectMap.get(position)) {
						// addAnimation(holder.checkbox);
						// }

						if (isChecked) {
							if (checkedRecId.size() < 1) {
								if (null != activity) {
									activity.tvDel.setEnabled(true);
									activity.tvDel.setTextColor(mContext
											.getResources().getColor(
													android.R.color.white));
								} else {
									fragment.tvDel.setEnabled(true);
									fragment.tvDel.setTextColor(mContext
											.getResources().getColor(
													android.R.color.white));
								}

								// activity.tvDel.setVisibility(View.VISIBLE);
							}
							checkedRecId.add(position);
							if (checkedRecId.size() == mList.size()) {// 选满时全选按钮选中
								if (null != activity) {
									activity.cbChoiceAll.setChecked(true);
								} else {
									fragment.cbChoiceAll.setChecked(true);
								}
							}

						} else {
							checkedRecId.remove(position);
							if (checkedRecId.size() < 1) {
								if (null != activity) {
									activity.tvDel.setEnabled(false);
									// activity.tvDel.setVisibility(View.GONE);
									activity.tvDel.setTextColor(mContext
											.getResources().getColor(
													R.color.light_gray));
								} else {
									fragment.tvDel.setEnabled(false);
									// activity.tvDel.setVisibility(View.GONE);
									fragment.tvDel.setTextColor(mContext
											.getResources().getColor(
													R.color.light_gray));
								}

							} else if (checkedRecId.size() == mList.size() - 1) {// 未选满时取消全选
								if (null != activity) {
									activity.flag = 1;
									activity.cbChoiceAll.setChecked(false);
									activity.flag = 0;// 初始化
								} else {
									fragment.flag = 1;
									fragment.cbChoiceAll.setChecked(false);
									fragment.flag = 0;// 初始化
								}

							}
						}

					}
				});

		return convertView;
	}

}
