package com.greensoft.greentranserpnative.ui.bottomsheet.vendorSelection

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.bottomsheet.vendorSelection.model.VendorModelDRS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VendorSelectionViewModel @Inject constructor(private var _repo:VendorSelectionRepository):BaseViewModel(){
    init {
        isError = _repo.isError
    }
    val vendorLiveData : LiveData<ArrayList<VendorModelDRS>>
        get() = _repo.vendorLiveData

    fun getVendorList( companyId:String,charStr:String,category:String){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getVendorList(companyId,charStr, category)
        }
    }
}