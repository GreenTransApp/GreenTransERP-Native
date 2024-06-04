package com.greensoft.greentranserpnative.ui.operation.booking

//import com.google.android.material.floatingactionbutton.FloatingActionButton

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.ENV
import com.greensoft.greentranserpnative.ENV.Companion.DEBUGGING
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityBookingBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.attachedImages.AttachedImagesBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.attachedImages.OnAddImageClickListener
import com.greensoft.greentranserpnative.ui.bottomsheet.bookingIndentInfoBottomSheet.BookingIndentInfoBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.common.models.CommonBottomSheetModel
import com.greensoft.greentranserpnative.ui.bottomsheet.printGR.PrintGrBottomSheet
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.common.alert.CommonAlert
import com.greensoft.greentranserpnative.ui.common.viewImage.ViewImage
import com.greensoft.greentranserpnative.ui.home.models.UserMenuModel
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.booking.models.*
import com.greensoft.greentranserpnative.ui.operation.eway_bill.EwayBillBottomSheet
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.models.PickupRefModel
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.models.SinglePickupRefModel
import com.greensoft.greentranserpnative.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.round


@AndroidEntryPoint
class BookingActivity @Inject constructor() : BaseActivity(), OnRowClick<Any>,
    BottomSheetClick<Any>, OnAddImageClickListener,
    AlertCallback<Any> {

    lateinit var activityBinding: ActivityBookingBinding
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var bottomSheetAdapter: AttachedImagesBottomSheet
    lateinit var rvImages: RecyclerView
    private var menuModel: UserMenuModel? = null
    private var title: String = "BOOKING"
    lateinit var palletWiseRemarks: String;
    lateinit var bottomSheetDialog: BottomSheetDialog;
    lateinit var palletName: String

    //    private var bookingList: List<BookingRecyclerModel> = ArrayList()
    private val viewModel: BookingViewModel by viewModels()
    private var customerList: ArrayList<CustomerSelectionModel> = ArrayList()
    private var cngrList: ArrayList<ConsignorSelectionModel> = ArrayList()
    private var cngeList: ArrayList<ConsignorSelectionModel> = ArrayList()
    private var deptList: ArrayList<DepartmentSelectionModel> = ArrayList()
    private var originList: ArrayList<OriginSelectionModel> = ArrayList()
    private var destinationList: ArrayList<DestinationSelectionModel> = ArrayList()
    private var pickupBoylist: ArrayList<PickupBoySelectionModel> = ArrayList()
    private var temperatureList: ArrayList<TemperatureSelectionModel> = ArrayList()
    private var packingList: ArrayList<PackingSelectionModel> = ArrayList()
    private var gelPackList: ArrayList<GelPackItemSelectionModel> = ArrayList()
    private var contentList: ArrayList<ContentSelectionModel> = ArrayList()
    private var singleRefList: ArrayList<SinglePickupRefModel> = ArrayList()
    private var pckgTypeList: ArrayList<PckgTypeSelectionModel> = ArrayList()
    private var pickupRefData: ArrayList<PickupRefModel> = ArrayList()
    private var serviceTypeList: ArrayList<ServiceTypeSelectionLov> = ArrayList()
    private var pickupByTypeList: ArrayList<PickupBySelection> = ArrayList()
    private var agentList: ArrayList<AgentSelectionModel> = ArrayList()
    private var vehicleVendorList: ArrayList<AgentSelectionModel> = ArrayList()
    private var vehicleList: ArrayList<BookingVehicleSelectionModel> = ArrayList()
    private var addList: ArrayList<SinglePickupRefModel> = ArrayList()
    private var bookingAdapter: BookingAdapter? = null
    private val menuCode: String = "GTAPP_GRBOOKING"

    var isFirstTime: Boolean = true
//    var serviceSelectedItems = arrayOf("SELECT", "AIR", "SURFACE", "SURFACE EXPRESS", "TRAIN")
//    var pckgsTypeSelectedItems = arrayOf("SELECT", "JEENA PACKING", "PRE PACKED")

//    var pickupTypeSelectedItems =
//        arrayOf("JEENA STAFF", "AGENT", " MARKET VEHICLE", "OFFICE/AIRPORT DROP", "CANCEL")

    var transactionId = ""
    var destCode = ""
    var custCode = ""
    var productCode = ""
    var cngrCity = ""
    var cngrZipCode = ""
    var cngrState = ""
    var cngrMobileNo = ""
    var cngreMailId = ""
    var cngrCSTNo = ""
    var cngrLSTNo = ""
    var cngrTINNo = ""
    var cngrCode = ""
    var cngrGstNo = ""
    var cngrSTaxRegNo = ""
    var cngeCity = ""
    var cngeZipCode = ""
    var cngeState = ""
    var cngeMobileNo = ""
    var cngeeMailId = ""
    var cngeCSTNo = ""
    var cngeLSTNo = ""
    var cngeTINNo = ""
    var cngeCode = ""
    var cngeGstNo = ""
    var cngeSTaxRegNo = ""
    var custDeptId = ""
    var boyId = ""
    var vendorCode = ""
    var pickupByValue = ""
    var vehicleCode = ""
    var selectedGelPackItemCode = ""
    var pickupContentId = ""
    var aWeight = ""
    var volWeight = ""
    var awbCheck = ""
    private var sqlDate: String? = null

    //    var actualAWeight = ""
    var actualAWeight: Float = 0f
    var actualVWeight = ""
    var isAddMoreBtnVisible: Boolean = false;

    companion object {
        val JEENA_PACKING: String = "Jeena Packing"
        const val PRINT_GR_TAG = "PRINT_GR_TAG"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("BOOKING ENTRY")
        setObservers()
        menuModel = getMenuData()
        activityBinding.inputDate.inputType = InputType.TYPE_NULL;
        activityBinding.inputTime.inputType = InputType.TYPE_NULL;
        setLayoutVisibility()
        activityBinding.inputDate.setText(getViewCurrentDate())
        sqlDate = getSqlCurrentDate()
        activityBinding.inputTime.setText(getSqlCurrentTime())
        activityBinding.inputGrNo.hint = "ENTER GR #"
        activityBinding.inputPickupBoy.setText(userDataModel!!.username.toString())
        getIntentData()

        getServiceType()
        getPickupBy()

//        getCustomerList()
//        getCngrList()
//        getCngeList()
//        getDeptList()
//        getOriginList()
//        getDestinationList()
//        getPickupBoyList()
        getTempLov()
        getpackingLov()
        getGelPackLov()
//        getVehicleLov()
//        getVehicleVendor()
        getAgentLov()
//        getPckgTypeLov()
        getContentList()
        spinnerSetUp()
        setOnClick()

//        onChangeRecyclerview()

    }

    override fun onResume() {
        super.onResume()

//        refreshData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.booking_indent_info_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.booking_indent_info_menu -> {
//                Toast.makeText(this, "testing", Toast.LENGTH_SHORT).show()
                openBookingIndentInfoBottomSheet(this, "Booking Indent Information", this)

            }
        }
        return super.onOptionsItemSelected(item)
    }

    //    override fun onResume() {
