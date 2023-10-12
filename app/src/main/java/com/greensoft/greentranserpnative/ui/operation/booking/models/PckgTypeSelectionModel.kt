package com.greensoft.greentranserpnative.ui.operation.booking.models

data class PckgTypeSelectionModel(
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
