package com.greensoft.greentranserpnative.ui.operation.call_register

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityCallRegisterBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.acceptPickup.AcceptPickupBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.common.CommonBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.common.models.CommonBottomSheetModel
import com.greensoft.greentranserpnative.ui.bottomsheet.rejectPickup.RejectPickupBottomSheet
import com.greensoft.greentranserpnative.ui.operation.call_register.models.CallRegisterModel
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.PickupReferenceActivity
import com.greensoft.greentranserpnative.ui.home.models.UserMenuModel
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CallRegisterActivity @Inject constructor() : BaseActivity(), OnRowClick<Any>, BottomSheetClick<Any> {
    private var rvAdapter: CallRegisterAdapter? = null
    private val viewModel: CallRegisterViewModel by viewModels()
    private val pickupAccRejViewModel: PickupAccRejViewModel by viewModels()
    private var callRegisterList: ArrayList<CallRegisterModel> = ArrayList()
    private lateinit var toolbar: Toolbar
    private lateinit var activityBinding: ActivityCallRegisterBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var acceptBottomSheetDialog: AcceptPickupBottomSheet? = null
    private var rejectBottomSheetDialog: RejectPickupBottomSheet? = null
    private var menuModel: UserMenuModel? = null
    var fromDt=""
    var toDt=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityCallRegisterBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
//        toolbar?.navigationIcon = ContextCompat.getDrawable(this,R.drawable.calendar)
//        toolbar?.setNavigationOnClickListener {
//
//            openDatePicker()
//        }

        setUpToolbar("ACCEPT JOBS ")
        menuModel = getMenuData()

        setObserver()
        setOnClick()
        searchItem()
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }
      private fun refreshData(){
          callRegisterList.clear()
          setupRecyclerView()
          lifecycleScope.launch {
              getCallRegisterList()
              delay(1500)
              activityBinding.refreshLayout.isRefreshing=false
          }
      }
    private fun getCallRegisterList() {
        viewModel.getCallRegisterList(
            loginDataModel?.companyid.toString(),
            "gtapp_getpendingjobs",
            listOf("prmfromdt","prmtodt","prmbranchcode", "prmusercode", "prmmenucode", "prmsessionid"),
            arrayListOf(fromDt,toDt,userDataModel?.loginbranchcode.toString(), userDataModel?.usercode.toString(), menuModel?.menucode.toString(), userDataModel?.sessionid.toString())
        )
    }

    fun searchItem(): Boolean {
        activityBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                rvAdapter?.filter?.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                rvAdapter?.filter?.filter(newText)
                return false
            }

        })
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
          R.id.datePicker -> {
            openDatePicker()
          }
        }
        return super.onOptionsItemSelected(item)
    }




    private fun setObserver() {
        activityBinding.refreshLayout.setOnRefreshListener {
            refreshData()

        }
        viewModel.isError.observe(this) { errMsg ->
            errorToast(errMsg)
        }
        viewModel.viewDialogLiveData.observe(this, Observer { show ->
            if (show) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })
        viewModel.callRegisterLiveData.observe(this) { callRegister ->
            callRegisterList = callRegister
            setupRecyclerView()
//            searchItem()
        }
        mPeriod.observe(this) { datePicker ->
            fromDt= datePicker.viewFromDate.toString()
            toDt=datePicker.viewToDate.toString()
            refreshData()
        }

        // ------------------------------- //
        pickupAccRejViewModel.acceptPickupLiveData.observe(this) {
            successToast(it)
            if(acceptBottomSheetDialog!=null) {
                if(acceptBottomSheetDialog!!.isVisible) {
                    acceptBottomSheetDialog!!.dismiss()
                    refreshData()
                }
            }
        }
        pickupAccRejViewModel.rejectPickupLiveData.observe(this) {
            successToast(it)
            if(rejectBottomSheetDialog!=null) {
                if(rejectBottomSheetDialog!!.isVisible) {
                    rejectBottomSheetDialog!!.dismiss()
                    refreshData()
                }
            }
        }

    }

    private fun setOnClick() {

    }

    private fun acceptJobAlert(data: CallRegisterModel) {
        AlertDialog.Builder(this)
            .setTitle("Alert!!!")
            .setMessage("Are you sure you want to Accept this job?")
            .setPositiveButton("Yes") { _, _ ->
//                var intent = Intent(this, PickupReferenceActivity::class.java)
//                startActivity(intent)
                acceptBottomSheetDialog = AcceptPickupBottomSheet.newInstance(mContext,
                    loginDataModel?.companyid.toString(),
                    data.transactionid.toString(),
                    pickupAccRejViewModel
                )
                acceptBottomSheetDialog!!.show(
                    supportFragmentManager,
                    AcceptPickupBottomSheet.TAG
                )
            }
            .setNeutralButton("No") { _, _ -> }
            .show()
    }

    private fun rejectJobAlert(data: CallRegisterModel) {
        AlertDialog.Builder(this)
            .setTitle("Alert!!!")
            .setMessage("Are you sure you want to Reject this job?")
            .setPositiveButton("Yes") { _, _ ->
//                var intent = Intent(this, PickupReferenceActivity::class.java)
//                startActivity(intent)
                rejectBottomSheetDialog = RejectPickupBottomSheet.newInstance(mContext, loginDataModel?.companyid.toString(), data.transactionid.toString(), pickupAccRejViewModel)
                rejectBottomSheetDialog!!.show(supportFragmentManager, RejectPickupBottomSheet.TAG)
            }
            .setNeutralButton("No") { _, _ -> }
            .show()
    }


    private fun setupRecyclerView() {
        linearLayoutManager = LinearLayoutManager(this)
        rvAdapter = CallRegisterAdapter(callRegisterList, this@CallRegisterActivity)
        activityBinding.recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = rvAdapter
        }

    }

    override fun onItemClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }

    override fun onCLick(data: Any, clickType: String) {
        when (clickType) {
            "ACCEPT_SELECT" -> run {
                val model: CallRegisterModel = data as CallRegisterModel
//                val intent = Intent(this, PickupReferenceActivity::class.java)
//                startActivity(intent)
                acceptJobAlert(model)
            }

            "REJECT_SELECT" -> run {
//                successToast("reject btn click")
                val model: CallRegisterModel = data as CallRegisterModel
//                val intent = Intent(this, PickupReferenceActivity::class.java)
//                startActivity(intent)
                rejectJobAlert(model)

            }
        }
    }
}