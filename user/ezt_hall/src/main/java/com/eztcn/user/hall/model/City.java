package com.eztcn.user.hall.model;

import java.io.Serializable;

import xutils.db.annotation.Column;
import xutils.db.annotation.Id;
import xutils.db.annotation.Table;

public class City implements Serializable {
	private String id;
	private String ezOrder;
	private String cityname;
	private String provinceid;
	private String status;
	private String synStatus;
    private String sortLetters; // 显示数据拼音的首字母

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEzOrder() {
        return ezOrder;
    }

    public void setEzOrder(String ezOrder) {
        this.ezOrder = ezOrder;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSynStatus() {
        return synStatus;
    }

    public void setSynStatus(String synStatus) {
        this.synStatus = synStatus;
    }

    @Override
    public String toString() {
        return "City{" +
                "id='" + id + '\'' +
                ", ezOrder='" + ezOrder + '\'' +
                ", cityname='" + cityname + '\'' +
                ", provinceid='" + provinceid + '\'' +
                ", status='" + status + '\'' +
                ", synStatus='" + synStatus + '\'' +
                ", sortLetters='" + sortLetters + '\'' +
                '}';
    }
}
