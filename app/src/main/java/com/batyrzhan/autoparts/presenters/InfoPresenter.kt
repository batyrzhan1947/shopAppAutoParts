package com.batyrzhan.autoparts.presenters

import android.util.Log
import com.batyrzhan.autoparts.api.AdminPanelApiService
import com.batyrzhan.autoparts.contracts.InfoContract
import com.batyrzhan.autoparts.model.Help
import com.batyrzhan.common.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class InfoPresenter(
    private val apiService: AdminPanelApiService
) : BasePresenter<InfoContract.View>(),
    InfoContract.Presenter {

    private var helpList = mutableListOf<Help>()

    override fun getDataHelps() {
        apiService.getHelps()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { list ->
                    helpList.clear()
                    helpList.addAll(list)
                    view?.showAllDataFromDB(helpList)
                }, { th ->
                    Log.e("App", "onViewCreated: ", th)
                }
            ).disposeOnCleared()
    }
}