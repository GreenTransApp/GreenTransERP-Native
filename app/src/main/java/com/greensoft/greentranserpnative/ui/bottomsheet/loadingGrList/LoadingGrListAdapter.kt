package com.greensoft.greentranserpnative.ui.bottomsheet.loadingGrList

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.greensoft.greentranserpnative.databinding.ItemGrListBinding
import com.greensoft.greentranserpnative.databinding.ItemLoadingGrListBinding
import com.greensoft.greentranserpnative.model.LoadingGrListModel
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.grList.models.GrListModel
import java.lang.String
import java.util.Locale

class LoadingGrListAdapter(
    val mContext: Context,
    val rvList: ArrayList<LoadingGrListModel>,
    val onRowClick: OnRowClick<Any>
): RecyclerView.Adapter<LoadingGrListAdapter.LoadingGrListViewHolder>(), Filterable {

    var filterList: ArrayList<LoadingGrListModel> = rvList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadingGrListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutBinding = ItemLoadingGrListBinding.inflate(inflater,parent,false)
        return LoadingGrListViewHolder(layoutBinding)
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun onBindViewHolder(holder: LoadingGrListViewHolder, position: Int) {
        var loadingGrListModel: LoadingGrListModel = filterList[position]
        holder.onBind(loadingGrListModel, position)
    }


    inner class LoadingGrListViewHolder(private val layoutBinding: ItemLoadingGrListBinding): ViewHolder(layoutBinding.root) {

        fun onBind(model: LoadingGrListModel, position: Int) {
            layoutBinding.model = model
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filterList = rvList
                } else {
                    val filteredList: ArrayList<LoadingGrListModel> = ArrayList()
                    for (row in rvList) {
                        if(row.grno?.contains(charString, true) == true){
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
                filterList = filterResults.values as ArrayList<LoadingGrListModel>
                notifyDataSetChanged()
            }
        }
    }

}