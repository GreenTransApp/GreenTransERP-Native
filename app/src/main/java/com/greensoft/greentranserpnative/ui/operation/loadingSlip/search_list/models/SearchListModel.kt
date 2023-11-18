package com.greensoft.greentranserpnative.ui.operation.loadingSlip.search_list.models

data class SearchListModel(
    val branchname: String,
    val commandmessage: Any,
    val commandstatus: Int,
    val destinationname: String,
    val drivername: String,
    val loadingdate: String,
    val loadingno: String,
    val sno: Int,
    val vehicleno: String
)
