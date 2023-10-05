package com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad.models.LoadingSlipHeaderDataModel
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad.models.StickerModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ScanLoadRepository @Inject constructor(): BaseRepository() {
    private val stickerListMutData = MutableLiveData<ArrayList<StickerModel>>()
    private val headerMutData = MutableLiveData<LoadingSlipHeaderDataModel>()

    val headerLivedata : LiveData<LoadingSlipHeaderDataModel>
        get() = headerMutData
    val stickerListLivedata : LiveData<ArrayList<StickerModel>>
        get() = stickerListMutData

    fun getScanSticker(companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewDialogMutData.postValue(true)
        api.commonApi(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
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
}