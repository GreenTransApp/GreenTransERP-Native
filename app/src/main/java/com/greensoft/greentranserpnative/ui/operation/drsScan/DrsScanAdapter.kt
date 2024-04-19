package com.greensoft.greentranserpnative.ui.operation.drsScan

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.ItemScannedDrsBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.drsScan.model.ScannedDrsModel
import com.greensoft.greentranserpnative.ui.operation.notificationPanel.model.NotificationPanelBottomSheetModel
import java.util.Locale
import javax.inject.Inject

class DrsScanAdapter @Inject constructor(
    val scannedDrsList: ArrayList<ScannedDrsModel>,
    private val onRowClick: OnRowClick<Any>):RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    var filterList: ArrayList<ScannedDrsModel> = ArrayList()
    companion object {
        val REMOVE_STICKER_TAG = "REMOVE_STICKER_TAG"
    }

    init {
        filterList = scannedDrsList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutBinding = ItemScannedDrsBinding.inflate(inflater,parent, false)
        return DrsScanViewHolder(layoutBinding, parent.context)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       val dataHolder = holder as DrsScanAdapter.DrsScanViewHolder
        val dataModel:ScannedDrsModel = filterList[position]
        dataHolder.onBind(dataModel,onRowClick)
    }
    override fun getItemCount(): Int {
        return filterList.size
    }

    inner class DrsScanViewHolder(
        private val layoutBinding:ItemScannedDrsBinding,
        private var mContext:Context):RecyclerView.ViewHolder(layoutBinding.root){

            fun onBind(scannedDrsModel:ScannedDrsModel, onRowClick: OnRowClick<Any>){

                layoutBinding.index = adapterPosition
                layoutBinding.grNo.text = scannedDrsModel.grno.toString()
                layoutBinding.stickerNO.text = scannedDrsModel.stickerno.toString()
                layoutBinding.removeBtn.setOnClickListener {
                    onRowClick.onClick(scannedDrsModel, REMOVE_STICKER_TAG)
                }
            }

    }

    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filterList = scannedDrsList
                } else {
                    val filteredList: ArrayList<ScannedDrsModel> = ArrayList()
                    for (row in scannedDrsList) {
                        if (row.stickerno?.lowercase()?.contains(charString.lowercase(Locale.getDefault())) == true
                            ||
                            row.grno?.lowercase()?.contains(charString.lowercase(Locale.getDefault())) == true) {
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
                filterList = try {
                    filterResults.values as ArrayList<ScannedDrsModel>
                } catch (ex: Exception) {
                    ArrayList()
                }
                notifyDataSetChanged()
            }
        }
    }
}