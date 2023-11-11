package com.greensoft.greentranserpnative.ui.operation.chatScreen

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityChatScreenBinding
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.common.scanPopup.ScanPopup
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.chatScreen.models.ChatScreenModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatScreenActivity @Inject constructor() : BaseActivity(), OnRowClick<Any>,
    BottomSheetClick<Any>, AlertCallback<Any> {
    private lateinit var activityBinding: ActivityChatScreenBinding
    private var getChatList: ArrayList<ChatScreenModel> = ArrayList()
    private lateinit var manager: LinearLayoutManager
    private val viewModel: ChatScreenViewModel by viewModels()
    private var chatScreenAdapter: ChatScreenAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityChatScreenBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("Communication Details")
        onClick()
        getChats()
        setObservers()
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

    private fun setObservers(){
        viewModel.isError.observe(this){ errMsg->
            errorToast(errMsg)
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
            getChatList = resultChatList
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
        viewModel.getChats(
            loginDataModel?.companyid.toString(),
//            "10",
                "gtnative_getdrivercustomercomments",
            listOf("prmtransactionid"),
            arrayListOf("10050")
//            arrayListOf("53057")
        )
    }
    private fun addChat(){
        viewModel.addChats(
            loginDataModel?.companyid.toString(),
//            "10",
            "gtnative_adddrivercustomercomments",
            listOf("prmtransactionid","prmboyid","prmcustcode","prmcomment"),
            arrayListOf("10050",userDataModel?.executiveid.toString(),"",activityBinding.messageBox.text.toString())
//            arrayListOf("53057","","",activityBinding.messageBox.text.toString())
        )
    }

    private fun deleteChat(chat: ChatScreenModel?){
        if(chat == null) {
            errorToast("Something went wrong, Please try again.")
            return
        }
        viewModel.deleteChats(
            loginDataModel?.companyid.toString(),
//            "10",
            "gtnative_deletedrivercustomercomments",
            listOf("prmtransactionid","prmdataid","prmdeletedby"),
            arrayListOf("10050", chat.dataid.toString(),"")
//            arrayListOf("53057","","",activityBinding.messageBox.text.toString())
        )
    }

    private fun setupRecyclerView() {
        if (chatScreenAdapter == null) {
            manager = LinearLayoutManager(this)
            activityBinding.recyclerview.layoutManager = manager
        }
        chatScreenAdapter = ChatScreenAdapter(getChatList, this)
        activityBinding.recyclerview.adapter = chatScreenAdapter
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