package com.greensoft.greentranserpnative.ui.operation.pod_entry

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.operation.pod_entry.models.PodEntryModel
import com.greensoft.greentranserpnative.ui.operation.pod_entry.models.PodSaveModel
import com.greensoft.greentranserpnative.ui.operation.pod_entry.models.RelationListModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PodEntryViewModel  @Inject constructor(private val _repo: PodEntryRepository) : BaseViewModel() {

    init {
        isError = _repo.isError
    }

    val viewDialogLiveData: LiveData<Boolean>
        get()= _repo.viewDialogLiveData

    val podDetailsLiveData: LiveData<PodEntryModel>
        get() = _repo.podLiveData
   val relationLiveData: LiveData<ArrayList<RelationListModel>>
        get() = _repo.relationLiveData

    val saveLiveData: LiveData<PodSaveModel>
        get() = _repo.podSaveLiveData


    fun getPodDetails( companyId:String,grNo:String){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getGrDetail(companyId,grNo)
        }
    }
    fun getRelation( companyId:String){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getRelationList(companyId)
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