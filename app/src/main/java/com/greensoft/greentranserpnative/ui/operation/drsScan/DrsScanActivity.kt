package com.greensoft.greentranserpnative.ui.operation.drsScan

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.ENV
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
import okhttp3.internal.Util
import javax.inject.Inject

@AndroidEntryPoint
class DrsScanActivity @Inject constructor(): BaseActivity(), OnRowClick<Any>, AlertCallback<Any>,BottomSheetClick<Any> {
    private lateinit var activityBinding:ActivityDrsScanBinding
    private val viewModel: DrsScanViewModel by viewModels()
    private var rvAdapter: DrsScanAdapter? = null
    private lateinit var manager: LinearLayoutManager
    private var rvList: ArrayList<ScannedDrsModel> = ArrayList()
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
        setupRecyclerView()
        setObservers()
        setOnClick()
        getDrsStickerList()
    }

    private fun setOnClick(){
        activityBinding.submitBtn.setOnClickListener {
            var stickerNo = activityBinding.inputSticker.text.toString()
            if(stickerNo == "") {
                errorToast("Please enter a sticker number to submit.")
            } else {
                var stickerAlreadyExists: Boolean = false
                for (scannedDrsList in rvList) {
                    if (scannedDrsList.stickerno == stickerNo) {
                        stickerAlreadyExists = true
                        removeSticker(stickerNo)
                        break
                    }
                }
                if (!stickerAlreadyExists) {
                    updateSticker(stickerNo)
                }
            }
//            errorToast("updateSticker fun call here")
        }
    }

    override fun onResume() {
        super.onResume()
//        if(Utils.drsNo != "" || Utils.drsNo != null) {
//            drsNo = Utils.drsNo!!
//            getDrsData(drsNo)
//        }
    }

    private fun getDrsData(drsNo:String?){
        if(drsNo == "" || drsNo == null) {
            return
        }
        viewModel.getDrsData(
            getCompanyId(),
            getUserCode(),
            getLoginBranchCode(),
            drsNo,
            getSessionId()
        )
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
                    deliveryBoy = drsActivityData?.username.toString()
                    remark = drsActivityData?.remarks.toString()

                } else {
                    Log.e("DrsScanActivity", "data was corrupted.")
                    errorToast("Something went wrong, Please try again.")
                    finish()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Utils.drsNo = drsNo
    }

    private fun setObservers(){
        viewModel.isError.observe(this) { errMsg ->
            errorToast(errMsg)
        }
        viewModel.viewDialogLiveData.observe(this) { show ->
//            progressBar.visibility = if(show) View.VISIBLE else View.GONE
            if (show) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }
        mScanner.observe(this){ scannedStickerNo->
            var stickerAlreadyExists: Boolean = false
            for(scannedDrsList in rvList) {
                if(scannedDrsList.stickerno == scannedStickerNo) {
                    stickerAlreadyExists = true
                    removeSticker(scannedStickerNo)
                    break
                }
            }
            if(!stickerAlreadyExists) {
                updateSticker(scannedStickerNo)
            }
//            Utils.debugToast(this, "sticker no $scannedStickerNo")
//            Toast.makeText(this, "sticker no $stickerData", Toast.LENGTH_SHORT).show()
        }

        viewModel.updateStickerLiveData.observe(this){ updateSticker->
            if(updateSticker.commandstatus == 1) {
                if(updateSticker.drsno != null && updateSticker.drsno != "") {
                    drsNo = updateSticker.drsno.toString()
                }
                getDrsStickerList()
                if(updateSticker.commandmessage != null) {
                    successToast(updateSticker.commandmessage.toString())
                } else {
                    successToast("Added Sticker Successfully.")
                }
            }
        }
        viewModel.removeStickerLiveData.observe(this) { removeSticker ->
            if(removeSticker.commandstatus == 1) {
                if(removeSticker.drsno != null && removeSticker.drsno != "") {
                    drsNo = removeSticker.drsno.toString()
                }
                getDrsStickerList()
                if(removeSticker.commandmessage != null) {
                    successToast(removeSticker.commandmessage.toString())
                } else {
                    successToast("Removed Sticker Successfully.")
                }
            }
        }
        viewModel.stickerListLiveData.observe(this) { stickerList ->
            if(stickerList.size > 0) {
                rvList = stickerList
                setupRecyclerView()
            }
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

    private fun removeSticker(stickerNo: String){
        viewModel.removeSticker(
            getCompanyId(),
            getUserCode(),
            getLoginBranchCode(),
            drsNo,
            stickerNo,
            getSessionId()
        )
    }

    private fun getDrsStickerList(){
        if(drsNo == "") {
            Utils.logger("DRS SCAN ACTIVITY", "DRS IS NULL")
            return
        }
        viewModel.getDrsStickers(
            getCompanyId(),
            getUserCode(),
            getLoginBranchCode(),
            drsNo,
            getSessionId()
        )
    }

    private fun setupRecyclerView() {
        if (rvAdapter == null) {
            manager = LinearLayoutManager(this)
            activityBinding.rvScanDRS.layoutManager = manager
        }
        rvAdapter = DrsScanAdapter(rvList, this)
        activityBinding.rvScanDRS.adapter = rvAdapter
    }

    private  fun showRemoveStickerAlert(drsStickerModel: ScannedDrsModel){
        CommonAlert.createAlert(
            context = this,
            header = "Alert!!",
            description = " Are You Sure You Want To Remove Sticker?",
            callback =this,
            alertCallType = DrsScanAdapter.REMOVE_STICKER_TAG,
            data = drsStickerModel
        )
    }


    override fun onAlertClick(alertClick: AlertClick, alertCallType: String, data: Any?) {
        try {
          when(alertClick){
              AlertClick.YES->{
                  if (alertCallType == DrsScanAdapter.REMOVE_STICKER_TAG){
                      try {
                          successToast("ALERT CLICK YES TILL DONE")
//                          val drsStickerModel: ScannedDrsModel = data as ScannedDrsModel
//                          removeSticker(drsStickerModel.stickerno.toString())
                      } catch (ex: Exception) {
                          errorToast(ENV.SOMETHING_WENT_WRONG_ERR_MSG)
                          return
                      }
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
        if (clickType==DrsScanAdapter.REMOVE_STICKER_TAG){
            try {
                val drsStickerModel: ScannedDrsModel = data as ScannedDrsModel
                showRemoveStickerAlert(drsStickerModel)
            } catch (ex: Exception) {
                errorToast(ENV.SOMETHING_WENT_WRONG_ERR_MSG)
                return
            }
        }
    }

    override fun onStop() {
        super.onStop()
        Utils.logger("DRS_ACTIVITY_CYCLE", "DRS SCAN ACTIVITY onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Utils.logger("DRS_ACTIVITY_CYCLE", "DRS SCAN ACTIVITY onDestroy")
    }


}