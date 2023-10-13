package com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityScanLoadBinding
import com.greensoft.greentranserpnative.ui.common.scanPopup.ScanPopup
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad.models.LoadingSlipHeaderDataModel
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad.models.StickerModel
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import dagger.hilt.android.AndroidEntryPoint
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
        getScanSticker()
        setObservers()

        onClicks()
    }

    fun setUi() {
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
    }

    private fun getScanSticker(){


        viewModel.getScanSticker(
            "86858483",
            "greentransapp_getinscannedsticker",
            listOf("prmgrnid","prmwarehouseid","prmbranchcode","prmpartid"),
            arrayListOf("1","1","00000","11363")
        )
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