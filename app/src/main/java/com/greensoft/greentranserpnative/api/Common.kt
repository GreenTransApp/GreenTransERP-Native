package com.greensoft.greentranserpnative.api

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.telephony.TelephonyManager
import android.util.Base64
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

object Common {
    const val RESTART_INTENT = "uk.ac.shef.oak.restarter"
    var progressDoalog: ProgressDialog? = null
    var ImageTransactionIdList = ArrayList<String>()
    private var mContext: Context? = null
    const val EVENT_CONNECT_STATUS = "EVENT_CONNECT_STATUS"
    lateinit var dateFormat: DateFormat
    var appVersion = "1.0"
    var appVersionDate = "2020-06-27"

    // public  static  String companyId="2324";
    //    public  static String companyId="96965855";
    var manifestNo: String? = null
    var scanType: String? = null
    var totalPackage: String? = null
    var palletcode: String? = null
    var pageType: String? = null
    var modeCode = ""
    var branchName = ""
    var branchCode = ""
    var WereHouseNo = ""
    var grNo = ""
    var outScanType: String? = null
    var planningNo: String? = null
    var planningForSticker: String? = null
    var viewType: String? = null
    var newOutscan: String? = null
    var stickerStr: String? = null
    var fromDate = ""
    var toDate = ""
    var viewfromDate = ""
    var viewtoDate = ""
    var deliverySheetNo: String? = null
    var vehicleName = ""
    var vehicleId = 0
    var fromStation = ""
    var arrivalId: String? = null
    var StickerType = ""
    var title = ""

    //    public  static Drs drs;
    //    public  static  int selectedTab=0;
    //    public  static Gr gr;
    //    public  static String selectedRelation;
    //    public  static ImageView imageView;
    //    public  static String PodImageTitle;
    //    public  static String grNo;
    //    public  static Bitmap podSignature;
    //    public  static DeliverySummary deliverySummary;
    //    public  static ArrayList<GrTrackingDetail> detailList = new ArrayList<>();
    //    public  static ArrayList<MisDetail> misDetailsList = new ArrayList<>();
    //    public  static ArrayList<Delivered> deliveredList = new ArrayList<>();
    //    public  static ArrayList<TotalDelivery> totalDeliveryList = new ArrayList<>();
    //    public  static ArrayList<Pending> pendingList = new ArrayList<>();
    //    public  static ArrayList<UnDelivered> undeliveredList = new ArrayList<>();
    fun setContext(mcontext: Context?) {
        mContext = mcontext
    }

    fun showProgressDialog() {
        progressDoalog = ProgressDialog(mContext)
        progressDoalog!!.setMessage("Loading....")
        progressDoalog!!.setCancelable(false)
        //    progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDoalog!!.show()
    }

    fun getsqlFormat(mDate: String?): String? {
        var mDate = mDate
        try {
            var spf = SimpleDateFormat("MMM dd, yyyy hh:mm:ss aaa")
            val newDate = spf.parse(mDate)
            spf = SimpleDateFormat("dd MMM yyyy")
            mDate = spf.format(newDate)
            println(mDate)
        } catch (e: Exception) {
        }
        return mDate
    }

    fun hideProgressDialog() {
        progressDoalog!!.dismiss()
    }

    //  public static String convertToCurrency(String amount)
    //  {
    //
    //      if(amount=="null")
    //      {
    //          amount="0";
    //      }
    //      else  if(amount==null)
    //      {
    //          amount="0";
    //      }
    //
    //      Float floatAmount=Float.parseFloat(amount);
    //    Locale locale = new Locale("en", "in");
    //    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
    //    String IndianRuppe= fmt.format(floatAmount);
    // return  IndianRuppe;
    //  }
    val currentDate: String
        get() {
            dateFormat = SimpleDateFormat("dd-MM-yyyy")
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"))
            return dateFormat.format(Date())
        }

    fun getsqlDate(): String {
        dateFormat = SimpleDateFormat("yyyy-MM-dd")
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"))
        return dateFormat.format(Date())
    }

    fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm")
        dateFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        return dateFormat.format(Date())
    }

    fun isNetworkConnected(): Boolean {
        val cm = mContext!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }

    fun errorDialog(Message: String?) {
        val builder: AlertDialog.Builder
        builder = AlertDialog.Builder(mContext!!)
        //        builder.setMessage(Message) .setTitle(R.string.dialog_title);

        //Setting message manually and performing action on button click
        builder.setMessage(Message)
            .setCancelable(false)
            .setPositiveButton("Ok") { dialog, id -> }
        val alert = builder.create()
        alert.setTitle("Alert")
        alert.show()
    }

    fun InternetError() {
        val builder: AlertDialog.Builder
        builder = AlertDialog.Builder(mContext!!)
        //        builder.setMessage(Message) .setTitle(R.string.dialog_title);

        //Setting message manually and performing action on button click
        builder.setMessage("Internet Not Working Please Check Your Internet Connection and Try Again")
            .setCancelable(false)
            .setPositiveButton("Ok") { dialog, id -> }
        val alert = builder.create()
        alert.setTitle("Alert")
        alert.show()
    }

    fun encodeImage(bm: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun Base64toBitmap(encodedImage: String?): Bitmap {
        val decodedString =
            Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    //    public  static  void showFragment(Fragment fragment) {
    //
    //        FragmentTransaction ft = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
    //        ft.setCustomAnimations( R.anim.enter_from_right, R.anim.exit_from_left,R.anim.enter_from_left, R.anim.exit_from_right);
    //        ft.replace(R.id.nav_host_fragment, fragment);
    //        ft.addToBackStack(null);
    //        ft.commit();
    //
    //    }
    //    public  static boolean isValidMobile(String phone) {
    //        return phone.length()==10;
    //    }
    //
    fun getEditTextValue(value: EditText): String {
        return value.text.toString().trim { it <= ' ' }.uppercase(Locale.getDefault())
    }

    fun getUniqueIMEIId(): String {
        try {
            val telephonyManager =
                mContext!!.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (ActivityCompat.checkSelfPermission(
                    mContext!!,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return ""
            }
            val imei = telephonyManager.deviceId
            Log.e("imei", "=$imei")
            return if (imei != null && !imei.isEmpty()) {
                imei
            } else {
                Build.SERIAL
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "not_found"
    }

    fun getVisibleFragment(): Fragment? {
        val fragmentManager = (mContext as FragmentActivity?)!!.supportFragmentManager
        @SuppressLint("RestrictedApi") val fragments = fragmentManager.fragments
        if (fragments != null) {
            for (fragment in fragments) {
                if (fragment != null && fragment.isVisible) return fragment
            }
        }
        return null
    }

    fun isValidMobile(phone: String): Boolean {
        return if (!Pattern.matches("[a-zA-Z]+", phone)) {
            phone.length > 9 && phone.length <= 10
        } else false
    }

    //    public static   void checkPermission()
    //    {
    //        Dexter.withActivity((Activity) mContext)
    //                .withPermissions(Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE
    //                        )
    //                .withListener(new MultiplePermissionsListener() {
    //                    @Override
    //                    public void onPermissionsChecked(MultiplePermissionsReport report) {
    //                        if (report.areAllPermissionsGranted()) {
    //                            // showImagePickerOptions();
    //                        }
    //
    //                        if (report.isAnyPermissionPermanentlyDenied()) {
    //                            //Common.showSettingsDialog();
    //                        }
    //                    }
    //
    //                    @Override
    //                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
    //
    //                    }
    //
    //
    //
    //                }).check();
    //
    //    }
    fun getLast7DaysDate(): String {
        dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(Date(Date().time - 7 * 24 * 60 * 60 * 1000))
    }

    fun getVIewLast7DaysDate(): String {
        dateFormat = SimpleDateFormat("MM-dd-yyyy")
        return dateFormat.format(Date(Date().time - 7 * 24 * 60 * 60 * 1000))
    }

    fun getFormat(mformat: String?): String? {
        var outputDateStr: String? = null
        try {
            val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val outputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
            val date = inputFormat.parse(mformat)
            outputDateStr = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return outputDateStr
    }

    fun getLocalBitmapUri(bmp: Bitmap, context: Context): Uri? {
        var bmpUri: Uri? = null
        try {
            val file = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "share_image_" + System.currentTimeMillis() + ".png"
            )
            val out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()
            bmpUri = Uri.fromFile(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri
    } //    public static void playSound(Context mContext){
    //        final MediaPlayer mp = MediaPlayer.create(mContext, R.raw.error_sound);
    //        mp.start();
    //    }
}