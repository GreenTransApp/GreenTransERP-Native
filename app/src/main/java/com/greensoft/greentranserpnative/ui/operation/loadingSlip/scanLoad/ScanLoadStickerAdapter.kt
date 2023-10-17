package com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.InscanPendingStickerListBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad.models.StickerModel


class ScanLoadStickerAdapter (private val context: Context,
                              private val stickerList:ArrayList<StickerModel>,
                              private val onRowClick: OnRowClick<Any>
): RecyclerView.Adapter<ScanLoadStickerAdapter.ScanLoadStickerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanLoadStickerViewHolder {
        val inflater  = LayoutInflater.from(parent.context)
        val layoutBinding = InscanPendingStickerListBinding.inflate(inflater,parent,false)
        return ScanLoadStickerViewHolder(layoutBinding, onRowClick)
    }

    override fun onBindViewHolder(holder: ScanLoadStickerViewHolder, position: Int) {
        holder.onBind(stickerList[position])
    }

    override fun getItemCount(): Int {
       return stickerList.size
    }

    class ScanLoadStickerViewHolder (private val layoutBinding:InscanPendingStickerListBinding, private val onRowClick: OnRowClick<Any>): RecyclerView.ViewHolder(layoutBinding.root){

        private fun setOnClick(stickerModel: StickerModel) {
            layoutBinding.removeBtn.setOnClickListener {
                onRowClick.onClick(stickerModel, "REMOVE_STICKER")
            }
        }

        fun onBind(stickerModel: StickerModel) {
            layoutBinding.stickerModel = stickerModel
            layoutBinding.index = adapterPosition
            setOnClick(stickerModel)
        }
    }
}