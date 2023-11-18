package com.greensoft.greentranserpnative.ui.operation.loadingSlip.newScanLoad

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.InscanPendingStickerListBinding
import com.greensoft.greentranserpnative.databinding.ItemLoadedStickersBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.newScanLoad.models.LoadedStickerModel

class NewScanLoadStickerAdapter (private val context: Context,
    private val stickerList:ArrayList<LoadedStickerModel>,
    private val onRowClick: OnRowClick<Any>
    ): RecyclerView.Adapter<NewScanLoadStickerAdapter.NewScanLoadStickerViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewScanLoadStickerViewHolder {
            val inflater  = LayoutInflater.from(parent.context)
            val layoutBinding = ItemLoadedStickersBinding.inflate(inflater,parent,false)
            return NewScanLoadStickerViewHolder(layoutBinding, onRowClick)
        }

        override fun onBindViewHolder(holder: NewScanLoadStickerViewHolder, position: Int) {
            holder.onBind(stickerList[position])
        }

        override fun getItemCount(): Int {
            return stickerList.size
        }

        class NewScanLoadStickerViewHolder (private val layoutBinding: ItemLoadedStickersBinding, private val onRowClick: OnRowClick<Any>): RecyclerView.ViewHolder(layoutBinding.root){

            private fun setOnClick(stickerModel: LoadedStickerModel) {
                layoutBinding.removeBtn.setOnClickListener {
                    onRowClick.onClick(stickerModel, "REMOVE_STICKER")
                }
            }

            fun onBind(stickerModel: LoadedStickerModel) {
                layoutBinding.stickerModel = stickerModel
                layoutBinding.index = adapterPosition
                setOnClick(stickerModel)
            }
        }
    }