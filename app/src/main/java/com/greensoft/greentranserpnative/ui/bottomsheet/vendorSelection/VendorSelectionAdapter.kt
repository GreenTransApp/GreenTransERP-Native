package com.greensoft.greentranserpnative.ui.bottomsheet.vendorSelection
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.ItemVendorSelectionBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.vendorSelection.model.VendorModelDRS
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import java.util.Locale
import javax.inject.Inject

class VendorSelectionAdapter @Inject constructor(
    private val vendorList: ArrayList<VendorModelDRS>,
    private val onRowClick: OnRowClick<Any>? = null):RecyclerView.Adapter<RecyclerView.ViewHolder>(),Filterable{
    companion object {
        val VENDOR_SELECTION_ROW_CLICK: String = "VENDOR_LOV_ROW_CLICK"
    }
    private var filterList: ArrayList<VendorModelDRS> = ArrayList()


    init {
        filterList = vendorList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutBinding = ItemVendorSelectionBinding.inflate(inflater,parent,false)
        return VendorSelectionViewHolder(layoutBinding,parent.context)
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dataHolder = holder as VendorSelectionAdapter.VendorSelectionViewHolder
        val dataModel: VendorModelDRS = filterList[position]
        dataHolder.onBind(dataModel,onRowClick)
    }

    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filterList = vendorList
                } else {
                    val filteredList: ArrayList<VendorModelDRS> = ArrayList()
                    for (row in vendorList) {
                        if (row.vendname.lowercase().contains(charString.lowercase(Locale.getDefault()))) {
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
                filterList = filterResults.values as ArrayList<VendorModelDRS>
                notifyDataSetChanged()
            }
        }
    }


    inner class VendorSelectionViewHolder(
       private val layoutBinding: ItemVendorSelectionBinding,
       private val mContext: Context ):RecyclerView.ViewHolder(layoutBinding.root){
        fun onBind(vendorModel: VendorModelDRS, onRowClick: OnRowClick<Any>?){
            layoutBinding.vendorData = vendorModel
            layoutBinding.layout.setOnClickListener {
                onRowClick?.onClick(vendorModel, VENDOR_SELECTION_ROW_CLICK)
            }
        }
       }
}