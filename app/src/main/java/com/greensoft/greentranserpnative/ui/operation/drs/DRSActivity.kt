package com.greensoft.greentranserpnative.ui.operation.drs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityDrsBinding
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DRSActivity @Inject constructor(): BaseActivity(), OnRowClick<Any>, AlertCallback<Any> {
    private lateinit var activityBinding:ActivityDrsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityDrsBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

        setObservers()
    }

    private fun setObservers(){

    }

    override fun onClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }
    override fun onAlertClick(alertClick: AlertClick, alertCallType: String, data: Any?) {
        TODO("Not yet implemented")
    }
}