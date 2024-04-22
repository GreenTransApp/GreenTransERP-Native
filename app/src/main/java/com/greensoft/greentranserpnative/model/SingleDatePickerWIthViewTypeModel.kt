package com.greensoft.greentranserpnative.model

import com.greensoft.greentranserpnative.common.PeriodSelection

data class SingleDatePickerWIthViewTypeModel(
    val viewType: String,
    val periodSelection: PeriodSelection,
    val withAdapter: Boolean = false,
    val index: Int = -1
)
