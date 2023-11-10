package com.greensoft.greentranserpnative.ui.operation.counterDetail.model

data class NotificationPanelBottomSheetModel(
    val notiname: String,
    val noticounter: String,
) {
    override fun toString(): String {
        return notiname
    }
}
