package com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models

data class ManifestEnteredDataModel(
    var branchName:String,
    var branchCode:String,
    var manifestNo:String,
    var pickupLocation:String,
    var driverCode:String,
    var manifestDt:String,
    var manifestTime:String,
    var driverName:String,
    var drivermobile:String,
    var vehicleType:String,
    var vendorName:String,
    var vendorCode:String,
    var vehicleCode:String,
    var vehicleNo:String,
    var capacity:String,
    var loadedBy:String?,
    var areaCode:String,
    var remark:String,

    )
