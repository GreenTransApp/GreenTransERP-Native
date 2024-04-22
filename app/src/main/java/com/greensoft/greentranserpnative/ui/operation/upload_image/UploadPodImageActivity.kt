package com.greensoft.greentranserpnative.ui.operation.upload_image

import android.os.Bundle
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityUploadPodImageBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UploadPodImageActivity @Inject constructor() : BaseActivity() {
    private lateinit var activityBinding: ActivityUploadPodImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityUploadPodImageBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("Upload POD Image")
        setObservers()
        setOnClicks()
    }

    private fun getImageFromGR(gr: String) {

    }

    private fun setOnClicks() {
        activityBinding.btnGetImage.setOnClickListener {
            var gr = activityBinding.inputGr.text.toString()
            if(gr.isNullOrBlank()) {
                errorToast("Please enter GR #.")
            } else {
                getImageFromGR(gr)
            }
        }
        activityBinding.btnSave.setOnClickListener {
            var imageClicked = activityBinding.ivAddImage.drawable
        }
    }

    private fun setObservers() {

    }

    private fun onRefresh() {

    }
}