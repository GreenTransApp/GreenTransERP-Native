package com.greensoft.greentranserpnative.ui.operation.upload_image

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.lifecycle.lifecycleScope
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityUploadPodImageBinding
import com.greensoft.greentranserpnative.ui.operation.upload_image.viewmodel.UploadImageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UploadPodImageActivity @Inject constructor() : BaseActivity() {
    private lateinit var activityBinding: ActivityUploadPodImageBinding
    private val viewModel: UploadImageViewModel by viewModels()
    private var imageBase64: String = ""
    private var isGrValidated: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityUploadPodImageBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("Upload POD Image")
        setObservers()
        setOnClicks()
        resetPage()
    }


    private fun getImageFromGR(gr: String) {
        viewModel.validateGrForPodUpload(
            companyId = getCompanyId(),
            grNo = gr
        )
    }

    private fun setOnClicks() {
        imageClicked.observe(this) { clicked ->
            if(clicked) {
                successToast("CLICKED")
                var imageBitmap = imageBitmapList.get(imageBitmapList.size - 1)
                imageBase64 = imageBase64List.get(imageBase64List.size - 1)
                activityBinding.ivAddImage.setImageBitmap(imageBitmap)
            }
        }
        activityBinding.ivAddImage.setOnClickListener {
            showImageDialog()
        }
        activityBinding.btnGetImage.setOnClickListener {
            var gr = activityBinding.inputGr.text.toString()
            if(gr.isNullOrBlank()) {
                errorToast("Please enter GR #.")
            } else {
                getImageFromGR(gr)
            }
        }
        activityBinding.btnSave.setOnClickListener {
            if(imageBase64 == "") {
                errorToast("Please select/click an image for upload.")
            } else {
                var gr = activityBinding.inputGr.text.toString()
                uploadPodImage(gr, imageBase64)
            }
        }
    }

    private fun uploadPodImage(grNo: String, imageBase64: String) {
        viewModel.uploadPodImage(
            companyId = getCompanyId(),
            grNo = grNo,
            imageBase64 = imageBase64,
            signImage = "",
            userCode = getUserCode(),
            menuCode = "GTAPP_UPLOADPOD",
            sessionId = getSessionId()
        )
    }

    private fun setObservers() {
        activityBinding.swipeRefreshLayout.setOnRefreshListener {
            onRefresh()
        }
        viewModel.validateGrForPodLiveData.observe(this) { isGrValidated ->
            this.isGrValidated = isGrValidated
        }
        viewModel.isError.observe(this) { errMsg ->
            errorToast(errMsg)
        }
        viewModel.viewDialogLiveData.observe(this) { showProgressDialog ->
            if(showProgressDialog) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }
        viewModel.uploadPodImageLiveData.observe(this) {
            if(it.commandstatus == 1) {
                successToast(it.commandmessage ?: "Pod Image Uploaded Successfully.")
                resetPage()
            }
        }
    }

    private fun resetPage() {
        imageBase64 = ""
        isGrValidated = false
        activityBinding.inputGr.setText("")
        activityBinding.ivAddImage.visibility = View.GONE
        activityBinding.ivAddImage.setImageResource(R.drawable.baseline_add_a_photo_24)
    }

    private fun onRefresh() {
        resetPage()
        lifecycleScope.launch {
            delay(1500)
            activityBinding.swipeRefreshLayout.isRefreshing = false
        }
    }
}