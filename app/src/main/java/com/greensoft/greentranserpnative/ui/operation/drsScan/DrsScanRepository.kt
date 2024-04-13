package com.greensoft.greentranserpnative.ui.operation.drsScan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.operation.drsScan.model.RemoveStickerModelDRS
import com.greensoft.greentranserpnative.ui.operation.drsScan.model.ScannedDrsModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class DrsScanRepository  @Inject constructor(): BaseRepository(){
    private val updateStickerMuteLiveData = MutableLiveData<ArrayList<ScannedDrsModel>>()
    private val removeStickerMuteLiveData = MutableLiveData<ArrayList<RemoveStickerModelDRS>>()

     val updateStickerLiveData: LiveData<ArrayList<ScannedDrsModel>>
        get() = updateStickerMuteLiveData

     val removeStickerLiveData: LiveData<ArrayList<RemoveStickerModelDRS>>
        get() = removeStickerMuteLiveData


    fun updateSticker( companyId:String,userCode:String,loginBranchCode:String,branchCode:String, manifestNo:String,
                       drsDt:String,drsTime:String, modeCode:String,vendCode:String,custCode:String, remarks:String,
                       stickerNo:String,deliveredBy:String,agentCode:String,vehicleNo:String,sessionId:String) {
        viewDialogMutData.postValue(true)
        api.updateDrsSticker( companyId,userCode,loginBranchCode,branchCode,manifestNo,drsDt, drsTime,
            modeCode, vendCode,custCode, remarks,stickerNo,deliveredBy, agentCode, vehicleNo,sessionId,).enqueue(object:Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult1(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<ScannedDrsModel>>() {}.type
                            val resultList: ArrayList<ScannedDrsModel> = gson.fromJson(jsonArray.toString(), listType);
                            updateStickerMuteLiveData.postValue(resultList);

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

    fun removeSticker( companyId:String,userCode:String,loginBranchCode:String,manifestNo:String,stickerNo:String,sessionId:String) {
        viewDialogMutData.postValue(true)
        api.removeDrsSticker( companyId,userCode,loginBranchCode,manifestNo,stickerNo,sessionId,).enqueue(object:Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult1(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<RemoveStickerModelDRS>>() {}.type
                            val resultList: ArrayList<RemoveStickerModelDRS> = gson.fromJson(jsonArray.toString(), listType);
                            removeStickerMuteLiveData.postValue(resultList);

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