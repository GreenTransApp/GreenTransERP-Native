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
import com.greensoft.greentranserpnative.ui.operation.booking.models.PackingSelectionModel
import com.greensoft.greentranserpnative.ui.operation.despatch_manifest.models.DespatchManifestEnteredDataModel
import com.greensoft.greentranserpnative.ui.bottomsheet.modeCode.ModeCodeSelectionModel
import com.greensoft.greentranserpnative.ui.operation.despatch_manifest.models.GroupModeCodeModel
import com.greensoft.greentranserpnative.ui.operation.despatch_manifest.models.ToStationModel
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


    lateinit var linearLayoutManager: LinearLayoutManager
    private var branchList: ArrayList<BranchSelectionModel> = ArrayList()
    private var locationList: ArrayList<PickupLocationModel> = ArrayList()
    private var toStationList: ArrayList<ToStationModel> = ArrayList()
    private var driverList: ArrayList<DriverSelectionModel> = ArrayList()
    private var vendorList: ArrayList<VendorSelectionModel> = ArrayList()
    private var vehicleList: ArrayList<VehicleSelectionModel> = ArrayList()
    private var vehicleTypeList: ArrayList<VehicleTypeModel> = ArrayList()
    private var groupModeList: ArrayList<GroupModeCodeModel> = ArrayList()
    private var modeList: ArrayList<ModeCodeSelectionModel> = ArrayList()

    private val bookingViewModel: BookingViewModel by viewModels()
    private var rvAdapter: SavePickupManifestAdapter? = null
    private var grList: ArrayList<GrSelectionModel> = ArrayList()
    private var contentList: ArrayList<ContentSelectionModel> = ArrayList()
    private var packingList: ArrayList<PackingSelectionModel> = ArrayList()
    private var model: ManifestEnteredDataModel? = null
    var modeTypeList = arrayOf("SURFACE", "AIR")
    var modeTypeCode =""
    var contentCode = ""
    var content = ""
    var enteredPckgs = ""
    var enteredGWeight = ""
    var enteredBalancePckg = ""

    var vendorCode = ""
    var areaCode = ""
    var branchCode = ""
    var vehicleCode = ""
    var groupCode = ""
    var modeCode = ""
    var vehicleCategory = ""
    var loadedByType = ""
    var manifestDt = getSqlCurrentDate()
    var awbDt = getSqlCurrentDate()
    var driverCode = ""
    var destinationCode = ""
    var menuCode = ""




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityDespatchManifestEntryBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        activityBinding.inputDate.setText(getViewCurrentDate())
        activityBinding.inputAirlineDt.setText(getViewCurrentDate())
        activityBinding.inputTime.setText(getSqlCurrentTime())
        activityBinding.inputBranch.setText(userDataModel?.loginbranchname)
        branchCode = userDataModel?.loginbranchcode.toString()
        activityBinding.inputManifestNum.hint = "Enter Manifest #"
        menuCode =
            if (Utils.menuModel == null) " " else Utils.menuModel?.menucode.toString()
        setUpToolbar("Outstation Manifest")
        setObservers()


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

