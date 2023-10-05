package com.greensoft.greentranserpnative.ui.print.dcCode.utils

import android.widget.Toast
import com.greensoft.greentranserpnative.hilt.App

object UIUtils {
    fun toast(strRes: Int) {
        Toast.makeText(App.get(), strRes, Toast.LENGTH_SHORT).show()
    }

    fun toast(str: String) {
        Toast.makeText(App.get(), str, Toast.LENGTH_SHORT).show()
    }
}