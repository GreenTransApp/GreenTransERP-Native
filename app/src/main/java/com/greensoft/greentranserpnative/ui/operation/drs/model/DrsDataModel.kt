package com.greensoft.greentranserpnative.ui.operation.drs.model

data class DrsDataModel(
    val commandstatus: Int?,
    val drsdate: String,
    val drstime: String,
    val deliveredby: String?,
    val vendorname:String?,
    val vehicleno:String?,
    val driver:String?,
    val remark:String?,
    val vehiclecode:String?,
    val vendcode:String?

)
