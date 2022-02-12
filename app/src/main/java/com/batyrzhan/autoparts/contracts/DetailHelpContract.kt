package com.batyrzhan.autoparts.contracts

import com.batyrzhan.autoparts.model.Help
import com.batyrzhan.common.base.MvpPresenter
import com.batyrzhan.common.base.MvpView

interface DetailHelpContract {
    interface View : MvpView {
        fun showProgress()
        fun hideProgress()
        fun showText(item: List<Help>)
    }

    interface Presenter : MvpPresenter<View> {
        fun getDetailHelp(helpID: Int)
    }
}