package com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad.models.LoadingSlipHeaderDataModel
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad.models.SaveStickerModel
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad.models.StickerModel
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad.models.ValidateStickerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanLoadViewModel @Inject constructor( private val _repo: ScanLoadRepository)  : BaseViewModel() {

    init {
        isError = _repo.isError
    }
    val headerLivedata : LiveData<LoadingSlipHeaderDataModel>
        get() = _repo.headerLivedata
    val stickerListLivedata : LiveData<ArrayList<StickerModel>>
        get() = _repo.stickerListLivedata
    val validateStickerLiveData : LiveData<ValidateStickerModel>
        get() = _repo.validateStickerLivedata
    val saveStickerLiveData: LiveData<SaveStickerModel>
        get() = _repo.saveStickerLiveData

    fun getScannedSticker(companyId: String, spName: String, param: ArrayList<String>, values: ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getScannedSticker(companyId,spName, param,values)
        }
    }

    fun validateSticker(companyId: String, spName: String, param: ArrayList<String>, values: ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.validateSticker(companyId,spName, param,values)
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

}