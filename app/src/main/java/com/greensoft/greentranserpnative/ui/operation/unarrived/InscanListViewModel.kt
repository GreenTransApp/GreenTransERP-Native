package com.greensoft.greentranserpnative.ui.operation.unarrived

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.operation.unarrived.models.InscanListModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InscanListViewModel @Inject constructor(private val _repository: InscanListRepository) : BaseViewModel() {
    init {
        isError = _repository.isError
    }
    val viewDialogLiveData: LiveData<Boolean>
        get()= _repository.viewDialogMutData


    val inscanListLiveData: LiveData<ArrayList<InscanListModel>>
        get() = _repository.inscanListLiveData


    fun getInscanList(companyId: String, branchCode: String,fromBranchCode:String,fromDt:String,toDt:String,manifestType:String,modeType:String){
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getInscanList(companyId, branchCode,fromBranchCode, fromDt, toDt, manifestType, modeType)
        }
    }

}