package com.eztcn.user.hall.common;

/**
 * @Author: lizhipeng
 * @Data: 16/6/25 上午11:00
 * @Description: 龙卡单例
 */
public class DragonStatusSingle {
    private static DragonStatusSingle mInstance;
    /**
     * activeDate : 2016-04-08 00:00:00
     * bankCardId : 6217000060024977405
     * custId : 420503198208291814
     * custName : 阳星
     * endDate : 2016-12-06 00:00:00
     * guideNum : 1
     * id : 17
     * leadNum : 0
     * opencard : 2015-12-30 00:00:00
     * payFlag : 1
     * pfId : 355
     * phone : 13902088207
     * remindNum : 1
     * sex : 0
     * uid : 4461524
     * uname : 13902088207
     */

    private String activeDate;
    private String bankCardId;
    private String custId;
    private String custName;
    private String endDate;
    private int guideNum;
    private int id;
    private int leadNum;
    private String opencard;
    private int payFlag;
    private int pfId;
    private String phone;
    private int remindNum;
    private int sex;
    private int uid;
    private String uname;

    private boolean isOpenDragon = false;//是否开通龙卡

    public boolean isOpenDragon() {
        return isOpenDragon;
    }

    public void setOpenDragon(boolean openDragon) {
        isOpenDragon = openDragon;
    }

    private DragonStatusSingle() {
    }

    public static synchronized DragonStatusSingle getInstance(){
        if (mInstance == null){
            mInstance = new DragonStatusSingle();
        }
        return mInstance;
    }


    public String getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(String activeDate) {
        this.activeDate = activeDate;
    }

    public String getBankCardId() {
        return bankCardId;
    }

    public void setBankCardId(String bankCardId) {
        this.bankCardId = bankCardId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getGuideNum() {
        return guideNum;
    }

    public void setGuideNum(int guideNum) {
        this.guideNum = guideNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLeadNum() {
        return leadNum;
    }

    public void setLeadNum(int leadNum) {
        this.leadNum = leadNum;
    }

    public String getOpencard() {
        return opencard;
    }

    public void setOpencard(String opencard) {
        this.opencard = opencard;
    }

    public int getPayFlag() {
        return payFlag;
    }

    public void setPayFlag(int payFlag) {
        this.payFlag = payFlag;
    }

    public int getPfId() {
        return pfId;
    }

    public void setPfId(int pfId) {
        this.pfId = pfId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRemindNum() {
        return remindNum;
    }

    public void setRemindNum(int remindNum) {
        this.remindNum = remindNum;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}
