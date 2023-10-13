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


    @FormUrlEncoded
    @POST("BookingAPI/SaveJeenaBookingErpNative")
    fun saveJeenaBooking(
        @Field("companyId") companyId: String,
        @Field ("prmbranchcode") prmbranchcode: String,
        @Field ("prmbookingdt") prmbookingdt: String,
        @Field ("prmtime") prmtime: String,
        @Field ("prmegrno") prmegrno: String,
        @Field ("prmcustcode") prmcustcode: String,
        @Field ("prmdestcode") prmdestcode: String,
        @Field ("prmproductcode") prmproductcode: String,
        @Field ("prmpckgs") prmpckgs: String,
        @Field ("prmaweight") prmaweight: String,
        @Field ("prmvweight") prmvweight: String,
        @Field ("prmcweight") prmcweight: String,
        @Field ("prmcreateid") prmcreateid: String,
        @Field ("prmsessionid") prmsessionid: String,
        @Field ("prmrefno") prmrefno: String,
        @Field ("prmcngr") prmcngr: String,
        @Field ("prmcngraddress") prmcngraddress: String,
        @Field ("prmcngrcity") prmcngrcity: String,
        @Field ("prmcngrzipcode") prmcngrzipcode: String,
        @Field ("prmcngrstate") prmcngrstate: String,
        @Field ("prmcngrmobileno") prmcngrmobileno: String,
        @Field ("prmcngremailid") prmcngremailid: String,
        @Field ("prmcngrCSTNo") prmcngrCSTNo: String,
        @Field ("prmcngrLSTNo") prmcngrLSTNo: String,
        @Field ("prmcngrTINNo") prmcngrTINNo: String,
        @Field ("prmcngrSTaxRegNo") prmcngrSTaxRegNo: String,
        @Field ("prmcnge") prmcnge: String,
        @Field ("prmcngeaddress") prmcngeaddress: String,
        @Field ("prmcngecity") prmcngecity: String,
        @Field ("prmcngezipcode") prmcngezipcode: String,
        @Field ("prmcngestate") prmcngestate: String,
        @Field ("prmcngemobileno") prmcngemobileno: String,
        @Field ("prmcngeemailid") prmcngeemailid: String,
        @Field ("prmcngeCSTNo") prmcngeCSTNo: String,
        @Field ("prmcngeLSTNo") prmcngeLSTNo: String,
        @Field ("prmcngeTINNo") prmcngeTINNo: String,
        @Field ("prmcngeSTaxRegNo") prmcngeSTaxRegNo: String,
        @Field ("prmtransactionid") prmtransactionid: String,
        @Field ("prmmawbchargeapplicable") prmmawbchargeapplicable: String,
        @Field ("prmcustdeptid") prmcustdeptid: String,
        @Field ("prmreferencenostr") prmreferencenostr: String,
        @Field ("prmweightstr") prmweightstr: String,
        @Field ("prmpackagetypestr") prmpackagetypestr: String,
        @Field ("prmtempuraturestr") prmtempuraturestr: String,
        @Field ("prmpackingstr") prmpackingstr: String,
        @Field ("prmgoodsstr") prmgoodsstr: String,
        @Field ("prmdryicestr") prmdryicestr: String,
        @Field ("prmdryiceqtystr") prmdryiceqtystr: String,
        @Field ("prmdataloggerstr") prmdataloggerstr: String,
        @Field ("prmdataloggernostr") prmdataloggernostr: String,
        @Field ("prmdimlength") prmdimlength: String,
        @Field ("prmdimbreath") prmdimbreath: String,
        @Field ("prmdimheight") prmdimheight: String,
        @Field ("prmpickupboyname") prmpickupboyname: String,
        @Field ("prmboyid") prmboyid: String,
        @Field ("prmboxnostr") prmboxnostr: String,
        @Field ("prmstockitemcodestr") prmstockitemcodestr: String,
        @Field ("prmgelpackstr") prmgelpackstr: String,
        @Field ("prmgelpackitemcodestr") prmgelpackitemcodestr: String,
        @Field ("prmgelpackqtystr") prmgelpackqtystr: String,
        @Field ("prmmenucode") prmmenucode: String,
        @Field ("prminvoicenostr") prminvoicenostr: String,
        @Field ("prminvoicedtstr") prminvoicedtstr: String,
        @Field ("prminvoicevaluestr") prminvoicevaluestr: String,
        @Field ("prmewaybillnostr") prmewaybillnostr: String,
        @Field ("prmewaybilldtstr") prmewaybilldtstr: String,
        @Field ("prmewbvaliduptodtstr") prmewbvaliduptodtstr: String,
        @Field ("prmvendorcode") prmvendorcode: String,
        @Field ("prmpackagestr") prmpackagestr: String,
        @Field ("prmpickupby") prmpickupby: String,
        @Field ("prmvehicleno") prmvehicleno: String,
        @Field ("prmvweightstr") prmvweightstr: String,
        @Field ("prmvehiclecode") prmvehiclecode: String,
        @Field ("prmcngrcode") prmcngrcode: String,
        @Field ("prmcngecode") prmcngecode: String,
        @Field ("prmremarks") prmremarks: String,
        @Field ("prmcngrgstno") prmcngrgstno: String,
        @Field ("prmcngegstno") prmcngegstno: String
    ): Call<CommonResult>





}