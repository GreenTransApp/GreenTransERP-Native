package com.greensoft.greentranserpnative.ui.operation.booking

import android.R
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
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
    private val mContext: Context,
    private val activity: BookingActivity,
    private val bookingList: ArrayList<SinglePickupRefModel>,
    private val onRowClick: OnRowClick<Any>,
//    context: Context
): RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

   inner class BookingViewHolder ( val binding:BookingItemViewBinding):RecyclerView.ViewHolder(binding.root) {
       private var dataLoggerItems = arrayOf("SELECT", "YES", "NO")
       var actualVWeight:Float=0f
       var actualWeight:Int=0
       fun setOnClicks(singlePickupRefModel: SinglePickupRefModel) {
           binding.gelPackItem.setOnClickListener {
               onRowClick.onRowClick(singlePickupRefModel, "GEL_PACK_SELECT", adapterPosition)
           }
           binding.content.setOnClickListener {
               onRowClick.onRowClick(singlePickupRefModel, "CONTENT_SELECT", adapterPosition)

           }

           binding.temperature.setOnClickListener {
               onRowClick.onRowClick(singlePickupRefModel, "TEMPERATURE_SELECT", adapterPosition)
           }
           binding.packing.setOnClickListener {
               onRowClick.onRowClick(singlePickupRefModel, "PACKING_SELECT", adapterPosition)
           }
           binding.btnRemove.setOnClickListener {
               removeItem(singlePickupRefModel, adapterPosition)
//                onRowClick.onCLick(singlePickupRefModel, "REMOVE_SELECT")
           }

           binding.length.addTextChangedListener(object :TextWatcher{
               override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
               override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
               override fun afterTextChanged(s: Editable?) {
//                   onRowClick.onRowClick(singlePickupRefModel, "LENGTH_SELECT", adapterPosition)
//                   Log.d("test", "afterTextChanged:  length of  obj")
                   calculateVWeight(adapterPosition, binding)
                   Log.d("actualVWeight", " data  -----${actualVWeight}")
               }
           })

           binding.breadth.addTextChangedListener(object :TextWatcher{
               override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
               override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
               override fun afterTextChanged(s: Editable?) {
//                   onRowClick.onRowClick(singlePickupRefModel, "LENGTH_SELECT", adapterPosition)
//                   Log.d("test", "afterTextChanged:  length of  obj")
                   calculateVWeight(adapterPosition, binding)

               }
           })
           binding.height.addTextChangedListener(object :TextWatcher{
               override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
               override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
               override fun afterTextChanged(s: Editable?) {
//                   onRowClick.onRowClick(singlePickupRefModel, "LENGTH_SELECT", adapterPosition)
//                   Log.d("test", "afterTextChanged:  length of  obj")
                   calculateVWeight(adapterPosition, binding)
               }
           })

           binding.weight.addTextChangedListener(object :TextWatcher{
               override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
               override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
               override fun afterTextChanged(s: Editable?) {
//                   onRowClick.onRowClick(singlePickupRefModel, "LENGTH_SELECT", adapterPosition)
//                   Log.d("test", "afterTextChanged:  length of  obj")
                   calculateTotalAWeight(adapterPosition, binding)
               }
           })


//            binding.selectDatalogger.setOnItemClickListener {
//                onRowClick.onRowClick(singlePickupRefModel, "DATALOGGER_SELECT",adapterPosition)
//            }

           binding.chekGelPack.setOnCheckedChangeListener { _, isChecked ->
               if (isChecked) {
                   binding.gelPackItem.isEnabled = true
               } else {
                   binding.gelPackItem.isEnabled = false
                   binding.gelPackItem.text.clear()
               }
           }

//                 binding.inputLayoutDatalogger.setOnClickListener {
//                     Toast.makeText(mContext, "test", Toast.LENGTH_SHORT).show()
//                     val dataLoggerAdapter =
//                         ArrayAdapter(mContext, R.layout.simple_list_item_1, dataLoggerItems)
//                     binding.selectDatalogger.adapter = dataLoggerAdapter
//           binding.selectDatalogger.onItemSelectedListener = object: OnItemSelectedListener {
//               override fun onItemSelected(
//                   parent: AdapterView<*>?,
//                   view: View?,
//                   position: Int,
//                   id: Long
//               ) {
//
//               }
//
//               override fun onNothingSelected(parent: AdapterView<*>?) {
//
//               }
//
//           }


       }
         fun calculateVWeight(index: Int, layoutBinding: BookingItemViewBinding){
           if(layoutBinding.length.text.toString().isNotEmpty() && layoutBinding.breadth.toString().isNotEmpty() && layoutBinding.height.toString().isNotEmpty() ) {
               Log.d("CALC VWEIGHT", "Data in wrong format: LENGTH: ${layoutBinding.length.text}, BREADTH: ${layoutBinding.breadth.text}, HEIGHT: ${layoutBinding.height.text}")
           }
           val length: Double? = layoutBinding.length.text.toString().toDoubleOrNull()
           val breadth: Double? = layoutBinding.breadth.text.toString().toDoubleOrNull()
           val height: Double? = layoutBinding.height.text.toString().toDoubleOrNull()

           if(length == null) {
//               activity.errorToast("Length in wrong format.")
               return;
           } else if(breadth == null) {
//               activity.errorToast("Breadth in wrong format.")
               return
           } else if(height == null) {
//               activity.errorToast("Height in wrong format.")
               return;
           }

           if(length > 0 && breadth > 0 && height > 0){
               Log.d("TEST", "length:${ bookingList[index].pckglength.toString()}")
               if(bookingList[index].servicetype =="A"){
//                   actualVWeight= (bookingList[index].pckglength * bookingList[index].pckgbreath * bookingList[index].pckgheight).toFloat()/6000
                   actualVWeight= (layoutBinding.length.toString().toInt() * layoutBinding.breadth.toString().toInt() * layoutBinding.height.toString().toInt()).toFloat()/6000
               }else{
//                   actualVWeight= (bookingList[index].pckglength * bookingList[index].pckgbreath * bookingList[index].pckgheight).toFloat()/5000
                   actualVWeight= (layoutBinding.length.text.toString().toDouble() * layoutBinding.breadth.text.toString().toDouble() * layoutBinding.height.text.toString().toDouble()).toFloat()/5000
               }

           }else if(bookingList[index].volfactor.isNaN()){
               actualVWeight = 0f

           }else{
               actualVWeight= Math.ceil(bookingList[index].volfactor.toFloat() * bookingList[index].pcs.toDouble()).toFloat()
           }
           activity.setVWeight(actualVWeight)
//           Log.d("test", "calculateVWeight: vol weight${actualVWeight} ")
           calculateChargeableWeight()

       }

         fun calculateTotalAWeight(index: Int, layoutBinding: BookingItemViewBinding){
           var aWeight: Int? = layoutBinding.weight.text.toString().toIntOrNull()
            if(aWeight == null) {
//               activity.errorToast("Weight in wrong format.")
               return;
           }
           if(aWeight > 0)
               actualWeight =layoutBinding.weight.text.toString().toInt()

           if(actualWeight.toFloat().isNaN()){
               actualWeight=0
           }
          activity.setAWeight(actualWeight)
           calculateChargeableWeight()
       }


        fun calculateChargeableWeight(){

           if((actualWeight.toString().isNotEmpty()) && actualVWeight.toString().isNotEmpty()){
               if(actualVWeight > actualWeight){
                   activity.setCWeight(actualVWeight.toInt())

               } else if(actualVWeight<= actualWeight.toFloat()){
                   activity.setCWeight(actualWeight.toInt())
               }
           }
       }
     fun resetCalculation(productCode:String){
         calculateVWeight(adapterPosition, binding)
         calculateTotalAWeight(adapterPosition, binding)
     }

       fun bindData(singlePickupRefModel: SinglePickupRefModel, onRowClick: OnRowClick<Any>) {
           binding.gridData = singlePickupRefModel
           binding.index = adapterPosition
           setOnClicks(singlePickupRefModel)
           setUpAdapter()
       }

       private fun setUpAdapter() {
           val dataLoggerAdapter = ArrayAdapter(mContext, R.layout.simple_list_item_1, dataLoggerItems)
           binding.selectDatalogger.adapter = dataLoggerAdapter
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