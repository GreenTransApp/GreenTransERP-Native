package com.greensoft.greentranserpnative.ui.bottomsheet.pinCodeSelection

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.ItemPinCodeSelectionBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.pinCodeSelection.model.PinCodeModel
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import java.util.Locale
import javax.inject.Inject

class PinCodeSelectionAdapter @Inject constructor(
    private val pinCodeList:ArrayList<PinCodeModel>,
    private val onRowClick: OnRowClick<Any>? = null ):RecyclerView.Adapter<RecyclerView.ViewHolder>(),Filterable {

    companion object {
        val PIN_CODE_SELECTION_ROW_CLICK: String = "PIN_CODE_LOV_ROW_CLICK"

    }
    private var filterList:ArrayList<PinCodeModel> = ArrayList()

    init {
        filterList = pinCodeList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutBinding=ItemPinCodeSelectionBinding.inflate(inflater,parent,false)
        return PinCodeSelectionViewHolder(layoutBinding,parent.context)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dataHolder = holder as PinCodeSelectionAdapter.PinCodeSelectionViewHolder
        val dataModel:PinCodeModel = filterList[position]
        dataHolder.onBind(dataModel,onRowClick)
    }



    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filterList = pinCodeList
                } else {
                    val filteredList: ArrayList<PinCodeModel> = ArrayList()
                    for (row in pinCodeList) {
                        if (row.text.lowercase().contains(charString.lowercase(Locale.getDefault()))) {
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
                filterList = filterResults.values as ArrayList<PinCodeModel>
                notifyDataSetChanged()
            }
        }
    }


    inner class PinCodeSelectionViewHolder(
        val layoutBinding: ItemPinCodeSelectionBinding,
        val mContext:Context ):RecyclerView.ViewHolder(layoutBinding.root){

            fun onBind(pinCodeModel:PinCodeModel,onRowClick: OnRowClick<Any>?){
                layoutBinding.pinCodeData = pinCodeModel

                layoutBinding.layout.setOnClickListener {
                    onRowClick?.onClick(pinCodeModel,PIN_CODE_SELECTION_ROW_CLICK)
                }
            }

    }
}