//        super.onResume()
//        receivedDataList.clear()
//        successToast(mContext,"resume")
//    }

    private fun getSingleRefData() {
        viewModel.getSingleRefData(
            loginDataModel?.companyid.toString(),
//            "10",
//            "greentransweb_jeenabooking_getpickupforbooking",
            "nativeerp_jeenabooking_getpickupforbooking",
            listOf("prmtransactionid"),
            arrayListOf(transactionId)
//            arrayListOf("804320")
        )
    }

    private fun getServiceType() {
        viewModel.getServiceList(
            loginDataModel?.companyid.toString(),
            "gtapp_getproductmast",
            listOf("prmusercode", "prmmenucode", "prmsessionid"),
            arrayListOf(
                userDataModel?.usercode.toString(),
                menuModel?.menucode.toString(),
                userDataModel?.sessionid.toString()
            )
        )
    }

    private fun getPickupBy() {
        viewModel.getPickupByLov(
            loginDataModel?.companyid.toString(),
            "gtapp_getpickupby",
            listOf("prmbranchcode", "prmusercode", "prmmenucode", "prmsessionid"),
            arrayListOf(
                userDataModel?.loginbranchcode.toString(),
                userDataModel?.usercode.toString(),
                menuModel?.menucode.toString(),
                userDataModel?.sessionid.toString()
            )
        )
    }

    private fun getIntentData() {
        if (intent != null) {
            val jsonString = intent.getStringExtra("ARRAY_JSON")
            if (jsonString != "") {
                val gson = Gson()
                val listType = object : TypeToken<List<PickupRefModel>>() {}.type
                val resultList: ArrayList<PickupRefModel> =
                    gson.fromJson(jsonString.toString(), listType)
                pickupRefData.addAll(resultList)
                pickupRefData.forEachIndexed { _, element ->
                    transactionId = element.transactionid.toString()
                    getSingleRefData()
                }
            }
        }
    }

    private fun openEwayBillBottomSheet(viewModel: BookingViewModel) {
        val bottomSheet = EwayBillBottomSheet.newInstance(
            this,
            "EWAY BILL",
            viewModel,
            loginDataModel,
            userDataModel
        )
        bottomSheet.show(supportFragmentManager, "Eway-Bill-BottomSheet")
    }

    private fun checkNullOrEmpty(value: Any?): String {
        if (value == "" || value == null) {
            return "NOT AVAILABLE"
        }
        return value.toString()
    }

    fun addMoreGridItem(refModel: SinglePickupRefModel) {
        Log.d("CREATING NEW ITEM IN THE GRID, PACKAGE_TYPE", refModel.packagetype.toString())
        if (refModel.packagetype.toString() == JEENA_PACKING) {

        } else {
            try {
                val obj = SinglePickupRefModel(refModel)
                singleRefList.add(obj)
                bookingAdapter?.notifyItemInserted(singleRefList.size - 1)
            } catch (err: Exception) {
                errorToast(err.message)
            }
        }
    }

    fun calculateVWeightUsingDimensions(
        length: Double,
        breadth: Double,
        height: Double,
        pcs: Int?
    ): Double {
        var value = 0
        if (productCode == "A") {
            value = (length * breadth * height).toInt() / 6000
        } else {
            value = (length * breadth * height).toInt() / 5000
        }
          var vWeight =  Math.ceil(value* pcs!!.toDouble())
         return vWeight
    }

    private fun setReferenceData() {
//        singleRefList.forEachIndexed { _, element ->
        if (singleRefList.isEmpty()) return
        val element: SinglePickupRefModel = singleRefList.get(0)
        activityBinding.tvJob.text = element.transactionid.toString() ?: "Not available"
        activityBinding.inputCustName.setText(checkNullOrEmpty(element.custname).toString())
        activityBinding.inputCngrName.setText(checkNullOrEmpty(element.cngr).toString())
//            activityBinding.inputCngrAddress.setText(checkNullOrEmpty(element.cngraddress).toString())
        activityBinding.inputCngeName.setText(checkNullOrEmpty(element.cnge).toString())
//            activityBinding.inputCngeAddress.setText(checkNullOrEmpty(element.cngeaddress).toString())
        activityBinding.inputDepartment.setText(checkNullOrEmpty(element.departmentname).toString())
        activityBinding.inputOrigin.setText(checkNullOrEmpty(element.Origin).toString())
        activityBinding.inputDestination.setText(checkNullOrEmpty(element.destname).toString())
        activityBinding.selectService.setAutofillHints(checkNullOrEmpty(element.servicetype).toString())
        activityBinding.selectedPckgsType.setText(checkNullOrEmpty(element.packagetype).toString())
        activityBinding.inputPckgsNo.setText(checkNullOrEmpty(element.pckgs).toString())
        var totalAWeight = 0.00;
        singleRefList.forEachIndexed { _, ref ->
            totalAWeight += ref.aweight
        }
        activityBinding.inputAWeight.setText(checkNullOrEmpty(totalAWeight))
        if (checkNullOrEmpty(element.volfactor) != "NOT AVAILABLE") {
            val volumetricWeight = round(element.volfactor)
            activityBinding.inputVolWeight.setText(volumetricWeight.toString())
        }
        aWeight = activityBinding.inputAWeight.text.toString()
        volWeight = activityBinding.inputVolWeight.toString()
//             activityBinding.selectedPckgsType.isEnabled=false
        custCode = element.custcode.toString()
        cngrCode = element.cngrcode.toString()
        cngeCode = element.cngecode.toString()
        destCode = element.destcode.toString()
//            addListMuteLiveData.postValue(activityBinding.inputPckgsNo.text.toString())
//            activityBinding.selectedPickupType.setAutofillHints(checkNullOrEmpty(element.gelpacktype).toString())
//           errorToast(activityBinding.selectedPckgsType.toString())


//        }
//        if(element.packagetype.toString() == "Pre Packed"){
//            activityBinding.btnAddMore.visibility = View.VISIBLE
////            isAddMoreBtnVisible = true;
//        }else{
//            activityBinding.btnAddMore.visibility = View.GONE
////            isAddMoreBtnVisible = false;
//        }
    }

    fun setLayoutVisibility() {
        activityBinding.inputLayoutPickBoy.visibility = View.VISIBLE
        activityBinding.inputLayoutAgent.visibility = View.GONE
        activityBinding.inputLayoutVehicle.visibility = View.GONE
        activityBinding.inputLayoutVehicleVendor.visibility = View.GONE
    }

    //      fun resetCalculation() {
//          activityBinding.selectService.text.addTextChangedListener(object : TextWatcher {
//              override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                  TODO("Not yet implemented")
//              }
//
//              override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                  TODO("Not yet implemented")
//              }
//
//              override fun afterTextChanged(s: Editable?) {
//                  TODO("Not yet implemented")
//              }
//          })
//      }
    private fun spinnerSetUp() {

        activityBinding.selectService.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    productCode = serviceTypeList[position].value
                    bookingAdapter?.serviceTypeChanged()
                    when (productCode) {
                        //AIR
                        "A" -> run {
//                           bookingAdapter?.calculateTotalVWeight()
                        }
                        //SURFACE
                        "S" -> run {
//                           bookingAdapter?.calculateTotalVWeight()
                        }
                        //SURFACEXPRESS
                        "SE" -> run {
//                            bookingAdapter?.calculateTotalVWeight()
                        }
                        //TRAIN
                        "T" -> run {
//                            bookingAdapter?.calculateTotalVWeight()
                        }
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
//                    TODO("Not yet implemented")
                }
            }


