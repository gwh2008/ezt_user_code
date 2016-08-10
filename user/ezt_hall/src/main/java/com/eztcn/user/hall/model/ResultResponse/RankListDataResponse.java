package com.eztcn.user.hall.model.ResultResponse;

import java.io.Serializable;

/**
 * Created by 蒙 on 2016/6/17.
 */
public class RankListDataResponse implements Serializable{
    private String flag;
    private String name;
    private String rank;
    private String vol;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getVol() {
        return vol;
    }

    public void setVol(String vol) {
        this.vol = vol;
    }

    @Override
    public String toString() {
        return "RankListDataResponse{" +
                "flag='" + flag + '\'' +
                ", name='" + name + '\'' +
                ", rank='" + rank + '\'' +
                ", vol='" + vol + '\'' +
                '}';
    }
}
