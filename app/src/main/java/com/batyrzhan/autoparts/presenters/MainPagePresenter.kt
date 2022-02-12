package com.batyrzhan.autoparts.presenters

import android.util.Log
import com.batyrzhan.autoparts.api.AdminPanelApiService
import com.batyrzhan.autoparts.contracts.MainPageContract
import com.batyrzhan.autoparts.model.ShopNotification
import com.batyrzhan.common.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainPagePresenter(
    private val apiService: AdminPanelApiService
) : BasePresenter<MainPageContract.View>(),
    MainPageContract.Presenter {
    private val compositeDisposable = CompositeDisposable()
    private var notifyList = mutableListOf<ShopNotification>()

    override fun disposeCompositeDisposable() {
        compositeDisposable.disposeOnCleared()
    }


    override fun getDataNotifications() {
        apiService.getNotifications()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { list ->
                    notifyList.clear()
                    notifyList.addAll(list)
                    view?.showAllNotificationsFromDB(notifyList)
                }, { th ->
                    Log.e("App", "onViewCreated: ", th)
                }
            ).also { compositeDisposable }
    }


}
