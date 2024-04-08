package com.greensoft.greentranserpnative.ui.operation.unarrived

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.operation.unarrived.models.InscanListModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class InscanListRepository @Inject constructor(): BaseRepository(){

    private val inscanListMuteLiveData = MutableLiveData<ArrayList<InscanListModel>>()
    val inscanListLiveData: LiveData<ArrayList<InscanListModel>>
        get() = inscanListMuteLiveData


    fun getInscanList(companyId: String, userCode:String, branchCode: String,sessionId:String, fromBranchCode:String,fromDt:String,toDt:String,manifestType:String,modeType:String){
  viewDialogMutData.postValue(true)
        api.getInscanList(companyId, userCode, branchCode,sessionId, fromBranchCode, fromDt, toDt, manifestType, modeType).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<InscanListModel>>() {}.type
                            val resultList: ArrayList<InscanListModel> = gson.fromJson(jsonArray.toString(), listType);
                            inscanListMuteLiveData.postValue(resultList);
                        }
                    } else {
                        isError.postValue(result.CommandMessage.toString());
                    }
                } else {
                    isError.postValue(SERVER_ERROR);
                }
           viewDialogMutData.postValue(false)

            }

            override fun onFailure(call: Call<CommonResult?>, t: Throwable) { viewDialogMutData.postValue(false)
                isError.postValue(t.message)
            }

        })
    }

}