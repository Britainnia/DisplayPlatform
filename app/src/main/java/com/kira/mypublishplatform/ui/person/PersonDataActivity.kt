package com.kira.mypublishplatform.ui.person

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dyhdyh.widget.loadingbar2.LoadingBar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kira.mypublishplatform.R
import com.kira.mypublishplatform.activity.LoginActivity
import com.kira.mypublishplatform.base.BaseActivity
import com.kira.mypublishplatform.base.MyApplication
import com.kira.mypublishplatform.bean.ResponseBean
import com.kira.mypublishplatform.databinding.ActivityPersonDataBinding
import com.kira.mypublishplatform.model.OldInfoModel
import com.kira.mypublishplatform.utils.ConstUtils
import com.kira.mypublishplatform.utils.Logger
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

import okhttp3.ResponseBody
import java.io.IOException

class PersonDataActivity : BaseActivity<ActivityPersonDataBinding>() {
//    private ImageView backBt, userImg;
//    private EditText phone, registerAddress, detailAddress, socialNo, urgentPhone, urgentName, urgentRelation;
//    private TextView name, userName, idCard, age, phoneNumber, save, sex, national, marriageStatus, oldType, dateBirth, com, org;
//    private EditHelper editHelper = new EditHelper(this);
//    private final static int REQUEST_GALLERY = 0;
//    private final static int REQUEST_CAMERA = 1;
//    private final static int REQUEST_CROP = 3;
//    private File mTmpFile;
//    private File mCropImageFile;
//    private static final List<String> sexTypes= new ArrayList<>(Arrays.asList("男","女"));
//    private static final List<String> marriageTypes = new ArrayList<>(Arrays.asList("未婚","已婚","离异","丧偶"));
//    private static final List<String> oldTypes = new ArrayList<>(Arrays.asList("退休干部","退休工人"));
    private var oldInfoModel = OldInfoModel()
//    private var mBinding: ActivityPersonDataBinding? = null
    private val mContext: Context = this@PersonDataActivity
//    private TextView inComes,diseaseType,incomeSource;
//    private static final ArrayList<String> diseaseTypes = new ArrayList<>(Arrays.asList("高血压","糖尿病","癌症","肿瘤","慢性呼吸性疾病","其他慢性病"));
//    private static final ArrayList<String> sourceTypes = new ArrayList<>(Arrays.asList("子女供养","家庭存款","救助金","退休金","无固定来源"));  //多选
//    private static final ArrayList<String> inComeTypes = new ArrayList<>(Arrays.asList("0~499","500~999","1000~1999","2000~2999","3000~3999","4000以上"));
//    private ArrayList<String> nationalStr = new ArrayList<>();
//    private ArrayList<String> orgStr = new ArrayList<>();
//    private ArrayList<TextBean> options1Items = new ArrayList<>();//一级
//    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();//二级
//    override fun loadXml() {
//        mBinding = ActivityPersonDataBinding.inflate(layoutInflater)
//        setContentView(mBinding!!.root)
//            this,
//            R.layout.activity_person_data
//        )
//    }

    override fun getIntentData(savedInstanceState: Bundle?) {
        if (intent != null) oldInfoModel =
            intent.getSerializableExtra("model") as OldInfoModel
    }

    override fun initData() {
//        phone.ellipsize = TextUtils.TruncateAt.MARQUEE
//        phone.isSingleLine = true
//        phone.isSelected = true
//        phone.isFocusable = true
//        phone.isFocusableInTouchMode = true

        oldInfoModel.oldName = MyApplication.mSp.getStr(ConstUtils.USER_NAME)
        oldInfoModel.oldPhone = MyApplication.mSp.getStr(ConstUtils.PHONE)
        oldInfoModel.loginAccount = MyApplication.mSp.getStr(ConstUtils.NAME)
        binding.model = oldInfoModel

    }

    override fun setListener() {

        binding.ivBack.setOnClickListener {
            finish()
        }

    }

    private val permission2: Unit
        get() {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) { //申请ACCESS_COARSE_LOCATION权限
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    100
                ) //自定义的code
            } else if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) { //申请WRITE_EXTERNAL_STORAGE权限
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    101
                ) //自定义的code
            }
        }

    private val permission1: Unit
        get() {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) { //申请ACCESS_COARSE_LOCATION权限
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    200
                ) //自定义的code
            }
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showLongToast("未授予读写权限，无法使用该功能，请开启权限")
                permission2
            }
        } else if (requestCode == 101) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showLongToast("未授予拍照权限，无法使用该功能，请开启权限")
            } else {
                permission2
            }
        } else if (requestCode == 200) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showLongToast("未授予拍照权限，无法使用该功能，请开启权限")
            } else {
                permission1
            }
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case REQUEST_CAMERA:
//                if (resultCode == RESULT_OK) {
////                    crop(mTmpFile.getAbsolutePath());
//                    sourceImgPath = mTmpFile.getAbsolutePath();
//                    ImageLoader.getInstance().displayImage(Uri.fromFile(new File(sourceImgPath)).toString(), userImg, options);
//                    isImageChanged = true;
////                    upLoadImages(mTmpFile.getAbsolutePath());
//                } else {
//                    Logger.i("放弃拍照");
////                   showShortToast("拍照失败");
//                }
//                break;
//            case REQUEST_CROP:
//                if (resultCode == RESULT_OK) {
//                    // 把图片上传到服务器
////                    upLoadImages();
//                    //显示图片
//                    //userImg.setImageURI(Uri.fromFile(mCropImageFile));
//                } else {
//                    Logger.i("放弃截图");
////                    showShortToast("截图失败");
//                }
//                break;
//            case REQUEST_GALLERY:
//                if (resultCode == RESULT_OK && data != null) {
//                    sourceImgPath = handleImage(data);
////                    crop(imagePath);
//                    ImageLoader.getInstance().displayImage(Uri.fromFile(new File(sourceImgPath)).toString(), userImg, options);
//                    isImageChanged = true;
////                    upLoadImages(imagePath);
//                } else {
//                    Logger.i("放弃打开图库");
////                    showShortToast("打开图库失败");
//                }
//                break;
//            case 250:
//                if (resultCode == RESULT_OK)
//                    getInfo();
//                break;
//        }
//    }

    private fun getInfo() {
        LoadingBar.dialog(mContext).extras(arrayOf<Any>("信息获取中..")).show()
        val token = MyApplication.mSp.getStr(ConstUtils.USER_TOKEN)


        MyApplication.api.getMyData(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ResponseBody> {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    LoadingBar.dialog(mContext).cancel()
                    Logger.e("==onError==" + e.message)
                    showShortToast("获取用户信息失败," + e.message)
                }

                override fun onNext(body: ResponseBody) {
                    LoadingBar.dialog(mContext).cancel()
                    try {
                        val json = body.string()
                        val rawBean =
                            Gson().fromJson(json, ResponseBean::class.java)
                        when {
                            rawBean.isStatus -> {
                                val bean =
                                    Gson().fromJson<ResponseBean<OldInfoModel>>(
                                        json,
                                        object : TypeToken<ResponseBean<OldInfoModel?>?>() {}.type
                                    )
                                oldInfoModel = bean.data!!
                            }
                            "10009" == rawBean.code -> {
                                val intent =
                                    Intent(mContext, LoginActivity::class.java)
                                intent.putExtra(ConstUtils.LOGIN_TOKEN, true)
                                startActivityForResult(intent, 250)
                                showLongToast("登录过期，请重新登录")
                            }
                            else -> {
                                showLongToast("获取用户信息失败！" + rawBean.msg)
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            })
    }

}