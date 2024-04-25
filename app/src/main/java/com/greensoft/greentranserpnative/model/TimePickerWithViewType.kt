package com.greensoft.greentranserpnative.model

import com.greensoft.greentranserpnative.common.TimeSelection

data class TimePickerWithViewType(
    val viewType: String,
    val timeSelection: String,
    val withAdapter: Boolean = false,
    val index: Int = -1
)
