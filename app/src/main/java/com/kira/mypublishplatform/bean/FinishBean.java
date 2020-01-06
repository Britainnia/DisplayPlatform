package com.kira.mypublishplatform.bean;

public class FinishBean {

    /**
     * contactName : string
     * orderNo : string
     * orderStatus : string
     * pageNum : 0
     * pageSize : 0
     * serviceId : 0
     * serviceStartTime1 : 2019-09-17T01:23:30.008Z
     * serviceStartTime2 : 2019-09-17T01:23:30.008Z
     */

    private String contactName;
    private String orderNo;
    private String orderStatus;
    private Integer pageNum;
    private Integer pageSize;
    private Integer serviceId;
    private String serviceStartTime1;
    private String serviceStartTime2;

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceStartTime1() {
        return serviceStartTime1;
    }

    public void setServiceStartTime1(String serviceStartTime1) {
        this.serviceStartTime1 = serviceStartTime1;
    }

    public String getServiceStartTime2() {
        return serviceStartTime2;
    }

    public void setServiceStartTime2(String serviceStartTime2) {
        this.serviceStartTime2 = serviceStartTime2;
    }
}

