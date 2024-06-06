package com.greensoft.greentranserpnative.ui.operation.upload_image

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityUploadBookingImageBinding
import com.greensoft.greentranserpnative.ui.operation.upload_image.viewmodel.UploadImageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UploadBookingImageActivity @Inject constructor() : BaseActivity() {
    private lateinit var activityBinding: ActivityUploadBookingImageBinding
    private val viewModel: UploadImageViewModel by viewModels()
    private var imageBase64: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityUploadBookingImageBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("Upload Booking Image")
        setObservers()
        setOnClicks()
    }

    override fun onResume() {
        super.onResume()
//        onRefresh()
    }

    private fun uploadBookingImage(base64Image: String, grNo: String) {
        viewModel.uploadBookingImage(
            companyId = getCompanyId(),
            transactionId = "0",
            titleName = "BOOKING",
            imageBase64 = base64Image,
            userCode = getUserCode(),
            sessionId = getSessionId(),
            menuCode = "GTAPP_BOOKING",
            masterCode = grNo,
            branchCode = getLoginBranchCode()
        )
    }


    private fun setOnClicks() {
//        activityBinding.btnGetImage.setOnClickListener {
//            var gr = activityBinding.inputGr.text.toString()
//            if(gr.isNullOrBlank()) {
//                errorToast("Please enter GR #.")
//            } else {
//                getImageFromGR(gr)
//            }
//        }
        activityBinding.ivAddImage.setOnClickListener {
            showImageDialog()
        }

        activityBinding.btnSave.setOnClickListener {
            validateBeforeSave()
        }
    }

    private fun validateBeforeSave() {
        var gr = activityBinding.inputGr.text.toString()
        if(gr.isBlank()) {
            errorToast("Please enter GR #.")
        } else {
//            var imageClicked = activityBinding.ivAddImage.drawable
            var grNo: String? = activityBinding.inputGr.text.toString()
            if(grNo.isNullOrBlank()) {
                errorToast("Please enter a GR # to upload.")
            } else {
                if(imageBase64.isBlank()) {
                    errorToast("Please click an image to upload.")
                } else {
                    uploadBookingImage(imageBase64, grNo)
                }
            }
        }
    }

    private fun setObservers() {
        imageClicked.observe(this) { clicked ->
            if(clicked) {
//                successToast("CLICKED")
                var imageBitmap = imageBitmapList.get(imageBitmapList.size - 1)
                imageBase64 = imageBase64List.get(imageBase64List.size - 1)
                activityBinding.ivAddImage.setImageBitmap(imageBitmap)
            }
        }
        activityBinding.swipeRefreshLayout.setOnRefreshListener {
            onRefresh()
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
        viewModel.uploadBookingImageLiveData.observe(this) {
            if(it.commandstatus == 1) {
                successToast(it.commandmessage ?: "Booking Image Uploaded Successfully.")
                resetPage()
            }
        }
    }

    private fun resetPage() {
        imageBase64 = ""
        activityBinding.inputGr.setText("")
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