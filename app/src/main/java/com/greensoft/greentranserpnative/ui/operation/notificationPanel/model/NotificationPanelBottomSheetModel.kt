package com.greensoft.greentranserpnative.ui.operation.notificationPanel.model

data class NotificationPanelBottomSheetModel(
    val commandstatus: Int,
    val commandmessage: String?,
    val notiname: String,
    val page: String,
    val noticount: Int,
) {
    override fun toString(): String {
        return notiname
    }
}
