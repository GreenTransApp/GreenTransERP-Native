package com.greensoft.greentranserpnative.api

import com.greensoft.greentranserpnative.common.CommonResult
import retrofit2.Call
import retrofit2.http.*


interface Api {

    @FormUrlEncoded
    @POST("WMS/ValidateLogin")
    fun userLogin(
        @Field("prmusername") prmusername: String?,
        @Field("prmpassword") prmpassword: String?,
        @Field("prmappversion") prmappversion: String?,
        @Field("prmappversiondt") prmappversiondt: String?,
        @Field("prmdevicedt") prmdevicedt: String?,
        @Field("prmdeviceid") prmdeviceid: String?
    ): Call<CommonResult>

    @FormUrlEncoded
    @POST("WMS/CommonApi")
    fun commonApi(
        @Field("prmcompanyid") companyId: String?,
        @Field("spname") spName: String?,
        @Field("param") params: List<String>,
        @Field("values") values: ArrayList<String>
    ): Call<CommonResult>


    @GET("WMS/LoginbranchList")
    fun getBranchForLogin(
//        @Query("prmcompanyid") prmcompanyid: String?,
        @Query("prmconnstring") prmcompanyid: String?,
        @Query("prmusername") prmusername: String?
    ): Call<CommonResult>

    @FormUrlEncoded
    @POST("Printer/ValidateBranchSelection")
    fun validateBranchSelection(
        @Field("prmconstring") companyId: String?,
        @Field("prmusername") userName: String?,
        @Field("prmselectedbranch") branchCode: String?,
        @Field("prmappversion") appVersion: String?,
        @Field("prmappversiondt") appVersionDt: String?,
        @Field("prmdeviceid") deviceId: String?
    ): Call<CommonResult>

    // greentrans_pickupacceptedbyda
    @FormUrlEncoded
    @POST("BookingAPI/AcceptPickup")
    fun acceptPickup(
        @Field("prmconnstring") companyId: String?,
        @Field("prmtransactionid") transactionId: String?,
        @Field("prmdapickupdate") pickupDate: String?,
        @Field("prmdapickuptime") pickupTime: String?,
        @Field("prmdapickupremarks") pickupRemarks: String?
    ): Call<CommonResult>

    // greentrans_pickupdeniedbyda
    @GET("BookingAPI/PickupDeniedbyda")
    fun rejectPickup(
        @Query("prmconnstring") companyId: String?,
        @Query("prmtransactionid") transactionId: String?,
        @Query("prmdacancelremarks") pickupRemarks: String?
    ): Call<CommonResult>

    // Gets the UserMast User Data
    @GET("UserDataLogin")
    fun userDataLogin(
        @Query("prmcompanyid") prmcompanyid: String?,
        @Query(encoded = true, value = "prmusername") prmusername: String?
    ): Call<CommonResult>
}