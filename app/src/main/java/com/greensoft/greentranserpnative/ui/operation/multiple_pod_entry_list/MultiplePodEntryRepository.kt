package com.greensoft.greentranserpnative.ui.operation.multiple_pod_entry_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.operation.pending_for_delivery_update_list.models.PodEntryListModel

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MultiplePodEntryRepository  @Inject constructor() : BaseRepository(){
    val viewDialogLiveData: LiveData<Boolean>
        get() = viewDialogMutData






}