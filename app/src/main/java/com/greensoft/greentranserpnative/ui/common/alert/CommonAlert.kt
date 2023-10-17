package com.greensoft.greentranserpnative.ui.common.alert

import android.app.AlertDialog
import android.content.Context
import com.greensoft.greentranserpnative.ui.bottomsheet.acceptPickup.AcceptPickupBottomSheet
import com.greensoft.greentranserpnative.ui.onClick.AlertCallback


class CommonAlert {

    companion object {
        // alertCallType to know for which the alert was create ex. if(alertCallType == "REMOVE_STICKER")
        fun <T> createAlert(context: Context, header: String, description: String, callback: AlertCallback<T>, alertCallType: String, data: T?) {
            AlertDialog.Builder(context)
                .setTitle(header)
                .setMessage(description)
                .setPositiveButton("Yes") { _,_ ->
                    callback.onAlertClick(AlertClick.YES, alertCallType, data)
                }
                .setNeutralButton("No") { _, _ ->
                    callback.onAlertClick(AlertClick.NO, alertCallType, data)
                }
                .show()
        }
    }
}