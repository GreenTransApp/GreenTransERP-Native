package com.greensoft.greentranserpnative.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.home.models.NotificationModel
import com.greensoft.greentranserpnative.ui.home.models.UserMenuModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val _repository: HomeRepository) :BaseViewModel(){

    init {
        isError=_repository.isError
    }

    val viewDialogLiveData: LiveData<Boolean>
        get()= _repository.viewDialogLiveData
    val menuLiveData: LiveData<ArrayList<UserMenuModel>>
        get() = _repository.menuLiveData

    val notificationLiveData: LiveData<NotificationModel>
        get() = _repository.notificationLiveData

    fun getUserMenu(companyId:String, spname: String, param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getUserMenu(companyId,spname, param,values)
        }
    }
}