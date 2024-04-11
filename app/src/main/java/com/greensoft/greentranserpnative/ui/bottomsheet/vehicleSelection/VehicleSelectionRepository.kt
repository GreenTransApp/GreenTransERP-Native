package com.greensoft.greentranserpnative.ui.bottomsheet.vehicleSelection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.bottomsheet.vehicleSelection.model.VehicleModelDRS
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import javax.inject.Inject

class VehicleSelectionRepository @Inject constructor(): BaseRepository(){
    private val vehicleMuteLiveData = MutableLiveData<ArrayList<VehicleModelDRS>>()

    val vehicleLiveData : LiveData<ArrayList<VehicleModelDRS>>
        get() = vehicleMuteLiveData


    fun getVehicleList( companyId:String,branchCode:String,userCode:String,charStr:String,vendCode:String, modeType:String) {
        viewDialogMutData.postValue(true)
        api.getVehicleListDRS(companyId,branchCode,userCode,charStr,vendCode,modeType).enqueue(object:
            Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<VehicleModelDRS>>() {}.type
                            val resultList: ArrayList<VehicleModelDRS> = gson.fromJson(jsonArray.toString(), listType);
                            vehicleMuteLiveData.postValue(resultList);

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