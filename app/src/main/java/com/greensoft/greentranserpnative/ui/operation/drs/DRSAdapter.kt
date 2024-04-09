package com.greensoft.greentranserpnative.ui.operation.drs

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.ItemGrDetailDrsBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.drs.model.GrDetailModelDRS

import javax.inject.Inject

class DRSAdapter @Inject constructor(
    private val grList: ArrayList<GrDetailModelDRS>,
    private val onRowClick: OnRowClick<Any>,):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutBinding = ItemGrDetailDrsBinding.inflate(inflater,parent,false)
        return DRSViewHolder(layoutBinding,parent.context)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dataHolder = holder as DRSAdapter.DRSViewHolder
        val dataModel: GrDetailModelDRS = grList[position]
        dataHolder.onBind(dataModel,onRowClick)
    }

    override fun getItemCount(): Int {
        return grList.size
    }

    inner  class DRSViewHolder(
        private val layoutBinding:ItemGrDetailDrsBinding,
        private val context: Context):RecyclerView.ViewHolder(layoutBinding.root){

            fun onBind(grModelData:GrDetailModelDRS, onRowClick: OnRowClick<Any>){
                layoutBinding.grData = grModelData
                layoutBinding.index = adapterPosition
                layoutBinding.removeBtn.setOnClickListener {
                    onRowClick.onClick(grModelData,"REMOVE_GR")
                }

            }
    }
}