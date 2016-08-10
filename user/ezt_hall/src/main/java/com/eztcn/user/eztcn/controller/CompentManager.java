/**
 * 
 */
package com.eztcn.user.eztcn.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.bean.compent.Compent;
import com.eztcn.user.eztcn.bean.compent.GridCompent;
import com.eztcn.user.eztcn.bean.compent.IntentParams;
import com.eztcn.user.eztcn.bean.compent.ItemCompent;
import com.eztcn.user.eztcn.bean.compent.ScrollCompent;

/**
 * @author Liu Gang
 * 
 *         2015年11月19日 上午10:50:50 拆除后台的json组装首页界面
 * 
 */
public class CompentManager {
	/**
	 * 组件类型 0:scrollview 1:gridView;
	 * 
	 */
	private final int TYPE_SCROLL = 0, TYPE_GRID = 1;

	public List<Compent> parseCompent(String jsonStr) {
		List<Compent> compentList = new ArrayList<Compent>();
		try {
			JSONObject object = new JSONObject(jsonStr);
			JSONArray rootArray = object.getJSONArray("compentParents");
			Compent compent = null;
			for (int i = 0; i < rootArray.length(); i++) {

				JSONObject compentParentObj = (JSONObject) rootArray.get(i);
				int compentType = compentParentObj.getInt("compentType");
				// 根据类型创建组件
				switch (compentType) {
				case TYPE_SCROLL: {
					compent = new ScrollCompent();
				}
					break;
				case TYPE_GRID: {
					compent = new GridCompent();
					if (compentParentObj.has("row")) {
						((GridCompent) compent).setRow(compentParentObj
								.getInt("row"));
					} else {
						// 默认gridView设置三行
						((GridCompent) compent).setRow(3);
					}
				}
					break;
				}
				JSONArray childArray = compentParentObj
						.getJSONArray("compentChildren");
				for (int j = 0; j < childArray.length(); j++) {
					String imgUrl = "";
					String name1Str = "", name2Str = "";
					String jumpLink = "";// Class.forName
					JSONObject childObj = childArray.getJSONObject(j);
					if (childObj.has("jumpLink")) {
						jumpLink = childObj.getString("jumpLink");
					}
					if (childObj.has("imgUrl")) {
						imgUrl = childObj.getString("imgUrl");
					}
					if (childObj.has("name1")) {
						name1Str = childObj.getString("name1");
					}
					if (childObj.has("name2")) {
						name2Str = childObj.getString("name2");
					}
					List<IntentParams> intentParamList = new ArrayList<IntentParams>();
					if (childObj.has("compentParams")) {
						JSONArray paramArray = childObj
								.getJSONArray("compentParams");
						for (int k = 0; k < paramArray.length(); k++) {
							JSONObject paramObj = paramArray.getJSONObject(k);
							String keyStr = paramObj.getString("key");
							String valueStr = paramObj.getString("value");
							IntentParams param = new IntentParams(keyStr,
									valueStr);
							intentParamList.add(param);
						}
					}
					ItemCompent itemCompent = new ItemCompent(BaseApplication
							.getInstance().getApplicationContext(), imgUrl,
							name1Str, name2Str, jumpLink, intentParamList);

					compent.addChild(itemCompent);
				}
				compentList.add(compent);
			}
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return compentList;
	}
}
