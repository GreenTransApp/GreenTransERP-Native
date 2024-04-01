package com.greensoft.greentranserpnative.ui.operation.inscan_detail_with_scanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityInScanDetailWithScannerBinding
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.InScanDetailsAdapter
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.model.InScanWithoutScannerModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InScanDetailWithScannerActivity  @Inject constructor(): BaseActivity(), OnRowClick<Any>,
    AlertCallback<Any> {
    private lateinit var activityBinding: ActivityInScanDetailWithScannerBinding
    private lateinit var manager: LinearLayoutManager
    private val viewModel: InScanDetailWithScannerViewModel by viewModels()
    private var inScanCardAdapterList: InScanWithScannerAdapter? = null
    private var inScanCardDetailList:ArrayList<InScanWithoutScannerModel> = ArrayList()
    private  var inScanDetailData: InScanWithoutScannerModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityInScanDetailWithScannerBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("In-Scan Detail With Scanner")
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
    private fun setInScanData() {
        activityBinding.inputMawb.text = inScanDetailData?.mawbno?: "No data available"
        activityBinding.inputManifest.text = inScanDetailData?.manifestno ?: "No data available"
        activityBinding.inputAirline.text = inScanDetailData?.airline?.toString() ?: "No data available"
        activityBinding.inputVehicle.text = inScanDetailData?.modename ?: "No data available"
        activityBinding.inputDispatchOn.text = inScanDetailData?.manifestdt?: "No data available"
        activityBinding.inputFromStation.text = inScanDetailData?.origin ?: "No data available"
        activityBinding.inputToStation.text = inScanDetailData?.tostation ?: "No data available"
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
        inScanCardAdapterList = InScanWithScannerAdapter(inScanCardDetailList, this)
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