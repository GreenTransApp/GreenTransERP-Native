package com.greensoft.greentranserpnative.ui.operation.outstation_inscan_detail_without_scanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.InScanDetailsRepository
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.model.DamageReasonModel
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.model.InScanDetailScannerModel
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.model.InscanDetailsSaveModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class OutstationInscanDetailWithoutScannerViewModel @Inject constructor(private val _repo: OutstationInscanDetailWithoutScannerRepository):
    BaseViewModel(){

    init {
        isError = _repo.isError
    }

    val inScanDetailLiveData: LiveData<InScanDetailScannerModel>
        get() = _repo.inScanDetailLiveData
    val inScanCardLiveData: LiveData<ArrayList<InScanDetailScannerModel>>
        get() = _repo.inScanCardLiveData

    val damagePckgsReasonLiveData: LiveData<ArrayList<DamageReasonModel>>
        get() = _repo.damageReasonLiveData
    val inScanSaveLiveData: LiveData<InscanDetailsSaveModel>
        get() = _repo.inscanSaveLiveData
    fun getInScanDetails(companyId: String, userCode: String, branchCode: String, manifestNo:String,manifestType:String,sessionId:String){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getInScanDetails(companyId,userCode, branchCode,manifestNo,manifestType,sessionId)
        }
    }
    fun getDamageReasonList(companyId: String,){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getDamageReasonList(companyId)
        }
    }
    fun saveInScanDetailsWithoutScan(
        companyid:String,
        manifestno:String,
        mawbno:String,
        branchcode:String,
        receivedt:String,
        receivetime:String,
        vehiclecode:String,
        remarks:String,
        grno:String,
        mfpckgs:String,
        pckgs:String,
        weight:String,
        damagepckgs:String,
        damagereasoncode:String,
        detailremarks:String,
        usercode:String,
        menucode:String,
        sessionid:String,
        fromstncode:String,
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.saveInscanDetails(
                companyid,
                manifestno,
                mawbno,
                branchcode,
                receivedt,
                receivetime,
                vehiclecode,
                remarks,
                grno,
                mfpckgs,
                pckgs,
                weight,
                damagepckgs,
                damagereasoncode,
                detailremarks,
                usercode,
                menucode,
                sessionid,
                fromstncode,
            )
        }
    }
}