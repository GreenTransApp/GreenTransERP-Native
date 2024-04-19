package com.greensoft.greentranserpnative.ui.bottomsheet.destinationBottomSheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DestinationSelectionViewModel  @Inject constructor(private val _repo: DestinationSelectionRepository) : BaseViewModel(){
    val toStationLiveData: LiveData<ArrayList<ToStationModel>>
        get() = _repo.toStationLiveData

    fun getToStationList( companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getDestinationList(companyId,spname, param,values)
        }
    }
}