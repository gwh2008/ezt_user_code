package com.eztcn.user.hall.model.ResultResponse;

import java.util.ArrayList;

/**
 * Created by 蒙 on 2016/6/21.
 */
public class DoctorSelectRuleResponse {
    private ArrayList<CityListDataResponse> doctorLevel;
    private ArrayList<CityListDataResponse> registerType;

    public ArrayList<CityListDataResponse> getDoctorLevel() {
        return doctorLevel;
    }

    public void setDoctorLevel(ArrayList<CityListDataResponse> doctorLevel) {
        this.doctorLevel = doctorLevel;
    }

    public ArrayList<CityListDataResponse> getRegisterType() {
        return registerType;
    }

    public void setRegisterType(ArrayList<CityListDataResponse> registerType) {
        this.registerType = registerType;
    }

    @Override
    public String toString() {
        return "DoctorSelectRuleResponse{" +
                "doctorLevel=" + doctorLevel +
                ", registerType=" + registerType +
                '}';
    }
}
