package com.greensoft.greentranserpnative.ui.operation.loadingSlip.summary

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.databinding.ItemSummaryScanloadBinding
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.summary.models.SummaryScanLoadModel
import java.util.*

class SummaryListAdapter(private val mContext: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private var rvList: ArrayList<SummaryScanLoadModel>? = null
    private var filterList: ArrayList<SummaryScanLoadModel>? = null
    private val EMPTY_VIEW = 1
    private val DATA_VIEW = 2
    var view: View? = null

    class EmptyViewHolder(itemView: View?) : RecyclerView.ViewHolder(
        itemView!!
    ) {
        var txtType: TextView

        init {
            txtType = itemView!!.findViewById<View>(R.id.text_send) as TextView
            //            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    inner class DataViewHolder(var itemBinding: ItemSummaryScanloadBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        fun bind(model: SummaryScanLoadModel) {
            itemBinding.sNo.text = model.sno.toString()
            itemBinding.grNo.text = model.grno
            itemBinding.grDate.text = model.bookingdt
            itemBinding.originName.text = model.origin
            itemBinding.destName.text = model.destination
            itemBinding.consignorName.text = model.cngr
            itemBinding.consigneeName.text = model.cnge
            itemBinding.bookedPckgs.text = model.bookedpckgs.toString()
            itemBinding.grossWeight.text = model.grossweight.toString()
            itemBinding.despatchPckgs.text = model.despatchedpckgs.toString()
            itemBinding.loadedPckgs.text = model.loadedpckgs.toString()
            itemBinding.loadedWeight.text = model.loadedweight.toString()
            itemBinding.balancePckgs.text = model.balancepckgs.toString()
        }
    }

    fun setData(rvList: ArrayList<SummaryScanLoadModel>?) {
        this.rvList = rvList
        filterList = rvList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (filterList!!.size > 0) filterList!!.size else 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (filterList!!.size == 0) EMPTY_VIEW else DATA_VIEW
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.empty_view, parent, false)
            EmptyViewHolder(view)
        } else {
            val itemBinding =
                ItemSummaryScanloadBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            //                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grn_list, parent, false);
            DataViewHolder(itemBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            EMPTY_VIEW -> (holder as EmptyViewHolder).txtType.text = "No Data Available"
            DATA_VIEW -> {
                val dataViewHolder = holder as DataViewHolder
                val dataModel = filterList!![position]
                dataViewHolder.bind(dataModel)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                filterList = if (charString.isEmpty()) {
                    rvList
                } else {
                    val filteredList = ArrayList<SummaryScanLoadModel>()
                    for (row in rvList!!) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.origin.lowercase(Locale.getDefault())
                                .contains(charString.lowercase(Locale.getDefault()))
                            || row.grno.lowercase(Locale.getDefault())
                                .contains(charString.lowercase(Locale.getDefault()))
                            || row.destination.lowercase(Locale.getDefault()).contains(
                                charString.lowercase(
                                    Locale.getDefault()
                                )
                            )
                        ) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filterList = filterResults.values as ArrayList<SummaryScanLoadModel>
                notifyDataSetChanged()
            }
        }
    }
}