//
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
        viewModel.groupModeLiveData.observe(this) { groupCode ->
            if (groupCode.elementAt(0).commandstatus == 1) {
                groupModeList = groupCode
                openGroupModeSelectionBottomSheet(groupModeList)
            } else {
                errorToast(groupCode.elementAt(0).commandmessage.toString())
            }

        }
        viewModel.modeCodeLiveData.observe(this) { modeCode ->
            if (modeCode.elementAt(0).commandstatus == 1) {
                modeList = modeCode
                openModeCodeSelectionBottomSheet(modeList)
            } else {
//                errorToast(groupCode.elementAt(0).commandmessage.toString())
            }

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
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, modeTypeList)
        activityBinding.selectedModeType.adapter = adapter

        activityBinding.selectedModeType.onItemSelectedListener =
            object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    var value = modeTypeList[position].toString()
                    when(value){
                        "SURFACE"-> run{
                          modeTypeCode ="S"
                            activityBinding.surfaceLayout.visibility=View.VISIBLE
                            activityBinding.airLayout.visibility=View.GONE
                            activityBinding.inputAirline.text= null
                            activityBinding.inputFlight.text= null
                            activityBinding.inputAirlineAwb.text= null
                            activityBinding.inputAwbWeight.text= null
                            activityBinding.inputAirlineDt.text =null
                            modeCode= ""
                            groupCode= ""

                        }
                        "AIR" -> run{
                            modeTypeCode="A"
                            activityBinding.airLayout.visibility=View.VISIBLE
                            activityBinding.surfaceLayout.visibility=View.GONE
                            activityBinding.inputDriverName.text= null
                            driverCode =""
                            activityBinding.inputDriverMobile.text =null
                            activityBinding.inputVehicleNumber.text= null
                            vehicleCode =""
                            activityBinding.inputAirlineDt.setText(getViewCurrentDate())

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


    fun  getDestinationList(){
        viewModel.getToStationList(
            getCompanyId(),
            "greentransapp_StationMast",
            listOf("prmbranchcode","prmusercode","prmcharstr"),
            arrayListOf(getLoginBranchCode(),getUserCode(),""))
    }


    private fun openToStationBottomSheet(rvList: ArrayList<ToStationModel>) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].StnName.toString(), rvList[i]))

        }
        openCommonBottomSheet(this, "Destination Selection", this, commonList)
    }


    private fun openVehicleSelectionBottomSheet(rvList: ArrayList<VehicleSelectionModel>) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].regno, rvList[i]))
        }
        openCommonBottomSheet(this, "Vehicle Selection", this, commonList)
    }

    private fun getGroupModeCode() {
        viewModel.getGroupModeList(
            loginDataModel?.companyid.toString(),
            "greentrans_getmodegroup",
            listOf("prmmodetype"),
            arrayListOf(modeTypeCode)
        )
    }

    private fun openGroupModeSelectionBottomSheet(rvList: ArrayList<GroupModeCodeModel>) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].groupname.toString(), rvList[i]))
        }
        openCommonBottomSheet(this, "Group Mode Selection", this, commonList)
    }

    private fun getModeCodeList() {
        if(activityBinding.inputDestination.text.isNullOrEmpty()){
            errorToast("Please Select Destination")
            return
        }else if(activityBinding.inputAirline.text.isNullOrEmpty()){
            errorToast("Please Select Mode Group First")
            return
        }
        viewModel.getModeCode(
            loginDataModel?.companyid.toString(),
            "greentransapp_showmodeV2",
            listOf("prmorgcode","prmdestcode","prmmodetype","prmmodegroupcode","prmcharstr"),
            arrayListOf(getLoginBranchCode(),areaCode,modeTypeCode,groupCode,"a")
        )
    }

    private fun openModeCodeSelectionBottomSheet(rvList: ArrayList<ModeCodeSelectionModel>) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].regno.toString(), rvList[i]))
        }
        openCommonBottomSheet(this, "Mode Selection", this, commonList)
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

