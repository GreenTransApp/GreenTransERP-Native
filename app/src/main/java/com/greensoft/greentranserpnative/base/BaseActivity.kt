package com.greensoft.greentranserpnative.base

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.device.ScanManager
import android.device.scanner.configuration.PropertyID
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.media.RingtoneManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import com.google.android.material.card.MaterialCardView
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.ENV
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.common.PeriodSelection
import com.greensoft.greentranserpnative.model.SingleDatePickerWIthViewTypeModel
import com.greensoft.greentranserpnative.model.TimePickerWithViewType
import com.greensoft.greentranserpnative.ui.bottomsheet.acceptPickup.AcceptPickupBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.common.CommonBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.common.models.CommonBottomSheetModel
import com.greensoft.greentranserpnative.ui.common.cameraX.CropImageActivity
import com.greensoft.greentranserpnative.ui.home.models.UserMenuModel
import com.greensoft.greentranserpnative.ui.login.models.LoginDataModel
import com.greensoft.greentranserpnative.ui.login.models.UserDataModel
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.notificationPanel.NotificationPanelBottomSheet
import com.greensoft.greentranserpnative.ui.operation.notificationPanel.model.NotificationPanelBottomSheetModel
import com.greensoft.greentranserpnative.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
open class BaseActivity @Inject constructor(): AppCompatActivity() {
    lateinit var mContext: Context
//    var progressDialog: ProgressDialog? = null
//    var prefManager: PrefManager? = null
    var serverErrorMsg = "Something went wrong, please try again later."
    var somethingWentWrongErrorMsg = "Something Went Wrong, Please Try Again."
    var internetError = "Internet Not Working Please Check Your Internet Connection"
    val noLoadingGRSelectedErrMsg = "No Loading/GR # is selected for manifest. Please select at least 1 Loading/GR #."

    var mPeriod: MutableLiveData<PeriodSelection> = MutableLiveData()
    var timePeriod: MutableLiveData<String> = MutableLiveData()
    var imageClicked: MutableLiveData<Boolean> = MutableLiveData()
    private lateinit var storagePermission: Array<String>
    private lateinit var cameraPermission: Array<String>
    private lateinit var uri: Uri
    private lateinit var file : File
    private lateinit var camIntent: Intent
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryLauncher: ActivityResultLauncher<String>

    //    var timePeriod: MutableLiveData<TimeSelection?> = MutableLiveData<Any?>()
    val mScanner = MutableLiveData<String>()
    private var scanManager: ScanManager? = null


    private var materialDatePicker: MaterialDatePicker<*>? = null
    private var singleDatePicker: MaterialDatePicker<*>? = null

    var singleDatePeriodWithViewType: MutableLiveData<SingleDatePickerWIthViewTypeModel> = MutableLiveData()
    private var singleDatePickerWithViewType: MaterialDatePicker<*>? = null

    var timeSelectionWithViewType: MutableLiveData<TimePickerWithViewType> = MutableLiveData()

//    var timePicker: TimePicker? = null
    var loginDataModel: LoginDataModel? = null
    var userDataModel: UserDataModel? = null
    private var dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    var progressDialog: ProgressDialog? = null
    private var prefs: SharedPreferences? = null

    private lateinit var imageLauncher: ActivityResultLauncher<Intent>
    var imageBase64List = ArrayList<String>()
    var imageBitmapList = ArrayList<Bitmap>()
    var imageUriList = ArrayList<Uri>()

