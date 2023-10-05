package com.greensoft.greentranserpnative.ui.operation.grList

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityGrListBinding
import com.greensoft.greentranserpnative.databinding.BottomSheetPrintStickerConfirmationBinding
import com.greensoft.greentranserpnative.hilt.App
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.grList.models.GrListModel
import com.greensoft.greentranserpnative.ui.operation.grList.models.PrintStickerModel
import dagger.hilt.android.AndroidEntryPoint
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
    private var fromSticker: String? = null
    private  var toSticker:String? = null
    private lateinit var fromDate: String
    private lateinit var toDate:String
//    private val printer = TSCPrinter(App.get().curConnect)
    private var printer: TSCPrinter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityGrListBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        printer = TSCPrinter(App.get().curConnect)
        getGrList()
        setObservers()
    }

    override fun onCLick(data: Any, clickType: String) {
        if (clickType == "BUTTON_CLICK") {
            if (isPrinterConnected()) {
                val model = data as GrListModel
                printStickerBottomSheet(model)
            }
        }
    }

    private fun getGrList(){
        viewModel.getGrList(
            "10",
            "greentrans_bookingsearchlist",
            listOf("prmbranchcode","prmfromdt","prmtodt","prmdoctype","prmgrno"),
            arrayListOf("00000","2023-09-14","2023-09-28","M","")
        )
    }

    private fun getStickerForPrint(){
        viewModel.getStickerForPrint(
            "10",
            "printsticker",
            listOf("prmgrno","prmfromstickerno","prmtostickerno"),
            arrayListOf("70224753","1","1")
        )
    }

    private fun setObservers(){

        mPeriod.observe(this) { it ->
//            successToast(it.getSqlFromDate());
//            successToast(it.getSqlToDate());
            fromDate = it.sqlFromDate.toString()
            toDate = it.sqlToDate.toString()
//            refreshData()
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

    }

//    private fun refreshData() {
//        if (fromDate == null || toDate == null) {
//            fromDate = getSqlDate()!!
//            toDate = getSqlDate()!!
//        }
////        getGrList(fromDate, toDate)
//    }

//    override fun onResume() {
//        super.onResume()
//        refreshData()
//    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.main_menu, menu)
//        return true
//    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val id = item.itemId
//        if (id == R.id.datePicker) {
//            if (!materialDatePicker?.isAdded!!) {
//                materialDatePicker?.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
//            }
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }


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
                    fromSticker = from
                    toSticker = to
                    getStickerForPrint()
                }
//                fromSticker = from
//                toSticker = to
//                getStickerForPrint()
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
//    fun getPrintSticker(grNo: String?, from: String?, to: String?) {
//        if (isNetworkConnected() && userDataModel != null) {
//            viewModel.getStickerForPrint(
//                java.lang.String.valueOf(userDataModel?.companyid),
//                grNo!!,
//                from,
//                to
//            )
//        } else {
//            errorToast(getInternetError())
//        }
//    }
}