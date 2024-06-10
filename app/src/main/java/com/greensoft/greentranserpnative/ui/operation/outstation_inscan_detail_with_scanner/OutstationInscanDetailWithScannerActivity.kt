package com.greensoft.greentranserpnative.ui.operation.outstation_inscan_detail_with_scanner

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityInScanDetailWithScannerBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.receivingDetails.ReceivingDetailsBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.receivingDetails.models.ReceivingDetailsEnteredDataModel
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_with_scanner.InScanWithScannerAdapter
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.model.InScanDetailScannerModel
import com.greensoft.greentranserpnative.ui.operation.outstation_inscan_detail_with_scanner.adapter.OutstationInscanDetailsWithScannerAdapter
import com.greensoft.greentranserpnative.ui.operation.unarrived.models.InscanListModel
import com.greensoft.greentranserpnative.utils.Utils
import java.lang.Exception
import javax.inject.Inject

class OutstationInscanDetailWithScannerActivity  @Inject constructor(): BaseActivity(),
    OnRowClick<Any>,
    AlertCallback<Any>, BottomSheetClick<Any> {
        lateinit var activityBinding:ActivityInScanDetailWithScannerBinding
        private lateinit var manager: LinearLayoutManager
        private val viewModel: OutstationInscanDetailWithScannerViewModel by viewModels()
        private var inScanCardAdapterList: OutstationInscanDetailsWithScannerAdapter? = null
        private var inScanCardDetailList:ArrayList<InScanDetailScannerModel> = ArrayList()
        private  var inScanDetailData: InScanDetailScannerModel? = null
        private var inscanListData: ArrayList<InscanListModel> = ArrayList()
        private var manifestNo:String? =""
        private var inScanSelectedData: InscanListModel? = null
        private var receivingDetail: ReceivingDetailsEnteredDataModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityInScanDetailWithScannerBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        activityBinding = ActivityInScanDetailWithScannerBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar(" Outstation In-Scan Detail With Scan")
        getInScanMfHeadFromIntent()
        setObservers()
        getInScanDetails()

        setOnClick()
        if (savedInstanceState == null) {
            openReceivingDetailsBottomSheet()
        }

    }

    private fun openReceivingDetailsBottomSheet() {
        if(receivingDetail == null) {
            receivingDetail = ReceivingDetailsEnteredDataModel(
                manifestNo = manifestNo,
                receivingViewDate = getViewCurrentDate(),
                receivingSqlDate = getSqlCurrentDate(),
                receivingViewTime = getSqlCurrentTime(),
                receivingUserName = userDataModel?.username,
                receivingUserCode = getUserCode(),
                receivingRemarks = null
            )
        }
        Utils.logger(ReceivingDetailsBottomSheet.TAG, receivingDetail.toString())
        val instance = ReceivingDetailsBottomSheet.newInstance(
            mContext = this,
            companyId = getCompanyId(),
            manifestNo = manifestNo.toString(),
            bottomSheetClick = this,
            receivingDetailsEnteredDataModel = receivingDetail!!

        )
        instance.show(
            supportFragmentManager,
            ReceivingDetailsBottomSheet.TAG
        )
    }

    override fun onItemClick(data: Any, clickType: String) {
        if(clickType == ReceivingDetailsBottomSheet.SAVE_CLICK_TAG) {
            try {
                val model = data as ReceivingDetailsEnteredDataModel
                this.receivingDetail = model
                Utils.logger(ReceivingDetailsBottomSheet.TAG, receivingDetail?.receivingViewDate)
            } catch (err: Exception) {
                errorToast(err.message)
            }
        }
    }

    private fun setObservers(){
        viewModel.isError.observe(this) { errMsg ->
            errorToast(errMsg)
        }
        viewModel.inScanDetailLiveData.observe(this){ inScanData->
            inScanDetailData = inScanData
            setInScanData()
        }
        viewModel.inScanCardLiveData.observe(this){ inScanCardList->
            inScanCardDetailList = inScanCardList
            setupRecyclerView()
        }
        mScanner.observe(this){ stickerNo->
//            Companion.successToast(mContext, stickerNo)
            addInScanSticker(stickerNo)
//            playSound()
        }
        viewModel.inScanAddStickerLiveData.observe(this) { inScanSuccess ->
            successToast(inScanSuccess.commandmessage.toString())
            getInScanDetails();
        }
    }
    private fun setInScanData() {

        activityBinding.inputMawb.text = inScanDetailData?.mawbno?: "No data available"
        activityBinding.inputManifest.text = inScanDetailData?.manifestno ?: "No data available"
//        activityBinding.inputAirline.text = inScanDetailData?.airline?.toString() ?: "No data available"
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
        activityBinding.receivingDetailEdit.setOnClickListener {
            openReceivingDetailsBottomSheet()
        }

    }
    private fun getInScanMfHeadFromIntent() {
        if(intent != null) {
            val jsonString = intent.getStringExtra("ManifestNo")
            if(jsonString != "") {
                val gson = Gson()
                val listType = object : TypeToken<InscanListModel>() {}.type
                val resultList: InscanListModel =
                    gson.fromJson(jsonString.toString(), listType)
                inScanSelectedData = resultList;
                if(inScanSelectedData != null) {
                    manifestNo = inScanSelectedData?.manifestno
                } else {
                    Log.e("InScanDetailWithScannerActivity", "Manifest was corrupted.")
                    errorToast("Something went wrong, Please try again.")
                    finish()
                }
            }
        }
    }


    private fun getInScanDetails(){
        if(inScanCardDetailList.size > 0) {
            inScanCardDetailList.clear()
            setupRecyclerView()
        }
        viewModel.getInScanDetails(
            getCompanyId(),
            getUserCode(),
            getLoginBranchCode(),
            manifestNo.toString(),
            "O",
            getSessionId()
        )
    }
    private fun addInScanSticker(stickerNo: String){
        viewModel.addInScanSticker(
            getCompanyId(),
            getUserCode(),
            getLoginBranchCode(),
            "WMSAPP_INSCANPICKUP",
            getSessionId(),
            stickerNo,
            manifestNo.toString(),
            receivingDetail?.receivingSqlDate.toString(),
            receivingDetail?.receivingViewTime.toString()
        )
    }

    private fun setupRecyclerView() {
        if (inScanCardAdapterList == null) {
            manager = LinearLayoutManager(this)
            activityBinding.rvCardDetails.layoutManager = manager
        }
        inScanCardAdapterList = OutstationInscanDetailsWithScannerAdapter(inScanCardDetailList, this)
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