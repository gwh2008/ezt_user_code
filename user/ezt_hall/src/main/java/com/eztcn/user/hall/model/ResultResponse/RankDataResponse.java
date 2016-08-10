package com.eztcn.user.hall.model.ResultResponse;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 蒙 on 2016/6/17.
 */
public class RankDataResponse implements Serializable{
    private ArrayList<RankListDataResponse> month;
    private ArrayList<RankListDataResponse> week;
    private ArrayList<RankListDataResponse> year;

    public ArrayList<RankListDataResponse> getMonth() {
        return month;
    }

    public void setMonth(ArrayList<RankListDataResponse> month) {
        this.month = month;
    }

    public ArrayList<RankListDataResponse> getWeek() {
        return week;
    }

    public void setWeek(ArrayList<RankListDataResponse> week) {
        this.week = week;
    }

    public ArrayList<RankListDataResponse> getYear() {
        return year;
    }

    public void setYear(ArrayList<RankListDataResponse> year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "RankDataResponse{" +
                "month=" + month +
                ", week=" + week +
                ", year=" + year +
                '}';
    }
}
