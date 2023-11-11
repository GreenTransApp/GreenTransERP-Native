package com.greensoft.greentranserpnative.ui.operation.notificationPanel.model

data class NotificationPanelBottomSheetModel(
    val notiname: String,
    val noticounter: String,
) {
    override fun toString(): String {
        return notiname
    }
}
