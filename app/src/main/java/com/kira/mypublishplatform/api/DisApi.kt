package com.kira.mypublishplatform.api

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * 网络访问接口api
 */
interface DisApi {
    /**
     * 服务人员基本信息
     *
     */
    @POST("zhyl-provider/staff/getCountNum")
    fun getePersonCount(@Header("TOKEN") token: String?): Observable<ResponseBody>

    /**
     * 服务人员开始跟踪
     *
     */
    @FormUrlEncoded
    @POST("zhyl-provider/staff/startOrder")
    fun starteOrder(
        @Header("TOKEN") token: String?
        , @Field("longitude") longitude: Double?
        , @Field("latitude") latitude: Double?
        , @Field("orderId") orderId: String?
    ): Observable<ResponseBody>

    /**
     * 服务人员开始服务
     *
     */
    @FormUrlEncoded
    @POST("zhyl-provider/staff/startService")
    fun starteService(
        @Header("TOKEN") token: String?, @Field(
            "orderId"
        ) orderId: Int?, @Field("oldNo") oldNo: String?
    ): Observable<ResponseBody>

    /**
     * 服务人员结束服务
     *
     */
    @FormUrlEncoded
    @POST("zhyl-provider/staff/endService")
    fun endeService(
        @Header("TOKEN") token: String?, @Field(
            "orderId"
        ) orderId: Int?, @Field("oldNo") oldNo: String?
    ): Observable<ResponseBody>

    /**
     * 服务人员接单
     */
    @FormUrlEncoded
    @POST("zhyl-provider/staff/acceptOrder")
    fun accepteOrder(
        @Header("TOKEN") token: String?, @Field(
            "orderId"
        ) orderId: String?
    ): Observable<ResponseBody>

    /**
     * 服务人员拒单
     */
    @FormUrlEncoded
    @POST("zhyl-provider/staff/refuseOrder")
    fun refuseeOrder(
        @Header("TOKEN") token: String?, @Field(
            "orderId"
        ) orderId: String?
    ): Observable<ResponseBody>

    /**
     * 服务人员结算
     *
     */
    @FormUrlEncoded
    @POST("zhyl-provider/staff/settlement")
    fun sendeSettlement(
        @Header("TOKEN") token: String?, @Field(
            "orderId"
        ) orderId: Int, @Field("price") price: Double
    ): Observable<ResponseBody>

    /**
     * 获取待接单列表(服务人员)
     *
     * @param body
     * @return
     */
    @POST("zhyl-provider/staff/getToAcceptOrders")
    fun getToAccepteOrderList(@Header("TOKEN") token: String?, @Body body: RequestBody?): Observable<ResponseBody>

    /**
     * 获取服务中列表(服务人员)
     */
    @FormUrlEncoded
    @POST("zhyl-provider/staff/inService")
    fun getToServiceeOrderList(
        @Header("TOKEN") token: String?, @Field(
            "pageNum"
        ) page: Int?, @Field("pageSize") size: Int?
    ): Observable<ResponseBody>

    /**
     * 获取已完成列表(服务人员)
     */
//    @FormUrlEncoded
    @POST("zhyl-provider/staff/finishService")
    fun getToFinisheOrderList(@Header("TOKEN") token: String?, @Body body: RequestBody?): Observable<ResponseBody>

    /**
     * 获取待开始、已开始列表(服务人员)
     *
     */
    @FormUrlEncoded
    @POST("zhyl-provider/staff/getToStartOrders")
    fun getToStarteOrderList(
        @Header("TOKEN") token: String?
        , @Field("longitude") longitude: Double?
        , @Field("latitude") latitude: Double?
        , @Field("pageNum") page: Int?
        , @Field("pageSize") size: Int?
    ): Observable<ResponseBody>

    /**
     * 获取订单详情（待开始除外）
     * (服务人员)
     *
     */
    @FormUrlEncoded
    @POST("zhyl-provider/staff/inServiceDetail")
    fun geteServiceDetail(
        @Header("TOKEN") token: String?, @Field(
            "orderId"
        ) orderId: Int
    ): Observable<ResponseBody>

    /**
     * 获取各列表数量(全部)
     */
//    @FormUrlEncoded
    @POST("zhyl-provider/person/getTotalNum")
    fun getListNumber(@Header("TOKEN") token: String?): Observable<ResponseBody>

