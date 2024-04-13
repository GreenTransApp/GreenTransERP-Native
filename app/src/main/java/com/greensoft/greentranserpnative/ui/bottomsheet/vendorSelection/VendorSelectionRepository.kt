package com.greensoft.greentranserpnative.ui.bottomsheet.vendorSelection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.bottomsheet.vendorSelection.model.VendorModelDRS
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class VendorSelectionRepository @Inject constructor(): BaseRepository() {

    private val vendorMuteLiveData = MutableLiveData<ArrayList<VendorModelDRS>>()

    val vendorLiveData : LiveData<ArrayList<VendorModelDRS>>
        get() = vendorMuteLiveData

    fun getVendorList( companyId:String,charStr:String,category:String) {
        viewDialogMutData.postValue(true)
        api.getVendListDRS(companyId,charStr, category).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<VendorModelDRS>>() {}.type
                            val resultList: ArrayList<VendorModelDRS> = gson.fromJson(jsonArray.toString(), listType);
                            vendorMuteLiveData.postValue(resultList);

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