//        activityBinding.selectedPckgsType.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(
//                    parent: AdapterView<*>?,
//                    view: View?,
//                    position: Int,
//                    id: Long
//                ) {
//                    when(position){
//                        1-> run{
//
//                        }
//                    }
//                }
//
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//                    TODO("Not yet implemented")
//                }
//            }

        activityBinding.selectedPickupType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    pickupByValue = pickupByTypeList[position].value
                    when (pickupByValue) {

                        //JEENA STRAFF
                        "S" -> run {
                            activityBinding.inputLayoutPickBoy.visibility = View.VISIBLE
                            activityBinding.inputLayoutAgent.visibility = View.GONE
                            activityBinding.inputLayoutVehicle.visibility = View.GONE
                            activityBinding.inputLayoutVehicleVendor.visibility = View.GONE

//                            activityBinding.inputPickupBoy.text?.clear()
//                            activityBinding.inputAgent.text?.clear()
                            activityBinding.inputVehicle.text?.clear()
                            activityBinding.inputVehicleVendor.text?.clear()


                        }
//                        //AGENT
//                        "V"-> run{
//                            activityBinding.inputLayoutPickBoy.visibility=View.GONE
//                            activityBinding.inputLayoutAgent.visibility=View.VISIBLE
//                            activityBinding.inputLayoutVehicleVendor.visibility=View.GONE
//                            activityBinding.inputLayoutVehicle.visibility=View.GONE
//
//                            activityBinding.inputPickupBoy.text?.clear()
//                            activityBinding.inputAgent.text?.clear()
//                            activityBinding.inputVehicle.text?.clear()
//                            activityBinding.inputVehicleVendor.text?.clear()
//                        }
//                        //MARKET VEHICLE
                        "M" -> run {
                            activityBinding.inputLayoutPickBoy.visibility = View.VISIBLE
                            activityBinding.inputLayoutVehicle.visibility = View.VISIBLE
                            activityBinding.inputLayoutVehicleVendor.visibility = View.VISIBLE
                            activityBinding.inputLayoutAgent.visibility = View.GONE

//                            activityBinding.inputPickupBoy.text?.clear()
//                            activityBinding.inputAgent.text?.clear()
                            activityBinding.inputVehicle.text?.clear()
                            activityBinding.inputVehicleVendor.text?.clear()

                        }
//                        //OFFICE/AIRPORT DROP
                        "O" -> run {
                            activityBinding.inputLayoutAgent.visibility = View.GONE
                            activityBinding.inputLayoutPickBoy.visibility = View.VISIBLE
                            activityBinding.inputLayoutVehicle.visibility = View.GONE
                            activityBinding.inputLayoutVehicleVendor.visibility = View.GONE

//                            activityBinding.inputPickupBoy.text?.clear()
//                            activityBinding.inputAgent.text?.clear()
                            activityBinding.inputVehicle.text?.clear()
                            activityBinding.inputVehicleVendor.text?.clear()
                        }
//                        //CANCEL
//                        "N"-> run{
//                            activityBinding.inputLayoutAgent.visibility=View.GONE
//                            activityBinding.inputLayoutVehicle.visibility=View.GONE
//                            activityBinding.inputLayoutVehicleVendor.visibility=View.GONE
//                            activityBinding.inputLayoutPickBoy.visibility=View.VISIBLE
//
//                            activityBinding.inputPickupBoy.text?.clear()
//                            activityBinding.inputAgent.text?.clear()
//                            activityBinding.inputVehicle.text?.clear()
//                            activityBinding.inputVehicleVendor.text?.clear()
//
//
//                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
//                    TODO("Not yet implemented")
                }
            }


    }

    private fun setOnClick() {
        activityBinding.attachedImageLayout.setOnClickListener {
//            Toast.makeText(mContext, "Attached file is clicked", Toast.LENGTH_SHORT).show()
            showImageBottomSheet();
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
        activityBinding.inputCustName.setOnClickListener {
            if (DEBUGGING) {
                //            Toast.makeText(this, "testing", Toast.LENGTH_SHORT).show()
            }
//            getCustomerList()
        }

        activityBinding.inputCngrName.setOnClickListener {
//            getCngrList()
        }

        activityBinding.inputCngeName.setOnClickListener {
//            getCngeList()
        }
        activityBinding.inputDepartment.setOnClickListener {
//            getDeptList()
        }
        activityBinding.inputOrigin.setOnClickListener {
//            getOriginList()
        }
        activityBinding.inputDestination.setOnClickListener {
//            getDestinationList()
        }

        activityBinding.inputPickupBoy.setOnClickListener {
//            getPickupBoyList()


        }
        activityBinding.inputAgent.setOnClickListener {
            getAgentLov()
        }
        activityBinding.inputVehicleVendor.setOnClickListener {
            getVehicleVendor()
        }
        activityBinding.inputVehicle.setOnClickListener {
            getVehicleLov()
        }


        activityBinding.inputDate.setOnClickListener {
//            successToast(mContext, "datePicker")
//            openDatePicker()
//            openSingleDatePicker()
//            openBookingDatePicker()
        }

        activityBinding.inputTime.setOnClickListener {
//            successToast(mContext, "timePicker")
            openTimePicker()
        }

        activityBinding.chekActualAWB.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                awbCheck = "Y"
            } else {
                awbCheck = "N"
            }

        }

        activityBinding.btnEwayBill.setOnClickListener {
            openEwayBillBottomSheet(viewModel)
        }
        activityBinding.btnSave.setOnClickListener {
            checkFieldsBeforeSave()
        }
        activityBinding.btnAddMore.setOnClickListener {
            if (singleRefList.isNotEmpty()) {
                addMoreGridItem(singleRefList[0])
            }
        }
    }
