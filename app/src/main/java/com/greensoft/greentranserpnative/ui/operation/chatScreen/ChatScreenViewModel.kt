package com.greensoft.greentranserpnative.ui.operation.chatScreen

import androidx.lifecycle.LiveData
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.operation.chatScreen.models.ChatScreenModel
import com.greensoft.greentranserpnative.ui.operation.grList.models.GrListModel
import javax.inject.Inject

class ChatScreenViewModel @Inject constructor(private val _repository: ChatScreenRepository):BaseViewModel(){
    init {
        isError = _repository.isError
    }

    val chatListLiveData : LiveData<ArrayList<ChatScreenModel>>
        get() = _repository.chatListLiveData

    val viewDialogLiveData: LiveData<Boolean>
        get()= _repository.viewDialogLiveData
}