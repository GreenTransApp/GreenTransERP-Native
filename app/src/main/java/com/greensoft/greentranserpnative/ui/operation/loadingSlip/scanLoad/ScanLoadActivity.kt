package com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityScanLoadBinding
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.common.alert.CommonAlert
import com.greensoft.greentranserpnative.ui.common.scanPopup.ScanPopup
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad.models.LoadingSlipHeaderDataModel
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad.models.StickerModel
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.summary.SummaryScanLoadActivity
import com.greensoft.greentranserpnative.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.Util
import javax.inject.Inject

@AndroidEntryPoint
class ScanLoadActivity @Inject constructor(): BaseActivity(), OnRowClick<Any>, AlertCallback<Any> {
    private lateinit var activityBinding: ActivityScanLoadBinding
    private lateinit var manager: LinearLayoutManager
    private lateinit var headerData: LoadingSlipHeaderDataModel
    private val viewModel: ScanLoadViewModel by viewModels()
    private var stickerList: ArrayList<StickerModel> = ArrayList()
    private val menuCode: String = "GTAPP_SCANANDLOAD"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityScanLoadBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("SCAN AND LOAD")
        if(Utils.grModel == null) {
            finish()
        }
        activityBinding.grNo = Utils.grModel?.grno
        onClicks()
        setObservers()
        getScannedSticker()
    }

