package com.greensoft.greentranserpnative.ui.operation.drs.model

data class VehicleModelDRS(
    val capacity: Double,
    val commandmessage: Any,
    val commandstatus: Int,
    val modetype: String,
    val regno: String,
    val typename: String,
    val vehiclecode: String
)