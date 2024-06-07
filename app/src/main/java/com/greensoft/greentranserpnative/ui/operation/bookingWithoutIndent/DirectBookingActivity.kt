package com.greensoft.greentranserpnative.ui.operation.bookingWithoutIndent

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityDirectBookingBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.cngrCngeSelection.CngrCngeSelectionBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.cngrCngeSelection.model.CngrCngeSelectionModel
import com.greensoft.greentranserpnative.ui.bottomsheet.common.models.CommonBottomSheetModel
import com.greensoft.greentranserpnative.ui.bottomsheet.customerCodeSelection.CustomerCodeSelectionBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.customerSelection.CustomerSelectionBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.customerSelection.model.CustomerSelectionModel
import com.greensoft.greentranserpnative.ui.bottomsheet.departmentSelection.DepartmentSelectionBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.departmentSelection.model.DepartmentSelectionModel
import com.greensoft.greentranserpnative.ui.bottomsheet.pinCodeSelection.PinCodeSelectionBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.pinCodeSelection.model.PinCodeModel
import com.greensoft.greentranserpnative.ui.bottomsheet.printGR.PrintGrBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.vehicleSelection.VehicleSelectionBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.vehicleSelection.model.VehicleModelDRS
import com.greensoft.greentranserpnative.ui.bottomsheet.vendorSelection.VendorSelectionBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.vendorSelection.model.VendorModelDRS
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.common.alert.CommonAlert
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.booking.BookingActivity
import com.greensoft.greentranserpnative.ui.operation.booking.BookingViewModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.ContentSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.GelPackItemSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.PackingSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.TemperatureSelectionModel
import com.greensoft.greentranserpnative.ui.operation.bookingWithoutIndent.adapter.DirectBookingAdapter
import com.greensoft.greentranserpnative.ui.operation.bookingWithoutIndent.adapter.InVoiceListAdapter
import com.greensoft.greentranserpnative.ui.operation.bookingWithoutIndent.model.InvoiceDataModel
import com.greensoft.greentranserpnative.ui.operation.drs.model.DeliverByModel
import com.greensoft.greentranserpnative.ui.operation.eway_bill.EwayBillBottomSheet
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.models.SinglePickupRefModel
import com.greensoft.greentranserpnative.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.round

