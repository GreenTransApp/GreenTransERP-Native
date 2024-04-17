package com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.adapter

import android.R
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.ScanStickerItemBinding
import com.greensoft.greentranserpnative.databinding.UndeliveredStickerItemBinding
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.GrSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.models.SinglePickupRefModel
import com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.models.ScanStickerModel
import java.util.Locale

class ScanUndeliveryAdapter (private val stickerList: ArrayList<ScanStickerModel>,
                             private val mContext: Context,
                              private val bottomSheetClick: BottomSheetClick<Any>
) : RecyclerView.Adapter<ScanUndeliveryAdapter.ScanUndeliveryViewHolder>(), Filterable {
    private  var filterList: ArrayList<ScanStickerModel>




    init {
        filterList=stickerList
    }
   inner  class ScanUndeliveryViewHolder(val binding:UndeliveredStickerItemBinding ) :
        RecyclerView.ViewHolder(binding.root){
         val reasonList = arrayOf(
             "ADDRESS NOT AVAILABLE",
             "DAMAGED GOODS",
             "CONCERN PERSON NOT AVAILABLE",
             "TRAIN LATE",
             "FLIGHT DELAY",
             "SUNDAY HOLIDAY"
         )

       private fun setOnClicks(model: ScanStickerModel) {
           binding.reason.onItemSelectedListener = object:
               AdapterView.OnItemSelectedListener {
               override fun onItemSelected(
                   parent: AdapterView<*>?,
                   view: View?,
                   position: Int,
                   id: Long
               ) {
                   stickerList[adapterPosition].reasons = reasonList[position]
                   Log.d("reasons", "onItemSelected:${ stickerList[adapterPosition].reasons} ")
//                   when(item) {
////
//                       "ADDRESS NOT AVAILABLE" -> {
//                       }
//                       "DAMAGED GOODS" -> {
//                       }
//                       "CONCERN PERSON NOT AVAILABLE" -> {
//                       }
//                       "TRAIN LATE" -> {
//                       }
//                       "FLIGHT DELAY" -> {
//                       }
//                       "SUNDAY HOLIDAY" -> {
//                       }
//                       else -> {
//
//                       }
//                   }
               }

               override fun onNothingSelected(parent: AdapterView<*>?) {

               }

           }
       }

        fun bindData(model: ScanStickerModel) {
            binding.model = model
            binding.index = adapterPosition
            setUpAdapter()
            setOnClicks(model)


        }
       private fun setUpAdapter() {
           val reasonAdapter = ArrayAdapter(mContext, R.layout.simple_list_item_1, reasonList)
           binding.reason.adapter = reasonAdapter

       }
    }

    fun getAdapterData(): ArrayList<ScanStickerModel>{
        return stickerList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanUndeliveryViewHolder {
        var binding: UndeliveredStickerItemBinding =
            UndeliveredStickerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScanUndeliveryViewHolder(binding)

    }

    override fun getItemCount(): Int = filterList.size

    override fun onBindViewHolder(holder: ScanUndeliveryViewHolder, position: Int) {
        val data = filterList[holder.adapterPosition]
        holder.bindData(data, )
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filterList = stickerList
                } else {
                    val filteredList: ArrayList<ScanStickerModel> = ArrayList()
                    for (row in stickerList) {
                        if(
                            row.stickerno.toString().contains(charString.lowercase(Locale.getDefault()))

                        ){
                            filteredList.add(row)
                        }
                    }
                    filterList = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence?,
                filterResults: FilterResults
            ) {
                filterList = filterResults.values as ArrayList<ScanStickerModel>
                notifyDataSetChanged()
            }
        }
    }
}