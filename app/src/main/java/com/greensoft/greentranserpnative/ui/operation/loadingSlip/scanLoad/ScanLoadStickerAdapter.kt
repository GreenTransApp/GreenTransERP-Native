package com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.InscanPendingStickerListBinding
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad.models.StickerModel


class ScanLoadStickerAdapter (private val stickerList:ArrayList<StickerModel>): RecyclerView.Adapter<ScanLoadStickerAdapter.ScanLoadStickerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanLoadStickerViewHolder {
        val inflater  = LayoutInflater.from(parent.context)
        val layoutBinding = InscanPendingStickerListBinding.inflate(inflater,parent,false)
        return ScanLoadStickerViewHolder(layoutBinding)
    }

    override fun onBindViewHolder(holder: ScanLoadStickerViewHolder, position: Int) {
        holder.onBind(stickerList[position])
    }

    override fun getItemCount(): Int {
       return stickerList.size
    }

    class ScanLoadStickerViewHolder (private val layoutBinding:InscanPendingStickerListBinding): RecyclerView.ViewHolder(layoutBinding.root){
        fun onBind(stickerModel: StickerModel) {
            layoutBinding.stickerModel = stickerModel
            layoutBinding.index = adapterPosition
        }
    }
}