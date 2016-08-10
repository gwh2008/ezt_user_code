package com.eztcn.user.hall.model.bean;

import java.io.Serializable;
/**
 * Created by lx on 2016/6/28.
 * 更新bean。
 */
public class UpdateBean implements Serializable{

    private int id;
    private int isMandatory;
    private int status;
    private  String vuContent;
    private  String vuCreateTime;
    private  int vuDownloadTimes;
    private  String vuDownloadUrl;
    private  String vuNumber;
    private  int vuSn;
    private  int vuType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(int isMandatory) {
        this.isMandatory = isMandatory;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getVuContent() {
        return vuContent;
    }

    public void setVuContent(String vuContent) {
        this.vuContent = vuContent;
    }

    public String getVuCreateTime() {
        return vuCreateTime;
    }

    public void setVuCreateTime(String vuCreateTime) {
        this.vuCreateTime = vuCreateTime;
    }

    public int getVuDownloadTimes() {
        return vuDownloadTimes;
    }

    public void setVuDownloadTimes(int vuDownloadTimes) {
        this.vuDownloadTimes = vuDownloadTimes;
    }

    public String getVuDownloadUrl() {
        return vuDownloadUrl;
    }

    public void setVuDownloadUrl(String vuDownloadUrl) {
        this.vuDownloadUrl = vuDownloadUrl;
    }

    public String getVuNumber() {
        return vuNumber;
    }

    public void setVuNumber(String vuNumber) {
        this.vuNumber = vuNumber;
    }

    public int getVuSn() {
        return vuSn;
    }

    public void setVuSn(int vuSn) {
        this.vuSn = vuSn;
    }

    public int getVuType() {
        return vuType;
    }

    public void setVuType(int vuType) {
        this.vuType = vuType;
    }
}
