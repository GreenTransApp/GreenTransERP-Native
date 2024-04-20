package com.greensoft.greentranserpnative.ui.operation.outstation_inscan_detail_with_scanner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.ItemInScanWithScannerCardBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.model.InScanDetailScannerModel
import javax.inject.Inject

class OutstationInscanDetailsWithScannerAdapter  @Inject constructor(
    private val cardList: ArrayList<InScanDetailScannerModel>,
    private val onRowClick: OnRowClick<Any>, ): RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    inner class InScanWithScannerViewHolder (
        private val layoutBinding: ItemInScanWithScannerCardBinding,
        private val context: Context
    ): RecyclerView.ViewHolder(layoutBinding.root){
        fun  onBind(inScanWithoutScannerModel: InScanDetailScannerModel, onRowClick: OnRowClick<Any>){
            layoutBinding.inScanCardDetailModel = inScanWithoutScannerModel

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutBinding = ItemInScanWithScannerCardBinding.inflate(inflater,parent, false)
        return InScanWithScannerViewHolder(layoutBinding,parent.context)
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dataViewHolder = holder as OutstationInscanDetailsWithScannerAdapter.InScanWithScannerViewHolder
        val dataModel:InScanDetailScannerModel = cardList[position]
        dataViewHolder.onBind(dataModel, onRowClick)
    }
}