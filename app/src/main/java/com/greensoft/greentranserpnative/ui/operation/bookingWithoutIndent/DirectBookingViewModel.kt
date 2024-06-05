package com.greensoft.greentranserpnative.ui.operation.bookingWithoutIndent

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.BoxNoValidateModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.ContentSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.GelPackItemSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.PackingSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.SaveBookingModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.TemperatureSelectionModel
import com.greensoft.greentranserpnative.ui.operation.bookingWithoutIndent.model.SaveDirectBookingModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DirectBookingViewModel @Inject constructor(private val _repository: DirectBookingRepository) :
    BaseViewModel() {

    init {
        isError = _repository.isError
    }

    val viewDialogLiveData: LiveData<Boolean>
        get() = _repository.viewDialogMutData
    val tempLiveData: LiveData<ArrayList<TemperatureSelectionModel>>
        get() = _repository.tempLiveData

    val contentLiveData: LiveData<ArrayList<ContentSelectionModel>>
        get() = _repository.contentLiveData

    val packingLiveData: LiveData<ArrayList<PackingSelectionModel>>
        get() = _repository.packingLiveData

    val gelPackItemLiveData: LiveData<ArrayList<GelPackItemSelectionModel>>
        get() = _repository.gelPackLiveData
    val saveBookingLiveData: LiveData<SaveDirectBookingModel>
        get() = _repository.saveBookingLiveData

    val boxNoValidateLiveData: LiveData<BoxNoValidateModel>
        get() = _repository.boxValidateLiveData


    fun getTemperatureList(
        companyId: String,
        spname: String,
        param: List<String>,
        values: ArrayList<String>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getTemperatureLov(companyId, spname, param, values)
        }
    }

    fun getContentLov(
        companyId: String,
        spname: String,
        param: List<String>,
        values: ArrayList<String>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getContentLov(companyId, spname, param, values)
        }
    }

    fun getPackingLov(
        companyId: String,
        spname: String,
        param: List<String>,
        values: ArrayList<String>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getPackingLov(companyId, spname, param, values)
        }
    }

    fun getGelPackLov(
        companyId: String,
        spname: String,
        param: List<String>,
        values: ArrayList<String>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getGelPackLov(companyId, spname, param, values)
        }
    }

    fun boxNoValidate(
        companyId: String,
        spname: String,
        param: List<String>,
        values: ArrayList<String>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.boxNoValidate(companyId, spname, param, values)
        }
    }

    fun saveBooking(
        companyId:String,
        branchcode:String,
        bookingdt:String,
        time:String,
        egrno:String,
        custcode:String,
        destcode:String,
        productcode:String,
        pckgs:Int,
        aweight:String,
        vweight:String,
        cweight:String,
        createid:String,
        sessionid:String,
        refno:String,
        cngr:String,
        cngraddress:String,
        cngrcity:String,
        cngrzipcode:String,
        cngrstate:String,
        cngrmobileno:String,
        cngremailid:String,
        cngrCSTNo:String,
        cngrLSTNo:String,
        cngrTINNo:String,
        cngrSTaxRegNo:String,
        cnge:String,
        cngeaddress:String,
        cngecity:String,
        cngezipcode:String,
        cngestate:String,
        cngemobileno:String,
        cngeemailid:String,
        cngeCSTNo:String,
        cngeLSTNo:String,
        cngeTINNo:String,
        cngeSTaxRegNo:String,
        transactionid:Int,
        mawbchargeapplicable:String,
        custdeptid:String,
        referencenostr:String,
        weightstr:String,
        packagetypestr:String,
        tempuraturestr:String,
        packingstr:String,
        goodsstr:String,
        dryicestr:String,
        dryiceqtystr:String,
        dataloggerstr:String,
        dataloggernostr:String,
        dimlength:String,
        dimbreath:String,
        dimheight:String,
        pickupboyname:String,
        boyid:Int,
        boxnostr:String,
        stockitemcodestr:String,
        gelpackstr:String,
        gelpackitemcodestr:String,
        gelpackqtystr:String,
        menucode:String,
        invoicenostr:String,
        invoicedtstr:String,
        invoicevaluestr:String,
        ewaybillnostr:String,
        ewaybilldtstr:String,
        ewbvaliduptodtstr:String,
        vendorcode:String,
        pckgsstr:String,
        pickupby:String,
        vehicleno:String,
        vweightstr:String,
        vehiclecode:String,
        cngrcode:String,
        cngecode:String,
        remarks:String,
        cngrgstno:String,
        cngegstno:String,
        cngrtelno: String,
        cngetelno:String,
        orgpincode:String,
        destpincode:String,
        pickuppoint:String,
        deliverypoint:String

    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.saveDirectBooking(
                companyId,
                branchcode ,
                bookingdt ,
                time ,
                egrno ,
                custcode ,
                destcode ,
                productcode ,
                pckgs ,
                aweight ,
                vweight ,
                cweight ,
                createid ,
                sessionid ,
                refno ,
                cngr ,
                cngraddress ,
                cngrcity ,
                cngrzipcode ,
                cngrstate ,
                cngrmobileno ,
                cngremailid ,
                cngrCSTNo ,
                cngrLSTNo ,
                cngrTINNo ,
                cngrSTaxRegNo ,
                cnge ,
                cngeaddress ,
                cngecity ,
                cngezipcode ,
                cngestate ,
                cngemobileno ,
                cngeemailid ,
                cngeCSTNo ,
                cngeLSTNo ,
                cngeTINNo ,
                cngeSTaxRegNo ,
                transactionid ,
                mawbchargeapplicable ,
                custdeptid ,
                referencenostr ,
                weightstr ,
                packagetypestr ,
                tempuraturestr ,
                packingstr ,
                goodsstr ,
                dryicestr ,
                dryiceqtystr ,
                dataloggerstr ,
                dataloggernostr ,
                dimlength ,
                dimbreath ,
                dimheight ,
                pickupboyname ,
                boyid ,
                boxnostr ,
                stockitemcodestr ,
                gelpackstr ,
                gelpackitemcodestr ,
                gelpackqtystr ,
                menucode ,
                invoicenostr ,
                invoicedtstr ,
                invoicevaluestr ,
                ewaybillnostr ,
                ewaybilldtstr ,
                ewbvaliduptodtstr ,
                vendorcode ,
                pckgsstr ,
                pickupby ,
                vehicleno ,
                vweightstr ,
                vehiclecode ,
                cngrcode ,
                cngecode ,
                remarks ,
                cngrgstno ,
                cngegstno ,
                cngrtelno ,
                cngetelno ,
                orgpincode ,
                destpincode ,
                pickuppoint ,
                deliverypoint

            )
        }
    }

}