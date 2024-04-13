package com.greensoft.greentranserpnative.ui.operation.drs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityDrsBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.common.models.CommonBottomSheetModel
import com.greensoft.greentranserpnative.ui.bottomsheet.vehicleSelection.VehicleSelectionBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.vehicleSelection.model.VehicleModelDRS
import com.greensoft.greentranserpnative.ui.bottomsheet.vendorSelection.VendorSelectionBottomSheet
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.drs.model.DrsDataModel
import com.greensoft.greentranserpnative.ui.operation.drs.model.GrDetailModelDRS
import com.greensoft.greentranserpnative.ui.operation.drs.model.SaveDRSModel
import com.greensoft.greentranserpnative.ui.bottomsheet.vendorSelection.model.VendorModelDRS
import com.greensoft.greentranserpnative.ui.operation.drsScan.DrsScanActivity
import com.greensoft.greentranserpnative.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DRSActivity @Inject constructor(): BaseActivity(), OnRowClick<Any>, AlertCallback<Any>,
    BottomSheetClick<Any> {
    private lateinit var activityBinding:ActivityDrsBinding
    private var drsDate = getSqlCurrentDate()
    private var vendorList:ArrayList<VendorModelDRS>  = ArrayList()
    private var vehicleList:ArrayList<VehicleModelDRS> = ArrayList()
    private var grDetailList:ArrayList<GrDetailModelDRS> = ArrayList()
    private var rvAdapter: DRSAdapter?= null
    private var saveDRSModel:SaveDRSModel? = null
    private lateinit var manager:LinearLayoutManager
    private val viewModel: DRSViewModel by viewModels()
    var vendorCode = ""
    var vehicleCode = ""
    var spinnerValue = ""
    private var drsNo:String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityDrsBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("DRS")
        activityBinding.inputDate.setText(getViewCurrentDate())
        activityBinding.inputTime.setText(getSqlCurrentTime())
        activityBinding.deliveryBoyName.setText(userDataModel?.username.toString())
        grDetailList = generateSimpleList()
        setSpinners()
        setOnClick()
        setObservers()
//        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        drsNo = Utils.drsNo
        if (drsNo!=null||drsNo==""){
            getDrsData(drsNo)
        }
    }

    fun getDrsData(drsNo:String?){

    }

    private fun setObservers(){
        mPeriod.observe(this) { date ->
            activityBinding.inputDate.setText(date.viewsingleDate)
            drsDate = date.sqlsingleDate.toString()
        }
        timePeriod.observe(this) { time ->
            activityBinding.inputTime.setText(time)
        }

        viewModel.vendorLiveData.observe(this){vendorData->
            vendorList = vendorData
            openVendorSelectionBottomSheet(vendorList)
        }

//        viewModel.vehicleLiveData.observe(this){vehicleData ->
//            vehicleList = vehicleData
//            openVehicleSelectionBottomSheet(vehicleList)
//        }
        viewModel.saveDRSLiveData.observe(this){saveDRSData->
            saveDRSModel = saveDRSData
        }

    }
    private fun openVendorSelectionBottomSheet(rvList: ArrayList<VendorModelDRS>) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].vendname, rvList[i]))
//            getVehicleList()
        }
        openCommonBottomSheet(this, "Vendor Selection", this, commonList)
    }

    private fun openVehicleSelectionBottomSheet(rvList: ArrayList<VehicleModelDRS>) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].regno, rvList[i]))
//            getVehicleList()
        }
        openCommonBottomSheet(this, "Vehicle Selection", this, commonList)
    }
    private fun setSpinners() {
        val deliveryModeList = listOf("AGENT", "MARKET VEHICLE","JEENA STAFF","OFFICE/AIRPORT DROP")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, deliveryModeList)
        activityBinding.deliveryBy.adapter = adapter

        activityBinding.deliveryBy.onItemSelectedListener =
            object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    spinnerValue = deliveryModeList[position].toString()

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }

    }
    private fun setOnClick(){
        activityBinding.inputDate.setOnClickListener {
            openSingleDatePicker()
        }
        activityBinding.inputTime.setOnClickListener {
            openTimePicker()
        }
        activityBinding.inputVendorName.setOnClickListener {
//            getVendorList()
            vendorBottomSheet(this,"Vendor Selection",this)
        }
        activityBinding.inputVehicleName.setOnClickListener {
//            getVehicleList()
            vehicleBottomSheet(this,"Vehicle Selection",this)
        }
        activityBinding.scanDrsBtn.setOnClickListener {

            val gson = Gson()
            try {
                var model= DrsDataModel(
                    commandstatus = 1,
                    drsdate = drsDate,
                    drstime = activityBinding.inputTime.text.toString(),
                    deliveredby = spinnerValue,
                    vendorname = activityBinding.inputVendorName.text.toString(),
                    vendcode = vendorCode,
                    vehicleno = activityBinding.inputVehicleName.text.toString(),
                    vehiclecode = vehicleCode,
                    driver = activityBinding.deliveryBoyName.text.toString(),
                    remark = activityBinding.inputRemark.text.toString()
                )
                val jsonString = gson.toJson(model)
                val intent = Intent(this, DrsScanActivity::class.java)
                intent.putExtra("DrsModelData", jsonString)
                startActivity(intent)
            } catch (ex: java.lang.Exception) {
                errorToast("Data Conversion Error: " + ex.message)
            }
        }
    }

    private fun getVendorList(){
        viewModel.getVendorList(
            getCompanyId(),
            "a",
            ""
        )
    }

    private fun getVehicleList(){
        viewModel.getVehicleList(
            getCompanyId(),
            getLoginBranchCode(),
            getUserCode(),
            "s",
            "JEENA",
            "",
        )
    }

    private fun saveDRS(){
        viewModel.saveDRS(
            getCompanyId(),
            getLoginBranchCode(),
            drsDt = activityBinding.inputDate.text.toString(),
            drsTime = activityBinding.inputTime.text.toString(),
            "",
            driverName = activityBinding.deliveryBoyName.text.toString(),
            "",
            "A9531",
            remarks = activityBinding.inputRemark.text.toString(),
            "",
            "",
            "",
            "",
            "",
            "",
            getUserCode(),
            getSessionId(),
            "GTAPP_drs",
            userDataModel?.executiveid.toString(),
            deliveredBy = activityBinding.deliveryBy.toString(),
            vendorCode,
            dlvVehicleNo = activityBinding.inputVehicleName.toString(),

        )
    }

