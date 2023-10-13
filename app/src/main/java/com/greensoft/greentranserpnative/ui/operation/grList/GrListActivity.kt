package com.greensoft.greentranserpnative.ui.operation.grList

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
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
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.greensoft.greentranserpnative.ENV
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityGrListBinding
import com.greensoft.greentranserpnative.databinding.BottomSheetPrintStickerConfirmationBinding
import com.greensoft.greentranserpnative.hilt.App
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.grList.models.GrListModel
import com.greensoft.greentranserpnative.ui.operation.grList.models.PrintStickerModel
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.scanLoad.ScanLoadActivity
import com.greensoft.greentranserpnative.ui.print.dcCode.activity.SelectBluetoothActivity
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
    private var printer: TSCPrinter? = null

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
        (if(getMenuData() == null) getMenuData()?.displayname else "GR LIST ")?.let { menuName ->
            var menuNameEdited = menuName
            if(bookingType == "M") {
                menuNameEdited += " ( Manual )"
            } else {
                menuNameEdited += " ( Computerize )"
            }
            setUpToolbar(menuNameEdited)
        }
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
    override fun onCLick(data: Any, clickType: String) {
        val model = data as GrListModel
        if (clickType == "PRINT_STICKER") {
            printer = TSCPrinter(App.get().curConnect)
            if (isPrinterConnected()) {
                printStickerBottomSheet(model)
            }
        } else if (clickType == "SCAN_STICKER") {
            val intent = Intent(this@GrListActivity, ScanLoadActivity::class.java)
            Utils.grNo = model.grno
            startActivity(intent)
        }
    }

    private fun getGrList(){
        viewModel.getGrList(
            loginDataModel?.companyid.toString(),
            "greentrans_bookingsearchlist",
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
                printStickers(getUserData()?.compname, printStickerList)
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
    bsDialog.setContentView(bottomSheetBinding.getRoot())
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
        val isPrinterConnected = AtomicBoolean(false)
        if (printer != null && App.get().curConnect!= null) {

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

    fun printStickers(compName: String?, stickerList: java.util.ArrayList<PrintStickerModel>) {
        val defaultStartX = 20
        val defaultStartY = 20
        val defaultBarHeight = 2
        val defaultBarWidth = 580
        val defaultBarAndVarGap = 15
        val compNameX = defaultStartX + 10
        val barAfterCompX = defaultStartX - 10
        val barAfterCompY = defaultStartY + 30
        val barCodeX = defaultStartX + 10
        val barCodeY = defaultStartY + 45
        val barCodeHeight = 120
        val barAfterBarCodeX = defaultStartX - 10
        val barAfterBarCodeY = barCodeHeight + 105
        val grY = barAfterBarCodeY + 15
        val dtX = 300
        val barAfterGrX = defaultStartX - 10
        val barAfterGrY = grY + defaultBarAndVarGap + 10
        val originY = barAfterGrY + defaultBarAndVarGap
        val barAfterOrgX = defaultStartX - 10
        val barAfterOrgY = originY + defaultBarAndVarGap + 10
        val destY = barAfterOrgY + defaultBarAndVarGap
        val barAfterDestX = defaultStartX - 10
        val barAfterDestY = destY + defaultBarAndVarGap + 10
        val pckgsY = barAfterDestY + defaultBarAndVarGap
        val weightX = 300
        for (i in stickerList.indices) {
            val model = stickerList[i]
            printer!!.sizeMm(120.0, 60.0)
                .gapInch(0.0, 0.0)
                .offsetInch(0.0)
                .speed(5.0)
                .density(10)
                .direction(TSCConst.DIRECTION_FORWARD)
                .reference(5, 2)
                .cls()
                .box(4, 6, 568, 390, 3) //                .box(16, 16, 540, 380, 3)
                .text(
                    compNameX,
                    defaultStartY,
                    TSCConst.FNT_16_24,
                    TSCConst.ROTATION_0,
                    1,
                    1,
                    compName
                )
                .bar(barAfterCompX, barAfterCompY, defaultBarWidth, defaultBarHeight)
                .barcode(
                    barCodeX,
                    barCodeY,
                    TSCConst.CODE_TYPE_128,
                    barCodeHeight,
                    TSCConst.ALIGNMENT_CENTER,
                    TSCConst.ROTATION_0,
                    3,
                    3,
                    model.stickerno
                )
                .bar(barAfterBarCodeX, barAfterBarCodeY, defaultBarWidth, defaultBarHeight)
                .text(
                    defaultStartX,
                    grY,
                    TSCConst.FNT_16_24,
                    TSCConst.ROTATION_0,
                    1,
                    1,
                    "GR | " + model.grno
                )
                .text(
                    dtX,
                    grY,
                    TSCConst.FNT_16_24,
                    TSCConst.ROTATION_0,
                    1,
                    1,
                    "DT | " + model.grdt
                )
                .bar(barAfterGrX, barAfterGrY, defaultBarWidth, defaultBarHeight)
                .text(
                    defaultStartX,
                    originY,
                    TSCConst.FNT_16_24,
                    TSCConst.ROTATION_0,
                    1,
                    1,
                    "ORG   | " + model.originname
                )
                .bar(barAfterOrgX, barAfterOrgY, defaultBarWidth, defaultBarHeight)
                .text(
                    defaultStartX,
                    destY,
                    TSCConst.FNT_16_24,
                    TSCConst.ROTATION_0,
                    1,
                    1,
                    "DEST  | " + model.destinationname
                )
                .bar(barAfterDestX, barAfterDestY, defaultBarWidth, defaultBarHeight)
                .text(
                    defaultStartX,
                    pckgsY,
                    TSCConst.FNT_16_24,
                    TSCConst.ROTATION_0,
                    1,
                    1,
                    "PCKGS | " + model.packageid + "/" + model.pckgs
                )
                .text(
                    weightX,
                    pckgsY,
                    TSCConst.FNT_16_24,
                    TSCConst.ROTATION_0,
                    1,
                    1,
                    "WEIGHT | " + model.weight
                ) //                .qrcode(265, 30, TSCConst.EC_LEVEL_H, 4, TSCConst.QRCODE_MODE_MANUAL, TSCConst.ROTATION_0, "test qrcode")
                //                .text(200, 144, TSCConst.FNT_16_24, TSCConst.ROTATION_0, 1, 1, "Test EN")
                //                .text(38, 165, TSCConst.FNT_16_24, TSCConst.ROTATION_0, 1, 2, "HELLO")
                //                .bar(200, 183, 166, 30)
                //                .bar(334, 145, 30, 30)
                .print(1)
        }
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
        var menuNameEdited = getMenuData()!!.menuname
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