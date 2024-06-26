package com.greensoft.greentranserpnative.ui.operation.pickup_manifest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.model.LoadingGrListModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.DestinationSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.*
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

    private val toStationMuteLiveData = MutableLiveData<ArrayList<DestinationSelectionModel>>()
     val toStationLiveData: LiveData<ArrayList<DestinationSelectionModel>>
         get() = toStationMuteLiveData

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

    private val loadingMuteLiveData = MutableLiveData<ArrayList<LoadingListModel>>()
    val loadingLiveData: LiveData<ArrayList<LoadingListModel>>
        get() = loadingMuteLiveData

    private val saveManifestMuteLiveData = MutableLiveData<SavePickupManifestModel>()
    val saveManifestLiveData: LiveData<SavePickupManifestModel>
        get() = saveManifestMuteLiveData

    private val loadingGrListMuteLiveData = MutableLiveData<ArrayList<LoadingGrListModel>>()
    val loadingGrListLiveData: LiveData<ArrayList<LoadingGrListModel>>
        get() = loadingGrListMuteLiveData


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
    fun getDestinationList(companyId: String, spname: String, param: List<String>, values: ArrayList<String>){
        api.commonApiWMS(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result)
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<DestinationSelectionModel>>() {}.type
                            val resultList: ArrayList<DestinationSelectionModel> = gson.fromJson(jsonArray.toString(), listType);
//                            destinationMuteLiveData.postValue(resultList);
                            toStationMuteLiveData.postValue(resultList);

                        }
                    } else {
                        isError.postValue(result.CommandMessage.toString());
                    }
                } else {
                    isError.postValue(SERVER_ERROR)
                }
                viewDialogMutData.postValue(false)
            }

            override fun onFailure(call: Call<CommonResult>, t: Throwable) {
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

    fun getVehicleTypeList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>) {
        viewDialogMutData.postValue(true)
        api.commonApiWMS(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<VehicleTypeModel>>() {}.type
                            val resultList: ArrayList<VehicleTypeModel> = gson.fromJson(jsonArray.toString(), listType);
                            vehicleTypeMuteLiveData.postValue(resultList);

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

    fun savePickupManifest(
        companyId:String,
        branchcode:String,
        manifestdt:String,
        time:String,
        manifestno:String,
        modecode:String,
        tost:String,
        drivercode:String,
        drivermobileno:String,
        loadedby:String,
        remarks:String,
        ispickupmanifest:String,
//        grno:String,
        loadingNo:String,
        pckgs:String,
        aweight:String,
        goods:String,
        packing:String,
        areacode:String,
        vendcoe:String,
        loadedbytype:String,
        menucode:String,
        usercode:String,
        sessionid:String,
        paymentnotapplicable:String,
        skipinscan:String,
    ) {
        viewDialogMutData.postValue(true)
        api.savePickupManifest(
            companyId,
            branchcode,
            manifestdt,
            time,
            manifestno,
            modecode,
            tost,
            drivercode,
            drivermobileno,
            loadedby,
            remarks,
            ispickupmanifest,
            loadingNo,
            pckgs,
            aweight,
            goods,
            packing,
            areacode,
            vendcoe,
            loadedbytype,
            menucode,
            usercode,
            sessionid,
            paymentnotapplicable,
            skipinscan
        )
            .enqueue(object: Callback<CommonResult> {
                override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                    if(response.body() != null){
                        val result = response.body()!!
                        val gson = Gson()
                        if(result.CommandStatus == 1){
                            val jsonArray = getResult(result);
                            if(jsonArray != null) {

                                val listType = object: TypeToken<List<SavePickupManifestModel>>() {}.type
                                val resultList: ArrayList<SavePickupManifestModel> = gson.fromJson(jsonArray.toString(), listType);
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

    fun getLoadingGrList(companyId:String, userCode:String, branchCode:String, menuCode: String, sessionId:String, loadingNo:String) {
        viewDialogMutData.postValue(true)
        api.getLoadingGrList(companyId,userCode, branchCode, menuCode, sessionId, loadingNo).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<LoadingGrListModel>>() {}.type
                            val resultList: ArrayList<LoadingGrListModel> = gson.fromJson(jsonArray.toString(), listType);
                            loadingGrListMuteLiveData.postValue(resultList);
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