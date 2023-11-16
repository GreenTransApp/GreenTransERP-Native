package com.greensoft.greentranserpnative.ui.operation.chatScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.ItemCustomerChatScreenBinding
import com.greensoft.greentranserpnative.databinding.ItemDriverChatScreenBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.chatScreen.models.ChatScreenModel
import javax.inject.Inject

class ChatScreenAdapter @Inject constructor(
    private val chatList:ArrayList<ChatScreenModel>,
    private val onRowClick: OnRowClick<Any>,):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val DRIVER_VIEW = 1
    private val CUSTOMER_VIEW = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if(viewType == 1) {
            val layoutDriverBinding = ItemDriverChatScreenBinding.inflate(inflater, parent, false)
            return ChatScreenViewHolder(layoutDriverBinding)
        } else  {
            val layoutCustomerBinding = ItemCustomerChatScreenBinding.inflate(inflater, parent, false)
            return CustomerChatScreenViewHolder(layoutCustomerBinding)
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(chatList[position].sendertype == "D") DRIVER_VIEW else CUSTOMER_VIEW
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            DRIVER_VIEW -> {
                val chatScreenViewHolder = holder as ChatScreenViewHolder
                chatScreenViewHolder.onBind(chatList[holder.adapterPosition], onRowClick)
            }
            CUSTOMER_VIEW -> {
                val customerChatScreenViewHolder = holder as CustomerChatScreenViewHolder
                customerChatScreenViewHolder.onBind(chatList[holder.adapterPosition], onRowClick)
            }
            else -> {

            }
        }
    }

    class ChatScreenViewHolder(var layoutBinding: ItemDriverChatScreenBinding):RecyclerView.ViewHolder(layoutBinding.root){

         fun onBind(chatData : ChatScreenModel, onRowClick: OnRowClick<Any>){
            layoutBinding.chatsData = chatData

//            layoutBindling.chatCard.setOnClickListener {
//                onRowClick.onLongClick(chatdata,"DELETE")
//            }
             layoutBinding.chatCard.setOnLongClickListener {
                 onRowClick.onLongClick(chatData,"DELETE")
                 return@setOnLongClickListener true
             }
        }
    }

    class CustomerChatScreenViewHolder(var layoutBinding: ItemCustomerChatScreenBinding):RecyclerView.ViewHolder(layoutBinding.root){

        fun onBind(chatData : ChatScreenModel, onRowClick: OnRowClick<Any>){
            layoutBinding.chatsData = chatData

//            layoutBinding.chatCard.setOnClickListener {
//                onRowClick.onLongClick(chatData,"DELETE")
//            }
        }
    }
}