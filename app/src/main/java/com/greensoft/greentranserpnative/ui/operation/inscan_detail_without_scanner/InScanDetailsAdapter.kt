package com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.databinding.ItemInscanCardDetailBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.model.DamageReasonModel
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.model.InScanWithoutScannerModel
import javax.inject.Inject

class InScanDetailsAdapter @Inject constructor(
    private val inScanCardList: ArrayList<InScanWithoutScannerModel>,
    private val onRowClick: OnRowClick<Any>,
    private val mActivity: InScanDetailsActivity
): RecyclerView.Adapter<RecyclerView.ViewHolder> () {

    companion object {
        val TAG_SAVE_CARD_CLICK: String = "SAVE_CARD"
        val TAG_DAMAGE_REASON_CLICK: String = "DAMAGE_REASON_SELECT"
    }

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
        private fun  setOnClick(model:InScanWithoutScannerModel, onRowClickLocal: OnRowClick<Any>){
//            layoutBinding.inputDamagePckgsReason.setOnClickListener{
//               onRowClickLocal.onRowClick(model, TAG_DAMAGE_REASON_CLICK, adapterPosition)
//            }
            layoutBinding.btnSaveCard.setOnClickListener {
                var receivedPckgs: String? = layoutBinding.inputReceivePckgs.text.toString()
//                var damagePckgs: String? = layoutBinding.inputDamagePckgs.text.toString()
                var receivePckgsParsed: Int? = 0
//                var damagePckgsParsed: Int? = 0
                if(receivedPckgs != null || receivedPckgs != "") {
                    try {
                        receivePckgsParsed = receivedPckgs?.toIntOrNull()
                    } catch (ex: Exception) {
                        receivePckgsParsed = 0
                    }
                }

//                if(damagePckgs != null || damagePckgs != "") {
//                    try {
//                        damagePckgsParsed = damagePckgs?.toIntOrNull()
//                    } catch (ex: Exception) {
//                        damagePckgsParsed = 0
//                    }
//                }

                if(receivePckgsParsed != null && receivePckgsParsed!! < 0) {
                    mActivity.errorToast("Received Pckgs cannot be Empty.")
                    return@setOnClickListener
                }
//                if(damagePckgsParsed != null && damagePckgsParsed!! < 0) {
//                    mActivity.errorToast("Damage Pckgs cannot be Empty")
//                    return@setOnClickListener
//                }
//                if(receivePckgsParsed!! + damagePckgsParsed!! > model.despatchpckgs) {
//                    mActivity.errorToast("Received + Damage cannot be more than Despatch.")
//                    return@setOnClickListener
//                }
                model.receivedpckgs = receivePckgsParsed!!.toDouble()
//                model.damage = damagePckgsParsed!!.toDouble()
                onRowClickLocal.onRowClick(model, TAG_SAVE_CARD_CLICK, adapterPosition)
            }
        }

        fun onBind(inScanWithoutScannerModel: InScanWithoutScannerModel, onRowClickLocal: OnRowClick<Any>){
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
//            layoutBinding.inputDamagePckgs.setOnFocusChangeListener { view, focused ->
//                if(focused) {
//                    var damagePckgs = layoutBinding.inputDamagePckgs.text.toString().toIntOrNull()
//                    if(damagePckgs != null && damagePckgs == 0){
//                        layoutBinding.inputDamagePckgs.setText("")
//                    }
//                }
//            }
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

//            layoutBinding.inputDamagePckgs.addTextChangedListener(object: TextWatcher {
//                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                }
//
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                }
//
//                override fun afterTextChanged(s: Editable?) {
//                    val enteredVal = s.toString().toIntOrNull()
//                    if (enteredVal != null) {
//                        if (enteredVal < 0) {
//                            layoutBinding.inputDamagePckgs.setText("0")
//                        } else {
//                            inScanCardList[adapterPosition].damage = enteredVal.toDouble()
//                        }
//                    } else {
//                        inScanCardList[adapterPosition].damage = 0.0
//                    }
////                    if (enteredVal != null && enteredVal.toString().startsWith("0")) {
////                       binding.pckgs.setText(trimLeadingZeros(p0.toString()))
////                    }
//                }
//
//            })
        }

    }

//    fun getEnteredData(adapterPosition: Int): InScanWithoutScannerModel? {
//        if(adapterPosition >= inScanCardList.size) {
//            return null
//        }
//        return inScanCardList[adapterPosition]
//    }

    fun setDamageReason(model: DamageReasonModel, adapterPosition: Int) {
        inScanCardList[adapterPosition].damagereason = model.text
        inScanCardList[adapterPosition].damagereasoncode = model.value
        notifyItemChanged(adapterPosition)
    }

}