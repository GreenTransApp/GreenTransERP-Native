package com.greensoft.greentranserpnative.ui.operation.outstation_inscan_detail_with_scanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_with_scanner.models.InScanAddStickerModel
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.model.InScanDetailScannerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OutstationInscanDetailWithScannerViewModel @Inject constructor(private val _repo: OutstationInscanDetailWithScannerRepository):
    BaseViewModel() {
    init {
        isError = _repo.isError
    }

    val inScanDetailLiveData: LiveData<InScanDetailScannerModel>
        get() = _repo.inScanDetailLiveData
    val inScanCardLiveData: LiveData<ArrayList<InScanDetailScannerModel>>
        get() = _repo.inScanCardLiveData

    val inScanAddStickerLiveData: LiveData<InScanAddStickerModel>
        get() = _repo.inScanAddStickerLiveData

    fun getInScanDetails(companyId: String, userCode: String, branchCode: String, manifestNo:String,manifestType:String,sessionId:String){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getInScanDetails(companyId,userCode, branchCode,manifestNo,manifestType,sessionId)
        }
    }
    fun addInScanSticker(companyId: String, userCode: String, branchCode: String,menuCode:String, sessionId:String,stickerNo:String,manifestNo: String,receiveDt: String, receiveTime: String){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.addInScanSticker(companyId,userCode, branchCode, menuCode,sessionId,stickerNo,manifestNo, receiveDt, receiveTime)
        }
    }
}