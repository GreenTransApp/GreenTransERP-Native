package com.greensoft.greentranserpnative.ui.bottomsheet.vehicleSelection

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.ItemVehicleSelectionBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.vehicleSelection.model.VehicleModelDRS
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import java.util.Locale
import javax.inject.Inject

class VehicleSelectionAdapter @Inject constructor(
    private val vehicleList: ArrayList<VehicleModelDRS>,
    private val onRowClick: OnRowClick<Any>? = null,
    ):RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

        companion object {
            val VEHICLE_SELECTION_ROW_CLICK: String = "VEHICLE_LOV_ROW_CLICK"
        }

        private var filterList: ArrayList<VehicleModelDRS> = ArrayList()

        init {
            filterList = vehicleList
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutBinding = ItemVehicleSelectionBinding.inflate(inflater,parent,false)
        return VehicleSelectionViewHolder(layoutBinding,parent.context)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dataHolder = holder as VehicleSelectionAdapter.VehicleSelectionViewHolder
        val dataModel: VehicleModelDRS = filterList[position]
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
                    filterList = vehicleList
                } else {
                    val filteredList: ArrayList<VehicleModelDRS> = ArrayList()
                    for (row in vehicleList) {
                        if (row.regno.lowercase().contains(charString.lowercase(Locale.getDefault()))) {
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
                filterList = filterResults.values as ArrayList<VehicleModelDRS>
                notifyDataSetChanged()
            }
        }
    }

    inner class VehicleSelectionViewHolder(
        private val layoutBinding:ItemVehicleSelectionBinding,
        private val mContext:Context):RecyclerView.ViewHolder(layoutBinding.root){

            fun onBind(vehicleModel:VehicleModelDRS, onRowClick: OnRowClick<Any>?){
                layoutBinding.vehicleData = vehicleModel
                layoutBinding.layout.setOnClickListener {
                    onRowClick?.onClick(vehicleModel,VEHICLE_SELECTION_ROW_CLICK)
                }
            }
    }
}
