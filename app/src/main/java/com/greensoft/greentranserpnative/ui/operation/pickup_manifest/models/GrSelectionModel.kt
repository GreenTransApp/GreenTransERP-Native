package com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models


data class GrSelectionModel(
    val commandstatus: Long,
    val commandmessage: Any?,
    var isSelected: Boolean = false,
    val grno: String,
    val grdt: String,
    val picktime: String,
    val branchcode: String,
    val branchname: String,
    val destcode: String,
    val destname: String,
    val pickuppoint: Any?,
    val deliverypoint: Any?,
    val custcode: String,
    val custname: String?,
    val cngr: String,
    val cngrname: String,
    val cnge: String,
    val cngename: String,
    var packing: String,
    val modecode: Any?,
    val regno: Any?,
    val allocateto: Any?,
    val allocatedtoname: Any?,
    var goods: String,
    var pckgs: Any?,
    val aweight: Any?,
    var mfpckg: Any?,
    val loadingno: String,
    val loadingdt: String,
    val loadingviewdt: String,

    )
