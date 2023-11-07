//package com.greensoft.greentranserpnative.ui.bottomsheet.attachedImages
//
//import android.content.Context
//import android.graphics.Bitmap
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import androidx.recyclerview.widget.RecyclerView
//import com.greensoft.greentranserpnative.R
//
//class AttachedImageBottomSheet(
//    private val mContext: Context,
//    private val imagesBase64: ArrayList<String>,
//    private val imagesBitmap: ArrayList<Bitmap>,
//    val listener: OnAddImageClickListener
//):  RecyclerView.Adapter<AttachedImagesBottomSheet.AttachedImageBottomSheetViewHolder>(){
//    private val TAG="PalletWiseRemarksAdapterBottomSheet"
//    private val CAMERA_REQUEST = 1888
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachedImageBottomSheetViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_images_rv, parent, false)
//        val viewHolder = AttachedImageBottomSheetViewHolder(view)
//        return viewHolder
//    }
//
//    override fun getItemCount(): Int = imagesBase64.size
//
//
//    override fun onBindViewHolder(holder: AttachedImageBottomSheetViewHolder, position: Int) {
//        if(imagesBase64[holder.adapterPosition] != "EMPTY") {
//            holder.imageView.setImageBitmap(imagesBitmap[holder.adapterPosition])
//        }
//        holder.imageView.setOnClickListener{
//            listener.addImage(clickType = "Open Dialog" )
//        }
//    }
//
//    class AttachedImageBottomSheetViewHolder(val itemView: View): RecyclerView.ViewHolder(itemView) {
//        val imageView = itemView.findViewById<ImageView>(R.id.img)
//    }
//
//}