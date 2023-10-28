package com.greensoft.greentranserpnative.utils

import android.util.Log
import com.greensoft.greentranserpnative.ui.home.models.UserMenuModel
import com.greensoft.greentranserpnative.ui.operation.eway_bill.ItemEwayBillModel
import com.greensoft.greentranserpnative.ui.operation.eway_bill.models.EwayBillDetailResponse
import com.greensoft.greentranserpnative.ui.operation.grList.models.GrListModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.ManifestEnteredDataModel
import java.text.SimpleDateFormat
import java.util.*

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
    var ewayBillValidated = false
    var ewayBillDetailResponse: EwayBillDetailResponse? = null
    var enteredValidatedEwayBillList: ArrayList<ItemEwayBillModel>? = null
    var enteredEwayBillValidatedData: ArrayList<EwayBillDetailResponse> = ArrayList()
    fun logger(TAG: String?, msg: String?) {
        Log.d(TAG, msg!!)
    }
//    fun changeDateFormatForEway(date: String?): String {
//        if(date == null) return BaseActivity.getSqlCurrentDate()
////        var dateSplittedVal: List<String> = date.split(Pattern().split("/"),0)
//        var dateTimeAmPMSplitted = date.split(" ").toTypedArray()
//        var da
//    }

    fun changeDateFormatForEway(date: String?): String {
//        val parser: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss a", Locale.ENGLISH)
//        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
//        val ldt: LocalDateTime = LocalDateTime.parse(date, parser)
//        val formatted: String = ldt.format(formatter)


        val sqlFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val ewayFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss a")
        val date: Date = ewayFormat.parse(date)

//        Log.d("DATE", (parseFormat.format(date).toString() + " - " + displayFormat.format(date)).toString())
        Log.d("DATE", sqlFormat.format(date).toString())
        return sqlFormat.format(date)
    }
}