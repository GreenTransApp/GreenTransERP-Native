package com.greensoft.greentranserpnative.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.home.models.NotificationModel
import com.greensoft.greentranserpnative.ui.home.models.UserMenuModel
import com.greensoft.greentranserpnative.ui.operation.notificationPanel.model.NotificationPanelBottomSheetModel
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.await
import javax.inject.Inject


class HomeRepository @Inject constructor(): BaseRepository() {
    val viewDialogLiveData: LiveData<Boolean>
        get() = viewDialogMutData

    private val menuMuteLiveData = MutableLiveData<ArrayList<UserMenuModel>>()
    val menuLiveData: LiveData<ArrayList<UserMenuModel>>
        get() = menuMuteLiveData

    private val notificationMutLiveData = MutableLiveData<NotificationModel>()
    val notificationLiveData: LiveData<NotificationModel>
        get() = notificationMutLiveData

    private val notificationPanelMuteLiveData = MutableLiveData<ArrayList<NotificationPanelBottomSheetModel>>()
    val notificationPanelListLiveData: LiveData<ArrayList<NotificationPanelBottomSheetModel>>
        get() = notificationPanelMuteLiveData

//    suspend fun getUserMenu( companyId:String,spname: String,param:List<String>, values:ArrayList<String>): ArrayList<UserMenuModel>? {
    suspend fun getUserMenu( companyId:String, userCode: String, branchCode: String, fromDt: String, toDt: String, appVersion: String): ArrayList<UserMenuModel>? {
        viewDialogMutData.postValue(true)
        try {
//            delay(5000)
//            val response: CommonResult = api.commonApiWMS(companyId, spname, param, values).await()
            val response: CommonResult = api.getUserMenu(companyId, userCode, branchCode, fromDt, toDt, appVersion).await()
//            if(response.body() != null){
//                val result = response.body()!!
            val result = response
            val gson = Gson()
            if(result.CommandStatus == 1){

                val jsonArray1 = getResult1(result);
                if(jsonArray1 != null) {
                    val listType = object: TypeToken<List<NotificationModel>>() {}.type
                    val resultList: ArrayList<NotificationModel> = gson.fromJson(jsonArray1.toString(), listType);
                    notificationMutLiveData.postValue(resultList[0]);
                }
                val jsonArray = getResult(result);
                if(jsonArray != null) {
                    val listType = object: TypeToken<List<UserMenuModel>>() {}.type
                    val resultList: ArrayList<UserMenuModel> = gson.fromJson(jsonArray.toString(), listType);
                    viewDialogMutData.postValue(false)
                    return resultList
                }
            } else {
                isError.postValue(result.CommandMessage.toString());
            }
//            } else {
//                isError.postValue(SERVER_ERROR);
//            }
            viewDialogMutData.postValue(false)
        } catch (httpException: HttpException) {
            viewDialogMutData.postValue(false)
            isError.postValue(httpException.message)
        }
        catch (ex: Exception) {
            viewDialogMutData.postValue(false)
            isError.postValue(ex.message)
        }
        return null

//        api.commonApiWMS(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
//            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
//                if(response.body() != null){
//                    val result = response.body()!!
//                    val gson = Gson()
//                    if(result.CommandStatus == 1){
//                        val jsonArray = getResult(result);
//                        if(jsonArray != null) {
//                            val listType = object: TypeToken<List<UserMenuModel>>() {}.type
//                            val resultList: ArrayList<UserMenuModel> = gson.fromJson(jsonArray.toString(), listType);
////                            menuMuteLiveData.postValue(resultList);
//                            return resultList
//                        }
//                        val jsonArray1 = getResult1(result);
//                        if(jsonArray1 != null) {
//                            val listType = object: TypeToken<List<NotificationModel>>() {}.type
//                            val resultList: ArrayList<NotificationModel> = gson.fromJson(jsonArray1.toString(), listType);
//                            notificationMutLiveData.postValue(resultList[0]);
//                        }
//                    } else {
//                        isError.postValue(result.CommandMessage.toString());
//                    }
//                } else {
//                    isError.postValue(SERVER_ERROR);
//                }
//                viewDialogMutData.postValue(false)
//
//            }
//
//            override fun onFailure(call: Call<CommonResult?>, t: Throwable) {
//                viewDialogMutData.postValue(false)
//                isError.postValue(t.message)
//            }
//
//        })

    }

//    fun getUserMenu( companyId:String,spname: String,param:List<String>, values:ArrayList<String>) {
//        viewDialogMutData.postValue(true)
//        api.commonApiWMS(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
//            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
//                if(response.body() != null){
//                    val result = response.body()!!
//                    val gson = Gson()
//                    if(result.CommandStatus == 1){
//                        val jsonArray = getResult(result);
//                        if(jsonArray != null) {
//                            val listType = object: TypeToken<List<UserMenuModel>>() {}.type
//                            val resultList: ArrayList<UserMenuModel> = gson.fromJson(jsonArray.toString(), listType);
//                            menuMuteLiveData.postValue(resultList);
//                        }
//                        val jsonArray1 = getResult1(result);
//                        if(jsonArray1 != null) {
//                            val listType = object: TypeToken<List<NotificationModel>>() {}.type
//                            val resultList: ArrayList<NotificationModel> = gson.fromJson(jsonArray1.toString(), listType);
//                            notificationMutLiveData.postValue(resultList[0]);
//                        }
//                    } else {
//                        isError.postValue(result.CommandMessage.toString());
//                    }
//                } else {
//                    isError.postValue(SERVER_ERROR);
//                }
//                viewDialogMutData.postValue(false)
//
//            }
//
//            override fun onFailure(call: Call<CommonResult?>, t: Throwable) {
//                viewDialogMutData.postValue(false)
//                isError.postValue(t.message)
//            }
//
//        })
//
//    }

    fun getNotificationPanelList(companyId:String,spname: String,param:List<String>, values:ArrayList<String>) {
        viewDialogMutData.postValue(true)
        api.commonApiWMS(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<NotificationPanelBottomSheetModel>>() {}.type
                            val resultList: ArrayList<NotificationPanelBottomSheetModel> = gson.fromJson(jsonArray.toString(), listType);
                            notificationPanelMuteLiveData.postValue(resultList);
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