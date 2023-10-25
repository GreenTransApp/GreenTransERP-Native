package com.greensoft.greentranserpnative.ui.operation.pickup_manifest

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel

import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PickupManifestViewModel @Inject constructor(private val _repo: PickupManifestRepository) : BaseViewModel() {
    init {
        isError = _repo.isError
    }

    val viewDialogLiveData: LiveData<Boolean>
        get()= _repo.viewDialogLiveData
    val branchLiveData: LiveData<ArrayList<BranchSelectionModel>>
        get() = _repo.branchLiveData
    val pickupLocationLiveData: LiveData<ArrayList<PickupLocationModel>>
        get() = _repo.pickupLocationLiveData
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

    val manifestLiveData: LiveData<SavePickupManifestModel>
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

    fun getGrList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getGrList(companyId,spname, param,values)
        }
    }
    fun getVehicleType( companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getVehicleTypeList(companyId,spname, param,values)
        }
    }

    fun savePickupManifest(
        branchcode :String ,
        manifestdt :String ,
        time :String ,
        manifestno :String ,
        modecode :String ,
        tost :String ,
        drivercode :String ,
        drivermobileno :String ,
        loadedby :String ,
        remarks :String ,
        ispickupmanifest :String ,
        grno :String ,
        pckgs :String ,
        aweight :String ,
        goods :String ,
        packing :String ,
        areacode :String ,
        vendcoe :String ,
        loadedbytype :String ,
        menucode :String ,
        usercode :String ,
        sessionid :String ,
        paymentnotapplicable :String ,
        skipinscan :String ,

        ){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.savePickupManifest(
                branchcode,
                manifestdt,
                time,
                manifestno,
                modecode,
                tost,
                drivercode,
                drivermobileno,
                loadedby,
                remarks,
                ispickupmanifest,
                grno,
                pckgs,
                aweight,
                goods,
                packing,
                areacode,
                vendcoe,
                loadedbytype,
                menucode,
                usercode,
                sessionid,
                paymentnotapplicable,
                skipinscan,
            )
        }
    }
}