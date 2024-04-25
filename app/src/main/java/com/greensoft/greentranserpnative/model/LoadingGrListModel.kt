package com.greensoft.greentranserpnative.model

data class LoadingGrListModel(
    val commandstatus: Int?,
    val commandmessage: String?,
    val grno: String?,
    val custcode: String?,
    val custname: String?,
    val orgcode: String?,
    val orgname: String?,
    val destcode: String?,
    val destname: String?,
    val pckgs: Double?,
    val cweight: Double?
)