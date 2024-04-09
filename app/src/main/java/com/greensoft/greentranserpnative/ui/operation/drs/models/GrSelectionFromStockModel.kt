package com.greensoft.greentranserpnative.ui.operation.drs.models

data class GrSelectionFromStockModel(
    val aweight: Double?,
    val cnge: String?,
    val cngr: String?,
    val commandmessage: Any?,
    val commandstatus: Int?,
    val customer: String?,
    val dataid: Any?,
    val date: String?,
    val destcode: Any?,
    val destination: String?,
    val documentno: String?,
    val documenttype: String?,
    val orgcode: Any?,
    val origin: String?,
    val outputid: Int?,
    val outputtype: String?,
    val pcs: Int?,
    val time: String?,
    var isSelected: Boolean,

    )