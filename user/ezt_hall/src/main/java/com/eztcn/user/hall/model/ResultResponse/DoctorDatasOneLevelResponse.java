package com.eztcn.user.hall.model.ResultResponse;

import java.io.Serializable;

/**
 * Created by 蒙 on 2016/6/17.
 */
public class DoctorDatasOneLevelResponse implements Serializable{

    private String collectCount;//关注数
    private String isCollect; //是否被关注
    private DoctorDatasTwoLevelResponse rankBean;
    private String regCount;//预约量

    public String getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(String isCollect) {
        this.isCollect = isCollect;
    }

    public String getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(String collectCount) {
        this.collectCount = collectCount;
    }

    public DoctorDatasTwoLevelResponse getRankBean() {
        return rankBean;
    }

    public void setRankBean(DoctorDatasTwoLevelResponse rankBean) {
        this.rankBean = rankBean;
    }

    public String getRegCount() {
        return regCount;
    }

    public void setRegCount(String regCount) {
        this.regCount = regCount;
    }

    @Override
    public String toString() {
        return "DoctorDatasOneLevelResponse{" +
                "collectCount='" + collectCount + '\'' +
                ", isCollect=" + isCollect +
                ", rankBean=" + rankBean +
                ", regCount='" + regCount + '\'' +
                '}';
    }
}
