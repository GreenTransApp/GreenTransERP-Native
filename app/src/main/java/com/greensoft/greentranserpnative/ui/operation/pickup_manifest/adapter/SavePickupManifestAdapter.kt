package com.greensoft.greentranserpnative.ui.operation.pickup_manifest.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.SavePickupManifestItemBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.booking.models.ContentSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.PackingSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.PickupManifestEntryActivity
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.SavePickupManifestActivity
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.GrSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.LoadingListModel
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.models.SinglePickupRefModel
import java.util.Locale
import kotlin.math.roundToInt
import kotlin.properties.Delegates

class SavePickupManifestAdapter(private val mContext: Context,
                                private val activity: SavePickupManifestActivity,
                                private val manifestList: ArrayList<LoadingListModel>,
                                private val onRowClick: OnRowClick<Any>,

                                ) : RecyclerView.Adapter<SavePickupManifestAdapter.ManifestViewHolder>() , Filterable {
    private  var filterList: ArrayList<LoadingListModel> = ArrayList()

    init {
        filterList=manifestList

    }
     inner class ManifestViewHolder(private val binding: SavePickupManifestItemBinding) :
        RecyclerView.ViewHolder(binding.root){

         fun setOnClick(model:LoadingListModel){
//             binding.content.setOnClickListener {
//                 onRowClick.onRowClick(model, "CONTENT_SELECT", adapterPosition)
//
//             }
//             binding.packing.setOnClickListener {
//                 onRowClick.onRowClick(model, "PACKING_SELECT", adapterPosition)
//
//             }
//             binding.pckgs.addTextChangedListener(object : TextWatcher {
//                 override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//                 override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//                 override fun afterTextChanged(s: Editable?) {
//
//                     var pckgsValue=""
//                      if(binding.pckgs.text.isNotEmpty()){
//                          pckgsChange(adapterPosition,binding)
//                          pckgsValue=binding.pckgs.text.toString()
//                      }else{
//                          pckgsValue=binding.balancePckgs.text.toString()
//                      }
//                     activity.getPckgsValue(pckgsValue.toInt())
//
//
////                     if(binding.pckgs.text.){
////                       var pckgsValue=""
//////                         val pckgs:Int=binding.pckgs.text.toString().toInt()
////                         if(binding.pckgs.text.isNotEmpty()){
////                             pckgsValue=binding.pckgs.text.toString()
////                         }else{
////                             pckgsValue="0"
////                         }
////
////                         activity.getPckgsValue(pckgsValue.toInt())
////                     }
////                     val pckgs:Int=binding.pckgs.text.toString().toInt()
////                     activity.getPckgsValue(pckgs)
//
//                 }
//             })
//
//
//             binding.gWeight.addTextChangedListener ( object :TextWatcher{
//                 override fun beforeTextChanged(
//                     s: CharSequence?,
//                     start: Int,
//                     count: Int,
//                     after: Int) {}
//                 override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//                 override fun afterTextChanged(s: Editable?) {
//                         activity.getGWeightValue(binding.gWeight.text.toString().toDouble())
//
//
//
//                 }
//
//             } )
//
             binding.btnRemove.setOnClickListener {
                 onRowClick.onRowClick(model, "REMOVE_SELECT", adapterPosition)
//                 removeItem(model, adapterPosition)
             }
         }
        fun bindData(model: LoadingListModel, onRowClick: OnRowClick<Any>) {
//           model.pckgs = model.pckgs.toString().toDouble().roundToInt()
//           model.mfpckg = model.mfpckg.toString().toDouble().roundToInt()
            binding.grModel = model
            binding.index = adapterPosition
            setOnClick(model)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManifestViewHolder {
        var binding: SavePickupManifestItemBinding =
            SavePickupManifestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ManifestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ManifestViewHolder, position: Int) {
        val data = filterList[holder.adapterPosition]
        holder.bindData(data, onRowClick)


    }

    override fun getItemCount():Int = filterList.size

//   fun pckgsChange(index: Int, layoutBinding: SavePickupManifestItemBinding){
//           val pckgs:Int?=layoutBinding.pckgs.text.toString().toIntOrNull()
//           val balancePckgs:Int?=layoutBinding.balancePckgs.text.toString().toIntOrNull()
//           if (pckgs != null && balancePckgs != null) {
//           if(pckgs >= 0 && pckgs > balancePckgs){
////               layoutBinding.pckgs.text= layoutBinding.balancePckgs.text
//               if(balancePckgs.toString() != layoutBinding.pckgs.text.toString()) {
//                   layoutBinding.pckgs.setText(balancePckgs.toString())
//                   Toast.makeText(mContext, "pckgs can not be greater than balance pckgs", Toast.LENGTH_SHORT).show()
//               }
//               activity.getBalancepckg(layoutBinding.balancePckgs.text.toString().toInt())
//
//           }
//           else if( pckgs == 0){
//               layoutBinding.pckgs.setText(layoutBinding.balancePckgs.text.toString())
////                   Toast.makeText(mContext, "went something wrong", Toast.LENGTH_SHORT).show()
//           }
//           }
//   }

    fun getEnteredData(): ArrayList<LoadingListModel>{
        return manifestList
    }
//    fun setContent(contentModel: LoadingListModel, adapterPosition: Int) {
////        manifestList[adapterPosition].goods = contentModel.itemname
//        manifestList.forEachIndexed { index, grSelectionModel ->
//            if(grSelectionModel.loadingno == filterList[adapterPosition].loadingno) {
//                grSelectionModel.goods = contentModel.itemname
//                filterList[adapterPosition].goods = contentModel.itemname
//            }
//        }
//        notifyItemChanged(adapterPosition)
//    }

//    fun setPacking(pckgsModel: PackingSelectionModel, adapterPosition: Int) {
////        manifestList[adapterPosition].packing = pckgsModel.packingname
//
//        manifestList.forEachIndexed { index, grSelectionModel ->
//            if(grSelectionModel.grno == filterList[adapterPosition].grno) {
//                grSelectionModel.packing = pckgsModel.packingname
//                filterList[adapterPosition].packing = pckgsModel.packingname
//            }
//        }
//        notifyItemChanged(adapterPosition)
//    }

//    fun removeItem(model: GrSelectionModel, index: Int) {
//        manifestList.removeAt(index)
//        notifyDataSetChanged()
//    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filterList = manifestList
                } else {
                    val filteredList: ArrayList<LoadingListModel> = ArrayList()
                    for (row in manifestList) {
                        if(
//                             row.custname.lowercase().contains(charString.lowercase(Locale.getDefault()))
                            row.loadingno.toString().contains(charString.lowercase(Locale.getDefault()))
//                            ||row.grdt.contains(charString.lowercase(Locale.getDefault()))
//                            ||row.custname!!.lowercase().contains(charString.lowercase(Locale.getDefault()))
//                            ||row.destname.lowercase().contains(charString.lowercase(Locale.getDefault()))
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
                filterList = filterResults.values as ArrayList<LoadingListModel>
                notifyDataSetChanged()
            }
        }
    }
}