//    private fun refreshData(){
//
//    }

    private fun setObservers() {
        mScanner.observe(this) { boxNo ->
            boxNoValidate(boxNo)
        }
        imageClicked.observe(this) { clicked ->
            if (clicked) {
                setBottomSheetRecyclerView()
            }
        }
        timePeriod.observe(this) { time ->
            activityBinding.inputTime.setText(time)

        }
        viewModel.boxNoValidateLiveData.observe(this) {
            if (it.commandstatus == 1) {
                bookingAdapter?.enterBoxOnNextAvailable(it.boxno.toString())
            }
        }
        activityBinding.refreshLayout.setOnRefreshListener {
//            refreshData()
            lifecycleScope.launch {
                delay(1500)
                activityBinding.refreshLayout.isRefreshing = false
            }
        }
        viewModel.isError.observe(this) { errMsg ->
            errorToast(errMsg)
        }
        mPeriod.observe(this) { datePicker ->
            activityBinding.inputDate.setText(datePicker.viewsingleDate)
            sqlDate = datePicker.sqlsingleDate.toString()
        }

        timePeriod.observe(this) { timePicker ->

//            activityBinding.inputTime.setText(timePicker.viewSingleTime)
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
        viewModel.viewDialogLiveData.observe(this, Observer { show ->
//            progressBar.visibility = if(show) View.VISIBLE else View.GONE
//            Utils.logger("viewDialog", show.toString())
            if (show) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })

        viewModel.custLiveData.observe(this) { custData ->
            customerList = custData
            openCustSelectionBottomSheet(customerList)
        }

        viewModel.cngrLiveData.observe(this) { cngrData ->
            cngrList = cngrData
            openCngrSelectionBottomSheet(cngrList)
        }

        viewModel.cngrLiveData.observe(this) { cngeData ->
            cngeList = cngeData
            openCngeSelectionBottomSheet(cngeList)
        }

        viewModel.deptLiveData.observe(this) { deptData ->
            deptList = deptData
            openDeptSelectionBottomSheet(deptList)
        }
        viewModel.originLiveData.observe(this) { originData ->
            originList = originData
            openOriginSelectionBottomSheet(originList)
        }

        viewModel.destinationLiveData.observe(this) { destData ->
            destinationList = destData
            openDestinationSelectionBottomSheet(destinationList)
        }

        viewModel.pickupBoyLiveData.observe(this) { boyData ->
            pickupBoylist = boyData
            openPickupBoySelectionBottomSheet(pickupBoylist)
        }

        viewModel.tempLiveData.observe(this) { tempData ->
            temperatureList = tempData

        }
        viewModel.contentLiveData.observe(this) { itemData ->
            contentList = itemData
        }
        viewModel.agentLiveData.observe(this) { agent ->
            agentList = agent

//            openAgentSelectionBottomSheet(agentList)
        }
        viewModel.vendorLiveData.observe(this) { vendor ->
            vehicleVendorList = vendor
            openVehicleVendorSelectionBottomSheet(vehicleVendorList)
        }
        viewModel.vehicleLiveData.observe(this) { vehicle ->
            vehicleList = vehicle
            openVehicleSelectionBottomSheet(vehicleList)
        }
        viewModel.packingLiveData.observe(this) { packing ->
            packingList = packing
        }
        viewModel.gelPackItemLiveData.observe(this) { gelPack ->
            gelPackList = gelPack
        }

        viewModel.singleRefLiveData.observe(this) { data ->
            if (data.isNotEmpty()) {
                if (data[0].packagetype.toString() == JEENA_PACKING) {
                    activityBinding.boxNoTxt.visibility = View.VISIBLE
                    activityBinding.btnAddMore.visibility = View.GONE
                } else {
                    activityBinding.boxNoTxt.visibility = View.GONE
                    activityBinding.btnAddMore.visibility = View.VISIBLE
                }

            }
            singleRefList = data
            setReferenceData()
            setupRecyclerView()
        }
        viewModel.pickupByLiveData.observe(this) { pickup ->
            pickupByTypeList = pickup
            val pickupAdapter =
                ArrayAdapter(this, android.R.layout.simple_list_item_1, pickupByTypeList)
            activityBinding.selectedPickupType.adapter = pickupAdapter
        }

        viewModel.ServiceTypeLiveData.observe(this) { service ->
            serviceTypeList = service
            val serviceAdapter =
                ArrayAdapter(this, android.R.layout.simple_list_item_1, serviceTypeList)
            activityBinding.selectService.adapter = serviceAdapter

        }
        viewModel.pckgLiveData.observe(this) { pckgType ->
            pckgTypeList = pckgType
            val pckgsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, pckgTypeList)
//          activityBinding.selectedPckgsType.adapter = pckgsAdapter


        }
    }

    private fun getPckgTypeLov() {
        viewModel.getPckgType(
            loginDataModel?.companyid.toString(),
            "gtapp_getpackagetype",
            listOf(),
            arrayListOf()
        )
    }

    private fun getCustomerList() {
        viewModel.getCustomerList(
            loginDataModel?.companyid.toString(),
            "greentrans_showcustomerLOV",
            listOf("prmbranchcode"),
            arrayListOf(userDataModel?.loginbranchcode.toString())
        )
    }

    private fun openCustSelectionBottomSheet(rvList: ArrayList<CustomerSelectionModel>) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].custname, rvList[i]))

        }
        openCommonBottomSheet(this, "Customer Selection", this, commonList)

    }

    private fun getCngrList() {
        viewModel.getCngrList(
            loginDataModel?.companyid.toString(),
            "greentrans_showconsignorconsignee",
            listOf("prmbranchcode", "prmcustcode", "prmcngrcnge", "prmcustwise"),
            arrayListOf(
                userDataModel?.loginbranchcode.toString(),
                userDataModel?.custcode.toString(),
                "R",
                ""
            )
        )
    }

    private fun openCngrSelectionBottomSheet(rvList: ArrayList<ConsignorSelectionModel>) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].name, rvList[i]))

        }
        openCommonBottomSheet(this, "Consignor Selection", this, commonList)

    }

    private fun getCngeList() {
        viewModel.getCngrList(
            loginDataModel?.companyid.toString(),
            "greentrans_showconsignorconsignee",
            listOf("prmbranchcode", "prmcustcode", "prmcngrcnge", "prmcustwise"),
            arrayListOf(
                userDataModel?.loginbranchcode.toString(),
                userDataModel?.custcode.toString(),
                "E",
                ""
            )
        )
    }

    private fun openCngeSelectionBottomSheet(rvList: ArrayList<ConsignorSelectionModel>) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].name, rvList[i]))

        }
        openCommonBottomSheet(this, "Consignee Selection", this, commonList)

    }

    private fun getDeptList() {
        viewModel.getDeptList(
            loginDataModel?.companyid.toString(),
            "showcustomerdepartment",
            listOf("prmcustcode", "prmorgcode"),
            arrayListOf(
                userDataModel?.custcode.toString(),
                userDataModel?.loginbranchcode.toString()
            )
        )
    }

    private fun openDeptSelectionBottomSheet(rvList: ArrayList<DepartmentSelectionModel>) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].custdeptname, rvList[i]))

        }
        openCommonBottomSheet(this, "Department Selection", this, commonList)
    }

    private fun getOriginList() {
        viewModel.getOriginList(
            loginDataModel?.companyid.toString(),
            "greentrans_branchLOVforBooking",
            listOf("prmloginbranchcode"),
            arrayListOf(userDataModel?.loginbranchcode.toString())
        )
    }

    private fun openOriginSelectionBottomSheet(rvList: ArrayList<OriginSelectionModel>) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].stnname, rvList[i]))

        }
        openCommonBottomSheet(this, "origin Selection", this, commonList)
    }

    private fun getDestinationList() {
        viewModel.getDestinationList(
            loginDataModel?.companyid.toString(),
            "greentrans_destinationLOVforBooking",
            listOf(),
            arrayListOf()
        )
    }

    private fun openDestinationSelectionBottomSheet(rvList: ArrayList<DestinationSelectionModel>) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].stnname.toString(), rvList[i]))

        }
        openCommonBottomSheet(this, "Destination Selection", this, commonList)
    }

    private fun getAgentLov() {
        viewModel.getAgentLov(
            loginDataModel?.companyid.toString(),
            "greentrans_vendlist",
            listOf("prmvendorcategory", "prmmenucode"),
            arrayListOf("PUD VENDOR", menuModel?.menucode.toString())
        )
    }

    private fun openAgentSelectionBottomSheet(rvList: ArrayList<AgentSelectionModel>) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].vendname, rvList[i]))

        }
        openCommonBottomSheet(this, "Agent Selection", this, commonList)
    }

    private fun getVehicleLov() {
        viewModel.getVehicleLov(
            loginDataModel?.companyid.toString(),
            "greentrans_showmode",
            listOf(
                "prmorgcode",
                "prmdestcode",
                "prmmodetype",
                "prmmodegroupcode",
                "prmvendcode",
                "prmownervendcode"
            ),
            arrayListOf("", "", "S", "S", "", "")  // need to modify
        )
    }

    private fun openVehicleSelectionBottomSheet(rvList: ArrayList<BookingVehicleSelectionModel>) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].regno, rvList[i]))

        }
        openCommonBottomSheet(this, "Vehicle Selection", this, commonList)
    }

    private fun getVehicleVendor() {
        viewModel.getVendorLov(
            loginDataModel?.companyid.toString(),
            "greentrans_vendlist",
            listOf("prmvendorcategory", "prmmenucode"),
            arrayListOf("VEHICLE VENDOR", menuModel?.menucode.toString())
        )
    }

    private fun openVehicleVendorSelectionBottomSheet(rvList: ArrayList<AgentSelectionModel>) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].vendname, rvList[i]))

        }
        openCommonBottomSheet(this, "Vendor Selection", this, commonList)
    }


    private fun getPickupBoyList() {
        viewModel.getPickupBoyList(
            loginDataModel?.companyid.toString(),
            "greentrans_getpickupdeliveryboy",
            listOf("prmbranchcode", "prmboytype"),
            arrayListOf(userDataModel?.loginbranchcode.toString(), "P")
        )
    }

    private fun openPickupBoySelectionBottomSheet(rvList: ArrayList<PickupBoySelectionModel>) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].name, rvList[i]))

        }
        openCommonBottomSheet(this, "Pickup Boy Selection", this, commonList)
    }

    private fun getTempLov() {
        viewModel.getTemperatureList(
            loginDataModel?.companyid.toString(),
            "gettemparature_lov",
            listOf(),
            arrayListOf()
        )
    }

    private fun openTemperatureSelectionBottomSheet(
        rvList: ArrayList<TemperatureSelectionModel>,
        index: Int
    ) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].name, rvList[i]))

        }
        openCommonBottomSheet(this, "Temperature Selection", this, commonList, true, index)
    }

    private fun getContentList() {
        viewModel.getContentLov(
            loginDataModel?.companyid.toString(),
            "greentrans_showgoodsLOV",
            listOf(),
            arrayListOf()
        )
    }

    private fun openContentSelectionBottomSheet(
        rvList: ArrayList<ContentSelectionModel>,
        index: Int
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
        rvList: ArrayList<PackingSelectionModel>,
        index: Int
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
        rvList: ArrayList<GelPackItemSelectionModel>,
        index: Int
    ) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].itemname, rvList[i]))

        }
        openCommonBottomSheet(this, "Gel Pack Selection", this, commonList, true, index)

    }


