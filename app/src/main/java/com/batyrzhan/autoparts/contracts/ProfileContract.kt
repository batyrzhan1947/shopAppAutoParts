package com.batyrzhan.autoparts.contracts

import com.batyrzhan.common.base.MvpPresenter
import com.batyrzhan.common.base.MvpView

interface ProfileContract {

    interface View : MvpView{
        fun showUserInfo(name: String, email: String, phone: String, address: String)

    }
    interface Presenter : MvpPresenter<View>{
        fun setUserIfExist()
        fun shutdownExecutors()
    }

}