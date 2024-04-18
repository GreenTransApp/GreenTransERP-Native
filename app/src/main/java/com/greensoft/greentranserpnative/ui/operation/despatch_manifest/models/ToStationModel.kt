package com.greensoft.greentranserpnative.ui.operation.despatch_manifest.models

data class ToStationModel(
    val Active: String?,
    val Delivery: String?,
    val StnCity: String?,
    val StnCode: String?,
    val StnName: String?,
    val StnState: String?,
    val allowarrivalatsameasbranch: String?,
    val commandmessage: Any?,
    val commandstatus: Int?,
    val unscheduled: String?
)