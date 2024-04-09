package com.greensoft.greentranserpnative.ui.operation.pod_entry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityPodEntryBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PodEntryActivity  @Inject constructor(): BaseActivity(), OnRowClick<Any> {
    lateinit var  activityBinding:ActivityPodEntryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding= ActivityPodEntryBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("POD Entry")
    }

    override fun onClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }
}