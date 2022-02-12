package com.batyrzhan.autoparts.contracts

import com.batyrzhan.autoparts.model.Help
import com.batyrzhan.autoparts.model.PartCategory
import com.batyrzhan.common.base.MvpPresenter
import com.batyrzhan.common.base.MvpView

interface CatalogContract {
    interface View : MvpView {
        fun showAllDataFromDB(list: List<PartCategory>)
    }

    interface Presenter : MvpPresenter<View> {
        fun getDataCatalogs()
    }
}