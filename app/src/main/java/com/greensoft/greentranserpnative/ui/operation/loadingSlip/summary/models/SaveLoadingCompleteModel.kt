package com.greensoft.greentranserpnative.ui.operation.loadingSlip.summary.models

data class SaveLoadingCompleteModel(
    val balancepckgs: Double,
    val bookedpckgs: Double,
    val bookingdt: String,
    val cnge: String,
    val cngr: String,
    val commandmessage: Any,
    val commandstatus: Int,
    val despatchedpckgs: Double,
    val destination: String,
    val grno: String,
    val grossweight: Double,
    val loadedpckgs: Int,
    val loadedweight: Double,
    val origin: String,
    val sno: Int,
    val manifestno: String?
)