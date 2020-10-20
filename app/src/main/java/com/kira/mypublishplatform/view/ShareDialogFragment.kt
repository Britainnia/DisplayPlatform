package com.kira.mypublishplatform.view

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.model.ShareModel
import java.util.*

class ShareDialogFragment(list: MutableList<ShareModel>?, private val mContext: Context) :
    DialogFragment() {
    private var mList: MutableList<ShareModel>? = null
    private var mCallback: Callback? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 去掉默认的标题
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.fragment_share, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var shareAdapter: BaseQuickAdapter<*, *>
        val mRecyclerView: RecyclerView = view.findViewById(R.id.rv_share)
        //        RecyclerView nRecyclerView = view.findViewById(R.id.rv_right);
        mRecyclerView.layoutManager = GridLayoutManager(mContext, 3)
        //        nRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.adapter =
            object : BaseQuickAdapter<ShareModel, BaseViewHolder>(R.layout.item_sub, mList) {
                override fun convert(helper: BaseViewHolder, item: ShareModel) {
                    helper.setText(R.id.grid_item_name, item.name)
                    val options = RequestOptions()
                        .placeholder(R.mipmap.no_pic) //占位图
                        .error(R.mipmap.no_resource) //异常占位图
                    //            .override(200, 100)
//            .diskCacheStrategy(DiskCacheStrategy.NONE)//不进行磁盘缓存
                    Glide.with(mContext)
                        .load(item.icon)
                        .apply(options)
                        .into(helper.getView(R.id.grid_item_image))
                }
            }.also { shareAdapter = it }

        shareAdapter.onItemClickListener =
            BaseQuickAdapter.OnItemClickListener { _: BaseQuickAdapter<*, *>?, _: View?, position: Int ->
                if (mCallback != null) {
                    mCallback!!.onItemClick(position)
                }
            }
    }

    override fun onStart() {
        super.onStart()
        // 下面这些设置必须在此方法(onStart())中才有效
        val window = dialog!!.window
        // 如果不设置这句代码, 那么弹框就会与四边都有一定的距离
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent)
            // 设置动画
            window.setWindowAnimations(R.style.bottomDialog)
            val params = window.attributes
            params.gravity = Gravity.BOTTOM
            // 如果不设置宽度,那么即使你在布局中设置宽度为 match_parent 也不会起作用
            params.width = resources.displayMetrics.widthPixels
            window.attributes = params
        }
    }

    interface Callback {
        fun onItemClick(choice: Int)
    }

    fun setCallback(callback: Callback?) {
        mCallback = callback
    }

    init {
        mList = list ?: mutableListOf()
    }
}