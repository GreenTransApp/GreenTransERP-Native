package com.greensoft.greentranserpnative.ui.operation.pickup_reference

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.PickupRefRecyclerItemBinding
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.models.PickupRefModel
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import java.util.Locale


class PickupReferenceAdapter(private val pickupRefList: ArrayList<PickupRefModel>,
                             private val onRowClick: OnRowClick<Any>
) :
    RecyclerView.Adapter<PickupReferenceAdapter.PickupRefViewHolder>() ,Filterable {

//    private lateinit var binding: PickupRefRecyclerItemBinding
//    lateinit var onRowClick: OnRowClick<PickupRefModel>
private  var filterList: ArrayList<PickupRefModel>
    init {
        filterList=pickupRefList
    }



    class PickupRefViewHolder(private val binding: PickupRefRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(pickupRefModel: PickupRefModel, onRowClick: OnRowClick<Any>) {
            binding.pickupRefData = pickupRefModel
            binding.index = adapterPosition
            binding.btnSelect.setOnClickListener {
                onRowClick.onCLick(pickupRefModel, "REF_SELECT")
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickupRefViewHolder {
        var binding: PickupRefRecyclerItemBinding =
            PickupRefRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PickupRefViewHolder(binding)
    }

    override fun getItemCount(): Int = filterList.size

    override fun onBindViewHolder(holder: PickupRefViewHolder, position: Int) {
        val pickupRefData = filterList[holder.adapterPosition]
        holder.bindData(pickupRefData, onRowClick)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filterList = pickupRefList
                } else {
                    val filteredList: ArrayList<PickupRefModel> = ArrayList()
                    for (row in pickupRefList) {
                        if(row.custname.lowercase().contains(charString.lowercase(Locale.getDefault()))
                            ||row.jobno.contains(charString.lowercase(Locale.getDefault()))
                            ||row.referenceno.contains(charString.lowercase(Locale.getDefault()))
                            ||row.jobdate.contains(charString.lowercase(Locale.getDefault()))
                            ||row.origin.contains(charString.lowercase(Locale.getDefault()))
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
                filterList = filterResults.values as ArrayList<PickupRefModel>
                notifyDataSetChanged()
            }
        }
    }
}