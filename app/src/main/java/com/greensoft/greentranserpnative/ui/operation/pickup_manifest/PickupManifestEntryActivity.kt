package com.greensoft.greentranserpnative.ui.operation.pickup_manifest

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseActivity

import com.greensoft.greentranserpnative.databinding.ActivityPickupManifestEntryBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.common.models.CommonBottomSheetModel
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.booking.BookingViewModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.ContentSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.PackingSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.adapter.SavePickupManifestAdapter
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.BranchSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.DriverSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.GrSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.PickupLocationModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.ManifestEnteredDataModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.VehicleSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.VehicleTypeModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.VendorSelectionModel
import com.greensoft.greentranserpnative.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PickupManifestEntryActivity @Inject constructor() : BaseActivity(), OnRowClick<Any>, BottomSheetClick<Any> {
    lateinit var activityBinding: ActivityPickupManifestEntryBinding
    private val viewModel: PickupManifestViewModel by viewModels()
//    var selectedVehicleType = arrayOf("OWN", "ATTACHED", "MARKET")
    var selectedLoadedBy = arrayOf("CUSTOMER", "SELF")
    lateinit var linearLayoutManager: LinearLayoutManager
    private var branchList: ArrayList<BranchSelectionModel> = ArrayList()
    private var locationList: ArrayList<PickupLocationModel> = ArrayList()
    private var driverList: ArrayList<DriverSelectionModel> = ArrayList()
    private var vendorList: ArrayList<VendorSelectionModel> = ArrayList()
    private var vehicleList: ArrayList<VehicleSelectionModel> = ArrayList()
    private var vehicleTypeList: ArrayList<VehicleTypeModel> = ArrayList()

    private val bookingViewModel: BookingViewModel by viewModels()
    private var rvAdapter: SavePickupManifestAdapter? = null
    private var grList: ArrayList<GrSelectionModel> = ArrayList()
    private var contentList: ArrayList<ContentSelectionModel> = ArrayList()
    private var packingList: ArrayList<PackingSelectionModel> = ArrayList()
    private var model: ManifestEnteredDataModel? = null


    var contentCode=""
    var content=""
    var enteredPckgs=""
    var enteredGWeight=""
    var enteredBalancePckg=""

    var vendorCode = ""
    var areaCode = ""
    var branchCode = ""
    var vehicleCode = ""
    var vehicleCategory = ""
    var loadedByType = ""
    var manifestDt= getSqlCurrentDate()
    var driverCode=""

    var menuCode="GTAPP_NATIVEPICKUPMANIFEST"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding=ActivityPickupManifestEntryBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        activityBinding.inputDate.setText(getViewCurrentDate())
        activityBinding.inputTime.setText(getSqlCurrentTime())
        setUpToolbar("Pickup Manifest")
        setObservers()


//        getPickupLocation()
//        getDriverList()
//        getVendorList()
        getVehicleTypeList()
        setOnclick()
//        getIntentData()
        setSpinners()
//        val data=Utils.selectedGrList
//        getIntentData()



    }

    override fun onRestart() {
        super.onRestart()


    }
    override fun onResume() {
        super.onResume()
//        val data=Utils.selectedGrList
//        successToast(data.toString())
//        getIntentData()
    }


//    private fun getIntentData() {
//        if(intent != null) {
//            val jsonString = intent.getStringExtra("ARRAY_JSON")
//            if(jsonString != "") {
//                val gson = Gson()
//                val listType = object : TypeToken<List<GrSelectionModel>>() {}.type
//                val resultList: ArrayList<GrSelectionModel> =
//                    gson.fromJson(jsonString.toString(), listType)
//                grList.addAll(resultList)
//                setupRecyclerView()
//            }
//        }
//    }

