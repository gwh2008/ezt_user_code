package cn.column.app.common.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TitleEntity implements Comparable<TitleEntity> {

	private String titleid;
	private String titleName;
	private String titleType;
	private Integer tilteIndex;
	private String tilteBgColor;

	public ArrayList<TitleEntity> getListEntity(String json) throws Exception {
		ArrayList<TitleEntity> list = new ArrayList<TitleEntity>();
		JSONObject obj = new JSONObject(json);
		JSONArray array1 = obj.getJSONArray("data");
		for (int i = 0; i < array1.length(); i++) {
			JSONObject obj1 = (JSONObject) array1.get(i);
			TitleEntity entity = new TitleEntity();
			entity.setTitleName(obj1.optString("columnname"));
			entity.setTitleid(obj1.optString("id"));
			entity.setTilteIndex(obj1.optInt("index"));
			list.add(entity);
		}
		return list;

	}

	public String getTitleid() {
		return titleid;
	}

	public void setTitleid(String titleid) {
		this.titleid = titleid;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public String getTitleType() {
		return titleType;
	}

	public void setTitleType(String titleType) {
		this.titleType = titleType;
	}

	public Integer getTilteIndex() {
		return tilteIndex;
	}

	public void setTilteIndex(Integer tilteIndex) {
		this.tilteIndex = tilteIndex;
	}

	public String getTilteBgColor() {
		return tilteBgColor;
	}

	public void setTilteBgColor(String tilteBgColor) {
		this.tilteBgColor = tilteBgColor;
	}

	@Override
	public int compareTo(TitleEntity another) {
		// TODO Auto-generated method stub
		return this.getTilteIndex().compareTo(another.getTilteIndex());
	}

}
