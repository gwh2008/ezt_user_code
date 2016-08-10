package com.eztcn.user.hall.model;

/**
 * @Author: lizhipeng
 * @Data: 16/6/25 下午6:03
 * @Description:
 */
public class UpLoadAvatarResponse {

    /**
     * data : http://images.eztcn.com.cn/images/patient/eztcn2.0/7BA88A782515F9E7B78ACAD1A6C0E5A6.jpeg
     * detailMsg : 数据修改成功!
     * flag : true
     * msg : 数据修改成功!
     * number : 2002
     */

    private String data;
    private String detailMsg;
    private boolean flag;
    private String msg;
    private String number;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDetailMsg() {
        return detailMsg;
    }

    public void setDetailMsg(String detailMsg) {
        this.detailMsg = detailMsg;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
