package com.eztcn.user.eztcn.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eztcn.user.eztcn.bean.DragonCard;
import com.eztcn.user.eztcn.bean.EztUser;
import com.eztcn.user.eztcn.bean.HealthCard;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.bean.LightAccompanying;
import com.eztcn.user.eztcn.utils.ResolveResponse;
import com.eztcn.user.eztcn.utils.StringUtil;

/**
 * @title 轻陪诊解析
 * @describe
 * @author ezt
 * @created 2014年12月23日
 */
public class LightAccompanyingManager {

	/**
	 * 解析服务器套餐详情
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> getPackageDetail(String t) {
		Map<String, Object> map = null;
		JSONObject dataJson;
		try {
			map = new HashMap<String, Object>();
			JSONObject ject = new JSONObject(t);
			if (!ject.isNull("data")) {
				dataJson = ject.getJSONObject("data");
			} else {
				return null;
			}
			if (!dataJson.isNull("hosBeans")) {
				JSONArray hosArray = dataJson.getJSONArray("hosBeans");
				ArrayList<Hospital> list = new ArrayList<Hospital>();
				int CountyId = 0;
				for (int i = 0; i < hosArray.length(); i++) {
					Hospital hos = new Hospital();
					JSONObject hosJect = hosArray.getJSONObject(i);

					if (!hosJect.isNull("eztHospitalehCounty")) {
						Hospital h = new Hospital();
						int id = hosJect.getInt("eztHospitalehCounty");
						if (id != CountyId) {
							if (!hosJect.isNull("eztHospitalehCountyName"))
								h.sethAddress(hosJect
										.getString("eztHospitalehCountyName"));

							CountyId = id;
							list.add(h);
						}

					}

					if (!hosJect.isNull("eztHospitalehName")) {
						hos.sethName(hosJect.getString("eztHospitalehName"));
					}
					if (!hosJect.isNull("eztHospitalehLevelName")) {
						hos.setHosLevel(hosJect
								.getString("eztHospitalehLevelName"));
					}

					list.add(hos);
				}
				map.put("hosList", list);
			}
			if (!dataJson.isNull("itemBeans")) {
				JSONArray itemArray = dataJson.getJSONArray("itemBeans");
				ArrayList<LightAccompanying> list = new ArrayList<LightAccompanying>();
				for (int i = 0; i < itemArray.length(); i++) {
					LightAccompanying l = new LightAccompanying();
					JSONObject itemJect = itemArray.getJSONObject(i);
					if (!itemJect.isNull("traItemtiName")) {// 服务标题
						l.setItemName(itemJect.getString("traItemtiName"));
					}

					if (!itemJect.isNull("traItemremark")) {// 服务描述
						l.setRemark(itemJect.getString("traItemremark"));
					}

					if (!itemJect.isNull("id")) {
						l.setId(itemJect.getString("id"));
					}

					if (!itemJect.isNull("traPackageItemuseNums")) {
						l.setRemainNums(itemJect
								.getInt("traPackageItemuseNums"));
					}
					list.add(l);
				}
				map.put("itemList", list);
			}
			if (!dataJson.isNull("packageBean")) {
				JSONObject packageJect = dataJson.getJSONObject("packageBean");

				if (!packageJect.isNull("epName"))
					map.put("cardName", packageJect.getString("epName"));

				if (!packageJect.isNull("id"))
					map.put("cardId", packageJect.getString("id"));

				if (!packageJect.isNull("epAmount"))
					map.put("carAmount", packageJect.getString("epAmount"));

				if (!packageJect.isNull("remark"))
					map.put("remark", packageJect.getString("remark"));

			}

			return map;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析激活卡
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> getActivation(String t) {
		Map<String, Object> map = null;
		JSONObject dataJson;
		try {
			map = new HashMap<String, Object>();
			dataJson = new JSONObject(t);
			if (!dataJson.isNull("flag"))
				map.put("flag", dataJson.getBoolean("flag"));

			if (!dataJson.isNull("detailMsg"))
				map.put("msg", dataJson.getString("detailMsg"));
			return map;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 创建健康卡订单
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> createTraOrderpayPackage(String t) {
		Map<String, Object> map = null;
		JSONObject dataJson;
		try {
			map = new HashMap<String, Object>();
			dataJson = new JSONObject(t);
			boolean isFlag = false;
			if (!dataJson.isNull("flag")) {
				isFlag = dataJson.getBoolean("flag");
				map.put("flag", isFlag);
			}
			if (isFlag) {// 成功
				if (!dataJson.isNull("data")) {
					map.put("data", dataJson.getString("data"));
				}

			} else {
				if (!dataJson.isNull("detailMsg"))
					map.put("msg", dataJson.getString("detailMsg"));
			}

			return map;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取健康卡列表
	 * 
	 * @param t
	 * @return
	 */
	public static ArrayList<HealthCard> getHealthcardList(String t) {
		ArrayList<HealthCard> list = null;
		try {
			Object object = ResolveResponse.resolveData(t);// 解析返回的数据
			if (object instanceof JSONObject) {// 成功
				JSONObject j = (JSONObject) object;
				JSONArray jArray = null;
				if (!j.isNull("rows")) {
					jArray = j.getJSONArray("rows");
				} else {
					return list;
				}
				list = new ArrayList<HealthCard>();
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject ject = jArray.getJSONObject(i);
					HealthCard h = new HealthCard();

					if (!ject.isNull("hcCardNo"))
						h.setCardNum(ject.getString("hcCardNo"));

					if (!ject.isNull("id"))
						h.setId(ject.getString("id"));

					if (!ject.isNull("hcStatus"))// 使用状态0未使用，1已使用
						h.setState(ject.getInt("hcStatus"));

					if (!ject.isNull("hcActivateDt"))// 激活时间
						h.setActiveTime(ject.getString("hcActivateDt"));

					if (!ject.isNull("traPackageepName"))// 卡名称
						h.setCardName(ject.getString("traPackageepName"));

					if (!ject.isNull("hcEndValidity"))// 截止有效期
						h.setHcEndValidity(ject.getString("hcEndValidity"));

					if (!ject.isNull("hcBeginServiceValidity"))// 有效期开始
						h.setHcBeginServiceValidity(ject
								.getString("hcBeginServiceValidity"));
					if (!ject.isNull("traPackageepPic")) {// 图片
						h.setCardCover(ject.getString("traPackageepPic"));
					}

					list.add(h);
				}
			} else if (object instanceof Boolean) {
				boolean isSuc = (Boolean) object;
				if (!isSuc) {// 访问失败
					list = null;
				} else {// 数据为空

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			list = null;
		}
		return list;
	}

	/**
	 * 健康卡详情
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> getHealthcardDetail(String t) {
		Map<String, Object> map = null;
		JSONObject dataJson;
		try {
			map = new HashMap<String, Object>();
			JSONObject ject = new JSONObject(t);
			if (!ject.isNull("data")) {
				dataJson = ject.getJSONObject("data");
			} else {
				return null;
			}

			if (!dataJson.isNull("healthCarUseBeans")) {
				JSONArray itemArray = dataJson
						.getJSONArray("healthCarUseBeans");
				ArrayList<LightAccompanying> list = new ArrayList<LightAccompanying>();
				for (int i = 0; i < itemArray.length(); i++) {
					LightAccompanying l = new LightAccompanying();
					JSONObject itemJect = itemArray.getJSONObject(i);

					if (!itemJect.isNull("itemId")) {
						l.setItemId(itemJect.getString("itemId"));
					}

					if (!itemJect.isNull("id")) {
						l.setId(itemJect.getString("id"));
					}

					if (!itemJect.isNull("itemtiName")) {// 服务标题
						l.setItemName(itemJect.getString("itemtiName"));
					}

					if (!itemJect.isNull("itemremark")) {// 服务描述
						l.setRemark(itemJect.getString("itemremark"));
					}

					if (!itemJect.isNull("userRemainNums")) {// 剩余次数
						l.setRemainNums(itemJect.getInt("userRemainNums"));
					}

					if (!itemJect.isNull("itemStatus")) {
						l.setItemStatus(itemJect.getInt("itemStatus"));// 使用状态
					}

					if (!itemJect.isNull("packageremark")) {
						map.put("remark", itemJect.getString("packageremark"));
					}

					list.add(l);
				}
				map.put("itemList", list);
			}

			if (!dataJson.isNull("hosBeans")) {
				JSONArray hosArray = dataJson.getJSONArray("hosBeans");
				ArrayList<Hospital> list = new ArrayList<Hospital>();
				int CountyId = 0;
				for (int i = 0; i < hosArray.length(); i++) {
					Hospital hos = new Hospital();
					JSONObject hosJect = hosArray.getJSONObject(i);

					if (!hosJect.isNull("eztHospitalehCounty")) {
						Hospital h = new Hospital();
						int id = hosJect.getInt("eztHospitalehCounty");
						if (id != CountyId) {
							if (!hosJect.isNull("eztHospitalehCountyName"))
								h.sethAddress(hosJect
										.getString("eztHospitalehCountyName"));

							CountyId = id;
							list.add(h);
						}

					}

					if (!hosJect.isNull("eztHospitalehName")) {
						hos.sethName(hosJect.getString("eztHospitalehName"));
					}
					if (!hosJect.isNull("eztHospitalehLevelName")) {
						hos.setHosLevel(hosJect
								.getString("eztHospitalehLevelName"));
					}

					list.add(hos);
				}
				map.put("hosList", list);
			}

			if (!dataJson.isNull("healthCarBean")) {
				JSONObject packageJect = dataJson
						.getJSONObject("healthCarBean");

				if (!packageJect.isNull("hcBeginServiceValidity"))// 有效期开始
				{
					String str = StringUtil.dealWithDate(packageJect
							.getString("hcBeginServiceValidity"));
					map.put("startTime", str);
				} else {
					map.put("startTime", "");
				}

				if (!packageJect.isNull("hcEndValidity"))// 有效期结束
				{
					String str = StringUtil.dealWithDate(packageJect
							.getString("hcEndValidity"));
					map.put("endTime", str);

				} else {
					map.put("endTime", "");
				}

				if (!packageJect.isNull("hcCardNo"))// 卡号
					map.put("cardNo", packageJect.getString("hcCardNo"));

				if (!packageJect.isNull("hcStatus"))// 状态
					map.put("state", packageJect.getInt("hcStatus"));

				if (!dataJson.isNull("packageBeans")) {
					JSONObject pkgJson = dataJson.getJSONObject("packageBeans");
					if (!pkgJson.isNull("remark")) {
						map.put("remark", pkgJson.getString("remark"));
					} else {
						map.put("remark", "");
					}
					if (!pkgJson.isNull("process")) {
						map.put("process", pkgJson.getString("process"));
					} else {
						map.put("process", "");
					}
					if (!pkgJson.isNull("notice")) {
						map.put("notice", pkgJson.getString("notice"));
					} else {
						map.put("notice", "");
					}
					if (!pkgJson.isNull("epPic")) {
						map.put("epPic", pkgJson.getString("epPic"));
					} else {
						map.put("epPic", "");
					}
				}
			}

			return map;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取卡服务项详情
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> getItemDetailNew(String t) {
		Map<String, Object> map = null;
		JSONObject dataJson;
		map = new HashMap<String, Object>();
		try {
			JSONObject ject = new JSONObject(t);
			if (!ject.isNull("flag")) {
				boolean isSucc = ject.getBoolean("flag");
				map.put("flag", isSucc);
				if (isSucc) {
					if (!ject.isNull("data")) {
						JSONObject jsonData = ject.getJSONObject("data");
						if (!jsonData.isNull("ItemList")) {
							JSONArray jsonItemArray = jsonData
									.getJSONArray("ItemList");
							List<LightAccompanying> serItems = new ArrayList<LightAccompanying>();
							for (int i = 0; i < jsonItemArray.length(); i++) {
								JSONObject jsonItem = jsonItemArray
										.getJSONObject(i);
								LightAccompanying serItem = new LightAccompanying();
								if (!jsonItem.isNull("nums")) {
									int num = jsonItem.getInt("nums");
									serItem.setRemainNums(num);
									if (!jsonItem.isNull("traItem")) {
										JSONObject jsonItem_ = jsonItem
												.getJSONObject("traItem");
										if (!jsonItem_.isNull("aPPUsage")) {
											serItem.setAppUsage(jsonItem_
													.getString("aPPUsage"));
										}
										if (!jsonItem_.isNull("createtime")) {
										}
										if (!jsonItem_.isNull("epAmount")) {
										}
										if (!jsonItem_.isNull("epStatus")) {
											serItem.setItemStatus(jsonItem_
													.getInt("epStatus"));
										}
										if (!jsonItem_.isNull("id")) {
											serItem.setId(jsonItem_
													.getString("id"));
										}
										if (!jsonItem_.isNull("notice")) {
											serItem.setNotice(jsonItem_
													.getString("notice"));
										}
										if (!jsonItem_.isNull("pCUsage")) {
										}
										if (!jsonItem_.isNull("process")) {
											serItem.setProcess(jsonItem_
													.getString("process"));
										}
										if (!jsonItem_.isNull("remark")) {
											serItem.setRemark(jsonItem_
													.getString("remark"));
										}
										if (!jsonItem_.isNull("status")) {
										}
										if (!jsonItem_.isNull("tiName")) {
											serItem.setItemName(jsonItem_
													.getString("tiName"));
										}
										if (!jsonItem_.isNull("tiType")) {
										}
										if (!jsonItem_.isNull("times")) {
										}

									}
								}
								serItems.add(serItem);
							}
							map.put("data", serItems);
						}
					}
				} else {
					// 请求到错误信息时候塞入错误数据
					if (!ject.isNull("detailMsg")) {
						map.put("msg", ject.getString("detailMsg"));
					}
				}
			}
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 获取卡服务项详情
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> getItemDetail(String t) {
		Map<String, Object> map = null;
		JSONObject dataJson;
		try {
			map = new HashMap<String, Object>();
			JSONObject ject = new JSONObject(t);
			if (!ject.isNull("data")) {
				dataJson = ject.getJSONObject("data");
			} else {
				return null;
			}

			LightAccompanying l = new LightAccompanying();

			if (!dataJson.isNull("itemBeans")) {
				JSONObject itemObject = dataJson.getJSONObject("itemBeans");

				if (!itemObject.isNull("id")) {
					l.setId(itemObject.getString("id"));
				}
				if (!itemObject.isNull("userRemainNums")) {
					l.setRemainNums(itemObject.getInt("userRemainNums"));// 剩余数量
				}
				if (!itemObject.isNull("itemstatus")) {
					l.setItemStatus(itemObject.getInt("itemstatus"));// 使用状态
				}

			}
			if (!dataJson.isNull("traItem")) {
				JSONObject traItemObject = dataJson.getJSONObject("traItem");

				if (!traItemObject.isNull("notice")) {
					l.setNotice(traItemObject.getString("notice"));// 须知
				}
				if (!traItemObject.isNull("process")) {
					l.setProcess(traItemObject.getString("process"));// 流程
				}
				if (!traItemObject.isNull("remark")) {
					l.setRemark(traItemObject.getString("remark"));// 描述
				}
			}

			map.put("item", l);
			return map;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取龙卡详情
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> getHealthDragonInfo(String t) {
		Map<String, Object> map = null;
		JSONObject dataJson;
		try {
			map = new HashMap<String, Object>();
			JSONObject ject = new JSONObject(t);
			if (!ject.isNull("data")) {
				dataJson = ject.getJSONObject("data");
			} else {
				return map;
			}

			if (!dataJson.isNull("traHealthCarUseList")) {
				JSONArray itemArray = dataJson
						.getJSONArray("traHealthCarUseList");
				ArrayList<LightAccompanying> list = new ArrayList<LightAccompanying>();

				for (int i = 0; i < itemArray.length(); i++) {
					LightAccompanying l = new LightAccompanying();
					JSONObject itemJect = itemArray.getJSONObject(i);

					if (!itemJect.isNull("id")) {
						l.setId(itemJect.getString("id"));
					}

					if (!itemJect.isNull("itemId")) {
						l.setItemId(itemJect.getString("itemId"));
					}

					if (!itemJect.isNull("itemtiName")) {// 服务标题
						l.setItemName(itemJect.getString("itemtiName"));
					}

					if (!itemJect.isNull("itemremark")) {// 服务描述
						l.setRemark(itemJect.getString("itemremark"));
					}

					if (!itemJect.isNull("userRemainNums")) {// 剩余次数
						l.setRemainNums(itemJect.getInt("userRemainNums"));
					}

					if (!itemJect.isNull("appUsage")) {
						l.setAppUsage(itemJect.getString("appUsage"));// 服务使用类型
					}

					if (!dataJson.isNull("itemStatus")) {
						l.setItemStatus(dataJson.getInt("itemStatus"));// 使用状态
					}

					if (!itemJect.isNull("packageremark")) {
						map.put("remark", itemJect.getString("packageremark"));
					}

					list.add(l);
				}
				map.put("itemList", list);
			}

			if (!dataJson.isNull("eztUserAffinityCard")) {
				JSONObject packageJect = dataJson
						.getJSONObject("eztUserAffinityCard");

				if (!packageJect.isNull("acActivateDt"))// 有效期开始
				{
					String str = StringUtil.dealWithDate(packageJect
							.getString("acActivateDt"));
					map.put("startTime", str);
				} else {
					map.put("startTime", "");
				}

				if (!packageJect.isNull("acEndValidity"))// 有效期结束
				{
					String str = StringUtil.dealWithDate(packageJect
							.getString("acEndValidity"));
					map.put("endTime", str);

				} else {
					map.put("endTime", "");
				}

				if (!packageJect.isNull("cardNum"))// 卡号
					map.put("cardNo", packageJect.getString("cardNum"));

				if (!packageJect.isNull("acStatus"))// 状态
					map.put("state", packageJect.getInt("acStatus"));

			}

			return map;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 验证用户 卡信息
	 * {"data":null,"detailMsg":"数据验证成功","flag":true,"msg":"成功","number":"2000"}
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> cardAuth(String t) {
		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject ject = new JSONObject(t);
			boolean flag = false;
			if (!ject.isNull("flag")) {
				flag = ject.getBoolean("flag");
			}
			if (!flag) {
				if (!ject.isNull("detailMsg"))
					map.put("msg", ject.get("detailMsg"));
			} else {
				if (!ject.isNull("data")) {
					map.put("data", ject.get("data"));
				}
				if (!ject.isNull("detailMsg"))
					map.put("msg", ject.get("detailMsg"));
			}
			map.put("flag", flag);
			return map;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 龙卡激活绑定
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> Cardbinding(String t) {
		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject ject = new JSONObject(t);
			boolean flag = false;
			if (!ject.isNull("flag")) {
				flag = ject.getBoolean("flag");
			}
			if (!flag) {
				if (!ject.isNull("detailMsg")) {
					map.put("msg", ject.getBoolean("detailMsg"));
				}

			}
			map.put("flag", flag);
			return map;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * {"data":{"activeDate":"2016-04-04 00:00:00"
	 * ,"bankCardId":"4555037654321567890" ,"custId":"142401197406162441",
	 * "custName":"韩扬" ,"endDate":"2017-04-04 00:00:00", "guideNum":0,"id":12,
	 * "leadNum":0, "opencard":"2016-04-04 16:52:42", "payFlag":1, "pfId":356,
	 * "phone":"13821430717", "remindNum":3, "uid":2392895,
	 * "uname":"13821430717"
	 * },"detailMsg":"获取数据成功","flag":true,"msg":"成功","number":"2000"}
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> gainCcbInfobyUid(String t) {
		Map<String, Object> map = null;
		JSONObject ject;
		boolean flag = false;
		try {
			map = new HashMap<String, Object>();
			ject = new JSONObject(t);
			if (!ject.isNull("flag")) {
				flag = ject.getBoolean("flag");
			}
			if (!flag) {
				if (!ject.isNull("detailMsg")) {
					map.put("msg", ject.get("detailMsg"));
				}
			} else {
				if (!ject.isNull("data")) {
					DragonCard dragonCard = new DragonCard();
					JSONObject jsonChild = ject.getJSONObject("data");

					EztUser user = new EztUser();

					if (!jsonChild.isNull("opencard")) {
						dragonCard.setOpenCard(jsonChild.getString("opencard"));
					}

					if (!jsonChild.isNull("activeDate")) {
						dragonCard.setActiveDate(jsonChild
								.getString("activeDate"));
					}
					if (!jsonChild.isNull("bankCardId")) {
						dragonCard.setBankCardId(jsonChild
								.getString("bankCardId"));
					}

					if (!jsonChild.isNull("custId")) {
						user.setIdCard(jsonChild.getString("custId"));
					}
					if (!jsonChild.isNull("custName")) {
						user.setUserName(jsonChild.getString("custName"));
					}

					if (!jsonChild.isNull("endDate")) {
						dragonCard.setEndDate(jsonChild.getString("endDate"));
					}
					if (!jsonChild.isNull("guideNum")) {
						dragonCard.setGuideNum(jsonChild.getString("guideNum"));
					}
					if (!jsonChild.isNull("id")) {
						dragonCard.setId(jsonChild.getString("id"));
					}
					if (!jsonChild.isNull("leadNum")) {
						dragonCard.setLevelNum(jsonChild.getString("leadNum"));
					}

					if (!jsonChild.isNull("payFlag")) {
						dragonCard.setPayFlag(jsonChild.getInt("payFlag"));
					}

					if (!jsonChild.isNull("phone")) {
						user.setMobile(jsonChild.getString("phone"));
					}

					if (!jsonChild.isNull("remindNum")) {
						dragonCard.setRemindNum(jsonChild
								.getString("remindNum"));
					}
					if (!jsonChild.isNull("uid")) {
						user.setUserId(jsonChild.getInt("uid"));
					}
					if (!jsonChild.isNull("uname")) {
						user.setUserName(jsonChild.getString("uname"));
					}
					dragonCard.setUser(user);
					map.put("data", dragonCard);
				}
			}
			map.put("flag", flag);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return map;
	}

	/**
	 * { "data":{ "ItemList":[
	 * 
	 * ], "traCCBAthentication":{ "activeDate":"2015-12-14 00:00:00",
	 * "bankCardId":"6217000060021548134", "custId":"130520197503292858",
	 * "custName":"侯五六", "endDate":"2016-03-31 17:47:06", "guideNum":8, "id":6,
	 * "leadNum":9, "payFlag":1, "pfId":356, "phone":"15522662132",
	 * "remindNum":10, "uid":2, "uname":null } }, "detailMsg":"获取数据成功",
	 * "flag":true, "msg":"成功", "number":"2000" }
	 * 
	 * 
	 * {"data":{"activeDate":"2016-04-04 00:00:00"
	 * ,"bankCardId":"4555037654321567890" ,"custId":"142401197406162441",
	 * "custName":"韩扬" ,"endDate":"2017-04-04 00:00:00", "guideNum":0,"id":12,
	 * "leadNum":0,
	 * 
	 * 
	 * "opencard":"2016-04-04 16:52:42",
	 * 
	 * "payFlag":1, "pfId":356, "phone":"13821430717", "remindNum":3,
	 * "uid":2392895,
	 * "uname":"13821430717"},"detailMsg":"获取数据成功","flag":true,"msg"
	 * :"成功","number":"2000"}
	 * 
	 * ©2014 Json.cn All right reserved. 京ICP备15025187号-1
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> gainCcbInfo30(String t) {
		Map<String, Object> map = null;
		JSONObject ject;
		boolean flag = false;
		try {
			map = new HashMap<String, Object>();
			ject = new JSONObject(t);
			if (!ject.isNull("flag")) {
				flag = ject.getBoolean("flag");
			}
			if (!flag) {
				if (!ject.isNull("detailMsg")) {
					map.put("msg", ject.get("detailMsg"));
				}
			} else {
				if (!ject.isNull("data")) {
					DragonCard dragonCard = new DragonCard();
					ject = ject.getJSONObject("data");
					if (!ject.isNull("traCCBAthentication")) {
						JSONObject jsonChild = ject
								.getJSONObject("traCCBAthentication");
						EztUser user = new EztUser();
						if (!jsonChild.isNull("activeDate")) {
							dragonCard.setActiveDate(jsonChild
									.getString("activeDate"));
						}
						if (!jsonChild.isNull("bankCardId")) {
							dragonCard.setBankCardId(jsonChild
									.getString("bankCardId"));
						}

						if (!jsonChild.isNull("custId")) {
							user.setIdCard(jsonChild.getString("custId"));
						}
						if (!jsonChild.isNull("custName")) {
							user.setUserName(jsonChild.getString("custName"));
						}

						if (!jsonChild.isNull("endDate")) {
							dragonCard.setEndDate(jsonChild
									.getString("endDate"));
						}
						if (!jsonChild.isNull("guideNum")) {
							dragonCard.setGuideNum(jsonChild
									.getString("guideNum"));
						}
						if (!jsonChild.isNull("id")) {
							dragonCard.setId(jsonChild.getString("id"));
						}
						if (!jsonChild.isNull("leadNum")) {
							dragonCard.setLevelNum(jsonChild
									.getString("leadNum"));
						}

						if (!jsonChild.isNull("payFlag")) {
							dragonCard.setPayFlag(jsonChild.getInt("payFlag"));
						}

						if (!jsonChild.isNull("phone")) {
							user.setMobile(jsonChild.getString("phone"));
						}

						if (!jsonChild.isNull("remindNum")) {
							dragonCard.setRemindNum(jsonChild
									.getString("remindNum"));
						}
						if (!jsonChild.isNull("uid")) {
							user.setUserId(jsonChild.getInt("uid"));
						}
						if (!jsonChild.isNull("uname")) {
							user.setUserName(jsonChild.getString("uname"));
						}
						dragonCard.setUser(user);
					}
					map.put("data", dragonCard);
				}
			}
			map.put("flag", flag);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return map;
	}
	/**
	 * 获取建行卡信息
	 * 
	 * @param t
	 * @return
	 */
	// public static Map<String, Object> gainCcbInfo(String t) {
	// Map<String, Object> map = null;
	// JSONObject ject;
	// boolean flag = false;
	// try {
	// map = new HashMap<String, Object>();
	// ject = new JSONObject(t);
	// if (!ject.isNull("flag")) {
	// flag = ject.getBoolean("flag");
	// }
	// if (!flag) {
	// if (!ject.isNull("detailMsg")) {
	// map.put("msg", ject.get("detailMsg"));
	// }
	// } else {
	// if (!ject.isNull("data")) {
	// DragonCard dragonCard = new DragonCard();
	// List<LightAccompanying> serItemList = new ArrayList<LightAccompanying>();
	// ject = ject.getJSONObject("data");
	// if (!ject.isNull("ItemList")) {
	// JSONArray serverItemArray = ject
	// .getJSONArray("ItemList");
	// for (int i = 0; i < serverItemArray.length(); i++) {
	// JSONObject itemJson = serverItemArray
	// .getJSONObject(i);
	// LightAccompanying serverItem = new LightAccompanying();
	// if (!itemJson.isNull("itemId")) {
	// serverItem.setItemId(itemJson
	// .getString("itemId"));
	// }
	// if (!itemJson.isNull("itemName")) {
	// serverItem.setItemName(itemJson
	// .getString("itemName"));
	// }
	// if (!itemJson.isNull("nums")) {
	// serverItem.setRemainNums(itemJson
	// .getInt("nums"));
	// }
	// serItemList.add(serverItem);
	// }
	// dragonCard.setServerList(serItemList);
	// }
	// if (!ject.isNull("traCCBAthentication")) {
	// JSONObject jsonChild = ject
	// .getJSONObject("traCCBAthentication");
	// EztUser user = new EztUser();
	// if (!jsonChild.isNull("activeDate")) {
	// dragonCard.setActiveDate(jsonChild
	// .getString("activeDate"));
	// }
	// if (!jsonChild.isNull("bankCardId")) {
	// dragonCard.setBankCardId(jsonChild
	// .getString("bankCardId"));
	// }
	//
	// if (!jsonChild.isNull("custId")) {
	// user.setIdCard(jsonChild.getString("custId"));
	// }
	// if (jsonChild.isNull("custName")) {
	// user.setUserName(jsonChild.getString("custName"));
	// }
	//
	// if (jsonChild.isNull("hasDate")) {
	// dragonCard.setHasDate(jsonChild
	// .getString("hasDate"));
	// }
	// if (jsonChild.isNull("id")) {
	// dragonCard.setId(jsonChild.getString("id"));
	// }
	// if (jsonChild.isNull("payFlag")) {
	// dragonCard.setPayFlag(jsonChild.getInt("payFlag"));
	// }
	//
	// if (jsonChild.isNull("phone")) {
	// user.setMobile(jsonChild.getString("phone"));
	// }
	// if (jsonChild.isNull("reserve")) {
	// dragonCard.setReserve(jsonChild
	// .getString("reserve"));
	// }
	// if (jsonChild.isNull("status")) {
	// dragonCard.setStatus(jsonChild.getInt("status"));
	// }
	// if (jsonChild.isNull("transCode")) {
	// dragonCard.setTransCode(jsonChild
	// .getString("transCode"));
	// }
	// if (jsonChild.isNull("valid")) {
	// dragonCard.setValid(jsonChild.getString("valid"));
	// }
	// dragonCard.setUser(user);
	// }
	// map.put("data", dragonCard);
	// }
	// }
	// map.put("flag", flag);
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// return map;
	// }
}
