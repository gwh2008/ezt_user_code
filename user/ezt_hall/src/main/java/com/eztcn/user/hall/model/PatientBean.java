package com.eztcn.user.hall.model;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by lx on 2016/6/8.
 * 患者bean。
 */
public class PatientBean implements Serializable{

    private int auth;
    private String createby;
    private String createdate;
    private String epAddress;
    private int epAge;
    private String epBirthCertificate;
    private String epBirthday;

    public int getAuth() {
        return auth;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

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

    public String getEpAddress() {
        return epAddress;
    }

    public void setEpAddress(String epAddress) {
        this.epAddress = epAddress;
    }

    public int getEpAge() {
        return epAge;
    }

    public void setEpAge(int epAge) {
        this.epAge = epAge;
    }

    public String getEpBirthCertificate() {
        return epBirthCertificate;
    }

    public void setEpBirthCertificate(String epBirthCertificate) {
        this.epBirthCertificate = epBirthCertificate;
    }

    public String getEpBirthday() {
        return epBirthday;
    }

    public void setEpBirthday(String epBirthday) {
        this.epBirthday = epBirthday;
    }

    public String getEpBorndate() {
        return epBorndate;
    }

    public void setEpBorndate(String epBorndate) {
        this.epBorndate = epBorndate;
    }

    public int getEpCity() {
        return epCity;
    }

    public void setEpCity(int epCity) {
        this.epCity = epCity;
    }

    public String getEpContactinfo() {
        return epContactinfo;
    }

    public void setEpContactinfo(String epContactinfo) {
        this.epContactinfo = epContactinfo;
    }

    public int getEpCounty() {
        return epCounty;
    }

    public void setEpCounty(int epCounty) {
        this.epCounty = epCounty;
    }

    public int getEpCulturalLeave() {
        return epCulturalLeave;
    }

    public void setEpCulturalLeave(int epCulturalLeave) {
        this.epCulturalLeave = epCulturalLeave;
    }

    public String getEpHiid() {
        return epHiid;
    }

    public void setEpHiid(String epHiid) {
        this.epHiid = epHiid;
    }

    public String getEpHkMacaoPass() {
        return epHkMacaoPass;
    }

    public void setEpHkMacaoPass(String epHkMacaoPass) {
        this.epHkMacaoPass = epHkMacaoPass;
    }

    public int getEpIdnoType() {
        return epIdnoType;
    }

    public void setEpIdnoType(int epIdnoType) {
        this.epIdnoType = epIdnoType;
    }

    public int getEpIncomeRange() {
        return epIncomeRange;
    }

    public void setEpIncomeRange(int epIncomeRange) {
        this.epIncomeRange = epIncomeRange;
    }

    public int getEpIsCheeck() {
        return epIsCheeck;
    }

    public void setEpIsCheeck(int epIsCheeck) {
        this.epIsCheeck = epIsCheeck;
    }

    public String getEpMilitaryCard() {
        return epMilitaryCard;
    }

    public void setEpMilitaryCard(String epMilitaryCard) {
        this.epMilitaryCard = epMilitaryCard;
    }

    public String getEpMobile() {
        return epMobile;
    }

    public void setEpMobile(String epMobile) {
        this.epMobile = epMobile;
    }

    public String getEpName() {
        return epName;
    }

    public void setEpName(String epName) {
        this.epName = epName;
    }

    public int getEpNation() {
        return epNation;
    }

    public void setEpNation(int epNation) {
        this.epNation = epNation;
    }

    public int getEpNativeProvince() {
        return epNativeProvince;
    }

    public void setEpNativeProvince(int epNativeProvince) {
        this.epNativeProvince = epNativeProvince;
    }

    public int getEpNativecity() {
        return epNativecity;
    }

    public void setEpNativecity(int epNativecity) {
        this.epNativecity = epNativecity;
    }

    public int getEpNativecounty() {
        return epNativecounty;
    }

    public void setEpNativecounty(int epNativecounty) {
        this.epNativecounty = epNativecounty;
    }

    public String getEpPassport() {
        return epPassport;
    }

    public void setEpPassport(String epPassport) {
        this.epPassport = epPassport;
    }

    public String getEpPic() {
        if (!TextUtils.isEmpty(epPic)){
            return epPic.substring(epPic.lastIndexOf("/"));
        }
        return "";
    }

    public void setEpPic(String epPic) {
        this.epPic = epPic;
    }

    public String getEpPid() {
        return epPid;
    }

    public void setEpPid(String epPid) {
        this.epPid = epPid;
    }

    public int getEpProfession() {
        return epProfession;
    }

    public void setEpProfession(int epProfession) {
        this.epProfession = epProfession;
    }

    public int getEpProvince() {
        return epProvince;
    }

    public void setEpProvince(int epProvince) {
        this.epProvince = epProvince;
    }

    public int getEpRemote() {
        return epRemote;
    }

    public void setEpRemote(int epRemote) {
        this.epRemote = epRemote;
    }

    public int getEpSex() {
        return epSex;
    }

    public void setEpSex(int epSex) {
        this.epSex = epSex;
    }

    public String getEpSocialSecurity() {
        return epSocialSecurity;
    }

    public void setEpSocialSecurity(String epSocialSecurity) {
        this.epSocialSecurity = epSocialSecurity;
    }

    public int getEpVip() {
        return epVip;
    }

    public void setEpVip(int epVip) {
        this.epVip = epVip;
    }

    public int getEpWedlock() {
        return epWedlock;
    }

    public void setEpWedlock(int epWedlock) {
        this.epWedlock = epWedlock;
    }

    public String getEpZipcode() {
        return epZipcode;
    }

    public void setEpZipcode(String epZipcode) {
        this.epZipcode = epZipcode;
    }

    public int getFrozen() {
        return frozen;
    }

    public void setFrozen(int frozen) {
        this.frozen = frozen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    private String epBorndate;
    private int epCity;
    private String epContactinfo;
    private int epCounty;
    private int epCulturalLeave;
    private String epHiid;//医保号码。
    private String epHkMacaoPass;
    private int epIdnoType;
    private int epIncomeRange;
    private int epIsCheeck;
    private String epMilitaryCard;
    private String epMobile;
    private String epName;
    private int epNation;
    private int epNativeProvince;
    private int epNativecity;
    private int epNativecounty;
    private String epPassport;
    private String epPic;
    private String epPid;//身份证号码
    private int epProfession;
    private int epProvince;
    private int epRemote;
    private int epSex;
    private String epSocialSecurity;
    private int epVip;
    private int epWedlock;
    private String epZipcode;
    private int frozen;
    private int id;
    private String lasteditby;
    private String lasteditdate;
    private int patientId;
    private int status;
    private int synStatus;
    private int userId;

}
