package com.greensoft.greentranserpnative.ui.operation.pickup_manifest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityGrSelectionBinding
import com.greensoft.greentranserpnative.databinding.ActivitySavePickupManifestBinding
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SavePickupManifestActivity @Inject constructor() : BaseActivity(), OnRowClick<Any>,
    BottomSheetClick<Any> {
    lateinit var  activityBinding: ActivitySavePickupManifestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding=ActivitySavePickupManifestBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
    }

    override fun onItemClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }

    override fun onCLick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }
}