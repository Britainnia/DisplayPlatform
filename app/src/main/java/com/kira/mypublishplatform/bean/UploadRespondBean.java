package com.kira.mypublishplatform.bean;

public class UploadRespondBean {

    /**
     * result : true
     * path : http://192.168.1.229/newOld/provider/20190925/image/20190925151528_3294.jpg
     */

    private boolean result;
    private String path;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
