package com.greensoft.greentranserpnative.ui.operation.pickup_reference

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.models.PickupRefModel
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.models.SinglePickupRefModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PickupReferenceViewModel @Inject constructor(private val _repository: PickupReferenceRepository) : BaseViewModel() {
    init {
        isError = _repository.isError
    }

    val viewDialogLiveData: LiveData<Boolean>
        get()= _repository.viewDialogMutData

    val pickupRefLiveData: LiveData<ArrayList<PickupRefModel>>
        get() = _repository.pickupRefLiveData

    val singleRefLiveData: LiveData<ArrayList<SinglePickupRefModel>>
        get() = _repository.singleRefLiveData

    fun getPickupRefList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getPickupRefData(companyId,spname, param,values)
        }
    }

    fun getSingleRefData( companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getSingleRefData(companyId,spname, param,values)
        }
    }

}