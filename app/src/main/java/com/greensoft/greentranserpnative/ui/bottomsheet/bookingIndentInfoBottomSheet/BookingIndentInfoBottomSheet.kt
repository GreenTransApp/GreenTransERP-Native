package com.greensoft.greentranserpnative.ui.bottomsheet.bookingIndentInfoBottomSheet


import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.base.BaseFragment
import com.greensoft.greentranserpnative.databinding.BottomSheetBookingIndentInfoBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.bookingIndentInfoBottomSheet.model.BookingIndentInfoModel
import com.greensoft.greentranserpnative.ui.login.models.UserDataModel
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BookingIndentInfoBottomSheet  @Inject constructor(): BaseFragment() {
    private lateinit var layoutBinding: BottomSheetBookingIndentInfoBinding
    private val viewModel: BookingIndentInfoViewModel by viewModels()
    private var onRowClick: OnRowClick<Any>? = null
    private var title: String = "Selection"
    private lateinit var transactionId:String
    private var bookingIndentInfoData:BookingIndentInfoModel? = null



    companion object {
        const val TAG = "CommonBottomSheet"
        var ITEM_CLICK_VIEW_TYPE = "COMMON_BOTTOM_SHEET_ITEM"
        fun  newInstance(
            mContext: Context,
            title: String,
            transactionId: String,
            onRowClick: OnRowClick<Any>,
            withAdapter: Boolean = false,
            index: Int = -1
        ): BookingIndentInfoBottomSheet{
            val instance: BookingIndentInfoBottomSheet = BookingIndentInfoBottomSheet()
            instance.mContext = mContext
            instance.title = title
            ITEM_CLICK_VIEW_TYPE = title
            instance.onRowClick = onRowClick
            instance.transactionId = transactionId
            return instance
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutBinding = BottomSheetBookingIndentInfoBinding.inflate(inflater)
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
        close()
        getBookingIndentInfo()
        setObservers()


    }

    fun setObservers(){
        viewModel.bookingIndentInfoLiveData.observe(this){bookingIndentInfo->
            bookingIndentInfoData = bookingIndentInfo
            setUpCardUi()
        }
    }

    fun setUpCardUi(){
        layoutBinding.tvJob.text = bookingIndentInfoData?.transactionid.toString()?:"No data available"

        layoutBinding.tvCustomer.text = bookingIndentInfoData?.custname?:"No data available"
        layoutBinding.tvDepartment.text  = bookingIndentInfoData?.custdeptname?:"No data available"
        layoutBinding.tvConsignee.text = bookingIndentInfoData?.cnge?:"No data available"
        layoutBinding.tvConsignor.text = bookingIndentInfoData?.cngr?:"No data available"
        layoutBinding.tvOrigin.text = bookingIndentInfoData?.branchname?:"No data available"
        layoutBinding.tvDestination.text = bookingIndentInfoData?.destname?:"No data available"

        layoutBinding.tvOriginPin.text = bookingIndentInfoData?.orgpincode?:"No data available"
        layoutBinding.tvOriginLocation.text = bookingIndentInfoData?.orglocation?:"No data available"
        layoutBinding.tvOriginOda.text = bookingIndentInfoData?.orgoda?:"No data available"
        layoutBinding.tvOriginOdaDistance.text = bookingIndentInfoData?.orgodadistance?:"No data available"

        layoutBinding.tvDestiPin.text = bookingIndentInfoData?.destpincode?:"No data available"
        layoutBinding.tvDestiLocation.text = bookingIndentInfoData?.destlocation?:"No data available"
        layoutBinding.tvDestiOda.text = bookingIndentInfoData?.destoda?:"No data available"
        layoutBinding.tvDestiOdaDistance.text = bookingIndentInfoData?.destodadistance?:"No data available"
    }

    fun getBookingIndentInfo(){
        viewModel.getBookingIndentInfo(
            userDataModel?.companyid.toString(),
            transactionId
        )
    }

    override fun onDetach() {
        super.onDetach()
        onRowClick = null
    }


    fun close(){
        layoutBinding.closeBottomSheet.setOnClickListener {
            dismiss()
        }
    }

}