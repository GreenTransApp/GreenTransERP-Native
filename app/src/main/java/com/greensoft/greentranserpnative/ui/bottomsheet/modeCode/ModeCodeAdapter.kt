package com.greensoft.greentranserpnative.ui.bottomsheet.modeCode

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.ItemVehicleSelectionBinding
import com.greensoft.greentranserpnative.databinding.ModeCodeSelectionItemBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.vehicleSelection.VehicleSelectionAdapter
import com.greensoft.greentranserpnative.ui.bottomsheet.vehicleSelection.model.VehicleModelDRS
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import java.util.Locale
import javax.inject.Inject

class ModeCodeAdapter @Inject constructor(
    private val modeCodeList: ArrayList<ModeCodeSelectionModel>,
    private val onRowClick: OnRowClick<Any>? = null,
):RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    companion object {
        val MODE_CODE_SELECTION_ROW_CLICK: String = "MODE_LOV_ROW_CLICK"
    }

    private var filterList: ArrayList<ModeCodeSelectionModel> = ArrayList()

    init {
        filterList = modeCodeList
    }

    inner class ModeCodeSelectionViewHolder(
        private val layoutBinding: ModeCodeSelectionItemBinding,
        private val mContext: Context
    ) : RecyclerView.ViewHolder(layoutBinding.root) {


        fun onBind(model: ModeCodeSelectionModel, onRowClick: OnRowClick<Any>?) {
            layoutBinding.model = model
            layoutBinding.layout.setOnClickListener {
                onRowClick?.onClick(model,
                    ModeCodeAdapter.MODE_CODE_SELECTION_ROW_CLICK
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutBinding = ModeCodeSelectionItemBinding.inflate(inflater, parent, false)
        return ModeCodeSelectionViewHolder(layoutBinding, parent.context)
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dataHolder = holder as ModeCodeAdapter.ModeCodeSelectionViewHolder
        val dataModel: ModeCodeSelectionModel = filterList[position]
        dataHolder.onBind(dataModel, onRowClick)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filterList = modeCodeList
                } else {
                    val filteredList: ArrayList<ModeCodeSelectionModel> = ArrayList()
                    for (row in modeCodeList) {
                        if (row.regno.toString().lowercase()
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
                filterList = filterResults.values as ArrayList<ModeCodeSelectionModel>
                notifyDataSetChanged()
            }

        }
    }
}