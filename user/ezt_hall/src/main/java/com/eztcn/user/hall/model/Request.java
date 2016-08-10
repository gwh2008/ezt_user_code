package com.eztcn.user.hall.model;

import com.eztcn.user.hall.utils.CheckSignUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lizhipeng
 * @Data: 16/5/24 上午10:13
 * @Description:
 */
public class Request implements IModel {
    protected String appId;
    protected String timestamp;
    protected String sign;
    protected Map<String, String> map = new HashMap<>();
    protected CheckSignUtils jni = new CheckSignUtils();

    public String getAppId() {
        appId = jni.getAppId();
//        appId = "09721ab88e0a552087391be1ef0c6826";
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTimestamp() {
        timestamp = System.currentTimeMillis() / 1000 + "";
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    /**
     * 获取签名值
     *
     * @param params 请求参数
     * @return
     */
    protected String getSign(Map<String, String> params) {
        map.put("appId", getAppId());
        map.put("timestamp", getTimestamp());
        map.putAll(params);
        String[] keys = map.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        StringBuilder query = new StringBuilder();
        for (String key : keys) {
            if (!"signature".equals(key)) {
                String value = map.get(key);
                query.append(key);
                query.append(value);
            }
        }
        return query.toString();
    }

    public Map<String, String> getFormMap(Map<String, String> params) {
        String sign = jni.MD5(getSign(params));
        setSign(sign);
        map.put("signature", sign);
        return map;
    }

}
