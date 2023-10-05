package com.greensoft.greentranserpnative.ui.operation.grList

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.operation.grList.models.GrListModel
import com.greensoft.greentranserpnative.ui.operation.grList.models.PrintStickerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class GrListViewModel  @Inject constructor(private val _repository: GrListRepository) :BaseViewModel() {

    init {
        isError = _repository.isError
    }

    val grListLivedata : LiveData<ArrayList<GrListModel>>
        get() = _repository.grListLivedata

    val viewDialogLiveData: LiveData<Boolean>
        get()= _repository.viewDialogLiveData
    val printStickerLiveData: LiveData<ArrayList<PrintStickerModel>>
        get() = _repository.printStickerLiveData

    fun getGrList(companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO){
            _repository.getGrList(companyId,spname, param,values)
        }
    }

    fun getStickerForPrint(companyId:String,spname: String,param:List<String>, values:ArrayList<String>) {
        viewModelScope.launch(Dispatchers.IO){
            _repository.getStickerForPrint(companyId,spname, param,values)
        }
    }
}