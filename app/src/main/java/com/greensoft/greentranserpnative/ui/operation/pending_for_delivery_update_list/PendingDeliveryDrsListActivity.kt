package com.greensoft.greentranserpnative.ui.operation.pending_for_delivery_update_list

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityPendingDeliveryDrsListBinding
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.pending_for_delivery_update_list.models.DrsPendingListModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class PendingDeliveryDrsListActivity  @Inject constructor(): BaseActivity(),
    AlertCallback<Any>, OnRowClick<Any>,
    BottomSheetClick<Any> {

        lateinit var  activityBinding:ActivityPendingDeliveryDrsListBinding
        private  val viewModel:PendingDeliveryDrsListViewModel by viewModels()
        private  var rvList:ArrayList<DrsPendingListModel> = ArrayList()
        private var rvAdapter :PendingDeliveryDrsListAdapter? =null
        private lateinit var manager: LinearLayoutManager
        private var fromDate: String = getSqlDate()!!
        private var toDate:String = getSqlDate()!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityPendingDeliveryDrsListBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("Pending Delivery Drs List")
//        getPendingDrsList()
         setObservers()
    }

     private fun setObservers(){
       viewModel.drsPendingListLiveData.observe(this){ drsData ->
           rvList =drsData
           setupRecyclerView()

       }
         mPeriod.observe(this) { it ->
             fromDate = it.sqlFromDate.toString()
             toDate = it.sqlToDate.toString()
             refreshData()

         }
         viewModel.isError.observe(this){ errMsg->
             errorToast(errMsg)
         }
         viewModel.viewDialogLiveData.observe(this, Observer { show ->
             if(show) {
                 showProgressDialog()
             } else {
                 hideProgressDialog()
             }
         })
     }

    private fun refreshData() {
        getPendingDrsList()
        lifecycleScope.launch {
            delay(1500)
            activityBinding.swipeRefreshLayout.isRefreshing = false
        }
    }
    private fun setOnClick(){

    }

    private fun setupRecyclerView() {
        if(rvAdapter== null) {
            manager = LinearLayoutManager(this)
            activityBinding.recyclerView.layoutManager = manager
            rvAdapter = PendingDeliveryDrsListAdapter(rvList, this)
        }
        activityBinding.recyclerView.adapter = rvAdapter



    }

    private fun getPendingDrsList(){
      viewModel.getDrsPendingList(
          companyId = getCompanyId(),
          userCode = getUserCode(),
          loginBranchCode = getLoginBranchCode(),
          fromDt = "2023-08-15",
          toDt = "2024-08-31",
//          fromDt = fromDate,
//          toDt = toDate,
          sessionId = getSessionId()
      )
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
    override fun onAlertClick(alertClick: AlertClick, alertCallType: String, data: Any?) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }

    override fun onClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }
}