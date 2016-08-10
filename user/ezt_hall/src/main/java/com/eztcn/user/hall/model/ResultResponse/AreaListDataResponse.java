package com.eztcn.user.hall.model.ResultResponse;

/**
 * Created by 蒙 on 2016/6/18.
 */
public class AreaListDataResponse {
    private String countyname;
    private String cityid;
    private String ezOrder;
    private String status;
    private String synStatus;
    private String id;

    public String getCountyname() {
        return countyname;
    }

    public void setCountyname(String countyname) {
        this.countyname = countyname;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getEzOrder() {
        return ezOrder;
    }

    public void setEzOrder(String ezOrder) {
        this.ezOrder = ezOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSynStatus() {
        return synStatus;
    }

    public void setSynStatus(String synStatus) {
        this.synStatus = synStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AreaListDataResponse{" +
                "countyname='" + countyname + '\'' +
                ", cityid='" + cityid + '\'' +
                ", ezOrder='" + ezOrder + '\'' +
                ", status='" + status + '\'' +
                ", synStatus='" + synStatus + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
