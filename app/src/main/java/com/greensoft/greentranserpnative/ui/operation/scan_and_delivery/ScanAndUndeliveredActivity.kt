package com.greensoft.greentranserpnative.ui.operation.scan_and_delivery

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityScanAndUndeliveredBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.signBottomSheet.SignatureBottomSheetCompleteListener
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.GrSelectionModel
import com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.models.ScanStickerModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ScanAndUndeliveredActivity @Inject constructor() : BaseActivity(), OnRowClick<Any>,
    BottomSheetClick<Any>, AlertCallback<Any>, SignatureBottomSheetCompleteListener {
        lateinit var activityBinding:ActivityScanAndUndeliveredBinding
    private var rvList: ArrayList<ScanStickerModel> = ArrayList()
        
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityScanAndUndeliveredBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        getStickerData()

    }
    private fun getStickerData() {
//        if(grList.isNotEmpty()) grList.clear()
        if(intent != null) {
            val jsonString = intent.getStringExtra("ARRAY_JSON")
            if(jsonString != "") {
                val gson = Gson()
                val listType = object : TypeToken<List<ScanStickerModel>>() {}.type
                val resultList: ArrayList<ScanStickerModel> =
                    gson.fromJson(jsonString.toString(), listType)
                rvList.addAll(resultList)

            }
        }
    }




    override fun onComplete(clickType: String, imageBitmap: Bitmap) {
        TODO("Not yet implemented")
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
}