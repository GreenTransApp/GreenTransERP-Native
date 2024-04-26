package com.greensoft.greentranserpnative.ui.bottomsheet.signBottomSheet

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import com.github.gcacace.signaturepad.views.SignaturePad
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.greensoft.greentranserpnative.databinding.BottomSheetSignatureBinding

class BottomSheetSignature: BottomSheetDialogFragment() {
    lateinit var layoutBinding: BottomSheetSignatureBinding
    var mContext: Context? = null
    var companyId: String? = null
    var signPad: SignaturePad? = null
    var signBitmap: Bitmap? = null
    var completeListener: SignatureBottomSheetCompleteListener? = null
    var adapterPosition = -1

    companion object {
        var TAG = Companion::class.java.name
        var COMPLETED_CLICK_LISTENER_TAG = "SIGN_COMPLETED"
        fun newInstance(
            mContext: Context,
            companyId: String,
            completeListener: SignatureBottomSheetCompleteListener,
            signBitmap: Bitmap?,
            index: Int = -1
        ): BottomSheetSignature {
            val instance = BottomSheetSignature()
            instance.mContext = mContext
            instance.companyId = companyId
            instance.completeListener = completeListener
            instance.signBitmap = signBitmap
            instance.adapterPosition = index
            return instance
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutBinding = BottomSheetSignatureBinding.inflate(inflater)
        if(signBitmap != null) {
            layoutBinding.signaturePad.signatureBitmap = signBitmap
        }
        signPad = layoutBinding.signaturePad
        return layoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClicks()
        setObserver()
        dialog!!.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet: FrameLayout =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
//                Utils.Log("COMMON_BOTTOM_SHEET", "Opened");
            BottomSheetBehavior.from(bottomSheet).state =
                BottomSheetBehavior.STATE_EXPANDED
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO;
            layoutBinding.extraSpace.minimumHeight =
                Resources.getSystem().displayMetrics.heightPixels / 2

            behavior.isDraggable = false
        }
    }

    private fun setOnClicks() {
        layoutBinding.closeBottomSheet.setOnClickListener {
            closeBottomSheet()
        }
        if(signPad != null) {
            layoutBinding.btnClear.setOnClickListener {
                signPad?.clear()
            }

            layoutBinding.btnComplete.setOnClickListener {
                if(signPad != null) {
                    if(signPad!!.transparentSignatureBitmap != null) {
//                        returnImageBitmap(signPad?.signatureBitmap!!)
                        returnImageBitmap(signPad?.transparentSignatureBitmap!!)
                    } else {
                        toast("Something went wrong, Please try again.")
//                        Toast.makeText(context, "Something went wrong, Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun closeBottomSheet() {
        if(dialog!=null) {
            dialog!!.dismiss()
        } else {
            toast("Something went wrong, Please try again.")
        }
    }


    fun returnImageBitmap(bitmap: Bitmap) {
        if(completeListener != null) {
            completeListener!!.onComplete(COMPLETED_CLICK_LISTENER_TAG, bitmap)
            completeListener!!.onCompleteWithAdapter(COMPLETED_CLICK_LISTENER_TAG, bitmap, adapterPosition)
            closeBottomSheet()
        }
    }

    private fun setObserver() {
        if(signPad != null) {
            signPad?.setOnSignedListener(object: SignaturePad.OnSignedListener {
                override fun onStartSigning() {
                    Log.d("Signature Bottom Sheet Fragment", "onStartSigning()")
                }

                override fun onSigned() {
                    Log.d("Signature Bottom Sheet Fragment", "onSigned()")
                }

                override fun onClear() {
                    Log.d("Signature Bottom Sheet Fragment", "onClear()")
                }

            })
        }
    }

    fun toast(msg: String?) {
        Toast.makeText(context, msg ?: "Something went wrong, Please try again.", Toast.LENGTH_SHORT).show()
    }

}