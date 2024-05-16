package com.greensoft.greentranserpnative.ui.onClick

import com.greensoft.greentranserpnative.databinding.PodListItemBinding

interface OnRowClick<T> {
    fun onClick(data: T, clickType: String)
    fun onLongClick(data: T, clickType: String) {}

    fun onRowClick(data: T, clickType: String, index: Int) {}
    fun onImageClickedMultiplePodEntry(data: T, clickType: String, index: Int, layoutBinding: PodListItemBinding) {}
}