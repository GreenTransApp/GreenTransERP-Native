package com.greensoft.greentranserpnative.ui.operation.chatScreen.models

data class ChatScreenModel (
//    table:
    val boytype: String,
    val drivername: String,
    val emailid: String,
    val imagepath: Any,
    val licenseno: String,
    val mobileno: String,
    val pagetitle: String,

//    table 1:
    val appdatetime: String,
    val boyid: Any,
    val commandmessage: Any,
    val commandstatus: Int,
    val createdon: String,
    val custcode: String,
    val dataid: Int,
    val drivercode: Any,
    val message: String,
    val readstatus: String,
    val sendername: String,
    val sendertype: String
)