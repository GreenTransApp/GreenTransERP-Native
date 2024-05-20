package com.greensoft.greentranserpnative.ui.bottomsheet.drsScanBottomSheet

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.greensoft.greentranserpnative.ENV
import com.greensoft.greentranserpnative.base.BaseFragment
import com.greensoft.greentranserpnative.databinding.BottomSheetDrsScanBinding
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.common.alert.CommonAlert
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.drs.model.DrsDataModel
import com.greensoft.greentranserpnative.ui.operation.drsScan.DrsScanAdapter
import com.greensoft.greentranserpnative.ui.operation.drsScan.DrsScanViewModel
import com.greensoft.greentranserpnative.ui.operation.drsScan.model.ScannedDrsModel
import com.greensoft.greentranserpnative.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DrsScanBottomSheet @Inject constructor(): BaseFragment(), OnRowClick<Any>, AlertCallback<Any> {
    private lateinit var layoutBinding: BottomSheetDrsScanBinding
    private var onRowClick: OnRowClick<Any> = this
    private lateinit var bottomSheetClick: BottomSheetClick<Any>
    private var title: String = "DRS SCAN"
    private val viewModel: DrsScanViewModel by viewModels()
    private var rvAdapter: DrsScanAdapter? = null
    private lateinit var manager: LinearLayoutManager
    private var rvList: ArrayList<ScannedDrsModel> = ArrayList()
    private var drsNo: String = ""
    private var drsDetails: DrsDataModel? = null

    companion object{
        const val TAG = "VendorBottomSheet"
        const val DRS_STICKER_DELETE = "DRS_STICKER_DELETE"
        const val DRS_NO_SAVED_TAG = "DRS_SAVED_TAG"
        private var instance: DrsScanBottomSheet? = null

        fun newInstance(
            mContext: Context,
            onBottomSheetClick: BottomSheetClick<Any>,
            drsNo: String?,
            drsDetails: DrsDataModel
        ): DrsScanBottomSheet {
            val drsScanBottomSheet = DrsScanBottomSheet()
            drsScanBottomSheet.mContext = mContext
            drsScanBottomSheet.bottomSheetClick = onBottomSheetClick
            drsScanBottomSheet.drsDetails = drsDetails
            drsNo?.let { nullSafeDrsNo ->
                drsScanBottomSheet.drsNo = nullSafeDrsNo
            }
            instance = drsScanBottomSheet
            return drsScanBottomSheet
        }

        fun sendStickerToFragment(stickerNo: String) {
            instance?.mScanner?.postValue(stickerNo)
        }

    }

    private fun getDrsStickerList(){
        if(drsNo == "") {
            Utils.logger("DRS SCAN ACTIVITY", "DRS IS NULL")
            return
        }
        viewModel.getDrsStickers(
            getCompanyId(),
            getUserCode(),
            getLoginBranchCode(),
            drsNo,
            getSessionId()
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutBinding = BottomSheetDrsScanBinding.inflate(layoutInflater)
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
                rvAdapter?.filter?.filter(query ?: "")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                rvAdapter?.filter?.filter(newText ?: "")
                return false
            }

        })
        setOnClicks()
    }

    private fun setObservers(){
        layoutBinding.swipeRefreshLayout.setOnRefreshListener {
            getDrsStickerList()
            lifecycleScope.launch {
                delay(1500)
                layoutBinding.swipeRefreshLayout.isRefreshing = false
            }
        }
        viewModel.isError.observe(this) { errMsg ->
            errorToast(errMsg)
        }
        viewModel.viewDialogLiveData.observe(this) { show ->
//            progressBar.visibility = if(show) View.VISIBLE else View.GONE
            if (show) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }
        mScanner.observe(this){ scannedStickerNo->
            var stickerAlreadyExists: Boolean = false
            for(scannedDrsList in rvList) {
                if(scannedDrsList.stickerno == scannedStickerNo) {
                    stickerAlreadyExists = true
                    break
                }
            }
            if(stickerAlreadyExists) {
                removeSticker(scannedStickerNo)
            } else {
                updateSticker(scannedStickerNo)
            }
//            Utils.debugToast(this, "sticker no $scannedStickerNo")
//            Toast.makeText(this, "sticker no $stickerData", Toast.LENGTH_SHORT).show()
        }

        viewModel.updateStickerLiveData.observe(this){ updateSticker->
            if(updateSticker.commandstatus == 1) {
                if(updateSticker.drsno != null && updateSticker.drsno != "") {
                    drsNo = updateSticker.drsno.toString()
                    bottomSheetClick.onItemClick(drsNo, DRS_NO_SAVED_TAG)
                }
                getDrsStickerList()
                if(updateSticker.commandmessage != null) {
                    successToast(updateSticker.commandmessage.toString())
                } else {
                    successToast("Added Sticker Successfully.")
                }
            }
        }
        viewModel.removeStickerLiveData.observe(this) { removeSticker ->
            if(removeSticker.commandstatus == 1) {
                if(removeSticker.drsno != null && removeSticker.drsno != "") {
                    drsNo = removeSticker.drsno.toString()
                }
                getDrsStickerList()
                if(removeSticker.commandmessage != null) {
                    successToast(removeSticker.commandmessage.toString())
                } else {
                    successToast("Removed Sticker Successfully.")
                }
            }
        }
        viewModel.stickerListLiveData.observe(this) { stickerList ->
            if(stickerList.size > 0) {
                rvList = stickerList
                setupRecyclerView()
            }
        }
    }

    private fun setupRecyclerView() {
        if (rvAdapter == null) {
            manager = LinearLayoutManager(mContext)
            layoutBinding.recyclerView.layoutManager = manager
        }
        rvAdapter = DrsScanAdapter(rvList, onRowClick)
        layoutBinding.recyclerView.adapter = rvAdapter
    }



    override fun onDetach() {
        super.onDetach()
    }

    private  fun showRemoveStickerAlert(drsStickerModel: ScannedDrsModel){
        CommonAlert.createAlert(
            context = mContext,
            header = "REMOVE ALERT!!",
            description = " Are You Sure You Want To Remove Sticker# ${drsStickerModel.stickerno} of GR# ${drsStickerModel.grno}?",
            callback = this,
            alertCallType = DrsScanAdapter.REMOVE_STICKER_TAG,
            data = drsStickerModel
        )
    }

    fun setOnClicks(){
        layoutBinding.closeBottomSheet.setOnClickListener {
            dismiss()
        }
        layoutBinding.submitBtn.setOnClickListener {
            var scannedStickerNo = layoutBinding.inputSticker.text.toString()
            if(scannedStickerNo.isBlank()) {
                errorToast("No Sticker# entered to submit. Please enter a sticker# to submit.")
            } else {
                var stickerAlreadyExists = false
                for (scannedDrsModel in rvList) {
                    if (scannedDrsModel.stickerno == scannedStickerNo) {
                        stickerAlreadyExists = true
                        showRemoveStickerAlert(scannedDrsModel)
//                        removeSticker(scannedStickerNo)
                        break
                    }
                }
                if (!stickerAlreadyExists) {
                    updateSticker(scannedStickerNo)
                }
            }
        }
    }

    private fun updateSticker(stickerNo: String){
        drsDetails?.let { detail ->
            viewModel.updateSticker(
                getCompanyId(),
                getUserCode(),
                getLoginBranchCode(),
                getLoginBranchCode(),
                drsNo,
                detail.drsdate,
                detail.drstime,
//                detail.vehiclecode.toString(),
                "A9531",
                detail.vendcode.toString(),
                "",
                detail.remarks.toString(),
                stickerNo,
                detail.deliveredby.toString(),
                detail.dlvagentcode.toString(),
                detail.vehicleno.toString(),
                getSessionId()
            )
        }
    }

    private fun removeSticker(stickerNo: String){
        viewModel.removeSticker(
            getCompanyId(),
            getUserCode(),
            getLoginBranchCode(),
            drsNo,
            stickerNo,
            getSessionId()
        )
    }

    override fun onClick(data: Any, clickType: String) {
        when(clickType) {
            DrsScanAdapter.REMOVE_STICKER_TAG -> run {
                try {
                    val scannedDrsData = data as ScannedDrsModel
                    removeSticker(scannedDrsData.stickerno.toString())
                } catch (ex: Exception) {
                    errorToast(ENV.SOMETHING_WENT_WRONG_ERR_MSG)
                }
            }
        }
    }

    override fun onAlertClick(alertClick: AlertClick, alertCallType: String, data: Any?) {
        when(alertClick) {
            AlertClick.YES -> run {
                when(alertCallType) {
                    DrsScanAdapter.REMOVE_STICKER_TAG -> {
                        try {
                            var scannedStickerModel = data as ScannedDrsModel
                            scannedStickerModel.stickerno?.let { removeSticker(it) }
                        } catch (ex: Exception) {
                            errorToast(ENV.SOMETHING_WENT_WRONG_ERR_MSG)
                        }
                    }
                    else -> {
                        errorToast(ENV.SOMETHING_WENT_WRONG_ERR_MSG)
                    }
                }
            }
            AlertClick.NO -> run {

            }
            else -> {
                errorToast(ENV.SOMETHING_WENT_WRONG_ERR_MSG)
            }
        }
    }

}