package com.greensoft.greentranserpnative.ui.operation.loadingSlip.search_list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivitySearchListBinding
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.newScanLoad.NewScanAndLoadActivity
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.search_list.models.SearchListModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.PickupManifestViewModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.adapter.SavePickupManifestAdapter
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.BranchSelectionModel
import com.greensoft.greentranserpnative.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class SearchListActivity @Inject constructor(): BaseActivity(), OnRowClick<Any>,
    AlertCallback<Any> {
    lateinit var  activityBinding:ActivitySearchListBinding
    private val viewModel: SearchListViewModel by viewModels()
    lateinit var linearLayoutManager: LinearLayoutManager
    private var rvAdapter: SearchListAdapter? = null
    private var searchList: ArrayList<SearchListModel> = ArrayList()

     var fromDt=""
     var toDt=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding=ActivitySearchListBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("Search List")
        setObserver()

    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun refreshData(){
        getSearchList()
        lifecycleScope.launch {
            delay(1500)
            activityBinding.pullDownToRefresh.isRefreshing = false
        }
    }

    private fun setObserver(){
        activityBinding.pullDownToRefresh.setOnRefreshListener {
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
          viewModel.searchListLiveData.observe(this){data->
              searchList=data
              setupRecyclerView()
          }

          mPeriod.observe(this) { date ->
              fromDt = date.sqlFromDate.toString()
              toDt = date.sqlToDate.toString()
              getSearchList()
          }

      }
      fun setOnClick(){

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



    private fun getSearchList() {
        viewModel.getSearchList(
            loginDataModel?.companyid.toString(),
            "gtapp_scanandload_searchlist",
            listOf("prmbranchcode","prmfromdt","prmtodt"),
            arrayListOf(userDataModel!!.loginbranchcode.toString(),fromDt,toDt)
        )
    }
    private fun setupRecyclerView() {
        linearLayoutManager = LinearLayoutManager(this)
        rvAdapter = SearchListAdapter(this,searchList, this@SearchListActivity)
        activityBinding.recyclerView.layoutManager = linearLayoutManager
        activityBinding.recyclerView.adapter = rvAdapter


    }
    override fun onAlertClick(alertClick: AlertClick, alertCallType: String, data: Any?) {
        TODO("Not yet implemented")
    }

    override fun onClick(data: Any, clickType: String) {
        when(clickType) {
            "EDIT_CLICKED" -> {
                val model = data as SearchListModel
                Utils.loadingNo = data.loadingno
                finish()
//                val intent = Intent(this@SearchListActivity, NewScanAndLoadActivity::class.java);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                startActivity(intent)
            }
        }
    }

}