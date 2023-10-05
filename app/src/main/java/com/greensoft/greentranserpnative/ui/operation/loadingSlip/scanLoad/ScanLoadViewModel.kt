package com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad.models.LoadingSlipHeaderDataModel
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad.models.StickerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanLoadViewModel @Inject constructor( private val _repo: ScanLoadRepository)  : BaseViewModel() {

    init {
        isError = _repo.isError
    }
    val headerLivedata : LiveData<LoadingSlipHeaderDataModel>
        get() = _repo.headerLivedata
    val stickerListLivedata : LiveData<ArrayList<StickerModel>>
        get() = _repo.stickerListLivedata

    fun getScanSticker(companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getScanSticker(companyId,spname, param,values)
        }
    }
}