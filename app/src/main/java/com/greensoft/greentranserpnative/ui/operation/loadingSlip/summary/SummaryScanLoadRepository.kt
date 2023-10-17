package com.greensoft.greentranserpnative.ui.operation.loadingSlip.summary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.summary.models.SaveLoadingCompleteModel
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.summary.models.SummaryScanLoadModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class SummaryScanLoadRepository @Inject constructor() : BaseRepository() {
    var viewDialogLiveData: LiveData<Boolean> = viewDialogMutData
    private val summaryListMutData = MutableLiveData<ArrayList<SummaryScanLoadModel>>()
    private val saveLoadingCompleteMutData = MutableLiveData<SaveLoadingCompleteModel>()
    var summaryListLiveData: LiveData<ArrayList<SummaryScanLoadModel>> = summaryListMutData
    var saveLoadingCompleteLiveData: LiveData<SaveLoadingCompleteModel> = saveLoadingCompleteMutData
    fun getSummaryForLoading(
        companyId: String?, branchCode: String?, loadingNo: String?, closeLoading: String?,
        userCode: String?, menuCode: String?, sessionId: String?
    ) {
        viewDialogMutData.postValue(true)
        api.scanLoadSummary(
            companyId,
            branchCode,
            loadingNo,
            closeLoading,
            userCode,
            menuCode,
            sessionId
        ).enqueue(object : Callback<CommonResult?> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult?>) {
                viewDialogMutData.postValue(false)
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        if (result.CommandStatus == 1) {
                            val jsonArray = getResult(result)
                            if (jsonArray != null) {
                                val listType =
                                    object : TypeToken<List<SummaryScanLoadModel?>?>() {}.type
                                val gson = Gson()
                                val resultList = gson.fromJson<ArrayList<SummaryScanLoadModel>>(
                                    jsonArray.toString(),
                                    listType
                                )
                                summaryListMutData.postValue(resultList)
                            }
                        } else {
                            isError.postValue(result.CommandMessage.toString())
                        }
                    } else {
                        isError.postValue(SERVER_ERROR)
                    }
                }
            }

            override fun onFailure(call: Call<CommonResult?>, t: Throwable) {
                viewDialogMutData.postValue(false)
                isError.postValue(t.message)
            }
        })
    }

    fun saveLoadingComplete(
        companyId: String?, branchCode: String?, loadingNo: String?, closeLoading: String?,
        userCode: String?, menuCode: String?, sessionId: String?
    ) {
        viewDialogMutData.postValue(true)
        api.scanLoadSummary(
            companyId,
            branchCode,
            loadingNo,
            closeLoading,
            userCode,
            menuCode,
            sessionId
        ).enqueue(object : Callback<CommonResult?> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult?>) {
                viewDialogMutData.postValue(false)
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        if (result.CommandStatus == 1) {
                            val jsonArray = getResult(result)
                            if (jsonArray != null) {
                                val listType =
                                    object : TypeToken<List<SaveLoadingCompleteModel?>?>() {}.type
                                val gson = Gson()
                                val resultList = gson.fromJson<ArrayList<SaveLoadingCompleteModel>>(
                                    jsonArray.toString(),
                                    listType
                                )
                                saveLoadingCompleteMutData.postValue(resultList[0])
                            }
                        } else {
                            isError.postValue(result.CommandMessage.toString())
                        }
                    } else {
                        isError.postValue(SERVER_ERROR)
                    }
                }
            }

            override fun onFailure(call: Call<CommonResult?>, t: Throwable) {
                viewDialogMutData.postValue(false)
                isError.postValue(t.message)
            }
        })
    }
}