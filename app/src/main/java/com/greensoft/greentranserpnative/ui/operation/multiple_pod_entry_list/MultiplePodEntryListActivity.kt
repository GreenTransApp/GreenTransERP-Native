package com.greensoft.greentranserpnative.ui.operation.multiple_pod_entry_list

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityMultiplePodEntryListBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.common.models.CommonBottomSheetModel
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.booking.models.ContentSelectionModel
import com.greensoft.greentranserpnative.ui.operation.multiple_pod_entry_list.adapters.MultiplePodEntryAdapter
import com.greensoft.greentranserpnative.ui.operation.multiple_pod_entry_list.models.RelationListModel
import com.greensoft.greentranserpnative.ui.operation.pending_for_delivery_update_list.models.PodEntryListModel
import javax.inject.Inject

class MultiplePodEntryListActivity  @Inject constructor(): BaseActivity(),
    AlertCallback<Any>, OnRowClick<Any>,
    BottomSheetClick<Any> {
        lateinit var  activityBinding:ActivityMultiplePodEntryListBinding
        private val viewModel:MultiplePodEntryViewModel by viewModels()
        private var rvAdapter :MultiplePodEntryAdapter ?= null
    private var relationList: ArrayList<RelationListModel> = ArrayList()
    private var rvList: ArrayList<PodEntryListModel> = ArrayList()
        private lateinit var manager: LinearLayoutManager
    private var drsSelectedData: PodEntryListModel? = null
   var drNo:String =""
   var relationName:String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityMultiplePodEntryListBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("Save Multiple POD")
        setObserver()
        getRelationList()
        getDrsDetails()
    }


     private fun setObserver(){
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

         viewModel.relationLiveData.observe(this){ List->
             relationList =List


         }
     }
      private fun getDrsDetails(){
          if(intent != null) {
              val jsonString = intent.getStringExtra("ARRAY_JSON")
              if(jsonString != "") {
                  val gson = Gson()
                  val listType = object : TypeToken<List<PodEntryListModel>>() {}.type
                  val resultList: ArrayList<PodEntryListModel> =
                      gson.fromJson(jsonString.toString(), listType)
                  rvList.addAll(resultList)
                  rvList.forEachIndexed { _, element ->
                      drNo=element.drsno.toString()
                      setupRecyclerView()
                  }
              }
          }
    }

    private fun setupRecyclerView() {
        manager = LinearLayoutManager(this)
        rvAdapter = MultiplePodEntryAdapter(rvList, this)
        activityBinding.recyclerView.apply {
            layoutManager = manager
            adapter = rvAdapter
        }
    }

    private  fun getRelationList(){
        viewModel.getRelation(
            companyId = getCompanyId()
        )
    }

    private fun openRelationSelectionBottomSheet(rvList: ArrayList<RelationListModel>, index: Int) {
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for (i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].relations.toString(), rvList[i]))

        }
        openCommonBottomSheet(this, "Relation Selection", this, commonList, true, index)

    }
    override fun onRowClick(data: Any, clickType: String, index: Int) {
        super.onRowClick(data, clickType, index)
        if (clickType == "RELATION_SELECT") {
            openRelationSelectionBottomSheet(relationList, index)

        }

    }


    override fun onItemClickWithAdapter(data: Any, clickType: String, index: Int) {
        super.onItemClickWithAdapter(data, clickType, index)
        if (clickType == "Relation Selection") {
            val selectedRelation=data as RelationListModel
            rvAdapter?.setRelation(selectedRelation, index)
            relationName=selectedRelation.relations.toString()

        }
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