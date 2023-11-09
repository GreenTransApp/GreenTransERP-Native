package com.greensoft.greentranserpnative.ui.operation.counterDetail
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import android.widget.Filter
//import android.widget.Filterable
//import androidx.recyclerview.widget.RecyclerView
//import com.greensoft.greentranserpnative.databinding.ItemCounterBottomSheetBinding
//import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
//import com.greensoft.greentranserpnative.ui.operation.counterDetail.model.CounterBottomSheetModel
//import java.util.*
//import kotlin.collections.ArrayList
//
//
//open class CounterBottomSheetAdapter<T>(
//    private val mContext: Context,
////    private val bottomSheet: CounterBottomSheet<T>,
//    private val rvList: ArrayList<CounterBottomSheetModel<T>>,
//    private val bottomSheetClick: BottomSheetClick<T>,
//    private val ITEM_CLICK_VIEW_TYPE: String,
//    private val withAdapter: Boolean = false,
//    private val index: Int = -1,
//    ):
//    RecyclerView.Adapter<CounterBottomSheetAdapter.CommonBottomSheetViewHolder>() , Filterable{
//
//    private var filterList: ArrayList<CounterBottomSheetModel<T>> = ArrayList()
//
//    init {
//        filterList = rvList
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonBottomSheetViewHolder {
//        val itemCommonBottomSheetBinding = ItemCounterBottomSheetBinding.inflate(
//            LayoutInflater.from(parent.context), null, false
//        )
//        val viewHolder = CommonBottomSheetViewHolder(itemCommonBottomSheetBinding)
//        return viewHolder
//    }
//
//    override fun getItemCount(): Int = filterList.size
//
//    override fun onBindViewHolder(holder: CommonBottomSheetViewHolder, position: Int) {
//        val currentModel = filterList[holder.adapterPosition]
//        holder.bind(currentModel.title)
//        holder.itemView.setOnClickListener {
//            if(withAdapter) {
//                bottomSheetClick.onItemClickWithAdapter(currentModel.data, ITEM_CLICK_VIEW_TYPE, index)
//            } else {
//                bottomSheetClick.onItemClick(currentModel.data, ITEM_CLICK_VIEW_TYPE)
//            }
////            bottomSheet.dismiss()
//        }
//    }
//
//
//    override fun getFilter(): Filter? {
//        return object : Filter() {
//             override fun performFiltering(charSequence: CharSequence): FilterResults? {
//                val charString = charSequence.toString()
//                if (charString.isEmpty()) {
//                    filterList = rvList
//                } else {
//                    val filteredList: ArrayList<CounterBottomSheetModel<T>> = ArrayList()
//                    for (row in rvList) {
//
//                        // name match condition. this might differ depending on your requirement
//                        // here we are looking for name or phone number match
//                        if (row.title.lowercase().contains(charString.lowercase(Locale.getDefault()))) {
//                            filteredList.add(row)
//                        }
//                    }
//                    filterList = filteredList
//                }
//                val filterResults = FilterResults()
//                filterResults.values = filterList
//                return filterResults
//            }
//
//            override fun publishResults(
//                charSequence: CharSequence?,
//                filterResults: FilterResults
//            ) {
//                filterList = filterResults.values as ArrayList<CounterBottomSheetModel<T>>
//                notifyDataSetChanged()
//            }
//        }
//    }
//
//    class CommonBottomSheetViewHolder(private val itemBinding: ItemCounterBottomSheetBinding): RecyclerView.ViewHolder(itemBinding.root) {
//        fun bind(title: String) {
//            itemBinding.itemName.text = title
//        }
//    }
//}