package com.greensoft.greentranserpnative.ui.operation.booking

import android.content.Context
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
import kotlin.math.log
import kotlin.math.log10

class BookingAdapter @Inject constructor(
    private val mContext: Context,
    private val bookingList: ArrayList<SinglePickupRefModel>,
    private val onRowClick: OnRowClick<Any>,
//    context: Context
): RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

   inner class BookingViewHolder ( val binding:BookingItemViewBinding):RecyclerView.ViewHolder(binding.root){

        fun bindData(singlePickupRefModel: SinglePickupRefModel, onRowClick: OnRowClick<Any>){
            binding.gridData = singlePickupRefModel
            binding.index = adapterPosition

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
                 removeItem(singlePickupRefModel,adapterPosition)
//                onRowClick.onCLick(singlePickupRefModel, "REMOVE_SELECT")
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
    fun setPacking(pckgsModel: PackingSelectionModel, adapterPosition: Int) {
        bookingList[adapterPosition].packing = pckgsModel.packingname
        notifyItemChanged(adapterPosition)
    }
    fun setGelPack(model: GelPackItemSelectionModel, adapterPosition: Int) {
        bookingList[adapterPosition].gelpack = model.itemname
        notifyItemChanged(adapterPosition)
    }

//     fun removeItem(model:SinglePickupRefModel,adapterPosition: Int){
//
//     }



    fun removeItem(model:SinglePickupRefModel, index: Int) {
        bookingList.removeAt(index)
        notifyDataSetChanged()
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