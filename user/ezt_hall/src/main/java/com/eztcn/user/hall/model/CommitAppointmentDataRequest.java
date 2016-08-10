package com.eztcn.user.hall.model;

import java.io.Serializable;

/**
 * Created by 蒙 on 2016/6/28.
 */
public class CommitAppointmentDataRequest implements Serializable{
    private String  operateUserId;//用户id
    private String  poolId;//号池id
    private String  pfId;//350
    private String  sourcePfId;//平台id，是安卓平台和苹果平台的区分标志  是安卓  355
    private String  cartoonNum;//""
    private String  cardNum;//""
    private String  ip;//""
    private String  regMark;//401
    private String  mobile;//用户手机号
    private String  idno;//用户epPid
    private String  patientId;//就诊人id
    private String  payWay;//0自费 1医保
    private String  isFirst;//0初诊 1 复诊
    private String  medicareCardNum;//医保卡号
    private String  visitCardNum;//就诊卡号
    private String  code;//验证码

    public String getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(String operateUserId) {
        this.operateUserId = operateUserId;
    }

    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }

    public String getPfId() {
        return pfId;
    }

    public void setPfId(String pfId) {
        this.pfId = pfId;
    }

    public String getSourcePfId() {
        return sourcePfId;
    }

    public void setSourcePfId(String sourcePfId) {
        this.sourcePfId = sourcePfId;
    }

    public String getCartoonNum() {
        return cartoonNum;
    }

    public void setCartoonNum(String cartoonNum) {
        this.cartoonNum = cartoonNum;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRegMark() {
        return regMark;
    }

    public void setRegMark(String regMark) {
        this.regMark = regMark;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(String isFirst) {
        this.isFirst = isFirst;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "CommitAppointmentDataRequest{" +
                "operateUserId='" + operateUserId + '\'' +
                ", poolId='" + poolId + '\'' +
                ", pfId='" + pfId + '\'' +
                ", sourcePfId='" + sourcePfId + '\'' +
                ", cartoonNum='" + cartoonNum + '\'' +
                ", cardNum='" + cardNum + '\'' +
                ", ip='" + ip + '\'' +
                ", regMark='" + regMark + '\'' +
                ", mobile='" + mobile + '\'' +
                ", idno='" + idno + '\'' +
                ", patientId='" + patientId + '\'' +
                ", payWay='" + payWay + '\'' +
                ", isFirst='" + isFirst + '\'' +
                ", medicareCardNum='" + medicareCardNum + '\'' +
                ", visitCardNum='" + visitCardNum + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
