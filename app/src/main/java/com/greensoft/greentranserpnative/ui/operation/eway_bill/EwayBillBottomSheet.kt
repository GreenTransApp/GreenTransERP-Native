package com.greensoft.greentranserpnative.ui.operation.eway_bill

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.greensoft.greentranserpnative.R
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
import com.greensoft.greentranserpnative.ui.operation.eway_bill.models.EwayBillDetailResponse
import com.greensoft.greentranserpnative.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.Util
import java.util.concurrent.atomic.AtomicBoolean
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
    private var disableEwayBillForBooking = false
    var showChangeAlert = false
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
    }

    private fun disableEwayAlert() {
        CommonAlert.createAlert(
            context!!,
            "DISABLE ALERT!!!",
            "WARNING: All entered E-WAY BILLs will be lost and you will have to enter it again. Are you sure you want to disable?",
            this,
            "DISABLE_EWAY",
            ""
        )
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
        dialog!!.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet: FrameLayout = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
//                Utils.Log("COMMON_BOTTOM_SHEET", "Opened");
            BottomSheetBehavior.from(bottomSheet).state =
                BottomSheetBehavior.STATE_EXPANDED
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO;
            layoutBinding.extraSpace.minimumHeight = Resources.getSystem().displayMetrics.heightPixels / 2
        }
        onClickAction()
        setOnChangeListener()
        if(Utils.enteredValidatedEwayBillList == null) {
            layoutBinding.inputNoOfEwayBill.setText("1")
        } else if(Utils.enteredValidatedEwayBillList != null && Utils.enteredValidatedEwayBillList!!.isNotEmpty()) {
//            ewayBillList = Utils.enteredValidatedEwayBillList!!
            setUpRecyclerView()
        }
    }

