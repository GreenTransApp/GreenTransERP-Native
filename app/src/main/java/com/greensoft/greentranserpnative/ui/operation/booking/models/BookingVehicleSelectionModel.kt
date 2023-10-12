package com.greensoft.greentranserpnative.ui.operation.booking.models

data class BookingVehicleSelectionModel(
    val capacity: Double,
    val category: String,
    val commandstatus: Int,
    val groupname: String,
    val owned: String,
    val ownername: Any,
    val panno: String,
    val regno: String,
    val typecode: String,
    val typename: String,
    val vehiclecode: String,
    val vendcode: Any,
    val vendname: Any
)