package com.eztcn.user.hall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.eztcn.user.R;

import com.eztcn.user.R;
import com.eztcn.user.hall.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zll on 2016/6/2.
 * 预约复印病历页面
 */
public class OrderCopyCaseActivity extends BaseActivity {
    private ListView listview_show;
    private List<String> list;
    private MyAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTitleBar(true,"复印病历",null);

    }


    @Override
    protected int preView() {
        return R.layout.new_activity_order_copycase;
    }

    /**
     * 初始化view控件
     */
    @Override
    protected void initView() {
        listview_show = (ListView) findViewById(R.id.listview_show);
        list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add("");
        }
        adapter = new MyAdapter();
        listview_show.setAdapter(adapter);
        listview_show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //ToastUtils.shortToast(mContext,"您点击了item");
                Intent intent=new Intent();
                intent.setClass(mContext,CopyCaseOrderActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void initData() {

    }



    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.new_order_copy_case_list_item, null);
                viewHolder.text_hosiptal_name = (TextView) convertView.findViewById(R.id.text_hosiptal_name);
                viewHolder.img_arrow = (ImageView) convertView.findViewById(R.id.img_arrow);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            return convertView;
        }
    }

    class ViewHolder {
        private TextView text_hosiptal_name;
        private ImageView img_arrow;

    }
}
