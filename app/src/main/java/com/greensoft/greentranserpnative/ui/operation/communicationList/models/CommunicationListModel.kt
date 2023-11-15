package com.greensoft.greentranserpnative.ui.operation.communicationList.models

data class CommunicationListModel (
    var commandstatus: Int,
    var commandmessage: String?,
    var transactionid: Int?,
    var custname: String?,
    var custcode: String?,
    var destcode: String?,
    var dest: String?,
    var orgcode: String?,
    var origin: String?,
    val totalcustcomments: Int
)