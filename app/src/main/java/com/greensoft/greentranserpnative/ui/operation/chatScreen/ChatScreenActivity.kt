package com.greensoft.greentranserpnative.ui.operation.chatScreen

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityChatScreenBinding
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.chatScreen.models.ChatScreenModel
import com.greensoft.greentranserpnative.ui.operation.communicationList.models.CommunicationListModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChatScreenActivity @Inject constructor() : BaseActivity(), OnRowClick<Any>,
    BottomSheetClick<Any>, AlertCallback<Any> {
    private lateinit var activityBinding: ActivityChatScreenBinding
    private var chatList: ArrayList<ChatScreenModel> = ArrayList()
    private lateinit var manager: LinearLayoutManager
    private val viewModel: ChatScreenViewModel by viewModels()
    private var chatScreenAdapter: ChatScreenAdapter? = null
    private var communicationModel: CommunicationListModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityChatScreenBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        getIntentData()
        initUi()
        onClick()
        setObservers()
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun getIntentData() {
        if(intent.extras != null){
            var data = intent.extras!!.getString("communication_data")
            if(data != null) {
                var gson = Gson()
                try {
                    val commListModel = gson.fromJson(data, CommunicationListModel::class.java)
                    communicationModel = commListModel
                }
                catch (ex: Exception) {
                    errorToast(somethingWentWrongErrorMsg)
                }
            }
        }
    }

    fun onClick(){
       activityBinding.sentBtn.setOnClickListener {

           if (activityBinding.messageBox.text.toString().equals("")){
               errorToast("Enter some text")
           }
           else{
               addChat()
           }
//           successToast("Send clicked")
       }
    }

    private fun initUi(){
        setSupportActionBar(activityBinding.toolBar.root)
        if(communicationModel != null) {
            setUpToolbar(communicationModel!!.custname.toString())
        } else {
            setUpToolbar("Chat")
        }

    }
    private fun refreshData() {
        getChats()
    }


//    private fun openCommunicationDetailBottomSheet(rvList: ArrayList<NotificationPanelBottomSheetModel>) {
////        val commonList = ArrayList<NotificationPanelBottomSheetModel>()
////        for (i in 0 until rvList.size) {
////            commonList.add(NotificationPanelBottomSheetModel(rvList[i].notiname, i.toString()))
////
////        }
////        openCounterBottomSheet(this, "Chat Detail", this, commonList)
////    }

    private fun setObservers(){
        viewModel.isError.observe(this){ errMsg->
            errorToast(errMsg)
        }

        activityBinding.refreshLayout.setOnRefreshListener {
            refreshData()
            lifecycleScope.launch {
                delay(1500)
                activityBinding.refreshLayout.isRefreshing = false
            }
        }
//        viewModel.viewDialogLiveData.observe(this, Observer { show ->
////            progressBar.visibility = if(show) View.VISIBLE else View.GONE
//            if(show) {
//                showProgressDialog()
//            } else {
//                hideProgressDialog()
//
//            }
//        })
        viewModel.getChatListLiveData.observe(this){ resultChatList->
            chatList = resultChatList
            setupRecyclerView()
        }

        viewModel.addChatListLiveData.observe(this){
            activityBinding.messageBox.text?.clear()
            getChats()
        }
        viewModel.deleteChatListLiveData.observe(this){

        }
    }
    private fun getChats(){
        if(communicationModel != null) {
            viewModel.getChats(
                loginDataModel?.companyid.toString(),
                "gtnative_getdrivercustomercomments",
                listOf("prmtransactionid","prmisboy"),
                arrayListOf(communicationModel!!.transactionid.toString(), "Y")
            )
        } else {
            errorToast(somethingWentWrongErrorMsg)
        }
    }
    private fun addChat(){
        if(communicationModel != null) {
            viewModel.addChats(
                loginDataModel?.companyid.toString(),
                "gtnative_adddrivercustomercomments",
                listOf("prmtransactionid", "prmboyid", "prmcustcode", "prmcomment"),
                arrayListOf(
                    communicationModel!!.transactionid.toString()
                    ,userDataModel?.executiveid.toString(),
                    "",
                    activityBinding.messageBox.text.toString()
                )
                //            arrayListOf("53057","","",activityBinding.messageBox.text.toString())
            )
        }
        else {
            errorToast(somethingWentWrongErrorMsg)
        }
    }

    private fun deleteChat(chat: ChatScreenModel?){
        if(chat == null || communicationModel == null) {
            errorToast(somethingWentWrongErrorMsg)
            return
        }
        viewModel.deleteChats(
            loginDataModel?.companyid.toString(),
            "gtnative_deletedrivercustomercomments",
            listOf("prmtransactionid","prmdataid","prmdeletedby"),
            arrayListOf(communicationModel!!.transactionid.toString(), chat.dataid.toString(),"D")
        )
    }

    private fun setupRecyclerView() {
        if (chatScreenAdapter == null) {
            manager = LinearLayoutManager(this)
            activityBinding.recyclerview.layoutManager = manager
        }
        chatScreenAdapter = ChatScreenAdapter(chatList, this)
        activityBinding.recyclerview.adapter = chatScreenAdapter
        lifecycleScope.launch {
            delay(200)
            if(chatList.size > 1) {
                activityBinding.recyclerview.smoothScrollToPosition(chatList.size - 1)
//                activityBinding.recyclerview.scrollToPosition(chatList.size - 1)
            }
        }
    }

    override fun onLongClick(data: Any, clickType: String) {
        super.onLongClick(data, clickType)
        if (clickType=="DELETE"){
            try {
                val chatScreenModel: ChatScreenModel = data as ChatScreenModel
                openDeleteAlert(chatScreenModel)
            } catch (ex: Exception) {
                errorToast(ex.message)
            }
        }
    }
    private fun openDeleteAlert(chat: ChatScreenModel?){
//        val dialogFragment = ScanPopup("Do you want to Delete ?")
//        dialogFragment.show(supportFragmentManager, "SCAN_PALLET_POP_UP")
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Do you want to delete ?")
        builder.setTitle("Alert !")
        builder.setCancelable(false)
        builder.setPositiveButton("Yes") {
                dialog, which -> deleteChat(chat)
        }
        builder.setNegativeButton("No") {
                dialog, which -> dialog.cancel()
        }

        val alertDialog = builder.create()
        alertDialog.show()
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