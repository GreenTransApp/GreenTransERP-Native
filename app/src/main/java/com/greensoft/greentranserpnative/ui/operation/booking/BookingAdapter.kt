package com.greensoft.greentranserpnative.ui.operation.booking

import android.R
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.compose.ui.res.colorResource
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.BookingItemViewBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.booking.models.ContentSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.GelPackItemSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.PackingSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.TemperatureSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.models.SinglePickupRefModel
import javax.inject.Inject
import kotlin.math.ceil

class BookingAdapter @Inject constructor(
    private val mContext: Context,
    private var serviceType: String,
    private val activity: BookingActivity,
    private val bookingList: ArrayList<SinglePickupRefModel>,
    private val onRowClick: OnRowClick<Any>,
//    context: Context
): RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {
    var actualVWeight:Float=0f
    var actualWeight:Int=0
    val TAG ="BookingAdapter"

    val originalColor = Color.WHITE
    val changeBoxColor = Color.RED
    val validateColor =  Color.parseColor("#509753")

    private fun logDebug(str: String?) {
        var printable = str
        if(printable.isNullOrBlank()) {
            printable = "EMPTY"
        }
        Log.d(TAG, printable)
    }

//    init {
//        setObservers()
//    }
   inner class BookingViewHolder ( val binding:BookingItemViewBinding):RecyclerView.ViewHolder(binding.root) {
       private var dataLoggerItems = arrayOf("YES", "NO")
//    private var dataLoggerItems = arrayOf("SELECT", "YES", "NO")



    private fun setOnClicks(singlePickupRefModel: SinglePickupRefModel) {
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
//          binding.btnValidateBox.setOnClickListener {
//              onRowClick.onRowClick(singlePickupRefModel, "VALIDATE_BOX", adapterPosition)
//
//          }

//           binding.pckgs.addTextChangedListener(object : TextWatcher {
//               override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//               }
//
//               override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//               }
//
//               override fun afterTextChanged(p0: Editable?) {
//                   val enteredVal = p0.toString().toIntOrNull()
//                   if (enteredVal != null) {
//                       if (enteredVal <= 0) {
//                           binding.pckgs.setText("")
//                       }
//                   }
//                   if (enteredVal != null && enteredVal.toString().startsWith("0")) {
////                       binding.pckgs.setText(trimLeadingZeros(p0.toString()))
//                   }
//               }
//
//           })

        binding.pckgs.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                bookingList[adapterPosition].pcs = p0.toString().toIntOrNull() ?: 0
                Log.d("entered_pckgs",bookingList[adapterPosition].pcs.toString())
//                val enteredVal = p0.toString().toIntOrNull()
//                if (enteredVal != null) {
//                    if (enteredVal <= 0) {
////                        binding.pckgs.setText("")
//
//                        bookingList[adapterPosition].pcs = binding.pckgs.text.toString().toInt()
//                        Log.d("entered_pckgs",bookingList[adapterPosition].pcs.toString())
//                    }
//                }
//                if (enteredVal != null && enteredVal.toString().startsWith("0")) {
////                       binding.pckgs.setText(trimLeadingZeros(p0.toString()))
//                }
            }

        })

           binding.length.addTextChangedListener(object : TextWatcher {
               override fun beforeTextChanged(
                   s: CharSequence?,
                   start: Int,
                   count: Int,
                   after: Int
               ) {
               }

               override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
               override fun afterTextChanged(s: Editable?) {
//                   onRowClick.onRowClick(singlePickupRefModel, "LENGTH_SELECT", adapterPosition)
//                   Log.d("test", "afterTextChanged:  length of  obj")
                       bookingList[adapterPosition].localLength =
                           s.toString().toDoubleOrNull() ?: "0".toDouble()
                       calculateVWeight(adapterPosition, binding)
                       Log.d("actualVWeight", " data  -----${actualVWeight}")
               }
           })

           binding.breadth.addTextChangedListener(object : TextWatcher {
               override fun beforeTextChanged(
                   s: CharSequence?,
                   start: Int,
                   count: Int,
                   after: Int
               ) {
               }

               override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
               override fun afterTextChanged(s: Editable?) {
//                   onRowClick.onRowClick(singlePickupRefModel, "LENGTH_SELECT", adapterPosition)
//                   Log.d("test", "afterTextChanged:  length of  obj")
                   bookingList[adapterPosition].localBreath =
                       s.toString().toDoubleOrNull() ?: "0".toDouble()
                   calculateVWeight(adapterPosition, binding)

               }
           })
           binding.height.addTextChangedListener(object : TextWatcher {
               override fun beforeTextChanged(
                   s: CharSequence?,
                   start: Int,
                   count: Int,
                   after: Int
               ) {
               }

               override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
               override fun afterTextChanged(s: Editable?) {
//                   onRowClick.onRowClick(singlePickupRefModel, "LENGTH_SELECT", adapterPosition)
//                   Log.d("test", "afterTextChanged:  length of  obj")
                   bookingList[adapterPosition].localHeight =
                       s.toString().toDoubleOrNull() ?: "0".toDouble()
                   calculateVWeight(adapterPosition, binding)
               }
           })

           binding.weight.addTextChangedListener(object : TextWatcher {
               override fun beforeTextChanged(
                   s: CharSequence?,
                   start: Int,
                   count: Int,
                   after: Int
               ) {
               }

               override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
               override fun afterTextChanged(s: Editable?) {
//                   onRowClick.onRowClick(singlePickupRefModel, "LENGTH_SELECT", adapterPosition)
//                   Log.d("test", "afterTextChanged:  length of  obj")
                   bookingList[adapterPosition].weight =
                       s.toString().toDoubleOrNull() ?: "0".toDouble()
                   calculateTotalAWeight(adapterPosition, binding)
               }
           })

           binding.boxNo.addTextChangedListener(object : TextWatcher {
               override fun beforeTextChanged(
                   s: CharSequence?,
                   start: Int,
                   count: Int,
                   after: Int
               ) {
               }

               override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
               override fun afterTextChanged(s: Editable?) {
//                   activity.setBoxNum(binding.boxNo.text.toString())
                   setEnteredBoxNo(adapterPosition,binding)

               }
           })


            binding.refNo.addTextChangedListener(object : TextWatcher {
                       override fun beforeTextChanged(
                           s: CharSequence?,
                           start: Int,
                           count: Int,
                           after: Int
                       ) {
                       }

                       override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                       override fun afterTextChanged(s: Editable?) {
            //                   activity.setBoxNum(binding.boxNo.text.toString())
                           setEnteredReferenceNum(adapterPosition,binding)

                       }
                   })


        binding.dataloggerNum.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
