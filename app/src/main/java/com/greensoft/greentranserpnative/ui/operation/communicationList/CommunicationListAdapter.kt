package com.greensoft.greentranserpnative.ui.operation.communicationList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.databinding.ItemCommunicationListBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.communicationList.models.CommunicationListModel
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.search_list.SearchListAdapter
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.search_list.models.SearchListModel
import javax.inject.Inject

class CommunicationListAdapter @Inject constructor(

    private val communicationCardList:ArrayList<CommunicationListModel>,
    private val onRowClick: OnRowClick<Any>,
//    ):RecyclerView.Adapter<CommunicationListAdapter.CommunicationListViewHolder>(){
    ):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val EMPTY_VIEW = 1
    private val DATA_VIEW = 2

    class EmptyViewHolder(itemView: View?) : RecyclerView.ViewHolder(
        itemView!!
    ) {
        var txtType: TextView

        init {
            txtType = itemView!!.findViewById<View>(R.id.text_send) as TextView

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == EMPTY_VIEW) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.empty_view, parent, false)
            EmptyViewHolder(view)
        } else {
            val inflater = LayoutInflater.from(parent.context)
            val layoutBinding = ItemCommunicationListBinding.inflate(inflater, parent, false)
            CommunicationListViewHolder(layoutBinding, parent.context)
        }
    }

//    override fun onBindViewHolder(holder: CommunicationListAdapter.CommunicationListViewHolder, position: Int) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            EMPTY_VIEW -> (holder as EmptyViewHolder).txtType.text = "No Data Available"
            DATA_VIEW -> {
                val dataViewHolder = holder as CommunicationListAdapter.CommunicationListViewHolder
                val dataModel: CommunicationListModel = communicationCardList[position]
//                holder.onBind(communicationCardList[position],onRowClick)
                dataViewHolder.onBind(dataModel, onRowClick)
            }
        }

    }

    override fun getItemCount(): Int {
       return communicationCardList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(communicationCardList.size <= 0) EMPTY_VIEW else DATA_VIEW
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