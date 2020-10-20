package com.kira.mypublishplatform.ui.service

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.activity.HeadActivity
import com.kira.mypublishplatform.adapter.ScrollLeftAdapter
import com.kira.mypublishplatform.adapter.ScrollRightAdapter
import com.kira.mypublishplatform.base.LazyLoad1Fragment
import com.kira.mypublishplatform.bean.ScrollBean
import com.kira.mypublishplatform.bean.ScrollBean.ScrollItemBean
import com.kira.mypublishplatform.databinding.FragmentServiceBinding
import com.kira.mypublishplatform.model.OldInfoModel
import java.util.*

class ServiceFragment : LazyLoad1Fragment<FragmentServiceBinding>() {
    private val oldInfoModel = OldInfoModel()

    private var left: MutableList<String> = arrayListOf()
    private var right: MutableList<ScrollBean> = arrayListOf()
    private var leftAdapter: ScrollLeftAdapter? = null
    private var rightAdapter: ScrollRightAdapter? = null

    //右侧title在数据中所对应的position集合
    private val tPosition: MutableList<Int> = ArrayList()
//    private var mContext: Context? = null

    //title的高度
    private var tHeight = 0

    //记录右侧当前可见的第一个item的position
    private var first = 0
    private var rightManager: GridLayoutManager? = null

