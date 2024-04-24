package com.greensoft.greentranserpnative.ui.operation.despatch_manifest

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivitySaveDespatchManifestBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.common.models.CommonBottomSheetModel
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.common.alert.CommonAlert
import com.greensoft.greentranserpnative.ui.home.HomeActivity
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.booking.BookingViewModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.ContentSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.PackingSelectionModel
import com.greensoft.greentranserpnative.ui.operation.despatch_manifest.adapters.SaveDespatchManifestAdapter
import com.greensoft.greentranserpnative.ui.operation.despatch_manifest.models.DespatchManifestEnteredDataModel
import com.greensoft.greentranserpnative.ui.operation.despatch_manifest.models.DespatchSaveModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.GrSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.LoadingListModel
import com.greensoft.greentranserpnative.utils.Utils
import com.greensoft.greentranserpnative.utils.Utils.checkNullOrEmpty
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SaveDespatchManifestActivity @Inject constructor() : BaseActivity(), OnRowClick<Any>,
    BottomSheetClick<Any>, AlertCallback<Any> {
    lateinit var  activityBinding:ActivitySaveDespatchManifestBinding
    lateinit var linearLayoutManager: LinearLayoutManager
    private val viewModel: DespatchManifestViewModel by viewModels()
    private val bookingViewModel: BookingViewModel by viewModels()
    private var rvAdapter: SaveDespatchManifestAdapter? = null
    private var rvList: ArrayList<LoadingListModel> = ArrayList()
    private var contentList: ArrayList<ContentSelectionModel> = ArrayList()
    private var packingList: ArrayList<PackingSelectionModel> = ArrayList()
    private var model: DespatchManifestEnteredDataModel? = null
    var contentCode=""
    var content=""
    var enteredPckgs=""
    var enteredGWeight=""
    var enteredBalancePckg=""
    var menuCode="GTAPP_LONGROUTEMANIFEST"
    var vehicleTypeTxt=""
            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
                activityBinding= ActivitySaveDespatchManifestBinding.inflate(layoutInflater)
                setContentView(activityBinding.root)
                model= Utils.despatchManifestModel
                setObserver()
                setSupportActionBar(activityBinding.toolBar.root)
                setUpToolbar("SAVE OUTSTATION MANIFEST")
                setOnClick()
                setEnteredManifestData()
                getContentList()
                getpackingLov()
    }
    override fun onResume() {
        super.onResume()
        getIntentData()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.loading_slip_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.loading_slip -> {
                showAlert()
//
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun getContentList() {
        bookingViewModel.getContentLov(
            loginDataModel?.companyid.toString(),
            "greentrans_showgoodsLOV",
            listOf(),
            arrayListOf()
        )
    }

    private fun setEnteredManifestData(){
        if (model!!.modeTypeCode.toString()=="S"){
            activityBinding.selectedModeType.setText("SURFACE")
            activityBinding.surfaceLayout.visibility=View.VISIBLE
            activityBinding.airLayout.visibility = View.GONE
        }else if(model!!.modeTypeCode.toString()=="A"){
            activityBinding.selectedModeType.setText("AIR")
            activityBinding.surfaceLayout.visibility=View.GONE
            activityBinding.airLayout.visibility = View.VISIBLE
        }else if(model?.vendorGr != null){
            activityBinding.inputVendorGr.visibility = View.VISIBLE
            activityBinding.inputVendorGr.setText(model?.vendorGr.toString())
        }
        activityBinding.inputBranch.setText(model!!.branchName.toString())
        activityBinding.inputManifestNum.setText(model!!.manifestNo.toString())
        activityBinding.inputDestination.setText(model!!.tostation.toString())
        activityBinding.inputDate.setText(model!!.manifestDt.toString())
        activityBinding.inputTime.setText(model!!.manifestTime.toString())
        activityBinding.inputDriverName.setText(model!!.driverName.toString())
        activityBinding.inputDriverMobile.setText(model!!.drivermobile.toString())
        activityBinding.inputVendorName.setText(model!!.vendorName.toString())
        activityBinding.inputVehicleNumber.setText(model!!.vehicleNo.toString())
        activityBinding.inputRemark.setText(model!!.remark.toString())
        activityBinding.inputAirlineAwb.setText(model!!.awbNo.toString())
        activityBinding.inputFlight.setText(model?.flight.orEmpty())
        activityBinding.inputAirlineAwb.setText(model?.groupName.orEmpty())
        activityBinding.inputAirlineDt.setText(model?.awbViewDt.orEmpty())
        activityBinding.inputAwbWeight.setText(model?.airlineawbweight.toString())


    }
    private fun openContentSelectionBottomSheet(rvList: ArrayList<ContentSelectionModel>, index: Int) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].itemname, rvList[i]))

        }
        openCommonBottomSheet(this, "Content Selection", this, commonList, true, index)

    }

    private fun getpackingLov() {
        bookingViewModel.getPackingLov(
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
    private  fun showAlert(){
        CommonAlert.createAlert(
            context = this,
            header = "Alert!!",
            description = " Are You Sure You Want To Select Loading Slip  Again",
            callback =this,
            alertCallType ="RESELECT_SLIP",
            data = ""
        )
    }



    private fun setupRecyclerView() {
        linearLayoutManager = LinearLayoutManager(this)
        rvAdapter = SaveDespatchManifestAdapter(this,this,rvList, this)
        activityBinding.recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = rvAdapter
        }


    }
    private fun setObserver(){
//         activityBinding.refreshLayout.setOnRefreshListener {
//             refreshData()
//
//         }
        viewModel.isError.observe(this) { errMsg ->
            errorToast(errMsg)
        }

        viewModel.viewDialogLiveData.observe(this,) { show ->

            if(show) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }
        bookingViewModel.contentLiveData.observe(this) { itemData ->
            contentList = itemData
        }
        bookingViewModel.packingLiveData.observe(this) { packing ->
            packingList = packing
        }
        viewModel.manifestLiveData.observe(this){manifest->

            if(manifest != null) {
                if(manifest.commandstatus==1){
                    showManifestCreatedAlert(manifest)

                    successToast(manifest.commandmessage.toString())
                }else{
                    errorToast(manifest.commandmessage.toString())
                }
            }


        }
//         manifestLiveData.observe(this){data->
//             successToast(data.toString())
//         }
    }

    private fun showManifestCreatedAlert(model: DespatchSaveModel) {
        AlertDialog.Builder(this)
            .setTitle("Success!!!")
            .setMessage(model.commandmessage)
            .setPositiveButton("Okay") { _, _ ->
                goToHomeActivity()
                finish()
            }
            .show()
    }
    private fun goToHomeActivity(){
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun getIntentData() {
//        if(grList.isNotEmpty()) grList.clear()
        if(intent != null) {
            val jsonString = intent.getStringExtra("ARRAY_JSON")
            if(jsonString != "") {
                val gson = Gson()
                val listType = object : TypeToken<List<LoadingListModel>>() {}.type
                val resultList: ArrayList<LoadingListModel> =
                    gson.fromJson(jsonString.toString(), listType)
                rvList.addAll(resultList)
                setupRecyclerView()
            }
        }
    }

    fun getPckgsValue(pckgs: Int){
        enteredPckgs=pckgs.toString()
    }

    fun getBalancepckg(balancePckg:Int){
        enteredBalancePckg=balancePckg.toString()
    }
    fun getGWeightValue(gWeight:Double){
        enteredGWeight=gWeight.toString()
    }


    override fun onItemClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }

    override fun onClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }

    override fun onRowClick(data: Any, clickType: String, index: Int) {
        super.onRowClick(data, clickType, index)
        if (clickType == "CONTENT_SELECT") {
            openContentSelectionBottomSheet(contentList, index)

        }else if(clickType =="PACKING_SELECT"){
            openPackingSelectionBottomSheet(packingList,index)

        }else if(clickType =="REMOVE_SELECT"){

            val removingItem: GrSelectionModel = data as GrSelectionModel

            if(rvList.isNotEmpty() && index <= rvList.size - 1) {
                rvList.removeAt(index)
                setupRecyclerView()
                successToast("Successfully Removed. GR #: ${removingItem.grno}")
            } else {
                errorToast("Something went wrong.")
            }

        }

    }



    override fun onItemClickWithAdapter(data: Any, clickType: String, index: Int) {
        super.onItemClickWithAdapter(data, clickType, index)
//        if (clickType == "Content Selection") {
//            val selectedContent=data as ContentSelectionModel
//            rvAdapter?.setContent(selectedContent, index)
//            contentCode=selectedContent.itemcode
//            content=selectedContent.itemname

//        }else if(clickType=="Packing Selection"){
//            val selectedPckg=data as PackingSelectionModel
//            rvAdapter?.setPacking(selectedPckg, index)
//
//        }
    }
    override fun onAlertClick(alertClick: AlertClick, alertCallType: String, data: Any?) {
        try {
            when (alertClick) {
                AlertClick.YES -> {
                    if (alertCallType == "RESELECT_SLIP") {
                        val intent= Intent(this, GrSelectionForDespatchManifestActivity::class.java)
                        startActivity(intent)
                    }else if(alertCallType =="SELECT_SAVE"){
                            saveOutstationManifest()

                    }else if(alertCallType == "SELECT_REMOVE"){

                    }

                }
                AlertClick.NO -> {
                }


            }
        } catch (ex: Exception) {
            errorToast(ex.message)
        }
    }
    private fun setOnClick(){

        activityBinding.btnSave.setOnClickListener {
            showAlertOnSave()
        }
    }

    private  fun showAlertOnSave(){
        CommonAlert.createAlert(
            context = this,
            header = "Alert!!",
            description = " Are You Sure You Want To Save Manifest ?",
            callback =this,
            alertCallType ="SELECT_SAVE",
            data = ""
        )
    }

     private fun saveOutstationManifest(){
         var actualLoadingNo: String = ""
         var actualPacking: String = "~"
         var actualAWeight: String = "~"
         var actualContent: String = "~"
         var actualModeCode: String = ""
         var adapterEnteredData: ArrayList<LoadingListModel>? = rvAdapter?.getEnteredData()
         run enteredData@{
             adapterEnteredData?.forEachIndexed { index, model ->
//                 if (Utils.isDecimalNotEntered(model.aweight.toString())) {
//                     errorToast("GWeight Not Entered at INPUT - ${index + 1}")
//                     return
//                 } else if (Utils.isDecimalNotEntered(model.pckgs.toString())) {
//                     errorToast("Pckgs Not Entered at INPUT - ${index + 1}")
//                     return
//                 }else if (Utils.isStringNotEntered(model.goods.toString())) {
//                     errorToast("Content Not Entered at INPUT - ${index + 1}")
//                     return
//                 }
             }
         }
         for(i in 0 until rvList.size){

             val loadingNo = rvList[i].loadingno.toString()
//             val packing = grList[i].packing.toString()
//             val content = grList[i].goods.toString()
//             val aWeight = grList[i].aweight.toString()

             actualLoadingNo += "$loadingNo~"
//             actualPacking += "$packing~"
//             actualContent += "$content~"
//             actualAWeight += "$aWeight~"
         }
         if(checkNullOrEmpty(model?.modeCode) == "NOT AVAILABLE"){
             actualModeCode = model?.vehicleCode.toString()
         } else {
            actualModeCode = model?.modeCode.toString()
         }

         viewModel.saveOutstationManifest(
             companyid = getCompanyId(),
             branchcode= getLoginBranchCode(),
             dt= model?.manifestDt.toString(),
             time= model?.manifestTime.toString(),
             manifestno= model?.manifestNo.toString(),
             tost= model?.tostation.toString(),
             modetype= model?.modeTypeCode.toString(),
//             modecode= model?.modeCode ?: model?.vehicleCode.toString(),
             modecode= actualModeCode,
             remarks= model?.remark.toString(),
             drivercode= model?.driverCode.toString(),
             drivermobileno= model?.drivermobile.toString(),
             vendcode= model?.vendorCode.toString(),
             loadingnostr= actualLoadingNo,
             sessionid= getSessionId(),
             usercode= getUserCode(),
             menucode= "GTAPP_LONGROUTEMANIFEST",
             loadedby= userDataModel?.username.toString(),
             airlineawbno= model?.awbNo.toString(),
             airlineawbdt= model?.awbDt.toString(),
             airlineawbfreight= "0",
             airlineawbpckgs= model?.airlineawbpckgs.toString(),
             airlineawbweight= model?.airlineawbweight.toString(),
             vendorcd=model?.vendorGr.toString()
         )

     }

}