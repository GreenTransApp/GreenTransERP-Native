package com.greensoft.greentranserpnative.ui.operation.upload_image

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityUploadBookingImageBinding
import com.greensoft.greentranserpnative.ui.operation.upload_image.viewmodel.UploadImageViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UploadBookingImageActivity @Inject constructor() : BaseActivity() {
    private lateinit var activityBinding: ActivityUploadBookingImageBinding
    private val viewModel: UploadImageViewModel by viewModels()
    private val base64Image: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityUploadBookingImageBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("Upload Booking Image")
        setObservers()
        setOnClicks()
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
                if(base64Image.isBlank()) {
                    errorToast("Please click an image to upload.")
                } else {
                    uploadBookingImage(base64Image, grNo)
                }
            }
        }
    }

    private fun setObservers() {

    }

    private fun onRefresh() {

    }
}