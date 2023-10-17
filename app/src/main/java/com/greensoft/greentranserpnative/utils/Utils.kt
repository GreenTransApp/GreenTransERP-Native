package com.greensoft.greentranserpnative.utils

import android.util.Log
import com.greensoft.greentranserpnative.ui.home.models.UserMenuModel
import com.greensoft.greentranserpnative.ui.operation.grList.models.GrListModel

object Utils {
    var grModel: GrListModel? = null
    var menuModel: UserMenuModel? = null
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