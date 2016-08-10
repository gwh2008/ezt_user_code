package com.eztcn.user.eztcn.adapter;

import java.util.List;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.MedicalRecord_ImgType;
import com.eztcn.user.eztcn.bean.Medical_img;
import com.eztcn.user.eztcn.db.EztDictionaryDB;

/**
 * @title 创建病历
 * @describe
 * @author ezt
 * @created 2015年3月19日
 */
public class CreateEMRAdapter extends BaseArrayListAdapter<Medical_img>
		implements OnClickListener {

	private EMR_ImageAdapter adapter;
	private int showView = 0;

	public CreateEMRAdapter(Activity context) {
		super(context);
	}

	public CreateEMRAdapter(Activity context, int showView) {
		super(context);
		this.showView = showView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_emr_img, null);
			holder.imgLayout = (GridView) convertView
					.findViewById(R.id.imgList);
//			holder.imgLayout = (CustomGridView) convertView //2015-12-24 修改上传病历界面显示不合理问题
//					.findViewById(R.id.imgList);
			
			
			
			holder.txtLabel = (TextView) convertView
					.findViewById(R.id.txtLabel);
			holder.selectedType = (TextView) convertView
					.findViewById(R.id.selectedType);
			holder.addImg = (ImageView) convertView.findViewById(R.id.addImg);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Medical_img img = mList.get(position);
		holder.selectedType.setText(EztDictionaryDB.getInstance(mContext)
				.getLabelByTag("mrPicType", img.getRecordType()));
		initGridView(holder.imgLayout, holder.addImg, img.getUrlList(),
				position);
		if (showView == 0) {
			holder.selectedType.setText(EztDictionaryDB.getInstance(mContext)
					.getLabelByTag("mrPicType", img.getRecordType()));
			holder.selectedType.setTag(position);
			holder.selectedType.setOnClickListener(this);
			holder.addImg.setTag(holder);
			holder.addImg.setOnClickListener(this);
		} else {
			holder.selectedType.setVisibility(View.GONE);
			holder.txtLabel.setTextColor(mContext.getResources().getColor(
					R.color.dark_gray));
			holder.txtLabel.setText(EztDictionaryDB.getInstance(mContext)
					.getLabelByTag("mrPicType", img.getRecordType()));
		}
		return convertView;
	}

	public void initGridView(GridView gridView, ImageView add,
			List<MedicalRecord_ImgType> list, final int p) {
		if (list.size() >= 5) {//2015-12-24 修改上传病历界面显示不合理问题
//		if (list.size() >= 4) {
			add.setVisibility(View.GONE);
		} else {
			add.setVisibility(View.VISIBLE);
		}
		adapter = new EMR_ImageAdapter(mContext);
		gridView.setAdapter(adapter);
		adapter.setList(list);
		if (showView == 1) {
			add.setVisibility(View.GONE);
			return;
		}
		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				listener.deleteClick(view, p, position);
				return false;
			}
		});
	}

	public class ViewHolder {
		ImageView addImg;
		TextView selectedType;
		TextView txtLabel;
		GridView imgLayout; //2015-12-24 修改上传病历界面显示不合理问题
//		CustomGridView imgLayout;
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.addImg) {
			ViewHolder holder = (ViewHolder) v.getTag();
			Integer position = (Integer) holder.selectedType.getTag();
			listener.addClick(position);
		} else {
			Integer position = (Integer) v.getTag();
			listener.selectType(v, position);
		}
	}

	onAddClickListener listener;

	public void setOnAddListener(onAddClickListener listener) {
		this.listener = listener;
	}

	public interface onAddClickListener {

		public void addClick(int position);

		public void selectType(View view, int position);

		public void deleteClick(View view, int line, int position);
	}

}
