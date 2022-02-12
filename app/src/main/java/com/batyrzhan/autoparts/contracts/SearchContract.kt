package com.batyrzhan.autoparts.contracts

import com.batyrzhan.autoparts.model.Product
import com.batyrzhan.autoparts.model.ShopNotification
import com.batyrzhan.common.base.MvpPresenter
import com.batyrzhan.common.base.MvpView

interface SearchContract {

    interface View : MvpView {
        fun setFilteredToAdapter(list: List<Product>)
        fun hideProgress()
        fun showProgress()
        fun setProductsToAdapter(list: List<Product>)
        //  fun setCountCart()
    }

    interface Presenter : MvpPresenter<View> {
        fun getProductsRecently()
        fun filter(text:String)
    }

}