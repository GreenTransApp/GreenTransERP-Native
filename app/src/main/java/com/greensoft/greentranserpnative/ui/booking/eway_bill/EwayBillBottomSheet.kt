package com.greensoft.greentranserpnative.ui.booking.eway_bill

import android.content.Context
import android.os.Bundle
import android.service.autofill.OnClickAction
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.greensoft.greentranserpnative.databinding.EwayBillLayoutBinding
import com.greensoft.greentranserpnative.ui.booking.BookingAdapter
import com.greensoft.greentranserpnative.ui.booking.pickup_reference.models.SinglePickupRefModel
import com.greensoft.greentranserpnative.ui.bottomsheet.common.CommonBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.common.models.CommonBottomSheetModel
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

        ): EwayBillBottomSheet{
            val instance: EwayBillBottomSheet = EwayBillBottomSheet()
            instance.title = title
            ITEM_CLICK_VIEW_TYPE = title
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
        layoutBinding = EwayBillLayoutBinding.inflate(inflater)
//        return inflater.inflate(R.layout.eway_bill_layout, container, false)
        loadBookingList()
        setUpRecyclerView()

        return  layoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        OnClickAction()
        (activity as AppCompatActivity?)!!.supportActionBar?.title = title
    }

    fun OnClickAction(){

 }
    fun setUpRecyclerView(){
        if(adapter == null) {
            linearLayoutManager = LinearLayoutManager(activity)
            layoutBinding.recyclerView.layoutManager = linearLayoutManager
//            adapter = EwayBillAdapter(ewayBillList, bottomSheetClick!!)
            adapter = EwayBillAdapter(ewayBillList)
        }
        layoutBinding.recyclerView.adapter = adapter
    }
    private fun loadBookingList() {
       var ewayList: ArrayList<EwayBillModel> = ArrayList()

//        for (i in 0..100){
////            ewayBillList.addAll()
//
//        }

        val arr = arrayOfNulls<EwayBillModel>(5)
        arr.forEach {
            Log.d("test", "loadBookingList:testing....... ")
        }

        ewayBillList = arrayListOf(EwayBillModel(""))
    }

    override fun onItemClick(data: Any, clickType: String) {
        when(data){
//            "DELETE_REF" ->  run{
//                Log.d("test", "onItemClick:  testing ......")
//            }
        }
    }

}