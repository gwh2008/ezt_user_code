package com.eztcn.user.hall.model.bean;

import java.io.Serializable;

/**
 * Created by lx on 2016/6/17.
 * 请求参数。
 */
public class IdParams implements Serializable{

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
