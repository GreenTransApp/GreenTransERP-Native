package com.greensoft.greentranserpnative.ui.operation.pending_for_delivery_update_list.models

import android.graphics.Bitmap
import com.greensoft.greentranserpnative.utils.Utils
import okhttp3.internal.Util
import kotlin.math.sign

data class PodEntryListModel(
    val arrivalatstn: String?,
    val arrivalatstncode: String?,
    val aweight: Double?,
    val branchcode: String?,
    val branchname: String?,
    val cnge: String?,
    val cngr: String?,
    val commandmessage: Any?,
    val commandstatus: Int?,
    val customercode: String?,
    val customername: String?,
    val dataid: Int?,
    val deliveredby: String?,
    val deliverystatus: String?,
    val dest: String?,
    val dlvagentcode: String?,
    val dlvagentname: String?,
    val dlvvehicleno: String?,
    val documenttype: String?,
    val drdt: Any?,
    val drivercode: Any?,
    val drivername: Any?,
    val drno: Any?,
    val drsno: String?,
    val enableundelivery: String?,
    val executiveid: Int?,
    val executivename: String?,
    val gateindate: Any?,
    val gateintime: Any?,
    val gateoutdate: Any?,
    val gateouttime: Any?,
    val goods: String?,
    val grdt: String?,
    val grno: String?,
    val innerqty: Int?,
    val invoiceno: String?,
    val lhcno: Any?,
    val loadedby: Any?,
    val manifestdt: String?,
    val manifesttime: String?,
    val mfcategoryid: Int?,
    var mobileno: String?,
    val modecode: String?,
    val modename: String?,
    val org: String?,
    val packing: String?,
    val paymentnotapplicable: String?,
    val pckgs: Double?,
    val plantname: Any?,
    val remark: Any?,
    val remarks: String?,
    val smsmobileno: Any?,
    val topay: Double?,
    val vendcode: Any?,
    val vendname: Any?,
    var dlvdt: String?,
    var sqlpoddt: Any?,
    var dlvtime: String?,
    var receivedby: String?,
    var relation: String?,
    var signImg: Bitmap?,
    var signImgBase64: String?,
    var podImg: Bitmap?,
    var podImgBase64: String?,
    var stampRequired:String?,
    var signRequired :String?
) {
    fun subStringableString(str: String?) : String {
        return if(str.isNullOrBlank()) "null" else str.substring(0, 10)
    }
    override fun toString(): String {
        val jsonLikeString = "{\n" +
                "\tdlvDt: ${dlvdt},\n" +
                "\tdlvTime: ${dlvtime},\n" +
                "\treceivedBy: ${receivedby},\n" +
                "\tmobileNo: ${mobileno},\n" +
                "\trelation: ${relation},\n" +
                "\tsignature: ${subStringableString(signImgBase64)},\n" +
                "\tpodImage: ${subStringableString(podImgBase64)}\n" +
                "\n}"
        return jsonLikeString
    }
}