package com.eztcn.user.eztcn.adapter;

import xutils.BitmapUtils;
import xutils.bitmap.BitmapCommonUtils;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.InformateComment;

/**
 * @title 资讯评价adapter
 * @describe
 * @author ezt
 * @created 2014年12月31日
 */
public class EvaluateListAdapter extends BaseArrayListAdapter<InformateComment>
		implements OnClickListener {

//	private FinalBitmap fb;
private BitmapUtils bitmapUtils;
	private Bitmap defaultBit;

	public EvaluateListAdapter(Activity context) {
		super(context);
//		fb = FinalBitmap.create(context);
		defaultBit = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.button_green);
		
		bitmapUtils=new BitmapUtils(context);
		bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(mContext).scaleDown(3));
	        bitmapUtils.configDefaultLoadingImage(defaultBit);
		    bitmapUtils.configDefaultLoadFailedImage(defaultBit);
	}

	static class ViewHolder {
		TextView tvName, tvTime, tvContent, tvReply;
		ImageView img;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mContext.getLayoutInflater().inflate(
					R.layout.item_evaluate_informate, null);

			holder.tvName = (TextView) convertView
					.findViewById(R.id.item_evaluate_name);// 读者姓名
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.item_evaluate_time);// 评论时间
			holder.tvContent = (TextView) convertView
					.findViewById(R.id.item_evaluate_content);// 内容
			holder.tvReply = (TextView) convertView
					.findViewById(R.id.item_evaluate_reply);// 回复
			holder.img = (ImageView) convertView
					.findViewById(R.id.item_evaluate_img);// 头像

			holder.tvReply.setTag(position);
			holder.tvReply.setOnClickListener(this);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		InformateComment comment = mList.get(position);

		holder.tvName.setText(comment.getUserName());
		holder.tvTime.setText(comment.getCreateTime());
		holder.tvContent.setText(comment.getContent());

		final String imgurl = "";
//		fb.display(holder.img, imgurl, defaultBit, defaultBit);
		bitmapUtils.display(holder.img, imgurl);
		return convertView;
	}

	@Override
	public void onClick(View v) {
		int pos = (Integer) v.getTag();
		this.callBack.onclickReply(pos);
	}

	public interface ReplyCallBack {
		public void onclickReply(int pos);
	}

	ReplyCallBack callBack;

	public void Reply(ReplyCallBack callBack) {
		this.callBack = callBack;
	}

}
