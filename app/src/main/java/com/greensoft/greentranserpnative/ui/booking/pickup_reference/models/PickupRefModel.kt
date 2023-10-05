package com.greensoft.greentranserpnative.ui.booking.pickup_reference.models

data class PickupRefModel(
    val commandmessage: Any,
    val commandstatus: Int?,
    val custname: String,
    val jobdate: String,
    val jobno: String,
    val jobt: String,
    val origin: String,
    val referenceno: String,
    val transactionid: Int
)