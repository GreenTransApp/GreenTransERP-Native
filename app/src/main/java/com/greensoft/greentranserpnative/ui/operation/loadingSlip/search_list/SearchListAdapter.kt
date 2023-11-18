package com.greensoft.greentranserpnative.ui.operation.loadingSlip.search_list

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.databinding.ItemSearchListBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.search_list.models.SearchListModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.GrSelectionModel
import java.lang.String
import java.util.Locale
import kotlin.CharSequence
import kotlin.Int



class SearchListAdapter(private val mContext: Context,
                        private val rvList : ArrayList<SearchListModel>,
                        private val onRowClick: OnRowClick<Any>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private var filterList: ArrayList<SearchListModel> = ArrayList()
    private val EMPTY_VIEW = 1
    private val DATA_VIEW = 2
    var view: View? = null

    class EmptyViewHolder(itemView: View?) : RecyclerView.ViewHolder(
        itemView!!
    ) {
        var txtType: TextView

        init {
            txtType = itemView!!.findViewById<View>(R.id.text_send) as TextView

        }
    }

    inner class DataViewHolder(itemBinding: ItemSearchListBinding) :
        RecyclerView.ViewHolder(itemBinding.getRoot()) {
        var itemBinding: ItemSearchListBinding

        init {
            this.itemBinding = itemBinding
        }

        fun bind(model: SearchListModel, position: Int) {
            itemBinding.sNo.setText((position + 1).toString())
            itemBinding.loadingNo.setText(String.valueOf(model.loadingno))
            itemBinding.loadingDate.setText(String.valueOf(model.loadingdate))
            itemBinding.branchName.setText(String.valueOf(model.branchname))
            itemBinding.destName.setText(String.valueOf(model.destinationname))
            itemBinding.vehicleNo.setText(String.valueOf(model.vehicleno))
            itemBinding.driverName.setText(String.valueOf(model.drivername))
            itemBinding.manifestNo.setText("")
            itemBinding.btnEDIT.setOnClickListener { view ->


            }
        }
    }

    fun setData(rvList: ArrayList<SearchListModel>) {
//        this.rvList = rvList
        filterList = rvList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (filterList.size == 0) EMPTY_VIEW else DATA_VIEW
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.empty_view, parent, false)
            EmptyViewHolder(view)
        } else {
            val itemBinding: ItemSearchListBinding =
                ItemSearchListBinding.inflate(
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
                val dataModel: SearchListModel = filterList[position]
                dataViewHolder.bind(dataModel, position)
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
                    val filteredList: ArrayList<SearchListModel> =
                        ArrayList<SearchListModel>()
                    for (row in rvList!!) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.loadingno.toString()
                                .contains(charString.lowercase(Locale.getDefault()))
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
                filterList = filterResults.values as ArrayList<SearchListModel>
                notifyDataSetChanged()
            }
        }
    }
}