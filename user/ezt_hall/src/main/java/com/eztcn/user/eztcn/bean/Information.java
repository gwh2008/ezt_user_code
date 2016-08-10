package com.eztcn.user.eztcn.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @title 资讯实体类
 * @describe
 * @author ezt
 * @created 2014年12月16日
 */
public class Information implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String infoTitle;// 资讯标题

	private String infoDescription;// 资讯描述

	private String imgUrl;// 图片地址

	private String createTime;// 创建时间

	private String body;// 主要内容

	private String evaluateNum;// 评价数

	private String type;// 类型

	private String source;// 来源

	private String url;// 分享链接地址
    private String articleUrl;// 添加了详情页面的网页地址字段
	
    private ArrayList<Doctor> doclist;//推荐医生列表
    

	public ArrayList<Doctor> getDoclist() {
		return doclist;
	}

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public void setDoclist(ArrayList<Doctor> doclist) {
		this.doclist = doclist;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getEvaluateNum() {
		return evaluateNum;
	}

	public void setEvaluateNum(String evaluateNum) {
		this.evaluateNum = evaluateNum;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInfoTitle() {
		return infoTitle;
	}

	public void setInfoTitle(String infoTitle) {
		this.infoTitle = infoTitle;
	}

	public String getInfoDescription() {
		return infoDescription;
	}

	public void setInfoDescription(String infoDescription) {
		this.infoDescription = infoDescription;
	}


	public Information(String id, String infoTitle, String infoDescription,
			String imgUrl, String createTime, String body, String evaluateNum,
			String type, String source, String url) {
		super();
		this.id = id;
		this.infoTitle = infoTitle;
		this.infoDescription = infoDescription;
		this.imgUrl = imgUrl;
		this.createTime = createTime;
		this.body = body;
		this.evaluateNum = evaluateNum;
		this.type = type;
		this.source = source;
		this.url = url;
	}

	public Information() {
		super();
	}

}
