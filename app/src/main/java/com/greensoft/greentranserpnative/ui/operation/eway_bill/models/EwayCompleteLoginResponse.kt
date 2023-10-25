package com.greensoft.greentranserpnative.ui.operation.eway_bill.models

data class EwayCompleteLoginResponse(
    val errorList: List<Any>,
    val message: Any,
    val response: CompleteLoginResponse,
    val status: Int
)

data class CompleteLoginResponse(
    val passwordexpired: Boolean,
    val subscription: Subscription,
    val token: String
)

data class Subscription(
    val actualcount: Int,
    val count: Int,
    val enddate: String
)