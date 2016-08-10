package com.eztcn.user.hall.model;

/**
 * @Author: lizhipeng
 * @Data: 16/6/13 下午7:42
 * @Description: 我关注的热门医生
 */
public class MyHotDoctorRequest implements IModel {

    private int page;
    private int rowsPerPage;
    private String userId;
    private String ecType;

    public String getEcType() {
        return ecType;
    }

    public void setEcType(String ecType) {
        this.ecType = ecType;
    }
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "MyHotDoctorRequest{" +
                "page=" + page +
                ", rowsPerPage=" + rowsPerPage +
                ", userId='" + userId + '\'' +
                '}';
    }
}
