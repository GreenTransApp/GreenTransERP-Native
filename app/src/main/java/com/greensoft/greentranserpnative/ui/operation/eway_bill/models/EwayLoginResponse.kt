package com.greensoft.greentranserpnative.ui.operation.eway_bill.models

data class EwayLoginResponse(
    val errorList: List<EwayLoginErrorList>,
    val message: String,
    val response: LoginResponse,
    val status: Int
)

data class EwayLoginErrorList(
    val field: String?,
    val message: String?
)

data class LoginResponse(
    val mfaEnabled: Boolean,
    val orgs: List<Org>,
    val passwordexpired: Boolean,
    val token: String
)


data class Org(
    val orgId: Int,
    val orgname: String,
    val sysadmin: Int
)