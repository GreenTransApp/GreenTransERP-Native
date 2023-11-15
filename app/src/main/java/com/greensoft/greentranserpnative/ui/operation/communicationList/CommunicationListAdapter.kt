package com.greensoft.greentranserpnative.ui.operation.communicationList

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
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
        return CommunicationListViewHolder(layoutBinding, parent.context)
    }

    override fun onBindViewHolder(holder: CommunicationListAdapter.CommunicationListViewHolder, position: Int) {
        holder.onBind(communicationCardList[position],onRowClick)
    }

    override fun getItemCount(): Int {
       return communicationCardList.size
    }


    inner class CommunicationListViewHolder (private val layoutBinding: ItemCommunicationListBinding,
        private val context: Context):RecyclerView.ViewHolder(layoutBinding.root){
        lateinit var badgeDrawable: BadgeDrawable
        fun onBind(communicationListModel: CommunicationListModel, onRowClick: OnRowClick<Any>){
            layoutBinding.communicationListModel = communicationListModel

            layoutBinding.communicationCard.setOnClickListener {
                onRowClick.onClick(communicationListModel,"OPEN_CHAT")
            }

            badgeDrawable = BadgeDrawable.create(context)
            //Important to change the position of the Badge
            badgeDrawable.number = communicationListModel.totalcustcomments
            badgeDrawable.horizontalOffset = 45
            badgeDrawable.verticalOffset = 25
            layoutBinding.linearLayoutInsideCard.viewTreeObserver
                .addOnGlobalLayoutListener(@ExperimentalBadgeUtils object :
                    ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        BadgeUtils.attachBadgeDrawable(badgeDrawable, layoutBinding.linearLayoutInsideCard, null)
                        layoutBinding.linearLayoutInsideCard.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })

        }
    }
}