package com.greensoft.greentranserpnative.ui.bottomsheet.vehicleSelection

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.bottomsheet.vehicleSelection.model.VehicleModelDRS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehicleSelectionViewModel @Inject constructor(private val _repo:VehicleSelectionRepository):BaseViewModel() {

    init {
        isError = _repo.isError
    }
    val vehicleLiveData : LiveData<ArrayList<VehicleModelDRS>>
        get() = _repo.vehicleLiveData

    fun getVehicleList(companyId:String,branchCode:String,userCode:String,charStr:String,vendCode:String, modeType:String){
        viewModelScope.launch(Dispatchers.IO){
            _repo.getVehicleList(companyId,branchCode,userCode,charStr,vendCode,modeType)
        }
    }
}