//                   activity.setBoxNum(binding.boxNo.text.toString())
//                setEnteredBoxNo(adapterPosition,binding)
                bookingList[adapterPosition].dataloggerno = s.toString()
            }
        })






           binding.checkGelPack.setOnCheckedChangeListener { compoundButton, bool ->

               if(bool) {
                   singlePickupRefModel.gelpack="Y"
//
               } else {
                   singlePickupRefModel.gelpack="N"
               }
               binding.gelPackItem.isEnabled = (bool)
           }
//           binding.chekGelPack.setOnCheckedChangeListener { _, isChecked ->
//               if(isChecked){
//
//                   binding.gelPackItem.isEnabled=true
//               }else{
//                   binding.gelPackItem.isEnabled=false
//                   singlePickupRefModel.gelpack = if(binding.gelPackItem.isEnabled) "Y" else "N"
//               }
////               binding.gelPackItem.isEnabled = isChecked
////               singlePickupRefModel.gelpack = if(binding.gelPackItem.isEnabled) "Y" else "N"
//           }

//                 binding.inpu
       //                 tLayoutDatalogger.setOnClickListener {
//                     Toast.makeText(mContext, "test", Toast.
       //                     _SHORT).show()
//                     val dataLoggerAdapter =
//                         ArrayAdapter(mContext, R.layout.simple_list_item_1, dataLoggerItems)
//                     binding.selectDatalogger.adapter = dataLoggerAdapter
           binding.selectDatalogger.onItemSelectedListener = object:
               AdapterView.OnItemSelectedListener {
               override fun onItemSelected(
                   parent: AdapterView<*>?,
                   view: View?,
                   position: Int,
                   id: Long
               ) {
                   var dataLoggerSelection = dataLoggerItems.get(position)
                    when(dataLoggerSelection) {
//                        "SELECT", "NO" -> {
                        "NO" -> {
                            binding.dataloggerNum.isEnabled = false
                            binding.dataloggerNum.setText("")
                        }
                        "YES" -> {
                            binding.dataloggerNum.isEnabled = true
                            binding.dataloggerNum.setText(singlePickupRefModel.dataloggerno)
                        }
                        else -> {

                        }
                    }
               }

               override fun onNothingSelected(parent: AdapterView<*>?) {

               }

           }
            binding.length.setOnFocusChangeListener { view, selected ->
                if(selected && binding.length.text.toString() == "0.0" ||  binding.length.text.toString() == "0") {
                    binding.length.text = Editable.Factory.getInstance().newEditable("")
                }
            }
            binding.breadth.setOnFocusChangeListener { view, selected ->
                if(selected && binding.breadth.text.toString() == "0.0" ||  binding.breadth.text.toString() == "0") {
                    binding.breadth.text = Editable.Factory.getInstance().newEditable("")
                }
            }
            binding.height.setOnFocusChangeListener { view, selected ->
                if(selected && binding.height.text.toString() == "0.0" ||  binding.height.text.toString() == "0") {
                    binding.height.text = Editable.Factory.getInstance().newEditable("")
                }
            }

       }

       private fun trimLeadingZeros(string: String): String? {
           var string = string
           var startsWithZero = true
           while (startsWithZero) {
               if (string.startsWith("0") && string.length >= 2 && !string.substring(1, 2)
                       .equals(".", ignoreCase = true)
               ) {
                   string = string.substring(1)
               } else {
                   startsWithZero = false
               }
           }
           return string
       }



       fun bindData(singlePickupRefModel: SinglePickupRefModel, onRowClick: OnRowClick<Any>) {
//           if(singlePickupRefModel.gelpacktype)
           logDebug("DATALOGGER: ${singlePickupRefModel.dataloggerno}")
           if(singlePickupRefModel.gelpacktype == "0") {
               singlePickupRefModel.gelpacktype = "SELECT"
           }
           binding.gridData = singlePickupRefModel
           binding.index = adapterPosition
           if(singlePickupRefModel.packagetype.toString() == BookingActivity.JEENA_PACKING) {
               binding.boxLayout.visibility = View.VISIBLE
           } else {
               binding.boxLayout.visibility = View.GONE
           }
           setOnClicks(singlePickupRefModel)
           setUpAdapter()
           if(singlePickupRefModel.gelpack == "Y") {
               binding.checkGelPack.isChecked = true
           }
           if(singlePickupRefModel.datalogger == "Y") {
               binding.selectDatalogger.setSelection(0) // 0 Index is -> YES in the List
           } else {
               binding.selectDatalogger.setSelection(1)
           }
//           if(singlePickupRefModel.isBoxValidated) {
//               boxNoValidated(binding, singlePickupRefModel)
//           } else {
//               boxNotValidated(binding, singlePickupRefModel)
//           }
       }

       private fun setUpAdapter() {
           val dataLoggerAdapter = ArrayAdapter(mContext, R.layout.simple_list_item_1, dataLoggerItems)
           binding.selectDatalogger.adapter = dataLoggerAdapter
//           binding.selectDatalogger.setSelection(1)
       }



   }

