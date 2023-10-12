package com.greensoft.greentranserpnative.ui.operation.booking

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.AgentSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.BookingVehicleSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.ConsignorSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.ContentSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.CustomerSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.DepartmentSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.DestinationSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.GelPackItemSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.OriginSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.PackingSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.PckgTypeSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.PickupBoySelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.PickupBySelection
import com.greensoft.greentranserpnative.ui.operation.booking.models.ServiceTypeSelectionLov
import com.greensoft.greentranserpnative.ui.operation.booking.models.TemperatureSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.models.SinglePickupRefModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(private val _repository: BookingRepository) : BaseViewModel() {
    init {
        isError = _repository.isError
    }
    val viewDialogLiveData: LiveData<Boolean>
        get()= _repository.viewDialogMutData

    val custLiveData: LiveData<ArrayList<CustomerSelectionModel>>
        get() = _repository.custLiveData

    val cngrLiveData: LiveData<ArrayList<ConsignorSelectionModel>>
        get() = _repository.cngrLiveData

    val deptLiveData: LiveData<ArrayList<DepartmentSelectionModel>>
        get() = _repository.deptLiveData

    val originLiveData: LiveData<ArrayList<OriginSelectionModel>>
        get() = _repository.originLiveData

    val destinationLiveData: LiveData<ArrayList<DestinationSelectionModel>>
        get() = _repository.destinationLiveData

    val pickupBoyLiveData: LiveData<ArrayList<PickupBoySelectionModel>>
        get() = _repository.pickupBoyLiveData

    val tempLiveData: LiveData<ArrayList<TemperatureSelectionModel>>
        get() = _repository.tempLiveData

    val contentLiveData: LiveData<ArrayList<ContentSelectionModel>>
        get() = _repository.contentLiveData

    val packingLiveData: LiveData<ArrayList<PackingSelectionModel>>
        get() = _repository.packingLiveData

    val gelPackItemLiveData: LiveData<ArrayList<GelPackItemSelectionModel>>
        get() = _repository.gelPackLiveData
    val vendorLiveData: LiveData<ArrayList<AgentSelectionModel>>
        get() = _repository.vendorLiveData
    val agentLiveData: LiveData<ArrayList<AgentSelectionModel>>
        get() = _repository.agentLiveData
    val vehicleLiveData: LiveData<ArrayList<BookingVehicleSelectionModel>>
        get() = _repository.vehicleLiveData
    val pickupByLiveData: LiveData<ArrayList<PickupBySelection>>
        get() = _repository.pickupByLiveData

    val singleRefLiveData: LiveData<ArrayList<SinglePickupRefModel>>
        get() = _repository.singleRefLiveData
    val ServiceTypeLiveData: LiveData<ArrayList<ServiceTypeSelectionLov>>
        get() = _repository.serviceListLiveData
    val pckgLiveData: LiveData<ArrayList<PckgTypeSelectionModel>>
        get() = _repository.pckgTypeLiveData

    fun getCustomerList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getCustomerList(companyId,spname, param,values)
        }
    }

    fun getCngrList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getConsignorDetails(companyId,spname, param,values)
        }
    }
    fun getDeptList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getDepartmentDetails(companyId,spname, param,values)
        }
    }

    fun getOriginList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getOriginData(companyId,spname, param,values)
        }
    }

    fun getDestinationList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getDestinationData(companyId,spname, param,values)
        }
    }

    fun getPickupBoyList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getPickupBoyDetail(companyId,spname, param,values)
        }
    }

    fun getTemperatureList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getTemperatureLov(companyId,spname, param,values)
        }
    }

    fun getContentLov( companyId:String,spname: String,param:List<String>, values:ArrayList<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getContentLov(companyId, spname, param, values)
        }
    }

    fun getPackingLov( companyId:String,spname: String,param:List<String>, values:ArrayList<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getPackingLov(companyId, spname, param, values)
        }
    }
    fun getGelPackLov( companyId:String,spname: String,param:List<String>, values:ArrayList<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getGelPackLov(companyId, spname, param, values)
        }
    }
    fun getVendorLov( companyId:String,spname: String,param:List<String>, values:ArrayList<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getVendorLov(companyId, spname, param, values)
        }
    }
    fun getAgentLov( companyId:String,spname: String,param:List<String>, values:ArrayList<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getAgentLov(companyId, spname, param, values)
        }
    }
    fun getVehicleLov( companyId:String,spname: String,param:List<String>, values:ArrayList<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getVehicleLov(companyId, spname, param, values)
        }
    }
    fun getPickupByLov( companyId:String,spname: String,param:List<String>, values:ArrayList<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getPickupByLov(companyId, spname, param, values)
        }
    }
    fun getSingleRefData( companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getSingleRefData(companyId,spname, param,values)
        }
    }
    fun getServiceList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getServiceType(companyId,spname, param,values)
        }
    }

    fun getPckgType( companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getPckgTypeLov(companyId,spname, param,values)
        }
    }
}