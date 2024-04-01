package com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityInscanDetailsBinding
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.model.InScanWithoutScannerModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InScanDetailsActivity  @Inject constructor(): BaseActivity(), OnRowClick<Any>, AlertCallback<Any> {
    private lateinit var activityBinding: ActivityInscanDetailsBinding
    private lateinit var manager: LinearLayoutManager
    private val viewModel: InScanDetailsViewModel by viewModels()
    private var inScanCardAdapterList:InScanDetailsAdapter? = null
    private var inScanCardDetailList:ArrayList<InScanWithoutScannerModel> = ArrayList()
    private  var inScanDetailData: InScanWithoutScannerModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityInscanDetailsBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("In-Scan Detail Without Scanner")
        setObservers()
        getInScanDetails()

        setOnClick()

    }


    private fun setObservers(){
        viewModel.inScanDetailLiveData.observe(this){ inScanData->
            inScanDetailData = inScanData
            setInScanData()
        }
        viewModel.inScanCardLiveData.observe(this){ inScanCardList->
            inScanCardDetailList = inScanCardList
            setupRecyclerView()
        }
    }
//    private fun setInScanData() {
//
//        activityBinding.inputMawb.text = inScanDetailData?.mawbno.toString()
//        activityBinding.inputManifest.text = inScanDetailData?.manifestno
//        activityBinding.inputAirline.text = inScanDetailData?.airline.toString()
//        activityBinding.inputVehicle.text = inScanDetailData?.modename
//        activityBinding.inputDispatchOn.text = inScanDetailData?.manifestdt.toString()
//        activityBinding.inputFromStation.text = inScanDetailData?.origin
//        activityBinding.inputToStation.text = inScanDetailData?.tostation
//    }

    private fun setInScanData() {
        activityBinding.inputMawb.text = inScanDetailData?.mawbno?: "Data not available"
        activityBinding.inputManifest.text = inScanDetailData?.manifestno ?: "Data not available"
        activityBinding.inputAirline.text = inScanDetailData?.airline?.toString() ?: "Data not available"
        activityBinding.inputVehicle.text = inScanDetailData?.modename ?: "Data not available"
        activityBinding.inputDispatchOn.text = inScanDetailData?.manifestdt?: "Data not available"
        activityBinding.inputFromStation.text = inScanDetailData?.origin ?: "Data not available"
        activityBinding.inputToStation.text = inScanDetailData?.tostation ?: "Data not available"
    }


    private fun setOnClick(){

        activityBinding.layoutCardDetail.setOnClickListener {
            toggleCardVisibility()
        }
        activityBinding.cardExpendBtn.setOnClickListener {
            toggleCardVisibility()
        }

    }

    private fun getInScanDetails(){
        viewModel.getInScanDetails(
            getCompanyId(),
//            "17846899",
            "A810",
            getLoginBranchCode(),
            "1040000014",
            "o",
            "syst"
        )
    }

    private fun setupRecyclerView() {
        if (inScanCardAdapterList == null) {
            manager = LinearLayoutManager(this)
            activityBinding.rvCardDetails.layoutManager = manager
        }
        inScanCardAdapterList = InScanDetailsAdapter(inScanCardDetailList, this)
        activityBinding.rvCardDetails.adapter = inScanCardAdapterList
//    }
    }

   private fun toggleCardVisibility() {

       val currentVisibility = activityBinding.linearLayoutInsideCard.visibility
       if (currentVisibility == View.VISIBLE) {

           activityBinding.linearLayoutInsideCard.visibility = View.GONE
           activityBinding.cardExpendBtn.setImageResource(R.drawable.outline_arrow_circle_down_24)
       } else {

           activityBinding.linearLayoutInsideCard.visibility = View.VISIBLE
           activityBinding.cardExpendBtn.setImageResource(R.drawable.outline_arrow_circle_up_24)
       }
    }




    override fun onClick(data: Any, clickType: String) {
        if (clickType == "SAVE_CARD") {
            Toast.makeText(mContext, "Save Button Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAlertClick(alertClick: AlertClick, alertCallType: String, data: Any?) {}


}