package com.greensoft.greentranserpnative.ui.bottomsheet.printGR

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.bottomsheet.printGR.model.GrDetailForPrintModel
import com.greensoft.greentranserpnative.ui.operation.grList.models.PrintStickerModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class PrintGrRepository @Inject constructor(): BaseRepository() {
    private val printStickerMutData = MutableLiveData<ArrayList<PrintStickerModel>>()
    private val grDetailPrintMutData = MutableLiveData<GrDetailForPrintModel>()
    val printStickerLiveData: LiveData<ArrayList<PrintStickerModel>>
        get() = printStickerMutData

    val grDetailPrintLiveData:LiveData<GrDetailForPrintModel>
        get() = grDetailPrintMutData

    fun getStickerForPrint(companyId:String,grNo: String,fromStickerNo: String,toStickerNo:String) {
        viewDialogMutData.postValue(true)
        api.getPrintStickers(companyId,grNo, fromStickerNo,toStickerNo).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<PrintStickerModel>>() {}.type
                            val resultList: ArrayList<PrintStickerModel> = gson.fromJson(jsonArray.toString(), listType);
                            printStickerMutData.postValue(resultList);

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

    fun getGrDetailForPrint(companyId:String,userCode: String,branchCode:String,sessionId:String,grNo:String) {
        viewDialogMutData.postValue(true)
        api.getGrPrintDetail(companyId,userCode, branchCode,sessionId,grNo).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<GrDetailForPrintModel>>() {}.type
                            val resultList: ArrayList<GrDetailForPrintModel> = gson.fromJson(jsonArray.toString(), listType);
                            grDetailPrintMutData.postValue(resultList[0]);

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