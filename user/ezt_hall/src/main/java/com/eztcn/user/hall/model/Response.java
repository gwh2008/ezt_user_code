package com.eztcn.user.hall.model;

/**
 * @Author: lizhipeng
 * @Data: 16/5/24 上午10:13
 * @Description:
 */
public class Response<T> implements IModel {
    protected int code;
    protected String message;
    protected String detailMsg;
    protected String msg;
    protected String number;
    protected boolean flag;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getDetailMsg() {
        return detailMsg;
    }

    public void setDetailMsg(String detailMsg) {
        this.detailMsg = detailMsg;
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

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", detailMsg='" + detailMsg + '\'' +
                ", msg='" + msg + '\'' +
                ", number='" + number + '\'' +
                ", flag='" + flag + '\'' +
                ", data='" + data.toString() +
                '}';
    }
}
