package com.batyrzhan.autoparts.contracts

import com.batyrzhan.autoparts.database.model.Order
import com.batyrzhan.common.base.MvpPresenter
import com.batyrzhan.common.base.MvpView

interface HistoryOrdersContract {
    interface View : MvpView {
        fun showList(list: List<Order>)
    }

    interface Presenter : MvpPresenter<View> {
        fun getDataHistoryOrders()
    }
}