package com.eztcn.user.hall.model.bean;

/**
 * Created by lx on 2016/6/16.
 * 我的关注返回的bean。
 */
public class CollectBean {

    private int contentId;
    private String ecCancelDatetime;
    private String ecDatetime;
    private int ecMessage;
    private int ecState;
    private int ecType;
    private int id;
    private int status;
    private int userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public String getEcCancelDatetime() {
        return ecCancelDatetime;
    }

    public void setEcCancelDatetime(String ecCancelDatetime) {
        this.ecCancelDatetime = ecCancelDatetime;
    }

    public String getEcDatetime() {
        return ecDatetime;
    }

    public void setEcDatetime(String ecDatetime) {
        this.ecDatetime = ecDatetime;
    }

    public int getEcMessage() {
        return ecMessage;
    }

    public void setEcMessage(int ecMessage) {
        this.ecMessage = ecMessage;
    }

    public int getEcState() {
        return ecState;
    }

    public void setEcState(int ecState) {
        this.ecState = ecState;
    }

    public int getEcType() {
        return ecType;
    }

    public void setEcType(int ecType) {
        this.ecType = ecType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
