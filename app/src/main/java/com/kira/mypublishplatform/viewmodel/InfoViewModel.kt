package com.kira.mypublishplatform.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kira.mypublishplatform.model.OldInfoModel

class InfoViewModel : ViewModel() {
    private val infoModel: MutableLiveData<OldInfoModel> by lazy {
        MutableLiveData<OldInfoModel>()
    }

    fun select(item: OldInfoModel) {
        infoModel.value = item
    }
}