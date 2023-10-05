package com.greensoft.greentranserpnative.api

import com.greensoft.greentranserpnative.common.CommonResult
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object ConvertJson {
    fun getJsonString(response: CommonResult): JSONArray? {
        var Obj: JSONObject? = null
        var JsonArray: JSONArray? = null
        var JsonResult: JSONArray? = null
        val Data: String
        try {
            Data = response.DataSet
            Obj = JSONObject(Data)
            JsonArray = Obj.getJSONArray("Table")
            JsonResult = if (JsonArray.length() != 0) {
                val jObj = JsonArray.getJSONObject(0)
                if (jObj["commandstatus"].toString() == "1") {
                    JsonArray
                } else {
                    //  Common.errorDialog(jObj.get("commandmessage").toString());
                    JsonArray
                }
            } else {
                JsonArray
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return JsonResult
    }

    fun getJsonObj(response: CommonResult): JSONObject? {
        var Obj: JSONObject? = null
        val JsonArray: JSONArray? = null
        val JsonResult: JSONArray? = null
        val Data: String
        try {
            Data = response.DataSet
            Obj = JSONObject(Data)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return Obj
    }
}