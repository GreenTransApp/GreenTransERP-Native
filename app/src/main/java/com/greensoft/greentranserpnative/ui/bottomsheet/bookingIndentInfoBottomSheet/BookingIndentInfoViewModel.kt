package com.greensoft.greentranserpnative.ui.bottomsheet.bookingIndentInfoBottomSheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.bottomsheet.bookingIndentInfoBottomSheet.model.BookingIndentInfoModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingIndentInfoViewModel  @Inject constructor(private val _repo:BookingIndentInfoRepository):BaseViewModel(){

    init {
        isError = _repo.isError
    }

    val bookingIndentInfoLiveData: LiveData<BookingIndentInfoModel>
        get() = _repo.bookingIndentLiveData

    fun getBookingIndentInfo(companyId:String,transactionId:String){
        viewModelScope.launch(Dispatchers.IO){
            _repo.getBookingInScanDetails(companyId,transactionId)
        }
    }
}