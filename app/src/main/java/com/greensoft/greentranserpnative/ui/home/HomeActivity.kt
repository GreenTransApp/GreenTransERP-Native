package com.greensoft.greentranserpnative.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.greensoft.greentranserpnative.ENV
import com.greensoft.greentranserpnative.R
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityHomeBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.bookingIndentInfoBottomSheet.BookingIndentInfoBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.printGR.PrintGrBottomSheet
import com.greensoft.greentranserpnative.ui.bottomsheet.vehicleSelection.VehicleSelectionBottomSheet
import com.greensoft.greentranserpnative.ui.home.models.UserMenuModel
import com.greensoft.greentranserpnative.ui.login.LoginActivity
import com.greensoft.greentranserpnative.ui.navDrawer.ProfileActivity
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.call_register.CallRegisterActivity
import com.greensoft.greentranserpnative.ui.operation.communicationList.CommunicationListActivity
import com.greensoft.greentranserpnative.ui.operation.communicationList.models.CommunicationListModel
import com.greensoft.greentranserpnative.ui.operation.delivery.DeliveryOptionActivity
import com.greensoft.greentranserpnative.ui.operation.despatch_manifest.DespatchManifestEntryActivity
import com.greensoft.greentranserpnative.ui.operation.drs.DRSActivity
import com.greensoft.greentranserpnative.ui.operation.notificationPanel.model.NotificationPanelBottomSheetModel
import com.greensoft.greentranserpnative.ui.operation.loadingSlip.newScanLoad.NewScanAndLoadActivity
import com.greensoft.greentranserpnative.ui.operation.outstation_unarrived.OutstationInscanListActivity
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.PickupManifestEntryActivity
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.PickupReferenceActivity
import com.greensoft.greentranserpnative.ui.operation.unarrived.InscanListActivity
import com.greensoft.greentranserpnative.ui.operation.upload_image.UploadBookingImageActivity
import com.greensoft.greentranserpnative.ui.operation.upload_image.UploadPodImageActivity
import com.greensoft.greentranserpnative.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.InputStream
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.system.exitProcess


@AndroidEntryPoint
class HomeActivity   @Inject constructor(): BaseActivity(), OnRowClick<Any>, NavigationView.OnNavigationItemSelectedListener {

    lateinit var activityBinding: ActivityHomeBinding
    private var communicationCardList: ArrayList<CommunicationListModel> = ArrayList()
    private val viewModel: HomeViewModel by viewModels()
    lateinit var bottomSheetDialog: BottomSheetDialog;
    private var  menuList: ArrayList<UserMenuModel> = ArrayList()
//    private var notificationDetailList: ArrayList<NotificationPanelBottomSheetModel> = ArrayList()
//    lateinit var layoutManager: LinearLayoutManager
    lateinit var layoutManager: GridLayoutManager
    lateinit var adapter: UserMenuAdapter
    val executor = Executors.newSingleThreadExecutor()
    val handler = Handler(Looper.getMainLooper())
    var image: Bitmap? = null
    val CAMERA_REQUEST_CODE: Int = 10001
    val fromDt: String = "2023-09-11"
    val toDt: String = "2023-10-11"
//    lateinit var badgeDrawable: BadgeDrawable
    private  var transactionId:String = "884042"

