package com.greensoft.greentranserpnative.ui.operation.chatScreen

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.Manifest.permission.CALL_PHONE
import androidx.core.app.ActivityCompat
import com.greensoft.greentranserpnative.base.BaseFragment
import com.greensoft.greentranserpnative.databinding.FragmentCustomerInfoBottomSheetBinding
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CustomerInfoBottomSheet @Inject constructor() : BaseFragment(), BottomSheetClick<Any>, AlertCallback<Any> {
    private  lateinit var layoutBinding:FragmentCustomerInfoBottomSheetBinding
    private var title: String = "Selection"
    private var transactionId: String = ""
    private var custName: String = ""
    private var origin: String = ""
    private var destination: String = ""
    private var mobileNum: String = ""
    private var number: String = "9718004651"
    var REQUEST_PHONE_CALL= 1

    private lateinit var activity: ChatScreenActivity

    companion object {
        var ITEM_CLICK_VIEW_TYPE = "CUST_INFO_BOTTOM_SHEET"
        fun newInstance(
            activity: ChatScreenActivity,
            title: String,
            transactionId:String,
            custName:String,
            origin:String,
            destination:String,
            mobileNum:String,


            ): CustomerInfoBottomSheet {
            val instance = CustomerInfoBottomSheet()
            ITEM_CLICK_VIEW_TYPE = title
            instance.title = title
            instance.activity = activity
            instance.transactionId = transactionId
            instance.custName = custName
            instance.origin = origin
            instance.destination = destination
            instance.mobileNum = mobileNum
//
            return instance
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
            layoutBinding=FragmentCustomerInfoBottomSheetBinding.inflate(inflater)
            return layoutBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickAction()
        layoutBinding.custMobileNum.setPaintFlags(layoutBinding.custMobileNum.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        layoutBinding.transactionId.setText(transactionId)
        layoutBinding.custName.setText(custName)
        layoutBinding.custOrigin.setText(origin)
        layoutBinding.custDest.setText(destination)
        layoutBinding.custMobileNum.setText(mobileNum)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PHONE_CALL){
            makeCall()
        }
    }

     private  fun onClickAction(){
         layoutBinding.closeBottomSheet.setOnClickListener {
             dismiss()
         }

         layoutBinding.custMobileNum.setOnClickListener {

             if(ActivityCompat.checkSelfPermission(mContext,CALL_PHONE) != PackageManager.PERMISSION_GRANTED) ActivityCompat.requestPermissions(requireActivity(),arrayOf(android.Manifest.permission.CALL_PHONE),REQUEST_PHONE_CALL)
         else {
                 makeCall()
             }
         }
     }


    private fun makeCall(){
        val intent=Intent(Intent.ACTION_CALL)
        intent.setData(Uri.parse("tel:$mobileNum"))
        if (ActivityCompat.checkSelfPermission(mContext, CALL_PHONE) != PackageManager.PERMISSION_GRANTED
        ) {
            errorToast("Permission denied")
            return
        }
        startActivity(intent)
    }
    override fun onAlertClick(alertClick: AlertClick, alertCallType: String, data: Any?) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }


}