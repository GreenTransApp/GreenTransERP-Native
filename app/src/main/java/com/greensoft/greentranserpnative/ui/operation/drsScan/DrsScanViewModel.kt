package com.greensoft.greentranserpnative.ui.operation.drsScan

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.operation.drsScan.model.RemoveStickerResponseModel
import com.greensoft.greentranserpnative.ui.operation.drsScan.model.ScannedDrsModel
import com.greensoft.greentranserpnative.ui.operation.drsScan.model.UpdateStickerResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrsScanViewModel  @Inject constructor(private val _repo: DrsScanRepository):BaseViewModel() {

    init {
        isError = _repo.isError
    }

    val updateStickerLiveData:LiveData<UpdateStickerResponseModel>
        get() = _repo.updateStickerLiveData

    val removeStickerLiveData:LiveData<RemoveStickerResponseModel>
        get() = _repo.removeStickerLiveData

    val stickerListLiveData: LiveData<ArrayList<ScannedDrsModel>>
        get() = _repo.stickerListLiveData

    val viewDialogLiveData: LiveData<Boolean>
        get()= _repo.viewDialogLiveData



    fun updateSticker( companyId:String,userCode:String,loginBranchCode:String,branchCode:String, manifestNo:String,
                       drsDt:String,drsTime:String, modeCode:String,vendCode:String,custCode:String, remarks:String,
                       stickerNo:String,deliveredBy:String,agentCode:String,vehicleNo:String,sessionId:String){

        viewModelScope.launch(Dispatchers.IO){
            _repo.updateSticker(companyId,userCode,loginBranchCode,branchCode,manifestNo,drsDt, drsTime,modeCode,vendCode,
                custCode, remarks,stickerNo,deliveredBy, agentCode, vehicleNo,sessionId,)
        }
    }

    fun removeSticker(companyId:String,userCode:String,loginBranchCode:String,manifestNo:String,stickerNo:String,sessionId:String){
        viewModelScope.launch(Dispatchers.IO){
            _repo.removeSticker(companyId,userCode,loginBranchCode,manifestNo,stickerNo,sessionId)
        }
    }

    fun getDrsStickers(companyId:String,userCode:String,loginBranchCode:String,manifestNo:String,sessionId:String){
        viewModelScope.launch(Dispatchers.IO){
            _repo.getDrsStickerList(companyId,userCode,loginBranchCode,manifestNo,sessionId)
        }
    }

    fun getDrsData(companyId:String,userCode:String,loginBranchCode:String,manifestNo:String,sessionId:String){
        viewModelScope.launch(Dispatchers.IO){
            _repo.getDrsDataWithDrsNo(companyId,userCode,loginBranchCode,manifestNo,sessionId)
        }
    }
}