//    private fun setupRecyclerView() {
//        linearLayoutManager = LinearLayoutManager(this)
//        if (rvAdapter == null) {
//            rvAdapter = SavePickupManifestAdapter(this,this,grList, this@PickupManifestEntryActivity)
//            activityBinding.recyclerView.apply {
//                layoutManager = linearLayoutManager
//                adapter = rvAdapter
//            }
//        }
//
//
//    }

    fun getPckgsValue(pckgs: Double){
        enteredPckgs=pckgs.toString()
    }

    fun getBalancepckg(balancePckg:Double){
        enteredBalancePckg=balancePckg.toString()
    }
    fun getGWeightValue(gWeight:Double){
        enteredGWeight=gWeight.toString()
    }

    private fun setOnclick(){
          activityBinding.inputBranch.setOnClickListener{
              getBranchList()
          }
          activityBinding.inputPickupLocation.setOnClickListener{
              getPickupLocation()
//              openPickupLocationSelectionBottomSheet(locationList)
          }
          activityBinding.inputDriverName.setOnClickListener{
              getDriverList()
//              openDriverSelectionBottomSheet(driverList)
          }
          activityBinding.inputVendorName.setOnClickListener{
//              openVendorSelectionBottomSheet(vendorList)
              getVendorList()
          }
          activityBinding.inputVehicleNumber.setOnClickListener{
//                  if (activityBinding.inputVendorName.text!!.isNullOrEmpty()){
//                      errorToast("Please Select Vendor First")
//                      return@setOnClickListener
//                  }
                  getVehicleList()


          }

          activityBinding.autoManifestCheck.setOnCheckedChangeListener { compoundButton, bool ->
              if(bool) {
                  activityBinding.inputManifestNum.setText("")

              }
              activityBinding.inputManifestNum.isEnabled = !(bool)

          }
          activityBinding.inputDate.setOnClickListener {
              openSingleDatePicker()
          }
          activityBinding.inputTime.setOnClickListener {
              openTimePicker()
          }
//           activityBinding.autoManifestCheck.setOnClickListener {
//               if (activityBinding.autoManifestCheck.isChecked){
//                   activityBinding.inputManifestNum.isFocusable=false
//                   activityBinding.inputManifestNum.text!!.clear()
//                   activityBinding.inputManifestNum.setBackgroundColor(ContextCompat.getColor(this, R.color.tran_light_grey))
//
//
//               }else{
//                   activityBinding.inputManifestNum.isFocusable=true
//                   successToast("not show")
//               }
//           }
          activityBinding.btnGrSelect.setOnClickListener {
            if (activityBinding.inputBranch.text.isNullOrEmpty()) {
                Companion.errorToast(this,"Please Select Branch")
                return@setOnClickListener
            }else if(!activityBinding.autoManifestCheck.isChecked){
                if(activityBinding.inputManifestNum .text.isNullOrEmpty()) {
                    Companion.errorToast(this,"Please Enter Manifest Number")
                    return@setOnClickListener
            }
            }else if (activityBinding.inputDate.text.isNullOrEmpty()) {
                Companion.errorToast(this,"Please Select Date")
                return@setOnClickListener
            }else if (activityBinding.inputTime.text.isNullOrEmpty()) {
                Companion.errorToast(this,"Please Select Time")
                return@setOnClickListener
            }else if (activityBinding.inputDriverName .text.isNullOrEmpty()) {
                Companion.errorToast(this,"Please Driver Name")
                return@setOnClickListener

            }else if (activityBinding.inputVehicleNumber .text.isNullOrEmpty()) {
                Companion.errorToast(this,"Please Select Vehicle Number")
                return@setOnClickListener
            }
              val intent= Intent(this,GrSelectionActivity::class.java)
              getManifestData()
              startActivity(intent)

          }

     }

      private fun setObservers(){
          viewModel.isError.observe(this) { errMsg ->
              errorToast(errMsg)
          }
          viewModel.viewDialogLiveData.observe(this, Observer { show ->
//            progressBar.visibility = if(show) View.VISIBLE else View.GONE
              if(show) {
                  showProgressDialog()
              } else {
                  hideProgressDialog()
              }
          })

          viewModel.branchLiveData.observe(this) { branchData ->
              if (branchData.elementAt(0).commandstatus ==1){
                    branchList = branchData
                    openBranchSelectionBottomSheet(branchList)
              }else{
                  errorToast(branchData.elementAt(0).commandmessage.toString())
              }
          }

          viewModel.pickupLocationLiveData.observe(this) { locationData ->
              if(locationData.elementAt(0).commandstatus ==1){
              locationList = locationData
                  openPickupLocationSelectionBottomSheet(locationList)
              }else{

              }
          }
          viewModel.driverLiveData.observe(this) { driverData ->
              if(driverData.elementAt(0).commandstatus ==1){
              driverList = driverData
                  openDriverSelectionBottomSheet(driverList)
              }
          }
          viewModel.vendorLiveData.observe(this) { vendData ->
              if(vendData.elementAt(0).commandstatus ==1){
              vendorList = vendData
                  openVendorSelectionBottomSheet(vendorList)
              }

          }
          viewModel.vehicleLiveData.observe(this) { vehicleData ->
              if(vehicleData.elementAt(0).commandstatus== 1){
                  vehicleList = vehicleData
                  openVehicleSelectionBottomSheet(vehicleList)
              }else{
                 errorToast(vehicleData.elementAt(0).commandmessage.toString())
              }

          }
          viewModel.vehicleTypeLiveData.observe(this) { type ->
              vehicleTypeList = type
              val vehicleAdapter =
                  ArrayAdapter(this, android.R.layout.simple_list_item_1, vehicleTypeList)
              activityBinding.selectedVehicleType.adapter = vehicleAdapter
          }

         mPeriod.observe(this){date->
             activityBinding.inputDate.setText(date.viewsingleDate)
             manifestDt=date.sqlsingleDate.toString()
         }
          timePeriod.observe(this){time ->
              activityBinding.inputTime.setText(time)
          }
      }

      private fun setSpinners(){

              activityBinding.selectedVehicleType.onItemSelectedListener =
                  object : AdapterView.OnItemSelectedListener {
                      override fun onItemSelected(
                          parent: AdapterView<*>?,
                          view: View?,
                          position: Int,
                          id: Long
                      ) {
                           vehicleCategory=vehicleTypeList[position].value
                          when(vehicleCategory){
                              //OWN
                              "O"-> run{
                                  activityBinding.inputVendorName.text!!.clear()
                                  activityBinding.inputVendorName.isEnabled= false
                                  activityBinding.inputVehicleNumber.text!!.clear()
                                  vendorCode=""
                              }
                              //ATTACHED
                              "A"-> run{
                                  activityBinding.inputVendorName.isEnabled= true
                                  activityBinding.inputVehicleNumber.text!!.clear()

                              }
                              "M"-> run{
                                  vendorCode=""
                                  activityBinding.inputVendorName.text!!.clear()
                                  activityBinding.inputVendorName.isEnabled= false
                                  activityBinding.inputVehicleNumber.text!!.clear()
                              }

                          }
                      }

                      override fun onNothingSelected(parent: AdapterView<*>?) {
                          TODO("Not yet implemented")
                      }
              }
          val loadedByAdapter =
              ArrayAdapter(this, android.R.layout.simple_list_item_1, selectedLoadedBy)
              activityBinding.selectedLoadedBy.adapter = loadedByAdapter
              activityBinding.selectedLoadedBy.onItemSelectedListener =
                  object : AdapterView.OnItemSelectedListener {
                      override fun onItemSelected(
                          parent: AdapterView<*>?,
                          view: View?,
                          position: Int,
                          id: Long
                      ) {
                          when(position){
                              0-> run{
                                  //customer

                                  loadedByType="C"
                              }
                              // self
                              1 -> kotlin.run {
                                  loadedByType="S"
                              }
                          }

                      }

                      override fun onNothingSelected(parent: AdapterView<*>?) {
                          TODO("Not yet implemented")
                      }
              }

      }


    private fun getBranchList() {
        viewModel.getBranchList(
            loginDataModel?.companyid.toString(),
            "greentrans_showbranchOPSLOV",
            listOf("prmbranchcode"),
            arrayListOf(userDataModel?.loginbranchcode.toString())
        )
    }

    private fun openBranchSelectionBottomSheet(rvList: ArrayList<BranchSelectionModel>) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].stnname, rvList[i]))

        }
        openCommonBottomSheet(this, "Branch Selection", this, commonList)
    }

    private fun getPickupLocation() {
        viewModel.getPickupLocation(
            loginDataModel?.companyid.toString(),
            "greentransweb_showlocalarealist",
            listOf("prmbranchcode"),
            arrayListOf(userDataModel?.loginbranchcode.toString())
        )
    }

    private fun openPickupLocationSelectionBottomSheet(rvList: ArrayList<PickupLocationModel>) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].name, rvList[i]))

        }
        openCommonBottomSheet(this, "Pickup Location Selection", this, commonList)
    }

    private fun getDriverList() {
        viewModel.getDriverList(
            loginDataModel?.companyid.toString(),
            "greentrans_driversearchlist",
            listOf("prmmenucode","prmvehiclecategory"),
            arrayListOf(menuCode,vehicleCategory)
        )
    }

    private fun openDriverSelectionBottomSheet(rvList: ArrayList<DriverSelectionModel>) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].drivername, rvList[i]))

        }
        openCommonBottomSheet(this, "Driver Selection", this, commonList)
    }
    private fun getVendorList() {
        viewModel.getVendorList(
            loginDataModel?.companyid.toString(),
            "greentrans_vendlist",
            listOf("prmvendorcategory","prmmenucode"),
            arrayListOf("VEHICLE VENDOR",menuCode)
        )
    }

    private fun openVendorSelectionBottomSheet(rvList: ArrayList<VendorSelectionModel>) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].vendname, rvList[i]))
