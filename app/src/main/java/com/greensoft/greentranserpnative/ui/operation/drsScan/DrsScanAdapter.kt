package com.greensoft.greentranserpnative.ui.operation.drsScan

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.ItemScannedDrsBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.drsScan.model.ScannedDrsModel
import javax.inject.Inject

class DrsScanAdapter @Inject constructor(
    private val scannedDrsList: ArrayList<ScannedDrsModel>,
    private val onRowClick: OnRowClick<Any>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutBinding = ItemScannedDrsBinding.inflate(inflater,parent, false)
        return DrsScanViewHolder(layoutBinding, parent.context)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       val dataHolder = holder as DrsScanAdapter.DrsScanViewHolder
        val dataModel:ScannedDrsModel = scannedDrsList[position]
        dataHolder.onBind(dataModel,onRowClick)
    }
    override fun getItemCount(): Int {
        return scannedDrsList.size
    }

    inner class DrsScanViewHolder(
        private val layoutBinding:ItemScannedDrsBinding,
        private var mContext:Context):RecyclerView.ViewHolder(layoutBinding.root){

            fun onBind(scannedDrsModel:ScannedDrsModel, onRowClick: OnRowClick<Any>){

                layoutBinding.index = adapterPosition
                layoutBinding.removeBtn.setOnClickListener {
                    onRowClick.onClick(scannedDrsModel,"REMOVE_STICKER")
                }
            }

    }
}