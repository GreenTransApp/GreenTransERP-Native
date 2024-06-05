package com.greensoft.greentranserpnative.ui.operation.scan_and_delivery

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityScanAndDeliveryBinding
import com.greensoft.greentranserpnative.model.ImageUtil
import com.greensoft.greentranserpnative.ui.bottomsheet.signBottomSheet.BottomSheetSignature
import com.greensoft.greentranserpnative.ui.bottomsheet.signBottomSheet.SignatureBottomSheetCompleteListener
import com.greensoft.greentranserpnative.ui.bottomsheet.undeliveredPodBottomSheet.UndeliveredScanPodBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.undeliveredPodBottomSheet.models.UndeliveredEnteredDataModel
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.common.alert.CommonAlert
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.pod_entry.models.PodEntryModel
import com.greensoft.greentranserpnative.ui.operation.multiple_pod_entry_list.models.RelationListModel
import com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.adapter.ScanDeliveryAdapter
import com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.models.SavePodWithStickersModel
import com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.models.ScanDelReasonModel
import com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.models.ScanDeliverySaveModel
import com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.models.ScanStickerModel
import com.greensoft.greentranserpnative.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ScanAndDeliveryActivity @Inject constructor() : BaseActivity(), OnRowClick<Any>,
    BottomSheetClick<Any>, AlertCallback<Any>, SignatureBottomSheetCompleteListener{
    lateinit var  activityBinding:ActivityScanAndDeliveryBinding
    lateinit var linearLayoutManager: LinearLayoutManager
    private var podDetail: PodEntryModel? = null
    private var savePodData: ScanDeliverySaveModel? = null
    private var rvAdapter: ScanDeliveryAdapter? = null
    var relationType = ""
    var podImagePath =""
    var signPath =""
    var grNo = ""
    var dlvDtViewDt = getViewCurrentDate()
    var dlvDtSqlDt = getSqlCurrentDate()
    var dlvTime = ""
//    var dlvTime = getSqlCurrentTime()
    var signBitmap: Bitmap? = null
    private var selectedRelation: RelationListModel? = null
    private var relationList: ArrayList<RelationListModel> = ArrayList()
    private var rvList: ArrayList<ScanStickerModel> = ArrayList()
    private var unDelReasonList: ArrayList<ScanDelReasonModel> = ArrayList()
    private val viewModel: ScanAndDeliveryViewModel by viewModels()

    companion object {
        val REMOVE_STICKER_ALERT_TAG: String = "REMOVE_STICKER_ALERT_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityScanAndDeliveryBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
//        startScanningAlert()
        setInitData()
        setObservers()
        setOnClick()
        setSpinner()
        getRelationList()
        getScanDelReasonList()
    }

    private fun setInitData(){
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("Scan And Delivery")
        activityBinding.inputDate.setText(dlvDtViewDt)
//        activityBinding.inputTime.setText(dlvTime)
        activityBinding.stickerGrid.visibility =  View.GONE
        activityBinding.btnUpArrow.visibility=View.GONE
//        activityBinding.inputDate.setText(getViewCurrentDate())
//        activityBinding.inputTime.setText(getSqlCurrentTime())
//        activityBinding.inputDeliveryDt.setText(getViewCurrentDate())
//        activityBinding.inputDeliveryTime.setText(getSqlCurrentTime())
        activityBinding.signatureLayout.visibility = View.GONE
        activityBinding.imageLayout.visibility = View.GONE

        activityBinding.inputReceiverBy.filters = Utils.allCapsInput(activityBinding.inputReceiverBy)
    }

    private fun saveStickerToPod(stickerNo: String) {
     viewModel.updateStickerForPod(
         companyId =  getCompanyId(),
         userCode = getUserCode(),
         branchCode = getLoginBranchCode(),
         loginBranchCode = getLoginBranchCode(),
         stickerNo = stickerNo,
         menuCode = "GTAPP_DELIVERYNATIVE",
         sessionId = getSessionId(),
         grNo = grNo
     )
    }

    override fun onRowClick(data: Any, clickType: String, index: Int) {
        super.onRowClick(data, clickType, index)
        when(clickType) {
            ScanDeliveryAdapter.REMOVE_STICKER_RV_TAG -> {
                try {
                    val scanStickerModel: ScanStickerModel = data as ScanStickerModel
                    removeStickerAlert(scanStickerModel.stickerno.toString())
                } catch (ex: Exception) {
                    errorToast(ex.message)
                }
            }
        }
    }
    private fun setSpinner(){
        activityBinding.inputRelation.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedRelation = relationList[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
    }


    private fun setObservers() {
        mScanner.observe(this) { stickerNo ->
//            successToast(stickerNo)

//            stickerNumber = stickerNo
//            setupRecyclerView()
            var stickerAlreadyExists: Boolean = false
            for(scanStickerModel in rvList) {
                if(scanStickerModel.stickerno == stickerNo && scanStickerModel.scanned == "Y") {
                    stickerAlreadyExists = true
                    break
                }
            }
            if(stickerAlreadyExists) {
                removeStickerAlert(stickerNo)
            } else {
                saveStickerToPod(stickerNo)
            }
        }
        viewModel.unDelReasonList.observe(this) { reasonList ->
            unDelReasonList = reasonList
        }
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

        viewModel.podDetailsLiveData.observe(this) { data ->
            podDetail = data
            if(data.commandstatus != 1){
                errorToast(data.commandmessage.toString());
            }else{
                setPodDetails()
            }


        }
        viewModel.relationLiveData.observe(this){ List->
            relationList = List
            if(relationList.size > 0) {
                selectedRelation = relationList[0]
            }
            val relationAdapter =
                ArrayAdapter(this, android.R.layout.simple_list_item_1,relationList )
            activityBinding.inputRelation.adapter = relationAdapter

        }
        viewModel.stickerLiveData.observe(this){sticker->
            rvList = sticker
            setupRecyclerView()
            if(rvList.size > 0){
                activityBinding.stickerGrid.visibility =  View.VISIBLE
                activityBinding.btnUpArrow.visibility=View.VISIBLE
                activityBinding.btnDownArrow.visibility= View.GONE
                activityBinding.inputGrno.setText(rvList[0].grno.toString())

            }

        }

        mPeriod.observe(this) { date ->
            dlvDtViewDt = date.viewsingleDate.toString()
            dlvDtSqlDt = date.sqlsingleDate.toString()
            activityBinding.inputDate.setText(date.viewsingleDate)
        }
        timePeriod.observe(this) { time ->
            dlvTime = time
            activityBinding.inputTime.setText(time)
        }

        imageClicked.observe(this) { clicked ->
            if(clicked && imageBitmapList.size > 0) {
                activityBinding.podImage.setImageBitmap(imageBitmapList[0]);
                podImagePath= imageBase64List[0]
            }
        }

        viewModel.updateStickerLiveData.observe(this){ data->
            savePodData = data;
            grNo = savePodData?.grno.toString()
            getPodDetails(savePodData?.grno.toString())
            getScanStickerList(savePodData?.grno.toString())
        }

        viewModel.savePodWithStickersLiveData.observe(this) { savePod ->
            if(savePod.commandstatus == 1) {
                successToast("SUCCESS")
                showPODCreatedAlert(savePod)
            }
        }


    }

    private fun showPODCreatedAlert(model: SavePodWithStickersModel) {
        AlertDialog.Builder(this)
            .setTitle("Success!!!")
            .setMessage(model.commandmessage)
            .setPositiveButton("Okay") { _, _ ->
                finish()
            }
            .show()
    }




    private fun setupRecyclerView() {
        linearLayoutManager = LinearLayoutManager(this)
        rvAdapter = ScanDeliveryAdapter(rvList, this,this,this)
        activityBinding.recyclerView.layoutManager = linearLayoutManager
        activityBinding.recyclerView.adapter = rvAdapter

    }


    private fun setPodDetails(){
        activityBinding.inputGrno.setText(podDetail?.grno.toString())
//        activityBinding.inputOrigin.setText(Utils.checkNullOrEmpty(podDetail?.origin.toString()))
//        activityBinding.inputDestination.setText(Utils.checkNullOrEmpty(podDetail?.destname.toString()))
//        activityBinding.inputBookingDt.setText(Utils.checkNullOrEmpty(podDetail?.grdt.toString()))
//        activityBinding.inputBookingTime.setText(Utils.checkNullOrEmpty(podDetail?.picktime.toString()))
//        activityBinding.inputArrivalDt.setText(Utils.checkNullOrEmpty(podDetail?.receivedt.toString()))
//        activityBinding.inputArrivalTime.setText(Utils.checkNullOrEmpty(podDetail?.receivetime.toString()))
//        activityBinding.inputReceiverBy.filters = Utils.allCapsInput(activityBinding.inputReceiverBy)
//        activityBinding.inputRemark.setText(Utils.checkNullOrEmpty(podDetail?.remarks.toString()))
//        activityBinding.inputDeliveryBoy.setText(Utils.checkNullOrEmpty(userDataModel?.username.toString()))

//        activityBinding.inputReceiverBy.setText(Utils.checkNullOrEmpty(podDetail?.name.toString()))
//        activityBinding.inputReceiverbyMobile.setText(Utils.checkNullOrEmpty(podDetail?.phno.toString()))
    }
      @SuppressLint("UseCompatLoadingForDrawables")
      private  fun setOnClick(){
          activityBinding.btnDownArrow.setOnClickListener{
              activityBinding.stickerGrid.visibility =  View.VISIBLE
              activityBinding.btnUpArrow.visibility=View.VISIBLE
              activityBinding.btnDownArrow.visibility= View.GONE
          }
          activityBinding.btnUpArrow.setOnClickListener {
              activityBinding.stickerGrid.visibility =  View.GONE
              activityBinding.btnDownArrow.visibility=View.VISIBLE
              activityBinding.btnUpArrow.visibility= View.GONE
          }
//          activityBinding.btnGrProceed.setOnClickListener {
//              getPodDetails()
//              getScanStickerList()
//          }
          activityBinding.inputDate.setOnClickListener {
              openSingleDatePicker()
          }
          activityBinding.inputTime.setOnClickListener {
              openTimePicker()
          }
//          activityBinding.inputDeliveryDt.setOnClickListener {
//              openSingleDatePicker()
//          }
//          activityBinding.inputDeliveryTime.setOnClickListener {
//              openTimePicker()
//          }

          activityBinding.signatureLayout.setOnClickListener {
              val bottomSheet= BottomSheetSignature.newInstance(this, getCompanyId(), this, signBitmap)
              bottomSheet.show(supportFragmentManager, BottomSheetSignature.TAG)
          }
          activityBinding.imageLayout.setOnClickListener{
              imageBase64List.clear()
              imageBitmapList.clear()
              showImageDialog()
          }

          activityBinding.signCheck.setOnCheckedChangeListener { _, Bool ->
              if (activityBinding.signCheck.isChecked) {
                  activityBinding.signatureLayout.visibility =View.VISIBLE
                  activityBinding.mainImgLayout.visibility =View.VISIBLE
              }else{
                  activityBinding.signatureLayout.visibility =View.GONE
                  activityBinding.mainImgLayout.visibility =View.GONE
                  activityBinding.signImg.setImageDrawable(Utils.getResourcesDrawable(mContext, R.drawable.image))
//                  activityBinding.signImg.setImageDrawable(getResources().getDrawable(R.drawable.image, null))
              }
          }
          activityBinding.imageCheck.setOnCheckedChangeListener { buttonView, isChecked ->
              if (activityBinding.imageCheck.isChecked) {
                  activityBinding.imageLayout.visibility = View.VISIBLE
                  activityBinding.mainImgLayout.visibility = View.VISIBLE
              } else {
                  activityBinding.imageLayout.visibility = View.GONE
                  activityBinding.mainImgLayout.visibility = View.GONE
                  activityBinding.podImage.setImageDrawable(Utils.getResourcesDrawable(mContext, R.drawable.image))

              }

          }
          activityBinding.btnSaveSelect.setOnClickListener {
              finalSaveWithValidation()
          }


      }
    private  fun getRelationList(){
        viewModel.getRelation(
            companyId = getCompanyId()
        )
    }

     private fun getScanStickerList(grNumber: String){
         viewModel.getStickerList(
             companyId = getCompanyId(),
             userCode = getUserCode(),
             loginBranchCode = getLoginBranchCode(),
             branchCode = getLoginBranchCode(),
             menuCode = "SCAN_DELIVERY",
             grNo = grNumber,
//             grNo = activityBinding.inputGrno.text.toString(),
             sessionId =getSessionId()
         )
     }
    private fun getPodDetails(grNumber:String){
//        if(activityBinding.inputGrno.text.isNullOrEmpty()){
//            errorToast("Please enter GR#.")
//            return
//        }
        viewModel.getPodDetails(
            companyId = getCompanyId(),
//            grNo = activityBinding.inputGrno.text.toString()
            grNo = grNumber
        )
    }

    private fun getScanDelReasonList() {
        viewModel.getDelReasonList(
            loginDataModel?.companyid.toString(),
            "gtapp_undlvreason",
            listOf("prmusercode","prmloginbranchcode","prmsessionid"),
            arrayListOf(getUserCode(),getLoginBranchCode(),getSessionId())
        )
    }



    private  fun removeStickerAlert (stickerNo: String){
        CommonAlert.createAlert(
            context = this,
            header = "Alert!!",
            description = " Are You Sure You Want To Remove This Sticker?",
            callback =this,
            alertCallType = REMOVE_STICKER_ALERT_TAG,
            data =  stickerNo
        )
    }
//private  fun savePodStickerAlert (stickerNo: String){
//        CommonAlert.createAlert(
//            context = this,
//            header = "Alert!!",
//            description = " Are You Sure You Want To Save This POD?",
//            callback =this,
//            alertCallType ="SAVE_STICKER",
//            data =  stickerNo
//        )
//    }


    private fun finalSaveWithValidation(){
        val isSignChecked = activityBinding.signCheck.isChecked
        val isPodChecked = activityBinding.imageCheck.isChecked
        val inputTime = activityBinding.inputTime.text.toString()
        val signImage = signPath
        val podImage = podImagePath
        val receiveBy: String = activityBinding.inputReceiverBy.text.toString()
        val mobileNo: String = activityBinding.inputReceiverbyMobile.text.toString()

        if(grNo.isBlank()) {
            errorToast("Please scan a sticker to make the POD.")
        } else if(inputTime.isBlank()) {
            errorToast("Please enter Delivery Time.")
        } else if(receiveBy.isBlank()) {
            errorToast("Please enter Receive By.")
        } else if(mobileNo.isBlank()) {
            errorToast("Please enter Mobile No.")
        } else if(isSignChecked && signImage.isBlank()) {
            errorToast("Please add your Signature.")
        } else if(isPodChecked && podImage.isBlank()) {
            errorToast("Please add POD Image.")
        } else if(selectedRelation == null) {
            errorToast("Select Relation.")
        } else {
            var undeliveredStickerList: ArrayList<ScanStickerModel> = ArrayList()
            rvList.forEachIndexed { index, item ->
                if (rvList[index].scanned == "N") {
                    undeliveredStickerList.add(item)
//                navigateToUndeliveredPage(undeliveredStikerList)
                }

            }
            if (undeliveredStickerList.size > 0) {
                openUndeliveredBottomSheet(
                    mContext,
                    "Not-Delivered Stickers",
                    this,
                    undeliveredStickerList,
                    unDelReasonList
                )
            } else {
                savePodWithStickers("", "")
            }
        }
    }

    private fun openUndeliveredBottomSheet(mContext: Context, title: String, bottomSheetClick: BottomSheetClick<Any>, stickerList: ArrayList<ScanStickerModel>, unDelReasonList: ArrayList<ScanDelReasonModel>) {

        val bottomSheetDialog = UndeliveredScanPodBottomSheet.newInstance(mContext, title, bottomSheetClick, stickerList,unDelReasonList)

        bottomSheetDialog.show(supportFragmentManager, UndeliveredScanPodBottomSheet.TAG)
    }

    fun navigateToUndeliveredPage(item:ArrayList<ScanStickerModel>){
        val gson = Gson()
        val jsonString = gson.toJson(item)
        val intent = Intent(this, ScanAndUndeliveredActivity::class.java)
        intent.putExtra("ARRAY_JSON", jsonString)

        startActivity(intent)
        finish()
    }
    private  fun startScanningAlert(){
        CommonAlert.createAlert(
            context = this,
            header = "Alert!!",
            description = "Please Start Scanning.",
            callback =this,
            alertCallType ="START_SCANNING",
            data =  ""
        )
    }

    override fun onAlertClick(alertClick: AlertClick, alertCallType: String, data: Any?) {
        if(alertClick == AlertClick.YES) {
            if (alertCallType == REMOVE_STICKER_ALERT_TAG) {
                saveStickerToPod(data.toString())
//            } else if (alertCallType == "SAVE_STICKER") {
//                saveStickerToPod(data.toString())
//            } else if (alertCallType == "START_SCANNING") {

            }
        }
    }

    override fun onItemClick(data: Any, clickType: String) {
        when (clickType) {
            UndeliveredScanPodBottomSheet.UNDELIVERED_SAVE_CLICK_TAG -> {
                try {
                    val undeliveredDataModel = data as UndeliveredEnteredDataModel
//                    Utils.logger(javaClass.toString(), "{\n'Un-Sticker: ${undeliveredDataModel.unDelStickerStr}'\n'Reason: ${undeliveredDataModel.unDelReasonStr}\n}'")
                    savePodWithStickers(undeliveredDataModel.unDelStickerStr, undeliveredDataModel.unDelReasonStr)
                } catch (ex: Exception) {
                    errorToast(ex.message)
                }

            }
        }
    }

    override fun onClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }

    override fun onSignComplete(clickType: String, imageBitmap: Bitmap) {
        if(clickType == BottomSheetSignature.COMPLETED_CLICK_LISTENER_TAG) {
            signBitmap = imageBitmap
            signPath = ImageUtil.convert(signBitmap!!)
            activityBinding.signImg.setImageBitmap(signBitmap);

        }
    }

    private fun savePodWithStickers(unDelStickerStr: String, unDelReasonStr: String) {
        val isSignChecked = activityBinding.signCheck.isChecked
        val isPodChecked = activityBinding.imageCheck.isChecked
        val inputTime = activityBinding.inputTime.text.toString()
        val signImage = signPath
        val podImage = podImagePath
        val receiveBy: String = activityBinding.inputReceiverBy.text.toString()
        val mobileNo: String = activityBinding.inputReceiverbyMobile.text.toString()

        if(grNo.isBlank()) {
            errorToast("Please scan a sticker to make the POD.")
        } else if(inputTime.isBlank()) {
            errorToast("Please enter Delivery Time.")
        } else if(receiveBy.isBlank()) {
            errorToast("Please enter Receive By.")
        } else if(mobileNo.isBlank()) {
            errorToast("Please enter Mobile No.")
        } else if(isSignChecked && signImage.isBlank()) {
            errorToast("Please add your Signature.")
        } else if(isPodChecked && podImage.isBlank()) {
            errorToast("Please add POD Image.")
        } else if(selectedRelation == null) {
            errorToast("Select Relation.")
        } else {
//            if(ENV.DEBUGGING) {
//                errorToast("ENV.DEBUGGING AT POD SAVE")
//                Log.d(this.localClassName, unDelStickerStr)
//                Log.d(this.localClassName, unDelReasonStr)
//                return
//            }
            viewModel.savePodWithStickers(
                getCompanyId(),
                podImagePath,
                signPath,
                getUserCode(),
                getLoginBranchCode(),
                grNo,
                dlvDtSqlDt,
                dlvTime,
                receiveBy,
                selectedRelation?.relations,
                mobileNo,
                if(isSignChecked) "Y" else "N",
                if(isPodChecked) "Y" else "N",
                "",
                "",
                getSessionId(),
                Utils.menuModel?.menucode,
                unDelStickerStr,
                unDelReasonStr
            )
        }
    }
}