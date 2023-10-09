package com.greensoft.greentranserpnative.ui.operation.pickup_manifest

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.greensoft.greentranserpnative.base.BaseActivity

import com.greensoft.greentranserpnative.databinding.ActivityPickupManifestEntryBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.common.models.CommonBottomSheetModel
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.gr_select.GrSelectFragment
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.BranchSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.DriverSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.PickupLocationModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.VehicleSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.VendorSelectionModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PickupManifestEntryActivity @Inject constructor() : BaseActivity(), OnRowClick<Any>, BottomSheetClick<Any> {
    lateinit var activityBinding: ActivityPickupManifestEntryBinding
    private val viewModel: PickupManifestViewModel by viewModels()
    var selectedVehicleType = arrayOf("OWN", "ATTACHED", "MARKET")
    var selectedLoadedBy = arrayOf("CUSTOMER", "SELF")
    private var branchList: ArrayList<BranchSelectionModel> = ArrayList()
    private var locationList: ArrayList<PickupLocationModel> = ArrayList()
    private var driverList: ArrayList<DriverSelectionModel> = ArrayList()
    private var vendorList: ArrayList<VendorSelectionModel> = ArrayList()
    private var vehicleList: ArrayList<VehicleSelectionModel> = ArrayList()
    var vendorCode = ""
    var vehicleCategory = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding=ActivityPickupManifestEntryBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("Pickup Manifest")
        getBranchList()
        getPickupLocation()
        getDriverList()
        getVendorList()
        getVehicleList()
        setObservers()
        setOnclick()
        setSpinners()

    }


      private fun setOnclick(){
          activityBinding.inputBranch.setOnClickListener{
              openBranchSelectionBottomSheet(branchList)
          }
          activityBinding.inputPickupLocation.setOnClickListener{
              openPickupLocationSelectionBottomSheet(locationList)
          }
          activityBinding.inputDriverName.setOnClickListener{
              openDriverSelectionBottomSheet(driverList)
          }
          activityBinding.inputVendorName.setOnClickListener{
              openVendorSelectionBottomSheet(vendorList)
          }
          activityBinding.inputVehicleNumber.setOnClickListener{
              openVehicleSelectionBottomSheet(vehicleList)
          }
          activityBinding.btnGrSelect.setOnClickListener {
              val grSelect= GrSelectFragment()
              .show(supportFragmentManager,"show Gr Select")
          }

     }

      private fun setObservers(){
          viewModel.isError.observe(this) { errMsg ->
              errorToast(errMsg)
          }
          viewModel.branchLiveData.observe(this) { branchData ->
              branchList = branchData
          }

          viewModel.pickupLocationLiveData.observe(this) { locationData ->
              locationList = locationData
          }
          viewModel.driverLiveData.observe(this) { driverData ->
              driverList = driverData
          }
          viewModel.vendorLiveData.observe(this) { vendData ->
              vendorList = vendData

          }
          viewModel.vehicleLiveData.observe(this) { vehicleData ->
              vehicleList = vehicleData
          }

      }

      private fun setSpinners(){
          val vehicleAdapter =
              ArrayAdapter(this, android.R.layout.simple_list_item_1, selectedVehicleType)
              activityBinding.selectedVehicleType.adapter = vehicleAdapter
              activityBinding.selectedVehicleType.onItemSelectedListener =
                  object : AdapterView.OnItemSelectedListener {
                      override fun onItemSelected(
                          parent: AdapterView<*>?,
                          view: View?,
                          position: Int,
                          id: Long
                      ) {
                          when(position){
                              //OWN
                              1-> run{
                                  vehicleCategory="O"
                              }
                              //ATTACHED
                              2-> run{
                                  vehicleCategory="A"
                              }
                              3-> run{
                                  vehicleCategory="M"
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
            arrayListOf("","")
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
            arrayListOf("","")
        )
    }

    private fun openVendorSelectionBottomSheet(rvList: ArrayList<VendorSelectionModel>) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].vendname, rvList[i]))

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

    private fun openVehicleSelectionBottomSheet(rvList: ArrayList<VehicleSelectionModel>) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].regno, rvList[i]))

        }
        openCommonBottomSheet(this, "Vehicle Selection", this, commonList)
    }

    override fun onCLick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(data: Any, clickType: String) {
      when(clickType){
          "Branch Selection"-> run{
              val selectedBranch = data as BranchSelectionModel
              activityBinding.inputBranch.setText(selectedBranch.stnname)
          }

          "Pickup Location Selection"-> run{
              val selectedLocation = data as PickupLocationModel
              activityBinding.inputPickupLocation.setText(selectedLocation.name)
          }

          "Driver Selection"-> run{
              val selectedDriver = data as DriverSelectionModel
              activityBinding.inputDriverName.setText(selectedDriver.drivername)
              activityBinding.inputDriverMobile.setText(selectedDriver.mobileno)
          }
          "Vendor Selection"-> run{
              val selectedVendor = data as VendorSelectionModel
              activityBinding.inputVendorName.setText(selectedVendor.vendname)
              vendorCode=selectedVendor.vendcode

          }
          "Vehicle Selection"-> run{
              val selectedVehicle = data as VehicleSelectionModel
              activityBinding.inputVehicleNumber.setText(selectedVehicle.regno)

          }
      }
    }
}