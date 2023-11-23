package com.greensoft.greentranserpnative.ui.bottomsheet.attachedImages

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.ui.login.LoginActivity
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.models.SinglePickupRefModel

class AttachedImagesBottomSheet(
    private val mContext: Context,
    private val imagesBase64: ArrayList<String>,
    private val imagesBitmap: ArrayList<Bitmap>,
    private val imagesUri: ArrayList<Uri>,
    val listener: OnAddImageClickListener
):  RecyclerView.Adapter<AttachedImagesBottomSheet.AttachedImageBottomSheetViewHolder>(){
    private val TAG="PalletWiseRemarksAdapterBottomSheet"
    private val CAMERA_REQUEST = 1888
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachedImageBottomSheetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_images_rv, parent, false)
        val viewHolder = AttachedImageBottomSheetViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int = imagesBase64.size


    override fun onBindViewHolder(holder: AttachedImageBottomSheetViewHolder, position: Int) {
        if(imagesBase64[holder.adapterPosition] != "EMPTY") {
            holder.imageView.setImageBitmap(imagesBitmap[holder.adapterPosition])
        }
        holder.imageView.setOnClickListener{
            listener.addImage(clickType = "VIEW_IMAGE",
                imagesBitmap[holder.adapterPosition])
//                imagesBase64[holder.adapterPosition],
//                imagesUri[holder.adapterPosition])
        }

        holder.imageView.setOnLongClickListener{
//            removeItem(holder.adapterPosition)
//            openDeleteAlert(holder.adapterPosition)
            deleteAlert(holder.adapterPosition)
            return@setOnLongClickListener true
        }
    }


    private fun deleteAlert(index: Int){
        androidx.appcompat.app.AlertDialog.Builder(mContext)
            .setTitle("Alert!!!")
            .setMessage("Are you sure you want to delete  image?")
            .setPositiveButton("Yes") { _,_ ->
                imagesBase64.removeAt(index)
                imagesUri.removeAt(index)
                notifyDataSetChanged()
                Toast.makeText(mContext, "Image Delete Successfully.", Toast.LENGTH_SHORT).show()

            }
            .setNegativeButton("No") { _,_ ->}
            .show()
    }

//    private fun openDeleteAlert(index:Int){
//        val builder = AlertDialog.Builder(mContext)
//        builder.setMessage("Do you want to delete ?")
//        builder.setTitle("Alert !")
//        builder.setCancelable(false)
//        builder.setPositiveButton("Yes") {
//                dialog, which ->
//            removeItem(index)
//
//        }
//        builder.setNegativeButton("No") {
//                dialog, which ->
//            dialog.cancel()
//        }
//
//        val alertDialog = builder.create()
//        alertDialog.show()
//    }
    fun removeItem(index: Int) {
        imagesBase64.removeAt(index)
        imagesUri.removeAt(index)
        notifyDataSetChanged()
        if(imagesBase64.size==0){
            Toast.makeText(mContext, "Image Delete Successfully.", Toast.LENGTH_SHORT).show()
        }

        Log.d(TAG, "removeItem: ${imagesBase64.size.toString()}")
        Log.d(TAG, "removeItem: ${imagesUri.size.toString()}")
    }
    class AttachedImageBottomSheetViewHolder(val itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.img)
    }

}