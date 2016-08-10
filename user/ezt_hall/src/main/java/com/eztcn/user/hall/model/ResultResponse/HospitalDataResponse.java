package com.eztcn.user.hall.model.ResultResponse;

import java.io.Serializable;

/**
 * Created by 蒙 on 2016/6/17.
 */
public class HospitalDataResponse implements Serializable{

    private String ehName;//医院名称
    private String rpMainNum;//可预约量
    private String rpNum;//总放号量
    private String id;//医院id
    private String ehTel;//医院电话
    private String remark;//简介
    private String ehAddress;//地址
    private String ehPicIndex;//医院图片
    private String ehIsOpenBed;//是否开通预约病床
    private String ehIsOpenOrderCheck;//是否开通预约检查
    private String subscribeDrug;//是否开通预约药品

    public String getSubscribeDrug() {
        return subscribeDrug;
    }

    public void setSubscribeDrug(String subscribeDrug) {
        this.subscribeDrug = subscribeDrug;
    }

    public String getEhIsOpenBed() {
        return ehIsOpenBed;
    }

    public void setEhIsOpenBed(String ehIsOpenBed) {
        this.ehIsOpenBed = ehIsOpenBed;
    }

    public String getEhIsOpenOrderCheck() {
        return ehIsOpenOrderCheck;
    }

    public void setEhIsOpenOrderCheck(String ehIsOpenOrderCheck) {
        this.ehIsOpenOrderCheck = ehIsOpenOrderCheck;
    }

    public String getEhPicIndex() {
        return ehPicIndex;
    }

    public void setEhPicIndex(String ehPicIndex) {
        this.ehPicIndex = ehPicIndex;
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

    public String getEhTel() {
        return ehTel;
    }

    public void setEhTel(String ehTel) {
        this.ehTel = ehTel;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEhAddress() {
        return ehAddress;
    }

    public void setEhAddress(String ehAddress) {
        this.ehAddress = ehAddress;
    }

}
