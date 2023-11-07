package com.greensoft.greentranserpnative.ui.bottomsheet.attachedImages

import android.graphics.Bitmap
import android.net.Uri

interface OnAddImageClickListener {
//    fun addImage(clickType: String, imageBitmap: Bitmap, imageBase64: String, imageUri: Uri)
    fun addImage(clickType: String, imageBitmap: Bitmap)
}