    /**
     * 更新个人信息(服务人员)
     *
     * @param body
     * @return
     */
    @POST("zhyl-provider/staff/editInfo")
    fun getePersonInfo(@Header("TOKEN") token: String?, @Body body: RequestBody?): Observable<ResponseBody>

    /**
     * 获取短信验证码
     *
     * @param mobile
     * @param type
     */
    @FormUrlEncoded
    @POST("zhyl-provider/index/getCode")
    fun sendMsg(@Field("phone") mobile: String?, @Field("type") type: String?): Observable<ResponseBody>

    /**
     * 获取密钥
     */
    @POST("disapp/login/getpk")
    fun getKey(): Observable<ResponseBody>

    /**
     * 用户登录
     *
     * @param account 账号
     * @param password 密码
     * @return
     */
    @FormUrlEncoded
    @POST("disapp/login/cklogin")
    fun clientLogin(
        @Field("loginAccount") account: String?
        , @Field("loginPass") password: String?
    ): Observable<ResponseBody>
    //            , @Field("regId") String regId
//            , @Field("roleType") String role);

    /**
     * 获取应用
     */
    @POST("disapp/remote/u/getAppsNoLogin")
    fun getApps(): Observable<ResponseBody>

    /**
     * 用户注册
     *
     */
    @POST("disapp/remote/u/regadd")
    fun clientRegister(@Body body: RequestBody?): Observable<ResponseBody>

    @FormUrlEncoded
    @POST("disapp/remote/u/sendmsg")
    fun sendResetMsg(@Field("loginAccount") phone: String?): Observable<ResponseBody>

    /**
     * 用户重置密码
     *
     * @param phone 手机号
     * @param password 密码
     * @return
     */
    @POST("disapp/remote/u/repass")
    fun clientResetPass(
        @Field("oldPhone") phone: String?
        , @Field("newPass") password: String?
        , @Field("msgCode") code: String?
        , @Field("roleType") role: String?
    ): Observable<ResponseBody>

    /**
     * 用户修改密码
     *
     * @param account 账号
     * @param passone 新密码
     * @param passtwo 旧密码
     * @return
     */
    @FormUrlEncoded
    @POST("disapp/remote/u/chpass")
    fun clientChangePass(
        @Header("TOKEN") token: String?
        , @Field("loginAccount") account: String?
        , @Field("newPass") passone: String?
        , @Field("oldPass") passtwo: String?
    ): Observable<ResponseBody>

    /**
     * 用户修改手机号
     *
     * @param password 旧密码
     * @param phone 新手机
     * @param message 验证码
     * @return
     */
    @FormUrlEncoded
    @POST("disapp/remote/u/chphone")
    fun clientChangePhone(
        @Header("TOKEN") token: String?
        , @Field("newPhone") phone: String?
        , @Field("oldPass") password: String?
        , @Field("msgCode") message: String?
    ): Observable<ResponseBody>

    @FormUrlEncoded
    @POST("disapp/remote/u/sendchphonemsg")
    fun sendChangeMsg(
        @Header("TOKEN") token: String?, @Field(
            "newPhone"
        ) phone: String?
    ): Observable<ResponseBody>

    /**
     * 志愿者/社会人员基本信息
     *
     */
    @POST("zhyl-provider/person/myData")
    fun getMyData(@Header("TOKEN") token: String?): Observable<ResponseBody>

    /**
     * 获取活动列表
     *
     */
    @FormUrlEncoded
    @POST("disapp/mh/mhinfo/getMobileGrid.do")
    fun getActList(
        @Field("code") code: String?
        , @Field("text") text: String?
        , @Field("page") page: Int?
        , @Field("rows") size: Int?
    ): Observable<ResponseBody>

    /**
     * 获取资讯列表
     *
     */
    @FormUrlEncoded
    @POST("disapp/mh/mhinfo/getMobileGrid.do")
    fun getInfoList(
        @Field("code") code: String?
        , @Field("page") page: Int?
        , @Field("rows") size: Int?
    ): Observable<ResponseBody>

    /**
     * 志愿者接单
     */
    @FormUrlEncoded
    @POST("zhyl-provider/volunteer/acceptOrder")
    fun acceptvOrder(
        @Header("TOKEN") token: String?, @Field(
            "orderId"
        ) orderId: String?
    ): Observable<ResponseBody>

    /**
     * 志愿者/社会人员拒单
     */
    @FormUrlEncoded
    @POST("zhyl-provider/person/rejectOrder")
    fun refusevsOrder(
        @Header("TOKEN") token: String?, @Field(
            "orderId"
        ) orderId: String?, @Field("reason") reason: String?
    ): Observable<ResponseBody>

