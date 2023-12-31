package com.greensoft.greentranserpnative.ui.operation.loadingSlip.search_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.search_list.models.SearchListModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.BranchSelectionModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class SearchListRepository @Inject constructor(): BaseRepository() {

    val viewDialogLiveData: LiveData<Boolean>
        get() = viewDialogMutData

    private val searchListMuteLiveData = MutableLiveData<ArrayList<SearchListModel>>()
    val searchListLiveData: LiveData<ArrayList<SearchListModel>>
        get() = searchListMuteLiveData

    fun getSearchList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>) {
        viewDialogMutData.postValue(true)
        api.commonApiWMS(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<SearchListModel>>() {}.type
                            val resultList: ArrayList<SearchListModel> = gson.fromJson(jsonArray.toString(), listType);
                            searchListMuteLiveData.postValue(resultList);
                        } else {
                            try {
                                val dataSet = result.DataSet
                                val obj = JSONObject(dataSet)
                                val array = obj.getJSONArray("Table")
                                if (array.length() <= 0)
                                    isError.postValue("No Data Found")
                            } catch (ex: Exception) {
                                isError.postValue(SERVER_ERROR)
                            }
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