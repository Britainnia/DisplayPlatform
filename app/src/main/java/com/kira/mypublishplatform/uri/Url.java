package com.kira.mypublishplatform.uri;

import rxhttp.wrapper.annotation.DefaultDomain;
import rxhttp.wrapper.annotation.Domain;

public class Url {
    @DefaultDomain() //设置为默认域名
    public static String baseUrl = "http://120.199.82.52:5000/";
    @Domain(name = "BaseUrlTest") //非默认域名，并取别名为BaseUrlTest
    public static String testUrl = "http://192.168.1.107:9900/";
}
