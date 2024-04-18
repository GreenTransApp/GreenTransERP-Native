package com.greensoft.greentranserpnative.ui.operation.despatch_manifest

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.DestinationSelectionModel
import com.greensoft.greentranserpnative.ui.operation.despatch_manifest.models.DespatchSaveModel
import com.greensoft.greentranserpnative.ui.operation.despatch_manifest.models.FlightModeCodeModel
import com.greensoft.greentranserpnative.ui.operation.despatch_manifest.models.GroupModeCodeModel
import com.greensoft.greentranserpnative.ui.operation.despatch_manifest.models.ToStationModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.BranchSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.DriverSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.GrSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.PickupLocationModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.SavePickupManifestModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.VehicleSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.VehicleTypeModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.VendorSelectionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DespatchManifestViewModel @Inject constructor(private val _repo: DespatchManifestRepository) : BaseViewModel() {

    init {
        isError = _repo.isError
    }

    val viewDialogLiveData: LiveData<Boolean>
        get()= _repo.viewDialogLiveData
    val branchLiveData: LiveData<ArrayList<BranchSelectionModel>>
        get() = _repo.branchLiveData
    val pickupLocationLiveData: LiveData<ArrayList<PickupLocationModel>>
        get() = _repo.pickupLocationLiveData
    val toStationLiveData: LiveData<ArrayList<ToStationModel>>
        get() = _repo.toStationLiveData
    val driverLiveData: LiveData<ArrayList<DriverSelectionModel>>
        get() = _repo.driverLiveData

    val vendorLiveData: LiveData<ArrayList<VendorSelectionModel>>
        get() = _repo.vendorLiveData

    val vehicleLiveData: LiveData<ArrayList<VehicleSelectionModel>>
        get() = _repo.vehicleLiveData

    val grLiveData: LiveData<ArrayList<GrSelectionModel>>
        get() = _repo.grLiveData
    val vehicleTypeLiveData: LiveData<ArrayList<VehicleTypeModel>>
        get() = _repo.vehicleTypeLiveData
 val groupModeLiveData: LiveData<ArrayList<GroupModeCodeModel>>
        get() = _repo.groupModeLiveData
    val modeCodeLiveData: LiveData<ArrayList<FlightModeCodeModel>>
        get() = _repo.modeCodeLiveData

    val manifestLiveData: LiveData<DespatchSaveModel>
        get() = _repo.saveManifestLiveData


    fun getBranchList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getBranchList(companyId,spname, param,values)
        }
    }
    fun getPickupLocation( companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getPickupLocation(companyId,spname, param,values)
        }
    }
    fun getToStationList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getDestinationList(companyId,spname, param,values)
        }
    }

    fun getDriverList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getDriverList(companyId,spname, param,values)
        }
    }

    fun getVendorList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getVendorList(companyId,spname, param,values)
        }
    }
    fun getVehicleList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getVehicleList(companyId,spname, param,values)
        }
    }

    fun getGroupModeList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getGroupModeList(companyId,spname, param,values)
        }
    }
fun getModeCode( companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getModeCodeList(companyId,spname, param,values)
        }
    }

    fun getGrList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getGrList(companyId,spname, param,values)
        }
    }


    fun saveOutstationManifest(
        companyid:String,
        branchcode:String,
        dt:String,
        time:String,
        manifestno:String,
        tost:String,
        modetype:String,
        modecode:String,
        remarks:String,
        drivercode:String,
        drivermobileno:String,
        vendcode:String,
        loadingnostr:String,
        sessionid:String,
        usercode:String,
        menucode:String,
        loadedby:String,
        airlineawbno:String,
        airlineawbdt:String,
        airlineawbfreight:String,
        airlineawbpckgs:String,
        airlineawbweight:String,
        vendorcd:String

        ){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.saveOutstationManifest(
                companyid,
                branchcode,
                dt,
                time,
                manifestno,
                tost,
                modetype,
                modecode,
                remarks,
                drivercode,
                drivermobileno,
                vendcode,
                loadingnostr,
                sessionid,
                usercode,
                menucode,
                loadedby,
                airlineawbno,
                airlineawbdt,
                airlineawbfreight,
                airlineawbpckgs,
                airlineawbweight,
                vendorcd
            )
        }
    }
}