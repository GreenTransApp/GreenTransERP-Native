package com.greensoft.greentranserpnative.ui.operation.multiple_pod_entry_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.operation.multiple_pod_entry_list.models.RelationListModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MultiplePodEntryRepository  @Inject constructor() : BaseRepository(){
    val viewDialogLiveData: LiveData<Boolean>
        get() = viewDialogMutData
    private val relationListMuteLiveData = MutableLiveData<ArrayList<RelationListModel>>()
    val relationLiveData: LiveData<ArrayList<RelationListModel>>
        get() = relationListMuteLiveData

    fun getRelationList( companyId:String) {
        viewDialogMutData.postValue(true)
        api.getRelationList(companyId).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<RelationListModel>>() {}.type
                            val resultList: ArrayList<RelationListModel> = gson.fromJson(jsonArray.toString(), listType);
                            relationListMuteLiveData.postValue(resultList)
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