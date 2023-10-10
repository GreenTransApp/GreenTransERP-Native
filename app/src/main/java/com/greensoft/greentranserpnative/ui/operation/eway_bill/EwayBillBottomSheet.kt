package com.greensoft.greentranserpnative.ui.operation.eway_bill

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.greensoft.greentranserpnative.databinding.EwayBillLayoutBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.acceptPickup.AcceptPickupBottomSheet
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EwayBillBottomSheet @Inject constructor() : BottomSheetDialogFragment(), BottomSheetClick<Any> {
    private lateinit var layoutBinding: EwayBillLayoutBinding
    lateinit var linearLayoutManager: LinearLayoutManager
    private var adapter: EwayBillAdapter? = null
    private var bottomSheetClick: BottomSheetClick<Any> ?=null
    private var ewayBillList: ArrayList<EwayBillModel> = ArrayList()
    private var title: String = "Selection"

    companion object {
        var ITEM_CLICK_VIEW_TYPE = "EWAY_BILL_BOTTOMSHEET"
        fun  newInstance(
            mContext: Context,
            title: String,

        ): EwayBillBottomSheet {
            val instance= EwayBillBottomSheet()
            instance.title = title
            ITEM_CLICK_VIEW_TYPE = title
            return instance
        }
    }

    fun dataWillBeLostAlert() {
        AlertDialog.Builder(context)
            .setTitle("Alert!!!")
            .setMessage("WARNING: All entered E-WAY BILLs will be lost and you will have to enter it again. Are you sure you want to change the list?")
            .setPositiveButton("Yes") { _, _ ->
                setUpRecyclerView()
            }
            .setNeutralButton("No") { _, _ -> }
            .show()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutBinding = EwayBillLayoutBinding.inflate(inflater)
//        loadBookingList()
        ewayBillList = arrayListOf(EwayBillModel())
        setUpRecyclerView()

        return  layoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        OnClickAction()
        (activity as AppCompatActivity?)!!.supportActionBar?.title = title
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
}

    fun OnClickAction(){

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
            ewayBillList.add(EwayBillModel(""))
        }
        linearLayoutManager = LinearLayoutManager(activity)
        layoutBinding.recyclerView.layoutManager = linearLayoutManager
//            adapter = EwayBillAdapter(ewayBillList, bottomSheetClick!!)
        adapter = EwayBillAdapter(ewayBillList)
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

}