package com.greensoft.greentranserpnative.ui.operation.despatch_manifest

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityDespatchManifestEntryBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.common.models.CommonBottomSheetModel
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.booking.BookingViewModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.ContentSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.DestinationSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.PackingSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.adapter.SavePickupManifestAdapter
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.BranchSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.DriverSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.GrSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.ManifestEnteredDataModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.PickupLocationModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.VehicleSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.VehicleTypeModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.VendorSelectionModel
import com.greensoft.greentranserpnative.utils.Utils
import javax.inject.Inject

class DespatchManifestEntryActivity @Inject constructor() : BaseActivity(), OnRowClick<Any>,
    BottomSheetClick<Any> {
    lateinit var activityBinding: ActivityDespatchManifestEntryBinding
    private val viewModel: DespatchManifestViewModel by viewModels()

    var selectedLoadedBy = arrayOf("CUSTOMER", "SELF")
    lateinit var linearLayoutManager: LinearLayoutManager
    private var branchList: ArrayList<BranchSelectionModel> = ArrayList()
    private var locationList: ArrayList<PickupLocationModel> = ArrayList()
    private var toStationList: ArrayList<DestinationSelectionModel> = ArrayList()
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


    var contentCode = ""
    var content = ""
    var enteredPckgs = ""
    var enteredGWeight = ""
    var enteredBalancePckg = ""

    var vendorCode = ""
    var areaCode = ""
    var branchCode = ""
    var vehicleCode = ""
    var vehicleCategory = ""
    var loadedByType = ""
    var manifestDt = getSqlCurrentDate()
    var driverCode = ""
    var destinationCode = ""
    var menuCode = ""




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityDespatchManifestEntryBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        activityBinding.inputDate.setText(getViewCurrentDate())
        activityBinding.inputTime.setText(getSqlCurrentTime())
        activityBinding.inputBranch.setText(userDataModel?.loginbranchname)
        branchCode = userDataModel?.loginbranchcode.toString()
        activityBinding.inputManifestNum.hint = "Enter Manifest #"
        menuCode =
            if (Utils.menuModel == null) " " else Utils.menuModel?.menucode.toString()
        setUpToolbar("Despatch Manifest")
        setObservers()

        getVehicleTypeList()
        setOnclick()

        setSpinners()

    }
    private fun setObservers() {
        viewModel.isError.observe(this) { errMsg ->
            errorToast(errMsg)
        }
        viewModel.viewDialogLiveData.observe(this, Observer { show ->
//            progressBar.visibility = if(show) View.VISIBLE else View.GONE
            if (show) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })

        viewModel.branchLiveData.observe(this) { branchData ->
            if (branchData.elementAt(0).commandstatus == 1) {
                branchList = branchData
                openBranchSelectionBottomSheet(branchList)
            } else {
                errorToast(branchData.elementAt(0).commandmessage.toString())
            }
        }

        viewModel.pickupLocationLiveData.observe(this) { locationData ->
            if (locationData.elementAt(0).commandstatus == 1) {
                locationList = locationData
                openPickupLocationSelectionBottomSheet(locationList)
            } else {

            }
        }
        viewModel.toStationLiveData.observe(this){ toStationData ->
            if (toStationData.elementAt(0).commandstatus == 1) {
                toStationList = toStationData
                openToStationBottomSheet(toStationList)
            }
        }
        viewModel.driverLiveData.observe(this) { driverData ->
            if (driverData.elementAt(0).commandstatus == 1) {
                driverList = driverData
                openDriverSelectionBottomSheet(driverList)
            }
        }
        viewModel.vendorLiveData.observe(this) { vendData ->
            if (vendData.elementAt(0).commandstatus == 1) {
                vendorList = vendData
                openVendorSelectionBottomSheet(vendorList)
            }

        }
        viewModel.vehicleLiveData.observe(this) { vehicleData ->
            if (vehicleData.elementAt(0).commandstatus == 1) {
                vehicleList = vehicleData
                openVehicleSelectionBottomSheet(vehicleList)
            } else {
                errorToast(vehicleData.elementAt(0).commandmessage.toString())
            }

        }
        viewModel.vehicleTypeLiveData.observe(this) { type ->
            vehicleTypeList = type
            val vehicleAdapter =
                ArrayAdapter(this, android.R.layout.simple_list_item_1, vehicleTypeList)
            activityBinding.selectedVehicleType.adapter = vehicleAdapter
        }

        mPeriod.observe(this) { date ->
            activityBinding.inputDate.setText(date.viewsingleDate)
            manifestDt = date.sqlsingleDate.toString()
        }
        timePeriod.observe(this) { time ->
            activityBinding.inputTime.setText(time)
        }
    }
    private fun setSpinners() {
        val toStationModeList = listOf("To Airport", "To Hub")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, toStationModeList)
        activityBinding.inputToStation.adapter = adapter

        activityBinding.inputToStation.onItemSelectedListener =
            object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    toStationModeList[position]

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        activityBinding.selectedVehicleType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    vehicleCategory = vehicleTypeList[position].value
                    when (vehicleCategory) {
                        //OWN
                        "O" -> run {
//                            activityBinding.inputLayoutVendorName.visibility = View.GONE
                            activityBinding.inputVendorName.text!!.clear()
                            activityBinding.inputVendorName.isEnabled = false
                            activityBinding.inputVehicleNumber.text!!.clear()
//                            vendorCode = ""
                        }
                        //ATTACHED
                        "A" -> run {
//                            activityBinding.inputLayoutVendorName.visibility = View.VISIBLE
                            activityBinding.inputVendorName.isEnabled = true
                            activityBinding.inputVehicleNumber.text!!.clear()
                        }
                        "M" -> run {
//                            activityBinding.inputLayoutVendorName.visibility = View.GONE
//                            vendorCode = ""
                            activityBinding.inputVendorName.text!!.clear()
//                            activityBinding.inputVendorName.isEnabled = false
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
                    when (position) {
                        0 -> run {
                            //customer

                            loadedByType = "C"
                        }
                        // self
                        1 -> kotlin.run {
                            loadedByType = "S"
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


    private fun openToStationBottomSheet(rvList: ArrayList<DestinationSelectionModel>) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].stnname, rvList[i]))

        }
        openCommonBottomSheet(this, "Pickup Location Selection", this, commonList)
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

    private fun getDriverList() {
        viewModel.getDriverList(
            loginDataModel?.companyid.toString(),
            "greentrans_driversearchlist",
            listOf("prmmenucode", "prmvehiclecategory"),
            arrayListOf(menuCode, vehicleCategory)
        )
    }
    private fun openDriverSelectionBottomSheet(rvList: ArrayList<DriverSelectionModel>) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].drivername, rvList[i]))

        }
        openCommonBottomSheet(this, "Driver Selection", this, commonList)
    }

    private fun getPickupLocation() {
        viewModel.getPickupLocation(
            loginDataModel?.companyid.toString(),
            "greentransapp_pickuplocationlov",
            listOf("prmbranchcode"),
            arrayListOf(userDataModel?.loginbranchcode.toString())
        )
    }

    private fun getVendorList() {
        viewModel.getVendorList(
            loginDataModel?.companyid.toString(),
            "greentrans_vendlist",
            listOf("prmvendorcategory", "prmmenucode"),
            arrayListOf("VEHICLE VENDOR", menuCode)
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
            "greentransapp_vehiclelov",
            listOf(),
            arrayListOf()

        )
    }


    private fun openPickupLocationSelectionBottomSheet(rvList: ArrayList<PickupLocationModel>) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].name, rvList[i]))

        }
        openCommonBottomSheet(this, "Pickup Location Selection", this, commonList)
    }

    private fun setOnclick() {

        activityBinding.inputPickupLocation.setOnClickListener {
            getPickupLocation()

        }

        activityBinding.inputDriverName.setOnClickListener {
            getDriverList()

        }
        activityBinding.inputVendorName.setOnClickListener {
            getVendorList()
        }
        activityBinding.inputVehicleNumber.setOnClickListener {
            getVehicleList()


        }

        activityBinding.autoManifestCheck.setOnCheckedChangeListener { compoundButton, selected ->
            activityBinding.inputManifestNum.setText("")
            if (selected) {
                activityBinding.inputManifestNum.hint = "Auto Manifest"
            } else {
                activityBinding.inputManifestNum.hint = "Enter Manifest #"
            }
            activityBinding.inputManifestNum.isEnabled = !(selected)

        }
        activityBinding.inputDate.setOnClickListener {
            openSingleDatePicker()
        }
        activityBinding.inputTime.setOnClickListener {
            openTimePicker()
        }

        activityBinding.btnGrSelect.setOnClickListener {
            if (activityBinding.inputBranch.text.isNullOrEmpty()) {
                Companion.errorToast(this, "Please Select Branch")
                return@setOnClickListener
            } else if (!activityBinding.autoManifestCheck.isChecked) {
                if (activityBinding.inputManifestNum.text.isNullOrEmpty()) {
                    Companion.errorToast(this, "Please Enter Manifest Number")
                    return@setOnClickListener
                }
            } else if (activityBinding.inputDate.text.isNullOrEmpty()) {
                Companion.errorToast(this, "Please select Date")
                return@setOnClickListener
            } else if (activityBinding.inputTime.text.isNullOrEmpty()) {
                Companion.errorToast(this, "Please select Time")
                return@setOnClickListener
            } else if (activityBinding.inputDriverName.text.isNullOrEmpty()) {
                Companion.errorToast(this, "Please select Driver.")
                return@setOnClickListener

            } else if (activityBinding.inputVendorName.text.isNullOrEmpty()) {
                Companion.errorToast(this, "Please select Vendor.")
                return@setOnClickListener
            }
            else if (activityBinding.inputVehicleNumber.text.isNullOrEmpty()) {
                Companion.errorToast(this, "Please select Vehicle Number.")
                return@setOnClickListener
            }
//            val intent = Intent(this, GrSelectionForDespatchManifestActivity::class.java)
//            getManifestData()
//            startActivity(intent)

        }

    }
    private fun getManifestData() {
        Utils.manifestModel = ManifestEnteredDataModel(
            branchName = activityBinding.inputBranch.text.toString(),
            branchCode = branchCode,
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
        when (clickType) {
            "Pickup Location Selection" -> run {
                val selectedLocation = data as PickupLocationModel
                activityBinding.inputPickupLocation.setText(selectedLocation.name)
                areaCode = selectedLocation.code.toString()
            }



            "Driver Selection" -> run {
                val selectedDriver = data as DriverSelectionModel
                activityBinding.inputDriverName.setText(selectedDriver.drivername)
                activityBinding.inputDriverMobile.setText(selectedDriver.mobileno)
                driverCode = selectedDriver.drivercode.toString()
            }
            "Vendor Selection" -> run {
                val selectedVendor = data as VendorSelectionModel
                activityBinding.inputVendorName.setText(selectedVendor.vendname)
                vendorCode = selectedVendor.vendcode
//              getVehicleList()


            }
            "Vehicle Selection" -> run {
                val selectedVehicle = data as VehicleSelectionModel
                activityBinding.inputVehicleNumber.setText(selectedVehicle.regno)
                vehicleCode = selectedVehicle.vehiclecode
                activityBinding.inputCapacity.setText(selectedVehicle.capacity.toString())

            }
        }
    }

    override fun onClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }
}