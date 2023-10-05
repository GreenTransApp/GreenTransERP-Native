package com.greensoft.greentranserpnative.common

data class TimeSelection(
    private var viewToTime: String? = null,
    private var viewFromTime: String? = null,
    private var sqlFromTime: String? = null,
    private var sqlToTime: String? = null,
    private var sqlSingleTime:String? = null,
    private var viewSingleTime: String?=null
)
