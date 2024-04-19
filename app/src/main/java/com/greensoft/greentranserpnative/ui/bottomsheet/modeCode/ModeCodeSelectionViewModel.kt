package com.greensoft.greentranserpnative.ui.bottomsheet.modeCode

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.operation.despatch_manifest.DespatchManifestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModeCodeSelectionViewModel
@Inject constructor(private val _repo: ModeCodeSelectionRepository) : BaseViewModel()
{

    init {
        isError = _repo.isError
    }

    val modeCodeLiveData: LiveData<ArrayList<ModeCodeSelectionModel>>
        get() = _repo.modeCodeLiveData



    fun getModeCode( companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getModeCodeList(companyId,spname, param,values)
        }
    }
}