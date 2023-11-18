package com.greensoft.greentranserpnative.ui.operation.loadingSlip.newScanLoad.models

data class LoadedStickerModel (
    val aweight: Double,
    val commandmessage: Any,
    val commandstatus: Int,
    val consignee: String,
    val consignor: String,
    val cweight: Double,
    val dataid: Int,
    val destination: String,
    val grdate: String,
    val grno: String,
    val origin: String,
    val pmark: Any,
    val rowno: Int,
    val stickerno: String,
    val vweight: Double
)