    /**
     * 志愿者结算
     *
     */
    @FormUrlEncoded
    @POST("zhyl-provider/volunteer/settlement")
    fun sendvSettlement(
        @Header("TOKEN") token: String?, @Field(
            "orderId"
        ) orderId: Int, @Field("price") price: Double
    ): Observable<ResponseBody>

    /**
     * 获取待接单列表(志愿者)
     *
     * @param body
     * @return
     */
    @POST("zhyl-provider/volunteer/listOrders")
    fun getToAcceptvOrderList(@Header("TOKEN") token: String?, @Body body: RequestBody?): Observable<ResponseBody>

    /**
     * 获取服务中列表(志愿者)
     */
    @FormUrlEncoded
    @POST("zhyl-provider/volunteer/getInService")
    fun getToServicevOrderList(
        @Header("TOKEN") token: String?, @Field(
            "pageNum"
        ) page: Int?, @Field("pageSize") size: Int?
    ): Observable<ResponseBody>

    /**
     * 获取已完成列表(志愿者)
     */
//    @FormUrlEncoded
    @POST("zhyl-provider/volunteer/getFinishOrders")
    fun getToFinishvOrderList(@Header("TOKEN") token: String?, @Body body: RequestBody?): Observable<ResponseBody>

    /**
     * 获取待开始、已开始列表(志愿者)
     *
     */
    @FormUrlEncoded
    @POST("zhyl-provider/volunteer/getToStartOrders")
    fun getToStartvOrderList(
        @Header("TOKEN") token: String?
        , @Field("longitude") longitude: Double?
        , @Field("latitude") latitude: Double?
        , @Field("pageNum") page: Int?
        , @Field("pageSize") size: Int?
    ): Observable<ResponseBody>

    /**
     * 获取订单详情（待开始除外）
     * (志愿者)
     *
     */
    @FormUrlEncoded
    @POST("zhyl-provider/volunteer/getInServiceDetail")
    fun getvServiceDetail(
        @Header("TOKEN") token: String?, @Field(
            "orderId"
        ) orderId: Int
    ): Observable<ResponseBody>

    /**
     * 获取个人信息(所有)
     *
     */
    @POST("zhyl-provider/person/myInfo")
    fun getPersonInfo(@Header("TOKEN") token: String?): Observable<ResponseBody>

    /**
     * 更新个人信息(志愿者/社会人员)
     *
     * @param body
     * @return
     */
    @POST("zhyl-provider/person/perfectInfo")
    fun getvsPersonInfo(@Header("TOKEN") token: String?, @Body body: RequestBody?): Observable<ResponseBody>

    /**
     * 转赠时间(志愿者/社会人员)
     *
     * @return
     */
    @FormUrlEncoded
    @POST("zhyl-provider/person/transfer")
    fun transferTime(
        @Header("TOKEN") token: String?
        , @Field("transferAccount") account: String?
        , @Field("transferTime") time: String?
    ): Observable<ResponseBody>

    /**
     * 上传图片的接口
     *
     * @param partList
     * @return
     */
    @Multipart
    @POST("zhyl-provider/file/uploadFile")
    fun upload(@Header("TOKEN") token: String?, @Part partList: List<MultipartBody.Part?>?): Observable<ResponseBody>
    //    /**
//     * 获取完整图片路径的接口
//     *
//     * @param fileName
//     * @return
//     */
//    @GET("zhyl-old/code/{fileName}")
//    Observable<ResponseBody> getPicture(@Header("TOKEN") String token, @Path("fileName") String fileName);
    /**
     * 获取项目列表
     *
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("zhyl-provider/person/getProviderItems")
    fun getProviderItems(
        @Header("TOKEN") token: String?
        , @Field("serviceType") serviceType: String?
        , @Field("serviceName") serviceName: String?
        , @Field("status") status: String?
    ): Observable<ResponseBody>

    /**
     * 提交项目
     *
     * @param token
     * @return
     */
    @POST("zhyl-provider/person/addService")
    fun addProviderItems(@Header("TOKEN") token: String?, @Body body: RequestBody?): Observable<ResponseBody>

    /**
     * 修改项目
     *
     * @param token
     * @return
     */
    @POST("zhyl-provider/person/editService")
    fun editProviderItems(@Header("TOKEN") token: String?, @Body body: RequestBody?): Observable<ResponseBody>

