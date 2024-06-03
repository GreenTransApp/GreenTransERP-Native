package com.greensoft.greentranserpnative.ui.operation.bookingOption

import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityBookingOptionBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.bookingWithoutIndent.DirectBookingActivity
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.PickupReferenceActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BookingOptionActivity @Inject constructor() : BaseActivity(), OnRowClick<Any> {
    private lateinit var activityBinding:ActivityBookingOptionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityBookingOptionBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("Booking Options")
        setOnClick()
    }
    private fun setOnClick(){
        activityBinding.bookingBtn.setOnClickListener {
            val intent = Intent(this, PickupReferenceActivity::class.java)
//            intent.putExtra("MENU_DATA", jsonSerialized)
            startActivity(intent)
        }
        activityBinding.directBooking.setOnClickListener {
            val intent = Intent(this,DirectBookingActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onClick(data: Any, clickType: String) {
        val gson = Gson()
        val jsonSerialized = gson.toJson(data)
    }
}