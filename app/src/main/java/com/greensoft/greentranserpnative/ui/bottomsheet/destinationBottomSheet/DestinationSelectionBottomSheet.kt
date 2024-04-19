package com.greensoft.greentranserpnative.ui.bottomsheet.destinationBottomSheet

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
import com.greensoft.greentranserpnative.databinding.DestinationSelectionBottomSheetBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.modeCode.ModeCodeSelectionBottomSheet
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import javax.inject.Inject


class DestinationSelectionBottomSheet @Inject constructor(): BaseFragment(), OnRowClick<Any>{
   lateinit var  layoutBinding:DestinationSelectionBottomSheetBinding
    private var toStationList: ArrayList<ToStationModel> = ArrayList()
    private val viewModel: DestinationSelectionViewModel by viewModels()
    private var onRowClick: DestinationSelectionBottomSheet = this
    private var selectedItem: OnRowClick<Any>? = null
    private var title: String = "Selection"
    private var modeCode: String = ""
    private var rvAdapter: DestinationSelectionAdapter? = null
    private lateinit var manager: LinearLayoutManager
    companion object {
        const val TAG = "DestinationBottomSheet"
        const val CLICK_TYPE = "DESTINATION_SELECTION"

        fun newInstance(
            mContext: Context,
            title: String,
            modeCode:String,
            onSelectedItem: OnRowClick<Any>
        ): DestinationSelectionBottomSheet {
            val instance: DestinationSelectionBottomSheet = DestinationSelectionBottomSheet()
            instance.mContext = mContext
            instance.title = title
            instance.modeCode=modeCode
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
        layoutBinding = DestinationSelectionBottomSheetBinding.inflate(layoutInflater)
        return  layoutBinding.root


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

                getDestinationList(newText)
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


    private fun setupRecyclerView() {
        if(toStationList.isEmpty()) {
            layoutBinding.emptyView.visibility = View.VISIBLE
        } else {
            layoutBinding.emptyView.visibility = View.GONE
        }
        if (rvAdapter == null) {
            manager = LinearLayoutManager(mContext)
            layoutBinding.recyclerView.layoutManager = manager
        }
        rvAdapter = DestinationSelectionAdapter(toStationList, onRowClick)
        layoutBinding.recyclerView.adapter = rvAdapter
    }

    fun setObservers(){
       viewModel.toStationLiveData.observe(this){ toStationData ->
           if (toStationData.elementAt(0).commandstatus == 1) {
               toStationList = toStationData
               setupRecyclerView()
           }
       }
   }

    fun  getDestinationList(query:String?){
        viewModel.getToStationList(
            getCompanyId(),
            "greentransapp_StationMast",
            listOf("prmbranchcode","prmusercode","prmcharstr"),
            arrayListOf(getLoginBranchCode(),getUserCode(),query.toString()))
    }

    override fun onClick(data: Any, clickType: String) {
        if(selectedItem != null) {
            if(clickType == DestinationSelectionAdapter.DESTINATION_SELECTION_ROW_CLICK) {
                selectedItem?.onClick(data, DestinationSelectionBottomSheet.CLICK_TYPE)
                dismiss()
            }
        }
    }

}