package com.greensoft.greentranserpnative.ui.home

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.greensoft.greentranserpnative.ENV
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityHomeBinding
import com.greensoft.greentranserpnative.ui.operation.call_register.CallRegisterActivity
import com.greensoft.greentranserpnative.ui.operation.pickup_reference.PickupReferenceActivity
import com.greensoft.greentranserpnative.ui.home.models.UserMenuModel
import com.greensoft.greentranserpnative.ui.login.LoginActivity
import com.greensoft.greentranserpnative.ui.onClick.OnRowClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
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
    private fun setOnClicks() {
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