private fun setOnChangeListener() {
    layoutBinding.inputNoOfEwayBill.addTextChangedListener(object: TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val enteredVal = p0.toString().toIntOrNull()
//            if(enteredVal!=null) {
//                if(enteredVal > 0) {
                    var isFilled = false
                    ewayBillList.forEachIndexed { index, itemEwayBillModel ->
                        if(itemEwayBillModel.ewayBillNo.isNotBlank()) {
                            isFilled = true
                        }
                    }
                    showChangeAlert = isFilled
//                }
//            }
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            val enteredVal = p0.toString().toIntOrNull()
            if(showChangeAlert && enteredVal != null && enteredVal > 0 && !disableEwayBillForBooking) dataWillBeLostAlert()
            else if(enteredVal != null && enteredVal > 0) setUpRecyclerView()
        }

    })

    bookingViewModel.ewayBillViewDialogLiveData.observe(this) { show ->
        Utils.logger("EWAY", show.toString())
        if(show) {
            showProgressDialog()
        } else {
            hideProgressDialog()
        }
    }

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

    bookingViewModel.ewayBillValidationDoneLiveData.observe(this) { isDone ->
        if(isDone) {
            val detail: EwayBillDetailResponse? = bookingViewModel.ewayBillDetailLiveData.value
            if(detail != null) {
                if(detail.status == 1) {
                    Utils.ewayBillValidated = true
                    Utils.ewayBillDetailResponse = detail
                    Utils.enteredValidatedEwayBillList = ewayBillList
                    successToast("Eway Bill Validated, You can continue on your booking.")
                } else {
                    Utils.ewayBillValidated = false
                    Utils.ewayBillDetailResponse = null
                    Utils.enteredValidatedEwayBillList = null
                    errorToast(detail.errorList[0].message.toString())
                }
            } else {
                Utils.ewayBillValidated = false
                Utils.ewayBillDetailResponse = null
                Utils.enteredValidatedEwayBillList = null
                errorToast("Some Error occurred while retrieving eway bill data.")
            }
        }
    }
}

    private fun onClickAction(){
        layoutBinding.validateEway.setOnClickListener {
            if(!disableEwayBillForBooking) {
                validateEwayBillInputFields()
            } else {
                errorToast("EWAY-BILL is disabled by you. Please click on enable EWAY-BILL.")
            }
        }
        layoutBinding.disableEway.setOnClickListener {
            if(disableEwayBillForBooking) {
                // EWAY is disabled right now, now it will be enabled.
                layoutBinding.disableEway.background.setTint(resources.getColor(R.color.danger, null))
                layoutBinding.disableEway.text = "DISABLE EWAY"
                disableEwayBillForBooking = !disableEwayBillForBooking
                layoutBinding.validateEway.isEnabled = !disableEwayBillForBooking
                layoutBinding.validateEway.background.setTint(resources.getColor(R.color.success, null))
                layoutBinding.inputNoOfEwayBill.isEnabled = !disableEwayBillForBooking
            } else {
                disableEwayAlert()
//                layoutBinding.disableEway.background.setTint(resources.getColor(R.color.success, null))
//                layoutBinding.disableEway.text = "ENABLE EWAY"
            }
//            disableEwayAlert()
        }

        layoutBinding.closeBottomSheet.setOnClickListener {
            dismiss()
        }
    }

    private fun validateEwayBillInputFields() {
        if(adapter != null) {
            if(adapter!!.isAllEwayBillInputsFilled()) {
                bookingViewModel.getAccParaValue("61", userDataModel!!.companyid.toString())
            }
        }
    }

    fun isValidatedListEmpty() {
        var isFilled = false
        Utils.enteredValidatedEwayBillList?.forEachIndexed { index, itemEwayBillModel ->
            if(!itemEwayBillModel.ewayBillNo.toString().isNullOrBlank()) {
                isFilled = true
            }
        }
        if(!isFilled) clearOldEwayBillData()
    }

    private fun setUpRecyclerView(){
        isValidatedListEmpty()
        if(Utils.enteredValidatedEwayBillList != null && Utils.enteredValidatedEwayBillList!!.isNotEmpty()) {
            ewayBillList = Utils.enteredValidatedEwayBillList!!
        } else {
            val noOfEwayStr: String = layoutBinding.inputNoOfEwayBill.text.toString()
            var noOfEwayBill: Int? = noOfEwayStr.toIntOrNull()
            if (noOfEwayBill == null) {
                layoutBinding.inputNoOfEwayBill.setText("")
                noOfEwayBill = 0
            }
            ewayBillList.clear()
            for (i in 0 until noOfEwayBill) {
                ewayBillList.add(ItemEwayBillModel(""))
            }
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

    fun clearOldEwayBillData() {
        Utils.ewayBillValidated = false
        Utils.ewayBillDetailResponse = null
        Utils.enteredValidatedEwayBillList?.clear()
        Utils.enteredEwayBillValidatedData.clear()
    }

    override fun onAlertClick(alertClick: AlertClick, alertCallType: String, data: Any?) {
       when(alertClick) {
           AlertClick.YES -> {
               if(alertCallType == "EWAY_DATA_LOST") {
                   clearOldEwayBillData()
                   setUpRecyclerView()
               } else if(alertCallType == "DISABLE_EWAY") {
                   clearOldEwayBillData()
                   if(!disableEwayBillForBooking) {
//                       layoutBinding.disableEway.background.setTint(resources.getColor(R.color.danger, null))
//                       layoutBinding.disableEway.text = "DISABLE EWAY"
//                   } else {
                       layoutBinding.disableEway.background.setTint(resources.getColor(R.color.success, null))
                       layoutBinding.disableEway.text = "ENABLE EWAY"
                       disableEwayBillForBooking = !disableEwayBillForBooking
                       layoutBinding.validateEway.isEnabled = !disableEwayBillForBooking
                       layoutBinding.validateEway.background.setTint(resources.getColor(R.color.disabled_color, null))
                       layoutBinding.inputNoOfEwayBill.isEnabled = !disableEwayBillForBooking
                       layoutBinding.inputNoOfEwayBill.setText("1")
                   }
               }
           }
           AlertClick.NO -> {

           }
       }
    }

}