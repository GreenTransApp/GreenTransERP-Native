package com.greensoft.greentranserpnative.ui.operation.bookingWithoutIndent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.operation.booking.models.SaveBookingModel
import com.greensoft.greentranserpnative.ui.operation.bookingWithoutIndent.model.SaveDirectBookingModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class DirectBookingRepository @Inject constructor(): BaseRepository(){
    private val saveBookingMuteLiveData = MutableLiveData<SaveDirectBookingModel>()
    val saveBookingLiveData: LiveData<SaveDirectBookingModel>
        get() = saveBookingMuteLiveData
    fun saveDirectBooking(companyId:String,
                          branchcode          :String,
                          bookingdt           :String,
                          time                :String,
                          grno               :String,
                          custcode            :String,
                          destcode            :String,
                          productcode         :String,
                          pckgs               :String,
                          aweight             :String,
                          vweight             :String,
                          cweight             :String,
                          createid            :String,
                          sessionid           :String,
                          refno               :String,
                          cngr                :String,
                          cngraddress         :String,
                          cngrcity            :String,
                          cngrzipcode         :String,
                          cngrstate           :String,
                          cngrmobileno        :String,
                          cngremailid         :String,
                          cngrCSTNo           :String,
                          cngrLSTNo           :String,
                          cngrTINNo           :String,
                          cngrSTaxRegNo       :String,
                          cnge                :String,
                          cngeaddress         :String,
                          cngecity            :String,
                          cngezipcode         :String,
                          cngestate           :String,
                          cngemobileno        :String,
                          cngeemailid         :String,
                          cngeCSTNo           :String,
                          cngeLSTNo           :String,
                          cngeTINNo           :String,
                          cngeSTaxRegNo       :String,
                          transactionid       :String,
                          mawbchargeapplicable:String,
                          custdeptid          :String,
                          referencenostr      :String,
                          weightstr           :String,
                          packagetypestr      :String,
                          tempuraturestr      :String,
                          packingstr          :String,
                          goodsstr            :String,
                          dryicestr           :String,
                          dryiceqtystr        :String,
                          dataloggerstr       :String,
                          dataloggernostr     :String,
                          dimlength           :String,
                          dimbreath           :String,
                          dimheight           :String,
                          pickupboyname       :String,
                          boyid               :String,
                          boxnostr            :String,
                          stockitemcodestr    :String,
                          gelpackstr          :String,
                          gelpackitemcodestr  :String,
                          gelpackqtystr       :String,
                          menucode            :String,
                          invoicenostr        :String,
                          invoicedtstr        :String,
                          invoicevaluestr     :String,
                          ewaybillnostr       :String,
                          ewaybilldtstr       :String,
                          ewbvaliduptodtstr   :String,
                          vendorcode          :String,
                          pckgsstr            :String,
                          pickupby            :String,
                          vehicleno           :String,
                          vweightstr          :String,
                          vehiclecode         :String,
                          cngrcode            :String,
                          cngecode            :String,
                          remarks             :String,
                          cngrgstno           :String,
                          cngegstno           :String,
                          cngrtelno           :String,
                          cngetelno           :String,
                          orgpincode          :String,
                          destpincode         :String,
                          pickuppoint         :String,
                          deliverypoint       :String,)
    {
        viewDialogMutData.postValue(true)
        api.SaveDirectBooking(
            companyId,
            branchcode          ,
            bookingdt           ,
            time                ,
            grno                ,
            custcode            ,
            destcode            ,
            productcode         ,
            pckgs               ,
            aweight             ,
            vweight             ,
            cweight             ,
            createid            ,
            sessionid           ,
            refno               ,
            cngr                ,
            cngraddress         ,
            cngrcity            ,
            cngrzipcode         ,
            cngrstate           ,
            cngrmobileno        ,
            cngremailid         ,
            cngrCSTNo           ,
            cngrLSTNo           ,
            cngrTINNo           ,
            cngrSTaxRegNo       ,
            cnge                ,
            cngeaddress         ,
            cngecity            ,
            cngezipcode         ,
            cngestate           ,
            cngemobileno        ,
            cngeemailid         ,
            cngeCSTNo           ,
            cngeLSTNo           ,
            cngeTINNo           ,
            cngeSTaxRegNo       ,
            transactionid       ,
            mawbchargeapplicable,
            custdeptid          ,
            referencenostr      ,
            weightstr           ,
            packagetypestr      ,
            tempuraturestr      ,
            packingstr          ,
            goodsstr            ,
            dryicestr           ,
            dryiceqtystr        ,
            dataloggerstr       ,
            dataloggernostr     ,
            dimlength           ,
            dimbreath           ,
            dimheight           ,
            pickupboyname       ,
            boyid               ,
            boxnostr            ,
            stockitemcodestr    ,
            gelpackstr          ,
            gelpackitemcodestr  ,
            gelpackqtystr       ,
            menucode            ,
            invoicenostr        ,
            invoicedtstr        ,
            invoicevaluestr     ,
            ewaybillnostr       ,
            ewaybilldtstr       ,
            ewbvaliduptodtstr   ,
            vendorcode          ,
            pckgsstr            ,
            pickupby            ,
            vehicleno           ,
            vweightstr          ,
            vehiclecode         ,
            cngrcode            ,
            cngecode            ,
            remarks             ,
            cngrgstno           ,
            cngegstno           ,
            cngrtelno           ,
            cngetelno           ,
            orgpincode          ,
            destpincode         ,
            pickuppoint         ,
            deliverypoint       ,
        )
            .enqueue(object: Callback<CommonResult> {
                override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                    if(response.body() != null){
                        val result = response.body()!!
                        val gson = Gson()
                        if(result.CommandStatus == 1){
                            val jsonArray = getResult(result);
                            if(jsonArray != null) {
                                val listType = object: TypeToken<List<SaveDirectBookingModel>>() {}.type
                                val resultList: ArrayList<SaveDirectBookingModel> = gson.fromJson(jsonArray.toString(), listType);
                                saveBookingMuteLiveData.postValue(resultList[0])
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