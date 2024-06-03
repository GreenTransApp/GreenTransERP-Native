package com.greensoft.greentranserpnative.ui.bottomsheet.departmentSelection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.bottomsheet.customerSelection.model.CustomerSelectionModel
import com.greensoft.greentranserpnative.ui.bottomsheet.departmentSelection.model.DepartmentSelectionModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class DepartmentSelectionRepository @Inject constructor():BaseRepository() {

    private val departmentMuteLiveData = MutableLiveData<ArrayList<DepartmentSelectionModel>>()

    val departmentLiveData : LiveData<ArrayList<DepartmentSelectionModel>>
        get() = departmentMuteLiveData


    fun getDepartmentList( companyId:String?,userCode:String?,sessionId:String?,custCode: String?,menuCode:String?,originCode:String?,charStr:String?) {
        viewDialogMutData.postValue(true)
        api.getDepartmentList(companyId,userCode, custCode,sessionId,menuCode,originCode,charStr).enqueue(object:
            Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<DepartmentSelectionModel>>() {}.type
                            val resultList: ArrayList<DepartmentSelectionModel> = gson.fromJson(jsonArray.toString(), listType);
                            departmentMuteLiveData.postValue(resultList);

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