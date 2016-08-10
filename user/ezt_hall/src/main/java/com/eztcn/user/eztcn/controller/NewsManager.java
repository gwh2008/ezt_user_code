package com.eztcn.user.eztcn.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eztcn.user.eztcn.bean.Doctor;
import com.eztcn.user.eztcn.bean.InformateComment;
import com.eztcn.user.eztcn.bean.Information;
import com.eztcn.user.eztcn.bean.InformationColumn;
import com.eztcn.user.eztcn.utils.Logger;

/**
 * @title 资讯管理
 * @describe
 * @author ezt
 * @created 2015年1月7日
 */
public class NewsManager {

	/**
	 * 解析资讯详情
	 */
	public static Map<String, Object> getNewsDetail(String t) {

		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			boolean flag = false;
			if (!json.isNull("flag")) {
				flag = json.getBoolean("flag");
				map.put("flag", flag);
			}
			if (!flag) {
				map.put("msg", json.getString("msg"));
			} else {
				if (!json.isNull("data")) {
					JSONObject json1 = json.getJSONObject("data");
					Information info = new Information();
					if (!json1.isNull("id")) {
						info.setId(json1.getString("id"));
					}

					if (!json1.isNull("wzzurl")) {
						info.setUrl(json1.getString("wzzurl"));
					}

					if (!json1.isNull("body")) {
						info.setBody(json1.getString("body"));
					}

					if (!json1.isNull("comment")) {
						info.setEvaluateNum(json1.getString("comment"));
					}

					if (!json1.isNull("senddate")) {
						info.setCreateTime(json1.getString("senddate"));
					}

					if (!json1.isNull("title")) {
						info.setInfoTitle(json1.getString("title"));
					}

					if (!json1.isNull("typename")) {
						info.setType(json1.getString("typename"));
					}
					if (!json1.isNull("source")) {
						info.setSource(json1.getString("source"));
					}

					ArrayList<Doctor> docList = new ArrayList<Doctor>();
					if (!json1.isNull("doclist")) {
						JSONArray jArray = json1.getJSONArray("doclist");
						for (int i = 0; i < jArray.length(); i++) {
							JSONObject j = jArray.getJSONObject(i);
							Doctor doc = new Doctor();
							if (!j.isNull("id")) {
								doc.setId(j.getString("id"));
							}

							if (!j.isNull("edName")) {
								doc.setDocName(j.getString("edName"));
							}

							if (!j.isNull("ehName")) {
								doc.setDocHos(j.getString("ehName"));
							}
							if (!j.isNull("dptName")) {
								doc.setDocDept(j.getString("dptName"));
							}
							if (!j.isNull("edPic")) {
								doc.setDocHeadUrl(j.getString("edPic"));

							}
							if (!j.isNull("deptdocid")) {
								doc.setDeptDocId(j.getString("deptdocid"));
							}
							
							if (!j.isNull("branch")) {
								doc.setDocDeptId(j.getString("branch"));
							}
							
							docList.add(doc);

						}
						info.setSource(json1.getString("source"));
					}
					info.setDoclist(docList);
					map.put("info", info);
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 解析资讯栏目
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> getNewsColumn(String t) {

		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			boolean flag = false;
			if (!json.isNull("flag")) {
				flag = json.getBoolean("flag");
				map.put("flag", flag);
			}
			if (!flag) {
				map.put("msg", json.getString("msg"));
			} else {
				if (!json.isNull("data")) {
					JSONArray jarray = json.getJSONArray("data");
					ArrayList<InformationColumn> columns = new ArrayList<InformationColumn>();
					for (int i = 0; i < jarray.length(); i++) {
						InformationColumn column = new InformationColumn();
						JSONObject json1 = jarray.getJSONObject(i);
						if (!json1.isNull("id")) {
							column.setId(json1.getString("id"));
						}

						if (!json1.isNull("typename")) {
							column.setInfoName(json1.getString("typename"));
						}

						columns.add(column);
					}

					map.put("columns", columns);
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 获取资讯列表
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> getNewsList(String t) {

		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			boolean flag = false;
			if (!json.isNull("flag")) {
				flag = json.getBoolean("flag");
				map.put("flag", flag);
			}
			if(!json.isNull("message")){
				map.put("message", json.getString("message"));
			}
			if (!flag) {
				map.put("msg", json.getString("msg"));
			} else {
				if (!json.isNull("data")) {
					JSONArray jarray = json.getJSONArray("data");
					ArrayList<Information> infoList = new ArrayList<Information>();

					for (int i = 0; i < jarray.length(); i++) {
						Information info = new Information();
						JSONObject json1 = jarray.getJSONObject(i);
						if (!json1.isNull("id")) {
							info.setId(json1.getString("id"));
						}

						if (!json1.isNull("comment")) {
							info.setEvaluateNum(json1.getString("comment"));// 评价数
						}

						if (!json1.isNull("subTitle")) {
							info.setInfoDescription(json1
									.getString("subTitle"));// 描述
						}

						if (!json1.isNull("title")) {
							info.setInfoTitle(json1.getString("title"));
						}

						if (!json1.isNull("imageUrl")) {
							info.setImgUrl(json1.getString("imageUrl"));
						}
                        if (!json1.isNull("articleUrl")) {
                            info.setArticleUrl(json1.getString("articleUrl"));
                        }

						infoList.add(info);
					}

					map.put("infoList", infoList);
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 添加资讯评价
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> addNewsComment(String t) {
		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			if (!json.isNull("flag")) {
				map.put("flag", json.getBoolean("flag"));
			}

			if (!json.isNull("msg")) {
				map.put("msg", json.getString("msg"));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 获取资讯评价列表
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> getNewsCommentList(String t) {
		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			boolean flag = false;
			if (!json.isNull("flag")) {
				flag = json.getBoolean("flag");
				map.put("flag", flag);
			}
			if (flag) {
				ArrayList<InformateComment> list = new ArrayList<InformateComment>();
				if (!json.isNull("data")) {
					JSONArray jarray = json.getJSONArray("data");
					for (int i = 0; i < jarray.length(); i++) {
						InformateComment coment = new InformateComment();
						JSONObject json1 = jarray.getJSONObject(i);
						if (!json1.isNull("dtime")) {
							coment.setCreateTime(json1.getString("dtime"));
						}
						if (!json1.isNull("msg")) {
							coment.setContent(json1.getString("msg"));
						}
						if (!json1.isNull("username")) {
							coment.setUserName(json1.getString("username"));
						}

						list.add(coment);
					}

				}

				map.put("comments", list);

			} else {// 失败
				if (!json.isNull("msg"))
					map.put("msg", json.getString("msg"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}
}
