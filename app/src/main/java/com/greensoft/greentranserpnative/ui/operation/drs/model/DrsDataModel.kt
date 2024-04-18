package com.greensoft.greentranserpnative.ui.operation.drs.model

data class DrsDataModel(
    val commandstatus: Int?,
    val commandmessage: String?,
    val drsdate: String,
    val drstime: String,
    val deliveredby: String?,
    val vendcode:String?,
    val vendorname:String?,
    val vehiclecode:String?,
    val vehicleno:String?,
    val usercode:String?,
    val username:String?,
    val remarks:String?

)
