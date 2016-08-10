package com.eztcn.user.hall.model;

/**
 * @Author: lizhipeng
 * @Data: 16/6/8 下午6:04
 * @Description:
 */
public class OneThousandRequest {

    /**
     * userID : 0
     * pageNumber : 1
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String userID;
        private String pageNumber;

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public String getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(String pageNumber) {
            this.pageNumber = pageNumber;
        }
    }
}
