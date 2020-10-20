package com.kira.mypublishplatform.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.kira.mypublishplatform.BR
import java.io.Serializable

class OldInfoModel : BaseObservable(), Serializable {
    /**
     * id : 1
     * oldName : 谢东敖
     * oldSex : 男
     * idCard : a9e8ae34e049b15409da2d73b18d9de08cf0c2c902af4d13094d78c44a439272
     * testIdCard : 330281********3314
     * oldPhone : 021-49878266
     * oldHandset : 17621571991
     * oldFwdzId : 33
     * oldHjAddress : 浙江余姚市气象台
     * oldJzAddress : 子陵新村
     * urgentPhone : null
     * urgentName : null
     * urgentRelation : null
     * oldPinPath : http://192.168.1.229/newOld/old/1567154995.png
     * oldAge : 88
     * marriage : 未婚
     * nation : 汉族
     * state : null
     * oldType : 退休工人
     * birthday : 1959-08-27
     * livingConditions : 与亲戚合住
     * ssStreet : null
     * ssCommunity : 凤山街道/子陵社区
     * ssMechanism : 余姚阳光养老机构
     * createTime : null
     * updateTime : 2019-09-16T05:49:15.000+0000
     * cdNames : null
     * bloodType : null
     * disabilityl : null
     * cjqk : null
     * monthIncome : null
     * economicSources : null
     * medicalSecurity : null
     * socialNo : null
     * familyId : null
     * city : null
     * street : null
     * community : null
     * road : null
     * num : null
     * room : null
     */

    var oldHandset: String? = null
    var oldFwdzId = 0
    var oldHjAddress: String? = null
    var oldJzAddress: String? = null
    var urgentPhone: String? = null
    var urgentName: String? = null
    var urgentRelation: String? = null
    var oldPinPath: String? = null
    var oldAge: String? = null
    var marriage: String? = null
    var nation: String? = null

    var oldType: String? = null
    var birthday: String? = null
    var livingConditions: String? = null
    var ssStreet: String? = null
    var ssCommunity: String? = null
    var ssMechanism: String? = null
    var state: String? = null
    var disabilityl: String? = null
    var monthIncome: String? = null
    var medicalSecurity: String? = null

    var city: String? = null
    var street: String? = null
    var community: String? = null
    var road: String? = null
    var num: String? = null
    var room: String? = null
    var bloodType: String? = null
    var cjqk: String? = null
    var economicSources: String? = null

    @get:Bindable
    var id = 0
        set(id) {
            field = id
            notifyPropertyChanged(BR.id)
        }
    @get:Bindable
    var oldName: String? = null
        set(oldName) {
            field = oldName
            notifyPropertyChanged(BR.oldName)
        }
    @get:Bindable
    var oldSex: String? = null
        set(oldSex) {
            field = oldSex
            notifyPropertyChanged(BR.oldSex)
        }
    @get:Bindable
    var loginAccount: String? = null
        set(loginAccount) {
            field = loginAccount
            notifyPropertyChanged(BR.loginAccount)
        }

    @get:Bindable
    var oldPhone: String? = null
        set(oldPhone) {
            field = oldPhone
            notifyPropertyChanged(BR.oldPhone)
        }
    @get:Bindable
    var cdNames: String? = null
        set(cdNames) {
            field = cdNames
            notifyPropertyChanged(BR.cdNames)
        }
    @get:Bindable
    var accountBalance: String? = null
        set(accountBalance) {
            field = accountBalance
            notifyPropertyChanged(BR.accountBalance)
        }

    @get:Bindable
    var socialNo: String? = null
        set(socialNo) {
            field = socialNo
            notifyPropertyChanged(BR.socialNo)
        }
}