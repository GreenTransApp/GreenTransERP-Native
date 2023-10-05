package com.greensoft.greentranserpnative.ui.operation.call_register

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.CallRegisterItemBinding
import com.greensoft.greentranserpnative.ui.operation.call_register.models.CallRegisterModel
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import java.util.Locale
import javax.inject.Inject

class CallRegisterAdapter @Inject constructor(private val callRegisterList:ArrayList<CallRegisterModel>, private val onRowClick: OnRowClick<Any>,):
RecyclerView.Adapter<CallRegisterAdapter.CallRegisterViewHolder>(),Filterable {

    private var filterList: ArrayList<CallRegisterModel>
    init {
        filterList=callRegisterList
    }

     class CallRegisterViewHolder(private val adapterBinding:CallRegisterItemBinding):RecyclerView.ViewHolder(adapterBinding.root){
          fun bindData(model: CallRegisterModel, onRowClick: OnRowClick<Any>){
              adapterBinding.registerList=model
              adapterBinding.index=adapterPosition
              adapterBinding.btnAccept.setOnClickListener{
                  onRowClick.onCLick(model,"ACCEPT_SELECT")

              }
              adapterBinding.btnReject.setOnClickListener{
                  onRowClick.onCLick(model,"REJECT_SELECT")
              }

          }
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallRegisterViewHolder {
        val binding =
            CallRegisterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CallRegisterViewHolder(binding)
    }

    override fun getItemCount(): Int = filterList.size


    override fun onBindViewHolder(holder: CallRegisterViewHolder, position: Int) {
        val data = filterList[holder.adapterPosition]
        holder.bindData(data,onRowClick)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filterList = callRegisterList
                } else {
                    val filteredList: ArrayList<CallRegisterModel> = ArrayList()
                    for (row in callRegisterList) {
                         if(
//                             row.custname.lowercase().contains(charString.lowercase(Locale.getDefault()))
                             row.transactionid.toString().contains(charString.lowercase(Locale.getDefault()))
                             ||row.calldt.contains(charString.lowercase(Locale.getDefault()))
                             ||row.stnname.lowercase().contains(charString.lowercase(Locale.getDefault()))
                             ||row.stnname1.lowercase().contains(charString.lowercase(Locale.getDefault()))
                         ){
                             filteredList.add(row)
                         }
                    }
                    filterList = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence?,
                filterResults: FilterResults
            ) {
                filterList = filterResults.values as ArrayList<CallRegisterModel>
                notifyDataSetChanged()
            }
        }
    }
}