//    private fun loadBookingList() {
//        bookingList = listOf(
//            BookingRecyclerModel("1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
//        )
//    }


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
                sqlDate!!,
                "",
                boxNo
            )
        )
    }

    private fun setupRecyclerView() {
        if (bookingAdapter == null) {
            linearLayoutManager = object : LinearLayoutManager(this) {
                override fun onLayoutCompleted(state: RecyclerView.State?) {
                    super.onLayoutCompleted(state)
                    isFirstTime = false;
                }
            }
            activityBinding.recyclerView.layoutManager = linearLayoutManager
        }
        bookingAdapter = BookingAdapter(this, productCode, this, singleRefList, this)
        activityBinding.recyclerView.adapter = bookingAdapter
    }

//    private  fun onChangeRecyclerview(){
//        if(activityBinding.inputPckgsNo.text!!.isNotEmpty()){
//            addListLiveData.observe(this){addList
////                successToast("data modified")
//            }
//            addList=singlePickupDataList
//            addList.plusAssign(dataList)
//            activityBinding.recyclerView.adapter!!.notifyItemChanged(1)
//        }
//    }

    override fun onItemClick(data: Any, clickType: String) {

        if (clickType == "Customer Selection") {
            val selectedCustomer = data as CustomerSelectionModel
            activityBinding.inputCustName.setText(selectedCustomer.custname)
            custCode = selectedCustomer.custcode

        } else if (clickType == "Consignor Selection") {
            val selectedCngr = data as ConsignorSelectionModel
            activityBinding.inputCngrName.setText(selectedCngr.name)
//            activityBinding.inputCngrAddress.setText(selectedCngr.address)
            cngrCity = selectedCngr.city
            cngrZipCode = selectedCngr.zipcode
            cngrState = selectedCngr.state
            cngrMobileNo = selectedCngr.secondarymobileno.toString()
            cngreMailId = selectedCngr.email.toString()
            cngrCSTNo = selectedCngr.cstno.toString()
            cngrLSTNo = selectedCngr.lstno.toString()
            cngrTINNo = selectedCngr.tinno.toString()
//            cngrCode=selectedCngr.code.toString()
            cngrGstNo = selectedCngr.gstno.toString()

        } else if (clickType == "Consignee Selection") {
            val selectedCnge = data as ConsignorSelectionModel
            activityBinding.inputCngeName.setText(selectedCnge.name)
//            activityBinding.inputCngeAddress.setText(selectedCnge.address)
            cngeCity = selectedCnge.city.toString()
            cngeZipCode = selectedCnge.zipcode.toString()
            cngeState = selectedCnge.state.toString()
            cngeMobileNo = selectedCnge.secondarymobileno.toString()
            cngeeMailId = selectedCnge.email.toString()
            cngeCSTNo = selectedCnge.cstno.toString()
            cngeLSTNo = selectedCnge.lstno.toString()
            cngeTINNo = selectedCnge.tinno.toString()
//            cngeCode=selectedCnge.code.toString()
            cngeGstNo = selectedCnge.gstno.toString()
//            cngeSTaxRegNo =selectedCnge..toString()
        } else if (clickType == "Department Selection") {
            val selectedDept = data as DepartmentSelectionModel
            activityBinding.inputDepartment.setText(selectedDept.custdeptname)
            custDeptId = selectedDept.custdeptid.toString()


        } else if (clickType == "origin Selection") {
            val selectedOrigin = data as OriginSelectionModel
            activityBinding.inputOrigin.setText(selectedOrigin.stnname)


        } else if (clickType == "Destination Selection") {
            val selectedDestination = data as DestinationSelectionModel
            activityBinding.inputDestination.setText(selectedDestination.stnname)
            destCode = selectedDestination.stncode.toString()

        } else if (clickType == "Pickup Boy Selection") {
            val selectedPickupBoy = data as PickupBoySelectionModel
            activityBinding.inputPickupBoy.setText(selectedPickupBoy.name)
            boyId = selectedPickupBoy.boyid.toString()
        } else if (clickType == "Agent Selection") {
            val selectedAgent = data as AgentSelectionModel
            activityBinding.inputAgent.setText(selectedAgent.vendname)

        } else if (clickType == "Vehicle Selection") {
            val selectedVehicle = data as BookingVehicleSelectionModel
            activityBinding.inputVehicle.setText(selectedVehicle.regno)
            vehicleCode = selectedVehicle.vehiclecode

        } else if (clickType == "Vendor Selection") {
            val selectedVendor = data as AgentSelectionModel
            activityBinding.inputVehicleVendor.setText(selectedVendor.vendname)
            vendorCode = selectedVendor.vendcode.toString()

        }
    }

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


    override fun onClick(data: Any, clickType: String) {

    }
    //    fun getCurrentServiceType(serviceType:String,vWeight: Float,aWeight: Int,cWeight: Int){
