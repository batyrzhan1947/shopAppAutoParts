package com.batyrzhan.autoparts.contracts

import com.batyrzhan.autoparts.model.MarksProduct
import com.batyrzhan.autoparts.model.Product
import com.batyrzhan.common.base.MvpPresenter
import com.batyrzhan.common.base.MvpView

interface DetailProductContract {

    interface View:MvpView{
        fun showProgress()
        fun hideProgress()
        fun showText(item: Product)
        fun showSnackBar(msg: Int)
        fun showMarksAuto(item: MarksProduct)
    }
    interface Presenter:MvpPresenter<View>{
        fun getDetailProduct(productID: Int)
        fun inputData(countProduct:String,productQuantity:Int,productID:Int, productPrice:Double, productNameDetail:String,imgProduct:String,currencyCode:String)
        fun clearDispose()
    }
}
