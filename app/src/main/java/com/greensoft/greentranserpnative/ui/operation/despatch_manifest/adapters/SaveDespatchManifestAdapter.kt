package com.greensoft.greentranserpnative.ui.operation.despatch_manifest.adapters

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.SavePickupManifestItemBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.booking.models.ContentSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.PackingSelectionModel
import com.greensoft.greentranserpnative.ui.operation.despatch_manifest.SaveDespatchManifestActivity
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.SavePickupManifestActivity
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.adapter.SavePickupManifestAdapter
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.GrSelectionModel
import java.util.Locale
import kotlin.math.roundToInt

class SaveDespatchManifestAdapter (private val mContext: Context,
                                   private val activity: SaveDespatchManifestActivity,
                                   private val manifestList: ArrayList<GrSelectionModel>,
                                   private val onRowClick: OnRowClick<Any>,

                                   ) : RecyclerView.Adapter<SaveDespatchManifestAdapter.DespatchManifestViewHolder>() ,
    Filterable {
    private  var filterList: ArrayList<GrSelectionModel> = ArrayList()

    init {
        filterList=manifestList

    }
    inner class DespatchManifestViewHolder(private val binding: SavePickupManifestItemBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun setOnClick(model: GrSelectionModel){
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

                    var pckgsValue=""
                    if(binding.pckgs.text.isNotEmpty()){
                        pckgsChange(adapterPosition,binding)
                        pckgsValue=binding.pckgs.text.toString()
                    }else{
                        pckgsValue=binding.balancePckgs.text.toString()
                    }
                    activity.getPckgsValue(pckgsValue.toInt())



                }
            })
            binding.btnRemove.setOnClickListener {
                onRowClick.onRowClick(model, "REMOVE_SELECT", adapterPosition)
//                 removeItem(model, adapterPosition)
            }

            binding.gWeight.addTextChangedListener ( object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    activity.getGWeightValue(binding.gWeight.text.toString().toDouble())



                }

            } )

        }
        fun bindData(model: GrSelectionModel, onRowClick: OnRowClick<Any>) {
            model.pckgs = model.pckgs.toString().toDouble().roundToInt()
            model.mfpckg = model.mfpckg.toString().toDouble().roundToInt()
            binding.grModel = model
            binding.index = adapterPosition
            setOnClick(model)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DespatchManifestViewHolder {
        var binding: SavePickupManifestItemBinding =
            SavePickupManifestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DespatchManifestViewHolder(binding)
    }

    override fun getItemCount():Int = filterList.size

    override fun onBindViewHolder(holder: DespatchManifestViewHolder, position: Int) {
        val data = filterList[holder.adapterPosition]
        holder.bindData(data, onRowClick)

    }

    fun pckgsChange(index: Int, layoutBinding: SavePickupManifestItemBinding){
        val pckgs:Int?=layoutBinding.pckgs.text.toString().toIntOrNull()
        val balancePckgs:Int?=layoutBinding.balancePckgs.text.toString().toIntOrNull()
        if (pckgs != null && balancePckgs != null) {
            if(pckgs >= 0 && pckgs > balancePckgs){
//               layoutBinding.pckgs.text= layoutBinding.balancePckgs.text
                if(balancePckgs.toString() != layoutBinding.pckgs.text.toString()) {
                    layoutBinding.pckgs.setText(balancePckgs.toString())
                    Toast.makeText(mContext, "pckgs can not be greater than balance pckgs", Toast.LENGTH_SHORT).show()
                }
                activity.getBalancepckg(layoutBinding.balancePckgs.text.toString().toInt())

            }
            else if( pckgs == 0){
                layoutBinding.pckgs.setText(layoutBinding.balancePckgs.text.toString())
//                   Toast.makeText(mContext, "went something wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getEnteredData(): ArrayList<GrSelectionModel>{
        return manifestList
    }
    fun setContent(contentModel: ContentSelectionModel, adapterPosition: Int) {
//        manifestList[adapterPosition].goods = contentModel.itemname
        manifestList.forEachIndexed { index, grSelectionModel ->
            if(grSelectionModel.grno == filterList[adapterPosition].grno) {
                grSelectionModel.goods = contentModel.itemname
                filterList[adapterPosition].goods = contentModel.itemname
            }
        }
        notifyItemChanged(adapterPosition)
    }
    fun setPacking(pckgsModel: PackingSelectionModel, adapterPosition: Int) {
//        manifestList[adapterPosition].packing = pckgsModel.packingname

        manifestList.forEachIndexed { index, grSelectionModel ->
            if(grSelectionModel.grno == filterList[adapterPosition].grno) {
                grSelectionModel.packing = pckgsModel.packingname
                filterList[adapterPosition].packing = pckgsModel.packingname
            }
        }
        notifyItemChanged(adapterPosition)
    }


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
                            ||row.custname!!.lowercase().contains(charString.lowercase(Locale.getDefault()))
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