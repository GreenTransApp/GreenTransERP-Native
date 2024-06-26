package com.greensoft.greentranserpnative.ui.bottomsheet.receivingDetails.models

data class ReceivingDetailsEnteredDataModel(
    var manifestNo: String?,
    var receivingViewDate: String?,
    var receivingSqlDate: String?,
    var receivingViewTime: String?,
    var receivingUserName: String?,
    var receivingUserCode: String?,
    var receivingRemarks: String?
) {
    override fun toString(): String {
        return "manifestNo: $manifestNo, receivingViewDate: $receivingViewDate, " +
        "receivingSqlDate: $receivingSqlDate, receivingViewTime: $receivingViewTime, receivingUserName: $receivingUserName, "+
        "receivingUserCode: $receivingUserCode, receivingRemarks: $receivingRemarks"
    }
}
