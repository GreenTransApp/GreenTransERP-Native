package com.greensoft.greentranserpnative.ui.operation.communicationList

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityCommunicatonListBinding
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.chatScreen.ChatScreenActivity
import com.greensoft.greentranserpnative.ui.operation.communicationList.models.CommunicationListModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CommunicationListActivity @Inject constructor() : BaseActivity(), OnRowClick<Any> {
    private lateinit var activityBinding: ActivityCommunicatonListBinding
    private var communicationListAdapter: CommunicationListAdapter? = null
    private lateinit var manager: LinearLayoutManager
    private var communicationList: ArrayList<CommunicationListModel> = ArrayList()
//    private var communicationCardList: ArrayList<CommunicationListModel> = ArrayList()
    private val viewModel: CommunicatonListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityCommunicatonListBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("Customer Communication")
        setupRecyclerView()
        setObservers()
        getCommunicationList()
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun refreshData() {
        getCommunicationList()
    }

    override fun onClick(data: Any, clickType: String) {
        val gson = Gson()
        if (clickType == "OPEN_CHAT") {
            val intent = Intent(mContext, ChatScreenActivity::class.java)
            var parsedData = data as CommunicationListModel
            intent.putExtra("communication_data", gson.toJson(parsedData))
            startActivity(intent)
//            Toast.makeText(mContext, "Card Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setObservers(){
        activityBinding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
            lifecycleScope.launch {
                delay(1500)
                activityBinding.swipeRefreshLayout.isRefreshing = false
            }
        }
        viewModel.isError.observe(this){ errMsg->
            errorToast(errMsg)
        }

        viewModel.communicationListLiveData.observe(this){ commuListData->
            communicationList = commuListData
            setupRecyclerView()
        }
        viewModel.viewDialogLiveData.observe(this) { show ->

            if (show) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }

    }
    private fun getCommunicationList(){
        viewModel.getCommunicationList(
            loginDataModel?.companyid.toString(),
            "gtapp_getjobforchat",
            listOf("prmusercode","prmsessionid"),
            arrayListOf(userDataModel?.usercode.toString(), userDataModel?.sessionid.toString())
        )
    }

    private fun setupRecyclerView() {
        if (communicationListAdapter == null) {
            manager = LinearLayoutManager(this)
            activityBinding.recyclerView.layoutManager = manager
        }
        communicationListAdapter = CommunicationListAdapter(communicationList, this)
        activityBinding.recyclerView.adapter = communicationListAdapter
//    }
    }

}