package com.greensoft.greentranserpnative.ui.operation.bookingWithoutIndent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.ENV
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.operation.booking.models.BoxNoValidateModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.ContentSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.GelPackItemSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.PackingSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.SaveBookingModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.TemperatureSelectionModel
import com.greensoft.greentranserpnative.ui.operation.bookingWithoutIndent.model.SaveDirectBookingModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class DirectBookingRepository @Inject constructor(): BaseRepository(){
    private val saveBookingMuteLiveData = MutableLiveData<SaveDirectBookingModel>()
    val saveBookingLiveData: LiveData<SaveDirectBookingModel>
        get() = saveBookingMuteLiveData
    private val packingMuteLiveData = MutableLiveData<ArrayList<PackingSelectionModel>>()
    val packingLiveData: LiveData<ArrayList<PackingSelectionModel>>
        get() = packingMuteLiveData

    private val contentMuteLiveData = MutableLiveData<ArrayList<ContentSelectionModel>>()
    val contentLiveData: LiveData<ArrayList<ContentSelectionModel>>
        get() = contentMuteLiveData

    private val gelPackMuteLiveData = MutableLiveData<ArrayList<GelPackItemSelectionModel>>()
    val gelPackLiveData: LiveData<ArrayList<GelPackItemSelectionModel>>
        get() = gelPackMuteLiveData

    private val boxValidateMuteLiveData = MutableLiveData<BoxNoValidateModel>()
    val boxValidateLiveData: LiveData<BoxNoValidateModel>
        get() = boxValidateMuteLiveData

    private val tempMuteLiveData = MutableLiveData<ArrayList<TemperatureSelectionModel>>()
    val tempLiveData: LiveData<ArrayList<TemperatureSelectionModel>>
        get() = tempMuteLiveData

    fun boxNoValidate(companyId: String, spname: String, param: List<String>, values: ArrayList<String>){
        viewDialogMutData.postValue(true);
        api.commonApiWMS(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult>, response: Response<CommonResult>) {
                viewDialogMutData.postValue(false);
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<BoxNoValidateModel>>() {}.type
                            val resultList: ArrayList<BoxNoValidateModel> = gson.fromJson(jsonArray.toString(), listType);
                            boxValidateMuteLiveData.postValue(resultList[0]);

                        }
                    } else {
                        isError.postValue(result.CommandMessage.toString())
                    }
                } else {
                    isError.postValue(SERVER_ERROR)
                }
                viewDialogMutData.postValue(false)
            }

            override fun onFailure(call: Call<CommonResult>, t: Throwable) {
                viewDialogMutData.postValue(false)
                isError.postValue(t.message)
            }

        })
    }

    fun getTemperatureLov(companyId: String, spname: String, param: List<String>, values: ArrayList<String>){
        api.commonApiWMS(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<TemperatureSelectionModel>>() {}.type
                            val resultList: ArrayList<TemperatureSelectionModel> = gson.fromJson(jsonArray.toString(), listType);
                            tempMuteLiveData.postValue(resultList)

                        }
                    } else {
                        isError.postValue(result.CommandMessage.toString());
                    }
                } else {
                    isError.postValue(SERVER_ERROR)
                }
                viewDialogMutData.postValue(false)
            }

            override fun onFailure(call: Call<CommonResult>, t: Throwable) {
                viewDialogMutData.postValue(false)
                isError.postValue(t.message)
            }

        })
    }

    fun getPackingLov(companyId: String, spname: String, param: List<String>, values: ArrayList<String>){
        api.commonApiWMS(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<PackingSelectionModel>>() {}.type
                            val resultList: ArrayList<PackingSelectionModel> = gson.fromJson(jsonArray.toString(), listType);
                            packingMuteLiveData.postValue(resultList);

                        }
                    } else {
                        isError.postValue(result.CommandMessage.toString())
                    }
                } else {
                    isError.postValue(SERVER_ERROR);
                }
                viewDialogMutData.postValue(false)
            }

            override fun onFailure(call: Call<CommonResult>, t: Throwable) {
                viewDialogMutData.postValue(false)
                isError.postValue(t.message)
            }

        })
    }

    fun getContentLov(companyId: String, spname: String, param: List<String>, values: ArrayList<String>){
        api.commonApiWMS(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<ContentSelectionModel>>() {}.type
                            val resultList: ArrayList<ContentSelectionModel> = gson.fromJson(jsonArray.toString(), listType);
                            contentMuteLiveData.postValue(resultList)

                        }
                    } else {
                        isError.postValue(result.CommandMessage.toString());
                    }
                } else {
                    isError.postValue(SERVER_ERROR);
                }
                viewDialogMutData.postValue(false)
            }

            override fun onFailure(call: Call<CommonResult>, t: Throwable) {
                viewDialogMutData.postValue(false)
                isError.postValue(t.message)
            }

        })
    }

    fun getGelPackLov(companyId: String, spname: String, param: List<String>, values: ArrayList<String>) {
        api.commonApiWMS(companyId, spname, param, values).enqueue(object : Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult>, response: Response<CommonResult>) {
                if (response.body() != null) {
                    val result = response.body()!!
                    val gson = Gson()
                    if (result.CommandStatus == 1) {
                        val jsonArray = getResult(result);
                        if (jsonArray != null) {
                            val listType =
                                object : TypeToken<List<GelPackItemSelectionModel>>() {}.type
                            val resultList: ArrayList<GelPackItemSelectionModel> =
                                gson.fromJson(jsonArray.toString(), listType);
                            gelPackMuteLiveData.postValue(resultList)

                        }
                    } else {
                        isError.postValue(result.CommandMessage.toString());
                    }
                } else {
                    isError.postValue(SERVER_ERROR);
                }
                viewDialogMutData.postValue(false)
            }

            override fun onFailure(call: Call<CommonResult>, t: Throwable) {
                viewDialogMutData.postValue(false)
                isError.postValue(t.message)
            }

        })


    }


    fun saveDirectBooking(companyId:String,
                          branchcode :String ,
                          bookingdt :String ,
                          time :String ,
                          egrno :String ,
                          custcode :String ,
                          destcode :String ,
                          productcode :String ,
                          pckgs :Int ,
                          aweight :String ,
                          vweight :String ,
                          cweight :String ,
                          createid :String ,
                          sessionid :String ,
                          refno :String ,
                          cngr :String ,
                          cngraddress :String ,
                          cngrcity :String ,
                          cngrzipcode :String ,
                          cngrstate :String ,
                          cngrmobileno :String ,
                          cngremailid :String ,
                          cngrCSTNo :String ,
                          cngrLSTNo :String ,
                          cngrTINNo :String,
                          cngrSTaxRegNo :String ,
                          cnge :String ,
                          cngeaddress :String ,
                          cngecity :String ,
                          cngezipcode :String ,
                          cngestate :String ,
                          cngemobileno :String ,
                          cngeemailid :String ,
                          cngeCSTNo :String ,
                          cngeLSTNo :String ,
                          cngeTINNo :String ,
                          cngeSTaxRegNo :String ,
                          transactionid :Int ,
                          mawbchargeapplicable :String ,
                          custdeptid :String ,
                          referencenostr :String ,
                          weightstr :String ,
                          packagetypestr :String ,
                          tempuraturestr :String ,
                          packingstr :String ,
                          goodsstr :String ,
                          dryicestr :String ,
                          dryiceqtystr :String ,
                          dataloggerstr :String ,
                          dataloggernostr :String ,
                          dimlength :String ,
                          dimbreath :String ,
                          dimheight :String ,
                          pickupboyname :String ,
                          boyid :Int ,
                          boxnostr :String ,
                          stockitemcodestr :String ,
                          gelpackstr :String ,
                          gelpackitemcodestr :String ,
                          gelpackqtystr :String ,
                          menucode :String ,
                          invoicenostr :String ,
                          invoicedtstr :String ,
                          invoicevaluestr :String ,
                          ewaybillnostr :String ,
                          ewaybilldtstr :String ,
                          ewbvaliduptodtstr :String ,
                          vendorcode :String ,
                          pckgsstr :String ,
                          pickupby :String ,
                          vehicleno :String ,
                          vweightstr :String ,
                          vehiclecode :String ,
                          cngrcode :String ,
                          cngecode :String ,
                          remarks :String ,
                          cngrgstno :String ,
                          cngegstno :String ,
                          cngrtelno :String ,
                          cngetelno :String ,
                          orgpincode :String ,
                          destpincode :String ,
                          pickuppoint :String ,
                          deliverypoint :String )
    {
        viewDialogMutData.postValue(true)

        if(ENV.DEBUGGING) {
            val jsonString = """
        {
            "companyId": "$companyId",
            "branchcode": "$branchcode",
            "bookingdt": "$bookingdt",
            "time": "$time",
            "egrno": "$egrno",
            "custcode": "$custcode",
            "destcode": "$destcode",
            "productcode": "$productcode",
            "pckgs": $pckgs,
            "aweight": "$aweight",
            "vweight": "$vweight",
            "cweight": "$cweight",
            "createid": "$createid",
            "sessionid": "$sessionid",
            "refno": "$refno",
            "cngr": "$cngr",
            "cngraddress": "$cngraddress",
            "cngrcity": "$cngrcity",
            "cngrzipcode": "$cngrzipcode",
            "cngrstate": "$cngrstate",
            "cngrmobileno": "$cngrmobileno",
            "cngremailid": "$cngremailid",
            "cngrCSTNo": "$cngrCSTNo",
            "cngrLSTNo": "$cngrLSTNo",
            "cngrTINNo": "$cngrTINNo",
            "cngrSTaxRegNo": "$cngrSTaxRegNo",
            "cnge": "$cnge",
            "cngeaddress": "$cngeaddress",
            "cngecity": "$cngecity",
            "cngezipcode": "$cngezipcode",
            "cngestate": "$cngestate",
            "cngemobileno": "$cngemobileno",
            "cngeemailid": "$cngeemailid",
            "cngeCSTNo": "$cngeCSTNo",
            "cngeLSTNo": "$cngeLSTNo",
            "cngeTINNo": "$cngeTINNo",
            "cngeSTaxRegNo": "$cngeSTaxRegNo",
            "transactionid": $transactionid,
            "mawbchargeapplicable": "$mawbchargeapplicable",
            "custdeptid": $custdeptid,
            "referencenostr": "$referencenostr",
            "weightstr": "$weightstr",
            "packagetypestr": "$packagetypestr",
            "tempuraturestr": "$tempuraturestr",
            "packingstr": "$packingstr",
            "goodsstr": "$goodsstr",
            "dryicestr": "$dryicestr",
            "dryiceqtystr": "$dryiceqtystr",
            "dataloggerstr": "$dataloggerstr",
            "dataloggernostr": "$dataloggernostr",
            "dimlength": "$dimlength",
            "dimbreath": "$dimbreath",
            "dimheight": "$dimheight",
            "pickupboyname": "$pickupboyname",
            "boyid": $boyid,
            "boxnostr": "$boxnostr",
            "stockitemcodestr": "$stockitemcodestr",
            "gelpackstr": "$gelpackstr",
            "gelpackitemcodestr": "$gelpackitemcodestr",
            "gelpackqtystr": "$gelpackqtystr",
            "menucode": "$menucode",
            "invoicenostr": "$invoicenostr",
            "invoicedtstr": "$invoicedtstr",
            "invoicevaluestr": "$invoicevaluestr",
            "ewaybillnostr": "$ewaybillnostr",
            "ewaybilldtstr": "$ewaybilldtstr",
            "ewbvaliduptodtstr": "$ewbvaliduptodtstr",
            "vendorcode": "$vendorcode",
            "pckgsstr": "$pckgsstr",
            "pickupby": "$pickupby",
            "vehicleno": "$vehicleno",
            "vweightstr": "$vweightstr",
            "vehiclecode": "$vehiclecode",
            "cngrcode": "$cngrcode",
            "cngecode": "$cngecode",
            "remarks": "$remarks",
            "cngrgstno": "$cngrgstno",
            "cngegstno": "$cngegstno",
            "cngrtelno": "$cngrtelno",
            "cngetelno": "$cngetelno",
            "orgpincode": "$orgpincode",
            "destpincode": "$destpincode",
            "pickuppoint": "$pickuppoint",
            "deliverypoint": "$deliverypoint"
        }
    """.trimIndent()

            Log.d("SaveDirectBooking Params", jsonString)
            return
        }
        api.SaveDirectBooking(
            companyId,
            branchcode ,
            bookingdt ,
            time ,
            egrno ,
            custcode ,
            destcode ,
            productcode ,
            pckgs ,
            aweight ,
            vweight ,
            cweight ,
            createid ,
            sessionid ,
            refno ,
            cngr ,
            cngraddress ,
            cngrcity ,
            cngrzipcode ,
            cngrstate ,
            cngrmobileno ,
            cngremailid ,
            cngrCSTNo ,
            cngrLSTNo ,
            cngrTINNo ,
            cngrSTaxRegNo ,
            cnge ,
            cngeaddress ,
            cngecity ,
            cngezipcode ,
            cngestate ,
            cngemobileno ,
            cngeemailid ,
            cngeCSTNo ,
            cngeLSTNo ,
            cngeTINNo ,
            cngeSTaxRegNo ,
            transactionid ,
            mawbchargeapplicable ,
            custdeptid ,
            referencenostr ,
            weightstr ,
            packagetypestr ,
            tempuraturestr ,
            packingstr ,
            goodsstr ,
            dryicestr ,
            dryiceqtystr ,
            dataloggerstr ,
            dataloggernostr ,
            dimlength ,
            dimbreath ,
            dimheight ,
            pickupboyname ,
            boyid ,
            boxnostr ,
            stockitemcodestr ,
            gelpackstr ,
            gelpackitemcodestr ,
            gelpackqtystr ,
            menucode ,
            invoicenostr ,
            invoicedtstr ,
            invoicevaluestr ,
            ewaybillnostr ,
            ewaybilldtstr ,
            ewbvaliduptodtstr ,
            vendorcode ,
            pckgsstr ,
            pickupby ,
            vehicleno ,
            vweightstr ,
            vehiclecode ,
            cngrcode ,
            cngecode ,
            remarks ,
            cngrgstno ,
            cngegstno ,
            cngrtelno ,
            cngetelno ,
            orgpincode ,
            destpincode ,
            pickuppoint ,
            deliverypoint
        )
            .enqueue(object: Callback<CommonResult> {
                override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                    if(response.body() != null){
                        val result = response.body()!!
                        val gson = Gson()
                        if(result.CommandStatus == 1){
                            val jsonArray = getResult(result);
                            if(jsonArray != null) {
                                val listType = object: TypeToken<List<SaveDirectBookingModel>>() {}.type
                                val resultList: ArrayList<SaveDirectBookingModel> = gson.fromJson(jsonArray.toString(), listType);
                                saveBookingMuteLiveData.postValue(resultList[0])
                            }
                        } else {
                            isError.postValue(result.CommandMessage.toString());
                        }
                    } else {
                        isError.postValue(SERVER_ERROR);
                    }
                    viewDialogMutData.postValue(false)

                }

                override fun onFailure(call: Call<CommonResult?>, t: Throwable) {
                    viewDialogMutData.postValue(false)
                    isError.postValue(t.message)
                }

            })

    }

}