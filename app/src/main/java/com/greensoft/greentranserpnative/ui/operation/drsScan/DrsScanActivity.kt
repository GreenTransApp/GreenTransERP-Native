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
import com.greensoft.greentranserpnative.ui.common.alert.CommonAlert
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.drs.model.DrsDataModel
import com.greensoft.greentranserpnative.ui.operation.drsScan.model.ScannedDrsModel
import com.greensoft.greentranserpnative.utils.Utils
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
    private var drsNo:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityDrsScanBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("DRS Scan")
        this.drsNo = Utils.drsNo ?: ""
        getDrsActivityDataByIntent()

        updateStickersDrsList = generateSimpleList()
        setupRecyclerView()
        setObservers()
        setOnClick()
    }

    private fun setOnClick(){
        activityBinding.submitBtn.setOnClickListener {
//            updateSticker(stickerNo)
            errorToast("updateSticker fun call here")
        }
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
//            updateSticker(stickerData)
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
            drsNo,
            drsDate,
            drsTime,
            vehicleCode,
            vendCode,
            "",
            remark,
            stickerNo,
            deliveredBy,
            vendCode,
            vehicleNo,
            getSessionId()
        )
    }

    private fun removeSticker(){
        viewModel.removeSticker(
            getCompanyId(),
            getUserCode(),
            getLoginBranchCode(),
            drsNo,
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

    private  fun showAlert(){
        CommonAlert.createAlert(
            context = this,
            header = "Alert!!",
            description = " Are You Sure You Want To Remove Sticker?",
            callback =this,
            alertCallType ="REMOVE_STICKER",
            data = ""
        )
    }


    override fun onAlertClick(alertClick: AlertClick, alertCallType: String, data: Any?) {
        try {
          when(alertClick){
              AlertClick.YES->{
                  if (alertCallType=="REMOVE_STICKER"){
//                      removeSticker()
                  errorToast("removeAPI fun call here")
                  }
              }
              AlertClick.NO -> {
              }

          }
        } catch (ex:Exception){
            errorToast(ex.message)
        }
    }

    override fun onItemClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }

    override fun onClick(data: Any, clickType: String) {
        if (clickType=="REMOVE_STICKER"){
            showAlert()
        }
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