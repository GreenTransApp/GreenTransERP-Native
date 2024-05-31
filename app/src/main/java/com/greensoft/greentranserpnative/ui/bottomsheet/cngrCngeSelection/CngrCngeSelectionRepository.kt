package com.greensoft.greentranserpnative.ui.bottomsheet.cngrCngeSelection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.bottomsheet.cngrCngeSelection.model.CngrCngeSelectionModel
import com.greensoft.greentranserpnative.ui.bottomsheet.customerSelection.model.CustomerSelectionModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class CngrCngeSelectionRepository @Inject constructor():BaseRepository() {

    private val cngrCngeMuteLiveData=MutableLiveData<ArrayList<CngrCngeSelectionModel>>()

    val cngrCngeLiveData:LiveData<ArrayList<CngrCngeSelectionModel>>
        get() = cngrCngeMuteLiveData


    fun getCngrCngrist( companyId:String?,branchCode:String?,userCode:String?,custCode: String?,cngrCnge:String?,sessionId:String?,charStr:String?) {
        viewDialogMutData.postValue(true)
        api.getCngrCngeList(companyId,branchCode,userCode,custCode,cngrCnge, sessionId,charStr).enqueue(object:
            Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<CngrCngeSelectionModel>>() {}.type
                            val resultList: ArrayList<CngrCngeSelectionModel> = gson.fromJson(jsonArray.toString(), listType);
                            cngrCngeMuteLiveData.postValue(resultList);

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