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
import java.util.Locale

class SavePickupManifestAdapter(private val mContext: Context,
                                private val activity: SavePickupManifestActivity,
                                private val manifestList: ArrayList<GrSelectionModel>,
                                private val onRowClick: OnRowClick<Any>,

                                ) : RecyclerView.Adapter<SavePickupManifestAdapter.ManifestViewHolder>() , Filterable {
    private  var filterList: ArrayList<GrSelectionModel> = ArrayList()

    init {
        filterList=manifestList

    }
     inner class ManifestViewHolder(private val binding: SavePickupManifestItemBinding) :
        RecyclerView.ViewHolder(binding.root){

         fun setOnClick(model:GrSelectionModel){
             binding.content.setOnClickListener {
                 onRowClick.onRowClick(model, "CONTENT_SELECT", adapterPosition)

             }
             binding.packing.setOnClickListener {
                 onRowClick.onRowClick(model, "PACKING_SELECT", adapterPosition)
             }
             binding.pckgs.addTextChangedListener(object : TextWatcher {
                 override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                 override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                 override fun afterTextChanged(s: Editable?) {
                     pckgsChange(adapterPosition,binding)

                 }
             })
             binding.btnRemove.setOnClickListener {
                 onRowClick.onRowClick(model, "REMOVE_SELECT", adapterPosition)
//                 removeItem(model, adapterPosition)
             }

             binding.gWeight.addTextChangedListener { object :TextWatcher{
                 override fun beforeTextChanged(
                     s: CharSequence?,
                     start: Int,
                     count: Int,
                     after: Int
                 ) {}
                 override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                 override fun afterTextChanged(s: Editable?) {
                     activity.getGWeightValue(binding.gWeight.text.toString().toDouble())
                 }

             } }

         }
        fun bindData(model: GrSelectionModel, onRowClick: OnRowClick<Any>) {
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

   fun pckgsChange(index: Int, layoutBinding: SavePickupManifestItemBinding){
           val pckgs:Double?=layoutBinding.pckgs.text.toString().toDoubleOrNull()
           val balancePckgs:Double?=layoutBinding.balancePckgs.text.toString().toDoubleOrNull()
           if (pckgs != null && balancePckgs != null) {
           if(pckgs >= 0 && pckgs > balancePckgs){
               layoutBinding.pckgs.setText(balancePckgs.toString())
               activity.getPckgsValue(layoutBinding.pckgs.text.toString().toDouble())
               activity.getBalancepckg(layoutBinding.balancePckgs.text.toString().toDouble())
               Toast.makeText(mContext, "pckgs can not be greater then balance pckgs", Toast.LENGTH_SHORT).show()
           }else{
//                   Toast.makeText(mContext, "went something wrong", Toast.LENGTH_SHORT).show()
           }
           }
   }
    fun setContent(contentModel: ContentSelectionModel, adapterPosition: Int) {

        manifestList[adapterPosition].content = contentModel.itemname
        notifyItemChanged(adapterPosition)
    }
    fun setPacking(pckgsModel: PackingSelectionModel, adapterPosition: Int) {
        manifestList[adapterPosition].packing = pckgsModel.packingname
        notifyItemChanged(adapterPosition)
    }

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
                    val filteredList: ArrayList<GrSelectionModel> = ArrayList()
                    for (row in manifestList) {
                        if(
//                             row.custname.lowercase().contains(charString.lowercase(Locale.getDefault()))
                            row.grno.toString().contains(charString.lowercase(Locale.getDefault()))
                            ||row.grdt.contains(charString.lowercase(Locale.getDefault()))
                            ||row.custname.lowercase().contains(charString.lowercase(Locale.getDefault()))
                            ||row.destname.lowercase().contains(charString.lowercase(Locale.getDefault()))
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
                filterList = filterResults.values as ArrayList<GrSelectionModel>
                notifyDataSetChanged()
            }
        }
    }
}
