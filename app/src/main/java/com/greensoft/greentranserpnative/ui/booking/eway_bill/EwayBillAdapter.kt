package com.greensoft.greentranserpnative.ui.booking.eway_bill

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.EwayBillRecyclerviewItemBinding
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick

class EwayBillAdapter(private val EwayBillList: ArrayList<EwayBillModel>,
//                      private val bottomSheetClick: BottomSheetClick<Any>
                      ):RecyclerView.Adapter<EwayBillAdapter.EwayBillViewHolder>() {

    inner class EwayBillViewHolder(private  val binding:EwayBillRecyclerviewItemBinding)
        :RecyclerView.ViewHolder(binding.root) {

            fun bindData(model:EwayBillModel){
                binding.ewayBillModel=model
                binding.index=adapterPosition
//                binding.btnDelete.setOnClickListener{
//                    bottomSheetClick.onItemClick(model,"DELETE_REF")
//                }

            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EwayBillViewHolder {
        val binding:EwayBillRecyclerviewItemBinding=
            EwayBillRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return EwayBillViewHolder(binding)
    }

    override fun getItemCount(): Int =EwayBillList.size

    override fun onBindViewHolder(holder: EwayBillViewHolder, position: Int) {
        val eWayBillData = EwayBillList[holder.adapterPosition]
        holder.bindData(eWayBillData)
    }
}