package com.greensoft.greentranserpnative.ui.operation.loadingSlip.newScanLoad

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityNewScanAndLoadBinding
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.common.alert.CommonAlert
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.newScanLoad.models.LoadedStickerModel
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.newScanLoad.models.ValidateStickerModel
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad.models.StickerModel
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.summary.SummaryScanLoadActivity
import com.greensoft.greentranserpnative.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NewScanAndLoadActivity @Inject constructor(): BaseActivity(), OnRowClick<Any>, AlertCallback<Any> {
    private lateinit var activityBinding: ActivityNewScanAndLoadBinding
    private lateinit var manager: LinearLayoutManager
//    private lateinit var headerData: LoadingSlipHeaderDataModel
    private val viewModel: NewScanAndLoadViewModel by viewModels()
    private var stickerList: ArrayList<LoadedStickerModel> = ArrayList()
//    private val menuCode: String = "GTAPP_SCANANDLOAD"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityNewScanAndLoadBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("SCAN AND LOAD")
        setHeaderData(if(Utils.loadingNo == "") "Not Available" else Utils.loadingNo, 0)
        onClicks()
        setObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.scanloadsearchlist, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.scanLoadSearchListMenu -> {
                openScanLoadSearchList()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openScanLoadSearchList() {
//        val intent = Intent(this@NewScanAndLoadActivity, )
//        startActivity(intent)
        successToast("Open Activity")
    }


    private fun setHeaderData(loadingNo: String, scannedStickers: Int) {
        activityBinding.scannedGrStickers = scannedStickers.toString()
        activityBinding.loadingNo = loadingNo
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun refreshData() {
        stickerList.clear()
        setupRecyclerView()
        getScannedStickersScanLoad(Utils.loadingNo)
        lifecycleScope.launch {
            delay(1500)
            activityBinding.pullDownToRefresh.isRefreshing = false
        }
    }

    private fun setUi() {
//        activityBinding.totalScanned.text = headerData.totalscanned.toString()
//        activityBinding.totalBalance.text = headerData.totalbalance.toString()
//        activityBinding.total.text = headerData.total.toString()
    }

    override fun onClick(data: Any, clickType: String) {
        val rowData = data as LoadedStickerModel
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

//        viewModel.headerLivedata.observe(this){ headerDataModel ->
//            headerData = headerDataModel
//            setUi()
//        }

        viewModel.stickerListLivedata.observe(this){stickerData->
            stickerList = stickerData
            setupRecyclerView()
        }
        viewModel.validateStickerLiveData.observe(this) { validateStickerModel ->
//            successToast(validateStickerModel.commandmessage.toString())
            saveSticker(
                stickerNo = validateStickerModel.stickerno,
//                grNo = Utils.grModel!!.grno
                grNo = validateStickerModel.grno
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
        setHeaderData(if(Utils.loadingNo == "") "Not Available" else Utils.loadingNo, stickerList.size)
        manager = LinearLayoutManager(this)
        activityBinding.rvSticker.apply {
            adapter = NewScanLoadStickerAdapter(this@NewScanAndLoadActivity, stickerList, this@NewScanAndLoadActivity)
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

    private fun getScannedStickersScanLoad(loadingNo: String) {
        if (isNetworkConnected()) {
            viewModel.getScannedStickers(
                getCompanyId(),
                if (loadingNo == "") "0" else loadingNo
            )
        } else {
            errorToast(internetError)
        }
    }

    private fun validateSticker(stickerNo: String) {
        if(isNetworkConnected()) {
            viewModel.validateSticker(
                getCompanyId(),
                getLoginBranchCode(),
                stickerNo,
                getSqlCurrentDate()
            )
        } else {
            errorToast(internetError)
        }
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
            "", // ( Origin )
            "", // ( Destination )
            "",
            "",
            "",
            getUserCode(),
            "",
            "",
            "$stickerNo~",
            "$grNo~",
            getUserCode(),
            getSessionId(),
            "",
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
                if(Utils.menuModel == null) "GTAPP_LOADING" else Utils.menuModel!!.menucode,
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
                if(Utils.menuModel == null) "GTAPP_LOADING" else Utils.menuModel!!.menucode,
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
        val intent = Intent(this@NewScanAndLoadActivity, SummaryScanLoadActivity::class.java)
        intent.putExtra("LOADING_NO", Utils.loadingNo)
        intent.putExtra("SAVE", loadingComplete) // "Y" -- "N"
        startActivity(intent)
    }

//    private fun showDifferentDestAlert(result: ValidateStickerModel) {
//        val builder = AlertDialog.Builder(mContext)
//        builder.setTitle("Destination Difference Alert !")
//        builder.setMessage(
//            """
//            Sticker #: ${result.stickerno}
//            Destination: ${selectedToBranch.getStnname()}
//            Not Matching
//            To Station: ${result.destination}.
//            Do you want to continue?
//            """.trimIndent()
//        )
//        builder.setPositiveButton("Yes") { dialogInterface, i ->
//            saveSticker(
//                result.stickerno,
//                result.grno
//            )
//        }
//        builder.setNeutralButton("Cancel") { dialogInterface, i -> dialogInterface.dismiss() }
//        builder.setCancelable(false)
//        builder.show()
//    }

}