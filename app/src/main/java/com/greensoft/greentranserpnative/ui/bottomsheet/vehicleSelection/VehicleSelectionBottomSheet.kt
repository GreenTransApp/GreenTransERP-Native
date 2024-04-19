package com.greensoft.greentranserpnative.ui.bottomsheet.vehicleSelection


import android.content.Context
import android.content.res.Resources
import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.greensoft.greentranserpnative.base.BaseFragment
import com.greensoft.greentranserpnative.databinding.BottomsheetVehicleSelectionBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.bookingIndentInfoBottomSheet.BookingIndentInfoViewModel
import com.greensoft.greentranserpnative.ui.bottomsheet.vehicleSelection.model.VehicleModelDRS
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.VehicleSelectionModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.http.Query
import javax.inject.Inject

@AndroidEntryPoint
class VehicleSelectionBottomSheet  @Inject constructor(): BaseFragment(), OnRowClick<Any>{

    private lateinit var layoutBinding: BottomsheetVehicleSelectionBinding
    private var onRowClick: OnRowClick<Any> = this
    private var onVehicleSelected: OnRowClick<Any>? = null
    private var title: String = "Selection"
    private val viewModel:VehicleSelectionViewModel by viewModels()
    private var rvAdapter: VehicleSelectionAdapter? = null
    private lateinit var manager:LinearLayoutManager
    private var vehicleList:ArrayList<VehicleModelDRS> = ArrayList()
    private var vehicleName:String = ""
    companion object{
        const val TAG = "VehicleBottomSheet"
        const val VEHICLE_CLICK_TYPE = "VEHICLE_LOV_SELECTION"
        fun newInstance(
            mContext: Context,
            title: String,
            onVehicleSelected: OnRowClick<Any>
        ):VehicleSelectionBottomSheet{
            val instance:VehicleSelectionBottomSheet = VehicleSelectionBottomSheet()
            instance.mContext = mContext
            instance.title = title
            instance.onVehicleSelected = onVehicleSelected
            return instance

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutBinding = BottomsheetVehicleSelectionBinding.inflate(layoutInflater)
//        return super.onCreateView(inflater, container, savedInstanceState)
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
//        vehicleList = demoList()

        layoutBinding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
//                rvAdapter?.filter?.filter(query)
//                getVehicleList(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                rvAdapter?.filter?.filter(newText)
                getVehicleList(newText)
                return false
            }

        })

        close()
    }

    private fun setObservers(){
        viewModel.vehicleLiveData.observe(this){vehicleData->
            vehicleList = vehicleData
            setupRecyclerView()
        }
    }

        private fun setupRecyclerView() {
        if (rvAdapter == null) {
            manager = LinearLayoutManager(mContext)
            layoutBinding.recyclerView.layoutManager = manager
        }
        rvAdapter = VehicleSelectionAdapter(vehicleList, onRowClick)
        layoutBinding.recyclerView.adapter = rvAdapter
    }

    private fun getVehicleList(query:String?){
        if (query != null) {
            viewModel.getVehicleList(
                getCompanyId(),
                getLoginBranchCode(),
                getUserCode(),
                query,
                "JEENA",
                "S",
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        onVehicleSelected = null
    }


    fun close(){
        layoutBinding.closeBottomSheet.setOnClickListener {
            dismiss()
        }
    }
    private fun demoList(): ArrayList<VehicleModelDRS> {
        val dataList: ArrayList<VehicleModelDRS> =
            java.util.ArrayList<VehicleModelDRS>()
        for (i in 1..100) {
            dataList.add(VehicleModelDRS(1.5*i,"",1,"",
                i.toString(),"",""))
        }
        return dataList
    }

    override fun onClick(data: Any, clickType: String) {
        if(onVehicleSelected != null) {
            if(clickType == VehicleSelectionAdapter.VEHICLE_SELECTION_ROW_CLICK) {
                onVehicleSelected?.onClick(data, VEHICLE_CLICK_TYPE)
                dismiss()
            }
        }
    }

}