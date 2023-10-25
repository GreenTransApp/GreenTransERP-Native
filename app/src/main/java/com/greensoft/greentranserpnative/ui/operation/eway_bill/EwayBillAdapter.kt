package com.greensoft.greentranserpnative.ui.operation.eway_bill

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.EwayBillRecyclerviewItemBinding
import com.greensoft.greentranserpnative.ui.operation.booking.BookingActivity

class EwayBillAdapter(
            private val mActivity: BookingActivity,
            private val EwayBillList: ArrayList<ItemEwayBillModel>,
//          private val bottomSheetClick: BottomSheetClick<Any>
    ):RecyclerView.Adapter<EwayBillAdapter.EwayBillViewHolder>() {

    inner class EwayBillViewHolder(private  val binding:EwayBillRecyclerviewItemBinding)
        :RecyclerView.ViewHolder(binding.root) {

        fun bindData(model: ItemEwayBillModel){
            binding.ewayBillModel=model
            binding.index=adapterPosition
            binding.ewaybillTxt.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    model.ewayBillNo = p0.toString()
                }

            })
//          binding.btnDelete.setOnClickListener{
//              bottomSheetClick.onItemClick(model,"DELETE_REF")
//          }
        }
    }



    fun isAllEwayBillInputsFilled(): Boolean {
        if(EwayBillList.isEmpty()) {
            mActivity.errorToast("At-least one Eway Bill is required to validate.")
        }
        EwayBillList.forEachIndexed { index, itemEwayBillModel ->
            if(itemEwayBillModel.ewayBillNo.isEmpty()) {
                mActivity.errorToast("Eway-Bill input is empty at ${index + 1}")
                return false
            }
        }
        return true
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