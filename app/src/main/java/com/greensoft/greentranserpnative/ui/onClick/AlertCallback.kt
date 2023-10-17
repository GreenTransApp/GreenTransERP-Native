package com.greensoft.greentranserpnative.ui.onClick

import com.greensoft.greentranserpnative.ui.common.alert.AlertClick

interface AlertCallback<T> {
    fun onAlertClick(alertClick: AlertClick, alertCallType: String, data: T?)
}