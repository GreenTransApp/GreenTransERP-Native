package com.greensoft.greentranserpnative.ui.operation.multiple_pod_entry_list

import android.app.AlertDialog
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityMultiplePodEntryListBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.common.models.CommonBottomSheetModel
import com.greensoft.greentranserpnative.ui.bottomsheet.signBottomSheet.BottomSheetSignature
import com.greensoft.greentranserpnative.ui.bottomsheet.signBottomSheet.SignatureBottomSheetCompleteListener
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.multiple_pod_entry_list.adapters.MultiplePodEntryAdapter
import com.greensoft.greentranserpnative.ui.operation.multiple_pod_entry_list.models.RelationListModel
import com.greensoft.greentranserpnative.ui.operation.pending_for_delivery_update_list.models.PodEntryListModel
import javax.inject.Inject

class MultiplePodEntryListActivity  @Inject constructor(): BaseActivity(),
    AlertCallback<Any>, OnRowClick<Any>, SignatureBottomSheetCompleteListener,
    BottomSheetClick<Any> {
        lateinit var  activityBinding:ActivityMultiplePodEntryListBinding
        private val viewModel:MultiplePodEntryViewModel by viewModels()
        private var rvAdapter :MultiplePodEntryAdapter ?= null
    private var relationList: ArrayList<RelationListModel> = ArrayList()
    private var rvList: ArrayList<PodEntryListModel> = ArrayList()
        private lateinit var manager: LinearLayoutManager
    private var drsSelectedData: PodEntryListModel? = null
   var drNo:String =""
   var relationName:String =""
//    var signPath =""
//    var dtDetails :SingleDatePickerWIthViewTypeModel? =null
    var podImagePath =""
    var enteredMobileNum=""
    var enteredReceivedBy=""
//    var signBitmap: Bitmap? = null
    var deliveryDt =""
    var deliveryTime =""
    var currentDt = getViewCurrentDate()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityMultiplePodEntryListBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("Save Multiple POD")
        setObserver()
        getRelationList()
        getDrsDetails()
    }


     private fun setObserver(){
         viewModel.isError.observe(this) { errMsg ->
             errorToast(errMsg)
         }
         viewModel.viewDialogLiveData.observe(this, Observer { show ->

             if (show) {
                 showProgressDialog()
             } else {
                 hideProgressDialog()
             }
         })

         viewModel.relationLiveData.observe(this){ List->
             relationList =List


         }
         imageClicked.observe(this) { clicked ->
             if (clicked) {

             }
         }
         singleDatePeriodWithViewType.observe(this){ periodWithViewType ->
//             dtDetails = mPeriod
             if(rvAdapter != null) {
                 rvAdapter?.setSelectedDt(periodWithViewType.periodSelection, periodWithViewType.index)
                 deliveryDt = periodWithViewType.periodSelection.sqlsingleDate.toString()
             }
         }
         timeSelectionWithViewType.observe(this){ timeWithViewType ->
//             dtDetails = mPeriod
             if(rvAdapter != null) {
                 rvAdapter?.setSelectedTime(timeWithViewType.timeSelection, timeWithViewType.index)
                  deliveryTime = timeWithViewType.timeSelection

             }
         }


     }


    private fun setPodImage(index: Int){
        rvAdapter?.setPodImage(imageBitmapList[0],index)
        podImagePath= imageBase64List[0].toString()
    }


      private fun getDrsDetails(){
          if(intent != null) {
              val jsonString = intent.getStringExtra("ARRAY_JSON")
              if(jsonString != "") {
                  val gson = Gson()
                  val listType = object : TypeToken<List<PodEntryListModel>>() {}.type
                  val resultList: ArrayList<PodEntryListModel> =
                      gson.fromJson(jsonString.toString(), listType)
                  rvList.addAll(resultList)
                  rvList.forEachIndexed { _, element ->
                      drNo=element.drsno.toString()
                      setupRecyclerView()
                  }
              }
          }
    }

    private fun setupRecyclerView() {
        manager = LinearLayoutManager(this)
        rvAdapter = MultiplePodEntryAdapter(rvList, this,this,this
        )
        activityBinding.recyclerView.apply {
            layoutManager = manager
            adapter = rvAdapter
        }
    }



    private fun openRelationSelectionBottomSheet(rvList: ArrayList<RelationListModel>, index: Int) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].relations.toString(), rvList[i]))

        }
        openCommonBottomSheet(this, "Relation Selection", this, commonList, true, index)

    }
    override fun onRowClick(data: Any, clickType: String, index: Int) {
        super.onRowClick(data, clickType, index)
        if (clickType == "RELATION_SELECT") {
            openRelationSelectionBottomSheet(relationList, index)

        } else if(clickType == "SAVE_SELECT") {
//            successToast("test${index}")
            val podCardModel = data as PodEntryListModel
            AlertDialog.Builder(this)
                .setTitle("Alert!!!")
                .setMessage("Are you sure you want to save this POD entry?")
                .setPositiveButton("Yes") { _, _ ->
                    savePod(podCardModel, index)
                }
                .setNeutralButton("No") { _, _ ->


                }
                .show()


        }else if(clickType == "SIGNATURE_SELECT"){
            val model = data as PodEntryListModel
            val bottomSheet= BottomSheetSignature.newInstance(this, getCompanyId(), this, model.signImg)
            bottomSheet.show(supportFragmentManager, BottomSheetSignature.TAG)

        }else if(clickType == "POD_IMAGE_SELECT"){
            showImageDialog()
        } else if(clickType == "DATE_SELECT"){
            openSingleDatePickerWithViewType("DATE_SELECTION",true,index)
        }else if (clickType  == "TIME_SELECT"){
            openTimePickerWithViewType("TIME_SELECTION",true, index)
        }


    }


    override fun onItemClickWithAdapter(data: Any, clickType: String, index: Int) {
        super.onItemClickWithAdapter(data, clickType, index)
        if (clickType == "Relation Selection") {
            val selectedRelation=data as RelationListModel
            rvAdapter?.setRelation(selectedRelation, index)
            relationName=selectedRelation.relations.toString()

//        }else if (clickType == "DATE_SELECTION"){
//            val selectedDt=data as PodEntryListModel
//            rvAdapter?.setSelectedDt(selectedDt, index)
//            var dt = selectedDt.poddt.toString()
        }
    }
    override fun onAlertClick(alertClick: AlertClick, alertCallType: String, data: Any?) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(data: Any, clickType: String) {

    }
    override fun onClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }

    override fun onSignCompleteWithAdapter(clickType: String, imageBitmap: Bitmap, index: Int) {
        super.onSignCompleteWithAdapter(clickType, imageBitmap, index)
        if(clickType == BottomSheetSignature.COMPLETED_CLICK_LISTENER_TAG) {
//            signBitmap = imageBitmap
//            signPath = ImageUtil.convert(signBitmap!!)
            rvAdapter?.setSignatureImage(imageBitmap, index)
        }
    }

    fun getEnteredMobileNo(mobileNo:String){
        enteredMobileNum = mobileNo
    }

     fun getEnterReceivedBy(receivedBy:String){
         enteredReceivedBy = receivedBy
     }

      fun getRelationList(){
        viewModel.getRelation(
            companyId = getCompanyId()
        )
    }

    private fun savePod(podCardModel: PodEntryListModel, index: Int) {
        var grNo: String = podCardModel.grno.toString()
        var podTime: String = podCardModel.dlvtime.toString()
        var PodDt: String = podCardModel.dlvdt.toString()

        var stampRequired: String = podCardModel.stampRequired.toString()
        var signRequired: String = podCardModel.signRequired.toString()
        var remark: String = ""
        var dataIdStr = "";
        var enteredQtyStr = "";
        var savingSignImage: String = if(signRequired == "Y" && podCardModel.signImgBase64 != null) podCardModel.signImgBase64!! else ""
        var savingPodImage: String = if(stampRequired == "Y" && podCardModel.podImgBase64 != null) podCardModel.podImgBase64!! else ""
//        var adapterEnteredData: PodEntryListModel? = rvAdapter?.getEnteredData(index)

        if (podCardModel.mobileno.isNullOrEmpty()) {
            errorToast("Mobile No. Not Entered at INPUT - ${index + 1}")
            return
        } else if(podCardModel.receivedby.isNullOrEmpty()){
            errorToast(" received By Not Entered at INPUT - ${index + 1}")
            return
        }else if(podCardModel.relation.isNullOrEmpty()){
            errorToast(" relation Not Selected at INPUT - ${index + 1}")
            return
        }else if(podCardModel.dlvtime.isNullOrEmpty()){
            errorToast(" delivery time Not Selected at INPUT - ${index + 1}")
            return
        }else if(podCardModel.podImg == null && stampRequired == "Y"){
            errorToast(" pod image not attached at INPUT - ${index + 1}")
            return
        }else if(podCardModel.signImg == null && signRequired == "Y"){
            errorToast(" sign image not attached at INPUT - ${index + 1}")
            return
        }


        viewModel.savePodEntry(
            companyId =getCompanyId(),
            loginBranchCode= getLoginBranchCode(),
            grNo = grNo,
            dlvTime = deliveryTime,
            name =enteredReceivedBy,
            dlvDt =deliveryDt,
            relation = relationName,
            phoneNo = enteredMobileNum,
            sign =signRequired ,
            stamp = stampRequired,
            podImage = savingPodImage,
            signImage = savingSignImage,
            remarks ="",
            userCode = getUserCode(),
            sessionId =getSessionId(),
            delayReason = "",
            menuCode = "GTAPP_PODENTRY",
            deliveryBoy ="",
            boyId = getExecutiveId(),
            podDt = getSqlCurrentDate(),

            pckgs = "",
            pckgsStr = enteredQtyStr,
            dataIdStr = dataIdStr

        )
        }
    }

