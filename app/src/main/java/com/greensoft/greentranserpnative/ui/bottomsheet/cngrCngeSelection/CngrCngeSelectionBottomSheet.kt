package com.greensoft.greentranserpnative.ui.bottomsheet.cngrCngeSelection

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
import com.greensoft.greentranserpnative.databinding.BottomSheetCngrCngeSelectionBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.cngrCngeSelection.model.CngrCngeSelectionModel
import com.greensoft.greentranserpnative.ui.bottomsheet.customerSelection.CustomerSelectionAdapter
import com.greensoft.greentranserpnative.ui.bottomsheet.customerSelection.CustomerSelectionBottomSheet
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CngrCngeSelectionBottomSheet  @Inject constructor(): BaseFragment(), OnRowClick<Any> {
    private lateinit var layoutBinding:BottomSheetCngrCngeSelectionBinding
    private var onRowClick: OnRowClick<Any> = this
    private var onCngrCngeSelected: OnRowClick<Any>? = null
    private var title: String = ""
    private val viewModel: CngrCngeSelectionViewModel by viewModels()
    private var rvAdapter: CngrCngeSelectionAdapter?=null
    private lateinit var manager: LinearLayoutManager
    private var cngrCngeList:ArrayList<CngrCngeSelectionModel> = ArrayList()
    private var cngrCngeType:String? = null
    private var custCode:String? =null
    private var branchCode:String? =null



    companion object{
        const val TAG = "CngrCngeBottomSheet"
        const val CNGR_CLICK_TYPE = "CNGR_LOV_SELECTION"
        const val CNGE_CLICK_TYPE = "CNGE_LOV_SELECTION"

        fun newInstance(
            mContext: Context,
            title: String,
            onCngrCngeSelected: OnRowClick<Any>,
            clickType: String,
            custCode:String,
            branchcode  :String
        ): CngrCngeSelectionBottomSheet {
            val instance: CngrCngeSelectionBottomSheet = CngrCngeSelectionBottomSheet()
            instance.mContext = mContext
            instance.title = title
            instance.onCngrCngeSelected = onCngrCngeSelected
            instance.cngrCngeType = clickType
            instance.custCode = custCode
            instance.branchCode = branchcode
            return instance
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutBinding = BottomSheetCngrCngeSelectionBinding.inflate(layoutInflater)
        return layoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (cngrCngeType=="R"){
            layoutBinding.toolbarTitle.text = "Consignor Selection"

        } else if (cngrCngeType=="E"){
            layoutBinding.toolbarTitle.text = "Consignee Selection "

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
                getCngrCngeList(newText)
                return false
            }

        })
        close()
    }

    private fun setObservers(){
        viewModel.cngrCngeLiveData.observe(this){ cngrCngeData->
            cngrCngeList = cngrCngeData
            setupRecyclerView()
        }

    }

    private fun getCngrCngeList(query:String?){
        if (query!=null){
            viewModel.getCngrCngeList(
                getCompanyId(),
                branchCode,
                getUserCode(),
//                "0000000001",
                custCode,
                cngrCngeType,
                getSessionId(),
                query

            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        onCngrCngeSelected = null
    }

    fun close(){
        layoutBinding.closeBottomSheet.setOnClickListener {
            dismiss()
        }
    }


    private fun setupRecyclerView() {
        if (rvAdapter == null) {
            manager = LinearLayoutManager(mContext)
            layoutBinding.recyclerView.layoutManager = manager
        }
        rvAdapter = CngrCngeSelectionAdapter(cngrCngeList, onRowClick)
        layoutBinding.recyclerView.adapter = rvAdapter
    }



    override fun onClick(data: Any, clickType: String) {
        if (onCngrCngeSelected!=null){
            if (clickType== CngrCngeSelectionAdapter.CNGR_CNGE_SELECTION_ROW_CLICK && cngrCngeType=="R"){
                onCngrCngeSelected?.onClick(data, CNGR_CLICK_TYPE)
                dismiss()
            } else if (clickType== CngrCngeSelectionAdapter.CNGR_CNGE_SELECTION_ROW_CLICK && cngrCngeType=="E"){
                onCngrCngeSelected?.onClick(data, CNGE_CLICK_TYPE)
                dismiss()
            }
        }
    }

}