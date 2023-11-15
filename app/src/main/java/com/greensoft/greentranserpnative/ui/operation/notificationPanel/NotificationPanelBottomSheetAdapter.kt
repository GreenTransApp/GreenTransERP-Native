package com.greensoft.greentranserpnative.ui.operation.notificationPanel

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.greensoft.greentranserpnative.databinding.ItemNotiPanelBottomSheetBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.communicationList.CommunicationListActivity
import com.greensoft.greentranserpnative.ui.operation.notificationPanel.model.NotificationPanelBottomSheetModel
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
        val viewHolder = CommonBottomSheetViewHolder(itemCommonBottomSheetBinding, mContext)
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

    class CommonBottomSheetViewHolder(private val itemBinding: ItemNotiPanelBottomSheetBinding,
        private val context: Context): RecyclerView.ViewHolder(itemBinding.root) {

        lateinit var badgeDrawable: BadgeDrawable
        fun bind(counterBottomSheetModel: NotificationPanelBottomSheetModel, onRowClick: OnRowClick<Any>) {
            itemBinding.counterBottomSheetModel = counterBottomSheetModel
//            itemBinding.itemName.setOnClickListener {
            itemBinding.root.setOnClickListener {
//                onRowClick.onClick(counterBottomSheetModel,counterBottomSheetModel.page)
                when(counterBottomSheetModel.page) {
                    "COMM" -> {
                        val intent = Intent(context, CommunicationListActivity::class.java)
                        context.startActivity(intent)
                    }
                    else -> {
                        Toast.makeText(context, "Not yet implemented. Please contact system admin.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            itemBinding.itemName.text = counterBottomSheetModel.notiname
            badgeDrawable = BadgeDrawable.create(context)
            //Important to change the position of the Badge
            badgeDrawable.number = counterBottomSheetModel.noticount
            badgeDrawable.horizontalOffset = 5
            badgeDrawable.verticalOffset = 5
            itemBinding.itemName.viewTreeObserver
                .addOnGlobalLayoutListener(@ExperimentalBadgeUtils object :
                    ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        BadgeUtils.attachBadgeDrawable(badgeDrawable, itemBinding.itemName, null)
                        itemBinding.itemName.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })

        }
    }
}