//    fun fakeData() {
//        stickerList.add(StickerModel("","test",1,1,"123123","asr","UDIT123123123123"))
//        stickerList.add(StickerModel("","test",1,1,"123123","asr","UDIT12375686578"))
//        stickerList.add(StickerModel("","test",1,1,"123123","asr","UDIT123"))
//        stickerList.add(StickerModel("","test",1,1,"123123","asr","UDIT12876t3434"))
//        stickerList.add(StickerModel("","test",1,1,"123123","asr","UDIT1rhthrth23"))
//        stickerList.add(StickerModel("","test",1,1,"123123","asr","UDIT123jyhrtyhj3123"))
//        stickerList.add(StickerModel("","test",1,1,"123123","asr","UDIT123123123123"))
//        stickerList.add(StickerModel("","test",1,1,"123123","asr","UDIT123123123123"))
//        setupRecyclerView()
//    }

    private fun refreshData() {
        stickerList.clear()
        setupRecyclerView()
        getScannedSticker()
        lifecycleScope.launch {
            delay(1500)
            activityBinding.pullDownToRefresh.isRefreshing = false
        }
    }

    private fun setUi() {
        activityBinding.totalScanned.text = headerData.totalscanned.toString()
//        activityBinding.totalBalance.text = headerData.totalbalance.toString()
        activityBinding.total.text = headerData.total.toString()
    }

    override fun onClick(data: Any, clickType: String) {
        val rowData = data as StickerModel
        if(clickType == "REMOVE_STICKER") {
            CommonAlert.createAlert(
                context = this,
                header = "Remove Sticker!",
                description = "Are you sure you want to remove this sticker [ ${rowData.stickerno} ] from Load?",
                callback = this,
                alertCallType = "REMOVE_STICKER",
                rowData
            )
        }
    }

    private fun setObservers(){
        activityBinding.pullDownToRefresh.setOnRefreshListener {
            refreshData()
        }
        mScanner.observe(this) { stickerNo ->
            var existingSticker = false
            stickerList.forEachIndexed { index, stickerModel ->
                if(stickerModel.stickerno == stickerNo) {
                    existingSticker = true
                }
            }
            if(existingSticker) {
                removeAlreadyScannedStickerAlert(stickerNo)
            } else {
                validateSticker(stickerNo)
            }
        }

        viewModel.isError.observe(this) { errMsg ->
            errorToast(errMsg)
        }

        viewModel.headerLivedata.observe(this){ headerDataModel ->
            headerData = headerDataModel
            setUi()
        }

        viewModel.stickerListLivedata.observe(this){stickerData->
            stickerList = stickerData
            setupRecyclerView()
        }
        viewModel.validateStickerLiveData.observe(this) { validateStickerModel ->
            successToast(validateStickerModel.commandmessage.toString())
            saveSticker(
                stickerNo = validateStickerModel.stickerno,
                grNo = Utils.grModel!!.grno
            )
        }
        viewModel.saveStickerLiveData.observe(this) { saveStickerModel ->
            Utils.loadingNo = saveStickerModel.loadingno.toString()
            successToast(saveStickerModel.commandmessage.toString())
            refreshData()
        }
        viewModel.removeStickerLiveData.observe(this) { removeStickerModel ->
            refreshData()
        }

    }

    private fun setupRecyclerView(){
        manager = LinearLayoutManager(this)
        activityBinding.rvSticker.apply {
            adapter = ScanLoadStickerAdapter(this@ScanLoadActivity, stickerList, this@ScanLoadActivity)
            layoutManager = manager
        }
    }
    private fun onClicks(){
        activityBinding.openSummary.setOnClickListener {
            openSummary("N")
        }
        activityBinding.loadingComplete.setOnClickListener {
            openSummary("Y")
        }
//        activityBinding.openPallet.setOnClickListener {
//            openPopup()
//        }
    }
    private fun openScanPalletPopUp(){
        val dialogFragment = ScanPopup("SCAN PALLET")
        dialogFragment.show(supportFragmentManager, "SCAN_PALLET_POP_UP")
    }

    private fun getScannedSticker(){
        viewModel.getScannedSticker(
            getCompanyId(),
            "gtapp_getinscannedsticker",
            arrayListOf("prmgrno","prmbranchcode","prmusercode","prmsessionid"),
            arrayListOf(
                Utils.grModel!!.grno,
                getLoginBranchCode(),
                getUserCode(),
                getSessionId()
            )
        )
    }

    private fun validateSticker(stickerNo: String) {
        viewModel.validateSticker(
            getCompanyId(),
            "gtapp_getstickerdetailforscanandload",
            arrayListOf("prmgrno", "prmstickerno", "prmbranchcode", "prmdt", "prmusercode", "prmsessionid"),
            arrayListOf(
                Utils.grModel!!.grno,
                stickerNo,
                getLoginBranchCode(),
                getSqlCurrentDate(),
                getUserCode(),
                getSessionId()
            )
        )
    }

    private fun removeAlreadyScannedStickerAlert(stickerNo: String) {
        CommonAlert.createAlert(
            context = this,
            header = "Remove Sticker!",
            description = "Are you sure you want to remove this sticker [ $stickerNo ] from Load?",
            callback = this,
            alertCallType = "REMOVE_SCANNED_STICKER",
            stickerNo
        )
    }



    private fun saveSticker(stickerNo: String, grNo: String) {
        saveStickerScanLoad(
            Utils.loadingNo,
            getSqlCurrentDate(),
            getSqlCurrentTime(),
            getLoginBranchCode(),
            Utils.grModel!!.orgcode, // ( Origin )
            Utils.grModel!!.destcode, // ( Destination )
            Utils.grModel!!.modetype,
            "",
            Utils.grModel!!.modecode,
            getUserCode(),
            Utils.grModel!!.drivercode,
            "",
            "$stickerNo~",
            "$grNo~",
            getUserCode(),
            getSessionId(),
            Utils.grModel!!.drivermobile,
            "O" //                despatchType.equals("OUTSTATION") ? "O" : "D"
        )
    }

    private fun saveStickerScanLoad(
        loadingNo: String?,
        loadingDt: String?,
        loadingTime: String?,
        stnCode: String?,
        branchCode: String?,
        destCode: String?,
        modeType: String?,
        vendCode: String?,
        modeCode: String?,
        loadedBy: String?,
        driverCode: String?,
        remarks: String?,
        stickerNoStr: String?,
        grNoStr: String?,
        userCode: String?,
        sessionId: String?,
        driverMobileNo: String?,
        despatchType: String?
    ) {
        if (isNetworkConnected()) {
            viewModel.updateSticker(
                getCompanyId(), loadingNo, loadingDt, loadingTime, stnCode, branchCode,
                destCode, modeType, vendCode, modeCode, loadedBy, driverCode, remarks,
                stickerNoStr, grNoStr, userCode,
                menuCode,  // "GTNATAPP_SCANANDLOAD"
                sessionId, driverMobileNo, despatchType
            )
        } else {
            errorToast(internetError)
        }
    }

    private fun removeStickerScanLoad(stickerNo: String?) {
        if (isNetworkConnected()) {
            viewModel.removeSticker(
                getCompanyId(),
                stickerNo,
                getUserCode(),
                menuCode,  // "GTNATAPP_SCANANDLOAD"
                getSessionId()
            )
        } else {
            errorToast(internetError)
        }
    }

    override fun onAlertClick(alertClick: AlertClick, alertCallType: String, data: Any?) {
        try {
            when (alertClick) {
                AlertClick.YES -> {
                    if (alertCallType == "REMOVE_STICKER") {
                        if (data != null) {
                            val stickerData = data as StickerModel
                            removeStickerScanLoad(stickerData.stickerno)
                        }
                    } else if (alertCallType == "REMOVE_SCANNED_STICKER") {
                        if (data != null) {
                            val stickerNo: String = data.toString()
                            validateSticker(stickerNo)
                        }
                    }
                }
                AlertClick.NO -> {

                }
            }
        } catch (ex: Exception) {
            errorToast(ex.message)
        }
    }

    fun openSummary(loadingComplete: String) {
        if (stickerList.size < 1) {
            errorToast("Please scan stickers to get the summary.")
            return
        }
        val intent = Intent(this@ScanLoadActivity, SummaryScanLoadActivity::class.java)
        intent.putExtra("LOADING_NO", Utils.loadingNo)
        intent.putExtra("SAVE", loadingComplete) // "Y" -- "N"
        startActivity(intent)
    }

}