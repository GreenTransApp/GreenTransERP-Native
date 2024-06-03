package com.greensoft.greentranserpnative.ui.bottomsheet.cngrCngeSelection

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.ItemCngrCngeSelectionBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.cngrCngeSelection.model.CngrCngeSelectionModel
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import java.util.Locale
import javax.inject.Inject

class CngrCngeSelectionAdapter @Inject constructor(
    private val cngrCngeList:ArrayList<CngrCngeSelectionModel>,
    private val onRowClick: OnRowClick<Any>? = null):RecyclerView.Adapter<RecyclerView.ViewHolder>(),Filterable{

    companion object {
        val CNGR_CNGE_SELECTION_ROW_CLICK: String = "CNGR_CNGE_LOV_ROW_CLICK"
    }
    private var filterList:ArrayList<CngrCngeSelectionModel> = ArrayList()

    init {
        filterList = cngrCngeList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutBinding = ItemCngrCngeSelectionBinding.inflate(inflater,parent, false)
        return CngrCngeViewHolder(layoutBinding,parent.context)

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dataHolder = holder as CngrCngeSelectionAdapter.CngrCngeViewHolder
        val dataModel: CngrCngeSelectionModel = filterList[position]
        dataHolder.onBind(dataModel,onRowClick)
    }


    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filterList = cngrCngeList
                } else {
                    val filteredList: ArrayList<CngrCngeSelectionModel> = ArrayList()
                    for (row in cngrCngeList) {
                        if (row.name .lowercase().contains(charString.lowercase(Locale.getDefault()))) {
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
                filterList = filterResults.values as ArrayList<CngrCngeSelectionModel>
                notifyDataSetChanged()
            }
        }
    }

    inner class CngrCngeViewHolder(
        private val layoutBinding:ItemCngrCngeSelectionBinding,
        private val context:Context):RecyclerView.ViewHolder(layoutBinding.root){

        fun onBind(cngrCngeModal:CngrCngeSelectionModel,onRowClick: OnRowClick<Any>?){
            layoutBinding.cngrCngeData = cngrCngeModal
            layoutBinding.layout.setOnClickListener {
                onRowClick?.onClick(cngrCngeModal,CNGR_CNGE_SELECTION_ROW_CLICK)
            }
        }
    }
}