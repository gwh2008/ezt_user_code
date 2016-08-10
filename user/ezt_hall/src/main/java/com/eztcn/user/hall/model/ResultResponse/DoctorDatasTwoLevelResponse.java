package com.eztcn.user.hall.model.ResultResponse;

import java.io.Serializable;

/**
 * Created by 蒙 on 2016/6/17.
 */
public class DoctorDatasTwoLevelResponse implements Serializable{

    private String monthFlag;//月升降 (1升  -1降)
    private String monthRank;//月排行
    private String weekFlag;
    private String weekRank;
    private String yearFlag;
    private String yearRank;

    public String getMonthFlag() {
        return monthFlag;
    }

    public void setMonthFlag(String monthFlag) {
        this.monthFlag = monthFlag;
    }

    public String getMonthRank() {
        return monthRank;
    }

    public void setMonthRank(String monthRank) {
        this.monthRank = monthRank;
    }

    public String getWeekFlag() {
        return weekFlag;
    }

    public void setWeekFlag(String weekFlag) {
        this.weekFlag = weekFlag;
    }

    public String getWeekRank() {
        return weekRank;
    }

    public void setWeekRank(String weekRank) {
        this.weekRank = weekRank;
    }

    public String getYearFlag() {
        return yearFlag;
    }

    public void setYearFlag(String yearFlag) {
        this.yearFlag = yearFlag;
    }

    public String getYearRank() {
        return yearRank;
    }

    public void setYearRank(String yearRank) {
        this.yearRank = yearRank;
    }

    @Override
    public String toString() {
        return "DoctorDatasTwoLevelResponse{" +
                "monthFlag='" + monthFlag + '\'' +
                ", monthRank='" + monthRank + '\'' +
                ", weekFlag='" + weekFlag + '\'' +
                ", weekRank='" + weekRank + '\'' +
                ", yearFlag='" + yearFlag + '\'' +
                ", yearRank='" + yearRank + '\'' +
                '}';
    }
}
