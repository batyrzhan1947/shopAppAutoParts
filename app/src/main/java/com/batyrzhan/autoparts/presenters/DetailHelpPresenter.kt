package com.batyrzhan.autoparts.presenters

import android.util.Log
import com.batyrzhan.autoparts.api.AdminPanelApiService
import com.batyrzhan.autoparts.contracts.DetailHelpContract
import com.batyrzhan.common.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DetailHelpPresenter(
    private val apiService: AdminPanelApiService
) : BasePresenter<DetailHelpContract.View>(),
    DetailHelpContract.Presenter {

    override fun getDetailHelp(helpID: Int) {
        view?.showProgress()
        if (helpID.toString().isNotEmpty()) {
            apiService.getHelpContent(helpID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ item ->
                    view?.showText(item)
                    view?.hideProgress()
                }, { th ->
                    Log.e("App", "getDetailHelp: ", th)
                    view?.hideProgress()
                }).disposeOnCleared()
        }
    }
}
