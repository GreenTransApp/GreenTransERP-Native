package com.greensoft.greentranserpnative.ui.operation.outstation_unarrived

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityOutstationInscanListBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_with_scanner.InScanDetailWithScannerActivity
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.InScanDetailsActivity
import com.greensoft.greentranserpnative.ui.operation.outstation_inscan_detail_with_scanner.OutstationInscanDetailWithScannerActivity
import com.greensoft.greentranserpnative.ui.operation.outstation_inscan_detail_without_scanner.OutstationInscanDetailWithoutScannerActivity
import com.greensoft.greentranserpnative.ui.operation.unarrived.InscanListAdapter
import com.greensoft.greentranserpnative.ui.operation.unarrived.InscanListViewModel
import com.greensoft.greentranserpnative.ui.operation.unarrived.models.InscanListModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import net.posprinter.utils.DataForSendToPrinterTSC.delay
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class OutstationInscanListActivity@Inject constructor(): BaseActivity(), OnRowClick<Any> {
    lateinit var  activityBinding:ActivityOutstationInscanListBinding
    private val viewModel: OutstationInscanListViewModel by viewModels()
    private var inscanList: ArrayList<InscanListModel> = ArrayList()
    private var rvAdapter: OutstationInscanListAdapter? = null
    var fromDt= getSqlCurrentDate()
    var toDt= getSqlCurrentDate()

    lateinit var linearLayoutManager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding= ActivityOutstationInscanListBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("Outstation In-Scan List")
        setObserver()
        searchItem()
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
        rvAdapter = OutstationInscanListAdapter(inscanList, this)
        activityBinding.recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = rvAdapter
        }
    }

    private fun getInscanList(){
        viewModel.getInscanList(
            companyId = getCompanyId(),
//           companyId = "17846899",  // need to change for  jeena
            userCode = getUserCode(),
            branchCode = getLoginBranchCode(),
            sessionId = getSessionId(),
            "ALL",
            //fromDt = fromDt,
            fromDt = "2023-11-01",
            //toDt = toDt,
            toDt = "2024-04-01",
            manifestType = "O",
            modeType = "A",

            );
    }
    override fun onClick(data: Any, clickType: String) {
        if (clickType== "SCAN_SELECT"){
            val gson = Gson()
            try {
                var data = data as InscanListModel
                val jsonString = gson.toJson(data)
                val intent = Intent(this, OutstationInscanDetailWithScannerActivity::class.java)
                intent.putExtra("ManifestNo", jsonString)
                startActivity(intent)
            } catch (ex: Exception) {
                errorToast("Data Conversion Error: " + ex.message)
            }

        }
        else if (clickType=="WITHOUT_SCAN"){
            val gson = Gson()
            try {
                var data = data as InscanListModel
                val jsonString = gson.toJson(data)
                val intent = Intent(this, OutstationInscanDetailWithoutScannerActivity::class.java)
                intent.putExtra("ManifestNo", jsonString)
                startActivity(intent)
            } catch (ex: Exception){
                errorToast("Data Conversion Error: " + ex.message)
            }
        }
    }
}