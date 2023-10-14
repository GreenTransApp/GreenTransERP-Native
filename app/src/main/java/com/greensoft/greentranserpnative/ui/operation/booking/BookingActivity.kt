package com.greensoft.greentranserpnative.ui.operation.booking

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.ENV.Companion.DEBUGGING
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityBookingBinding
import com.greensoft.greentranserpnative.ui.operation.eway_bill.EwayBillBottomSheet
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.booking.models.ConsignorSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.ContentSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.CustomerSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.DepartmentSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.DestinationSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.OriginSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.PackingSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.PickupBoySelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.TemperatureSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.models.SinglePickupRefModel
import com.greensoft.greentranserpnative.ui.bottomsheet.common.models.CommonBottomSheetModel
import com.greensoft.greentranserpnative.ui.home.models.UserMenuModel
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.operation.booking.models.AgentSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.BookingVehicleSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.GelPackItemSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.PckgTypeSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.PickupBySelection
import com.greensoft.greentranserpnative.ui.operation.booking.models.ServiceTypeSelectionLov
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.models.PickupRefModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class BookingActivity @Inject constructor() : BaseActivity(), OnRowClick<Any>, BottomSheetClick<Any> {

    lateinit var activityBinding: ActivityBookingBinding
    lateinit var linearLayoutManager: LinearLayoutManager
    private var menuModel: UserMenuModel? = null
    private var title: String = "BOOKING"
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
//    var serviceSelectedItems = arrayOf("SELECT", "AIR", "SURFACE", "SURFACE EXPRESS", "TRAIN")
//    var pckgsTypeSelectedItems = arrayOf("SELECT", "JEENA PACKING", "PRE PACKED")

//    var pickupTypeSelectedItems =
//        arrayOf("JEENA STAFF", "AGENT", " MARKET VEHICLE", "OFFICE/AIRPORT DROP", "CANCEL")

    private val addListMuteLiveData = MutableLiveData<String>()
    val addListLiveData: LiveData<String>
        get() = addListMuteLiveData
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
    var cngrGstNo=""
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
    var cngeGstNo=""
    var cngeSTaxRegNo = ""
    var custDeptId = ""
    var boyId = ""
    var vendorCode = ""
    var pickupByValue = ""
    var vehicleCode = ""
    var selectedGelPackItenCode = ""
    var pickupContentId = ""
    var aWeight = ""
    var volWeight = ""
//    var actualAWeight = ""
    var actualAWeight:Float=0f
    var actualVWeight = ""


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
        activityBinding.inputTime.setText(getSqlCurrentTime())

        getIntentData()
        getSingleRefData()
        getServiceType()
        getPickupBy()
        getCustomerList()
        getCngrList()
        getCngeList()
        getDeptList()
        getOriginList()
        getDestinationList()
        getPickupBoyList()
        getTempLov()
        getpackingLov()
        getGelPackLov()
        getVehicleLov()
        getVehicleVendor()
        getAgentLov()
        getPckgTypeLov()
        getContentList()
        spinnerSetUp()
        setOnClick()

//        onChangeRecyclerview()

    }

    override fun onResume() {
        super.onResume()
//        refreshData()
    }

    //    override fun onResume() {
