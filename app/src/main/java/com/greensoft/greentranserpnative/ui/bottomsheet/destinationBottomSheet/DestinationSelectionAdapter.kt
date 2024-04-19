package com.greensoft.greentranserpnative.ui.bottomsheet.destinationBottomSheet

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.DestinationSelectionItemBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import java.util.Locale
import javax.inject.Inject

class DestinationSelectionAdapter  @Inject constructor(
    private val destinationList: ArrayList<ToStationModel>,
    private val onRowClick: OnRowClick<Any>? = null,
):RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    companion object {
        val DESTINATION_SELECTION_ROW_CLICK: String = "DESTINATION_LOV_ROW_CLICK"
    }
    private var filterList: ArrayList<ToStationModel> = ArrayList()

    init {
        filterList = destinationList
    }
    inner class DestinationSelectionViewHolder(
        private val layoutBinding: DestinationSelectionItemBinding,
        private val mContext: Context
    ) : RecyclerView.ViewHolder(layoutBinding.root) {


        fun onBind(model: ToStationModel, onRowClick: OnRowClick<Any>?) {
            layoutBinding.model = model
            layoutBinding.layout.setOnClickListener {
                onRowClick?.onClick(model,
                    DestinationSelectionAdapter.DESTINATION_SELECTION_ROW_CLICK
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutBinding = DestinationSelectionItemBinding.inflate(inflater, parent, false)
        return DestinationSelectionViewHolder(layoutBinding, parent.context)
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dataHolder = holder as DestinationSelectionAdapter.DestinationSelectionViewHolder
        val dataModel: ToStationModel = filterList[position]
        dataHolder.onBind(dataModel, onRowClick)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filterList = destinationList
                } else {
                    val filteredList: ArrayList<ToStationModel> = ArrayList()
                    for (row in destinationList) {
                        if (row.StnName.toString().lowercase()
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
                filterList = filterResults.values as ArrayList<ToStationModel>
                notifyDataSetChanged()
            }

        }
    }
}