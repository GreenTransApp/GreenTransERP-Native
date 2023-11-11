package com.greensoft.greentranserpnative.ui.operation.counterDetail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.ItemNotiPanelBottomSheetBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.counterDetail.model.NotificationPanelBottomSheetModel
import java.util.*
import kotlin.collections.ArrayList


open class NotificationPanelBottomSheetAdapter(
    private val mContext: Context,
    private val bottomSheet: NotificationPanelBottomSheet,
    private val counterList: ArrayList<NotificationPanelBottomSheetModel>,
    private val onRowClick: OnRowClick<Any>,
    private val ITEM_CLICK_VIEW_TYPE: String,
    private val withAdapter: Boolean = false,
    private val index: Int = -1,
    ):
    RecyclerView.Adapter<NotificationPanelBottomSheetAdapter.CommonBottomSheetViewHolder>() , Filterable{

    private var filterList: ArrayList<NotificationPanelBottomSheetModel> = ArrayList()

    init {
        filterList = counterList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonBottomSheetViewHolder {
        val itemCommonBottomSheetBinding = ItemNotiPanelBottomSheetBinding.inflate(
            LayoutInflater.from(parent.context), null, false
        )
        val viewHolder = CommonBottomSheetViewHolder(itemCommonBottomSheetBinding)
        return viewHolder
    }

    override fun getItemCount(): Int = filterList.size

    override fun onBindViewHolder(holder: CommonBottomSheetViewHolder, position: Int) {
        holder.bind(filterList[position],onRowClick)

//        holder.itemView.setOnClickListener {
//            if(withAdapter) {
//                onRowClick.onClick(currentModel.data, ITEM_CLICK_VIEW_TYPE, index)
//            } else {
//                onRowClick.onClick(currentModel.data, ITEM_CLICK_VIEW_TYPE)
//            }
////            bottomSheet.dismiss()
//        }
    }


    override fun getFilter(): Filter? {
        return object : Filter() {
             override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filterList = counterList
                } else {
                    val filteredList: ArrayList<NotificationPanelBottomSheetModel> = ArrayList()
                    for (row in counterList) {


                        if (row.notiname.lowercase().contains(charString.lowercase(Locale.getDefault()))) {
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
                filterList = filterResults.values as ArrayList<NotificationPanelBottomSheetModel>
                notifyDataSetChanged()
            }
        }
    }

    class CommonBottomSheetViewHolder(private val itemBinding: ItemNotiPanelBottomSheetBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(counterBottomSheetModel: NotificationPanelBottomSheetModel, onRowClick: OnRowClick<Any>) {
            itemBinding.counterBottomSheetModel = counterBottomSheetModel
//            itemBinding.itemName.setOnClickListener {
            itemBinding.root.setOnClickListener {
                onRowClick.onClick(counterBottomSheetModel,"OPEN_DETAIL")
            }
        }
    }
}