//    fun setObservers() {
//        activity.mScanner.observe(activity) { sticker ->
//            activity.errorToast(sticker)
//        }
//    }

//    fun nextAvailable() {
//        bookingList.forEachIndexed { index, singlePickupRefModel ->
//            if(singlePickupRefModel.isBoxValidated == false && singlePickupRefModel.boxno.toString().isNotBlank()) {
//                return index
//            }
//        }
//    }


//     fun boxNoValidated( layoutBinding: BookingItemViewBinding, singlePickupRefModel: SinglePickupRefModel){
//         if(singlePickupRefModel.isBoxValidated){
//             layoutBinding.btnValidateBox.text = "Change Box"
////                 layoutBinding.boxNo.setBackgroundColor(validateColor)
////                 layoutBinding.btnValidateBox.setBackgroundColor(validateColor)
//             layoutBinding.boxNo.isFocusable=false
//         }
//     }

//     fun boxNotValidated(layoutBinding: BookingItemViewBinding, singlePickupRefModel: SinglePickupRefModel){
//        layoutBinding.boxNo.text.clear()
//        if(!singlePickupRefModel.isBoxValidated){
//         layoutBinding.btnValidateBox.text = "Validate Box"
////          layoutBinding.boxNo.setBackgroundColor(validateColor)
////          layoutBinding.btnValidateBox.setBackgroundResource(changeBoxColor)
//         layoutBinding.boxNo.isFocusable=true
//
//        }
//     }

    fun enterBoxOnNextAvailable(boxNo: String) {
        run forEachExit@{
            bookingList.forEachIndexed { _, singlePickupRefModel ->
//            Log.d("BOX_VALIDATED", singlePickupRefModel.isBoxValidated.toString())
                if (singlePickupRefModel.boxno.isNullOrBlank()) singlePickupRefModel.boxno = ""
                if (!singlePickupRefModel.isBoxValidated && singlePickupRefModel.boxno.toString()
                        .isBlank()
                ) {
                    Log.d("BOX_NO", singlePickupRefModel.boxno.toString())
                    singlePickupRefModel.boxno = boxNo
                    return@forEachExit
                }
            }
        }
        changeBoxNo()
    }

    private fun changeBoxNo() {
        notifyDataSetChanged()
    }

    fun setEnteredBoxNo(index: Int, layoutBinding: BookingItemViewBinding){
     bookingList[index].boxno=layoutBinding.boxNo.text.toString()
    }
    fun setEnteredWeight(index: Int, layoutBinding: BookingItemViewBinding){
     bookingList[index].weight=layoutBinding.weight.text.toString().toDouble()
    }
    fun setEnteredReferenceNum(index: Int, layoutBinding: BookingItemViewBinding){
     bookingList[index].referenceno=layoutBinding.refNo.text.toString()
    }

   fun setEnteredReferenceNo(){

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
            if(activity.productCode =="A"){
//                   actualVWeight= (bookingList[index].pckglength * bookingList[index].pckgbreath * bookingList[index].pckgheight).toFloat()/6000
//                actualVWeight= (layoutBinding.length.toString().toInt() * layoutBinding.breadth.toString().toInt() * layoutBinding.height.toString().toInt()).toFloat()/6000
                bookingList[index].localVWeight= ((layoutBinding.length.text.toString().toDouble() * layoutBinding.breadth.text.toString().toDouble() * layoutBinding.height.text.toString().toDouble()).toFloat()/6000).toInt()
            }else{
//                   actualVWeight= (bookingList[index].pckglength * bookingList[index].pckgbreath * bookingList[index].pckgheight).toFloat()/5000
//                   actualVWeight= (layoutBinding.length.text.toString().toDouble() * layoutBinding.breadth.text.toString().toDouble() * layoutBinding.height.text.toString().toDouble()).toFloat()/5000
                bookingList[index].localVWeight= ((layoutBinding.length.text.toString().toDouble() * layoutBinding.breadth.text.toString().toDouble() * layoutBinding.height.text.toString().toDouble()).toFloat()/5000).toInt()
            }

        }else if(bookingList[index].volfactor.isNaN()){
//            actualVWeight = 0f

        }else{
//            actualVWeight= ceil(bookingList[index].volfactor.toFloat() * bookingList[index].pcs.toDouble()).toFloat()
            bookingList[index].localVWeight= (ceil(bookingList[index].volfactor.toFloat() * bookingList[index].pcs.toDouble()).toFloat()).toInt()
        }
        calculateTotalVWeight()

    }

    fun serviceTypeChanged() {
        if(activity.productCode =="A"){
            bookingList.forEachIndexed { index, singlePickupRefModel ->
                singlePickupRefModel.localVWeight = ((singlePickupRefModel.localLength * singlePickupRefModel.localBreath * singlePickupRefModel.localHeight ).toFloat() / 6000 * singlePickupRefModel.pcs).toInt()
            }
        }else{
            bookingList.forEachIndexed { index, singlePickupRefModel ->
                singlePickupRefModel.localVWeight = ((singlePickupRefModel.localLength * singlePickupRefModel.localBreath * singlePickupRefModel.localHeight ).toFloat() / 5000 * singlePickupRefModel.pcs).toInt()
            }
        }
        calculateTotalVWeight()
    }
   fun calculateTotalVWeight(){
       actualVWeight=0f
      bookingList.forEachIndexed {index, element ->
          actualVWeight += element.localVWeight
      }
       activity.setVWeight(actualVWeight)
       calculateChargeableWeight()
   }
    fun calculateTotalAWeight(index: Int, layoutBinding: BookingItemViewBinding){
        var aWeight: Int? = layoutBinding.weight.text.toString().toIntOrNull()
        actualWeight= 0
        if(aWeight == null) {
//               activity.errorToast("Weight in wrong format.")
            return
        }
        if(aWeight > 0){

            bookingList.forEachIndexed {index, element ->
                actualWeight= (actualWeight+element.weight).toInt()
            }
        }
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

    fun getEnteredData(): ArrayList<SinglePickupRefModel>{
        return bookingList
    }

    fun setContent(contentModel: ContentSelectionModel, adapterPosition: Int) {
        Log.d("TEST_TEST", contentModel.itemname.toString())
        bookingList[adapterPosition].contents = contentModel.itemname
        bookingList[adapterPosition].contentsCode = contentModel.itemcode
        notifyItemChanged(adapterPosition)
//        notifyDataSetChanged()
    }
    fun setTemperature(tempModel: TemperatureSelectionModel, adapterPosition: Int) {
        bookingList[adapterPosition].tempurature = tempModel.name
        bookingList[adapterPosition].tempuratureCode = tempModel.value
        notifyItemChanged(adapterPosition)
//        notifyDataSetChanged()
    }
    fun setPacking(pckgsModel: PackingSelectionModel, adapterPosition: Int) {
        bookingList[adapterPosition].packing = pckgsModel.packingname
        bookingList[adapterPosition].packingcode = pckgsModel.packingcode
        notifyItemChanged(adapterPosition)
//        notifyDataSetChanged()
    }
    fun setGelPack(model: GelPackItemSelectionModel, adapterPosition: Int) {
        bookingList[adapterPosition].gelpacktype = model.itemname
        bookingList[adapterPosition].gelpackitemcode = model.itemcode
        notifyItemChanged(adapterPosition)
//        notifyDataSetCha
    //        nged()
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