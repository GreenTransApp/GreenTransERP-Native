package com.greensoft.greentranserpnative.ui.operation.eway_bill.models

data class EwayBillCredentialsModel(
    val commandstatus: Int?,
    val commandmessage: String?,
    val ewayuserid: String?,
    val ewaypassword: String?,
    val compgstin: String?
)
