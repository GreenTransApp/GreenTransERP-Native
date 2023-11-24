package com.greensoft.greentranserpnative.ui.operation.grList

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.greensoft.greentranserpnative.ENV
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityGrListBinding
import com.greensoft.greentranserpnative.databinding.BottomSheetPrintStickerConfirmationBinding
import com.greensoft.greentranserpnative.hilt.App
import com.greensoft.greentranserpnative.ui.home.models.UserMenuModel
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.grList.models.GrListModel
import com.greensoft.greentranserpnative.ui.operation.grList.models.PrintStickerModel
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad.ScanLoadActivity
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.summary.SummaryScanLoadActivity
import com.greensoft.greentranserpnative.ui.print.dcCode.activity.SelectBluetoothActivity
import com.greensoft.greentranserpnative.ui.print.printManager.PrintManager
import com.greensoft.greentranserpnative.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.posprinter.TSCConst
import net.posprinter.TSCPrinter
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject


@AndroidEntryPoint
class GrListActivity @Inject constructor() : BaseActivity(), OnRowClick<Any> {
    private lateinit var activityBinding: ActivityGrListBinding
    private lateinit var manager: LinearLayoutManager
    private val viewModel: GrListViewModel by viewModels()
    private var grList: ArrayList<GrListModel> = ArrayList()
    private var printStickerList: ArrayList<PrintStickerModel> = ArrayList()
    private var grListAdapter: GrListAdapter? = null
    private var fromDate: String = getSqlDate()!!
    private var toDate:String = getSqlDate()!!
    private var bookingType: String = "M"
//    private val printer = TSCPrinter(App.get().curConnect)
    var printer: TSCPrinter? = null
    private var userMenuModel: UserMenuModel = UserMenuModel(
        commandstatus = 1,
        commandmessage = "Success",
        outputtype = "GR LIST",
        outputid = 0,
        documenttype = "GR LIST",
        menuname = "GR LIST",
        menucode = "GTAPP_GRLIST",
        displayname = "GR LIST",
        rights = "11111",
        cntr = 0
    );

