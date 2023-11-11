package com.greensoft.greentranserpnative.ui.operation.communicationList

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.operation.communicationList.models.CommunicationListModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunicatonListViewModel @Inject constructor(private val _repository: CommunicationListRepository):
    BaseViewModel() {

    init {
        isError = _repository.isError
    }

    val communicationListLiveData : LiveData<ArrayList<CommunicationListModel>>
        get() = _repository.communicationListLiveData

    val viewDialogLiveData: LiveData<Boolean>
        get()= _repository.viewDialogLiveData

    fun getCommunicationList(companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO){
            _repository.getCommunicationList(companyId,spname, param,values)
        }
    }
}