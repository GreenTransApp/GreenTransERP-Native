package com.greensoft.greentranserpnative.ui.operation.loadingSlip.search_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad.ScanLoadRepository
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.search_list.models.SearchListModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.BranchSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.PickupLocationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchListViewModel  @Inject constructor(private val _repo: SearchListRepository)  : BaseViewModel() {

    init {
        isError = _repo.isError
    }

    val viewDialogLiveData: LiveData<Boolean>
        get() = _repo.viewDialogMutData


    val searchListLiveData: LiveData<ArrayList<SearchListModel>>
        get() = _repo.searchListLiveData

    fun getSearchList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getSearchList(companyId,spname, param,values)
        }
    }
}