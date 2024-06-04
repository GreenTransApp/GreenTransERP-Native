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
    val cngeaddress: String?,
    val cngecity: String?,
    val cngecode: String?,
    val cngecstno: String?,
    val cngeemail: String?,
    val cngelstno: String?,
    val cngestate: String?,
    val cngestaxregno: String?,
    val cngetelno: String?,
    val cngetinno: String?,
    val cngezipcode: String?,
    val cngr: String?,
    val cngraddress: String?,
    val cngrcity: String?,
    val cngrcode: String?,
    val cngrcstno: String?,
    val cngremail: String?,
    val cngrlstno: String?,
    val cngrstate: String?,
    val cngrstaxregno: String?,
    val cngrtelno: String?,
    val cngrtinno: String?,
    val cngrzipcode: String?,
    val commandmessage: String?,
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
    var packagetype: String?,
    var packing: String,
    var packingcode: String?,
    val pckgbreath: Double,
    val pckgheight: Double,
    val pckglength: Double,
    var localBreath: Double,
    var localHeight: Double,
    var localLength: Double,
    val pckgs: Int,
    var pcs: Int,
    var referenceno: String?,
    val servicetype: String,
    val stockitemcode: String,
    var tempurature: String?,
    var tempuratureCode: String,
    val transactionid: Int,
    var volfactor: Float,
    var weight: Double,
    var localVWeight: Float,
    var isBoxValidated: Boolean
) {

    init {
        Log.d("SinglePickupRefModel", "Single Pickup Ref Model Init Called: ")
        localLength = pckglength
        localBreath = pckgbreath
        localHeight = pckgheight
    }

    // Copy constructor
    constructor(original: SinglePickupRefModel) : this(
        Departmentcode = original.Departmentcode,
        Origin = original.Origin,
        weight = original.weight,
        boxno = original.boxno,
        branchname = original.branchname,
        cnge = original.cnge,
        cngeaddress = original.cngeaddress,
        cngecity = original.cngecity,
        cngecode = original.cngecode,
        cngecstno = original.cngecstno,
        cngeemail = original.cngeemail,
        cngelstno = original.cngelstno,
        cngestate = original.cngestate,
        cngestaxregno = original.cngestaxregno,
        cngetelno = original.cngetelno,
        cngetinno = original.cngetinno,
        cngezipcode = original.cngezipcode,
        cngr = original.cngr,
        cngraddress = original.cngraddress,
        cngrcity = original.cngrcity,
        cngrcode = original.cngrcode,
        cngrcstno = original.cngrcstno,
        cngremail = original.cngremail,
        cngrlstno = original.cngrlstno,
        cngrstate = original.cngrstate,
        cngrstaxregno = original.cngrstaxregno,
        cngrtelno = original.cngrtelno,
        cngrtinno = original.cngrtinno,
        cngrzipcode = original.cngrzipcode,
        commandmessage = original.commandmessage,
        commandstatus = original.commandstatus,
        custcode = original.custcode,
        custname = original.custname,
        datalogger = original.datalogger,
        dataloggerno = original.dataloggerno,
        departmentname = original.departmentname,
        destcode = original.destcode,
        destname = original.destname,
        iatavolfactor = original.iatavolfactor,
        orgcode = original.orgcode,
        packagetype = original.packagetype,
        transactionid = original.transactionid,


        packing = "",
        packingcode = "",
        pckgbreath = 0.0,
        pckgheight = 0.0,
        pckglength = 0.0,
        localBreath = 0.0,
        localHeight = 0.0,
        localLength = 0.0,
        pckgs = 0,
        pcs = 0,
        referenceno = "",
        contents = "",
        contentsCode = "",
        servicetype = "",
        stockitemcode = "",
        tempurature = "",
        tempuratureCode = "",
        volfactor = 0.0f,
        aweight = 0.0,
        localVWeight = 0.0f,
        isBoxValidated = false,
        dryice = "",
        dryiceqty = 0.0,
        detailreferenceno = "",
        enabledryiceqty = "",
        gelpack = "",
        gelpackqty = 0,
        gelpacktype = "",
        gelpackitemcode = "",
        goods = "",
//        packing = original.packing,
//        packingcode = original.packingcode,
//        pckgbreath = original.pckgbreath,
//        pckgheight = original.pckgheight,
//        pckglength = original.pckglength,
//        localBreath = original.localBreath,
//        localHeight = original.localHeight,
//        localLength = original.localLength,
//        pckgs = original.pckgs,
//        pcs = original.pcs,
//        referenceno = original.referenceno,
//        servicetype = original.servicetype,
//        stockitemcode = original.stockitemcode,
//        tempurature = original.tempurature,
//        tempuratureCode = original.tempuratureCode,
//        transactionid = original.transactionid,
//        volfactor = original.volfactor,
//        weight = original.weight,
//        localVWeight = original.localVWeight,
//        isBoxValidated = original.isBoxValidated
    ) {
        Log.d("SinglePickupRefModel", "COPY CONSTRUCTOR")
    }

}
