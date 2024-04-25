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
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.LoadingListModel
import java.util.Locale

class GrSelectionAdapter(private val grList: ArrayList<LoadingListModel>,
                         private val mContext: Context,
                         private val onRowClick: OnRowClick<Any>
) : RecyclerView.Adapter<GrSelectionViewHolder>() , Filterable {
    private  var filterList: ArrayList<LoadingListModel>
    var isAllCheck:Boolean=false
    var notCheck:Boolean=true

    companion object {
        val LOADING_NO_CLICK_TAG = "LOADING_NO_CLICK_TAG"
        val CHECK_ALL_CLICK_TAG = "CHECK_ALL_CLICK_TAG"
    }

    init {
        filterList=grList
    }
   inner class GrSelectionViewHolder(private val binding: SelectedGrItemBinding) :
        RecyclerView.ViewHolder(binding.root){


        fun bindData(model: LoadingListModel, onRowClick: OnRowClick<Any>) {
            binding.grModel = model
            binding.index = adapterPosition
            binding.loadingNoTxt.setOnClickListener {
                onRowClick.onRowClick(model, GrSelectionAdapter.LOADING_NO_CLICK_TAG, adapterPosition)
            }
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

    fun getSelectedItems(): ArrayList<LoadingListModel> {
        val selectedItems: ArrayList<LoadingListModel> = ArrayList()
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
                    val filteredList: ArrayList<LoadingListModel> = ArrayList()
                    for (row in grList) {
                        if(
                            row.loadingno.toString().contains(charString.lowercase(Locale.getDefault()))
//                            || row.loadingtime?.contains(charString.lowercase(Locale.getDefault())) == true

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
                filterList = filterResults.values as ArrayList<LoadingListModel>
                notifyDataSetChanged()
            }
        }
    }
}
