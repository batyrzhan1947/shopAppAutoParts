package com.batyrzhan.autoparts.presenters

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.batyrzhan.autoparts.api.AdminPanelApiService
import com.batyrzhan.autoparts.api.Executors
import com.batyrzhan.autoparts.contracts.CheckOutContract
import com.batyrzhan.autoparts.database.AppDataBase
import com.batyrzhan.autoparts.database.model.Cart
import com.batyrzhan.autoparts.database.model.Order
import com.batyrzhan.autoparts.utils.Constant
import com.batyrzhan.common.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class CheckOutPresenter(
    private val apiService: AdminPanelApiService,
    private val db: AppDataBase
) : BasePresenter<CheckOutContract.View>(), CheckOutContract.Presenter {
    private var date: String = ""
    private var dateFormat: SimpleDateFormat
    private var executors: Executors? = null
    private var rand: String
    private val allowedChar = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"

    private var totalPriceStr: String = ""
    private var taxStr: String = ""
    private var orderPriceStr: String = ""
    private var priceTax: String = ""
    private var totalPrice: Double? = null
    private var strTax: Double = 0.0
    private var currencyCode: String = ""
    private var dataOrderList: String? = null

    init {
        executors = Executors()
        dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        date = dateFormat.format(Calendar.getInstance().time)
        rand = getRandomString(9)
    }

    override fun setUserInfo() {
        view?.showProgress()

        executors?.diskIO()?.execute {
            db.userDao().getCount().let {
                if (it > 0) {
                    db.userDao().getUser().apply {
                        executors?.mainThread()?.execute {
                            view?.showUserInfo(
                                this.userName,
                                this.userEmail, this.userPhone, this.userAdress
                            )
                        }
                    }
                }
            }
        }
    }

    override fun getSpinnerData() {
        val list = mutableListOf<String>()
        apiService.getShipping()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                for (item in it) {
                    list.add(item.shipping_name)
                }
                view?.setSpinnerAdapter(list)
            }, {
                Log.e("App", "getSpinnerData: ", it)
                view?.hideProgress()
            }).disposeOnCleared()
    }

    override fun addOrdersAndDeleteAllCarts(strOrderList: String?, strOrderTotal: String?) {
        executors?.diskIO()?.execute {
            strOrderList?.let { orderList ->
                strOrderTotal?.let { orderTotal ->
                    db.orderDao().addOrder(Order(rand, orderList, orderTotal, date))
                }
            }
            db.cartDao().deleteAllCarts()
        }
    }

    override fun setParamsPOST(
        strName: String, strEmail: String,
        strPhone: String,checkedRb: String, strAddress: String, strShipping: String,
        strOrderList: String, strOrderTotal: String, strComment: String,
        userId: String, baseUrl: String
    ): StringRequest {

        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, Constant.POST_ORDER,
            Response.Listener {
                Handler(Looper.getMainLooper()).postDelayed({
                    view?.hideDialog()
                    view?.dialogSuccess()
                }, 2000)
            },
            Response.ErrorListener { volleyError ->
                view?.hideDialog()
                view?.showError(volleyError.toString())

            }) {

            override fun getParams(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["code"] = rand
                params["name"] = strName
                params["email"] = strEmail
                params["phone"] = strPhone
                params["payment_type"]= checkedRb
                params["address"] = strAddress
                params["shipping"] = strShipping
                params["order_list"] = strOrderList
                params["order_total"] = strOrderTotal
                params["comment"] = strComment
                params["player_id"] = userId
                params["date"] = date
                params["server_url"] = baseUrl
                return params
            }
        }
        return stringRequest
    }

    private fun getRandomString(randomString: Int): String {
        val random = Random()
        val stringBuilder = StringBuilder(randomString)
        for (i in 0 until randomString) stringBuilder.append(
            allowedChar[random.nextInt(allowedChar.length)]
        )
        return stringBuilder.toString()
    }

    override fun getDataFromDB() {
        dataOrderList = ""
        var orderPrice = 0.0
        totalPrice = 0.0
        var tax: Double
        // get Tax and Currency
        apiService.getTaxCurrency()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { item ->
                    strTax = item.tax
                    currencyCode = item.currency_code
                    val cartList = mutableListOf<Cart>()
                    // get All Cart from Database
                    db.cartDao().getAllCarts()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ list ->
                            cartList.clear()
                            cartList.addAll(list)
                            for (cart in cartList) {
                                val menuName = cart.productName
                                val quantity = cart.productQuantity.toString()
                                val subTotalPrice = cart.totalProductsPrice
                                val subTotalPriceStr =
                                    String.format(Locale.GERMAN, "%1$,.0f", subTotalPrice)

                                orderPrice += subTotalPrice

                                dataOrderList +=
                                    if (cart == cartList.last())
                                    "$quantity шт. $menuName $subTotalPriceStr $currencyCode.\n"
                                else "$quantity шт. $menuName $subTotalPriceStr $currencyCode,\n"

                            }

                            dataOrderList?.let { view?.setOrderList(it) }

                            // Net zakaza

                            tax = orderPrice * (strTax / 100)


                            totalPrice = orderPrice + tax

                            priceTax = String.format(Locale.GERMAN, "%1$,.0f", strTax)
                            orderPriceStr =
                                String.format(Locale.GERMAN, "%1$,.0f", orderPrice)
                            taxStr =
                                String.format(Locale.GERMAN, "%1$,.0f", tax)
                            totalPriceStr =
                                String.format(Locale.GERMAN, "%1$,.0f", totalPrice)

                            view?.setDataToEditTexts(
                                dataOrderList!!,
                                orderPriceStr,
                                currencyCode,
                                priceTax,
                                taxStr,
                                totalPriceStr,
                                strTax.toString()
                            )

                        }, { th ->
                            Log.e("App", "getDataFromDB: ", th)
                            view?.hideProgress()
                        }).disposeOnCleared()
                    view?.hideProgress()
                }, {
                    Log.e("App", "getTaxCurrency: ", it)
                    view?.hideProgress()
                }
            ).disposeOnCleared()
    }
}