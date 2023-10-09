package com.greensoft.greentranserpnative.ui.operation.pickup_manifest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.operation.booking.models.CustomerSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.gr_select.models.GrSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.BranchSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.DriverSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.PickupLocationModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.VehicleSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.VendorSelectionModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class PickupManifestRepository @Inject constructor(): BaseRepository(){
    val viewDialogLiveData: LiveData<Boolean>
        get() = viewDialogMutData

    private val branchMuteLiveData = MutableLiveData<ArrayList<BranchSelectionModel>>()
    val branchLiveData: LiveData<ArrayList<BranchSelectionModel>>
        get() = branchMuteLiveData

    private val pickupLocationMuteLiveData = MutableLiveData<ArrayList<PickupLocationModel>>()
    val pickupLocationLiveData: LiveData<ArrayList<PickupLocationModel>>
        get() = pickupLocationMuteLiveData

    private val driverMuteLiveData = MutableLiveData<ArrayList<DriverSelectionModel>>()
    val driverLiveData: LiveData<ArrayList<DriverSelectionModel>>
        get() = driverMuteLiveData

    private val vendorMuteLiveData = MutableLiveData<ArrayList<VendorSelectionModel>>()
    val vendorLiveData: LiveData<ArrayList<VendorSelectionModel>>
        get() = vendorMuteLiveData

    private val vehicleMuteLiveData = MutableLiveData<ArrayList<VehicleSelectionModel>>()
    val vehicleLiveData: LiveData<ArrayList<VehicleSelectionModel>>
        get() = vehicleMuteLiveData

    private val grMuteLiveData = MutableLiveData<ArrayList<GrSelectionModel>>()
    val grLiveData: LiveData<ArrayList<GrSelectionModel>>
        get() = grMuteLiveData


    fun getBranchList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>) {
        viewDialogMutData.postValue(true)
        api.commonApi(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<BranchSelectionModel>>() {}.type
                            val resultList: ArrayList<BranchSelectionModel> = gson.fromJson(jsonArray.toString(), listType);
                            branchMuteLiveData.postValue(resultList);

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

    fun getPickupLocation( companyId:String,spname: String,param:List<String>, values:ArrayList<String>) {
        viewDialogMutData.postValue(true)
        api.commonApi(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<PickupLocationModel>>() {}.type
                            val resultList: ArrayList<PickupLocationModel> = gson.fromJson(jsonArray.toString(), listType);
                            pickupLocationMuteLiveData.postValue(resultList);

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
 fun getDriverList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>) {
        viewDialogMutData.postValue(true)
        api.commonApi(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<DriverSelectionModel>>() {}.type
                            val resultList: ArrayList<DriverSelectionModel> = gson.fromJson(jsonArray.toString(), listType);
                            driverMuteLiveData.postValue(resultList);

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

    fun getVendorList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>) {
        viewDialogMutData.postValue(true)
        api.commonApi(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<VendorSelectionModel>>() {}.type
                            val resultList: ArrayList<VendorSelectionModel> = gson.fromJson(jsonArray.toString(), listType);
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

    fun getVehicleList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>) {
        viewDialogMutData.postValue(true)
        api.commonApi(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<VehicleSelectionModel>>() {}.type
                            val resultList: ArrayList<VehicleSelectionModel> = gson.fromJson(jsonArray.toString(), listType);
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
    fun getGrList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>) {
        viewDialogMutData.postValue(true)
        api.commonApi(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<GrSelectionModel>>() {}.type
                            val resultList: ArrayList<GrSelectionModel> = gson.fromJson(jsonArray.toString(), listType);
                            grMuteLiveData.postValue(resultList);

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