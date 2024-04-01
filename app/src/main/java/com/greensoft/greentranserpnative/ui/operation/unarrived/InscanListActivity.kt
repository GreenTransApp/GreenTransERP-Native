package com.greensoft.greentranserpnative.ui.operation.unarrived

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityInscanListBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.PickupReferenceAdapter
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.models.PickupRefModel
import com.greensoft.greentranserpnative.ui.operation.unarrived.models.InscanListModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class InscanListActivity @Inject constructor(): BaseActivity(), OnRowClick<Any> {
    lateinit var activityBinding: ActivityInscanListBinding
    private val viewModel: InscanListViewModel by viewModels()
    private var inscanList: ArrayList<InscanListModel> = ArrayList()
    private var rvAdapter: InscanListAdapter? = null
    lateinit var linearLayoutManager: LinearLayoutManager

    var fromDt= getSqlCurrentDate()
    var toDt= getSqlCurrentDate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding= ActivityInscanListBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("Inscan List")
        setObserver()
        searchItem()
        refreshData()
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

     private fun setObserver(){
         activityBinding.refreshLayout.setOnRefreshListener {
             refreshData()
         }
         viewModel.isError.observe(this) { errMsg ->
             errorToast(errMsg)
         }
         viewModel.viewDialogLiveData.observe(this, Observer { show ->
             if(show) {
                 showProgressDialog()
             } else {
                 hideProgressDialog()
             }
         })
         viewModel.inscanListLiveData.observe(this) { list ->
             inscanList = list
             setupRecyclerView()

         }
         mPeriod.observe(this) { datePicker ->
             fromDt= datePicker.sqlFromDate.toString()
             toDt=datePicker.sqlToDate.toString()
             getInscanList()

         }
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

     private fun getInscanList(){
         viewModel.getInscanList(
          companyId = loginDataModel?.companyid.toString(),
          // companyId = "17846899",  // need to change for  jeena
             branchCode = userDataModel?.loginbranchcode.toString(),
           //  branchCode = "00000",
             "ALL",
             fromDt = fromDt,
             toDt = toDt,
             manifestType = "O",
             modeType = "A",

         );
     }

    private fun refreshData(){
        inscanList.clear()
        setupRecyclerView()
        lifecycleScope.launch {
            getInscanList()
            delay(1500)
            activityBinding.refreshLayout.isRefreshing=false
        }
    }
    private fun setupRecyclerView() {
        linearLayoutManager = LinearLayoutManager(this)
        rvAdapter = InscanListAdapter(inscanList, this@InscanListActivity)
        activityBinding.recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = rvAdapter
        }
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

    override fun onClick(data: Any, clickType: String) {
        when(clickType) {
            "SCAN_SELECT" -> run {
                val model: InscanListModel = data as InscanListModel


            }
        }
    }
}