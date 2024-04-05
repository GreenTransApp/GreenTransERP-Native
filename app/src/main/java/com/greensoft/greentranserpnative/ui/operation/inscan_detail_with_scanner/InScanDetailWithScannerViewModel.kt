package com.greensoft.greentranserpnative.ui.operation.inscan_detail_with_scanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_with_scanner.models.InScanAddStickerModel
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.model.InScanWithoutScannerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InScanDetailWithScannerViewModel @Inject constructor(private val _repo: InScanDetailWithScannerRepository):
    BaseViewModel(){


    init {
        isError = _repo.isError
    }

    val inScanDetailLiveData: LiveData<InScanWithoutScannerModel>
        get() = _repo.inScanDetailLiveData
    val inScanCardLiveData: LiveData<ArrayList<InScanWithoutScannerModel>>
        get() = _repo.inScanCardLiveData

    val inScanAddStickerLiveData:LiveData<InScanAddStickerModel>
        get() = _repo.inScanAddStickerLiveData

    fun getInScanDetails(companyId: String, userCode: String, branchCode: String, manifestNo:String,manifestType:String,sessionId:String){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getInScanDetails(companyId,userCode, branchCode,manifestNo,manifestType,sessionId)
        }
    }
    fun addInScanSticker(companyId: String, userCode: String, branchCode: String, manifestNo:String,manifestType:String,sessionId:String){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.addInScanSticker(companyId,userCode, branchCode,manifestNo,manifestType,sessionId)
        }
    }
}