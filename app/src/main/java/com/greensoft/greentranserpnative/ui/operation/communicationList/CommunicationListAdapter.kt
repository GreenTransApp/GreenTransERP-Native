package com.greensoft.greentranserpnative.ui.operation.communicationList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.ItemCommunicationListBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.communicationList.models.CommunicationListModel
import javax.inject.Inject

class CommunicationListAdapter @Inject constructor(
    private val communicationCardList:ArrayList<CommunicationListModel>,
    private val onRowClick: OnRowClick<Any>,
    ):RecyclerView.Adapter<CommunicationListAdapter.CommunicationListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunicationListAdapter.CommunicationListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutBinding = ItemCommunicationListBinding.inflate(inflater,parent,false)
        return CommunicationListViewHolder(layoutBinding)
    }

    override fun onBindViewHolder(holder: CommunicationListAdapter.CommunicationListViewHolder, position: Int) {
        holder.onBind(communicationCardList[position],onRowClick)
    }

    override fun getItemCount(): Int {
       return communicationCardList.size
    }


    inner class CommunicationListViewHolder (private val layoutBinding: ItemCommunicationListBinding):RecyclerView.ViewHolder(layoutBinding.root){

        fun onBind(communicationListModel: CommunicationListModel, onRowClick: OnRowClick<Any>){
            layoutBinding.communicationListModel = communicationListModel

            layoutBinding.communicationCard.setOnClickListener {
                onRowClick.onClick(communicationListModel,"OPEN_DETAIL")
            }
        }
    }
}