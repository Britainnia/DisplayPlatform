package com.kira.mypublishplatform.adapter

import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.activity.LoginActivity
import com.kira.mypublishplatform.base.MyApplication
import com.kira.mypublishplatform.model.MultipleItem
import com.kira.mypublishplatform.ui.web.HandleWebActivity
import com.kira.mypublishplatform.utils.ConstUtils
import com.kira.mypublishplatform.utils.FastClickUtils.Companion.isValidClick
import com.kira.mypublishplatform.view.TitleDialog

class TabRecyclerAdapter(
    data: List<MultipleItem?>
) : BaseMultiItemQuickAdapter<MultipleItem?, BaseViewHolder>(data) {
    private var dSize: Int = data.size
    private var parentHeight = 0
    private var itemHeight = 0

    override fun getItemCount(): Int {
        return dSize + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == dSize) {
            MultipleItem.FOOTER
        } else {
            MultipleItem.ITEM
        }
    }

    public override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (viewType == MultipleItem.FOOTER) { //Footer是最后留白的位置，以便最后一个item能够出发tab的切换
            val view = View(parent.context)
            view.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                parentHeight - itemHeight
            )
            BaseViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(mResId, parent, false)
            view.post {
                parentHeight = parent.height
                itemHeight = view.height
            }
            BaseViewHolder(view)
        }
    }

    override fun convert(helper: BaseViewHolder, item: MultipleItem?) {
        if (helper.itemViewType == MultipleItem.ITEM) {
            helper.setText(R.id.title, item?.type)
            val mGrid = helper.getView<RecyclerView>(R.id.recycler_view)
            mGrid.layoutManager = GridLayoutManager(mContext, 4)
            val mAdapter = SubAdapter(item?.items!!)
            mGrid.adapter = mAdapter

            mAdapter.setOnItemClickListener { _: BaseQuickAdapter<*, *>?, _: View?, poi: Int ->
                if (!isValidClick) {

                    val needLog = if (item.items[poi].needLogin == null) {false}
                    else {item.items[poi].needLogin!! }

                    if (needLog && !MyApplication.mSp.getBoolean(ConstUtils.LOGIN_STATE)) {
                        val builder = TitleDialog.Builder(mContext)
                        builder.setTitle("登录账号").setMessage("访问该应用需要登录您的账号")
                            .setPositiveButton(
                                "前往登录"
                            ) { dialog: DialogInterface, _: Int ->
                                mContext.startActivity(
                                    Intent(
                                        mContext,
                                        LoginActivity::class.java
                                    )
                                )
                                dialog.dismiss()
                            }
                            .setNegativeButton(
                                "看看别的"
                            ) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
                            .create().show()
                        return@setOnItemClickListener
                    } else {
                        val intent = Intent()
                        intent.setClass(mContext, HandleWebActivity::class.java)
                            .putExtra("title", item.items[poi].name)
                            .putExtra("url", item.items[poi].href)
                        mContext.startActivity(intent)
                    }
                }
            }
        }
    }

    companion object {
        private const val mResId = R.layout.item_layout
        private const val mEmpty = R.layout.fragment_home
    }

}