    var resultLauncher: ActivityResultLauncher<Intent>
    = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            if(data != null) {
//                val imageBitmap = data.extras?.getString("data") as Bitmap
//                val dataString: String? = data.extras?.getString("data")
                val dataString: Uri? = data.data
                if(dataString != null) {
                    try {
                        val inputStream: InputStream? =
                            contentResolver.openInputStream(dataString)
//                        --------- compLogo ----------
                        val bitmap = BitmapFactory.decodeStream(inputStream)
//                        activityBinding.compLogo.setImageBitmap(bitmap)
                        val selectedImagePath = getPathFormatUri(dataString)
                    } catch (e: java.lang.Exception) {
                        Toast.makeText(this, e.message, Toast.LENGTH_SHORT)
                            .show()
                    }
//                    val imageBitmap: Bitmap = dataString as Bitmap
//                    activityBinding.compLogo.setImageBitmap(imageBitmap)
                } else {
                    errorToast("Unable to get the proper image.")
                }
            }
        }
    }

    private fun getPathFormatUri(contentUri: Uri): String? {
        val filePath: String?
        val cursor: Cursor? = contentResolver
            .query(contentUri, null, null, null, null)
        if (cursor == null) {
            filePath = contentUri.path
        } else {
            cursor.moveToFirst()
            val index: Int = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
         activityBinding.branchName.text = userDataModel!!.loginbranchname.toString()
//
        if(ENV.DEBUGGING) {
            activityBinding.testDebugging.visibility = View.VISIBLE
        }
        setupUi()
        setObserver()
        setOnClicks()
        requestCameraPermission()
        if(!isScannerWorking()) {
            errorToast(ENV.SCANNER_NOT_WORKING_MSG)
        }
    }
    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun getNotificationPanelList() {
        Log.e("NOTI PANEL", "getNotificationPanelList: " )
        viewModel.getNotificationPanelList(
            loginDataModel?.companyid.toString(),
            "gtapp_getnotificationpanel",
            listOf("prmusercode","prmsessionid"),
            arrayListOf(userDataModel?.usercode.toString(), userDataModel?.sessionid.toString())
        )
    }

     private fun refreshData(){
         getUserMenu()
     }
    private fun getUserMenu(){
//            "greentransapp_usermenu_native_WMS",
//            listOf("prmusercode","prmapp"),
        viewModel.getUserMenu(
            loginDataModel?.companyid.toString(),
            "gtapp_erpnative_jeenadashboard",
            listOf("prmusercode","prmloginbranchcode","prmfromdt","prmtodt","prmappversion"),
            arrayListOf(userDataModel?.usercode.toString(), userDataModel?.loginbranchcode.toString(), fromDt, toDt ,ENV.APP_VERSION)
        )
    }

    private fun setNotificationCounter(counter: Int) {
//        badgeDrawable.number = counter
//        val param = activityBinding.notificationCounter.layoutParams as ViewGroup.MarginLayoutParams
        var counterAsString = counter.toString()
        if(counter > 999) {
            counterAsString = "999+"
        }
//        if(counter < 10) {
//            param.setMargins(0,0,12,0)
//        } else if (counter < 100) {
//            param.setMargins(0,0, 8,0)
//        } else if (counter < 1000) {
//            param.setMargins(0, 0 , 2, 0)
//        } else {
//            param.setMargins(0, 0 , 0, 0)
//            counterAsString = "999+"
//        }
//        activityBinding.notificationCounter.layoutParams = param
        activityBinding.notificationCounter.text = counterAsString
    }
     private fun setupUi(){
//         badgeDrawable = BadgeDrawable.create(this@HomeActivity)
         //Important to change the position of the Badge
//         badgeDrawable.horizontalOffset = 60
//         badgeDrawable.verticalOffset = 30
//         badgeDrawable.horizontalOffset = 40
//         badgeDrawable.verticalOffset = 20
         activityBinding.navigationView.setNavigationItemSelectedListener(this)
         setNotificationCounter(0)
//         activityBinding.notificationBtn.viewTreeObserver
//             .addOnGlobalLayoutListener(@ExperimentalBadgeUtils object : OnGlobalLayoutListener {
//                 override fun onGlobalLayout() {
//                     BadgeUtils.attachBadgeDrawable(badgeDrawable, activityBinding.notificationBtn, null)
//                     activityBinding.notificationBtn.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                 }
//             })

         //set header of nav drawer
         val navigationView=activityBinding.navigationView
         val headerView : View = navigationView.getHeaderView(0)
         val companyName=headerView.findViewById<TextView>(R.id.company_name)
         val branchName=headerView.findViewById<TextView>(R.id.loginBranch)
         val compLogo=headerView.findViewById<ImageView>(R.id.company_logo)
         companyName.setText(loginDataModel?.compname.toString())
         branchName.setText(userDataModel?.loginbranchname.toString())

//         activityBinding.compName.text = loginDataModel!!.compname.toString()
//         activityBinding.loginBranch.text = userDataModel!!.loginbranchname.toString()

         executor.execute {

             val imageURL = loginDataModel!!.logoimage.toString()
             if(imageURL == null){
                 showProgressDialog()
             }
             try {
                 val `in` = java.net.URL(imageURL).openStream()
                 image = BitmapFactory.decodeStream(`in`)

                 handler.post {
//                     activityBinding.compLogo.setImageBitmap(image)
                     compLogo.setImageBitmap(image)

                 }
             }

             catch (e: Exception) {
                 e.printStackTrace()
             }
         }

    }




    override fun onBackPressed() {
        exitAlert()
    }

    private fun exitAlert() {
     AlertDialog.Builder(this)
        .setTitle("Alert!!!")
        .setMessage("Are you sure you want to exit the app?")
        .setPositiveButton("Yes") { _, _ ->
            moveTaskToBack(true);
            exitProcess(-1)
        }
        .setNeutralButton("No") { _, _ -> }
        .show()
    }

    private fun setObserver() {

        capturedImage.observe(this) { imageUri ->
//            successToast("HOME_ACTIVITY ${imageUri.path}")
//            activityBinding.compLogo.setImageURI(imageUri)
        }
        activityBinding.refreshLayout.setOnRefreshListener {
            refreshData()
            lifecycleScope.launch {
                delay(1500)
                activityBinding.refreshLayout.isRefreshing = false
            }
        }
        viewModel.isError.observe(this) { errMsg ->
            errorToast(errMsg)
        }
        viewModel.viewDialogLiveData.observe(this, Observer { show ->
            if (show) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })
        viewModel.menuLiveData.observe(this) { menu ->
            menuList = menu
            setupRecyclerView()
        }

        viewModel.notificationLiveData.observe(this) { notificationData ->
//            successToast(notificationData.totalnoti.toString())
            if(notificationData.commandstatus == 1) {
                setNotificationCounter(notificationData.totalnoti)
            }
        }

        viewModel.notificationPanelListLiveData.observe(this) { panelList ->
            if (panelList.isNotEmpty()) {
                if(panelList[0].commandstatus == 1) {
                    openNotificationBottomSheet(panelList)
                }
            }
        }

        mScanner.observe(this){data->
            Companion.successToast(mContext,data)
//            playSound()
        }
    }

//    fun setOnClicks() {
//        activityBinding.cameraTest.setOnClickListener {
////            successToast(packageName)
////            successToast(isNetworkConnected().toString())
//            successToast(getConnectionType().toString())
//        }
//
//
//    }

      private fun setupRecyclerView(){
//          layoutManager = LinearLayoutManager(this)
        layoutManager = GridLayoutManager(this, 2)
          activityBinding.recyclerView.layoutManager = this.layoutManager
          activityBinding.recyclerView.adapter = UserMenuAdapter(menuList, this@HomeActivity)
//              .apply {
//              layoutManager = layoutManager
//              adapter = UserMenuAdapter(menuList, this@HomeActivity)
//
//          }

     }


    private fun testFunction() {
//        val intent=Intent(this,InScanDetailWithScannerActivity::class.java)
        val intent=Intent(this,DRSActivity::class.java)
        startActivity(intent)
//        Utils.changeDateFormatForEway("28/08/2023 02:37:00 PM")
//        Utils.changeDateFormatForEway("08/08/2023 09:31:00 AM")
        Utils.changeDateFormatForEwayInvoiceDt("30-10-2023")
//        dispatchTakePictureIntent()
//        selectImage()
//        val intent = Intent(this, CameraX::class.java)
//        startActivity(intent)
//        val frag = CameraXFullscreenFragment()
//        supportFragmentManager.beginTransaction().add(frag, "TEST").commit()

//        openBookingIndentInfoBottomSheet(this,"Booking Indent Information",this,)

    }

    private fun setOnClicks() {
        activityBinding.btnUploadPodImage.setOnClickListener {
            val intent=Intent(this,UploadPodImageActivity::class.java)
            startActivity(intent)
        }
        activityBinding.btnUploadBookingImage.setOnClickListener {
            val intent=Intent(this, UploadBookingImageActivity::class.java)
            startActivity(intent)
        }
        activityBinding.notiImg.setOnClickListener {
//            showNotificationModelBottomSheet()
//            openNotificationBottomSheet(notificationDetailList)
            getNotificationPanelList()
        }
        activityBinding.toolbar.setNavigationOnClickListener {
            if(activityBinding.myDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                activityBinding.myDrawerLayout.closeDrawer(GravityCompat.START)
            } else {
                activityBinding.myDrawerLayout.openDrawer(GravityCompat.START)
            }
        }
        activityBinding.testDebugging.setOnClickListener {
            testFunction()

        }
//        activityBinding.btnLogOut.setOnClickListener {
//           logOut()
//        }
//        activityBinding.booking.setOnClickListener {
////            val intent = Intent(this, CallRegisterActivity::class.java)
//            val intent = Intent(this, PickupReferenceActivity::class.java)
//            startActivity(intent)
//        }
    }
    private fun logOut(){
        AlertDialog.Builder(this)
            .setTitle("Alert!!!")
            .setMessage("Are you sure you want to LOG-OUT?")
            .setPositiveButton("Yes") { _,_ ->
                  clearStorage()
//                loginPrefsEditor.putBoolean("isLogin", false)
//                loginPrefsEditor.commit()
                var intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            .setNegativeButton("No") { _,_ ->}
            .show()
    }


    override fun onClick(data: Any, clickType: String) {
        val gson = Gson()
        val jsonSerialized = gson.toJson(data)
        val menuModel: UserMenuModel = data as UserMenuModel
        Utils.menuModel = menuModel
        when(clickType){
//           "SELECTED_MENU"-> run{
//               val model: UserMenuModel = data as UserMenuModel
//               successToast(model.displayname)
//           }
            "GTAPP_ACCEPTJOBS" -> run {
                val intent = Intent(this, CallRegisterActivity::class.java)
                intent.putExtra("MENU_DATA", jsonSerialized)
                startActivity(intent)
            }
            "GTAPP_PENDINGINDENT" -> run {
                val intent = Intent(this, PickupReferenceActivity::class.java)
                intent.putExtra("MENU_DATA", jsonSerialized)
                startActivity(intent)
            }
            "GTAPP_LOADING" -> run {
                val intent = Intent(this, NewScanAndLoadActivity::class.java)
                intent.putExtra("MENU_DATA", jsonSerialized)
                startActivity(intent)
            }
            "GTAPP_NATIVEPICKUPMANIFEST" -> run {
                val intent = Intent(this, PickupManifestEntryActivity::class.java)
                intent.putExtra("MENU_DATA", jsonSerialized)
                startActivity(intent)
            }
            "GTAPP_PICKUPARRIVAL" -> run {
                val intent = Intent(this, InscanListActivity::class.java)
                intent.putExtra("MENU_DATA", jsonSerialized)
                startActivity(intent)
            }
            "GTAPP_LONGROUTEMANIFEST" -> run {
                val intent = Intent(this, DespatchManifestEntryActivity::class.java)
                intent.putExtra("MENU_DATA", jsonSerialized)
                startActivity(intent)
            }
            "GTAPP_LONGROUTEARRIVAL" -> run {
                val intent = Intent(this, OutstationInscanListActivity::class.java)
                intent.putExtra("MENU_DATA", jsonSerialized)
                startActivity(intent)
            }
            "GTAPP_DRSNATIVE" -> run {
                val intent = Intent(this, DRSActivity::class.java)
                Utils.drsNo = null
                intent.putExtra("MENU_DATA", jsonSerialized)
                startActivity(intent)
            }
            "GTAPP_DELIVERYNATIVE" -> run {
                val intent = Intent(this, DeliveryOptionActivity::class.java)
                intent.putExtra("MENU_DATA", jsonSerialized)
                startActivity(intent)
            }

            // it was moved from the main dashboard ( home page ) to search of
            //  Pending booking on the top right of the screen ( search list )
//            "GTAPP_GRLIST" -> run {
//                val intent = Intent(this, GrListActivity::class.java)
//                intent.putExtra("MENU_DATA", jsonSerialized)
//                startActivity(intent)
//            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_account -> run{
                var intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_settings -> run{

            }
            R.id.nav_logout-> run{
                logOut()
            }
        }
        return  true
    }

    private fun showNotificationModelBottomSheet() {
        val communication: TextView?
        val communicationListData: TextView?
        val countersCard: CardView?
//        val done: Button?
        var addImage: ExtendedFloatingActionButton?
        bottomSheetDialog = BottomSheetDialog(mContext)
        bottomSheetDialog.setContentView(R.layout.notification_model_bottomsheet)
        bottomSheetDialog.dismissWithAnimation = true
        communication = bottomSheetDialog.findViewById<TextView>(R.id.communication_tv)
        communicationListData = bottomSheetDialog.findViewById<TextView>(R.id.communication_data)
        countersCard = bottomSheetDialog.findViewById<CardView>(R.id.counters_card)

//        communicationListData?.setText(communicationCardList.size)
        countersCard?.setOnClickListener {
            val intent = Intent(this, CommunicationListActivity::class.java)
            startActivity(intent)
        }
//        addImage?.setOnClickListener {
//           bottomSheetDialog.dismiss()
//        }
//        setBottomSheetRV()
        bottomSheetDialog.show()
    }
    private fun openNotificationBottomSheet(rvList: ArrayList<NotificationPanelBottomSheetModel>) {
//        val commonList = ArrayList<NotificationPanelBottomSheetModel>()
//        for (i in 0 until rvList.size) {
//            commonList.add(NotificationPanelBottomSheetModel(rvList[i].notiname, i.toString()))
//
//        }
//        openCounterBottomSheet(this, "Notification Detail", this, commonList)
        openCounterBottomSheet(this, "Notification Panel", this, rvList)
    }

    private fun openBookingIndentInfoBottomSheet(mContext: Context, title: String, bottomSheetClick: OnRowClick<Any>,withAdapter: Boolean = false, index: Int = -1) {
//        showProgressDialog()
        val bottomSheetDialog = BookingIndentInfoBottomSheet.newInstance(
            mContext,
            title,
            transactionId,
            bottomSheetClick,
            withAdapter,
            index
        )
//        hideProgressDialog()
        bottomSheetDialog.show(supportFragmentManager, BookingIndentInfoBottomSheet.TAG)
    }
//    private fun generateSimpleList(): ArrayList<NotificationPanelBottomSheetModel> {
//        val dataList: ArrayList<NotificationPanelBottomSheetModel> =
//            java.util.ArrayList<NotificationPanelBottomSheetModel>()
//        for (i in 0..99) {
//            dataList.add(NotificationPanelBottomSheetModel("Test",i.toString()))
//        }
//        return dataList
//    }
}