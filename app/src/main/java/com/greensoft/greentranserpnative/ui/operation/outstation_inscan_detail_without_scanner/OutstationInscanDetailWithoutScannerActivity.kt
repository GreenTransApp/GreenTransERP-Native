package com.greensoft.greentranserpnative.ui.operation.outstation_inscan_detail_without_scanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityOutstationInscanDetailWithoutScannerBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.common.models.CommonBottomSheetModel
import com.greensoft.greentranserpnative.ui.bottomsheet.receivingDetails.ReceivingDetailsBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.receivingDetails.models.ReceivingDetailsEnteredDataModel
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.common.alert.CommonAlert
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.InScanDetailsAdapter
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.InScanDetailsViewModel
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.model.DamageReasonModel
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.model.InScanDetailScannerModel
import com.greensoft.greentranserpnative.ui.operation.outstation_inscan_detail_with_scanner.OutstationInscanDetailWithScannerViewModel
import com.greensoft.greentranserpnative.ui.operation.unarrived.models.InscanListModel
import com.greensoft.greentranserpnative.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import javax.inject.Inject
@AndroidEntryPoint
class OutstationInscanDetailWithoutScannerActivity @Inject constructor(): BaseActivity(),
    AlertCallback<Any>, OnRowClick<Any>,
    BottomSheetClick<Any> {
        lateinit var activityBinding:ActivityOutstationInscanDetailWithoutScannerBinding

    private lateinit var manager: LinearLayoutManager
    private val viewModel: OutstationInscanDetailWithoutScannerViewModel by viewModels()
    private var inScanCardAdapter: OutstationInscanDetailWithoutScannerAdapter? = null
    private var inScanCardDetailList: ArrayList<InScanDetailScannerModel> = ArrayList()
    private var damagePckgsReasonList: ArrayList<DamageReasonModel> = ArrayList()
    private var inScanDetailData: InScanDetailScannerModel? = null
    private var inScanSelectedData: InscanListModel? = null
    private var receivingDetail: ReceivingDetailsEnteredDataModel? = null

    private var manifestNo: String? = ""
    private var mawb: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityOutstationInscanDetailWithoutScannerBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar(" OutstationIn-Scan W/O Scanner")

        getInscanData()
        getDamagePckgsReasonList()
        setObservers()
        getInScanDetails()
        setOnClick()
        openReceivingDetailsBottomSheet()
    }

    private fun getInscanData() {

        if (intent != null) {
            val jsonString = intent.getStringExtra("ManifestNo")
            if (jsonString != "") {
                val gson = Gson()
                val listType = object : TypeToken<InscanListModel>() {}.type
                val resultList: InscanListModel =
                    gson.fromJson(jsonString.toString(), listType)
                inScanSelectedData = resultList;
                if (inScanSelectedData != null) {
                    manifestNo = inScanSelectedData?.manifestno
                } else {
                    Log.e("InScanDetailWithScannerActivity", "Manifest was corrupted.")
                    errorToast("Something went wrong, Please try again.")
                    finish()
                }
            }
        }
    }

    private fun setObservers() {
//        mPeriod.observe(this) { datePicker ->
//            activityBinding.inputDate.setText(datePicker.viewsingleDate)
//            sqlDate=datePicker.sqlsingleDate.toString()
//        }
        viewModel.isError.observe(this) { errMsg ->
            errorToast(errMsg)
        }
        timePeriod.observe(this){ timePicker->
//            activityBinding.inputTime.setText(timePicker.viewSingleTime)
        }
        viewModel.inScanDetailLiveData.observe(this) { inScanData ->
            inScanDetailData = inScanData
            setInScanData()
        }
        viewModel.inScanCardLiveData.observe(this) { inScanCardList ->
            inScanCardDetailList = inScanCardList
            setupRecyclerView()
        }

        viewModel.damagePckgsReasonLiveData.observe(this) { damageReasons ->
            damagePckgsReasonList = damageReasons
//            setupRecyclerView()
        }
        viewModel.inScanSaveLiveData.observe(this) { inScanDetailSave ->
            successToast(inScanDetailSave.commandmessage)
            getInScanDetails()
        }
    }

    private fun openDamagePckgsReasonBottomSheet(rvList: ArrayList<DamageReasonModel>, index: Int) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].text.toString(), rvList[i]))

        }
        openCommonBottomSheet(this, "Damage Reason Selection", this, commonList, true, index)

    }
