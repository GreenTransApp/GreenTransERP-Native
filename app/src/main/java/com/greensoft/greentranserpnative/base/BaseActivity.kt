package com.greensoft.greentranserpnative.base

import android.R
import android.app.ProgressDialog

import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.PorterDuff
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.ENV
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.common.PeriodSelection
import com.greensoft.greentranserpnative.common.TimeSelection
import com.greensoft.greentranserpnative.ui.bottomsheet.common.CommonBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.common.models.CommonBottomSheetModel
import com.greensoft.greentranserpnative.ui.home.models.UserMenuModel
import com.greensoft.greentranserpnative.ui.login.LoginActivity
import com.greensoft.greentranserpnative.ui.login.models.LoginDataModel
import com.greensoft.greentranserpnative.ui.login.models.UserDataModel
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
open class BaseActivity @Inject constructor(): AppCompatActivity() {
    lateinit var mContext: Context
//    var progressDialog: ProgressDialog? = null
//    var prefManager: PrefManager? = null
    var serverErrorMsg = "Something Went Wrong Please Try Again Later"
    var internetError = "Internet Not Working Please Check Your Internet Connection"

    var mPeriod: MutableLiveData<PeriodSelection> = MutableLiveData()
    var timePeriod: MutableLiveData<TimeSelection> = MutableLiveData()
//    var timePeriod: MutableLiveData<TimeSelection?> = MutableLiveData<Any?>()
var materialDatePicker: MaterialDatePicker<*>? = null
    private var singleDatePicker: MaterialDatePicker<*>? = null
//    var timePicker: TimePicker? = null
    var loginDataModel: LoginDataModel? = null
    var userDataModel: UserDataModel? = null
    private var dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    var progressDialog: ProgressDialog? = null
    private var prefs: SharedPreferences? = null


    fun setUpToolbar(title: String) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = title
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext=this@BaseActivity
        setObservers()
        val loginModel = getLoginData()

        if(loginModel != null) {
            loginDataModel = loginModel
        }
        val userModel = getUserData()
        if(userModel != null) {
            userDataModel = userModel
        }
//        openDatePicker()
//        SingleDateSelector()
    }

    private fun setObservers() {
//        capturedImage.observe(this) { imageUri ->
//            successToast("BASE_ACTIVITY ${imageUri.path}")
//        }
    }