//        super.onResume()
//        receivedDataList.clear()
//        successToast(mContext,"resume")
//    }

    private fun getSingleRefData(){
        viewModel.getSingleRefData(
            loginDataModel?.companyid.toString(),
            "greentransweb_jeenabooking_getpickupforbooking",
            listOf("prmtransactionid"),
            arrayListOf(transactionId)
        )
    }
    private fun getServiceType(){
        viewModel.getServiceList(
            loginDataModel?.companyid.toString(),
            "gtapp_getproductmast",
            listOf("prmusercode","prmmenucode","prmsessionid"),
            arrayListOf(userDataModel?.usercode.toString(),menuModel?.menucode.toString(),userDataModel?.sessionid.toString())
        )
    }

    private fun getPickupBy(){
        viewModel.getPickupByLov(
            loginDataModel?.companyid.toString(),
            "gtapp_getpickupby",
            listOf("prmbranchcode","prmusercode","prmmenucode","prmsessionid"),
            arrayListOf(userDataModel?.loginbranchcode.toString(),userDataModel?.usercode.toString(),menuModel?.menucode.toString(),userDataModel?.sessionid.toString())
        )
    }

        private fun getIntentData() {
        if(intent != null) {
            val jsonString = intent.getStringExtra("ARRAY_JSON")
            if(jsonString != "") {
                    val gson = Gson()
                    val listType = object : TypeToken<List<PickupRefModel>>() {}.type
                    val resultList: ArrayList<PickupRefModel> =
                    gson.fromJson(jsonString.toString(), listType)
                    pickupRefData.addAll(resultList)
                    pickupRefData.forEachIndexed { _, element ->
                    transactionId=element.transactionid.toString()
                }
                }
        }
    }
    private fun openEwayBillBottomSheet(){
        val bottomSheet= EwayBillBottomSheet()
        bottomSheet.show(supportFragmentManager,"Eway-Bill-BottomSheet")
    }
    private fun checkNullOrEmpty(value: Any?): String {
        if(value == "" || value == null){
            return "NOT AVAILABLE"
        }
        return value.toString()
    }

    private fun setReferenceData(){
        singleRefList.forEachIndexed { _, element ->

            activityBinding.inputCustName.setText(checkNullOrEmpty(element.custname).toString())
            activityBinding.inputCngrName.setText(checkNullOrEmpty(element.cngr).toString())
            activityBinding.inputCngrAddress.setText(checkNullOrEmpty(element.cngraddress).toString())
            activityBinding.inputCngeName.setText(checkNullOrEmpty(element.cnge).toString())
            activityBinding.inputCngeAddress.setText(checkNullOrEmpty(element.cngeaddress).toString())
            activityBinding.inputDepartment.setText(checkNullOrEmpty(element.departmentname).toString())
            activityBinding.inputOrigin.setText(checkNullOrEmpty(element.Origin).toString())
            activityBinding.inputDestination.setText(checkNullOrEmpty(element.destname).toString())
            activityBinding.selectService.setAutofillHints(checkNullOrEmpty(element.servicetype).toString())
            activityBinding.selectedPckgsType.setAutofillHints(checkNullOrEmpty(element.packagetype).toString())
            activityBinding.inputPckgsNo.setText(checkNullOrEmpty(element.pckgs).toString())
            activityBinding.inputAWeight.setText(checkNullOrEmpty(element.aweight).toString())
            activityBinding.inputVolWeight.setText(checkNullOrEmpty(element.volfactor).toString())
            aWeight=activityBinding.inputAWeight.toString()
            volWeight=activityBinding.inputVolWeight.toString()
//            addListMuteLiveData.postValue(activityBinding.inputPckgsNo.text.toString())
            addListMuteLiveData.postValue(element.pckgs.toString())
//            activityBinding.selectedPickupType.setAutofillHints(checkNullOrEmpty(element.gelpacktype).toString())



        }
    }

    fun setLayoutVisibility(){
        activityBinding.inputLayoutPickBoy.visibility=View.VISIBLE
        activityBinding.inputLayoutAgent.visibility=View.GONE
        activityBinding.inputLayoutVehicle.visibility=View.GONE
        activityBinding.inputLayoutVehicleVendor.visibility=View.GONE
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
                      productCode=serviceTypeList[position].value
                    when(productCode){
                        //AIR
                       "A"-> run{

                       }
                        //SURFACE
                        "S"->run{
//                            errorToast(productCode)
                        }
                        //SURFACEXPRESS
                        "SE"->run{

                        }
                        //TRAIN
                        "T"->run{

                        }
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }




        activityBinding.selectedPckgsType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }

        activityBinding.selectedPickupType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    pickupByValue=pickupByTypeList[position].value
                    when(pickupByValue){
                        //JEENA STRAFF
                        "S"-> run{
                            activityBinding.inputLayoutPickBoy.visibility=View.VISIBLE
                            activityBinding.inputLayoutAgent.visibility=View.GONE
                            activityBinding.inputLayoutVehicle.visibility=View.GONE
                            activityBinding.inputLayoutVehicleVendor.visibility=View.GONE

                            activityBinding.inputPickupBoy.text?.clear()
                            activityBinding.inputAgent.text?.clear()
                            activityBinding.inputVehicle.text?.clear()
                            activityBinding.inputVehicleVendor.text?.clear()


                        }
                        //AGENT
                        "V"-> run{
                            activityBinding.inputLayoutPickBoy.visibility=View.GONE
                            activityBinding.inputLayoutAgent.visibility=View.VISIBLE
                            activityBinding.inputLayoutVehicleVendor.visibility=View.GONE
                            activityBinding.inputLayoutVehicle.visibility=View.GONE

                            activityBinding.inputPickupBoy.text?.clear()
                            activityBinding.inputAgent.text?.clear()
                            activityBinding.inputVehicle.text?.clear()
                            activityBinding.inputVehicleVendor.text?.clear()
                        }
                        //MARKET VEHICLE
                        "M"-> run{
                            activityBinding.inputLayoutPickBoy.visibility=View.VISIBLE
                            activityBinding.inputLayoutVehicle.visibility=View.VISIBLE
                            activityBinding.inputLayoutVehicleVendor.visibility=View.VISIBLE
                            activityBinding.inputLayoutAgent.visibility=View.GONE

                            activityBinding.inputPickupBoy.text?.clear()
                            activityBinding.inputAgent.text?.clear()
                            activityBinding.inputVehicle.text?.clear()
                            activityBinding.inputVehicleVendor.text?.clear()

                        }
                        //OFFICE/AIRPORT DROP
                        "O"-> run{
                            activityBinding.inputLayoutAgent.visibility=View.GONE
                            activityBinding.inputLayoutPickBoy.visibility=View.VISIBLE
                            activityBinding.inputLayoutVehicle.visibility=View.GONE
                            activityBinding.inputLayoutVehicleVendor.visibility=View.GONE

                            activityBinding.inputPickupBoy.text?.clear()
                            activityBinding.inputAgent.text?.clear()
                            activityBinding.inputVehicle.text?.clear()
                            activityBinding.inputVehicleVendor.text?.clear()
                        }
                        //CANCEL
                        "N"-> run{
                            activityBinding.inputLayoutAgent.visibility=View.GONE
                            activityBinding.inputLayoutVehicle.visibility=View.GONE
                            activityBinding.inputLayoutVehicleVendor.visibility=View.GONE
                            activityBinding.inputLayoutPickBoy.visibility=View.VISIBLE

                            activityBinding.inputPickupBoy.text?.clear()
                            activityBinding.inputAgent.text?.clear()
                            activityBinding.inputVehicle.text?.clear()
                            activityBinding.inputVehicleVendor.text?.clear()


                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }


    }

    private fun setOnClick() {
        activityBinding.autoGrCheck.setOnCheckedChangeListener { compoundButton, bool ->
            if(bool) {
                activityBinding.inputGrNo.setText("")

            }
            activityBinding.inputGrNo.isEnabled = !(bool)
        }
        activityBinding.inputCustName.setOnClickListener {
            if (DEBUGGING) {
                //            Toast.makeText(this, "testing", Toast.LENGTH_SHORT).show()
            }
//            openCustSelectionBottomSheet(customerList)
        }

        activityBinding.inputCngrName.setOnClickListener {
//            openCngrSelectionBottomSheet(cngrList)
        }

        activityBinding.inputCngeName.setOnClickListener {
//            openCngeSelectionBottomSheet(cngeList)
        }
        activityBinding.inputDepartment.setOnClickListener {
//            openDeptSelectionBottomSheet(deptList)
        }
        activityBinding.inputOrigin.setOnClickListener {
//            openOriginSelectionBottomSheet(originList)
        }
        activityBinding.inputDestination.setOnClickListener {
//            openDestinationSelectionBottomSheet(destinationList)
        }

        activityBinding.inputPickupBoy.setOnClickListener {
            openPickupBoySelectionBottomSheet(pickupBoylist)
        }
        activityBinding.inputAgent.setOnClickListener {
            openAgentSelectionBottomSheet(agentList)
        }
        activityBinding.inputVehicleVendor.setOnClickListener {
            openVehicleVendorSelectionBottomSheet(vehicleVendorList)
        }
        activityBinding.inputVehicle.setOnClickListener {
            openVehicleSelectionBottomSheet(vehicleList)
        }


        activityBinding.inputDate.setOnClickListener {
//            successToast(mContext, "datePicker")
//            openDatePicker()
            openSingleDatePicker()
        }

        activityBinding.inputTime.setOnClickListener {
//            successToast(mContext, "timePicker")
            openTimePicker()
        }

        activityBinding.chekActualAWB.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                activityBinding.chekActualAWB.text = "Y"
            }
            else {
                activityBinding.chekActualAWB.text = "N"
            }

        }

        activityBinding.btnEwayBill.setOnClickListener{
            openEwayBillBottomSheet()
        }
        activityBinding.btnSave.setOnClickListener {
            checkFieldsBeforeSave()
        }

    }
