package com.greensoft.greentranserpnative.ui.operation.pod_entry

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.compose.ui.text.toUpperCase
import androidx.databinding.adapters.ViewBindingAdapter.setOnClick
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityPodEntryBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.signBottomSheet.BottomSheetSignature
import com.greensoft.greentranserpnative.ui.bottomsheet.signBottomSheet.SignatureBottomSheetCompleteListener
import com.greensoft.greentranserpnative.ui.common.cameraX.Cropper
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.eway_bill.EwayBillBottomSheet
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.PickupManifestViewModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.BranchSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.ManifestEnteredDataModel
import com.greensoft.greentranserpnative.ui.operation.pod_entry.models.PodEntryModel
import com.greensoft.greentranserpnative.ui.operation.pod_entry.models.RelationListModel
import com.greensoft.greentranserpnative.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PodEntryActivity  @Inject constructor(): BaseActivity(), OnRowClick<Any>, SignatureBottomSheetCompleteListener {
    lateinit var  activityBinding:ActivityPodEntryBinding
    private var podDetail: PodEntryModel? = null
    var relationType = ""
    var stampRequired =""
    var signRequired =""
    var podImagePath =""


    private var relationList: ArrayList<RelationListModel> = ArrayList()
    private val viewModel: PodEntryViewModel by viewModels()
    var grDt = getSqlCurrentDate()
    var signBitmap: Bitmap? = null
    lateinit var bottomSheetDialog: BottomSheetDialog;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding= ActivityPodEntryBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("POD Entry")
        activityBinding.inputDate.setText(getViewCurrentDate())
        activityBinding.inputTime.setText(getSqlCurrentTime())
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

                    mPeriod.observe(this) { date ->
                    activityBinding.inputDate.setText(date.viewsingleDate)
                    grDt = date.sqlsingleDate.toString()
                }
                    timePeriod.observe(this) { time ->
                        activityBinding.inputTime.setText(time)
                    }
         viewModel.podDetailsLiveData.observe(this) { data ->
             podDetail = data
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

    }


     private fun setOnClick(){
         activityBinding.btnGrProceed.setOnClickListener {
             getPodDetails()
         }
         activityBinding.inputDate.setOnClickListener {
             openSingleDatePicker()
         }
         activityBinding.inputTime.setOnClickListener {
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
                 signRequired = "Y"
             }else{
                 activityBinding.signatureLayout.visibility =View.GONE
                 signBitmap = null
                 signRequired = "N"
                 activityBinding.signImg.setImageDrawable(null)
             }
             activityBinding.imageCheck.setOnCheckedChangeListener { buttonView, isChecked ->
                 if (activityBinding.imageCheck.isChecked) {
                     activityBinding.imageLayout.visibility = View.VISIBLE
                     stampRequired = "Y"
                 } else {
                     activityBinding.imageLayout.visibility = View.GONE
                     imageBase64List.clear()
                     imageBitmapList.clear()
                     activityBinding.podImage.setImageDrawable(null)
                     stampRequired = "N"

                 }

             }

         }
     }

     private fun setPodImage(){
         if(imageBase64List.isEmpty() && imageBitmapList.isEmpty()) {
             imageBase64List.add("EMPTY")
             imageBitmapList.add(BitmapFactory.decodeResource(mContext.resources,
                 R.drawable.baseline_add_a_photo_24))
         }
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





     }

    override fun onClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }


    private  fun getRelationList(){
        viewModel.getRelation(
            companyId = getCompanyId()
        )
    }
    private fun getPodDetails(){
        if(activityBinding.inputGrno.text.isNullOrEmpty()){
            errorToast("Please enter GR#.")
            return
        }
        viewModel.getPodDetails(
            companyId = getCompanyId(),
            grNo = activityBinding.inputGrno.text.toString()
        )
    }

    private fun savePod(){
        viewModel.savePodEntry(
            companyId =getCompanyId(),
            loginBranchCode= getLoginBranchCode(),
            grNo = activityBinding.inputGrno.text.toString(),
            dlvTime = activityBinding.inputDeliveryTime.text.toString(),
            name = activityBinding.inputReceiverBy.text.toString(),
            dlvDt =activityBinding.inputDeliveryDt.text.toString() ,
            relation = activityBinding.inputRelation.toString(),
            phoneNo = activityBinding.inputReceiverbyMobile.text.toString(),
            sign = signRequired ,
            stamp = stampRequired,
            podImage = podImagePath,
            signImage = "",
            remarks =activityBinding.inputRemark.text.toString(),
            userCode = getUserCode(),
            sessionId =getSessionId(),
            delayReason = "",
            menuCode = "GTAPP_PODENTRY",
            deliveryBoy = activityBinding.inputDeliveryBoy.text.toString().uppercase(),
            boyId = "",
            podDt = activityBinding.inputDate.toString(),
            pckgs = "",
            pckgsStr = "",
            dataIdStr = ""

        )
    }

    override fun onComplete(clickType: String, imageBitmap: Bitmap) {
        if(clickType == BottomSheetSignature.COMPLETED_CLICK_LISTENER_TAG) {
            signBitmap = imageBitmap
            activityBinding.signImg.setImageBitmap(signBitmap);
        }
    }
}