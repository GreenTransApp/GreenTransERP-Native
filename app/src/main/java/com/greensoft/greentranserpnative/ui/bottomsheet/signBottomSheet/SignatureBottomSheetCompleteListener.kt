package com.greensoft.greentranserpnative.ui.bottomsheet.signBottomSheet

import android.graphics.Bitmap

interface SignatureBottomSheetCompleteListener {
    fun onComplete(clickType: String, imageBitmap: Bitmap)
}