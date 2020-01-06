package com.kira.mypublishplatform.bean;

public class RegisterBean {


    /**
     * code : string
     * cryCode : string
     * password : string
     * phone : string
     * type : string
     * username : string
     */

    private String code;
    private String cryCode;
    private String loginAccount;
    private String password;
    private String phone;
    private String type;
    private String username;
    private String msgId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCryCode() {
        return cryCode;
    }

    public void setCryCode(String cryCode) {
        this.cryCode = cryCode;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
