package com.eztcn.user.hall.model.bean;

import java.io.Serializable;

/**
 * Created by lx on 2016/6/16.
 * 我的关注bean。
 */
public class MyAttention implements Serializable {

  private   CollectBean eztCollectBean;
  private   HosDeptDocBean eztHosDeptDocBean;


    public CollectBean getEztCollectBean() {
        return eztCollectBean;
    }

    public void setEztCollectBean(CollectBean eztCollectBean) {
        this.eztCollectBean = eztCollectBean;
    }

    public HosDeptDocBean getEztHosDeptDocBean() {
        return eztHosDeptDocBean;
    }

    public void setEztHosDeptDocBean(HosDeptDocBean eztHosDeptDocBean) {
        this.eztHosDeptDocBean = eztHosDeptDocBean;
    }
}
