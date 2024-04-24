package com.greensoft.greentranserpnative.ui.operation.scan_and_delivery

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityScanAndUndeliveredBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.signBottomSheet.SignatureBottomSheetCompleteListener
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.adapter.ScanUndeliveryAdapter
import com.greensoft.greentranserpnative.ui.operation.scan_and_delivery.models.ScanStickerModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ScanAndUndeliveredActivity @Inject constructor() : BaseActivity(), OnRowClick<Any>,
    BottomSheetClick<Any>, AlertCallback<Any>, SignatureBottomSheetCompleteListener {
        lateinit var activityBinding:ActivityScanAndUndeliveredBinding
        lateinit var linearLayoutManager: LinearLayoutManager
        private var rvAdapter: ScanUndeliveryAdapter? = null
        private var rvList: ArrayList<ScanStickerModel> = ArrayList()
        
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityScanAndUndeliveredBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
//        setSupportActionBar(activityBinding.toolBar.root)
//        setUpToolbar("UNDELIVERED STICKER")
        getStickerData()
        searchItem()

    }

    private fun getStickerData() {

        if(intent != null) {
            val jsonString = intent.getStringExtra("ARRAY_JSON")
            if(jsonString != "") {
                val gson = Gson()
                val listType = object : TypeToken<List<ScanStickerModel>>() {}.type
                val resultList: ArrayList<ScanStickerModel> =
                    gson.fromJson(jsonString.toString(), listType)
                rvList.addAll(resultList)
                setupRecyclerView()

            }
        }
    }


    private fun setupRecyclerView() {
        if(rvList.isEmpty()) {
            activityBinding.emptyView.visibility = View.VISIBLE
        } else {
            activityBinding.emptyView.visibility = View.GONE
        }
        linearLayoutManager = LinearLayoutManager(this)
//        rvAdapter = ScanUndeliveryAdapter(rvList, this,this, un)
        activityBinding.recyclerView.layoutManager = linearLayoutManager
        activityBinding.recyclerView.adapter = rvAdapter

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


    private fun saveUndeliveredSticker(){
        var actualStickerNoStr: String = ""
        var actualReasonStr: String = ""
        var adapterData: ArrayList<ScanStickerModel>? = rvAdapter?.getAdapterData()
//        run enteredData@ {
//            adapterData?.forEachIndexed { index, model ->
//            }

            for(i in 0 until rvList.size) {
                val stickerNoStr=rvList[i].stickerno
                val reasonsStr=adapterData?.get(i)?.reasons
                actualStickerNoStr += "$stickerNoStr,"
                actualReasonStr += "$reasonsStr,"
            }

        }

    override fun onComplete(clickType: String, imageBitmap: Bitmap) {
        TODO("Not yet implemented")
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