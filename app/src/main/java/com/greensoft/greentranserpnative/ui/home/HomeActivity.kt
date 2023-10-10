package com.greensoft.greentranserpnative.ui.home

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.greensoft.greentranserpnative.ENV
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityHomeBinding
import com.greensoft.greentranserpnative.ui.home.models.UserMenuModel
import com.greensoft.greentranserpnative.ui.login.LoginActivity
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import com.greensoft.greentranserpnative.ui.operation.call_register.CallRegisterActivity
import com.greensoft.greentranserpnative.ui.operation.pickup_manifest.PickupManifestEntryActivity
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.PickupReferenceActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.InputStream
import java.util.concurrent.Executors
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity   @Inject constructor(): BaseActivity(), OnRowClick<Any> {

    lateinit var activityBinding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private var  menuList: ArrayList<UserMenuModel> = ArrayList()
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var adapter: UserMenuAdapter
    val executor = Executors.newSingleThreadExecutor()
    val handler = Handler(Looper.getMainLooper())
    var image: Bitmap? = null
    val CAMERA_REQUEST_CODE: Int = 10001

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
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        activityBinding.compLogo.setImageBitmap(bitmap)
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
        if(ENV.DEBUGGING) {
            activityBinding.testDebugging.visibility = View.VISIBLE
        }
        setupUi()
        setObserver()
        setOnClicks()
    }
    override fun onResume() {
        super.onResume()
        refreshData()
    }

     private fun refreshData(){
         getUserMenu()
         setupUi()
     }
    private fun getUserMenu(){
        viewModel.getUserMenu(
            loginDataModel?.companyid.toString(),
            "greentransapp_usermenu_native_WMS",
            listOf("prmusercode","prmapp"),
            arrayListOf(userDataModel?.usercode.toString(), ENV.APP_NAME)
        )
    }

     private fun setupUi(){
         activityBinding.compName.text = loginDataModel!!.compname.toString()
         activityBinding.loginBranch.text = userDataModel!!.loginbranchname.toString()

         executor.execute {

             val imageURL = loginDataModel!!.logoimage.toString()
             if(imageURL == null){
                 showProgressDialog()
             }
             try {
                 val `in` = java.net.URL(imageURL).openStream()
                 image = BitmapFactory.decodeStream(`in`)

                 handler.post {
                     activityBinding.compLogo.setImageBitmap(image)
                 }
             }

             catch (e: Exception) {
                 e.printStackTrace()
             }
         }

    }
    private fun setObserver() {
        capturedImage.observe(this) { imageUri ->
            successToast("HOME_ACTIVITY ${imageUri.path}")
            activityBinding.compLogo.setImageURI(imageUri)
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
          linearLayoutManager = LinearLayoutManager(this)
          activityBinding.recyclerView.apply {
              layoutManager = linearLayoutManager
              adapter = UserMenuAdapter(menuList, this@HomeActivity)

          }

     }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    private fun dispatchTakePictureIntent() {
//        Intent(Intent.ACTION_,MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
//            takePictureIntent.resolveActivity(packageManager)?.also {
//                // Create the File where the photo should go
//                val photoFile: File? = try {
//                    createImageFile()
//                } catch (ex: IOException) {
//                    // Error occurred while creating the File
//                    ...
//                    null
//                }
//                // Continue only if the File was successfully created
//                photoFile?.also {
//                    val photoURI: Uri = FileProvider.getUriForFile(
//                        this,
//                        "com.example.android.fileprovider",
//                        it
//                    )
//                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//                }
//            }
//            resultLauncher.launch(takePictureIntent)
//        }
    }

    private fun testFunction() {
        val intent=Intent(this,PickupManifestEntryActivity::class.java)
        startActivity(intent)
//        dispatchTakePictureIntent()
//        selectImage()
//        val intent = Intent(this, CameraX::class.java)
//        startActivity(intent)
//        val frag = CameraXFullscreenFragment()
//        supportFragmentManager.beginTransaction().add(frag, "TEST").commit()
    }
    private fun setOnClicks() {
        activityBinding.testDebugging.setOnClickListener {
        //    successToast("Test")
            testFunction()
        }
        activityBinding.btnLogOut.setOnClickListener {
//            successToast(packageName)
           logOut()
        }
//        activityBinding.booking.setOnClickListener {
////            successToast(packageName)
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


    override fun onCLick(data: Any, clickType: String) {
        val gson = Gson()
        val jsonSerialized = gson.toJson(data)
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
       }
    }
}