package com.greensoft.greentranserpnative.ui.login.models

data class LoginDataModel(
    val commandmessage: String,
    val commandstatus: Int,
    val companyid: Int,
    val compgstin: Any,
    val compname: String,
    val connstring: String,
    val dbname: String,
    val dbpassword: String,
    val displayname: String,
    val divisionlogin: String,
    val emailpassword: Any,
    val emailusername: Any,
    val enableeway: String,
    val ewaypassword: Any,
    val ewayurl: Any,
    val ewayuserid: Any,
    val grouplogin: String,
    val host: Any,
    val location: String,
    val logoimage: String?,
    val mobileno: String,
    val port: Any,
    val serverip: String,
    val smtpfrom: Any,
    val username: String,
    val password: String
)
