package com.eztcn.user.hall.model.ResultResponse;

import java.io.Serializable;

/**
 * Created by 蒙 on 2016/6/17.
 */
public class HospitalListResponse implements Serializable{

    private String ehName;//医院名称
    private String rpMainNum;//可预约量
    private String rpNum;//总放号量
    private String id;//医院id
    private String ehPic;//医院图片
    private String ed_value_showCn;//医院等级
    private String ehIsneedPatientCard;//是否是天津市肿瘤医院,1代表是

    public String getEhIsneedPatientCard() {
        return ehIsneedPatientCard;
    }

    public void setEhIsneedPatientCard(String ehIsneedPatientCard) {
        this.ehIsneedPatientCard = ehIsneedPatientCard;
    }

    public String getEhName() {
        return ehName;
    }

    public void setEhName(String ehName) {
        this.ehName = ehName;
    }

    public String getRpMainNum() {
        return rpMainNum;
    }

    public void setRpMainNum(String rpMainNum) {
        this.rpMainNum = rpMainNum;
    }

    public String getRpNum() {
        return rpNum;
    }

    public void setRpNum(String rpNum) {
        this.rpNum = rpNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEhPic() {
        return ehPic;
    }

    public void setEhPic(String ehPic) {
        this.ehPic = ehPic;
    }

    public String getEd_value_showCn() {
        return ed_value_showCn;
    }

    public void setEd_value_showCn(String ed_value_showCn) {
        this.ed_value_showCn = ed_value_showCn;
    }

    @Override
    public String toString() {
        return "HospitalListResponse{" +
                "ehName='" + ehName + '\'' +
                ", rpMainNum='" + rpMainNum + '\'' +
                ", rpNum='" + rpNum + '\'' +
                ", id='" + id + '\'' +
                ", ehPic='" + ehPic + '\'' +
                ", ed_value_showCn='" + ed_value_showCn + '\'' +
                ", ehIsneedPatientCard='" + ehIsneedPatientCard + '\'' +
                '}';
    }
}
