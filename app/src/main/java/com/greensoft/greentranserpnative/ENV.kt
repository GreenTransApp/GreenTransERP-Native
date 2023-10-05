package com.greensoft.greentranserpnative

import android.bluetooth.BluetoothDevice

class ENV {
    companion object {
        private val PREF_TAG = "GreenTransERPNative"
        val LOGIN_PREF_TAG = "loginPrefs_${PREF_TAG}"
        val IS_LOGIN_PREF_TAG = "isLogin_${PREF_TAG}"
        val LOGIN_DATA_PREF_TAG = "loginData_${PREF_TAG}"
        val USER_DATA_PREF_TAG = "userData_${PREF_TAG}"
        val SAVE_LOGIN_PREF_TAG = "saveLogin_${PREF_TAG}"
        val LOGIN_USERNAME_PREF_TAG = "userName_${PREF_TAG}"
        val LOGIN_PASSWORD_PREF_TAG = "passWord_${PREF_TAG}"


        val DEBUGGING: Boolean = true
        val LOCAL_API: Boolean = false

        val APP_NAME = "GreenTransERPNative"
//        val DEBUG_USERNAME: String = "ADMIN@GREENTRANS"
//        val DEBUG_PASSWORD: String = "GREEN@#321"
        val DEBUG_USERNAME: String = "ADMIN@JEENA"
        val DEBUG_PASSWORD: String = "CRITICARE@123"

        val APP_VERSION = "0.0.1"
        val APP_VERSION_DATE = "2023-08-17"
        val DEVICE_ID = ""
//        val LIVE_BASE_URL: String = "https://greentrans.in:444/API/Printer/";
        val LIVE_BASE_URL: String = "https://greentrans.in:444/API/";
        val DEBUG_BASE_URL: String = "http://192.168.1.252:45455/API/Printer/";


        fun getBaseUrl(): String {
            if (DEBUGGING && LOCAL_API) {
                return DEBUG_BASE_URL;
            }
            return LIVE_BASE_URL;
        }
    }
}