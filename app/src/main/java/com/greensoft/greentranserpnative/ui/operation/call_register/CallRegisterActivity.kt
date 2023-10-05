package com.greensoft.greentranserpnative.ui.operation.call_register

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityCallRegisterBinding
import com.greensoft.greentranserpnative.ui.operation.call_register.models.CallRegisterModel
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.PickupReferenceActivity
import com.greensoft.greentranserpnative.ui.home.models.UserMenuModel
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CallRegisterActivity @Inject constructor() : BaseActivity(), OnRowClick<Any>, BottomSheetClick<Any> {
    private var rvAdapter: CallRegisterAdapter? = null
    private val viewModel: CallRegisterViewModel by viewModels()
    private var callRegisterList: ArrayList<CallRegisterModel> = ArrayList()
    private lateinit var toolbar: Toolbar
    private lateinit var activityBinding: ActivityCallRegisterBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var menuModel: UserMenuModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityCallRegisterBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar as Toolbar)
        setUpToolbar("CALL REGISTER")
        menuModel = getMenuData()

        setObserver()
        setOnClick()
        searchItem()
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }
      fun refreshData(){
          getCallRegisterList()
      }
    private fun getCallRegisterList() {
        viewModel.getCallRegisterList(
            loginDataModel?.companyid.toString(),
            "gtapp_getpendingjobs",
            listOf("prmbranchcode", "prmusercode", "prmmenucode", "prmsessionid"),
            arrayListOf(userDataModel?.loginbranchcode.toString(), userDataModel?.usercode.toString(), menuModel?.menucode.toString(), userDataModel?.sessionid.toString())
        )
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

    private fun setObserver() {
        activityBinding.refreshLayout.setOnRefreshListener {
            refreshData()
            lifecycleScope.launch {
                delay(1500)
                activityBinding.refreshLayout.isRefreshing=false
            }
        }
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
        viewModel.callRegisterLiveData.observe(this) { callRegister ->
            callRegisterList = callRegister
            setupRecyclerView()
//            searchItem()
        }
    }

    private fun setOnClick() {

    }

    private fun acceptJobs() {
        AlertDialog.Builder(this)
            .setTitle("Alert!!!")
            .setMessage("Are you sure you want to accept this job")
            .setPositiveButton("Yes") { _, _ ->
                var intent = Intent(this, PickupReferenceActivity::class.java)
                startActivity(intent)
            }
            .setNeutralButton("No") { _, _ -> }
            .show()
    }

    private fun setupRecyclerView() {
        linearLayoutManager = LinearLayoutManager(this)
        if (rvAdapter == null)
            rvAdapter = CallRegisterAdapter(callRegisterList, this@CallRegisterActivity)
        activityBinding.recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = rvAdapter
        }


    }

    override fun onItemClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }

    override fun onCLick(data: Any, clickType: String) {
        when (clickType) {
            "ACCEPT_SELECT" -> run {
//                val model: CallRegisterModel = data as CallRegisterModel
//                val intent = Intent(this, PickupReferenceActivity::class.java)
//                startActivity(intent)
                acceptJobs()
            }

            "REJECT_SELECT" -> run {
                successToast("reject btn click")
            }
        }
    }
}