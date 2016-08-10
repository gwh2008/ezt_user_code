package cn.column.app.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;

import java.util.ArrayList;
import cn.column.app.common.entity.TitleEntity;
import cn.column.app.ui.custom.pdgrid.PagedDragDropGrid;
import cn.column.app.ui.custom.pdgrid.PagedDragDropGridAdapter;


public class DragDropGridAdapter implements  PagedDragDropGridAdapter{
	PagedDragDropGrid gridview;
	ArrayList<TitleEntity> list;
	Activity activity;
	ServiceColumnAdapter adapter;
	private LayoutInflater mInflater;
	public DragDropGridAdapter(Activity ac, PagedDragDropGrid gv, ArrayList<TitleEntity> l, ServiceColumnAdapter at)
	{
		adapter=at;
		activity=ac;
		gridview=gv;
		list=l;
		mInflater = LayoutInflater.from(activity);
	}
	@Override
	public int pageCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int itemCountInPage(int page) {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public View view(int page, int index) {
		// TODO Auto-generated method stub
		
		View convertView = mInflater.inflate(R.layout.column_edit_items, null);
		TextView tvTitle=(TextView)convertView.findViewById(R.id.column_tv_newstitle);
		TextView tvId=(TextView)convertView.findViewById(R.id.column_tv_id);
		tvTitle.setText(list.get(index).getTitleName());
		tvId.setText(list.get(index).getTitleid());
		if(index==0 ||index==1)
		{
			//单独设置item的颜色。
			tvTitle.setTextColor(activity.getResources().getColor(R.color.colorBlack));
			
		}
		convertView.setTag(index);
		convertView.setOnClickListener(onClickListener);
		convertView.setOnLongClickListener(onLongClickListener);
		return convertView;
	}

	private OnClickListener onClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Toast.makeText(activity, "view" +v.getTag(), Toast.LENGTH_SHORT).show();  
			showDelColumnAnim(v);
		}
	};
	
	private OnLongClickListener onLongClickListener=new OnLongClickListener() {
		
		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			return gridview.onLongClick(v);
		}
	};
	
	private void showDelColumnAnim(View v)
	{
		int index=Integer.parseInt(v.getTag().toString());
		//if(index==0||index==1)return ;//防止第一个第二个被删除。固定第一个或者第二个item。
		
		adapter.addItems(list.get(index));
		this.deleteItem(0,index);
		
	}
	
	
	@Override
	public int rowCount() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public int columnCount() {
		// TODO Auto-generated method stub
		return AUTOMATIC;
	}

	@Override
	public void printLayout() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void swapItems(int pageIndex, int itemIndexA, int itemIndexB) {
		// TODO Auto-generated method stub
		
		//B起始位置 
		//A目标位置
		Toast.makeText(activity, "select" + itemIndexA+" ---select "+itemIndexB, Toast.LENGTH_SHORT).show();  
	}

	@Override
	public void moveItemToPreviousPage(int pageIndex, int itemIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveItemToNextPage(int pageIndex, int itemIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteItem(int pageIndex, int itemIndex) {
		// TODO Auto-generated method stub
		list.remove(itemIndex);
		gridview.notifyDataSetChanged();
	}

	@Override
	public int deleteDropZoneLocation() {
		// TODO Auto-generated method stub
		return BOTTOM;
	}

	@Override
	public boolean showRemoveDropZone() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
