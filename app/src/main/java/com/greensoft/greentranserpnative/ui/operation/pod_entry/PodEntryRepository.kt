package com.greensoft.greentranserpnative.ui.operation.pod_entry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.operation.pod_entry.models.PodEntryModel
import com.greensoft.greentranserpnative.ui.operation.pod_entry.models.PodSaveModel
import com.greensoft.greentranserpnative.ui.operation.multiple_pod_entry_list.models.RelationListModel
import com.greensoft.greentranserpnative.ui.operation.pod_entry.models.getGrNoModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class PodEntryRepository  @Inject constructor(): BaseRepository() {
    val viewDialogLiveData: LiveData<Boolean>
        get() = viewDialogMutData

    private val podMuteLiveData = MutableLiveData<PodEntryModel>()
    private val grNoMuteLiveData = MutableLiveData<getGrNoModel>()
    private val podSaveMuteLiveData = MutableLiveData<PodSaveModel>()
    private val relationListMuteLiveData = MutableLiveData<ArrayList<RelationListModel>>()
    val podLiveData: LiveData<PodEntryModel>
        get() = podMuteLiveData

    val grNoLiveData: LiveData<getGrNoModel>
        get() = grNoMuteLiveData

    val podSaveLiveData: LiveData<PodSaveModel>
        get() = podSaveMuteLiveData
    val relationLiveData: LiveData<ArrayList<RelationListModel>>
        get() = relationListMuteLiveData
    fun getGrDetail(companyId:String, grNo:String) {
        viewDialogMutData.postValue(true)
        api.getPodDetails(companyId,grNo).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<PodEntryModel>>() {}.type
                            val resultList: ArrayList<PodEntryModel> = gson.fromJson(jsonArray.toString(), listType);
                            podMuteLiveData.postValue(resultList[0]);

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


    fun getGrFromSticker(companyId:String, userCode: String, branchCode: String, menuCode: String, stickerNo:String, sessionId: String) {
        viewDialogMutData.postValue(true)
        api.getGrnoFromSticker(companyId, userCode, branchCode, menuCode, stickerNo, sessionId).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<getGrNoModel>>() {}.type
                            val resultList: ArrayList<getGrNoModel> = gson.fromJson(jsonArray.toString(), listType);
                            grNoMuteLiveData.postValue(resultList[0]);

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


    fun savePodEntry(
        companyId:String,
        loginBranchCode: String,
        grNo: String,
        dlvTime: String,
        name: String,
        dlvDt: String,
        relation: String,
        phoneNo: String,
        sign: String,
        stamp: String,
        podImage: String,
        signImage: String,
        remarks: String,
        userCode: String,
        sessionId: String,
        delayReason: String,
        menuCode: String,
        deliveryBoy: String,
        boyId: String,
        podDt: String,
        pckgs: String,
        pckgsStr: String,
        dataIdStr: String,
    ) {
        viewDialogMutData.postValue(true)
        api.SavePodEntry(
            companyId,
            loginBranchCode,
            grNo,
            dlvTime,
            name,
            dlvDt,
            relation,
            phoneNo,
            sign,
            stamp,
            podImage,
            signImage,
            remarks,
            userCode,
            sessionId,
            delayReason,
            menuCode,
            deliveryBoy,
            boyId,
            podDt,
            pckgs,
            pckgsStr,
            dataIdStr,
        )
            .enqueue(object: Callback<CommonResult> {
                override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                    if(response.body() != null){
                        val result = response.body()!!
                        val gson = Gson()
                        if(result.CommandStatus == 1){
                            val jsonArray = getResult(result);
                            if(jsonArray != null) {
                                val listType = object: TypeToken<List<PodSaveModel>>() {}.type
                                val resultList: ArrayList<PodSaveModel> = gson.fromJson(jsonArray.toString(), listType);
                                podSaveMuteLiveData.postValue(resultList[0])
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