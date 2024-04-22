package com.greensoft.greentranserpnative.ui.operation.multiple_pod_entry_list

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityMultiplePodEntryListBinding
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.multiple_pod_entry_list.adapters.MultiplePodEntryAdapter
import com.greensoft.greentranserpnative.ui.operation.pending_for_delivery_update_list.models.PodEntryListModel
import javax.inject.Inject

class MultiplePodEntryListActivity  @Inject constructor(): BaseActivity(),
    AlertCallback<Any>, OnRowClick<Any>,
    BottomSheetClick<Any> {
        lateinit var  activityBinding:ActivityMultiplePodEntryListBinding
        private val viewModel:MultiplePodEntryViewModel by viewModels()
        private val rvAdapter :MultiplePodEntryAdapter ?= null
        private lateinit var manager: LinearLayoutManager
    private var drsSelectedData: PodEntryListModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityMultiplePodEntryListBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("Save Multiple POD")
        getDrsDetails()
    }

      private fun getDrsDetails(){
          if(intent != null) {
              val jsonString = intent.getStringExtra("ManifestNo")
              if(jsonString != "") {
                  val gson = Gson()
                  val listType = object : TypeToken<PodEntryListModel>() {}.type
                  val resultList: PodEntryListModel =
                      gson.fromJson(jsonString.toString(), listType)
                  drsSelectedData = resultList;
                  if(drsSelectedData != null) {
//                      drsNo = drsSelectedData?.drsno

                  } else {
                      errorToast("Something went wrong, Please try again.")
                      finish()
                  }
              }
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