//    fun setUpToolbar(activity: AppCompatActivity, title: String) {
//        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
//        activity.supportActionBar?.title = title
//    }

    companion object {

        var capturedImage: MutableLiveData<Uri> = MutableLiveData()

        open fun getSqlCurrentDate(): String {
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val date = Date()
            return formatter.format(date)
        }

        open fun getViewCurrentDate(): String {
            val formatter = SimpleDateFormat("dd-MM-yyyy")
            val date = Date()
            return formatter.format(date)
        }

        open fun getSqlCurrentTime(): String {
            val formatter = SimpleDateFormat("HH:mm")
            val date = Date()
            return formatter.format(date)
        }

        fun successToast(mContext: Context, msg: String?) {
//        Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();

            if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.Q) {

                val toast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG)
                val view = toast.view
                view!!.background.setColorFilter(
                    ContextCompat.getColor(mContext, R.color.holo_green_dark),

                    PorterDuff.Mode.SRC_IN
                )
                val text = view!!.findViewById<TextView>(R.id.message)
                text.setTextColor(ContextCompat.getColor(mContext, R.color.white))
                toast.show()
                // only for gingerbread and newer versions
            } else {
                if(msg != null) {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun errorToast(mContext: Context, msg: String?) {
            //        Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
            if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.Q) {
                val toast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG)
                val view = toast.view
                view!!.background.setColorFilter(
                    ContextCompat.getColor(mContext, R.color.holo_red_dark),
                    PorterDuff.Mode.SRC_IN
                )
                val text = view!!.findViewById<TextView>(R.id.message)
                text.setTextColor(ContextCompat.getColor(mContext, R.color.white))
                toast.show()
            } else {
                if(msg != null) {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    open fun getMenuData(): UserMenuModel? {
        val gson = Gson()
        var deSerialisedData: UserMenuModel? = null
        if(intent.extras == null) {
            return null
        }
        intent.extras?.let { data ->
            val serializedData = data.getString("MENU_DATA") ?: return null
            val modelType = object : TypeToken<UserMenuModel>() {}.type
            deSerialisedData = gson.fromJson(serializedData, modelType)
            Utils.logger("MENU_DATA", deSerialisedData?.menucode);
        }
        return deSerialisedData
    }

    open fun openCommonBottomSheet(mContext: Context, title: String, bottomSheetClick: BottomSheetClick<Any>, commonList: ArrayList<CommonBottomSheetModel<Any>>, withAdapter: Boolean = false, index: Int = -1) {
//        showProgressDialog()
        val bottomSheetDialog = CommonBottomSheet.newInstance(mContext, title, bottomSheetClick, commonList, withAdapter, index)
//        hideProgressDialog()
        bottomSheetDialog.show(supportFragmentManager, CommonBottomSheet.TAG)
    }


    private fun getSharedPref(): SharedPreferences {
        if(this.prefs != null) {
            return prefs!!
        }
//        prefs = getPreferences(MODE_PRIVATE)
//        prefs = getSharedPreferences("KGS_PRINTING_APP", MODE_PRIVATE)
//        prefs = PreferenceManager.getDefaultSharedPreferences(mContext)
        prefs = getSharedPreferences(packageName, Context.MODE_PRIVATE);
        return prefs!!
    }

    fun saveStorageBoolean(tag: String, data: Boolean) {
        prefs = getSharedPref()
        val prefEditor = prefs?.edit()
        prefEditor?.putBoolean(tag, data)
        prefEditor?.apply()
    }
    fun getStorageBoolean(tag: String): Boolean {
        prefs = getSharedPref()
        return prefs?.getBoolean(tag, false)!!
    }

//    fun isNetworkConnected(): Boolean {
//        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
//        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
//    }

    fun isNetworkConnected(): Boolean {
        // 0 means no internet,
        return getConnectionType() != 0
    }

//    @IntRange(from = 0, to = 3)
    fun getConnectionType(): Int {
        var result = 0 // Returns connection type. 0: none; 1: mobile data; 2: wifi; 3: vpn
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        result = 2
                    } else if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        result = 1
                    } else if (hasTransport(NetworkCapabilities.TRANSPORT_VPN)){
                        result = 3
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = 2
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = 1
                    } else if(type == ConnectivityManager.TYPE_VPN) {
                        result = 3
                    }
                }
            }
        }
        return result
    }


    open fun showProgressDialog() {
        progressDialog = ProgressDialog(mContext)
        progressDialog!!.setMessage("Loading....")
        progressDialog!!.setCancelable(false)
        //    progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog!!.show()
    }

    open fun hideProgressDialog() {
        progressDialog?.dismiss()
    }


    fun saveStorageString(tag: String, data: String) {
        prefs = getSharedPref()
        val prefEditor = prefs?.edit()
        prefEditor?.putString(tag, data)
        prefEditor?.apply()
    }
    fun getStorageString(tag: String): String {
        prefs = getSharedPref()
        return prefs?.getString(tag, "")!!
    }

    fun clearStorage() {
        prefs = getSharedPref()
        val prefEditor = prefs?.edit()
        prefEditor?.clear()
        prefEditor?.apply()
    }

    protected fun getLoginData(): LoginDataModel? {
        prefs = getSharedPref()
        val userDataJson = getStorageString(ENV.LOGIN_DATA_PREF_TAG)
        if(userDataJson != "") {
            val dataModel: LoginDataModel? = Gson().fromJson(userDataJson, LoginDataModel::class.java)
            if(dataModel != null) {
                loginDataModel = dataModel
            }
            return dataModel
        }
        return null
    }

    protected fun getUserData(): UserDataModel? {
        prefs = getSharedPref()
        val userDataJson = getStorageString(ENV.USER_DATA_PREF_TAG)
        if(userDataJson != "") {
            val dataModel: UserDataModel = Gson().fromJson(userDataJson, UserDataModel::class.java)
            if(dataModel != null) {
                userDataModel = dataModel
            }
            return dataModel
        }
        return null
    }

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
                    errorToast(jObj["commandmessage"].toString())
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

    open fun successToast(msg: String?) {
//        Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();

        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.Q) {

            val toast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG)
            val view = toast.view
            view!!.background.setColorFilter(
                ContextCompat.getColor(mContext, R.color.holo_green_dark),

                PorterDuff.Mode.SRC_IN
            )
            val text = view!!.findViewById<TextView>(R.id.message)
            text.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            toast.show()
                // only for gingerbread and newer versions
        } else {
            if(msg != null) {
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    open fun errorToast(msg: String?) {
        //        Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.Q) {
            val toast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG)
            val view = toast.view
            view!!.background.setColorFilter(
                ContextCompat.getColor(mContext, R.color.holo_red_dark),
                PorterDuff.Mode.SRC_IN
            )
            val text = view!!.findViewById<TextView>(R.id.message)
            text.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            toast.show()
        } else {
            if(msg != null) {
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
            }
        }
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
                    errorToast(jObj["commandmessage"].toString())
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


    fun openDatePicker() {
        Log.d("BASE ACTIVITY", "PERIOD SELECTION CLICKED")
        val materialDateBuilder: MaterialDatePicker.Builder<*> =
            MaterialDatePicker.Builder.dateRangePicker()
        materialDateBuilder.setTitleText("SELECT A PERIOD")
        materialDatePicker = materialDateBuilder.build()
        materialDatePicker!!.addOnPositiveButtonClickListener { selection ->
                val viewFormat = SimpleDateFormat("MM-dd-yyyy")
                val sqlFormat = SimpleDateFormat("yyyy-MM-dd")
                val selectedDate = selection as androidx.core.util.Pair<Long?, Long?>
                if(selectedDate.first != null && selectedDate.second != null) {
                    val fromDate = Date(selectedDate.first!!)
                    val toDate = Date(selectedDate.second!!)
                    val periodSelection = PeriodSelection()
                    periodSelection.sqlFromDate = sqlFormat.format(fromDate)
                    periodSelection.sqlToDate = sqlFormat.format(toDate)
                    periodSelection.viewFromDate = viewFormat.format(fromDate)
                    periodSelection.viewToDate = viewFormat.format(toDate)
                    mPeriod.postValue(periodSelection)
                }
        }
        if(materialDatePicker != null) {
            if (materialDatePicker!!.isVisible) {
                return;
            }
            materialDatePicker!!.show(supportFragmentManager, "DATE_PICKER");
        }
//            MaterialPickerOnPositiveButtonClickListener<Pair<Long?, Long?>> { (first, second) ->
//                val viewFormat = SimpleDateFormat("MM-dd-yyyy")
//                val sqlFormat = SimpleDateFormat("yyyy-MM-dd")
//                val fromDate = Date(first)
//                val toDate = Date(second)
//                val periodSelection = PeriodSelection()
//                periodSelection.setSqlFromDate(sqlFormat.format(fromDate))
//                periodSelection.setSqlToDate(sqlFormat.format(toDate))
//                periodSelection.setViewFromDate(viewFormat.format(fromDate))
//                periodSelection.setViewToDate(viewFormat.format(toDate))
//                mPeriod.postValue(periodSelection)
//            })
    }

    open fun openSingleDatePicker() {
        Log.d("BASE ACTIVITY", "SINGLE PERIOD SELECTION")
        val materialDateBuilder: MaterialDatePicker.Builder<*> =
            MaterialDatePicker.Builder.datePicker()
        materialDateBuilder.setTitleText("SELECT A DATE")
        singleDatePicker = materialDateBuilder.build()
        singleDatePicker!!.addOnPositiveButtonClickListener{ selection: Any ->
                val viewFormat = SimpleDateFormat("MM-dd-yyyy")
                val sqlFormat = SimpleDateFormat("yyyy-MM-dd")
                val selectedDate = selection
                val singleDate = Date(selectedDate as Long)
                val periodSelection = PeriodSelection()
                periodSelection.sqlsingleDate = sqlFormat.format(singleDate)
                periodSelection.viewsingleDate = viewFormat.format(singleDate)
                mPeriod.postValue(periodSelection)
            }
        if(singleDatePicker != null) {
            if (singleDatePicker!!.isVisible) {
                return;
            }
            singleDatePicker!!.show(supportFragmentManager, "DATE_PICKER");
        }
    }

//  fun openTimePicker(){
//      val calendar = Calendar.getInstance()
//       val  timeSetListener=TimePickerDialog.OnTimeSetListener{timePicker, hour, minute ->
//           calendar.set(Calendar.HOUR_OF_DAY, hour)
//           calendar.set(Calendar.MINUTE, minute)
//           val materialTimePicker: MaterialTimePicker = MaterialTimePicker.Builder().build()
//
//           materialTimePicker!!.addOnPositiveButtonClickListener{selection: Any ->
//                val viewFormat= SimpleDateFormat("hh:mm a")
//               val sqlFormat =  SimpleDateFormat("hh:mm")
//               val timeSelection = TimeSelection()
//                   timeSelection.
//           }
//       }
//
//  }

    open fun openTimePicker() {
        val c = Calendar.getInstance()
        val hour = c[Calendar.HOUR_OF_DAY]
        val minute = c[Calendar.MINUTE]
        val timePickerDialog = TimePickerDialog(this,
            { timePicker, hourOfDay, minute ->
                var timeSelection: TimeSelection? = null
                if (hourOfDay < 10) {
                    val viewSingleTime = "0$hourOfDay:$minute"
                    val sqlSingleTime = "0$hourOfDay:$minute"
                    timeSelection = TimeSelection(
                        viewSingleTime = viewSingleTime,
                        sqlSingleTime = sqlSingleTime
                    )
                } else {
                    val viewSingleTime = "$hourOfDay:$minute"
                    val sqlSingleTime = "$hourOfDay:$minute"
                    timeSelection = TimeSelection(
                        viewSingleTime = viewSingleTime,
                        sqlSingleTime = sqlSingleTime
                    )
                }
                timePeriod.postValue(timeSelection!!)
            }, hour, minute, false
        )
        timePickerDialog.show()
    }

//    fun openSingleDatePicker() {
//        Log.d("BASE ACTIVITY", "SINGLE PERIOD SELECTION")
//        val materialDateBuilder: Material.Builder<*> =
//            MaterialDatePicker.Builder.dateRangePicker()
//        materialDateBuilder.setTitleText("SELECT A PERIOD")
//        materialDatePicker = materialDateBuilder.build()
//        materialDatePicker!!.addOnPositiveButtonClickListener { selection ->
//            val viewFormat = SimpleDateFormat("MM-dd-yyyy")
//            val sqlFormat = SimpleDateFormat("yyyy-MM-dd")
//            val selectedDate = selection as androidx.core.util.Pair<Long?, Long?>
//            if(selectedDate.first != null && selectedDate.second != null) {
//                val fromDate = Date(selectedDate.first!!)
//                val toDate = Date(selectedDate.second!!)
//                val periodSelection = PeriodSelection()
//                periodSelection.sqlFromDate = sqlFormat.format(fromDate)
//                periodSelection.sqlToDate = sqlFormat.format(toDate)
//                periodSelection.viewFromDate = viewFormat.format(fromDate)
//                periodSelection.viewToDate = viewFormat.format(toDate)
//                mPeriod.postValue(periodSelection)
//            }
//        }
//        if(materialDatePicker != null) {
//            if (materialDatePicker!!.isVisible) {
//                return;
//            }
//            materialDatePicker!!.show(supportFragmentManager, "DATE_PICKER");
//        }
//    }

    open fun getSqlDate(): String? {
        dateFormat = SimpleDateFormat("yyyy-MM-dd")
        dateFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        return dateFormat.format(Date())
    }


    open fun getViewDate(): String? {
        dateFormat = SimpleDateFormat("dd-MM-yyyy")
        dateFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        return dateFormat.format(Date())
    }


}


