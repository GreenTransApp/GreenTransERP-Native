package com.greensoft.greentranserpnative.ui.bottomsheet.customerSelection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.bottomsheet.customerSelection.model.CustomerSelectionModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class CustomerSelectionRepository @Inject constructor(): BaseRepository(){

    private val customerMuteLiveData = MutableLiveData<ArrayList<CustomerSelectionModel>>()

    val customerLiveData : LiveData<ArrayList<CustomerSelectionModel>>
        get() = customerMuteLiveData

    fun getCustomerList( companyId:String,userCode:String,branchCode:String,sessionId:String,menuCode:String,charStr:String) {
        viewDialogMutData.postValue(true)
        api.getCustomerList(companyId,userCode, branchCode,sessionId,menuCode,charStr).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<CustomerSelectionModel>>() {}.type
                            val resultList: ArrayList<CustomerSelectionModel> = gson.fromJson(jsonArray.toString(), listType);
                            customerMuteLiveData.postValue(resultList);

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