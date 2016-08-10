/**
 * 
 */
package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.InsItemDetial;

/**
 * @author Liu Gang
 * 
 *         2015年11月25日 下午2:25:53 检验报告详情适配器
 */
public class InsDetailAdapter extends BaseArrayListAdapter<InsItemDetial> {

	public InsDetailAdapter(Activity context) {
		super(context);
		// TODO 自动生成的构造函数存根
	}

	class ViewHolder {
		/**
		 * 患者的检验结果 ep. 检验结果：10
		 */
		TextView tvResult;
		/**
		 * 检验小项目 名称
		 */
		TextView tvName;
		/**
		 * 检验标准范围 ep. 标准值 : 8 ~ 15
		 */
		TextView tvRange;
	}

	ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext,
					R.layout.item_lins_detail_list, null);
			holder.tvResult = (TextView) convertView
					.findViewById(R.id.tvReportResult);
			holder.tvRange = (TextView) convertView
					.findViewById(R.id.tvReportRange);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.tvInsDName);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		InsItemDetial insItemDetial = mList.get(position);
		holder.tvName.setText(insItemDetial.getDetailName());
		if (null == insItemDetial.getSampResult()) {
			holder.tvResult.setText("检验结果：暂无数据");
		} else
			holder.tvResult.setText("检验结果：" + insItemDetial.getSampResult());
		if (null == insItemDetial.getStandardValue()) {
			holder.tvRange.setText("标准值：无");
		} else
			holder.tvRange.setText("标准值：" + insItemDetial.getStandardValue());
		return convertView;
	}

}
