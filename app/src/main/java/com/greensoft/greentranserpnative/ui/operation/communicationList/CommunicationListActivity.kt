package com.greensoft.greentranserpnative.ui.operation.communicationList

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityCommunicatonListBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.chatScreen.ChatScreenActivity
import com.greensoft.greentranserpnative.ui.operation.communicationList.models.CommunicationListModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CommunicationListActivity @Inject constructor() : BaseActivity(), OnRowClick<Any> {
    private lateinit var activityBinding: ActivityCommunicatonListBinding
    private var communicationListAdapter: CommunicationListAdapter? = null
    private lateinit var manager: LinearLayoutManager
    private var communicationCardList: ArrayList<CommunicationListModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityCommunicatonListBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        communicationCardList = generateSimpleList()
        setupRecyclerView()
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("Communication List")
    }

    override fun onClick(data: Any, clickType: String) {
        if (clickType == "OPEN_DETAIL") {
            val intent = Intent(mContext, ChatScreenActivity::class.java)
            startActivity(intent)
//            Toast.makeText(mContext, "Card Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        if (communicationListAdapter == null) {
        manager = LinearLayoutManager(this)
        activityBinding.recyclerView.layoutManager = manager
        communicationListAdapter = CommunicationListAdapter(communicationCardList, this)
        activityBinding.recyclerView.adapter = communicationListAdapter
    }
    }

    private fun generateSimpleList(): ArrayList<CommunicationListModel> {
        val dataList: ArrayList<CommunicationListModel> =
            java.util.ArrayList<CommunicationListModel>()
        for (i in 0..99) {
            dataList.add(CommunicationListModel(i.toString(),"test","X","Y" ))
        }
        return dataList
    }
}