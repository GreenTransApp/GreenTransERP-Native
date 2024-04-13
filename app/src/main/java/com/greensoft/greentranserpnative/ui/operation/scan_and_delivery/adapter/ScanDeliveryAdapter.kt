package com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.ScanStickerItemBinding
import com.greensoft.greentranserpnative.databinding.SelectedGrItemBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.models.ScanStickerModel

class ScanDeliveryAdapter (private val stickerList: ArrayList<ScanStickerModel>,
                           private val mContext: Context,
                           private val onRowClick: OnRowClick<Any>
) : RecyclerView.Adapter<ScanDeliveryAdapter.ScanDeliveryViewHolder>(){


    inner class ScanDeliveryViewHolder(private val binding: ScanStickerItemBinding) :
        RecyclerView.ViewHolder(binding.root){


        fun bindData(model: ScanStickerModel, onRowClick: OnRowClick<Any>) {
            binding.stickerModel = model
            binding.index = adapterPosition


            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanDeliveryViewHolder {
        var binding: ScanStickerItemBinding =
            ScanStickerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScanDeliveryViewHolder(binding)
    }

    override fun getItemCount(): Int = stickerList.size

    override fun onBindViewHolder(holder: ScanDeliveryViewHolder, position: Int) {
        val data = stickerList[holder.adapterPosition]
        holder.bindData(data, onRowClick)
    }


}