//    private fun setupRecyclerView() {
//        if (rvAdapter == null) {
//            manager = LinearLayoutManager(this)
//            activityBinding.recyclerView.layoutManager = manager
//        }
//        rvAdapter = DRSAdapter(grDetailList, this)
//        activityBinding.recyclerView.adapter = rvAdapter
////    }
//    }


    override fun onClick(data: Any, clickType: String) {
        if (clickType==VendorSelectionBottomSheet.VENDOR_CLICK_TYPE){
            try {
                val selectedVendor = data as VendorModelDRS
                activityBinding.inputVendorName.setText(selectedVendor.vendname)
                vendorCode = selectedVendor.vendcode
            } catch (ex:Exception){
                errorToast(ex.message)
            }
        }
        else if(clickType == VehicleSelectionBottomSheet.VEHICLE_CLICK_TYPE) {
            try {
                val selectedVehicle = data as VehicleModelDRS
                activityBinding.inputVehicleName.setText(selectedVehicle.regno)
                vehicleCode = selectedVehicle.vehiclecode
//                successToast("Ho gaya bhai: ${selectedVehicle.regno}" )
            } catch (ex: Exception) {
                errorToast(ex.message)
            }
        } else {
            try {
                val model = data as GrDetailModelDRS
                if (clickType == "REMOVE_GR") {
                    for (i in 0 until grDetailList.size) {
                        if (grDetailList[i].grno == model.grno) {
                            grDetailList.removeAt(i)
                            rvAdapter?.notifyDataSetChanged()

                        }
                    }
                }
            } catch (err: Exception) {
                errorToast("Something went wrong. Please try again. ${err.message}")
            }
        }
    }
    override fun onAlertClick(alertClick: AlertClick, alertCallType: String, data: Any?) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(data: Any, clickType: String) {
        when(clickType){
            "Vendor Selection" ->run {
                val selectedVendor = data as VendorModelDRS
                activityBinding.inputVendorName.setText(selectedVendor.vendname)
                vendorCode = selectedVendor.vendcode
            }
//            "Vehicle Selection" ->run {
//                val selectedVehicle = data as VehicleModelDRS
//                activityBinding.inputVehicleName.setText(selectedVehicle.regno)
//                vehicleCode = selectedVehicle.vehiclecode
//                Toast.makeText(this, "vehicle Number: ${selectedVehicle.regno}", Toast.LENGTH_SHORT).show()
//            }

            }
        }

    private fun vehicleBottomSheet(mContext: Context, title:String, onRowClick: OnRowClick<Any>) {
        val bottomSheetDialog = VehicleSelectionBottomSheet.newInstance(mContext,title,onRowClick)
        bottomSheetDialog.show(supportFragmentManager, VehicleSelectionBottomSheet.TAG)
    }

    private fun vendorBottomSheet(mContext: Context, title:String, onRowClick: OnRowClick<Any>) {
        val bottomSheetDialog = VendorSelectionBottomSheet.newInstance(mContext,title,onRowClick)
        bottomSheetDialog.show(supportFragmentManager, VendorSelectionBottomSheet.TAG)
    }

    private fun generateSimpleList(): ArrayList<GrDetailModelDRS> {
        val dataList: ArrayList<GrDetailModelDRS> =
            java.util.ArrayList<GrDetailModelDRS>()
        for (i in 0..9) {
            dataList.add(GrDetailModelDRS(i,"",1,(100+i).toString()))
        }
        return dataList
    }

    override fun onDestroy() {
        super.onDestroy()
        Utils.drsNo = null
    }
    }