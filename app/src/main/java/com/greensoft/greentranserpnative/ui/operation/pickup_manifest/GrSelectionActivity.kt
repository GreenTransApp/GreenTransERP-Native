package com.greensoft.greentranserpnative.ui.operation.pickup_manifest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityGrSelectionBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.common.models.CommonBottomSheetModel
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.common.alert.CommonAlert
import com.greensoft.greentranserpnative.ui.home.models.UserMenuModel
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.booking.BookingActivity
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad.models.StickerModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.adapter.GrSelectionAdapter
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.BranchSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.GrSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.ManifestEnteredDataModel
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.PickupReferenceViewModel
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.models.PickupRefModel
import com.greensoft.greentranserpnative.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.zip.Inflater
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
        setUpToolbar("SELECT GR")
        grDt= getSqlCurrentDate()
        setObserver()
        setOnClick()
        searchItem()




//        getGrList()

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
            "greentranswebsg_grallocationlistforpickupmanifest",
            listOf("prmbranchcode","prmdt"),
//            arrayListOf(userDataModel?.loginbranchcode.toString())
            arrayListOf("321","2023-10-09")
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

                 if (grList.size != 0) {
//                 selectedGrList.addAll(grList)
                     hideProgressDialog()

                     activityBinding.allCheck.setOnCheckedChangeListener{_,isCheck->
                         if(!isCheck){
                             rvAdapter!!.selectAll(isCheck)
                         }else{
                             rvAdapter!!.selectAll(isCheck)
                         }
                     }
                     val gson = Gson()
                     val jsonString = gson.toJson(selectedGrList)
                     if(!rvAdapter!!.notCheck)
                     {
                         errorToast("Please select atleast one loading slip")
                         return@setOnClickListener
                     }

                     val intent = Intent(this, SavePickupManifestActivity::class.java)
                     intent.putExtra("ARRAY_JSON", jsonString)
                     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                     startActivity(intent)
                     finish()
                     finishAffinity()
                 } else {
                     errorToast(mContext, "DATA NOT FOUND")
                 }
//             }
         }




     }
    private fun setObserver(){
        activityBinding.refreshLayout.setOnRefreshListener {
            refreshData()
            lifecycleScope.launch {
                delay(1500)
                activityBinding.refreshLayout.isRefreshing=false
            }
        false}
        viewModel.isError.observe(this) { errMsg ->
            errorToast(errMsg)
        }
//        viewModel.viewDialogLiveData.observe(this, Observer { show ->
//            if(show) {
//                showProgressDialog()
//            } else {
//                hideProgressDialog()
//            }
//        })



        viewModel.grLiveData.observe(this) { data ->

            grList = data

//            setupRecyclerView()

        }
        mPeriod.observe(this) { date ->
            grDt = date.sqlsingleDate.toString()
            setupRecyclerView()

        }
    }
    private fun setupRecyclerView() {

        linearLayoutManager = LinearLayoutManager(this)
        if (rvAdapter == null) {
            activityBinding.emptyView.visibility= View.GONE
            rvAdapter = GrSelectionAdapter(grList, this,this@GrSelectionActivity)
            activityBinding.recyclerView.apply {
                layoutManager = linearLayoutManager
                adapter = rvAdapter
            }
        }
    }

    override fun onItemClick(data: Any, clickType: String) {

    }
    override fun onClick(data: Any, clickType: String) {
       when (clickType) {
            "CHECK_SELECTED" -> run {
                val model: GrSelectionModel = data as GrSelectionModel
                selectedGrList.add(model)
                val gson = Gson()
//                successToast("show")
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


