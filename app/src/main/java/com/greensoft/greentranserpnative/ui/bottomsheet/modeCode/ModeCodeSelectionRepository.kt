package com.greensoft.greentranserpnative.ui.bottomsheet.modeCode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ModeCodeSelectionRepository @Inject constructor(): BaseRepository(){
    val viewDialogLiveData: LiveData<Boolean>
        get() = viewDialogMutData

    private val modeCodeMuteLiveData = MutableLiveData<ArrayList<ModeCodeSelectionModel>>()
    val modeCodeLiveData: LiveData<ArrayList<ModeCodeSelectionModel>>
        get() = modeCodeMuteLiveData


    fun getModeCodeList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>) {
        viewDialogMutData.postValue(true)
        api.commonApiWMS(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<ModeCodeSelectionModel>>() {}.type
                            val resultList: ArrayList<ModeCodeSelectionModel> = gson.fromJson(jsonArray.toString(), listType);
                            modeCodeMuteLiveData.postValue(resultList);

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