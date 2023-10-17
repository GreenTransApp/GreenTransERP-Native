package com.greensoft.greentranserpnative.ui.common.scanPopup

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.greensoft.greentranserpnative.databinding.FragmentScanPopupBinding


class ScanPopup(private val titleMessage: String)  : androidx.fragment.app.DialogFragment() {
    private lateinit var layoutBinding:FragmentScanPopupBinding
    override fun onCreateView(
         inflater: LayoutInflater,
         container: ViewGroup?,
         savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        layoutBinding = FragmentScanPopupBinding.inflate(layoutInflater)
        return layoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        layoutBinding = FragmentScanPopupBinding.inflate(LayoutInflater.from(context))
        layoutBinding.popUpTitle.text = titleMessage
        val builder = AlertDialog.Builder(requireActivity())
        isCancelable = false
        builder.setView(layoutBinding.root)
        layoutBinding.root.setOnClickListener {
            requireActivity().finish()
        }
        layoutBinding.closeBtn.setOnClickListener {
            dismiss()
        }
        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }
}