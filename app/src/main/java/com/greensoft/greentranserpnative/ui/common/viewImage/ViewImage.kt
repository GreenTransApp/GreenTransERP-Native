package com.greensoft.greentranserpnative.ui.common.viewImage

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.FragmentViewImageBinding
import com.greensoft.greentranserpnative.utils.Utils
import com.squareup.picasso.Picasso


class ViewImage()  : androidx.fragment.app.DialogFragment() {

    private lateinit var layoutBinding:FragmentViewImageBinding
    private var title: String = "View Image"
    private var parentContext: Context? = null
    private var parentActivity: BaseActivity? = null
//    private var imageBase64: String? = null
    private var imageBitmap: Bitmap? = null
//    private var imageUri: Uri? = null

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        super.onCreateView(inflater, container, savedInstanceState)
//        layoutBinding = FragmentViewImageBinding.inflate(layoutInflater)
//        return layoutBinding.root
//    }

    companion object {
        val TAG: String = "ViewImageFragment"
        fun newInstance(
            parentContext: Context,
            parentActivity: BaseActivity,
            title: String,
            bitmap: Bitmap,
//            base64: String,
//            uri: Uri
        ): ViewImage {
            val instance = ViewImage()
            instance.parentContext = parentContext
            instance.parentActivity = parentActivity
            instance.title = title
            instance.imageBitmap = bitmap
//            instance.imageBase64 = base64
//            instance.imageUri = uri
            return instance
        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(imageBitmap != null) {
//            val handler = Handler(Looper.getMainLooper())
//            handler.post(Runnable {
//                layoutBinding.viewImage.setImageBitmap(Utils.imageBitmap)
//            layoutBinding.viewImage.setImageDrawable(resources.getDrawable(R.drawable.baseline_add_a_photo_24, null))
//            })

//            layoutBinding.viewImage.setImageBitmap(this.imageBitmap)
        } else {
            parentActivity?.errorToast("Could not get the image. Please try again.")
        }
    }





    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        layoutBinding = FragmentViewImageBinding.inflate(layoutInflater)
//        Picasso.get().load(this.imageUri).into(layoutBinding.viewImage)
        layoutBinding.viewImage.setImageBitmap(imageBitmap)
        val builder = AlertDialog.Builder(requireActivity())
        isCancelable = true
        builder.setView(layoutBinding.root)
        layoutBinding.cancelViewImage.setOnClickListener {
            dismiss()
        }
        layoutBinding.root.setOnClickListener {
            requireActivity().finish()
        }
        layoutBinding.viewImage.setOnClickListener {

        }
        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }
}