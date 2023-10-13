package com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityScanLoadBinding
import com.greensoft.greentranserpnative.ui.common.scanPopup.ScanPopup
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad.models.LoadingSlipHeaderDataModel
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad.models.StickerModel
import com.greensoft.greentranserpnative.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ScanLoadActivity @Inject constructor(): BaseActivity(), OnRowClick<Any>  {
    private lateinit var activityBinding: ActivityScanLoadBinding
    private lateinit var manager: LinearLayoutManager
    private lateinit var headerData: LoadingSlipHeaderDataModel
    private val viewModel: ScanLoadViewModel by viewModels()
    private var stickerList: ArrayList<StickerModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityScanLoadBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("SCAN AND LOAD")
        onClicks()
        setObservers()
        getScannedSticker()
    }

    private fun refreshData() {
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

    override fun onCLick(data: Any, clickType: String) {
//        activityBinding.openPallet.setOnClickListener {
//            val popupIntent = Intent(this, ScanPopup::class.java)
//            startActivity(popupIntent)
//        }
    }

    private fun setObservers(){
        activityBinding.pullDownToRefresh.setOnRefreshListener {
            refreshData()
        }
        mScanner.observe(this) { stickerNo ->
            validateSticker(stickerNo)
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
        }
        viewModel.saveStickerLiveData.observe(this) { saveStickerModel ->
            Utils.loadingNo = saveStickerModel.loadingno.toString()
            successToast(saveStickerModel.commandmessage.toString())
            refreshData()
        }

    }

    private fun getScannedSticker(){
        viewModel.getScannedSticker(
            getCompanyId(),
            "gtapp_getinscannedsticker",
            arrayListOf("prmgrno","prmbranchcode","prmusercode","prmsessionid"),
            arrayListOf(
                Utils.grNo,
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
                Utils.grNo,
                stickerNo,
                getLoginBranchCode(),
                getSqlCurrentDate(),
                getUserCode(),
                getSessionId()
            )
        )
    }



//    fun saveSticker(stickerNo: String, grNo: String) {
//        saveStickerScanLoad(
//            Utils.loadingNo,
//            getSqlCurrentDate(),
//            getSqlCurrentTime(),
//            getLoginBranchCode(),
//            fromBranch, // ( Origin )
//            toBranch, // ( Destination )
//            modeType,
//            "",
//            vehicleCode,
//            getUserCode(),
//            selectedDriver.getDrivercode(),
//            "",
//            "$stickerNo~",
//            "$grNo~",
//            getUserCode(),
//            getSessionId(),
//            selectedDriver.getMobileno(),
//            despatchtype //                despatchType.equals("OUTSTATION") ? "O" : "D"
//        )
//    }

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
                "GTNATAPP_SCANANDLOAD",  // "WMSAPP_SCANLOAD"
                sessionId, driverMobileNo, despatchType
            )
        } else {
            errorToast(internetError)
        }
    }

    private fun setupRecyclerView(){
        manager = LinearLayoutManager(this)
        activityBinding.rvSticker.apply {
            adapter = ScanLoadStickerAdapter(stickerList)
            layoutManager = manager
        }
    }
    private fun onClicks(){
//        activityBinding.openPallet.setOnClickListener {
//            openPopup()
//        }
    }
    private fun openPopup(){
        val dialogFragment = ScanPopup()
        dialogFragment.show(supportFragmentManager, "My  Fragment")
    }
}