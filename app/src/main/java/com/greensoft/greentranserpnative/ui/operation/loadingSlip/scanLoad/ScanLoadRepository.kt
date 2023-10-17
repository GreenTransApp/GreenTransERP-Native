package com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ScanLoadRepository @Inject constructor(): BaseRepository() {
    private val stickerListMutData = MutableLiveData<ArrayList<StickerModel>>()
    private val headerMutData = MutableLiveData<LoadingSlipHeaderDataModel>()
    private val validateStickerMutData = MutableLiveData<ValidateStickerModel>()
    private val saveStickerMutData = MutableLiveData<SaveStickerModel>()
    private val removeStickerMutData = MutableLiveData<RemoveStickerModel>()

    val headerLivedata : LiveData<LoadingSlipHeaderDataModel>
        get() = headerMutData
    val stickerListLivedata : LiveData<ArrayList<StickerModel>>
        get() = stickerListMutData
    val validateStickerLivedata : LiveData<ValidateStickerModel>
        get() = validateStickerMutData
    val saveStickerLiveData: LiveData<SaveStickerModel>
        get() = saveStickerMutData
    val removeStickerLiveData: LiveData<RemoveStickerModel>
        get() = removeStickerMutData

    fun getScannedSticker(companyId: String, spName: String, param: ArrayList<String>, values: ArrayList<String>){
        viewDialogMutData.postValue(true)
        api.commonApi(companyId,spName, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<LoadingSlipHeaderDataModel>>() {}.type
                            val resultList: ArrayList<LoadingSlipHeaderDataModel> = gson.fromJson(jsonArray.toString(), listType);
                            headerMutData.postValue(resultList[0]);
                        }
                        val jsonArray1 = getResult1(result);
                        if(jsonArray1 != null) {
                            val listType = object: TypeToken<List<StickerModel>>() {}.type
                            val resultList: ArrayList<StickerModel> = gson.fromJson(jsonArray1.toString(), listType);
                            stickerListMutData.postValue(resultList);
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

    fun validateSticker(companyId: String, spName: String, param: ArrayList<String>, values: ArrayList<String>){
        viewDialogMutData.postValue(true)
        api.commonApi(companyId,spName, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<ValidateStickerModel>>() {}.type
                            val resultList: ArrayList<ValidateStickerModel> = gson.fromJson(jsonArray.toString(), listType);
                            validateStickerMutData.postValue(resultList[0]);
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

    fun updateSticker(
        companyId: String?, loadingNo: String?, loadingDt: String?, loadingTime: String?, stnCode: String?, branchCode: String?,
        destCode: String?, modeType: String?, vendCode: String?, modeCode: String?, loadedBy: String?, driverCode: String?, remarks: String?,
        stickerNoStr: String?, grNoStr: String?, userCode: String?, menuCode: String?, sessionId: String?, driverMobileNo: String?, despatchType: String?
    ) {
        viewDialogMutData.postValue(true)
        api.saveStickerScanLoad(companyId, loadingNo, loadingDt, loadingTime, stnCode, branchCode,
            destCode, modeType, vendCode, modeCode, loadedBy, driverCode, remarks,
            stickerNoStr, grNoStr, userCode, menuCode, sessionId, driverMobileNo, despatchType).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<SaveStickerModel>>() {}.type
                            val resultList: ArrayList<SaveStickerModel> = gson.fromJson(jsonArray.toString(), listType);
                            saveStickerMutData.postValue(resultList[0]);
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

    fun removeSticker(
        companyId: String?, stickerNo: String?, userCode: String?, menuCode: String?, sessionId: String?
    ) {
        viewDialogMutData.postValue(true)
        api.removeStickerScanLoad(companyId, stickerNo, userCode, menuCode, sessionId).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<RemoveStickerModel>>() {}.type
                            val resultList: ArrayList<RemoveStickerModel> = gson.fromJson(jsonArray.toString(), listType);
                            removeStickerMutData.postValue(resultList[0]);
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