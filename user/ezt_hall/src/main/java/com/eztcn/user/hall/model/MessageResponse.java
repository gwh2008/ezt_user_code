package com.eztcn.user.hall.model;

import java.util.List;

/**
 * @Author: lizhipeng
 * @Data: 16/6/29 下午2:32
 * @Description: 首页消息fragment响应
 */
public class MessageResponse {


    /**
     * combo : {}
     * footer : []
     * outher : {}
     * page : 1
     * rows : [{"content":"您在就诊时间内未就诊，已造成爽约。当月爽约3次将无法使用医指通平台的预约挂号服务","createtime":"2016-06-28 20:38:47","id":36,"infoSysType":4,"orderNumber":null,"title":"预约挂号","type":16,"userId":4416513}]
     * rowsPerPage : 15
     * total : 0
     */

    private int page;
    private int rowsPerPage;
    private int total;
    /**
     * content : 您在就诊时间内未就诊，已造成爽约。当月爽约3次将无法使用医指通平台的预约挂号服务
     * createtime : 2016-06-28 20:38:47
     * id : 36
     * infoSysType : 4
     * orderNumber : null
     * title : 预约挂号
     * type : 16
     * userId : 4416513
     */

    private List<RowsBean> rows;

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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        private String content;
        private String createtime;
        private int id;
        private int infoSysType;
        private Object orderNumber;
        private String title;
        private int type;
        private int userId;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getInfoSysType() {
            return infoSysType;
        }

        public void setInfoSysType(int infoSysType) {
            this.infoSysType = infoSysType;
        }

        public Object getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(Object orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
