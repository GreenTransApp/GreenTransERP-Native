package com.greensoft.greentranserpnative.ui.operation.drs

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.bottomsheet.vehicleSelection.model.VehicleModelDRS
import com.greensoft.greentranserpnative.ui.operation.drs.model.SaveDRSModel
import com.greensoft.greentranserpnative.ui.operation.drs.model.VendorModelDRS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DRSViewModel @Inject constructor(private val _repo:DRSRepository): BaseViewModel() {

    init {
        isError = _repo.isError
    }

    val vendorLiveData: LiveData<ArrayList<VendorModelDRS>>
        get() = _repo.vendorLiveData

    val vehicleLiveData :LiveData<ArrayList<VehicleModelDRS>>
        get() = _repo.vehicleLiveData

    val saveDRSLiveData:LiveData<SaveDRSModel>
        get() = _repo.saveDRSLiveData


    fun getVendorList( companyId:String,charStr:String,category:String){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getVendorList(companyId,charStr, category)
        }
    }

    fun getVehicleList(companyId:String,branchCode:String,userCode:String,charStr:String,vendCode:String, modeType:String){
        viewModelScope.launch(Dispatchers.IO){
            _repo.getVehicleList(companyId,branchCode,userCode,charStr,vendCode,modeType)
        }
    }

    fun saveDRS( companyId:String,branchCode:String,drsDt:String,drsTime:String, drsSno:String,
                 driverName:String, driverTele:String, modeCode:String, remarks:String,grnoStr:String,
                 drnoStr:String,  pckgsStr:String,weightStr:String,remarksStr:String,loadedBy:String,
                 userCode:String,sessionId:String,menuCode:String,executiveId:String,deliveredBy:String,
                 dlvAgentCode:String,dlvVehicleNo:String){

        viewModelScope.launch(Dispatchers.IO){
            _repo.saveDRS(companyId,branchCode,drsDt, drsTime, drsSno,driverName,driverTele,modeCode, remarks,
                grnoStr, drnoStr, pckgsStr, weightStr, remarksStr, loadedBy, userCode, sessionId, menuCode,
                executiveId, deliveredBy, dlvAgentCode, dlvVehicleNo,)
        }

    }
}