package com.greensoft.greentranserpnative.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.login.models.BranchModel
import com.greensoft.greentranserpnative.ui.login.models.LoginDataModel
import com.greensoft.greentranserpnative.ui.login.models.UserDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val _repo: LoginRepository): BaseViewModel() {
    init {
        isError = _repo.isError
    }

    val viewDialogLiveData: LiveData<Boolean>
        get()= _repo.viewDialogLiveData

    val loginUserLiveData: LiveData<LoginDataModel>
        get() = _repo.loginUserLiveData

//    val userMastLiveData: LiveData<UserMastUserDataModel>
//        get() = _repo.userMastLiveData

    val branchListLiveData: LiveData<ArrayList<BranchModel>>
        get() = _repo.branchListLiveData

    val userDataLiveData: LiveData<UserDataModel>
        get() = _repo.userDataLiveData

    val checkUserCredsLiveData: LiveData<LoginDataModel>
        get() = _repo.checkUserCredsLiveData

    fun login(userName: String, password: String, appversion: String, appversiondt: String, devicedt: String, deviceid: String){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.userLogin(userName, password, appversion, appversiondt, devicedt, deviceid)
        }
    }

    fun checkUserCredsForLoggedInUser(userName: String, password: String, appversion: String, appversiondt: String, devicedt: String, deviceid: String){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.checkCredsAlreadyLoggedInUser(userName, password, appversion, appversiondt, devicedt, deviceid)
        }
    }

    fun getBranchList(companyId: String, userName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.branchSelectionForLoginData(companyId, userName)
        }
    }

    fun validateSelectedBranch(companyId: String, userName: String, branchCode: String, appVersion: String,
                                       appVersionDt: String, deviceId: String){
        viewModelScope.launch (Dispatchers.IO) {
            _repo.validateSelectedBranch(
                companyId,
                userName,
                branchCode,
                appVersion,
                appVersionDt,
                deviceId
            )
        }
    }


//    fun getUserMastUserData(companyId: String, userName: String){
//        viewModelScope.launch(Dispatchers.IO) {
//            _repo.userMastUserDataAfterLogin(companyId, userName)
//        }
//    }

//    fun branchSelectionForLoginData(companyId: String, username: String){
//        viewModelScope.launch(Dispatchers.IO) {
//            repo.branchSelectionForLoginData(companyId, username)
//        }
//    }
//    fun ParkingForLogin(companyid: String, branchcode: String){
//        viewModelScope.launch(Dispatchers.IO) {
//            repo.parkingSelectionForLoginData(companyid, branchcode)
//        }
//    }
}

//class ViewModelFactory(val loginRepository: LoginRepository): ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return LoginViewModel(loginRepository) as T
//    }
//}
