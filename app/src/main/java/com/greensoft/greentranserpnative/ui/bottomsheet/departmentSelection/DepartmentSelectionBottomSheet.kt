package com.greensoft.greentranserpnative.ui.bottomsheet.departmentSelection

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
import com.greensoft.greentranserpnative.databinding.BottomSheetDepartmentSelectionBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.customerSelection.CustomerSelectionAdapter
import com.greensoft.greentranserpnative.ui.bottomsheet.customerSelection.CustomerSelectionBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.departmentSelection.model.DepartmentSelectionModel
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class DepartmentSelectionBottomSheet @Inject constructor(): BaseFragment(), OnRowClick<Any>{

    private lateinit var layoutBinding:BottomSheetDepartmentSelectionBinding

    private var onRowClick: OnRowClick<Any> = this
    private var onDepartmentSelected: OnRowClick<Any>? = null
    private var title: String = "Selection"
    private var clickType: String = ""
    private val viewModel: DepartmentSeletionViewModel by viewModels()
    private var rvAdapter: DepartmentSelectionAdapter?=null
    private lateinit var manager: LinearLayoutManager
    private var departmentList:ArrayList<DepartmentSelectionModel> = ArrayList()
    private var departmentCategory:String = ""


    companion object{
        const val TAG = "DepartmentBottomSheet"
        const val DEPARTMENT_CLICK_TYPE = "DEPARTMENT_LOV_SELECTION"

        fun newInstance(
            mContext: Context,
            title: String,
            onDepartmentSelected: OnRowClick<Any>,
            clickType: String
        ): DepartmentSelectionBottomSheet {
            val instance: DepartmentSelectionBottomSheet = DepartmentSelectionBottomSheet()
            instance.mContext = mContext
            instance.title = title
            instance.onDepartmentSelected = onDepartmentSelected
            instance.clickType = clickType
            return instance
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutBinding = BottomSheetDepartmentSelectionBinding.inflate(layoutInflater)
        return layoutBinding.root
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
                getDepartmentList(newText)
                return false
            }

        })
        close()
    }

    private fun setObservers(){
        viewModel.departmentLiveData.observe(this){departmentData->
            departmentList = departmentData
            setupRecyclerView()
        }

    }


    private fun setupRecyclerView() {
        if (rvAdapter == null) {
            manager = LinearLayoutManager(mContext)
            layoutBinding.recyclerView.layoutManager = manager
        }
        rvAdapter = DepartmentSelectionAdapter(departmentList, onRowClick)
        layoutBinding.recyclerView.adapter = rvAdapter
    }


    private fun getDepartmentList(query:String?){
        if (query!=null){
            viewModel.getDepartmentList(
                getCompanyId(),
                getUserCode(),
                getSessionId(),
                "0000000001",
                "GTAPP_BOOKINGWITHOUTINDENT",
                "104",
                query
            )
        }

    }

    override fun onDetach() {
        super.onDetach()
        onDepartmentSelected = null
    }
    fun close(){
        layoutBinding.closeBottomSheet.setOnClickListener {
            dismiss()
        }
    }

    override fun onClick(data: Any, clickType: String) {
        if (onDepartmentSelected!=null){
            if (clickType== DepartmentSelectionAdapter.DEPARTMENT_SELECTION_ROW_CLICK){
                onDepartmentSelected?.onClick(data, DEPARTMENT_CLICK_TYPE)
                dismiss()
            }
        }
    }


}