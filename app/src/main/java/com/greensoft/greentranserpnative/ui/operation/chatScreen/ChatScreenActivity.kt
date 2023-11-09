package com.greensoft.greentranserpnative.ui.operation.chatScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityChatScreenBinding
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatScreenActivity @Inject constructor() : BaseActivity(), OnRowClick<Any>,
    BottomSheetClick<Any>, AlertCallback<Any> {
    private lateinit var activityBinding: ActivityChatScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityChatScreenBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("Communication Details")
        onClick()
    }

    fun onClick(){
       activityBinding.sentBtn.setOnClickListener {
           successToast("Send clicked")
       }
    }
    override fun onAlertClick(alertClick: AlertClick, alertCallType: String, data: Any?) {

    }

    override fun onItemClick(data: Any, clickType: String) {

    }

    override fun onClick(data: Any, clickType: String) {

    }
}