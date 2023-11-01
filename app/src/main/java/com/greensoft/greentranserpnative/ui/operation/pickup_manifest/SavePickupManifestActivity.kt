package com.greensoft.greentranserpnative.ui.operation.pickup_manifest

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivitySavePickupManifestBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.common.models.CommonBottomSheetModel
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.common.alert.CommonAlert
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.booking.BookingViewModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.ContentSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.PackingSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.adapter.SavePickupManifestAdapter
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.GrSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.ManifestEnteredDataModel
import com.greensoft.greentranserpnative.utils.Utils

import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SavePickupManifestActivity @Inject constructor() : BaseActivity(), OnRowClick<Any>, BottomSheetClick<Any>, AlertCallback<Any> {
    lateinit var  activityBinding: ActivitySavePickupManifestBinding
    lateinit var linearLayoutManager: LinearLayoutManager
    private val viewModel: PickupManifestViewModel by viewModels()
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
    var menuCode="GTAPP_NATIVEPICKUPMANIFEST"
    var vehicleTypeTxt=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding=ActivitySavePickupManifestBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        model=Utils.manifestModel
        setObserver()
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("SAVE PICKUP MANIFEST")
        setOnClick()
        setEnteredManifestData()
        getContentList()
        getpackingLov()
//        searchItem()

//        Utils.manifestList.forEachIndexed{index, element ->
//
//        }
//         model=getManifestData()


    }



    override fun onResume() {
        super.onResume()
        getIntentData()
//        refreshData()
    }
//    private fun refreshData(){
////        grList.clear()
////        setupRecyclerView()
//        lifecycleScope.launch {
//            getIntentData()
//            delay(1000)
////            activityBinding.refreshLayout.isRefreshing=false
//        }
//    }

//    fun searchItem(): Boolean {
//        activityBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                rvAdapter?.filter?.filter(query)
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                rvAdapter?.filter?.filter(newText)
//                return false
//            }
//
//        })
//        return true
//    }
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
         if (model!!.loadedBy.toString()=="C"){
             activityBinding.selectedLoadedBy.setText("CUSTOMER")
         }else if(model!!.loadedBy.toString()=="S"){
             activityBinding.selectedLoadedBy.setText("SELF")
         }
         activityBinding.inputBranch.setText(model!!.branchName.toString())
         activityBinding.inputManifestNum.setText(model!!.manifestNo.toString())
         activityBinding.inputPickupLocation.setText(model!!.pickupLocation.toString())
         activityBinding.inputDate.setText(model!!.manifestDt.toString())
         activityBinding.inputTime.setText(model!!.manifestTime.toString())
         activityBinding.inputDriverName.setText(model!!.driverName.toString())
         activityBinding.inputDriverMobile.setText(model!!.drivermobile.toString())
         activityBinding.inputVendorName.setText(model!!.vendorName.toString())
         activityBinding.inputVehicleNumber.setText(model!!.vehicleNo.toString())
         activityBinding.inputCapacity.setText(model!!.capacity.toString())
         activityBinding.inputRemark.setText(model!!.remark.toString())
         if(model!!.vehicleType.toString()== "O"){
             activityBinding.selectedVehicleType.setText("OWN")
         }else if (model!!.vehicleType.toString()== "A"){
             activityBinding.selectedVehicleType.setText("ATTACHED")
         }else if (model!!.vehicleType.toString()== "M"){
             activityBinding.selectedVehicleType.setText("MARKET")
         }

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
        rvAdapter = SavePickupManifestAdapter(this,this,grList, this@SavePickupManifestActivity)
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

         }
