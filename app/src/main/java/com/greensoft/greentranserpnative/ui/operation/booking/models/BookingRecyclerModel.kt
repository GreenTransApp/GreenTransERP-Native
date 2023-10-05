package com.greensoft.greentranserpnative.ui.operation.booking.models

data class BookingRecyclerModel(
    val sNo:String,
    val pckgs:String,
    val refNo:String,
    val packing:String,
    val boxNo:String,
    val content:String,
    val selectedTemperature:String,
    val selectedDatalogger:String,
    val dataloggerNo:String,
    val weight:String,
    val length:String,
    val breadth:String,
    val height:String,
//    val gelPack:Boolean,
    val gelPackItem:String,
    val gelPackQuantity:String,
    val dryIceQuantity:String,
)


