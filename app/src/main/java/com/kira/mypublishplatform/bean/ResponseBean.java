package com.kira.mypublishplatform.bean;

import java.io.Serializable;

/**
 * @program: 测试
 * @description:返回的JSON数据结构标准
 * @author:
 * @create: 2018-10-17 09:01
 **/
public class ResponseBean <T> implements Serializable {
    private boolean status;
    private T data;
    private String code;
    private String msg;

    public ResponseBean(){}

    public ResponseBean(boolean status, T data) {
        super();
        this.status = status;
        this.data = data;
    }

    public ResponseBean(boolean status, T data, String code, String msg) {
        super();
        this.status = status;
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public ResponseBean(boolean status, String code, String msg) {
        this.status = status;
        this.code = code;
        this.msg = msg;
    }

    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

}

