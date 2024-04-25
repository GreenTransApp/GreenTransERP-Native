package com.greensoft.greentranserpnative.ui.operation.multiple_pod_entry_list.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.common.PeriodSelection
import com.greensoft.greentranserpnative.databinding.PodListItemBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.signBottomSheet.BottomSheetSignature
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.booking.models.ContentSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.GelPackItemSelectionModel
import com.greensoft.greentranserpnative.ui.operation.multiple_pod_entry_list.MultiplePodEntryListActivity
import com.greensoft.greentranserpnative.ui.operation.multiple_pod_entry_list.models.RelationListModel
import com.greensoft.greentranserpnative.ui.operation.pending_for_delivery_update_list.models.PodEntryListModel
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.models.SinglePickupRefModel
import javax.inject.Inject

class MultiplePodEntryAdapter  @Inject constructor(
    private val podList:ArrayList<PodEntryListModel>,
    private val onRowClick: OnRowClick<Any>,
    private val mContext: Context,
    private  val  activity:MultiplePodEntryListActivity
):RecyclerView.Adapter<MultiplePodEntryAdapter.MultiplePodEntryViewHolder>(){

    inner class MultiplePodEntryViewHolder(private val layoutBinding: PodListItemBinding) :
        RecyclerView.ViewHolder(layoutBinding.root) {


         fun setOnclick(model:PodEntryListModel){
             layoutBinding.inputRelation.setOnClickListener {
                 onRowClick.onRowClick(model, "RELATION_SELECT", adapterPosition)

             }
             layoutBinding.inputDate.setOnClickListener {
                 onRowClick.onRowClick(model, "DATE_SELECT", adapterPosition)

             }

             layoutBinding.inputTime.setOnClickListener {
                 onRowClick.onRowClick(model, "TIME_SELECT", adapterPosition)

             }
             layoutBinding.btnSavePod.setOnClickListener {
                 onRowClick.onRowClick(model, "SAVE_SELECT", adapterPosition)

             }

             layoutBinding.signatureLayout.setOnClickListener {
                 onRowClick.onRowClick(model, "SIGNATURE_SELECT", adapterPosition)
             }
             layoutBinding.imageLayout.setOnClickListener {
                 onRowClick.onRowClick(model, "POD_IMAGE_SELECT", adapterPosition)
             }


             layoutBinding.signCheck.setOnCheckedChangeListener { _, Bool ->
                 if (layoutBinding.signCheck.isChecked) {
                     layoutBinding.signatureLayout.visibility = View.VISIBLE
                     layoutBinding.mainImgLayout.visibility= View.VISIBLE
                     podList[adapterPosition].signRequired = "Y"


                 }else{
                     layoutBinding.mainImgLayout.visibility= View.GONE
//                     layoutBinding.signatureLayout.visibility = View.GONE
                     layoutBinding.signImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.image))
                     podList[adapterPosition].signRequired = "N"
                     podList[adapterPosition].signImg = null
                 }
             }
             layoutBinding.imageCheck.setOnCheckedChangeListener { buttonView, isChecked ->
                 if (layoutBinding.imageCheck.isChecked) {
                     layoutBinding.imageLayout.visibility = View.VISIBLE
                     layoutBinding.mainImgLayout.visibility= View.VISIBLE
                     podList[adapterPosition].stampRequired = "Y"

                 } else {
                     layoutBinding.imageLayout.visibility = View.GONE
//                     layoutBinding.mainImgLayout.visibility = View.GONE
                     podList[adapterPosition].stampRequired = "N"
                     layoutBinding.podImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.image))


                 }

             }

             layoutBinding.inputMobile.addTextChangedListener ( object : TextWatcher {
                 override fun beforeTextChanged(
                     s: CharSequence?,
                     start: Int,
                     count: Int,
                     after: Int) {}
                 override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                 override fun afterTextChanged(s: Editable?) {
                         activity.getEnteredMobileNo(layoutBinding.inputMobile.text.toString())



                 }

             } )
             layoutBinding.inputReceiverBy.addTextChangedListener ( object : TextWatcher {
                 override fun beforeTextChanged(
                     s: CharSequence?,
                     start: Int,
                     count: Int,
                     after: Int) {}
                 override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                 override fun afterTextChanged(s: Editable?) {
                         activity.getEnterReceivedBy(layoutBinding.inputReceiverBy.text.toString())

                 }

             } )

         }
        fun onBind(model: PodEntryListModel, onRowClick: OnRowClick<Any>) {
            layoutBinding.model = model
            layoutBinding.index = adapterPosition
            setOnclick(model)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiplePodEntryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutBinding = PodListItemBinding.inflate(inflater, parent, false)
        return MultiplePodEntryViewHolder(layoutBinding)

    }

    override fun getItemCount(): Int {
        return podList.size
    }

    override fun onBindViewHolder(holder: MultiplePodEntryViewHolder, position: Int) {
        holder.onBind(podList[position], onRowClick)
    }

    fun setRelation(model: RelationListModel, adapterPosition: Int) {

        podList.forEachIndexed { index,podModel  ->
            if(podModel.grno == podList[adapterPosition].grno) {
                podModel.relation = model.relations
                podList[adapterPosition].relation = model.relations
            }
        }
        notifyItemChanged(adapterPosition)
    }
//    fun setSelectedDt(model: PodEntryListModel, adapterPosition: Int) {
    fun setSelectedDt(model: PeriodSelection, adapterPosition: Int) {
        podList[adapterPosition].poddt = model.viewsingleDate
        podList[adapterPosition].sqlpoddt = model.sqlsingleDate
//        podList.forEachIndexed { index,podModel  ->
//            if(podModel.grno == podList[adapterPosition].grno) {
//                podModel.poddt = model.poddt
//
//            }
//        }
        notifyItemChanged(adapterPosition)
    }

    fun setSelectedTime(time: String, adapterPosition: Int) {
        podList[adapterPosition].podtime = time
        notifyItemChanged(adapterPosition)
    }

    fun getEnteredData(): ArrayList<PodEntryListModel>{
        return podList
    }


    fun setSignatureImage(bitmap: Bitmap, adapterPosition: Int) {
        podList[adapterPosition].signImg = bitmap
        notifyItemChanged(adapterPosition)
    }

    fun setPodImage(bitmap: Bitmap, adapterPosition: Int) {
        podList[adapterPosition].podImg = bitmap
        notifyItemChanged(adapterPosition)
    }
}