//            getVehicleList()
        }
        openCommonBottomSheet(this, "Vendor Selection", this, commonList)
    }

    private fun getVehicleList() {
        viewModel.getVehicleList(
            loginDataModel?.companyid.toString(),
            "greentranswebsg_showvehicleontype",
            listOf("prmcategory","prmvendorcode"),
            arrayListOf(vehicleCategory,vendorCode)
        )
    }

    private fun getVehicleTypeList() {
        viewModel.getVehicleType(
            loginDataModel?.companyid.toString(),
            "gtapp_getvehicletype",
            listOf(),
            arrayListOf()
        )
    }

    private fun openVehicleSelectionBottomSheet(rvList: ArrayList<VehicleSelectionModel>) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].regno, rvList[i]))
        }
        openCommonBottomSheet(this, "Vehicle Selection", this, commonList)
    }

    override fun onClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }
 private fun  getManifestData(){
        Utils.manifestModel=ManifestEnteredDataModel(
             branchName = activityBinding.inputBranch.text.toString(),
             branchCode=branchCode,
             manifestNo = activityBinding.inputManifestNum.text.toString(),
             pickupLocation = activityBinding.inputPickupLocation.text.toString(),
             driverCode = driverCode,
             manifestDt = manifestDt,
             manifestTime = activityBinding.inputTime.text.toString(),
             driverName = activityBinding.inputDriverName.text.toString(),
             drivermobile = activityBinding.inputDriverMobile.text.toString(),
             vehicleType = vehicleCategory,
             vendorName = activityBinding.inputVendorName.text.toString(),
             vendorCode = vendorCode,
             vehicleNo = activityBinding.inputVehicleNumber.text.toString(),
             vehicleCode = vehicleCode,
             loadedBy = loadedByType,
             capacity = activityBinding.inputCapacity.text.toString(),
             areaCode = areaCode,
             remark = activityBinding.inputRemark.text.toString(),
         )

 }
    override fun onItemClick(data: Any, clickType: String) {
      when(clickType){
          "Branch Selection"-> run{
              val selectedBranch = data as BranchSelectionModel
              activityBinding.inputBranch.setText(selectedBranch.stnname)
              branchCode=selectedBranch.stncode.toString()
          }

          "Pickup Location Selection"-> run{
              val selectedLocation = data as PickupLocationModel
              activityBinding.inputPickupLocation.setText(selectedLocation.name)
              areaCode=selectedLocation.code.toString()
          }

          "Driver Selection"-> run{
              val selectedDriver = data as DriverSelectionModel
              activityBinding.inputDriverName.setText(selectedDriver.drivername)
              activityBinding.inputDriverMobile.setText(selectedDriver.mobileno)
              driverCode=selectedDriver.drivercode.toString()
          }
          "Vendor Selection"-> run{
              val selectedVendor = data as VendorSelectionModel
              activityBinding.inputVendorName.setText(selectedVendor.vendname)
              vendorCode=selectedVendor.vendcode
//              getVehicleList()


          }
          "Vehicle Selection"-> run{
              val selectedVehicle = data as VehicleSelectionModel
              activityBinding.inputVehicleNumber.setText(selectedVehicle.regno)
              vehicleCode= selectedVehicle.vehiclecode
             activityBinding.inputCapacity.setText(selectedVehicle.capacity.toString())

          }
      }
    }
}