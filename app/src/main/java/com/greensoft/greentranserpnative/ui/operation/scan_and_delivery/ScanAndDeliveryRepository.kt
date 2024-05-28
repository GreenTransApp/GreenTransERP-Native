package com.greensoft.greentranserpnative.ui.operation.scan_and_delivery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.operation.pod_entry.models.PodEntryModel
import com.greensoft.greentranserpnative.ui.operation.multiple_pod_entry_list.models.RelationListModel
import com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.models.SavePodWithStickersModel
import com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.models.ScanDelReasonModel
import com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.models.ScanDeliverySaveModel
import com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.models.ScanStickerModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ScanAndDeliveryRepository @Inject constructor(): BaseRepository() {
    val viewDialogLiveData: LiveData<Boolean>
        get() = viewDialogMutData

    private val podMuteLiveData = MutableLiveData<PodEntryModel>()
    private val relationListMuteLiveData = MutableLiveData<ArrayList<RelationListModel>>()
    private val stickerListMuteLiveData = MutableLiveData<ArrayList<ScanStickerModel>>()
    private val updateStickerForPODMuteLiveData = MutableLiveData<ScanDeliverySaveModel>()
    private val scanDlReasonMuteLiveData = MutableLiveData<ArrayList<ScanDelReasonModel>>()
    private val savePodWithStickersMuteLiveData = MutableLiveData<SavePodWithStickersModel>()
    val podLiveData: LiveData<PodEntryModel>
        get() = podMuteLiveData
    val relationLiveData: LiveData<ArrayList<RelationListModel>>
        get() = relationListMuteLiveData

    val stickerLiveData: LiveData<ArrayList<ScanStickerModel>>
        get() = stickerListMuteLiveData

    val updateStickerForPodLiveData: LiveData<ScanDeliverySaveModel>
        get() = updateStickerForPODMuteLiveData

    val scanDelReasonLiveData: LiveData<ArrayList<ScanDelReasonModel>>
        get() = scanDlReasonMuteLiveData

    val savePodWithStickersLiveData: LiveData<SavePodWithStickersModel>
        get() = savePodWithStickersMuteLiveData
    fun getPodData( companyId:String,grNo:String) {
//        viewDialogMutData.postValue(true)
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

    fun getStickerList( companyId:String,userCode:String,loginBranchCode:String,branchCode:String,grNo:String,menuCode:String,sessionId:String) {
//        viewDialogMutData.postValue(true)
        api.getPodStickerList(companyId,userCode,loginBranchCode,branchCode, grNo, menuCode,sessionId).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<ScanStickerModel>>() {}.type
                            val resultList: ArrayList<ScanStickerModel> = gson.fromJson(jsonArray.toString(), listType);
                            stickerListMuteLiveData.postValue(resultList)
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
    fun saveScanDeliveryPod(
        companyId:String,
        userCode: String,
        loginBranchCode: String,
        branchCode: String,
        stickerNo: String,
        menuCode: String,
        sessionId: String,
        grNo: String
    ) {
        viewDialogMutData.postValue(true)
        api.updateScanDeliverySticker(
            companyId,
            userCode,
            loginBranchCode,
            branchCode,
            stickerNo,
            menuCode,
            sessionId,
            grNo
        )
            .enqueue(object: Callback<CommonResult> {
                override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                    if(response.body() != null){
                        val result = response.body()!!
                        val gson = Gson()
                        if(result.CommandStatus == 1){
                            val jsonArray = getResult(result);
                            if(jsonArray != null) {
                                val listType = object: TypeToken<List<ScanDeliverySaveModel>>() {}.type
                                val resultList: ArrayList<ScanDeliverySaveModel> = gson.fromJson(jsonArray.toString(), listType);
                                updateStickerForPODMuteLiveData.postValue(resultList[0])
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

    fun getDelReasonList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>) {
        viewDialogMutData.postValue(true)
        api.commonApiWMS(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<ScanDelReasonModel>>() {}.type
                            val resultList: ArrayList<ScanDelReasonModel> = gson.fromJson(jsonArray.toString(), listType);
                            scanDlReasonMuteLiveData.postValue(resultList);

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

    fun savePodWithStickers(companyId: String?, podImage: String?, signImage: String?, userCode: String?, loginBranchCode: String?,
        grNo: String?, dlvDt: String?, dlvTime: String?, name: String?, relation: String?, phoneNo: String?, isSign: String?,
        isStamp: String?, remarks: String?, podDt: String?, sessionId: String?, menuCode: String?, unDelStickerStr: String?,
        unDelReasonStr: String?
    ) {
        viewDialogMutData.postValue(true)
        api.savePodWithStickers(
            companyId,
            podImage,
            signImage,
            userCode,
            loginBranchCode,
            grNo,
            dlvDt,
            dlvTime,
            name,
            relation,
            phoneNo,
            isSign,
            isStamp,
            remarks,
            podDt,
            sessionId,
            menuCode,
            unDelStickerStr,
            unDelReasonStr
        ).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<SavePodWithStickersModel>>() {}.type
                            val resultList: ArrayList<SavePodWithStickersModel> = gson.fromJson(jsonArray.toString(), listType);
                            savePodWithStickersMuteLiveData.postValue(resultList[0]);

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