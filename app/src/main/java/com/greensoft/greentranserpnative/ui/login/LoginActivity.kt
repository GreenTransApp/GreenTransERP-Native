package com.greensoft.greentranserpnative.ui.login

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter.AllCaps
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.greensoft.greentranserpnative.ENV
import com.greensoft.greentranserpnative.base.BaseActivity
import com.greensoft.greentranserpnative.databinding.ActivityLoginBinding
import com.greensoft.greentranserpnative.ui.bottomsheet.common.models.CommonBottomSheetModel
import com.greensoft.greentranserpnative.ui.home.HomeActivity
import com.greensoft.greentranserpnative.ui.login.models.BranchModel
import com.greensoft.greentranserpnative.ui.onClick.BottomSheetClick
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity @Inject constructor(): BaseActivity(), BottomSheetClick<Any> {
    private lateinit var  activityBinding: ActivityLoginBinding
    val TAG = "LoginActivity"
    private var saveLogin : Boolean = false
    private var isLogin : Boolean = false
    private val viewModel: LoginViewModel by viewModels()
//    private lateinit var loginPrefs: SharedPreferences;
//    private lateinit var loginPrefsEditor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        mContext = this
        activityBinding.inputUsername.filters = activityBinding.inputUsername.filters + AllCaps()
        activityBinding.inputPassword.filters = activityBinding.inputPassword.filters + AllCaps()
        setObservers()

//        loginPrefs = getSharedPreferences(ENV.LOGIN_PREF_TAG, MODE_PRIVATE)
//        loginPrefsEditor = loginPrefs.edit()

        saveLogin = getStorageBoolean(ENV.SAVE_LOGIN_PREF_TAG);
        isLogin = getStorageBoolean(ENV.IS_LOGIN_PREF_TAG)
        if (saveLogin) {
            getLoginData()
            getUserData()
            if(userDataModel != null) {
                activityBinding.inputUsername.setText(
                    userDataModel!!.username
                )
                activityBinding.inputPassword.setText(
                    userDataModel!!.password
                )
                activityBinding.cboxRememberMe.isChecked = true
            }
            if(isLogin) {
                if(userDataModel != null && loginDataModel != null) {
                    checkUserCreds()
                }
            }
        }
        else if(isLogin) {
            getLoginData()
            getUserData()
            if(userDataModel != null && loginDataModel != null) {
                checkUserCreds()
            }
        }
        if(ENV.DEBUGGING) {
            activityBinding.inputUsername.text = Editable.Factory().newEditable(ENV.DEBUG_USERNAME)
            activityBinding.inputPassword.text = Editable.Factory().newEditable(ENV.DEBUG_PASSWORD)
        }



        activityBinding.btnLogin.setOnClickListener {
            if (activityBinding.cboxRememberMe.isChecked) {
                saveStorageBoolean(ENV.SAVE_LOGIN_PREF_TAG, true);
                saveStorageString(
                    ENV.LOGIN_USERNAME_PREF_TAG,
                    activityBinding.inputUsername.text.toString()
                )
                saveStorageString(
                    ENV.LOGIN_PASSWORD_PREF_TAG,
                    activityBinding.inputPassword.text.toString()
                )
            } else {
                clearStorage()
            }
            if(activityBinding.inputUsername.text.toString().trim().isEmpty()){
                errorToast("User Name Required!");
            }else if(activityBinding.inputPassword.text.toString().trim().isEmpty()){
                errorToast("Password Required!");
            }else{
                login()
            }
        }
    }
    private fun setObservers(){
        viewModel.isError.observe(this) { errMsg ->
            errorToast(errMsg)
        }
        viewModel.viewDialogLiveData.observe(this, Observer { show ->
//            progressBar.visibility = if(show) View.VISIBLE else View.GONE
            if(show) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })

        viewModel.loginUserLiveData.observe(this) { loginModel ->
            saveStorageString(
                ENV.LOGIN_DATA_PREF_TAG,
                Gson().toJson(loginModel)
            )
            viewModel.getBranchList(
                loginModel.connstring,
                loginModel.username
            )
        }

        viewModel.checkUserCredsLiveData.observe(this) { loginModel ->
            saveStorageString(
                ENV.LOGIN_DATA_PREF_TAG,
                Gson().toJson(loginModel)
            )
            viewModel.validateSelectedBranch(
                loginModel.companyid.toString(),
                loginModel.username.toString(),
                userDataModel?.loginbranchcode!!,
                ENV.APP_VERSION,
                ENV.APP_VERSION_DATE,
                ENV.DEVICE_ID
            )
        }

//        viewModel.userMastLiveData.observe(this) { userData ->
//            saveStorage(
//                ENV.USER_DATA_PREF_TAG,
//                Gson().toJson(userData)
//            )
//            successToast(userData.displayname)
//
////            goToHomeActivity()
//        }


        viewModel.branchListLiveData.observe(this) { branchList ->
                openBranchSelectionBottomSheet(branchList)
        }

        viewModel.userDataLiveData.observe(this) { userDataModel ->
            saveStorageString(
                ENV.USER_DATA_PREF_TAG,
                Gson().toJson(userDataModel)
            )
            goToHomeActivity()
        }

//        loginViewModel.parkingList.observe(this, Observer { result ->
//            parkingList = result.Table
//            var toPushParkingList = ArrayList<String>()
//            for(i in parkingList.indices){
//                toPushParkingList.add(parkingList[i].parkingname)
//            }
//            Log.d(TAG, parkingList[0].toString())
//            val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, toPushParkingList)
//            parkingAutoCompleteTextView.setAdapter(adapter)

//            loginPrefsEditor.putString("ParkingList", parkingListJson)
//            loginPrefsEditor.commit()
//        })
    }

    private fun openBranchSelectionBottomSheet(rvList: ArrayList<BranchModel>){
        val commonList = ArrayList<CommonBottomSheetModel<Any>>()
        for(i in 0 until rvList.size) {
            commonList.add(CommonBottomSheetModel(rvList[i].branchname, rvList[i]))
        }
        openCommonBottomSheet(this, "Branch Selection", this, commonList)
    }
    private fun goToHomeActivity(){
//        val loginDataJson = Gson().toJson(loginModel)
//
        saveStorageBoolean(ENV.IS_LOGIN_PREF_TAG, true)
//        saveStorage (
//            ENV.USER_DATA_PREF_TAG,
//            loginDataJson
//        )
//        loginPrefsEditor.commit()
//        Log.d(TAG, "Login")

//        val intent = Intent(this, MainActivity::class.java)
//        val intent = Intent(this, BookingActivity::class.java)
//        val intent = Intent(this, PickupReferenceActivity::class.java)
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun login(){
        viewModel.login(
            userName = activityBinding.inputUsername.text.toString(),
            password = activityBinding.inputPassword.text.toString(),
            appversion= ENV.APP_VERSION,
            appversiondt= ENV.APP_VERSION_DATE,
            devicedt=ENV.APP_VERSION_DATE,
            deviceid= ENV.DEVICE_ID
        )
    }

    private fun checkUserCreds(){
        viewModel.checkUserCredsForLoggedInUser(
            userName = loginDataModel?.username!!,
            password = userDataModel?.password!!,
//            password = loginDataModel?.p,
            appversion= ENV.APP_VERSION,
            appversiondt= ENV.APP_VERSION_DATE,
            devicedt=ENV.APP_VERSION_DATE,
            deviceid= ENV.DEVICE_ID
        )
    }


//    fun showBranchSelectionAlert() {
//        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
//        val customLayout: View =  LayoutInflater.from(this)
//            .inflate(
//                R.layout.branch_selection,
//                null
//            )
//        builder.setView(customLayout)
//        branchAutoCompleteTextView = customLayout.findViewById(R.id.et_bs_branch)
//        parkingAutoCompleteTextView = customLayout.findViewById(R.id.et_bs_parking)
//
//        var toPushBranchList = ArrayList<String>()
//        for(i in branchList.indices){
//            toPushBranchList.add(branchList[i].branchname)
//        }
//
//        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, toPushBranchList)
//        branchAutoCompleteTextView.setAdapter(adapter)
//
//
//        val submitButton: Button= customLayout.findViewById(R.id.btn_bs_submit)
//
//        submitButton.setOnClickListener {
//            if(isBranchSelected){
//                if(isParkingSelected){
//                    goToHomeActivity()
//                }
//            }
//        }


//        branchAutoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
//            if(isParkingSelected) isParkingSelected = false
//            isBranchSelected = true;
//            loginViewModel.ParkingForLogin(userTableAPIResponse.Table[0].connstring, branchList[position].branchcode)
//        }
//        parkingAutoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
//            isParkingSelected = true
//        }

//        val dialog: AlertDialog = builder.create()
//
//        dialog.show()
//    }

//    private fun successToast(msg: String?) {
//        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
//    }
//
//    private fun errorToast(msg: String?) {
//        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
//    }

    override fun onBackPressed() {
        this.moveTaskToBack(true);
    }

    override fun onItemClick(data: Any, clickType: String) {
        if(clickType.uppercase(Locale.getDefault()) == "BRANCH SELECTION") {
            val selectedBranch = data as BranchModel
//            successToast(selectedBranch.branchname + selectedBranch.branchcode)
            validateSelectedBranch(selectedBranch)
        }
    }

    private fun validateSelectedBranch(branchListModel: BranchModel) {
        val loginDataModel = getLoginData()
        if(loginDataModel != null) {
            viewModel.validateSelectedBranch(
                loginDataModel.companyid.toString(),
                activityBinding.inputUsername.text.toString(),
                branchListModel.branchcode,
                ENV.APP_VERSION,
                ENV.APP_VERSION_DATE,
                ENV.DEVICE_ID
            )
        } else {
            errorToast("Some Error Occurred.")
        }
    }

//    fun isNetworkConnected(): Boolean {
//        val cm = getSystemService<Any>(CONNECTIVITY_SERVICE) as ConnectivityManager
//        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
//    }


}