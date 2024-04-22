package com.greensoft.greentranserpnative.ui.operation.multiple_pod_entry_list.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.PodListItemBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.pending_for_delivery_update_list.models.PodEntryListModel
import javax.inject.Inject

class MultiplePodEntryAdapter  @Inject constructor(
    private val podList:ArrayList<PodEntryListModel>,
    private val onRowClick: OnRowClick<Any>,
):RecyclerView.Adapter<MultiplePodEntryAdapter.MultiplePodEntryViewHolder>(){

    inner class MultiplePodEntryViewHolder(private val layoutBinding: PodListItemBinding) :
        RecyclerView.ViewHolder(layoutBinding.root) {
        fun onBind(model: PodEntryListModel, onRowClick: OnRowClick<Any>) {
            layoutBinding.model = model
            layoutBinding.index = adapterPosition

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiplePodEntryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutBinding = PodListItemBinding.inflate(inflater, parent, false)
        return MultiplePodEntryViewHolder(layoutBinding)
    }

    override fun getItemCount(): Int {
        return podList.size
    }

    override fun onBindViewHolder(holder: MultiplePodEntryViewHolder, position: Int) {
        holder.onBind(podList[position], onRowClick)
    }
}