package com.greensoft.greentranserpnative.ui.bottomsheet.acceptPickup

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.common.PeriodSelection
import com.greensoft.greentranserpnative.common.TimeSelection
import com.greensoft.greentranserpnative.databinding.BottomsheetAcceptPickupBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.common.models.CommonBottomSheetModel
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import java.text.SimpleDateFormat
import java.util.*

class AcceptPickupBottomSheet: BottomSheetDialogFragment() {

    private lateinit var layoutBinding: BottomsheetAcceptPickupBinding
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
        ): AcceptPickupBottomSheet {
            val instance = AcceptPickupBottomSheet()
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
        layoutBinding = BottomsheetAcceptPickupBinding.inflate(inflater)
        return layoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.setSupportActionBar(layoutBinding.toolBar.root)
        (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity?)!!.supportActionBar?.title = "ACCEPT PICKUP"
        layoutBinding.transactionId = transactionId
        layoutBinding.inputDate.setText(BaseActivity.getViewCurrentDate())
        layoutBinding.inputTime.setText(BaseActivity.getSqlCurrentTime())
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
    }

    private fun setObserver() {
        mPeriod.observe(this) { period ->
            layoutBinding.inputDate.setText(period.viewsingleDate)
        }
        timePeriod.observe(this) { time ->
            layoutBinding.inputTime.setText(time)
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
            singleDatePicker!!.show(activity!!.supportFragmentManager, "DATE_PICKER");
        }
    }

    private fun openTimePicker() {
        val materialTimePicker: MaterialTimePicker = MaterialTimePicker.Builder()
            .setTitleText("SELECT YOUR TIMING")
            .setHour(12)
            .setMinute(10)
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .build()

        materialTimePicker.show(activity!!.supportFragmentManager, TAG)
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