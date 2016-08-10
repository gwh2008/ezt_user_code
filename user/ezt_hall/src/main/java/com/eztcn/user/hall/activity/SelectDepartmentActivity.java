package com.eztcn.user.hall.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.Request;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.model.ResultResponse.DepartmentListDataResponse;
import com.eztcn.user.hall.model.ResultResponse.HospitalListResponse;
import com.eztcn.user.hall.utils.GlideUtils;
import com.eztcn.user.hall.utils.LogUtils;
import com.eztcn.user.hall.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
*
 * 选择科室页面
 *
 * @author 蒙
 */

public class SelectDepartmentActivity extends BaseActivity implements View.OnClickListener,
        ResultSubscriber.OnResultListener {
    private ArrayList<DepartmentListDataResponse> datasOne = new ArrayList<>();//一级数据源
    private ArrayList<DepartmentListDataResponse> datasTwo = new ArrayList<>();//二级数据源
    private ArrayList<DepartmentListDataResponse> datasThree = new ArrayList<>();//三级数据源
    private ExpandableListView expandablelistview;//一级的ExpandableListView
    private GroupExpandableListAdapter groupAdapter = new GroupExpandableListAdapter();//一级的ExpandableListView的适配器
    private TextView hospital_name;
    private HospitalListResponse hospitalListResponse;//上个页面传过来的医院信息
    private ImageView hospital_pic;
    private HashMap<Integer,ChildeExpListview> expandableListViews = new HashMap<Integer,ChildeExpListview>();//某个二级列表中的所有的ChildeExpListview
    private HashMap<Integer,ChildExpandableListAdapter> adatpers = new HashMap<Integer,ChildExpandableListAdapter>();//某个二级列表中的所有的ChildeExpListview得适配器
    private int index_twoLevel = -1;//二级列表中选中的条目的索引值

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTitleBar(true, "选择科室", null);
    }

    @Override
    protected int preView() {
        return R.layout.new_activity_selectpartment;
    }

    public void initView() {
        expandablelistview = (ExpandableListView) findViewById(R.id.new_activity_select_department_hospital_expandableListView);
        hospital_name = (TextView) findViewById(R.id.new_activity_select_department_hospital_name);
        hospital_pic = (ImageView) findViewById(R.id.new_activity_select_department_hospital_pic);
    }


    @Override
    protected void initData() {
        findViewById(R.id.new_activity_select_department_hospital_relativeLayout).setOnClickListener(this);
        hospitalListResponse = (HospitalListResponse) getIntent().getSerializableExtra("hospital");
        hospital_name.setText(hospitalListResponse.getEhName());
        GlideUtils.into(mContext, HTTPHelper.HOSPITAL_PIC + "hosView" + hospitalListResponse.getId() + ".jpg", hospital_pic, R.drawable.new_hospital_default);

        expandablelistview.setAdapter(groupAdapter);

        //一级expandablelistview点击展开时的监听
        expandablelistview.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                //清理之前加载的掉旧的数据，然后让适配器更新
                expandableListViews.clear();
                adatpers.clear();
                datasTwo.clear();
                groupAdapter.notifyDataSetChanged();
                for (int i = 0; i < datasOne.size(); i++) {//将非选中的条目都关闭
                    if (i != groupPosition) {
                        expandablelistview.collapseGroup(i);
                    }
                }

                if ("1".equals(datasOne.get(groupPosition).getIsDept())) {//如果该一级列表没有子元素，将此列表当做根节点处理，进行页面的跳转
                    //将该列表再次关闭
                    expandablelistview.collapseGroup(groupPosition);

                    //进入选择科室页
                    startActivity(new Intent(mContext, SelectDoctorsListActivity.class)
                            .putExtra("departmentId", datasOne.get(groupPosition).getDtpId())
                            .putExtra("hospitalId",hospitalListResponse.getId()));
                    return;
                }

                //获取对应的二级列表数据
                getTwoLevelDepartmentData(datasOne.get(groupPosition).getDptCateId());
            }

        });

        //进入页面就直接加载一级数据
        getOneLevelDepartmentData();
    }

    /**
     * 获取一级科室数据
     */
    public void getOneLevelDepartmentData() {
        showProgressDialog("加载科室中...");
        Request request = new Request();
        Map<String, String> params = new HashMap<>();
        params.put("hospitalId", hospitalListResponse.getId());//医院id
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC).getOneLevelDepartmentData(request.getFormMap(params), 111, this);

    }

    /**
     * 获取二级科室数据
     */
    public void getTwoLevelDepartmentData(String pid) {
        showProgressDialog("加载科室中...");
        Request request = new Request();
        Map<String, String> params = new HashMap<>();
        params.put("hospitalId", hospitalListResponse.getId());//医院id
        params.put("dptCateId", pid);//科室id
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC).getTwoLevelDepartmentData(request.getFormMap(params), 222, this);

    }

    /**
     * 获取三级科室数据
     */
    public void getThreeLevelDepartmentData(String dptCateId) {
        showProgressDialog("加载科室中...");
        Request request = new Request();
        Map<String, String> params = new HashMap<>();
        params.put("hospitalId", hospitalListResponse.getId());//医院id
        params.put("dptCateId", dptCateId);//科室id
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC).getThreeLevelDepartmentData(request.getFormMap(params), 333, this);

    }


    class GroupExpandableListAdapter extends BaseExpandableListAdapter {
        @Override
        public int getGroupCount() {//一级列表的个数
            return datasOne.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {//二级列表的个数
            return datasTwo.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return datasOne.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return datasTwo.get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            ViewHolder mHolder = null;
            if (convertView == null) {
                mHolder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.new_select_department_list_item_one_level, null);
                mHolder.name = (TextView) convertView.findViewById(R.id.department_name);
                mHolder.all_count = (TextView) convertView.findViewById(R.id.all_count);
                mHolder.remain_count = (TextView) convertView.findViewById(R.id.remain_count);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }

            //对一级列表进行数据的显示
            mHolder.name.setText(datasOne.get(groupPosition).getDptName());
            mHolder.remain_count.setText(datasOne.get(groupPosition).getRpRemainNum());
            mHolder.all_count.setText(datasOne.get(groupPosition).getRpNum());
            return convertView;
        }

        class ViewHolder {
            private TextView name;
            private TextView all_count;
            private TextView remain_count;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            //每一个二级列表的条目就是一个单独的ChildExpandableListAdapter，和单独的适配器
            ChildExpandableListAdapter carStyleAdapter = new ChildExpandableListAdapter(datasTwo.get(childPosition));
            //将生成的适配器加入集合，以便统一处理
            adatpers.put(childPosition,carStyleAdapter);

            ChildeExpListview secondLevelexplv = new ChildeExpListview(mContext);
            //将生成的ChildeExpListview加入集合，以便统一处理
            expandableListViews.put(childPosition,secondLevelexplv);

            secondLevelexplv.setGroupIndicator(null);
            secondLevelexplv.setAdapter(carStyleAdapter);

            //对每一个二级列表做展开监听
            secondLevelexplv.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                @Override
                public void onGroupExpand(int groupPosition) {
                    //记录选中的二级列表的条目索引
                    index_twoLevel = childPosition;

                    //将三级数据清空，让适配器更新
                    datasThree.clear();
                    for (int i = 0; i < adatpers.size(); i++) {
                        adatpers.get(i).notifyDataSetChanged();
                    }
                    //关闭非选中的二级列表
                    for (int i = 0; i < expandableListViews.size(); i++) {
                        if (i != childPosition) {
                            expandableListViews.get(i).collapseGroup(i);
                        }
                    }
                    if ("1".equals(datasTwo.get(childPosition).getIsDept())) {//如果该二级列表没有子元素，将此列表当做根节点处理，进行页面的跳转
                        //将次列表再次关闭
                        expandableListViews.get(childPosition).collapseGroup(childPosition);

                        startActivity(new Intent(mContext, SelectDoctorsListActivity.class)
                                .putExtra("departmentId", datasTwo.get(childPosition).getDtpId())
                                    .putExtra("hospitalId",hospitalListResponse.getId()));
                        return;
                    }

                    //加载对应的三级列表的数据
                    getThreeLevelDepartmentData(datasTwo.get(childPosition).getDptCateId());
                }

            });
            return secondLevelexplv;


        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }


    }

    class ChildExpandableListAdapter extends BaseExpandableListAdapter {

        //每个适配器对应的数据源
        private DepartmentListDataResponse data;

        public ChildExpandableListAdapter(DepartmentListDataResponse data) {
            this.data = data;
        }

        @Override
        public int getGroupCount() {//为了让每一个二级列表都为单独的ExpandableList，此处返回为1
            return 1;
        }

        @Override
        public int getChildrenCount(int groupPosition) {//返回三级列表的个数
            return datasThree.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return datasTwo.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return datasThree.get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            ViewHolder mHolder = null;
            if (convertView == null) {
                mHolder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.new_select_department_list_item_two_level, null);
                mHolder.name = (TextView) convertView.findViewById(R.id.department_name);
                mHolder.all_count = (TextView) convertView.findViewById(R.id.all_count);
                mHolder.remain_count = (TextView) convertView.findViewById(R.id.remain_count);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }

            //显示二级列表的数据
            mHolder.name.setText(data.getDptName());
            mHolder.remain_count.setText(data.getRpRemainNum());
            mHolder.all_count.setText(data.getRpNum());
            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ViewHolderChild mHolderChild = null;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.new_select_department_list_item_three_level, null);
                mHolderChild = new ViewHolderChild();
                mHolderChild.name = (TextView) convertView.findViewById(R.id.department_name);
                mHolderChild.all_count = (TextView) convertView.findViewById(R.id.all_count);
                mHolderChild.remain_count = (TextView) convertView.findViewById(R.id.remain_count);
                convertView.setTag(mHolderChild);
            } else {
                mHolderChild = (ViewHolderChild) convertView.getTag();
            }

            //显示三级列表对应的数据
            mHolderChild.name.setText(datasThree.get(childPosition).getDptName());
            mHolderChild.remain_count.setText(datasThree.get(childPosition).getRpRemainNum());
            mHolderChild.all_count.setText(datasThree.get(childPosition).getRpNum());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//点击条目跳转页面
                    startActivity(new Intent(mContext, SelectDoctorsListActivity.class)
                            .putExtra("departmentId", datasThree.get(childPosition).getDtpId())
                            .putExtra("hospitalId",hospitalListResponse.getId()));
                }
            });
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        class ViewHolder {
            private TextView name;
            private TextView all_count;
            private TextView remain_count;
        }

        class ViewHolderChild {
            private TextView name;
            private TextView all_count;
            private TextView remain_count;
        }
    }

    /**
     * 自定义的ExpandableListView，为了让ExpandableListView嵌套时正常显示
     */
    public class ChildeExpListview extends ExpandableListView {

        public ChildeExpListview(Context context) {
            super(context);
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(getWindowWidth(), MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(5000, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_activity_select_department_hospital_relativeLayout:
                startActivity(new Intent(mContext, HospitalHomeActivity.class)
                        .putExtra("hospital", hospitalListResponse));
                break;
            case R.id.new_activity_appointment_main_location:
                break;
            case R.id.new_activity_appointment_main_select:
                break;
        }
    }

    @Override
    public void onNext(IModel t, int requestType) {
        dismissProgressDialog();
        Response response = (Response) t;
        if (response.isFlag()) {
            if (requestType == 111) {//一级科室数据
                datasOne = (ArrayList<DepartmentListDataResponse>) response.getData();
                if (datasOne == null) {
                    datasOne = new ArrayList<>();
                }
                groupAdapter.notifyDataSetChanged();
            } else if (requestType == 222) {//二级科室数据
                datasTwo = (ArrayList<DepartmentListDataResponse>) response.getData();
                if (datasTwo == null) {
                    datasTwo = new ArrayList<>();
                }
                groupAdapter.notifyDataSetChanged();
            } else if (requestType == 333) {//三级科室数据
                datasThree = (ArrayList<DepartmentListDataResponse>) response.getData();
                if (datasThree == null) {
                    datasThree = new ArrayList<>();
                }
                adatpers.get(index_twoLevel).notifyDataSetChanged();
            }
        } else {
            ToastUtils.shortToast(mContext, response.getDetailMsg());
        }
    }

    @Override
    public void onStart(int requestType) {

    }

    @Override
    public void onCompleted(int requestType) {

    }

    @Override
    public void onError(int requestType) {
        dismissProgressDialog();
    }

}
