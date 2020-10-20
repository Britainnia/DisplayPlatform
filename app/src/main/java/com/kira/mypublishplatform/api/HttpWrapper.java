package com.kira.mypublishplatform.api;

import com.kira.mypublishplatform.bean.ResponseBean;

import io.reactivex.Observable;
import rxhttp.wrapper.param.RxHttp;

public class HttpWrapper {

    public static Observable<ResponseBean> getKey() {
        return RxHttp.postJson("disapp/login/getpk")
//                .setDomainToBaseUrlTestIfAbsent() //指定使用测试域名
                .asResponse(ResponseBean.class);

    }

    public static Observable<ResponseBean> clientLogin(String account, String password) {
        return RxHttp.postJson("disapp/login/cklogin")
                .setDomainToBaseUrlTestIfAbsent() //指定使用测试域名
                .add("loginAccount", account)
                .add("loginPass", password)
                .asResponse(ResponseBean.class);
    }

}