//        activityBinding.inputPickupLocation.setOnClickListener {
//            getPickupLocation()
//
//        }

        activityBinding.inputDriverName.setOnClickListener {
            getDriverList()

        }
        activityBinding.inputVendorName.setOnClickListener {
            getVendorList()
        }
        activityBinding.inputVehicleNumber.setOnClickListener {
            getVehicleList()


        }
        activityBinding.inputDestination.setOnClickListener {
            getDestinationList()
        }
        activityBinding.inputAirline.setOnClickListener {
            getGroupModeCode()
        }
        activityBinding.inputFlight.setOnClickListener {
            getModeCodeList()
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
        activityBinding.inputAirlineDt.setOnClickListener {
            openSingleDatePicker()
        }
        activityBinding.inputTime.setOnClickListener {
            openTimePicker()
        }

        activityBinding.btnGrSelect.setOnClickListener {
           if (!activityBinding.autoManifestCheck.isChecked) {
                if (activityBinding.inputManifestNum.text.isNullOrEmpty()) {
                    Companion.errorToast(this, "Please Enter Manifest Number")
                    return@setOnClickListener
                }
            } else if (modeTypeCode == "S") {
               if (activityBinding.inputDriverName.text.isNullOrEmpty()) {
                   Companion.errorToast(this, "Please select Driver.")
                   return@setOnClickListener

               }  else if (activityBinding.inputVehicleNumber.text.isNullOrEmpty()) {
                   Companion.errorToast(this, "Please select Vehicle Number.")
                   return@setOnClickListener
               }
           }else if(modeTypeCode == "A"){
               if (activityBinding.inputAirline.text.isNullOrEmpty()) {
                   Companion.errorToast(this, "Please select Airline.")
                   return@setOnClickListener

               }  else if (activityBinding.inputAirlineAwb.text.isNullOrEmpty()) {
                   Companion.errorToast(this, "Please select Airline AWB.")
                   return@setOnClickListener
               } else if (activityBinding.inputFlight.text.isNullOrEmpty()) {
                   Companion.errorToast(this, "Please select Flight.")
                   return@setOnClickListener
               }else if (activityBinding.inputAwbWeight.text.isNullOrEmpty()) {
                   Companion.errorToast(this, "Please enter.")
                   return@setOnClickListener
               }
           } else if (activityBinding.inputVendorName.text.isNullOrEmpty()) {
                Companion.errorToast(this, "Please select Vendor.")
                return@setOnClickListener
            }

            val intent = Intent(this, GrSelectionForDespatchManifestActivity::class.java)
            getManifestData()
            startActivity(intent)

        }

    }
    private fun getManifestData() {
        Utils.despatchManifestModel = DespatchManifestEnteredDataModel(
            branchName = activityBinding.inputBranch.text.toString(),
            branchCode = branchCode,
            manifestNo = activityBinding.inputManifestNum.text.toString(),
            tostation=  activityBinding.inputDestination.text.toString(),
            driverCode = driverCode,
            manifestDt = manifestDt,
            manifestTime = activityBinding.inputTime.text.toString(),
            driverName = activityBinding.inputDriverName.text.toString(),
            drivermobile = activityBinding.inputDriverMobile.text.toString(),
            vendorName = activityBinding.inputVendorName.text.toString(),
            vendorCode = vendorCode,
            vehicleNo = activityBinding.inputVehicleNumber.text.toString(),
            vehicleCode = vehicleCode,
            areaCode = areaCode,
            remark = activityBinding.inputRemark.text.toString(),
             groupCode=groupCode,
             modeCode =modeCode,
             pckgs =activityBinding.inputAwbPckgs.text.toString(),
             modeTypeCode=modeTypeCode,
            vendorGr = activityBinding.inputVendorGr.text.toString(),
            awbNo=activityBinding.inputAirlineAwb.text.toString(),
//            awbDt=activityBinding.inputAirlineDt.text.toString(),
            awbDt=awbDt,
            airlineawbpckgs=activityBinding.inputAwbPckgs.text.toString(),
            airlineawbweight=activityBinding.inputAwbWeight.text.toString(),
        )

    }
    override fun onItemClick(data: Any, clickType: String) {
        when (clickType) {
            "Destination Selection" -> run {
                val selectedLocation = data as ToStationModel
                activityBinding.inputDestination.setText(selectedLocation.StnName)
                areaCode = selectedLocation.StnCode.toString()
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
                if (selectedVendor.vendcode ==null){
                    activityBinding.inputLayoutVendorGr.visibility= View.VISIBLE
                }else{
                    vendorCode = selectedVendor.vendcode
                    activityBinding.inputLayoutVendorGr.visibility=View.GONE
                }


//              getVehicleList()


            }
            "Vehicle Selection" -> run {
                val selectedVehicle = data as VehicleSelectionModel
                activityBinding.inputVehicleNumber.setText(selectedVehicle.regno)
                vehicleCode = selectedVehicle.vehiclecode
//                activityBinding.inputCapacity.setText(selectedVehicle.capacity.toString())

            }

            "Group Mode Selection" -> run {
                val selectedGroup = data as GroupModeCodeModel
                activityBinding.inputAirline.setText(selectedGroup.groupname)
                groupCode = selectedGroup.groupcode.toString()
            }
            "Mode Selection" -> run {
                val selectedMode = data as ModeCodeSelectionModel
                activityBinding.inputFlight.setText(selectedMode.regno)
                modeCode = selectedMode.vehiclecode.toString()


            }
        }
    }

    override fun onClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }



}