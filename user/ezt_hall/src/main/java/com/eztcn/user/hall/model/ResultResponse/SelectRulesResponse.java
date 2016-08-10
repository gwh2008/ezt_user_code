package com.eztcn.user.hall.model.ResultResponse;

/**
 * Created by 蒙 on 2016/6/17.
 */
public class SelectRulesResponse {

    private String ehLevel1;
    private String ehLevel2;
    private String ehLevel3;
    private String numType01;
    private String numType02;

    public String getEhLevel1() {
        return ehLevel1;
    }

    public void setEhLevel1(String ehLevel1) {
        this.ehLevel1 = ehLevel1;
    }

    public String getEhLevel2() {
        return ehLevel2;
    }

    public void setEhLevel2(String ehLevel2) {
        this.ehLevel2 = ehLevel2;
    }

    public String getEhLevel3() {
        return ehLevel3;
    }

    public void setEhLevel3(String ehLevel3) {
        this.ehLevel3 = ehLevel3;
    }

    public String getNumType01() {
        return numType01;
    }

    public void setNumType01(String numType01) {
        this.numType01 = numType01;
    }

    public String getNumType02() {
        return numType02;
    }

    public void setNumType02(String numType02) {
        this.numType02 = numType02;
    }

    @Override
    public String toString() {
        return "SelectRulesResponse{" +
                "ehLevel1='" + ehLevel1 + '\'' +
                ", ehLevel2='" + ehLevel2 + '\'' +
                ", ehLevel3='" + ehLevel3 + '\'' +
                ", numType01='" + numType01 + '\'' +
                ", numType02='" + numType02 + '\'' +
                '}';
    }
}
