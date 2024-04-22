package com.greensoft.greentranserpnative.ui.operation.delivery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityDeliveryOptionBinding
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.pending_for_delivery_update_list.PendingDeliveryDrsListActivity
import com.greensoft.greentranserpnative.ui.operation.pod_entry.PodEntryActivity
import com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.ScanAndDeliveryActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DeliveryOptionActivity @Inject constructor() : BaseActivity(), OnRowClick<Any>,
    BottomSheetClick<Any>, AlertCallback<Any> {

        lateinit var  activityBinding:ActivityDeliveryOptionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding= ActivityDeliveryOptionBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("Delivery Options")
        setOnClick()
    }

     private  fun setOnClick(){
         activityBinding.btnScanDelivery.setOnClickListener {
             val intent= Intent(this,ScanAndDeliveryActivity::class.java)
             startActivity(intent)
         }
         activityBinding.btnPodEntry.setOnClickListener {
//             val intent= Intent(this,PodEntryActivity::class.java)
             val intent= Intent(this,PendingDeliveryDrsListActivity::class.java)
             startActivity(intent)
         }

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