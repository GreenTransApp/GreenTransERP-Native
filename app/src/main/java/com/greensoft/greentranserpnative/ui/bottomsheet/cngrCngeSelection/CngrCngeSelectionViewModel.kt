package com.greensoft.greentranserpnative.ui.bottomsheet.cngrCngeSelection

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.bottomsheet.cngrCngeSelection.model.CngrCngeSelectionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CngrCngeSelectionViewModel @Inject constructor(private var _repo:CngrCngeSelectionRepository):BaseViewModel(){

    init {
        isError = _repo.isError
    }

    val cngrCngeLiveData:LiveData<ArrayList<CngrCngeSelectionModel>>
        get() = _repo.cngrCngeLiveData

    fun getCngrCngeList(companyId:String?,branchCode:String?,userCode:String?,custCode: String?,cngrCnge:String?,sessionId:String?,charStr:String?){
        viewModelScope.launch (Dispatchers.IO){
            _repo.getCngrCngrist(companyId,branchCode,userCode,custCode,cngrCnge, sessionId,charStr)
        }
    }

}