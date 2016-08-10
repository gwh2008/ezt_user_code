package com.eztcn.user.hall.model;

/**
 * @Author: lizhipeng
 * @Data: 16/5/31 上午9:33
 * @Description:
 */
public class LoginRequest extends Request {
    private String name;
    private String pwd;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
