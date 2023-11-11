package com.greensoft.greentranserpnative.ui.operation.communicationList

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityCommunicatonListBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.chatScreen.ChatScreenActivity
import com.greensoft.greentranserpnative.ui.operation.chatScreen.models.ChatScreenModel
import com.greensoft.greentranserpnative.ui.operation.communicationList.models.CommunicationListModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CommunicationListActivity @Inject constructor() : BaseActivity(), OnRowClick<Any> {
    private lateinit var activityBinding: ActivityCommunicatonListBinding
    private var communicationListAdapter: CommunicationListAdapter? = null
    private lateinit var manager: LinearLayoutManager
    private var communicatonList: ArrayList<CommunicationListModel> = ArrayList()
    private var communicationCardList: ArrayList<CommunicationListModel> = ArrayList()
    private val viewModel: CommunicatonListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityCommunicatonListBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        communicationCardList = generateSimpleList()
        setupRecyclerView()
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("Communication List"+userDataModel?.custname.toString())
        getCommunicationList()
        setObservers()
    }

    override fun onClick(data: Any, clickType: String) {
        if (clickType == "OPEN_DETAIL") {
            val intent = Intent(mContext, ChatScreenActivity::class.java)
            startActivity(intent)
//            Toast.makeText(mContext, "Card Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setObservers(){
        viewModel.isError.observe(this){ errMsg->
            errorToast(errMsg)
        }

        viewModel.communicationListLiveData.observe(this){ commuListData->
            communicatonList = commuListData
        }
    }
    private fun getCommunicationList(){
        viewModel.getCommunicationList(
            loginDataModel?.companyid.toString(),
            "",
            listOf("prmtransactionid","prmboyid","prmcustcode","prmcomment"),
            arrayListOf("10050","","","")
        )
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
            dataList.add(CommunicationListModel("",1,i+100 ,"",
            "","","","",""))
        }
        return dataList
    }
}