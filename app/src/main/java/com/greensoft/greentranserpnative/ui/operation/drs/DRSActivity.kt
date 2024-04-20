package com.greensoft.greentranserpnative.ui.operation.drs

import android.content.Context
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
import com.greensoft.greentranserpnative.ui.bottomsheet.drsScanBottomSheet.DrsScanBottomSheet
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
import com.greensoft.greentranserpnative.ui.common.alert.CommonAlert
import com.greensoft.greentranserpnative.ui.operation.drs.model.DeliverByModel
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
    var deliverByCode = "M"
    private var drsNo:String? = ""
    private val deliveryModeList: ArrayList<DeliverByModel> = ArrayList()

    companion object {
        const val COMPLETE_DRS_TAG = "COMPLETE_DRS_TAG"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityDrsBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("DRS")
        deliveryModeList.add(DeliverByModel(name = "AGENT", code = "V"))
        deliveryModeList.add(DeliverByModel(name = "MARKET VEHICLE", code = "M"))
        deliveryModeList.add(DeliverByModel(name = "JEENA STAFF", code = "S"))
        deliveryModeList.add(DeliverByModel(name = "OFFICE / AIRPORT DROP", code = "O"))
        activityBinding.inputDate.setText(getViewCurrentDate())
//        activityBinding.inputTime.setText(getSqlCurrentTime())
        activityBinding.deliveryBoyName.setText(userDataModel?.username.toString())
        setSpinners()
        setOnClick()
        setObservers()
//        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        drsNo = Utils.drsNo
        if (drsNo!=null && drsNo!=""){
            getDrsData(drsNo)
        }
    }

    private fun getDrsData(drsNo:String?){
        if(drsNo == "" || drsNo == null) {
            return
        }
        viewModel.getDrsData(
            getCompanyId(),
            getUserCode(),
            getLoginBranchCode(),
            drsNo,
            getSessionId()
        )
    }

    private fun setObservers(){
        mScanner.observe(this) { scanSticker ->
            DrsScanBottomSheet.sendStickerToFragment(scanSticker)
        }
        viewModel.drsPreFillLiveData.observe(this) { saveDRSModel ->
            activityBinding.inputDate.setText(saveDRSModel.drsdate)
            activityBinding.inputTime.setText(saveDRSModel.drstime)
            activityBinding.inputVendorName.setText(saveDRSModel.vendorname)
            activityBinding.inputVehicleName.setText(saveDRSModel.vehicleno)
            activityBinding.inputRemark.setText(saveDRSModel.remarks)
        }
        viewModel.isError.observe(this) { errMsg ->
            errorToast(errMsg)
        }
        viewModel.viewDialogLiveData.observe(this) { show ->
//            progressBar.visibility = if(show) View.VISIBLE else View.GONE
            if (show) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }

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

    private fun changeVisibilityCompleteBtn(showBtn: Boolean) {
        activityBinding.completeBtn.visibility = if(showBtn) View.VISIBLE else View.GONE
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
//        val deliveryModeList = listOf("AGENT", "MARKET VEHICLE","JEENA STAFF","OFFICE/AIRPORT DROP")
//        val deliveryModeList = listOf("JEENA STAFF")
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
                    deliverByCode = deliveryModeList[position].code
                    vendorCode = ""
                    vehicleCode = ""
                    when(deliverByCode) {
                        "V" -> run {
                            activityBinding.inputVendorName.setText("")
                            activityBinding.inputVehicleName.setText("")
                            activityBinding.agentVendorHeader.setText("Select Agent")
                            activityBinding.agentVendorPlaceholder.setHint("Select Agent")
                            activityBinding.inputLayoutVendor.visibility = View.VISIBLE
                            activityBinding.inputLayoutVehicleNumber.visibility = View.GONE

                        }
                        "M" -> run {
                            activityBinding.inputVendorName.setText("")
                            activityBinding.agentVendorHeader.setText("Select Vendor")
                            activityBinding.agentVendorPlaceholder.setHint("Select Vendor")
                            activityBinding.inputLayoutVendor.visibility = View.VISIBLE
                            activityBinding.inputLayoutVehicleNumber.visibility = View.VISIBLE
                        }
                        "S" -> run {
                            activityBinding.inputVehicleName.setText("")
                            activityBinding.inputVendorName.setText("")
                            activityBinding.inputLayoutVendor.visibility = View.GONE
                            activityBinding.inputLayoutVehicleNumber.visibility = View.GONE
                            activityBinding.inputLayoutVehicleNumber.visibility = View.GONE
                        }
                        "O" -> run {
                            activityBinding.inputVehicleName.setText("")
                            activityBinding.inputVendorName.setText("")
                            activityBinding.inputLayoutVendor.visibility = View.GONE
                            activityBinding.inputLayoutVehicleNumber.visibility = View.GONE
                            activityBinding.inputLayoutVehicleNumber.visibility = View.GONE
                        }
                        else -> {

                        }
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }


        activityBinding.deliveryBy.setSelection(1)
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
            validateSelectedDataForScan()
        }
        activityBinding.completeBtn.setOnClickListener {
            if(drsNo == "") {
                errorToast("Please scan some stickers to save this DRS!!!")
            } else {
                showCompleteAlert()
            }
        }
    }

    private fun validateSelectedDataForScan() {
        val drsTime = activityBinding.inputTime.text.toString()
        if(drsTime == null || drsTime == "") {
            errorToast("Please select time.")
            return
        }
        when(deliverByCode) {
            "V" -> run {
                if(vendorCode == "") {
                    errorToast("Please select agent.")
                } else {
                    openScanDrsBottomSheet()
                }
            }
            "M" -> run {
                if(vendorCode == "") {
                    errorToast("Please select vendor.")
                } else if(vehicleCode == "") {
                    errorToast("Please select vehicle.")
                } else {
                    openScanDrsBottomSheet()
                }
            }
            "S" -> run {
                openScanDrsBottomSheet()
            }
            "O" -> run {
                openScanDrsBottomSheet()
            }
            else -> {

            }
        }
    }

    private fun openScanDrsBottomSheet() {

        val gson = Gson()
        try {
            var model= DrsDataModel(
                commandstatus = 1,
                commandmessage = "",
                drsdate = drsDate,
                drstime = activityBinding.inputTime.text.toString(),
                deliveredby = deliverByCode,
                vendorname = activityBinding.inputVendorName.text.toString(),
                vendcode = vendorCode,
                vehicleno = activityBinding.inputVehicleName.text.toString(),
                vehiclecode = vehicleCode,
                username = activityBinding.deliveryBoyName.text.toString(),
                usercode = getUserCode(),
                dlvagentcode = vendorCode,
                remarks = activityBinding.inputRemark.text.toString()
            )
//                val jsonString = gson.toJson(model)
//                val intent = Intent(this, DrsScanActivity::class.java)
//                intent.putExtra("DrsModelData", jsonString)
//                startActivity(intent)
            val drsScanBottomSheet = DrsScanBottomSheet.newInstance(
                mContext = this,
                drsNo = drsNo,
                drsDetails = model,
                onBottomSheetClick = this
            )
            drsScanBottomSheet.show(supportFragmentManager, DrsScanBottomSheet.TAG)
        } catch (ex: java.lang.Exception) {
            errorToast("Data Conversion Error: " + ex.message)
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

//    private fun saveDRS(){
//        viewModel.saveDRS(
//            getCompanyId(),
//            getLoginBranchCode(),
//            drsDt = activityBinding.inputDate.text.toString(),
//            drsTime = activityBinding.inputTime.text.toString(),
//            "",
//            driverName = activityBinding.deliveryBoyName.text.toString(),
//            "",
//            "A9531",
//            remarks = activityBinding.inputRemark.text.toString(),
//            "",
//            "",
//            "",
//            "",
//            "",
//            "",
//            getUserCode(),
//            getSessionId(),
//            "GTAPP_drs",
//            userDataModel?.executiveid.toString(),
//            deliveredBy = activityBinding.deliveryBy.toString(),
//            vendorCode,
//            dlvVehicleNo = activityBinding.inputVehicleName.toString(),
//
//        )
//    }

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
                vendorCode = selectedVendor . vendcode
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
        when(alertClick) {
            AlertClick.YES -> run {
                when(alertCallType) {
                    COMPLETE_DRS_TAG -> run {
                        if(drsNo == null || drsNo == "") {
                            errorToast("Please scan some stickers before completing.")
                        } else {
                            finish()
                        }
                    }
                    else -> {

                    }
                }
            }
            AlertClick.NO -> run {

            }
            else -> {

            }
        }
    }

    override fun onItemClick(data: Any, clickType: String) {
        when(clickType){
            "Vendor Selection" ->run {
                val selectedVendor = data as VendorModelDRS
                activityBinding.inputVendorName.setText(selectedVendor.vendname)
                vendorCode = selectedVendor.vendcode
            }
            DrsScanBottomSheet.DRS_NO_SAVED_TAG -> run {
                val drsNo = data as String
                this.drsNo = drsNo
                Utils.logger(classLoader.javaClass.name, "DRS # created $drsNo")
                changeVisibilityCompleteBtn(true)
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

    private fun vendorBottomSheet(mContext: Context, title: String, onRowClick: OnRowClick<Any>) {
        val bottomSheetDialog = VendorSelectionBottomSheet.newInstance(mContext,title,onRowClick,if(deliverByCode == "M") "VEHICLE VENDOR" else "PUD VENDOR")
        bottomSheetDialog.show(supportFragmentManager, VendorSelectionBottomSheet.TAG)
    }

    private  fun showCompleteAlert(){
        CommonAlert.createAlert(
            context = this,
            header = "Complete Alert!!",
            description = "Are you sure to save this DRS?",
            callback =this,
            alertCallType = COMPLETE_DRS_TAG,
            data = drsNo
        )
    }

//    private fun generateSimpleList(): ArrayList<GrDetailModelDRS> {
//        val dataList: ArrayList<GrDetailModelDRS> =
//            java.util.ArrayList<GrDetailModelDRS>()
//        for (i in 0..9) {
//            dataList.add(GrDetailModelDRS(i,"",1,(100+i).toString()))
//        }
//        return dataList
//    }


    }