package com.eztcn.user.hall.model.ResultResponse;

import com.eztcn.user.hall.model.bean.MyAttention;
import java.util.List;

/**
 * Created by lx on 2016/6/16.
 * 我的关注的医生的相应的数据体
 */
public class MyAttentionResponse {

    private Object combo;
    private List<Object> footer;
    private Object outher;
    private int page;
    private int rowsPerPage;
    private int total;
    private List<MyAttention> rows;

    public Object getCombo() {
        return combo;
    }

    public void setCombo(Object combo) {
        this.combo = combo;
    }

    public List<Object> getFooter() {
        return footer;
    }

    public void setFooter(List<Object> footer) {
        this.footer = footer;
    }

    public Object getOuther() {
        return outher;
    }

    public void setOuther(Object outher) {
        this.outher = outher;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<MyAttention> getRows() {
        return rows;
    }

    public void setRows(List<MyAttention> rows) {
        this.rows = rows;
    }
}
