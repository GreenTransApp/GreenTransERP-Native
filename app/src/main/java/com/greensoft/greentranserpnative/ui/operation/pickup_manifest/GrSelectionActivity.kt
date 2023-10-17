package com.greensoft.greentranserpnative.ui.operation.pickup_manifest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityGrSelectionBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.common.models.CommonBottomSheetModel
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.booking.BookingActivity
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.adapter.GrSelectionAdapter
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.BranchSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.GrSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.PickupReferenceViewModel
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.models.PickupRefModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.zip.Inflater
import javax.inject.Inject

@AndroidEntryPoint
class GrSelectionActivity @Inject constructor() : BaseActivity(), OnRowClick<Any>,
    BottomSheetClick<Any> {
    lateinit var  activityBinding: ActivityGrSelectionBinding
    lateinit var linearLayoutManager: LinearLayoutManager
    private val viewModel: PickupManifestViewModel by viewModels()
    private var rvAdapter: GrSelectionAdapter? = null
    private var grList: ArrayList<GrSelectionModel> = ArrayList()
    private var selectedGrList: ArrayList<GrSelectionModel> = ArrayList()
    private var branchList: ArrayList<BranchSelectionModel> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding=ActivityGrSelectionBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("SELECT GR")
        setObserver()
        setOnClick()

//        getGrList()
    }

    override fun onResume() {
        super.onResume()
        refreshData()

    }
    private fun getGrList(){
        viewModel.getGrList(
            loginDataModel?.companyid.toString(),
            "greentranswebsg_grallocationlistforpickupmanifest",
            listOf("prmbranchcode","prmdt"),
//            arrayListOf(userDataModel?.loginbranchcode.toString())
            arrayListOf("321","2023-10-09")
        )
    }




    private fun refreshData(){
        getGrList()

    }

     private fun setOnClick(){
         activityBinding.btnContinue.setOnClickListener {
             if(grList.size !=0){
//                 selectedGrList.addAll(grList)
                 hideProgressDialog()
                 val gson = Gson()
                 val jsonString = gson.toJson(selectedGrList)
                 val intent = Intent(this,SavePickupManifestActivity ::class.java)
                 intent.putExtra("ARRAY_JSON", jsonString)
                 startActivity(intent)
//
             }
             else{
                 successToast(mContext,"data not send")
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
            setupRecyclerView()

        }
    }
    private fun setupRecyclerView() {
        linearLayoutManager = LinearLayoutManager(this)
        if (rvAdapter == null) {
            rvAdapter = GrSelectionAdapter(grList, this@GrSelectionActivity)
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
                successToast("show")


            }

        }
    }
}