@AndroidEntryPoint
class DirectBookingActivity @Inject constructor() : BaseActivity(), OnRowClick<Any>,
    AlertCallback<Any>, BottomSheetClick<Any> {
    private lateinit var activityBinding: ActivityDirectBookingBinding
    private lateinit var manager: LinearLayoutManager
    private val bookingTypeList: ArrayList<DeliverByModel> = ArrayList()
    private val serviceTypeList: ArrayList<DeliverByModel> = ArrayList()
    private val pckgsTypeList: ArrayList<DeliverByModel> = ArrayList()
    private val invoiceList: ArrayList<InvoiceDataModel> = ArrayList()
    private var bookingList: ArrayList<SinglePickupRefModel> = ArrayList()
    private var temperatureList: ArrayList<TemperatureSelectionModel> = ArrayList()
    private var packingList: ArrayList<PackingSelectionModel> = ArrayList()
    private var gelPackList: ArrayList<GelPackItemSelectionModel> = ArrayList()
    private var contentList: ArrayList<ContentSelectionModel> = ArrayList()
    private var bookingAdapter: DirectBookingAdapter? = null
    private val viewModel: DirectBookingViewModel by viewModels()
    private val bookingViewModel: BookingViewModel by viewModels()
    private var rvAdapter: InVoiceListAdapter? = null
    var vendorCode = ""
    var vehicleCode = ""
    var bookedByCode = "M"
    var productCode = ""
    var pckgsByCode = ""
    var pckgsType = ""
    var custDeptid = ""
    var originCode = ""
    var destinationCode = ""

    var cngrAddress = ""
    var cngrCity = ""
    var cngrzipCode = ""
    var cngrState = ""
    var cngrTelNo = ""
    var cngrEmail = ""
    var cngrCstNo = ""
    var cngrLstNo = ""
    var cngrTinNo = ""
    var cngrGstNo = ""
    var cngrStaxregno = ""
    var cngrCode = ""

    var cngeAddress = ""
    var cngeCity = ""
    var cngezipCode = ""
    var cngeState = ""
    var cngeTelNo = ""
    var cngeEmail = ""
    var cngeCstNo = ""
    var cngeLstNo = ""
    var cngeTinNo = ""
    var cngeGstNo = ""
    var cngeStaxregno = ""
    var cngeCode = ""
    var pickupContentId = ""
    var selectedGelPackItemCode = ""
    private val menuCode: String = "GTAPP_BOOKINGWITHOUTINDENT"
    private var bookingSqlDt: String? = null

    companion object {
        val JEENA_PACKING: String = "Jeena Packing"
        val PRE_PACKED: String = "Pre Packed"
        const val PRINT_GR_TAG = "PRINT_GR_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityDirectBookingBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        Utils.ewayBillDetailResponse = null
        Utils.enteredValidatedEwayBillList = null
        Utils.enteredEwayBillValidatedData.clear()
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("Direct Booking")



        setSpinnersData()
        setServiceTypeSpinner()
        setPckgsTypeSpinner()
        setBookedBySpinners()
        activityBinding.itemGrid.visibility = View.GONE
        activityBinding.inputDate.inputType = InputType.TYPE_NULL;
        activityBinding.inputTime.inputType = InputType.TYPE_NULL;
        activityBinding.inputDate.setText(getViewCurrentDate())
        bookingSqlDt = getSqlCurrentDate()
        activityBinding.inputTime.setText(getSqlCurrentTime())
        activityBinding.inputPickupBoy.setText(userDataModel?.username)
        if (activityBinding.inputNoOfPckgs.text!!.isNotEmpty()) {
            activityBinding.btnAddMore.visibility = View.VISIBLE
        }
        setObservers()
        setOnClick()

        getContentList()
        getTempLov()
        getpackingLov()
        getGelPackLov()
    }

    private fun setSpinnersData() {


        serviceTypeList.add(DeliverByModel(name = "AIR", code = "A"))
        serviceTypeList.add(DeliverByModel(name = "SURFACE", code = "S"))
        serviceTypeList.add(DeliverByModel(name = "SURFACE EXPRESS", code = "E"))
        serviceTypeList.add(DeliverByModel(name = "TRAIN", code = "T"))

        pckgsTypeList.add(DeliverByModel(name = "Jeena Packing", code = "Jeena Packing"))
        pckgsTypeList.add(DeliverByModel(name = "Pre Packed", code = "Pre Packed"))

        bookingTypeList.add(DeliverByModel(name = "AGENT", code = "V"))
        bookingTypeList.add(DeliverByModel(name = "MARKET VEHICLE", code = "M"))
        bookingTypeList.add(DeliverByModel(name = "JEENA STAFF", code = "S"))
        bookingTypeList.add(DeliverByModel(name = "OFFICE / AIRPORT DROP", code = "O"))
    }

    private fun setOnClick() {
        activityBinding.inputDate.setOnClickListener {
            openSingleDatePicker()
        }
        activityBinding.inputTime.setOnClickListener {
            openTimePicker()
        }
        activityBinding.autoGrCheck.setOnCheckedChangeListener { compoundButton, bool ->
            activityBinding.inputGrNo.setText("")
            if (bool) {
                activityBinding.inputGrNo.hint = "AUTO GR #"
//                activityBinding.inputGrNo.
            } else {
                activityBinding.inputGrNo.hint = "ENTER GR #"
            }
            activityBinding.inputGrNo.isEnabled = !(bool)
        }

        activityBinding.inputCustCode.setOnClickListener {
            customerCodeSelectionBottomSheet(this, "Customer Code Selection", this, "custCode")
        }

        activityBinding.inputCustName.setOnClickListener {
            customerSelectionBottomSheet(this, "Customer Selection", this, "custName")

        }
        activityBinding.inputDepartmentName.setOnClickListener {
            departmentSelectionBottomSheet(this, "Department Selection", this, "department")
        }
        activityBinding.inputOriginPinCode.setOnClickListener {
            pinCodeSelectionBottomSheet(this, "Pin Code Selection", this, "O")
        }
        activityBinding.inputDestinationPinCode.setOnClickListener {
            pinCodeSelectionBottomSheet(this, "Pin Code Selection", this, "D")
        }
        activityBinding.consignorName.setOnClickListener {
            var branchcode: String = ""
            if (activityBinding.inputCustCode.text != null && activityBinding.inputCustCode.text!!.isNotEmpty()) {
                branchcode = getLoginBranchCode()
                cngrCngeSelectionBottomSheet(
                    this,
                    "Consignor Selection",
                    this,
                    "R",
                    activityBinding.inputCustCode.text.toString(),
                    branchcode
                )

            } else {
                errorToast("Please Select Customer First")
            }
        }
        activityBinding.consigneeName.setOnClickListener {
            var branchcode: String = ""
            if (destinationCode != null && destinationCode.isNotEmpty()) {
                branchcode = destinationCode
                cngrCngeSelectionBottomSheet(
                    this,
                    "Consignee Selection",
                    this,
                    "E",
                    activityBinding.inputCustCode.text.toString(),
                    branchcode
                )
            } else {
                errorToast("Please Select Destination First")
            }
        }

        activityBinding.inputVehicle.setOnClickListener {
            vehicleBottomSheet(this, "Vehicle Selection", this)
        }
        activityBinding.inputVehicleVendor.setOnClickListener {
            vendorBottomSheet(this, "Vendor Selection", this)
        }
//        activityBinding.invoiceBtn.setOnClickListener {
//            val inputText = activityBinding.inputNoOfInvoice.text.toString()
//            if (inputText.isNotEmpty()) {
//                val numberOfItems = inputText.toInt()
//                updateInvoiceList(numberOfItems)
//                setupInvoiceRecyclerView()
//            }
//        }
        activityBinding.btnEwayBill.setOnClickListener {
            openEwayBillBottomSheet(bookingViewModel)
        }
        activityBinding.btnSave.setOnClickListener {
            checkFieldsBeforeSave()
        }
        activityBinding.btnAddMore.setOnClickListener {
            if (bookingList.isNotEmpty()) {
                addMoreGridItem(bookingList[0]);
            } else{
                errorToast("Please enter no of pckgs first")
            }
        }

        activityBinding.inputNoOfPckgs.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                var noOfPckgs = p0.toString().toIntOrNull() ?: 0
                if (pckgsType != DirectBookingActivity.JEENA_PACKING) {
                    activityBinding.btnAddMore.visibility = View.VISIBLE
                }
                if (noOfPckgs > 0) {
                    activityBinding.itemGrid.visibility = View.VISIBLE
                    activityBinding.gridType.visibility = View.VISIBLE
                    addItemToGrid(noOfPckgs)
                } else {
                    bookingList.clear()
                    activityBinding.itemGrid.visibility = View.GONE
                    activityBinding.gridType.visibility = View.GONE

                }
            }

        })
    }

    private fun addItemToGrid(noOfPckgs: Int){


        var data = SinglePickupRefModel(
            Departmentcode = 0,
            Origin = "",
            aweight = 0.0,
            boxno = "",
            branchname = "",
            cnge = "",
            cngeaddress = "",
            cngecity = "",
            cngecode = "",
            cngecstno = "",
            cngeemail = "",
            cngelstno = "",
            cngestate = "",
            cngestaxregno = "",
            cngetelno = "",
            cngetinno = "",
            cngezipcode = "",
            cngr = "",
            cngraddress = "",
            cngrcity = "",
            cngrcode = "",
            cngrcstno = "",
            cngremail = "",
            cngrlstno = "",
            cngrstate = "",
            cngrstaxregno = "",
            cngrtelno = "",
            cngrtinno = "",
            cngrzipcode = "",
            commandmessage = "",
            commandstatus = 0,
            contents = "",
            contentsCode = "",
            custcode = "",
            custname = "",
            datalogger = "",
            dataloggerno = "",
            departmentname = "",
            destcode = "",
            destname = "",
            detailreferenceno = "",
            dryice = "",
            dryiceqty = 0.0,
            enabledryiceqty = "",
            gelpack = "N",
            gelpackqty = 0,
            gelpacktype = "",
            gelpackitemcode = "",
            goods = "",
            iatavolfactor = 0,
            orgcode = "",
            packagetype = "",
            packing = "",
            packingcode = "",
            pckgbreath = 0.0,
            pckgheight = 0.0,
            pckglength = 0.0,
            localBreath = 0.0,
            localHeight = 0.0,
            localLength = 0.0,
            pckgs = 0,
            pcs = 0,
            referenceno = "",
            servicetype = "",
            stockitemcode = "",
            tempurature = "",
            tempuratureCode = "",
            transactionid = 0,
            volfactor = 0.0f,
            weight = 0.0,
            localVWeight = 0,
            isBoxValidated = false
        )
        bookingList.clear();
        if (pckgsType == DirectBookingActivity.JEENA_PACKING) {
//                        activityBinding.btnAddMore.visibility = View.GONE

            for (i in 1..noOfPckgs) {
//                              var obj =SinglePickupRefModel(data)
                data.packagetype = JEENA_PACKING
                bookingList.add(data)
            }
        } else {
//                        activityBinding.btnAddMore.visibility = View.VISIBLE
//                             var obj =SinglePickupRefModel(data)
            data.packagetype = PRE_PACKED
            bookingList.add(data)
//                        bookingAdapter?.addItemAtIndex(data,0)
        }
        setupBookingRecyclerView()
    }

    private fun openEwayBillBottomSheet(viewModel: BookingViewModel) {
        val bottomSheet = EwayBillBottomSheet.newInstance(
            this, "EWAY BILL", viewModel, loginDataModel, userDataModel
        )
        bottomSheet.show(supportFragmentManager, "Eway-Bill-BottomSheet")
    }

    private fun setObservers() {
        mPeriod.observe(this) { datePicker ->
            activityBinding.inputDate.setText(datePicker.viewsingleDate)
            bookingSqlDt = datePicker.sqlsingleDate.toString()
        }
        timePeriod.observe(this) { time ->
            activityBinding.inputTime.setText(time)

        }
        activityBinding.refreshLayout.setOnRefreshListener {
//            refreshData()
            lifecycleScope.launch {
                delay(1500)
                activityBinding.refreshLayout.isRefreshing = false
            }
        }

        viewModel.tempLiveData.observe(this) { tempData ->
            temperatureList = tempData

        }
        viewModel.contentLiveData.observe(this) { itemData ->
            contentList = itemData
        }

        viewModel.packingLiveData.observe(this) { packing ->
            packingList = packing
        }
        viewModel.gelPackItemLiveData.observe(this) { gelPack ->
            gelPackList = gelPack
        }
        viewModel.boxNoValidateLiveData.observe(this) {
            if (it.commandstatus == 1) {
                bookingAdapter?.enterBoxOnNextAvailable(it.boxno.toString())
            }
        }
        viewModel.saveBookingLiveData.observe(this) { data ->
            if (data != null) {
                if (data.commandstatus == 1) {
//                    showGrCreatedAlert(data)
                    showPrintGrAlert(data.grno.toString())
//                    successToast(data.commandmessage.toString())
                } else {
                    errorToast(data.commandmessage.toString())
                }
            }
        }
    }
    private fun showPrintGrAlert(grNo: String) {
        CommonAlert.createAlert(
            context = this,
            header = "Alert!!",
            description = "Are you sure to print GR#: $grNo ?",
            callback = this,
            alertCallType = DirectBookingActivity.PRINT_GR_TAG,
            data = grNo
        )
    }
