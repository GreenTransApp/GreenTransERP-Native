package com.greensoft.greentranserpnative.ui.operation.loadingSlip.newScanLoad.models

data class LoadingSlipHeaderDataModel(
    val commandmessage: Any,
    val commandstatus: Int,
    val partcode: String,
    val partqty: Int,
    val total: Int,
    val totalbalance: Int,
    val totalscanned: Int
)