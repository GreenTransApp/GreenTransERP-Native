package com.greensoft.greentranserpnative.ui.operation.pickup_manifest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.SavePickupManifestItemBinding
import com.greensoft.greentranserpnative.databinding.SelectedGrItemBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.GrSelectionModel

class SavePickupManifestAdapter (private val manifestList: ArrayList<GrSelectionModel>,
                                 private val onRowClick: OnRowClick<Any>
) : RecyclerView.Adapter<SavePickupManifestAdapter.ManifestViewHolder>() , Filterable {
    private  var filterList: ArrayList<GrSelectionModel>
    init {
        filterList=manifestList
    }
    class ManifestViewHolder(private val binding: SavePickupManifestItemBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bindData(model: GrSelectionModel, onRowClick: OnRowClick<Any>) {
            binding.grModel = model
            binding.index = adapterPosition


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManifestViewHolder {
        var binding: SavePickupManifestItemBinding =
            SavePickupManifestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavePickupManifestAdapter.ManifestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ManifestViewHolder, position: Int) {
        val data = filterList[holder.adapterPosition]
        holder.bindData(data, onRowClick)


    }

    override fun getItemCount():Int = filterList.size

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }
}
