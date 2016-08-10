package com.eztcn.user.hall.model.ResultResponse;

import com.eztcn.user.hall.model.bean.Family;
import com.eztcn.user.hall.model.bean.Patient;
import java.io.Serializable;
/**
 * Created by lx on 2016/6/17.
 * 我的就诊人的返回的数据bean。
 */
public class MyPatientResponse implements Serializable{

    private Family family;
    private Patient patient;

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