    private var btLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode === RESULT_OK) {
            val mac: String? =
                result.data?.getStringExtra(SelectBluetoothActivity.INTENT_MAC)
            val name: String? =
                result.data?.getStringExtra(SelectBluetoothActivity.INTENT_BT_NAME)
            if (mac != null && name != null) {
                saveStorageString(ENV.DEFAULT_BLUETOOTH_ADDRESS_PREF_TAG, mac)
                saveStorageString(ENV.DEFAULT_BLUETOOTH_NAME_PREF_TAG, name)
                App.get().connectBt(mac, name)
            } else {
                errorToast("Some Error Occured while trying to connect to bluetooth, please restart the app.")
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityGrListBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        printer = TSCPrinter(App.get().curConnect)
        initUi()
        getDefaultConnection()
//        getGrList()
        setObservers()
        searchItem()

    }


    private fun getDefaultConnection() {
        val mac = getStorageString(ENV.DEFAULT_BLUETOOTH_ADDRESS_PREF_TAG)
        val name = getStorageString(ENV.DEFAULT_BLUETOOTH_NAME_PREF_TAG)
        App.get().connectBt(mac, name)
    }
    override fun onClick(data: Any, clickType: String) {
        val model = data as GrListModel
        if (clickType == "PRINT_STICKER") {
//            printer = TSCPrinter(App.get().curConnect)
            if (isPrinterConnected()) {
                printStickerBottomSheet(model)
            }
        } else if (clickType == "SCAN_STICKER") {
            val i = Intent(this@GrListActivity, ScanLoadActivity::class.java)
            if(model.grno.isNullOrEmpty()) {
                errorToast("GR# not Available or Valid. Please Check.")
                return;
            } else if(model.orgcode.isNullOrEmpty()) {
                errorToast("GR Origin not available, Cannot proceed for Loading.")
                return;
            } else if(model.destcode.isNullOrEmpty()) {
                errorToast("GR Destination not available, Cannot Proceed for Loading.")
                return;
            }
            Utils.grModel = model
            Utils.loadingNo = model.loadingno ?: ""
            startActivity(i)
        }
    }

    private fun getGrList(){
        viewModel.getGrList(
            loginDataModel?.companyid.toString(),
            "gtapp_bookingsearchlist",
            listOf("prmbranchcode","prmfromdt","prmtodt","prmdoctype","prmgrno"),
            arrayListOf(userDataModel?.loginbranchcode.toString(),fromDate,toDate,bookingType,"")
        )
    }

    private fun getStickerForPrint( grNo: String, from: String, to : String){
        viewModel.getStickerForPrint(
            loginDataModel?.companyid.toString(),
            "printsticker",
            listOf("prmgrno","prmfromstickerno","prmtostickerno"),
            arrayListOf(grNo,from,to)
        )
    }

    private fun setObservers(){

        mPeriod.observe(this) { it ->
//            successToast(it.getSqlFromDate());
//            successToast(it.getSqlToDate());
            fromDate = it.sqlFromDate.toString()
            toDate = it.sqlToDate.toString()
            refreshData()
        }
        viewModel.isError.observe(this){ errMsg->
            errorToast(errMsg)
        }
        viewModel.viewDialogLiveData.observe(this, Observer { show ->
//            progressBar.visibility = if(show) View.VISIBLE else View.GONE
            if(show) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })
        viewModel.grListLivedata.observe(this){ resultGrList->
            if (resultGrList != null) {
                grList = resultGrList
                setupRecyclerView()
            }
        }
        viewModel.printStickerLiveData.observe(this){ printStickerResponse->
            printStickerList = printStickerResponse
            if (isPrinterConnected()) {
                PrintManager(tscPrinter = this.printer).printStickers(getUserData()?.compname, printStickerList)
            }
        }

//        activityBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                // filter recycler view when query submitted
//                grListAdapter?.getFilter()?.filter(query)
//                return false
//            }
//
//            override fun onQueryTextChange(query: String): Boolean {
//                // filter recycler view when text is changed
//                grListAdapter?.getFilter()?.filter(query)
//                return false
//            }
//        })
//        activityBinding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//            }
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                activityBinding.swipeRefreshLayout.setEnabled(manager.findFirstCompletelyVisibleItemPosition() == 0)
//            }
//        })
    }

    private fun initUi() {
        mContext = this@GrListActivity
        fromDate = getSqlDate()!!
        toDate = getSqlDate()!!
//        userMenuModel = getMenuData()
        activityBinding.swipeRefreshLayout.setOnRefreshListener(OnRefreshListener {
            refreshData()

        })
//        if (ENV.DEBUGGING) {
//            fromDate = "2023-06-01"
//            bookingType = "M"
//        }
    }
    private fun refreshData() {
        getGrList()
        lifecycleScope.launch {
            delay(1500)
            activityBinding.swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()
        refreshData()
//        userMenuModel = Utils.menuModel
        var menuNameEdited = userMenuModel.menuname
        menuNameEdited += if(bookingType == "M") {
            " ( Manual )"
        } else {
            " ( Computerize )"
        }
        setUpToolbar(menuNameEdited ?: "GR LIST")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.gr_list_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val id = item.itemId
//        if (id == R.id.datePicker) {
//            if (!materialDatePicker?.isAdded!!) {
//                materialDatePicker?.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
//            }
//            return true
//        }
//        return super.onOptionsItemSelected(item)
        return when (item.itemId) {
            R.id.bookig_type_btn->{
               showBookingTypeSelection()
                true
            }
            R.id.datePicker -> {
                openDatePicker()
                true
            }
            R.id.datePicker -> {
                openDatePicker()
                true
            }
            R.id.printer_connection ->{
                    btLauncher.launch(Intent(this,SelectBluetoothActivity::class.java))

                true
            }
//            R.id.manual_booking -> {
////
//                true
//            }
//            R.id.computer_booking ->{
//                Toast.makeText(this, "Com", Toast.LENGTH_SHORT).show()
//                true
//            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun setupRecyclerView() {
        if(grListAdapter == null) {
            manager = LinearLayoutManager(this)
            activityBinding.recyclerView.layoutManager = manager
            grListAdapter = GrListAdapter(grList, this)
        }
        activityBinding.recyclerView.adapter = grListAdapter



    }

    private  fun printStickerBottomSheet(model: GrListModel) {
    val bsDialog = BottomSheetDialog(mContext)
    //        View view = View.inflate(this, R.layout.bottom_sheet_print_sticker_confirmation, null);
    val bottomSheetBinding: BottomSheetPrintStickerConfirmationBinding =
        BottomSheetPrintStickerConfirmationBinding.inflate(
            layoutInflater
        )
    bsDialog.setContentView(bottomSheetBinding.root)
    var fromSticker: EditText
    var toSticker: EditText
    var confirm: Button
    bottomSheetBinding.inputGr.setText(model.grno)
    bottomSheetBinding.inputPckgs.setText(java.lang.String.valueOf(model.pckgs))
    bottomSheetBinding.inputFrom.setText("1")
    bottomSheetBinding.inputTo.setText(java.lang.String.valueOf(model.pckgs))
    bottomSheetBinding.saveBtn.setOnClickListener {
        if (isPrinterConnected()) {
            val from: String =
                java.lang.String.valueOf(bottomSheetBinding.inputFrom.text)
            val to: String = java.lang.String.valueOf(bottomSheetBinding.inputTo.text)
            val fromInt = (if (from == "") "0" else from).toInt()
            val toInt = (if (to == "") "0" else to).toInt()
            if (fromInt < 0) {
                errorToast("[ FROM STICKER ] Cannot be Negative.")
                return@setOnClickListener
            } else if (toInt < 0) {
//                errorToast("[ TO STICKER ] Cannot be Zero or Negative.");
                errorToast("[ TO STICKER ] Cannot be Negative.")
                return@setOnClickListener
            } else if (fromInt > toInt) {
                errorToast("[ FROM STICKER ] Cannot be Greater than [ TO STICKER ].")
                return@setOnClickListener
            }
            printConfirmationAlert(model, from, to)
        }
    }
    bsDialog.show()
}
    private fun isPrinterConnected(): Boolean {
        try {
            val isPrinterConnected = AtomicBoolean(false)
            if (printer != null && App.get().curConnect != null) {
                printer!!.isConnect { isConnected: Int ->
                    if (isConnected == 1) {
                        isPrinterConnected.set(true)
                    }
                }

            }
            if (isPrinterConnected.get()) {
                return true
            } else {
                errorToast("Printer may not be connected. Please check the bluetooth connection.")
            }
        } catch (ex: Exception) {
            errorToast("Could not connect to any bluetooth printer.")
            Log.d("PRINTER EXCEPTION", ex.message.toString())
        }
        return false
    }
    private fun printConfirmationAlert(model: GrListModel, from: String, to: String) {
        val msg = """
            ${
            """
    Are you sure you want to print?
    [   GR   ]:  ${model.grno}
    """.trimIndent()
        }
            [ From ]:  $from
            [    To   ]:  $to
            """.trimIndent()
        val dialog = AlertDialog.Builder(mContext)
            .setTitle("Alert !!!")
            .setMessage(msg)
            .setPositiveButton("Yes") { dialogInterface, i ->
                if (isPrinterConnected()) {
//                    fromSticker = from
//                    toSticker = to
                    getStickerForPrint(model.grno,from,to)
                }
            }
            .setNeutralButton(
                "Cancel"
            ) { dialogInterface: DialogInterface, i: Int -> dialogInterface.cancel() }
        dialog.show()
    }

    private fun showBookingTypeSelection() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_booking_type)
        val manual = bottomSheetDialog.findViewById<MaterialCardView>(R.id.layout_m)
        val computerize = bottomSheetDialog.findViewById<MaterialCardView>(R.id.layout_c)
        manual?.setOnClickListener {
            bookingType = "M"
            changeMenuName()
            getGrList()
            bottomSheetDialog.dismiss()
        }
        computerize?.setOnClickListener {
            bookingType = "C"
            changeMenuName()
            getGrList()
            bottomSheetDialog.dismiss()
//            val i = Intent(this@GrListActivity, GrListActivity::class.java)
//            startActivity(i)
        }
        bottomSheetDialog.show()
    }
    fun changeMenuName() {
        var menuNameEdited = userMenuModel.menuname
        menuNameEdited += if(bookingType == "M") {
            " ( Manual )"
        } else {
            " ( Computerize )"
        }
        setUpToolbar(menuNameEdited)
    }
    fun searchItem(): Boolean {
        activityBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                grListAdapter?.getFilter()?.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                grListAdapter?.getFilter()?.filter(newText)
                return false
            }

        })
        return true
    }


}