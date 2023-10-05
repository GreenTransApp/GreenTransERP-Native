package com.greensoft.greentranserpnative.ui.login.models

data class BranchModel(
    val branchcode: String,
    val branchname: String,
    val commandmessage: Any,
    val commandstatus: Int,
    val defaultbranchcode: String,
    val defaultbranchname: String
) {
    override fun toString(): String {
        return branchname
    }
}