    /**
     * 删除项目
     *
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("zhyl-provider/person/deleteItem")
    fun deleteProviderItems(
        @Header("TOKEN") token: String?, @Field(
            "itemCode"
        ) code: String?, @Field("itemType") type: String?
    ): Observable<ResponseBody>

    /**
     * 获取政府购买
     *
     * @param token
     * @return
     */
    @POST("zhyl-provider/item/getAllGovItems")
    fun getAllGovItems(@Header("TOKEN") token: String?): Observable<ResponseBody>

    /**
     * 获取社会化
     *
     * @param token
     * @return
     */
    @POST("zhyl-provider/item/getAllItems")
    fun getAllItems(@Header("TOKEN") token: String?): Observable<ResponseBody>

    /**
     * 获取门店列表
     */
    @GET("Stores/searchByAPP")
    fun searchByAPP(
        @Header("Authorization") token: String?, @Query("center") center: String?,
        @Query("radius") radius: String?, @Query("limit") limit: String?,
        @Query("page") page: String?, @Query("keywords") keywords: String?
    ): Observable<ResponseBody>

    /**
     * 社会人员开始跟踪
     *
     */
    @FormUrlEncoded
    @POST("zhyl-provider/social/startOrder")
    fun startsOrder(
        @Header("TOKEN") token: String?
        , @Field("longitude") longitude: Double?
        , @Field("latitude") latitude: Double?
        , @Field("orderId") orderId: String?
    ): Observable<ResponseBody>

    /**
     * 社会人员开始服务
     *
     */
    @FormUrlEncoded
    @POST("zhyl-provider/social/startService")
    fun startsService(
        @Header("TOKEN") token: String?, @Field(
            "orderId"
        ) orderId: Int?, @Field("oldNo") oldNo: String?
    ): Observable<ResponseBody>

    /**
     * 社会人员结束服务
     *
     */
    @FormUrlEncoded
    @POST("zhyl-provider/social/endService")
    fun endsService(
        @Header("TOKEN") token: String?, @Field(
            "orderId"
        ) orderId: Int?, @Field("oldNo") oldNo: String?
    ): Observable<ResponseBody>

    /**
     * 社会人员接单
     */
    @FormUrlEncoded
    @POST("zhyl-provider/social/acceptOrder")
    fun acceptsOrder(
        @Header("TOKEN") token: String?, @Field(
            "orderId"
        ) orderId: String?
    ): Observable<ResponseBody>

    /**
     * 社会人员结算
     *
     */
    @FormUrlEncoded
    @POST("zhyl-provider/social/settlement")
    fun sendsSettlement(
        @Header("TOKEN") token: String?, @Field(
            "orderId"
        ) orderId: Int, @Field("price") price: Double
    ): Observable<ResponseBody>

    /**
     * 获取待接单列表(社会人员)
     *
     * @param body
     * @return
     */
    @POST("zhyl-provider/social/getCanOrderList")
    fun getToAcceptsOrderList(@Header("TOKEN") token: String?, @Body body: RequestBody?): Observable<ResponseBody>

    /**
     * 获取服务中列表(社会人员)
     */
    @FormUrlEncoded
    @POST("zhyl-provider/social/inService")
    fun getToServicesOrderList(
        @Header("TOKEN") token: String?, @Field(
            "pageNum"
        ) page: Int?, @Field("pageSize") size: Int?
    ): Observable<ResponseBody>

    /**
     * 获取已完成列表(社会人员)
     */
//    @FormUrlEncoded
    @POST("zhyl-provider/social/getFinishOrders")
    fun getToFinishsOrderList(@Header("TOKEN") token: String?, @Body body: RequestBody?): Observable<ResponseBody>

    /**
     * 获取待开始、已开始列表(社会人员)
     *
     */
    @FormUrlEncoded
    @POST("zhyl-provider/social/getToStartOrders")
    fun getToStartsOrderList(
        @Header("TOKEN") token: String?
        , @Field("longitude") longitude: Double?
        , @Field("latitude") latitude: Double?
        , @Field("pageNum") page: Int?
        , @Field("pageSize") size: Int?
    ): Observable<ResponseBody>

    /**
     * 获取订单详情（待开始除外）
     * (社会人员)
     *
     */
    @FormUrlEncoded
    @POST("zhyl-provider/social/inServiceDetail")
    fun getsServiceDetail(
        @Header("TOKEN") token: String?, @Field(
            "orderId"
        ) orderId: Int
    ): Observable<ResponseBody>
}