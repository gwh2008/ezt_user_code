package com.eztcn.user.hall.model.ResultResponse;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 蒙 on 2016/6/17.
 */
public class WeekDatasResponse implements Serializable{

    private String doctorPoolDate;//当日时间
    private String doctorPoolsNums;//可预约时间段的个数
    private ArrayList<WeekTimesCountDataResponse> doctorPools;//时间段的数据

    public String getDoctorPoolDate() {
        return doctorPoolDate;
    }

    public void setDoctorPoolDate(String doctorPoolDate) {
        this.doctorPoolDate = doctorPoolDate;
    }

    public String getDoctorPoolsNums() {
        return doctorPoolsNums;
    }

    public void setDoctorPoolsNums(String doctorPoolsNums) {
        this.doctorPoolsNums = doctorPoolsNums;
    }

    public ArrayList<WeekTimesCountDataResponse> getDoctorPools() {
        return doctorPools;
    }

    public void setDoctorPools(ArrayList<WeekTimesCountDataResponse> doctorPools) {
        this.doctorPools = doctorPools;
    }

    @Override
    public String toString() {
        return "WeekDatasResponse{" +
                "doctorPoolDate='" + doctorPoolDate + '\'' +
                ", doctorPoolsNums='" + doctorPoolsNums + '\'' +
                ", doctorPools=" + doctorPools +
                '}';
    }
}
