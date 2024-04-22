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
    @POST("HomeAPI/CommonApi")
    fun commonApiHome(
        @Field("prmcompanyid") companyId: String?,
        @Field("spname") spName: String?,
        @Field("param") params: List<String>,
        @Field("values") values: ArrayList<String>
    ) : Call<CommonResult>
//    ) {
//        Log.d("Common Api Home", "with spName: $spName")
//        Log.d("Params for ${spName}:", params.toString())
//        Log.d("Values for ${spName}:", values.toString())
//    }

    @FormUrlEncoded
    @POST("WMS/CommonApi")
    fun commonApiWMS(
        @Field("prmcompanyid") companyId: String?,
        @Field("spname") spName: String?,
        @Field("param") params: List<String>,
        @Field("values") values: ArrayList<String>
    ) : Call<CommonResult>


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
        @Field("prmdapickupremarks") pickupRemarks: String?,
        @Field("prmsendmsgtocust") prmsendmsgtocust: String
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
    @POST("WMS/ScanAndLoadUpdate")
    fun saveStickerScanLoad(
        @Field("prmconnstring") companyId: String?,
        @Field("prmloadingno") loadingNo: String?,
        @Field("prmloadingdt") loadingDt: String?,
        @Field("prmloadingtime") loadingTime: String?,
        @Field("prmstncode") stnCode: String?,
        @Field("prmbranchcode") branchCode: String?,
        @Field("prmdestcode") destCode: String?,
        @Field("prmmodetype") modeType: String?,
        @Field("prmvendcode") vendCode: String?,
        @Field("prmmodecode") modeCode: String?,
        @Field("prmloadedby") loadedBy: String?,
        @Field("prmdrivercode") driverCode: String?,
        @Field("prmremarks") remarks: String?,
        @Field("prmstickornostr") stickerNoStr: String?,
        @Field("prmgrnostr") grNoStr: String?,
        @Field("prmusercode") userCode: String?,
        @Field("prmmenucode") menuCode: String?,
        @Field("prmsessionid") sessionId: String?,
        @Field("prmdrivertelno") driverMobileNo: String?,
        @Field("prmdespatchtype") despatchType: String?
    ): Call<CommonResult>

    @FormUrlEncoded
    @POST("WMS/ScanAndLoadRemove")
    fun removeStickerScanLoad(
        @Field("prmcompanyid") companyId: String?,
        @Field("prmstickerno") stickerNo: String?,
        @Field("prmusercode") userCode: String?,
        @Field("prmmenucode") menuCode: String?,
        @Field("prmsessionid") sessionId: String?
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
        @Field ("prmpckgs") prmpckgs: Int,
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
        @Field ("prmcngegstno") prmcngegstno: String,
        @Field("prmimages") prmimages: ArrayList<String>,
    ): Call<CommonResult>


    @FormUrlEncoded
    @POST("ManifestAPI/SavePickupManifestERPNative")
     fun savePickupManifest(
        @Field("prmconnstring") companyId: String,
        @Field("prmbranchcode")prmbranchcode :String ,
        @Field("prmdt")prmdt :String ,
        @Field("prmtime")prmtime :String ,
        @Field("prmmanifestno")prmmanifestno :String ,
        @Field("prmmodecode")prmmodecode :String ,
        @Field("prmtost")prmtost :String ,
        @Field("prmdrivercode")prmdrivercode :String ,
        @Field("prmdrivermobileno")prmdrivermobileno :String ,
        @Field("prmloadedby")prmloadedby :String ,
        @Field("prmremarks")prmremarks :String ,
        @Field("prmispickupmanifest")prmispickupmanifest :String ,
//        @Field("prmgrno")prmgrno :String ,
        @Field("prmloadingno") prmloadingno :String ,
        @Field("prmpckgs")prmpckgs :String ,
        @Field("prmaweight")prmaweight :String ,
        @Field("prmgoods")prmgoods :String ,
        @Field("prmpacking")prmpacking :String ,
        @Field("prmareacode")prmareacode :String ,
        @Field("prmvendcoe")prmvendcoe :String ,
        @Field("prmloadedbytype")prmloadedbytype :String ,
        @Field("prmmenucode")prmmenucode :String ,
        @Field("prmusercode")prmusercode :String ,
        @Field("prmsessionid")prmsessionid :String ,
        @Field("prmpaymentnotapplicable")prmpaymentnotapplicable :String ,
        @Field("prmskipinscan")prmskipinscan :String ,
    ):Call<CommonResult>
    @GET("WMS/ScanLoadSummary")
    fun scanLoadSummary(
        @Query("prmconnstring") companyId: String?,
        @Query("prmbranchcode") branchCode: String?,
        @Query("prmloadingno") loadingNo: String?,
        @Query("prmcloseloading") closeLoading: String?,
        @Query("prmusercode") userCode: String?,
        @Query("prmmenucode") menuCode: String?,
        @Query("prmsessionid") sessionId: String?
    ): Call<CommonResult>

    @GET("BookingAPI/GetKey")
    fun getAccParaValueFromKey(
        @Query("KeyNo") keyNo: String,
        @Query("prmcompanyid") companyId: String
    ): Call<CommonResult>

    @GET("WMS/LoadStickerGetList")
    fun getLoadedStickers(
        @Query("prmcompanyid") companyId: String,
        @Query("prmloadingno") loadingNo: String
    ): Call<CommonResult>

    @GET("WMS/GetStickerDetailForScanAndLoad")
    fun validateStickerDetail(
        @Query("prmcompanyid") companyId: String,
        @Query("prmbranchcode") branchCode: String?,
        @Query("prmstickerno") stickerNo: String?,
        @Query("prmdt") date: String?
    ): Call<CommonResult>
 @GET("InscanAPI/GetPendingRetrialval_NV")
    fun getInscanList(
        @Query("prmconnstring") companyId: String,
        @Query("prmusercode") userCode: String?,
        @Query("prmbranchcode") branchCode: String?,
        @Query("prmsessionid") sessionId: String?,
        @Query("prmfrombranchcode") fromBranchCode: String?,
        @Query("prmfromdt") fromDt: String?,
        @Query("prmtodt") toDt: String?,
        @Query("prmmanifesttype") manifestType: String?,
        @Query("prmmodetype") modeType: String?
    ): Call<CommonResult>

    @GET("WMS/GetPendingArrivalDetail")
    fun getInScanDetail(
        @Query("prmconnstring") companyId: String,
        @Query("prmusercode") userCode: String?,
        @Query("prmbranchcode") branchCode: String?,
        @Query("prmmanifestno") manifestNo: String?,
        @Query("prmmanifesttype") manifestType: String?,
        @Query("prmsessionid") sessionId: String?
    ): Call<CommonResult>

    @GET("BookingAPI/GetPickupDataForBooking")
    fun getBookingIndentInfo(
        @Query("prmconnstring") companyId: String,
        @Query("prmtransactionid") transactionId: String?
    ):Call<CommonResult>

    @GET("InscanAPI/GetDamageReason")
    fun getDamageReasonList(
        @Query("prmconnstring") companyId: String,

    ):Call<CommonResult>
    @FormUrlEncoded
    @POST("WMS/AddInScanStickerUnArrived")
    fun addInScanStickerUnArrived(
        @Field("prmcompanyid") companyId: String,
        @Field("prmusercode") userCode: String?,
        @Field("prmbranchcode") branchCode: String?,
        @Field("prmmenucode") menuCode: String?,
        @Field("prmsessionid") sessionId: String?,
        @Field("prmstickerno") stickerNo: String?,
        @Field("prmmanifestno") manifestNo: String?,
        @Field("prmreceivedt") receiveDt: String?,
        @Field("prmreceivetime") receiveTime: String?
    ):Call<CommonResult>

    @FormUrlEncoded
    @POST("WMS/AddInScanStickerUnArrived_Outstation")
    fun addInScanStickerUnArrived_outstation(
        @Field("prmcompanyid") companyId: String,
        @Field("prmusercode") userCode: String?,
        @Field("prmbranchcode") branchCode: String?,
        @Field("prmmenucode") menuCode: String?,
        @Field("prmsessionid") sessionId: String?,
        @Field("prmstickerno") stickerNo: String?,
        @Field("prmmanifestno") manifestNo: String?,
        @Field("prmreceivedt") receiveDt: String?,
        @Field("prmreceivetime") receiveTime: String?
    ):Call<CommonResult>

    @FormUrlEncoded
    @POST("WMS/SaveInscanDetailWithoutScan")
    fun saveInscanDetailWithoutScan(
        @Field("prmconnstring") companyId: String,
        @Field("prmmanifestno")manifestNo:String,
        @Field("prmmawbno")mawbNo:String,
        @Field("prmbranchcode")branchCode:String,
        @Field("prmreceivedt")receiveDt:String,
        @Field("prmreceivetime")receiveTime:String,
        @Field("prmvehiclecode")vehicleCode:String,
        @Field("prmremarks")remarks:String,
        @Field("prmgrno")grNo:String,
        @Field("prmmfpckgs")mfPckgs:String,
        @Field("prmpckgs")pckgs:String,
        @Field("prmweight")weight:String,
        @Field("prmdamagepckgs")damagePckgs:String,
        @Field("prmdamagereasoncode")damageReasoncode:String,
        @Field("prmdetailremarks")detailRemarks:String,
        @Field("prmusercode")userCode:String,
        @Field("prmmenucode")menuCode:String,
        @Field("prmsessionid")sessionId:String,
        @Field("prmfromstncode")fromstnCode:String,
    ):Call<CommonResult>

    @FormUrlEncoded
    @POST("WMS/SaveInscanDetailWithoutScan_Outstation")
    fun saveOutstationInscanDetailWithoutScan(
        @Field("prmconnstring") companyid: String,
        @Field("prmmanifestno")manifestno:String,
        @Field("prmmawbno")mawbno:String,
        @Field("prmbranchcode")branchcode:String,
        @Field("prmreceivedt")receivedt:String,
        @Field("prmreceivetime")receivetime:String,
        @Field("prmvehiclecode")vehiclecode:String,
        @Field("prmremarks")remarks:String,
        @Field("prmgrno")grno:String,
        @Field("prmmfpckgs")mfpckgs:String,
        @Field("prmpckgs")pckgs:String,
        @Field("prmweight")weight:String,
        @Field("prmdamagepckgs")damagepckgs:String,
        @Field("prmdamagereasoncode")damagereasoncode:String,
        @Field("prmdetailremarks")detailremarks:String,
        @Field("prmusercode")usercode:String,
        @Field("prmmenucode")menucode:String,
        @Field("prmsessionid")sessionid:String,
        @Field("prmfromstncode")fromstncode:String,
    ):Call<CommonResult>

    @GET("ManifestAPI/GetVendorListV2")
    fun getVendListDRS(
        @Query("prmconnstring") companyId: String,
        @Query("prmcharstr") charStr: String?,
        @Query("prmcategory") category: String?,
    ): Call<CommonResult>

    @GET("ManifestAPI/VehicleSelectionFromJeenaBooking")
    fun getVehicleListDRS(
        @Query("prmconnstring") companyId: String,
        @Query("prmbranchcode") branchCode:String?,
        @Query("prmusercode") userCode: String?,
        @Query("prmcharstr") charStr: String?,
        @Query("prmvendcode") vendCode: String?,
        @Query("prmmodetype") modeType: String?,
    ): Call<CommonResult>
 @GET("PODAPI/showpoddetails")
    fun getPodDetails(
        @Query("prmconnstring") companyId: String,
        @Query("prmgrno") grNo:String?,

    ): Call<CommonResult>

    @GET("WMS/WMSAPP_GETGRNOFROMSTICKER")
    fun getGrnoFromSticker(
        @Query("prmcompanyid") companyId: String?,
        @Query("prmusercode") userCode: String?,
        @Query("prmbranchcode") branchCode: String?,
        @Query("prmmenucode") menuCode: String?,
        @Query("prmstickerno") stickerNo: String?,
        @Query("prmsessionid") sessionId: String?
    ): Call<CommonResult>

    @GET("PODAPI/gerRelations")
    fun getRelationList(
        @Query("prmconnstring") companyId: String,
    ): Call<CommonResult>

    @FormUrlEncoded
    @POST("ManifestAPI/updateDRSNew_v1")
    fun saveDRS(
        @Field("prmconnstring") companyId: String,
        @Field("prmbranchcode")branchCode:String?,
        @Field("prmdrsdt")drsDt:String?,
        @Field("prmdrstime")drsTime:String?,
        @Field("prmdrsno")drsSno:String?,
        @Field("prmdrivername")driverName:String?,
        @Field("prmdrivertelno")driverTele:String?,
        @Field("prmmodecode")modeCode:String?,
        @Field("prmremarks")remarks:String?,
        @Field("prmgrnostr")grnoStr:String?,
        @Field("prmdrnostr")drnoStr:String?,
        @Field("prmpckgstr")pckgsStr:String?,
        @Field("prmweightstr")weightStr:String?,
        @Field("prmremarksstr")remarksStr:String?,
        @Field("prmloadedby")loadedBy:String?,
        @Field("prmusercode")userCode:String?,
        @Field("prmsessionid")sessionId:String?,
        @Field("prmmenucode")menuCode:String?,
        @Field("prmexecutiveid")executiveId:String?,
        @Field("prmdeliveredby")deliveredBy:String?,
        @Field("prmdlvagentcode")dlvAgentCode:String?,
        @Field("prmdlvvehicleno")dlvVehicleNo:String?,

    ):Call<CommonResult>

    @FormUrlEncoded
    @POST("PODAPI/SavePodV2")
    fun SavePodEntry(
        @Field("prmconnstring") companyId :String?,
        @Field("prmloginbranchcode") loginBranchCode:String?,
        @Field("prmgrno") grNo:String?,
        @Field("prmdlvtime") dlvTime:String?,
        @Field("prmname") name:String?,
        @Field("prmdlvdt") dlvDt:String?,
        @Field("prmrelation") relation:String?,
        @Field("prmphno") phoneNo:String?,
        @Field("prmsign") sign:String?,
        @Field("prmstamp") stamp:String?,
        @Field("prmpodimage") podImage:String?,
        @Field("prmsignimage") signImage:String?,
        @Field("prmremarks") remarks:String?,
        @Field("prmusercode") userCode:String?,
        @Field("prmsessionid") sessionId:String?,
        @Field("prmdelayreason") delayReason:String?,
        @Field("prmmenucode") menuCode:String?,
        @Field("prmdeliveryboy") deliveryBoy:String?,
        @Field("prmboyid") boyId:String?,
        @Field("prmpoddt") podDt:String?,
        @Field("prmpckgs") pckgs:String?,
        @Field("prmpckgstr")pckgsStr :String?,
        @Field("prmdataidstr") dataIdStr: String ?,
        ):Call<CommonResult>


    @GET("WMS/GetPodEntryStickers")
    fun getPodStickerList(
        @Query("prmconnstring") companyId: String,
        @Query("prmusercode") userCode: String?,
        @Query("prmloginbranchcode") loginBranchCode: String?,
        @Query("prmbranchcode") branchCode: String?,
        @Query("prmgrno") grNo: String?,
        @Query("prmmenucode") menuCode: String?,
        @Query("prmsessionid") sessionId: String?
    ): Call<CommonResult>


    @FormUrlEncoded
    @POST("WMS/DrsSticker_Update")
    fun updateDrsSticker(
        @Field("prmconnstring") companyId: String,
        @Field("prmusercode")userCode:String?,
        @Field("prmloginbranchcode")loginBranchCode:String?,
        @Field("prmbranchcode")branchCode:String?,
        @Field("prmmanifestno")manifestNo:String?,
        @Field("prmdrsdt")drsDt:String?,
        @Field("prmdrstime")drsTime: String?,
        @Field("prmmodecode")modeCode:String?,
        @Field("prmvendcode")vendCode:String?,
        @Field("prmcustcode")custCode:String?,
        @Field("prmremarks")remark:String?,
        @Field("prmstickerno")stickerNo:String?,
        @Field("prmdeliveredby")deliveredBy:String?,
        @Field("prmdlvagentcode")agentCode:String?,
        @Field("prmdlvvehicleno")vehicleNo:String?,
        @Field("prmsessionid")sessionId:String?,
    ):Call<CommonResult>

    @FormUrlEncoded
    @POST("WMS/DrsSticker_Remove")
    fun removeDrsSticker(
        @Field("prmconnstring") companyId: String,
        @Field("prmusercode")userCode:String?,
        @Field("prmloginbranchcode")loginBranchCode:String?,
        @Field("prmmanifestno")manifestNo:String?,
        @Field("prmstickerno")stickerNo:String?,
        @Field("prmsessionid")sessionId:String?,
    ):Call<CommonResult>

    @GET("WMS/DrsSticker_GetStickerList")
    fun getDrsStickerList(
        @Query("prmconnstring") companyId: String,
        @Query("prmusercode")userCode:String?,
        @Query("prmloginbranchcode")loginBranchCode:String?,
        @Query("prmmanifestno")manifestNo:String?,
        @Query("prmsessionid")sessionId:String?,
    ): Call<CommonResult>

    @GET("WMS/WMSAPP_GETDRSDATA")
    fun getDrsData(
        @Query("prmconnstring") companyId: String,
        @Query("prmusercode")userCode:String?,
        @Query("prmloginbranchcode")loginBranchCode:String?,
        @Query("prmmanifestno")manifestNo:String?,
        @Query("prmsessionid")sessionId:String?,
    ): Call<CommonResult>

    @GET("WMS/pendingForDeliveryUpdate_Native")
    fun getDrsPendingList(
        @Query("prmconnstring") companyId: String,
        @Query("prmusercode")userCode:String?,
        @Query("prmloginbranchcode")loginBranchCode:String?,
        @Query("prmfromdt")fromDt:String?,
        @Query("prmtodt")toDt:String?,
        @Query("prmsessionid")sessionId:String?,
    ): Call<CommonResult>
  @FormUrlEncoded
    @POST("WMS/SaveDelivery_Scan_Stickers")
    fun updateScanDeliverySticker(
        @Field("prmconnstring") companyId: String,
        @Field("prmusercode")userCode:String?,
        @Field("prmloginbranchcode")loginBranchCode:String?,
        @Field("prmbranchcode")branchCode:String?,
        @Field("prmstickerno")stickerNo:String?,
        @Field("prmmenucode")menuCode:String?,
        @Field("prmsessionid")sessionId:String?,
    ):Call<CommonResult>

    @FormUrlEncoded
    @POST("WMS/SaveInscanDetailWithoutScan_Outstation")
    fun saveInscanDetailWithoutScan_Outstation(
        @Field("prmconnstring") companyId: String,
        @Field("prmmanifestno")manifestNo:String,
        @Field("prmmawbno")mawbNo:String,
        @Field("prmbranchcode")branchCode:String,
        @Field("prmreceivedt")receiveDt:String,
        @Field("prmreceivetime")receiveTime:String,
        @Field("prmvehiclecode")vehicleCode:String,
        @Field("prmremarks")remarks:String,
        @Field("prmgrno")grNo:String,
        @Field("prmmfpckgs")mfPckgs:String,
        @Field("prmpckgs")pckgs:String,
        @Field("prmweight")weight:String,
        @Field("prmdamagepckgs")damagePckgs:String,
        @Field("prmdamagereasoncode")damageReasoncode:String,
        @Field("prmdetailremarks")detailRemarks:String,
        @Field("prmusercode")userCode:String,
        @Field("prmmenucode")menuCode:String,
        @Field("prmsessionid")sessionId:String,
        @Field("prmfromstncode")fromstnCode:String,
    ):Call<CommonResult>

    @FormUrlEncoded
    @POST("ManifestAPI/WMSAPP_CREATEOUTSTATIONMANIFEST")
    fun UpdateOutstationManifest(
        @Field("prmconnstring") companyid:String?,
        @Field("prmbranchcode") branchcode:String?,
        @Field("prmdt") dt:String?,
        @Field("prmtime") time:String?,
        @Field("prmmanifestno")manifestno :String?,
        @Field("prmtost")tost :String?,
        @Field("prmmodetype") modetype:String?,
        @Field("prmmodecode")modecode :String?,
        @Field("prmremarks")remarks :String?,
        @Field("prmdrivercode")drivercode :String?,
        @Field("prmdrivermobileno") drivermobileno:String?,
        @Field("prmvendcode") vendcode:String?,
        @Field("prmloadingnostr")loadingnostr :String?,
        @Field("prmsessionid") sessionid:String?,
        @Field("prmusercode") usercode:String?,
        @Field("prmmenucode")menucode :String?,
        @Field("prmloadedby") loadedby:String?,
        @Field("prmairlineawbno") airlineawbno:String?,
        @Field("prmairlineawbdt") airlineawbdt:String?,
        @Field("prmairlineawbfreight") airlineawbfreight:String?,
        @Field("prmairlineawbpckgs") airlineawbpckgs:String?,
        @Field("prmairlineawbweight") airlineawbweight:String?,
        @Field("prmvendorcd")vendorcd :String?,

        ):Call<CommonResult>
    @GET("PODAPI/ValidateforPodUpload")
    fun validateGrForPOD(
        @Query("prmconnstring") companyId: String,
        @Query("prmgrno") grNo:String?
    ): Call<CommonResult>

    @FormUrlEncoded
    @POST("BookingAPI/UploadPodImage")
    fun uploadPodImage(
        @Field("prmconnstring") companyId: String?,
        @Field("prmgrno") grNo: String?,
        @Field("prmpodimage") podImageBase64: String?,
        @Field("prmsignimage") signImageBase64: String?,
        @Field("prmusercode") userCode: String?,
        @Field("prmmenucode") menuCode: String?,
        @Field("prmsessionid") sessionId: String?
    ):Call<CommonResult>

    @FormUrlEncoded
    @POST("BookingAPI/UploadBookingImage")
    fun uploadBookingImage(
        @Field("prmconnstring") companyId: String?,
        @Field("prmtransactionid") transactionId: String?,
        @Field("prmtitlename") titleName: String?,
        @Field("prmimagepath") imageInBase64: String?,
        @Field("prmusercode") userCode: String?,
        @Field("prmsessionid") sessionId: String?,
        @Field("prmmenucode") menuCode: String?,
        @Field("prmmastercode") masterCode: String?,
        @Field("prmbranchcode") branchCode: String?
    ):Call<CommonResult>

}