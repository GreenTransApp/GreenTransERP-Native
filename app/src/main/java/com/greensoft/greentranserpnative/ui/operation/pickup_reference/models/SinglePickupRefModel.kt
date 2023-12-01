package com.greensoft.greentranserpnative.ui.operation.pickup_reference.models

import android.util.Log
import androidx.compose.ui.text.font.FontWeight

data class SinglePickupRefModel(
    val Departmentcode: Int?,
    val Origin: String?,
    val aweight: Double,
    var boxno: String?,
    val branchname: String,
    val cnge: String?,
    val cngeaddress: Any,
    val cngecity: Any,
    val cngecode: String?,
    val cngecstno: Any,
    val cngeemail: Any,
    val cngelstno: Any,
    val cngestate: Any,
    val cngestaxregno: Any,
    val cngetelno: Any,
    val cngetinno: Any,
    val cngezipcode: Any?,
    val cngr: String?,
    val cngraddress: Any,
    val cngrcity: Any,
    val cngrcode: String?,
    val cngrcstno: Any,
    val cngremail: Any,
    val cngrlstno: Any,
    val cngrstate: Any,
    val cngrstaxregno: Any,
    val cngrtelno: Any,
    val cngrtinno: Any,
    val cngrzipcode: Any,
    val commandmessage: Any,
    val commandstatus: Int,
    var contents: String,
    var contentsCode: String,
    val custcode: String?,
    val custname: String,
    val datalogger: String,
    var dataloggerno: String?,
    val departmentname: String,
    val destcode: String?,
    val destname: String,
    val detailreferenceno: String,
    val dryice: String,
    val dryiceqty: Double,
    val enabledryiceqty: String,
    var gelpack: String = "N",
    val gelpackqty: Int,
    var gelpacktype: String,
    var gelpackitemcode: String?,
    val goods: String,
    val iatavolfactor: Int,
    val orgcode: String?,
    val packagetype: String?,
    var packing: String,
    var packingcode: String?,
    val pckgbreath: Double,
    val pckgheight: Double,
    val pckglength: Double,
    var localBreath: Double,
    var localHeight: Double,
    var localLength: Double,
    val pckgs: Int,
    val pcs: Int,
    var referenceno: Any?,
    val servicetype: String,
    val stockitemcode: String,
    var tempurature: String?,
    var tempuratureCode: String,
    val transactionid: Int,
    var volfactor: Float,
    val weight: Double,
    var localVWeight: Float,
    var isBoxValidated: Boolean
) {

    init {
        Log.d("SinglePickupRefModel", "Single Pickup Ref Model Init Called: ")
        localLength = pckglength
        localBreath = pckgbreath
        localHeight = pckgheight
    }
}
