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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

    fun getUserMenu( companyId:String,spname: String,param:List<String>, values:ArrayList<String>) {
        viewDialogMutData.postValue(true)
        api.commonApiWMS(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<UserMenuModel>>() {}.type
                            val resultList: ArrayList<UserMenuModel> = gson.fromJson(jsonArray.toString(), listType);
                            menuMuteLiveData.postValue(resultList);
                        }
                        val jsonArray1 = getResult1(result);
                        if(jsonArray1 != null) {
                            val listType = object: TypeToken<List<NotificationModel>>() {}.type
                            val resultList: ArrayList<NotificationModel> = gson.fromJson(jsonArray1.toString(), listType);
                            notificationMutLiveData.postValue(resultList[0]);
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