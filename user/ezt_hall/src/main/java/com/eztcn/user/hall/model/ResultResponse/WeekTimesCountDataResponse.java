package com.eztcn.user.hall.model.ResultResponse;

import java.io.Serializable;

/**
 * Created by 蒙 on 2016/6/17.
 */
public class WeekTimesCountDataResponse implements Serializable{

    private String deptId;//
    private String doctorId;//
    private String endDate;//
    private String examinationFee;//
    private String hospitalId;//
    private String isAmPm;//
    private String isRemain;//
    private String noShowTime;//
    private String numType;//
    private String otherFee;//
    private String pfId;//
    private String poolId;//
    private String regDate;//
    private String regDateWeek;//
    private String regMark;//
    private String registerFee;//
    private String registerPoolId;//
    private String rpNum;//
    private String rpRemainNum;//
    private String startDate;//
    private String totalFee;//

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getExaminationFee() {
        return examinationFee;
    }

    public void setExaminationFee(String examinationFee) {
        this.examinationFee = examinationFee;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getIsAmPm() {
        return isAmPm;
    }

    public void setIsAmPm(String isAmPm) {
        this.isAmPm = isAmPm;
    }

    public String getIsRemain() {
        return isRemain;
    }

    public void setIsRemain(String isRemain) {
        this.isRemain = isRemain;
    }

    public String getNoShowTime() {
        return noShowTime;
    }

    public void setNoShowTime(String noShowTime) {
        this.noShowTime = noShowTime;
    }

    public String getNumType() {
        return numType;
    }

    public void setNumType(String numType) {
        this.numType = numType;
    }

    public String getOtherFee() {
        return otherFee;
    }

    public void setOtherFee(String otherFee) {
        this.otherFee = otherFee;
    }

    public String getPfId() {
        return pfId;
    }

    public void setPfId(String pfId) {
        this.pfId = pfId;
    }

    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getRegDateWeek() {
        return regDateWeek;
    }

    public void setRegDateWeek(String regDateWeek) {
        this.regDateWeek = regDateWeek;
    }

    public String getRegMark() {
        return regMark;
    }

    public void setRegMark(String regMark) {
        this.regMark = regMark;
    }

    public String getRegisterFee() {
        return registerFee;
    }

    public void setRegisterFee(String registerFee) {
        this.registerFee = registerFee;
    }

    public String getRegisterPoolId() {
        return registerPoolId;
    }

    public void setRegisterPoolId(String registerPoolId) {
        this.registerPoolId = registerPoolId;
    }

    public String getRpNum() {
        return rpNum;
    }

    public void setRpNum(String rpNum) {
        this.rpNum = rpNum;
    }

    public String getRpRemainNum() {
        return rpRemainNum;
    }

    public void setRpRemainNum(String rpRemainNum) {
        this.rpRemainNum = rpRemainNum;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    @Override
    public String toString() {
        return "WeekTimesCountDataResponse{" +
                "deptId='" + deptId + '\'' +
                ", doctorId='" + doctorId + '\'' +
                ", endDate='" + endDate + '\'' +
                ", examinationFee='" + examinationFee + '\'' +
                ", hospitalId='" + hospitalId + '\'' +
                ", isAmPm='" + isAmPm + '\'' +
                ", isRemain='" + isRemain + '\'' +
                ", noShowTime='" + noShowTime + '\'' +
                ", numType='" + numType + '\'' +
                ", otherFee='" + otherFee + '\'' +
                ", pfId='" + pfId + '\'' +
                ", poolId='" + poolId + '\'' +
                ", regDate='" + regDate + '\'' +
                ", regDateWeek='" + regDateWeek + '\'' +
                ", regMark='" + regMark + '\'' +
                ", registerFee='" + registerFee + '\'' +
                ", registerPoolId='" + registerPoolId + '\'' +
                ", rpNum='" + rpNum + '\'' +
                ", rpRemainNum='" + rpRemainNum + '\'' +
                ", startDate='" + startDate + '\'' +
                ", totalFee='" + totalFee + '\'' +
                '}';
    }
}
