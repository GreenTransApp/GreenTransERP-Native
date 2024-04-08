package com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner

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
import com.greensoft.greentranserpnative.databinding.ActivityInscanDetailsBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.common.models.CommonBottomSheetModel
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.common.alert.CommonAlert
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.model.DamageReasonModel
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.model.InScanWithoutScannerModel
import com.greensoft.greentranserpnative.ui.operation.unarrived.models.InscanListModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InScanDetailsActivity  @Inject constructor(): BaseActivity(), AlertCallback<Any> , OnRowClick<Any>,
    BottomSheetClick<Any> {
    private lateinit var activityBinding: ActivityInscanDetailsBinding
    private lateinit var manager: LinearLayoutManager
    private val viewModel: InScanDetailsViewModel by viewModels()
    private var inScanCardAdapter: InScanDetailsAdapter? = null
    private var inScanCardDetailList: ArrayList<InScanWithoutScannerModel> = ArrayList()
    private var damagePckgsReasonList: ArrayList<DamageReasonModel> = ArrayList()
    private var inScanDetailData: InScanWithoutScannerModel? = null
    private var inScanSelectedData: InscanListModel? = null


    private var manifestNo: String? = ""
    private var mawb: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityInscanDetailsBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("Inscan Details Without Scanner")
        getInscanData()
        getDamagePckgsReasonList()
        setObservers()
        getInScanDetails()
        setOnClick()

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
            setupRecyclerView()
        }
    }

    private fun openDamagePckgsReasonBottomSheet(rvList: ArrayList<DamageReasonModel>, index: Int) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].text.toString(), rvList[i]))

        }
        openCommonBottomSheet(this, "Damage Reason Selection", this, commonList, true, index)

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


    }

    private fun getInScanDetails() {
        viewModel.getInScanDetails(
            getCompanyId(),
//            "17846899",
            userDataModel?.usercode.toString(),
            getLoginBranchCode(),
            manifestNo!!,
            "I",
            getSessionId()
        )
    }

    private fun getDamagePckgsReasonList() {
        viewModel.getDamageReasonList(
            getCompanyId(),

            )
    }

    private fun setupRecyclerView() {
        if (inScanCardAdapter == null) {
            manager = LinearLayoutManager(this)
            activityBinding.rvCardDetails.layoutManager = manager
        }
        inScanCardAdapter = InScanDetailsAdapter(inScanCardDetailList, this)
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

    private fun showAlertOnSave() {
        CommonAlert.createAlert(
            context = this,
            header = "Alert!!",
            description = " Are You Sure You Want To Save InScan Details  ?",
            callback = this,
            alertCallType = "SELECT_SAVE",
            data = ""
        )
    }


    override fun onClick(data: Any, clickType: String) {
        if (clickType == "SAVE_CARD") {
            Toast.makeText(mContext, "Save Button Clicked", Toast.LENGTH_SHORT).show()


        }
    }

    override fun onAlertClick(alertClick: AlertClick, alertCallType: String, data: Any?) {
        if (alertCallType == "SELECT_SAVE") {
           // successToast("test")
           // saveInscanDetailWithoutScan()

        }
    }

    private fun saveInscanDetailWithoutScan(index: Int) {

        viewModel.saveInScanDetailsWithoutScan(
            companyId = getCompanyId(),
            manifestNo = manifestNo.toString(),
            mawbNo = mawb.toString(),
            branchCode = getLoginBranchCode(),
            receiveDt = "",
            receiveTime = "",
            vehicleCode = inScanCardDetailList[index].modecode.toString(),
            remarks = inScanDetailData?.remarks.toString(),
            grNo = inScanCardDetailList[index].grno.toString(),
            mfPckgs =inScanCardDetailList[index].despatchpckgs.toString(),
            pckgs = "",
            weight = inScanCardDetailList[index].despatchweight.toString(),
            damagePckgs = inScanCardDetailList[index].damage.toString(),
            damageReasoncode = damagePckgsReasonList[index].value.toString(),
            detailRemarks = "",
            userCode = userDataModel?.usercode.toString(),
            menuCode = "",
            sessionId = getSessionId(),
            fromstnCode = inScanCardDetailList[index].orgcode.toString(),

            )
    }

    override fun onItemClick(data: Any, clickType: String) {

    }

    override fun onRowClick(data: Any, clickType: String, index: Int) {
        if (clickType == "DAMAGE_REASON_SELECT") {
           openDamagePckgsReasonBottomSheet(damagePckgsReasonList, index)

        }else if (clickType == "SAVE_CARD") {
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




