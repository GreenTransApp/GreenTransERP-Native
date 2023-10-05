package com.greensoft.greentranserpnative.common

data class CommonResult(
    val CommandMessage: Any,
    val CommandStatus: Int,
    val DataSet: String,
    val FirstParameter: Any,
    val FourthParameter: Any,
    val Message: Any,
    val SecondParameter: Any,
    val Succeed: Boolean,
    val ThirdParameter: Any
)