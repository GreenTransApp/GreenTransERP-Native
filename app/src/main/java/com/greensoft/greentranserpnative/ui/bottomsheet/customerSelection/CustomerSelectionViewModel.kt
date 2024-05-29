package com.greensoft.greentranserpnative.ui.bottomsheet.customerSelection

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.bottomsheet.customerSelection.model.CustomerSelectionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerSelectionViewModel @Inject constructor(private var _repo: CustomerSelectionRepository):BaseViewModel(){

    init {
        isError = _repo.isError
    }

    val customerLivedata: LiveData<ArrayList<CustomerSelectionModel>>
        get() = _repo.customerLiveData

    fun getCustomerList(companyId:String,userCode:String,branchCode:String,sessionId:String,menuCode:String,charStr:String){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getCustomerList(companyId,userCode, branchCode,sessionId,menuCode,charStr)
        }
    }

}