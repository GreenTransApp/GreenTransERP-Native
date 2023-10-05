package com.logixperts.printapp.printer.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.databinding.ActivityFirstBinding
import com.greensoft.greentranserpnative.hilt.App
import com.greensoft.greentranserpnative.ui.print.dcCode.activity.CpclActivity
import com.greensoft.greentranserpnative.ui.print.dcCode.activity.PosActivity
import com.greensoft.greentranserpnative.ui.print.dcCode.activity.SelectBluetoothActivity
import com.greensoft.greentranserpnative.ui.print.dcCode.activity.SelectNetActivity
import com.greensoft.greentranserpnative.ui.print.dcCode.activity.TscActivity
import com.greensoft.greentranserpnative.ui.print.dcCode.activity.ZplActivity
import com.greensoft.greentranserpnative.ui.print.dcCode.utils.Constant
import com.greensoft.greentranserpnative.ui.print.dcCode.utils.UIUtils
import com.greensoft.greentranserpnative.ui.print.dcCode.widget.DlgUsbSelect
import com.jeremyliao.liveeventbus.LiveEventBus
import net.posprinter.POSConnect

class MainActivity : AppCompatActivity() {
    private lateinit var activityBinding: ActivityFirstBinding
    private var pos = 0
    private val launcher = registerForActivityResult(StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val mac = result.data?.getStringExtra(SelectBluetoothActivity.INTENT_MAC)
            App.BT_NAME = result.data?.getStringExtra(SelectBluetoothActivity.INTENT_BT_NAME).toString()
            activityBinding.addressTv.text = mac
        }
    }
    private val netLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode === RESULT_OK && result.data != null) {
            activityBinding.addressEt.setText(result.data!!.getStringExtra(SelectNetActivity.MAC_ADDRESS))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        initListener()
        val entries = POSConnect.getSerialPort()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, entries)
        activityBinding.serialPortNs.adapter = adapter
        LiveEventBus.get<Boolean>(Constant.EVENT_CONNECT_STATUS).observeForever {
            refreshButton(it)
        }
    }

    private fun initListener() {
        activityBinding.buttonConnect.setOnClickListener {
            refreshButton(false)
            when (pos) {
                0 -> connectUSB()
                1 -> connectNet()
                2 -> connectBt()
                3 -> {
                    if (activityBinding.serialPortNs.selectedItem == null) {
                        UIUtils.toast(R.string.cannot_find_serial)
                    } else {
                        connectSerial(activityBinding.serialPortNs.selectedItem.toString(), activityBinding.baudRateNs.selectedItem.toString())
                    }
                }
                4 ->{
                    connectMAC()
                }
            }
        }
        activityBinding.buttonDisconnect.setOnClickListener {
            refreshButton(false)
            App.get().curConnect?.close()
        }
        activityBinding.buttonpos.setOnClickListener {
            val intent = Intent(this, PosActivity::class.java)
            startActivity(intent)
        }
        activityBinding.buttonTsc.setOnClickListener {
            val intent = Intent(this, TscActivity::class.java)
            startActivity(intent)
        }
        activityBinding.cpclBtn.setOnClickListener {
            startActivity(Intent(this, CpclActivity::class.java))
        }
        activityBinding.zplBtn.setOnClickListener {
            startActivity(Intent(this, ZplActivity::class.java))
        }
        activityBinding.addressTv.setOnClickListener {
            when (pos) {
                2 -> launcher.launch(Intent(this, SelectBluetoothActivity::class.java))
                0 -> DlgUsbSelect(this, POSConnect.getUsbDevices(this)) { s ->
                    activityBinding.addressTv.text = s
                }.show()
            }
        }
        activityBinding.searchIv.setOnClickListener { view -> netLauncher.launch(Intent(this, SelectNetActivity::class.java)) }
        activityBinding.portSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, i: Int, l: Long) {
                pos = i
                activityBinding.baudRateNs.visibility = if (i == 3) View.VISIBLE else View.GONE
                activityBinding.serialPortNs.visibility = if (i == 3) View.VISIBLE else View.GONE
                activityBinding.addressEt.visibility = if (i == 1 || i == 4) View.VISIBLE else View.GONE
                activityBinding.addressTv.visibility = if (i == 2 || i == 0) View.VISIBLE else View.GONE
                activityBinding.searchIv.visibility = if (i == 4) View.VISIBLE else View.GONE
                if (i == 0) {
                    searchUsb()
                } else if (i == 1) {
                    activityBinding.addressEt.setText("192.168.1.10")
                } else if (i == 4) {
                    activityBinding.addressEt.setText("")
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    private fun refreshButton(connect: Boolean) {
        activityBinding.buttonDisconnect.isEnabled = connect
        activityBinding.buttonpos.isEnabled = connect
        activityBinding.buttonTsc.isEnabled = connect
        activityBinding.cpclBtn.isEnabled = connect
        activityBinding.zplBtn.isEnabled = connect
    }

    //net connection
    private fun connectNet() {
        val ipAddress = activityBinding.addressEt.text.toString()
        if (ipAddress == "") {
            UIUtils.toast(R.string.none_ipaddress)
        } else {
            App.get().connectNet(ipAddress)
        }
    }

    //USB connection
    private fun connectUSB() {
        val usbAddress = activityBinding.addressTv.text.toString()
        if (usbAddress == "") {
            UIUtils.toast(R.string.usb_select)
        } else {
            App.get().connectUSB(usbAddress)
        }
    }

    // MAC connection. Only supports receipt printers with network port
    private fun connectMAC(){
        val macAddress = activityBinding.addressEt.text.toString()
        if (macAddress == "") {
            UIUtils.toast(R.string.none_mac_address)
        } else {
            App.get().connectMAC(macAddress)
        }
    }

    //bluetooth connection
    private fun connectBt() {
        val bleAddress = activityBinding.addressTv.text.toString()
        if (bleAddress == "") {
            UIUtils.toast(R.string.bt_select)
        } else {
            App.get().connectBt(bleAddress, "")
        }
    }

    private fun connectSerial(port: String, boudrate: String) {
        App.get().connectSerial(port, boudrate)
    }

    private fun searchUsb(): String {
        val usbNames = POSConnect.getUsbDevices(this)
        var ret = ""
        if (usbNames.isNotEmpty()) {
            ret = usbNames[0]
        }
        activityBinding.addressTv.text = ret
        return ret
    }

    override fun onDestroy() {
        super.onDestroy()
        App.get().curConnect?.close()
    }
}