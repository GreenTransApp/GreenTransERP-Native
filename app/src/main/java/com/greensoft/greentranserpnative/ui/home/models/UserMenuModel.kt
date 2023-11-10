package com.greensoft.greentranserpnative.ui.home.models

data class UserMenuModel(
    val commandstatus: Int,
    val commandmessage: String?,
    val menuname: String,
    val menucode: String,
    val displayname: String,
    val rights: String,
    val outputid: Int,
    val outputtype: String,
    val documenttype: String,
    val cntr: Int
//    val addrec: String,
//    val cancelrec: String,
//    val deleterec: String,
//    val exestr: Any,
//    val menutype: String,
//    val modulename: String,
//    val page: String,
//    val parentcode: String,
//    val printrec: String,
//    val sequenceid: Any,
//    val updaterec: String
)