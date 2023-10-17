package com.greensoft.greentranserpnative.ui.onClick

interface OnRowClick<T> {
    fun onClick(data: T, clickType: String)
    fun onLongClick(data: T, clickType: String) {}

    fun onRowClick(data: T, clickType: String, index: Int) {}
}