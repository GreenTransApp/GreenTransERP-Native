package com.greensoft.greentranserpnative.ui.operation.bookingWithoutIndent

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.greensoft.greentranserpnative.ui.bottomsheet.vehicleSelection.VehicleSelectionBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.vehicleSelection.model.VehicleModelDRS
import com.greensoft.greentranserpnative.ui.bottomsheet.vendorSelection.VendorSelectionBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.vendorSelection.model.VendorModelDRS
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.bookingWithoutIndent.model.InvoiceDataModel
import com.greensoft.greentranserpnative.ui.operation.drs.model.DeliverByModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class DirectBookingActivity @Inject constructor(): BaseActivity(), OnRowClick<Any>, AlertCallback<Any>,
    BottomSheetClick<Any> {
    private lateinit var activityBinding:ActivityDirectBookingBinding
    private lateinit var manager: LinearLayoutManager
    private val bookingTypeList: ArrayList<DeliverByModel> = ArrayList()
    private val serviceTypeList: ArrayList<DeliverByModel> = ArrayList()
    private val pckgsTypeList: ArrayList<DeliverByModel> = ArrayList()
    private val invoiceList: ArrayList<InvoiceDataModel> = ArrayList()

    private var rvAdapter: InVoiceListAdapter?=null
    var vendorCode = ""
    var vehicleCode = ""
    var bookedByCode = "M"
    var serviceByCode = ""
    var pckgsByCode = ""

    private var sqlDate: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityDirectBookingBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("Direct Booking")
        setSpinnersData()
        setServiceTypeSpinner()
        setPckgsTypeSpinner()
        setBookedBySpinners()

        activityBinding.inputDate.inputType = InputType.TYPE_NULL;
        activityBinding.inputTime.inputType = InputType.TYPE_NULL;
        activityBinding.inputDate.setText(getViewCurrentDate())
        sqlDate = getSqlCurrentDate()
        activityBinding.inputTime.setText(getSqlCurrentTime())
        activityBinding.inputPickupBoy.setText(userDataModel?.username)

        setObservers()
        setOnClick()
    }

    private fun setSpinnersData(){


        serviceTypeList.add(DeliverByModel(name = "AIR", code = "A"))
        serviceTypeList.add(DeliverByModel(name = "SURFACE", code = "S"))
        serviceTypeList.add(DeliverByModel(name = "SURFACE EXPRESS", code = "E"))
        serviceTypeList.add(DeliverByModel(name = "TRAIN", code = "T"))

        pckgsTypeList.add(DeliverByModel(name = "Jeena Packing", code = "J"))
        pckgsTypeList.add(DeliverByModel(name = "Pre Packed", code = "T"))

        bookingTypeList.add(DeliverByModel(name = "AGENT", code = "V"))
        bookingTypeList.add(DeliverByModel(name = "MARKET VEHICLE", code = "M"))
        bookingTypeList.add(DeliverByModel(name = "JEENA STAFF", code = "S"))
        bookingTypeList.add(DeliverByModel(name = "OFFICE / AIRPORT DROP", code = "O"))
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
        activityBinding.inputDepartmentName.setOnClickListener {
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

        activityBinding.inputVehicle.setOnClickListener {
            vehicleBottomSheet(this,"Vehicle Selection",this)
        }
        activityBinding.inputVehicleVendor.setOnClickListener {
            vendorBottomSheet(this,"Vendor Selection", this)
        }
        activityBinding.invoiceBtn.setOnClickListener {
            val inputText = activityBinding.inputNoOfInvoice.text.toString()
            if (inputText.isNotEmpty()) {
                val numberOfItems = inputText.toInt()
                updateInvoiceList(numberOfItems)
                setupInvoiceRecyclerView()
            }
        }


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

    private fun vehicleBottomSheet(mContext: Context, title:String, onRowClick: OnRowClick<Any>) {
        val bottomSheetDialog = VehicleSelectionBottomSheet.newInstance(mContext,title,onRowClick)
        bottomSheetDialog.show(supportFragmentManager, VehicleSelectionBottomSheet.TAG)
    }

    private fun vendorBottomSheet(mContext: Context, title: String, onRowClick: OnRowClick<Any>) {
        val bottomSheetDialog = VendorSelectionBottomSheet.newInstance(mContext,title,onRowClick, if(bookedByCode == "M") "VEHICLE VENDOR" else "PUD VENDOR")
        bottomSheetDialog.show(supportFragmentManager, VendorSelectionBottomSheet.TAG)
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
                activityBinding.inputDepartmentName.setText(selectedDepartmentData.custdeptname)
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
        else if (clickType==VehicleSelectionBottomSheet.VEHICLE_CLICK_TYPE){
            try {
                val selectedVehicleData = data as VehicleModelDRS
                activityBinding.inputVehicle.setText(selectedVehicleData.regno)
            } catch (ex:Exception){
                errorToast(ex.message)
            }

        }
        else if (clickType==VendorSelectionBottomSheet.VENDOR_CLICK_TYPE){
            try {
                val selectedVendorData = data as VendorModelDRS
                activityBinding.inputVehicleVendor.setText(selectedVendorData.vendname)
            } catch (ex:Exception){
                errorToast(ex.message)
            }
        }

//        else if (clickType=="EwbDate"){
//            try {
//            openSingleDatePicker()
//            } catch (ex:Exception){
//                errorToast(ex.message)
//            }
//        }
//        else if (clickType=="InVoiceDate"){
//            try {
//                openSingleDatePicker()
//            } catch (ex:Exception){
//                errorToast(ex.message)
//            }
//        }
//        else if (clickType=="EwbValidUptoDate"){
//            try {
//                openSingleDatePicker()
//            } catch (ex:Exception){
//                errorToast(ex.message)
//            }
//        }

    }

    private fun setBookedBySpinners() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, bookingTypeList)
        activityBinding.selectedBookedType.adapter = adapter
        activityBinding.selectedBookedType.onItemSelectedListener =
            object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    bookedByCode = bookingTypeList[position].code
                    vendorCode = ""
                    vehicleCode = ""
                    when(bookedByCode) {
                        "V" -> run {
                            activityBinding.inputVehicleVendor.setText("")
                            activityBinding.inputVehicle.setText("")
                            activityBinding.agentVendorHeader.setText("Select Agent")
                            activityBinding.agentVendorPlaceholder.setHint("Select Agent")
                            activityBinding.inputLayoutVehicleVendor.visibility = View.VISIBLE
                            activityBinding.inputLayoutVehicle.visibility = View.GONE
                            activityBinding.inputLayoutPickBoy.visibility= View.GONE

                        }
                        "M" -> run {
                            activityBinding.inputVehicleVendor.setText("")
                            activityBinding.agentVendorHeader.setText("Vehicle Vendor")
                            activityBinding.agentVendorPlaceholder.setHint("Select Vehicle Vendor")
                            activityBinding.inputLayoutVehicleVendor.visibility = View.VISIBLE
                            activityBinding.inputLayoutVehicle.visibility = View.VISIBLE
                            activityBinding.inputLayoutPickBoy.visibility= View.VISIBLE
                        }
                        "S" -> run {
                            activityBinding.inputVehicle.setText("")
                            activityBinding.inputVehicleVendor.setText("")
                            activityBinding.inputLayoutVehicleVendor.visibility = View.GONE
                            activityBinding.inputLayoutVehicle.visibility = View.GONE
                            activityBinding.inputLayoutVehicle.visibility = View.GONE
                            activityBinding.inputLayoutPickBoy.visibility= View.VISIBLE
                        }
                        "O" -> run {
                            activityBinding.inputVehicle.setText("")
                            activityBinding.inputVehicleVendor.setText("")
                            activityBinding.inputLayoutVehicleVendor.visibility = View.GONE
                            activityBinding.inputLayoutVehicle.visibility = View.GONE
                            activityBinding.inputLayoutVehicle.visibility = View.GONE
                            activityBinding.inputLayoutPickBoy.visibility= View.VISIBLE
                        }
                        else -> {

                        }
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }


        activityBinding.selectedBookedType.setSelection(1)
    }
    private fun setServiceTypeSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, serviceTypeList)
        activityBinding.selectService.adapter = adapter
        activityBinding.selectService.onItemSelectedListener =
            object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    serviceByCode = serviceTypeList[position].code
                    when(serviceByCode) {
                        "A" -> run {
                            errorToast("air")
                        }
                        "S" -> run {
                            errorToast("surface")
                        }
                        "E" -> run {
                           errorToast("express")
                        }
                        "T" -> run {
                            errorToast("train")
                        }
                        else -> {

                        }
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }


        activityBinding.selectService.setSelection(0)
    }
    private fun setPckgsTypeSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, pckgsTypeList)
        activityBinding.selectPckgs.adapter = adapter
        activityBinding.selectPckgs.onItemSelectedListener =
            object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    pckgsByCode = pckgsTypeList[position].code
                    when(pckgsByCode) {
                        "J" -> run {
                            successToast("jeena packing block")
                        }
                        "T" -> run {
                            successToast("pre packing block")
                        }

                        else -> {

                        }
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }


        activityBinding.selectPckgs.setSelection(1)
    }

    private fun setupInvoiceRecyclerView() {
        if (rvAdapter == null) {
            manager = LinearLayoutManager(this)
            activityBinding.invoiceRecyclerView.layoutManager = manager
        }
        rvAdapter = InVoiceListAdapter(invoiceList, this,this)
        activityBinding.invoiceRecyclerView.adapter = rvAdapter
//    }
    }
    private fun updateInvoiceList(numberOfItems: Int) {
        invoiceList.clear() // Clear the current list
        for (i in 1..numberOfItems) {
            invoiceList.add(InvoiceDataModel("","","","","","",""))
        }
    }
}