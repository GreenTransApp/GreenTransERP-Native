package com.greensoft.greentranserpnative.ui.bottomsheet.vendorSelection

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
import com.greensoft.greentranserpnative.databinding.BottomsheetVendorSelectionBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.vehicleSelection.VehicleSelectionAdapter
import com.greensoft.greentranserpnative.ui.bottomsheet.vendorSelection.model.VendorModelDRS
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.http.Query
import javax.inject.Inject

@AndroidEntryPoint
class VendorSelectionBottomSheet @Inject constructor(): BaseFragment(), OnRowClick<Any> {
    private lateinit var layoutBinding:BottomsheetVendorSelectionBinding
    private var onRowClick: OnRowClick<Any> = this
    private var onVendorSelected: OnRowClick<Any>? = null
    private var title: String = "Selection"
    private val viewModel: VendorSelectionViewModel by viewModels()
    private var rvAdapter:VendorSelectionAdapter?=null
    private lateinit var manager: LinearLayoutManager
    private var vendorList:ArrayList<VendorModelDRS> = ArrayList()
    private var vendorName:String = ""
    private var vendorCategory:String = ""

    companion object{
        const val TAG = "VendorBottomSheet"
        const val VENDOR_CLICK_TYPE = "VENDOR_LOV_SELECTION"

        fun newInstance(
            mContext: Context,
            title: String,
            onVendorSelected: OnRowClick<Any>,
            vendorCategoryType: String
        ):VendorSelectionBottomSheet{
            val instance:VendorSelectionBottomSheet = VendorSelectionBottomSheet()
            instance.mContext = mContext
            instance.title = title
            instance.onVendorSelected = onVendorSelected
            instance.vendorCategory = vendorCategoryType
            return instance
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutBinding = BottomsheetVendorSelectionBinding.inflate(layoutInflater)
        return layoutBinding.root
//        return super.onCreateView(inflater, container, savedInstanceState)
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
                getVendorList(newText)
                return false
            }

        })
        close()
    }

    private fun setObservers(){
        viewModel.vendorLiveData.observe(this){vendData->
            vendorList = vendData
            setupRecyclerView()
        }
    }

    private fun setupRecyclerView() {
        if (rvAdapter == null) {
            manager = LinearLayoutManager(mContext)
            layoutBinding.recyclerView.layoutManager = manager
        }
        rvAdapter = VendorSelectionAdapter(vendorList, onRowClick)
        layoutBinding.recyclerView.adapter = rvAdapter
    }

    private fun getVendorList(query: String?){
        if (query!=null){
            viewModel.getVendorList(
                getCompanyId(),
                query,
                vendorCategory,
            )
        }

    }

    override fun onDetach() {
        super.onDetach()
        onVendorSelected = null
    }


    fun close(){
        layoutBinding.closeBottomSheet.setOnClickListener {
            dismiss()
        }
    }

    override fun onClick(data: Any, clickType: String) {
       if (onVendorSelected!=null){
           if (clickType==VendorSelectionAdapter.VENDOR_SELECTION_ROW_CLICK){
               onVendorSelected?.onClick(data, VENDOR_CLICK_TYPE)
               dismiss()
           }
       }
    }
}