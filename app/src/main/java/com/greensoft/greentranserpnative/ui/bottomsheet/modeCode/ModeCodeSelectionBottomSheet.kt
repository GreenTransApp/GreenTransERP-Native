package com.greensoft.greentranserpnative.ui.bottomsheet.modeCode

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseFragment
import com.greensoft.greentranserpnative.databinding.ModeCodeSelectionBottomSheetBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.vendorSelection.VendorSelectionBottomSheet
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ModeCodeSelectionBottomSheet @Inject constructor(): BaseFragment(), OnRowClick<Any> {
    private lateinit var layoutBinding:ModeCodeSelectionBottomSheetBinding
    companion object{
        const val TAG = "ModeCodeBottomSheet"
        const val CLICK_TYPE = "MODE_CODE_SELECTION"


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.mode_code_selection_bottom_sheet, container, false)
    }



    override fun onClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }
}