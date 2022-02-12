package com.batyrzhan.autoparts.contracts

import com.batyrzhan.autoparts.model.Help
import com.batyrzhan.common.base.MvpPresenter
import com.batyrzhan.common.base.MvpView

interface InfoContract {

    interface View : MvpView {
        fun showAllDataFromDB(list: List<Help>)
    }

    interface Presenter : MvpPresenter<View> {
        fun getDataHelps()
    }

}