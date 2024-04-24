package com.greensoft.greentranserpnative.ui.operation.despatch_manifest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.operation.despatch_manifest.models.DespatchSaveModel
import com.greensoft.greentranserpnative.ui.operation.despatch_manifest.models.GroupModeCodeModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.BranchSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.DriverSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.GrSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.LoadingListModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.PickupLocationModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.VehicleSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.VehicleTypeModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.VendorSelectionModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class DespatchManifestRepository  @Inject constructor(): BaseRepository() {
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

    private val vehicleTypeMuteLiveData = MutableLiveData<ArrayList<VehicleTypeModel>>()
    val vehicleTypeLiveData: LiveData<ArrayList<VehicleTypeModel>>
        get() = vehicleTypeMuteLiveData

    private val groupModeMuteLiveData = MutableLiveData<ArrayList<GroupModeCodeModel>>()
    val groupModeLiveData: LiveData<ArrayList<GroupModeCodeModel>>
        get() = groupModeMuteLiveData

    private val loadingMuteLiveData = MutableLiveData<ArrayList<LoadingListModel>>()
    val loadingLiveData: LiveData<ArrayList<LoadingListModel>>
        get() = loadingMuteLiveData

    private val saveManifestMuteLiveData = MutableLiveData<DespatchSaveModel>()
    val saveManifestLiveData: LiveData<DespatchSaveModel>
        get() = saveManifestMuteLiveData


    fun getBranchList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>) {
        viewDialogMutData.postValue(true)
        api.commonApiWMS(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
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
        api.commonApiWMS(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
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
        api.commonApiWMS(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
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
        api.commonApiWMS(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
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
        api.commonApiWMS(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
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
                        } else {
                            isError.postValue("No Data Found.")
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
        api.commonApiWMS(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
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

    fun getGroupModeList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>) {
        viewDialogMutData.postValue(true)
        api.commonApiWMS(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<GroupModeCodeModel>>() {}.type
                            val resultList: ArrayList<GroupModeCodeModel> = gson.fromJson(jsonArray.toString(), listType);
                            groupModeMuteLiveData.postValue(resultList);

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

    fun getLoadingList( companyId:String,userCode:String,branchCode:String,sessionId:String,mfType:String,dt:String) {
        viewDialogMutData.postValue(true)
        api.getLoadingList(companyId,userCode, branchCode,sessionId,mfType,dt).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<LoadingListModel>>() {}.type
                            val resultList: ArrayList<LoadingListModel> = gson.fromJson(jsonArray.toString(), listType);
                            loadingMuteLiveData.postValue(resultList);

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

    fun saveOutstationManifest(
        companyid:String,
        branchcode:String,
        dt:String,
        time:String,
        manifestno:String,
        tost:String,
        modetype:String,
        modecode:String,
        remarks:String,
        drivercode:String,
        drivermobileno:String,
        vendcode:String,
        loadingnostr:String,
        sessionid:String,
        usercode:String,
        menucode:String,
        loadedby:String,
        airlineawbno:String,
        airlineawbdt:String,
        airlineawbfreight:String,
        airlineawbpckgs:String,
        airlineawbweight:String,
        vendorcd:String
    ) {
        viewDialogMutData.postValue(true)
        api.UpdateOutstationManifest(
            companyid,
            branchcode,
            dt,
            time,
            manifestno,
            tost,
            modetype,
            modecode,
            remarks,
            drivercode,
            drivermobileno,
            vendcode,
            loadingnostr,
            sessionid,
            usercode,
            menucode,
            loadedby,
            airlineawbno,
            airlineawbdt,
            airlineawbfreight,
            airlineawbpckgs,
            airlineawbweight,
            vendorcd
        )
            .enqueue(object: Callback<CommonResult> {
                override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                    if(response.body() != null){
                        val result = response.body()!!
                        val gson = Gson()
                        if(result.CommandStatus == 1){
                            val jsonArray = getResult(result);
                            if(jsonArray != null) {

                                val listType = object: TypeToken<List<DespatchSaveModel>>() {}.type
                                val resultList: ArrayList<DespatchSaveModel> = gson.fromJson(jsonArray.toString(), listType);
                                saveManifestMuteLiveData.postValue(resultList[0])
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