//        activityBinding.inputVolWeight.setText(vWeight.toString())
//        activityBinding.inputAWeight.setText(aWeight.toString())
//        activityBinding.inputCWeight.setText(cWeight.toString())
//    }


    fun setVWeight(vWeight: Float) {
        val localVWeight = round(vWeight)
        activityBinding.inputVolWeight.setText(localVWeight.toString())
    }

    fun setAWeight(aWeight: Int) {
        activityBinding.inputAWeight.setText(aWeight.toString())
    }

    fun setCWeight(cWeight: Int) {
        activityBinding.inputCWeight.setText(cWeight.toString())
        val aWeight = activityBinding.inputAWeight.text.toString().toDoubleOrNull()
        val cWeight = activityBinding.inputCWeight.text.toString().toDoubleOrNull()
        var vWeight = activityBinding.inputVolWeight.text.toString().toDoubleOrNull()
        if (aWeight != null) {
            if (vWeight == null) vWeight = 0.0
            val higherValue = Math.max(aWeight.toInt() ?: 0, vWeight.toInt() ?: 0)
            activityBinding.inputCWeight.setText(higherValue.toString())
        }
//        if(isFirstTime) {
//            activityBinding.inputCWeight.setText(activityBinding.inputAWeight.text.toString())
//        }
    }
//    private  fun calculateVWeight(index: Int, layoutBinding: BookingItemViewBinding){
//        if(singleRefList[index].pckglength.toString().isNotEmpty() && singleRefList[index].pckgbreath.toString().isNotEmpty()&& singleRefList[index].pckgheight.toString().isNotEmpty() ){
//           if(productCode =="A"){
//               singleRefList[index].volfactor= (singleRefList[index].pckglength * singleRefList[index].pckgbreath * singleRefList[index].pckgheight).toFloat()/6000
//           }else{
////               singleRefList[index].volfactor= (singleRefList[index].pckglength * singleRefList[index].pckgbreath * singleRefList[index].pckgheight).toFloat()/5000
////               singleRefList[index].volfactor= (.pckglength * singleRefList[index].pckgbreath * singleRefList[index].pckgheight).toFloat()/5000
//           }
//
//        }else if(singleRefList[index].volfactor.isNaN()){
//            singleRefList[index].volfactor = 0f
//
//        }else{
//            singleRefList[index].volfactor = Math.ceil(singleRefList[index].volfactor.toFloat() * singleRefList[index].pcs.toDouble()).toFloat()
//        }
//        calculateTotalVWeight()
//    }


//    private  fun calculateTotalVWeight(){
//        val  num=0
//        actualVWeight=num.toString()
//        singleRefList.forEachIndexed { _, element ->
//            actualVWeight=volWeight+element.volfactor.toFloat()
//        }
//        calculateChargeableWeight()
//
//    }

//     private  fun calculateTotalAWeight(){
////              val num=0
////              actualAWeight=num.toString()
//              singleRefList.forEachIndexed { _, element ->
//                   actualAWeight=aWeight.toFloat()+element.aweight.toFloat()
//              }
//              if(actualAWeight.isNaN()){
//                  actualAWeight=0f
//              }
//             calculateChargeableWeight()
//
//     }
//     private fun calculateChargeableWeight(){
//
//         if((actualAWeight.toString() != "") && volWeight!!.isNotEmpty()){
//             if(volWeight.toFloat() > aWeight.toFloat()){
//              activityBinding.inputCWeight.setText(volWeight)
//             } else if(volWeight.toFloat() <= actualAWeight.toFloat()){
//                 activityBinding.inputCWeight.setText(actualAWeight.toString())
//             }
//         }
//     }

    private fun checkFieldsBeforeSave() {
        if (activityBinding.inputDate.text.isNullOrEmpty()) {
            activityBinding.inputDate.error = "Please Select Date"
            errorToast("Please Select Date")

            return
        } else if (activityBinding.inputTime.text.isNullOrEmpty()) {
            errorToast("Please Select Date")
            return
        } else if (activityBinding.inputCustName.text.isNullOrEmpty()) {
            errorToast("Please Select Customer")
            return
        } else if (activityBinding.inputCngrName.text.isNullOrEmpty()) {
            errorToast("Please Select Consignor Name")
            return
//        }else if (activityBinding.inputCngrAddress.text.isNullOrEmpty()){
//            errorToast("Please Select Consignor Address")
//            return
        } else if (activityBinding.inputCngeName.text.isNullOrEmpty()) {
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
        } else if (activityBinding.inputAWeight.text.isNullOrEmpty()) {
            errorToast("Please Enter Volumetric Weight.")
            return
        } else if (activityBinding.inputCWeight.text.isNullOrEmpty()) {
            errorToast("Please Enter Chargeable Weight.")
            return
        }

        if (pickupByValue == "V") {
            if (activityBinding.inputAgent.text.isNullOrEmpty()) {
                errorToast("Please Select Agent")
                return
            }
        } else if (pickupByValue == "S") {
            if (activityBinding.inputPickupBoy.text.isNullOrEmpty()) {
                errorToast("Please Select Pickup Boy")
                return
            }
        } else if (pickupByValue == "M") {
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
        } else if (pickupByValue == "O") {
            if (activityBinding.inputPickupBoy.text.isNullOrEmpty()) {
                errorToast("Please Select Pickup Boy")
                return
            }
        } else if (pickupByValue == "N") {
            if (activityBinding.inputPickupBoy.text.isNullOrEmpty()) {
                errorToast("Please Select Pickup Boy")
                return
            }
        }
        if (!activityBinding.autoGrCheck.isChecked && activityBinding.inputGrNo.text.isNullOrBlank()) {
            errorToast("Please Enter GR Number")
            return
        }


        AlertDialog.Builder(this)
            .setTitle("Alert!!!")
            .setMessage("Are you sure you want to save this booking?")
            .setPositiveButton("Yes") { _, _ ->
                saveBooking()
            }
            .setNeutralButton("No") { _, _ -> }
            .show()

    }

    private fun setBottomSheetRecyclerView() {
        if (imageBase64List.isEmpty() && imageBitmapList.isEmpty()) {
            imageBase64List.add("EMPTY")
            imageBitmapList.add(
                BitmapFactory.decodeResource(
                    mContext.resources,
                    R.drawable.baseline_add_a_photo_24
                )
            )
        }
        bottomSheetAdapter =
            AttachedImagesBottomSheet(
                mContext,
                imageBase64List,
                imageBitmapList,
                imageUriList,
                this
            )
        //        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        val gridLayoutManager = GridLayoutManager(mContext, 3)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        rvImages.layoutManager = gridLayoutManager
        rvImages.adapter = bottomSheetAdapter
        rvImages.itemAnimator = DefaultItemAnimator()
        rvImages.isNestedScrollingEnabled = false
    }

    private fun setBottomSheetRV() {
//        if(imageBase64List.isEmpty() && imageBitmapList.isEmpty()) {
//            imageBase64List.add("EMPTY")
//            imageBitmapList.add(BitmapFactory.decodeResource(mContext.resources,
//                R.drawable.baseline_add_a_photo_24))
//        }
        bottomSheetAdapter =
            AttachedImagesBottomSheet(
                mContext,
                imageBase64List,
                imageBitmapList,
                imageUriList,
                this
            )
        //        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        val gridLayoutManager = GridLayoutManager(mContext, 3)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        rvImages.layoutManager = gridLayoutManager
        rvImages.adapter = bottomSheetAdapter
        rvImages.itemAnimator = DefaultItemAnimator()
        rvImages.isNestedScrollingEnabled = false
    }

