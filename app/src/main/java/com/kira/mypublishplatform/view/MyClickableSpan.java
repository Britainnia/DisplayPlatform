package com.kira.mypublishplatform.view;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.kira.mypublishplatform.R;
import com.kira.mypublishplatform.base.MyApplication;
import com.kira.mypublishplatform.utils.ToastUtils;

public class MyClickableSpan extends ClickableSpan {

    private String content;

    public MyClickableSpan(String content) {
        this.content = content;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(false);
        ds.setColor(Color.parseColor("#FF0000"));
    }

    @Override
    public void onClick(View widget) {
        TitleDialog.Builder builder = new TitleDialog.Builder(MyApplication.mContext);
        builder.setTitle(R.string.submit_order).setMessage("您是否确认提交本订单?")
                .setPositiveButton("确认", (dialog, i) -> {
                        ToastUtils.showShort(MyApplication.mContext, "我按了确认");
                    dialog.dismiss();
                })
                .setNegativeButton("取消", (dialog, i) -> dialog.dismiss())
                .create().show();
    }

}
