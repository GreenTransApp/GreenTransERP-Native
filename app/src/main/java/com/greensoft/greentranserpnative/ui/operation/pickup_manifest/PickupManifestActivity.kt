package com.greensoft.greentranserpnative.ui.operation.pickup_manifest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityBookingBinding
import com.greensoft.greentranserpnative.databinding.ActivityPickupManifestBinding
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@AndroidEntryPoint
class PickupManifestActivity @Inject constructor() : BaseActivity(), OnRowClick<Any>, BottomSheetClick<Any> {
    lateinit var activityBinding: ActivityPickupManifestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding=ActivityPickupManifestBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

    }

    override fun onCLick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }
}