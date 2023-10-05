package com.greensoft.greentranserpnative.base

import androidx.lifecycle.MutableLiveData
import com.greensoft.greentranserpnative.api.Api
import com.greensoft.greentranserpnative.common.CommonResult
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

open class BaseRepository @Inject constructor() {
    @Inject
    lateinit var api: Api
    val API_ERROR = "Could not get the data from the SERVER."
    val SERVER_ERROR = "Something Went Wrong Please Try Again Later"
    val INTERNET_ERROR = "Internet Not Working Please Check Your Internet Connection"
    val isError: MutableLiveData<String> = MutableLiveData()
    val viewDialogMutData: MutableLiveData<Boolean> = MutableLiveData()

    open fun getResult(response: CommonResult): JSONArray? {
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
                    isError.postValue(jObj["commandmessage"].toString())
                    null
                }
            } else {
                null
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return JsonResult
    }
    open fun getResult1(response: CommonResult): JSONArray? {
        var Obj: JSONObject? = null
        var JsonArray: JSONArray? = null
        var JsonResult: JSONArray? = null
        val Data: String
        try {
            Data = response.DataSet
            Obj = JSONObject(Data)
            JsonArray = Obj.getJSONArray("Table1")
            JsonResult = if (JsonArray.length() != 0) {
                val jObj = JsonArray.getJSONObject(0)
                if (jObj["commandstatus"].toString() == "1") {
                    JsonArray
                } else {
                    isError.postValue(jObj["commandmessage"].toString())
                    null
                }
            } else {
                null
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return JsonResult
    }
}