//         manifestLiveData.observe(this){data->
//             successToast(data.toString())
//         }
     }

    private fun getIntentData() {
//        if(grList.isNotEmpty()) grList.clear()
        if(intent != null) {
            val jsonString = intent.getStringExtra("ARRAY_JSON")
            if(jsonString != "") {
                val gson = Gson()
                val listType = object : TypeToken<List<GrSelectionModel>>() {}.type
                val resultList: ArrayList<GrSelectionModel> =
                    gson.fromJson(jsonString.toString(), listType)
                grList.addAll(resultList)
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
//            grList.clear()
            val removingItem: GrSelectionModel = data as GrSelectionModel
//            CommonAlert.createAlert(
//                context = this,
//                header = "Alert!!",
//                description = " Are You Sure You Want To Remove GR #: ${removingItem.grno}",
//                callback =this,
//                alertCallType ="SELECT_REMOVE",
//                data = ""
//            )
//
            if(grList.isNotEmpty() && index <= grList.size - 1) {
                grList.removeAt(index)
                setupRecyclerView()
                successToast("Successfully Removed. GR #: ${removingItem.grno}")
            } else {
                errorToast("Something went wrong.")
            }

        }

    }

 private  fun removeAt(index:Int){
     if(grList.isNotEmpty() && index <= grList.size - 1) {
                grList.removeAt(index)
                setupRecyclerView()
//                successToast("Successfully Removed. GR #: ${removingItem.grno}")
            } else {
                errorToast("Something went wrong.")
            }

     }


    override fun onItemClickWithAdapter(data: Any, clickType: String, index: Int) {
        super.onItemClickWithAdapter(data, clickType, index)
        if (clickType == "Content Selection") {
            val selectedContent=data as ContentSelectionModel
            rvAdapter?.setContent(selectedContent, index)
            contentCode=selectedContent.itemcode
            content=selectedContent.itemname

        }else if(clickType=="Packing Selection"){
            val selectedPckg=data as PackingSelectionModel
            rvAdapter?.setPacking(selectedPckg, index)

        }
    }
    override fun onAlertClick(alertClick: AlertClick, alertCallType: String, data: Any?) {
        try {
            when (alertClick) {
                AlertClick.YES -> {
                    if (alertCallType == "RESELECT_SLIP") {
                        val intent=Intent(this,GrSelectionActivity::class.java)
                         startActivity(intent)
                    }else if(alertCallType =="SELECT_SAVE"){
//                        successToast("testing")
                        savePickupManifest()
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
    private fun savePickupManifest(){

        var actualGrNo: String = ""
        var actualPacking: String = ""
        var actualAWeight: String = ""
        var actualContent: String = ""
        var adapterEnteredData: ArrayList<GrSelectionModel>? = rvAdapter?.getEnteredData()
        run enteredData@{
            adapterEnteredData?.forEachIndexed { index, model ->
                if (Utils.isDecimalNotEntered(model.aweight.toString())) {
                    errorToast("GWeight Not Entered at INPUT - ${index + 1}")
                    return
                } else if (Utils.isDecimalNotEntered(model.pckgs.toString())) {
                    errorToast("Pckgs Not Entered at INPUT - ${index + 1}")
                    return
                }else if (Utils.isStringNotEntered(model.goods.toString())) {
                    errorToast("Content Not Entered at INPUT - ${index + 1}")
                    return
                }
            }
        }
    for(i in 0 until grList.size){
        val grNo = grList[i].grno.toString()
        val packing = grList[i].packing.toString()
        val content = grList[i].goods.toString()
        val aWeight = grList[i].aweight.toString()

        actualGrNo += "$grNo~"
        actualPacking += "$packing~"
        actualContent += "$content~"
        actualAWeight += "$aWeight~"
    }

         viewModel.savePickupManifest(
             companyId=userDataModel!!.companyid.toString(),
             branchcode = userDataModel?.loginbranchcode.toString(),
             manifestdt =model!!.manifestDt.toString(),
             time = model!!.manifestTime.toString(),
             manifestno = model!!.manifestNo.toString(),
             modecode = model!!.vehicleCode.toString(),
             tost = model!!.branchCode.toString(),
             drivercode = model!!.driverCode.toString(),
             drivermobileno = model!!.drivermobile.toString(),
             loadedby = model!!.loadedBy.toString(),
             remarks = model!!.remark.toString(),
             ispickupmanifest = "Y",
             grno = actualGrNo,
             pckgs = enteredPckgs,
             aweight = enteredGWeight ,
             goods = actualContent,
             packing =actualPacking,
             areacode = model!!.areaCode.toString(),
             vendcoe = model!!.vendorCode.toString(),
             loadedbytype = model!!.loadedBy.toString(),
             menucode = menuCode,
             usercode = userDataModel!!.usercode.toString(),
             sessionid = userDataModel!!.sessionid.toString(),
             paymentnotapplicable = "N",
             skipinscan = "N"
             )
     }
}