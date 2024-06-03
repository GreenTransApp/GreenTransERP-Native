package com.greensoft.greentranserpnative.ui.bottomsheet.departmentSelection

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.ItemDepartmentSelectionBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.departmentSelection.model.DepartmentSelectionModel
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import java.util.Locale
import javax.inject.Inject

class DepartmentSelectionAdapter @Inject constructor(
    private val departmentList:ArrayList<DepartmentSelectionModel>,
    private val onRowClick: OnRowClick<Any>? = null):RecyclerView.Adapter<RecyclerView.ViewHolder>(),Filterable {

    companion object {
        val DEPARTMENT_SELECTION_ROW_CLICK: String = "DEPARTMENT_LOV_ROW_CLICK"
    }
    private var filterList:ArrayList<DepartmentSelectionModel> = ArrayList()

    init {
        filterList = departmentList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutBinding = ItemDepartmentSelectionBinding.inflate(inflater,parent,false)
        return DepartmentViewHolder(layoutBinding,parent.context)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dataHolder = holder as DepartmentSelectionAdapter.DepartmentViewHolder
        val dataModel: DepartmentSelectionModel = filterList[position]
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
                    filterList = departmentList
                } else {
                    val filteredList: ArrayList<DepartmentSelectionModel> = ArrayList()
                    for (row in departmentList) {
                        if (row.custdeptname .lowercase().contains(charString.lowercase(Locale.getDefault()))) {
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
                filterList = filterResults.values as ArrayList<DepartmentSelectionModel>
                notifyDataSetChanged()
            }
        }
    }


    inner class DepartmentViewHolder(
        private val layoutBinding:ItemDepartmentSelectionBinding,
        private val mContext: Context ):RecyclerView.ViewHolder(layoutBinding.root){

            fun onBind(departmentDataModel:DepartmentSelectionModel, onRowClick: OnRowClick<Any>?){
                layoutBinding.departData = departmentDataModel
                layoutBinding.layout.setOnClickListener {
                    onRowClick?.onClick(departmentDataModel,DEPARTMENT_SELECTION_ROW_CLICK)
                }
            }

    }
}