//    private fun updateImageToRV(data: Bitmap) {
//        imagesBase64List.remove("EMPTY")
//        imagesBitmapList.add(data)
//        //        imagesBase64List.add(Common.encodeImage(data));
//        imagesBase64List.add(ImageUtil.convert(data))
//        imagesBase64List.add("EMPTY")
//        setBottomSheetRecyclerView()
//    }

    private fun showBottomSheet() {
        val header: TextView?
        val saveYes: Button?
        val saveNo: Button?
//        imagesBase64List.clear()
//        imagesBitmapList.clear()
//        imageURIList.clear()
//        imagesBase64List.add("EMPTY")
        bottomSheetDialog = BottomSheetDialog(mContext)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_pallet_wise_remarks)
        bottomSheetDialog.dismissWithAnimation = true
        header = bottomSheetDialog.findViewById<TextView>(R.id.HEADER)
        rvImages = bottomSheetDialog.findViewById<RecyclerView>(R.id.rv_put_away_closing_images)!!
        val closeRemarks: TextInputEditText? =
            bottomSheetDialog.findViewById<View>(R.id.inputSticker) as TextInputEditText?
        saveYes = bottomSheetDialog.findViewById<Button>(R.id.yes_close)
        saveNo = bottomSheetDialog.findViewById<Button>(R.id.no_close)
        val headerText = "PALLET REMARKS "
        header!!.text = headerText
//        bottomSheetAdapter =
//            AttachedImageBottomSheet(mContext, imagesBase64List, imagesBitmapList, this)

