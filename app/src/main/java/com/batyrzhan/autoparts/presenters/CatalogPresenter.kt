package com.batyrzhan.autoparts.presenters

import android.util.Log
import com.batyrzhan.autoparts.api.AdminPanelApiService
import com.batyrzhan.autoparts.contracts.CatalogContract
import com.batyrzhan.autoparts.contracts.InfoContract
import com.batyrzhan.autoparts.model.PartCategory
import com.batyrzhan.common.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CatalogPresenter(
    private val apiService: AdminPanelApiService
) : BasePresenter<CatalogContract.View>(),
    CatalogContract.Presenter {

    private var categoryList = mutableListOf<PartCategory>()

    override fun getDataCatalogs() {
        apiService.getCategories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { list ->
                    categoryList.clear()
                    categoryList.addAll(list)
                    view?.showAllDataFromDB(categoryList)
                }, { th ->
                    Log.e("App", "onViewCreated: ", th)
                }
            ).disposeOnCleared()
    }

}