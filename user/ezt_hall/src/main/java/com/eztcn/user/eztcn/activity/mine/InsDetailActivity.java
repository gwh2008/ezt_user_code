/**
 * 
 */
package com.eztcn.user.eztcn.activity.mine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.InsDetailAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.InsItemDetial;
import com.eztcn.user.eztcn.impl.InsReportImpl;

/**
 * @author Liu Gang
 * 
 *         2015年11月25日 下午3:35:23 小检验项即大检验项详情
 */
public class InsDetailActivity extends FinalActivity implements
		 IHttpResult {
	@ViewInject(R.id.lvInsDetail)
	private ListView lvInsDetial;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insdetail);
		ViewUtils.inject(InsDetailActivity.this);
		// 大检验项id
		String bItemId = getIntent().getStringExtra("bItemId");
		String bItemName= getIntent().getStringExtra("bItemName");
		loadTitleBar(true, bItemName, null);
		InsReportImpl insReportImpl = new InsReportImpl();
		HashMap<String, Object> params =new HashMap<String, Object>();
		params.put("samp_id", bItemId);
		insReportImpl.getSampleAndSampleDetailMessage(params, this);
		showProgressToast();
	}
	/**
	 * @param
	 * insItemDetials 检验项目详情数据适配
	 */
	private void setResultdata(List<InsItemDetial> insItemDetials){
		InsDetailAdapter adapter=new InsDetailAdapter(mContext);
		lvInsDetial.setAdapter(adapter);
		adapter.setList(insItemDetials);
	}

	@Override
	public void result(Object... object) {
		
		hideProgressToast();
		if (object == null) {
			return;
		}
		Object[] obj = object;
		Integer taskID = (Integer) obj[0];
		if (taskID == null) {
			return;
		}
		boolean status = (Boolean) obj[1];
		if (!status) {
			Toast.makeText(mContext, obj[3] + "", Toast.LENGTH_SHORT).show();
			return;
		}
		Map<String, Object> map = (Map<String, Object>) obj[2];
		if (map == null || map.size() == 0) {
			return;
		}
		boolean flag = (Boolean) map.get("flag");
		if (!flag) {//返回错误信息
			Toast.makeText(mContext, map.get("msg") + "".toString(),
					Toast.LENGTH_SHORT).show();
		}else{
			if(!map.containsKey("data")){
				Toast.makeText(mContext,"服务器繁忙，请重试！",
						Toast.LENGTH_SHORT).show();
			}else{
				List<InsItemDetial> insItemDetials = (List<InsItemDetial>) map.get("data");
				setResultdata(insItemDetials);
			}
		}
	}
}
