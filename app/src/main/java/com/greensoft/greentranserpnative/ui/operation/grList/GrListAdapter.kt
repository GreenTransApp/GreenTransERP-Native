package com.greensoft.greentranserpnative.ui.operation.grList

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.compose.ui.text.toLowerCase
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.ItemGrListBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.call_register.models.CallRegisterModel
import com.greensoft.greentranserpnative.ui.operation.grList.models.GrListModel
import java.lang.String
import java.util.Locale
import javax.inject.Inject
import kotlin.Any
import kotlin.CharSequence
import kotlin.Int

class GrListAdapter @Inject constructor(
    private val grList:ArrayList<GrListModel>,
    private val onRowClick: OnRowClick<Any>,
    ):RecyclerView.Adapter<GrListAdapter.GrListViewHolder>(){
    private var filterList: ArrayList<GrListModel>
    init {
        filterList = grList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutBinding = ItemGrListBinding.inflate(inflater,parent,false)
        return GrListViewHolder(layoutBinding)
    }

    override fun onBindViewHolder(holder: GrListViewHolder, position: Int) {
       holder.onBind(filterList[position],onRowClick)
    }

    override fun getItemCount(): Int {
       return filterList.size
    }

   inner class GrListViewHolder(private val layoutBinding: ItemGrListBinding):RecyclerView.ViewHolder(layoutBinding.root){
        fun onBind(grListModel: GrListModel,onRowClick: OnRowClick<Any>){
            layoutBinding.grModel = grListModel
            layoutBinding.index = adapterPosition

            layoutBinding.scanBtn.setOnClickListener {
                onRowClick.onCLick(grListModel,"SCAN_STICKER")
            }

            layoutBinding.printBtn.setOnClickListener {
                onRowClick.onCLick(grListModel,"PRINT_STICKER")
            }
        }
    }

     fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filterList = grList
                } else {
                    val filteredList: ArrayList<GrListModel> = ArrayList()
                    for (row in grList) {
                        if(
//                             row.custname.lowercase().contains(charString.lowercase(Locale.getDefault()))
                            row.grno.contains(charString.lowercase(Locale.getDefault()))
                            ||row.destname.contains(charString.lowercase(Locale.getDefault()))
                            ||row.cnge.lowercase().contains(charString.lowercase(Locale.getDefault()))
                            ||row.cngr.lowercase().contains(charString.lowercase(Locale.getDefault()))
                            ||String.valueOf(row.custname).lowercase(Locale.getDefault())
                                .contains(
                                    charString.lowercase(
                                        Locale.getDefault()
                                    )
                                )

                        ){
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
                filterList = filterResults.values as ArrayList<GrListModel>
                notifyDataSetChanged()
            }
        }
    }
}