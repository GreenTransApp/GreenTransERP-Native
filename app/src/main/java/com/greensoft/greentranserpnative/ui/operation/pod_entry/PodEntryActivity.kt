package com.greensoft.greentranserpnative.ui.operation.pod_entry

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityPodEntryBinding
import com.greensoft.greentranserpnative.model.ImageUtil
import com.greensoft.greentranserpnative.ui.bottomsheet.signBottomSheet.BottomSheetSignature
import com.greensoft.greentranserpnative.ui.bottomsheet.signBottomSheet.SignatureBottomSheetCompleteListener
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.common.alert.CommonAlert
import com.greensoft.greentranserpnative.ui.home.HomeActivity
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.despatch_manifest.models.DespatchSaveModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.GrSelectionActivity
import com.greensoft.greentranserpnative.ui.operation.pod_entry.models.PodEntryModel
import com.greensoft.greentranserpnative.ui.operation.pod_entry.models.PodSaveModel
import com.greensoft.greentranserpnative.ui.operation.pod_entry.models.RelationListModel
import com.greensoft.greentranserpnative.ui.operation.pod_entry.models.getGrNoModel
import com.greensoft.greentranserpnative.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PodEntryActivity  @Inject constructor(): BaseActivity(), OnRowClick<Any>, SignatureBottomSheetCompleteListener,
    AlertCallback<Any> {
    lateinit var  activityBinding:ActivityPodEntryBinding
    private var podDetail: PodEntryModel? = null
    private var podSaveModel: PodSaveModel? = null
    var relationType = ""
    var stampRequired =""
    var signRequired =""
    var podImagePath =""
    var signPath =""
    var pckgs =""
    var alreadyExistGr:Boolean = false
    var dlvSqlDt=""


    private var relationList: ArrayList<RelationListModel> = ArrayList()
    private val viewModel: PodEntryViewModel by viewModels()
    var podDt = getSqlCurrentDate()
    var signBitmap: Bitmap? = null
    lateinit var bottomSheetDialog: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding= ActivityPodEntryBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("POD Entry")
        activityBinding.inputDate.setText(getViewCurrentDate())
        activityBinding.inputTime.setText(getSqlCurrentTime())
        activityBinding.inputDeliveryDt.setText(getViewCurrentDate())
        activityBinding.inputDeliveryTime.setText(getSqlCurrentTime())
        dlvSqlDt = getSqlCurrentDate()
        activityBinding.signatureLayout.visibility = View.GONE
        activityBinding.imageLayout.visibility = View.GONE
        setOnClick()
        setObservers()
        setSpinner()

        getRelationList()
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
                    mScanner.observe(this) { stickerNo ->
//                        successToast(stickerNo)
                        getGrNumberFromStickerNo(stickerNo)

                    }

                    viewModel.podGrNoLiveData.observe(this){data->
                        data.grno?.let { grNo ->
                            activityBinding.inputGrno.setText(grNo)
                            getGrDetails(data.grno)
                        }
                    }
                    mPeriod.observe(this) { date ->
                    activityBinding.inputDate.setText(date.viewsingleDate)
                    podDt = date.sqlsingleDate.toString()
                    }
                    timePeriod.observe(this) { time ->
                        activityBinding.inputTime.setText(time)
                    }

//                    mPeriod.observe(this) { date ->
//                    activityBinding.inputDeliveryDt.setText(date.viewsingleDate)
//
//                    }
//                    timePeriod.observe(this) { time ->
//                        activityBinding.inputDeliveryTime.setText(time)
//                    }


                    viewModel.podDetailsLiveData.observe(this) { data ->
                     podDetail = data
                     alreadyExistGr = data.commandstatus != 1
                     setPodDetails()
                    }
                    viewModel.relationLiveData.observe(this){ List->
                        relationList =List
                        val relationAdapter =
                            ArrayAdapter(this, android.R.layout.simple_list_item_1,relationList )
                        activityBinding.inputRelation.adapter = relationAdapter

                    }

                    imageClicked.observe(this) { clicked ->
                        if(clicked) {
                            setPodImage()
                        }
                    }
                viewModel.saveLiveData.observe(this){savePod->
                    podSaveModel = savePod
                    if(podSaveModel?.commandstatus ==1){
                        showSuccessAlert(savePod)
                    }else{
                        errorToast(podSaveModel?.commandmessage.toString())
                    }
                }


    }


     private fun getGrNumberFromStickerNo(stickerNo:String){
       viewModel.getGrFromSticker(
           companyId = getCompanyId(),
           userCode = getUserCode(),
           branchCode = getLoginBranchCode(),
           menuCode = "GTAPP_DELIVERYNATIVE",
           stickerNo = stickerNo,
           sessionId = getSessionId()
       )
     }

     private fun setOnClick(){
         activityBinding.btnGrProceed.setOnClickListener {
             getGrDetails(activityBinding.inputGrno.text.toString())
         }
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
                 activityBinding.mainImgLayout.visibility=View.VISIBLE
                 signRequired = "Y"

             }else{
                 activityBinding.mainImgLayout.visibility=View.GONE
                 activityBinding.signatureLayout.visibility =View.GONE
                 signBitmap = null
                 signRequired = "N"
                 activityBinding.signImg.setImageDrawable(getResources().getDrawable(R.drawable.image))
             }
         }
         activityBinding.imageCheck.setOnCheckedChangeListener { buttonView, isChecked ->
             if (activityBinding.imageCheck.isChecked) {
                 activityBinding.imageLayout.visibility = View.VISIBLE
                 activityBinding.mainImgLayout.visibility=View.VISIBLE
                 stampRequired = "Y"
             } else {
                 activityBinding.imageLayout.visibility = View.GONE
                 activityBinding.signatureLayout.visibility =View.GONE
                 imageBase64List.clear()
                 imageBitmapList.clear()
                 activityBinding.podImage.setImageDrawable(getResources().getDrawable(R.drawable.image))
                 stampRequired = "N"

             }

         }

          activityBinding.btnSavePod.setOnClickListener {
              alertForSavePod()
          }
     }


     private  fun alertForSavePod(){
//         if (!alreadyExistGralreadyExistGr){
//             if(activityBinding.inputGrno.text.isNullOrEmpty()){
//                 errorToast("Please enter GR#.")
//                 return
//             }
//         }else
             if(activityBinding.inputDeliveryTime.text.isNullOrEmpty()){
             errorToast("Please select delivery time.")
              return
         }else if(signBitmap == null){
             errorToast("Signature not added.")
             return
         }else if(podImagePath == null|| podImagePath == ""){
             errorToast("POD Image not added.")
           return
         }

         AlertDialog.Builder(this)
             .setTitle("Alert!!!")
             .setMessage("Are you sure you want to save this POD entry?")
             .setPositiveButton("Yes") { _, _ ->
                 savePod()
             }
             .setNeutralButton("No") { _, _ -> }
             .show()

     }
     private fun setPodImage(){
//         if(imageBase64List.isEmpty() && imageBitmapList.isEmpty()) {
//             imageBase64List.add("EMPTY")
//             imageBitmapList.add(BitmapFactory.decodeResource(mContext.resources,
//                 R.drawable.baseline_add_a_photo_24))
//         }
         activityBinding.podImage.setImageBitmap(imageBitmapList[0]);
         podImagePath= imageBase64List[0].toString()
     }





     private fun setPodDetails(){
//activityBinding.inputDate.setText(podDetail?.dlvdt.toString())
//activityBinding.inputTime.setText(podDetail?.dlvtime.toString())
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
pckgs= podDetail?.pckgs.toString()





     }

    override fun onClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }


    private  fun getRelationList(){
        viewModel.getRelation(
            companyId = getCompanyId()
        )
    }
    private fun getGrDetails(grNo:String){
        if(activityBinding.inputGrno.text.isNullOrEmpty()){
            errorToast("Please enter GR#.")
            return
        }
        viewModel.getPodDetails(
            companyId = getCompanyId(),
            grNo = grNo
//            grNo = activityBinding.inputGrno.text.toString()
        )
    }

    private fun savePod(){

        var dataIdStr = "";
        var enteredQtyStr = "";
        viewModel.savePodEntry(
            companyId =getCompanyId(),
            loginBranchCode= getLoginBranchCode(),
            grNo = activityBinding.inputGrno.text.toString(),
            dlvTime = activityBinding.inputDeliveryTime.text.toString(),
            name = activityBinding.inputReceiverBy.text.toString(),
            dlvDt =dlvSqlDt,
            relation = relationType,
            phoneNo = activityBinding.inputReceiverbyMobile.text.toString(),
            sign = signRequired ,
            stamp = stampRequired,
            podImage = podImagePath,
            signImage = signPath,
            remarks =activityBinding.inputRemark.text.toString(),
            userCode = getUserCode(),
            sessionId =getSessionId(),
            delayReason = "",
            menuCode = "GTAPP_PODENTRY",
            deliveryBoy = activityBinding.inputDeliveryBoy.text.toString().uppercase(),
            boyId = getExecutiveId(),
            podDt = podDt,
            pckgs = pckgs,
            pckgsStr = enteredQtyStr,
            dataIdStr = dataIdStr

        )
    }

    override fun onComplete(clickType: String, imageBitmap: Bitmap) {
        if(clickType == BottomSheetSignature.COMPLETED_CLICK_LISTENER_TAG) {
            signBitmap = imageBitmap
            activityBinding.signImg.setImageBitmap(signBitmap);
            signPath = ImageUtil.convert(signBitmap!!)

        }
    }




    private fun showSuccessAlert(model: PodSaveModel) {
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

    override fun onAlertClick(alertClick: AlertClick, alertCallType: String, data: Any?) {
        if(alertCallType =="SAVE_POD"){
            val intent=Intent(this, HomeActivity::class.java)
            startActivity(intent)

        }
    }
}