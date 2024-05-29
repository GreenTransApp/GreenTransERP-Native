package com.greensoft.greentranserpnative.ui.bottomsheet.pinCodeSelection

import android.content.Context
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
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
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseFragment
import com.greensoft.greentranserpnative.databinding.BottomSheetPinCodeSelectionBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.customerSelection.CustomerSelectionAdapter
import com.greensoft.greentranserpnative.ui.bottomsheet.customerSelection.CustomerSelectionBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.pinCodeSelection.model.PinCodeModel
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import javax.inject.Inject

class PinCodeSelectionBottomSheet @Inject constructor(): BaseFragment(), OnRowClick<Any> {

    private lateinit var layoutBinding: BottomSheetPinCodeSelectionBinding
    private var onRowClick: OnRowClick<Any> = this
    private var onPinCodeSelected: OnRowClick<Any>? = null
    private var title: String = "Selection"
    private var locationtype: String = ""
    private val viewModel: PinCodeSelectionViewModel by viewModels()
    private var rvAdapter: PinCodeSelectionAdapter?=null
    private lateinit var manager: LinearLayoutManager
    private var pinCodeDataList:ArrayList<PinCodeModel> = ArrayList()


    companion object{
        const val TAG = "PinCodeBottomSheet"
        const val ORIGIN_PIN_CODE_CLICK_TYPE = "ORIGIN_PIN_CODE_LOV_SELECTION"
        const val DESTINATION_PIN_CODE_CLICK_TYPE = "DESTINATION_PIN_CODE_LOV_SELECTION"

        fun newInstance(
            mContext: Context,
            title: String,
            onVendorSelected: OnRowClick<Any>,
            clickType: String
        ): PinCodeSelectionBottomSheet {
            val instance: PinCodeSelectionBottomSheet = PinCodeSelectionBottomSheet()
            instance.mContext = mContext
            instance.title = title
            instance.onPinCodeSelected = onVendorSelected
            instance.locationtype = clickType
            return instance
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutBinding = BottomSheetPinCodeSelectionBinding.inflate(layoutInflater)
        return layoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (locationtype=="O"){
           layoutBinding.toolbarTitle.text = "Origin PinCode Seletion"
        }
        else if (locationtype=="D"){
            layoutBinding.toolbarTitle.text = "Destination PinCode Selection"
        }


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
                getPinCodeList(newText)
                return false
            }

        })
        close()
    }

    private fun setObservers(){
        viewModel.pinCodeLiveData.observe(this){pinCodeData->
            pinCodeDataList = pinCodeData
            setupRecyclerView()
        }
    }

    private fun setupRecyclerView() {
        if (rvAdapter == null) {
            manager = LinearLayoutManager(mContext)
            layoutBinding.recyclerView.layoutManager = manager
        }
        rvAdapter = PinCodeSelectionAdapter(pinCodeDataList, onRowClick)
        layoutBinding.recyclerView.adapter = rvAdapter
    }

    private fun getPinCodeList(query:String?){
        if (query!=null){
            viewModel.getPinCode(

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
        onPinCodeSelected = null
    }

    fun close(){
        layoutBinding.closeBottomSheet.setOnClickListener {
            dismiss()
        }
    }




    override fun onClick(data: Any, clickType: String) {
        if (onPinCodeSelected!=null){
            if (clickType== PinCodeSelectionAdapter.PIN_CODE_SELECTION_ROW_CLICK && locationtype=="O"){
                onPinCodeSelected?.onClick(data, ORIGIN_PIN_CODE_CLICK_TYPE)
                dismiss()
            }
             if (clickType== PinCodeSelectionAdapter.PIN_CODE_SELECTION_ROW_CLICK && locationtype=="D"){
                 onPinCodeSelected?.onClick(data, DESTINATION_PIN_CODE_CLICK_TYPE)
                 dismiss()
            }
        }
    }


}