//    private fun updateBookingList(numberOfItems: Int) {
//        bookingList.clear() // Clear the current list
//        for (i in 1..numberOfItems) {
//            val obj = SinglePickupRefModel(refModel)
//            bookingList.add(obj)
//            bookingAdapter?.notifyItemInserted(bookingList.size - 1)
//        }
//    }


    //------------------- grid lovs  api  calling and selection methods-------------------------------------------------------------------------
    private fun getContentList() {
        viewModel.getContentLov(
            loginDataModel?.companyid.toString(), "greentrans_showgoodsLOV", listOf(), arrayListOf()
        )
    }

    private fun openContentSelectionBottomSheet(
        rvList: ArrayList<ContentSelectionModel>, index: Int
    ) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].itemname, rvList[i]))

        }
        openCommonBottomSheet(this, "Content Selection", this, commonList, true, index)

    }


    private fun getpackingLov() {
        viewModel.getPackingLov(
            loginDataModel?.companyid.toString(),
            "greentransweb_packinglov_forbooking",
            listOf(),
            arrayListOf()
        )
    }

    private fun openPackingSelectionBottomSheet(
        rvList: ArrayList<PackingSelectionModel>, index: Int
    ) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].packingname, rvList[i]))

        }
        openCommonBottomSheet(this, "Packing Selection", this, commonList, true, index)

    }


    private fun getGelPackLov() {
        viewModel.getGelPackLov(
            loginDataModel?.companyid.toString(),
            "greentrans_booking_gelpacklov",
            listOf(),
            arrayListOf()
        )
    }

    private fun openGelPackSelectionBottomSheet(
        rvList: ArrayList<GelPackItemSelectionModel>, index: Int
    ) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].itemname, rvList[i]))

        }
        openCommonBottomSheet(this, "Gel Pack Selection", this, commonList, true, index)

    }


    private fun boxNoValidate(boxNo: String?) {
        if (boxNo.isNullOrBlank()) {
            errorToast("Not a valid Box, Please scan again")
            return
        }
        viewModel.boxNoValidate(
            loginDataModel?.companyid.toString(),
            "greentransweb_validateboxno",
            listOf("prmbranchcode", "prmpackingcode", "prmasondt", "prmgrno", "prmboxno"),
            arrayListOf(
                userDataModel!!.loginbranchcode.toString(),
                selectedGelPackItemCode,
                bookingSqlDt!!,
                "",
                boxNo
            )
        )
    }


    private fun getTempLov() {
        viewModel.getTemperatureList(
            loginDataModel?.companyid.toString(), "gettemparature_lov", listOf(), arrayListOf()
        )
    }

    private fun openTemperatureSelectionBottomSheet(
        rvList: ArrayList<TemperatureSelectionModel>, index: Int
    ) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].name, rvList[i]))

        }
        openCommonBottomSheet(this, "Temperature Selection", this, commonList, true, index)
    }

    private val tempMuteLiveData = MutableLiveData<ArrayList<TemperatureSelectionModel>>()
    val tempLiveData: LiveData<ArrayList<TemperatureSelectionModel>>
        get() = tempMuteLiveData

