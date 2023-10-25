package com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models

data class VehicleTypeModel(
    val commandmessage: String,
    val commandstatus: Int,
    val text: String,
    val value: String
)

{
    override fun toString(): String {
        return text
    }
}