//    private fun refreshData(){
//
//    }

    private fun setObservers() {
        timePeriod.observe(this) { time ->
            activityBinding.inputTime.setText(time)
        }
        activityBinding.refreshLayout.setOnRefreshListener {
//            refreshData()
            lifecycleScope.launch {
                delay(1500)
                activityBinding.refreshLayout.isRefreshing=false
            }
        }
        viewModel.isError.observe(this) { errMsg ->
            errorToast(errMsg)
        }
        mPeriod.observe(this) { datePicker ->
            activityBinding.inputDate.setText(datePicker.viewsingleDate)
        }

        timePeriod.observe(this){ timePicker->

//            activityBinding.inputTime.setText(timePicker.viewSingleTime)
        }
        viewModel.saveBookingLiveData.observe(this){ data->


        }
        viewModel.viewDialogLiveData.observe(this, Observer { show ->
//            progressBar.visibility = if(show) View.VISIBLE else View.GONE
            if (show) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })

        viewModel.custLiveData.observe(this) { custData ->
            customerList = custData
        }

        viewModel.cngrLiveData.observe(this) { cngrData ->
            cngrList = cngrData
        }

        viewModel.cngrLiveData.observe(this) { cngeData ->
            cngeList = cngeData
        }

        viewModel.deptLiveData.observe(this) { deptData ->
            deptList = deptData
        }
        viewModel.originLiveData.observe(this) { originData ->
            originList = originData
        }

        viewModel.destinationLiveData.observe(this) { destData ->
            destinationList = destData
        }
        viewModel.pickupBoyLiveData.observe(this) { boyData ->
            pickupBoylist = boyData
        }
        viewModel.pickupBoyLiveData.observe(this) { boyData ->
            pickupBoylist = boyData
        }

        viewModel.tempLiveData.observe(this) { tempData ->
            temperatureList = tempData

        }
        viewModel.contentLiveData.observe(this) { itemData ->
            contentList = itemData
        }
        viewModel.agentLiveData.observe(this) { agent ->
            agentList = agent
        }
        viewModel.vendorLiveData.observe(this) { vendor ->
            vehicleVendorList = vendor
        }
        viewModel.vehicleLiveData.observe(this) { vehicle ->
            vehicleList = vehicle
        }
        viewModel.packingLiveData.observe(this) { packing ->
            packingList = packing
            }
        viewModel.gelPackItemLiveData.observe(this) { gelPack ->
            gelPackList = gelPack
            }

        viewModel.singleRefLiveData.observe(this) { data ->
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
                ArrayAdapter(this, android.R.layout.simple_list_item_1,serviceTypeList )
            activityBinding.selectService.adapter = serviceAdapter

        }
      viewModel.pckgLiveData.observe(this) { pckgType ->
          pckgTypeList = pckgType
          val pckgsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, pckgTypeList)
          activityBinding.selectedPckgsType.adapter = pckgsAdapter

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
            arrayListOf(userDataModel?.loginbranchcode.toString(), userDataModel?.custcode.toString(), "R", "")
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
            arrayListOf(userDataModel?.loginbranchcode.toString(), userDataModel?.custcode.toString(), "E", "")
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
            arrayListOf(userDataModel?.custcode.toString(),userDataModel?.loginbranchcode.toString())
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
            commonList.add(CommonBottomSheetModel(rvList[i].stnname, rvList[i]))

        }
        openCommonBottomSheet(this, "Destination Selection", this, commonList)
    }

    private fun getAgentLov() {
        viewModel.getAgentLov(
            loginDataModel?.companyid.toString(),
            "greentrans_vendlist",
            listOf("prmvendorcategory","prmmenucode"),
            arrayListOf("PUD VENDOR",menuModel?.menucode.toString())
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
            listOf("prmorgcode","prmdestcode","prmmodetype","prmmodegroupcode","prmvendcode","prmownervendcode"),
            arrayListOf("","","S","S","","")  // need to modify
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
                    listOf("prmvendorcategory","prmmenucode"),
                    arrayListOf("VEHICLE VENDOR",menuModel?.menucode.toString())
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
    private fun openTemperatureSelectionBottomSheet(rvList: ArrayList<TemperatureSelectionModel>, index: Int) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].name, rvList[i]))

        }
        openCommonBottomSheet(this, "Temperature Selection", this, commonList,true, index)
    }

    private fun getContentList() {
        viewModel.getContentLov(
            loginDataModel?.companyid.toString(),
            "greentrans_showgoodsLOV",
            listOf(),
            arrayListOf()
        )
    }
    private fun openContentSelectionBottomSheet(rvList: ArrayList<ContentSelectionModel>, index: Int) {
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
    private fun openPackingSelectionBottomSheet(rvList: ArrayList<PackingSelectionModel>, index: Int) {
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
    private fun openGelPackSelectionBottomSheet(rvList: ArrayList<GelPackItemSelectionModel>, index: Int) {
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

    private fun setupRecyclerView() {
        if(bookingAdapter == null) {
            linearLayoutManager = LinearLayoutManager(this)
            activityBinding.recyclerView.layoutManager = linearLayoutManager
            bookingAdapter = BookingAdapter(this, this, singleRefList, this)
        }
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
            custCode=selectedCustomer.custcode

        } else if (clickType == "Consignor Selection") {
            val selectedCngr = data as ConsignorSelectionModel
            activityBinding.inputCngrName.setText(selectedCngr.name)
            activityBinding.inputCngrAddress.setText(selectedCngr.address)
            cngrCity = selectedCngr.city
            cngrZipCode = selectedCngr.zipcode
            cngrState = selectedCngr.state
            cngrMobileNo = selectedCngr.secondarymobileno.toString()
            cngreMailId = selectedCngr.email.toString()
            cngrCSTNo = selectedCngr.cstno.toString()
            cngrLSTNo = selectedCngr.lstno.toString()
            cngrTINNo = selectedCngr.tinno.toString()
            cngrCode=selectedCngr.code.toString()
            cngrGstNo=selectedCngr.gstno.toString()

        } else if (clickType == "Consignee Selection") {
            val selectedCnge = data as ConsignorSelectionModel
            activityBinding.inputCngeName.setText(selectedCnge.name)
            activityBinding.inputCngeAddress.setText(selectedCnge.address)
            cngeCity =selectedCnge.city.toString()
            cngeZipCode =selectedCnge.zipcode.toString()
            cngeState =selectedCnge.state.toString()
            cngeMobileNo =selectedCnge.secondarymobileno.toString()
            cngeeMailId =selectedCnge.email.toString()
            cngeCSTNo =selectedCnge.cstno.toString()
            cngeLSTNo =selectedCnge.lstno.toString()
            cngeTINNo =selectedCnge.tinno.toString()
            cngeCode=selectedCnge.code.toString()
            cngeGstNo=selectedCnge.gstno.toString()
//            cngeSTaxRegNo =selectedCnge..toString()
        } else if (clickType == "Department Selection") {
            val selectedDept = data as DepartmentSelectionModel
            activityBinding.inputDepartment.setText(selectedDept.custdeptname)
            custDeptId=selectedDept.custdeptid.toString()


        } else if (clickType == "origin Selection") {
            val selectedOrigin = data as OriginSelectionModel
            activityBinding.inputOrigin.setText(selectedOrigin.stnname)


        } else if (clickType == "Destination Selection") {
            val selectedDestination = data as DestinationSelectionModel
            activityBinding.inputDestination.setText(selectedDestination.stnname)
            destCode=selectedDestination.stncode.toString()

        } else if (clickType == "Pickup Boy Selection") {
            val selectedPickupBoy = data as PickupBoySelectionModel
            activityBinding.inputPickupBoy.setText(selectedPickupBoy.name)
            boyId=selectedPickupBoy.boyid.toString()
        } else if (clickType == "Agent Selection") {
            val selectedAgent = data as AgentSelectionModel
            activityBinding.inputAgent.setText(selectedAgent.vendname)

        } else if (clickType == "Vehicle Selection") {
            val selectedVehicle = data as BookingVehicleSelectionModel
            activityBinding.inputVehicle.setText(selectedVehicle.regno)
            vehicleCode=selectedVehicle.vehiclecode

        } else if (clickType == "Vendor Selection") {
            val selectedVendor = data as AgentSelectionModel
            activityBinding.inputVehicleVendor.setText(selectedVendor.vendname)
            vendorCode=selectedVendor.vendcode.toString()

        }
    }

    override fun onItemClickWithAdapter(data: Any, clickType: String, index: Int) {
        if (clickType == "Content Selection") {
            val selectedContent=data as ContentSelectionModel
            bookingAdapter?.setContent(selectedContent, index)
            pickupContentId=selectedContent.itemcode

        } else if(clickType=="Temperature Selection"){
            val selectedTemp=data as TemperatureSelectionModel
            bookingAdapter?.setTemperature(selectedTemp, index)

        }else if(clickType=="Packing Selection"){
            val selectedPckg=data as PackingSelectionModel
            bookingAdapter?.setPacking(selectedPckg, index)

        }else if(clickType=="Gel Pack Selection"){
            val selectedGelPack=data as GelPackItemSelectionModel
            bookingAdapter?.setGelPack(selectedGelPack, index)
            selectedGelPackItenCode=selectedGelPack.itemcode.toString()

        }


    }

    override fun onRowClick(data: Any, clickType: String, index: Int) {

        if (clickType == "CONTENT_SELECT") {
            openContentSelectionBottomSheet(contentList, index)

        }else if (clickType == "TEMPERATURE_SELECT"){
            openTemperatureSelectionBottomSheet(temperatureList,index)

        }else if(clickType =="GEL_PACK_SELECT"){
                openGelPackSelectionBottomSheet(gelPackList,index)

        }else if(clickType =="PACKING_SELECT"){
            openPackingSelectionBottomSheet(packingList,index)

        }else if(clickType =="LENGTH_SELECT"){
//           errorToast("length select")

        }else if(clickType == "REMOVE_SELECT"){
//                singlePickupDataList.forEachIndexed {index, element ->
//                singlePickupDataList.removeAt(index)
//                activityBinding.recyclerView.adapter!!.notifyItemRangeRemoved(index,singlePickupDataList.size)
//
        }
    }



    override fun onCLick(data: Any, clickType: String) {

    }
    fun setVWeight(vWeight: Float) {
        activityBinding.inputVolWeight.setText(vWeight.toString())
    }

    fun setAWeight(aWeight: Int) {
        activityBinding.inputAWeight.setText(aWeight.toString())
    }
    fun setCWeight(cWeight: Int) {
        activityBinding.inputCWeight.setText(cWeight.toString())
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

    private fun checkFieldsBeforeSave(){
        if(activityBinding.inputDate.text.isNullOrEmpty()){
            activityBinding.inputDate.error="Please Select Date"
            errorToast("Please Select Date")

            return
        }else if (activityBinding.inputTime.text.isNullOrEmpty()){
            errorToast("Please Select Date")
            return
        }else if (activityBinding.inputCustName.text.isNullOrEmpty()){
            errorToast("Please Select Customer")
            return
        }else if (activityBinding.inputCngrName.text.isNullOrEmpty()){
            errorToast("Please Select Consignor Name")
            return
        }else if (activityBinding.inputCngrAddress.text.isNullOrEmpty()){
            errorToast("Please Select Consignor Address")
            return
        }else if (activityBinding.inputCngeName.text.isNullOrEmpty()){
            errorToast("Please Select Consignee Name")
            return
        }else if (activityBinding.inputCngeAddress.text.isNullOrEmpty()){
            errorToast("Please Select Consignee Address")
            return
        }else if (activityBinding.inputOrigin.text.isNullOrEmpty()){
            errorToast("Please Select origin")
            return
        }else if (activityBinding.inputDestination.text.isNullOrEmpty()){
            errorToast("Please Select Destination")
            return
        }else if (activityBinding.selectedPckgsType.autofillHints.isNullOrEmpty()){
            errorToast("Please Select Pckgs Type")
            return
        }else if (activityBinding.inputAWeight.text.isNullOrEmpty()){
            errorToast("Please Select Actual Weight")
            return
        }else if (activityBinding.inputAWeight.text.isNullOrEmpty()){
            errorToast("Please Select Actual Weight")
            return
        }else if (activityBinding.inputCWeight.text.isNullOrEmpty()){
            errorToast("Please Enter Chargeable Weight")
            return
        }else if(pickupByValue == "V"){
            if(activityBinding.inputAgent.text.isNullOrEmpty()){
                errorToast("Please Select Agent")
                return
            }
        }else if(pickupByValue == "S"){
            if(activityBinding.inputPickupBoy.text.isNullOrEmpty()){
                errorToast("Please Select Pickup Boy")
                return
            }
        }else if(pickupByValue == "M"){
            if(activityBinding.inputVehicle.text.isNullOrEmpty()){
                errorToast("Please Select Vehicle")
                return
            }else if(activityBinding.inputPickupBoy.text.isNullOrEmpty()) {
                errorToast("Please Select Pickup Boy")
                return
            }else if(activityBinding.inputVehicleVendor.text.isNullOrEmpty()){
                errorToast("Please Select Vendor")
                return
            }
        }else if(pickupByValue =="O"){
             if(activityBinding.inputPickupBoy.text.isNullOrEmpty()) {
                errorToast("Please Select Pickup Boy")
                 return
            }
        }else if( pickupByValue == "N"){
            if(activityBinding.inputPickupBoy.text.isNullOrEmpty()) {
                errorToast("Please Select Pickup Boy")
                return
            }
        }else if(!activityBinding.autoGrCheck.isChecked && activityBinding.inputGrNo.text.isNullOrEmpty()){
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


    private fun saveBooking(){
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
        var actualPackageStr: String = ""
        var actualVWeightStr: String = ""


        for(i in 0 until singleRefList.size) {
            val refNo = singleRefList[i].referenceno
            val weightStr=singleRefList[i].weight
            val pckgType=singleRefList[i].packagetype
            val tempStr=singleRefList[i].tempurature
            val packing=singleRefList[i].packing
            val goodsStr=singleRefList[i].contents
            val dryIceStr=singleRefList[i].dryice
            val dryIceQtyStr=singleRefList[i].dryiceqty
            val dataLoggerStr=singleRefList[i].datalogger
            val dataLoggerNoStr=singleRefList[i].dataloggerno
            val dimHeight=singleRefList[i].pckgheight
            val dimBreath=singleRefList[i].pckgbreath
            val dimLength=singleRefList[i].pckglength
            val boxNoStr=singleRefList[i].boxno
            val gelPackStr=singleRefList[i].gelpack
            val gelPackQtyStr=singleRefList[i].gelpackqty
            val packageStr=singleRefList[i].packagetype
            val vWeightStr=singleRefList[i].volfactor

            actualRefNoStr += "$refNo,"
            actualWeightStr +="$weightStr,"
            actualPackageTypeStr +="$pckgType,"
            actualTempuratureStr +="$tempStr,"
            actualPackingStr +="$packing,"
            actualContent +="$goodsStr,"
            actualDryIceStr +="$dryIceStr,"
            actualDryIceQtyStr +="$dryIceQtyStr,"
            actualDataLoggerStr +="$dataLoggerStr,"
            actualDataLoggerNoStr +="$dataLoggerNoStr,"
            actualDimHeight +="$dimHeight,"
            actualDimBreath +="$dimBreath,"
            actualDimLength +="$dimLength,"
            actualBoxNo +="$boxNoStr,"
            actualGelPackStr +="$gelPackStr,"
            actualGelPackQtyStr+="$gelPackQtyStr,"
            actualPackageStr+="$packageStr,"
            actualVWeightStr+="$vWeightStr,"


        }
        viewModel.saveBooking(
            companyId = userDataModel?.companyid.toString(),
            branchCode = userDataModel?.loginbranchcode.toString(),
            bookingDt = activityBinding.inputDate.text.toString(),
            time =activityBinding.inputTime.text.toString(),
            grNo = activityBinding.inputGrNo.text.toString(),
            custCode = custCode,
            destCode = destCode,
            productCode = productCode,
            pckgs = activityBinding.inputPckgsNo.text.toString(),
            aWeight = activityBinding.inputAWeight.text.toString(),
            vWeight = activityBinding.inputVolWeight.text.toString(),
            cWeight = activityBinding.inputCWeight.text.toString(),
            createId = userDataModel?.usercode.toString(),
            sessionId = userDataModel?.sessionid.toString(),
            refNo = "",
            cngr = activityBinding.inputCngrName.text.toString(),
            cngrAddress = activityBinding.inputCngrAddress.text.toString(),
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
            cngeAddress = activityBinding.inputCngeAddress.text.toString(),
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
            awbChargeApplicable = activityBinding.chekActualAWB.toString(),
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
            dimLength =actualDimLength ,
            dimBreath =actualDimBreath,
            dimHeight = actualDimHeight,
            pickupBoyName = activityBinding.inputPickupBoy.text.toString(),
            boyId = boyId,
            boxnoStr = actualBoxNo,
            stockItemCodeStr = pickupContentId,
            gelPackStr = actualGelPackStr,
            gelPackItemCodeStr = selectedGelPackItenCode,
            gelPackQtyStr = actualGelPackQtyStr,
            menuCode = menuModel?.menucode.toString(),
            invoiceNoStr = "",
            invoiceDtStr = "",
            invoiceValueStr = "",
            ewayBillnNoStr = "",
            ewayBillDtStr = "",
            ewbValidupToDtStr = "",
            vendorCode = vendorCode,
            packageStr = actualPackageStr,
            pickupBy = pickupByValue,
            vehicleNo = activityBinding.inputVehicle.text.toString(),
            vWeightStr = actualVWeightStr,
            vehicleCode = vehicleCode,
            cngrCode = cngrCode,
            cngeCode = cngeCode,
            remarks = activityBinding.inputRemark.text.toString(),
            cngrGstNo =cngrGstNo ,
            cngeGstNo = cngeGstNo,
        )
    }
}