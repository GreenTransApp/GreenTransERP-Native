package com.greensoft.greentranserpnative.ui.operation.chatScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.operation.chatScreen.models.ChatScreenModel
import com.greensoft.greentranserpnative.ui.operation.chatScreen.models.CustomerDetailModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ChatScreenViewModel @Inject constructor(private val _repository: ChatScreenRepository):BaseViewModel(){
    init {
        isError = _repository.isError
    }

    val getChatListLiveData : LiveData<ArrayList<ChatScreenModel>>
        get() = _repository.getChatListLiveData
    val addChatListLiveData : LiveData<ArrayList<ChatScreenModel>>
        get() = _repository.addChatListLiveData

    val deleteChatListLiveData : LiveData<ArrayList<ChatScreenModel>>
        get() = _repository.deleteChatListLiveData
     val custDetailLiveData : LiveData<CustomerDetailModel>
        get() = _repository.custDetailLiveData


    val viewDialogLiveData: LiveData<Boolean>
        get()= _repository.viewDialogLiveData

    fun getChats(companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO){
            _repository.getChats(companyId,spname, param,values)
        }
    }
    fun addChats(companyId:String, spname: String, param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO){
            _repository.addChats(companyId,spname, param,values)
        }
    }
    fun deleteChats(companyId:String, spname: String, param:List<String>, values:ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO){
            _repository.deleteChats(companyId,spname, param,values)
        }
    }
}