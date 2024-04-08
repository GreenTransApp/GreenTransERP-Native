package com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityInscanDetailsBinding
import com.greensoft.greentranserpnative.ui.common.alert.AlertClick
import com.greensoft.greentranserpnative.ui.common.alert.CommonAlert
import com.greensoft.greentranserpnative.ui.home.models.UserMenuModel
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.inscan_detail_without_scanner.model.InScanWithoutScannerModel
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.models.PickupRefModel
import com.greensoft.greentranserpnative.ui.operation.unarrived.models.InscanListModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InScanDetailsActivity  @Inject constructor(): BaseActivity(), OnRowClick<Any>, AlertCallback<Any> {
    private lateinit var activityBinding: ActivityInscanDetailsBinding
    private lateinit var manager: LinearLayoutManager
    private val viewModel: InScanDetailsViewModel by viewModels()
    private var inScanCardAdapterList:InScanDetailsAdapter? = null
    private var inScanCardDetailList:ArrayList<InScanWithoutScannerModel> = ArrayList()
    private  var inScanDetailData: InScanWithoutScannerModel? = null
    private var inscanListData: ArrayList<InscanListModel> = ArrayList()
    private var manifestNo:String? =""
    private var mawb:String? =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityInscanDetailsBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("Inscan Details Without Scanner")
        getInscanData()
        setObservers()
        getInScanDetails()
        setOnClick()

    }

    private fun getInscanData() {
        if(intent != null) {
            val jsonString = intent.getStringExtra("ManifestNo")
            if(jsonString != "") {
                val gson = Gson()
                val listType = object : TypeToken<List<InscanListModel>>() {}.type
                val resultList: ArrayList<InscanListModel> =
                    gson.fromJson(jsonString.toString(), listType)
                inscanListData.addAll(resultList)
                inscanListData.forEachIndexed { _, element ->
                    manifestNo= element.manifestno.toString()
                    mawb=element.mawbno.toString()

                }
            }
        }
    }

    private fun setObservers(){
        viewModel.inScanDetailLiveData.observe(this){ inScanData->
            inScanDetailData = inScanData
            setInScanData()
        }
        viewModel.inScanCardLiveData.observe(this){ inScanCardList->
            inScanCardDetailList = inScanCardList
            setupRecyclerView()
        }
    }
//    private fun setInScanData() {
//
//        activityBinding.inputMawb.text = inScanDetailData?.mawbno.toString()
//        activityBinding.inputManifest.text = inScanDetailData?.manifestno
//        activityBinding.inputAirline.text = inScanDetailData?.airline.toString()
//        activityBinding.inputVehicle.text = inScanDetailData?.modename
//        activityBinding.inputDispatchOn.text = inScanDetailData?.manifestdt.toString()
//        activityBinding.inputFromStation.text = inScanDetailData?.origin
//        activityBinding.inputToStation.text = inScanDetailData?.tostation
//    }

    private fun setInScanData() {
        activityBinding.inputMawb.text = inScanDetailData?.mawbno?: "No data available"
        activityBinding.inputManifest.text = inScanDetailData?.manifestno ?: "No data available"
//        activityBinding.inputAirline.text = inScanDetailData?.airline?.toString() ?: "No data available"
        activityBinding.inputVehicle.text = inScanDetailData?.modename ?: "No data available"
        activityBinding.inputDispatchOn.text = inScanDetailData?.manifestdt?: "No data available"
        activityBinding.inputFromStation.text = inScanDetailData?.origin ?: "No data available"
        activityBinding.inputToStation.text = inScanDetailData?.tostation ?: "No data available"
    }


    private fun setOnClick(){

        activityBinding.layoutCardDetail.setOnClickListener {
            toggleCardVisibility()
        }
        activityBinding.cardExpendBtn.setOnClickListener {
            toggleCardVisibility()
        }


    }

    private fun getInScanDetails(){
        viewModel.getInScanDetails(
            getCompanyId(),
//            "17846899",
            userDataModel?.usercode.toString(),
            getLoginBranchCode(),
            manifestNo!!,
            "I",
            getSessionId()
        )
    }

    private fun setupRecyclerView() {
        if (inScanCardAdapterList == null) {
            manager = LinearLayoutManager(this)
            activityBinding.rvCardDetails.layoutManager = manager
        }
        inScanCardAdapterList = InScanDetailsAdapter(inScanCardDetailList, this)
        activityBinding.rvCardDetails.adapter = inScanCardAdapterList
//    }
    }

   private fun toggleCardVisibility() {

       val currentVisibility = activityBinding.linearLayoutInsideCard.visibility
       if (currentVisibility == View.VISIBLE) {

           activityBinding.linearLayoutInsideCard.visibility = View.GONE
           activityBinding.cardExpendBtn.setImageResource(R.drawable.outline_arrow_circle_down_24)
       } else {

           activityBinding.linearLayoutInsideCard.visibility = View.VISIBLE
           activityBinding.cardExpendBtn.setImageResource(R.drawable.outline_arrow_circle_up_24)
       }
    }

    private  fun showAlertOnSave(){
        CommonAlert.createAlert(
            context = this,
            header = "Alert!!",
            description = " Are You Sure You Want To Save InScan Details  ?",
            callback =this,
            alertCallType ="SELECT_SAVE",
            data = ""
        )
    }


    override fun onClick(data: Any, clickType: String) {
        if (clickType == "SAVE_CARD") {
            Toast.makeText(mContext, "Save Button Clicked", Toast.LENGTH_SHORT).show()
            showAlertOnSave()

        }
    }

    override fun onAlertClick(alertClick: AlertClick, alertCallType: String, data: Any?) {
        if(alertCallType =="SELECT_SAVE"){
            successToast("test")
            saveInscanDetailWithoutScan()

        }
    }

 private fun saveInscanDetailWithoutScan(){

     viewModel.saveInScanDetailsWithoutScan(
         companyId =getCompanyId(),
         manifestNo = manifestNo.toString(),
         mawbNo = mawb.toString(),
         branchCode =getLoginBranchCode() ,
         receiveDt = "",
         receiveTime = "",
         vehicleCode = inScanDetailData?.modecode.toString(),
         remarks = inScanDetailData?.remarks.toString(),
         grNo = inScanDetailData?.grno.toString(),
         mfPckgs = inScanDetailData?.despatchpckgs.toString(),
         pckgs = "",
         weight = inScanDetailData?.despatchweight.toString(),
         damagePckgs =inScanDetailData?.damage.toString(),
         damageReasoncode = inScanDetailData?.damagereason.toString(),
         detailRemarks = "",
         userCode = userDataModel?.usercode.toString(),
         menuCode = "",
         sessionId = getSessionId(),
         fromstnCode = inScanDetailData?.orgcode.toString(),

         )
 }
}





