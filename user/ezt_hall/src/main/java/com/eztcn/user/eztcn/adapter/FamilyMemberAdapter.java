package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.mine.AddMemberActivity;
import com.eztcn.user.eztcn.bean.FamilyMember;
import com.eztcn.user.eztcn.customView.RoundImageView;
import com.eztcn.user.eztcn.db.EztDictionaryDB;
import com.eztcn.user.eztcn.utils.StringUtil;

/**
 * @title 家庭成员
 * @describe
 * @author ezt
 * @created 2014年12月12日
 */
public class FamilyMemberAdapter extends BaseArrayListAdapter<FamilyMember>
		implements OnClickListener {

	public FamilyMemberAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		FamilyMember member;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.familymember_item, null);
			holder.memberName = (TextView) convertView
					.findViewById(R.id.memberName);
			holder.edit = (TextView) convertView.findViewById(R.id.edit);
			holder.relation = (TextView) convertView
					.findViewById(R.id.relation);
			holder.sex = (TextView) convertView.findViewById(R.id.sex);
			holder.age = (TextView) convertView.findViewById(R.id.age);
			holder.idCard = (TextView) convertView.findViewById(R.id.idCard);
			holder.phone = (TextView) convertView.findViewById(R.id.phone);
			// holder.tvDrug = (TextView) convertView
			// .findViewById(R.id.family_m_drug);// 药物过敏史

			holder.tvHealthCare = (TextView) convertView
					.findViewById(R.id.family_m_health_care);// 医保卡号

			holder.imgHead = (RoundImageView) convertView
					.findViewById(R.id.family_m_headimg);// 头像

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		member = mList.get(position);
		holder.memberName.setText(member.getMemberName());
		holder.relation.setText(EztDictionaryDB.getInstance(mContext)
				.getLabelByTag("kinship", member.getRelation()+""));
		if (member.getMainUser() == 1) {// 自己
			holder.edit.setVisibility(View.GONE);
		}else{
			holder.edit.setVisibility(View.VISIBLE);
		}
		holder.age.setText(member.getAge() + "");
		if (member.getIdCard() != null) {
			holder.age.setText(StringUtil.getAgeByIdCard(member.getIdCard())
					+ "");
		}
		if (member.getSex() == 0) {
			holder.sex.setText("男");
//			holder.imgHead.setImageResource(R.drawable.userman);
			holder.imgHead.setImageResource(R.drawable.userdefault);
		} else {
			holder.sex.setText("女");
//			holder.imgHead.setImageResource(R.drawable.userwomen);
			holder.imgHead.setImageResource(R.drawable.userdefault);
		}
		holder.idCard.setText(member.getIdCard());
		holder.phone.setText(member.getPhone());
		holder.edit.setOnClickListener(this);
		holder.edit.setTag(member);
		// holder.tvDrug.setText("暂无");
		holder.tvHealthCare.setText(member.getMedicalNo());
		return convertView;
	}

	class ViewHolder {
		TextView edit;
		TextView relation, sex, age, idCard, phone, memberName, tvHealthCare;
		RoundImageView imgHead;
	}

	/**
	 * 判断关系
	 * 
	 * @param tv
	 * @param relationId
	 */
	// public void judgeRelation(TextView tv, int relationId) {
	// final DictConstantsBean rList = EztDictionaryUtil.getInstence()
	// .getDictListByEn("kinship");
	// String[] nameArrys = new String[rList.getLabelValues().size()];
	// LabelValue dict = null;
	// for (int i = 0; i < nameArrys.length; i++) {
	// dict = rList.getLabelValues().get(i);
	// if (relationId == Integer.parseInt(dict.getValue())) {
	// tv.setText(dict.getLabel());
	// }
	// }
	// }

	@Override
	public void onClick(View v) {
		FamilyMember member = (FamilyMember) v.getTag();
		if (member == null) {
			return;
		}
		Intent intent = new Intent(mContext, AddMemberActivity.class);
		intent.putExtra("member", member);
		mContext.startActivityForResult(intent, 2);
	}
}
