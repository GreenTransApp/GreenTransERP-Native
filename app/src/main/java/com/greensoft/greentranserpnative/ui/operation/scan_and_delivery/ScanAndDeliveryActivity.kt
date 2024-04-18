package com.greensoft.greentranserpnative.ui.operation.scan_and_delivery

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.common.alert.CommonAlert
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.pod_entry.models.PodEntryModel
import com.greensoft.greentranserpnative.ui.operation.pod_entry.models.RelationListModel
import com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.adapter.ScanDeliveryAdapter
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
    var stampRequired =""
    var signRequired =""
    var podImagePath =""
    var signPath =""
    var pckgs =""
    var stickerNumber =""
    var grDt = getSqlCurrentDate()
    var signBitmap: Bitmap? = null
    private var relationList: ArrayList<RelationListModel> = ArrayList()
    private var rvList: ArrayList<ScanStickerModel> = ArrayList()
    private var undeliveredStickerList: ArrayList<ScanStickerModel> = ArrayList()
    private var unDelReasonList: ArrayList<ScanDelReasonModel> = ArrayList()
    private val viewModel: ScanAndDeliveryViewModel by viewModels()

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
        activityBinding.inputDate.setText(getViewCurrentDate())
        activityBinding.inputTime.setText(getSqlCurrentTime())
        activityBinding.stickerGrid.visibility =  View.GONE
        activityBinding.btnUpArrow.visibility=View.GONE
        activityBinding.inputDate.setText(getViewCurrentDate())
        activityBinding.inputTime.setText(getSqlCurrentTime())
        activityBinding.inputDeliveryDt.setText(getViewCurrentDate())
        activityBinding.inputDeliveryTime.setText(getSqlCurrentTime())
        activityBinding.signatureLayout.visibility = View.GONE
        activityBinding.imageLayout.visibility = View.GONE
    }

    private fun saveStickerToPod(stickerNo: String) {
     viewModel.saveScanDeliveryPod(
         companyId =  getCompanyId(),
         userCode = getUserCode(),
         branchCode = getLoginBranchCode(),
         loginBranchCode = getLoginBranchCode(),
         stickerNo = stickerNo,
         menuCode = "GTAPP_DELIVERYNATIVE",
         sessionId = getSessionId()
     )
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
                    relationType = relationList[position].relations.toString()
                    when (relationType) {

                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
    }


    private fun setObservers() {
        mScanner.observe(this) { stickerNo ->
            successToast(stickerNo)

//            stickerNumber = stickerNo
//            setupRecyclerView()
            var stickerAlreadyExists: Boolean = false
            for(scanStickerModel in rvList) {
                if(scanStickerModel.stickerno == stickerNo) {
                    stickerAlreadyExists = true
                    removeStickerAlert(stickerNo)
                    break
                }
            }
            if(!stickerAlreadyExists) {
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
            relationList =List
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

            }

        }

        mPeriod.observe(this) { date ->
            activityBinding.inputDate.setText(date.viewsingleDate)
            grDt = date.sqlsingleDate.toString()
        }
        timePeriod.observe(this) { time ->
            activityBinding.inputTime.setText(time)
        }

        imageClicked.observe(this) { clicked ->
            if(clicked) {
                setPodImage()
            }
        }

        viewModel.saveLiveData .observe(this){data->
            savePodData = data;
            getPodDetails(savePodData?.grno.toString())
            getScanStickerList(savePodData?.grno.toString())
        }


    }




    private fun setupRecyclerView() {
        linearLayoutManager = LinearLayoutManager(this)
        rvAdapter = ScanDeliveryAdapter(rvList, this,this,this)
        activityBinding.recyclerView.layoutManager = linearLayoutManager
        activityBinding.recyclerView.adapter = rvAdapter

    }

    private fun setPodImage(){
        if(imageBase64List.isEmpty() && imageBitmapList.isEmpty()) {
            imageBase64List.add("EMPTY")
            imageBitmapList.add(
                BitmapFactory.decodeResource(mContext.resources,
                R.drawable.baseline_add_a_photo_24))
        }
        activityBinding.podImage.setImageBitmap(imageBitmapList[0]);
        podImagePath= imageBase64List[0].toString()
    }

    private fun setPodDetails(){
        activityBinding.inputGrno.setText(podDetail?.grno.toString())
        activityBinding.inputOrigin.setText(Utils.checkNullOrEmpty(podDetail?.origin.toString()))
        activityBinding.inputDestination.setText(Utils.checkNullOrEmpty(podDetail?.destname.toString()))
        activityBinding.inputBookingDt.setText(Utils.checkNullOrEmpty(podDetail?.grdt.toString()))
        activityBinding.inputBookingTime.setText(Utils.checkNullOrEmpty(podDetail?.picktime.toString()))
        activityBinding.inputArrivalDt.setText(Utils.checkNullOrEmpty(podDetail?.receivedt.toString()))
        activityBinding.inputArrivalTime.setText(Utils.checkNullOrEmpty(podDetail?.receivetime.toString()))
        activityBinding.inputReceiverBy.setText(Utils.checkNullOrEmpty(podDetail?.name.toString()))
        activityBinding.inputReceiverbyMobile.setText(Utils.checkNullOrEmpty(podDetail?.phno.toString()))
        activityBinding.inputRemark.setText(Utils.checkNullOrEmpty(podDetail?.remarks.toString()))
        activityBinding.inputDeliveryBoy.setText(Utils.checkNullOrEmpty(userDataModel?.username.toString()))

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
          activityBinding.inputDeliveryDt.setOnClickListener {
              openSingleDatePicker()
          }
          activityBinding.inputDeliveryTime.setOnClickListener {
              openTimePicker()
          }

          activityBinding.signatureLayout.setOnClickListener {
              val bottomSheet= BottomSheetSignature.newInstance(this, getCompanyId(), this, signBitmap)
              bottomSheet.show(supportFragmentManager, BottomSheetSignature.TAG)
          }
          activityBinding.imageLayout.setOnClickListener{
              showImageDialog()
          }

          activityBinding.signCheck.setOnCheckedChangeListener { _, Bool ->
              if (activityBinding.signCheck.isChecked) {
                  activityBinding.signatureLayout.visibility =View.VISIBLE
                  activityBinding.mainImgLayout.visibility =View.VISIBLE
                  signRequired = "Y"
              }else{
                  activityBinding.signatureLayout.visibility =View.GONE
                  activityBinding.mainImgLayout.visibility =View.GONE
                  signBitmap = null
                  signRequired = "N"
                  activityBinding.signImg.setImageDrawable(getResources().getDrawable(R.drawable.image))
              }
          }
          activityBinding.imageCheck.setOnCheckedChangeListener { buttonView, isChecked ->
              if (activityBinding.imageCheck.isChecked) {
                  activityBinding.imageLayout.visibility = View.VISIBLE
                  activityBinding.mainImgLayout.visibility = View.VISIBLE
                  stampRequired = "Y"
              } else {
                  activityBinding.imageLayout.visibility = View.GONE
                  activityBinding.mainImgLayout.visibility = View.GONE
                  imageBase64List.clear()
                  imageBitmapList.clear()
                  activityBinding.podImage.setImageDrawable(getResources().getDrawable(R.drawable.image))
                  stampRequired = "N"

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
            alertCallType ="REMOVE_STICKER",
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
        undeliveredStickerList.clear()
        rvList.forEachIndexed{index, item ->
            if (rvList[index].scanned == "N"){
                undeliveredStickerList.add(item)
//                navigateToUndeliveredPage(undeliveredStikerList)
            }

        }
        openUndeliveredBottomSheet(mContext,"",this,undeliveredStickerList,unDelReasonList)
    }

    fun openUndeliveredBottomSheet(mContext: Context, title: String, bottomSheetClick: BottomSheetClick<Any>, stickerList: ArrayList<ScanStickerModel>, unDelReasonList: ArrayList<ScanDelReasonModel>) {

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
            if (alertCallType == "REMOVE_STICKER") {
                saveStickerToPod(data.toString())
            } else if (alertCallType == "SAVE_STICKER") {
                saveStickerToPod(data.toString())
            } else if (alertCallType == "START_SCANNING") {

            }
        }
    }

    override fun onItemClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }

    override fun onClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }

    override fun onComplete(clickType: String, imageBitmap: Bitmap) {
        if(clickType == BottomSheetSignature.COMPLETED_CLICK_LISTENER_TAG) {
            signBitmap = imageBitmap
            activityBinding.signImg.setImageBitmap(signBitmap);
            signPath = ImageUtil.convert(signBitmap!!)

        }
    }
}