//--------------------------------------------------------------------------------------------------------


    private fun setupBookingRecyclerView() {
        if (bookingAdapter == null) {
            manager = LinearLayoutManager(this)
            activityBinding.bookingRv.layoutManager = manager
        }
        bookingAdapter = DirectBookingAdapter(this, productCode, this, bookingList, this)
        activityBinding.bookingRv.adapter = bookingAdapter
//    }
    }

    fun addMoreGridItem(refModel: SinglePickupRefModel) {
        Log.d("CREATING NEW ITEM IN THE GRID, PACKAGE_TYPE", refModel.packagetype.toString())
        if (refModel.packagetype.toString() == BookingActivity.JEENA_PACKING) {

        } else {
            try {
                val obj = SinglePickupRefModel(refModel)
                bookingList.add(obj)
                bookingAdapter?.notifyItemInserted(bookingList.size - 1)
            } catch (err: Exception) {
                errorToast(err.message)
            }
        }
    }

    private fun checkFieldsBeforeSave() {
        if (activityBinding.inputDate.text.isNullOrEmpty()) {
            activityBinding.inputDate.error = "Please Select Date"
            errorToast("Please Select Date")
            return
        } else if (activityBinding.inputTime.text.isNullOrEmpty()) {
            errorToast("Please Select Time")
            return
        } else if (!activityBinding.autoGrCheck.isChecked && activityBinding.inputGrNo.text.isNullOrBlank()) {
            errorToast("Please Enter GR Number")
            return
        } else if (activityBinding.inputCustName.text.isNullOrEmpty()) {
            errorToast("Please Select Customer")
            return
        } else if (activityBinding.consignorName.text.isNullOrEmpty()) {
            errorToast("Please Select Consignor Name")
            return
//        }else if (activityBinding.inputCngrAddress.text.isNullOrEmpty()){
//            errorToast("Please Select Consignor Address")
//            return
        } else if (activityBinding.consigneeName.text.isNullOrEmpty()) {
            errorToast("Please Select Consignee Name")
            return
//        }else if (activityBinding.inputCngeAddress.text.isNullOrEmpty()){
//            errorToast("Please Select Consignee Address")
//            return
        } else if (activityBinding.inputOrigin.text.isNullOrEmpty()) {
            errorToast("Please Select origin")
            return
        } else if (activityBinding.inputDestination.text.isNullOrEmpty()) {
            errorToast("Please Select Destination")
            return
//        }
//        else if (activityBinding.selectedPckgsType.autofillHints.isNullOrEmpty()){
//            errorToast("Please Select Pckgs Type")
//            return
        } else if (activityBinding.inputAWeight.text.isNullOrEmpty()) {
            errorToast("Please Enter Actual Weight.")
            return
        } else if (activityBinding.inputVolWeight.text.isNullOrEmpty()) {
            errorToast("Please Enter Volumetric Weight.")
            return
        } else if (activityBinding.inputChargeableWeight.text.isNullOrEmpty()) {
            errorToast("Please Enter Chargeable Weight.")
            return
        }

        if (bookedByCode == "V") {
            if (activityBinding.inputVehicleVendor.text.isNullOrEmpty()) {
                errorToast("Please Select Agent")
                return
            }
        } else if (bookedByCode == "S") {
            if (activityBinding.inputPickupBoy.text.isNullOrEmpty()) {
                errorToast("Please Select Pickup Boy")
                return
            }
        } else if (bookedByCode == "M") {
            if (activityBinding.inputVehicle.text.isNullOrEmpty()) {
                errorToast("Please Select Vehicle")
                return
            } else if (activityBinding.inputPickupBoy.text.isNullOrEmpty()) {
                errorToast("Please Select Pickup Boy")
                return
            } else if (activityBinding.inputVehicleVendor.text.isNullOrEmpty()) {
                errorToast("Please Select Vendor")
                return
            }
        } else if (bookedByCode == "O") {
            if (activityBinding.inputPickupBoy.text.isNullOrEmpty()) {
                errorToast("Please Select Pickup Boy")
                return
            }


//        }
            //        else if (bookedByCode == "N") {
//            if (activityBinding.inputPickupBoy.text.isNullOrEmpty()) {
//                errorToast("Please Select Pickup Boy")
//                return
//            }
        }
        AlertDialog.Builder(this).setTitle("Alert!!!")
            .setMessage("Are you sure you want to save this booking?")
            .setPositiveButton("Yes") { _, _ ->
                saveBooking()
            }.setNeutralButton("No") { _, _ -> }.show()
    }


    private fun customerSelectionBottomSheet(
        mContext: Context, title: String, onRowClick: OnRowClick<Any>, clickType: String
    ) {
        val bottomSheetDialog =
            CustomerSelectionBottomSheet.newInstance(mContext, title, onRowClick, clickType)
        bottomSheetDialog.show(supportFragmentManager, CustomerSelectionBottomSheet.TAG)
    }

    private fun customerCodeSelectionBottomSheet(
        mContext: Context, title: String, onRowClick: OnRowClick<Any>, clickType: String
    ) {
        val bottomSheetDialog =
            CustomerCodeSelectionBottomSheet.newInstance(mContext, title, onRowClick, clickType)
        bottomSheetDialog.show(supportFragmentManager, CustomerCodeSelectionBottomSheet.TAG)
    }

    private fun departmentSelectionBottomSheet(
        mContext: Context, title: String, onRowClick: OnRowClick<Any>, clickType: String
    ) {
        val bottomSheetDialog =
            DepartmentSelectionBottomSheet.newInstance(mContext, title, onRowClick, clickType)
        bottomSheetDialog.show(supportFragmentManager, DepartmentSelectionBottomSheet.TAG)
    }

    private fun pinCodeSelectionBottomSheet(
        mContext: Context, title: String, onRowClick: OnRowClick<Any>, clickType: String
    ) {
        val bottomSheetDialog =
            PinCodeSelectionBottomSheet.newInstance(mContext, title, onRowClick, clickType)
        bottomSheetDialog.show(supportFragmentManager, PinCodeSelectionBottomSheet.TAG)
    }

    private fun cngrCngeSelectionBottomSheet(
        mContext: Context,
        title: String,
        onRowClick: OnRowClick<Any>,
        clickType: String,
        custcode: String,
        branchcode: String
    ) {
        val bottomSheetDialog = CngrCngeSelectionBottomSheet.newInstance(
            mContext, title, onRowClick, clickType, custcode, branchcode
        )
        bottomSheetDialog.show(supportFragmentManager, CngrCngeSelectionBottomSheet.TAG)
    }

    private fun vehicleBottomSheet(mContext: Context, title: String, onRowClick: OnRowClick<Any>) {
        val bottomSheetDialog = VehicleSelectionBottomSheet.newInstance(mContext, title, onRowClick)
        bottomSheetDialog.show(supportFragmentManager, VehicleSelectionBottomSheet.TAG)
    }

    private fun vendorBottomSheet(mContext: Context, title: String, onRowClick: OnRowClick<Any>) {
        val bottomSheetDialog = VendorSelectionBottomSheet.newInstance(
            mContext, title, onRowClick, if (bookedByCode == "M") "VEHICLE VENDOR" else "PUD VENDOR"
        )
        bottomSheetDialog.show(supportFragmentManager, VendorSelectionBottomSheet.TAG)
    }

    override fun onAlertClick(alertClick: AlertClick, alertCallType: String, data: Any?) {
        when (alertClick) {
            AlertClick.YES -> run {
                when (alertCallType) {
                    DirectBookingActivity.PRINT_GR_TAG -> run {
                        try {
                            var grNo: String = data as String
                            grNo.let {
                                openPrintGrBottomSheet(this, "Print Sticker", grNo)
                            }
                        } catch (ex: Exception) {
                            errorToast(ex.message)
                        }
                    }

                    else -> {
                        Log.d(DirectBookingActivity.Companion::class.java.name, alertCallType)
                    }
                }
            }

            AlertClick.NO -> run {
                finish()
            }

            else -> {

            }
        }
    }

    override fun onItemClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }

    override fun onClick(data: Any, clickType: String) {

        if (clickType == CustomerSelectionBottomSheet.CUSTOMER_CLICK_TYPE) {
            try {
                val selectedCustomerName = data as CustomerSelectionModel
                activityBinding.inputCustName.setText(selectedCustomerName.custname)
                activityBinding.inputCustCode.setText(selectedCustomerName.custcode)
            } catch (ex: Exception) {
                errorToast(ex.message)
            }
        } else if (clickType == PinCodeSelectionBottomSheet.ORIGIN_PIN_CODE_CLICK_TYPE) {
            try {
                val selectedPinData = data as PinCodeModel
                activityBinding.inputOriginPinCode.setText(selectedPinData.code)
                activityBinding.originGateway.setText(selectedPinData.flagseven)
                activityBinding.inputOriginOda.setText(selectedPinData.flagfive)
                activityBinding.inputOrigin.setText(selectedPinData.flagfour)
                activityBinding.inputOriginArea.setText(selectedPinData.flagone)
                activityBinding.inputOriginOdaDistance.setText(selectedPinData.intvalue.toString())
                originCode = selectedPinData.flagthree.toString()
            } catch (ex: Exception) {
                errorToast(ex.message)
            }
        } else if (clickType == PinCodeSelectionBottomSheet.DESTINATION_PIN_CODE_CLICK_TYPE) {
            try {
                val selectedPinData = data as PinCodeModel
                activityBinding.inputDestinationPinCode.setText(selectedPinData.code)
                activityBinding.destinationGateway.setText(selectedPinData.flageight)
                activityBinding.inputDestinationOda.setText(selectedPinData.flagsix)
                activityBinding.inputDestination.setText(selectedPinData.stnname)
                activityBinding.inputDestinationArea.setText(selectedPinData.flagone)
                activityBinding.inputDestinationOdaDistance.setText(selectedPinData.intvalue.toString())
                destinationCode = selectedPinData.stncode
            } catch (ex: Exception) {
                errorToast(ex.message)
            }
        } else if (clickType == DepartmentSelectionBottomSheet.DEPARTMENT_CLICK_TYPE) {
            try {
                val selectedDepartmentData = data as DepartmentSelectionModel
                activityBinding.inputDepartmentName.setText(selectedDepartmentData.custdeptname)
                custDeptid = selectedDepartmentData.custdeptid.toString()
            } catch (ex: Exception) {
                errorToast(ex.message)
            }
        } else if (clickType == CngrCngeSelectionBottomSheet.CNGR_CLICK_TYPE) {
            try {
                val selectedCngeCngrData = data as CngrCngeSelectionModel
                activityBinding.consignorName.setText(selectedCngeCngrData.name)
                cngrCode = selectedCngeCngrData.code
//                activityBinding.consignorMobile.setText(selectedCngeCngrData.telno ?: "")
                cngrAddress = selectedCngeCngrData.address
                cngrCity = selectedCngeCngrData.city
                cngrzipCode = selectedCngeCngrData.zipcode
                cngrState = selectedCngeCngrData.state
                cngrTelNo = selectedCngeCngrData.telno
                cngrEmail = selectedCngeCngrData.email.toString()
                cngrCstNo = selectedCngeCngrData.cstno.toString()
                cngrLstNo = selectedCngeCngrData.lstno.toString()
                cngrTinNo = selectedCngeCngrData.tinno.toString()
                cngrGstNo = selectedCngeCngrData.gstno.toString()
                cngrStaxregno = selectedCngeCngrData.staxregno.toString()

            } catch (ex: Exception) {
//                errorToast(ex.message)
            }
        } else if (clickType == CngrCngeSelectionBottomSheet.CNGE_CLICK_TYPE) {
            try {
                val selectedCngeCngrData = data as CngrCngeSelectionModel
                activityBinding.consigneeName.setText(selectedCngeCngrData.name)
                cngeCode = selectedCngeCngrData.code
                activityBinding.consigneeMobile.setText(selectedCngeCngrData.telno ?: "")
                cngeAddress = selectedCngeCngrData.address
                cngeCity = selectedCngeCngrData.city
                cngezipCode = selectedCngeCngrData.zipcode
                cngeState = selectedCngeCngrData.state
                cngeTelNo = selectedCngeCngrData.telno
                cngeEmail = selectedCngeCngrData.email.toString()
                cngeCstNo = selectedCngeCngrData.cstno.toString()
                cngeLstNo = selectedCngeCngrData.lstno.toString()
                cngeTinNo = selectedCngeCngrData.tinno.toString()
                cngeGstNo = selectedCngeCngrData.gstno.toString()
                cngeStaxregno = selectedCngeCngrData.staxregno.toString()
            } catch (ex: Exception) {
//                errorToast(ex.message)
            }
        } else if (clickType == VehicleSelectionBottomSheet.VEHICLE_CLICK_TYPE) {
            try {
                val selectedVehicleData = data as VehicleModelDRS
                activityBinding.inputVehicle.setText(selectedVehicleData.regno)
            } catch (ex: Exception) {
                errorToast(ex.message)
            }

        } else if (clickType == VendorSelectionBottomSheet.VENDOR_CLICK_TYPE) {
            try {
                val selectedVendorData = data as VendorModelDRS
                activityBinding.inputVehicleVendor.setText(selectedVendorData.vendname)
            } catch (ex: Exception) {
                errorToast(ex.message)
            }
        } else if (clickType == CustomerCodeSelectionBottomSheet.CUSTOMER_CLICK_TYPE) {
            try {
                val selectdData = data as CustomerSelectionModel
                activityBinding.inputCustCode.setText(selectdData.custcode)
                activityBinding.inputCustName.setText(selectdData.custname)
            } catch (ex: Exception) {
                errorToast(ex.message)
            }
        }

    }

    fun setVWeight(vWeight: Float) {
        val localVWeight = round(vWeight).toInt()
        activityBinding.inputVolWeight.setText(localVWeight.toString())
    }

    fun setAWeight(aWeight: Int) {
        activityBinding.inputAWeight.setText(aWeight.toString())
    }

    fun setCWeight(cWeight: Int) {
        activityBinding.inputChargeableWeight.setText(cWeight.toString())
        val aWeight = activityBinding.inputAWeight.text.toString().toDoubleOrNull()
        val cWeight = activityBinding.inputChargeableWeight.text.toString().toDoubleOrNull()
        var vWeight = activityBinding.inputVolWeight.text.toString().toDoubleOrNull()
        if (aWeight != null) {
            if (vWeight == null) vWeight = 0.0
            val higherValue = Math.max(aWeight.toInt() ?: 0, vWeight.toInt() ?: 0)
            activityBinding.inputChargeableWeight.setText(higherValue.toString())
        }

    }

    private fun setBookedBySpinners() {
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, bookingTypeList)
        activityBinding.selectedBookedType.adapter = adapter
        activityBinding.selectedBookedType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    bookedByCode = bookingTypeList[position].code
