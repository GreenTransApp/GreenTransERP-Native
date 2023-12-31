package com.greensoft.greentranserpnative.ui.operation.pickup_manifest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.SelectedGrItemBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.adapter.GrSelectionAdapter.GrSelectionViewHolder
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.GrSelectionModel
import java.util.Locale

class GrSelectionAdapter(private val grList: ArrayList<GrSelectionModel>,
                         private val mContext: Context,
                         private val onRowClick: OnRowClick<Any>
) : RecyclerView.Adapter<GrSelectionViewHolder>() , Filterable {
    private  var filterList: ArrayList<GrSelectionModel>
    var isAllCheck:Boolean=false
    var notCheck:Boolean=true

    init {
        filterList=grList
    }
   inner class GrSelectionViewHolder(private val binding: SelectedGrItemBinding) :
        RecyclerView.ViewHolder(binding.root){


        fun bindData(model: GrSelectionModel, onRowClick: OnRowClick<Any>) {
            binding.grModel = model
            binding.index = adapterPosition
//            binding.selectCheck.isChecked = isAllCheck
            binding.selectCheck.setOnCheckedChangeListener{_,isChecked->
//               if(!isChecked){
//                   notCheck=false
//                   onRowClick.onClick(model, "CHECK_SELECTED")
//               }else{
//                   onRowClick.onClick(model, "CHECK_SELECTED")
//               }

                model.isSelected = isChecked
                binding.grModel = model

            }
        }


    }

    fun getSelectedItems(): ArrayList<GrSelectionModel> {
        val selectedItems: ArrayList<GrSelectionModel> = ArrayList()
        grList.forEachIndexed { index, grSelectionModel ->
            if(grSelectionModel.isSelected) {
                selectedItems.add(grSelectionModel)
            }
        }
        return selectedItems
    }

//    fun checkManifestSelectedOrNot(){
//      if(notCheck == true){
//          Toast.makeText(mContext, "Please select atleast one  slip", Toast.LENGTH_SHORT).show()
//      }
//    }
     fun selectAll( isChecked:Boolean){
         isAllCheck=isChecked
         notifyDataSetChanged()
     }
//   fun checkAll(){
//       isCheck=true
//         notifyDataSetChanged()
//     }
//    fun unCheckAll(){
//        isCheck=false
//        notifyDataSetChanged()
//    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrSelectionViewHolder {
        var binding: SelectedGrItemBinding =
            SelectedGrItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GrSelectionViewHolder(binding)
    }

    override fun getItemCount(): Int = filterList.size

    override fun onBindViewHolder(holder: GrSelectionViewHolder, position: Int) {
        val data = filterList[holder.adapterPosition]
        holder.bindData(data, onRowClick)

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filterList = grList
                } else {
                    val filteredList: ArrayList<GrSelectionModel> = ArrayList()
                    for (row in grList) {
                        if(
                            row.grno.contains(charString.lowercase(Locale.getDefault()))
                            || row.custname?.contains(charString.lowercase(Locale.getDefault())) == true
                            ||row.picktime.lowercase().contains(charString.lowercase(Locale.getDefault()))
                            ||row.destname.lowercase().contains(charString.lowercase(Locale.getDefault()))
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
                filterList = filterResults.values as ArrayList<GrSelectionModel>
                notifyDataSetChanged()
            }
        }
    }
}