//        setBottomSheetRecyclerView()
        saveNo!!.setOnClickListener { view: View? -> bottomSheetDialog.dismiss() }
        saveYes!!.setOnClickListener { view: View? ->
//            successToast("Yes Clicked");
            if (closeRemarks!!.text.toString() == "" && imageBase64List.size == 1) {
                errorToast("Please Enter Remarks or Click Pictures")
            } else {
                palletWiseRemarks = closeRemarks.text.toString()
                bottomSheetDialog.dismiss()
            }
        }
        setBottomSheetRecyclerView()
        bottomSheetDialog.show()
    }


    private fun showImageBottomSheet() {
        val header: TextView?
        val done: Button?
        var addImage: ExtendedFloatingActionButton?
        bottomSheetDialog = BottomSheetDialog(mContext)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_add_images)
        bottomSheetDialog.dismissWithAnimation = true
        header = bottomSheetDialog.findViewById<TextView>(R.id.HEADER)
        rvImages = bottomSheetDialog.findViewById<RecyclerView>(R.id.rv_put_away_closing_images)!!
        done = bottomSheetDialog.findViewById<Button>(R.id.done_btn)
        addImage = bottomSheetDialog.findViewById<ExtendedFloatingActionButton>(R.id.add_image)
        done?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        addImage?.setOnClickListener {
            showImageDialog()
        }
        setBottomSheetRV()
        bottomSheetDialog.show()
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


        for (i in 0 until singleRefList.size) {
            totalPackage += singleRefList[i].pcs
            val refNo = singleRefList[i].detailreferenceno
            val weightStr = adapterEnteredData?.get(i)?.weight ?: singleRefList[i].weight ?: ""
            val pckgType = singleRefList[i].packagetype ?: ""
            val tempStr =
                if (adapterEnteredData == null) singleRefList[i].tempurature else adapterEnteredData[i].tempurature
            val packing = singleRefList[i].packing
            val goodsStr = singleRefList[i].contents
            val dryIceStr = singleRefList[i].dryice
            val dryIceQtyStr = adapterEnteredData?.get(i)?.dryiceqty ?: singleRefList[i].dryiceqty
            val dataLoggerStr =
                adapterEnteredData?.get(i)?.datalogger ?: singleRefList[i].datalogger
            var dataLoggerNoStr = ""


            if (dataLoggerStr == "Y") {
                dataLoggerNoStr =
                    adapterEnteredData?.get(i)?.dataloggerno ?: singleRefList[i].dataloggerno ?: ""
            }

            val gelPackItemCodeStr =
                adapterEnteredData?.get(i)?.gelpackitemcode ?: singleRefList[i].gelpackitemcode
                ?: ""
//            val dimHeight=singleRefList[i].localHeight
//            val dimBreath=singleRefList[i].localBreath
//            val dimLength=singleRefList[i].localLength
            val dimHeight =
                if (adapterEnteredData == null) singleRefList[i].localHeight else adapterEnteredData[i].localHeight
            val dimBreath =
                if (adapterEnteredData == null) singleRefList[i].localBreath else adapterEnteredData[i].localBreath
            val dimLength =
                if (adapterEnteredData == null) singleRefList[i].localLength else adapterEnteredData[i].localLength
            val boxNoStr = adapterEnteredData?.get(i)?.boxno ?: singleRefList[i].boxno ?: ""
            val gelPackStr = singleRefList[i].gelpack
            val gelPackQtyStr =
                adapterEnteredData?.get(i)?.gelpackqty ?: singleRefList[i].gelpackqty
            val packageStr =
                if (adapterEnteredData == null) singleRefList[i].pcs else adapterEnteredData[i].pcs
            val vWeightStr = singleRefList[i].volfactor

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
        if(totalPackage != activityBinding.inputPckgsNo.text.toString().toInt()){
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
        }
        Log.d("total pckgs",totalPackage.toString())

        if(ENV.DEBUGGING) {
             successToast(" at save")
            return
        }
        viewModel.saveBooking(
            companyId = userDataModel?.companyid.toString(),
            branchCode = userDataModel?.loginbranchcode.toString(),
            bookingDt = sqlDate!!,
//            bookingDt = activityBinding.inputDate.text.toString(),
            time = activityBinding.inputTime.text.toString(),
            grNo = activityBinding.inputGrNo.text.toString(),
            custCode = custCode,
            destCode = destCode,
            productCode = productCode,
            pckgs = activityBinding.inputPckgsNo.text.toString().toIntOrNull() ?: 0,
//            pckgs = actualPackageStr.toInt(),

            aWeight = activityBinding.inputAWeight.text.toString(),
            vWeight = activityBinding.inputVolWeight.text.toString(),
            cWeight = activityBinding.inputCWeight.text.toString(),
            createId = userDataModel?.usercode.toString(),
            sessionId = userDataModel?.sessionid.toString(),
            refNo = "",
            cngr = activityBinding.inputCngrName.text.toString(),
            cngrAddress = cngrAddress,
            cngrCity = cngrCity,
            cngrZipCode = cngrZipCode,
            cngrState = cngrState,
            cngrMobileNo = cngrMobileNo,
            cngreMailId = cngreMailId,
            cngrCSTNo = cngrCSTNo,
            cngrLSTNo = cngrLSTNo,
            cngrTINNo = cngrTINNo,
            cngrSTaxRegNo = "",
            cnge = activityBinding.inputCngeName.text.toString(),
            cngeAddress = cngeAddress,
            cngeCity = cngeCity,
            cngeZipCode = cngeZipCode,
            cngeState = cngeState,
            cngeMobileNo = cngeMobileNo,
            cngeeMailId = cngeeMailId,
            cngeCSTNo = cngeCSTNo,
            cngeLSTNo = cngeLSTNo,
            cngeTINNo = cngeTINNo,
            cngeSTaxRegNo = "",
            transactionId = transactionId,
            awbChargeApplicable = awbCheck,
            custDeptId = custDeptId,
            referenceNoStr = actualRefNoStr,
            weightStr = actualWeightStr,
            packageTypeStr = actualPackageTypeStr,
            tempuratureStr = actualTempuratureStr,
            packingStr = actualPackingStr,
            goodsStr = actualContent,
            dryIceStr = actualDryIceStr,
            dryIceQtyStr = actualDryIceQtyStr,
            dataLoggerStr = actualDataLoggerStr,
            dataLoggerNoStr = actualDataLoggerNoStr,
            dimLength = actualDimLength,
            dimBreath = actualDimBreath,
            dimHeight = actualDimHeight,
            pickupBoyName = activityBinding.inputPickupBoy.text.toString(),
            boyId = boyId,
            boxnoStr = actualBoxNo,
            stockItemCodeStr = pickupContentId,
            gelPackStr = actualGelPackStr,
            gelPackItemCodeStr = actualGelPackItemCodeStr,
            gelPackQtyStr = actualGelPackQtyStr,
//            menuCode = menuModel?.menucode.toString(),
            menuCode = menuCode,
            invoiceNoStr = invoiceStr,
            invoiceDtStr = invoiceDtStr,
            invoiceValueStr = invoiceValueStr,
            ewayBillNoStr = ewbNoStr,
            ewayBillDtStr = ewbDtStr,
            ewbValidupToDtStr = ewbValidUptoStr,
            vendorCode = vendorCode,
            packageStr = actualPackageStr,
            pickupBy = pickupByValue,
            vehicleNo = activityBinding.inputVehicle.text.toString(),
            vWeightStr = actualVWeightStr,
            vehicleCode = vehicleCode,
            cngrCode = cngrCode,
            cngeCode = cngeCode,
            remarks = activityBinding.inputRemark.text.toString(),
            cngrGstNo = cngrGstNo,
            cngeGstNo = cngeGstNo,
            images = imageBase64List
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        Utils.ewayBillValidated = false
        Utils.ewayBillDetailResponse = null
        Utils.enteredValidatedEwayBillList = null
        Utils.enteredEwayBillValidatedData.clear()
    }

    override fun addImage(clickType: String, imageBitmap: Bitmap) {
        if (clickType == "VIEW_IMAGE") {
//            viewImage(imageBitmap, imageBase64, imageUri)
            viewImage(imageBitmap)
//            for (i in 0..imageBase64List.size){
//                startActivity(Intent
//                (Intent.ACTION_VIEW, Uri.parse(imageBase64List.elementAt(i))))
//            }
//            viewImage()
//            startActivity(Intent
//                (Intent.ACTION_VIEW, Uri.parse()))
            /** replace with your own uri */

        }

    }

    private fun viewImage(imageBitmap: Bitmap) {
        Utils.imageBitmap = imageBitmap
//        val dialogFragment = ViewImage.newInstance(this, this, "View Image" ,imageBitmap, imageBase64, imageUri)
        val dialogFragment = ViewImage.newInstance(this, this, "View Image", imageBitmap)
        dialogFragment.show(supportFragmentManager, ViewImage.TAG)
    }

    private fun showGrCreatedAlert(model: SaveBookingModel) {
        AlertDialog.Builder(this)
            .setTitle("Success!!!")
            .setMessage(model.commandmessage)
            .setPositiveButton("Okay") { _, _ ->
                finish()
            }
            .show()
    }

    private fun openBookingIndentInfoBottomSheet(
        mContext: Context,
        title: String,
        bottomSheetClick: OnRowClick<Any>,
        withAdapter: Boolean = false,
        index: Int = -1
    ) {
//        showProgressDialog()
        val bottomSheetDialog = BookingIndentInfoBottomSheet.newInstance(
            mContext,
            title,
            transactionId,
            bottomSheetClick,
            withAdapter,
            index
        )
//        hideProgressDialog()
        bottomSheetDialog.show(supportFragmentManager, BookingIndentInfoBottomSheet.TAG)
    }

    private fun showPrintGrAlert(grNo: String) {
        CommonAlert.createAlert(
            context = this,
            header = "Alert!!",
            description = "Are you sure to print GR#: $grNo ?",
            callback = this,
            alertCallType = PRINT_GR_TAG,
            data = grNo
        )
    }

    override fun onAlertClick(alertClick: AlertClick, alertCallType: String, data: Any?) {
        when (alertClick) {
            AlertClick.YES -> run {
                when (alertCallType) {
                    PRINT_GR_TAG -> run {
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
                        Log.d(Companion::class.java.name, alertCallType)
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

    private fun openPrintGrBottomSheet(mContext: Context, title: String, grNo: String) {
        val bottomSheetDialog = PrintGrBottomSheet.newInstance(mContext, title, grNo)
        bottomSheetDialog.show(supportFragmentManager, PrintGrBottomSheet.TAG)
    }
}