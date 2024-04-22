package com.greensoft.greentranserpnative.ui.operation.upload_image.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greensoft.greentranserpnative.base.BaseViewModel
import com.greensoft.greentranserpnative.ui.operation.unarrived.InscanListRepository
import com.greensoft.greentranserpnative.ui.operation.unarrived.models.InscanListModel
import com.greensoft.greentranserpnative.ui.operation.upload_image.model.SaveUploadImageModel
import com.greensoft.greentranserpnative.ui.operation.upload_image.repo.UploadImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UploadImageViewModel @Inject constructor(private val _repository: UploadImageRepository) : BaseViewModel() {

    init {
        isError = _repository.isError
    }

    val viewDialogLiveData: LiveData<Boolean>
        get() = _repository.viewDialogMutData

    val validateGrForPodLiveData: LiveData<Boolean>
        get() = _repository.validateGrForPodImageUploadLiveData

    val uploadPodImageLiveData: LiveData<SaveUploadImageModel>
        get() = _repository.uploadPodImageLiveData

    val uploadBookingImageLiveData: LiveData<SaveUploadImageModel>
        get() = _repository.uploadBookingImageLiveData

    fun validateGrForPodUpload(companyId: String, grNo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.validateGrForPodImageUpload(companyId, grNo)
        }
    }

    fun uploadPodImage(companyId: String, grNo: String, imageBase64: String, signImage: String, userCode: String, menuCode: String, sessionId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.uploadPodImage(companyId, grNo, imageBase64, signImage, userCode, menuCode, sessionId)
        }
    }

    fun uploadBookingImage(companyId: String, transactionId: String, titleName: String, imageBase64: String, userCode: String,
                       sessionId: String, menuCode: String, masterCode: String, branchCode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.uploadBookingImage(companyId, transactionId, titleName, imageBase64, userCode,
                        sessionId, menuCode, masterCode, branchCode)
        }
    }
}
