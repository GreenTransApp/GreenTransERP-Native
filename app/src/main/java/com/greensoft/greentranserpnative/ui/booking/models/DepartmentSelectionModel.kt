package com.greensoft.greentranserpnative.ui.booking.models

data class DepartmentSelectionModel(
    val commandmessage: Any,
    val commandstatus: Int,
    val billcategory: String,
    val custdeptid: Int,
    val custdeptname: String,
    val gstno: Any,
    val state: String
)