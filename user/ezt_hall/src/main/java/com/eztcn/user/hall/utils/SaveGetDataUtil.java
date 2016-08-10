package com.eztcn.user.hall.utils;

import android.content.Context;

import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.hall.model.City;
import com.eztcn.user.hall.model.ResultResponse.AllCityListDataResponse;
import com.eztcn.user.hall.model.ResultResponse.CityListDataResponse;
import com.eztcn.user.hall.model.ResultResponse.HospitalListResponse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class SaveGetDataUtil {

    /**
     * 预约挂号模块需要用的到的合作的城市列表数据在本地保存的文件的名字
     */
    private final static String cityListMap_fileName = "cityListMap";
    /**
     * 全部的城市列表数据在本地保存的文件的名字
     */
    private final static String cityAllDataList_fileName = "cityAllDataList";

    public static void setCityListData(ArrayList<CityListDataResponse> cityList) {
        fileSave(cityList, cityListMap_fileName);
    }

    public static ArrayList<CityListDataResponse> getCityListData() {
        return (ArrayList<CityListDataResponse>) readObject(cityListMap_fileName);
    }

    public static void setAllCityListData(AllCityListDataResponse cityList) {
        fileSave(cityList, cityAllDataList_fileName);
    }

    public static AllCityListDataResponse getAllCityListData() {
        return (AllCityListDataResponse) readObject(cityAllDataList_fileName);
    }


    /**
     * 医院搜索页面用到的本地保存的医院搜索历史列表数据的文件的名字
     */
    private final static String hospitalHistoryListData_fileName = "hospitalHistoryListData";

    public static void setHospitalHistoryListData(ArrayList<HospitalListResponse> hospitalList) {
        fileSave(hospitalList, hospitalHistoryListData_fileName);
    }

    public static ArrayList<HospitalListResponse> getHospitalHistoryListData() {
        return (ArrayList<HospitalListResponse>) readObject(hospitalHistoryListData_fileName);
    }


    public static void fileSave(Object object, String fileName) {
        // 保存在本地
        try {
            // 通过openFileOutput方法得到一个输出流，方法参数为创建的文件名（不能有斜杠），操作模式
            FileOutputStream fos = BaseApplication.getInstance().openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);// 写入
            fos.close(); // 关闭输出流
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object readObject(String fileName) {
        Object object = null;
        try {
            FileInputStream fis = BaseApplication.getInstance().openFileInput(fileName); // 获得输入流
            // 获得输入流
            ObjectInputStream ois = new ObjectInputStream(fis);
            object = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
