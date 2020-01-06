package com.kira.mypublishplatform.bean;

import java.io.Serializable;
import java.util.List;

public class BaseListBean<T> implements Serializable {

    private int total;
    private List<T> rows;

    public BaseListBean(){}

    public BaseListBean(int count, List<T> data) {
        super();
        this.total = count;
        this.rows = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

}
