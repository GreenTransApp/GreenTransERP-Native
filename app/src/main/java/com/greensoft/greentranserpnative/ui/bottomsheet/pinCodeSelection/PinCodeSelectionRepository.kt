package com.greensoft.greentranserpnative.ui.bottomsheet.pinCodeSelection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.bottomsheet.pinCodeSelection.model.PinCodeModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class PinCodeSelectionRepository  @Inject constructor(): BaseRepository(){
    private val pinCodeMuteLiveData = MutableLiveData<ArrayList<PinCodeModel>>()

    val pinCodeLiveData : LiveData<ArrayList<PinCodeModel>>
        get() = pinCodeMuteLiveData

    fun getPinCodeList( companyId:String,branchCode:String,userCode:String,menuCode:String,sessionId:String,charStr:String) {
        viewDialogMutData.postValue(true)
        api.getPinCodeList(companyId, branchCode,userCode,menuCode,sessionId,charStr).enqueue(object:
            Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<PinCodeModel>>() {}.type
                            val resultList: ArrayList<PinCodeModel> = gson.fromJson(jsonArray.toString(), listType);
                            pinCodeMuteLiveData.postValue(resultList);

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