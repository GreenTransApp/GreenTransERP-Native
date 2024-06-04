package com.greensoft.greentranserpnative.ui.operation.pending_for_delivery_update_list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.PendingDrsListItemBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.pending_for_delivery_update_list.models.DrsPendingListModel
import java.util.Locale
import javax.inject.Inject

class PendingDeliveryDrsListAdapter  @Inject constructor(
    private val drsList:ArrayList<DrsPendingListModel>,
    private val onRowClick: OnRowClick<Any>,
):RecyclerView.Adapter<PendingDeliveryDrsListAdapter.PendingDeliveryDrsListViewHolder>(),Filterable {
    private var filterList: ArrayList<DrsPendingListModel>

    init {
        filterList = drsList

    }

    inner class PendingDeliveryDrsListViewHolder(private val layoutBinding: PendingDrsListItemBinding) :
        RecyclerView.ViewHolder(layoutBinding.root) {
        fun onBind(model: DrsPendingListModel, onRowClick: OnRowClick<Any>) {
            layoutBinding.model = model
            layoutBinding.index = adapterPosition
            layoutBinding.btn.setOnClickListener {
                onRowClick.onClick(model, "POD_SELECT")
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PendingDeliveryDrsListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutBinding = PendingDrsListItemBinding.inflate(inflater, parent, false)
        return PendingDeliveryDrsListViewHolder(layoutBinding)
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun onBindViewHolder(holder: PendingDeliveryDrsListViewHolder, position: Int) {
        holder.onBind(filterList[position], onRowClick)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filterList = drsList
                } else {
                    val filteredList: ArrayList<DrsPendingListModel> = ArrayList()
                    for (row in drsList) {
                        if(
                            row.documentno?.contains(charString, true) == true
//                             row.custname.lowercase().contains(charString.lowercase(Locale.getDefault()))
//                            row.grno.contains(charString.lowercase(Locale.getDefault()))
//                            ||row.destname.contains(charString.lowercase(Locale.getDefault()))
//                            ||row.cnge.lowercase().contains(charString.lowercase(Locale.getDefault()))
//                            ||row.cngr.lowercase().contains(charString.lowercase(Locale.getDefault()))


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
                filterList = filterResults.values as ArrayList<DrsPendingListModel>
                notifyDataSetChanged()
            }
        }

    }
}