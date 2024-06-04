package com.greensoft.greentranserpnative.ui.operation.pending_for_delivery_update_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.operation.pending_for_delivery_update_list.models.DrsPendingListModel
import com.greensoft.greentranserpnative.ui.operation.pending_for_delivery_update_list.models.PodEntryListModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PendingDeliveryDrsListViewModel @Inject constructor(private val _repo: PendingDeliveryDrsListRepository):
    BaseViewModel()  {

    init {
        isError = _repo.isError
    }
    val viewDialogLiveData: LiveData<Boolean>
        get()= _repo.viewDialogLiveData

    val drsPendingListLiveData: LiveData<ArrayList<DrsPendingListModel>>
        get() = _repo.drsPendingListLivedata


//    val podListLiveData: LiveData<ArrayList<PodEntryListModel>>
//        get() = _repo.podListLivedata


//    fun getPodEntryList(companyId: String,drsNo:String){
//        viewModelScope.launch(Dispatchers.IO) {
//            _repo.getPodList(companyId,drsNo)
//        }
//    }
    fun getDrsPendingList(companyId: String,userCode:String,loginBranchCode:String,fromDt:String,toDt:String,sessionId:String){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getDrsPendingList(companyId,userCode,loginBranchCode,fromDt,toDt,sessionId)
        }
    }


}