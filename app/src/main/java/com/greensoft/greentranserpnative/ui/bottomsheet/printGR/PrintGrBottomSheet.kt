package com.greensoft.greentranserpnative.ui.bottomsheet.printGR

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.greensoft.greentranserpnative.base.BaseFragment
import com.greensoft.greentranserpnative.databinding.BottomsheetPrintGrBinding
import javax.inject.Inject

class PrintGrBottomSheet @Inject constructor(): BaseFragment() {
    private lateinit var layoutBinding:BottomsheetPrintGrBinding
    private var title: String = "Selection"

    companion object{
        const val TAG = "PrintGrBottomSheet"
        const val VEHICLE_CLICK_TYPE = "PRINT_GR_LOV"
        fun newInstance(
            mContext: Context,
            title: String,
        ): PrintGrBottomSheet {
            val instance: PrintGrBottomSheet = PrintGrBottomSheet()
            instance.mContext = mContext
            instance.title = title
            return instance

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutBinding = BottomsheetPrintGrBinding.inflate(layoutInflater)
        return layoutBinding.root
//        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutBinding.toolbarTitle.text = title
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
        close()
    }

    private fun close(){
        layoutBinding.closeBottomSheet.setOnClickListener {
            dismiss()
        }
    }
}