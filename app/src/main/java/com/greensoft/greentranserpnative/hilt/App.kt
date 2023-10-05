package com.greensoft.greentranserpnative.hilt

import android.app.Application
import android.util.Log
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.api.Common
import com.greensoft.greentranserpnative.ui.print.dcCode.utils.UIUtils
import com.jeremyliao.liveeventbus.LiveEventBus

import dagger.hilt.android.HiltAndroidApp
import net.posprinter.IDeviceConnection
import net.posprinter.IPOSListener
import net.posprinter.POSConnect


@HiltAndroidApp
class App: Application(){
    private val connectListener = IPOSListener { code, msg ->
        when (code) {
            POSConnect.CONNECT_SUCCESS -> {
                UIUtils.toast(R.string.con_success)
                Log.d("CONNECT_SUCCESS", R.string.connection_success.toString())
                LiveEventBus.get<Boolean>(Common.EVENT_CONNECT_STATUS).post(true)
            }
            POSConnect.CONNECT_FAIL -> {
                UIUtils.toast(R.string.con_failed)
                Log.d("NOT_CONNECTED", R.string.con_failed.toString())
                BT_NAME = "Bluetooth Not Connected."
                LiveEventBus.get<Boolean>(Common.EVENT_CONNECT_STATUS).post(false)
            }
            POSConnect.CONNECT_INTERRUPT -> {
                UIUtils.toast(R.string.con_has_disconnect)
                Log.d("NOT_CONNECTED", R.string.connection_success.toString())
                BT_NAME = "Bluetooth Not Connected."
                LiveEventBus.get<Boolean>(Common.EVENT_CONNECT_STATUS).post(false)
            }
            POSConnect.SEND_FAIL -> {
                UIUtils.toast(R.string.send_failed)
                Log.d("NOT_CONNECTED", R.string.connection_success.toString())
                BT_NAME = "Bluetooth Not Connected."
            }
            POSConnect.USB_DETACHED -> {
                UIUtils.toast(R.string.usb_detached)
                Log.d("NOT_CONNECTED", R.string.usb_detached.toString())
            }
            POSConnect.USB_ATTACHED -> {
                UIUtils.toast(R.string.usb_attached)
                Log.d("CONNECT_SUCCESS", R.string.usb_attached.toString())

            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        POSConnect.init(this)
    }

    var curConnect: IDeviceConnection? = null

    fun connectUSB(pathName: String) {
        curConnect?.close()
        curConnect = POSConnect.createDevice(POSConnect.DEVICE_TYPE_USB)
        curConnect!!.connect(pathName, connectListener)
    }

    fun connectNet(ipAddress: String) {
        curConnect?.close()
        curConnect = POSConnect.createDevice(POSConnect.DEVICE_TYPE_ETHERNET)
        curConnect!!.connect(ipAddress, connectListener)
    }

    fun connectBt(macAddress: String, bluetoothName: String) {
        curConnect?.close()
        BT_NAME = bluetoothName
        curConnect = POSConnect.createDevice(POSConnect.DEVICE_TYPE_BLUETOOTH)
        curConnect!!.connect(macAddress, connectListener)
    }
    fun connectMAC(macAddress: String) {
        curConnect?.close()
        curConnect = POSConnect.connectMac(macAddress, connectListener)
    }

    fun connectSerial(port: String, boudrate: String) {
        curConnect?.close()
        curConnect = POSConnect.createDevice(POSConnect.DEVICE_TYPE_SERIAL)
        curConnect!!.connect("$port,$boudrate", connectListener)
    }

    companion object {
        private lateinit var app: App
        var BT_NAME: String = "Bluetooth Not Connected."

        fun get(): App {
            return app
        }
    }
}