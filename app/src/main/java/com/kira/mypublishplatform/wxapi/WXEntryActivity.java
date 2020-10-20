package com.kira.mypublishplatform.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;


import com.kira.mypublishplatform.base.MyApplication;
import com.kira.mypublishplatform.utils.Logger;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        IWXAPI api = WXAPIFactory.createWXAPI(this, MyApplication.WeChatId, false);
        api.handleIntent(getIntent(), this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResp(BaseResp resp) {
        String message = "分享回调";

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                message = "分享成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                message = "分享取消";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                message = "分享拒绝";
                break;
        }
        Logger.i(message + " errCode = " + resp.errCode);
//        Toast.makeText(this, message + " errCode = " + resp.errCode, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onReq(BaseReq arg0) {

    }
}
