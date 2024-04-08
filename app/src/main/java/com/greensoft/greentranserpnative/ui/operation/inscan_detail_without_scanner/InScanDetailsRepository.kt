package com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.model.DamageReasonModel
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.model.InScanWithoutScannerModel
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.model.InscanDetailsSaveModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.SavePickupManifestModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class InScanDetailsRepository @Inject constructor(): BaseRepository(){

    private val inScanDetailMutData = MutableLiveData<InScanWithoutScannerModel>()
    private val inScanDetailsCardMutData = MutableLiveData<ArrayList<InScanWithoutScannerModel>>()
    private val damageReasonMutData = MutableLiveData<ArrayList<DamageReasonModel>>()
    private val inscanSaveMutableLiveData = MutableLiveData<InscanDetailsSaveModel>()

    val inScanDetailLiveData:LiveData<InScanWithoutScannerModel>
        get() = inScanDetailMutData
    val inScanCardLiveData: LiveData<ArrayList<InScanWithoutScannerModel>>
        get() = inScanDetailsCardMutData

    val damageReasonLiveData: LiveData<ArrayList<DamageReasonModel>>
        get() = damageReasonMutData

    val inscanSaveLiveData: LiveData<InscanDetailsSaveModel>
        get() = inscanSaveMutableLiveData

    fun getInScanDetails(companyId: String, userCode: String, branchCode: String, manifestNo:String,manifestType:String,sessionId:String){
        viewDialogMutData.postValue(true)
        api.getInScanDetail(companyId,userCode, branchCode,manifestNo,manifestType,sessionId).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<InScanWithoutScannerModel>>() {}.type
                            val resultList: ArrayList<InScanWithoutScannerModel> = gson.fromJson(jsonArray.toString(), listType);
                            inScanDetailMutData.postValue(resultList[0]);
                            inScanDetailsCardMutData.postValue(resultList);
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

    fun getDamageReasonList(companyId: String){
        viewDialogMutData.postValue(true)
        api.getDamageReasonList(companyId).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<DamageReasonModel>>() {}.type
                            val resultList: ArrayList<DamageReasonModel> = gson.fromJson(jsonArray.toString(), listType)
                            damageReasonMutData.postValue(resultList)
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


    fun saveInscanDetails(
        companyId:String,
        manifestNo:String,
        mawbNo:String,
        branchCode:String,
        receiveDt:String,
        receiveTime:String,
        vehicleCode:String,
        remarks:String,
        grNo:String,
        mfPckgs:String,
        pckgs:String,
        weight:String,
        damagePckgs:String,
        damageReasoncode:String,
        detailRemarks:String,
        userCode:String,
        menuCode:String,
        sessionId:String,
        fromstnCode:String

        ) {
        viewDialogMutData.postValue(true)
        api.saveInscanDetailWithoutScan(
            companyId,
            manifestNo,
            mawbNo,
            branchCode,
            receiveDt,
            receiveTime,
            vehicleCode,
            remarks,
            grNo,
            mfPckgs,
            pckgs,
            weight,
            damagePckgs,
            damageReasoncode,
            detailRemarks,
            userCode,
            menuCode,
            sessionId,
            fromstnCode
        )
            .enqueue(object: Callback<CommonResult> {
                override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                    if(response.body() != null){
                        val result = response.body()!!
                        val gson = Gson()
                        if(result.CommandStatus == 1){
                            val jsonArray = getResult(result);
                            if(jsonArray != null) {

                                val listType = object: TypeToken<List<InscanDetailsSaveModel>>() {}.type
                                val resultList: ArrayList<InscanDetailsSaveModel> = gson.fromJson(jsonArray.toString(), listType);
                                inscanSaveMutableLiveData.postValue(resultList[0])
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