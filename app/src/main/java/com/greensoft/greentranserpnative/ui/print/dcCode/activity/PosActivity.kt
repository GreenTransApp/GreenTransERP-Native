package com.greensoft.greentranserpnative.ui.print.dcCode.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.databinding.ActivityPosBinding
import com.greensoft.greentranserpnative.hilt.App
import com.greensoft.greentranserpnative.ui.print.dcCode.utils.UIUtils
import com.greensoft.greentranserpnative.ui.print.dcCode.widget.ModifyBtDlg
import com.greensoft.greentranserpnative.ui.print.dcCode.widget.ModifyNetDlg
import com.greensoft.greentranserpnative.ui.print.dcCode.widget.ModifyWifiDlg
import net.posprinter.POSConst
import net.posprinter.POSPrinter
import net.posprinter.model.PTable
import net.posprinter.utils.StringUtils

class PosActivity : AppCompatActivity() {
    private val printer = POSPrinter(App.get().curConnect)
    private lateinit var activityBinding: ActivityPosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityPosBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        initListener()
    }

    private fun initListener() {
        activityBinding.btText.setOnClickListener {
            printText()
        }
        activityBinding.btbarcode.setOnClickListener {
            printBarcode()
        }
        activityBinding.btpic.setOnClickListener { printPIC() }
        activityBinding.qrcode.setOnClickListener { printQRCode() }
        activityBinding.checklink.setOnClickListener {
            val str = if (App.get().curConnect!!.isConnect) {
                "Connect"
            } else {
                "Disconnect"
            }
            UIUtils.toast(str)
        }

        activityBinding.printerStatusBtn.setOnClickListener {
            printer.printerStatus {
                val msg = when (it) {
                    POSConst.STS_NORMAL -> getString(R.string.printer_normal)
                    POSConst.STS_COVEROPEN -> getString(R.string.printer_front_cover_open)
                    POSConst.STS_PAPEREMPTY -> getString(R.string.printer_out_of_paper)
                    else -> "UNKNOWN"
                }
                val str = getString(R.string.printer_status)
                UIUtils.toast("$str:${msg}")
            }
        }
        activityBinding.openDrawer.setOnClickListener {
            printer.openCashBox(POSConst.PIN_TWO)
        }
        activityBinding.tableBtn.setOnClickListener {
            val table = PTable(arrayOf("Item", "QTY", "Price", "Total"), arrayOf(13, 10, 10, 11))
                .addRow("100328", "1","7.99","7.99")
                .addRow("680015", "4","0.99","3.96")
                .addRow("102501102501102501", "1","43.99","43.99")
                .addRow("021048", "1","4.99","4.99")
            printer.printTable(table)
                .feedLine(2)
                .cutHalfAndFeed(1)
        }
        activityBinding.wifiConfigBtn.setOnClickListener {
            ModifyWifiDlg(this, printer).show()
        }
        activityBinding.setNetBtn.setOnClickListener {
            ModifyNetDlg(this, printer).show()
        }
        activityBinding.setBleBtn.setOnClickListener {
            ModifyBtDlg(this, printer).show()
        }
        activityBinding.querySerialNumberBtn.setOnClickListener {
            printer.getSerialNumber {
                UIUtils.toast("SN:${StringUtils.bytes2String(it)}")
            }
        }
        activityBinding.setIpViaUdpBtn.setOnClickListener {
            val intent = Intent(this, SelectNetActivity::class.java)
            intent.putExtra("data", SelectNetActivity.OPT_MODIFY)
            startActivity(intent)
        }
    }

    private fun printText() {
        val str = "Welcome to the printer,this is print test content!\n"
        printer.printString(str)
            .printText("printText Demo\n", POSConst.ALIGNMENT_CENTER, POSConst.FNT_BOLD or POSConst.FNT_UNDERLINE, POSConst.TXT_1WIDTH or POSConst.TXT_2HEIGHT)
            .cutHalfAndFeed(1)
    }

    private fun printBarcode() {
        printer.initializePrinter()
            .printString("UPC-A\n")
            .printBarCode("123456789012", POSConst.BCS_UPCA)
            .printString("UPC-E\n")
            .printBarCode("042100005264", POSConst.BCS_UPCE, 2, 70, POSConst.ALIGNMENT_LEFT)//425261
            .printString("JAN8\n")
            .printBarCode("12345678", POSConst.BCS_JAN8, 2, 70, POSConst.ALIGNMENT_CENTER)
            .printString("JAN13\n")
            .printBarCode("123456791234", POSConst.BCS_JAN13, 2, 70, POSConst.ALIGNMENT_RIGHT)
            .printString("CODE39\n")
            .printBarCode("ABCDEFGHI", POSConst.BCS_Code39, 2, 70, POSConst.ALIGNMENT_CENTER, POSConst.HRI_TEXT_BOTH)
            .printString("ITF\n")
            .printBarCode("123456789012", POSConst.BCS_ITF, 70)
            .printString("CODABAR\n")
            .printBarCode("A37859B", POSConst.BCS_Codabar, 70)
            .printString("CODE93\n")
            .printBarCode("123456789", POSConst.BCS_Code93, 70)
            .printString("CODE128\n")
            .printBarCode("{BNo.123456", POSConst.BCS_Code128, 2,70, POSConst.ALIGNMENT_LEFT)
            .feedLine()
            .cutHalfAndFeed(1)
    }

    private fun printQRCode() {
        val content = "Welcome to Printer Technology to create advantages Quality to win in the future"
        printer.initializePrinter()
            .printQRCode(content)
            .feedLine()
            .cutHalfAndFeed(1)
    }

    private fun printPIC() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK) {
            try {
                val imagePath = data!!.data
                val resolver = contentResolver
                val b = MediaStore.Images.Media.getBitmap(resolver, imagePath)
                printPicCode(b)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //let the printer print bitmap
    private fun printPicCode(printBmp: Bitmap) {
        printer.printBitmap(printBmp, POSConst.ALIGNMENT_CENTER, 384)
            .feedLine()
            .cutHalfAndFeed(1)
    }
}