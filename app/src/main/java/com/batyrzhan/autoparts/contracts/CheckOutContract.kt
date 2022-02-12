package com.batyrzhan.autoparts.contracts

import com.android.volley.toolbox.StringRequest
import com.batyrzhan.common.base.MvpPresenter
import com.batyrzhan.common.base.MvpView

interface CheckOutContract {

    interface View : MvpView {
        fun showUserInfo(name: String, email: String, phone: String, address: String)
        fun showProgress()
        fun hideProgress()
        fun setSpinnerAdapter(list: MutableList<String>)
        fun setOrderList(orderList: String)
        fun showSnackBar(msg: Int)


        fun setDataToEditTexts(
            orderList: String,
            orderPriceStr: String,
            currencyCode: String,
            priceTax: String,
            taxStr: String,
            totalPriceStr: String,
            strTax: String
        )

        fun hideDialog()
        fun showError(str: String)
        fun dialogSuccess()
    }

    interface Presenter : MvpPresenter<View> {
        fun setUserInfo()
        fun getSpinnerData()
        fun getDataFromDB()
        fun addOrdersAndDeleteAllCarts(strOrderList: String?, strOrderTotal: String?)
        fun setParamsPOST(
            strName: String, strEmail: String,
            strPhone: String, checkedRb: String, strAddress: String, strShipping: String,
            strOrderList: String, strOrderTotal: String, strComment: String,
            userId: String, baseUrl: String
        ): StringRequest
    }
}