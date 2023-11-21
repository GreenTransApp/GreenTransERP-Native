package com.greensoft.greentranserpnative.ui.operation.eway_bill.models

data class EwayBillDetailResponse(
    val errorList: List<EwayBillDetailErrorList>,
    val message: String,
    val response: EwayDetailResponse,
    val status: Int
)

data class EwayBillDetailErrorList(
    val field: String?,
    val message: String?
)


data class EwayBillDetailStatus(
    var status: Int,
    var response: String?
)

data class Item(
    val cessNonAdvol: Double,
    val cessRate: Double,
    val cgstRate: Double,
    val hsnCode: String,
    val igstRate: Double,
    val itemNo: Int,
    val productDesc: String,
    val productId: Int,
    val productName: String,
    val qtyUnit: String,
    val quantity: String,
    val sgstRate: Double,
    val taxableAmount: Double
)


data class EwayDetailResponse(
    val actFromStateCode: String,
    val actToStateCode: String,
    val actualDist: Int,
    val archived: Int,
    val barcode: String,
    val cessNonAdvolValue: Double,
    val cessValue: Double,
    val cgstValue: Double,
    val docDate: String?,
    val docNo: String,
    val docType: String,
    val ewbDate: String?,
    val ewbId: Int,
    val ewbNo: String,
    val extendedTimes: Int,
    val finYear: String,
    val fromAddr1: String,
    val fromAddr2: String,
    val fromGstin: String,
    val fromPincode: String,
    val fromPlace: String,
    val fromStateCode: String,
    val fromTrdName: String,
    val genGstin: String,
    val genMode: String,
    val igstValue: Double,
    val itemList: List<Item>,
    val noValidDays: Int,
    val optForMultivehicle: Boolean,
    val otherValue: Double,
    val qrCode: String,
    val rejectStatus: String,
    val sgstValue: Double,
    val status: String,
    val subSupplyType: String,
    val supplyType: String,
    val toAddr1: String,
    val toAddr2: String,
    val toGstin: String,
    val toPincode: String,
    val toPlace: String,
    val toStateCode: String,
    val toTrdName: String,
    val totInvValue: Double,
    val totalValue: Double,
    val transDocNo: String,
    val transId: String,
    val transMode: String,
    val transName: String,
    val transactionType: String,
    val updateDate: String,
    val userGstin: String,
    val validUpto: String?,
    val vehicleDetails: List<VehicleDetail>,
    val vehicleNo: String
)

data class VehicleDetail(
    val canceled: Boolean,
    val enteredDate: String,
    val ewbNo: String,
    val fromPlace: String,
    val fromState: String,
    val groupNo: String,
    val transDocNo: String,
    val transMode: String,
    val tripshtNo: String,
    val userGSTINTransin: String,
    val vehicleNo: String
)