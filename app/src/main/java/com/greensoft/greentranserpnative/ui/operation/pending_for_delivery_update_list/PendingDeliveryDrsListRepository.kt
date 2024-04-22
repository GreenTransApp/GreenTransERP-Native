package com.greensoft.greentranserpnative.ui.operation.pending_for_delivery_update_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.operation.pending_for_delivery_update_list.models.DrsPendingListModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class PendingDeliveryDrsListRepository @Inject constructor() : BaseRepository() {
    val viewDialogLiveData: LiveData<Boolean>
        get() = viewDialogMutData
    private val drsPendingListMuteData = MutableLiveData<ArrayList<DrsPendingListModel>>()

    val drsPendingListLivedata : LiveData<ArrayList<DrsPendingListModel>>
        get() = drsPendingListMuteData

    fun getDrsPendingList(companyId:String,userCode:String,loginBranchCode:String,fromDt:String,toDt:String,sessionId:String) {
        viewDialogMutData.postValue(true)
        api.getDrsPendingList(companyId,userCode,loginBranchCode,fromDt,toDt,sessionId).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<DrsPendingListModel>>() {}.type
                            val resultList: ArrayList<DrsPendingListModel> = gson.fromJson(jsonArray.toString(), listType);
                            drsPendingListMuteData.postValue(resultList);

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