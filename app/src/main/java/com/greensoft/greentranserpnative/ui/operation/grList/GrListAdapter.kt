package com.greensoft.greentranserpnative.ui.operation.grList

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.compose.ui.text.toLowerCase
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.ItemGrListBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
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
       holder.onBind(grList[position],onRowClick)
    }

    override fun getItemCount(): Int {
       return grList.size
    }

   inner class GrListViewHolder(private val layoutBinding: ItemGrListBinding):RecyclerView.ViewHolder(layoutBinding.root){
        fun onBind(grListModel: GrListModel,onRowClick: OnRowClick<Any>){
            layoutBinding.grModel = grListModel
            layoutBinding.index = adapterPosition

            layoutBinding.printBtn.setOnClickListener {
                onRowClick.onCLick(grListModel,"BUTTON_CLICK")
            }
        }
    }

    fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                  filterList   = grList
                } else {
                    val filteredList = java.util.ArrayList<GrListModel>()
                    for (row in grList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.grno.toLowerCase()
                                .contains(charString.lowercase(Locale.getDefault()))
                            || row.destname.toLowerCase()
                                .contains(charString.lowercase(Locale.getDefault()))
                            || row.cnge.toLowerCase()
                                .contains(charString.lowercase(Locale.getDefault()))
                            || row.cngr.toLowerCase()
                                .contains(charString.lowercase(Locale.getDefault()))
                            || String.valueOf(row.custname).lowercase(Locale.getDefault())
                                .contains(
                                    charString.lowercase(
                                        Locale.getDefault()
                                    )
                                )
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

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                (filterResults.values as ArrayList<GrListModel>).also {data->
                    filterList = data
                }
                notifyDataSetChanged()
            }
        }
    }
}