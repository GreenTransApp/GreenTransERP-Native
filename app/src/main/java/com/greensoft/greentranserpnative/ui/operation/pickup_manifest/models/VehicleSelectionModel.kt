package com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models

data class VehicleSelectionModel(
    val commandstatus:Int,
    val commandmessage:String,
    val capacity: Any,
    val regno: String,
    val vehiclecode: String
)