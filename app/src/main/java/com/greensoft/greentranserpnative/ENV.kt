package com.greensoft.greentranserpnative

import android.bluetooth.BluetoothDevice

class ENV {
    companion object {
        const val SCANNER_NOT_WORKING_MSG: String = "Scanner not available. All functionality may not work."
        private val PREF_TAG = "GreenTransERPNative"
        val LOGIN_PREF_TAG = "loginPrefs_${PREF_TAG}"
        val IS_LOGIN_PREF_TAG = "isLogin_${PREF_TAG}"
        val LOGIN_DATA_PREF_TAG = "loginData_${PREF_TAG}"
        val USER_DATA_PREF_TAG = "userData_${PREF_TAG}"
        val SAVE_LOGIN_PREF_TAG = "saveLogin_${PREF_TAG}"
        val LOGIN_USERNAME_PREF_TAG = "userName_${PREF_TAG}"
        val LOGIN_PASSWORD_PREF_TAG = "passWord_${PREF_TAG}"
        val DEFAULT_BLUETOOTH_NAME_PREF_TAG = "DEFAULT_BLUETOOTH_NAME_${PREF_TAG}"
        val DEFAULT_BLUETOOTH_ADDRESS_PREF_TAG = "DEFAULT_BLUETOOTH_ADDRESS_${PREF_TAG}"
        val IMAGE_PREF_SHARE = "image_prefs${PREF_TAG}"

        val DEBUGGING: Boolean = false
        val LOCAL_API: Boolean = false

        val APP_NAME = "GreenTransERPNative"
//       const val DEBUG_USERNAME: String = "ADMIN@GREENTRANS"
//       const val DEBUG_PASSWORD: String = "GREEN@#321"
        const val DEBUG_USERNAME: String = "TESTING@JEENA"
        const val DEBUG_PASSWORD: String = "12345678"
//       const val DEBUG_USERNAME: String = "DELHIPICKUPBOY@GREENTRANS"
//       const val DEBUG_PASSWORD: String = "123456"
//        const val DEBUG_USERNAME: String = "PANKAJ@JEENA"
//        const val DEBUG_PASSWORD: String = "PANKAJ@123"

        val APP_VERSION = "0.0.1"
        val APP_VERSION_DATE = "2023-08-17"
        val DEVICE_ID = ""
//        val LIVE_BASE_URL: String = "https://greentrans.in:444/API/Printer/";
        private const val LIVE_BASE_URL: String = "https://greentrans.in:444/API/";
        private const val DEBUG_BASE_URL: String = "http://192.168.1.252:45455/API/";
//        private const val DEBUG_BASE_URL: String = "http://192.168.1.226:45459/API/";
        const val EWAY_BILL_LOGIN_URL: String = "https://api.easywaybill.in/ezewb/v1/auth/initlogin"
        const val EWAY_BILL_COMPLETE_LOGIN_URL: String = "https://api.easywaybill.in/ezewb/v1/auth/completelogin"
        const val EWAY_BILL_DETAIL_URL: String = "https://api.easywaybill.in/ezewb/v1/ewb/data"




        fun getBaseUrl(): String {
            if (DEBUGGING && LOCAL_API) {
                return DEBUG_BASE_URL;
            }
            return LIVE_BASE_URL;
        }
    }
}