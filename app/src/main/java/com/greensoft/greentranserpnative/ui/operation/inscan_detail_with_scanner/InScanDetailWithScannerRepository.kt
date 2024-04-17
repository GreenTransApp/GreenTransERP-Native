package com.greensoft.greentranserpnative.ui.operation.inscan_detail_with_scanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_with_scanner.models.InScanAddStickerModel
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.model.InScanWithoutScannerModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class InScanDetailWithScannerRepository @Inject constructor(): BaseRepository() {
    private val inScanDetailMutData = MutableLiveData<InScanWithoutScannerModel>()
    private val inScanDetailsCardMutData = MutableLiveData<ArrayList<InScanWithoutScannerModel>>()
    private val insertStickerMutData = MutableLiveData<InScanAddStickerModel>()

    val inScanDetailLiveData: LiveData<InScanWithoutScannerModel>
        get() = inScanDetailMutData
    val inScanCardLiveData: LiveData<ArrayList<InScanWithoutScannerModel>>
        get() = inScanDetailsCardMutData
    val inScanAddStickerLiveData :LiveData<InScanAddStickerModel>
        get() = insertStickerMutData

    fun getInScanDetails(companyId: String, userCode: String, branchCode: String, manifestNo:String,manifestType:String,sessionId:String){
        viewDialogMutData.postValue(true)
        api.getInScanDetail(companyId,userCode, branchCode,manifestNo,manifestType,sessionId).enqueue(object:
            Callback<CommonResult> {
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

    fun addInScanSticker(companyId: String, userCode: String, branchCode: String,menuCode:String, sessionId:String,stickerNo:String,manifestNo: String,receiveDt: String, receiveTime: String){
        viewDialogMutData.postValue(true)
        api.addInScanStickerUnArrived(companyId,userCode, branchCode,menuCode,sessionId,stickerNo,manifestNo, receiveDt, receiveTime).enqueue(object:
            Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<InScanAddStickerModel>>() {}.type
                            val resultList: ArrayList<InScanAddStickerModel> = gson.fromJson(jsonArray.toString(), listType);
                            insertStickerMutData.postValue(resultList[0]);
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