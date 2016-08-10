package com.eztcn.user.hall.model;

import java.util.Objects;

/**
 * Created by lx on 2016/6/8.
 * 返回的数据的 bean。
 */
public class DataResponse extends Response {

    public UserBean getUserbean() {
        return userbean;
    }

    public void setUserbean(UserBean userbean) {
        this.userbean = userbean;
    }

    public PatientBean getPatientBean() {
        return patientBean;
    }

    public void setPatientBean(PatientBean patientBean) {
        this.patientBean = patientBean;
    }

    public UserBean  userbean;
    public PatientBean patientBean;

}
