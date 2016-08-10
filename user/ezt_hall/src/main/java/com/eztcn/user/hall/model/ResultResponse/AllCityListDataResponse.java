package com.eztcn.user.hall.model.ResultResponse;

import com.eztcn.user.hall.model.City;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 蒙 on 2016/7/19.
 */
public class AllCityListDataResponse implements Serializable{
    private ArrayList<City> allCity;
    private ArrayList<City> hotCity;

    public ArrayList<City> getAllCity() {
        return allCity;
    }

    public void setAllCity(ArrayList<City> allCity) {
        this.allCity = allCity;
    }

    public ArrayList<City> getHotCity() {
        return hotCity;
    }

    public void setHotCity(ArrayList<City> hotCity) {
        this.hotCity = hotCity;
    }
}
