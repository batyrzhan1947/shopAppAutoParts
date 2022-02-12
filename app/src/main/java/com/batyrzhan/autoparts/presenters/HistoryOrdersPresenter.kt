package com.batyrzhan.autoparts.presenters

import android.util.Log
import com.batyrzhan.autoparts.contracts.HistoryOrdersContract
import com.batyrzhan.autoparts.database.AppDataBase
import com.batyrzhan.autoparts.database.model.Order
import com.batyrzhan.common.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HistoryOrdersPresenter(
    private val db: AppDataBase
) : BasePresenter<HistoryOrdersContract.View>(),
    HistoryOrdersContract.Presenter {
    private var ordersList = mutableListOf<Order>()

    override fun getDataHistoryOrders() {
        db.orderDao().getAllOrders()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                ordersList.clear()
                ordersList.addAll(it)
                view?.showList(ordersList)
            }, { th ->
                Log.e("App", "getData: ", th)
            }).disposeOnCleared()

    }
}