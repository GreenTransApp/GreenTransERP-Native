package com.greensoft.greentranserpnative.ui.bottomsheet.loadingGrList

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.greensoft.greentranserpnative.ENV
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.base.BaseFragment
import com.greensoft.greentranserpnative.databinding.BottomSheetLoadingGrListBinding
import com.greensoft.greentranserpnative.model.LoadingGrListModel
import com.greensoft.greentranserpnative.ui.bottomsheet.drsScanBottomSheet.DrsScanBottomSheet
import com.greensoft.greentranserpnative.ui.login.models.UserDataModel
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.drs.model.DrsDataModel
import com.greensoft.greentranserpnative.ui.operation.drsScan.DrsScanAdapter
import com.greensoft.greentranserpnative.ui.operation.drsScan.DrsScanViewModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.PickupManifestViewModel
import com.greensoft.greentranserpnative.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoadingGrListBottomSheet @Inject constructor(): BaseFragment(), OnRowClick<Any> {

    private lateinit var loadingNo: String
    private lateinit var bottomSheetClick: BottomSheetClick<Any>
    private val viewModel: PickupManifestViewModel by viewModels()
    private var rvAdapter: LoadingGrListAdapter? = null
    private var manager: LinearLayoutManager? = null
    private lateinit var layoutBinding: BottomSheetLoadingGrListBinding
    private var rvList: ArrayList<LoadingGrListModel> = ArrayList()

    companion object {
        const val TAG = "VendorBottomSheet"
        const val DRS_STICKER_DELETE = "DRS_STICKER_DELETE"
        const val DRS_NO_SAVED_TAG = "DRS_SAVED_TAG"
        private var instance: LoadingGrListBottomSheet? = null

        fun newInstance(
            mContext: Context,
            onBottomSheetClick: BottomSheetClick<Any>,
            loadingNo: String,
            userDataModel: UserDataModel
        ): LoadingGrListBottomSheet {
            val loadingGrListBottomSheet = LoadingGrListBottomSheet()
            loadingGrListBottomSheet.mContext = mContext
            loadingGrListBottomSheet.bottomSheetClick = onBottomSheetClick
            loadingGrListBottomSheet.userDataModel = userDataModel
            loadingGrListBottomSheet.loadingNo = loadingNo
            instance = loadingGrListBottomSheet
            return loadingGrListBottomSheet
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutBinding = BottomSheetLoadingGrListBinding.inflate(layoutInflater)
        return layoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        setObservers()

        getLoadingGrList()
    }

    private fun setOnClicks() {
        layoutBinding.closeBottomSheet.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun getLoadingGrList() {
//        if(userDataModel)
        var menuCode = "GTAPP_NATIVEPICKUPMANIFEST"
        Utils.menuModel?.let {
            menuCode = it.menucode
        }
        if(userDataModel == null) {
            errorToast(ENV.SOMETHING_WENT_WRONG_ERR_MSG)
        }
        viewModel.getLoadingGrList(
            companyId = userDataModel!!.companyid.toString(),
            userCode = userDataModel!!.usercode,
            branchCode = userDataModel!!.loginbranchcode,
            menuCode = menuCode,
            sessionId = userDataModel!!.sessionid,
            loadingNo = loadingNo
        )
    }


    private fun setObservers(){
        layoutBinding.swipeRefreshLayout.setOnRefreshListener {
            getLoadingGrList()
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

        viewModel.loadingGrListLiveData.observe(this) { grList ->
            if(grList.size > 0) {
                rvList = grList
                setupRecyclerView()
            }
        }
    }

    private fun setupRecyclerView() {
        if (rvAdapter == null) {
            manager = LinearLayoutManager(mContext)
            layoutBinding.recyclerView.layoutManager = manager
        }
        rvAdapter = LoadingGrListAdapter(mContext, rvList, this)
        layoutBinding.recyclerView.adapter = rvAdapter
    }

    override fun onClick(data: Any, clickType: String) {
        when(clickType) {

        }
    }


}