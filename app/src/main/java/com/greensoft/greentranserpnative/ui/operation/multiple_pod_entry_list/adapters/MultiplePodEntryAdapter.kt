package com.greensoft.greentranserpnative.ui.operation.multiple_pod_entry_list.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.ENV
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.common.PeriodSelection
import com.greensoft.greentranserpnative.databinding.PodListItemBinding
import com.greensoft.greentranserpnative.model.ImageUtil
import com.greensoft.greentranserpnative.ui.bottomsheet.signBottomSheet.BottomSheetSignature
import com.greensoft.greentranserpnative.ui.bottomsheet.signBottomSheet.SignatureBottomSheetCompleteListener
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.booking.models.ContentSelectionModel
import com.greensoft.greentranserpnative.ui.operation.booking.models.GelPackItemSelectionModel
import com.greensoft.greentranserpnative.ui.operation.multiple_pod_entry_list.MultiplePodEntryListActivity
import com.greensoft.greentranserpnative.ui.operation.multiple_pod_entry_list.models.RelationListModel
import com.greensoft.greentranserpnative.ui.operation.pending_for_delivery_update_list.models.PodEntryListModel
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.models.SinglePickupRefModel
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject

class MultiplePodEntryAdapter  @Inject constructor(
    private val podList:ArrayList<PodEntryListModel>,
    private val onRowClick: OnRowClick<Any>,
    private val mContext: Context,
    private  val  activity:MultiplePodEntryListActivity
):RecyclerView.Adapter<MultiplePodEntryAdapter.MultiplePodEntryViewHolder>(){

    private var setImageIndex: Int = -1
    companion object {
        fun convertImageUriToBase64(contentResolver: ContentResolver, imageUri: Uri): String? {
            return try {
                val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
                val buffer = ByteArrayOutputStream()
                val bufferSize = 1024
                val data = ByteArray(bufferSize)
                var bytesRead= 0
                while (inputStream?.read(data, 0, bufferSize).also {
                        if (it != null) {
                            bytesRead = it
                        }
                    } != -1) {
                    buffer.write(data, 0, bytesRead)
                }
                val byteArray: ByteArray = buffer.toByteArray()
                Base64.encodeToString(byteArray, Base64.DEFAULT)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun getBitmapFromBase64(base64: String): Bitmap? {
//        val base64Image = Cropper.convertImageUriToBase64(contentResolver, uri)
//        Toast.makeText(this, "bse", Toast.LENGTH_SHORT).show()
            // Decode the Base64 string into a byte array
            try {
                val decodedBytes: ByteArray = Base64.decode(base64, Base64.DEFAULT)
                return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            } catch (ex: Exception) {
                return null
            }
        }
    }

    inner class MultiplePodEntryViewHolder(private val layoutBinding: PodListItemBinding) :
        RecyclerView.ViewHolder(layoutBinding.root), SignatureBottomSheetCompleteListener {
        override fun onSignCompleteWithAdapter(clickType: String, imageBitmap: Bitmap, index: Int) {
            super.onSignCompleteWithAdapter(clickType, imageBitmap, index)
            podList[index].signImg = imageBitmap
            podList[index].signImgBase64 = ImageUtil.convert(imageBitmap)
            layoutBinding.signImg.setImageBitmap(imageBitmap)
        }

         fun setOnclick(model:PodEntryListModel){
             layoutBinding.inputRelation.setOnClickListener {
                 onRowClick.onRowClick(model, "RELATION_SELECT", adapterPosition)

             }
//             layoutBinding.inputDate.setOnClickListener {
//                 onRowClick.onRowClick(model, "DATE_SELECT", adapterPosition)
//
//             }
//             layoutBinding.inputTime.setOnClickListener {  }

//             layoutBinding.input.setOnClickListener {
//                 onRowClick.onRowClick(model, "TIME_SELECT", adapterPosition)
//
//             }
             layoutBinding.btnSavePod.setOnClickListener {
                 val currentModel = podList[adapterPosition]
                 onRowClick.onRowClick(currentModel, "SAVE_SELECT", adapterPosition)
             }

             layoutBinding.signatureLayout.setOnClickListener {
//                 onRowClick.onRowClick(model, "SIGNATURE_SELECT", adapterPosition)
                 val currentModel = podList[adapterPosition]
                 val bottomSheet= BottomSheetSignature.newInstance(mContext, activity.getCompanyId(), this, currentModel.signImg, adapterPosition)
                 bottomSheet.show(activity.supportFragmentManager, BottomSheetSignature.TAG)
             }
             layoutBinding.imageLayout.setOnClickListener {
                 onRowClick.onImageClickedMultiplePodEntry(model, "POD_IMAGE_SELECT", adapterPosition, layoutBinding)

//                 activity.showImageDialog()
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
//            layoutBinding.model = model
            layoutBinding.index = adapterPosition
//            model.dlvdt = activity.currentDt.toString()
//            layoutBinding.input

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
//        holder.setIsRecyclable(false)
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
        podList[adapterPosition].dlvdt = model.viewsingleDate
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
        podList[adapterPosition].dlvtime = time
        notifyItemChanged(adapterPosition)
    }

    fun getEnteredData(index: Int): PodEntryListModel{
        return podList[index]
    }


//    fun setSignatureImage(bitmap: Bitmap, adapterPosition: Int) {
//        podList[adapterPosition].signImg = bitmap
//        podList[adapterPosition].signImgBase64 = ImageUtil.convert(bitmap)
//        notifyItemChanged(adapterPosition)
//    }

    fun setPodImage(bitmap: Bitmap, adapterPosition: Int, layoutBinding: PodListItemBinding) {
        podList[adapterPosition].podImg = bitmap
        podList[adapterPosition].podImgBase64 = ImageUtil.convert(bitmap)
        layoutBinding.podImage.setImageBitmap(bitmap)
//        notifyItemChanged(adapterPosition)
    }
}