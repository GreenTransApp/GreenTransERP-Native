package com.greensoft.greentranserpnative.ui.operation.scan_and_delivery

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.operation.pod_entry.models.PodEntryModel
import com.greensoft.greentranserpnative.ui.operation.multiple_pod_entry_list.models.RelationListModel
import com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.models.SavePodWithStickersModel
import com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.models.ScanDelReasonModel
import com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.models.ScanDeliverySaveModel
import com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.models.ScanStickerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanAndDeliveryViewModel @Inject constructor(private val _repo: ScanAndDeliveryRepository) :
    BaseViewModel() {

    init {
        isError = _repo.isError
    }

    val viewDialogLiveData: LiveData<Boolean>
        get() = _repo.viewDialogLiveData

    val podDetailsLiveData: LiveData<PodEntryModel>
        get() = _repo.podLiveData
    val relationLiveData: LiveData<ArrayList<RelationListModel>>
        get() = _repo.relationLiveData
    val stickerLiveData: LiveData<ArrayList<ScanStickerModel>>
        get() = _repo.stickerLiveData
    val updateStickerLiveData: LiveData<ScanDeliverySaveModel>
        get() = _repo.updateStickerForPodLiveData
    val unDelReasonList: LiveData<ArrayList<ScanDelReasonModel>>
        get() = _repo.scanDelReasonLiveData

    val savePodWithStickersLiveData: LiveData<SavePodWithStickersModel>
        get() = _repo.savePodWithStickersLiveData

    fun getPodDetails(companyId: String, grNo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getPodData(companyId, grNo)
        }
    }

    fun getRelation(companyId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getRelationList(companyId)
        }
    }

    fun getStickerList(
        companyId: String,
        userCode: String,
        loginBranchCode: String,
        branchCode: String,
        grNo: String,
        menuCode: String,
        sessionId: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getStickerList(
                companyId,
                userCode,
                loginBranchCode,
                branchCode,
                grNo,
                menuCode,
                sessionId
            )
        }
    }

    fun getDelReasonList(
        companyId: String,
        spname: String,
        param: List<String>,
        values: ArrayList<String>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getDelReasonList(companyId, spname, param, values)
        }
    }

    fun updateStickerForPod(
        companyId: String,
        userCode: String,
        loginBranchCode: String,
        branchCode: String,
        stickerNo: String,
        menuCode: String,
        sessionId: String,
        grNo: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.saveScanDeliveryPod(
                companyId,
                userCode,
                loginBranchCode,
                branchCode,
                stickerNo,
                menuCode,
                sessionId,
                grNo
            )
        }
    }

    fun savePodWithStickers(companyId: String?, podImage: String?, signImage: String?, userCode: String?, loginBranchCode: String?,
                            grNo: String?, dlvDt: String?, dlvTime: String?, name: String?, relation: String?, phoneNo: String?, isSign: String?,
                            isStamp: String?, remarks: String?, podDt: String?, sessionId: String?, menuCode: String?, unDelStickerStr: String?,
                            unDelReasonStr: String?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.savePodWithStickers(companyId, podImage, signImage, userCode, loginBranchCode,
                grNo, dlvDt, dlvTime, name, relation, phoneNo, isSign,
                isStamp, remarks, podDt, sessionId, menuCode, unDelStickerStr,
                unDelReasonStr)
        }
    }


}