    fun setUpToolbar(title: String) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = title
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext=this@BaseActivity
        initialize()
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
        cameraPermission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        storagePermission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Handle the result from the camera
                val intent = Intent(this@BaseActivity, CropImageActivity::class.java)
                intent.putExtra("DATA", uri.toString()) // Pass the captured image's Uri to CropImageActivity
                imageLauncher.launch(intent)
//                startActivity(intent)
            }
        }

        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            if (result != null) {
                val intent = Intent(this@BaseActivity, CropImageActivity::class.java)
                intent.putExtra("DATA", result.toString()) // Pass the gallery image's Uri to CropImageActivity
                imageLauncher.launch(intent)
//                startActivity(intent)
            }
        }
        imageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                if (result.resultCode == Activity.RESULT_OK) {
                    var data = result.data
                    if (data != null) {
                        if (data.hasExtra("IMAGE_RESULT_BACK")) {
                            var imageUri = data.getStringExtra("IMAGE_RESULT_BACK")
                            if (!imageUri.isNullOrBlank()) {
                                var imageConvertedToUri: Uri? = Uri.parse(imageUri)
                                if(imageConvertedToUri != null) {
                                    var base64 = convertImageUriToBase64(
                                        contentResolver = contentResolver,
                                        imageUri = imageConvertedToUri
                                    )
                                    var bitmap: Bitmap? = null
                                    if (base64 != null) {
                                        bitmap = getBitmapFromBase64(base64 = base64)
                                        if (bitmap != null) {
                                            imageBase64List.add(base64)
                                            imageBitmapList.add(bitmap)
                                            imageUriList.add(imageConvertedToUri)
//                                            imageBase64List.add(imageBase64List.size - 1, base64)
//                                            imageBitmapList.add(imageBitmapList.size - 1, bitmap)
                                            imageClicked.postValue(true)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (ex: Exception) {
                errorToast(ex.message)
            }
        }
    }


    private fun initialize() {
        scanManager = ScanManager()
        try {
            if (scanManager != null) {
                scanManager?.openScanner()
                scanManager?.switchOutputMode(0)
            }
        } catch (ex: Exception) {
//            errorToast(ex.message)
            scanManager = null
        }
    }

    fun isScannerWorking(): Boolean {
        if(scanManager == null) {
            return false
        }
        return true
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onResume() {
        super.onResume()
        if(scanManager != null) {
//            errorToast("Scanner not available. All functionality may not work.")

            val filter = IntentFilter()
            val idbuf =
                intArrayOf(
                    PropertyID.WEDGE_INTENT_ACTION_NAME,
                    PropertyID.WEDGE_INTENT_DATA_STRING_TAG
                )
            val value_buf: Array<String?>? = scanManager?.getParameterString(idbuf)
            if (value_buf != null && value_buf[0] != null && value_buf[0] != "") {
                filter.addAction(value_buf[0])
            } else {
                filter.addAction(SCAN_ACTION)
            }
            if (Build.VERSION.SDK_INT >= 33) {
                registerReceiver(mScanReceiver, filter, RECEIVER_NOT_EXPORTED)
            } else {
                registerReceiver(mScanReceiver, filter)
            }
        }
        //          33
//        registerReceiver(mScanReceiver, filter, RECEIVER_NOT_EXPORTED)
    }

    override fun onPause() {
        super.onPause()
        if(scanManager !=null){
            scanManager?.stopDecode()
            unregisterReceiver(mScanReceiver)
        }
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
//        cameraSetup
         const val CAMERA_REQUEST = 100
         const val STORAGE_REQUEST = 200
        private const val SCAN_ACTION = ScanManager.ACTION_DECODE //default action

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

        fun showSnackBar(view: View, mContext: Context, msg: String?, isSuccessSnackBar: Boolean) {
            if(msg.isNullOrBlank()) {
                return
            }
            val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG)
            val layoutInflater = LayoutInflater.from(mContext)
            val customSnackView: View =
                layoutInflater.inflate(com.greensoft.greentranserpnative.R.layout.custom_snackbar, null)
            val materialCard: MaterialCardView = customSnackView.findViewById(R.id.snackbar_materialCardView)
            snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
            var strokeColor = "#38761d"
            var cardBackgroundColor = "#9fc490"
            if(!isSuccessSnackBar) {
                strokeColor = "#c62d42"
                cardBackgroundColor = "#ca9d9d"
            }
            materialCard.setStrokeColor(Color.parseColor(strokeColor))
            materialCard.setCardBackgroundColor(Color.parseColor(cardBackgroundColor))
            val snackbarLayout = snackbar.view as SnackbarLayout
            snackbarLayout.setPadding(0, 0, 0, 0)
            var textView: TextView = customSnackView.findViewById(R.id.snackBar_Msg)
            textView.setText(msg)
            snackbarLayout.addView(customSnackView, 0)
            snackbar.show()
        }
        fun successToast(mContext: Context, msg: String?, view: View? = null) {
//        Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();

            if(view != null) {
                showSnackBar(view, mContext, msg, true)
                return;
            } else {
                var rootView: View? = null
                try {
                    rootView = (this as AppCompatActivity).window.decorView.findViewById(android.R.id.content)
                } catch (ex: Exception) {
                    rootView = (mContext as AppCompatActivity).window.decorView.findViewById(android.R.id.content)
                }
                if(rootView != null)
                    showSnackBar(rootView, mContext, msg, true)
                else
                    Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show()
                return
            }

            if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.Q) {

                val toast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG)
                val view = toast.view
                view!!.background.setColorFilter(
                    ContextCompat.getColor(mContext, android.R.color.holo_green_dark),

                    PorterDuff.Mode.SRC_IN
                )
                val text = view.findViewById<TextView>(android.R.id.message)
                text.setTextColor(ContextCompat.getColor(mContext, R.color.white))
                toast.show()
                // only for gingerbread and newer versions
            } else {
                if(msg != null) {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun errorToast(mContext: Context, msg: String?, view: View? = null) {
            //        Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();

            if(view != null) {
                showSnackBar(view, mContext, msg, false)
                return;
            } else {
                var rootView: View? = null
                try {
                    rootView = (this as AppCompatActivity).window.decorView.findViewById(android.R.id.content)
                } catch (ex: Exception) {
                    rootView = (mContext as AppCompatActivity).window.decorView.findViewById(android.R.id.content)
                }
                if(rootView != null)
                    showSnackBar(rootView, mContext, msg, false)
                else
                    Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show()
                return
            }

            if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.Q) {
                val toast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG)
                val view = toast.view
                view!!.background.setColorFilter(
                    ContextCompat.getColor(mContext, android.R.color.holo_red_dark),
                    PorterDuff.Mode.SRC_IN
                )
                val text = view!!.findViewById<TextView>(android.R.id.message)
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
    open fun openCounterBottomSheet(mContext: Context, title: String, bottomSheetClick: OnRowClick<Any>, commonList: ArrayList<NotificationPanelBottomSheetModel>, withAdapter: Boolean = false, index: Int = -1) {
//        showProgressDialog()
        val bottomSheetDialog = NotificationPanelBottomSheet.newInstance(mContext, title, bottomSheetClick, commonList, withAdapter, index)
//        hideProgressDialog()
        bottomSheetDialog.show(supportFragmentManager, NotificationPanelBottomSheet.TAG)
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

    open fun successToast(msg: String?, view: View? = null) {
//        Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
        if(view != null) {
            showSnackBar(view, this, msg, true)
            return;
        } else {
            val rootView: View =
                (this as Activity).window.decorView.findViewById(android.R.id.content)
            showSnackBar(rootView, this, msg, true)
            return
        }

        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.Q) {

            val toast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG)
            val view = toast.view
            view!!.background.setColorFilter(
                ContextCompat.getColor(mContext, android.R.color.holo_green_dark),

                PorterDuff.Mode.SRC_IN
            )
            val text = view!!.findViewById<TextView>(android.R.id.message)
            text.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            toast.show()
                // only for gingerbread and newer versions
        } else {
            if(msg != null) {
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    open fun errorToast(msg: String?, view: View? = null) {
        playSound()
        if(view != null) {
            showSnackBar(view, this, msg, false)
            return;
        } else {
            val rootView: View =
                (this as Activity).window.decorView.findViewById(android.R.id.content)
            showSnackBar(rootView, this, msg, false)
            return
        }
        //        Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.Q) {
            val toast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG)
            val view = toast.view
            view!!.background.setColorFilter(
                ContextCompat.getColor(mContext, android.R.color.holo_red_dark),
                PorterDuff.Mode.SRC_IN
            )
            val text = view!!.findViewById<TextView>(android.R.id.message)
            text.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            toast.show()
        } else {
            if(msg != null) {
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    open fun showSnackBar(view: View, mContext: Context, msgParams: String?, isSuccessSnackBar: Boolean) {
        var msg = msgParams
        if(msg.isNullOrBlank()) {
            msg = "EMPTY STRING SNACKBAR"
//            return
        }
        val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG)
        val layoutInflater = LayoutInflater.from(mContext)
        val customSnackView: View =
            layoutInflater.inflate(com.greensoft.greentranserpnative.R.layout.custom_snackbar, null)
        val materialCard: MaterialCardView = customSnackView.findViewById(R.id.snackbar_materialCardView)
        if(isSuccessSnackBar) {
            materialCard.setStrokeColor(Color.parseColor("#38761d"))
            materialCard.setCardBackgroundColor(Color.parseColor("#9fc490"))
        } else {
            materialCard.setStrokeColor(Color.parseColor("#c62d42"))
            materialCard.setCardBackgroundColor(Color.parseColor("#ca9d9d"))
        }
        val snackbarLayout = snackbar.view as SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0)
        var textView: TextView = customSnackView.findViewById(R.id.snackBar_Msg)
        textView.setText(msg)
        snackbarLayout.addView(customSnackView, 0)
        snackbar.show()
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
                val viewFormat = SimpleDateFormat("dd-MM-yyyy")
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

    open fun openBookingDatePicker() {
        val constraints =CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
            .build()

//        ---------------------------

        Log.d("BASE ACTIVITY", "SINGLE PERIOD SELECTION")
        val materialDateBuilder: MaterialDatePicker.Builder<*> =
            MaterialDatePicker.Builder.datePicker()
        materialDateBuilder.setTitleText("SELECT A DATE")
        materialDateBuilder.setCalendarConstraints(constraints)
        singleDatePicker = materialDateBuilder.build()
        singleDatePicker!!.addOnPositiveButtonClickListener{ selection: Any ->
                val viewFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val sqlFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
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


    open fun openSingleDatePicker() {
        Log.d("BASE ACTIVITY", "SINGLE PERIOD SELECTION")
        val materialDateBuilder: MaterialDatePicker.Builder<*> =
            MaterialDatePicker.Builder.datePicker()
        materialDateBuilder.setTitleText("SELECT A DATE")
        singleDatePicker = materialDateBuilder.build()
        singleDatePicker!!.addOnPositiveButtonClickListener{ selection: Any ->
            val viewFormat = SimpleDateFormat("dd-MM-yyyy")
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

    open fun openSingleDatePickerWithViewType(viewType: String, withAdapter: Boolean = false, index: Int = -1) {
        Log.d("BASE ACTIVITY", "SINGLE PERIOD SELECTION")
        val materialDateBuilder: MaterialDatePicker.Builder<*> =
            MaterialDatePicker.Builder.datePicker()
        materialDateBuilder.setTitleText("SELECT A DATE")
        singleDatePickerWithViewType = materialDateBuilder.build()
        singleDatePickerWithViewType!!.addOnPositiveButtonClickListener{ selection: Any ->
            val viewFormat = SimpleDateFormat("dd-MM-yyyy")
            val sqlFormat = SimpleDateFormat("yyyy-MM-dd")
            val selectedDate = selection
            val singleDate = Date(selectedDate as Long)
            val periodSelection = PeriodSelection()
            periodSelection.sqlsingleDate = sqlFormat.format(singleDate)
            periodSelection.viewsingleDate = viewFormat.format(singleDate)
            val singleDatePickerWIthViewTypeModel = SingleDatePickerWIthViewTypeModel(
                viewType,
                periodSelection,
                withAdapter,
                index
            )
            singleDatePeriodWithViewType.postValue(singleDatePickerWIthViewTypeModel)
        }
        if(singleDatePickerWithViewType != null) {
            if (singleDatePickerWithViewType!!.isVisible) {
                return;
            }
            singleDatePickerWithViewType!!.show(supportFragmentManager, "DATE_PICKER");
        }
    }


    open fun openTimePickerWithViewType(viewType: String, withAdapter: Boolean = false, index: Int = -1) {
        val materialTimePicker: MaterialTimePicker = MaterialTimePicker.Builder()
            .setTitleText("SELECT YOUR TIMING")
            .setHour(12)
            .setMinute(10)
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .build()

        materialTimePicker.show(supportFragmentManager, "TIME_SELECTION")
        materialTimePicker.addOnPositiveButtonClickListener {

            val pickedHour: Int = materialTimePicker.hour
            val pickedMinute: Int = materialTimePicker.minute
            val formattedTime: String = when {
                (pickedMinute < 10)-> {
                    "${materialTimePicker.hour}:0${materialTimePicker.minute}"
                }
                else -> {
                    "${materialTimePicker.hour}:${materialTimePicker.minute}"
                }
            }
            val singleTimePickerWIthViewTypeModel = TimePickerWithViewType(
                viewType,
                formattedTime,
                withAdapter,
                index
            )
            timeSelectionWithViewType.postValue(singleTimePickerWIthViewTypeModel)
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

//    open fun openTimePicker() {
//        val c = Calendar.getInstance()
//        val hour = c[Calendar.HOUR_OF_DAY]
//        val minute = c[Calendar.MINUTE]
//        val timePickerDialog = TimePickerDialog(this,
//            { timePicker, hourOfDay, minute ->
//                var timeSelection: TimeSelection? = null
//                if (hourOfDay < 10) {
//                    val viewSingleTime = "0$hourOfDay:$minute"
//                    val sqlSingleTime = "0$hourOfDay:$minute"
//                    timeSelection = TimeSelection(
//                        viewSingleTime = viewSingleTime,
//                        sqlSingleTime = sqlSingleTime
//                    )
//                } else {
//                    val viewSingleTime = "$hourOfDay:$minute"
//                    val sqlSingleTime = "$hourOfDay:$minute"
//                    timeSelection = TimeSelection(
//                        viewSingleTime = viewSingleTime,
//                        sqlSingleTime = sqlSingleTime
//                    )
//                }
//                timePeriod.postValue(timeSelection!!)
//            }, hour, minute, false
//        )
//        timePickerDialog.show()
//    }

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
    private val mScanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val barcode = intent.getByteArrayExtra(ScanManager.DECODE_DATA_TAG)
            val barcodelen = intent.getIntExtra(ScanManager.BARCODE_LENGTH_TAG, 0)
            val temp = intent.getByteExtra(ScanManager.BARCODE_TYPE_TAG, 0.toByte())
            Log.i("debug", "----codetype--$temp")
            val barcodeStr =String(barcode!!, 0, barcodelen)
            mScanner.postValue(barcodeStr)
            if(scanManager != null) {
                scanManager?.stopDecode()
            }
        }
    }

    open fun playSound() {
        try {
            // Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ getApplicationContext().getPackageName() + "/" + R.raw.error_sound);
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(applicationContext, notification)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
//     fun isNetworkConnected(): Boolean {
//        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
//        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
//    }

    open fun openTimePicker() {
        val materialTimePicker: MaterialTimePicker = MaterialTimePicker.Builder()
            .setTitleText("SELECT YOUR TIMING")
            .setHour(12)
            .setMinute(10)
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .build()

        materialTimePicker.show(supportFragmentManager, AcceptPickupBottomSheet.TAG)
        materialTimePicker.addOnPositiveButtonClickListener {

            val pickedHour: Int = materialTimePicker.hour
            val pickedMinute: Int = materialTimePicker.minute
            val formattedTime: String = when {
                (pickedMinute < 10)-> {
                    "${materialTimePicker.hour}:0${materialTimePicker.minute}"
                }
                else -> {
                    "${materialTimePicker.hour}:${materialTimePicker.minute}"
                }
            }
            timePeriod.postValue(formattedTime)
        }
    }

    fun getCompanyId(): String {
        return userDataModel?.companyid.toString()
    }
    fun getLoginBranchCode(): String {
        return userDataModel?.loginbranchcode.toString()
    }
    fun getUserCode(): String {
        return userDataModel?.usercode.toString()
    }
    fun getSessionId(): String {
        return userDataModel?.sessionid.toString()
    }
    fun getExecutiveId(): String {
        return userDataModel?.executiveid.toString()
    }
//    fun snackBar(message:String){
//        val snackBar = Snackbar.make(activityBinding.layout, message, Snackbar.LENGTH_LONG)
//            .setAction("Click") {
//                Toast.makeText(this, "Snack bar action", Toast.LENGTH_SHORT).show()
//            }
//        snackBar.show()
//    }

     fun showImageDialog() {
        val options = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick Image From")
        builder.setItems(options) { dialog, which ->
            if (which == 0) {
                if (!checkCameraPermission()) {
                    requestCameraPermission()
                } else {
//                    pickFromGallery()
                    pickFromCamera()
                }
            } else if (which == 1) {
                if (!checkStoragePermission()) {
                    requestStoragePermission()
                } else {
                    pickFromGallery()
                }
            }
        }
        builder.create().show()
    }
    //    Checking permission
    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
    // Requesting gallery permission
    private fun requestStoragePermission() {
        requestPermissions(storagePermission, BaseActivity.STORAGE_REQUEST)
    }
    // checking camera permissions
//    private fun checkCameraPermission(): Boolean {
//        val result = ContextCompat.checkSelfPermission(this,
//            Manifest.permission.CAMERA,
//        ) == PackageManager.PERMISSION_GRANTED
//        val result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//        return result && result1
//    }
    private fun ifPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }
    private fun checkCameraPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            return ifPermissionGranted(Manifest.permission.READ_MEDIA_IMAGES)
                    && ifPermissionGranted(Manifest.permission.READ_MEDIA_VIDEO)
                    && ifPermissionGranted(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
                    && ifPermissionGranted(Manifest.permission.CAMERA)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ifPermissionGranted(Manifest.permission.READ_MEDIA_IMAGES)
                    && ifPermissionGranted(Manifest.permission.READ_MEDIA_VIDEO)
                    && ifPermissionGranted(Manifest.permission.CAMERA)
        } else {
            return ifPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)
                    && ifPermissionGranted(Manifest.permission.CAMERA)
        }

    }
    fun requestCameraPermission() {
        if(checkCameraPermission()) {
            return
        }
        var permissions: Array<String>? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            permissions = arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
                Manifest.permission.CAMERA
                )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.CAMERA
            )
        } else {
            permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        }
        if(permissions != null) {
            requestPermissions(permissions, CAMERA_REQUEST)
        } else {
            errorToast("Something went wrong. Please reinstall the app.")
        }
    }

    private fun pickFromGallery() {
        galleryLauncher.launch("image/*")
    }
    private fun pickFromCamera() {

        val imageFileName = "file" + System.currentTimeMillis().toString() + ".jpg"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        file = File(storageDir, imageFileName)
        uri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", file)
        camIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        camIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        camIntent.putExtra("return-Data", true)
//        camIntent.putExtra(ENV.IMAGE_PREF_SHARE,true)
        cameraLauncher.launch(camIntent)
    }

     private fun convertImageUriToBase64(contentResolver: ContentResolver, imageUri: Uri): String? {
        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
            val buffer = ByteArrayOutputStream()
            val bufferSize = 1024
            val data = ByteArray(bufferSize)
            var bytesRead= 0
            while (inputStream?.read(data, 0, bufferSize).also {
                    if (it != null) {
                        bytesRead = it
                    }
                } != -1) {
                buffer.write(data, 0, bytesRead)
            }
            val byteArray: ByteArray = buffer.toByteArray()
            Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getBitmapFromBase64(base64: String): Bitmap? {
//        val base64Image = Cropper.convertImageUriToBase64(contentResolver, uri)
//        Toast.makeText(this, "bse", Toast.LENGTH_SHORT).show()
        // Decode the Base64 string into a byte array
        try {
            val decodedBytes: ByteArray = Base64.decode(base64, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (ex: Exception) {
            errorToast(ex.message)
        }
        return null
    }

     fun viewImageFullScreen(){
         startActivity(Intent
             (Intent.ACTION_VIEW, Uri.parse(imageBase64List.elementAt(0)))
         )
    }

    fun logger(tag: String, msg: String) {
        Log.d(tag, msg)
    }

}


