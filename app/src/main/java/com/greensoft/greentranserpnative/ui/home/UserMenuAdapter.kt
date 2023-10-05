package com.greensoft.greentranserpnative.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.MenuItemsBinding
import com.greensoft.greentranserpnative.ui.home.models.UserMenuModel
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick

class UserMenuAdapter (private val menuList: ArrayList<UserMenuModel>,
                       private val onRowClick: OnRowClick<Any>):
                       RecyclerView.Adapter<UserMenuAdapter.MenuViewHolder>() {


      class  MenuViewHolder(private val binding:MenuItemsBinding,):
     RecyclerView.ViewHolder(binding.root)
     {
         fun bindData(model: UserMenuModel, onRowClick: OnRowClick<Any>) {
             binding.userMenu = model
             binding.itemLayout.setOnClickListener{
                 onRowClick.onCLick(model, model.menucode)
             }
         }

     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        var binding: MenuItemsBinding =
            MenuItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserMenuAdapter.MenuViewHolder(binding)
    }

    override fun getItemCount(): Int  = menuList.size

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menuData = menuList[holder.adapterPosition]
        holder.bindData(menuData, onRowClick)
    }

}