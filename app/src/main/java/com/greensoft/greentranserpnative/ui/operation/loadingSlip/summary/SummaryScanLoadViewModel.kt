package com.greensoft.greentranserpnative.ui.operation.loadingSlip.summary

import com.greensoft.greentranserpnative.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SummaryScanLoadViewModel @Inject constructor(private val _repository: SummaryScanLoadRepository): BaseViewModel() {
    var summaryListLiveData = _repository.summaryListLiveData
    var saveLoadingCompleteLiveData = _repository.saveLoadingCompleteLiveData
    init {
        isError = _repository.isError
    }

    var viewDialogLiveData = _repository.viewDialogLiveData
    fun getSummaryForLoading(
        companyId: String?, branchCode: String?, loadingNo: String?, closeLoading: String?,
        userCode: String?, menuCode: String?, sessionId: String?
    ) {
        _repository.getSummaryForLoading(
            companyId,
            branchCode,
            loadingNo,
            closeLoading,
            userCode,
            menuCode,
            sessionId
        )
    }

    fun saveLoadingComplete(
        companyId: String?, branchCode: String?, loadingNo: String?, closeLoading: String?,
        userCode: String?, menuCode: String?, sessionId: String?
    ) {
        _repository.saveLoadingComplete(
            companyId,
            branchCode,
            loadingNo,
            closeLoading,
            userCode,
            menuCode,
            sessionId
        )
    }
}