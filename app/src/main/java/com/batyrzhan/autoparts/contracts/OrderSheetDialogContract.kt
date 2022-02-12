package com.batyrzhan.autoparts.contracts

import com.batyrzhan.common.base.MvpPresenter
import com.batyrzhan.common.base.MvpView

interface OrderSheetDialogContract {
    interface View : MvpView {
        fun showOrderInfo(codeOrder: String, orderTotal:String,dateTime:String, orderList:String, statusOrder:String)
    }

    interface Presenter : MvpPresenter<View> {
        fun setOrder(code:String)
    }
}