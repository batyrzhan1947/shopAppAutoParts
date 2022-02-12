package com.batyrzhan.autoparts.contracts

import com.batyrzhan.autoparts.model.Product
import com.batyrzhan.common.base.MvpPresenter
import com.batyrzhan.common.base.MvpView

interface ProductsContract {
    interface View :MvpView{
        fun visibleEmptyLayout()
        fun goneEmptyLayout()
        fun hideProgress()
        fun showProgress()
        fun setProductsToAdapter(list: List<Product>)
    }
    interface Presenter : MvpPresenter<View>{
    fun getProducts(categoryId:String)
    fun checkComparatorSort(c:Comparator<Product> )
    }
}