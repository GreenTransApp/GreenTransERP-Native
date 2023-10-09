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
    val orgcode: String,
    val orgname: String,
    val destname: String,
    val destcode: String,
    val transactionid: Int,
    val vehiclerequired: String,
    val weight: Double
)