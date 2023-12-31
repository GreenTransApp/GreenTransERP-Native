package com.greensoft.greentranserpnative.ui.operation.loadingSlip.newScanLoad

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.newScanLoad.NewScanAndLoadRepository
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.newScanLoad.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewScanAndLoadViewModel @Inject constructor(private val _repo: NewScanAndLoadRepository)  : BaseViewModel() {

    init {
        isError = _repo.isError
    }
    val stickerListLivedata : LiveData<ArrayList<LoadedStickerModel>>
        get() = _repo.stickerListLivedata
    val validateStickerLiveData : LiveData<ValidateStickerModel>
        get() = _repo.validateStickerLivedata
    val saveStickerLiveData: LiveData<SaveStickerModel>
        get() = _repo.saveStickerLiveData
    val removeStickerLiveData: LiveData<RemoveStickerModel>
        get() = _repo.removeStickerLiveData
    val viewDialogLiveData: LiveData<Boolean>
        get() = _repo.viewDialogLiveData

//    fun getScannedSticker(companyId: String, spName: String, param: ArrayList<String>, values: ArrayList<String>){
//        viewModelScope.launch(Dispatchers.IO) {
//            _repo.getScannedSticker(companyId,spName, param,values)
//        }
//    }
    fun getScannedStickers(companyId: String, loadingNo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getScannedSticker(companyId, loadingNo)
        }
    }

    fun validateSticker(companyId: String, branchCode: String, stickerNo: String, dt: String){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.validateSticker(companyId, branchCode, stickerNo, dt)
        }
    }

    fun updateSticker(
        companyId: String?, loadingNo: String?, loadingDt: String?, loadingTime: String?, stnCode: String?, branchCode: String?,
        destCode: String?, modeType: String?, vendCode: String?, modeCode: String?, loadedBy: String?, driverCode: String?, remarks: String?,
        stickerNoStr: String?, grNoStr: String?, userCode: String?, menuCode: String?, sessionId: String?, driverMobileNo: String?, despatchType: String?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.updateSticker(companyId, loadingNo, loadingDt, loadingTime, stnCode, branchCode,
                destCode, modeType, vendCode, modeCode, loadedBy, driverCode, remarks,
                stickerNoStr, grNoStr, userCode, menuCode, sessionId, driverMobileNo, despatchType)
        }
    }

    fun removeSticker(companyId: String?, stickerNo: String?, userCode: String?, menuCode: String?, sessionId: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.removeSticker(companyId, stickerNo, userCode, menuCode, sessionId)
        }
    }

}