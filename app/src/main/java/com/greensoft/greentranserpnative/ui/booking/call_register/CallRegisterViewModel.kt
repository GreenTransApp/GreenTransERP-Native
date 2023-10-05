package com.greensoft.greentranserpnative.ui.booking.call_register

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.booking.call_register.models.CallRegisterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CallRegisterViewModel @Inject constructor(private val _repo:CallRegisterRepository) :BaseViewModel(){
    init {
        isError=_repo.isError
    }
    val viewDialogLiveData: LiveData<Boolean>
        get()= _repo.viewDialogLiveData
    val callRegisterLiveData: LiveData<ArrayList<CallRegisterModel>>
        get() = _repo.callRegisterLiveData


     fun getCallRegisterList(companyId:String, spname: String, param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getCallRegisterData(companyId,spname, param,values)
        }
    }

}