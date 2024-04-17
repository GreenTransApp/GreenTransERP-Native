package com.greensoft.greentranserpnative.ui.bottomsheet.receivingDetails

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.greensoft.greentranserpnative.ENV
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.common.PeriodSelection
import com.greensoft.greentranserpnative.databinding.BottomsheetAcceptPickupBinding
import com.greensoft.greentranserpnative.databinding.BottomsheetReceivingDetailsBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.acceptPickup.AcceptPickupBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.receivingDetails.models.ReceivingDetailsEnteredDataModel
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.operation.call_register.PickupAccRejViewModel
import com.greensoft.greentranserpnative.utils.Utils
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

class ReceivingDetailsBottomSheet @Inject constructor(): BottomSheetDialogFragment() {

    private lateinit var layoutBinding: BottomsheetReceivingDetailsBinding
    private var companyId: String = ""
    private var manifestNo: String = ""
    private lateinit var mContext: Context
    var singleDatePicker: MaterialDatePicker<*>? = null
    var sqlDate: String = ""
    var bottomSheetClick: BottomSheetClick<Any>? = null
    var receivingDetails: ReceivingDetailsEnteredDataModel? = null

    companion object {
        var mPeriod: MutableLiveData<PeriodSelection> = MutableLiveData()
        //        var timePeriod: MutableLiveData<TimeSelection> = MutableLiveData()
        var timePeriod: MutableLiveData<String> = MutableLiveData()
        //    var timePeriod: MutableLiveData<TimeSelection?> = MutableLiveData<Any?>()
//        var TAG = Companion::class.java.name
        var TAG = "RECEIVING DETAILS BOTTOM SHEET"
        var SAVE_CLICK_TAG = "RECEIVING_DETAILS_SAVE"
        fun newInstance(
            mContext: Context,
            companyId: String,
            manifestNo: String,
            bottomSheetClick: BottomSheetClick<Any>,
            receivingDetailsEnteredDataModel: ReceivingDetailsEnteredDataModel
        ): ReceivingDetailsBottomSheet {
            val instance = ReceivingDetailsBottomSheet()
            instance.mContext = mContext
            instance.companyId = companyId
            instance.manifestNo = manifestNo
            instance.bottomSheetClick = bottomSheetClick
            instance.receivingDetails = receivingDetailsEnteredDataModel
            return instance
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutBinding = BottomsheetReceivingDetailsBinding.inflate(inflater)
        return layoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        layoutBinding.manifestNo = manifestNo
        if(receivingDetails == null) {
            layoutBinding.inputDate.setText(BaseActivity.getViewCurrentDate())
            sqlDate = BaseActivity.getSqlCurrentDate()
            layoutBinding.inputTime.setText(BaseActivity.getSqlCurrentTime())
        } else {
            if(receivingDetails?.receivingViewDate != null) {
                layoutBinding.inputDate.setText(receivingDetails?.receivingViewDate)
                sqlDate = receivingDetails?.receivingSqlDate.toString()
            }
            if(receivingDetails?.receivingViewTime != null) {
                layoutBinding.inputTime.setText(receivingDetails?.receivingViewTime)
            }
            if(receivingDetails?.receivingRemarks != null) {
                layoutBinding.inputRemark.setText(receivingDetails?.receivingRemarks)
            }
            if(receivingDetails?.receivingUserName != null) {
                layoutBinding.inputAgent.setText(receivingDetails?.receivingUserName)
            }
        }
        setOnClick()
        setObserver()
    }

    private fun setOnClick() {
        layoutBinding.inputDate.setOnClickListener {
            openSingleDatePicker()
        }
        layoutBinding.inputTime.setOnClickListener {
            openTimePicker()
        }
        layoutBinding.closeBottomSheet.setOnClickListener {
            if(receivingDetails != null) {
                receivingDetails?.receivingRemarks = layoutBinding.inputRemark.text.toString()
                bottomSheetClick?.onItemClick(receivingDetails!!, SAVE_CLICK_TAG)
                dismiss()
            } else {
                Utils.printToast(mContext, ENV.SOMETHING_WENT_WRONG_ERR_MSG)
            }
        }
        layoutBinding.btnSave.setOnClickListener {
            if(receivingDetails != null) {
                receivingDetails?.receivingRemarks = layoutBinding.inputRemark.text.toString()
                bottomSheetClick?.onItemClick(receivingDetails!!, SAVE_CLICK_TAG)
                dismiss()
            } else {
                Utils.printToast(mContext, ENV.SOMETHING_WENT_WRONG_ERR_MSG)
            }
        }
    }

    private fun setObserver() {
        mPeriod.observe(this) { period ->
            layoutBinding.inputDate.setText(period.viewsingleDate)
            sqlDate = period.sqlsingleDate.toString()
            receivingDetails?.receivingViewDate = period.viewsingleDate
            receivingDetails?.receivingSqlDate = period.sqlsingleDate
        }
        timePeriod.observe(this) { time ->
            layoutBinding.inputTime.setText(time)
            receivingDetails?.receivingViewTime = time
        }
    }

    private fun openSingleDatePicker() {
        Log.d(TAG, "SINGLE PERIOD SELECTION")
        val materialDateBuilder: MaterialDatePicker.Builder<*> =
            MaterialDatePicker.Builder.datePicker()
        materialDateBuilder.setTitleText("SELECT A DATE")
        singleDatePicker = materialDateBuilder.build()
        singleDatePicker!!.addOnPositiveButtonClickListener{ selection: Any ->
//            val viewFormat = SimpleDateFormat("MM-dd-yyyy")
            val viewFormat = SimpleDateFormat("dd-MM-yyyy")
            val sqlFormat = SimpleDateFormat("yyyy-MM-dd")
            val selectedDate = selection
            val singleDate = Date(selectedDate as Long)
            val periodSelection = PeriodSelection()
            periodSelection.sqlsingleDate = sqlFormat.format(singleDate)
            periodSelection.viewsingleDate = viewFormat.format(singleDate)
            mPeriod.postValue(periodSelection)
        }
        if(singleDatePicker != null) {
            if (singleDatePicker!!.isVisible) {
                return;
            }
            singleDatePicker!!.show(requireActivity().supportFragmentManager, "DATE_PICKER");
        }
    }

    private fun openTimePicker() {
        val materialTimePicker: MaterialTimePicker = MaterialTimePicker.Builder()
            .setTitleText("SELECT YOUR TIMING")
            .setHour(12)
            .setMinute(10)
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .build()

        materialTimePicker.show(requireActivity().supportFragmentManager, TAG)
        materialTimePicker.addOnPositiveButtonClickListener {

            val pickedHour: Int = materialTimePicker.hour
            val pickedMinute: Int = materialTimePicker.minute
            val formattedTime: String = when {
                (pickedMinute < 10)-> {
                    "${materialTimePicker.hour}:0${materialTimePicker.minute}"
                }
                else -> {
                    "${materialTimePicker.hour}:${materialTimePicker.minute}"
                }
            }
            timePeriod.postValue(formattedTime)
        }
    }
}