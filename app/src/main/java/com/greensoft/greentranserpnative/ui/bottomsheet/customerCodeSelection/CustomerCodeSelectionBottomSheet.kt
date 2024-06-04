package com.greensoft.greentranserpnative.ui.bottomsheet.customerCodeSelection

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.greensoft.greentranserpnative.base.BaseFragment
import com.greensoft.greentranserpnative.databinding.CustomerCodeSelectionBottomSheetBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.customerSelection.CustomerSelectionAdapter
import com.greensoft.greentranserpnative.ui.bottomsheet.customerSelection.CustomerSelectionBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.customerSelection.model.CustomerSelectionModel
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CustomerCodeSelectionBottomSheet  @Inject constructor(): BaseFragment(), OnRowClick<Any> {
    private lateinit var layoutBinding: CustomerCodeSelectionBottomSheetBinding
    private var onRowClick: OnRowClick<Any> = this
    private var onCustomerSelected: OnRowClick<Any>? = null
    private var title: String = "Selection"
    private var clickType: String = "cusCode"
    private val viewModel: CustomerCodeSelectionViewModel by viewModels()
    private var rvAdapter: CustomerCodeSelectionAdapter?=null
    private lateinit var manager: LinearLayoutManager
    private var customerList:ArrayList<CustomerSelectionModel> = ArrayList()
    private var customerCategory:String = ""

    companion object {
        const val TAG = "CustomerCodeBottomSheet"
        const val CUSTOMER_CLICK_TYPE = "CUSTOMER_CODE_LOV_SELECTION"

        fun newInstance(
            mContext: Context,
            title: String,
            onCustomerSelected: OnRowClick<Any>,
            clickType: String
        ): CustomerCodeSelectionBottomSheet {
            val instance: CustomerCodeSelectionBottomSheet = CustomerCodeSelectionBottomSheet()
            instance.mContext = mContext
            instance.title = title
            instance.onCustomerSelected = onCustomerSelected
            instance.clickType = clickType
            return instance
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutBinding.toolbarTitle.text = title

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
            setObservers()
        }
        layoutBinding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                getCustomerList(newText)
                return false
            }

        })
        close()
    }
    private fun setObservers(){
        viewModel.customerLivedata.observe(this){ custList->
            customerList = custList
            setupRecyclerView()
        }

    }
    private fun setupRecyclerView() {
        if (rvAdapter == null) {
            manager = LinearLayoutManager(mContext)
            layoutBinding.recyclerView.layoutManager = manager
        }
        rvAdapter = CustomerCodeSelectionAdapter(customerList, onRowClick)
        layoutBinding.recyclerView.adapter = rvAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutBinding = CustomerCodeSelectionBottomSheetBinding.inflate(layoutInflater)
        return layoutBinding.root
    }



    private fun getCustomerList(query:String?){
        if (query!=null){
            viewModel.getCustomerList(
                getCompanyId(),
                getUserCode(),
                getLoginBranchCode(),
                getSessionId(),
                "GTAPP_BOOKINGWITHOUTINDENT",
                query
            )
        }

    }

    override fun onDetach() {
        super.onDetach()
        onCustomerSelected = null
    }

    fun close(){
        layoutBinding.closeBottomSheet.setOnClickListener {
            dismiss()
        }
    }

    override fun onClick(data: Any, clickType: String) {
        if (onCustomerSelected!=null){
            if (clickType== CustomerSelectionAdapter.CUSTOMER_SELECTION_ROW_CLICK){
                onCustomerSelected?.onClick(data, CustomerSelectionBottomSheet.CUSTOMER_CLICK_TYPE)
                dismiss()
            }
        }
    }
}