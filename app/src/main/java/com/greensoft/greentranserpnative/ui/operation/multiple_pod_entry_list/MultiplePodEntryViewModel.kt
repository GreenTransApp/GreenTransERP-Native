package com.greensoft.greentranserpnative.ui.operation.multiple_pod_entry_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.operation.multiple_pod_entry_list.models.RelationListModel
import com.greensoft.greentranserpnative.ui.operation.pending_for_delivery_update_list.models.PodEntryListModel
import com.greensoft.greentranserpnative.ui.operation.pod_entry.models.PodSaveModel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MultiplePodEntryViewModel @Inject constructor(private val _repo: MultiplePodEntryRepository):
    BaseViewModel() {

    init {
        isError = _repo.isError
    }

    val viewDialogLiveData: LiveData<Boolean>
        get()= _repo.viewDialogLiveData

    val relationLiveData: LiveData<ArrayList<RelationListModel>>
        get() = _repo.relationLiveData

    val grListLiveData: LiveData<ArrayList<PodEntryListModel>>
        get() = _repo.grListLiveData

    val saveLiveData: LiveData<PodSaveModel>
        get() = _repo.podSaveLiveData


    fun getRelation( companyId:String){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getRelationList(companyId)
        }
    }

    fun getGrListForPod(companyId: String, drsNo: String){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getGrListForPod(companyId, drsNo)
        }
    }


    fun savePodEntry( companyId:String,
                      loginBranchCode: String,
                      grNo: String,
                      dlvTime: String,
                      name: String,
                      dlvDt: String,
                      relation: String,
                      phoneNo: String,
                      sign: String,
                      stamp: String,
                      podImage: String,
                      signImage: String,
                      remarks: String,
                      userCode: String,
                      sessionId: String,
                      delayReason: String,
                      menuCode: String,
                      deliveryBoy: String,
                      boyId: String,
                      podDt: String,
                      pckgs: String,
                      pckgsStr: String,
                      dataIdStr: String,){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.savePodEntry( companyId,
                loginBranchCode,
                grNo,
                dlvTime,
                name,
                dlvDt,
                relation,
                phoneNo,
                sign,
                stamp,
                podImage,
                signImage,
                remarks,
                userCode,
                sessionId,
                delayReason,
                menuCode,
                deliveryBoy,
                boyId,
                podDt,
                pckgs,
                pckgsStr,
                dataIdStr,)
        }
    }

}