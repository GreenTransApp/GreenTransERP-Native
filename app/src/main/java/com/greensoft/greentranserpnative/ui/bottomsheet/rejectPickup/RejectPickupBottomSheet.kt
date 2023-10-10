package com.greensoft.greentranserpnative.ui.bottomsheet.rejectPickup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.common.PeriodSelection
import com.greensoft.greentranserpnative.databinding.BottomsheetAcceptPickupBinding
import com.greensoft.greentranserpnative.databinding.BottomsheetRejectPickupBinding
import com.greensoft.greentranserpnative.ui.operation.call_register.PickupAccRejViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RejectPickupBottomSheet @Inject constructor(): BottomSheetDialogFragment() {

    private lateinit var layoutBinding: BottomsheetRejectPickupBinding
    private var companyId: String = ""
    private var transactionId: String = ""
    private lateinit var mContext: Context
    private lateinit var viewModel: PickupAccRejViewModel
    companion object {
        var TAG = Companion::class.java.name
        fun newInstance(
            mContext: Context,
            companyId: String,
            transactionId: String,
            viewModel: PickupAccRejViewModel
        ): RejectPickupBottomSheet {
            val instance = RejectPickupBottomSheet()
            instance.mContext = mContext
            instance.companyId = companyId
            instance.transactionId = transactionId
            instance.viewModel = viewModel
            return instance
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutBinding = BottomsheetRejectPickupBinding.inflate(inflater)
        return layoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutBinding.transactionId = transactionId
        setOnClick()
//        setObserver()
    }

    private fun setOnClick() {
        layoutBinding.btnReject.setOnClickListener {
            viewModel.rejectPickup(companyId,transactionId,layoutBinding.inputRemark.text.toString())
        }
    }

//    private fun setObserver() {
//        viewModel.rejectPickupLiveData.observe(this) {
//            if(it != null) {
//
//                dismiss()
//            }
//        }
//    }

}