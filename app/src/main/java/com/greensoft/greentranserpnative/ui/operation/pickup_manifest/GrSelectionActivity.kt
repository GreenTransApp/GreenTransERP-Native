package com.greensoft.greentranserpnative.ui.operation.pickup_manifest

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityGrSelectionBinding
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.adapter.GrSelectionAdapter
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.BranchSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.GrSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.ManifestEnteredDataModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class GrSelectionActivity @Inject constructor() : BaseActivity(), OnRowClick<Any>,
    BottomSheetClick<Any> , AlertCallback<Any> {
    lateinit var  activityBinding: ActivityGrSelectionBinding
    lateinit var linearLayoutManager: LinearLayoutManager
    private val viewModel: PickupManifestViewModel by viewModels()
    private var rvAdapter: GrSelectionAdapter? = null
    private var grList: ArrayList<GrSelectionModel> = ArrayList()
    private var selectedGrList: ArrayList<GrSelectionModel> = ArrayList()
    private var branchList: ArrayList<BranchSelectionModel> = ArrayList()
     var grDt=""
    private var model: ManifestEnteredDataModel? = null
    var data=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding=ActivityGrSelectionBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
//        pickupManifestData.forEachIndexed { _, element ->
//
//            Log.d("test", "onCreate:  ${element} ")
//        }

        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("SELECT LOADING SLIP")
        grDt= getSqlCurrentDate()
        setObserver()
        setOnClick()
        searchItem()




//        getGrList()

    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return super.onKeyUp(keyCode, event)
        if ( event!!.isCanceled)
        {
           successToast("data")
            return true
        }
    }

    override fun onResume() {
        super.onResume()

        refreshData()
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
                openSingleDatePicker()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getGrList(){
        viewModel.getGrList(
            loginDataModel?.companyid.toString(),
            "greentransapp_grallocationlistforpickupmanifest",
            listOf("prmbranchcode","prmdt"),
            arrayListOf(userDataModel?.loginbranchcode.toString(),grDt)
//            arrayListOf("321","2023-10-09")
//            "gtapp_loadingallocationlist_pickupmanifest",
//            listOf("prmbranchcode","prmdt","prmusercode","prmmenucode","prmsessionid"),
////            arrayListOf(userDataModel?.loginbranchcode.toString())
//            arrayListOf(userDataModel?.loginbranchcode.toString(),grDt,"GTAPP_NATIVEPICKUPMANIFEST",userDataModel?.sessionid.toString())
        )
    }

    private fun refreshData(){
        getGrList()

    }

     private fun setOnClick(){
         activityBinding.btnContinue.setOnClickListener {

//             Utils.selectedGrList.addAll(grList)
             if(rvAdapter == null) {
                 errorToast(noLoadingGRSelectedErrMsg)
                 return@setOnClickListener
             }
             val selectedItems: ArrayList<GrSelectionModel> = rvAdapter!!.getSelectedItems()
             if (selectedItems.size == 0) {
                 errorToast(noLoadingGRSelectedErrMsg)
                 return@setOnClickListener
             } else {
                 hideProgressDialog()

                 activityBinding.allCheck.setOnCheckedChangeListener{_,isCheck->
                     rvAdapter!!.selectAll(isCheck)
                 }

                 val gson = Gson()
                 val jsonString = gson.toJson(selectedItems)
                 val intent = Intent(this, SavePickupManifestActivity::class.java)
//                     val intent = Intent(this, PickupManifestEntryActivity::class.java)
                 intent.putExtra("ARRAY_JSON", jsonString)
                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                 startActivity(intent)
                 finish()
             }
         }




     }
    private fun setObserver(){
        activityBinding.refreshLayout.setOnRefreshListener {
            refreshData()
            lifecycleScope.launch {
                delay(1500)
                activityBinding.refreshLayout.isRefreshing=false
            }


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



        viewModel.grLiveData.observe(this) { data ->
           grList = data
            setupRecyclerView()


        }
        mPeriod.observe(this) { date ->
            grDt = date.sqlsingleDate.toString()
            getGrList()
//            setupRecyclerView()
//            if(!rvAdapter!!.notCheck)
//             {
//                 errorToast("Please select atleast one loading slip")
//                 return@observe
//             }

        }
    }
    private fun setupRecyclerView() {

        linearLayoutManager = LinearLayoutManager(this)
//        if (rvAdapter == null) {
//            activityBinding.emptyView.visibility= View.GONE
//        }
        if(grList.isEmpty()) {
            activityBinding.emptyView.visibility = View.VISIBLE
        } else {
            activityBinding.emptyView.visibility = View.GONE
        }
        rvAdapter = GrSelectionAdapter(grList, this,this@GrSelectionActivity)
        activityBinding.recyclerView.layoutManager = linearLayoutManager
        activityBinding.recyclerView.adapter = rvAdapter

    }

    override fun onItemClick(data: Any, clickType: String) {

    }
    override fun onClick(data: Any, clickType: String) {
       when (clickType) {
            "CHECK_SELECTED" -> run {

                val model: GrSelectionModel = data as GrSelectionModel
                grList.add(model)


                val gson = Gson()

            }

        }
    }

    override fun onAlertClick(alertClick: AlertClick, alertCallType: String, data: Any?) {
        try {
            when (alertClick) {
                AlertClick.YES -> {
                    if (alertCallType == "RESELECT_SLIP") {
                       refreshData()
                    }
                }
                AlertClick.NO -> {

                }
            }
        } catch (ex: Exception) {
            errorToast(ex.message)
        }
    }


}


