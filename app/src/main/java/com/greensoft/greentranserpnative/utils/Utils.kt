package com.greensoft.greentranserpnative.utils

import android.graphics.Bitmap
import android.net.Uri
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

    var imageBitmap: Bitmap? = null
    var imageUri: Uri? = null
    fun logger(TAG: String?, msg: String?) {
        Log.d(TAG, msg!!)
    }

    fun isNumberEntered(number: String?): Boolean {
        logger("isNumberEntered", number.toString())
        val enteredNum = number.toString().toIntOrNull()
        if(number.isNullOrBlank() || enteredNum == null || enteredNum <= 0) {
            return false
        }
        return true
    }
    fun isNumberNotEntered(number: String?): Boolean {
        logger("isNumberNotEntered", number.toString())
        val enteredNum = number.toString().toIntOrNull()
        if(number.isNullOrBlank() || enteredNum == null || enteredNum <= 0) {
            return true
        }
        return false
    }
    fun isDecimalEntered(number: String?): Boolean {
        logger("isNumberEntered", number.toString())
        val enteredNum = number.toString().toDoubleOrNull()
        if(number.isNullOrBlank() || enteredNum == null || enteredNum <= 0.0) {
            return false
        }
        return true
    }
    fun isDecimalNotEntered(number: String?): Boolean {
        logger("isNumberNotEntered", number.toString())
        val enteredNum = number.toString().toDoubleOrNull()
        if(number.isNullOrBlank() || enteredNum == null || enteredNum <= 0.0) {
            return true
        }
        return false
    }
    fun isStringNotEntered(number: String?): Boolean {
        val enteredNum = number.toString()
        if(number.isNullOrBlank() || enteredNum == null || enteredNum <= "") {
            return true
        }
        return false
    }

//    fun changeDateFormatForEway(date: String?): String {
//        if(date == null) return BaseActivity.getSqlCurrentDate()
////        var dateSplittedVal: List<String> = date.split(Pattern().split("/"),0)
//        var dateTimeAmPMSplitted = date.split(" ").toTypedArray()
//        var da
//    }

    fun convert2SmallDateTime(date: String): String {
        if(date.length >= 10) return ""
        return date.subSequence(0, 9).toString()
    }

    fun changeDateFormatForEway(date: String): String {
//        val parser: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss a", Locale.ENGLISH)
//        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
//        val ldt: LocalDateTime = LocalDateTime.parse(date, parser)
//        val formatted: String = ldt.format(formatter)


        val sqlFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val ewayFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss a")
        val date: Date = ewayFormat.parse(date)

//        Log.d("DATE", (parseFormat.format(date).toString() + " - " + displayFormat.format(date)).toString())
        Log.d("DATE", sqlFormat.format(date).toString())
        return convert2SmallDateTime(sqlFormat.format(date))
    }

    fun changeDateFormatForEwayInvoiceDt(date: String): String {
//        val parser: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss a", Locale.ENGLISH)
//        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
//        val ldt: LocalDateTime = LocalDateTime.parse(date, parser)
//        val formatted: String = ldt.format(formatter)


        val sqlFormat = SimpleDateFormat("yyyy-MM-dd")
        val ewayFormat = SimpleDateFormat("dd-MM-yyyy")
        val date: Date = ewayFormat.parse(date)

//        Log.d("DATE", (parseFormat.format(date).toString() + " - " + displayFormat.format(date)).toString())
        Log.d("SMALL_DT_INVOICE_FORMAT", sqlFormat.format(date).toString())
        return sqlFormat.format(date)
    }
}