package com.greensoft.greentranserpnative.ui.operation.scan_and_delivery

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.operation.pod_entry.PodEntryRepository
import com.greensoft.greentranserpnative.ui.operation.pod_entry.models.PodEntryModel
import com.greensoft.greentranserpnative.ui.operation.pod_entry.models.RelationListModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ScanAndDeliveryViewModel @Inject constructor(private val _repo: ScanAndDeliveryRepository) : BaseViewModel() {

    init {
        isError = _repo.isError
    }

    val viewDialogLiveData: LiveData<Boolean>
        get()= _repo.viewDialogLiveData

    val podDetailsLiveData: LiveData<PodEntryModel>
        get() = _repo.podLiveData
    val relationLiveData: LiveData<ArrayList<RelationListModel>>
        get() = _repo.relationLiveData


    fun getPodDetails( companyId:String,grNo:String){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getPodData(companyId,grNo)
        }
    }
    fun getRelation( companyId:String){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getRelationList(companyId)
        }
    }
    }
