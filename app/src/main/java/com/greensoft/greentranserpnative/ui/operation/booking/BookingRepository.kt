package com.greensoft.greentranserpnative.ui.operation.booking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.login.models.UserDataModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.AgentSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.BookingVehicleSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.ConsignorSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.ContentSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.CustomerSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.DepartmentSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.DestinationSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.GelPackItemSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.OriginSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.PckgTypeSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.PackingSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.PickupBoySelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.PickupBySelection
import com.greensoft.greentranserpnative.ui.operation.booking.models.SaveBookingModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.ServiceTypeSelectionLov
import com.greensoft.greentranserpnative.ui.operation.booking.models.TemperatureSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.models.SinglePickupRefModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class BookingRepository @Inject constructor():BaseRepository(){

    private val custMuteLiveData = MutableLiveData<ArrayList<CustomerSelectionModel>>()
    val custLiveData: LiveData<ArrayList<CustomerSelectionModel>>
        get() = custMuteLiveData

    private val cngrMuteLiveData = MutableLiveData<ArrayList<ConsignorSelectionModel>>()
    val cngrLiveData: LiveData<ArrayList<ConsignorSelectionModel>>
        get() = cngrMuteLiveData

    private val deptMuteLiveData = MutableLiveData<ArrayList<DepartmentSelectionModel>>()
    val deptLiveData: LiveData<ArrayList<DepartmentSelectionModel>>
        get() = deptMuteLiveData

    private val originMuteLiveData = MutableLiveData<ArrayList<OriginSelectionModel>>()
    val originLiveData: LiveData<ArrayList<OriginSelectionModel>>
        get() = originMuteLiveData

    private val destinationMuteLiveData = MutableLiveData<ArrayList<DestinationSelectionModel>>()
    val destinationLiveData: LiveData<ArrayList<DestinationSelectionModel>>
        get() = destinationMuteLiveData

    private val pickupBoyMuteLiveData = MutableLiveData<ArrayList<PickupBoySelectionModel>>()
    val pickupBoyLiveData: LiveData<ArrayList<PickupBoySelectionModel>>
        get() = pickupBoyMuteLiveData

    private val tempMuteLiveData = MutableLiveData<ArrayList<TemperatureSelectionModel>>()
    val tempLiveData: LiveData<ArrayList<TemperatureSelectionModel>>
        get() = tempMuteLiveData


    private val packingMuteLiveData = MutableLiveData<ArrayList<PackingSelectionModel>>()
    val packingLiveData: LiveData<ArrayList<PackingSelectionModel>>
        get() = packingMuteLiveData

    private val contentMuteLiveData = MutableLiveData<ArrayList<ContentSelectionModel>>()
    val contentLiveData: LiveData<ArrayList<ContentSelectionModel>>
        get() = contentMuteLiveData

    private val gelPackMuteLiveData = MutableLiveData<ArrayList<GelPackItemSelectionModel>>()
    val gelPackLiveData: LiveData<ArrayList<GelPackItemSelectionModel>>
        get() = gelPackMuteLiveData

    private val agentMuteLiveData = MutableLiveData<ArrayList<AgentSelectionModel>>()
    val agentLiveData: LiveData<ArrayList<AgentSelectionModel>>
        get() = agentMuteLiveData

    private val vendorMuteLiveData = MutableLiveData<ArrayList<AgentSelectionModel>>()
    val vendorLiveData: LiveData<ArrayList<AgentSelectionModel>>
        get() = agentMuteLiveData

    private val vehicleMuteLiveData = MutableLiveData<ArrayList<BookingVehicleSelectionModel>>()
    val vehicleLiveData: LiveData<ArrayList<BookingVehicleSelectionModel>>
        get() = vehicleMuteLiveData


    private val pickupByMuteLiveData = MutableLiveData<ArrayList<PickupBySelection>>()
    val pickupByLiveData: LiveData<ArrayList<PickupBySelection>>
        get() = pickupByMuteLiveData


    private val serviceListMuteLiveData = MutableLiveData<ArrayList<ServiceTypeSelectionLov>>()
    val serviceListLiveData: LiveData<ArrayList<ServiceTypeSelectionLov>>
        get() = serviceListMuteLiveData

    private val singleRefMuteLiveData = MutableLiveData<ArrayList<SinglePickupRefModel>>()
    val singleRefLiveData: LiveData<ArrayList<SinglePickupRefModel>>
        get() = singleRefMuteLiveData

    private val pckgTypeMuteLiveData = MutableLiveData<ArrayList<PckgTypeSelectionModel>>()
    val pckgTypeLiveData: LiveData<ArrayList<PckgTypeSelectionModel>>
        get() = pckgTypeMuteLiveData

    private val saveBookingMuteLiveData = MutableLiveData<SaveBookingModel>()
    val saveBookingLiveData: LiveData<SaveBookingModel>
        get() = saveBookingMuteLiveData



    fun getCustomerList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>) {
        viewDialogMutData.postValue(true)
        api.commonApi(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<CustomerSelectionModel>>() {}.type
                            val resultList: ArrayList<CustomerSelectionModel> = gson.fromJson(jsonArray.toString(), listType);
                            custMuteLiveData.postValue(resultList);

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


    fun getConsignorDetails(companyId: String, spname: String, param: List<String>, values: ArrayList<String>){
        api.commonApi(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<ConsignorSelectionModel>>() {}.type
                            val resultList: ArrayList<ConsignorSelectionModel> = gson.fromJson(jsonArray.toString(), listType);
                            cngrMuteLiveData.postValue(resultList);

                        }
                    } else {
                        isError.postValue(result.CommandMessage.toString())
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


    fun getDepartmentDetails(companyId: String, spname: String, param: List<String>, values: ArrayList<String>){
        api.commonApi(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<DepartmentSelectionModel>>() {}.type
                            val resultList: ArrayList<DepartmentSelectionModel> = gson.fromJson(jsonArray.toString(), listType);
                            deptMuteLiveData.postValue(resultList);

                        }
                    } else {
                        isError.postValue(result.CommandMessage.toString())
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

    fun getOriginData(companyId: String, spname: String, param: List<String>, values: ArrayList<String>){
        api.commonApi(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<OriginSelectionModel>>() {}.type
                            val resultList: ArrayList<OriginSelectionModel> = gson.fromJson(jsonArray.toString(), listType);
                            originMuteLiveData.postValue(resultList)

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

    fun getDestinationData(companyId: String, spname: String, param: List<String>, values: ArrayList<String>){
        api.commonApi(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result)
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<DestinationSelectionModel>>() {}.type
                            val resultList: ArrayList<DestinationSelectionModel> = gson.fromJson(jsonArray.toString(), listType);
                            destinationMuteLiveData.postValue(resultList);

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

    fun getPickupBoyDetail(companyId: String, spname: String, param: List<String>, values: ArrayList<String>){
        api.commonApi(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<PickupBoySelectionModel>>() {}.type
                            val resultList: ArrayList<PickupBoySelectionModel> = gson.fromJson(jsonArray.toString(), listType);
                            pickupBoyMuteLiveData.postValue(resultList);

                        }
                    } else {
                        isError.postValue(result.CommandMessage.toString())
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

    fun getTemperatureLov(companyId: String, spname: String, param: List<String>, values: ArrayList<String>){
        api.commonApi(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<TemperatureSelectionModel>>() {}.type
                            val resultList: ArrayList<TemperatureSelectionModel> = gson.fromJson(jsonArray.toString(), listType);
                            tempMuteLiveData.postValue(resultList)

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

    fun getPackingLov(companyId: String, spname: String, param: List<String>, values: ArrayList<String>){
        api.commonApi(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<PackingSelectionModel>>() {}.type
                            val resultList: ArrayList<PackingSelectionModel> = gson.fromJson(jsonArray.toString(), listType);
                            packingMuteLiveData.postValue(resultList);

                        }
                    } else {
                        isError.postValue(result.CommandMessage.toString())
                    }
                } else {
                    isError.postValue(SERVER_ERROR);
                }
                viewDialogMutData.postValue(false)
            }

            override fun onFailure(call: Call<CommonResult>, t: Throwable) {
                viewDialogMutData.postValue(false)
                isError.postValue(t.message)
            }

        })
    }

    fun getContentLov(companyId: String, spname: String, param: List<String>, values: ArrayList<String>){
        api.commonApi(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<ContentSelectionModel>>() {}.type
                            val resultList: ArrayList<ContentSelectionModel> = gson.fromJson(jsonArray.toString(), listType);
                            contentMuteLiveData.postValue(resultList)

                        }
                    } else {
                        isError.postValue(result.CommandMessage.toString());
                    }
                } else {
                    isError.postValue(SERVER_ERROR);
                }
                viewDialogMutData.postValue(false)
            }

            override fun onFailure(call: Call<CommonResult>, t: Throwable) {
                viewDialogMutData.postValue(false)
                isError.postValue(t.message)
            }

        })
    }

    fun getGelPackLov(companyId: String, spname: String, param: List<String>, values: ArrayList<String>) {
        api.commonApi(companyId, spname, param, values).enqueue(object : Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult>, response: Response<CommonResult>) {
                if (response.body() != null) {
                    val result = response.body()!!
                    val gson = Gson()
                    if (result.CommandStatus == 1) {
                        val jsonArray = getResult(result);
                        if (jsonArray != null) {
                            val listType =
                                object : TypeToken<List<GelPackItemSelectionModel>>() {}.type
                            val resultList: ArrayList<GelPackItemSelectionModel> =
                                gson.fromJson(jsonArray.toString(), listType);
                            gelPackMuteLiveData.postValue(resultList)

                        }
                    } else {
                        isError.postValue(result.CommandMessage.toString());
                    }
                } else {
                    isError.postValue(SERVER_ERROR);
                }
                viewDialogMutData.postValue(false)
            }

            override fun onFailure(call: Call<CommonResult>, t: Throwable) {
                viewDialogMutData.postValue(false)
                isError.postValue(t.message)
            }

        })


    }

    fun getAgentLov(companyId: String, spname: String, param: List<String>, values: ArrayList<String>) {
        api.commonApi(companyId, spname, param, values).enqueue(object : Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult>, response: Response<CommonResult>) {
                if (response.body() != null) {
                    val result = response.body()!!
                    val gson = Gson()
                    if (result.CommandStatus == 1) {
                        val jsonArray = getResult(result);
                        if (jsonArray != null) {
                            val listType =
                                object : TypeToken<List<AgentSelectionModel>>() {}.type
                            val resultList: ArrayList<AgentSelectionModel> =
                                gson.fromJson(jsonArray.toString(), listType);
                            agentMuteLiveData.postValue(resultList)

                        }
                    } else {
                        isError.postValue(result.CommandMessage.toString());
                    }
                } else {
                    isError.postValue(SERVER_ERROR);
                }
                viewDialogMutData.postValue(false)
            }

            override fun onFailure(call: Call<CommonResult>, t: Throwable) {
                viewDialogMutData.postValue(false)
                isError.postValue(t.message)
            }

        })


    }

    fun getVendorLov(companyId: String, spname: String, param: List<String>, values: ArrayList<String>) {
        api.commonApi(companyId, spname, param, values).enqueue(object : Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult>, response: Response<CommonResult>) {
                if (response.body() != null) {
                    val result = response.body()!!
                    val gson = Gson()
                    if (result.CommandStatus == 1) {
                        val jsonArray = getResult(result);
                        if (jsonArray != null) {
                            val listType =
                                object : TypeToken<List<AgentSelectionModel>>() {}.type
                            val resultList: ArrayList<AgentSelectionModel> =
                                gson.fromJson(jsonArray.toString(), listType);
                            vendorMuteLiveData.postValue(resultList)

                        }
                    } else {
                        isError.postValue(result.CommandMessage.toString());
                    }
                } else {
                    isError.postValue(SERVER_ERROR);
                }
                viewDialogMutData.postValue(false)
            }

            override fun onFailure(call: Call<CommonResult>, t: Throwable) {
                viewDialogMutData.postValue(false)
                isError.postValue(t.message)
            }

        })


    }

    fun getVehicleLov(companyId: String, spname: String, param: List<String>, values: ArrayList<String>) {
        api.commonApi(companyId, spname, param, values).enqueue(object : Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult>, response: Response<CommonResult>) {
                if (response.body() != null) {
                    val result = response.body()!!
                    val gson = Gson()
                    if (result.CommandStatus == 1) {
                        val jsonArray = getResult(result);
                        if (jsonArray != null) {
                            val listType =
                                object : TypeToken<List<BookingVehicleSelectionModel>>() {}.type
                            val resultList: ArrayList<BookingVehicleSelectionModel> =
                                gson.fromJson(jsonArray.toString(), listType);
                            vehicleMuteLiveData.postValue(resultList)

                        }
                    } else {
                        isError.postValue(result.CommandMessage.toString());
                    }
                } else {
                    isError.postValue(SERVER_ERROR);
                }
                viewDialogMutData.postValue(false)
            }

            override fun onFailure(call: Call<CommonResult>, t: Throwable) {
                viewDialogMutData.postValue(false)
                isError.postValue(t.message)
            }

        })


    }
    fun getPickupByLov(companyId: String, spname: String, param: List<String>, values: ArrayList<String>) {
        api.commonApi(companyId, spname, param, values).enqueue(object : Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult>, response: Response<CommonResult>) {
                if (response.body() != null) {
                    val result = response.body()!!
                    val gson = Gson()
                    if (result.CommandStatus == 1) {
                        val jsonArray = getResult(result);
                        if (jsonArray != null) {
                            val listType =
                                object : TypeToken<List<PickupBySelection>>() {}.type
                            val resultList: ArrayList<PickupBySelection> =
                                gson.fromJson(jsonArray.toString(), listType);
                            pickupByMuteLiveData.postValue(resultList)

                        }
                    } else {
                        isError.postValue(result.CommandMessage.toString());
                    }
                } else {
                    isError.postValue(SERVER_ERROR);
                }
                viewDialogMutData.postValue(false)
            }

            override fun onFailure(call: Call<CommonResult>, t: Throwable) {
                viewDialogMutData.postValue(false)
                isError.postValue(t.message)
            }

        })


    }

    fun getServiceType(companyId: String, spname: String, param: List<String>, values: ArrayList<String>) {
        api.commonApi(companyId, spname, param, values).enqueue(object : Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult>, response: Response<CommonResult>) {
                if (response.body() != null) {
                    val result = response.body()!!
                    val gson = Gson()
                    if (result.CommandStatus == 1) {
                        val jsonArray = getResult(result);
                        if (jsonArray != null) {
                            val listType =
                                object : TypeToken<List<ServiceTypeSelectionLov>>() {}.type
                            val resultList: ArrayList<ServiceTypeSelectionLov> =
                                gson.fromJson(jsonArray.toString(), listType);
                            serviceListMuteLiveData.postValue(resultList)

                        }
                    } else {
                        isError.postValue(result.CommandMessage.toString());
                    }
                } else {
                    isError.postValue(SERVER_ERROR);
                }
                viewDialogMutData.postValue(false)
            }

            override fun onFailure(call: Call<CommonResult>, t: Throwable) {
                viewDialogMutData.postValue(false)
                isError.postValue(t.message)
            }

        })


    }


    fun getSingleRefData( companyId:String,spname: String,param:List<String>, values:ArrayList<String>) {
        viewDialogMutData.postValue(true)
        api.commonApi(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<SinglePickupRefModel>>() {}.type
                            val resultList: ArrayList<SinglePickupRefModel> = gson.fromJson(jsonArray.toString(), listType);
                            singleRefMuteLiveData.postValue(resultList);

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

    fun getPckgTypeLov(companyId:String, spname: String, param:List<String>, values:ArrayList<String>) {
        viewDialogMutData.postValue(true)
        api.commonApi(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<PckgTypeSelectionModel>>() {}.type
                            val resultList: ArrayList<PckgTypeSelectionModel> = gson.fromJson(jsonArray.toString(), listType);
                            pckgTypeMuteLiveData.postValue(resultList);

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

    fun saveJeenaBooking(companyId:String,branchCode:String, bookingDt:String, time:String, grNo:String, custCode:String, destCode:String, productCode:String, pckgs:Int, aWeight:String, vWeight:String, cWeight:String, createId:String, sessionId:String,
                         refNo:String, cngr:String, cngrAddress:String, cngrCity:String, cngrZipCode:String, cngrState:String, cngrMobileNo:String, cngreMailId:String, cngrCSTNo:String, cngrLSTNo:String, cngrTINNo:String, cngrSTaxRegNo:String, cnge:String, cngeAddress:String, cngeCity:String, cngeZipCode:String, cngeState:String, cngeMobileNo:String, cngeeMailId:String, cngeCSTNo:String, cngeLSTNo:String, cngeTINNo:String,
                         cngeSTaxRegNo:String, transactionId:String, awbChargeApplicable:String, custDeptId:String, referenceNoStr:String, weightStr:String, packageTypeStr:String, tempuratureStr:String, packingStr:String, goodsStr:String, dryIceStr:String, dryIceQtyStr:String, dataLoggerStr:String, dataLoggerNoStr:String, dimLength:String, dimBreath:String, dimHeight:String, pickupBoyName:String, boyId:String, boxnoStr:String, stockItemCodeStr:String, gelPackStr:String, gelPackItemCodeStr:String, gelPackQtyStr:String, menuCode:String, invoiceNoStr:String, invoiceDtStr:String, invoiceValueStr:String, ewayBillnNoStr:String, ewayBillDtStr:String, ewbValidupToDtStr:String, vendorCode:String, packageStr:String, pickupBy:String, vehicleNo:String, vWeightStr:String, vehicleCode:String, cngrCode:String, cngeCode:String, remarks:String, cngrGstNo:String, cngeGstNo:String) {

        viewDialogMutData.postValue(true)
        api.saveJeenaBooking(companyId,branchCode,bookingDt,time,grNo,custCode,destCode,productCode,pckgs,aWeight,vWeight,cWeight,createId,sessionId,refNo,cngr,cngrAddress,cngrCity,cngrZipCode,cngrState,cngrMobileNo,cngreMailId,
            cngrCSTNo, cngrLSTNo, cngrTINNo, cngrSTaxRegNo, cnge, cngeAddress, cngeCity, cngeZipCode, cngeState, cngeMobileNo, cngeeMailId, cngeCSTNo, cngeLSTNo, cngeTINNo,
            cngeSTaxRegNo, transactionId, awbChargeApplicable, custDeptId, referenceNoStr, weightStr, packageTypeStr, tempuratureStr, packingStr, goodsStr, dryIceStr, dryIceQtyStr, dataLoggerStr, dataLoggerNoStr, dimLength, dimBreath, dimHeight, pickupBoyName, boyId, boxnoStr, stockItemCodeStr, gelPackStr, gelPackItemCodeStr, gelPackQtyStr, menuCode, invoiceNoStr, invoiceDtStr, invoiceValueStr, ewayBillnNoStr, ewayBillDtStr, ewbValidupToDtStr, vendorCode, packageStr, pickupBy, vehicleNo, vWeightStr, vehicleCode, cngrCode, cngeCode, remarks, cngrGstNo, cngeGstNo

        )
            .enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<SaveBookingModel>>() {}.type
                            val resultList: ArrayList<SaveBookingModel> = gson.fromJson(result.DataSet, listType);
                            saveBookingMuteLiveData.postValue(resultList[0])
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