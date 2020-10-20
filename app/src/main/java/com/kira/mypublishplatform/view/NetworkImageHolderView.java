package com.kira.mypublishplatform.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kira.mypublishplatform.R;

/**
 * 广告栏网络图片加载
 */
public class NetworkImageHolderView extends Holder<String> {
    private ImageView imageView;
    private Context mContext;

    public NetworkImageHolderView(View itemView, Context context) {
        super(itemView);
        mContext = context;
    }

    @Override
    protected void initView(View itemView) {
        imageView = itemView.findViewById(R.id.image_net);
    }

    @Override
    public void updateUI(String data) {
        Glide.with(mContext).load(data).apply(new RequestOptions().placeholder(R.mipmap.no_pic).error(R.mipmap.no_resource)).into(imageView);
    }

}
