package com.greensoft.greentranserpnative.ui.bottomsheet.customerCodeSelection

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.ItemCustCodeSelectionBinding
import com.greensoft.greentranserpnative.databinding.ItemCustomerSelectionBinding

import com.greensoft.greentranserpnative.ui.bottomsheet.customerSelection.model.CustomerSelectionModel
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import java.util.Locale
import javax.inject.Inject

class CustomerCodeSelectionAdapter @Inject constructor(
    private val customerList:ArrayList<CustomerSelectionModel>,
    private val onRowClick: OnRowClick<Any>? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    companion object {
        val CUSTOMER_SELECTION_ROW_CLICK: String = "CUSTOMER_LOV_ROW_CLICK"
    }

    private var filterList: ArrayList<CustomerSelectionModel> = ArrayList()


    init {
        filterList = customerList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutBinding = ItemCustCodeSelectionBinding.inflate(inflater, parent, false)
        return CustomerSelectionViewHolder(layoutBinding, parent.context)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dataHolder = holder as CustomerCodeSelectionAdapter.CustomerSelectionViewHolder
        val dataModel: CustomerSelectionModel = filterList[position]
        dataHolder.onBind(dataModel, onRowClick)
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filterList = customerList
                } else {
                    val filteredList: ArrayList<CustomerSelectionModel> = ArrayList()
                    for (row in customerList) {
                        if (row.custcode.lowercase()
                                .contains(charString.lowercase(Locale.getDefault()))
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
                filterList = filterResults.values as ArrayList<CustomerSelectionModel>
                notifyDataSetChanged()
            }
        }
    }

    inner class CustomerSelectionViewHolder(
        private val layoutBinding: ItemCustCodeSelectionBinding,
        private val mContext: Context
    ) : RecyclerView.ViewHolder(layoutBinding.root) {
        fun onBind(customerSelectionModel: CustomerSelectionModel, onRowClick: OnRowClick<Any>?) {
            layoutBinding.custData = customerSelectionModel
            layoutBinding.layout.setOnClickListener {
                onRowClick?.onClick(customerSelectionModel, CUSTOMER_SELECTION_ROW_CLICK)
            }
        }
    }
}