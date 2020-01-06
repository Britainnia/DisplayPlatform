package com.kira.mypublishplatform.adapter

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
import com.kira.mypublishplatform.model.MultipleItem
import com.kira.mypublishplatform.model.SubModel
import com.kira.mypublishplatform.ui.web.HandleWebActivity
import com.kira.mypublishplatform.ui.web.WebActivity
import com.kira.mypublishplatform.utils.FastClickUtils.Companion.isValidClick
import java.util.*

class TabRecyclerAdapter(
    data: List<MultipleItem?>
) : BaseMultiItemQuickAdapter<MultipleItem?, BaseViewHolder>(data) {
    private var dSize: Int
    private var parentHeight = 0
    private var itemHeight = 0
    private val subs =
        arrayOf("余额宝", "花呗", "芝麻信用", "借呗", "蚂蚁保险")
    private val icons = intArrayOf(
        R.mipmap.icon_one,
        R.mipmap.icon_two,
        R.mipmap.icon_three,
        R.mipmap.icon_four,
        R.mipmap.icon_five
    )
    private val modelList: MutableList<SubModel?> = ArrayList()

//    private fun initList() {
//        modelList.clear()
//        for (a in subs.indices) {
//            val model = SubModel()
//            model.name = subs[a]
//            model.icon = icons[a]
//            model.href = "http://192.168.1.229:9900/zhyl/yygl/mh/$a"
//            modelList.add(model)
//        }
//    }

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
            //            return new BaseViewHolder(getItemView(mResId, parent));
        }
    }

    override fun convert(helper: BaseViewHolder, item: MultipleItem?) {
        if (helper.itemViewType == MultipleItem.ITEM) {
            helper.setText(R.id.title, item?.type)
            val mGrid = helper.getView<RecyclerView>(R.id.recycler_view)
            mGrid.layoutManager = GridLayoutManager(mContext, 3)
            val mAdapter = SubAdapter(R.layout.item_sub, item?.items!!)
            mGrid.adapter = mAdapter
            mAdapter.setOnItemClickListener { _: BaseQuickAdapter<*, *>?, _: View?, poi: Int ->
                if (!isValidClick) {
                    val intent = Intent()
                    intent.setClass(mContext, HandleWebActivity::class.java)
                        .putExtra("title", item.items[poi].name)
                        .putExtra("url", item.items[poi].href)
                    mContext.startActivity(intent)
                }
            }
        }
    }

    companion object {
        private const val mResId = R.layout.item_layout
        private const val mEmpty = R.layout.fragment_home
    }

    init {
        addItemType(MultipleItem.FOOTER, mEmpty)
        addItemType(MultipleItem.ITEM, mResId)
        dSize = data.size
//        initList()
    }

}