package com.batyrzhan.autoparts.contracts

import com.batyrzhan.autoparts.model.Product
import com.batyrzhan.autoparts.model.ShopNotification
import com.batyrzhan.common.base.MvpPresenter
import com.batyrzhan.common.base.MvpView

interface MainPageContract {

    interface View : MvpView {
        fun showAllNotificationsFromDB(list: List<ShopNotification>)
        fun showSnackBar(txt:String)
    }

    interface Presenter : MvpPresenter<View> {
        fun getDataNotifications()
        fun disposeCompositeDisposable()
    }

}