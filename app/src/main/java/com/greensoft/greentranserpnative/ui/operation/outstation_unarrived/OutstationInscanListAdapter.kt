package com.greensoft.greentranserpnative.ui.operation.outstation_unarrived

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.InscanListItemBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.unarrived.InscanListAdapter
import com.greensoft.greentranserpnative.ui.operation.unarrived.models.InscanListModel
import java.util.Locale

class OutstationInscanListAdapter (private val inscanList: ArrayList<InscanListModel>,
                                   private val onRowClick: OnRowClick<Any>) :
    RecyclerView.Adapter<OutstationInscanListAdapter.InscanViewHolder>() , Filterable {
    private  var filterList: ArrayList<InscanListModel>
    init {
        filterList=inscanList
    }


    class InscanViewHolder(private val binding: InscanListItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bindData(model: InscanListModel, onRowClick: OnRowClick<Any>){
            binding.inscanModel = model;
            binding.index = adapterPosition;
            binding.btnScan.setOnClickListener {
                onRowClick.onClick(model, "SCAN_SELECT")
            }
            binding.btnWithoutScan.setOnClickListener {
                onRowClick.onClick(model,"WITHOUT_SCAN")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InscanViewHolder {
        var binding: InscanListItemBinding =
            InscanListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OutstationInscanListAdapter.InscanViewHolder(binding)
    }
    override fun getItemCount():Int = filterList.size

    override fun onBindViewHolder(holder: InscanViewHolder, position: Int) {
        val inscanListData = filterList[holder.adapterPosition]
        holder.bindData(inscanListData, onRowClick)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filterList = inscanList
                } else {
                    val filteredList: ArrayList<InscanListModel> = ArrayList()
                    for (row in inscanList) {
                        if (row.mawbno.toString().contains(charString.lowercase(Locale.getDefault()))
                            || row.manifestno.toString().contains(charString.lowercase(Locale.getDefault()))
                            || row.fromstation.toString().contains(charString.lowercase(Locale.getDefault()))
                            || row.fromstation.toString().contains(charString.lowercase(Locale.getDefault()))

                        ) {
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
                filterList = filterResults.values as ArrayList<InscanListModel>
                notifyDataSetChanged()
            }
        }

}
}