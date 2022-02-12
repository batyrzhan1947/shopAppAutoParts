package com.batyrzhan.autoparts.contracts

import android.text.Editable
import com.batyrzhan.common.base.MvpPresenter
import com.batyrzhan.common.base.MvpView

interface EditUserContract {

    interface View : MvpView {
        fun showUserInfo(name: Editable, email: Editable, phone: Editable, address: Editable)
        fun setSnackBar(msg:String)
        fun transitionToFragment()
    }

    interface Presenter : MvpPresenter<View> {
        fun getUserIfExist()
        fun shutdownExecutors()
        fun setUser(name: String, email: String, phone: String, address: String)

    }
}