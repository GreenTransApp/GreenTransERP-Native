package com.greensoft.greentranserpnative.ui.bottomsheet.commonSelection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.ui.bottomsheet.commonSelection.model.CommonModel

import javax.inject.Inject

class CommonSelectionRepository @Inject constructor():BaseRepository() {

    private val itemMuteLiveData = MutableLiveData<ArrayList<CommonModel>>()

    val itemLiveData :LiveData<ArrayList<CommonModel>>
        get() = itemMuteLiveData


}