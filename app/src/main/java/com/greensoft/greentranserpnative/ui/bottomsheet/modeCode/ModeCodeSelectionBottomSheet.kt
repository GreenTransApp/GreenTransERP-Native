package com.greensoft.greentranserpnative.ui.bottomsheet.modeCode

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
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
import com.greensoft.greentranserpnative.databinding.BottomsheetVehicleSelectionBinding
import com.greensoft.greentranserpnative.databinding.ModeCodeSelectionBottomSheetBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.vehicleSelection.VehicleSelectionAdapter
import com.greensoft.greentranserpnative.ui.bottomsheet.vehicleSelection.VehicleSelectionBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.vehicleSelection.VehicleSelectionViewModel
import com.greensoft.greentranserpnative.ui.bottomsheet.vendorSelection.VendorSelectionBottomSheet
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ModeCodeSelectionBottomSheet @Inject constructor(): BaseFragment(), OnRowClick<Any> {
    private lateinit var layoutBinding:ModeCodeSelectionBottomSheetBinding
    private var modeList: ArrayList<ModeCodeSelectionModel> = ArrayList()

    private val viewModel: ModeCodeSelectionViewModel by viewModels()
    private var onRowClick: OnRowClick<Any> = this
    private var selectedItem: OnRowClick<Any>? = null
    private var title: String = "Selection"
    private var orgCode: String = ""
    private var modeCode: String = ""
    private var groupCode: String = ""
    private var rvAdapter: ModeCodeAdapter? = null
    private lateinit var manager: LinearLayoutManager


    companion object{
        const val TAG = "ModeCodeBottomSheet"
        const val CLICK_TYPE = "MODE_CODE_SELECTION"

        fun newInstance(
            mContext: Context,
            title: String,
            orgCode:String,
            modeCode:String,
            groupCode:String,
            onSelectedItem: OnRowClick<Any>
        ): ModeCodeSelectionBottomSheet {
            val instance: ModeCodeSelectionBottomSheet = ModeCodeSelectionBottomSheet()
            instance.mContext = mContext
            instance.title = title
            instance.orgCode=orgCode
            instance.modeCode=modeCode
            instance.groupCode=groupCode
            instance.selectedItem = onSelectedItem
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
        layoutBinding = ModeCodeSelectionBottomSheetBinding.inflate(layoutInflater)
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

               getModeCodeList(newText)
                return false
            }

        })

        close()
    }

    fun close(){
        layoutBinding.closeBottomSheet.setOnClickListener {
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        selectedItem = null
    }
    private   fun  setObservers (){
        viewModel.modeCodeLiveData.observe(this) { modeCode ->
            if (modeCode.elementAt(0).commandstatus == 1) {
                modeList = modeCode
                setupRecyclerView()

            } else {
//                errorToast(groupCode.elementAt(0).commandmessage.toString())
            }

        }

    }
    private fun setupRecyclerView() {
        if(modeList.isEmpty()) {
            layoutBinding.emptyView.visibility = View.VISIBLE
        } else {
            layoutBinding.emptyView.visibility = View.GONE
        }
        if (rvAdapter == null) {
            manager = LinearLayoutManager(mContext)
            layoutBinding.recyclerView.layoutManager = manager
        }
        rvAdapter = ModeCodeAdapter(modeList, onRowClick)
        layoutBinding.recyclerView.adapter = rvAdapter
    }

    private fun getModeCodeList(query:String?) {

        viewModel.getModeCode(
            loginDataModel?.companyid.toString(),
            "greentransapp_showmodeV2",
            listOf("prmorgcode","prmdestcode","prmmodetype","prmmodegroupcode","prmcharstr"),
            arrayListOf(getLoginBranchCode(),orgCode,modeCode,groupCode,query.toString())

        )
    }


    override fun onClick(data: Any, clickType: String) {
        if(selectedItem != null) {
            if(clickType == ModeCodeAdapter.MODE_CODE_SELECTION_ROW_CLICK) {
                selectedItem?.onClick(data, ModeCodeSelectionBottomSheet.CLICK_TYPE)
                dismiss()
            }
        }
    }
}