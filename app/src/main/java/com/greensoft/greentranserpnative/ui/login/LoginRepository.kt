package com.greensoft.greentranserpnative.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.login.models.BranchModel
import com.greensoft.greentranserpnative.ui.login.models.LoginDataModel
import com.greensoft.greentranserpnative.ui.login.models.UserDataModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class LoginRepository @Inject constructor(): BaseRepository()  {
    final val gson = Gson()
    private val loginUserMutData = MutableLiveData<LoginDataModel>()
    private val loginBranchListMutData= MutableLiveData<ArrayList<BranchModel>>()
    private val validateBranchMutData= MutableLiveData<UserDataModel>()
    private val checkUserCredsMutData = MutableLiveData<LoginDataModel>()

    val viewDialogLiveData: LiveData<Boolean>
        get() = viewDialogMutData

    val loginUserLiveData: LiveData<LoginDataModel>
        get() = loginUserMutData

    val branchListLiveData: LiveData<ArrayList<BranchModel>>
        get() = loginBranchListMutData

    val userDataLiveData: LiveData<UserDataModel>
        get() = validateBranchMutData

    val checkUserCredsLiveData: LiveData<LoginDataModel>
        get() = checkUserCredsMutData
//    val userMastLiveData: LiveData<UserMastUserDataModel>
//        get() = userMastMutData
//
//    suspend fun userLogin(username: String, password: String, appversion: String, appversiondt: String, devicedt: String, deviceid: String) {
//        viewDialogMutData.postValue(true)
//        val resultData: Response<CommonResult>? = api.userLogin(username, password, appversion, appversiondt, devicedt, deviceid)
//        if(resultData?.body()!=null){
//            val result = resultData.body()!!
//            val gson = Gson()
//            if(result.CommandStatus == 1){
//                val jsonArray = getResult(result);
//                if(jsonArray != null) {
//                    val listType = object: TypeToken<List<LoginDataModel>>() {}.type
//                    val resultList: ArrayList<LoginDataModel> = gson.fromJson(jsonArray.toString(), listType);
//                    loginUserMutData.postValue(resultList[0]);
//
//                }
//            } else {
//                isError.postValue(result.CommandMessage.toString());
//            }
//        } else {
//            isError.postValue(SERVER_ERROR);
//        }
//        viewDialogMutData.postValue(false)
//
//    }



    fun userLogin(username: String, password: String, appversion: String, appversiondt: String, devicedt: String, deviceid: String) {
        viewDialogMutData.postValue(true)
        api.userLogin(username, password, appversion, appversiondt, devicedt, deviceid).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<LoginDataModel>>() {}.type
                            val resultList: ArrayList<LoginDataModel> = gson.fromJson(jsonArray.toString(), listType);
                            loginUserMutData.postValue(resultList[0]);

                        }
                    } else {
                        isError.postValue(result.CommandMessage.toString());
                    }
                } else {
                    isError.postValue(SERVER_ERROR);
                }
                viewDialogMutData.postValue(false)

            }

            override fun onFailure(call: Call<CommonResult?>, t: Throwable) {
                viewDialogMutData.postValue(false)
                isError.postValue(t.message)
            }

        })

    }


    fun checkCredsAlreadyLoggedInUser(username: String, password: String, appversion: String, appversiondt: String, devicedt: String, deviceid: String) {
        viewDialogMutData.postValue(true)
        api.userLogin(username, password, appversion, appversiondt, devicedt, deviceid).enqueue(object : Callback<CommonResult>  {
            override fun onResponse(call: Call<CommonResult>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<LoginDataModel>>() {}.type
                            val resultList: ArrayList<LoginDataModel> = gson.fromJson(jsonArray.toString(), listType);
                            checkUserCredsMutData.postValue(resultList[0]);

                        }
                    } else {
                        isError.postValue(result.CommandMessage.toString());
                    }
                } else {
                    isError.postValue(SERVER_ERROR);
                }
                viewDialogMutData.postValue(false)

            }

            override fun onFailure(call: Call<CommonResult>, t: Throwable) {
                viewDialogMutData.postValue(false)
                isError.postValue(t.message)
            }

        })

    }

