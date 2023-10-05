package com.greensoft.greentranserpnative.utils

import android.util.Log

object Utils {
    var custCode = ""
    var fromDate = ""
    var toDate = ""
    var loadingNo = ""
    var manifestNo = ""
    var arrivalBranchCode = ""
    var receiveDate = ""
    var receiveViewDate = ""
    var receiveTime = ""
    var goDownName = ""
    var goDownId = ""
    var remarks = ""
    var manifestType = ""
    var vehicleCode = ""
    var driverCode = ""
    var fromStationCode = ""
    fun logger(TAG: String?, msg: String?) {
        Log.d(TAG, msg!!)
    }
}