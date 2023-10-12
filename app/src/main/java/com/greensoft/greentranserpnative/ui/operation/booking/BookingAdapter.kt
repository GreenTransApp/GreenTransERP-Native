package com.greensoft.greentranserpnative.ui.operation.booking

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.BookingItemViewBinding
import com.greensoft.greentranserpnative.ui.operation.booking.models.ContentSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.PackingSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.TemperatureSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.models.SinglePickupRefModel
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.booking.models.GelPackItemSelectionModel
import javax.inject.Inject

class BookingAdapter @Inject constructor(
    private val bookingList: ArrayList<SinglePickupRefModel>,
    private val onRowClick: OnRowClick<Any>,
//    context: Context
): RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

   inner class BookingViewHolder ( val binding:BookingItemViewBinding):RecyclerView.ViewHolder(binding.root){

        fun bindData(singlePickupRefModel: SinglePickupRefModel, onRowClick: OnRowClick<Any>){
            binding.gridData = singlePickupRefModel
            binding.index = adapterPosition

            binding.packing.setOnClickListener{
                onRowClick.onCLick(singlePickupRefModel, "PACKING_SELECT")
            }
            binding.gelPackItem.setOnClickListener{
                onRowClick.onRowClick(singlePickupRefModel, "GEL_PACK_SELECT",adapterPosition)
            }
            binding.content.setOnClickListener{
                onRowClick.onRowClick(singlePickupRefModel, "CONTENT_SELECT", adapterPosition)

            }

            binding.temperature.setOnClickListener{
                onRowClick.onRowClick(singlePickupRefModel, "TEMPERATURE_SELECT",adapterPosition)
            }
            binding.packing.setOnClickListener{
                onRowClick.onRowClick(singlePickupRefModel, "PACKING_SELECT",adapterPosition)
            }
            binding.btnRemove.setOnClickListener{
                 removeItem(adapterPosition)
                onRowClick.onCLick(singlePickupRefModel, "REMOVE_SELECT")
            }
            binding.selectDatalogger.setOnClickListener {
                onRowClick.onRowClick(singlePickupRefModel, "DATALOGGER_SELECT",adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val binding= BookingItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = BookingViewHolder(binding)
        return viewHolder
    }

    override fun getItemCount(): Int = bookingList.size

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val gridData = bookingList[holder.adapterPosition]
        holder.bindData(gridData,onRowClick)
    }


    fun setContent(contentModel: ContentSelectionModel, adapterPosition: Int) {
        Log.d("TEST_TEST", contentModel.itemname.toString())
        bookingList[adapterPosition].contents = contentModel.itemname
        notifyItemChanged(adapterPosition)
    }
    fun setTemperature(tempModel: TemperatureSelectionModel, adapterPosition: Int) {
        bookingList[adapterPosition].tempurature = tempModel.name
        notifyItemChanged(adapterPosition)
    }
    fun setPacking(pckgModel: PackingSelectionModel, adapterPosition: Int) {
        bookingList[adapterPosition].packing = pckgModel.packingname
        notifyItemChanged(adapterPosition)
    }
    fun setGelPack(Model: GelPackItemSelectionModel, adapterPosition: Int) {
        bookingList[adapterPosition].gelpack = Model.itemname
        notifyItemChanged(adapterPosition)
    }



    fun removeItem(position: Int) {
//        var currentList: ArrayList<SinglePickupRefModel> = ArrayList()
//        val currentListSize = bookingList.size
//        currentList=bookingList
        if(bookingList.size != 0){
            bookingList.removeAt(position)
            notifyItemChanged(position)
//          currentList.addAll(bookingList)
            notifyItemRangeRemoved(position,bookingList.size)
        } else{
            bookingList.clear()
        }

//
    }

}

//notifyItemRangeInserted(position, currentList.size)
//  }catch (e:Exception){
//    error(e)
//  }
// for ( list in bookingList){
//     bookingList.removeAt(position)
//     val currentListSize=bookingList.size
//     currentList.add(list)
//     notifyItemRangeRemoved(position,currentListSize)
//     notifyItemRangeInserted(position,currentList.size)
// }

//          bookingList.addAll(currentList)
//        notifyItemChanged(position,bookingList)