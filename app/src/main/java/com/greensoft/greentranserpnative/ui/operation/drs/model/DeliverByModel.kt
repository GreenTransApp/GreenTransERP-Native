package com.greensoft.greentranserpnative.ui.operation.drs.model

data class DeliverByModel(
    val name: String,
    val code: String
) {
    override fun toString(): String {
        return name
    }
}
