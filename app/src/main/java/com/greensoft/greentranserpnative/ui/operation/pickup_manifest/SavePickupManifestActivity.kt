package com.greensoft.greentranserpnative.ui.operation.pickup_manifest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityGrSelectionBinding
import com.greensoft.greentranserpnative.databinding.ActivitySavePickupManifestBinding
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.adapter.GrSelectionAdapter
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.adapter.SavePickupManifestAdapter
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.models.GrSelectionModel
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.models.SinglePickupRefModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SavePickupManifestActivity @Inject constructor() : BaseActivity(), OnRowClick<Any>,
    BottomSheetClick<Any> {
    lateinit var  activityBinding: ActivitySavePickupManifestBinding
    lateinit var linearLayoutManager: LinearLayoutManager
    private var rvAdapter: SavePickupManifestAdapter? = null
    private var grList: ArrayList<GrSelectionModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding=ActivitySavePickupManifestBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("SAVE PICKUP MANIFEST")
//        setupRecyclerView()
        getIntentData()
    }

    private fun setupRecyclerView() {
        linearLayoutManager = LinearLayoutManager(this)
        if (rvAdapter == null) {
            rvAdapter = SavePickupManifestAdapter(grList, this@SavePickupManifestActivity)
            activityBinding.recyclerView.apply {
                layoutManager = linearLayoutManager
                adapter = rvAdapter
            }
        }


    }
    private fun getIntentData() {
        if(intent != null) {
            val jsonString = intent.getStringExtra("ARRAY_JSON")
            if(jsonString != "") {
                val gson = Gson()
                val listType = object : TypeToken<List<GrSelectionModel>>() {}.type
                val resultList: ArrayList<GrSelectionModel> =
                    gson.fromJson(jsonString.toString(), listType)
                grList.addAll(resultList)
                setupRecyclerView()
            }
        }
    }

    override fun onItemClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }

    override fun onCLick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }
}