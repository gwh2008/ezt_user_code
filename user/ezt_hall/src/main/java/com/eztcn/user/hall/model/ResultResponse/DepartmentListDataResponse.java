package com.eztcn.user.hall.model.ResultResponse;

/**
 * Created by 蒙 on 2016/6/22.
 */
public class DepartmentListDataResponse {

    private String dcLevel;
    private String dcName;
    private String dptCateId;
    private String dptHid;
    private String dptName;
    private String dtpId;
    private String isDept;
    private String oneLevelDcName;
    private String pid;

    private String rpNum;
    private String rpRemainNum;

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

    public String getDcLevel() {
        return dcLevel;
    }

    public void setDcLevel(String dcLevel) {
        this.dcLevel = dcLevel;
    }

    public String getDcName() {
        return dcName;
    }

    public void setDcName(String dcName) {
        this.dcName = dcName;
    }

    public String getDptCateId() {
        return dptCateId;
    }

    public void setDptCateId(String dptCateId) {
        this.dptCateId = dptCateId;
    }

    public String getDptHid() {
        return dptHid;
    }

    public void setDptHid(String dptHid) {
        this.dptHid = dptHid;
    }

    public String getDptName() {
        return dptName;
    }

    public void setDptName(String dptName) {
        this.dptName = dptName;
    }

    public String getDtpId() {
        return dtpId;
    }

    public void setDtpId(String dtpId) {
        this.dtpId = dtpId;
    }

    public String getIsDept() {
        return isDept;
    }

    public void setIsDept(String isDept) {
        this.isDept = isDept;
    }

    public String getOneLevelDcName() {
        return oneLevelDcName;
    }

    public void setOneLevelDcName(String oneLevelDcName) {
        this.oneLevelDcName = oneLevelDcName;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "DepartmentListDataResponse{" +
                "dcLevel='" + dcLevel + '\'' +
                ", dcName='" + dcName + '\'' +
                ", dptCateId='" + dptCateId + '\'' +
                ", dptHid='" + dptHid + '\'' +
                ", dptName='" + dptName + '\'' +
                ", dtpId='" + dtpId + '\'' +
                ", isDept='" + isDept + '\'' +
                ", oneLevelDcName='" + oneLevelDcName + '\'' +
                ", pid='" + pid + '\'' +
                ", rpNum='" + rpNum + '\'' +
                ", rpRemainNum='" + rpRemainNum + '\'' +
                '}';
    }
}
