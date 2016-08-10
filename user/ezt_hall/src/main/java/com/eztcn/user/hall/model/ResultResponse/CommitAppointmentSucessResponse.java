package com.eztcn.user.hall.model.ResultResponse;

import java.io.Serializable;

/**
 * Created by 蒙 on 2016/6/17.
 */
public class CommitAppointmentSucessResponse implements Serializable {

    private String checkCode;
    private String ehName;//医院名称
    private String dptName;//科室名称
    private String edName;//医生名称
    private String edLevel; //doctorLevel id
    private String edLevelStr;//doctorLevel Name
    private String regId;// id
    private String regTime;//预约时间
    private String printTime;
    private String euNumber;
    private String mobile;//就诊人电话
    private String epName;//就诊人姓名
    private String payWay;//缴费方式
    private String epIdnoType;
    private String epPid;//身份证号码
    private String epHiid;
    private String epSex;
    private String epAge;
    private String content;//描述
    private String medicareCardNum;//医保卡号
    private String visitCardNum;//就诊卡号
    private String dptId;//科室ID
    private String beginTime;
    private String endTime;
    private String pic;//医生头像直接可以用
    private String isFirst;
    private String doctorId;//医生id
    private String edFree;//挂号费


    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getEdFree() {
        return edFree;
    }

    public void setEdFree(String edFree) {
        this.edFree = edFree;
    }

    public String getDptId() {
        return dptId;
    }

    public void setDptId(String dptId) {
        this.dptId = dptId;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(String isFirst) {
        this.isFirst = isFirst;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getEhName() {
        return ehName;
    }

    public void setEhName(String ehName) {
        this.ehName = ehName;
    }

    public String getDptName() {
        return dptName;
    }

    public void setDptName(String dptName) {
        this.dptName = dptName;
    }

    public String getEdName() {
        return edName;
    }

    public void setEdName(String edName) {
        this.edName = edName;
    }

    public String getEdLevel() {
        return edLevel;
    }

    public void setEdLevel(String edLevel) {
        this.edLevel = edLevel;
    }

    public String getEdLevelStr() {
        return edLevelStr;
    }

    public void setEdLevelStr(String edLevelStr) {
        this.edLevelStr = edLevelStr;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public String getPrintTime() {
        return printTime;
    }

    public void setPrintTime(String printTime) {
        this.printTime = printTime;
    }

    public String getEuNumber() {
        return euNumber;
    }

    public void setEuNumber(String euNumber) {
        this.euNumber = euNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEpName() {
        return epName;
    }

    public void setEpName(String epName) {
        this.epName = epName;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getEpIdnoType() {
        return epIdnoType;
    }

    public void setEpIdnoType(String epIdnoType) {
        this.epIdnoType = epIdnoType;
    }

    public String getEpPid() {
        return epPid;
    }

    public void setEpPid(String epPid) {
        this.epPid = epPid;
    }

    public String getEpHiid() {
        return epHiid;
    }

    public void setEpHiid(String epHiid) {
        this.epHiid = epHiid;
    }

    public String getEpSex() {
        return epSex;
    }

    public void setEpSex(String epSex) {
        this.epSex = epSex;
    }

    public String getEpAge() {
        return epAge;
    }

    public void setEpAge(String epAge) {
        this.epAge = epAge;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMedicareCardNum() {
        return medicareCardNum;
    }

    public void setMedicareCardNum(String medicareCardNum) {
        this.medicareCardNum = medicareCardNum;
    }

    public String getVisitCardNum() {
        return visitCardNum;
    }

    public void setVisitCardNum(String visitCardNum) {
        this.visitCardNum = visitCardNum;
    }

    @Override
    public String toString() {
        return "CommitAppointmentSucessResponse{" +
                "checkCode='" + checkCode + '\'' +
                ", ehName='" + ehName + '\'' +
                ", dptName='" + dptName + '\'' +
                ", edName='" + edName + '\'' +
                ", edLevel='" + edLevel + '\'' +
                ", edLevelStr='" + edLevelStr + '\'' +
                ", regId='" + regId + '\'' +
                ", regTime='" + regTime + '\'' +
                ", printTime='" + printTime + '\'' +
                ", euNumber='" + euNumber + '\'' +
                ", mobile='" + mobile + '\'' +
                ", epName='" + epName + '\'' +
                ", payWay='" + payWay + '\'' +
                ", epIdnoType='" + epIdnoType + '\'' +
                ", epPid='" + epPid + '\'' +
                ", epHiid='" + epHiid + '\'' +
                ", epSex='" + epSex + '\'' +
                ", epAge='" + epAge + '\'' +
                ", content='" + content + '\'' +
                ", medicareCardNum='" + medicareCardNum + '\'' +
                ", visitCardNum='" + visitCardNum + '\'' +
                ", dptId='" + dptId + '\'' +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", pic='" + pic + '\'' +
                ", isFirst='" + isFirst + '\'' +
                ", doctorId='" + doctorId + '\'' +
                ", edFree='" + edFree + '\'' +
                '}';
    }
}
