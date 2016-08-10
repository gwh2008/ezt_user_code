package com.eztcn.user.hall.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.hall.model.ResultResponse.RankListDataResponse;

import java.util.ArrayList;

/**
 * 医生排名需要的fragment
 * @author 蒙
 */
public class DoctorRankingFragment extends BaseFragment {
	ListView listView;
	private int type;//类型
	private MyAdapter adapter=new MyAdapter();
    private ArrayList<RankListDataResponse> datas=new ArrayList<>();

	/**
	 * 获取fragment对象，
	 * @param type  类型，1:周排名 2：月排名，3：年排名
	 * @return
	 */
	public static DoctorRankingFragment newInstance(int type) {
        DoctorRankingFragment fragment = new DoctorRankingFragment();
		fragment.type=type;
		return fragment;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        View view=null;
        if (view==null){
            view = inflater.inflate(R.layout.new_fragment_doctor_ranking, null);
        }
		initListview(view);
		return view;
	}

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){//加载网络数据

        }
    }

    public ArrayList<RankListDataResponse> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<RankListDataResponse> datas) {
        this.datas = datas;

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onFragmentFirstResume() {

    }

    /**
     * 初始化listview
     * @param view
     */
	private void initListview(View view) {
        listView=(ListView) view.findViewById(R.id.listView);
        listView.setAdapter(adapter);
	}

	class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return datas.size()>10?10:datas.size();
		}

		@Override
		public Object getItem(int position) {
			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		} 

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(mContext,R.layout.new_fragment_racking_list_item, null);
				holder = new ViewHolder();
				holder.order_image=(ImageView) convertView.findViewById(R.id.new_fragment_ranking_list_item_order_image);
				holder.order_text=(TextView) convertView.findViewById(R.id.new_fragment_ranking_list_item_order_text);
                holder.isAdd=(ImageView) convertView.findViewById(R.id.new_fragment_ranking_list_item_isAdd);
                holder.count=(TextView) convertView.findViewById(R.id.new_fragment_ranking_list_item_count);
                holder.name=(TextView) convertView.findViewById(R.id.new_fragment_ranking_list_item_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

            holder.name.setText(datas.get(position).getName());
            if ("1".equals(datas.get(position).getFlag())){
                holder.isAdd.setImageResource(R.drawable.new_ranking_add);
            }else{
                holder.isAdd.setImageResource(R.drawable.new_ranking_reduce);
            }
            holder.count.setText(datas.get(position).getVol());
            holder.order_image.setVisibility(View.VISIBLE);
            holder.order_text.setVisibility(View.GONE);
            if (position==0){
                holder.order_image.setImageResource(R.drawable.new_ranking_glod);
            }else if (position==1){
                holder.order_image.setImageResource(R.drawable.new_ranking_silver);
            }else if (position==2){
                holder.order_image.setImageResource(R.drawable.new_ranking_copper);
            }else{
                holder.order_image.setVisibility(View.GONE);
                holder.order_text.setVisibility(View.VISIBLE);
                holder.order_text.setText(datas.get(position).getRank());
            }

			return convertView;
		}

		class ViewHolder {
			private ImageView order_image;//代表前三名的图片
            private TextView order_text;//排名的数字
            private ImageView isAdd;//排名上涨或者下跌的图片
            private TextView count;//预约量
            private TextView name;//医生名字
		}
	}
	
}
