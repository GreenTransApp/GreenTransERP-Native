package com.greensoft.greentranserpnative.ui.bottomsheet.customerSelection


import com.greensoft.greentranserpnative.base.BaseFragment
import com.greensoft.greentranserpnative.ui.bottomsheet.customerSelection.model.CustomerSelectionModel
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CustomerSelectionBottomSheet  @Inject constructor(): BaseFragment(), OnRowClick<Any> {

    private lateinit var layoutBinding:CustomerSelectionBottomSheet
    private var onRowClick: OnRowClick<Any> = this
    private var onCustomerSelected: OnRowClick<Any>? = null
    private var title: String = "Selection"
    private var customerList:ArrayList<CustomerSelectionModel> = ArrayList()


    override fun onClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }
}