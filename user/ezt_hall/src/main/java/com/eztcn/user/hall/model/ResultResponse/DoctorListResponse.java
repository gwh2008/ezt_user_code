package com.eztcn.user.hall.model.ResultResponse;

import java.io.Serializable;

/**
 * Created by 蒙 on 2016/6/17.
 */
public class DoctorListResponse implements Serializable{

    private String docId;//医生id
    private String docName;//医生名字
    private String levelId;//级别id
    private String levelName;//级别名称
    private String hospitalId;//医院id
    private String hospitalName;//医院名称
    private String deptId;//部门id
    private String dptName;//部门名称
    private String goodAt;//擅长
    private Integer orderNum;//可预约量
    private String docPic;//头像
    private String docSex;//性别


    public String getDocPic() {
        return docPic;
    }

    public void setDocPic(String docPic) {
        this.docPic = docPic;
    }

    public String getDocSex() {
        return docSex;
    }

    public void setDocSex(String docSex) {
        this.docSex = docSex;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDptName() {
        return dptName;
    }

    public void setDptName(String dptName) {
        this.dptName = dptName;
    }

    public String getGoodAt() {
        return goodAt;
    }

    public void setGoodAt(String goodAt) {
        this.goodAt = goodAt;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public String toString() {
        return "DoctorListResponse{" +
                "docId='" + docId + '\'' +
                ", docName='" + docName + '\'' +
                ", levelId='" + levelId + '\'' +
                ", levelName='" + levelName + '\'' +
                ", hospitalId='" + hospitalId + '\'' +
                ", hospitalName='" + hospitalName + '\'' +
                ", deptId='" + deptId + '\'' +
                ", dptName='" + dptName + '\'' +
                ", goodAt='" + goodAt + '\'' +
                ", orderNum=" + orderNum +
                ", docPic='" + docPic + '\'' +
                ", docSex='" + docSex + '\'' +
                '}';
    }
}
