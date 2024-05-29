package com.greensoft.greentranserpnative.ui.bottomsheet.pinCodeSelection

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.bottomsheet.pinCodeSelection.model.PinCodeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PinCodeSelectionViewModel @Inject constructor(private var _repo:PinCodeSelectionRepository):BaseViewModel(){

    init {
        isError = _repo.isError
    }
    val pinCodeLiveData :LiveData<ArrayList<PinCodeModel>>
        get() = _repo.pinCodeLiveData

    fun getPinCode(companyId:String,userCode:String,branchCode:String,sessionId:String,menuCode:String,charStr:String){
        viewModelScope.launch (Dispatchers.IO){
            _repo.getPinCodeList(companyId,userCode, branchCode,sessionId,menuCode,charStr)
        }

    }
}