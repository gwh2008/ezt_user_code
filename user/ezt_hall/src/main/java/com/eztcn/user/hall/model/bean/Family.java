package com.eztcn.user.hall.model.bean;
import java.io.Serializable;
/**
 * Created by lx on 2016/6/17.
 */
public class Family implements Serializable{
    public String getCreateby() {
        return createby;
    }

    public void setCreateby(String createby) {
        this.createby = createby;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsMain() {
        return isMain;
    }

    public void setIsMain(int isMain) {
        this.isMain = isMain;
    }

    public int getKinship() {
        return kinship;
    }

    public void setKinship(int kinship) {
        this.kinship = kinship;
    }

    public String getLasteditby() {
        return lasteditby;
    }

    public void setLasteditby(String lasteditby) {
        this.lasteditby = lasteditby;
    }

    public String getLasteditdate() {
        return lasteditdate;
    }

    public void setLasteditdate(String lasteditdate) {
        this.lasteditdate = lasteditdate;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSynStatus() {
        return synStatus;
    }

    public void setSynStatus(int synStatus) {
        this.synStatus = synStatus;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private String createby;
    private String createdate;
    private int id;
    private int isMain;
    private int kinship;
    private String lasteditby;
    private String lasteditdate;
    private int patientId;
    private int status;
    private int synStatus;
    private int userId;
}
