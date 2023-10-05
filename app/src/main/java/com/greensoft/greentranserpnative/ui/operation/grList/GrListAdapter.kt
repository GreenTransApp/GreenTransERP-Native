package com.greensoft.greentranserpnative.ui.operation.grList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.ItemGrListBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.grList.models.GrListModel
import javax.inject.Inject

class GrListAdapter @Inject constructor(
    private val grList:ArrayList<GrListModel>,
    private val onRowClick: OnRowClick<Any>,
    ):RecyclerView.Adapter<GrListAdapter.GrListViewHolder>(){

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
}