package com.greensoft.greentranserpnative.ui.operation.pickup_manifest.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.SelectedGrItemBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.GrSelectionModel

class GrSelectionAdapter(private val grList: ArrayList<GrSelectionModel>,
                         private val onRowClick: OnRowClick<Any>
) : RecyclerView.Adapter<GrSelectionAdapter.GrSelectionViewHolder>() , Filterable {
    private  var filterList: ArrayList<GrSelectionModel>
    var isCheck:Boolean=false

    init {
        filterList=grList
    }
    class GrSelectionViewHolder(private val binding: SelectedGrItemBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bindData(model: GrSelectionModel, onRowClick: OnRowClick<Any>) {
            binding.grModel = model
            binding.index = adapterPosition



            binding.allCheck.setOnCheckedChangeListener{_,isChecked->
               if(isChecked){
                   onRowClick.onCLick(model, "CHECK_SELECTED")
                   Log.d("test", "bindData: checkbox check")
               }else{

               }

            }


        }
    }

//   open fun checkAll(isAllChecked:Boolean){
//         isCheck=isAllChecked;
//         notifyDataSetChanged()
//     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrSelectionViewHolder {
        var binding: SelectedGrItemBinding =
            SelectedGrItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GrSelectionAdapter.GrSelectionViewHolder(binding)
    }

    override fun getItemCount(): Int = filterList.size

    override fun onBindViewHolder(holder: GrSelectionViewHolder, position: Int) {
        val data = filterList[holder.adapterPosition]
        holder.bindData(data, onRowClick)
    }

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }
}
