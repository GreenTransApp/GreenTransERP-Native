package com.greensoft.greentranserpnative.ui.operation.booking.models

data class ServiceTypeSelectionLov(
    val commandmessage: String,
    val value: String,
    val text: String,
    val displayname: String,

)
{
    override fun toString(): String {
        return text
    }
}

