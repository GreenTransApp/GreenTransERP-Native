package com.greensoft.greentranserpnative.ui.operation.drsScan

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityDrsScanBinding
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.drs.model.DrsDataModel
import com.greensoft.greentranserpnative.ui.operation.drsScan.model.ScannedDrsModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DrsScanActivity @Inject constructor(): BaseActivity(), OnRowClick<Any>, AlertCallback<Any>,BottomSheetClick<Any> {
    private lateinit var activityBinding:ActivityDrsScanBinding
    private val viewModel: DrsScanViewModel by viewModels()
    private var rvAdapter: DrsScanAdapter? = null
    private lateinit var manager: LinearLayoutManager
    private var updateStickersDrsList: ArrayList<ScannedDrsModel> = ArrayList()
    private var stickerNo:String=""
    private var drsActivityData:DrsDataModel?= null

    private var drsDate:String = ""
    private var drsTime:String = ""
    private var deliveredBy:String = ""
    private var vendorName:String = ""
    private var vendCode:String = ""
    private var vehicleNo:String = ""
    private var vehicleCode:String = ""
    private var deliveryBoy:String = ""
    private var remark:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityDrsScanBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("DRS Scan")
        getDrsActivityDataByIntent()
        updateStickersDrsList = generateSimpleList()
        setupRecyclerView()
        setObservers()
    }

    private fun getDrsActivityDataByIntent() {
        if(intent != null) {
            val jsonString = intent.getStringExtra("DrsModelData")
            if(jsonString != "") {
                val gson = Gson()
                val listType = object : TypeToken<DrsDataModel>() {}.type
                val resultList: DrsDataModel =
                    gson.fromJson(jsonString.toString(), listType)
                drsActivityData = resultList;
                if(drsActivityData != null) {
                    drsDate = drsActivityData?.drsdate.toString()
                    drsTime = drsActivityData?.drstime.toString()
                    deliveredBy = drsActivityData?.deliveredby.toString()
                    vendorName = drsActivityData?.vendorname.toString()
                    vendCode = drsActivityData?.vendcode.toString()
                    vehicleNo = drsActivityData?.vehicleno.toString()
                    vehicleCode = drsActivityData?.vehiclecode.toString()
                    deliveryBoy = drsActivityData?.driver.toString()
                    remark = drsActivityData?.remark.toString()

                } else {
                    Log.e("DrsScanActivity", "data was corrupted.")
                    errorToast("Something went wrong, Please try again.")
                    finish()
                }
            }
        }
    }

    private fun setObservers(){
        mScanner.observe(this){ stickerData->
            stickerNo = stickerData
            Toast.makeText(this, "sticker no $stickerData", Toast.LENGTH_SHORT).show()
        }
        viewModel.updateStickerLiveData.observe(this){updateSticker->
            updateStickersDrsList = updateSticker
            setupRecyclerView()
        }
    }

    private fun updateSticker(stickerNo: String){
        viewModel.updateSticker(
            getCompanyId(),
            getUserCode(),
            getLoginBranchCode(),
            getLoginBranchCode(),
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            stickerNo,
            "",
            "",
            "",
            getSessionId()
        )
    }

    private fun removeSticker(){
        viewModel.removeSticker(
            getCompanyId(),
            getUserCode(),
            getLoginBranchCode(),
            "",
            stickerNo,
            getSessionId()
        )
    }

    private fun setupRecyclerView() {
        if (rvAdapter == null) {
            manager = LinearLayoutManager(this)
            activityBinding.rvScanDRS.layoutManager = manager
        }
        rvAdapter = DrsScanAdapter(updateStickersDrsList, this)
        activityBinding.rvScanDRS.adapter = rvAdapter
    }


    override fun onAlertClick(alertClick: AlertClick, alertCallType: String, data: Any?) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }

    override fun onClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }

    private fun generateSimpleList(): ArrayList<ScannedDrsModel> {
        val dataList: ArrayList<ScannedDrsModel> =
            java.util.ArrayList<ScannedDrsModel>()
        for (i in 1..100) {
            dataList.add(ScannedDrsModel("",i,(100+i).toString(),"",""))
        }
        return dataList
    }

}