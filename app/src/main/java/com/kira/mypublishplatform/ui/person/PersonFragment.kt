package com.kira.mypublishplatform.ui.person

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.activity.HeadActivity
import com.kira.mypublishplatform.base.LazyLoad1Fragment
import com.kira.mypublishplatform.base.MyApplication
import com.kira.mypublishplatform.databinding.FragmentPersonBinding
import com.kira.mypublishplatform.model.OldInfoModel
import com.kira.mypublishplatform.utils.ConstUtils
import com.kira.mypublishplatform.utils.DownloadUtils
import com.kira.mypublishplatform.utils.FastClickUtils.Companion.isValidClick
import com.kira.mypublishplatform.utils.Logger
import com.kira.mypublishplatform.utils.StatusBarUtils
import com.kira.mypublishplatform.view.TitleDialog

class PersonFragment : LazyLoad1Fragment<FragmentPersonBinding>() {
    private val oldInfoModel = OldInfoModel()
//    private var mBinding: PersonFragmentBinding? = null
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
        oldInfoModel.cdNames = MyApplication.mSp.getStr(ConstUtils.NAME)
        oldInfoModel.oldName = MyApplication.mSp.getStr(ConstUtils.USER_NAME)
        oldInfoModel.accountBalance = "0.0"
        oldInfoModel.socialNo = versionName
        binding.model = oldInfoModel

    }

    override fun setListener() {
        binding.detail.setOnClickListener {
            if (!isValidClick) { //                if (oldInfoModel != null) {
                val intent = Intent(
                    mActivity,
                    PersonDataActivity::class.java
                )
                intent.putExtra(
                    "model",
                    oldInfoModel
                )
//                    .putExtra(
//                        "url",
//                        "http://www.zjzwfw.gov.cn/zjservice/item/detail/index.do?impleCode=ff8080815df81d19015df864b93500694330800761000&webId=24"
//                    )
                startActivityForResult(
                    intent,
                    500
                )
            }
        }

        binding.message.setOnClickListener {
            if (!isValidClick) {
                startActivity(
                    Intent(
                        mActivity,
                        MessageActivity::class.java
                    )
                )
            }
        }

        binding.feedback.setOnClickListener {
            if (!isValidClick) {
                startActivity(
                    Intent(
                        mActivity,
                        FeedBackActivity::class.java
                    )
                )
            }
        }

        binding.version.setOnClickListener {
            if (!isValidClick) {
                versionName
            }
        }

        binding.about.setOnClickListener {
            if (!isValidClick) {
                startActivity(
                    Intent(
                        mActivity,
                        AboutActivity::class.java
                    )
                )
            }
        }

        binding.logout.setOnClickListener {
            if (!isValidClick) {
                val builder = TitleDialog.Builder(mActivity!!)
                builder.setTitle("登出").setMessage("您是否确认登出此账号?")
                    .setPositiveButton(
                        "确认"
                    ) { dialog: DialogInterface, _: Int ->
                        MyApplication.mSp.putBoolean(ConstUtils.LOGIN_STATE, false)
                        MyApplication.mSp.putStr(ConstUtils.USER_TOKEN, "")
                        mActivity!!.shiftNavigation(R.id.home_tab)
                        dialog.dismiss()
                    }
                    .setNegativeButton(
                        "取消"
                    ) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
                    .create().show()
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
        if (requestCode == 10086) {
            installProcess();//再次执行安装流程，包含权限判等
        }
    }
//    private void performUI() {
//
//        if (oldInfoModel != null) {
//            name.setText(oldInfoModel.getOldName() == null ? "" : oldInfoModel.getOldName());
//            account.setText(MessageFormat.format("￥{0}", oldInfoModel.getAccountBalance() == null ? "0.0" : oldInfoModel.getAccountBalance()));
////            ImageLoader.getInstance().displayImage(oldInfoModel.getOldPinPath(), headImage, options);
//        } else {
//            showLongToast("老人信息为空!");
//        }
//    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            StatusBarUtils.setStatusBarColor(mActivity, resources.getColor(R.color.theme))
        }
    }

    private fun controlVersion(version: String) {
        val group: Array<String> = version.split("\\.".toRegex()).toTypedArray()
        when {
            Integer.parseInt(group[0]) < 1 -> {
                installProcess()
            }
            Integer.parseInt(group[1]) < 2 -> {
                installProcess()
            }
            Integer.parseInt(group[2]) < 3 -> {
                installProcess()
            }
        }
    }

    /**
     * 获取版本号
     */
    private val versionName: String?
        get() {
            var name: String? = null
            try {
                val packInfo = mActivity!!.packageManager.getPackageInfo(mActivity!!.packageName, 0)
                if (packInfo != null) {
                    val versionName = packInfo.versionName
                    val versionCode = packInfo.versionCode
                    Logger.i("======版本名：" + versionName + "版本号：" + versionCode + "=======")
                    if (versionName != null) {
                        name = String.format("V %s", versionName)
                        controlVersion(versionName)
                    }

                }
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return name
    }

    //安装应用的流程
    private fun installProcess() {
        val builder = TitleDialog.Builder(mActivity!!)
        builder.setTitle("系统更新").setMessage("检测到最新版本,是否现在更新？")
            .setPositiveButton(
                "后台下载"
            ) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val b = mActivity!!.packageManager.canRequestPackageInstalls()
                    if (b) {
                        DownloadUtils(mActivity!!).downloadApk(
                            ConstUtils.BASE_URL + "newOld/apk/zhyl-old.apk",
                            "下载中..."
                        )
                    } else {
                        //请求安装未知应用来源的权限
                        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
                        startActivityForResult(intent, 10086)
                    }
                } else {
                    DownloadUtils(mActivity!!).downloadApk(
                        ConstUtils.BASE_URL + "newOld/apk/zhyl-old.apk",
                        "下载中..."
                    )
                }

            }
            .setNegativeButton(
                "下次再说"
            ) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .create().show()
    }

    companion object {
        fun newInstance(): PersonFragment {
            return PersonFragment()
        }
    }
}