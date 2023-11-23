package com.greensoft.greentranserpnative.ui.navDrawer

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import com.greensoft.greentranserpnative.R
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding=ActivityProfileBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setSupportActionBar(activityBinding.toolBar.root)
        setUpToolbar("PROFILE")
        setUserDetails()
        setOnClick()
    }

      fun setOnClick(){
         activityBinding.logOutBtn.setOnClickListener {
             logOut()
         }
     }
      fun  setUserDetails() {
          activityBinding.branchName.text = userDataModel!!.loginbranchname.toString()
          activityBinding.userName.setText(userDataModel!!.username.toString())
          activityBinding.userMobile.setText(loginDataModel!!.mobileno.toString())
          activityBinding.userEmail.setText(loginDataModel!!.emailusername.toString())
          executor.execute {
              val imageURL = loginDataModel!!.logoimage.toString()
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

}