//    suspend fun userMastUserDataAfterLogin(companyId: String, userName: String){
//        viewDialogMutData.postValue(true)
//        val resultData: Response<CommonResult>? = api.userDataLogin(companyId, userName)
//        if(resultData?.body()!=null){
//        val result = resultData.body()!!
//            val gson = Gson()
//            if(result.CommandStatus == 1){
//                val jsonArray: JSONArray? = getResult(result);
//                if(jsonArray != null) {
//                    val listType = object: TypeToken<List<UserMastUserDataModel>>() {}.type
//                    val resultList: ArrayList<UserMastUserDataModel> = gson.fromJson(jsonArray.toString(), listType);
//                    userMastMutData.postValue(resultList[0]);
//
//                }
//            } else {
//                isError.postValue(result.CommandMessage.toString());
//            }
//        } else {
//            isError.postValue(SERVER_ERROR);
//        }
//        viewDialogMutData.postValue(false)
//    }

//    suspend fun branchSelectionForLoginData(companyId: String, userName: String){
//        val resultData: Response<CommonResult>? = api.getBranchForLogin(companyId, userName)
//        if(resultData?.body()!=null){
//            val result = resultData.body()!!
//            val gson = Gson()
//            if(result.CommandStatus == 1){
//                val jsonArray = getResult(result);
//                if(jsonArray != null) {
//                    val listType = object: TypeToken<List<BranchModel>>() {}.type
//                    val resultList: ArrayList<BranchModel> = gson.fromJson(jsonArray.toString(), listType);
//                    loginBranchListMutData.postValue(resultList);
//
//                }
//            } else {
//                isError.postValue(result.CommandMessage.toString());
//            }
//        } else {
//            isError.postValue(SERVER_ERROR);
//        }
//        viewDialogMutData.postValue(false)
//    }

    fun branchSelectionForLoginData(companyId: String, userName: String){
        api.getBranchForLogin(companyId, userName).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<BranchModel>>() {}.type
                            val resultList: ArrayList<BranchModel> = gson.fromJson(jsonArray.toString(), listType);
                            loginBranchListMutData.postValue(resultList);

                        }
                    } else {
                        isError.postValue(result.CommandMessage.toString());
                    }
                } else {
                    isError.postValue(SERVER_ERROR);
                }
                viewDialogMutData.postValue(false)
            }

            override fun onFailure(call: Call<CommonResult>, t: Throwable) {
                viewDialogMutData.postValue(false)
                isError.postValue(t.message)
            }

        })
    }

//    suspend fun validateSelectedBranch(companyId: String, userName: String, branchCode: String, appVersion: String,
//            appVersionDt: String, deviceId: String){
//        val resultData: Response<CommonResult>? = api.validateBranchSelection(
//            companyId, userName, branchCode, appVersion, appVersionDt, deviceId
//        )
//        if(resultData?.body()!=null){
//            val result = resultData.body()!!
//            val gson = Gson()
//            if(result.CommandStatus == 1){
////                val jsonArray = getResult(result);
////                if(jsonArray != null) {
//                    val listType = object: TypeToken<List<UserDataModel>>() {}.type
//                    val resultList: ArrayList<UserDataModel> = gson.fromJson(result.DataSet, listType);
//                    validateBranchMutData.postValue(resultList[0]);
//
////                }
//            } else {
//                isError.postValue(result.CommandMessage.toString());
//            }
//        } else {
//            isError.postValue(SERVER_ERROR);
//        }
//        viewDialogMutData.postValue(false)
//    }

    fun validateSelectedBranch(companyId: String, userName: String, branchCode: String, appVersion: String,
                                    appVersionDt: String, deviceId: String){
        api.validateBranchSelection(companyId, userName, branchCode, appVersion, appVersionDt, deviceId).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult>, response: Response<CommonResult>) {
                if(response?.body() != null){
                val result = response.body()!!
                val gson = Gson()
                if(result.CommandStatus == 1){
                        val listType = object: TypeToken<List<UserDataModel>>() {}.type
                        val resultList: ArrayList<UserDataModel> = gson.fromJson(result.DataSet, listType);
                        validateBranchMutData.postValue(resultList[0])
                    } else {
                        isError.postValue(result.CommandMessage.toString());
                    }
                } else {
                    isError.postValue(SERVER_ERROR);
                }
                viewDialogMutData.postValue(false)
            }

            override fun onFailure(call: Call<CommonResult>, t: Throwable) {
                viewDialogMutData.postValue(false)
                isError.postValue(t.message)
            }

        })
    }
}
