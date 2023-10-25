package com.greensoft.greentranserpnative.utils

import android.util.Log
import com.greensoft.greentranserpnative.ui.home.models.UserMenuModel
import com.greensoft.greentranserpnative.ui.operation.eway_bill.models.EwayBillDetailResponse
import com.greensoft.greentranserpnative.ui.operation.grList.models.GrListModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.ManifestEnteredDataModel

object Utils {
    var grModel: GrListModel? = null
    var menuModel: UserMenuModel? = null
    var manifestModel: ManifestEnteredDataModel? = null
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
    var ewayBillDetailResponse: EwayBillDetailResponse? = null
    fun logger(TAG: String?, msg: String?) {
        Log.d(TAG, msg!!)
    }
}