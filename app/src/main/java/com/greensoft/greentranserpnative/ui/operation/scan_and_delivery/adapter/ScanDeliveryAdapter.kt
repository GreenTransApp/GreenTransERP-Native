package com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.databinding.ScanStickerItemBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.ScanAndDeliveryActivity
import com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.models.ScanStickerModel

class ScanDeliveryAdapter (private val stickerList: ArrayList<ScanStickerModel>,
                           private val mContext: Context,
                           private val activity:ScanAndDeliveryActivity,
                           private val onRowClick: OnRowClick<Any>
) : RecyclerView.Adapter<ScanDeliveryAdapter.ScanDeliveryViewHolder>(){


    inner class ScanDeliveryViewHolder(val binding: ScanStickerItemBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bindData(model: ScanStickerModel, onRowClick: OnRowClick<Any>) {
            binding.stickerModel = model
            binding.index = adapterPosition


            }
        }

//    fun setGridData(position: Int){
//
//        for (i in stickerList){
//
//        }
////        stickerList.forEachIndexed { index, model->
////            if(model.scanned =="Y") {
////
////            }
////        }
//
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanDeliveryViewHolder {
        var binding: ScanStickerItemBinding =
            ScanStickerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScanDeliveryViewHolder(binding)



    }

    override fun getItemCount(): Int = stickerList.size

    override fun onBindViewHolder(holder: ScanDeliveryViewHolder, position: Int) {
        val data = stickerList[holder.adapterPosition]
        holder.bindData(data, onRowClick)
        if(data.scanned == "N" ) {
            holder.binding.btnRemove.visibility = View.VISIBLE
            holder.binding.gridLayout.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.success
                )
            )

        }
        else {
            holder.binding.btnRemove.visibility = View.GONE
            holder.binding.gridLayout.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
                )
            )
        }
    }


}
