package com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.model.InScanWithoutScannerModel
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.model.InscanDetailsSaveModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InScanDetailsViewModel @Inject constructor(private val _repo: InScanDetailsRepository):BaseViewModel() {

    init {
        isError = _repo.isError
    }

    val inScanDetailLiveData:LiveData<InScanWithoutScannerModel>
        get() = _repo.inScanDetailLiveData
    val inScanCardLiveData:LiveData<ArrayList<InScanWithoutScannerModel>>
        get() = _repo.inScanCardLiveData
val inScanSaveLiveData:LiveData<InscanDetailsSaveModel>
        get() = _repo.inscanSaveLiveData

    fun getInScanDetails(companyId: String, userCode: String, branchCode: String, manifestNo:String,manifestType:String,sessionId:String){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getInScanDetails(companyId,userCode, branchCode,manifestNo,manifestType,sessionId)
        }
    }

    fun saveInScanDetailsWithoutScan(
        companyId:String,
        manifestNo:String,
        mawbNo:String,
        branchCode:String,
        receiveDt:String,
        receiveTime:String,
        vehicleCode:String,
        remarks:String,
        grNo:String,
        mfPckgs:String,
        pckgs:String,
        weight:String,
        damagePckgs:String,
        damageReasoncode:String,
        detailRemarks:String,
        userCode:String,
        menuCode:String,
        sessionId:String,
        fromstnCode:String){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.saveInscanDetails(
                companyId,
                manifestNo,
                mawbNo,
                branchCode,
                receiveDt,
                receiveTime,
                vehicleCode,
                remarks,
                grNo,
                mfPckgs,
                pckgs,
                weight,
                damagePckgs,
                damageReasoncode,
                detailRemarks,
                userCode,
                menuCode,
                sessionId,
                fromstnCode)
        }
    }
}