package com.greensoft.greentranserpnative.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {

    @Inject
    protected lateinit var baseRepo: BaseRepository
    lateinit var isError: LiveData<String>
//    get() = baseRepo.isError
}