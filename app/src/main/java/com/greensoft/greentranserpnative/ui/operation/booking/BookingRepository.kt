package com.greensoft.greentranserpnative.ui.operation.booking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.operation.booking.models.AgentSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.ConsignorSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.ContentSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.CustomerSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.DepartmentSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.DestinationSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.GelPackItemSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.OriginSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.PackingSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.PickupBoySelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.TemperatureSelectionModel
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
}