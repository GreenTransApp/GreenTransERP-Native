package com.greensoft.greentranserpnative.ui.operation.pickup_reference

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityPickupReferenceBinding
import com.greensoft.greentranserpnative.ui.operation.booking.BookingActivity
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.models.PickupRefModel
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.models.SinglePickupRefModel
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class PickupReferenceActivity  @Inject constructor(): BaseActivity(), OnRowClick<Any> {
    lateinit var activityBinding:ActivityPickupReferenceBinding
    private val viewModel: PickupReferenceViewModel by viewModels()
    lateinit var linearLayoutManager: LinearLayoutManager
    private var rvAdapter: PickupReferenceAdapter? = null
//    private var recyclerViewList : List<PickupRefModel> = ArrayList()
    private var pickupRefList: ArrayList<PickupRefModel> = ArrayList()
    private var singleRefList: ArrayList<SinglePickupRefModel> = ArrayList()
    var transactionId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding= ActivityPickupReferenceBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("PICK UP REFERENCE")
        setObserver()
//        loadBookingList()
//        getSingleRefData()
        searchItem()

    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }
    private fun refreshData(){
        getPickupRefData()
     }
  private fun setObserver(){
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
          if(show) {
              showProgressDialog()
          } else {
              hideProgressDialog()
          }
      })

      viewModel.pickupRefLiveData.observe(this) { pickupRefData ->
          pickupRefList = pickupRefData
          setupRecyclerView()
//          pickupRefList.forEachIndexed {index, element ->
//             transactionId=pickupRefList.elementAt(index).transactionid.toString()
//          }
//           for ((i) in pickupRefList.withIndex()){
//             transactionId=pickupRefList.elementAt(i).transactionid.toString()
//          }

//          val randomIndex = Random.nextInt(pickupRefList.size);
//          val pickedData = pickupRefList[randomIndex]
//            transactionId=pickedData.transactionid.toString()
      }
      viewModel.singleRefLiveData.observe(this) { singleRefData ->
//          hideProgressDialog()
          singleRefList = singleRefData
          if(singleRefList.size !=0){
            hideProgressDialog()
            val gson = Gson()
            val jsonString = gson.toJson(singleRefList)
            val intent = Intent(this, BookingActivity::class.java)
            intent.putExtra("ARRAY_JSON", jsonString)
            startActivity(intent)
//
          }
          else{
//            showProgressDialog()
              successToast(mContext,"data not send")
          }


      }

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


    private fun getPickupRefData(){
        viewModel.getPickupRefList(
            loginDataModel?.companyid.toString(),
            "logix_weblovjobforbooking_v2",
            listOf("prmbranchcode"),
            arrayListOf(userDataModel?.loginbranchcode.toString())
//            arrayListOf(userDataModel?.loginbranchcode.toString())
        )
    }

    private fun getSingleRefData(transationId:Int){
        viewModel.getSingleRefData(
            loginDataModel?.companyid.toString(),
            "greentransweb_jeenabooking_getpickupforbooking",
            listOf("prmtransactionid"),
            arrayListOf(transactionId.toString())
        )
    }

    private fun loadBookingList() {
//        for (i in recyclerViewList.indices) {
//
//        }
//        recyclerViewList = listOf(
////            PickupRefModel("","".toIntOrNull(),"","","","","","","".toInt())
//        )
    }

    private fun setupRecyclerView() {
          linearLayoutManager = LinearLayoutManager(this)
        if (rvAdapter == null) {
            rvAdapter = PickupReferenceAdapter(pickupRefList, this@PickupReferenceActivity)
            activityBinding.recyclerView.apply {
                layoutManager = linearLayoutManager
                adapter = rvAdapter
            }
        }
    }

    override fun onCLick(data: Any, clickType: String) {
        when(clickType) {
            "REF_SELECT" -> run {
                val model: PickupRefModel = data as PickupRefModel
                transactionId=model.transactionid.toString()
                getSingleRefData(model.transactionid)

//                successToast(model.custname)
            }
        }
    }

}