package com.greensoft.greentranserpnative.ui.operation.despatch_manifest.models

data class DespatchManifestEnteredDataModel(
    var branchName:String,
    var branchCode:String,
    var manifestNo:String,
    var tostation:String,
    var driverCode:String,
    var manifestDt:String,
    var manifestTime:String,
    var driverName:String,
    var drivermobile:String,
    var vendorName:String,
    var vendorCode:String,
    var vehicleCode:String,
    var vehicleNo:String,
    var areaCode:String,
    var remark:String,
    var groupCode:String?,
    var modeCode:String?,
    var pckgs:String?,
    var modeTypeCode:String,
)
