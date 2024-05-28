package com.greensoft.greentranserpnative.ui.operation.bookingWithoutIndent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityDirectBookingBinding
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class DirectBooking @Inject constructor(): BaseActivity(), OnRowClick<Any>, AlertCallback<Any>,
    BottomSheetClick<Any> {
    private lateinit var activityBinding:ActivityDirectBookingBinding

    private var sqlDate: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityDirectBookingBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("Direct Booking")

        activityBinding.inputDate.inputType = InputType.TYPE_NULL;
        activityBinding.inputTime.inputType = InputType.TYPE_NULL;
        activityBinding.inputDate.setText(getViewCurrentDate())
        sqlDate = getSqlCurrentDate()
        activityBinding.inputTime.setText(getSqlCurrentTime())

        setObservers()
        setOnClick()
    }

    private fun setOnClick(){
        activityBinding.inputDate.setOnClickListener {
            openSingleDatePicker()
        }
        activityBinding.inputTime.setOnClickListener {
            openTimePicker()
        }
        activityBinding.autoGrCheck.setOnCheckedChangeListener { compoundButton, bool ->
            activityBinding.inputGrNo.setText("")
            if(bool) {
                activityBinding.inputGrNo.hint = "AUTO GR #"
//                activityBinding.inputGrNo.
            } else {
                activityBinding.inputGrNo.hint = "ENTER GR #"
            }
            activityBinding.inputGrNo.isEnabled = !(bool)
        }
    }

    private fun setObservers(){
        mPeriod.observe(this) { datePicker ->
            activityBinding.inputDate.setText(datePicker.viewsingleDate)
            sqlDate=datePicker.sqlsingleDate.toString()
        }
        timePeriod.observe(this) { time ->
            activityBinding.inputTime.setText(time)

        }
    }

    private fun checkFieldsBeforeSave(){
        if(activityBinding.inputDate.text.isNullOrEmpty()){
            activityBinding.inputDate.error="Please Select Date"
            errorToast("Please Select Date")
            return
        }
        else if (activityBinding.inputTime.text.isNullOrEmpty()){
            errorToast("Please Select Date")
            return
        }
        if(!activityBinding.autoGrCheck.isChecked && activityBinding.inputGrNo.text.isNullOrBlank()){
            errorToast("Please Enter GR Number")
            return
        }
    }

    override fun onAlertClick(alertClick: AlertClick, alertCallType: String, data: Any?) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }

    override fun onClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }
}