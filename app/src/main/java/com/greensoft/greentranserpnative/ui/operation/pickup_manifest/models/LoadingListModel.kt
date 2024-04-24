package com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models

data class LoadingListModel(
    val commandmessage: Any?,
    val commandstatus: Int?,
    val loadingdt: String?,
    val loadingno: String?,
    val loadingtime: String?,
    val loadingviewdt: String?,
    val noofgr: Int,
    var isSelected: Boolean = false,
)