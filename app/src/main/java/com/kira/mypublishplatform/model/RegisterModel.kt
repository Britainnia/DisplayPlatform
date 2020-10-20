package com.kira.mypublishplatform.model

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import com.kira.mypublishplatform.utils.other.StringUtils
import com.kira.mypublishplatform.utils.other.Validators


class RegisterModel(val onButtonClickListener:(isBtnEnable: Boolean) -> Unit) {

    var userPhone= ObservableField<String>()
    var userPwd = ObservableField<String>()

    var isPhoneEmpty = true
    var isPwdEmpty = true

    fun btnIsEnable(model: RegisterModel) {

        isPropertyChanged(model.userPhone)
        isPropertyChanged(model.userPwd)

    }

    fun isPropertyChanged(property: ObservableField<String>) {
        property.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                isPropertyEmpty()
            }

        })

    }

    fun isPropertyEmpty() {
        isPhoneEmpty = if (StringUtils.isEmpty(userPhone.get())) StringUtils.isEmpty(userPhone.get())
        else Validators.isPhoneNumber(userPhone.get())

        isPwdEmpty = StringUtils.isEmpty(userPwd.get())

        if (!isPhoneEmpty && !isPwdEmpty) onButtonClickListener(true)
        else  onButtonClickListener(false)
    }

}