//
    private fun setInScanData() {
        activityBinding.inputMawb.text = inScanDetailData?.mawbno ?: "No data available"
        activityBinding.inputManifest.text = inScanDetailData?.manifestno ?: "No data available"
//        activityBinding.inputAirline.text = inScanDetailData?.airline?.toString() ?: "No data available"
        activityBinding.inputVehicle.text = inScanDetailData?.modename ?: "No data available"
        activityBinding.inputDispatchOn.text = inScanDetailData?.manifestdt ?: "No data available"
        activityBinding.inputFromStation.text = inScanDetailData?.origin ?: "No data available"
        activityBinding.inputToStation.text = inScanDetailData?.tostation ?: "No data available"
    }


    private fun setOnClick() {

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

    private fun getInScanDetails() {
        viewModel.getInScanDetails(
            getCompanyId(),
//            "17846899",
            userDataModel?.usercode.toString(),
            getLoginBranchCode(),
            manifestNo!!,
            "O",
            getSessionId()
        )
    }

    private fun getDamagePckgsReasonList() {
        viewModel.getDamageReasonList(getCompanyId())
    }

    private fun setupRecyclerView() {
        if (inScanCardAdapter == null) {
            manager = LinearLayoutManager(this)
            activityBinding.rvCardDetails.layoutManager = manager
        }
        inScanCardAdapter = OutstationInscanDetailWithoutScannerAdapter(inScanCardDetailList, this, this)
        activityBinding.rvCardDetails.adapter = inScanCardAdapter
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

    private fun validateEnteredData(model: InScanDetailScannerModel) {
        val nullOrZeroErr = "cannot be Empty / Zero."
        var receivePckgs: Int = 0
        var damagePckgs: Int = 0
        try {
            receivePckgs = model.receivedpckgs.toInt()
        } catch (ex: Exception) {
            errorToast("Receive Pckgs $nullOrZeroErr")
            return
        }
        try {
            damagePckgs = model.damage.toInt()
        } catch (ex: Exception) {
            errorToast("Damage Pckgs $nullOrZeroErr")
            return
        }
        if(receivePckgs <= 0) {
            errorToast("Receive Pckgs $nullOrZeroErr")
            return
        }
        if(damagePckgs > 0) {
            if(model.damagereason.isNullOrBlank()) {
                errorToast("Please select a damage reason")
                return;
            }
        }

        showAlertOnSave(model)
    }

    private fun showAlertOnSave(model: InScanDetailScannerModel) {
        CommonAlert.createAlert(
            context = this,
            header = "Alert!!",
            description = " Are You Sure You Want To Save In-Scan for GR# ${model.grno}?",
            callback = this,
            alertCallType = "SAVE_INSCAN",
            data = model
        )
    }

    override fun onAlertClick(alertClick: AlertClick, alertCallType: String, data: Any?) {
        if (alertCallType == "SAVE_INSCAN" && alertClick == AlertClick.YES) {
//            successToast("test")
            val model = data as InScanDetailScannerModel
            saveInscanDetailWithoutScan(model)
        }
    }

    private fun saveInscanDetailWithoutScan(model: InScanDetailScannerModel) {

        viewModel.saveInScanDetailsWithoutScan(
            companyid = getCompanyId(),
            manifestno=manifestNo.toString(),
            mawbno=mawb.toString(),
            branchcode=getLoginBranchCode(),
            receivedt=receivingDetail?.receivingSqlDate.toString(),
            receivetime=receivingDetail?.receivingViewTime.toString(),
            vehiclecode=model.modecode.toString(),
            remarks=receivingDetail?.receivingRemarks.toString(),
            grno=model.grno.toString(),
            mfpckgs= model.despatchpckgs.toString(),
            pckgs=model.receivedpckgs.toString(),
            weight=model.despatchweight.toString(),
            damagepckgs= model.damage.toString(),
            damagereasoncode=model.damagereasoncode.toString(),
            detailremarks = "",
            usercode=userDataModel?.usercode.toString(),
            menucode="GTAPP_LONGROUTEARRIVAL",
            sessionid=getSessionId(),
            fromstncode=model.orgcode.toString()
            )
    }

//    private fun saveInscanDetailWithoutScan(index: Int) {
//
//        viewModel.saveInScanDetailsWithoutScan(
//            companyId = getCompanyId(),
//            manifestNo = manifestNo.toString(),
//            mawbNo = mawb.toString(),
//            branchCode = getLoginBranchCode(),
//            receiveDt = "",
//            receiveTime = "",
//            vehicleCode = inScanCardDetailList[index].modecode.toString(),
//            remarks = inScanDetailData?.remarks.toString(),
//            grNo = inScanCardDetailList[index].grno.toString(),
//            mfPckgs =inScanCardDetailList[index].despatchpckgs.toString(),
//            pckgs = "",
//            weight = inScanCardDetailList[index].despatchweight.toString(),
//            damagePckgs = inScanCardDetailList[index].damage.toString(),
//            damageReasoncode = damagePckgsReasonList[index].value.toString(),
//            detailRemarks = "",
//            userCode = userDataModel?.usercode.toString(),
//            menuCode = "",
//            sessionId = getSessionId(),
//            fromstnCode = inScanCardDetailList[index].orgcode.toString(),
//
//            )
//    }


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


    override fun onClick(data: Any, clickType: String) {
        // Using the adapter click rather than this
    }
    override fun onRowClick(data: Any, clickType: String, index: Int) {
//        if (clickType == "DAMAGE_REASON_SELECT") {
        if (clickType == InScanDetailsAdapter.TAG_DAMAGE_REASON_CLICK) {
            openDamagePckgsReasonBottomSheet(damagePckgsReasonList, index)
        } else if (clickType == InScanDetailsAdapter.TAG_SAVE_CARD_CLICK) {
            try {
                val model = data as InScanDetailScannerModel
                validateEnteredData(model)
            } catch (err: Exception) {
                errorToast(err.message)
            }
            // saveInscanDetailWithoutScan(index)
        }
    }

    override fun onItemClickWithAdapter(data: Any, clickType: String, index: Int) {
        if (clickType == "Damage Reason Selection") {
            val selectedReason=data as DamageReasonModel
            inScanCardAdapter?.setDamageReason(selectedReason, index)

        }
    }
}

