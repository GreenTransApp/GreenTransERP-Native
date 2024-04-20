package com.greensoft.greentranserpnative.ui.operation.outstation_inscan_detail_without_scanner

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.ItemInscanCardDetailBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.InScanDetailsActivity
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.InScanDetailsAdapter
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.model.DamageReasonModel
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.model.InScanDetailScannerModel
import javax.inject.Inject

class OutstationInscanDetailWithoutScannerAdapter @Inject constructor(
    private val inScanCardList: ArrayList<InScanDetailScannerModel>,
    private val onRowClick: OnRowClick<Any>,
    private val mActivity: OutstationInscanDetailWithoutScannerActivity
): RecyclerView.Adapter<RecyclerView.ViewHolder> () {
    companion object {
        val TAG_SAVE_CARD_CLICK: String = "SAVE_CARD"
        val TAG_DAMAGE_REASON_CLICK: String = "DAMAGE_REASON_SELECT"
    }

    inner class InScanDetailViewHolder(
        private val  layoutBinding : ItemInscanCardDetailBinding,
        private val context: Context
    ): RecyclerView.ViewHolder(layoutBinding.root){



        private fun  setOnClick(model: InScanDetailScannerModel, onRowClickLocal: OnRowClick<Any>){

            layoutBinding.btnSaveCard.setOnClickListener {
                var receivedPckgs: String? = layoutBinding.inputReceivePckgs.text.toString()
                var receivePckgsParsed: Int? = 0
                if(receivedPckgs != null || receivedPckgs != "") {
                    try {
                        receivePckgsParsed = receivedPckgs?.toIntOrNull()
                    } catch (ex: Exception) {
                        receivePckgsParsed = 0
                    }
                }


                if(receivePckgsParsed != null && receivePckgsParsed!! < 0) {
                    mActivity.errorToast("Received Pckgs cannot be Empty.")
                    return@setOnClickListener
                }

                model.receivedpckgs = receivePckgsParsed!!.toDouble()

                onRowClickLocal.onRowClick(model,
                    OutstationInscanDetailWithoutScannerAdapter.TAG_SAVE_CARD_CLICK, adapterPosition)
            }
        }

        fun onBind(inScanWithoutScannerModel: InScanDetailScannerModel, onRowClickLocal: OnRowClick<Any>){
            layoutBinding.inScanCardDetailModel = inScanWithoutScannerModel
            setOnClick(inScanWithoutScannerModel, onRowClickLocal)
            setObservers()
        }

        private fun setObservers() {
            layoutBinding.inputReceivePckgs.setOnFocusChangeListener { view, focused ->
                if(focused) {
                    var receivePckgs = layoutBinding.inputReceivePckgs.text.toString().toIntOrNull()
                    if(receivePckgs != null && receivePckgs == 0){
                        layoutBinding.inputReceivePckgs.setText("")
                    }
                }
            }

            layoutBinding.inputReceivePckgs.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    val enteredVal = s.toString().toIntOrNull()
                    if (enteredVal != null) {
                        if (enteredVal < 0) {
                            layoutBinding.inputReceivePckgs.setText("0")
                        } else {
                            inScanCardList[adapterPosition].receivedpckgs = enteredVal.toDouble()
                        }
                    } else {
                        inScanCardList[adapterPosition].receivedpckgs = 0.0
                    }


                }

            })


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutBinding = ItemInscanCardDetailBinding.inflate(inflater, parent, false)
        return InScanDetailViewHolder(layoutBinding, parent.context)
    }

    override fun getItemCount(): Int {
        return inScanCardList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dataViewHolder = holder as OutstationInscanDetailWithoutScannerAdapter.InScanDetailViewHolder
        val dataModel:InScanDetailScannerModel = inScanCardList[position]
        dataViewHolder.onBind(dataModel, onRowClick)
    }

    fun setDamageReason(model: DamageReasonModel, adapterPosition: Int) {
        inScanCardList[adapterPosition].damagereason = model.text
        inScanCardList[adapterPosition].damagereasoncode = model.value
        notifyItemChanged(adapterPosition)
    }
}