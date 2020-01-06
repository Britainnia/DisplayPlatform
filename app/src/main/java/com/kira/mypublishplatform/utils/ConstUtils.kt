package com.kira.mypublishplatform.utils

/**
 * 固定字段
 */
object ConstUtils {
    /**
     * 集成路径
     */
    const val BASE_URL = "http://120.199.82.52:5000/"
    /**
     * 测试集成路径
     */
    const val BASE_TEST_URL = "http://192.168.1.107:9900/"
    /**
     * 图片加载主路径
     */
//    public static String IMAGE_HOST = "http://120.27.138.91:3600/api/containers/images/download/";
    /**
     * 图片加载测试路径
     */
    val IMAGE_HOST = "http://120.27.138.91:3601/api/containers/images/download/"
    /**
     * 登录状态
     */
    const val LOGIN_STATE = "loginState"
    /**
     * 记住密码的状态
     */
    val REMEMBER_PASSWORD = "rememberPassword"
    /**
     * 用户账号
     */
    const val USER_NAME = "userName"
    /**
     * 用户密码
     */
    val USER_PASSWORD = "userPassword"
    /**
     * 用户id
     */
    const val USER_ID = "id"
    /**
     * 用户role
     */
    const val USER_ROLE = "role"
    /**
     * 用户token
     */
    const val USER_TOKEN = "token"
    /**
     * 用户邮箱
     */
    val EMAIL = "email"
    /**
     * 公钥信息
     */
    const val PUBULIC_KEY = "publicKey"
    /**
     * 工作人员ID
     */
    val STAFF_ID = "staffId"
    /**
     * 用户真实姓名
     */
    const val NAME = "name"
    /**
     * 用户身份证号码
     */
    val ID_NUMBER = "idNumber"
    /**
     * 用户头像路径
     */
    val HEAD_IMAGE = "headImage"
    /**
     * 用户二维码
     */
    val QRCODE = "qrCode"
    /**
     * 参加工作的时间
     */
    val JOIN_DATE = "joinDate"
    /**
     * 性别
     */
    val SEX = "sex"
    /**
     * 生日
     */
    val BIRTH_DATE = "birthDate"
    /**
     * NID
     */
    val NID = "NID"
    /**
     * 手机号码
     */
    val PHONE = "phone"
    /**
     * 注册验证码
     */
    val REGISTER_MSG = "registerMsg"
    /**
     * 找回密码验证码
     */
    val FOTGOT_MSG = "forgotMsg"
    /**
     * 用户类型
     */
    val USER_TYPE = "userType"
    /**
     * 邮箱是否已认证
     */
    val EMAIL_VERIFIED = "emailVerified"
    /**
     * 启动登录页传值，控制是否跳转
     */
    const val LOGIN_TOKEN = "loginToken"
}