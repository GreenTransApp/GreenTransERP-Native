package com.greensoft.greentranserpnative.ui.onClick

interface BottomSheetClick <T> {
    fun onItemClick(data: T, clickType: String)
    fun onItemLongClick(data: T, clickType: String) {}
    fun onItemClickWithAdapter(data: T, clickType: String, index: Int) {}
}