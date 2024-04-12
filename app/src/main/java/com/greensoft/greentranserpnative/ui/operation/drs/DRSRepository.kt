package com.greensoft.greentranserpnative.ui.operation.drs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.bottomsheet.vehicleSelection.model.VehicleModelDRS
import com.greensoft.greentranserpnative.ui.operation.drs.model.SaveDRSModel
import com.greensoft.greentranserpnative.ui.operation.drs.model.VendorModelDRS
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class DRSRepository @Inject constructor(): BaseRepository() {

    private val vendorMuteLiveData = MutableLiveData<ArrayList<VendorModelDRS>>()
    private val vehicleMuteLiveData = MutableLiveData<ArrayList<VehicleModelDRS>>()
    private val saveDRSMuteLiveData = MutableLiveData<SaveDRSModel>()


    val vendorLiveData: LiveData<ArrayList<VendorModelDRS>>
        get() = vendorMuteLiveData

    val vehicleLiveData : LiveData<ArrayList<VehicleModelDRS>>
        get() = vehicleMuteLiveData

    val saveDRSLiveData: LiveData<SaveDRSModel>
        get() = saveDRSMuteLiveData

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
    fun getVehicleList( companyId:String,branchCode:String,userCode:String,charStr:String,vendCode:String, modeType:String) {
        viewDialogMutData.postValue(true)
        api.getVehicleListDRS(companyId,branchCode,userCode,charStr,vendCode,modeType).enqueue(object: Callback<CommonResult> {
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


    fun saveDRS( companyId:String,branchCode:String,drsDt:String,drsTime:String, drsSno:String,
                 driverName:String, driverTele:String, modeCode:String, remarks:String,grnoStr:String,
                 drnoStr:String,  pckgsStr:String,weightStr:String,remarksStr:String,loadedBy:String,
                 userCode:String,sessionId:String,menuCode:String,executiveId:String,deliveredBy:String,
                 dlvAgentCode:String,dlvVehicleNo:String) {
        viewDialogMutData.postValue(true)
        api.saveDRS( companyId,branchCode,drsDt, drsTime, drsSno,driverName,driverTele,modeCode, remarks,
            grnoStr, drnoStr, pckgsStr, weightStr, remarksStr, loadedBy, userCode, sessionId, menuCode,
            executiveId, deliveredBy, dlvAgentCode, dlvVehicleNo,).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<SaveDRSModel>>() {}.type
                            val resultList: ArrayList<SaveDRSModel> = gson.fromJson(jsonArray.toString(), listType);
                            saveDRSMuteLiveData.postValue(resultList[0]);

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