package com.greensoft.greentranserpnative.ui.bottomsheet.printGR

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.greensoft.greentranserpnative.ENV
import com.greensoft.greentranserpnative.base.BaseFragment
import com.greensoft.greentranserpnative.databinding.BottomsheetPrintGrBinding
import com.greensoft.greentranserpnative.hilt.App
import com.greensoft.greentranserpnative.ui.bottomsheet.printGR.model.GrDetailForPrintModel
import com.greensoft.greentranserpnative.ui.operation.grList.models.GrListModel
import com.greensoft.greentranserpnative.ui.print.dcCode.activity.SelectBluetoothActivity
import net.posprinter.TSCPrinter
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class PrintGrBottomSheet @Inject constructor(): BaseFragment() {
    private lateinit var layoutBinding:BottomsheetPrintGrBinding
    private val viewModel:PrintGrViewModel by viewModels()
    private var printGrDetail: GrDetailForPrintModel? = null
    private var title: String = "Print GR Stickers"
    private lateinit var grNo: String
    var printer: TSCPrinter? = null


    companion object{
        const val TAG = "PrintGrBottomSheet"
        const val PRINT_GR_LOV = "PRINT_GR_LOV"
        fun newInstance(
            mContext: Context,
            title: String,
            grNo: String
        ): PrintGrBottomSheet {
            val instance = PrintGrBottomSheet()
            instance.mContext = mContext
            instance.title = title
            instance.grNo = grNo
            return instance

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutBinding = BottomsheetPrintGrBinding.inflate(layoutInflater)
        return layoutBinding.root
//        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutBinding.toolbarTitle.text = title
        dialog!!.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet: FrameLayout =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
//                Utils.Log("COMMON_BOTTOM_SHEET", "Opened");
            BottomSheetBehavior.from(bottomSheet).state =
                BottomSheetBehavior.STATE_EXPANDED
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO;
            layoutBinding.extraSpace.minimumHeight =
                Resources.getSystem().displayMetrics.heightPixels / 2

            behavior.isDraggable = false

        }
        close()
        getGrDetailForPrint(grNo)
        setObservers()
    }


    private var btLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode === AppCompatActivity.RESULT_OK) {
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

    private fun setObservers(){
        viewModel.grDetailPrintLiveData.observe(this){grDataForPrint->
            printGrDetail = grDataForPrint
            setupUiData()
        }
    }

    private fun getGrDetailForPrint(grNo: String){
        viewModel.getGrDetailForPrint(
            getCompanyId(),
            getUserCode(),
            getLoginBranchCode(),
            getSessionId(),
            grNo
        )
    }

    private fun setupUiData(){
        layoutBinding.inputGr.setText(printGrDetail?.grno)?:"Some thing went wrong"
        layoutBinding.inputPckgs.setText(printGrDetail?.pckgs.toString())
        layoutBinding.inputFrom.setText("1")
        layoutBinding.inputTo.setText(printGrDetail?.pckgs.toString())

        layoutBinding.btConnectBtn.setOnClickListener {
            val intent = Intent(requireContext(), SelectBluetoothActivity::class.java)
            btLauncher.launch(intent)
        }

        layoutBinding.saveBtn.setOnClickListener {
//            if (isPrinterConnected()) {
                val from: String =
                    java.lang.String.valueOf(layoutBinding.inputFrom.text)
                val to: String = java.lang.String.valueOf(layoutBinding.inputTo.text)
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
            printGrDetail?.let { it1 -> printConfirmationAlert(it1, from, to) }
//            }
        }
    }

    private fun printConfirmationAlert(model: GrDetailForPrintModel, from: String, to: String) {
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
//                if (isPrinterConnected()) {
//                    fromSticker = from
//                    toSticker = to
                    getStickerForPrint(model.grno,from,to)
//                }
            }
            .setNeutralButton(
                "Cancel"
            ) { dialogInterface: DialogInterface, i: Int -> dialogInterface.cancel() }
        dialog.show()
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

    private fun getStickerForPrint(grNo: String,from: String,to: String){
        viewModel.getStickerForPrint(
            getCompanyId(),
            grNo,
            from,
            to
        )
    }

    private fun close(){
        layoutBinding.closeBottomSheet.setOnClickListener {
            dismiss()
        }
    }
}