//                    vendorCode = ""
//                    vehicleCode = ""
                    when (bookedByCode) {
                        //Agent
                        "V" -> run {
                            activityBinding.inputVehicleVendor.setText("")
                            activityBinding.inputVehicle.setText("")
                            activityBinding.agentVendorHeader.setText("Select Agent")
                            activityBinding.agentVendorPlaceholder.setHint("Select Agent")
                            activityBinding.inputLayoutVehicleVendor.visibility = View.VISIBLE
                            activityBinding.inputLayoutVehicle.visibility = View.GONE
                            activityBinding.inputLayoutPickBoy.visibility = View.GONE

                        }
                        // market vehicle
                        "M" -> run {
                            activityBinding.inputVehicleVendor.text?.clear()
                            activityBinding.agentVendorHeader.setText("Vehicle Vendor")
                            activityBinding.agentVendorPlaceholder.setHint("Select Vehicle Vendor")
                            activityBinding.inputLayoutVehicleVendor.visibility = View.VISIBLE
                            activityBinding.inputLayoutVehicle.visibility = View.VISIBLE
                            activityBinding.inputLayoutPickBoy.visibility = View.VISIBLE
                        }
                        //JEENA STRAFF
                        "S" -> run {
                            activityBinding.inputVehicle.text?.clear()
                            activityBinding.inputVehicleVendor.text?.clear()
                            activityBinding.inputLayoutVehicleVendor.visibility = View.GONE
                            activityBinding.inputLayoutVehicle.visibility = View.GONE
                            activityBinding.inputLayoutVehicle.visibility = View.GONE
                            activityBinding.inputLayoutPickBoy.visibility = View.VISIBLE
                        }
                        //OFFICE/AIRPORT DROP
                        "O" -> run {
                            activityBinding.inputVehicle.text?.clear()
                            activityBinding.inputVehicleVendor.text?.clear()
                            activityBinding.inputLayoutVehicleVendor.visibility = View.GONE
                            activityBinding.inputLayoutVehicle.visibility = View.GONE
                            activityBinding.inputLayoutVehicle.visibility = View.GONE
                            activityBinding.inputLayoutPickBoy.visibility = View.VISIBLE
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
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, serviceTypeList)
        activityBinding.selectService.adapter = adapter
        activityBinding.selectService.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    productCode = serviceTypeList[position].code
                    bookingAdapter?.serviceTypeChanged()
                    when (productCode) {
                        "A" -> run {
//                            errorToast("air")
                        }

                        "S" -> run {
//                            errorToast("surface")
                        }

                        "E" -> run {
//                           errorToast("express")
                        }

                        "T" -> run {
//                            errorToast("train")
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
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, pckgsTypeList)
        activityBinding.selectPckgs.adapter = adapter
        activityBinding.selectPckgs.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                   var noOfPckgs =activityBinding.inputNoOfPckgs.text.toString().toIntOrNull() ?: 0
                    pckgsByCode = pckgsTypeList[position].code
                    pckgsType = pckgsTypeList[position].name
                    when (pckgsByCode) {

                        "Jeena Packing" -> run {
                            activityBinding.btnAddMore.visibility = View.GONE
                            activityBinding.boxNoTxt.visibility = View.VISIBLE
                            activityBinding.actionTxt.visibility = View.GONE
                            activityBinding.gridType.setText(pckgsType)
                            addItemToGrid(noOfPckgs!!)
                        }

                        "Pre Packed" -> run {
                            activityBinding.btnAddMore.visibility = View.VISIBLE
                            activityBinding.boxNoTxt.visibility = View.GONE
                            activityBinding.actionTxt.visibility = View.VISIBLE
                            activityBinding.gridType.setText(pckgsType)
                            addItemToGrid(noOfPckgs!!)
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

//    private fun setupInvoiceRecyclerView() {
//        if (rvAdapter == null) {
//            manager = LinearLayoutManager(this)
//            activityBinding.invoiceRecyclerView.layoutManager = manager
//        }
//        rvAdapter = InVoiceListAdapter(invoiceList, this, this)
//        activityBinding.invoiceRecyclerView.adapter = rvAdapter
////    }
//    }

    private fun updateInvoiceList(numberOfItems: Int) {
        invoiceList.clear() // Clear the current list
        for (i in 1..numberOfItems) {
            invoiceList.add(InvoiceDataModel("", "", "", "", "", "", ""))
        }
    }

    private fun openPrintGrBottomSheet(mContext: Context, title: String, grNo: String) {
        val bottomSheetDialog = PrintGrBottomSheet.newInstance(mContext, title, grNo)
        bottomSheetDialog.show(supportFragmentManager, PrintGrBottomSheet.TAG)
    }
//    override fun onRowClick(data: Any, clickType: String, index: Int) {
//        super.onRowClick(data, clickType, index)
//        val removingItem: InvoiceDataModel = data as InvoiceDataModel
//
//        if (invoiceList.isNotEmpty() && index <= invoiceList.size - 1) {
//            invoiceList.removeAt(index)
//            setupInvoiceRecyclerView()
//            successToast("Successfully Removed. Invoice: ${removingItem.invoiceno}")
//        } else {
//            errorToast("Something went wrong.")
//        }
//    }


    override fun onItemClickWithAdapter(data: Any, clickType: String, index: Int) {
        if (clickType == "Content Selection") {
            val selectedContent = data as ContentSelectionModel
            bookingAdapter?.setContent(selectedContent, index)
            pickupContentId = selectedContent.itemcode

        } else if (clickType == "Temperature Selection") {
            val selectedTemp = data as TemperatureSelectionModel
            bookingAdapter?.setTemperature(selectedTemp, index)

        } else if (clickType == "Packing Selection") {
            val selectedPckg = data as PackingSelectionModel
            bookingAdapter?.setPacking(selectedPckg, index)

        } else if (clickType == "Gel Pack Selection") {
            val selectedGelPack = data as GelPackItemSelectionModel
            bookingAdapter?.setGelPack(selectedGelPack, index)
            selectedGelPackItemCode = selectedGelPack.itemcode.toString()

        }


    }

    override fun onRowClick(data: Any, clickType: String, index: Int) {

        if (clickType == "CONTENT_SELECT") {
            openContentSelectionBottomSheet(contentList, index)

        } else if (clickType == "TEMPERATURE_SELECT") {
            openTemperatureSelectionBottomSheet(temperatureList, index)

        } else if (clickType == "GEL_PACK_SELECT") {
            openGelPackSelectionBottomSheet(gelPackList, index)

        } else if (clickType == "PACKING_SELECT") {
            openPackingSelectionBottomSheet(packingList, index)

        } else if (clickType == "VALIDATE_BOX") {
            val singlePickupRefModel = data as SinglePickupRefModel
            if (singlePickupRefModel.boxno.isNullOrBlank()) {
                errorToast("Enter Box # to validate.")
            } else {
                boxNoValidate(singlePickupRefModel.boxno)
            }
        } else if (clickType == "REMOVE_SELECT") {
//                singlePickupDataList.forEachIndexed {index, element ->
//                singlePickupDataList.removeAt(index)
//                activityBinding.recyclerView.adapter!!.notifyItemRangeRemoved(index,singlePickupDataList.size)
//
        }
    }

    private fun saveBooking() {
        var actualRefNoStr: String = ""
        var actualWeightStr: String = ""
        var actualPackageTypeStr: String = ""
        var actualTempuratureStr: String = ""
        var actualPackingStr: String = ""
        var actualContent: String = ""
        var actualDryIceStr: String = ""
        var actualDryIceQtyStr: String = ""
        var actualDataLoggerNoStr: String = ""
        var actualDataLoggerStr: String = ""
        var actualDimHeight: String = ""
        var actualDimBreath: String = ""
        var actualDimLength: String = ""
        var actualBoxNo: String = ""
        var actualGelPackStr: String = ""
        var actualGelPackQtyStr: String = ""
        var actualGelPackItemCodeStr: String = ""
        var actualPackageStr: String = ""
        var actualVWeightStr: String = ""
        var totalPackage = 0;

        var adapterEnteredData: ArrayList<SinglePickupRefModel>? = bookingAdapter?.getEnteredData()
        run enteredData@{
            adapterEnteredData?.forEachIndexed { index, singlePickupRefModel ->
//                if (singlePickupRefModel.referenceno.toString().isNullOrEmpty()) {
//                    errorToast("Reference # Not Entered at INPUT - ${index + 1}")
//                    return
//                } else
//                 totalPackage += adapterEnteredData[index].pcs
                if (singlePickupRefModel.boxno.isNullOrEmpty() && singlePickupRefModel.packagetype == BookingActivity.JEENA_PACKING) {
                    errorToast("Box # Not Entered at INPUT - ${index + 1}")
                    return
                } else if (Utils.isDecimalNotEntered(singlePickupRefModel.weight.toString())) {
                    errorToast("Weight Not Entered at INPUT - ${index + 1}")
                    return
                } else if (Utils.isDecimalNotEntered(singlePickupRefModel.localLength.toString())) {
                    errorToast("Length Not Entered at INPUT - ${index + 1}")
                    return
                } else if (Utils.isDecimalNotEntered(singlePickupRefModel.localBreath.toString())) {
                    errorToast("Breadth Not Entered at INPUT - ${index + 1}")
                    return
                } else if (Utils.isDecimalNotEntered(singlePickupRefModel.localHeight.toString())) {
                    errorToast("Height Not Entered at INPUT - ${index + 1}")
                    return
                }
            }
        }


        for (i in 0 until bookingList.size) {
            totalPackage += bookingList[i].pcs
            val refNo = bookingList[i].detailreferenceno
            val weightStr = adapterEnteredData?.get(i)?.weight ?: bookingList[i].weight ?: ""
            val pckgType = bookingList[i].packagetype ?: ""
            val tempStr =
                if (adapterEnteredData == null) bookingList[i].tempurature else adapterEnteredData[i].tempurature
            val packing = bookingList[i].packing
            val goodsStr = bookingList[i].contents
            val dryIceStr = bookingList[i].dryice
            val dryIceQtyStr = adapterEnteredData?.get(i)?.dryiceqty ?: bookingList[i].dryiceqty
            val dataLoggerStr = adapterEnteredData?.get(i)?.datalogger ?: bookingList[i].datalogger
            var dataLoggerNoStr = ""


            if (dataLoggerStr == "Y") {
                dataLoggerNoStr =
                    adapterEnteredData?.get(i)?.dataloggerno ?: bookingList[i].dataloggerno ?: ""
            }

            val gelPackItemCodeStr =
                adapterEnteredData?.get(i)?.gelpackitemcode ?: bookingList[i].gelpackitemcode ?: ""
//            val dimHeight=singleRefList[i].localHeight
//            val dimBreath=singleRefList[i].localBreath
//            val dimLength=singleRefList[i].localLength
            val dimHeight =
                if (adapterEnteredData == null) bookingList[i].localHeight.toInt() else adapterEnteredData[i].localHeight.toInt()
            val dimBreath =
                if (adapterEnteredData == null) bookingList[i].localBreath.toInt()else adapterEnteredData[i].localBreath.toInt()
            val dimLength =
                if (adapterEnteredData == null) bookingList[i].localLength.toInt() else adapterEnteredData[i].localLength.toInt()
            val boxNoStr = adapterEnteredData?.get(i)?.boxno ?: bookingList[i].boxno ?: ""
            val gelPackStr = bookingList[i].gelpack
            val gelPackQtyStr = adapterEnteredData?.get(i)?.gelpackqty ?: bookingList[i].gelpackqty
            val packageStr =
                if (adapterEnteredData == null) bookingList[i].pcs else adapterEnteredData[i].pcs
            val vWeightStr = bookingList[i].volfactor
//            val vWeightStr = bookingList[i].localVWeight

            actualRefNoStr += "$refNo,"
            actualWeightStr += "$weightStr,"
            actualPackageTypeStr += "$pckgType,"
            actualTempuratureStr += "$tempStr,"
            actualPackingStr += "$packing,"
            actualContent += "$goodsStr,"
            actualDryIceStr += "$dryIceStr,"
            actualDryIceQtyStr += "$dryIceQtyStr,"
            actualDataLoggerStr += "$dataLoggerStr,"
            actualDataLoggerNoStr += "$dataLoggerNoStr,"
            actualDimHeight += "$dimHeight,"
            actualDimBreath += "$dimBreath,"
            actualDimLength += "$dimLength,"
            actualBoxNo += "$boxNoStr,"
            actualGelPackStr += "$gelPackStr,"
            actualGelPackQtyStr += "$gelPackQtyStr,"
            actualGelPackItemCodeStr += "$gelPackItemCodeStr,"
            actualPackageStr += "$packageStr,"
            actualVWeightStr += "$vWeightStr,"
        }
        var invoiceStr: String = ""
        var invoiceDtStr: String = ""
        var invoiceValueStr: String = ""
        var ewbNoStr: String = ""
        var ewbDtStr: String = ""
        var ewbValidUptoStr: String = ""
        var cngrAddress: String =

            if (Utils.ewayBillValidated && Utils.ewayBillDetailResponse != null) Utils.ewayBillDetailResponse!!.response.fromAddr1 else ""
        var cngeAddress: String =
            if (Utils.ewayBillValidated && Utils.ewayBillDetailResponse != null) Utils.ewayBillDetailResponse!!.response.fromAddr2 else ""
        if (totalPackage != activityBinding.inputNoOfPckgs.text.toString().toInt()) {
            errorToast("No Of Package Should Be Less Than Total Packages");
        }
        if (Utils.ewayBillValidated && Utils.enteredValidatedEwayBillList != null) {
            Utils.enteredEwayBillValidatedData.forEachIndexed { index, validatedData ->
                invoiceStr += "${validatedData.response.docNo},"
                if (validatedData.response.docDate != null) {
                    invoiceDtStr += "${Utils.changeDateFormatForEwayInvoiceDt(validatedData.response.docDate)},"
                } else {
                    invoiceDtStr += ","
                }
                invoiceValueStr += "${validatedData.response.totalValue},"
                ewbNoStr += "${validatedData.response.ewbNo},"
                if (validatedData.response.ewbDate != null) {
                    ewbDtStr += "${Utils.changeDateFormatForEway(validatedData.response.ewbDate)},"
                } else {
                    ewbDtStr += ","
                }
                if (validatedData.response.validUpto != null) {
                    ewbValidUptoStr += "${Utils.changeDateFormatForEway(validatedData.response.validUpto)},"
                } else {
                    ewbValidUptoStr += ","
                }

            }
        } else if (activityBinding.inputDepartmentName.text.isNullOrEmpty()) {
            custDeptid = ""
        }
        Log.d("total pckgs", totalPackage.toString())

        viewModel.saveBooking(
            companyId = userDataModel?.companyid.toString(),
            branchcode = originCode,
            bookingdt = bookingSqlDt.toString(),
            time = activityBinding.inputTime.text.toString(),
            egrno = activityBinding.inputGrNo.text.toString(),
            custcode = activityBinding.inputCustCode.text.toString(),
            destcode = destinationCode,
            productcode = productCode,
            pckgs = activityBinding.inputNoOfPckgs.text.toString().toInt(),
            aweight = activityBinding.inputAWeight.text.toString(),
            vweight = activityBinding.inputVolWeight.text.toString(),
            cweight = activityBinding.inputChargeableWeight.text.toString(),
            createid = getUserCode(),
            sessionid = getSessionId(),
            refno = "",
            cngr = activityBinding.consignorName.text.toString(),
            cngraddress = returnNonNullableString(cngrAddress),
            cngrcity = returnNonNullableString(cngrCity),
            cngrzipcode = returnNonNullableString(cngrzipCode),
            cngrstate = returnNonNullableString(cngrState),
            cngrmobileno = activityBinding.consignorMobile.text.toString(),
            cngremailid = returnNonNullableString(cngeEmail),
            cngrCSTNo = returnNonNullableString(cngrCstNo),
            cngrLSTNo = returnNonNullableString(cngrLstNo),
            cngrTINNo = returnNonNullableString(cngrTinNo),
            cngrSTaxRegNo = returnNonNullableString(cngrStaxregno),
            cnge = activityBinding.consigneeName.text.toString(),
            cngeaddress = returnNonNullableString(cngeAddress),
            cngecity = returnNonNullableString(cngeCity),
            cngezipcode = returnNonNullableString(cngezipCode),
            cngestate = returnNonNullableString(cngeState),
            cngemobileno = activityBinding.consigneeMobile.text.toString(),
            cngeemailid = returnNonNullableString(cngeEmail),
            cngeCSTNo = returnNonNullableString(cngeCstNo),
            cngeLSTNo = returnNonNullableString(cngeLstNo),
            cngeTINNo = returnNonNullableString(cngeTinNo),
            cngeSTaxRegNo = returnNonNullableString(cngeStaxregno),
            transactionid = 0,
            mawbchargeapplicable = "N",
            custdeptid = returnNonNullableString(custDeptid),
            referencenostr = actualRefNoStr,
            weightstr = actualWeightStr,
            packagetypestr = actualPackageTypeStr,
            tempuraturestr = actualTempuratureStr,
            packingstr = actualPackingStr,
            goodsstr = actualContent,
            dryicestr = actualDryIceStr,
            dryiceqtystr = actualDryIceQtyStr,
            dataloggerstr = actualDataLoggerStr,
            dataloggernostr = actualDataLoggerNoStr,
            dimlength = actualDimLength,
            dimbreath = actualDimBreath,
            dimheight = actualDimHeight,
            pickupboyname = activityBinding.inputPickupBoy.text.toString(),
            boyid = getExecutiveId().toInt(),
            boxnostr = actualBoxNo,
            stockitemcodestr = pickupContentId,
            gelpackstr = actualGelPackStr,
            gelpackitemcodestr = actualGelPackItemCodeStr,
            gelpackqtystr = actualGelPackQtyStr,
            menucode = menuCode,
            invoicenostr = invoiceStr,
            invoicedtstr = invoiceDtStr,
            invoicevaluestr = invoiceValueStr,
            ewaybillnostr = ewbNoStr,
            ewaybilldtstr = ewbDtStr,
            ewbvaliduptodtstr = ewbValidUptoStr,
            vendorcode = vendorCode,
            pckgsstr = actualPackageStr,
            pickupby = bookedByCode,
            vehicleno = activityBinding.inputVehicle.text.toString(),
            vweightstr = actualVWeightStr,
            vehiclecode = vehicleCode,
            cngrcode = cngrCode,
            cngecode = cngeCode,
            remarks = activityBinding.inputRemark.text.toString(),
            cngrgstno = returnNonNullableString(cngrGstNo),
            cngegstno = returnNonNullableString(cngeGstNo),
            cngrtelno = returnNonNullableString(cngeTelNo),
            cngetelno = returnNonNullableString(cngeTelNo),
            orgpincode = activityBinding.inputOriginPinCode.text.toString(),
            destpincode = activityBinding.inputDestinationPinCode.text.toString(),
            pickuppoint = activityBinding.inputOriginArea.text.toString(),
            deliverypoint = activityBinding.inputDestinationArea.text.toString()
        )
    }

    fun returnNonNullableString(str: String?): String {
        return if(str.isNullOrBlank()) "" else str
    }
}