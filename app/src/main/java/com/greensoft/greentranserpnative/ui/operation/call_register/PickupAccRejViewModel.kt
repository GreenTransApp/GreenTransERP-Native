package com.greensoft.greentranserpnative.ui.operation.call_register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.operation.call_register.models.CallRegisterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PickupAccRejViewModel @Inject constructor(private val _repo: CallRegisterRepository) :
    BaseViewModel(){
    init {
        isError=_repo.isError
    }
    val viewDialogLiveData: LiveData<Boolean>
        get()= _repo.viewDialogLiveData
    val acceptPickupLiveData: LiveData<String>
        get() = _repo.acceptPickupLiveData
    val rejectPickupLiveData: LiveData<String>
        get() = _repo.rejectPickupLiveData

    val testLiveData = MutableLiveData<Boolean>()


    fun acceptPickup(companyId:String, transactionId: String, pickupDate: String, pickupTime: String, pickupRemarks: String, sendMsgToCust: String){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.acceptPickup(companyId, transactionId, pickupDate, pickupTime, pickupRemarks, sendMsgToCust)
        }
    }

    fun rejectPickup(companyId:String, transactionId: String, rejectRemarks: String){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.rejectPickup(companyId, transactionId, rejectRemarks)
        }
    }

    fun closeBottomSheet() {
        testLiveData.postValue(true)
    }



}