package com.greensoft.greentranserpnative.ui.operation.bookingWithoutIndent

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityDirectBookingBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.cngrCngeSelection.CngrCngeSelectionBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.cngrCngeSelection.model.CngrCngeSelectionModel
import com.greensoft.greentranserpnative.ui.bottomsheet.customerSelection.CustomerSelectionBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.customerSelection.model.CustomerSelectionModel
import com.greensoft.greentranserpnative.ui.bottomsheet.departmentSelection.DepartmentSelectionBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.departmentSelection.model.DepartmentSelectionModel
import com.greensoft.greentranserpnative.ui.bottomsheet.pinCodeSelection.PinCodeSelectionBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.pinCodeSelection.model.PinCodeModel
import com.greensoft.greentranserpnative.ui.bottomsheet.vendorSelection.VendorSelectionBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.vendorSelection.model.VendorModelDRS
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class DirectBooking @Inject constructor(): BaseActivity(), OnRowClick<Any>, AlertCallback<Any>,
    BottomSheetClick<Any> {
    private lateinit var activityBinding:ActivityDirectBookingBinding

    private var sqlDate: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityDirectBookingBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("Direct Booking")

        activityBinding.inputDate.inputType = InputType.TYPE_NULL;
        activityBinding.inputTime.inputType = InputType.TYPE_NULL;
        activityBinding.inputDate.setText(getViewCurrentDate())
        sqlDate = getSqlCurrentDate()
        activityBinding.inputTime.setText(getSqlCurrentTime())

        setObservers()
        setOnClick()
    }

    private fun setOnClick(){
        activityBinding.inputDate.setOnClickListener {
            openSingleDatePicker()
        }
        activityBinding.inputTime.setOnClickListener {
            openTimePicker()
        }
        activityBinding.autoGrCheck.setOnCheckedChangeListener { compoundButton, bool ->
            activityBinding.inputGrNo.setText("")
            if(bool) {
                activityBinding.inputGrNo.hint = "AUTO GR #"
//                activityBinding.inputGrNo.
            } else {
                activityBinding.inputGrNo.hint = "ENTER GR #"
            }
            activityBinding.inputGrNo.isEnabled = !(bool)
        }

        activityBinding.inputCustCode.setOnClickListener {
            customerSelectionBottomSheet(this,"Customer Selection",this,"custCode")
        }

        activityBinding.inputCustName.setOnClickListener {
            customerSelectionBottomSheet(this,"Customer Selection",this,"custName")
        }
        activityBinding.inputDepartment.setOnClickListener {
            departmentSelectionBottomSheet(this,"Department Selection",this,"department")
        }
        activityBinding.inputOriginPinCode.setOnClickListener {
            pinCodeSelectionBottomSheet(this,"Pin Code Selection",this,"O")
        }
        activityBinding.inputDestinationPinCode.setOnClickListener {
            pinCodeSelectionBottomSheet(this,"Pin Code Selection",this,"D")
        }
        activityBinding.consignorName.setOnClickListener {
            cngrCngeSelectionBottomSheet(this,"Consignor Selection",this,"R")
        }
        activityBinding.consigneeName.setOnClickListener {
            cngrCngeSelectionBottomSheet(this,"Consignee Selection", this,"E")
        }
        activityBinding.inputPickupBoy.setText(userDataModel?.username)
    }

    private fun setObservers(){
        mPeriod.observe(this) { datePicker ->
            activityBinding.inputDate.setText(datePicker.viewsingleDate)
            sqlDate=datePicker.sqlsingleDate.toString()
        }
        timePeriod.observe(this) { time ->
            activityBinding.inputTime.setText(time)

        }
    }

    private fun checkFieldsBeforeSave(){
        if(activityBinding.inputDate.text.isNullOrEmpty()){
            activityBinding.inputDate.error="Please Select Date"
            errorToast("Please Select Date")
            return
        }
        else if (activityBinding.inputTime.text.isNullOrEmpty()){
            errorToast("Please Select Date")
            return
        }
        if(!activityBinding.autoGrCheck.isChecked && activityBinding.inputGrNo.text.isNullOrBlank()){
            errorToast("Please Enter GR Number")
            return
        }
    }


    private fun customerSelectionBottomSheet(mContext: Context, title: String, onRowClick: OnRowClick<Any>,clickType: String) {
        val bottomSheetDialog = CustomerSelectionBottomSheet.newInstance(mContext,title,onRowClick,clickType)
        bottomSheetDialog.show(supportFragmentManager, CustomerSelectionBottomSheet.TAG)
    }
    private fun departmentSelectionBottomSheet(mContext: Context, title: String, onRowClick: OnRowClick<Any>,clickType: String) {
        val bottomSheetDialog = DepartmentSelectionBottomSheet.newInstance(mContext,title,onRowClick,clickType)
        bottomSheetDialog.show(supportFragmentManager, DepartmentSelectionBottomSheet.TAG)
    }
    private fun pinCodeSelectionBottomSheet(mContext: Context, title: String, onRowClick: OnRowClick<Any>,clickType: String) {
        val bottomSheetDialog = PinCodeSelectionBottomSheet.newInstance(mContext,title,onRowClick,clickType)
        bottomSheetDialog.show(supportFragmentManager, PinCodeSelectionBottomSheet.TAG)
    }

    private fun cngrCngeSelectionBottomSheet(mContext: Context, title: String, onRowClick: OnRowClick<Any>,clickType: String) {
        val bottomSheetDialog = CngrCngeSelectionBottomSheet.newInstance(mContext,title,onRowClick,clickType)
        bottomSheetDialog.show(supportFragmentManager, CngrCngeSelectionBottomSheet.TAG)
    }

    override fun onAlertClick(alertClick: AlertClick, alertCallType: String, data: Any?) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }

    override fun onClick(data: Any, clickType: String) {
        if (clickType==CustomerSelectionBottomSheet.CUSTOMER_CLICK_TYPE){
            try {
                val selectedCustomerName = data as CustomerSelectionModel
                activityBinding.inputCustName.setText(selectedCustomerName.custname)
                activityBinding.inputCustCode.setText(selectedCustomerName.custcode)
            } catch (ex:Exception){
                errorToast(ex.message)
            }
        }
        else if (clickType==PinCodeSelectionBottomSheet.ORIGIN_PIN_CODE_CLICK_TYPE){
            try {
                val selectedPinData = data as PinCodeModel
                activityBinding.inputOriginPinCode.setText(selectedPinData.code)
                activityBinding.originGateway.setText(selectedPinData.flagseven)
                activityBinding.inputOriginOda.setText(selectedPinData.flagfive)
                activityBinding.inputOrigin.setText(selectedPinData.flagfour)
                activityBinding.inputOriginArea.setText(selectedPinData.flagone)
                activityBinding.inputOriginOdaDistance.setText(selectedPinData.intvalue.toString())
            } catch (ex:Exception){
                errorToast(ex.message)
            }
        }

       else if (clickType==PinCodeSelectionBottomSheet.DESTINATION_PIN_CODE_CLICK_TYPE){
            try {
                val selectedPinData = data as PinCodeModel
                activityBinding.inputDestinationPinCode.setText(selectedPinData.code)
                activityBinding.destinationGateway.setText(selectedPinData.flagseven)
                activityBinding.inputDestinationOda.setText(selectedPinData.flagfive)
                activityBinding.inputDestination.setText(selectedPinData.flagfour)
                activityBinding.inputDestinationArea.setText(selectedPinData.flagone)
                activityBinding.inputDestinationOdaDistance.setText(selectedPinData.intvalue.toString())
            } catch (ex:Exception){
                errorToast(ex.message)
            }
        }

       else if (clickType==DepartmentSelectionBottomSheet.DEPARTMENT_CLICK_TYPE){
            try {
                val selectedDepartmentData = data as DepartmentSelectionModel
                activityBinding.inputDepartment.setText(selectedDepartmentData.custdeptname)
            } catch (ex:Exception){
                errorToast(ex.message)
            }
        }
        else if (clickType==CngrCngeSelectionBottomSheet.CNGR_CLICK_TYPE){
            try {
                val selectedCngeCngrData = data as CngrCngeSelectionModel
                activityBinding.consignorName.setText(selectedCngeCngrData.name)
                activityBinding.consignorMobile.setText(selectedCngeCngrData.telno?:"")
            } catch (ex:Exception){
                errorToast(ex.message)
            }
        }
        else if (clickType==CngrCngeSelectionBottomSheet.CNGE_CLICK_TYPE){
            try {
                val selectedCngeCngrData = data as CngrCngeSelectionModel
                activityBinding.consigneeName.setText(selectedCngeCngrData.name)
                activityBinding.consigneeMobile.setText(selectedCngeCngrData.telno?:"")
            } catch (ex:Exception){
                errorToast(ex.message)
            }
        }

    }
}