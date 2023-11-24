package com.greensoft.greentranserpnative.ui.navDrawer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityProfileBinding
import com.greensoft.greentranserpnative.ui.login.LoginActivity
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity  @Inject constructor(): BaseActivity(), BottomSheetClick<Any> {
    lateinit var activityBinding:ActivityProfileBinding
    val handler = Handler(Looper.getMainLooper())
    val executor = Executors.newSingleThreadExecutor()
    var image: Bitmap? = null
    var REQUEST_PHONE_CALL= 1
    var mobileNo: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding=ActivityProfileBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("PROFILE")
        setUserDetails()
        setOnClick()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PHONE_CALL){
            makeCall(mobileNo)
        }
    }

      fun setOnClick(){
//         activityBinding.userMobile.setOnClickListener {
//             if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                 ActivityCompat.requestPermissions(
//                     this,
//                     arrayOf(android.Manifest.permission.CALL_PHONE),
//                     REQUEST_PHONE_CALL
//                 )
//             }
//             else {
//                 makeCall(mobileNo)
//             }
//         }
         activityBinding.logOutBtn.setOnClickListener {
             logOut()
         }
     }
      fun setUserDetails() {
          if(loginDataModel == null && userDataModel == null) {
            errorToast("Something went wrong. Please login again.")
          } else {
              activityBinding.userMobile.paintFlags = activityBinding.userMobile.paintFlags or Paint.UNDERLINE_TEXT_FLAG
              activityBinding.branchName.text = userDataModel!!.loginbranchname.toString()
              activityBinding.userName.text = userDataModel!!.username.toString()
              activityBinding.userMobile.text = loginDataModel!!.mobileno.toString()
              mobileNo = loginDataModel!!.mobileno.toString()
              activityBinding.userEmail.text = loginDataModel!!.emailusername.toString()
              executor.execute {
                  val imageURL = loginDataModel!!.logoimage
                  if (imageURL == null) {
                      showProgressDialog()
                  }
                  try {
                      val `in` = java.net.URL(imageURL).openStream()
                      image = BitmapFactory.decodeStream(`in`)

                      handler.post {
                          activityBinding.companyLogo.setImageBitmap(image)

                      }
                  } catch (e: Exception) {
                      e.printStackTrace()
                  }
              }
          }
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

    override fun onItemClick(data: Any, clickType: String) {
        TODO("Not yet implemented")
    }

    private fun makeCall(mobNo: String?){
        var mutableMobileNo = mobNo
        if(mutableMobileNo == null) {
            if(loginDataModel == null) {
                errorToast("Something went wrong. Please login again.")
                return
            } else {
                mutableMobileNo = loginDataModel!!.mobileno
                if(mutableMobileNo.isNullOrBlank()) {
                    errorToast("Something went wrong. Please login again.")
                    return
                }
            }
        }
        val intent=Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$mobNo")
        if (ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            errorToast("Permission denied")
            return
        }
        startActivity(intent)
    }

}
