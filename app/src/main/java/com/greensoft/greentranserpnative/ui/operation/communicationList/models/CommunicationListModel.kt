package com.greensoft.greentranserpnative.ui.operation.communicationList.models

data class CommunicationListModel (
    var commandstatus: Any,
    var commandmessage: Int?,
    var transactionid: Int?,
    var custname: String?,
    var custcode: String?,
    var destcode: String?,
    var dest: String?,
    var orgcode: String?,
    var origin: String?,
)