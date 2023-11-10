package com.greensoft.greentranserpnative.ui.operation.counterDetail

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.greensoft.greentranserpnative.databinding.LayoutNotiPanelBottomSheetBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.counterDetail.model.NotificationPanelBottomSheetModel


class NotificationPanelBottomSheet: BottomSheetDialogFragment() {
    private lateinit var layoutBinding: LayoutNotiPanelBottomSheetBinding
    private lateinit var mContext: Context
    private var onRowClick: OnRowClick<Any>? = null
    private var title: String = "Selection"
    private var rvList: ArrayList<NotificationPanelBottomSheetModel> = ArrayList()
    private var rvAdapter: NotificationPanelBottomSheetAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var withAdapter: Boolean = false
    private var index: Int = -1

    companion object {
        const val TAG = "CommonBottomSheet"
        var ITEM_CLICK_VIEW_TYPE = "COMMON_BOTTOM_SHEET_ITEM"
        fun  newInstance(
            mContext: Context,
            title: String,
            onRowClick: OnRowClick<Any>,
            rvList: ArrayList<NotificationPanelBottomSheetModel>,
            withAdapter: Boolean = false,
            index: Int = -1
        ): NotificationPanelBottomSheet{
            val instance: NotificationPanelBottomSheet = NotificationPanelBottomSheet()
            instance.mContext = mContext
            instance.title = title
            ITEM_CLICK_VIEW_TYPE = title
            instance.onRowClick = onRowClick
            instance.rvList = rvList
            instance.withAdapter = withAdapter
            instance.index = index
            return instance
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutBinding = LayoutNotiPanelBottomSheetBinding.inflate(inflater)
//        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return layoutBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        (activity as AppCompatActivity?)!!.setSupportActionBar(layoutBinding.toolBar.root)
//        (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        (activity as AppCompatActivity?)!!.supportActionBar?.title = title.
        layoutBinding.toolbarTitle.text = title

        setUpRecyclerView()
        layoutBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
            val bottomSheet: FrameLayout =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
//                Utils.Log("COMMON_BOTTOM_SHEET", "Opened");
            BottomSheetBehavior.from(bottomSheet).state =
                BottomSheetBehavior.STATE_EXPANDED
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO;
            layoutBinding.extraSpace.minimumHeight =
                Resources.getSystem().displayMetrics.heightPixels / 2
        }

        layoutBinding.closeBottomSheet.setOnClickListener {
            dismiss()
        }
    }


    private fun setUpRecyclerView() {
        if (rvAdapter == null) {
            rvAdapter = NotificationPanelBottomSheetAdapter(
                mContext,
                this,
                rvList,
                onRowClick!!,
                ITEM_CLICK_VIEW_TYPE,
                withAdapter,
                index
            )
            linearLayoutManager = LinearLayoutManager(activity)
        }
        layoutBinding.recyclerView.layoutManager = linearLayoutManager
        layoutBinding.recyclerView.adapter = rvAdapter
        layoutBinding.recyclerView.itemAnimator = DefaultItemAnimator()
        layoutBinding.recyclerView.isNestedScrollingEnabled = true
        rvAdapter!!.notifyDataSetChanged()
    }

    override fun onDetach() {
        super.onDetach()
        onRowClick = null
    }

}