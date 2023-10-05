package com.greensoft.greentranserpnative.ui.booking

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
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
import com.greensoft.greentranserpnative.ui.booking.models.ConsignorSelectionModel
import com.greensoft.greentranserpnative.ui.booking.models.ContentSelectionModel
import com.greensoft.greentranserpnative.ui.booking.models.CustomerSelectionModel
import com.greensoft.greentranserpnative.ui.booking.models.DepartmentSelectionModel
import com.greensoft.greentranserpnative.ui.booking.models.DestinationSelectionModel
import com.greensoft.greentranserpnative.ui.booking.models.OriginSelectionModel
import com.greensoft.greentranserpnative.ui.booking.models.PackingSelectionModel
import com.greensoft.greentranserpnative.ui.booking.models.PickupBoySelectionModel
import com.greensoft.greentranserpnative.ui.booking.models.TemperatureSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.models.SinglePickupRefModel
import com.greensoft.greentranserpnative.ui.bottomsheet.common.models.CommonBottomSheetModel
import com.greensoft.greentranserpnative.ui.home.models.UserMenuModel
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
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
    private var contentList: ArrayList<ContentSelectionModel> = ArrayList()
    private var singlePickupDataList: ArrayList<SinglePickupRefModel> = ArrayList()
    private var addList: ArrayList<SinglePickupRefModel> = ArrayList()
    private var bookingAdapter: BookingAdapter? = null
    var serviceSelectedItems = arrayOf("SELECT", "AIR", "SURFACE", "SURFACE EXPRESS", "TRAIN")
    var pckgsTypeSelectedItems = arrayOf("SELECT", "JEENA PACKING", "PRE PACKED")
    var pikupTypeSelectedItems =
        arrayOf("JEENA STRAFF", "AGENT", " MARKET VEHICLE", "OFFICE/AIRPORT DROP", "CANCEL")

    private val addListMuteLiveData = MutableLiveData<String>()
    val addListLiveData: LiveData<String>
        get() = addListMuteLiveData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar as Toolbar)
        setUpToolbar("BOOKING ENTRY")
        menuModel = getMenuData()
        activityBinding.inputDate.inputType = InputType.TYPE_NULL;
        activityBinding.inputTime.inputType = InputType.TYPE_NULL;
        setLayoutVisibility()
        getIntentData()
        getCustomerList()
        getCngrList()
        getCngeList()
        getDeptList()
        getOriginList()
        getDestinationList()
        getPickupBoyList()
        getTempLov()
        getpackingLov()
        getContentList()
        setObservers()
        setReferenceData()
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
    private fun getIntentData() {
        if(intent != null) {
            val jsonString = intent.getStringExtra("ARRAY_JSON")
            if(jsonString != "") {
                val gson = Gson()
                val listType = object : TypeToken<List<SinglePickupRefModel>>() {}.type
                val resultList: ArrayList<SinglePickupRefModel> =
                    gson.fromJson(jsonString.toString(), listType)
                singlePickupDataList.addAll(resultList)
                setupRecyclerView()
            }
        }
    }
    fun OpenEwayBillBottomSheet(){
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
         singlePickupDataList.forEachIndexed {_, element ->

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

    private fun spinnerSetUp() {
        val serviceAdapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, serviceSelectedItems)
        activityBinding.selectService.adapter = serviceAdapter
        activityBinding.selectService.onItemSelectedListener =
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

        val pckgsAdapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, pckgsTypeSelectedItems)
        activityBinding.selectedPckgsType.adapter = pckgsAdapter
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
        val pickupAdapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, pikupTypeSelectedItems)
        activityBinding.selectedPickupType.adapter = pickupAdapter
        activityBinding.selectedPickupType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when(position){
                        //JEENA STRAFF
                        0-> run{
                         activityBinding.inputLayoutPickBoy.visibility=View.VISIBLE
                        }
                        //AGENT
                        1-> run{
                          activityBinding.inputLayoutPickBoy.visibility=View.GONE
                          activityBinding.inputLayoutAgent.visibility=View.VISIBLE
                        }
                        //MARKET VEHICLE
                        2-> run{
                            activityBinding.inputLayoutPickBoy.visibility=View.VISIBLE
                            activityBinding.inputLayoutVehicle.visibility=View.VISIBLE
                            activityBinding.inputLayoutVehicleVendor.visibility=View.VISIBLE
                            activityBinding.inputLayoutAgent.visibility=View.GONE

                        }
                        //OFFICE/AIRPORT DROP
                        3-> run{
                            activityBinding.inputLayoutAgent.visibility=View.GONE
                            activityBinding.inputLayoutPickBoy.visibility=View.VISIBLE
                            activityBinding.inputLayoutVehicle.visibility=View.GONE
                            activityBinding.inputLayoutVehicleVendor.visibility=View.GONE
                        }
                        //CANCEL
                        4-> run{
                            activityBinding.inputLayoutAgent.visibility=View.GONE
                            activityBinding.inputLayoutVehicle.visibility=View.GONE
                            activityBinding.inputLayoutVehicleVendor.visibility=View.GONE
                            activityBinding.inputLayoutPickBoy.visibility=View.VISIBLE
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }


    }

    private fun setOnClick() {
        activityBinding.inputCustName.setOnClickListener {
            if (DEBUGGING) {
                //            Toast.makeText(this, "testing", Toast.LENGTH_SHORT).show()
            }
            openCustSelectionBottomSheet(customerList)
        }

        activityBinding.inputCngrName.setOnClickListener {
            openCngrSelectionBottomSheet(cngrList)
        }

        activityBinding.inputCngeName.setOnClickListener {
            openCngeSelectionBottomSheet(cngeList)
        }
        activityBinding.inputDepartment.setOnClickListener {
            openDeptSelectionBottomSheet(deptList)
        }
        activityBinding.inputOrigin.setOnClickListener {
            openOriginSelectionBottomSheet(originList)
        }
        activityBinding.inputDestination.setOnClickListener {
            openDestinationSelectionBottomSheet(destinationList)
        }

        activityBinding.inputPickupBoy.setOnClickListener {
            openPickupBoySelectionBottomSheet(pickupBoylist)
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

        activityBinding.chekActualAWB.setOnClickListener {
            if (activityBinding.chekActualAWB.isChecked) {

            } else {

            }
        }

   activityBinding.btnEwayBill.setOnClickListener{
       OpenEwayBillBottomSheet()
   }
    }
//    private fun refreshData(){
//
//    }

    private fun setObservers() {
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
    private fun openTemperatureSelectionBottomSheet(rvList: ArrayList<TemperatureSelectionModel>,index: Int) {
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
        viewModel.getContentLov(
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


//    private fun loadBookingList() {
//        bookingList = listOf(
//            BookingRecyclerModel("1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
//        )
//    }

    private fun setupRecyclerView() {
        if(bookingAdapter == null) {
            linearLayoutManager = LinearLayoutManager(this)
            activityBinding.recyclerView.layoutManager = linearLayoutManager
            bookingAdapter = BookingAdapter(singlePickupDataList, this)
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

        } else if (clickType == "Consignor Selection") {
            val selectedCngr = data as ConsignorSelectionModel
            activityBinding.inputCngrName.setText(selectedCngr.name)
            activityBinding.inputCngrAddress.setText(selectedCngr.address)

        } else if (clickType == "Consignee Selection") {
            val selectedCnge = data as ConsignorSelectionModel
            activityBinding.inputCngeName.setText(selectedCnge.name)
            activityBinding.inputCngeAddress.setText(selectedCnge.address)

        } else if (clickType == "Department Selection") {
            val selectedDept = data as DepartmentSelectionModel
            activityBinding.inputDepartment.setText(selectedDept.custdeptname)


        } else if (clickType == "origin Selection") {
            val selectedOrigin = data as OriginSelectionModel
            activityBinding.inputOrigin.setText(selectedOrigin.stnname)


        } else if (clickType == "Destination Selection") {
            val selectedDestination = data as DestinationSelectionModel
            activityBinding.inputDestination.setText(selectedDestination.stnname)


        } else if (clickType == "Pickup Boy Selection") {
            val selectedPickupBoy = data as PickupBoySelectionModel
            activityBinding.inputPickupBoy.setText(selectedPickupBoy.name)

        }
    }

    override fun onItemClickWithAdapter(data: Any, clickType: String, index: Int) {
        if (clickType == "Content Selection") {
            val selectedContent=data as ContentSelectionModel
            bookingAdapter?.setContent(selectedContent, index)

        } else if(clickType=="Temperature Selection"){
            val selectedTemp=data as TemperatureSelectionModel
            bookingAdapter?.setTemperature(selectedTemp, index)

        }else if(clickType=="Packing Selection"){
            val selectedPckg=data as PackingSelectionModel
            bookingAdapter?.setPacking(selectedPckg, index)

        }


    }

    override fun onRowClick(data: Any, clickType: String, index: Int) {
        if (clickType == "CONTENT_SELECT") {
            openContentSelectionBottomSheet(contentList, index)

        }else if (clickType == "TEMPERATURE_SELECT"){
              openTemperatureSelectionBottomSheet(temperatureList,index)

        }else if(clickType =="GEL_PACK_SELECT"){

        }else if(clickType == "REMOVE_SELECT"){
//                singlePickupDataList.forEachIndexed {index, element ->
//                singlePickupDataList.removeAt(index)
//                activityBinding.recyclerView.adapter!!.notifyItemRangeRemoved(index,singlePickupDataList.size)
//
        }
    }



    override fun onCLick(data: Any, clickType: String) {

    }
}