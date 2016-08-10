package com.eztcn.user.hall.model.ResultResponse;

/**
 * Created by 蒙 on 2016/6/16.
 */
public class AttentionDoctorResponse {
    private String contentId;
    private String ecCancelDatetime;
    private String ecDatetime;
    private String ecMessage;
    private String ecState;
    private String ecType;
    private String id;
    private String status;
    private String userId;

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
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

    public String getEcMessage() {
        return ecMessage;
    }

    public void setEcMessage(String ecMessage) {
        this.ecMessage = ecMessage;
    }

    public String getEcState() {
        return ecState;
    }

    public void setEcState(String ecState) {
        this.ecState = ecState;
    }

    public String getEcType() {
        return ecType;
    }

    public void setEcType(String ecType) {
        this.ecType = ecType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "AttentionDoctorResponse{" +
                "contentId='" + contentId + '\'' +
                ", ecCancelDatetime='" + ecCancelDatetime + '\'' +
                ", ecDatetime='" + ecDatetime + '\'' +
                ", ecMessage='" + ecMessage + '\'' +
                ", ecState='" + ecState + '\'' +
                ", ecType='" + ecType + '\'' +
                ", id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
