package com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.adapter

import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.ScanStickerItemBinding
import com.greensoft.greentranserpnative.databinding.UndeliveredStickerItemBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.models.ScanStickerModel

class ScanUndeliveryAdapter {

    inner class ScanUndeliveryViewHolder(val binding:UndeliveredStickerItemBinding ) :
        RecyclerView.ViewHolder(binding.root){

        fun bindData(model: ScanStickerModel, onRowClick: OnRowClick<Any>) {
            binding.model = model
            binding.index = adapterPosition


        }
    }
}