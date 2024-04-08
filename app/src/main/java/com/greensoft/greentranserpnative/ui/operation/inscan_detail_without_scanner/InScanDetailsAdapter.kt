package com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.ItemInscanCardDetailBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.booking.models.GelPackItemSelectionModel
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.model.DamageReasonModel
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.model.InScanWithoutScannerModel
import javax.inject.Inject

class InScanDetailsAdapter @Inject constructor(
    private val inScanCardList: ArrayList<InScanWithoutScannerModel>,
    private val onRowClick: OnRowClick<Any>,
): RecyclerView.Adapter<RecyclerView.ViewHolder> () {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutBinding = ItemInscanCardDetailBinding.inflate(inflater, parent, false)
        return InScanDetailViewHolder(layoutBinding, parent.context)
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dataViewHolder = holder as InScanDetailsAdapter.InScanDetailViewHolder
        val dataModel:InScanWithoutScannerModel = inScanCardList[position]
        dataViewHolder.onBind(dataModel, onRowClick)
    }

    override fun getItemCount(): Int {
        return inScanCardList.size
    }

    inner class InScanDetailViewHolder(
        private val  layoutBinding :ItemInscanCardDetailBinding,
        private val context: Context):RecyclerView.ViewHolder(layoutBinding.root){
            private fun  setOnClick(model:InScanWithoutScannerModel){
               layoutBinding.inputDamagePckgsReason.setOnClickListener{
                   onRowClick.onRowClick(model,"DAMAGE_REASON_SELECT", adapterPosition)
               }
                layoutBinding.btnSaveCard.setOnClickListener {
                    onRowClick.onRowClick(model,"SAVE_CARD", adapterPosition)
                }
             }

            fun onBind(inScanWithoutScannerModel: InScanWithoutScannerModel, onRowClick: OnRowClick<Any>){
                layoutBinding.inScanCardDetailModel = inScanWithoutScannerModel
                setOnClick(inScanWithoutScannerModel)
                layoutBinding.btnSaveCard.setOnClickListener {
                    onRowClick.onClick(inScanWithoutScannerModel,"SAVE_CARD")

                }
            }

    }

    fun setDamageReason(model: DamageReasonModel, adapterPosition: Int) {
        inScanCardList[adapterPosition].damagereason = model.text
        inScanCardList[adapterPosition].damagereasoncode = model.value
        notifyItemChanged(adapterPosition)

    }

}