package com.greensoft.greentranserpnative.ui.operation.grList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.operation.grList.models.GrListModel
import com.greensoft.greentranserpnative.ui.operation.grList.models.PrintStickerModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class GrListRepository @Inject constructor() : BaseRepository() {
    private val grListMutData = MutableLiveData<ArrayList<GrListModel>>()
    private val printStickerMutData = MutableLiveData<ArrayList<PrintStickerModel>>()

    val grListLivedata :LiveData<ArrayList<GrListModel>>
        get() = grListMutData
    val printStickerLiveData: LiveData<ArrayList<PrintStickerModel>>
        get() = printStickerMutData
    val viewDialogLiveData: LiveData<Boolean>
        get() = viewDialogMutData

    fun getGrList(companyId:String,spname: String,param:List<String>, values:ArrayList<String>) {
        viewDialogMutData.postValue(true)
        api.commonApi(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<GrListModel>>() {}.type
                            val resultList: ArrayList<GrListModel> = gson.fromJson(jsonArray.toString(), listType);
                            grListMutData.postValue(resultList);

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

    fun getStickerForPrint(companyId:String,spname: String,param:List<String>, values:ArrayList<String>) {
        viewDialogMutData.postValue(true)
        api.commonApi(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
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
}
