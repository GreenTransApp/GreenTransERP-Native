package com.greensoft.greentranserpnative.ui.bottomsheet.printGR

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.bottomsheet.printGR.model.GrDetailForPrintModel
import com.greensoft.greentranserpnative.ui.operation.grList.models.PrintStickerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrintGrViewModel  @Inject constructor(private val _repo: PrintGrRepository):BaseViewModel(){

    init {
        isError = _repo.isError
    }

    val printStickerLiveData: LiveData<ArrayList<PrintStickerModel>>
        get() = _repo.printStickerLiveData

    val grDetailPrintLiveData:LiveData<GrDetailForPrintModel>
        get() = _repo.grDetailPrintLiveData

    fun getStickerForPrint(companyId:String,grNo: String,fromStickerNo: String,toStickerNo:String) {
        viewModelScope.launch(Dispatchers.IO){
            _repo.getStickerForPrint(companyId,grNo, fromStickerNo,toStickerNo)
        }
    }

    fun getGrDetailForPrint(companyId:String,userCode: String,branchCode:String,sessionId:String,grNo:String){
        viewModelScope.launch(Dispatchers.IO){
            _repo.getGrDetailForPrint(companyId,userCode,branchCode,sessionId,grNo)
        }
    }
}