    protected var mActivity: HeadActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as HeadActivity
    }

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }

    override fun initData(savedInstanceState: Bundle?) {

        initServiceData()

        initLeft()
        initRight()

    }

    override fun setListener() {

        binding.recRight.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //获取右侧title的高度
                tHeight = binding.rightTitle.height
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                //判断如果是header
                if (right[first].isHeader) {
                    //获取此组名item的view
                    val view = rightManager!!.findViewByPosition(first)
                    if (view != null) {
                        //如果此组名item顶部和父容器顶部距离大于等于title的高度,则设置偏移量
                        if (view.top >= tHeight) {
                            binding.rightTitle.y = view.top - tHeight.toFloat()
                        } else {
                            //否则不设置
                            binding.rightTitle.y = 0f
                        }
                    }
                }

                //因为每次滑动之后,右侧列表中可见的第一个item的position肯定会改变,并且右侧列表中可见的第一个item的position变换了之后,
                //才有可能改变右侧title的值,所以这个方法内的逻辑在右侧可见的第一个item的position改变之后一定会执行
                val firstPosition = rightManager!!.findFirstVisibleItemPosition()
                if (first != firstPosition && firstPosition >= 0) {
                    //给first赋值
                    first = firstPosition
                    //不设置Y轴的偏移量
                    binding.rightTitle.y = 0f

                    //判断如果右侧可见的第一个item是否是header,设置相应的值
                    if (right[first].isHeader) {
                        binding.rightTitle.text = right[first].header
                    } else {
                        binding.rightTitle.text = right[first].t!!.parent
                    }
                }

                //遍历左边列表,列表对应的内容等于右边的title,则设置左侧对应item高亮
                for (i in left.indices) {
                    if (left[i] == binding.rightTitle.text.toString()) {
                        leftAdapter!!.selectItem(i)
                    }
                }

                //如果右边最后一个完全显示的item的position,等于bean中最后一条数据的position(也就是右侧列表拉到底了),
                //则设置左侧列表最后一条item高亮
                if (rightManager!!.findLastCompletelyVisibleItemPosition() == right.size - 1) {
                    leftAdapter!!.selectItem(left.size - 1)
                }
            }
        })

        leftAdapter!!.setOnItemChildClickListener { _: BaseQuickAdapter<*, *>?, view: View, position: Int ->
            when (view.id) {
                R.id.item -> {
                    leftAdapter!!.selectItem(position)
                    rightManager!!.scrollToPositionWithOffset(tPosition[position], 0)
                }
            }
        }

    }

    override fun loadData() {
//        getInfo()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
//            getInfo()
        }
    }

    private fun initRight() {
        rightManager = GridLayoutManager(mActivity, 2)
        if (rightAdapter == null) {
            rightAdapter = ScrollRightAdapter(right)
            binding.recRight.layoutManager = rightManager
            binding.recRight.addItemDecoration(object : ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    outRect[dpToPx(mActivity!!, getDimens(mActivity!!, R.dimen.dp_3))
                            , 0
                            , dpToPx(mActivity!!, getDimens(mActivity!!, R.dimen.dp_3)
                    )] = dpToPx(
                        mActivity!!, getDimens(mActivity!!, R.dimen.dp_3)
                    )
                }
            })
            binding.recRight.adapter = rightAdapter
        } else {
            rightAdapter!!.notifyDataSetChanged()
        }
        rightAdapter!!.setNewData(right)

        //设置右侧初始title
        if (right[first].isHeader) {
            binding.rightTitle.text = right[first].header
//            binding.rightTitle.setBackgroundColor(ContextCompat.getColor(mActivity!!.baseContext, R.color.bs_blue))
        }

    }

    private fun initLeft() {
        if (leftAdapter == null) {
            leftAdapter = ScrollLeftAdapter(left)
            binding.recLeft.layoutManager = LinearLayoutManager(
                mActivity!!,
                LinearLayoutManager.VERTICAL,
                false
            )
            binding.recLeft.addItemDecoration(
                DividerItemDecoration(
                    mActivity!!,
                    DividerItemDecoration.VERTICAL
                )
            )
            binding.recLeft.adapter = leftAdapter
        } else {
            leftAdapter!!.notifyDataSetChanged()
        }
        leftAdapter!!.setNewData(left as List<String?>?)

    }

    //获取数据(若请求服务端数据,请求到的列表需有序排列)
    private fun initServiceData() {
        left = mutableListOf()
        left.add("公司产品")
        left.add("政务大厅")
        left.add("便民服务")
        left.add("交通出行")
        left.add("居民生活")
        right = mutableListOf()
        right.add(ScrollBean(true, left[0]))
        right.add(
            ScrollBean(
                ScrollItemBean(
                    "https://www.gbicc.net/banner/54eff0d3-d89a-4979-854d-1e1d9886bd00.jpg",
                    "养老服务",
                    left[0]
                )
            )
        )
        right.add(
            ScrollBean(
                ScrollItemBean(
                    "https://www.gbicc.net/banner/32c00879-7353-4c6f-895d-bb8848cac1f5.jpg",
                    "金融超市",
                    left[0]
                )
            )
        )
        right.add(ScrollBean(true, left[1]))
        right.add(
            ScrollBean(
                ScrollItemBean(
                    "file:///android_asset/community.png",
                    "社区服务",
                    left[1]
                )
            )
        )
        right.add(
            ScrollBean(
                ScrollItemBean(
                    "file:///android_asset/government_file.png",
                    "政府文件",
                    left[1]
                )
            )
        )
        right.add(
            ScrollBean(
                ScrollItemBean(
                    "file:///android_asset/government_affair.png",
                    "政务",
                    left[1]
                )
            )
        )
        right.add(ScrollBean(true, left[2]))
        right.add(
            ScrollBean(
                ScrollItemBean(
                    "file:///android_asset/garbage_classification.png",
                    "垃圾分类",
                    left[2]
                )
            )
        )
        right.add(
            ScrollBean(
                ScrollItemBean(
                    "file:///android_asset/donation.png",
                    "爱心捐助",
                    left[2]
                )
            )
        )
        right.add(
            ScrollBean(
                ScrollItemBean(
                    "file:///android_asset/recycle.png",
                    "环保",
                    left[2]
                )
            )
        )
        right.add(
            ScrollBean(
                ScrollItemBean(
                    "file:///android_asset/onestop_service.png",
                    "一站式服务",
                    left[2]
                )
            )
        )
        right.add(
            ScrollBean(
                ScrollItemBean(
                    "file:///android_asset/school.png",
                    "附近学校",
                    left[2]
                )
            )
        )
        right.add(
            ScrollBean(
                ScrollItemBean(
                    "file:///android_asset/gas.png",
                    "附近加油站",
                    left[2]
                )
            )
        )
        right.add(
            ScrollBean(
                ScrollItemBean(
                    "file:///android_asset/bank.png",
                    "附近银行",
                    left[2]
                )
            )
        )
        right.add(ScrollBean(true, left[3]))
        right.add(
            ScrollBean(
                ScrollItemBean(
                    "file:///android_asset/bus.png",
                    "公交",
                    left[3]
                )
            )
        )
        right.add(
            ScrollBean(
                ScrollItemBean(
                    "file:///android_asset/underground.png",
                    "地铁",
                    left[3]
                )
            )
        )
        right.add(
            ScrollBean(
                ScrollItemBean(
                    "file:///android_asset/travel.png",
                    "旅游出行",
                    left[3]
                )
            )
        )
        right.add(
            ScrollBean(
                ScrollItemBean(
                    "file:///android_asset/entry_exit.png",
                    "出入境",
                    left[3]
                )
            )
        )
        right.add(ScrollBean(true, left[4]))
        right.add(
            ScrollBean(
                ScrollItemBean(
                    "file:///android_asset/air_quality.png",
                    "空气质量",
                    left[4]
                )
            )
        )
        right.add(
            ScrollBean(
                ScrollItemBean(
                    "file:///android_asset/diet.png",
                    "饮食",
                    left[4]
                )
            )
        )
        right.add(
            ScrollBean(
                ScrollItemBean(
                    "file:///android_asset/healthcare.png",
                    "医疗保健",
                    left[4]
                )
            )
        )
        right.add(
            ScrollBean(
                ScrollItemBean(
                    "file:///android_asset/forecast.png",
                    "天气预报",
                    left[4]
                )
            )
        )
        right.add(
            ScrollBean(
                ScrollItemBean(
                    "file:///android_asset/garbage_classification.png",
                    "垃圾回收",
                    left[4]
                )
            )
        )
        right.add(
            ScrollBean(
                ScrollItemBean(
                    "file:///android_asset/entertainment.png",
                    "娱乐",
                    left[4]
                )
            )
        )
        right.add(
            ScrollBean(
                ScrollItemBean(
                    "file:///android_asset/investigation.png",
                    "问卷调查",
                    left[4]
                )
            )
        )
        right.add(
            ScrollBean(
                ScrollItemBean(
                    "file:///android_asset/maintain.png",
                    "维修服务",
                    left[4]
                )
            )
        )
        right.add(
            ScrollBean(
                ScrollItemBean(
                    "file:///android_asset/network.png",
                    "网络设备",
                    left[4]
                )
            )
        )
        right.add(
            ScrollBean(
                ScrollItemBean(
                    "file:///android_asset/map.png",
                    "地图",
                    left[4]
                )
            )
        )
        right.add(
            ScrollBean(
                ScrollItemBean(
                    "file:///android_asset/safe_box.png",
                    "系统安全箱",
                    left[4]
                )
            )
        )
        for (i in right.indices) {
            if (right[i].isHeader) {
                //遍历右侧列表,判断如果是header,则将此header在右侧列表中所在的position添加到集合中
                tPosition.add(i)
            }
        }
    }

    /**
     * 获得资源 dimens (dp)
     *
     * @param context
     * @param id      资源id
     * @return
     */
    fun getDimens(context: Context, id: Int): Float {
        val dm = context.resources.displayMetrics
        val px = context.resources.getDimension(id)
        return px / dm.density
    }

    /**
     * dp转px
     *
     * @param context
     * @param dp
     * @return
     */
    fun dpToPx(context: Context, dp: Float): Int {
        val displayMetrics = context.resources.displayMetrics
        return (dp * displayMetrics.density + 0.5f).toInt()
    }

    companion object {
        fun newInstance(): ServiceFragment {
            return ServiceFragment()
        }
    }
}