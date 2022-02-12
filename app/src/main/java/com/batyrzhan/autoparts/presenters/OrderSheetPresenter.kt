package com.batyrzhan.autoparts.presenters

import android.util.Log
import com.batyrzhan.autoparts.contracts.OrderSheetDialogContract
import com.batyrzhan.autoparts.database.AppDataBase
import com.batyrzhan.common.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class OrderSheetPresenter(
    private val db: AppDataBase
) : BasePresenter<OrderSheetDialogContract.View>(),
    OrderSheetDialogContract.Presenter {

    override fun setOrder(code: String) {
        db.orderDao().getOrderWithCode(code)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view?.showOrderInfo(
                    it.codeOrder,
                    it.orderTotal,
                    it.dateTime,
                    it.orderList,
                    it.statusOrder
                )
            }, { th ->
                Log.e("App", "onViewCreated: ", th)
            }).disposeOnCleared()
    }
}