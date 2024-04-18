package com.greensoft.greentranserpnative.ui.bottomsheet.undeliveredPodBottomSheet

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
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseFragment
import com.greensoft.greentranserpnative.databinding.FragmentUndeliveredScanPodBottomSheetBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.common.models.CommonBottomSheetModel
import com.greensoft.greentranserpnative.ui.bottomsheet.undeliveredPodBottomSheet.models.UndeliveredEnteredDataModel
import com.greensoft.greentranserpnative.ui.bottomsheet.vehicleSelection.VehicleSelectionAdapter

import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.ScanAndDeliveryViewModel
import com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.adapter.ScanUndeliveryAdapter
import com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.models.ScanDelReasonModel
import com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.models.ScanStickerModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UndeliveredScanPodBottomSheet @Inject constructor(): BaseFragment() {
    private lateinit var layoutBinding: FragmentUndeliveredScanPodBottomSheetBinding
    private val viewModel:ScanAndDeliveryViewModel by viewModels()
    private var bottomSheetClick: BottomSheetClick<Any>? = null
    private var title: String = "Selection"
    private var rvList: ArrayList<ScanStickerModel> = ArrayList()
    private var rvAdapter: ScanUndeliveryAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var index: Int = -1
    private lateinit var undelReasonList: ArrayList<ScanDelReasonModel>

    companion object {

        const val TAG = "UndeliveredBottomSheet"
        var ITEM_CLICK_VIEW_TYPE = "UNDELIVERED_POD_BOTTOM_SHEET"
        val UNDELIVERED_SAVE_CLICK_TAG = "UNDELIVERED_SAVE_CLICK_TAG"
        fun  newInstance(
            mContext: Context,
            title: String,
            bottomSheetClick: BottomSheetClick<Any>,
            rvList: java.util.ArrayList<ScanStickerModel>,
            undelReasonList: ArrayList<ScanDelReasonModel>
        ): UndeliveredScanPodBottomSheet {
            val instance: UndeliveredScanPodBottomSheet = UndeliveredScanPodBottomSheet()
            instance.mContext = mContext
            instance.rvList = rvList
            instance.bottomSheetClick = bottomSheetClick
            instance.undelReasonList = undelReasonList
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
        layoutBinding = FragmentUndeliveredScanPodBottomSheetBinding.inflate(inflater)
        return layoutBinding.root
//        layoutBinding.toolbarTitle.text = title

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            layoutBinding.toolbarTitle.text = title
        setUpRecyclerView()
        layoutBinding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                rvAdapter?.filter?.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                rvAdapter?.filter?.filter(newText)
                return false
            }

        })

        dialog!!.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet: FrameLayout = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
//                Utils.Log("COMMON_BOTTOM_SHEET", "Opened");
            BottomSheetBehavior.from(bottomSheet).state =
                BottomSheetBehavior.STATE_EXPANDED
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO;
            layoutBinding.extraSpace.minimumHeight = Resources.getSystem().displayMetrics.heightPixels / 2
        }

        layoutBinding.btnContinue.setOnClickListener {
            var stickerCSV: String = ""
            var unDelReasonCodeCSV: String = ""
            rvList.forEachIndexed { index, scanStickerModel ->
                stickerCSV += scanStickerModel.stickerno.toString() + ","
                unDelReasonCodeCSV += scanStickerModel.reasonCode.toString() + ","
            }

            val undeliveredDataModel = UndeliveredEnteredDataModel(
                stickerStr = stickerCSV,
                unDelReasonCodeStr = unDelReasonCodeCSV,
            )
            bottomSheetClick?.onItemClick(undeliveredDataModel, UNDELIVERED_SAVE_CLICK_TAG)
            dismiss()
        }

        layoutBinding.closeBottomSheet.setOnClickListener {
            dismiss()
        }
    }

    private fun setUpRecyclerView() {

        if(rvAdapter == null) {
            rvAdapter = ScanUndeliveryAdapter(
                rvList,
                mContext,
                bottomSheetClick!!,
                undelReasonList!!
            )
            linearLayoutManager = LinearLayoutManager(activity)
        }
        layoutBinding.recyclerView.layoutManager = linearLayoutManager
        layoutBinding.recyclerView.adapter= rvAdapter
        layoutBinding.recyclerView.itemAnimator = DefaultItemAnimator()
        layoutBinding.recyclerView.isNestedScrollingEnabled = true
        rvAdapter!!.notifyDataSetChanged()
    }

}