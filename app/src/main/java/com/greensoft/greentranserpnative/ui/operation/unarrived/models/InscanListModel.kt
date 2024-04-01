package com.greensoft.greentranserpnative.ui.operation.unarrived.models

data class InscanListModel(
    val branchname: String?,
    val cnge: String?,
    val cngr: String?,
    val commandmessage: Any?,
    val commandstatus: Int?,
    val despatchpckgs: Double?,
    val despatchweight: Double?,
    val fromstation: String?,
    val manifestdt: String?,
    val manifestno: String?,
    val manifesttime: String?,
    val mawbno: String?,
    val modename: String?,
    val pending: Any?,
    val receivedpckgs: Double?,
    val receivedweight: Double?,
    val sno: Int?
)