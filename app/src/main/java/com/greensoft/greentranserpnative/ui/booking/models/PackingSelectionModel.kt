package com.greensoft.greentranserpnative.ui.booking.models

data class PackingSelectionModel(
    val breadth: Double,
    val commandmessage: Any,
    val commandstatus: Int,
    val height: Double,
    val iatavweight: Double,
    val length: Double,
    val noniatavweight: Double,
    val packingcode: String,
    val packingname: String
)