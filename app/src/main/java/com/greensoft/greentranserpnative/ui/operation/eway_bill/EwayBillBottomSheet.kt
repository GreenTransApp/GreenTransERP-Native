package com.greensoft.greentranserpnative.ui.operation.eway_bill

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.greensoft.greentranserpnative.base.BaseFragment
import com.greensoft.greentranserpnative.databinding.EwayBillLayoutBinding
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.common.alert.CommonAlert
import com.greensoft.greentranserpnative.ui.login.models.LoginDataModel
import com.greensoft.greentranserpnative.ui.login.models.UserDataModel
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.operation.booking.BookingActivity
import com.greensoft.greentranserpnative.ui.operation.booking.BookingViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EwayBillBottomSheet @Inject constructor() : BaseFragment(), BottomSheetClick<Any>, AlertCallback<Any> {
    private lateinit var layoutBinding: EwayBillLayoutBinding
    lateinit var linearLayoutManager: LinearLayoutManager
    private var adapter: EwayBillAdapter? = null
    private var bottomSheetClick: BottomSheetClick<Any> ?=null
    private var ewayBillList: ArrayList<ItemEwayBillModel> = ArrayList()
    private var title: String = "Selection"
    private lateinit var activity: BookingActivity
    private lateinit var bookingViewModel: BookingViewModel

    companion object {
        var ITEM_CLICK_VIEW_TYPE = "EWAY_BILL_BOTTOMSHEET"
        fun  newInstance(
            activity: BookingActivity,
            title: String,
            bookingViewModel: BookingViewModel,
            loginDataModel: LoginDataModel?,
            userDataModel: UserDataModel?
        ): EwayBillBottomSheet {
            val instance= EwayBillBottomSheet()
            ITEM_CLICK_VIEW_TYPE = title
            instance.title = title
            instance.activity = activity
            instance.bookingViewModel = bookingViewModel
            if(loginDataModel != null && userDataModel != null) {
                instance.loginDataModel = loginDataModel
                instance.userDataModel = userDataModel
            }
            return instance
        }
    }

    fun dataWillBeLostAlert() {
        CommonAlert.createAlert(
            context!!,
            "ALERT!!!",
            "WARNING: All entered E-WAY BILLs will be lost and you will have to enter it again. Are you sure you want to change the list?",
            this,
            "EWAY_DATA_LOST",
            ""
            )
//        AlertDialog.Builder(context)
//            .setTitle("Alert!!!")
//            .setMessage("WARNING: All entered E-WAY BILLs will be lost and you will have to enter it again. Are you sure you want to change the list?")
//            .setPositiveButton("Yes") { _, _ ->
//                setUpRecyclerView()
//            }
//            .setNeutralButton("No") { _, _ -> }
//            .show()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutBinding = EwayBillLayoutBinding.inflate(inflater)
//        loadBookingList()
        ewayBillList = arrayListOf(ItemEwayBillModel())
        setUpRecyclerView()

        return  layoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar?.title = title
        onClickAction()
        setOnChangeListener()
    }

private fun setOnChangeListener() {
    layoutBinding.inputNoOfEwayBill.addTextChangedListener(object: TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            dataWillBeLostAlert()
        }

    })

    bookingViewModel.accParaLiveData.observe(this) { commonResult ->
        if(commonResult.CommandStatus == 1 && commonResult.Message.toString() == "Y") {
            bookingViewModel.getEwayBillCreds(
                userDataModel?.companyid.toString(),
                "gtapp_getcompanyewaycreds",
                arrayListOf(),
                arrayListOf(),
                ewayBillList
            )
        }
    }
}

    private fun onClickAction(){
        layoutBinding.validateEway.setOnClickListener {
            validateEwayBillInputFields()
        }
        layoutBinding.disableEway.setOnClickListener {

        }
    }

    private fun validateEwayBillInputFields() {
        if(adapter != null) {
            if(adapter!!.isAllEwayBillInputsFilled()) {
                bookingViewModel.getAccParaValue("61", userDataModel!!.companyid.toString())
            }
        }
    }

    private fun setUpRecyclerView(){
        val noOfEwayStr: String = layoutBinding.inputNoOfEwayBill.text.toString()
        var noOfEwayBill: Int? = noOfEwayStr.toIntOrNull()
        if(noOfEwayBill == null) {
            layoutBinding.inputNoOfEwayBill.setText("1")
            noOfEwayBill = 1
        }
        ewayBillList.clear()
        for(i in 0 until noOfEwayBill) {
            ewayBillList.add(ItemEwayBillModel(""))
        }
        linearLayoutManager = LinearLayoutManager(activity)
        layoutBinding.recyclerView.layoutManager = linearLayoutManager
//            adapter = EwayBillAdapter(ewayBillList, bottomSheetClick!!)
        adapter = EwayBillAdapter(activity, ewayBillList)
        layoutBinding.recyclerView.adapter = adapter
    }
//    private fun loadBookingList() {
//       var ewayList: ArrayList<EwayBillModel> = ArrayList()
//
////        for (i in 0..100){
//////            ewayBillList.addAll()
////
////        }
//
//        val arr = arrayOfNulls<EwayBillModel>(5)
//        arr.forEach {
//            Log.d("test", "loadBookingList:testing....... ")
//        }
//
//        ewayBillList = arrayListOf(EwayBillModel(""))
//    }

    override fun onItemClick(data: Any, clickType: String) {
        when(data){
//            "DELETE_REF" ->  run{
//                Log.d("test", "onItemClick:  testing ......")
//            }
        }
    }

    override fun onAlertClick(alertClick: AlertClick, alertCallType: String, data: Any?) {
       when(alertClick) {
           AlertClick.YES -> {
               setUpRecyclerView()
           }
           AlertClick.NO -> {

           }
       }
    }

}