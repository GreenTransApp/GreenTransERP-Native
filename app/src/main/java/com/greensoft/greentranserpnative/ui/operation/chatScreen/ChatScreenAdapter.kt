package com.greensoft.greentranserpnative.ui.operation.chatScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.ItemChatScreenBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.chatScreen.models.ChatScreenModel
import javax.inject.Inject

class ChatScreenAdapter @Inject constructor(
    private val chatList:ArrayList<ChatScreenModel>,
    private val onRowClick: OnRowClick<Any>,):RecyclerView.Adapter<ChatScreenAdapter.ChatScreenViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatScreenViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutBindling = ItemChatScreenBinding.inflate(inflater, parent, false)
        return ChatScreenViewHolder(layoutBindling)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: ChatScreenViewHolder, position: Int) {
        holder.onBind(chatList[position],onRowClick)
    }

    class ChatScreenViewHolder(var layoutBindling: ItemChatScreenBinding):RecyclerView.ViewHolder(layoutBindling.root){

         fun onBind(chatdata : ChatScreenModel, onRowClick: OnRowClick<Any>){
            layoutBindling.chatsData = chatdata

            layoutBindling.chatCard.setOnClickListener {
                onRowClick.onLongClick(chatdata,"DELETE")
            }
        }
    }
}