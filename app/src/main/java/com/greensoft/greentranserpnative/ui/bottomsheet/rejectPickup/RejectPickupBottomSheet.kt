package com.greensoft.greentranserpnative.ui.bottomsheet.rejectPickup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.common.PeriodSelection
import com.greensoft.greentranserpnative.databinding.BottomsheetAcceptPickupBinding
import com.greensoft.greentranserpnative.databinding.BottomsheetRejectPickupBinding

class RejectPickupBottomSheet : BottomSheetDialogFragment() {

    private lateinit var layoutBinding: BottomsheetRejectPickupBinding
    private var transactionId: String = ""
    private lateinit var mContext: Context
    var singleDatePicker: MaterialDatePicker<*>? = null
    companion object {
        var mPeriod: MutableLiveData<PeriodSelection> = MutableLiveData()
        //        var timePeriod: MutableLiveData<TimeSelection> = MutableLiveData()
        var timePeriod: MutableLiveData<String> = MutableLiveData()
        //    var timePeriod: MutableLiveData<TimeSelection?> = MutableLiveData<Any?>()
        var TAG = Companion::class.java.name
        fun newInstance(
            mContext: Context,
            transactionId: String
        ): RejectPickupBottomSheet {
            val instance = RejectPickupBottomSheet()
            instance.mContext = mContext
            instance.transactionId = transactionId
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
//        setOnClick()
//        setObserver()
    }

}