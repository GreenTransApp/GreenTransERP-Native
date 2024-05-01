package com.greensoft.greentranserpnative.ui.bottomsheet.signBottomSheet

import android.graphics.Bitmap

interface SignatureBottomSheetCompleteListener {
    fun onSignComplete(clickType: String, imageBitmap: Bitmap) {}
    fun onSignCompleteWithAdapter(clickType: String, imageBitmap: Bitmap, index: Int) {}

}