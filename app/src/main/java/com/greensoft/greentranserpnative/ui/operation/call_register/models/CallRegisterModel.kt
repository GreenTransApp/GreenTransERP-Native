package com.greensoft.greentranserpnative.ui.operation.call_register.models

data class CallRegisterModel(
    val commandmessage: String?,
    val commandstatus: Int?,
    val calldt: String,
    val calltime: String,
    val custcode: String,
    val custname: String,
    val drivercode: Any,
    val drivername: Any,
    val ftl: String,
    val materialtype: String,
    val pcs: Int,
    val productcode: Any,
    val stncode: String,
    val stncode1: String,
    val stnname: String,
    val stnname1: String,
    val transactionid: Int,
    val vehiclerequired: String,
    val weight: Double
)