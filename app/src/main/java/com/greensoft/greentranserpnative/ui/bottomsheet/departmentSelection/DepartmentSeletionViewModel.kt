package com.greensoft.greentranserpnative.ui.bottomsheet.departmentSelection

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.bottomsheet.departmentSelection.model.DepartmentSelectionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DepartmentSeletionViewModel @Inject constructor(private var _repo:DepartmentSelectionRepository)
    :BaseViewModel(){

        init {
            isError = _repo.isError
        }

    val departmentLiveData:LiveData<ArrayList<DepartmentSelectionModel>>
        get() = _repo.departmentLiveData

    fun getDepartmentList(companyId:String?,userCode:String?,sessionId:String?,custCode: String?,menuCode:String?,originCode:String?,charStr:String?){
        viewModelScope.launch (Dispatchers.IO){
            _repo.getDepartmentList(companyId,userCode, custCode,sessionId,menuCode,originCode,charStr)
        }
    }
}