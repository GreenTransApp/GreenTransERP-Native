package com.greensoft.greentranserpnative.ui.operation.call_register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.operation.call_register.models.CallRegisterModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import javax.inject.Inject

class CallRegisterRepository @Inject constructor(): BaseRepository(){
    val viewDialogLiveData: LiveData<Boolean>
        get() = viewDialogMutData

    private val callRegisterMuteLiveData = MutableLiveData<ArrayList<CallRegisterModel>>()
    val callRegisterLiveData: LiveData<ArrayList<CallRegisterModel>>
        get() = callRegisterMuteLiveData

    private val acceptPickupMutableData = MutableLiveData<String>()
    val acceptPickupLiveData: LiveData<String>
        get() = acceptPickupMutableData

    private val rejectPickupMutableData = MutableLiveData<String>()
    val rejectPickupLiveData: LiveData<String>
        get() = rejectPickupMutableData





    fun getCallRegisterData( companyId:String,spname: String,param:List<String>, values:ArrayList<String>) {
        viewDialogMutData.postValue(true)
        api.commonApiWMS(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<CallRegisterModel>>() {}.type
                            val resultList: ArrayList<CallRegisterModel> = gson.fromJson(jsonArray.toString(), listType);
                            callRegisterMuteLiveData.postValue(resultList);

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

    fun acceptPickup(companyId:String, transactionId: String, pickupDate: String, pickupTime: String, pickupRemarks: String, sendMsgToCust: String) {
        viewDialogMutData.postValue(true)
        api.acceptPickup(companyId, transactionId,pickupDate, pickupTime, pickupRemarks, sendMsgToCust).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val jObj = jsonArray.getJSONObject(0)
                            val commandMessage = jObj["commandmessage"].toString()
                            if (jObj["commandstatus"].toString() == "1") {
                                acceptPickupMutableData.postValue(commandMessage)
                            } else {
                                isError.postValue(commandMessage)
                            }
//                            val listType = object: TypeToken<List<CallRegisterModel>>() {}.type
//                            val resultList: ArrayList<CallRegisterModel> = gson.fromJson(jsonArray.toString(), listType);
//                            callRegisterMuteLiveData.postValue(resultList);

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

    fun rejectPickup(companyId:String, transactionId: String, pickupRemarks: String) {
        viewDialogMutData.postValue(true)
        api.rejectPickup(companyId, transactionId, pickupRemarks).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val jObj = jsonArray.getJSONObject(0)
                            val commandMessage = jObj["commandmessage"].toString()
                            if (jObj["commandstatus"].toString() == "1") {
                                rejectPickupMutableData.postValue(commandMessage)
                            } else {
                                isError.postValue(commandMessage)
                            }
//                            val listType = object: TypeToken<List<CallRegisterModel>>() {}.type
//                            val resultList: ArrayList<CallRegisterModel> = gson.fromJson(jsonArray.toString(), listType);
//                            callRegisterMuteLiveData.postValue(resultList);

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