package com.greensoft.greentranserpnative.ui.operation.inscan_detail_with_scanner

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.ItemInScanWithScannerCardBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.model.InScanWithoutScannerModel
import javax.inject.Inject

class InScanWithScannerAdapter @Inject constructor(
    private val inScanWithScannerCardList: ArrayList<InScanWithoutScannerModel>,
    private val onRowClick: OnRowClick<Any>, ): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutBinding = ItemInScanWithScannerCardBinding.inflate(inflater,parent, false)
        return InScanWithScannerViewHolder(layoutBinding,parent.context)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dataViewHolder = holder as InScanWithScannerAdapter.InScanWithScannerViewHolder
        val dataModel:InScanWithoutScannerModel = inScanWithScannerCardList[position]
        dataViewHolder.onBind(dataModel, onRowClick)
    }

    override fun getItemCount(): Int {
        return inScanWithScannerCardList.size
    }

    inner class InScanWithScannerViewHolder(
        private val layoutBinding: ItemInScanWithScannerCardBinding,
        private val context: Context ):RecyclerView.ViewHolder(layoutBinding.root){
             fun  onBind(inScanWithoutScannerModel: InScanWithoutScannerModel, onRowClick: OnRowClick<Any>){
                layoutBinding.inScanCardDetailModel = inScanWithoutScannerModel
                layoutBinding.btnSaveCard.setOnClickListener {
                    onRowClick.onClick(inScanWithoutScannerModel,"SAVE_CARD")
                }
            }
    }
}