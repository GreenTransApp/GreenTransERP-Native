package com.greensoft.greentranserpnative.ui.operation.pod_entry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.activity.viewModels
import androidx.databinding.adapters.ViewBindingAdapter.setOnClick
import androidx.lifecycle.Observer
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityPodEntryBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.PickupManifestViewModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.BranchSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.ManifestEnteredDataModel
import com.greensoft.greentranserpnative.ui.operation.pod_entry.models.PodEntryModel
import com.greensoft.greentranserpnative.ui.operation.pod_entry.models.RelationListModel
import com.greensoft.greentranserpnative.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PodEntryActivity  @Inject constructor(): BaseActivity(), OnRowClick<Any> {
    lateinit var  activityBinding:ActivityPodEntryBinding
    private var podDetail: PodEntryModel? = null
    var relationType = ""
    private var relationList: ArrayList<RelationListModel> = ArrayList()
    private val viewModel: PodEntryViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding= ActivityPodEntryBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("POD Entry")
        setObservers()
        setSpinner()
        setOnClick()
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

         viewModel.podDetailsLiveData.observe(this) { data ->
             podDetail = data
             setPodDetails()
         }
        viewModel.relationLiveData.observe(this){ List->
            relationList =List
        }

    }
     private fun setOnClick(){
         activityBinding.btnGrProceed.setOnClickListener {
             getPodDetails()
         }
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
}