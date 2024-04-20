package com.greensoft.greentranserpnative.ui.bottomsheet.bookingIndentInfoBottomSheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.bottomsheet.bookingIndentInfoBottomSheet.model.BookingIndentInfoModel

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class BookingIndentInfoRepository@Inject constructor(): BaseRepository() {

    private val bookingIndentInfoMutData = MutableLiveData<BookingIndentInfoModel>()

    val bookingIndentLiveData: LiveData<BookingIndentInfoModel>
        get() = bookingIndentInfoMutData

    fun getBookingInScanDetails(companyId: String,transactionId:String){
        viewDialogMutData.postValue(true)
        api.getBookingIndentInfo(companyId,transactionId).enqueue(object:Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<BookingIndentInfoModel>>() {}.type
                            val resultList: ArrayList<BookingIndentInfoModel> = gson.fromJson(jsonArray.toString(), listType);
                            bookingIndentInfoMutData.postValue(resultList[0]);
                        }

                    } else {
                        isError.postValue(result.CommandMessage.toString());
                    }
                } else {
                    isError.postValue(SERVER_ERROR);
                }
                viewDialogMutData.postValue(false)

            }

            override fun onFailure(call: Call<CommonResult?>, t: Throwable) {
                viewDialogMutData.postValue(false)
                isError.postValue(t.message)
            }

        })
    }


}