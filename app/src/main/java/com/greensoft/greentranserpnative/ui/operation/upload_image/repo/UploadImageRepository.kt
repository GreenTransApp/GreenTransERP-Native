package com.greensoft.greentranserpnative.ui.operation.upload_image.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.operation.unarrived.models.InscanListModel
import com.greensoft.greentranserpnative.ui.operation.upload_image.model.SaveUploadImageModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class UploadImageRepository @Inject constructor(): BaseRepository() {


    private val validateGrForPodImageUploadMutableLiveData = MutableLiveData<Boolean>()
    val validateGrForPodImageUploadLiveData: LiveData<Boolean>
        get() = validateGrForPodImageUploadMutableLiveData

    private val uploadImageMutableLiveData = MutableLiveData<SaveUploadImageModel>()

    val uploadPodImageLiveData: LiveData<SaveUploadImageModel>
        get() = uploadImageMutableLiveData

    val uploadBookingImageLiveData: LiveData<SaveUploadImageModel>
        get() = uploadImageMutableLiveData

    fun validateGrForPodImageUpload(companyId: String, grNo: String){
        viewDialogMutData.postValue(true)
        api.validateGrForPOD(companyId, grNo).enqueue(object:
            Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            validateGrForPodImageUploadMutableLiveData.postValue(true)
                        }
                    } else {
                        isError.postValue(result.CommandMessage.toString());
                    }
                } else {
                    isError.postValue(SERVER_ERROR);
                }
                viewDialogMutData.postValue(false)

            }

            override fun onFailure(call: Call<CommonResult?>, t: Throwable) { viewDialogMutData.postValue(false)
                isError.postValue(t.message)
            }

        })
    }

    fun uploadPodImage(companyId: String, grNo: String, imageBase64: String, signImage: String, userCode: String, menuCode: String, sessionId: String){
        viewDialogMutData.postValue(true)
        api.uploadPodImage(companyId, grNo, imageBase64, signImage, userCode, menuCode, sessionId).enqueue(object:
            Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<SaveUploadImageModel>>() {}.type
                            val saveUploadImageModel: SaveUploadImageModel = gson.fromJson(jsonArray.toString(), listType);
                            uploadImageMutableLiveData.postValue(saveUploadImageModel);
                        }
                    } else {
                        isError.postValue(result.CommandMessage.toString());
                    }
                } else {
                    isError.postValue(SERVER_ERROR);
                }
                viewDialogMutData.postValue(false)

            }

            override fun onFailure(call: Call<CommonResult?>, t: Throwable) { viewDialogMutData.postValue(false)
                isError.postValue(t.message)
            }

        })
    }

    fun uploadBookingImage(companyId: String, transactionId: String, titleName: String, imageBase64: String, userCode: String,
                           sessionId: String, menuCode: String, masterCode: String, branchCode: String){
        viewDialogMutData.postValue(true)
        api.uploadBookingImage(
            companyId = companyId,
            transactionId = transactionId,
            titleName = titleName,
            imageInBase64 = imageBase64,
            userCode = userCode,
            sessionId = sessionId,
            menuCode = menuCode,
            masterCode = masterCode,
            branchCode = branchCode,
        ).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<SaveUploadImageModel>>() {}.type
                            val saveUploadImageModel: SaveUploadImageModel = gson.fromJson(jsonArray.toString(), listType);
                            uploadImageMutableLiveData.postValue(saveUploadImageModel);
                        }
                    } else {
                        isError.postValue(result.CommandMessage.toString());
                    }
                } else {
                    isError.postValue(SERVER_ERROR);
                }
                viewDialogMutData.postValue(false)

            }

            override fun onFailure(call: Call<CommonResult?>, t: Throwable) { viewDialogMutData.postValue(false)
                isError.postValue(t.message)
            }

        })
    }

}